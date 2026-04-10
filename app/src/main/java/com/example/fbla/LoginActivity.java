package com.example.fbla;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    CheckBox rememberMe;
    Button loginButton;
    TextView createAccount, forgotPassword;

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        rememberMe = findViewById(R.id.rememberMeCheckbox);
        loginButton = findViewById(R.id.loginButton);
        createAccount = findViewById(R.id.createAccount);
        forgotPassword = findViewById(R.id.forgotPassword);

        session = new UserSessionManager(this);

        Log.d("LOGIN_DEBUG", "onCreate userExists = " + session.userExists() + ", email = " + session.getEmail());

        if (session.userExists()) {
            emailInput.setText(session.getSavedEmail());
        }

        if (session.isRemembered()) {
            rememberMe.setChecked(true);
        }

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!session.userExists()) {
                Toast.makeText(this, "No account found. Please create one.", Toast.LENGTH_LONG).show();
                return;
            }

            if (session.validateLogin(email, password)) {
                session.saveUser(
                        session.getName(),
                        session.getEmail(),
                        session.getPassword(),
                        session.getGrade(),
                        session.getGradYear(),
                        session.getChapter(),
                        session.getRegion(),
                        session.getState(),
                        rememberMe.isChecked()
                );

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
            }
        });

        createAccount.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        forgotPassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }
}