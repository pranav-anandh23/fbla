package com.example.fbla;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResourcesFragment extends Fragment {

    private ResourceAdapter adapter;
    private String currentFilter = "All";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.activity_resources, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        EditText searchInput = view.findViewById(R.id.searchInput);
        RecyclerView resourcesRecycler = view.findViewById(R.id.resourcesRecycler);

        Button filterAll = view.findViewById(R.id.filterAll);
        Button filterStudy = view.findViewById(R.id.filterStudy);
        Button filterVideo = view.findViewById(R.id.filterVideo);
        Button filterRubric = view.findViewById(R.id.filterRubric);

        List<ResourceItem> resourceList = loadResourcesFromAssets();

        adapter = new ResourceAdapter(resourceList);
        resourcesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        resourcesRecycler.setAdapter(adapter);
        adapter.filter("", "All");

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString(), currentFilter);
            }

            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
        });

        filterAll.setOnClickListener(v -> {
            currentFilter = "All";
            setSelected(filterAll, filterStudy, filterVideo, filterRubric);
            adapter.filter(searchInput.getText().toString(), currentFilter);
        });

        filterStudy.setOnClickListener(v -> {
            currentFilter = "Study Guide";
            setSelected(filterStudy, filterAll, filterVideo, filterRubric);
            adapter.filter(searchInput.getText().toString(), currentFilter);
        });

        filterVideo.setOnClickListener(v -> {
            currentFilter = "Video";
            setSelected(filterVideo, filterAll, filterStudy, filterRubric);
            adapter.filter(searchInput.getText().toString(), currentFilter);
        });

        filterRubric.setOnClickListener(v -> {
            currentFilter = "Rubric";
            setSelected(filterRubric, filterAll, filterStudy, filterVideo);
            adapter.filter(searchInput.getText().toString(), currentFilter);
        });
    }

    /* ---------------- HELPERS ---------------- */

    private List<ResourceItem> loadResourcesFromAssets() {
        List<ResourceItem> list = new ArrayList<>();

        try {
            String[] files = requireContext().getAssets().list("resources");
            if (files == null) return list;

            for (String file : files) {
                String title = file.replace(".pdf", "").replace("-", " ");

                String type;
                if (title.toLowerCase().contains("rubric")) {
                    type = "Rubric";
                } else if (title.toLowerCase().contains("video")) {
                    type = "Video";
                } else {
                    type = "Study Guide";
                }

                list.add(new ResourceItem(
                        title,
                        "Official FBLA resource",
                        type,
                        file,
                        (int) (Math.random() * 3000 + 200)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private void setSelected(Button selected, Button... others) {
        selected.setBackgroundTintList(
                requireContext().getColorStateList(R.color.blue));
        selected.setTextColor(
                requireContext().getColor(R.color.white));

        for (Button b : others) {
            b.setBackgroundTintList(
                    requireContext().getColorStateList(R.color.blue));
            b.setTextColor(
                    requireContext().getColor(R.color.black));
        }
    }
}
