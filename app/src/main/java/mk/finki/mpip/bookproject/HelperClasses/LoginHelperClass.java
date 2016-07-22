package mk.finki.mpip.bookproject.HelperClasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import mk.finki.mpip.bookproject.Entities.User;

/**
 * Created by Riste on 19.7.2016.
 */
public class LoginHelperClass {

        public static Boolean isUserLoggedIn(Context context) {
            final SharedPreferences preferences = context.getSharedPreferences("LoginPreferences", Activity.MODE_PRIVATE);
            return preferences.getBoolean("isLogged", false);
        }

        public static void setUserLoggedIn(Context context,User user)
        {
            final SharedPreferences preferences = context.getSharedPreferences("LoginPreferences", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLogged", true);

            Gson gson = new Gson();
            String json = gson.toJson(user);
            editor.putString("currentUser",json);
            editor.commit();

            Log.v("testTag","loggedIn");
        }

        public static void logout(Context context) {
            final SharedPreferences preferences = context.getSharedPreferences("LoginPreferences", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLogged", false);
            editor.putString("currentUser","");
            editor.commit();

            Log.v("testTag","loggedOut");
        }

        public static void saveUsernameAndPassword(Context context, String username, String password)
        {

        }

        public static User getUserLogged(Context context) {
            final SharedPreferences preferences = context.getSharedPreferences("LoginPreferences", Activity.MODE_PRIVATE);

            Gson gson = new Gson();
            String json = preferences.getString("currentUser", "");
            User user = gson.fromJson(json,User.class);

            return user;
        }
}
