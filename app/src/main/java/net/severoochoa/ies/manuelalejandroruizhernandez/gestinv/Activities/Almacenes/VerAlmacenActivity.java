package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.Secciones.AnadirSeccionActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.Secciones.VerSeccionActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.StocksMinimos.AnadirStockMinimoActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.AlmacenesActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.SeccionAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.StockMinimoAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Almacen;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.ProductoCantidad;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Seccion;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.StockMinimo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;

public class VerAlmacenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Seccion>>, SeccionAdapter.OnItemClickListener, SeccionAdapter.OnItemLongClickListener, View.OnClickListener, StockMinimoAdapter.OnItemLongClickListener {

    private static final int REQUEST_CODE_ANADIR_SECCION = 1;
    private static final int REQUEST_CODE_ANADIR_STOCK_MINIMO = 3;
    private static final int REQUEST_CODE_VISUALIZAR_SECCION = 2;
    private static final String SAVE_EDITANDO = "EDITANDO";
    private boolean editando = false;

    private EditText etNombre, etDireccion;
    private RecyclerView rvSecciones;
    private RecyclerView rvStockMinimo;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    private DatabaseManager db;
    private Almacen almacen;
    private int ID_LOADER_SECCIONES = 1;
    private MenuItem mnModificar, mnGuardar;
    private SeccionAdapter seccionAdapter;
    private StockMinimoAdapter stockMinimoAdapter;
    private FloatingActionButton fabAnadirSeccion;
    private FloatingActionButton fabAnadirStockMinimo;
    public static String VALUE_ID = "id";
    public static String VALUE_IDALMACEN = "idalmacen";
    public static String VALUE_NOMBRE = "nombre";
    public static String VALUE_CANTIDAD = "cantidad";
    public static String VALUE_PRODUCTO = "producto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_almacen);

        etNombre = findViewById(R.id.etNombre);
        etDireccion = findViewById(R.id.etDireccion);
        fabAnadirSeccion = findViewById(R.id.fabAnadirSeccion);
        fabAnadirSeccion.setOnClickListener(this);
        db = DatabaseManager.getInstance(this);

        fabAnadirStockMinimo = findViewById(R.id.fabAnadirStockMinimo);
        fabAnadirStockMinimo.setOnClickListener(this);

        etDireccion.setEnabled(false);
        etNombre.setEnabled(false);

        almacen = new Almacen(getIntent().getLongExtra(AlmacenesActivity.VALUE_ID, 0)
                , getIntent().getStringExtra(AlmacenesActivity.VALUE_NOMBRE)
                , getIntent().getStringExtra(AlmacenesActivity.VALUE_DIRECCION)
        );

        etDireccion.setText(getIntent().getStringExtra(AlmacenesActivity.VALUE_DIRECCION));
        etNombre.setText(getIntent().getStringExtra(AlmacenesActivity.VALUE_NOMBRE));

        rvSecciones = findViewById(R.id.rvSecciones);
        rvSecciones.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvSecciones.setLayoutManager(linearLayoutManager);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvStockMinimo = findViewById(R.id.rvStocksMinimos);
        rvStockMinimo.setHasFixedSize(true);
        rvStockMinimo.setLayoutManager(linearLayoutManager1);
        if (savedInstanceState != null) {
            editando = savedInstanceState.getBoolean(SAVE_EDITANDO);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(ID_LOADER_SECCIONES) != null) {
            if (!getSupportLoaderManager().hasRunningLoaders())
                getSupportLoaderManager().restartLoader(ID_LOADER_SECCIONES, null, this);
        } else getSupportLoaderManager().initLoader(ID_LOADER_SECCIONES, null, this);

