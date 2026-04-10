package com.example.fbla;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.List;

public class
NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FEATURED = 1;
    private static final int TYPE_SMALL = 0;

    private final Context context;
    private final List<NewsItem> newsList;

    public NewsAdapter(Context context, List<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public int getItemViewType(int position) {
        return newsList.get(position).isFeatured() ? TYPE_FEATURED : TYPE_SMALL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        if (viewType == TYPE_FEATURED) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news_featured, parent, false);
            return new FeaturedHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news_small, parent, false);
            return new SmallHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position
    ) {
        NewsItem item = newsList.get(position);

        if (holder instanceof FeaturedHolder) {
            ((FeaturedHolder) holder).bind(item);
        } else {
            ((SmallHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // ================= FEATURED =================
    class FeaturedHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView category, title, description, date, readMore;

        FeaturedHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            category = itemView.findViewById(R.id.category);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            readMore = itemView.findViewById(R.id.readMore);
        }

        void bind(NewsItem item) {
            category.setText(item.getCategory());
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            date.setText(item.getDate());
            loadImage(item.getImage(), image);

            readMore.setOnClickListener(v ->
                    context.startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()))
                    )
            );
        }
    }

    // ================= SMALL =================
    class SmallHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView category, title, description, date, readMore;

        SmallHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            category = itemView.findViewById(R.id.category);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            readMore = itemView.findViewById(R.id.readMore);
        }

        void bind(NewsItem item) {
            category.setText(item.getCategory());
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            date.setText(item.getDate());
            loadImage(item.getImage(), image);

            readMore.setOnClickListener(v ->
                    context.startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()))
                    )
            );
        }
    }

    // ================= IMAGE LOADER =================
    private void loadImage(String fileName, ImageView imageView) {
        try {
            InputStream is = context.getAssets()
                    .open("news_images/" + fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);
            is.close();
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.ic_news); // fallback icon
        }
    }
}
