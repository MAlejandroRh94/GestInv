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

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Productos.CrearProductoActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Productos.VerProductoActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.ProductoAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;

public class ProductosActivity extends AppCompatActivity implements View.OnClickListener, ProductoAdapter.OnItemClickListener, ProductoAdapter.OnItemLongClickListener
        , LoaderManager.LoaderCallbacks<List<Producto>> {


    private RecyclerView rvProductos;
    private FloatingActionButton fabCrearProducto;
    private ProductoAdapter productoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseManager db;
    private final int REQUEST_CODE_ANADIR_PRODUCTO = 1;
    public static String VALUE_NOMBRE = "Nombre";
    public static String VALUE_IDCATEGORIA = "IDcategoria";
    private final int REQUEST_CODE_VISUALIZAR_PRODUCTO = 2;
    public static String VALUE_ID = "ID";
    public static int RESULT_MODIFICAR_PRODUCTO = 3;
    private int ID_LOADER_PRODUCTOS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        db = DatabaseManager.getInstance(this);

        fabCrearProducto = findViewById(R.id.fabCrearProductos);
        fabCrearProducto.setOnClickListener(this);

        rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProductos.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCrearProductos:
                db.openDB();
                if (db.getCategorias().size() != 0) {
                    Intent intentCrearPro = new Intent(this, CrearProductoActivity.class);
                    startActivityForResult(intentCrearPro, REQUEST_CODE_ANADIR_PRODUCTO);
                } else Toast.makeText(this, "No existen categorias de las que hacer el producto", Toast.LENGTH_SHORT).show();
                db.closeDB();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ANADIR_PRODUCTO:
                if (resultCode == RESULT_OK) {
                    String nombre = data.getStringExtra(VALUE_NOMBRE);
                    long idCategoria = data.getLongExtra(VALUE_IDCATEGORIA, 0);
                    Producto producto = new Producto(0, nombre, idCategoria);
                    db.openDB();
                    if (db.insertProducto(producto) != -1) {
                        Toast.makeText(this, "Inserccion Realizada", Toast.LENGTH_SHORT).show();
                    }
                    db.closeDB();
                }
                break;
            case REQUEST_CODE_VISUALIZAR_PRODUCTO:
                if (resultCode == RESULT_MODIFICAR_PRODUCTO) {
                    long id = data.getLongExtra(VALUE_ID, 0);
                    String nombre = data.getStringExtra(VALUE_NOMBRE);
                    long idCategoria = data.getLongExtra(VALUE_IDCATEGORIA, 0);
                    Producto producto = new Producto(id, nombre, idCategoria);
                    db.openDB();
                    if (db.updateProducto(producto) > 0) {
                        Toast.makeText(this, "Modificacion Realizada", Toast.LENGTH_SHORT).show();
                    }
                    db.closeDB();
                }
                break;
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        Producto producto = productoAdapter.getProducto(position);
        Intent intent = new Intent(this, VerProductoActivity.class);
        intent.putExtra(VALUE_ID, producto.getId());
        intent.putExtra(VALUE_NOMBRE, producto.getNombre());
        intent.putExtra(VALUE_IDCATEGORIA, producto.getIdCategoria());
        startActivityForResult(intent, REQUEST_CODE_VISUALIZAR_PRODUCTO);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea eliminar este elemento?")
                .setTitle("Eliminar Elemento")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Producto producto = productoAdapter.getProducto(position);
                        db.openDB();
                        if (db.deleteProducto(producto.getId()) > 0) {
                            Toast.makeText(ProductosActivity.this, "El producto ha sido eliminado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductosActivity.this, "El producto no ha podido ser eliminado", Toast.LENGTH_SHORT).show();
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
        getSupportLoaderManager().restartLoader(ID_LOADER_PRODUCTOS, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(ID_LOADER_PRODUCTOS) != null) {
            if (!getSupportLoaderManager().hasRunningLoaders())
                getSupportLoaderManager().restartLoader(ID_LOADER_PRODUCTOS, null, this);
        } else getSupportLoaderManager().initLoader(ID_LOADER_PRODUCTOS, null, this);
    }

    @Override
    public Loader<List<Producto>> onCreateLoader(int id, Bundle args) {
        return new ProductoLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Producto>> loader, List<Producto> data) {
        productoAdapter = new ProductoAdapter(this, data);
        rvProductos.setAdapter(productoAdapter);
        productoAdapter.SetOnItemClickListener(this);
        productoAdapter.SetOnItemLongClickListener(this);
    }

    @Override
    public void onLoaderReset(Loader<List<Producto>> loader) {
    }

    static class ProductoLoader extends AsyncTaskLoader<List<Producto>> {
        private List<Producto> productoList;
        private DatabaseManager db;

        public ProductoLoader(Context context) {
            super(context);
            db = DatabaseManager.getInstance(context);
        }

        @Override
        protected void onStartLoading() {
            if (productoList != null) {
                deliverResult(productoList);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Producto> loadInBackground() {
            db.openDB();
            List<Producto> productoList = db.getProductos();
            db.closeDB();
            return productoList;
        }

        @Override
        public void deliverResult(List<Producto> data) {
            productoList = data;
            super.deliverResult(data);
        }

    }
}
