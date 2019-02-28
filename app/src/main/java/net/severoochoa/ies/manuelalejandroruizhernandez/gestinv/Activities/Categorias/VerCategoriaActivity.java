package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Categorias;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.CategoriasActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.AtributoAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.CategoriaAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Atributo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Dialog.DialogIntroducirTexto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Utiles.Util;

import java.util.Calendar;
import java.util.List;

public class VerCategoriaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Atributo>>, CategoriaAdapter.OnItemLongClickListener {

    private static final String SAVE_EDITANDO = "EDITANDO";
    private EditText etNombre, etFechaAlta, etDescripcion, etUnidadMedida;
    private ImageView ivImagen;
    private RecyclerView rvAtributos;
    private GridLayoutManager gridLayoutManager;
    private boolean editando = false;

    private MenuItem mnModificar, mnGuardar;
    private String rutaImagen;
    private DatabaseManager db;
    private Categoria categoria;
    private AtributoAdapter atributoAdapter;
    private int ID_LOADER_ATRIBUTOS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_categoria);
        etNombre = findViewById(R.id.tvNombre);
        etFechaAlta = findViewById(R.id.tvFechaAlta);
        etDescripcion = findViewById(R.id.etDescripcion);
        ivImagen = findViewById(R.id.ivImagen);
        rvAtributos = findViewById(R.id.rvAtributos);
        etUnidadMedida = findViewById(R.id.etUnidadMedida);
        db = DatabaseManager.getInstance(this);

        etNombre.setEnabled(false);
        etFechaAlta.setEnabled(false);
        etDescripcion.setEnabled(false);
        etUnidadMedida.setEnabled(false);

        etNombre.setText(getIntent().getStringExtra(CategoriasActivity.VALUE_NOMBRE));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getIntent().getLongExtra(CategoriasActivity.VALUE_FECHA_ALTA, 0));
        etFechaAlta.setText(Util.calendarToNumericString(calendar));
        etDescripcion.setText(getIntent().getStringExtra(CategoriasActivity.VALUE_DESCRIPCION));
        etUnidadMedida.setText(getIntent().getStringExtra(CategoriasActivity.VALUE_UNIDADMEDIDA));
        rutaImagen = getIntent().getStringExtra(CategoriasActivity.VALUE_IMAGEN);
        categoria = new Categoria(
                getIntent().getLongExtra(CategoriasActivity.VALUE_ID, 0),
                getIntent().getStringExtra(CategoriasActivity.VALUE_NOMBRE),
                getIntent().getLongExtra(CategoriasActivity.VALUE_FECHA_ALTA, 0),
                getIntent().getStringExtra(CategoriasActivity.VALUE_IMAGEN),
                getIntent().getStringExtra(CategoriasActivity.VALUE_DESCRIPCION),
                getIntent().getStringExtra(CategoriasActivity.VALUE_UNIDADMEDIDA)
        );

        rvAtributos = findViewById(R.id.rvAtributos);
        rvAtributos.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, 3);
        rvAtributos.setLayoutManager(gridLayoutManager);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            Picasso.get()
                    .load("file://" + rutaImagen)
                    .error(R.drawable.ic_launcher_foreground).placeholder(R.drawable.ic_no_image)
                    .fit()
                    .centerCrop()
                    .into(ivImagen);
        }

        if (savedInstanceState != null) {
            editando = savedInstanceState.getBoolean(SAVE_EDITANDO);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(ID_LOADER_ATRIBUTOS) != null) {
            if (!getSupportLoaderManager().hasRunningLoaders())
                getSupportLoaderManager().restartLoader(ID_LOADER_ATRIBUTOS, null, this);
        } else getSupportLoaderManager().initLoader(ID_LOADER_ATRIBUTOS, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_ver_categoria, menu);
        mnModificar = menu.findItem(R.id.mnModificar);
        mnGuardar = menu.findItem(R.id.mnGuardar);
        if (editando) {
            etNombre.setEnabled(true);
            etDescripcion.setEnabled(true);
            etUnidadMedida.setEnabled(true);
            mnModificar.setVisible(false);
            mnGuardar.setVisible(true);
        } else
            mnGuardar.setVisible(false);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_EDITANDO, editando);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnAnadirAtr:
                DialogIntroducirTexto dialogIntroducirTexto = DialogIntroducirTexto.newInstance("Añadir Atributo", "Introduzca el valor del atributo");
                dialogIntroducirTexto.setOnDialogInsertarTextoListener(new DialogIntroducirTexto.DialogInsertarTextoInterface() {
                    @Override
                    public void onPossitiveDialogInsertarTexto(String valor) {
                        db.openDB();
                        Atributo atributo = new Atributo(0, valor);
                        long idAtr = db.insertAtributo(atributo);
                        atributo.setId(idAtr);
                        if (db.insertCatAtr(categoria, atributo) != -1) {
                            Toast.makeText(VerCategoriaActivity.this, "Insercion realizada", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(VerCategoriaActivity.this, "La Insercion no ha sido realizada", Toast.LENGTH_SHORT).show();

                        db.closeDB();
                        reiniciarLoader();
                    }

                    @Override
                    public void onNegativeDialogInsertarTexto() {
                    }
                });
                dialogIntroducirTexto.show(getSupportFragmentManager(), "id");
                return true;
            case R.id.mnModificar:
                etNombre.setEnabled(true);
                etDescripcion.setEnabled(true);
                etUnidadMedida.setEnabled(true);
                mnModificar.setVisible(false);
                mnGuardar.setVisible(true);
                editando = true;
                return true;
            case R.id.mnGuardar:
                if (!etNombre.getText().toString().isEmpty() && !etUnidadMedida.getText().toString().isEmpty()) {
                    Intent result = new Intent();
                    result.putExtra(CategoriasActivity.VALUE_ID, getIntent().getLongExtra(CategoriasActivity.VALUE_ID, 0));
                    result.putExtra(CategoriasActivity.VALUE_NOMBRE, etNombre.getText().toString());
                    result.putExtra(CategoriasActivity.VALUE_FECHA_ALTA, Util.numericStringToLong(etFechaAlta.getText().toString()));
                    result.putExtra(CategoriasActivity.VALUE_IMAGEN, getIntent().getStringExtra(CategoriasActivity.VALUE_IMAGEN));
                    result.putExtra(CategoriasActivity.VALUE_DESCRIPCION, etDescripcion.getText().toString());
                    result.putExtra(CategoriasActivity.VALUE_UNIDADMEDIDA, etUnidadMedida.getText().toString());
                    setResult(CategoriasActivity.RESULT_MODIFICAR_CATEGORIA, result);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reiniciarLoader() {
        getSupportLoaderManager().restartLoader(ID_LOADER_ATRIBUTOS, null, this);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea eliminar este atributo?")
                .setTitle("Eliminar Atributo")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Atributo atributo = atributoAdapter.getAtributo(position);
                        db.openDB();
                        if (db.deleteAtrFromCat(categoria.getId(), atributo.getId()) > 0) {
                            Toast.makeText(VerCategoriaActivity.this, "Atributo eliminado de la categoria", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(VerCategoriaActivity.this, "El Atributo no ha sido eliminado de la categoria", Toast.LENGTH_SHORT).show();

                        db.closeDB();
                        reiniciarLoader();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @NonNull
    @Override
    public Loader<List<Atributo>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AtributoLoader(this, categoria);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Atributo>> loader, List<Atributo> data) {
        atributoAdapter = new AtributoAdapter(data);
        rvAtributos.setAdapter(atributoAdapter);
        atributoAdapter.SetOnItemLongClickListener(this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Atributo>> loader) {
        rvAtributos.setAdapter(null);
    }

    static class AtributoLoader extends AsyncTaskLoader<List<Atributo>> {
        private List<Atributo> atributoList;
        private DatabaseManager db;
        private long categoriaId;

        public AtributoLoader(Context context, Categoria categoria) {
            super(context);
            categoriaId = categoria.getId();
            db = DatabaseManager.getInstance(context);
        }

        @Override
        protected void onStartLoading() {
            if (atributoList != null) {
                deliverResult(atributoList);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Atributo> loadInBackground() {
            db.openDB();
            List<Atributo> atributoList1 = db.getAtrFromCat(categoriaId);
            db.closeDB();
            return atributoList1;
        }

        @Override
        public void deliverResult(List<Atributo> data) {
            atributoList = data;
            super.deliverResult(data);
        }

    }
}
