package mk.finki.mpip.bookproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.HelperClasses.LoginHelperClass;
import mk.finki.mpip.bookproject.Tasks.CheckLoginTask;
import mk.finki.mpip.bookproject.Tasks.FavBookTask;


public class BookDetailActivity extends AppCompatActivity {
    ImageView bookImg;
    TextView bookName;
    TextView bookDesctiption;
    TextView bookAuthor;
    Button addFavorite;
    Button removeFavorite;
    private Picasso imageLoader;
    FavBookTask favBookTask;
    Book bookObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        doInject();

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


    private void doInject() {
        Intent i = getIntent();
        bookObj = (Book) i.getParcelableExtra("bookObj");
        imageLoader = Picasso.with(BookDetailActivity.this);
        bookImg = (ImageView) findViewById(R.id.bookImg);
        bookName = (TextView) findViewById(R.id.book_name);
        bookDesctiption = (TextView) findViewById(R.id.book_description);
        bookAuthor = (TextView) findViewById(R.id.book_author);
        addFavorite = (Button) findViewById(R.id.add_favorite);
        removeFavorite = (Button) findViewById(R.id.remove_favorite);
        favBookTask = new FavBookTask(BookDetailActivity.this);

        imageLoader.load(getResources().getString(R.string.book_image)).
                placeholder(R.mipmap.ic_person_black_24dp)
                .error(R.mipmap.ic_power_settings_new_black_24dp)
                .into(bookImg);
        bookName.setText(bookObj.getTitle());
        bookDesctiption.setText(bookObj.getDescription());
        bookAuthor.setText(bookObj.getAuthor().toString());
        setTitle(bookObj.getTitle());
        setFavBookBtn();
    }


    public void setFavBookBtn(){
        if(LoginHelperClass.isUserLoggedIn(BookDetailActivity.this)){
            addFavorite.setVisibility(View.VISIBLE);
            removeFavorite.setVisibility(View.VISIBLE);
            addFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (favBookTask.getStatus().equals(AsyncTask.Status.FINISHED))
                        favBookTask = new FavBookTask(BookDetailActivity.this);

                    if (favBookTask.getStatus().equals(AsyncTask.Status.PENDING))
                        favBookTask.execute(LoginHelperClass.getUserLogged(BookDetailActivity.this).getId().toString(),
                                bookObj.getId().toString());
                }
            });
            removeFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (favBookTask.getStatus().equals(AsyncTask.Status.FINISHED))
                        favBookTask = new FavBookTask(BookDetailActivity.this);

                    if (favBookTask.getStatus().equals(AsyncTask.Status.PENDING))
                        favBookTask.execute(LoginHelperClass.getUserLogged(BookDetailActivity.this).getId().toString(),
                                bookObj.getId().toString());
                }
            });

        }
        else{
            addFavorite.setVisibility(View.GONE);
            removeFavorite.setVisibility(View.GONE);
        }

    }

    public void setVisibility(Boolean isFav){
        if(isFav){
            addFavorite.setVisibility(View.GONE);
            removeFavorite.setVisibility(View.VISIBLE);
        }
        else{
            addFavorite.setVisibility(View.VISIBLE);
            removeFavorite.setVisibility(View.GONE);
        }
    }

        @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
