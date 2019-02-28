package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Recepciones;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.RecepcionesActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Almacen;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;


public class CrearRecepcionActivity extends AppCompatActivity {
    private static final String SAVE_PRODUCTO = "PROD";
    private static final String SAVE_CANT = "CANT";
    private static final String SAVE_ALMAC = "ALM";

    private EditText etCant;
    private Spinner spProducto, spAlmacen;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_recepcion);

        db = DatabaseManager.getInstance(this);
        etCant = findViewById(R.id.etCantidad);
        spAlmacen = findViewById(R.id.spAlmacen);
        spProducto = findViewById(R.id.spProducto);

        db.openDB();
        ArrayAdapter<Almacen> adaptadorAlm = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, db.getAlmacenes());
        ArrayAdapter<Producto> adaptadorPro = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, db.getProductos());
        db.closeDB();

        adaptadorAlm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaptadorPro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spProducto.setAdapter(adaptadorPro);
        spAlmacen.setAdapter(adaptadorAlm);

        if (savedInstanceState != null) {
            etCant.setText(savedInstanceState.getString(SAVE_CANT));
            spAlmacen.setSelection(savedInstanceState.getInt(SAVE_ALMAC));
            spProducto.setSelection(savedInstanceState.getInt(SAVE_PRODUCTO));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_crear_recepcion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnGuardar:
                if (!etCant.getText().toString().isEmpty()) {
                    Intent result = new Intent();
                    result.putExtra(RecepcionesActivity.VALUE_CANTIDAD, etCant.getText().toString());

                    Almacen almacen = (Almacen) spAlmacen.getSelectedItem();
                    result.putExtra(RecepcionesActivity.VALUE_IDALMACEN, almacen.getId());

                    Producto producto = (Producto) spProducto.getSelectedItem();
                    result.putExtra(RecepcionesActivity.VALUE_IDPRODUCTO, producto.getId());

                    setResult(RESULT_OK, result);
                    finish();
                } else
                    Toast.makeText(this, "Rellene los campos vacios ", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_CANT, etCant.getText().toString());
        outState.putInt(SAVE_PRODUCTO, spProducto.getSelectedItemPosition());
        outState.putInt(SAVE_ALMAC, spAlmacen.getSelectedItemPosition());
    }
}
