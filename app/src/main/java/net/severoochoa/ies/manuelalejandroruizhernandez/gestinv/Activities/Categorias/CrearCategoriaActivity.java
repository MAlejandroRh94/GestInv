package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.Categorias;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities.CategoriasActivity;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CrearCategoriaActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String SAVE_DESCRIPCION = "descripcion";
    private static final String SAVE_NOMBRE = "nombre";
    private static final String SAVE_RUTA_IMAGEN = "ruta_imagen";
    private static final String SAVE_UNIDAD_MEDIDA = "unidad_medida";
    private EditText etNombre, etDescripcion, etUnidadMedida;
    private ImageView ivImagen;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String rutaImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_categoria);
        etNombre = findViewById(R.id.etNombre);
        ivImagen = findViewById(R.id.ivImagen);
        etDescripcion = findViewById(R.id.etDescripcion);
        etUnidadMedida = findViewById(R.id.etUnidadMedida);
        ivImagen.setOnClickListener(this);
        if (savedInstanceState != null) {
            etNombre.setText(savedInstanceState.getString(SAVE_NOMBRE));
            etDescripcion.setText(savedInstanceState.getString(SAVE_DESCRIPCION));
            rutaImagen = savedInstanceState.getString(SAVE_RUTA_IMAGEN);
            etUnidadMedida.setText(savedInstanceState.getString(SAVE_UNIDAD_MEDIDA));
        }
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            Picasso.get()
                    .load("file://" + rutaImagen)
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_no_image)
                    .fit()
                    .centerCrop()
                    .into(ivImagen);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_RUTA_IMAGEN, rutaImagen);
        outState.putString(SAVE_NOMBRE, etNombre.getText().toString());
        outState.putString(SAVE_DESCRIPCION, etDescripcion.getText().toString());
        outState.putString(SAVE_UNIDAD_MEDIDA, etUnidadMedida.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_crear_categoria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnGuardar:
                if (!etNombre.getText().toString().isEmpty() && !etUnidadMedida.getText().toString().isEmpty()) {
                    Intent result = new Intent();
                    result.putExtra(CategoriasActivity.VALUE_NOMBRE, etNombre.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    result.putExtra(CategoriasActivity.VALUE_FECHA_ALTA, calendar.getTimeInMillis());
                    if (rutaImagen == null) rutaImagen = "";
                    result.putExtra(CategoriasActivity.VALUE_IMAGEN, rutaImagen);
                    result.putExtra(CategoriasActivity.VALUE_DESCRIPCION, etDescripcion.getText().toString());
                    result.putExtra(CategoriasActivity.VALUE_UNIDADMEDIDA, etUnidadMedida.getText().toString());
                    setResult(RESULT_OK, result);
                    finish();
                }else Toast.makeText(this, "Rellene los campos vacios ", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImagen:
                dispatchTakePictureIntent();
                break;
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Picasso.get()
                    .load("file://" + rutaImagen)
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_no_image)
                    .fit()
                    .centerCrop()
                    .into(ivImagen);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error al generar la imagen", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        rutaImagen = image.getAbsolutePath();
        return image;
    }


}
