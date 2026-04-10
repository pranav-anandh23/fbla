package com.example.fbla;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreatePostActivity extends AppCompatActivity {

    private Spinner postTypeSpinner;
    private EditText etEventName, etPlacement, etDetails;
    private Button btnCancelPost, btnSharePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        postTypeSpinner = findViewById(R.id.postTypeSpinner);
        etEventName = findViewById(R.id.etEventName);
        etPlacement = findViewById(R.id.etPlacement);
        etDetails = findViewById(R.id.etDetails);
        btnCancelPost = findViewById(R.id.btnCancelPost);
        btnSharePost = findViewById(R.id.btnSharePost);

        String[] postTypes = {
                "Qualification",
                "Placement",
                "Announcement",
                "Chapter Update",
                "Custom"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                postTypes
        );
        postTypeSpinner.setAdapter(adapter);

        btnCancelPost.setOnClickListener(v -> finish());

        btnSharePost.setOnClickListener(v -> sharePost());
    }

    private void sharePost() {
        String postType = postTypeSpinner.getSelectedItem().toString();
        String eventName = etEventName.getText().toString().trim();
        String placement = etPlacement.getText().toString().trim();
        String details = etDetails.getText().toString().trim();

        if (eventName.isEmpty()) {
            etEventName.setError("Required");
            return;
        }

        String postText = buildPostText(postType, eventName, placement, details);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "FBLA Update");
        shareIntent.putExtra(Intent.EXTRA_TEXT, postText);

        startActivity(Intent.createChooser(shareIntent, "Share your post"));
    }

    private String buildPostText(String postType, String eventName, String placement, String details) {
        UserSessionManager session = new UserSessionManager(this);
        String name = session.getName();
        String chapter = session.getChapter();
        String state = session.getState();

        switch (postType) {
            case "Qualification":
                return "We’re excited to share that " + name +
                        " has qualified for " + eventName +
                        ". Proud to represent " + chapter +
                        (state.isEmpty() ? "" : " in " + state) +
                        ".\n\n" +
                        (details.isEmpty() ? "" : details + "\n\n") +
                        "#FBLA #Leadership #Competition";

            case "Placement":
                return "Celebrating a great result at " + eventName + "!\n\n" +
                        name +
                        (placement.isEmpty() ? "" : " earned " + placement) +
                        ". Proud of this accomplishment for " + chapter +
                        (state.isEmpty() ? "" : " in " + state) +
                        ".\n\n" +
                        (details.isEmpty() ? "" : details + "\n\n") +
                        "#FBLA #Achievement #Competition";

            case "Announcement":
                return "FBLA Announcement: " + eventName + "\n\n" +
                        (details.isEmpty() ? "Stay tuned for more updates from our chapter." : details) +
                        "\n\n#FBLA #ChapterNews";

            case "Chapter Update":
                return "Chapter Update from " + chapter +
                        (state.isEmpty() ? "" : ", " + state) +
                        ":\n\n" +
                        eventName + "\n\n" +
                        (details.isEmpty() ? "" : details + "\n\n") +
                        "#FBLA #ChapterUpdate #Leadership";

            case "Custom":
            default:
                return eventName + "\n\n" +
                        (placement.isEmpty() ? "" : placement + "\n\n") +
                        details;
        }
    }
}