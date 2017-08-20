package model_class;


import android.content.Context;
import android.content.SharedPreferences;


public class LoginActivityManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String pref_name = "AttendanceServer";
    private static final String IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";
    private static final String USERNAME = "username";


    public LoginActivityManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setFirstLaunch(boolean is_first_time) {
        editor.putBoolean(IS_FIRST_LAUNCH, is_first_time);
        editor.commit();
    }

    public boolean isFirstTime() {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true);
    }

    public void setUserName(String userName) {
        editor.putString(USERNAME, userName);
        editor.commit();
    }

    public String getUsername(){
        return preferences.getString(USERNAME,"default_user");
    }

    public void setPassword(String password){
        editor.putString("password",password);
        editor.commit();
    }

    public String getPassword(){
        return preferences.getString("password","secret");
    }

}

