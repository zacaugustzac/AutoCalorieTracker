package iss.edu.sg.autocalorietracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Item> mItemList;

//    List<Integer> images;
//    List<String> name;
//    List<String> calorie;
//    List<String> timestamp;
//    LayoutInflater inflater;

    private OnItemClickListener mListener;

    public Adapter(ArrayList<Item> exampleList) {
        mItemList = exampleList;
    }
    public interface OnItemClickListener {
        void onShareClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

//    public Adapter(List<Integer> images, List<String> name, List<String> calorie, List<String> timestamp, Context context) {
//        this.images = images;
//        this.name = name;
//        this.calorie = calorie;
//        this.timestamp = timestamp;
//        this.inflater = inflater.from(context);
//    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        TextView calorie;
        TextView timestamp;
        ImageView share;
        ImageView edit;
        ImageView delete;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.catalogue);
            image = itemView.findViewById(R.id.img);
            calorie = itemView.findViewById(R.id.kcal);
            timestamp = itemView.findViewById(R.id.timestamp);
            share  = itemView.findViewById(R.id.share);
            edit  = itemView.findViewById(R.id.edit);
            delete  = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.historyitem, parent, false);
        ViewHolder evh = new ViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item currentItem = mItemList.get(position);
        URL url = null;
        try {
            String imageurl=currentItem.getImage();
            imageurl=imageurl.replace("localhost:8080","10.0.2.2:8080");
            url = new URL(imageurl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.image.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //holder.image.setImageResource(currentItem.getImage());
        holder.name.setText(currentItem.getName());
        holder.calorie.setText(currentItem.getCalorie());
        holder.timestamp.setText(currentItem.getTimestamp());
    }
    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}










//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.historyitem, parent, false);
//        ViewHolder vh = new ViewHolder(view, mListener);
//        return vh;
//    }



//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.name.setText(name.get(position));
//        holder.images.setImageResource(images.get(position));
//        holder.calorie.setText(calorie.get(position));
//        holder.timestamp.setText(timestamp.get(position));
//    }
//
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public int getItemCount() {
//        return name.size();
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public View deleteExercise;
//        public View edit_t;
//        TextView name;
//        ImageView images;
//        TextView calorie;
//        TextView timestamp;
//        ImageView share;
//        ImageView edit;
//        ImageView delete;
//
//        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
//            super(itemView);
//            name = itemView.findViewById(R.id.catalogue);
//            images = itemView.findViewById(R.id.img);
//            calorie = itemView.findViewById(R.id.kcal);
//            timestamp = itemView.findViewById(R.id.timestamp);
//            share  = itemView.findViewById(R.id.share);
//            edit  = itemView.findViewById(R.id.edit);
//            delete  = itemView.findViewById(R.id.delete);
//
//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.onDeleteClick(position);
//                        }
//                    }
//                }
//            });
//        }
//    }
//}
