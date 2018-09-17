package com.example.tamoor.DarkLight;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

    List<ImageModel> imageModelList;
    Context ctx;
    private android.content.res.Resources res;

    public ImagesAdapter(List<ImageModel> list, Context context) {
        imageModelList = list;
        ctx = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        GifImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image_gif);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(ctx).inflate(R.layout.gifs_dialog_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.imageView.setImageResource(imageModelList.get(position).getImage());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewImage(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageModelList.size();
    }

    private void viewImage(int pos) {
        Intent intent = new Intent(ctx, ImageActivity.class);
        intent.putExtra("image", imageModelList.get(pos).getImage());
        ctx.startActivity(intent);
    }

}
