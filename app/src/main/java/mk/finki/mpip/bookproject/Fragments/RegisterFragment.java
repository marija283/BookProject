package mk.finki.mpip.bookproject.Fragments;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mk.finki.mpip.bookproject.HomeActivity;
import mk.finki.mpip.bookproject.R;
import mk.finki.mpip.bookproject.Tasks.CheckLoginTask;
import mk.finki.mpip.bookproject.Tasks.UserRegisterTask;


/**
 * Created by Riste on 19.7.2016.
 */
public class RegisterFragment extends Fragment {
    EditText username;
    EditText password;
    EditText name;
    TextView linkText;
    Button registerBtn;
    UserRegisterTask registerTask;

    //create the Fragment
    public static RegisterFragment create() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doInject(view);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void doInject(View view){
        username = (EditText) view.findViewById(R.id.username);
        name = (EditText) view.findViewById(R.id.name);
        password = (EditText) view.findViewById(R.id.password);
        registerBtn = (Button) view.findViewById(R.id.register_btn);
        linkText = (TextView) view.findViewById(R.id.link_login);
        registerTask = new UserRegisterTask(getActivity());

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfUser = name.getText().toString();
                String usernameOfUser = username.getText().toString();
                String passOfUser = password.getText().toString();

                if (registerTask.getStatus().equals(AsyncTask.Status.FINISHED))
                    registerTask = new UserRegisterTask(getActivity());

                if (registerTask.getStatus().equals(AsyncTask.Status.PENDING))
                    registerTask.execute(nameOfUser,usernameOfUser,passOfUser);
            }
        });

        linkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.showLoginFragment(true);
            }
        });


    }


}
