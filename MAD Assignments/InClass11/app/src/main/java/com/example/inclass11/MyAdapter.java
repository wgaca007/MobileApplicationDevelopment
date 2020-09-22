package com.example.inclass11;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

private Bitmap image;
private LayoutInflater inflater;
private ItemClickListener itemClickListener;

    public MyAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MyAdapter(MainActivity mainActivity, Bitmap bitmapUpload) {
        this.inflater= LayoutInflater.from(mainActivity);
        this.image=bitmapUpload;
    }



    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
     holder.image.setImageBitmap(image);
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            image.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.OnItemLongClick(view,getAdapterPosition());
            return false;
        }
    }
    public interface ItemClickListener{
    void OnItemLongClick(View view,int Position);
    }
}
