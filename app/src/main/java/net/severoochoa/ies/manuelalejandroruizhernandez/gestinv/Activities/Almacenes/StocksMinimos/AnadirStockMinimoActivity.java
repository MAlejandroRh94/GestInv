package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.StocksMinimos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.VerAlmacenActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

public class AnadirStockMinimoActivity extends AppCompatActivity {
    private Spinner spProductos;
    private EditText etCantidad;
    private DatabaseManager db;
    private static final String SAVE_CANTIDAD = "cantidad";
    private static final String SAVE_IDPRODUCTO = "idproducto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_stock_minimo);
        db = DatabaseManager.getInstance(this);
        etCantidad = findViewById(R.id.etCantidad);
        spProductos = findViewById(R.id.spProductos);

        db.openDB();
        ArrayAdapter<Producto> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, db.getProductos());
        db.closeDB();

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProductos.setAdapter(adaptador);

        if (savedInstanceState != null) {
            etCantidad.setText(String.valueOf(savedInstanceState.getInt(SAVE_CANTIDAD)));
            spProductos.setSelection(savedInstanceState.getInt(SAVE_IDPRODUCTO));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_crear_producto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnGuardar:
                Intent result = new Intent();
                result.putExtra(VerAlmacenActivity.VALUE_CANTIDAD, etCantidad.getText().toString());
                Producto producto = (Producto) spProductos.getSelectedItem();
                result.putExtra(VerAlmacenActivity.VALUE_PRODUCTO, producto.getId());
                setResult(RESULT_OK, result);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_CANTIDAD, etCantidad.getText().toString());
        outState.putInt(SAVE_IDPRODUCTO, spProductos.getSelectedItemPosition());
    }
}
