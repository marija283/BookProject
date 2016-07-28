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

import mk.finki.mpip.bookproject.Adapters.GetAllBooksAdapter;
import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.R;

/**
 * Created by Riste on 15.7.2016.
 */
public class GetAllBooksTask extends AsyncTask<Void, Void, ArrayList<Book>> {

    Context context;
    private RestTemplate restTemplate;
    private GetAllBooksAdapter adapter;

    //create with new GetAllBooksTask(getActivity(),adapter);
    public GetAllBooksTask(Context context,GetAllBooksAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    ProgressDialog pd;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setTitle("Progress Dialog");
        pd.setMessage("Loading books..");
        pd.show();

    }

    @Override
    protected void onCancelled() {
        pd.dismiss();
        Toast.makeText(context,"No internet or Web Server Down..", Toast.LENGTH_LONG).show();
        super.onCancelled();
    }



    @Override
    protected ArrayList<Book> doInBackground(Void... params) {
        if (!hasInternetConnection(context)) {
            cancel(true);
            return null;
        }
        String url = context.getResources().getString(R.string.url_books);
        RestTemplate template = getRestTemplate();
        Book [] books = template.getForObject(url,Book[].class);

        ArrayList<Book> result = new ArrayList<Book>(Arrays.asList(books));

        Log.v("testTag","finished getting REST");
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Book> books) {
        super.onPostExecute(books);
        if (books != null) {
            adapter.clear();

            for (Book book : books)
                adapter.add(book);

            adapter.notifyDataSetChanged();
        }
        pd.dismiss();
    }

    private boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        //ako ima internet proveri dali web sterverot od APIto e avaible
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL(context.getResources().getString(R.string.url_books));   // Change to "http://google.com" for www  test.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5 * 1000);          // 5 s.
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.v("testTag","WebServer Avaible..");
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
}