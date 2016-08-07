package mk.finki.mpip.bookproject.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.R;

/**
 * Created by Riste on 25.7.2016.
 */
public class SearchBarAdapter extends CursorAdapter {

    private List<Book> items;
    private Picasso imageLoader;
    private TextView title;
    private TextView authorName;
    private ImageView bookImage;


    public SearchBarAdapter(Context context, Cursor cursor, List<Book> items) {

        super(context, cursor, false);

        this.items = items;
        imageLoader = Picasso.with(context);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Book book = items.get(cursor.getPosition());

        title.setText(book.getTitle());
        authorName.setText("by: " + book.getAuthor().getName());
        imageLoader.load(context.getResources().getString(R.string.book_image_real) + book.getId()).
                placeholder(R.mipmap.ic_person_black_24dp)
                .error(R.mipmap.ic_power_settings_new_black_24dp)
                .fit()
                .into(bookImage);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.suggestion_item, parent, false);

        title = (TextView) view.findViewById(R.id.suggestionTitle);
        authorName = (TextView) view.findViewById(R.id.suggestionAuthor);
        bookImage = (ImageView) view.findViewById(R.id.suggestionImage);

        return view;

    }

    public Long getBookId(int position){
        return items.get(position).getId();
    }
}