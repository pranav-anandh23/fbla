package com.example.fbla;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {

    private final List<ResourceItem> allItems;
    private final List<ResourceItem> filteredItems;

    public ResourceAdapter(List<ResourceItem> items) {
        this.allItems = items;
        this.filteredItems = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resource, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResourceItem item = filteredItems.get(position);

        holder.title.setText(item.title);
        holder.description.setText(item.description);
        holder.downloads.setText(item.downloads + " downloads");

        // Button label
        String buttonText =
                item.type.equalsIgnoreCase("Video") ? "Watch" :
                        item.type.equalsIgnoreCase("Rubric") ? "View Rubric" :
                                "Open";

        holder.actionBtn.setText(buttonText);

        // Force blue button + white text
        holder.actionBtn.setBackgroundTintList(
                ColorStateList.valueOf(
                        holder.itemView.getContext().getColor(R.color.blue)
                )
        );
        holder.actionBtn.setTextColor(
                holder.itemView.getContext().getColor(android.R.color.white)
        );

        // Click → open PDF
        holder.actionBtn.setOnClickListener(v -> {
            openPdfFromAssets(v.getContext(), item.fileName);
        });
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    public void filter(String query, String type) {
        filteredItems.clear();
        query = query.toLowerCase();

        for (ResourceItem item : allItems) {

            boolean matchesText =
                    item.title.toLowerCase().contains(query) ||
                            item.description.toLowerCase().contains(query);

            boolean matchesType =
                    type.equalsIgnoreCase("All") ||
                            item.type.equalsIgnoreCase(type);

            if (matchesText && matchesType) {
                filteredItems.add(item);
            }
        }
        notifyDataSetChanged();
    }

    // =========================
    // PDF OPENING LOGIC
    // =========================
    private void openPdfFromAssets(Context context, String fileName) {
        try {
            File file = new File(context.getCacheDir(), fileName);

            if (!file.exists()) {
                InputStream input = context.getAssets().open("resources/" + fileName);
                FileOutputStream output = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                input.close();
                output.close();
            }

            Uri uri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    file
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to open file", Toast.LENGTH_SHORT).show();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, downloads;
        Button actionBtn;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            downloads = itemView.findViewById(R.id.downloads);
            actionBtn = itemView.findViewById(R.id.actionBtn);
        }
    }
}
