package iss.edu.sg.autocalorietracker;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter4 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_FOOD = 0;
    final int VIEW_TYPE_ACTIVITY = 1;

    private ArrayList<Item> mItemList;
    private ArrayList<Activity> mItemList2;
    Context con;


    public Adapter4(ArrayList<Item> mItemList, ArrayList<Activity> mItemList2, Context con) {
        this.mItemList = mItemList;
        this.mItemList2 = mItemList2;
        this.con = con;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = null;
        if(viewType == VIEW_TYPE_FOOD){
            return new FoodViewHolder(itemView);
        }

        if(viewType == VIEW_TYPE_ACTIVITY){
            return new ActivityViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position){
        if(viewHolder instanceof FoodViewHolder){
            ((FoodViewHolder) viewHolder).populate(mItemList.get(position));
        }

        if(viewHolder instanceof ActivityViewHolder){
            ((ActivityViewHolder) viewHolder).populate(mItemList2.get(position - mItemList.size()));
        }
    }

    @Override
    public int getItemCount(){
        return 0;
//        return mItemList.size() + mItemList2.size();
    }

    @Override
    public int getItemViewType(int position){
        if(position < mItemList.size()){
            return VIEW_TYPE_FOOD;
        }

        if(position - mItemList.size() < mItemList2.size()){
            return VIEW_TYPE_ACTIVITY;
        }

        return -1;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        TextView calorie;
        public FoodViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.catalogue);
            image = itemView.findViewById(R.id.img);
            calorie = itemView.findViewById(R.id.kcal);
        }

        public void populate(Item item){
            name.setText(item.getName());
            calorie.setText(item.getTimestamp());

        }
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        TextView calorie;

        public ActivityViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.catalogue);
            image = itemView.findViewById(R.id.img);
            calorie = itemView.findViewById(R.id.kcal);
        }

        public void populate(Activity activity){
            name.setText(activity.getActivityName());
            image.setImageDrawable(activity.getActivityImg(R.drawable.cycling));
            calorie.setText(activity.getCalorieActivity());
        }
    }




//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView name;
//        ImageView image;
//        TextView calorie;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.catalogue);
//            image = itemView.findViewById(R.id.img);
//            calorie = itemView.findViewById(R.id.kcal);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.planitem, parent, false);
//        ViewHolder evh = new ViewHolder(v);
//        return evh;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        Item currentItem = mItemList.get(position);
//        URL url = null;
//        try {
//            String imageurl=currentItem.getImage();
//            String address = con.getString(R.string.address);
//            imageurl=imageurl.replace("localhost:8080",address+":8080");
//            url = new URL(imageurl);
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            holder.image.setImageBitmap(bmp);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        holder.name.setText(currentItem.getName());
//        holder.calorie.setText(currentItem.getCalorie());
//
//    }
//    @Override
//    public int getItemCount() {
//        try {
//            return mItemList.size()+1;
//        } catch (Exception ex){return 0;}
//    }
}






