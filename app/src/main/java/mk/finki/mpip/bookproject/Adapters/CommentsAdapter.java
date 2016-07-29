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
import mk.finki.mpip.bookproject.Entities.BookComment;
import mk.finki.mpip.bookproject.R;


/**
 * Created by Riste on 16.7.2016.
 */

public class CommentsAdapter extends BaseAdapter {

    private List<BookComment> allComments;
    private Context context;
    private Picasso imageLoader;
    private LayoutInflater inflater;

    public CommentsAdapter(Context ctx, List<BookComment> data) {
        this.context = ctx;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (data != null) {
            allComments = data;
        } else {
            allComments= new ArrayList<BookComment>();
        }
        imageLoader = Picasso.with(ctx);
    }

    public void add(BookComment comment) {
        allComments.add(comment);
        notifyDataSetChanged();
    }

    public void delete(int position) {
        if (position < allComments.size() && position >= 0) {
            allComments.remove(position);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return allComments.size();
    }

    @Override
    public Object getItem(int position) {
        return allComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return allComments.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewFastest(position, convertView, parent);
    }

    public void clear() {
        allComments.clear();
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
                            (RelativeLayout) inflater.inflate(R.layout.comment_list_item, null);
            holder.picture = (ImageView) holder.layout.findViewById(R.id.comment_image);
            holder.author = (TextView) holder.layout.findViewById(R.id.comment_author);
            holder.text = (TextView) holder.layout.findViewById(R.id.comment_text);
            holder.authorId = (TextView) holder.layout.findViewById(R.id.comment_author_id);

            convertView.setTag(holder);
        }

        //get the static holder with References to the Views.
        holder = (Holder) convertView.getTag();


        BookComment comment = (BookComment) getItem(position);
        imageLoader.load(context.getResources().getString(R.string.user_profile_photo) + comment.getUserFrom().getId()).
                placeholder(R.mipmap.ic_person_black_24dp)
                .error(R.mipmap.ic_power_settings_new_black_24dp)
                .fit()
                .into(holder.picture);
        holder.text.setText(comment.getComment());
        holder.author.setText(comment.getUserFrom().getUsername());
        holder.authorId.setText(comment.getUserFrom().getId().toString());

        return convertView;
    }



    static class Holder {
        RelativeLayout layout;
        ImageView picture;
        TextView author;
        TextView authorId;
        TextView text;
    }


}
