package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Informes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.AjustarInventarioActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.ProductoCantidadAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.ProductoCantidad;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;


public class VerStockAlmacenesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ProductoCantidad>> {
    private static final int ID_LOADER_PRODUCTO_CANTIDAD = 1;
    private RecyclerView rvProductoCantidad;
    private ProductoCantidadAdapter productoCantidadAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_stock_almacenes);
        db = DatabaseManager.getInstance(this);
        rvProductoCantidad = findViewById(R.id.rvProductoCantidad);
        rvProductoCantidad.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProductoCantidad.setLayoutManager(linearLayoutManager);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportLoaderManager().getLoader(ID_LOADER_PRODUCTO_CANTIDAD) != null) {
            if (!getSupportLoaderManager().hasRunningLoaders())
                getSupportLoaderManager().restartLoader(ID_LOADER_PRODUCTO_CANTIDAD, null, this);
        } else getSupportLoaderManager().initLoader(ID_LOADER_PRODUCTO_CANTIDAD, null, this);
    }

    @NonNull
    @Override
    public Loader<List<ProductoCantidad>> onCreateLoader(int id, @Nullable Bundle args) {
        return new ProductoCantidadLoader(this, getIntent().getLongExtra(AjustarInventarioActivity.VALUE_ID, 0));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<ProductoCantidad>> loader, List<ProductoCantidad> data) {
        productoCantidadAdapter = new ProductoCantidadAdapter(this, data);
        rvProductoCantidad.setAdapter(productoCantidadAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<ProductoCantidad>> loader) {

    }

    static class ProductoCantidadLoader extends AsyncTaskLoader<List<ProductoCantidad>> {
        private List<ProductoCantidad> productoCantidadList;
        private DatabaseManager db;
        private long idAlm;

        public ProductoCantidadLoader(Context context, long idAlm) {
            super(context);
            db = DatabaseManager.getInstance(context);
            this.idAlm = idAlm;
        }

        @Override
        protected void onStartLoading() {
            if (productoCantidadList != null) {
                deliverResult(productoCantidadList);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<ProductoCantidad> loadInBackground() {
            db.openDB();
            List<ProductoCantidad> productoCantidadList1 = db.getProductosStockFromAlmacen(idAlm);
            db.closeDB();
            return productoCantidadList1;
        }

        @Override
        public void deliverResult(List<ProductoCantidad> data) {
            productoCantidadList = data;
            super.deliverResult(data);
        }

    }
}
