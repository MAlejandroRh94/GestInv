package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import java.util.List;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Categorias.CrearCategoriaActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Categorias.VerCategoriaActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.CategoriaAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;


public class CategoriasActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Categoria>>, View.OnClickListener, CategoriaAdapter.OnItemClickListener, CategoriaAdapter.OnItemLongClickListener {
    public static final int REQUEST_CODE_ANADIR_CATEGORIA = 1;
    public static final int REQUEST_CODE_VISUALIZAR_CATEGORIA = 2;

    public static final int RESULT_MODIFICAR_CATEGORIA = 4;
    private static final int ID_LOADER_CATEGORIAS = 1;

    public static String VALUE_ID = "Id";
    public static String VALUE_NOMBRE = "Nombre";
    public static String VALUE_FECHA_ALTA = "Fecha_Alta";
    public static String VALUE_IMAGEN = "Imagen";
    public static String VALUE_DESCRIPCION = "Descripcion";
    public static String VALUE_UNIDADMEDIDA = "UnidadMedida";

    private RecyclerView rvCategorias;
    private FloatingActionButton fabCrearCategoria;
    private CategoriaAdapter categoriaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        db = DatabaseManager.getInstance(this);

        fabCrearCategoria = findViewById(R.id.fabCrearCategoria);
        fabCrearCategoria.setOnClickListener(this);

        rvCategorias = findViewById(R.id.rvCategorias);
        rvCategorias.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCategorias.setLayoutManager(linearLayoutManager);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCrearCategoria:
                Intent intent = new Intent(this, CrearCategoriaActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ANADIR_CATEGORIA);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ANADIR_CATEGORIA:
                if (resultCode == RESULT_OK) {
                    String nombre = data.getStringExtra(VALUE_NOMBRE);
                    long fechaAlta = data.getLongExtra(VALUE_FECHA_ALTA, 0);
                    String imagen = data.getStringExtra(VALUE_IMAGEN);
                    String descripcion = data.getStringExtra(VALUE_DESCRIPCION);
                    String unidadMedida = data.getStringExtra(VALUE_UNIDADMEDIDA);
                    Categoria categoria = new Categoria(0, nombre, fechaAlta, imagen, descripcion, unidadMedida);
                    db.openDB();
                    if (db.insertCategoria(categoria) != -1) {
                        Toast.makeText(this, "Inserccion Realizada", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "La Inserccion no ha sido Realizada", Toast.LENGTH_SHORT).show();

                    db.closeDB();
                }
                break;
            case REQUEST_CODE_VISUALIZAR_CATEGORIA:
                if (resultCode == RESULT_MODIFICAR_CATEGORIA) {
                    long id = data.getLongExtra(VALUE_ID, 0);
                    String nombre = data.getStringExtra(VALUE_NOMBRE);
                    long fechaAlta = data.getLongExtra(VALUE_FECHA_ALTA, 0);
                    String imagen = data.getStringExtra(VALUE_IMAGEN);
                    String descripcion = data.getStringExtra(VALUE_DESCRIPCION);
                    String unidadMedida = data.getStringExtra(VALUE_UNIDADMEDIDA);
                    Categoria categoria = new Categoria(id, nombre, fechaAlta, imagen, descripcion,unidadMedida);
                    db.openDB();
                    if (db.updateCategoria(categoria) > 0) {
                        Toast.makeText(this, "Modificacion Realizada", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "La Modificacion no ha sido Realizada", Toast.LENGTH_SHORT).show();

                    db.closeDB();
                }
                break;
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        Categoria categoria = categoriaAdapter.getCategoria(position);
        Intent intent = new Intent(this, VerCategoriaActivity.class);
        intent.putExtra(VALUE_ID, categoria.getId());
        intent.putExtra(VALUE_NOMBRE, categoria.getNombre());
        intent.putExtra(VALUE_IMAGEN, categoria.getImagen());
        intent.putExtra(VALUE_FECHA_ALTA, categoria.getFechaAlta());
        intent.putExtra(VALUE_DESCRIPCION, categoria.getDescripcion());
        intent.putExtra(VALUE_UNIDADMEDIDA, categoria.getUnidadMedida());
        startActivityForResult(intent, REQUEST_CODE_VISUALIZAR_CATEGORIA);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea eliminar este elemento?")
                .setTitle("Eliminar Elemento")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Categoria categoria = categoriaAdapter.getCategoria(position);
                        db.openDB();
                        if (db.deleteCategoria(categoria.getId()) > 0) {
                            Toast.makeText(CategoriasActivity.this, "La categoria ha sido eliminada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CategoriasActivity.this, "La categoria no ha podido ser eliminada", Toast.LENGTH_SHORT).show();
                        }
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

    private void reiniciarLoader() {
        getSupportLoaderManager().restartLoader(ID_LOADER_CATEGORIAS, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(ID_LOADER_CATEGORIAS) != null) {
            if (!getSupportLoaderManager().hasRunningLoaders())
                getSupportLoaderManager().restartLoader(ID_LOADER_CATEGORIAS, null, this);
        } else getSupportLoaderManager().initLoader(ID_LOADER_CATEGORIAS, null, this);
    }

    @Override
    public Loader<List<Categoria>> onCreateLoader(int id, Bundle args) {
        return new CategoriaLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Categoria>> loader, List<Categoria> data) {
        categoriaAdapter = new CategoriaAdapter(this, data);
        rvCategorias.setAdapter(categoriaAdapter);
        categoriaAdapter.SetOnItemClickListener(this);
        categoriaAdapter.SetOnItemLongClickListener(this);
    }

    @Override
    public void onLoaderReset(Loader<List<Categoria>> loader) {
    }

    static class CategoriaLoader extends AsyncTaskLoader<List<Categoria>> {
        private List<Categoria> categoriaList;
        private DatabaseManager db;

        public CategoriaLoader(Context context) {
            super(context);
            db = DatabaseManager.getInstance(context);
        }

        @Override
        protected void onStartLoading() {
            if (categoriaList != null) {
                deliverResult(categoriaList);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Categoria> loadInBackground() {
            db.openDB();
            List<Categoria> listCategoria = db.getCategorias();
            db.closeDB();
            return listCategoria;
        }

        @Override
        public void deliverResult(List<Categoria> data) {
            categoriaList = data;
            super.deliverResult(data);
        }

    }
}
