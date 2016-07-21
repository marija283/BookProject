package mk.finki.mpip.bookproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mk.finki.mpip.bookproject.Entities.Book;

public class BookDetailActivity extends AppCompatActivity {
    ImageView bookImg;
    TextView bookName;
    TextView bookDesctiption;
    TextView bookAuthor;
    private Picasso imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        doInject();

    }




    private void doInject() {
        Intent i = getIntent();
        Book bookObj = (Book) i.getParcelableExtra("bookObj");
        imageLoader = Picasso.with(BookDetailActivity.this);
        bookImg = (ImageView) findViewById(R.id.bookImg);
        bookName = (TextView) findViewById(R.id.book_name);
        bookDesctiption = (TextView) findViewById(R.id.book_description);
        bookAuthor = (TextView) findViewById(R.id.book_author);

        imageLoader.load("http://marketmybook.in/wp-content/uploads/2013/05/Library-Books.jpg").
                placeholder(R.mipmap.ic_person_black_24dp)
                .error(R.mipmap.ic_power_settings_new_black_24dp)
                .into(bookImg);
        bookName.setText(bookObj.getTitle());
        bookDesctiption.setText(bookObj.getDescription());
        bookAuthor.setText(bookObj.getAuthor().toString());
        setTitle(bookObj.getTitle());

    }

        @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
