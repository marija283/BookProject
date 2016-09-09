package mk.finki.mpip.bookproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mk.finki.mpip.bookproject.BookDetailActivity;
import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.Entities.User;
import mk.finki.mpip.bookproject.HelperClasses.LoginHelperClass;
import mk.finki.mpip.bookproject.Layout.CircleTransform;
import mk.finki.mpip.bookproject.R;
import mk.finki.mpip.bookproject.Layout.FlowLayout.LayoutParams;
import mk.finki.mpip.bookproject.Layout.FlowLayout;
import mk.finki.mpip.bookproject.Tasks.GetUserByIdTask;
import mk.finki.mpip.bookproject.Tasks.GetUserFavBooksTask;


public class UserProfileFragment extends Fragment {
    ImageButton profilePic;
    TextView userName;
    TextView shortBio;
    FlowLayout flowLayout;
    LayoutParams flowLP;
    private Picasso imageLoader;
    private Context context;
    GetUserFavBooksTask getUserFavBooksTask;
    User user;
    Button showMore;

    private OnFragmentInteractionListener mListener;

    public UserProfileFragment(Context ctx, User user) {
        this.context = ctx;
        this.user = user;
    }


    public static UserProfileFragment create(Context ctx, User u) {
        UserProfileFragment user = new UserProfileFragment(ctx, u);
        return user;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doInject(view);
    }

    public void doInject(View view){
        imageLoader = Picasso.with(context);
        profilePic = (ImageButton) view.findViewById(R.id.user_profile_photo);
        userName = (TextView) view.findViewById(R.id.user_profile_name);
        shortBio = (TextView) view.findViewById(R.id.user_profile_short_bio);
        flowLayout = (FlowLayout) view.findViewById(R.id.flow_layout);
        showMore = (Button) view.findViewById(R.id.show_more);
        callFavBookTask(false);

        showMore.setVisibility(view.GONE);

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flowLayout.getChildCount() > 0)
                    flowLayout.removeAllViews();

                if (getUserFavBooksTask.getStatus().equals(AsyncTask.Status.FINISHED))
                    callFavBookTask(true);

                if (getUserFavBooksTask.getStatus().equals(AsyncTask.Status.PENDING))
                    getUserFavBooksTask.execute(user.getId().toString());

                showMore.setVisibility(getView().GONE);
            }
        });

        imageLoader.load(context.getResources().getString(R.string.user_profile_photo) +  user.getId().toString()).
                placeholder(R.mipmap.ic_person_black_24dp)
                .error(R.mipmap.ic_power_settings_new_black_24dp)
                .transform(new CircleTransform())
                .into(profilePic);

        getActivity().setTitle(user.getFname() + " " + user.getLname());
        userName.setText(user.getFname() + " " + user.getLname());
        shortBio.setText(user.getBiography());

        if (getUserFavBooksTask.getStatus().equals(AsyncTask.Status.FINISHED))
            callFavBookTask(false);

        if (getUserFavBooksTask.getStatus().equals(AsyncTask.Status.PENDING))
            getUserFavBooksTask.execute(user.getId().toString());
    }

    public void callFavBookTask(final Boolean continueLoad){

        getUserFavBooksTask = new GetUserFavBooksTask(getActivity(), new GetUserFavBooksTask.UserFavBooksListener() {
            @Override
            public void UserFavBooksTaskFinished(ArrayList<Book> books) {
                flowLP = new LayoutParams(5, 5);
                for(final Book book : books) {
                    ImageView iv = new ImageView(context);
                    iv.setElevation(4);
                    iv.setPadding(20, 20, 20, 20);

//                    imageLoader.load(context.getResources().getString(R.string.book_image_real) +  book.getId().toString()).
//                            placeholder(R.mipmap.ic_person_black_24dp)
//                            .error(R.mipmap.ic_power_settings_new_black_24dp)
//                            .resize(220, 320)
//                            .into(iv);
                    Glide.with(UserProfileFragment.this)
                            .load(context.getResources().getString(R.string.book_image_real) +  book.getId().toString())
                            .placeholder(R.mipmap.ic_person_black_24dp)
                            .override(220, 320)
                            .centerCrop()
                            .error(R.mipmap.ic_power_settings_new_black_24dp)
                            .into(iv);

                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent( getActivity(), BookDetailActivity.class);
                            i.putExtra("bookObj", book);
                            getActivity().startActivity(i);
                        }
                    });

                    flowLayout.addView(iv, flowLP);
                    if(flowLayout.getChildCount() >= 6 && !continueLoad){
                        showMore.setVisibility(getView().VISIBLE);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(flowLayout.getChildCount() > 0)
            flowLayout.removeAllViews();

        if (getUserFavBooksTask.getStatus().equals(AsyncTask.Status.FINISHED))
            callFavBookTask(false);

        if (getUserFavBooksTask.getStatus().equals(AsyncTask.Status.PENDING))
            getUserFavBooksTask.execute(user.getId().toString());

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}


