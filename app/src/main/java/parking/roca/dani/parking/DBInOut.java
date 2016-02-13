package parking.roca.dani.parking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dani on 29/10/2015.
 */
public class DBInOut {
    // Database fields
    private SQLiteDatabase database;
    private ParkingDB dbHelper;
    private String[] allColumnsActivos = { ParkingDB.PRIMARY_KEY,
            ParkingDB.FECHA_ENTRADA , ParkingDB.COLOR, ParkingDB.PLAZA};
    private String[] allColumnsSalidas = { ParkingDB.PRIMARY_KEY,
            ParkingDB.FECHA_ENTRADA , ParkingDB.FECHA_SALIDA, ParkingDB.FACTURA};
    //private String[] colMatr = { ParkingDB.PRIMARY_KEY};

    public DBInOut(Context context) {
        dbHelper = new ParkingDB(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long EntrarCoche(String matricula, String entrada, int color, int plaza) {
        ContentValues values = new ContentValues();
        values.put(ParkingDB.PRIMARY_KEY, matricula);
        values.put(ParkingDB.FECHA_ENTRADA, entrada);
        values.put(ParkingDB.COLOR, color);
        values.put(ParkingDB.PLAZA, plaza);
        long insertId = database.insert(ParkingDB.TABLE_NAME, null, values);

        ContentValues values2 = new ContentValues();
        values2.put(ParkingDB.PRIMARY_KEY, matricula);
        values2.put(ParkingDB.FECHA_ENTRADA, entrada);
        values2.put(ParkingDB.FECHA_SALIDA, "-");
        values2.put(ParkingDB.FACTURA, "0");
        database.insert(ParkingDB.TABLE_NAME_2, null, values2);
        return insertId;
    }

    public void DeshacerEntrar(String matricula) {
        deleteRowTabla(matricula);
        deleteRowTablaSalidas(matricula);
    }

    public ArrayList<ArrayList<Object>> cochesDentro(){
        ArrayList<ArrayList<Object>> atributos = new ArrayList();
        Cursor cursor = database.query(ParkingDB.TABLE_NAME,
                allColumnsActivos, null, null,
                null, null, null);
        if(cursor.moveToFirst()) {
            ArrayList<Object> fila = new ArrayList<>();
            fila.add(cursor.getString(0));
            fila.add(cursor.getString(1));
            fila.add(cursor.getInt(2));
            fila.add(cursor.getInt(3));
            atributos.add(fila);
            while (cursor.moveToNext()) {
                fila = new ArrayList<>();
                fila.add(cursor.getString(0));
                fila.add(cursor.getString(1));
                fila.add(cursor.getInt(2));
                fila.add(cursor.getInt(3));
                atributos.add(fila);
            }
        }
        //DBOperations newComment = cursorToComment(cursor);
        cursor.close();
        return atributos;
    }

    public String SacarCoche(String matricula, String entrada, String salida, String dinero) {
        ContentValues values = new ContentValues();
        values.put(ParkingDB.FECHA_SALIDA, salida);
        values.put(ParkingDB.FACTURA, dinero);
        database.update(ParkingDB.TABLE_NAME_2, values, ParkingDB.PRIMARY_KEY
                + "=" + "\"" + matricula + "\" and " + ParkingDB.FECHA_SALIDA + "=" + "\""
                + "-" + "\"", null);
        deleteRowTabla(matricula);
        return "";
    }

    public void DeshacerSacarCoche(String matricula, String entrada, String salida, int color, int plaza) {
        ContentValues values = new ContentValues();
        values.put(ParkingDB.PRIMARY_KEY, matricula);
        values.put(ParkingDB.FECHA_ENTRADA, entrada);
        values.put(ParkingDB.COLOR, color);
        values.put(ParkingDB.PLAZA, plaza);
        database.insert(ParkingDB.TABLE_NAME, null, values);

        ContentValues values2 = new ContentValues();
        values2.put(ParkingDB.FECHA_SALIDA, "-");
        values2.put(ParkingDB.FACTURA, 0);
        database.update(ParkingDB.TABLE_NAME_2, values2, ParkingDB.PRIMARY_KEY
                + "=" + "\"" + matricula + "\" and " + ParkingDB.FECHA_SALIDA + "=" + "\""
                + salida + "\"", null);
    }

    public void deleteRowTabla(String matricula) {
        System.out.println("Comment deleted with id: " + matricula);
        database.delete(ParkingDB.TABLE_NAME, ParkingDB.PRIMARY_KEY
                + "=" + "\"" + matricula + "\"", null);
    }

    public void deleteRowTablaSalidas(String matricula) {
        System.out.println("Comment deleted with id: " + matricula);
        database.delete(ParkingDB.TABLE_NAME_2, ParkingDB.PRIMARY_KEY
                + "=" + "\"" + matricula + "\"" + " AND " + ParkingDB.FECHA_SALIDA + "=" + "\""
                + "-" + "\"", null);
    }


    public List<Object> getAllComments() {
        List<Object> comments = new ArrayList<Object>();

        Cursor cursor = database.query(ParkingDB.TABLE_NAME,
                allColumnsActivos, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Object comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Object cursorToComment(Cursor cursor) {
        Object comment = new Object();
        //comment.setId(cursor.getLong(0));
        //comment.setComment(cursor.getString(1));
        return comment;
    }

    public ArrayList<ArrayList<Object>> cochesPeriodo(String dataIni, String dataFi){
        ArrayList<ArrayList<Object>> atributos = new ArrayList();
        Cursor cursor = database.query(ParkingDB.TABLE_NAME_2,
                allColumnsSalidas, ParkingDB.FECHA_SALIDA + " BETWEEN " + "\"" + dataIni + "\""
                        + " AND " + "\"" + dataFi + "\"", null,
                null, null, ParkingDB.FECHA_SALIDA + " ASC");
        /*Log.i("query", ParkingDB.FECHA_SALIDA + " BETWEEN " + "\"" + dataIni + "\""
                + " AND " + "\"" + dataFi + "\"");*/
        if(cursor.moveToFirst()) {
            ArrayList<Object> fila = new ArrayList<>();
            fila.add(cursor.getString(0));
            fila.add(cursor.getString(1));
            fila.add(cursor.getString(2));
            fila.add(cursor.getString(3));
            atributos.add(fila);
            while (cursor.moveToNext()) {
                fila = new ArrayList<>();
                fila.add(cursor.getString(0));
                fila.add(cursor.getString(1));
                fila.add(cursor.getString(2));
                fila.add(cursor.getString(3));
                atributos.add(fila);
            }
        }
        //DBOperations newComment = cursorToComment(cursor);
        cursor.close();
        return atributos;
    }


    public int cochesPeriodoDistinct(String dataIni, String dataFi){
        Cursor cursor = database.rawQuery("SELECT COUNT(DISTINCT " + ParkingDB.PRIMARY_KEY + ") AS Count\n"
                + "FROM "+ ParkingDB.TABLE_NAME_2 + " \n" +
                "WHERE " + ParkingDB.FECHA_SALIDA + " BETWEEN " + "\"" + dataIni + "\"" +
                    " AND "+ "\"" + dataFi + "\" " + "OR " + ParkingDB.FECHA_ENTRADA + " >= " + "\"" + dataIni + "\" " +
                "AND " + ParkingDB.FECHA_ENTRADA + " <= " + "\"" + dataFi + "\"", null);
        int ret = 0;
        if(cursor.moveToFirst()) ret = cursor.getInt(0);
        cursor.close();
        return ret;
    }

    public int cochesPeriodoEntradas(String dataIni, String dataFi){
        Cursor cursor = database.rawQuery("SELECT COUNT(" + ParkingDB.PRIMARY_KEY + ") AS Count\n"
                + "FROM "+ ParkingDB.TABLE_NAME_2 + " \n" +
                "WHERE " + ParkingDB.FECHA_ENTRADA + " BETWEEN " + "\"" + dataIni + "\"" +
                " AND "+ "\"" + dataFi + "\"", null);
        int ret = 0;
        if(cursor.moveToFirst()) ret = cursor.getInt(0);
        cursor.close();
        return ret;
    }
}
/*
        String query = "select * from " + QUOTES_TABLE_NAME + "WHERE _id=?";
        database.rawQuery(query, new String[]{"3"});
        while(c.moveToNext()){
         String body = c.getString(ColomunQuotes.BODY_QUOTES);
         //Acciones con el valor obtenido
        }
        SELECT COUNT(*) FROM table_name;
         */
        /*Cursor cursor = database.query(ParkingDB.TABLE_NAME,
                allColumnsActivos, ParkingDB.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBOperations newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;*/