package mk.finki.mpip.bookproject.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mk.finki.mpip.bookproject.Adapters.CommentsAdapter;
import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.Entities.BookComment;
import mk.finki.mpip.bookproject.Entities.User;
import mk.finki.mpip.bookproject.HelperClasses.LoginHelperClass;
import mk.finki.mpip.bookproject.HomeActivity;
import mk.finki.mpip.bookproject.R;
import mk.finki.mpip.bookproject.Tasks.DeleteCommentTask;
import mk.finki.mpip.bookproject.Tasks.GetCommentsTask;
import mk.finki.mpip.bookproject.Tasks.GetUserByIdTask;
import mk.finki.mpip.bookproject.Tasks.PostCommentTask;

/**
 * Created by Riste on 15.7.2016.
 */
public class CommentFragment extends Fragment {
    private Book book;
    private User user;
    private LinearLayout commentLayout;
    private List<BookComment> commentList;
    private CommentsAdapter customAdapter;
    private GetCommentsTask getCommentTask;
    private PostCommentTask postCommentTask;
    private DeleteCommentTask deleteCommentTask;
    private GetUserByIdTask getUserByIdTask;

    private TextView commentLabel;
    private EditText commentInput;
    private Button commentPost;

    private AlertDialog alertDialog;
    FragmentManager fragmentManager;


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
        callGetCommentTask();
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

                if(comment!=null && !comment.isEmpty()){
                    callPostCommentTask(userId.toString(),bookId.toString(),comment);
                }
            }
        });

        fragmentManager = getFragmentManager();
        callGetUserByIdTask();


    }

    public void callGetCommentTask(){
        if (getCommentTask == null || getCommentTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            getCommentTask = new GetCommentsTask(getActivity(), new GetCommentsTask.GetCommentsListener() {
                @Override
                public void GetCommentsTaskFinished(ArrayList<BookComment> comments) {
                    //empty the commentLayout to bind from begining
                    commentLayout.removeAllViews();

                    if (comments != null && comments.size() > 0) {
                        customAdapter.clear();

                        for (BookComment comment : comments)
                            customAdapter.add(comment);

                       // customAdapter.notifyDataSetChanged();
                        int adapterCount = customAdapter.getCount();

                        for (int i = 0; i < adapterCount; i++) {
                            final View item = customAdapter.getView(i, null, null);

                            TextView hiddenIdTextView = (TextView) item.findViewById(R.id.comment_author_id);
                            final String authorId = hiddenIdTextView.getText().toString();
                            final Long commentId = customAdapter.getItemId(i);

                            //check if user is author of the comment and enable delete button
                            if(LoginHelperClass.isUserLoggedIn(getActivity())
                                && Long.parseLong(authorId) == LoginHelperClass.getUserLogged(getActivity()).getId()){

                                ImageButton btnDelete = (ImageButton) item.findViewById(R.id.comment_delete);
                                btnDelete.setVisibility(View.VISIBLE);
                                    //set dialog for delete button
                                btnDelete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

                                        alertBuilder.setTitle("Delete Comment");
                                        alertBuilder.setMessage("Do you really want to delte this comment ?");

                                        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                callDeleteCommentTask(commentId.toString());
                                            }
                                        });

                                        alertBuilder.setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        alertDialog.dismiss();
                                                    }
                                                });

                                        alertDialog = alertBuilder.create();
                                        alertDialog.show();

                                    }
                                });
                            }

                            ImageView authorImage = (ImageView) item.findViewById(R.id.comment_image);

                            authorImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   // Toast.makeText(getActivity(),"User id is "+authorId,Toast.LENGTH_SHORT).show();

                                    if (getUserByIdTask.getStatus().equals(AsyncTask.Status.FINISHED))
                                        callGetUserByIdTask();

                                    if (getUserByIdTask.getStatus().equals(AsyncTask.Status.PENDING))
                                        getUserByIdTask.execute(authorId);



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
        if (getCommentTask.getStatus().equals(AsyncTask.Status.PENDING))
            getCommentTask.execute(book.getId());
    }


    public void callGetUserByIdTask(){
        getUserByIdTask = new GetUserByIdTask(getActivity(), new GetUserByIdTask.GetUserByIdListener() {
            @Override
            public void GetUserByIdFinished(User u) {
                user = u;
                Intent i = new Intent( getActivity(), HomeActivity.class);
                i.putExtra("userProfile", "yes");
                i.putExtra("userObj", user);
                getActivity().startActivity(i);
            }
        });
    }

    public void callPostCommentTask(String userId, String bookId, String comment){
        if (postCommentTask == null || postCommentTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            postCommentTask = new PostCommentTask(getActivity(), new PostCommentTask.PostCommentListener() {
                @Override
                public void PostCommentTaskFinished(BookComment bookComment) {
                    if(bookComment != null){
                        callGetCommentTask();
                        commentInput.setText("");
                    }
                }
            });
        }
        if (postCommentTask.getStatus().equals(AsyncTask.Status.PENDING))
            postCommentTask.execute(userId,bookId,comment);
    }

    public void callDeleteCommentTask(String commentId){
        if (deleteCommentTask == null || deleteCommentTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            deleteCommentTask = new DeleteCommentTask(getActivity(), new DeleteCommentTask.DeleteCommentListener() {
                @Override
                public void DeleteCommentTaskFinished(Integer result) {
                    if(result != null)
                        callGetCommentTask();
                }
            });
        }
        if (deleteCommentTask.getStatus().equals(AsyncTask.Status.PENDING))
            deleteCommentTask.execute(commentId);
    }

}
