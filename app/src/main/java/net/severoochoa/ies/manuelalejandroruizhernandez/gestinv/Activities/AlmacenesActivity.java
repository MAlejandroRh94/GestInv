package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.CrearAlmacenActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.VerAlmacenActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.AlmacenAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Almacen;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;

public class AlmacenesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Almacen>>,View.OnClickListener , AlmacenAdapter.OnItemClickListener, AlmacenAdapter.OnItemLongClickListener {
    public static final int REQUEST_CODE_ANADIR_ALMACEN = 1;
    public static final int REQUEST_CODE_VISUALIZAR_ALMACEN = 2;
    public static final int RESULT_MODIFICAR_ALMACEN = 4;
    private static final int ID_LOADER_ALMACENES = 1;
    public static String VALUE_ID = "Id";
    public static String VALUE_NOMBRE = "Nombre";
    public static String VALUE_DIRECCION = "Direccion";
    private RecyclerView rvAlmacenes;
    private FloatingActionButton fabCrearAlmacen;
    private AlmacenAdapter almacenAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacenes);

        db = DatabaseManager.getInstance(this);
        fabCrearAlmacen = findViewById(R.id.fabCrearAlmacen);
        fabCrearAlmacen.setOnClickListener(this);
        rvAlmacenes = findViewById(R.id.rvAlmacenes);
        rvAlmacenes.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAlmacenes.setLayoutManager(linearLayoutManager);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCrearAlmacen:
                Intent intent = new Intent(this, CrearAlmacenActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ANADIR_ALMACEN);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ANADIR_ALMACEN:
                if (resultCode == RESULT_OK) {
                    String nombre = data.getStringExtra(VALUE_NOMBRE);
                    String direccion = data.getStringExtra(VALUE_DIRECCION);
                    Almacen almacen = new Almacen(0, nombre, direccion);
                    db.openDB();
                    if (db.insertAlmacen(almacen) != -1) {
                        Toast.makeText(this, "Inserccion Realizada", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "La Inserccion no ha sido Realizada", Toast.LENGTH_SHORT).show();

                    db.closeDB();
                }
                break;
            case REQUEST_CODE_VISUALIZAR_ALMACEN:
                if (resultCode == RESULT_MODIFICAR_ALMACEN) {
                    long id = data.getLongExtra(VALUE_ID, 0);
                    String nombre = data.getStringExtra(VALUE_NOMBRE);
                    String direccion = data.getStringExtra(VALUE_DIRECCION);
                    Almacen almacen = new Almacen(id, nombre, direccion);
                    db.openDB();
                    if (db.updateAlmacen(almacen) > 0) {
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
        Almacen almacen = almacenAdapter.getAlmacen(position);
        Intent intent = new Intent(this, VerAlmacenActivity.class);
        intent.putExtra(VALUE_ID, almacen.getId());
        intent.putExtra(VALUE_NOMBRE, almacen.getNombre());
        intent.putExtra(VALUE_DIRECCION, almacen.getDireccion());
        startActivityForResult(intent, REQUEST_CODE_VISUALIZAR_ALMACEN);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea eliminar este elemento?")
                .setTitle("Eliminar Elemento")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Almacen almacen = almacenAdapter.getAlmacen(position);
                        db.openDB();
                        if (db.deleteAlmacen(almacen.getId()) > 0) {
                            Toast.makeText(AlmacenesActivity.this, "El Almacen ha sido eliminado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AlmacenesActivity.this, "El Almacen no ha podido ser eliminado", Toast.LENGTH_SHORT).show();
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
        getSupportLoaderManager().restartLoader(ID_LOADER_ALMACENES, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(ID_LOADER_ALMACENES) != null) {
            if (!getSupportLoaderManager().hasRunningLoaders())
                getSupportLoaderManager().restartLoader(ID_LOADER_ALMACENES, null, this);
        } else getSupportLoaderManager().initLoader(ID_LOADER_ALMACENES, null, this);
    }

    @Override
    public Loader<List<Almacen>> onCreateLoader(int id, Bundle args) {
        return new AlmacenLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Almacen>> loader, List<Almacen> data) {
        almacenAdapter = new AlmacenAdapter(this, data);
        rvAlmacenes.setAdapter(almacenAdapter);
        almacenAdapter.SetOnItemClickListener(this);
        almacenAdapter.SetOnItemLongClickListener(this);
    }

    @Override
    public void onLoaderReset(Loader<List<Almacen>> loader) {
    }

    static class AlmacenLoader extends AsyncTaskLoader<List<Almacen>> {
        private List<Almacen> almacenList;
        private DatabaseManager db;

        public AlmacenLoader(Context context) {
            super(context);
            db = DatabaseManager.getInstance(context);
        }

        @Override
        protected void onStartLoading() {
            if (almacenList != null) {
                deliverResult(almacenList);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Almacen> loadInBackground() {
            db.openDB();
            List<Almacen> listAlmacen = db.getAlmacenes();
            db.closeDB();
            return listAlmacen;
        }

        @Override
        public void deliverResult(List<Almacen> data) {
            almacenList = data;
            super.deliverResult(data);
        }

    }

}
