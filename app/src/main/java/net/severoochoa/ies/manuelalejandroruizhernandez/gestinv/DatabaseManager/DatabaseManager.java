package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Almacen;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Atributo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Estado;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Pasillo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.ProductoCantidad;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Recepcion;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Seccion;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.StockMinimo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Transferencia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class DatabaseManager {

    //CREACION Y ELIMINACION

    private static final String SQL_CREATE_CATEGORIA =
            "CREATE TABLE " + CategoriaContract.TABLE_NAME + " ( " +
                    CategoriaContract._ID + " INTEGER PRIMARY KEY , " +
                    CategoriaContract.COLUMN_NOMBRE + " TEXT NOT NULL , " +
                    CategoriaContract.COLUMN_FECHAALTA + " INTEGER DEFAULT 0 , " +
                    CategoriaContract.COLUMN_IMAGEN + " TEXT , " +
                    CategoriaContract.COLUMN_DESCRIPCION + " TEXT ," +
                    CategoriaContract.COLUMN_UNIDADMEDIDA + " TEXT " +
                    ")";
    private static final String SQL_DELETE_CATEGORIA =
            "DROP TABLE IF EXISTS " + CategoriaContract.TABLE_NAME;

    private static final String SQL_CREATE_ATRIBUTO =
            "CREATE TABLE " + AtributoContract.TABLE_NAME + " ( " +
                    AtributoContract._ID + " INTEGER PRIMARY KEY , " +
                    AtributoContract.COLUMN_VALOR + " TEXT NOT NULL " +
                    ")";
    private static final String SQL_DELETE_ATRIBUTO =
            "DROP TABLE IF EXISTS " + AtributoContract.TABLE_NAME;

    private static final String SQL_CREATE_CATEGORIA_ATRIBUTO =
            "CREATE TABLE " + Categoria_AtributoContract.TABLE_NAME + " ( " +
                    Categoria_AtributoContract._ID + " INTEGER PRIMARY KEY , " +
                    Categoria_AtributoContract.COLUMN_IDCATEGORIA + " INTEGER DEFAULT 0 , " +
                    Categoria_AtributoContract.COLUMN_IDATRIBUTO + " INTEGER DEFAULT 0 , " +
                    " FOREIGN KEY (" + Categoria_AtributoContract.COLUMN_IDCATEGORIA + ") REFERENCES " + CategoriaContract.TABLE_NAME + "(" + CategoriaContract._ID + ") ON DELETE CASCADE  ," +
                    " FOREIGN KEY (" + Categoria_AtributoContract.COLUMN_IDATRIBUTO + ") REFERENCES " + AtributoContract.TABLE_NAME + "(" + AtributoContract._ID + ") ON DELETE CASCADE " +
                    ")";
    private static final String SQL_DELETE_CATEGORIA_ATRIBUTO =
            "DROP TABLE IF EXISTS " + Categoria_AtributoContract.TABLE_NAME;

    private static final String SQL_CREATE_PRODUCTO =
            "CREATE TABLE " + ProductoContract.TABLE_NAME + " (" +
                    ProductoContract._ID + " INTEGER PRIMARY KEY , " +
                    ProductoContract.COLUMN_NOMBRE + " TEXT NOT NULL , " +
                    ProductoContract.COLUMN_IDCATEGORIA + " INTEGER DEFAULT 0 , " +
                    " FOREIGN KEY (" + ProductoContract.COLUMN_IDCATEGORIA + ") REFERENCES " + CategoriaContract.TABLE_NAME + "(" + CategoriaContract._ID + ") ON DELETE CASCADE" +
                    ")";
    private static final String SQL_DELETE_PRODUCTO =
            "DROP TABLE IF EXISTS " + ProductoContract.TABLE_NAME;

    private static final String SQL_CREATE_ALMACEN =
            "CREATE TABLE " + AlmacenContract.TABLE_NAME + " (" +
                    AlmacenContract._ID + " INTEGER PRIMARY KEY , " +
                    AlmacenContract.COLUMN_NOMBRE + " TEXT NOT NULL , " +
                    AlmacenContract.COLUMN_DIRECCION + " TEXT " +
                    ")";

    private static final String SQL_DELETE_ALMACEN =
            "DROP TABLE IF EXISTS " + AlmacenContract.TABLE_NAME;

    private static final String SQL_CREATE_SECCION =
            "CREATE TABLE " + SeccionContract.TABLE_NAME + " (" +
                    SeccionContract._ID + " INTEGER PRIMARY KEY , " +
                    SeccionContract.COLUMN_IDALMACEN + " INTEGER DEFAULT 0 , " +
                    SeccionContract.COLUMN_NOMBRE + " TEXT NOT NULL , " +
                    "FOREIGN KEY (" + SeccionContract.COLUMN_IDALMACEN + ") REFERENCES " + AlmacenContract.TABLE_NAME + "(" + AlmacenContract._ID + ") ON DELETE CASCADE " +
                    ")";

    private static final String SQL_DELETE_SECCION =
            "DROP TABLE IF EXISTS " + SeccionContract.TABLE_NAME;

    private static final String SQL_CREATE_PASILLO =
            "CREATE TABLE " + PasilloContract.TABLE_NAME + " (" +
                    PasilloContract._ID + " INTEGER PRIMARY KEY , " +
                    PasilloContract.COLUMN_IDALMACEN + " INTEGER DEFAULT 0 , " +
                    PasilloContract.COLUMN_IDSECCION + " INTEGER DEFAULT 0 , " +
                    PasilloContract.COLUMN_IDPRODUCTO + " INTEGER DEFAULT NULL , " +
                    PasilloContract.COLUMN_NOMBRE + " TEXT NOT NULL , " +
                    PasilloContract.COLUMN_CANTIDAD + " INTEGER DEFAULT 0 , " +
                    "FOREIGN KEY (" + PasilloContract.COLUMN_IDALMACEN + ") REFERENCES " + AlmacenContract.TABLE_NAME + "(" + AlmacenContract._ID + ") ON DELETE CASCADE , " +
                    "FOREIGN KEY (" + PasilloContract.COLUMN_IDSECCION + ") REFERENCES " + SeccionContract.TABLE_NAME + "(" + SeccionContract._ID + ") ON DELETE CASCADE , " +
                    "FOREIGN KEY (" + PasilloContract.COLUMN_IDPRODUCTO + ") REFERENCES " + ProductoContract.TABLE_NAME + "(" + ProductoContract._ID + ") ON DELETE SET DEFAULT "
                    + ")";

    private static final String SQL_DELETE_PASILLO =
            "DROP TABLE IF EXISTS " + PasilloContract.TABLE_NAME;

    private static final String SQL_CREATE_TRANSFERENCIA =
            "CREATE TABLE " + TransferenciaContract.TABLE_NAME + " (" +
                    TransferenciaContract._ID + " INTEGER PRIMARY KEY , " +
                    TransferenciaContract.COLUMN_IDALMACEN + " INTEGER DEFAULT 0 , " +
                    TransferenciaContract.COLUMN_IDPRODUCTO + " INTEGER DEFAULT 0 , " +
                    TransferenciaContract.COLUMN_IDESTADO + " INTEGER DEFAULT 0 , " +
                    TransferenciaContract.COLUMN_CANTIDAD + " INTEGER DEFAULT 0, " +
                    TransferenciaContract.COLUMN_FECHA + " INTEGER DEFAULT 0, " +
                    "FOREIGN KEY (" + TransferenciaContract.COLUMN_IDALMACEN + ") REFERENCES " + AlmacenContract.TABLE_NAME + "(" + AlmacenContract._ID + ") ON DELETE CASCADE , " +
                    "FOREIGN KEY (" + TransferenciaContract.COLUMN_IDPRODUCTO + ") REFERENCES " + ProductoContract.TABLE_NAME + "(" + ProductoContract._ID + ") ON DELETE CASCADE , " +
                    "FOREIGN KEY (" + TransferenciaContract.COLUMN_IDESTADO + ") REFERENCES " + EstadoContract.TABLE_NAME + "(" + EstadoContract._ID + ") ON DELETE SET DEFAULT "
                    + ")";

    private static final String SQL_DELETE_TRANSFERENCIA =
            "DROP TABLE IF EXISTS " + TransferenciaContract.TABLE_NAME;

    private static final String SQL_CREATE_RECEPCION =
            "CREATE TABLE " + RecepcionContract.TABLE_NAME + " (" +
                    RecepcionContract._ID + " INTEGER PRIMARY KEY , " +
                    RecepcionContract.COLUMN_IDALMACEN + " INTEGER DEFAULT 0 , " +
                    RecepcionContract.COLUMN_IDPRODUCTO + " INTEGER DEFAULT 0 , " +
                    RecepcionContract.COLUMN_IDESTADO + " INTEGER DEFAULT 0 , " +
                    RecepcionContract.COLUMN_CANTIDAD + " INTEGER DEFAULT 0 , " +
                    RecepcionContract.COLUMN_FECHA + " INTEGER DEFAULT 0 , " +
                    "FOREIGN KEY (" + RecepcionContract.COLUMN_IDALMACEN + ") REFERENCES " + AlmacenContract.TABLE_NAME + "(" + AlmacenContract._ID + ") ON DELETE CASCADE , " +
                    "FOREIGN KEY (" + RecepcionContract.COLUMN_IDPRODUCTO + ") REFERENCES " + ProductoContract.TABLE_NAME + "(" + ProductoContract._ID + ") ON DELETE CASCADE , " +
                    "FOREIGN KEY (" + RecepcionContract.COLUMN_IDESTADO + ") REFERENCES " + EstadoContract.TABLE_NAME + "(" + EstadoContract._ID + ") ON DELETE SET DEFAULT "
                    + ")";

    private static final String SQL_DELETE_RECEPCION =
            "DROP TABLE IF EXISTS " + RecepcionContract.TABLE_NAME;

    private static final String SQL_CREATE_ESTADO =
            "CREATE TABLE " + EstadoContract.TABLE_NAME + " (" +
                    EstadoContract._ID + " INTEGER PRIMARY KEY , " +
                    EstadoContract.COLUMN_NOMBRE + " TEXT NOT NULL " +
                    ")";

    private static final String SQL_DELETE_ESTADO =
            "DROP TABLE IF EXISTS " + EstadoContract.TABLE_NAME;

    private static final String SQL_CREATE_STOCK_MINIMO =
            "CREATE TABLE " + Stock_MinimoContract.TABLE_NAME + " (" +
                    Stock_MinimoContract._ID + " INTEGER PRIMARY KEY , " +
                    Stock_MinimoContract.COLUMN_IDALMACEN + " INTEGER DEFAULT 0 , " +
                    Stock_MinimoContract.COLUMN_IDPRODUCTO + " INTEGER DEFAULT 0 , " +
                    Stock_MinimoContract.COLUMN_CANTIDAD_MINIMA + " INTEGER DEFAULT 0 ," +
                    "FOREIGN KEY (" + Stock_MinimoContract.COLUMN_IDALMACEN + ") REFERENCES " + AlmacenContract.TABLE_NAME + "(" + AlmacenContract._ID + ") ON DELETE CASCADE , " +
                    "FOREIGN KEY (" + Stock_MinimoContract.COLUMN_IDPRODUCTO + ") REFERENCES " + ProductoContract.TABLE_NAME + "(" + ProductoContract._ID + ") ON DELETE CASCADE " +
                    ")";

    private static final String SQL_DELETE_STOCK_MINIMO =
            "DROP TABLE IF EXISTS " + Stock_MinimoContract.TABLE_NAME;

    //BASE DE DATOS

    private static DatabaseManager instance;
    private static GestionInventarioDBHelper mDatabaseHelper;
    private int dbCounter;
    private SQLiteDatabase db;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager();
            //mDatabaseHelper = helper;
            mDatabaseHelper = new GestionInventarioDBHelper(context);
        }
        return instance;
    }

    public synchronized void openDB() {
        dbCounter++;
        if (dbCounter == 1) {
            db = mDatabaseHelper.getWritableDatabase();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                String query = "PRAGMA foreign_keys = ON";
                db.execSQL(query);
            }
        }
    }

    public synchronized void closeDB() {
        dbCounter--;
        if (dbCounter == 0) {
            db.close();
        }
    }

    public boolean isOpen() {
        return dbCounter > 0;
    }

    public synchronized void beginTransaction() {
        db.beginTransaction();
    }

    public synchronized void setTransactionSuccesful() {
        db.setTransactionSuccessful();
    }

    public synchronized void endTransaction() {
        db.endTransaction();
    }

    //CONSULTAS
    public void eliminarDB() {

        db.execSQL(SQL_DELETE_TRANSFERENCIA);
        db.execSQL(SQL_DELETE_RECEPCION);

        db.execSQL(SQL_DELETE_CATEGORIA_ATRIBUTO);
        db.execSQL(SQL_DELETE_ATRIBUTO);
        db.execSQL(SQL_DELETE_CATEGORIA);
        db.execSQL(SQL_DELETE_PRODUCTO);

        db.execSQL(SQL_DELETE_STOCK_MINIMO);
        db.execSQL(SQL_DELETE_PASILLO);
        db.execSQL(SQL_DELETE_SECCION);
        db.execSQL(SQL_DELETE_ALMACEN);

        db.execSQL(SQL_DELETE_ESTADO);

        db.execSQL(SQL_CREATE_CATEGORIA);
        db.execSQL(SQL_CREATE_PRODUCTO);
        db.execSQL(SQL_CREATE_ATRIBUTO);
        db.execSQL(SQL_CREATE_CATEGORIA_ATRIBUTO);

        db.execSQL(SQL_CREATE_ALMACEN);
        db.execSQL(SQL_CREATE_SECCION);
        db.execSQL(SQL_CREATE_PASILLO);
        db.execSQL(SQL_CREATE_STOCK_MINIMO);

        db.execSQL(SQL_CREATE_TRANSFERENCIA);
        db.execSQL(SQL_CREATE_RECEPCION);
        db.execSQL(SQL_CREATE_ESTADO);

        ContentValues values = new ContentValues();
        values.put(EstadoContract.COLUMN_NOMBRE, "Solicitud");
        db.insert(EstadoContract.TABLE_NAME, null, values);
        values.clear();
        values.put(EstadoContract.COLUMN_NOMBRE, "Preparacion");
        db.insert(EstadoContract.TABLE_NAME, null, values);
        values.clear();
        values.put(EstadoContract.COLUMN_NOMBRE, "Tramitado");
        db.insert(EstadoContract.TABLE_NAME, null, values);
    }

    public boolean cargarDatosPrueba() {
        try {

            ContentValues values = new ContentValues();
            Calendar calendar = Calendar.getInstance();

            values.put(CategoriaContract._ID, 101);
            values.put(CategoriaContract.COLUMN_NOMBRE, "Nombre Categoria 1");
            values.put(CategoriaContract.COLUMN_FECHAALTA, calendar.getTimeInMillis());
            values.put(CategoriaContract.COLUMN_IMAGEN, "Imagen Categoria 1");
            values.put(CategoriaContract.COLUMN_DESCRIPCION, "Decripcion Categoria 1");
            values.put(CategoriaContract.COLUMN_UNIDADMEDIDA, "Unidad de Medida Categoria 1");
            db.insert(CategoriaContract.TABLE_NAME, null, values);

            values.clear();
            values.put(CategoriaContract._ID, 102);
            values.put(CategoriaContract.COLUMN_NOMBRE, "Nombre Categoria 2");
            values.put(CategoriaContract.COLUMN_FECHAALTA, calendar.getTimeInMillis());
            values.put(CategoriaContract.COLUMN_IMAGEN, "Imagen Categoria 2");
            values.put(CategoriaContract.COLUMN_DESCRIPCION, "Decripcion Categoria 2");
            values.put(CategoriaContract.COLUMN_UNIDADMEDIDA, "Unidad de Medida Categoria 2");
            db.insert(CategoriaContract.TABLE_NAME, null, values);

            values.clear();
            values.put(AtributoContract._ID, 201);
            values.put(AtributoContract.COLUMN_VALOR, "Atributo 1 en Cat 1");
            db.insert(AtributoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(AtributoContract._ID, 202);
            values.put(AtributoContract.COLUMN_VALOR, "Atributo 2 en Cat 1");
            db.insert(AtributoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(AtributoContract._ID, 203);
            values.put(AtributoContract.COLUMN_VALOR, "Atributo 3 en Cat 1 y 2 ");
            db.insert(AtributoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(AtributoContract._ID, 204);
            values.put(AtributoContract.COLUMN_VALOR, "Atributo 4 en Cat 2");
            db.insert(AtributoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(Categoria_AtributoContract._ID, 301);
            values.put(Categoria_AtributoContract.COLUMN_IDCATEGORIA, 101);
            values.put(Categoria_AtributoContract.COLUMN_IDATRIBUTO, 201);
            db.insert(Categoria_AtributoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(Categoria_AtributoContract._ID, 302);
            values.put(Categoria_AtributoContract.COLUMN_IDCATEGORIA, 101);
            values.put(Categoria_AtributoContract.COLUMN_IDATRIBUTO, 202);
            db.insert(Categoria_AtributoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(Categoria_AtributoContract._ID, 303);
            values.put(Categoria_AtributoContract.COLUMN_IDCATEGORIA, 101);
            values.put(Categoria_AtributoContract.COLUMN_IDATRIBUTO, 203);
            db.insert(Categoria_AtributoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(Categoria_AtributoContract._ID, 304);
            values.put(Categoria_AtributoContract.COLUMN_IDCATEGORIA, 102);
            values.put(Categoria_AtributoContract.COLUMN_IDATRIBUTO, 203);
            db.insert(Categoria_AtributoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(Categoria_AtributoContract._ID, 305);
            values.put(Categoria_AtributoContract.COLUMN_IDCATEGORIA, 102);
            values.put(Categoria_AtributoContract.COLUMN_IDATRIBUTO, 204);
            db.insert(Categoria_AtributoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(ProductoContract._ID, 401);
            values.put(ProductoContract.COLUMN_NOMBRE, "Producto 1 Cat 2");
            values.put(ProductoContract.COLUMN_IDCATEGORIA, 102);
            db.insert(ProductoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(ProductoContract._ID, 402);
            values.put(ProductoContract.COLUMN_NOMBRE, "Producto 1 Cat 1");
            values.put(ProductoContract.COLUMN_IDCATEGORIA, 101);
            db.insert(ProductoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(ProductoContract._ID, 403);
            values.put(ProductoContract.COLUMN_NOMBRE, "Producto 2 Cat 1");
            values.put(ProductoContract.COLUMN_IDCATEGORIA, 101);
            db.insert(ProductoContract.TABLE_NAME, null, values);

            values.clear();
            values.put(AlmacenContract._ID, 501);
            values.put(AlmacenContract.COLUMN_NOMBRE, "Almacen 1");
            values.put(AlmacenContract.COLUMN_DIRECCION, "Direccion 1");
            db.insert(AlmacenContract.TABLE_NAME, null, values);

            values.clear();
            values.put(AlmacenContract._ID, 502);
            values.put(AlmacenContract.COLUMN_NOMBRE, "Almacen 2");
            values.put(AlmacenContract.COLUMN_DIRECCION, "Direccion 2");
            db.insert(AlmacenContract.TABLE_NAME, null, values);

            values.clear();
            values.put(SeccionContract._ID, 601);
            values.put(SeccionContract.COLUMN_NOMBRE, "Seccion 1 en Almacen 1");
            values.put(SeccionContract.COLUMN_IDALMACEN, 501);
            db.insert(SeccionContract.TABLE_NAME, null, values);

            values.clear();
            values.put(SeccionContract._ID, 602);
            values.put(SeccionContract.COLUMN_NOMBRE, "Seccion 2 en Almacen 1");
            values.put(SeccionContract.COLUMN_IDALMACEN, 501);
            db.insert(SeccionContract.TABLE_NAME, null, values);

            values.clear();
            values.put(SeccionContract._ID, 603);
            values.put(SeccionContract.COLUMN_NOMBRE, "Seccion 1 en Almacen 2");
            values.put(SeccionContract.COLUMN_IDALMACEN, 502);
            db.insert(SeccionContract.TABLE_NAME, null, values);

            values.clear();
            values.put(PasilloContract._ID, 701);
            values.put(PasilloContract.COLUMN_IDALMACEN, 501);
            values.put(PasilloContract.COLUMN_IDSECCION, 601);
            values.putNull(PasilloContract.COLUMN_IDPRODUCTO);
            values.put(PasilloContract.COLUMN_NOMBRE, "Pasillo 1 Seccion 1 Almacen 1");
            values.put(PasilloContract.COLUMN_CANTIDAD, 0);
            db.insert(PasilloContract.TABLE_NAME, null, values);

            values.clear();
            values.put(PasilloContract._ID, 702);
            values.put(PasilloContract.COLUMN_IDALMACEN, 501);
            values.put(PasilloContract.COLUMN_IDSECCION, 602);
            values.putNull(PasilloContract.COLUMN_IDPRODUCTO);
            values.put(PasilloContract.COLUMN_NOMBRE, "Pasillo 1 Seccion 2 Almacen 1");
            values.put(PasilloContract.COLUMN_CANTIDAD, 0);
            db.insert(PasilloContract.TABLE_NAME, null, values);

            values.clear();
            values.put(PasilloContract._ID, 703);
            values.put(PasilloContract.COLUMN_IDALMACEN, 501);
            values.put(PasilloContract.COLUMN_IDSECCION, 602);
            values.putNull(PasilloContract.COLUMN_IDPRODUCTO);
            values.put(PasilloContract.COLUMN_NOMBRE, "Pasillo 2 Seccion 2 Almacen 1");
            values.put(PasilloContract.COLUMN_CANTIDAD, 0);
            db.insert(PasilloContract.TABLE_NAME, null, values);

            values.clear();
            values.put(PasilloContract._ID, 704);
            values.put(PasilloContract.COLUMN_IDALMACEN, 502);
            values.put(PasilloContract.COLUMN_IDSECCION, 603);
            values.putNull(PasilloContract.COLUMN_IDPRODUCTO);
            values.put(PasilloContract.COLUMN_NOMBRE, "Pasillo 1 Seccion 1 Almacen 2");
            values.put(PasilloContract.COLUMN_CANTIDAD, 0);
            db.insert(PasilloContract.TABLE_NAME, null, values);


        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public long insertCategoria(Categoria categoria) {
        ContentValues values = new ContentValues();
        values.put(CategoriaContract.COLUMN_NOMBRE, categoria.getNombre());
        values.put(CategoriaContract.COLUMN_FECHAALTA, categoria.getFechaAlta());
        values.put(CategoriaContract.COLUMN_IMAGEN, categoria.getImagen());
        values.put(CategoriaContract.COLUMN_DESCRIPCION, categoria.getDescripcion());
        values.put(CategoriaContract.COLUMN_UNIDADMEDIDA, categoria.getUnidadMedida());
        long newRowId = db.insert(CategoriaContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public List<Categoria> getCategorias() {
        List<Categoria> listCategoria = new ArrayList<>();
        Cursor cursor = db.query(
                CategoriaContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        Categoria categoria = null;
        if (cursor.moveToFirst()) {
            do {
                long categoriaId = cursor.getLong(cursor.getColumnIndexOrThrow(CategoriaContract._ID));
                String categoriaNombre = cursor.getString(cursor.getColumnIndexOrThrow(CategoriaContract.COLUMN_NOMBRE));
                long categoriaFecha = cursor.getLong(cursor.getColumnIndexOrThrow(CategoriaContract.COLUMN_FECHAALTA));
                String categoriaImagen = cursor.getString(cursor.getColumnIndexOrThrow(CategoriaContract.COLUMN_IMAGEN));
                String categoriaDescripcion = cursor.getString(cursor.getColumnIndexOrThrow(CategoriaContract.COLUMN_DESCRIPCION));
                String categoriaUnidadMedida = cursor.getString(cursor.getColumnIndexOrThrow(CategoriaContract.COLUMN_UNIDADMEDIDA));
                categoria = new Categoria(categoriaId, categoriaNombre, categoriaFecha, categoriaImagen, categoriaDescripcion, categoriaUnidadMedida);
                listCategoria.add(categoria);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listCategoria;
    }

    public int updateCategoria(Categoria categoria) {
        ContentValues values = new ContentValues();
        values.put(CategoriaContract.COLUMN_NOMBRE, categoria.getNombre());
        values.put(CategoriaContract.COLUMN_FECHAALTA, categoria.getFechaAlta());
        values.put(CategoriaContract.COLUMN_IMAGEN, categoria.getImagen());
        values.put(CategoriaContract.COLUMN_DESCRIPCION, categoria.getDescripcion());
        values.put(CategoriaContract.COLUMN_UNIDADMEDIDA, categoria.getUnidadMedida());
        String where = CategoriaContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(categoria.getId())};
        int affectedRows = db.update(
                CategoriaContract.TABLE_NAME,
                values,
                where,
                whereArgs);
        return affectedRows;
    }

    public int deleteCategoria(long id) {
        String where = CategoriaContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        int deletedRows = db.delete(CategoriaContract.TABLE_NAME, where, whereArgs);
        return deletedRows;
    }

    public long insertCatAtr(Categoria categoria, Atributo atributo) {
        ContentValues values = new ContentValues();
        values.put(Categoria_AtributoContract.COLUMN_IDCATEGORIA, categoria.getId());
        values.put(Categoria_AtributoContract.COLUMN_IDATRIBUTO, atributo.getId());
        long newRowId = db.insert(Categoria_AtributoContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public List<Atributo> getAtrFromCat(long id) {
        List<Atributo> listaAtributo = new ArrayList<>();
        String[] whereArgs = new String[]{String.valueOf(id)};
        String where = Categoria_AtributoContract.COLUMN_IDCATEGORIA + " = ?";
        Cursor cursor = db.query(Categoria_AtributoContract.TABLE_NAME, null, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long idAtr = cursor.getLong(cursor.getColumnIndexOrThrow(Categoria_AtributoContract.COLUMN_IDATRIBUTO));

                Atributo atributo = null;
                String[] whereArgsAtr = new String[]{String.valueOf(idAtr)};
                String whereAtr = AtributoContract._ID + " = ? ";
                Cursor cursorAtr = db.query(AtributoContract.TABLE_NAME, null, whereAtr, whereArgsAtr, null, null, null);
                if (cursorAtr.moveToFirst()) {
                    idAtr = cursorAtr.getLong(cursorAtr.getColumnIndexOrThrow(AtributoContract._ID));
                    String valorAtr = cursorAtr.getString(cursorAtr.getColumnIndexOrThrow(AtributoContract.COLUMN_VALOR));
                    atributo = new Atributo(idAtr, valorAtr);
                }
                cursorAtr.close();

                listaAtributo.add(atributo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaAtributo;
    }

    public long insertAtributo(Atributo atributo) {
        ContentValues values = new ContentValues();
        values.put(AtributoContract.COLUMN_VALOR, atributo.getValor());
        long newRowId = db.insert(AtributoContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public int deleteAtrFromCat(long idcat, long idatr) {
        String where = Categoria_AtributoContract.COLUMN_IDCATEGORIA + " = ? AND " + Categoria_AtributoContract.COLUMN_IDATRIBUTO + " = ? ";
        String[] whereArgs = {String.valueOf(idcat), String.valueOf(idatr)};
        int deletedRows = db.delete(Categoria_AtributoContract.TABLE_NAME, where, whereArgs);
        return deletedRows;
    }

    public long insertProducto(Producto producto) {
        ContentValues values = new ContentValues();
        values.put(ProductoContract.COLUMN_NOMBRE, producto.getNombre());
        values.put(ProductoContract.COLUMN_IDCATEGORIA, producto.getIdCategoria());
        long newRowId = db.insert(ProductoContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public int deleteProducto(long id) {
        String where = ProductoContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        int deletedRows = db.delete(ProductoContract.TABLE_NAME, where, whereArgs);
        return deletedRows;
    }

    public int updateProducto(Producto producto) {
        ContentValues values = new ContentValues();
        values.put(ProductoContract.COLUMN_NOMBRE, producto.getNombre());
        values.put(ProductoContract.COLUMN_IDCATEGORIA, producto.getIdCategoria());
        String where = ProductoContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(producto.getId())};
        int affectedRows = db.update(
                ProductoContract.TABLE_NAME,
                values,
                where,
                whereArgs);
        return affectedRows;
    }

    public List<Producto> getProductos() {
        List<Producto> productoList = new ArrayList<>();
        Producto producto = null;
        Cursor cursor = db.query(ProductoContract.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(ProductoContract._ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ProductoContract.COLUMN_NOMBRE));
                long idCategoria = cursor.getLong(cursor.getColumnIndexOrThrow(ProductoContract.COLUMN_IDCATEGORIA));
                producto = new Producto(id, nombre, idCategoria);
                productoList.add(producto);
            } while (cursor.moveToNext());
        }
        return productoList;
    }

    public Producto getProducto(long id) {
        Producto producto = null;
        String where = ProductoContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(ProductoContract.TABLE_NAME, null, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            long idPro = cursor.getLong(cursor.getColumnIndexOrThrow(ProductoContract._ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ProductoContract.COLUMN_NOMBRE));
            long idCat = cursor.getLong(cursor.getColumnIndexOrThrow(ProductoContract.COLUMN_IDCATEGORIA));
            producto = new Producto(idPro, nombre, idCat);
        }
        return producto;
    }

    public Categoria getCategoria(long id) {
        Categoria categoria = null;
        String where = CategoriaContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(CategoriaContract.TABLE_NAME, null, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            long idCat = cursor.getLong(cursor.getColumnIndexOrThrow(CategoriaContract._ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(CategoriaContract.COLUMN_NOMBRE));
            long fechaAlta = cursor.getLong(cursor.getColumnIndexOrThrow(CategoriaContract.COLUMN_FECHAALTA));
            String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(CategoriaContract.COLUMN_DESCRIPCION));
            String imagen = cursor.getString(cursor.getColumnIndexOrThrow(CategoriaContract.COLUMN_IMAGEN));
            String unidadMedida = cursor.getString(cursor.getColumnIndexOrThrow(CategoriaContract.COLUMN_UNIDADMEDIDA));
            categoria = new Categoria(idCat, nombre, fechaAlta, imagen, descripcion, unidadMedida);
        }
        return categoria;
    }

    public List<Almacen> getAlmacenes() {
        List<Almacen> almacenList = new ArrayList<>();
        Almacen almacen = null;
        Cursor cursor = db.query(AlmacenContract.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(AlmacenContract._ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AlmacenContract.COLUMN_NOMBRE));
                String direccion = cursor.getString(cursor.getColumnIndexOrThrow(AlmacenContract.COLUMN_DIRECCION));
                almacen = new Almacen(id, nombre, direccion);
                almacenList.add(almacen);
            } while (cursor.moveToNext());
        }
        return almacenList;
    }

    public long insertAlmacen(Almacen almacen) {
        ContentValues values = new ContentValues();
        values.put(AlmacenContract.COLUMN_NOMBRE, almacen.getNombre());
        values.put(AlmacenContract.COLUMN_DIRECCION, almacen.getDireccion());
        long newRowId = db.insert(AlmacenContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public Almacen getAlmacen(long id) {
        Almacen almacen = null;
        String where = AlmacenContract._ID + "=?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(AlmacenContract.TABLE_NAME, null, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            long idAlm = cursor.getLong(cursor.getColumnIndexOrThrow(AlmacenContract._ID));
            String nombreAlm = cursor.getString(cursor.getColumnIndexOrThrow(AlmacenContract.COLUMN_NOMBRE));
            String direccionAlm = cursor.getString(cursor.getColumnIndexOrThrow(AlmacenContract.COLUMN_DIRECCION));
            almacen = new Almacen(idAlm, nombreAlm, direccionAlm);
        }
        return almacen;
    }

    public int deleteAlmacen(long id) {
        String where = AlmacenContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        int deletedRows = db.delete(AlmacenContract.TABLE_NAME, where, whereArgs);
        return deletedRows;
    }

    public int updateAlmacen(Almacen almacen) {
        ContentValues values = new ContentValues();
        values.put(AlmacenContract.COLUMN_NOMBRE, almacen.getNombre());
        values.put(AlmacenContract.COLUMN_DIRECCION, almacen.getDireccion());
        String where = AlmacenContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(almacen.getId())};
        int affectedRows = db.update(
                AlmacenContract.TABLE_NAME,
                values,
                where,
                whereArgs);
        return affectedRows;
    }

    public List<Seccion> getSeccionesFromAlmacen(long id) {
        List<Seccion> seccionList = new ArrayList<>();
        Seccion seccion = null;
        String where = SeccionContract.COLUMN_IDALMACEN + " =?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(SeccionContract.TABLE_NAME, null, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long idSeccion = cursor.getLong(cursor.getColumnIndexOrThrow(SeccionContract._ID));
                long idAlmacen = cursor.getLong(cursor.getColumnIndexOrThrow(SeccionContract.COLUMN_IDALMACEN));
                String nombreSeccion = cursor.getString(cursor.getColumnIndexOrThrow(SeccionContract.COLUMN_NOMBRE));
                seccion = new Seccion(idSeccion, idAlmacen, nombreSeccion);
                seccionList.add(seccion);
            } while (cursor.moveToNext());
        }
        return seccionList;
    }

    public List<Pasillo> getPasillosFromAlmacen(long id) {
        List<Pasillo> pasilloList = new ArrayList<>();
        Pasillo pasillo = null;
        String where = PasilloContract.COLUMN_IDALMACEN + " =?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(PasilloContract.TABLE_NAME, null, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long idPasillo = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract._ID));
                long almacen = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDALMACEN));
                long seccion = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDSECCION));
                long producto = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDPRODUCTO));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_NOMBRE));
                int cant = cursor.getInt(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_CANTIDAD));
                pasillo = new Pasillo(idPasillo, almacen, seccion, producto, nombre, cant);
                pasilloList.add(pasillo);
            } while (cursor.moveToNext());
        }
        return pasilloList;
    }

    public List<Pasillo> getPasillosFromSeccion(long id) {
        List<Pasillo> pasilloList = new ArrayList<>();
        Pasillo pasillo = null;
        String where = PasilloContract.COLUMN_IDSECCION + " =?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(PasilloContract.TABLE_NAME, null, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long idPasillo = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract._ID));
                long almacen = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDALMACEN));
                long seccion = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDSECCION));
                long producto = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDPRODUCTO));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_NOMBRE));
                int cant = cursor.getInt(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_CANTIDAD));
                pasillo = new Pasillo(idPasillo, almacen, seccion, producto, nombre, cant);
                pasilloList.add(pasillo);
            } while (cursor.moveToNext());
        }
        return pasilloList;
    }

    public long insertSeccionInAlmacen(long idAlm, Seccion seccion) {
        ContentValues values = new ContentValues();
        values.put(SeccionContract.COLUMN_NOMBRE, seccion.getNombre());
        values.put(SeccionContract.COLUMN_IDALMACEN, idAlm);
        long newRowId = db.insert(SeccionContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public int deleteSeccion(long id) {
        String where = SeccionContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        int deletedRows = db.delete(SeccionContract.TABLE_NAME, where, whereArgs);
        return deletedRows;
    }

    public int deletePasillo(long id) {
        String where = PasilloContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        int deletedRows = db.delete(PasilloContract.TABLE_NAME, where, whereArgs);
        return deletedRows;
    }

    public long insertPasilloInSeccion(long idAlm, long idSec, Pasillo pasillo) {
        ContentValues values = new ContentValues();
        values.put(PasilloContract.COLUMN_IDALMACEN, idAlm);
        values.put(PasilloContract.COLUMN_IDSECCION, idSec);
        values.putNull(PasilloContract.COLUMN_IDPRODUCTO);
        values.put(PasilloContract.COLUMN_NOMBRE, pasillo.getNombre());
        values.put(PasilloContract.COLUMN_CANTIDAD, pasillo.getCantidadAlmacenada());
        long newRowId = db.insert(PasilloContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public List<StockMinimo> getStockMinimoFromAlmacen(long idAlm) {
        List<StockMinimo> stockMinimoList = new ArrayList<>();
        StockMinimo stockMinimo = null;
        String where = Stock_MinimoContract.COLUMN_IDALMACEN + "=?";
        String[] whereArgs = {String.valueOf(idAlm)};
        Cursor cursor = db.query(Stock_MinimoContract.TABLE_NAME, null, where, whereArgs, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long idSto = cursor.getLong(cursor.getColumnIndexOrThrow(Stock_MinimoContract._ID));
                long idAlmSto = cursor.getLong(cursor.getColumnIndexOrThrow(Stock_MinimoContract.COLUMN_IDALMACEN));
                long idProdSto = cursor.getLong(cursor.getColumnIndexOrThrow(Stock_MinimoContract.COLUMN_IDPRODUCTO));
                int cantSto = cursor.getInt(cursor.getColumnIndexOrThrow(Stock_MinimoContract.COLUMN_CANTIDAD_MINIMA));
                stockMinimo = new StockMinimo(idSto, idProdSto, idAlmSto, cantSto);
                stockMinimoList.add(stockMinimo);
            } while (cursor.moveToNext());
        }
        return stockMinimoList;
    }

    public StockMinimo getStockMinimoFromAlmacenAndProducto(long idAlm, long idPro) {
        StockMinimo stockMinimo = null;
        String where = Stock_MinimoContract.COLUMN_IDALMACEN + "=? AND " + Stock_MinimoContract.COLUMN_IDPRODUCTO + " =? ";
        String[] whereArgs = {String.valueOf(idAlm), String.valueOf(idPro)};
        Cursor cursor = db.query(Stock_MinimoContract.TABLE_NAME, null, where, whereArgs, null, null, null, null);
        if (cursor.moveToFirst()) {
            long idSto = cursor.getLong(cursor.getColumnIndexOrThrow(Stock_MinimoContract._ID));
            long idAlmSto = cursor.getLong(cursor.getColumnIndexOrThrow(Stock_MinimoContract.COLUMN_IDALMACEN));
            long idProdSto = cursor.getLong(cursor.getColumnIndexOrThrow(Stock_MinimoContract.COLUMN_IDPRODUCTO));
            int cantSto = cursor.getInt(cursor.getColumnIndexOrThrow(Stock_MinimoContract.COLUMN_CANTIDAD_MINIMA));
            stockMinimo = new StockMinimo(idSto, idProdSto, idAlmSto, cantSto);
        }
        return stockMinimo;
    }

    public long insertStockMinimoInAlmacen(long idAlmacen, StockMinimo stockMinimo) {
        ContentValues values = new ContentValues();
        values.put(Stock_MinimoContract.COLUMN_IDPRODUCTO, stockMinimo.getIdProd());
        values.put(Stock_MinimoContract.COLUMN_IDALMACEN, idAlmacen);
        values.put(Stock_MinimoContract.COLUMN_CANTIDAD_MINIMA, stockMinimo.getCantMin());
        long newRowId = db.insert(Stock_MinimoContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public int deleteStockMinimo(long id) {
        String where = Stock_MinimoContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        int deletedRows = db.delete(Stock_MinimoContract.TABLE_NAME, where, whereArgs);
        return deletedRows;
    }

    public String getEstado(long idEstado) {
        String estado = "";
        String where = EstadoContract._ID + "=?";
        String[] whereArgs = {String.valueOf(idEstado)};
        Cursor cursor = db.query(EstadoContract.TABLE_NAME, null, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            estado = cursor.getString(cursor.getColumnIndexOrThrow(EstadoContract.COLUMN_NOMBRE));
        }
        return estado;
    }

    public List<Estado> getEstados() {
        List<Estado> estadoList = new ArrayList<>();
        Estado estado = null;
        Cursor cursor = db.query(EstadoContract.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                long id = cursor.getLong(cursor.getColumnIndexOrThrow(EstadoContract._ID));
                String estadoSt = cursor.getString(cursor.getColumnIndexOrThrow(EstadoContract.COLUMN_NOMBRE));
                estado = new Estado(id, estadoSt);
                estadoList.add(estado);
            } while (cursor.moveToNext());
        }
        return estadoList;
    }

    //getTransferencia
    public List<Transferencia> getTransferencias() {
        List<Transferencia> transferenciaList = new ArrayList<>();
        Transferencia transferencia = null;
        Cursor cursor = db.query(TransferenciaContract.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                transferencia = new Transferencia(
                        cursor.getLong(cursor.getColumnIndexOrThrow(TransferenciaContract._ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TransferenciaContract.COLUMN_IDPRODUCTO)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TransferenciaContract.COLUMN_IDESTADO)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TransferenciaContract.COLUMN_IDALMACEN)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TransferenciaContract.COLUMN_FECHA)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TransferenciaContract.COLUMN_CANTIDAD))
                );
                transferenciaList.add(transferencia);
            } while (cursor.moveToNext());
        }
        return transferenciaList;
    }

    public long insertTransferencia(Transferencia transferencia) {
        ContentValues values = new ContentValues();
        values.put(TransferenciaContract.COLUMN_IDALMACEN, transferencia.getIdAlmacen());
        values.put(TransferenciaContract.COLUMN_IDPRODUCTO, String.valueOf(transferencia.getIdProducto()));
        values.put(TransferenciaContract.COLUMN_IDESTADO, 1);
        values.put(TransferenciaContract.COLUMN_CANTIDAD, transferencia.getCantidadTransferida());
        Calendar c = Calendar.getInstance();
        values.put(TransferenciaContract.COLUMN_FECHA, c.getTimeInMillis());
        long newRowId = db.insert(TransferenciaContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public int deleteTransferencia(long idTrans) {
        String where = TransferenciaContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(idTrans)};
        int deletedRows = db.delete(TransferenciaContract.TABLE_NAME, where, whereArgs);
        return deletedRows;
    }

    //enoughStockToTransferencia
    public List<Pasillo> enoughStockToTransferencia(long idAlm, long idProd, int cantEnv) {
        List<Pasillo> pasilloList = new ArrayList<>();
        Pasillo pasillo = null;
        String where = PasilloContract.COLUMN_IDALMACEN + "= ? AND " + PasilloContract.COLUMN_IDPRODUCTO + "=?";
        String[] whereArgs = {String.valueOf(idAlm), String.valueOf(idProd)};
        Cursor cursor = db.query(PasilloContract.TABLE_NAME, null, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int tempCantEnv = cantEnv - cursor.getInt(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_CANTIDAD));
                if (tempCantEnv > 0) {
                    //Añadir el pasillo y seguir
                    pasillo = new Pasillo(
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract._ID)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDALMACEN)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDSECCION)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDPRODUCTO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_NOMBRE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_CANTIDAD))
                    );
                    pasillo.setCantidadAlmacenada(0);
                    pasilloList.add(pasillo);
                    cantEnv = tempCantEnv;
                } else if (tempCantEnv == 0) {
                    //Añadir el pasillo y terminar
                    pasillo = new Pasillo(
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract._ID)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDALMACEN)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDSECCION)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDPRODUCTO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_NOMBRE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_CANTIDAD))
                    );
                    pasillo.setCantidadAlmacenada(tempCantEnv);
                    pasilloList.add(pasillo);
                    cantEnv = tempCantEnv;
                    break;
                } else if (tempCantEnv < 0) {
                    //Hacer el calculo a la inversa para obtener la cantidad que tenemos que sacar y se queda en el pasillo , poniendo que la cant env es 0 y asi terminar el bucle
                    pasillo = new Pasillo(
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract._ID)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDALMACEN)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDSECCION)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDPRODUCTO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_NOMBRE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_CANTIDAD))
                    );
                    int calculoInv = pasillo.getCantidadAlmacenada() - cantEnv;
                    pasillo.setCantidadAlmacenada(calculoInv);
                    pasilloList.add(pasillo);
                    cantEnv = 0;
                    break;
                }
            } while (cursor.moveToNext());
        }
        if (cantEnv > 0) {
            pasilloList = null;
        }

        return pasilloList;
    }

    //updateTransferencia
    public int updateTransferencia(Transferencia transferencia) {
        ContentValues values = new ContentValues();
        values.put(TransferenciaContract.COLUMN_IDALMACEN, transferencia.getIdAlmacen());
        values.put(TransferenciaContract.COLUMN_IDPRODUCTO, transferencia.getIdProducto());
        values.put(TransferenciaContract.COLUMN_FECHA, transferencia.getFechaCreacion());
        values.put(TransferenciaContract.COLUMN_CANTIDAD, transferencia.getCantidadTransferida());
        values.put(TransferenciaContract.COLUMN_IDESTADO, transferencia.getIdEstado());
        String where = TransferenciaContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(transferencia.getIdTransferencia())};
        int affectedRows = db.update(
                TransferenciaContract.TABLE_NAME,
                values,
                where,
                whereArgs);
        return affectedRows;
    }

    //getRecepciones , insertRecepcion , getRecepcion , deleteRecepcion

    public List<Recepcion> getRecepciones() {
        List<Recepcion> recepcionList = new ArrayList<>();
        Recepcion recepcion = null;
        Cursor cursor = db.query(RecepcionContract.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                recepcion = new Recepcion(
                        cursor.getLong(cursor.getColumnIndexOrThrow(TransferenciaContract._ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TransferenciaContract.COLUMN_IDALMACEN)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TransferenciaContract.COLUMN_IDPRODUCTO)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TransferenciaContract.COLUMN_IDESTADO)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TransferenciaContract.COLUMN_FECHA)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TransferenciaContract.COLUMN_CANTIDAD))
                );
                recepcionList.add(recepcion);

            } while (cursor.moveToNext());
        }
        return recepcionList;
    }

    public long insertRecepcion(Recepcion recepcion) {
        ContentValues values = new ContentValues();
        values.put(RecepcionContract.COLUMN_IDALMACEN, recepcion.getIdAlmacen());
        values.put(RecepcionContract.COLUMN_IDPRODUCTO, String.valueOf(recepcion.getIdProducto()));
        values.put(RecepcionContract.COLUMN_IDESTADO, 1);
        values.put(RecepcionContract.COLUMN_CANTIDAD, recepcion.getCantidadRecibida());
        Calendar c = Calendar.getInstance();
        values.put(RecepcionContract.COLUMN_FECHA, c.getTimeInMillis());
        long newRowId = db.insert(RecepcionContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public int deleteRecepcion(long idRecepcion) {
        String where = RecepcionContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(idRecepcion)};
        int deletedRows = db.delete(RecepcionContract.TABLE_NAME, where, whereArgs);
        return deletedRows;
    }


    //enoughSpaceToRecepcion
    public Pasillo enoughSpaceToRecepcion(long idAlmacen, long idProducto, int cantidad) {
        Pasillo pasillo = null;
        String where = PasilloContract.COLUMN_IDALMACEN + " = ? AND " +
                "(" + PasilloContract.COLUMN_IDPRODUCTO + " = ? OR " + PasilloContract.COLUMN_IDPRODUCTO + " = NULL OR " + PasilloContract.COLUMN_CANTIDAD + " = 0)";
        String[] whereArgs = {String.valueOf(idAlmacen), String.valueOf(idProducto)};
        Cursor cursor = db.query(
                PasilloContract.TABLE_NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {

            long idPasilloPas = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract._ID));
            long idAlmacenPas = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDALMACEN));
            long idSeccionPas = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDSECCION));
            long idProductoPas = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDPRODUCTO));
            String nombrePas = cursor.getString(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_NOMBRE));
            int cantidadPas = cursor.getInt(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_CANTIDAD));


            cantidadPas = cantidadPas + cantidad;


            pasillo = new Pasillo(idPasilloPas, idAlmacenPas, idSeccionPas, idProducto, nombrePas, cantidadPas);

        }
        return pasillo;
    }

    //updateRecepcion
    public int updateRecepcion(Recepcion recepcion) {
        ContentValues values = new ContentValues();
        values.put(RecepcionContract.COLUMN_IDALMACEN, recepcion.getIdAlmacen());
        values.put(RecepcionContract.COLUMN_IDPRODUCTO, recepcion.getIdProducto());
        values.put(RecepcionContract.COLUMN_IDESTADO, recepcion.getIdEstado());
        values.put(RecepcionContract.COLUMN_CANTIDAD, recepcion.getCantidadRecibida());
        String where = RecepcionContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(recepcion.getId())};
        int updatedRows = db.update(RecepcionContract.TABLE_NAME, values, where, whereArgs);
        return updatedRows;
    }

    //finishRecepcion y finishTransferencia actualizan el pasillo o pasillos que nos devuelven los metodos de suficiente espacio para recepcion y suficiente stock para envio
    public int updatePasillo(Pasillo pasillo) {
        ContentValues values = new ContentValues();
        values.put(PasilloContract.COLUMN_IDALMACEN, pasillo.getIdAlmacen());
        values.put(PasilloContract.COLUMN_IDSECCION, pasillo.getIdSeccion());
        values.put(PasilloContract.COLUMN_IDPRODUCTO, pasillo.getIdProducto());
        values.put(PasilloContract.COLUMN_NOMBRE, pasillo.getNombre());
        values.put(PasilloContract.COLUMN_CANTIDAD, pasillo.getCantidadAlmacenada());
        String where = PasilloContract._ID + " = ?";
        String[] whereArgs = {String.valueOf(pasillo.getId())};
        int updatedRows = db.update(PasilloContract.TABLE_NAME, values, where, whereArgs);
        return updatedRows;
    }

    //getProductosStockFromAlmacen
    public List<ProductoCantidad> getProductosStockFromAlmacen(long id) {
        List<ProductoCantidad> productoCantidadList = new ArrayList<>();
        String[] select = {
                PasilloContract.COLUMN_IDPRODUCTO,
                " SUM(" + PasilloContract.COLUMN_CANTIDAD + ") "
        };
        //PUEDE QUE SUM DE ERROR PROBAR TOTAL EN ESE CASO
        String where = PasilloContract.COLUMN_IDALMACEN + " = ?";
        String[] whereArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                PasilloContract.TABLE_NAME,
                select,
                where,
                whereArgs,
                PasilloContract.COLUMN_IDPRODUCTO,
                null,
                null
        );
        ProductoCantidad productoCantidad = null;
        if (cursor.moveToFirst()) {
            do {
                //PUEDE QUE LA CANTIDAD NO ESTE EN ESA COLUMNA COMPROBAR
                long idProductoCantidad = cursor.getLong(cursor.getColumnIndexOrThrow(PasilloContract.COLUMN_IDPRODUCTO));
                if (idProductoCantidad != 0) {
                    int cantProductoCantidad = cursor.getInt(1);
                    productoCantidad = new ProductoCantidad(idProductoCantidad, cantProductoCantidad);
                    productoCantidadList.add(productoCantidad);
                }
            } while (cursor.moveToNext());
        }
        return productoCantidadList;
    }

    //CONTRACTS

    public static class CategoriaContract implements BaseColumns {
        public static final String TABLE_NAME = "Categoria";
        public static final String COLUMN_NOMBRE = "Nombre";
        public static final String COLUMN_FECHAALTA = "FechaAlta";
        public static final String COLUMN_IMAGEN = "Imagen";
        public static final String COLUMN_DESCRIPCION = "Descripcion";
        public static final String COLUMN_UNIDADMEDIDA = "UnidadMedida";
    }

    public static class AtributoContract implements BaseColumns {
        public static final String TABLE_NAME = "Atributo";
        public static final String COLUMN_VALOR = "Valor";
    }

    public static class Categoria_AtributoContract implements BaseColumns {
        public static final String TABLE_NAME = "Categoria_Atributo";
        public static final String COLUMN_IDCATEGORIA = "IDCategoria";
        public static final String COLUMN_IDATRIBUTO = "IDAtributo";
    }

    public static class ProductoContract implements BaseColumns {
        public static final String TABLE_NAME = "Producto";
        public static final String COLUMN_IDCATEGORIA = "IDCategoria";
        public static final String COLUMN_NOMBRE = "Nombre";
    }

    public static class AlmacenContract implements BaseColumns {
        public static final String TABLE_NAME = "Almacen";
        public static final String COLUMN_NOMBRE = "Nombre";
        public static final String COLUMN_DIRECCION = "Direccion";
    }

    public static class SeccionContract implements BaseColumns {
        public static final String TABLE_NAME = "Seccion";
        public static final String COLUMN_IDALMACEN = "IDAlmacen";
        public static final String COLUMN_NOMBRE = "Nombre";
    }

    public static class PasilloContract implements BaseColumns {
        public static final String TABLE_NAME = "Pasillo";
        public static final String COLUMN_IDALMACEN = "IDAlmacen";
        public static final String COLUMN_IDSECCION = "IDSeccion";
        public static final String COLUMN_IDPRODUCTO = "IDProducto";
        public static final String COLUMN_NOMBRE = "Nombre";
        public static final String COLUMN_CANTIDAD = "Cantidad";
    }

    public static class TransferenciaContract implements BaseColumns {
        public static final String TABLE_NAME = "Transferencia";
        public static final String COLUMN_IDALMACEN = "IDAlmacen";
        public static final String COLUMN_IDPRODUCTO = "IDProducto";
        public static final String COLUMN_IDESTADO = "IDEstado";
        public static final String COLUMN_CANTIDAD = "Cantidad";
        public static final String COLUMN_FECHA = "Fecha";
    }

    public static class RecepcionContract implements BaseColumns {
        public static final String TABLE_NAME = "Recepcion";
        public static final String COLUMN_IDALMACEN = "IDAlmacen";
        public static final String COLUMN_IDPRODUCTO = "IDProducto";
        public static final String COLUMN_IDESTADO = "IDEstado";
        public static final String COLUMN_CANTIDAD = "Cantidad";
        public static final String COLUMN_FECHA = "Fecha";
    }

    public static class EstadoContract implements BaseColumns {
        public static final String TABLE_NAME = "Estado";
        public static final String COLUMN_NOMBRE = "Nombre";
    }

    public static class Stock_MinimoContract implements BaseColumns {
        public static final String TABLE_NAME = "Stock_Minimo";
        public static final String COLUMN_IDALMACEN = "IDAlmacen";
        public static final String COLUMN_IDPRODUCTO = "IDProducto";
        public static final String COLUMN_CANTIDAD_MINIMA = "Cantidad_Minima";
    }

    public static class GestionInventarioDBHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;

        public static final String DATABASE_NAME = "GestionInventario.db";

        public GestionInventarioDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            createDBTables(db);
            insertEstados(db);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            deleteDBTables(db);
            createDBTables(db);
            insertEstados(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            deleteDBTables(db);
            createDBTables(db);
            insertEstados(db);
        }

        public void createDBTables(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_CATEGORIA);
            db.execSQL(SQL_CREATE_PRODUCTO);
            db.execSQL(SQL_CREATE_ATRIBUTO);
            db.execSQL(SQL_CREATE_CATEGORIA_ATRIBUTO);

            db.execSQL(SQL_CREATE_ALMACEN);
            db.execSQL(SQL_CREATE_SECCION);
            db.execSQL(SQL_CREATE_PASILLO);
            db.execSQL(SQL_CREATE_STOCK_MINIMO);

            db.execSQL(SQL_CREATE_TRANSFERENCIA);
            db.execSQL(SQL_CREATE_RECEPCION);
            db.execSQL(SQL_CREATE_ESTADO);
        }

        public void deleteDBTables(SQLiteDatabase db) {
            db.execSQL(SQL_DELETE_CATEGORIA);
            db.execSQL(SQL_DELETE_PRODUCTO);
            db.execSQL(SQL_DELETE_ATRIBUTO);
            db.execSQL(SQL_DELETE_CATEGORIA_ATRIBUTO);

            db.execSQL(SQL_DELETE_ALMACEN);
            db.execSQL(SQL_DELETE_SECCION);
            db.execSQL(SQL_DELETE_PASILLO);
            db.execSQL(SQL_DELETE_STOCK_MINIMO);

            db.execSQL(SQL_DELETE_TRANSFERENCIA);
            db.execSQL(SQL_DELETE_RECEPCION);
            db.execSQL(SQL_DELETE_ESTADO);
        }

        public void insertEstados(SQLiteDatabase db) {
            ContentValues values = new ContentValues();
            values.put(EstadoContract.COLUMN_NOMBRE, "Solicitud");
            db.insert(EstadoContract.TABLE_NAME, null, values);
            values.clear();
            values.put(EstadoContract.COLUMN_NOMBRE, "Preparacion");
            db.insert(EstadoContract.TABLE_NAME, null, values);
            values.clear();
            values.put(EstadoContract.COLUMN_NOMBRE, "Tramitado");
            db.insert(EstadoContract.TABLE_NAME, null, values);
        }
    }


    //Transferencia
    //Ver Transferencias muestra un rv de Transferencias , boton de crear Transferencia , click en el rv para ver el Transferencia , longclick para eliminarlo
    //al crear el Transferencia se comprobara si hay stock suficiente para enviar
    //ver Transferencia muestra la iformacion del Transferencia , spinner para cambiar su estado , una vez completado el Transferencia ya no se podra cambiar
    //al completar el Transferencia se restara la cantidad , si es necesario de los diferentes pasillos , comprobando previamente que todavia hay stock para enviar
    //entonces comprobaremos el stock total del producto en ese almacen para saber si debemos lanzar la notificacion de aviso de minimo

    //RECEPCION
    //Ver recepciones muestra un rv de recepciones , boton para crear recepcion , click en el rv para ver la recepcion , longclick para eliminarla
    //al crear la recepcion debemos comprobar si hay espacio para recibir en algun pasillo vacio , si el producto ya existe en un pasillo las recepciones se haran en ese pasillo
    //ver recepcion muestra la informacion de la recepcion , spinner para cambiar su estado , una vez completada la recepcion ya no se podra cambiar
    //al completar la recepcion se sumara , al pasillo la cantidad , comprobando previamente que todavia se puede recibir el producto

    //AJUSTAR INVENTARIO
    //mostrara un rv de almacenes , click para ver todos los productos y su stock total del almacen seleccionado


}