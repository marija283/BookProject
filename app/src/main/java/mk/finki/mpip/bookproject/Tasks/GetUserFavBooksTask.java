package mk.finki.mpip.bookproject.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.R;

/**
 * Created by Marija on 7/27/2016.
 */
public class GetUserFavBooksTask extends AsyncTask<String, Void, ArrayList<Book>> {

    Context context;
    private RestTemplate restTemplate;
    UserFavBooksListener userFavBooksListener;

    //create with new GetUserFavBooksTask(getActivity(), GetUserFavBooksTask.userFavBooksListener);
    public GetUserFavBooksTask(Context context, UserFavBooksListener userFavBooksListener) {
        this.context = context;
        this.userFavBooksListener = userFavBooksListener;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }



    @Override
    protected ArrayList<Book> doInBackground(String... params) {
        if (!hasInternetConnection(context)) {
            cancel(true);
            return null;
        }
        String user = params[0];
        String url = context.getResources().getString(R.string.user_fav_books) + user;
        RestTemplate template = getRestTemplate();
        Book [] books = template.getForObject(url,Book[].class);

        ArrayList<Book> result = new ArrayList<Book>(Arrays.asList(books));

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Book> books) {
        super.onPostExecute(books);
        userFavBooksListener.UserFavBooksTaskFinished(books);
    }

    private boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        //ako ima internet proveri dali web sterverot od APIto e avaible
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL(context.getResources().getString(R.string.url_books_real));   // Change to "http://google.com" for www  test.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5 * 1000);          // 5 s.
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.v("testTag","Get User Fav Books avaible..");
                    urlConnection.disconnect();
                    return true;

                } else {
                    urlConnection.disconnect();
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }

        return false;
    }


    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            //bez true vo konstruktorot zos custom sakame ovoj jackson Converterov da e da ne mapra se
            restTemplate = new RestTemplate();

            //konverterot da se namesti da ne pagja na properies koi gi nema vo klasata a gi ima vo json
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

            restTemplate.getMessageConverters().add(converter);
        }
        return restTemplate;
    }

    //listener za koga ke zavrsi taskot neso da se sluci
    public interface UserFavBooksListener{
        public void UserFavBooksTaskFinished(ArrayList<Book> books);
    }
}