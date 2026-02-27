package com.example.fbla;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {

    private static final String PREF = "user_session";

    private final SharedPreferences prefs;

    public UserSessionManager(Context ctx) {
        prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void saveUser(
            String name,
            String email,
            String password,
            String grade,
            String gradYear,
            String chapter,
            String region,
            String state,
            boolean remember
    ) {
        prefs.edit()
                .putString("name", name)
                .putString("email", email)
                .putString("password", password)
                .putString("grade", grade)
                .putString("gradYear", gradYear)
                .putString("chapter", chapter)
                .putString("region", region)
                .putString("state", state)
                .putBoolean("remember", remember)
                .apply();
    }

    public boolean userExists() {
        return prefs.contains("email");
    }

    public boolean validateLogin(String email, String pass) {
        return email.equals(prefs.getString("email", "")) &&
                pass.equals(prefs.getString("password", ""));
    }

    public boolean isRemembered() {
        return prefs.getBoolean("remember", false);
    }

    public String getSavedEmail() {
        return prefs.getString("email", "");
    }

    public String getName() { return prefs.getString("name", ""); }
    public String getEmail() { return prefs.getString("email", ""); }
    public String getGrade() { return prefs.getString("grade", ""); }
    public String getGradYear() { return prefs.getString("gradYear", ""); }
    public String getChapter() { return prefs.getString("chapter", ""); }
    public String getRegion() { return prefs.getString("region", ""); }
    public String getState() { return prefs.getString("state", ""); }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
