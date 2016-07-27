package mk.finki.mpip.bookproject.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import mk.finki.mpip.bookproject.Entities.User;
import mk.finki.mpip.bookproject.HelperClasses.LoginHelperClass;
import mk.finki.mpip.bookproject.HomeActivity;
import mk.finki.mpip.bookproject.R;
import mk.finki.mpip.bookproject.FlowLayout.LayoutParams;
import mk.finki.mpip.bookproject.FlowLayout;


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

        imageLoader.load(context.getResources().getString(R.string.user_profile_photo) +  user.getId().toString()).
                placeholder(R.mipmap.ic_person_black_24dp)
                .error(R.mipmap.ic_power_settings_new_black_24dp)
                .transform(new CircleTransform())
                .into(profilePic);

        getActivity().setTitle(user.getFname() + " " + user.getLname());
        userName.setText(user.getFname() + " " + user.getLname());
        shortBio.setText(user.getBiography());

        LayoutParams flowLP = new LayoutParams(5, 5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);

        for (int i = 0; i < 10; i++) {
            ImageView iv = new ImageView(context);
            iv.setElevation(4);
            iv.setPadding(20, 20, 20, 20);

            imageLoader.load(context.getResources().getString(R.string.user_profile_photo) +  user.getId().toString()).
                    placeholder(R.mipmap.ic_person_black_24dp)
                    .error(R.mipmap.ic_power_settings_new_black_24dp)
                    .resize(150, 100)
                    .into(iv);
            iv.setLayoutParams(layoutParams);

            flowLayout.addView(iv, flowLP);
        }
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


class CircleTransform implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
