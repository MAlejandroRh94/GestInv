package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Recepciones.CrearRecepcionActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Recepciones.VerRecepcionActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.RecepcionAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Pasillo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Recepcion;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;

public class RecepcionesActivity extends AppCompatActivity implements RecepcionAdapter.OnItemClickListener, RecepcionAdapter.OnItemLongClickListener, LoaderManager.LoaderCallbacks<List<Recepcion>>, View.OnClickListener {
    public static final int REQUEST_CODE_CREAR_RECEPCION = 1;
    public static final int REQUEST_CODE_VISUALIZAR_RECEPCION = 2;
    public static final int RESULT_CODE_MODIFICAR_RECEPCION = 3;
    private static final int ID_LOADER_RECEPCIONES = 1;

    public static String VALUE_IDPRODUCTO = "Idproducto";
    public static String VALUE_ID = "Id";
    public static String VALUE_IDALMACEN = "IdALMACEN";
    public static String VALUE_FECHA = "FECHA";
    public static String VALUE_CANTIDAD = "CANTIDAD";
    public static String VALUE_IDESTADO = "Estado";
    public static String VALUE_EDITABLE = "editable";


    private RecyclerView rvRecepciones;
    private FloatingActionButton fabCrearRecepcion;
    private RecepcionAdapter recepcionAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepciones);
        db = DatabaseManager.getInstance(this);
        fabCrearRecepcion = findViewById(R.id.fabCrearRecepcion);
        rvRecepciones = findViewById(R.id.rvRecepciones);
        fabCrearRecepcion.setOnClickListener(this);
        rvRecepciones.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRecepciones.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCrearRecepcion:
                db.openDB();
                if (db.getProductos().size() != 0 && db.getAlmacenes().size() != 0) {
                    Intent intent = new Intent(this, CrearRecepcionActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_CREAR_RECEPCION);
                }else Toast.makeText(this, "No existen productos o almacenes de los que hacer el envio", Toast.LENGTH_SHORT).show();
                db.closeDB();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CREAR_RECEPCION:
                if (resultCode == RESULT_OK) {
                    Recepcion recepcion = new Recepcion(
                            0,
                            data.getLongExtra(VALUE_IDALMACEN, 0),
                            data.getLongExtra(VALUE_IDPRODUCTO, 0),
                            1,
                            0,
                            Integer.valueOf(data.getStringExtra(VALUE_CANTIDAD))
                    );
                    db.openDB();
                    if (db.insertRecepcion(recepcion) != -1) {
                        Toast.makeText(this, "Inserccion Realizada", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "La Inserccion no ha sido Realizada", Toast.LENGTH_SHORT).show();

                    db.closeDB();
                }
                break;
            case REQUEST_CODE_VISUALIZAR_RECEPCION:
                if (resultCode == RESULT_CODE_MODIFICAR_RECEPCION) {
                    Recepcion recepcion = new Recepcion(
                            data.getLongExtra(VALUE_ID, 0),
                            data.getLongExtra(VALUE_IDALMACEN, 0),
                            data.getLongExtra(VALUE_IDPRODUCTO, 0),
                            data.getLongExtra(VALUE_IDESTADO, 0),
                            data.getLongExtra(VALUE_FECHA, 0),
                            data.getIntExtra(VALUE_CANTIDAD, 0)
                    );
                    if (data.getLongExtra(VALUE_IDESTADO, 0) == 3) {
                        db.openDB();
                        Pasillo pasillo = db.enoughSpaceToRecepcion(recepcion.getIdAlmacen(), recepcion.getIdProducto(), recepcion.getCantidadRecibida());
                        boolean error = false;
                        db.beginTransaction();
                        if (pasillo != null) {
                            if (db.updatePasillo(pasillo) <= 0 || db.updateRecepcion(recepcion) <= 0) {
                                error = true;
                            }
                        } else {
                            Toast.makeText(this, "No hay suficiente espacio para la recepcion", Toast.LENGTH_SHORT).show();
                        }
                        if (!error) {
                            Toast.makeText(this, "La recepcion ha sido correcta", Toast.LENGTH_SHORT).show();
                            db.setTransactionSuccesful();
                        } else {
                            Toast.makeText(this, "Error al realizar alguna de las actualizaciones de pasillo", Toast.LENGTH_SHORT).show();
                        }
                        db.endTransaction();
                        db.closeDB();
                    } else {
                        db.openDB();
                        if (db.updateRecepcion(recepcion) > 0) {
                            Toast.makeText(this, "Modificacion Realizada", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "La Modificacion no ha sido Realizada", Toast.LENGTH_SHORT).show();
                        db.closeDB();
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Recepcion recepcion = recepcionAdapter.getRecepcion(position);
        Intent intent = new Intent(this, VerRecepcionActivity.class);
        intent.putExtra(VALUE_ID, recepcion.getId());
        intent.putExtra(VALUE_IDPRODUCTO, recepcion.getIdProducto());
        intent.putExtra(VALUE_IDESTADO, recepcion.getIdEstado());
        intent.putExtra(VALUE_IDALMACEN, recepcion.getIdAlmacen());
        intent.putExtra(VALUE_FECHA, recepcion.getFechaCreacion());
        intent.putExtra(VALUE_CANTIDAD, recepcion.getCantidadRecibida());
        if (recepcion.getIdEstado() != 3) {
            intent.putExtra(VALUE_EDITABLE, true);
        } else intent.putExtra(VALUE_EDITABLE, false);


        startActivityForResult(intent, REQUEST_CODE_VISUALIZAR_RECEPCION);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea eliminar este elemento?")
                .setTitle("Eliminar Elemento")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Recepcion recepcion = recepcionAdapter.getRecepcion(position);
                        db.openDB();
                        if (db.deleteRecepcion(recepcion.getId()) > 0) {
                            Toast.makeText(RecepcionesActivity.this, "La recepcion ha sido eliminada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RecepcionesActivity.this, "La recepcion no ha podido ser eliminada", Toast.LENGTH_SHORT).show();
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
        getSupportLoaderManager().restartLoader(ID_LOADER_RECEPCIONES, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(ID_LOADER_RECEPCIONES) != null) {
            if (!getSupportLoaderManager().hasRunningLoaders())
                getSupportLoaderManager().restartLoader(ID_LOADER_RECEPCIONES, null, this);
        } else getSupportLoaderManager().initLoader(ID_LOADER_RECEPCIONES, null, this);
    }

    @NonNull
    @Override
    public Loader<List<Recepcion>> onCreateLoader(int id, @Nullable Bundle args) {
        return new RecepcionLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recepcion>> loader, List<Recepcion> data) {
        recepcionAdapter = new RecepcionAdapter(this, data);
        rvRecepciones.setAdapter(recepcionAdapter);
        recepcionAdapter.SetOnItemClickListener(this);
        recepcionAdapter.SetOnItemLongClickListener(this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recepcion>> loader) {

    }

    static class RecepcionLoader extends AsyncTaskLoader<List<Recepcion>> {
        private List<Recepcion> recepcionList;
        private DatabaseManager db;

        public RecepcionLoader(Context context) {
            super(context);
            db = DatabaseManager.getInstance(context);
        }

        @Override
        protected void onStartLoading() {
            if (recepcionList != null) {
                deliverResult(recepcionList);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Recepcion> loadInBackground() {
            db.openDB();
            List<Recepcion> recepcionList1 = db.getRecepciones();
            db.closeDB();
            return recepcionList1;
        }

        @Override
        public void deliverResult(List<Recepcion> data) {
            recepcionList = data;
            super.deliverResult(data);
        }

    }

}
