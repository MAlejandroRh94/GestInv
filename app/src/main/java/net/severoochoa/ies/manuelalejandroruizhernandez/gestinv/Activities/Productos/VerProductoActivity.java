package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Productos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.ProductosActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;


public class VerProductoActivity extends AppCompatActivity {
    private EditText etNombre;
    private MenuItem mnModificar, mnGuardar;
    private DatabaseManager db;
    private Spinner spCategorias;
    private static final String SAVE_EDITANDO = "EDITANDO";
    private boolean editando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);
        etNombre = findViewById(R.id.etNombre);
        spCategorias = findViewById(R.id.spCategorias);
        db = DatabaseManager.getInstance(this);
        etNombre.setEnabled(false);
        spCategorias.setEnabled(false);
        db.openDB();
        List<Categoria> categoriaList = db.getCategorias();
        db.closeDB();
        ArrayAdapter<Categoria> adaptador =
                new ArrayAdapter<Categoria>(this,
                        android.R.layout.simple_spinner_item, categoriaList);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategorias.setAdapter(adaptador);
        for (int i = 0; i < categoriaList.size(); i++) {
            if (categoriaList.get(i).getId() == getIntent().getLongExtra(ProductosActivity.VALUE_IDCATEGORIA, 0)) {
                spCategorias.setSelection(i);
            }
        }
        etNombre.setText(getIntent().getStringExtra(ProductosActivity.VALUE_NOMBRE));
        if (savedInstanceState != null) {
            editando = savedInstanceState.getBoolean(SAVE_EDITANDO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_ver_producto, menu);
        mnModificar = menu.findItem(R.id.mnModificar);
        mnGuardar = menu.findItem(R.id.mnGuardar);
        if (editando) {
            etNombre.setEnabled(true);
            spCategorias.setEnabled(true);
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
                editando = true;
                etNombre.setEnabled(true);
                spCategorias.setEnabled(true);
                mnModificar.setVisible(false);
                mnGuardar.setVisible(true);
                return true;
            case R.id.mnGuardar:
                if (!etNombre.getText().toString().isEmpty()) {
                    Intent result = new Intent();
                    result.putExtra(ProductosActivity.VALUE_ID, getIntent().getLongExtra(ProductosActivity.VALUE_ID, 0));
                    result.putExtra(ProductosActivity.VALUE_NOMBRE, etNombre.getText().toString());
                    Categoria categoria = (Categoria) spCategorias.getSelectedItem();
                    result.putExtra(ProductosActivity.VALUE_IDCATEGORIA, categoria.getId());
                    setResult(ProductosActivity.RESULT_MODIFICAR_PRODUCTO, result);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
