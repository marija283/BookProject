package mk.finki.mpip.bookproject;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.Fragments.CommentFragment;
import mk.finki.mpip.bookproject.HelperClasses.LoginHelperClass;
import mk.finki.mpip.bookproject.Tasks.CheckLoginTask;
import mk.finki.mpip.bookproject.Tasks.FavBookTask;
import mk.finki.mpip.bookproject.Tasks.GetFavBookStateTask;


public class BookDetailActivity extends AppCompatActivity {
    ImageView bookImg;
    TextView bookName;
    TextView bookDesctiption;
    TextView bookAuthor;
    TextView logInToCommentLink;
    ImageButton addFavorite;
    ImageButton removeFavorite;
    ImageButton share;
    private Picasso imageLoader;
    FavBookTask favBookTask;
    GetFavBookStateTask getFavBookStateTask;
    Book bookObj;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        doInject();

    }

    public boolean onOptionsItemSelected(MenuItem item){
        this.finish();
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
        logInToCommentLink = (TextView) findViewById(R.id.logInToCommentLink);
        addFavorite = (ImageButton) findViewById(R.id.add_favorite);
        removeFavorite = (ImageButton) findViewById(R.id.remove_favorite);
        share = (ImageButton) findViewById(R.id.share);
        favBookTask = new FavBookTask(BookDetailActivity.this);
        getFavBookStateTask = new GetFavBookStateTask(BookDetailActivity.this);

//        imageLoader.load(getResources().getString(R.string.book_image_real) + bookObj.getId()).
//                placeholder(R.mipmap.ic_person_black_24dp)
//                .fit() // this fit is better
//                .error(R.mipmap.ic_power_settings_new_black_24dp)
//                .into(bookImg);

        Glide.with(BookDetailActivity.this)
                .load(getResources().getString(R.string.book_image_real) + bookObj.getId())
                .placeholder(R.mipmap.ic_person_black_24dp)
                .fitCenter()
                .centerCrop()
                .error(R.mipmap.ic_power_settings_new_black_24dp)
                .into(bookImg);

        bookName.setText(bookObj.getTitle());
        bookDesctiption.setText(bookObj.getDescription());
        bookAuthor.setText(bookObj.getAuthor().toString());
        setTitle(bookObj.getTitle());
        addFavorite.setVisibility(View.GONE);
        removeFavorite.setVisibility(View.GONE);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"www.google.com");
                sendIntent.setType("text/plain");
                Intent.createChooser(sendIntent,"Share via");
                startActivity(sendIntent);
            }
        });

        logInToCommentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookDetailActivity.this, HomeActivity.class);
                i.putExtra("showLogin","true");
                BookDetailActivity.this.startActivity(i);
            }
        });

        //set comments fragment

        fragmentManager = getFragmentManager();
        CommentFragment commentFragment = CommentFragment.create(bookObj);
        fragmentManager.beginTransaction().replace(R.id.comment_container,commentFragment,"CommentFrag").commit();

    }


    public void setFavBookBtn(){
        if(LoginHelperClass.isUserLoggedIn(BookDetailActivity.this)){
            if (getFavBookStateTask.getStatus().equals(AsyncTask.Status.FINISHED))
                getFavBookStateTask = new GetFavBookStateTask(BookDetailActivity.this);

            if (getFavBookStateTask.getStatus().equals(AsyncTask.Status.PENDING))
                getFavBookStateTask.execute(
                        LoginHelperClass.getUserLogged(BookDetailActivity.this).getId().toString(), bookObj.getId().toString());

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

            logInToCommentLink.setVisibility(View.INVISIBLE);
        }
        else{
            addFavorite.setVisibility(View.GONE);
            removeFavorite.setVisibility(View.GONE);
            logInToCommentLink.setVisibility(View.VISIBLE);
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
    protected void onStart() {
        super.onStart();
        setFavBookBtn();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
