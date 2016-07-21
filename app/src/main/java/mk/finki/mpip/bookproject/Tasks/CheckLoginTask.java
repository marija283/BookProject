package mk.finki.mpip.bookproject.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
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

import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.Entities.User;
import mk.finki.mpip.bookproject.Fragments.LoginFragment;
import mk.finki.mpip.bookproject.HomeActivity;
import mk.finki.mpip.bookproject.R;

/**
 * Created by Riste on 20.7.2016.
 */
public class CheckLoginTask extends AsyncTask<String, Void, User> {
    Context context;
    private RestTemplate restTemplate;
    ProgressDialog pd;

    public CheckLoginTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setTitle("Progress Dialog");
        pd.setMessage("Trying to Log in..");
        pd.show();

    }

    @Override
    protected void onCancelled() {
        pd.dismiss();
        Toast.makeText(context,"No internet or server down..", Toast.LENGTH_LONG).show();
        super.onCancelled();
    }
    @Override
    protected User doInBackground(String... params) {
        if (!hasInternetConnection(context)) {
            cancel(true);
            return null;
        }

        String user = params[0];
        String password = params[1];

        String url = context.getResources().getString(R.string.url_books_real);
        RestTemplate template = getRestTemplate();

        // populate the data to post...FORM DATA POST NOT JSON...
        MultiValueMap<String, Object> formData;
        formData = new LinkedMultiValueMap<String, Object>();
        formData.add("username", user);
        formData.add("password", password);

        HttpHeaders requestHeaders = new HttpHeaders();
        // Sending multipart/form-data
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Populate the MultiValueMap being serialized and headers in an HttpEntity object to use for the request
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                formData, requestHeaders);

        // Make the network request, posting the message, expecting a String in response from the server
        ResponseEntity<User> response =
                template.exchange(url, HttpMethod.POST, requestEntity, User.class);

        User result = response.getBody();
        return  result;
    }

    @Override
    protected void onPostExecute(User result) {
        super.onPostExecute(result);
        pd.dismiss();

        if(result == null){
            Toast.makeText(context,"Wrong Username Or Password",Toast.LENGTH_LONG).show();
        }else{
            HomeActivity activity = (HomeActivity)context;
            activity.logInUser(result);
            Log.v("testTag","log in User Task finished");
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
                    Log.v("testTag","user login server Avaible..");
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
            //ova true avtomatski stava Messages Converters....
            restTemplate = new RestTemplate(true);
        }
        return restTemplate;
    }
}

