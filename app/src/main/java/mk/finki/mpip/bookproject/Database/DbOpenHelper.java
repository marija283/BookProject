package mk.finki.mpip.bookproject.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

  public static final String TABLE_NAME = "Books";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_TITLE = "title";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_IMAGE = "image";
  public static final String COLUMN_AUTHORNAME = "authorName";



  private static final int DATABASE_VERSION = 1;

  private static final String DATABASE_NAME = "BookAppDatabase.db";

  //posle primary key autoincrement ako sakame ID da dodava
  private static final String DATABASE_CREATE = String
    .format("create table %s (%s  integer primary key, "
                    + "%s text not null, %s text, %s text, %s text);",
            TABLE_NAME, COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_IMAGE, COLUMN_AUTHORNAME);

  public DbOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Don't do this in real end-user applications
    db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
    onCreate(db);
  }

}
