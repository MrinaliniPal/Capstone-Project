package tk.mrinalini.cureme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "movie.db";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MOVIE_TABLE = "CREATE TABLE " + DBContract.CureMe_DB_Entry.TABLE_NAME + " ( "
                + DBContract.CureMe_DB_Entry.SEARCH + " text not null);";
        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.CureMe_DB_Entry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}