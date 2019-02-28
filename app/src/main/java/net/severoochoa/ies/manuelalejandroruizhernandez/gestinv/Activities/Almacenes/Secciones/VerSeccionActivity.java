package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.Secciones;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.VerAlmacenActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.PasillosAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Pasillo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Seccion;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Dialog.DialogIntroducirTexto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;


public class VerSeccionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Pasillo>>, PasillosAdapter.OnItemLongClickListener {

    private TextView tvNombre;
    private RecyclerView rvPasillos;
    private LinearLayoutManager linearLayoutManager;
    private PasillosAdapter pasillosAdapter;
    private DatabaseManager db;
    private String VALUE_NOMBRE = "Nombre";
    private int ID_LOADER_PASILLOS = 1;
    private Seccion seccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseManager.getInstance(this);
        seccion = new Seccion(
                getIntent().getLongExtra(VerAlmacenActivity.VALUE_ID, 0),
                getIntent().getLongExtra(VerAlmacenActivity.VALUE_IDALMACEN, 0),
                getIntent().getStringExtra(VerAlmacenActivity.VALUE_NOMBRE)
        );
        setContentView(R.layout.activity_ver_seccion);
        tvNombre = findViewById(R.id.tvNombre);
        rvPasillos = findViewById(R.id.rvPasillos);
        tvNombre.setText(getIntent().getStringExtra(VerAlmacenActivity.VALUE_NOMBRE));
        rvPasillos.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPasillos.setLayoutManager(linearLayoutManager);


     
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_ver_seccion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnAnadirPas:
                DialogIntroducirTexto dialogIntroducirTexto = DialogIntroducirTexto.newInstance("Añadir Pasillo", "Introduzca el nombre del pasillo");
                dialogIntroducirTexto.setOnDialogInsertarTextoListener(new DialogIntroducirTexto.DialogInsertarTextoInterface() {
                    @Override
                    public void onPossitiveDialogInsertarTexto(String valor) {
                        long almacenId = getIntent().getLongExtra(VerAlmacenActivity.VALUE_IDALMACEN, 0);
                        long seccionId = getIntent().getLongExtra(VerAlmacenActivity.VALUE_ID, 0);
                        Pasillo pasillo = new Pasillo(0, 0, 0, 0, valor, 0);
                        db.openDB();
                        if (db.insertPasilloInSeccion(almacenId, seccionId, pasillo) != -1) {
                            Toast.makeText(VerSeccionActivity.this, "Inserccion Realizada", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(VerSeccionActivity.this, "La Inserccion no ha sido Realizada", Toast.LENGTH_SHORT).show();
                        db.closeDB();
                        reiniciarLoader();
                    }

                    @Override
                    public void onNegativeDialogInsertarTexto() {
                    }
                });
                dialogIntroducirTexto.show(getSupportFragmentManager(), "id");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea eliminar esta seccion?")
                .setTitle("Eliminar Seccion")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Pasillo pasillo = pasillosAdapter.getPasillo(position);
                        db.openDB();
                        if (db.deletePasillo(pasillo.getId()) > 0) {
                            Toast.makeText(VerSeccionActivity.this, "Pasillo eliminado de la seccion", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(VerSeccionActivity.this, "El pasillo no ha sido eliminado de la seccion", Toast.LENGTH_SHORT).show();
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
        getSupportLoaderManager().restartLoader(ID_LOADER_PASILLOS, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(ID_LOADER_PASILLOS) != null) {
            if (!getSupportLoaderManager().hasRunningLoaders())
                getSupportLoaderManager().restartLoader(ID_LOADER_PASILLOS, null, this);
        } else getSupportLoaderManager().initLoader(ID_LOADER_PASILLOS, null, this);
    }

    @NonNull
    @Override
    public Loader<List<Pasillo>> onCreateLoader(int id, @Nullable Bundle args) {
        return new PasilloLoader(this, seccion);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Pasillo>> loader, List<Pasillo> data) {
        pasillosAdapter = new PasillosAdapter(this, data);
        rvPasillos.setAdapter(pasillosAdapter);
        pasillosAdapter.SetOnItemLongClickListener(this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Pasillo>> loader) {
        rvPasillos.setAdapter(null);
    }

    static class PasilloLoader extends AsyncTaskLoader<List<Pasillo>> {
        private List<Pasillo> pasilloList;
        private DatabaseManager db;
        private long seccionId;

        public PasilloLoader(Context context, Seccion seccion) {
            super(context);
            seccionId = seccion.getId();
            db = DatabaseManager.getInstance(context);
        }

        @Override
        protected void onStartLoading() {
            if (pasilloList != null) {
                deliverResult(pasilloList);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Pasillo> loadInBackground() {
            db.openDB();
            List<Pasillo> pasilloList1 = db.getPasillosFromSeccion(seccionId);
            db.closeDB();
            return pasilloList1;
        }

        @Override
        public void deliverResult(List<Pasillo> data) {
            pasilloList = data;
            super.deliverResult(data);
        }

    }
}
