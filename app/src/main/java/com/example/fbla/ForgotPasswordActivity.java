package com.example.fbla;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText resetEmail;
    Button sendResetBtn;
    TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetEmail = findViewById(R.id.resetEmail);
        sendResetBtn = findViewById(R.id.sendResetBtn);
        backToLogin = findViewById(R.id.backToLogin);

        sendResetBtn.setOnClickListener(v -> {
            String email = resetEmail.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                resetEmail.setError("Please enter a valid email address");
                return;
            }

            // Mock reset logic (no real email sending)
            Toast.makeText(this,
                    "If an account exists, a reset link has been sent.",
                    Toast.LENGTH_LONG).show();
        });

        backToLogin.setOnClickListener(v -> finish());
    }
}
