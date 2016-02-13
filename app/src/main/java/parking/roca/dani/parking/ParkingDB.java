package parking.roca.dani.parking;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dani on 29/10/2015.
 */
public class ParkingDB extends SQLiteOpenHelper {
    //Ano: 2015. Mes: 9. Dia: 30.Hour: 18. Minuto: 58
    /*
    "Ano: " + cal.get(Calendar.YEAR) +
                                        ". Mes: " + (cal.get(Calendar.MONTH) + 1) + ". Dia: " + cal.get(Calendar.DAY_OF_MONTH) +
                                        ".Hour: " + cal.get(Calendar.HOUR_OF_DAY) + ". Minuto: " + cal.get(Calendar.MINUTE));
     */
    private static final int DATABASE_VERSION = 10;
    public static final String TABLE_NAME = "coches_activos2";
    public static final String TABLE_NAME_2 = "coches_salidas2";
    public static final String PRIMARY_ID = "_id";
    public static final String PRIMARY_KEY = "_matricula";
    public static final String FECHA_ENTRADA = "entrada";
    public static final String FECHA_SALIDA = "salida";
    public static final String FACTURA = "factura";
    public static final String COLOR = "color";
    public static final String PLAZA = "plaza";

    private static final String TABLE_CREATE_ACTIVOS =
            "CREATE TABLE " + TABLE_NAME + " (" + PRIMARY_KEY
                    + " text primary key, " +
                    FECHA_ENTRADA + " text not null, " +
                      COLOR + " integer not null, " + PLAZA + " integer not null);";

    private static final String TABLE_SALIDAS =
            "CREATE TABLE " + TABLE_NAME_2 + " (" + PRIMARY_ID
                    + " integer primary key autoincrement, " +
                    PRIMARY_KEY + " text not null, " +
                    FECHA_ENTRADA + " text not null, " +
                    FECHA_SALIDA + " text not null, " +
                    FACTURA + " text not null);";

    public ParkingDB(Context context) {
        super(context, "PARKINGS", null, DATABASE_VERSION);
        //SQLiteDatabase.CursorFactory factory;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TABLE_CREATE_ACTIVOS);
            db.execSQL(TABLE_SALIDAS);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ParkingDB.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME+ ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2+ ";");
        onCreate(db);
    }
}
