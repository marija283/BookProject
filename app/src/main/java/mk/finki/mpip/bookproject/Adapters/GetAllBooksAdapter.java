package mk.finki.mpip.bookproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.R;


/**
 * Created by Riste on 16.7.2016.
 */

public class GetAllBooksAdapter extends BaseAdapter {

    private List<Book> visibleBooks;
    private List<Book> allBooks;
    private String queryText;
    private Context context;
    private Picasso imageLoader;
    private LayoutInflater inflater;

    public GetAllBooksAdapter(Context ctx, List<Book> data) {
        this.context = ctx;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (data != null) {
            visibleBooks = allBooks = data;
        } else {
            visibleBooks = allBooks = new ArrayList<Book>();
        }
        imageLoader = Picasso.with(ctx);
    }

    public void add(Book person) {
        allBooks.add(person);
        search(queryText);
        notifyDataSetChanged();
    }

    public void delete(int position) {
        if (position < visibleBooks.size() && position >= 0) {
            allBooks.remove(position);
            search(queryText);
            notifyDataSetChanged();
        }
    }

    public void search(String text) {
        this.queryText = text;
        if (text != null) {
            text = text.toLowerCase();
            visibleBooks = new ArrayList<Book>();
            for (Book b : allBooks) {
                if (b.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    visibleBooks.add(b);
                }
            }
        } else {
            visibleBooks = allBooks;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return visibleBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return visibleBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return visibleBooks.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewFastest(position, convertView, parent);
    }

    public void clear() {
        allBooks.clear();
        search(queryText);
        notifyDataSetChanged();
    }


    public View getViewFastest(int position,
                               View convertView,
                               ViewGroup parent) {
        Holder holder;

        //set the holder to the References of the Views.
        if (convertView == null) {
            holder = new Holder();
            convertView =
                    holder.layout =
                            (RelativeLayout) inflater.inflate(R.layout.book_list_item, null);
            holder.picture = (ImageView) holder.layout.findViewById(R.id.book_image);
            holder.author = (TextView) holder.layout.findViewById(R.id.book_author);
            holder.title = (TextView) holder.layout.findViewById(R.id.book_title);


            convertView.setTag(holder);
        }

        //get the static holder with References to the Views.
        holder = (Holder) convertView.getTag();


        Book bookItem = (Book) getItem(position);
        imageLoader.load(context.getResources().getString(R.string.book_image_real) + bookItem.getId()).
                placeholder(R.mipmap.ic_person_black_24dp)
                .error(R.mipmap.ic_power_settings_new_black_24dp)
                .fit()
                .into(holder.picture);
        holder.title.setText(bookItem.getTitle());
        holder.author.setText(bookItem.getDescription().substring(0,10));


        return convertView;
    }



    static class Holder {
        RelativeLayout layout;
        ImageView picture;
        TextView author;
        TextView title;
    }


}
