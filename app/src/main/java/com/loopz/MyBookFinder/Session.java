package com.loopz.MyBookFinder;

import android.content.Context;
import android.content.SharedPreferences;


public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void addlogin(boolean logggedin,String userid, String username){
        editor.putBoolean("loggedInmode",logggedin);
        editor.putString("userid",userid);
        editor.putString("username",username);
        editor.commit();
    }
    public String getuserid() {
        return prefs.getString("userid", null);
    }
    public String getusername() {
        return prefs.getString("username", null);
    }


    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }
}