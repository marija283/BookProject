package mk.finki.mpip.bookproject.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import mk.finki.mpip.bookproject.Database.BooksDAO;
import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.HelperClasses.LoginHelperClass;
import mk.finki.mpip.bookproject.HomeActivity;
import mk.finki.mpip.bookproject.Tasks.BooksToDbTask;

/**
 * Created by Riste on 04.8.2016.
 */
public class DownloadBooksService extends Service {

    Context context = this;

    public DownloadBooksService() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("testTag","Service started ++");
        loadTheDatabase();
        return super.onStartCommand(intent, flags, startId);

    }

    private void loadTheDatabase() {
        BooksToDbTask booksToDb = new BooksToDbTask(context, new BooksToDbTask.BooksToDbListener() {
            @Override
            public void BooksToDbTaskFinished(ArrayList<Book> books) {
                if (books != null) {
                    //open database and add all
                    BooksDAO bookDao = new BooksDAO(context);
                    bookDao.open();
                    bookDao.deleteAll();
                    for(Book b : books){
                        bookDao.insert(b);
                    }
                    bookDao.close();
                    Log.v("testTag","Added to DATABASE from SERVICE "+books.size()+" books");
                }

                Log.v("testTag", "Service Stoped -- ");
                context.stopService(new Intent(context, DownloadBooksService.class));

                if (LoginHelperClass.getServiceStatus(context)) {

                    //setup calendar to run this service again in 7 seconds
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.SECOND, 7);

                    Intent intent = new Intent(context, DownloadBooksService.class);

                    PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);

                    AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pintent);
                }
            }
        });

        booksToDb.execute();
    }
}