        cargarStockMin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_ver_almacen, menu);
        mnModificar = menu.findItem(R.id.mnModificar);
        mnGuardar = menu.findItem(R.id.mnGuardar);
        if (editando) {
            etNombre.setEnabled(true);
            etDireccion.setEnabled(true);
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
            case R.id.mnModificar:
                etNombre.setEnabled(true);
                etDireccion.setEnabled(true);
                mnModificar.setVisible(false);
                mnGuardar.setVisible(true);
                editando = true;

                return true;
            case R.id.mnGuardar:
                if (!etNombre.getText().toString().isEmpty() && !etDireccion.getText().toString().isEmpty()) {
                    Intent result = new Intent();
                    result.putExtra(AlmacenesActivity.VALUE_ID, getIntent().getLongExtra(AlmacenesActivity.VALUE_ID, 0));
                    result.putExtra(AlmacenesActivity.VALUE_NOMBRE, etNombre.getText().toString());
                    result.putExtra(AlmacenesActivity.VALUE_DIRECCION, etDireccion.getText().toString());
                    setResult(AlmacenesActivity.RESULT_MODIFICAR_ALMACEN, result);
                    finish();
                }
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
                        Seccion seccion = seccionAdapter.getSeccion(position);
                        db.openDB();
                        if (db.deleteSeccion(seccion.getId()) > 0) {
                            Toast.makeText(VerAlmacenActivity.this, "Seccion eliminada del almacen", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(VerAlmacenActivity.this, "La seccion no ha sido eliminado del almacen", Toast.LENGTH_SHORT).show();
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
        getSupportLoaderManager().restartLoader(ID_LOADER_SECCIONES, null, this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, VerSeccionActivity.class);
        Seccion seccion = seccionAdapter.getSeccion(position);
        intent.putExtra(VALUE_ID, seccion.getId());
        intent.putExtra(VALUE_IDALMACEN, seccion.getIdAlmacen());
        intent.putExtra(VALUE_NOMBRE, seccion.getNombre());
        startActivityForResult(intent, REQUEST_CODE_VISUALIZAR_SECCION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ANADIR_SECCION:
                if (resultCode == RESULT_OK) {
                    String nombre = data.getStringExtra(VALUE_NOMBRE);
                    Seccion seccion = new Seccion(0, almacen.getId(), nombre);
                    db.openDB();
                    if (db.insertSeccionInAlmacen(almacen.getId(), seccion) != -1) {
                        Toast.makeText(this, "Inserccion Realizada", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "La Inserccion no ha sido Realizada", Toast.LENGTH_SHORT).show();
                    db.closeDB();
                }
                break;
            case REQUEST_CODE_ANADIR_STOCK_MINIMO:
                if (resultCode == RESULT_OK) {
                    int cant = Integer.valueOf(data.getStringExtra(VALUE_CANTIDAD));
                    long producto = data.getLongExtra(VALUE_PRODUCTO, 0);
                    StockMinimo stockMinimo = new StockMinimo(0, producto, 0, cant);
                    db.openDB();
                    if (db.getStockMinimoFromAlmacenAndProducto(almacen.getId(), producto) == null) {
                        if (db.insertStockMinimoInAlmacen(almacen.getId(), stockMinimo) != -1) {
                            Toast.makeText(this, "Inserccion Realizada", Toast.LENGTH_SHORT).show();
                            List<ProductoCantidad> productoCantidad = db.getProductosStockFromAlmacen(almacen.getId());
                            boolean prodEx = false;
                            for (int i = 0; i < productoCantidad.size(); i++) {
                                if (productoCantidad.get(i).getIdProducto() == producto) {
                                    prodEx = true;
                                    if (cant >= productoCantidad.get(i).getCantidadProducto()) {
                                        generarNotificacion(new ProductoCantidad(producto, cant));
                                    }
                                }
                            }
                            if (!prodEx) {
                                ProductoCantidad pC = new ProductoCantidad(producto, cant);
                                generarNotificacion(pC);
                            }
                        } else
                            Toast.makeText(this, "La Inserccion no ha sido Realizada", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Ya existe un stock minimo para este producto el creado no se ha insertado", Toast.LENGTH_SHORT).show();
                    db.closeDB();
                }
                break;
        }
    }

    private void generarNotificacion(ProductoCantidad productoCantidad) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannelName";
            String description = "NotificationChannelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NotificationChannelId", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        db.openDB();
        Producto producto = db.getProducto(productoCantidad.getIdProducto());
        db.closeDB();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "NotificationChannelId")
                .setSmallIcon(R.drawable.ic_stat_alerta_stock_minimo)
                .setContentTitle("Alerta Stock Minimo infringido")
                .setContentText("El stock actual del producto " + producto.getNombre() + " es inferior ... ")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(
                                "El stock actual del producto " + producto.getNombre() + " es inferior a la cantidad establecida en el almacen " + almacen.getNombre() + ", que es de " + productoCantidad.getCantidadProducto()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mBuilder.build());
    }

    @NonNull
    @Override
    public Loader<List<Seccion>> onCreateLoader(int id, @Nullable Bundle args) {
        return new SeccionLoader(this, almacen);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Seccion>> loader, List<Seccion> data) {
        seccionAdapter = new SeccionAdapter(this, data);
        rvSecciones.setAdapter(seccionAdapter);
        seccionAdapter.SetOnItemClickListener(this);
        seccionAdapter.SetOnItemLongClickListener(this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Seccion>> loader) {
        rvSecciones.setAdapter(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAnadirSeccion:
                Intent intentSeccion = new Intent(this, AnadirSeccionActivity.class);
                startActivityForResult(intentSeccion, REQUEST_CODE_ANADIR_SECCION);
                break;
            case R.id.fabAnadirStockMinimo:
                db.openDB();
                if (db.getProductos().size() != 0) {
                    Intent intentStockMinimo = new Intent(this, AnadirStockMinimoActivity.class);
                    startActivityForResult(intentStockMinimo, REQUEST_CODE_ANADIR_STOCK_MINIMO);
                } else
                    Toast.makeText(this, "No existen productos que marcar con un stock minimo", Toast.LENGTH_SHORT).show();
                db.closeDB();
                break;
        }
    }

    @Override
    public void onItemLongClickSto(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea eliminar este stock minimo?")
                .setTitle("Eliminar stock minimo")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StockMinimo stockMinimo = stockMinimoAdapter.getStockMinimo(position);
                        db.openDB();
                        if (db.deleteStockMinimo(stockMinimo.getId()) > 0) {
                            Toast.makeText(VerAlmacenActivity.this, "Stock minimo eliminado del almacen", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(VerAlmacenActivity.this, "El stock minimo no ha sido eliminado del almacen", Toast.LENGTH_SHORT).show();
                        db.closeDB();
                        cargarStockMin();
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

    private void cargarStockMin() {
        db.openDB();
        stockMinimoAdapter = new StockMinimoAdapter(this, db.getStockMinimoFromAlmacen(almacen.getId()));
        db.closeDB();
        rvStockMinimo.setAdapter(stockMinimoAdapter);
        stockMinimoAdapter.SetOnItemLongClickListener(this);
    }

    static class SeccionLoader extends AsyncTaskLoader<List<Seccion>> {
        private List<Seccion> seccionList;
        private DatabaseManager db;
        private long almacenId;

        public SeccionLoader(Context context, Almacen almacen) {
            super(context);
            almacenId = almacen.getId();
            db = DatabaseManager.getInstance(context);
        }

        @Override
        protected void onStartLoading() {
            if (seccionList != null) {
                deliverResult(seccionList);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Seccion> loadInBackground() {
            db.openDB();
            List<Seccion> almacenList1 = db.getSeccionesFromAlmacen(almacenId);
            db.closeDB();
            return almacenList1;
        }

        @Override
        public void deliverResult(List<Seccion> data) {
            seccionList = data;
            super.deliverResult(data);
        }

    }
}
