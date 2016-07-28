package mk.finki.mpip.bookproject;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
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

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import mk.finki.mpip.bookproject.Database.BooksDAO;
import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.Entities.User;
import mk.finki.mpip.bookproject.Fragments.ListFragment;
import mk.finki.mpip.bookproject.Fragments.LoginFragment;
import mk.finki.mpip.bookproject.Fragments.RegisterFragment;
import mk.finki.mpip.bookproject.Fragments.UserProfileFragment;
import mk.finki.mpip.bookproject.Adapters.SearchBarAdapter;
import mk.finki.mpip.bookproject.HelperClasses.LoginHelperClass;
import mk.finki.mpip.bookproject.Layout.CircleTransform;
import mk.finki.mpip.bookproject.Tasks.BooksToDbTask;
import mk.finki.mpip.bookproject.Tasks.GetSingleBookTask;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    SearchView mSearchView;
    ArrayList<String> testSuggestions;

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
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //neso za autocomplete
        mSearchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                loadSuggestions(newText);
                //ako treba i listViewto da se menuvaat...ama loso izlgleda
               // filterListView(newText);
                return true;
            }
        });
        mSearchView.setQueryHint("Search Here");
    }

    private void filterListView(String newText) {
        ListFragment listFragment = (ListFragment) getFragmentManager().findFragmentByTag("ListFrag");
        if(listFragment != null && listFragment.isVisible()) {
            listFragment.search(newText);
        }
    }


    private void loadSuggestions(String query){
        BooksDAO booksDao = new BooksDAO(this);

        booksDao.open();

        Cursor dbCursor = booksDao.queryAutocomplete(query);
        List<Book> result = BooksDAO.getBooksFromCursor(dbCursor);

        booksDao.close();

        //adding the custom adapter which will render the suggestions
        mSearchView.setSuggestionsAdapter(new SearchBarAdapter(this, dbCursor, result));
        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                SearchBarAdapter adapter = (SearchBarAdapter) mSearchView.getSuggestionsAdapter();
                Long clickedId = adapter.getBookId(position);

                GetSingleBookTask singleBookTask = new GetSingleBookTask(HomeActivity.this);

                if (singleBookTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                    singleBookTask = new GetSingleBookTask(HomeActivity.this);
                }
                if (singleBookTask.getStatus().equals(AsyncTask.Status.PENDING))
                    singleBookTask.execute(clickedId);

                return true;
            }
        });
    }

    private boolean changeLoginMenuItems() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        boolean isLogged = LoginHelperClass.isUserLoggedIn(this);

        MenuItem btnLogin = navigationView.getMenu().findItem(R.id.login);
        MenuItem btnRegister = navigationView.getMenu().findItem(R.id.register);
        MenuItem btnLogout = navigationView.getMenu().findItem(R.id.log_out);
        MenuItem btnMyProfile = navigationView.getMenu().findItem(R.id.my_profile);
        TextView usernameHolder =
                (TextView) navigationView.getHeaderView(0).findViewById(R.id.usernameHolder);
        ImageView userPhotoHolder =
                (ImageView) navigationView.getHeaderView(0).findViewById(R.id.userPhotoHolder);

        if(isLogged){
            btnLogin.setVisible(false);
            btnRegister.setVisible(false);
            btnLogout.setVisible(true);
            btnMyProfile.setVisible(true);

            User user = LoginHelperClass.getUserLogged(HomeActivity.this);
            Picasso imageLoader = Picasso.with(this);
            usernameHolder.setText("Welcome " + user.getUsername());

            imageLoader.load(getResources().getString(R.string.user_profile_photo) + user.getId())
                    .placeholder(R.mipmap.ic_person_black_24dp)
                    .error(R.mipmap.ic_power_settings_new_black_24dp)
                    .transform(new CircleTransform())
                    .into(userPhotoHolder);
        }else {
            btnLogin.setVisible(true);
            btnRegister.setVisible(true);
            btnLogout.setVisible(false);
            btnMyProfile.setVisible(false);
            usernameHolder.setText("Please Log In");
            userPhotoHolder.setImageResource(android.R.drawable.btn_star);
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
            UserProfileFragment userProfileFragment = (UserProfileFragment) getFragmentManager().findFragmentByTag("ProfileFrag");
            if(userProfileFragment == null || !userProfileFragment.isVisible())
            {
                userProfileFragment = UserProfileFragment.create(HomeActivity.this);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,userProfileFragment,"ProfileFrag")
                        .addToBackStack("ProfileFrag")
                        .commit();
            }
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
        loadTheDatabase();
    }

    private void loadTheDatabase() {
        BooksToDbTask booksToDb = new BooksToDbTask(this, new BooksToDbTask.BooksToDbListener() {
            @Override
            public void BooksToDbTaskFinished(ArrayList<Book> books) {
                if (books != null) {
                        //open database and add all
                    BooksDAO bookDao = new BooksDAO(HomeActivity.this);
                    bookDao.open();
                    bookDao.deleteAll();
                    for(Book b : books){
                        bookDao.insert(b);
                    }
                    bookDao.close();
                    Log.v("testTag","Added to DATABASE "+books.size()+" books");
                }
            }
        });

        booksToDb.execute();
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
