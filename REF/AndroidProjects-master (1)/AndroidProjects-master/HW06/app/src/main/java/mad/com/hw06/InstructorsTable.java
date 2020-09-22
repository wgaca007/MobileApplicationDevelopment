package mad.com.hw06;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by darsh on 11/3/2017.
 */

public class InstructorsTable {
    public static final String TABLE_NAME = "instructor";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_EMAIL_ID = "email_id";
    public static final String COLUMN_PERSONAL_WEBSITE = "personal_website";
    public static final String COLUMN_IMAGE = "image";

    static public void onCreate(SQLiteDatabase db){
        StringBuilder createQuery = new StringBuilder();
        createQuery.append("CREATE TABLE "+ TABLE_NAME +" (");
        createQuery.append(COLUMN_USERNAME+ " text not null, ");
        createQuery.append(COLUMN_FIRST_NAME+ " text not null, ");
        createQuery.append(COLUMN_LAST_NAME+ " text not null, ");
        createQuery.append(COLUMN_EMAIL_ID + " text not null, ");
        createQuery.append(COLUMN_PERSONAL_WEBSITE +" text not null, ");
        createQuery.append(COLUMN_IMAGE+" BLOB not null );");
        try{
            db.execSQL(createQuery.toString());
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        InstructorsTable.onCreate(db);
    }
}
