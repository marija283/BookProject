package mk.finki.mpip.bookproject.Fragments;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.R;
import mk.finki.mpip.bookproject.Tasks.CheckLoginTask;


/**
 * Created by Riste on 19.7.2016.
 */
public class LoginFragment extends Fragment {
    EditText usernameField;
    EditText passwordField;
    Button loginBtn;
    CheckLoginTask loginTask;

    //create the Fragment
    public static LoginFragment create() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, null);
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
        usernameField = (EditText) view.findViewById(R.id.usernameField);
        passwordField = (EditText) view.findViewById(R.id.passwordField);
        loginBtn = (Button) view.findViewById(R.id.loginBtn);
        loginTask = new CheckLoginTask(getActivity());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                if (loginTask.getStatus().equals(AsyncTask.Status.FINISHED))
                    loginTask = new CheckLoginTask(getActivity());

                if (loginTask.getStatus().equals(AsyncTask.Status.PENDING))
                    loginTask.execute(username,password);
            }
        });


    }


}
