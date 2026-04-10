package com.example.fbla;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText firstName, lastName, chapterName, regionNumber, email, password, confirmPassword;
    Spinner gradeSpinner, gradYearSpinner, stateSpinner;
    Button createAccountBtn;
    TextView backToLogin;

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        session = new UserSessionManager(this);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        chapterName = findViewById(R.id.chapterName);
        regionNumber = findViewById(R.id.regionNumber);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        gradeSpinner = findViewById(R.id.gradeSpinner);
        gradYearSpinner = findViewById(R.id.gradYearSpinner);
        stateSpinner = findViewById(R.id.stateSpinner);

        createAccountBtn = findViewById(R.id.createAccountBtn);
        backToLogin = findViewById(R.id.backToLogin);

        setupSpinners();

        backToLogin.setOnClickListener(v -> finish());

        createAccountBtn.setOnClickListener(v -> createAccount());

    }

    private void setupSpinners() {

        String[] grades = {"Select Grade", "9th", "10th", "11th", "12th"};
        gradeSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, grades));

        String[] years = {"Select Graduation Year", "2025", "2026", "2027", "2028", "2029", "2030"};
        gradYearSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, years));

        List<String> states = Arrays.asList(
                "Select State","Alabama","Alaska","Arizona","Arkansas","California","Colorado",
                "Connecticut","Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana",
                "Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan",
                "Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire",
                "New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma",
                "Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennessee",
                "Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"
        );

        stateSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, states));
    }

    private void createAccount() {

        String first = firstName.getText().toString().trim();
        String last = lastName.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String pass = password.getText().toString();
        String confirm = confirmPassword.getText().toString();

        String grade = gradeSpinner.getSelectedItem().toString();
        String gradYear = gradYearSpinner.getSelectedItem().toString();
        String state = stateSpinner.getSelectedItem().toString();
        String chapter = chapterName.getText().toString().trim();
        String region = regionNumber.getText().toString().trim();

        // ---------- BASIC VALIDATION ----------
        if (first.isEmpty() || last.isEmpty()) {
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (grade.equals("Select Grade")) {
            Toast.makeText(this, "Please select your grade", Toast.LENGTH_SHORT).show();
            return;
        }

        if (gradYear.equals("Select Graduation Year")) {
            Toast.makeText(this, "Please select your graduation year", Toast.LENGTH_SHORT).show();
            return;
        }

        if (state.equals("Select State")) {
            Toast.makeText(this, "Please select your state", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Please use a valid email address");
            return;
        }

        if (!isValidPassword(pass)) {
            password.setError("Password must contain 1 number and 1 special character (_ or *)");
            return;
        }

        if (!pass.equals(confirm)) {
            confirmPassword.setError("Passwords do not match");
            return;
        }

        // ---------- SAVE USER ----------
        String fullName = first + " " + last;

        session.saveUser(
                fullName,
                emailText,
                pass,
                grade,
                gradYear,
                chapter,
                region,
                state,
                true
        );

        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[_*]).{6,}$");
        return pattern.matcher(password).matches();
    }
}
