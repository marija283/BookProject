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

        public static boolean getReloadState(Context context){
            final SharedPreferences preferences = context.getSharedPreferences("ReloadPreferences", Activity.MODE_PRIVATE);
            Boolean reloadBool = preferences.getBoolean("activeReload", false);
            return  reloadBool;
        }

        public static boolean changeReloadState(Context context){
            final SharedPreferences preferences = context.getSharedPreferences("ReloadPreferences", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Boolean reloadBool = !preferences.getBoolean("activeReload", false);
            editor.putBoolean("activeReload", reloadBool);
            Log.v("testTag", "reloadot e :" + reloadBool.toString());
            editor.commit();
            return  reloadBool;
        }
        public static void setReloadState(Context context , boolean state){
            final SharedPreferences preferences = context.getSharedPreferences("ReloadPreferences", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activeReload", state);
            editor.commit();
        }

        public static boolean getServiceStatus(Context context){
            final SharedPreferences preferences = context.getSharedPreferences("ReloadPreferences", Activity.MODE_PRIVATE);
            Boolean serviceStatus = preferences.getBoolean("serviceStatus", false);
            return  serviceStatus;
        }
        public static void setServiceStatus(Context context , boolean state){
            final SharedPreferences preferences = context.getSharedPreferences("ReloadPreferences", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("serviceStatus", state);
            editor.commit();
        }
}
