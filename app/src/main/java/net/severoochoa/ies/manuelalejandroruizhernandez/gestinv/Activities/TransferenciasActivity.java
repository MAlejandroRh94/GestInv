package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Transferencias.CrearTransferenciasActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Transferencias.VerTransferenciasActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.TransferenciaAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Almacen;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Pasillo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.ProductoCantidad;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.StockMinimo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Transferencia;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;

public class TransferenciasActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Transferencia>>, View.OnClickListener, TransferenciaAdapter.OnItemClickListener, TransferenciaAdapter.OnItemLongClickListener {
    public static final int REQUEST_CODE_CREAR_TRANSFERENCIA = 1;
    public static final int REQUEST_CODE_VISUALIZAR_TRANSFERENCIA = 2;
    private static final int ID_LOADER_TRANSFERENCIAS = 1;

    public static String VALUE_IDPRODUCTO = "Idproducto";
    public static String VALUE_ID = "Id";
    public static String VALUE_IDALMACEN = "IdALMACEN";
    public static String VALUE_FECHA = "FECHA";
    public static String VALUE_CANTIDAD = "CANTIDAD";
    public static String VALUE_IDESTADO = "Estado";
    public static String VALUE_EDITABLE = "editable";


    private RecyclerView rvTransferencias;
    private FloatingActionButton fabCrearTransferencia;
    private TransferenciaAdapter transferenciaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseManager db;
    public static int RESULT_CODE_MODIFICAR_TRANSFERENCIA = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencias);
        db = DatabaseManager.getInstance(this);
        fabCrearTransferencia = findViewById(R.id.fabCrearTransferencia);
        rvTransferencias = findViewById(R.id.rvTransferencias);
        fabCrearTransferencia.setOnClickListener(this);
        rvTransferencias.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTransferencias.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCrearTransferencia:
                db.openDB();
                if (db.getProductos().size() != 0 && db.getAlmacenes().size() != 0) {
                    Intent intent = new Intent(this, CrearTransferenciasActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_CREAR_TRANSFERENCIA);
                } else
                    Toast.makeText(this, "No existen productos o almacenes de los que hacer la transferencia", Toast.LENGTH_SHORT).show();
                db.closeDB();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CREAR_TRANSFERENCIA:
                if (resultCode == RESULT_OK) {
                    Transferencia transferencia = new Transferencia(
                            0,
                            data.getLongExtra(VALUE_IDPRODUCTO, 0),
                            1,
                            data.getLongExtra(VALUE_IDALMACEN, 0),
                            0,
                            Integer.valueOf(data.getStringExtra(VALUE_CANTIDAD))
                    );
                    db.openDB();
                    if (db.insertTransferencia(transferencia) != -1) {
                        Toast.makeText(this, "Inserccion Realizada", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "La Inserccion no ha sido Realizada", Toast.LENGTH_SHORT).show();

                    db.closeDB();
                }
                break;
            case REQUEST_CODE_VISUALIZAR_TRANSFERENCIA:
                if (resultCode == RESULT_CODE_MODIFICAR_TRANSFERENCIA) {
                    Transferencia transferencia = new Transferencia(
                            data.getLongExtra(VALUE_ID, 0),
                            data.getLongExtra(VALUE_IDPRODUCTO, 0),
                            data.getLongExtra(VALUE_IDESTADO, 0),
                            data.getLongExtra(VALUE_IDALMACEN, 0),
                            data.getLongExtra(VALUE_FECHA, 0),
                            data.getIntExtra(VALUE_CANTIDAD, 0)
                    );
                    if (data.getLongExtra(VALUE_IDESTADO, 0) == 3) {
                        db.openDB();
                        List<Pasillo> pasilloList = db.enoughStockToTransferencia(transferencia.getIdAlmacen(), transferencia.getIdProducto(), transferencia.getCantidadTransferida());
                        boolean error = false;
                        db.beginTransaction();
                        if (pasilloList != null) {
                            for (int i = 0; i < pasilloList.size(); i++) {
                                if (db.updatePasillo(pasilloList.get(i)) <= 0) {
                                    error = true;
                                }
                            }
                            if (db.updateTransferencia(transferencia) <= 0) {
                                error = true;
                            }
                            List<ProductoCantidad> productoCantidadList = db.getProductosStockFromAlmacen(transferencia.getIdAlmacen());
                            for (int i = 0; i < productoCantidadList.size(); i++) {
                                if (productoCantidadList.get(i).getIdProducto() == transferencia.getIdProducto()) {
                                    StockMinimo stockMinimo = db.getStockMinimoFromAlmacenAndProducto(transferencia.getIdAlmacen(), transferencia.getIdProducto());
                                    if (stockMinimo != null) {
                                        if (stockMinimo.getCantMin() >= productoCantidadList.get(i).getCantidadProducto()) {
                                            stockMinimoNotificacion(stockMinimo);
                                        }
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, "No hay suficiente stock para realizar la transferencia", Toast.LENGTH_SHORT).show();
                        }
                        if (!error) {
                            Toast.makeText(this, "La transferencia ha sido correcta", Toast.LENGTH_SHORT).show();
                            db.setTransactionSuccesful();
                        } else {
                            Toast.makeText(this, "Error al realizar alguna de las actualizaciones de los pasillos", Toast.LENGTH_SHORT).show();
                        }
                        db.endTransaction();

                        db.closeDB();
                    } else {
                        db.openDB();
                        if (db.updateTransferencia(transferencia) > 0) {
                            Toast.makeText(this, "Modificacion Realizada", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "La Modificacion no ha sido Realizada", Toast.LENGTH_SHORT).show();
                        db.closeDB();
                    }
                }
                break;
        }
    }

    private void stockMinimoNotificacion(StockMinimo stockMinimo) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannelName";
            String description = "NotificationChannelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NotificationChannelId", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        String txtPr = "";
        String txtAl = "";
        db.openDB();
        Producto producto = db.getProducto(stockMinimo.getIdProd());
        if (producto != null) {
            txtPr = producto.getNombre();
        }
        Almacen almacen = db.getAlmacen(stockMinimo.getIdAlm());
        if (almacen != null) {
            txtAl = almacen.getNombre();
        }
        db.closeDB();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "NotificationChannelId")
                .setSmallIcon(R.drawable.ic_stat_alerta_stock_minimo)
                .setContentTitle("Alerta de Stock minimo sobrepasado")
                .setContentText("El stock del producto:" + txtPr + " es menor al establecido :" + stockMinimo.getCantMin() + " en el Almacen" + txtAl)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(
                                "El stock del producto:" + txtPr + " es menor al establecido :" + stockMinimo.getCantMin() + " en el Almacen" + txtAl))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mBuilder.build());

    }

    @Override
    public void onItemClick(View view, int position) {
        Transferencia transferencia = transferenciaAdapter.getTransferencia(position);
        Intent intent = new Intent(this, VerTransferenciasActivity.class);
        intent.putExtra(VALUE_ID, transferencia.getIdTransferencia());
        intent.putExtra(VALUE_IDPRODUCTO, transferencia.getIdProducto());
        intent.putExtra(VALUE_IDESTADO, transferencia.getIdEstado());
        intent.putExtra(VALUE_IDALMACEN, transferencia.getIdAlmacen());
        intent.putExtra(VALUE_FECHA, transferencia.getFechaCreacion());
        intent.putExtra(VALUE_CANTIDAD, transferencia.getCantidadTransferida());
        if (transferencia.getIdEstado() != 3) {
            intent.putExtra(VALUE_EDITABLE, true);
        } else intent.putExtra(VALUE_EDITABLE, false);
        startActivityForResult(intent, REQUEST_CODE_VISUALIZAR_TRANSFERENCIA);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea eliminar este elemento?")
                .setTitle("Eliminar Elemento")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Transferencia transferencia = transferenciaAdapter.getTransferencia(position);
                        db.openDB();
                        if (db.deleteTransferencia(transferencia.getIdTransferencia()) > 0) {
                            Toast.makeText(TransferenciasActivity.this, "La transferencia ha sido eliminada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TransferenciasActivity.this, "La transferencia no ha podido ser eliminada", Toast.LENGTH_SHORT).show();
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
        getSupportLoaderManager().restartLoader(ID_LOADER_TRANSFERENCIAS, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(ID_LOADER_TRANSFERENCIAS) != null) {
            if (!getSupportLoaderManager().hasRunningLoaders())
                getSupportLoaderManager().restartLoader(ID_LOADER_TRANSFERENCIAS, null, this);
        } else getSupportLoaderManager().initLoader(ID_LOADER_TRANSFERENCIAS, null, this);
    }

    @Override
    public Loader<List<Transferencia>> onCreateLoader(int id, Bundle args) {
        return new TransferenciaLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Transferencia>> loader, List<Transferencia> data) {
        transferenciaAdapter = new TransferenciaAdapter(this, data);
        rvTransferencias.setAdapter(transferenciaAdapter);
        transferenciaAdapter.SetOnItemClickListener(this);
        transferenciaAdapter.SetOnItemLongClickListener(this);
    }

    @Override
    public void onLoaderReset(Loader<List<Transferencia>> loader) {
    }


    static class TransferenciaLoader extends AsyncTaskLoader<List<Transferencia>> {
        private List<Transferencia> transferenciaList;
        private DatabaseManager db;

        public TransferenciaLoader(Context context) {
            super(context);
            db = DatabaseManager.getInstance(context);
        }

        @Override
        protected void onStartLoading() {
            if (transferenciaList != null) {
                deliverResult(transferenciaList);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Transferencia> loadInBackground() {
            db.openDB();
            List<Transferencia> listTransferencia = db.getTransferencias();
            db.closeDB();
            return listTransferencia;
        }

        @Override
        public void deliverResult(List<Transferencia> data) {
            transferenciaList = data;
            super.deliverResult(data);
        }

    }

}
