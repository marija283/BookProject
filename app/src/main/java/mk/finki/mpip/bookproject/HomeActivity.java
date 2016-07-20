package mk.finki.mpip.bookproject;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.squareup.picasso.Picasso;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.Fragments.ListFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.v("testTag","onCreate HomeActivity");

        //useless email floation shit
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        callListFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_refresh) {
            ListFragment listFragment = (ListFragment) getFragmentManager().findFragmentByTag("ListFrag");
            if(listFragment != null && listFragment.isVisible())
            {
                listFragment.callAsyincTask();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.log_out) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.v("testTag","onStart Home Activity");


    }

//    private class HttpRequestTask extends AsyncTask<Void, Void, Book>{
//        @Override
//        protected Book doInBackground(Void... params) {
//            try {
//                //Go to cmd...write ipconfig...Ethernet adapter VirtualBox Host-Only Network... IPv4Adress
//                final String url = "http://192.168.56.2:8080/book-project/api/books/1";
//                RestTemplate restTemplate = new RestTemplate();
//                Log.v("testTag","getting REST");
//
//                //konverterot da se namesti da ne pagja na properies koi gi nema vo klasata a gi ima vo json
//                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//                converter.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//
//                restTemplate.getMessageConverters().add(converter);
//
//                Book book = restTemplate.getForObject(url, Book.class);
//                Log.v("testTag","finished getting REST");
//                return book;
//            } catch (Exception e) {
//                Log.e("MainActivity", e.getMessage(), e);
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Book book) {
//            TextView greetingIdText = (TextView) findViewById(R.id.id_value);
//            TextView greetingContentText = (TextView) findViewById(R.id.content_value);
//            ImageView greetingImage = (ImageView) findViewById(R.id.content_image);
//
//            greetingIdText.setText(book.getId());
//            greetingContentText.setText(book.getDescription());
//            Picasso.with(HomeActivity.this)
//                    .load("http://192.168.56.2:8080/book-project/api/books/get-image/"+ book.getId())
//                    .into(greetingImage);
//
//        }
//    }


    private void callListFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        ListFragment listFragment = ListFragment.create();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,listFragment,"ListFrag").commit();

    }
}
