package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.AlmacenesActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;


public class CrearAlmacenActivity extends AppCompatActivity {
    private static final String SAVE_DIRECCION = "Direccion";
    private static final String SAVE_NOMBRE = "Nombre";
    private EditText etNombre, etDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_almacen);
        etNombre = findViewById(R.id.etNombre);
        etDireccion = findViewById(R.id.etDireccion);
        if (savedInstanceState != null) {
            etNombre.setText(savedInstanceState.getString(SAVE_NOMBRE));
            etDireccion.setText(savedInstanceState.getString(SAVE_DIRECCION));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_NOMBRE, etNombre.getText().toString());
        outState.putString(SAVE_DIRECCION, etDireccion.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_crear_almacen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnGuardar:
                if (!etDireccion.getText().toString().isEmpty() && !etNombre.getText().toString().isEmpty()) {
                    Intent result = new Intent();
                    result.putExtra(AlmacenesActivity.VALUE_NOMBRE, etNombre.getText().toString());
                    result.putExtra(AlmacenesActivity.VALUE_DIRECCION, etDireccion.getText().toString());
                    setResult(RESULT_OK, result);
                    finish();
                } else
                    Toast.makeText(this, "Rellene los campos vacios ", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
