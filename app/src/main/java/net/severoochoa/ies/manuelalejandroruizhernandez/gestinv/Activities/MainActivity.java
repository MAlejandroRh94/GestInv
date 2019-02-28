package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout clytCategorias, clytProductos, clytEnvios, clytRecepciones, clytAlmacenes, clytInformes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clytCategorias = findViewById(R.id.clytCategorias);
        clytProductos = findViewById(R.id.clytProductos);
        clytEnvios = findViewById(R.id.clytEnvios);
        clytRecepciones = findViewById(R.id.clytRecepciones);
        clytInformes = findViewById(R.id.clytInformes);
        clytAlmacenes = findViewById(R.id.clytAlmacenes);
        clytCategorias.setOnClickListener(this);
        clytProductos.setOnClickListener(this);
        clytEnvios.setOnClickListener(this);
        clytRecepciones.setOnClickListener(this);
        clytInformes.setOnClickListener(this);
        clytCategorias.setOnClickListener(this);
        clytAlmacenes.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnAjustarInventario:
                Intent intentAjInv = new Intent(this, AjustarInventarioActivity.class);
                startActivity(intentAjInv);
                return true;
            case R.id.mnAjustes:
                Intent intentAj = new Intent(this, PreferencesActivity.class);
                startActivity(intentAj);
                return true;
            case R.id.mnAcercaDe:
                Intent intentAc = new Intent(this, AcercaDeActivity.class);
                startActivity(intentAc);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.clytCategorias:
                intent = new Intent(this, CategoriasActivity.class);
                break;
            case R.id.clytProductos:
                intent = new Intent(this, ProductosActivity.class);
                break;
            case R.id.clytEnvios:
                intent = new Intent(this, TransferenciasActivity.class);
                break;
            case R.id.clytRecepciones:
                intent = new Intent(this, RecepcionesActivity.class);
                break;
            case R.id.clytAlmacenes:
                intent = new Intent(this, AlmacenesActivity.class);
                break;
            case R.id.clytInformes:
                intent = new Intent(this, InformesActivity.class);
                break;
        }
        startActivity(intent);
    }
}
