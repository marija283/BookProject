package mk.finki.mpip.bookproject.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mk.finki.mpip.bookproject.Entities.Author;
import mk.finki.mpip.bookproject.Entities.Book;

public class BooksDAO {
  private SQLiteDatabase database;
  private DbOpenHelper dbHelper;
    // Database fields

  private String[] allColumns = {
    DbOpenHelper.COLUMN_ID,
    DbOpenHelper.COLUMN_TITLE,
    DbOpenHelper.COLUMN_DESCRIPTION,
    DbOpenHelper.COLUMN_IMAGE,
    DbOpenHelper.COLUMN_AUTHORNAME
 };

  public BooksDAO(Context context) {
    dbHelper = new DbOpenHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    database.close();
    dbHelper.close();
  }

  public boolean insert(Book item) {

//    if (getById(item.getId()) != null) {
//      return update(item);
//    }

    long insertId = database.insert(DbOpenHelper.TABLE_NAME, null, itemToContentValues(item));

    if (insertId > 0) {
      return true;
    }
    else {
      return false;
    }

  }

  public boolean update(Book item) {
    long numRowsAffected =
            database.update(DbOpenHelper.TABLE_NAME, itemToContentValues(item),
                    DbOpenHelper.COLUMN_ID + " = " + item.getId(), null);
    return numRowsAffected > 0;
  }

  public List<Book> getAllItems() {

    List<Book> items = new ArrayList<Book>();

    Cursor cursor = database.query(DbOpenHelper.TABLE_NAME, allColumns, null, null, null, null, null);

    if (cursor.moveToFirst()) {
      do {
        items.add(cursorToItem(cursor));
      }
      while (cursor.moveToNext());
    }
    cursor.close();

    return items;
  }

  public Book getById(long id) {

    Cursor cursor = database.query(DbOpenHelper.TABLE_NAME, allColumns,
                    DbOpenHelper.COLUMN_ID + " = " + id, null, null, null, null);
    try {
      if (cursor.moveToFirst()) {
        return cursorToItem(cursor);
      }
      else {
        // no items found
        return null;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    finally {
      cursor.close();
    }

  }

    public Cursor queryAutocomplete(String query){
        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME, allColumns,
                DbOpenHelper.COLUMN_TITLE + " LIKE '%" + query + "%'", null, null, null, null);

        return cursor;
    }

  public void deleteAll(){
      database.execSQL("delete from " + DbOpenHelper.TABLE_NAME);
  }

  public static Book cursorToItem(Cursor cursor) {
      Book item = new Book();

      Long id = cursor.getLong(cursor.getColumnIndex(DbOpenHelper.COLUMN_ID));
      item.setId(id);

      String title = cursor.getString(cursor.getColumnIndex(DbOpenHelper.COLUMN_TITLE));
      item.setTitle(title);

      String description = cursor.getString(cursor.getColumnIndex(DbOpenHelper.COLUMN_DESCRIPTION));
      item.setDescription(description);

      String image = cursor.getString(cursor.getColumnIndex(DbOpenHelper.COLUMN_IMAGE));
      item.setImage(image);

      String authorName = cursor.getString(cursor.getColumnIndex(DbOpenHelper.COLUMN_AUTHORNAME));
      Author author = new Author();
      author.setId(0L);
      author.setName(authorName);
      item.setAuthor(author);

      return item;
  }

  protected ContentValues itemToContentValues(Book item) {
      ContentValues values = new ContentValues();
      values.put(DbOpenHelper.COLUMN_ID, item.getId());
      values.put(DbOpenHelper.COLUMN_TITLE, item.getTitle());
      values.put(DbOpenHelper.COLUMN_DESCRIPTION, item.getDescription());
      values.put(DbOpenHelper.COLUMN_IMAGE, item.getImage());
      values.put(DbOpenHelper.COLUMN_AUTHORNAME, item.getAuthor().getName()+" "+item.getAuthor().getSurname());

      return values;
  }

    public static List<Book> getBooksFromCursor(Cursor cursor){
        ArrayList<Book> result = new ArrayList<Book>();

        if (cursor.moveToFirst()) {
            do {
                result.add(cursorToItem(cursor));
            }
            while (cursor.moveToNext());
        }

        return result;
    }
}
