package iss.edu.sg.autocalorietracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder> {
    private ArrayList<Item> mItemList2;
    Context con;

    public Adapter2(ArrayList<Item> exampleList, Context con) {
        mItemList2 = exampleList;
        this.con=con;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        TextView calorie;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.catalogue);
            image = itemView.findViewById(R.id.img);
            calorie = itemView.findViewById(R.id.kcal);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestionsitem, parent, false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item currentItem = mItemList2.get(position);
        URL url = null;
        try {
            String imageurl=currentItem.getImage();
            String address = con.getString(R.string.address);
            imageurl=imageurl.replace("localhost:8080",address+":8080");
            url = new URL(imageurl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.image.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.name.setText(currentItem.getName());
        holder.calorie.setText(currentItem.getCalorie());

    }
    @Override
    public int getItemCount() {
        try {
            return mItemList2.size();
        } catch (Exception ex){return 0;}
    }
}






