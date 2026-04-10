package com.example.fbla;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {

    private static final String PREF = "user_session";

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_GRADE = "grade";
    private static final String KEY_GRAD_YEAR = "gradYear";
    private static final String KEY_CHAPTER = "chapter";
    private static final String KEY_REGION = "region";
    private static final String KEY_STATE = "state";

    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_LOGGED_IN = "loggedIn";

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
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email)
                .putString(KEY_PASSWORD, password)
                .putString(KEY_GRADE, grade)
                .putString(KEY_GRAD_YEAR, gradYear)
                .putString(KEY_CHAPTER, chapter)
                .putString(KEY_REGION, region)
                .putString(KEY_STATE, state)
                .putBoolean(KEY_REMEMBER, remember)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public boolean userExists() {
        return prefs.contains(KEY_EMAIL) && !prefs.getString(KEY_EMAIL, "").isEmpty();
    }

    public boolean validateLogin(String email, String pass) {
        return email.equals(prefs.getString(KEY_EMAIL, "")) &&
                pass.equals(prefs.getString(KEY_PASSWORD, ""));
    }

    public boolean isRemembered() {
        return prefs.getBoolean(KEY_REMEMBER, false);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public String getSavedEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getName() {
        return prefs.getString(KEY_NAME, "");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getPassword() {
        return prefs.getString(KEY_PASSWORD, "");
    }

    public String getGrade() {
        return prefs.getString(KEY_GRADE, "");
    }

    public String getGradYear() {
        return prefs.getString(KEY_GRAD_YEAR, "");
    }

    public String getChapter() {
        return prefs.getString(KEY_CHAPTER, "");
    }

    public String getRegion() {
        return prefs.getString(KEY_REGION, "");
    }

    public String getState() {
        return prefs.getString(KEY_STATE, "");
    }

    public String getUserName() {
        return prefs.getString(KEY_NAME, "");
    }

    // Sign out only. Keep the saved account.
    public void logout() {
        prefs.edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .putBoolean(KEY_REMEMBER, false)
                .apply();
    }

    // Delete the account completely.
    public void clearSession() {
        prefs.edit().clear().apply();
    }
}