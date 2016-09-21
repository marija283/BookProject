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

import mk.finki.mpip.bookproject.BookDetailActivity;
import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.R;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

/**
 * Created by Marija on 9/12/2016.
 */
public class JedisTask extends AsyncTask<String, Void, String> {
    Context context;
    private RestTemplate restTemplate;

    //create with new JedisTask(getActivity());
    public JedisTask(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {

        super.onPreExecute();


    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context,"No internet or Web Server Down..", Toast.LENGTH_LONG).show();
        super.onCancelled();
    }



    @Override
    protected String doInBackground(String... params) {
        if (!hasInternetConnection(context)) {
            cancel(true);
            return null;
        }
        String id = params[0];
        JedisShardInfo shardInfo = new JedisShardInfo("Riste.redis.cache.windows.net", 6379);
        shardInfo.setPassword("e/m/5h58TU0ZB6VfoKM1ps0MN/K1T7kzDPA9soGygIo="); /* Use your access key. */
        Jedis jedis = new Jedis(shardInfo);
        jedis.set("foo", "bar");


        return jedis.get("foo");
    }

    @Override
    protected void onPostExecute(String redis) {
        super.onPostExecute(redis);
        if(redis != null) {
            BookDetailActivity activity = (BookDetailActivity) context;
            activity.setRedis(redis);
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
                    Log.v("testTag","Getting single book avaible..");
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


        }
        return restTemplate;
    }

}