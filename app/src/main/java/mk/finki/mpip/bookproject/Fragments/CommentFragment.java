package mk.finki.mpip.bookproject.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mk.finki.mpip.bookproject.Adapters.CommentsAdapter;
import mk.finki.mpip.bookproject.Adapters.GetAllBooksAdapter;
import mk.finki.mpip.bookproject.BookDetailActivity;
import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.Entities.BookComment;
import mk.finki.mpip.bookproject.HelperClasses.LoginHelperClass;
import mk.finki.mpip.bookproject.R;
import mk.finki.mpip.bookproject.Tasks.GetAllBooksTask;
import mk.finki.mpip.bookproject.Tasks.GetCommentsTask;

/**
 * Created by Riste on 15.7.2016.
 */
public class CommentFragment extends Fragment {
    private Book book;
    private LinearLayout commentLayout;
    private List<BookComment> commentList;
    private CommentsAdapter customAdapter;
    private GetCommentsTask commentTask;

    private TextView commentLabel;
    private EditText commentInput;
    private Button commentPost;


    //create the Fragment
    public static CommentFragment create(Book book) {
        CommentFragment fragment = new CommentFragment();
        fragment.book = book;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.comment_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doInject(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        showInput();
        callAsyincTask();
    }

    private void showInput() {
        if(LoginHelperClass.isUserLoggedIn(getActivity())){
            commentInput.setVisibility(getView().VISIBLE);
            commentPost.setVisibility(getView().VISIBLE);
            commentLabel.setVisibility(getView().VISIBLE);
        }
        else{
            commentInput.setVisibility(getView().GONE);
            commentPost.setVisibility(getView().GONE);
            commentLabel.setVisibility(getView().GONE);
        }
    }

    private void doInject(View view){

        commentList = new ArrayList<BookComment>();
        customAdapter = new CommentsAdapter(getActivity(),commentList);
        commentLayout = (LinearLayout) view.findViewById(R.id.commentSection);

        commentInput = (EditText) view.findViewById(R.id.comment_input);
        commentPost = (Button) view.findViewById(R.id.comment_post);
        commentLabel = (TextView) view.findViewById(R.id.comment_label);

        commentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentInput.getText().toString();
                Long userId = LoginHelperClass.getUserLogged(getActivity()).getId();
                Long bookId = book.getId();

                Toast.makeText(getActivity(),
                        "User with id: "+userId + "commented on book  "+ bookId+" with text: "+comment,Toast.LENGTH_LONG).show();
            }
        });


    }

    public void callAsyincTask(){
        if (commentTask == null || commentTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            commentTask = new GetCommentsTask(getActivity(), new GetCommentsTask.GetCommentsListener() {
                @Override
                public void GetCommentsTaskFinished(ArrayList<BookComment> comments) {
                    if (comments != null && comments.size() > 0) {
                        customAdapter.clear();

                        for (BookComment comment : comments)
                            customAdapter.add(comment);

                       // customAdapter.notifyDataSetChanged();
                        int adapterCount = customAdapter.getCount();

                        for (int i = 0; i < adapterCount; i++) {
                            final View item = customAdapter.getView(i, null, null);

                            item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TextView hiddenIdTextView = (TextView) item.findViewById(R.id.comment_author_id);
                                    String id = hiddenIdTextView.getText().toString();
                                    Toast.makeText(getActivity(),"User id is "+id,Toast.LENGTH_SHORT).show();
                                }
                            });
                            commentLayout.addView(item);
                        }
                    }
                    else{
                        TextView textView = new TextView(getActivity());
                        textView.setText("NO COMMENTS FOR THIS BOOK YET");
                        textView.setBackgroundColor(Color.BLUE);
                        textView.setTextColor(Color.WHITE);
                        commentLayout.addView(textView);
                    }
                }
            });
        }
        if (commentTask.getStatus().equals(AsyncTask.Status.PENDING))
            commentTask.execute(book.getId());
    }

}
