package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Informes.VerStockAlmacenesActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter.AlmacenAdapter;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Almacen;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;

public class AjustarInventarioActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Almacen>>, AlmacenAdapter.OnItemClickListener {
    private static final int ID_LOADER_ALMACENES = 1;
    public static final int REQUEST_CODE_VISUALIZAR_ALMACEN = 2;
    public static String VALUE_ID = "Id";
    private RecyclerView rvAlmacenes;
    private AlmacenAdapter almacenAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustar_inventario);

        db = DatabaseManager.getInstance(this);
        rvAlmacenes = findViewById(R.id.rvAlmacenes);
        rvAlmacenes.setHasFixedSize(true);


        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        rvAlmacenes.setLayoutManager(linearLayoutManager);


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
    }

    @Override
    public void onItemClick(View view, int position) {
        Almacen almacen = almacenAdapter.getAlmacen(position);
        Intent intent = new Intent(this, VerStockAlmacenesActivity.class);
        intent.putExtra(VALUE_ID, almacen.getId());
        startActivityForResult(intent, REQUEST_CODE_VISUALIZAR_ALMACEN);
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
