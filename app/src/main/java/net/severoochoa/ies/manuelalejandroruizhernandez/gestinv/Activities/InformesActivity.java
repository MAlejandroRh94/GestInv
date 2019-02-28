package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Almacen;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.ProductoCantidad;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Utiles.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class InformesActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btListProAlm;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informes);
        btListProAlm = findViewById(R.id.btListAlmPro);
        btListProAlm.setOnClickListener(this);
        db = DatabaseManager.getInstance(this);
    }


    private static final int PERMISSION_STORAGE = 1;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    filePdf();
                } else {
                    Toast.makeText(this, "no hay permisos", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btListAlmPro) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_STORAGE);
            } else {
                filePdf();
            }
        }

    }

    private void filePdf() {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                Calendar c = Calendar.getInstance();
                String nombre = c.getTimeInMillis() + ".pdf";
                File ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "PDFs");
                File f = new File(ruta, nombre);
                if (!ruta.exists()) {
                    ruta.mkdirs();
                }
                if (!f.exists()) {
                    f.createNewFile();
                }

                Document document = new Document();

                PdfWriter.getInstance(document, new FileOutputStream(f));

                document.open();
                document.setPageSize(PageSize.A4);
                document.addCreationDate();
                //document.addAuthor("Alejandro Ruiz");
                //document.addCreator("Alejandro Ruiz Movil");
                BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
                float mHeadingFontSize = 20.0f;
                float mValueFontSize = 26.0f;
                BaseFont urName = BaseFont.createFont();
                LineSeparator lineSeparator = new LineSeparator();
                lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
                Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
                Chunk mOrderDetailsTitleChunk = new Chunk("Stock Productos Almacen ", mOrderDetailsTitleFont);
                Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
                mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
                document.add(mOrderDetailsTitleParagraph);

                Font mOrderIdFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);

                Chunk mOrderIdChunkFe = new Chunk("Fecha:" + Util.calendarToNumericString(c) + " Hora: " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE), mOrderIdFont);
                Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunkFe);
                document.add(mOrderIdParagraph);


                db.openDB();
                List<Almacen> almacenList = db.getAlmacenes();
                for (int i = 0; i < almacenList.size(); i++) {
                    Chunk mOrderIdChunk = new Chunk("Almacen:" + almacenList.get(i).getNombre(), mOrderIdFont);
                    mOrderIdParagraph = new Paragraph(mOrderIdChunk);
                    document.add(mOrderIdParagraph);
                    List<ProductoCantidad> productoCantidadList = db.getProductosStockFromAlmacen(almacenList.get(i).getId());
                    for (int j = 0; j < productoCantidadList.size(); j++) {
                        Producto producto = db.getProducto(productoCantidadList.get(j).getIdProducto());
                        if (producto != null) {
                            document.add(new Paragraph("Producto:" + producto.getNombre() + " Cantidad:" + productoCantidadList.get(j).getCantidadProducto()));
                        }
                    }
                    document.add(new Chunk(lineSeparator));

                }
                db.closeDB();


                document.close();


            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
