package iss.edu.sg.autocalorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter3 extends RecyclerView.Adapter<Adapter3.ViewHolder> {

    private ArrayList<Item> mItemList2;
    Context con;

    public Adapter3(ArrayList<Item> mItemList2, Context con) {
        this.mItemList2 = mItemList2;
        this.con = con;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.planitem, parent, false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }



    @Override
    public void onBindViewHolder(Adapter3.ViewHolder holder, int position) {
        Item currentItem = mItemList2.get(position);
        holder.image.setImageResource(Integer.valueOf(currentItem.getImage()));
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






