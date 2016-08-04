package mk.finki.mpip.bookproject.Tasks;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import mk.finki.mpip.bookproject.Entities.BookComment;
import mk.finki.mpip.bookproject.R;

/**
 * Created by Riste on 20.7.2016.
 */
public class PostCommentTask extends AsyncTask<String, Void, BookComment> {
    Context context;
    private RestTemplate restTemplate;
    private PostCommentListener listener;

    public PostCommentTask(Context context,PostCommentListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context,"No internet or server down..", Toast.LENGTH_LONG).show();
        super.onCancelled();
    }
    @Override
    protected BookComment doInBackground(String... params) {
        if (!hasInternetConnection(context)) {
            cancel(true);
            return null;
        }
        String userId = params[0];
        String bookId = params[1];
        String text = params[2];

        String url = context.getResources().getString(R.string.url_comment_post);
        RestTemplate template = getRestTemplate();

        // populate the data to post...FORM DATA POST NOT JSON...
        MultiValueMap<String, Object> formData;
        formData = new LinkedMultiValueMap<String, Object>();
        formData.add("userFrom", userId);
        formData.add("bookId", bookId);
        formData.add("text", text);

        HttpHeaders requestHeaders = new HttpHeaders();
        // Sending form-data
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Populate the MultiValueMap being serialized and headers in an HttpEntity object to use for the request
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                formData, requestHeaders);

        // Make the network request, posting the message, expecting a String in response from the server
        ResponseEntity<BookComment> response =
                template.exchange(url, HttpMethod.POST, requestEntity, BookComment.class);

        BookComment result = response.getBody();
        return  result;
    }

    @Override
    protected void onPostExecute(BookComment result) {
        super.onPostExecute(result);
        listener.PostCommentTaskFinished(result);
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
                    Log.v("testTag","post comment Avaible..");
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


    public interface PostCommentListener{
        void PostCommentTaskFinished(BookComment bookComment);
    }
}

