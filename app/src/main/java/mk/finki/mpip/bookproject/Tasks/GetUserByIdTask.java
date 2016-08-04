package mk.finki.mpip.bookproject.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import mk.finki.mpip.bookproject.BookDetailActivity;
import mk.finki.mpip.bookproject.Entities.User;
import mk.finki.mpip.bookproject.R;


/**
 * Created by Marija on 8/4/2016.
 */
public class GetUserByIdTask extends AsyncTask<String, Void, User>{

    Context context;
    private RestTemplate restTemplate;
    GetUserByIdListener getUserByIdListener;

    public GetUserByIdTask(Context context, GetUserByIdListener getUserByIdListener) {
        this.context = context;
        this.getUserByIdListener = getUserByIdListener;
    }

    ProgressDialog pd;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.show();

    }

    @Override
    protected void onCancelled() {
        pd.dismiss();
        Toast.makeText(context,"No internet or Web Server Down..", Toast.LENGTH_LONG).show();
        super.onCancelled();
    }



    @Override
    protected User doInBackground(String... params) {
        if (!hasInternetConnection(context)) {
            cancel(true);
            return null;
        }
        String id = params[0];
        String url = context.getResources().getString(R.string.get_user_by_id) + "/" + id;
        restTemplate = getRestTemplate();
        User user = restTemplate.getForObject(url,User.class);

        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        pd.dismiss();
        getUserByIdListener.GetUserByIdFinished(user);
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
                    Log.v("testTag","Getting User Avaible..");
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
    public interface GetUserByIdListener{
        public void GetUserByIdFinished(User user);
    }
}