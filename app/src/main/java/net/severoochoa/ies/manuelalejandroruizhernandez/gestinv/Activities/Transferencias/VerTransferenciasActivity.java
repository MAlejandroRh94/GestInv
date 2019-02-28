package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Transferencias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.RecepcionesActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.TransferenciasActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Almacen;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Estado;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Utiles.Util;

import java.util.Calendar;
import java.util.List;


public class VerTransferenciasActivity extends AppCompatActivity {
    private static final String SAVE_EDITANDO = "EDITANDO";

    private TextView tvProducto, tvAlmacen, tvFecha, tvCant;
    private Spinner spEstado;
    private DatabaseManager db;
    private MenuItem mnModificar, mnGuardar;
    private boolean editando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_transferencias);
        db = DatabaseManager.getInstance(this);
        spEstado = findViewById(R.id.spEstado);
        tvProducto = findViewById(R.id.tvProducto);
        tvCant = findViewById(R.id.tvCant);
        tvAlmacen = findViewById(R.id.tvAlmacen);
        tvFecha = findViewById(R.id.tvFecha);

        String txt = "";
        db.openDB();
        Producto producto = db.getProducto(getIntent().getLongExtra(TransferenciasActivity.VALUE_IDPRODUCTO, 0));
        if (producto != null) {
            tvProducto.setText(producto.getNombre());
            Categoria categoria = db.getCategoria(producto.getIdCategoria());
            if (categoria != null) {
                txt = categoria.getUnidadMedida();
            }
        }
        Almacen almacen = db.getAlmacen(getIntent().getLongExtra(TransferenciasActivity.VALUE_IDALMACEN, 0));
        if (almacen != null) {
            tvAlmacen.setText(almacen.getNombre());
        }
        List<Estado> estadoList = db.getEstados();
        db.closeDB();
        ArrayAdapter<Estado> adaptador =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, estadoList);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getIntent().getLongExtra(TransferenciasActivity.VALUE_FECHA, 0));
        tvFecha.setText(Util.calendarToNumericString(calendar));

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(adaptador);

        tvCant.setText(getIntent().getIntExtra(TransferenciasActivity.VALUE_CANTIDAD, 0) + txt);
        spEstado.setEnabled(false);

        for (int i = 0; i < estadoList.size(); i++) {
            if (estadoList.get(i).getId() == getIntent().getLongExtra(TransferenciasActivity.VALUE_IDESTADO, 0)) {
                spEstado.setSelection(i);
            }
        }
        if (savedInstanceState != null) {
            editando = savedInstanceState.getBoolean(SAVE_EDITANDO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_ver_transferencia, menu);
        mnModificar = menu.findItem(R.id.mnModificar);
        mnGuardar = menu.findItem(R.id.mnGuardar);
        if (getIntent().getBooleanExtra(RecepcionesActivity.VALUE_EDITABLE, false)) {
            if (editando) {
                spEstado.setEnabled(true);
                mnModificar.setVisible(false);
                mnGuardar.setVisible(true);
            } else
                mnGuardar.setVisible(false);
        } else {
            mnModificar.setVisible(false);
            mnGuardar.setVisible(false);
        }
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
                spEstado.setEnabled(true);
                mnModificar.setVisible(false);
                mnGuardar.setVisible(true);
                editando = true;

                return true;
            case R.id.mnGuardar:
                Intent result = new Intent();
                result.putExtra(TransferenciasActivity.VALUE_ID, getIntent().getLongExtra(TransferenciasActivity.VALUE_ID, 0));
                result.putExtra(TransferenciasActivity.VALUE_IDPRODUCTO, getIntent().getLongExtra(TransferenciasActivity.VALUE_IDPRODUCTO, 0));
                Estado estado = (Estado) spEstado.getSelectedItem();
                result.putExtra(TransferenciasActivity.VALUE_IDESTADO, estado.getId());

                result.putExtra(TransferenciasActivity.VALUE_FECHA, getIntent().getLongExtra(TransferenciasActivity.VALUE_FECHA, 0));
                result.putExtra(TransferenciasActivity.VALUE_IDALMACEN, getIntent().getLongExtra(TransferenciasActivity.VALUE_IDALMACEN, 0));
                result.putExtra(TransferenciasActivity.VALUE_CANTIDAD, getIntent().getIntExtra(TransferenciasActivity.VALUE_CANTIDAD, 0));

                setResult(TransferenciasActivity.RESULT_CODE_MODIFICAR_TRANSFERENCIA, result);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
