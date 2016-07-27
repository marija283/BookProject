package mk.finki.mpip.bookproject.HelperClasses;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.R;

/**
 * Created by Riste on 25.7.2016.
 */
public class ExampleAdapter extends CursorAdapter {

    private List<Book> items;

    private TextView title;
    private TextView authorName;


    public ExampleAdapter(Context context, Cursor cursor, List<Book> items) {

        super(context, cursor, false);

        this.items = items;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        title.setText(items.get(cursor.getPosition()).getTitle());
        authorName.setText(items.get(cursor.getPosition()).getAuthor().getName());

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.suggestion_item, parent, false);

        title = (TextView) view.findViewById(R.id.suggestionTitle);
        authorName = (TextView) view.findViewById(R.id.suggestionAuthor);

        return view;

    }

    public Long getBookId(int position){
        return items.get(position).getId();
    }
}