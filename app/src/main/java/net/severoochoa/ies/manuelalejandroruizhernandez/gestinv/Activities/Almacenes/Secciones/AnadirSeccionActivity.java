package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.Secciones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Almacenes.VerAlmacenActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;


public class AnadirSeccionActivity extends AppCompatActivity {
    private EditText etNombre;
    private String SAVE_NOMBRE = "Nombre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_seccion);
        etNombre = findViewById(R.id.etNombre);
        if (savedInstanceState != null) {
            etNombre.setText(savedInstanceState.getString(SAVE_NOMBRE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_NOMBRE, etNombre.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_anadir_seccion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnGuardar:
                if (!etNombre.getText().toString().isEmpty()) {
                    Intent result = new Intent();
                    result.putExtra(VerAlmacenActivity.VALUE_NOMBRE, etNombre.getText().toString());
                    setResult(RESULT_OK, result);
                    finish();
                } else Toast.makeText(this, "Rellene los campos vacios ", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
