package mk.finki.mpip.bookproject.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import mk.finki.mpip.bookproject.BookDetailActivity;
import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.Entities.User;
import mk.finki.mpip.bookproject.R;

/**
 * Created by Marija on 7/22/2016.
 */
public class FavBookTask  extends AsyncTask<String, Void, Boolean> {

    Context context;
    private RestTemplate restTemplate;

    public FavBookTask(Context ctx){
        context = ctx;
    }


        ProgressDialog pd;
//
    @Override
    protected void onPreExecute() {

        super.onPreExecute();
          pd = new ProgressDialog(context);
//        pd.setTitle("Progress Dialog");
//        pd.setMessage("Loading books..");
          pd.show();

    }

    @Override
    protected void onCancelled() {
       pd.dismiss();
     //   Toast.makeText(context,"No internet or Web Server Down..", Toast.LENGTH_LONG).show();
        super.onCancelled();
    }



    @Override
    protected Boolean doInBackground(String... params) {
        if (!hasInternetConnection(context)) {
            cancel(true);
            return null;
        }
        String user = params[0];
        String book = params[1];

        String url = context.getResources().getString(R.string.toggle_fav_book);
        RestTemplate template = getRestTemplate();

        MultiValueMap<String, Object> formData;
        formData = new LinkedMultiValueMap<String, Object>();
        formData.add("userId", user);
        formData.add("bookId", book);

        HttpHeaders requestHeaders = new HttpHeaders();
        // Sending multipart/form-data
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Populate the MultiValueMap being serialized and headers in an HttpEntity object to use for the request
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                formData, requestHeaders);

        // Make the network request, posting the message, expecting a String in response from the server
        ResponseEntity<Boolean> response =
                template.exchange(url, HttpMethod.POST, requestEntity, Boolean.class);

        return response.getBody();
    }

    @Override
    protected void onPostExecute(Boolean isFavBook) {
        super.onPostExecute(isFavBook);
        pd.dismiss();
        if(isFavBook != null){
            BookDetailActivity activity = (BookDetailActivity) context;
            activity.setVisibility(isFavBook);
        }


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
                    Log.v("testTag","FavBookTask server avaible..");
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
            restTemplate = new RestTemplate(true);

            //konverterot da se namesti da ne pagja na properies koi gi nema vo klasata a gi ima vo json
//            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//            converter.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

//            restTemplate.getMessageConverters().add(converter);
        }
        return restTemplate;
    }
}
