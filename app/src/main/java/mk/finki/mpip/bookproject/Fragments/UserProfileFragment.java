package mk.finki.mpip.bookproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import mk.finki.mpip.bookproject.Tasks.GetUserFavBooksTask;


public class UserProfileFragment extends Fragment {
    ImageButton profilePic;
    TextView userName;
    TextView shortBio;
    FlowLayout flowLayout;
    private Picasso imageLoader;
    private Context context;

    User user;


    private OnFragmentInteractionListener mListener;

    public UserProfileFragment(Context ctx) {
        this.context = ctx;
    }


    public static UserProfileFragment create(Context ctx) {
        UserProfileFragment user = new UserProfileFragment(ctx);
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
        user = LoginHelperClass.getUserLogged(context);
        imageLoader = Picasso.with(context);
        profilePic = (ImageButton) view.findViewById(R.id.user_profile_photo);
        userName = (TextView) view.findViewById(R.id.user_profile_name);
        shortBio = (TextView) view.findViewById(R.id.user_profile_short_bio);
        flowLayout = (FlowLayout) view.findViewById(R.id.flow_layout);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        final LayoutParams flowLP = new LayoutParams(5, 5);
        GetUserFavBooksTask getUserFavBooksTask = new GetUserFavBooksTask(getActivity(), new GetUserFavBooksTask.UserFavBooksListener() {
            @Override
            public void UserFavBooksTaskFinished(ArrayList<Book> books) {
                for(final Book book : books) {
                    ImageView iv = new ImageView(context);
                    iv.setElevation(4);
                    iv.setPadding(20, 20, 20, 20);

                    imageLoader.load(context.getResources().getString(R.string.book_image_real) +  book.getId().toString()).
                            placeholder(R.mipmap.ic_person_black_24dp)
                            .error(R.mipmap.ic_power_settings_new_black_24dp)
                            .resize(220, 320)
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
                }
            }
        });
        getUserFavBooksTask.execute(user.getId().toString());

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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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


