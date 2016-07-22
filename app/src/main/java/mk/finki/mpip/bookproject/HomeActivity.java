package mk.finki.mpip.bookproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
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
import android.widget.Toast;
import mk.finki.mpip.bookproject.Entities.User;
import mk.finki.mpip.bookproject.Fragments.ListFragment;
import mk.finki.mpip.bookproject.Fragments.LoginFragment;
import mk.finki.mpip.bookproject.Fragments.RegisterFragment;
import mk.finki.mpip.bookproject.HelperClasses.LoginHelperClass;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.v("testTag","onCreate HomeActivity");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set login or register
        init();
        callListFragment();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            ListFragment listFragment = (ListFragment) getFragmentManager().findFragmentByTag("ListFrag");
            if(listFragment != null && listFragment.isVisible())
            {
                super.onBackPressed();
            }
            else
                getFragmentManager().popBackStack();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        setupSearchView();
        return true;
    }
    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setQueryHint("Search Here");
    }




    private boolean changeLoginMenuItems() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        boolean isLogged = LoginHelperClass.isUserLoggedIn(this);

        MenuItem btnLogin = navigationView.getMenu().findItem(R.id.login);
        MenuItem btnRegister = navigationView.getMenu().findItem(R.id.register);
        MenuItem btnLogout = navigationView.getMenu().findItem(R.id.log_out);
        TextView usernameHolder =
                (TextView) navigationView.getHeaderView(0).findViewById(R.id.usernameHolder);

        if(isLogged){
            btnLogin.setVisible(false);
            btnRegister.setVisible(false);
            btnLogout.setVisible(true);
            usernameHolder.setText("Welcome " + LoginHelperClass.getUserLogged(HomeActivity.this).getUsername());
        }else {
            btnLogin.setVisible(true);
            btnRegister.setVisible(true);
            btnLogout.setVisible(false);
            usernameHolder.setText("Please Log In");
        }

        return isLogged;

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
            LoginHelperClass.logout(HomeActivity.this);

            changeLoginMenuItems();

            Toast.makeText(HomeActivity.this,"You have been Logged out",Toast.LENGTH_LONG).show();


        } else if (id == R.id.login) {
            LoginFragment loginFragment = (LoginFragment) getFragmentManager().findFragmentByTag("LoginFrag");
            if(loginFragment == null || !loginFragment.isVisible())
            {
                loginFragment = LoginFragment.create();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,loginFragment,"LoginFrag")
                        .addToBackStack("LoginFrag")
                        .commit();
            }
        } else if (id == R.id.register){
            RegisterFragment registerFragment = (RegisterFragment) getFragmentManager().findFragmentByTag("RegisterFrag");
            if(registerFragment == null || !registerFragment.isVisible())
            {
                registerFragment = RegisterFragment.create();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,registerFragment,"RegisterFrag")
                        .addToBackStack("RegisterFrag")
                        .commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        changeLoginMenuItems();
        Log.v("testTag","onStart Home Activity");

    }

    //initialize what u need after Creating the Activity
    private void init() {

        fragmentManager = getFragmentManager();
    }

    private void callListFragment() {
        ListFragment listFragment = ListFragment.create();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,listFragment,"ListFrag").commit();

    }

    public void logInUser(User user){
        LoginHelperClass.setUserLoggedIn(this,user);
        changeLoginMenuItems();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void showLoginFragment(){
        LoginFragment loginFragment = (LoginFragment) getFragmentManager().findFragmentByTag("LoginFrag");
        if(loginFragment == null || !loginFragment.isVisible())
        {
            loginFragment = LoginFragment.create();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,loginFragment,"LoginFrag")
                    .addToBackStack("LoginFrag")
                    .commit();
        }
    }
}
