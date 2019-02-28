package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Productos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.ProductosActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

public class CrearProductoActivity extends AppCompatActivity {
    private Spinner spCategoria;
    private EditText etNombre;
    private DatabaseManager db;
    private static final String SAVE_NOMBRE = "nombre";
    private static final String SAVE_IDCATEGORIA = "idcategoria";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        db = DatabaseManager.getInstance(this);
        etNombre = findViewById(R.id.etNombre);
        spCategoria = findViewById(R.id.spCategoria);


        db.openDB();
        ArrayAdapter<Categoria> adaptador =
                new ArrayAdapter<Categoria>(this,
                        android.R.layout.simple_spinner_item, db.getCategorias());

        db.closeDB();
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCategoria.setAdapter(adaptador);


        if (savedInstanceState != null) {
            etNombre.setText(savedInstanceState.getString(SAVE_NOMBRE));
            spCategoria.setSelection(savedInstanceState.getInt(SAVE_IDCATEGORIA));
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
                if (!etNombre.getText().toString().isEmpty()) {
                    Intent result = new Intent();
                    result.putExtra(ProductosActivity.VALUE_NOMBRE, etNombre.getText().toString());
                    Categoria categoria = (Categoria) spCategoria.getSelectedItem();
                    result.putExtra(ProductosActivity.VALUE_IDCATEGORIA, categoria.getId());
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
        outState.putString(SAVE_NOMBRE, etNombre.getText().toString());
        outState.putInt(SAVE_IDCATEGORIA, spCategoria.getSelectedItemPosition());
    }
}
