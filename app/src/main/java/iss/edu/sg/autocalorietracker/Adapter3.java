package iss.edu.sg.autocalorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter3 extends RecyclerView.Adapter<Adapter3.ViewHolder> {
    final int VIEW_TYPE_FOOD = 0;
    final int VIEW_TYPE_ACTIVITY = 1;

    private ArrayList<Item> mItemList2;
//    private ArrayList<Activity> mItemList2;
    Context con;

    public Adapter3(ArrayList<Item> mItemList2, Context con) {
        this.mItemList2 = mItemList2;
//        this.mItemList2 = mItemList2;
        this.con = con;
    }


//    public Adapter4(ArrayList<Item> mItemList, ArrayList<Activity> mItemList2, Context con) {
//        this.mItemList = mItemList;
//        this.mItemList2 = mItemList2;
//        this.con = con;
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
//        ImageView image;
        TextView calorie;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.catalogue);
//            image = itemView.findViewById(R.id.img);
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

        holder.name.setText(currentItem.getName());
        holder.calorie.setText(currentItem.getCalorie());

    }
    @Override
    public int getItemCount() {
        try {
            return mItemList2.size();
        } catch (Exception ex){return 0;}
    }



//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.planitem, parent, false);
//        if(viewType == VIEW_TYPE_FOOD){
//            System.out.println("foodView");
//            return new FoodViewHolder(itemView);
//
//        }
//
//        if(viewType == VIEW_TYPE_ACTIVITY){
//            System.out.println("activityView");
//            return new ActivityViewHolder(itemView);
//        }
//
//
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position){
////        if(position == mItemList.size()){
////            ((ActivityViewHolder) viewHolder).populate(mItemList2.get(position ));
////        }
//
//        if(viewHolder instanceof FoodViewHolder){
//            ((FoodViewHolder) viewHolder).populate(mItemList.get(position));
//        }
//
//        if(viewHolder instanceof ActivityViewHolder){
//            ((ActivityViewHolder) viewHolder).populate(mItemList2.get(position - mItemList.size()));
//        }
//    }
//
//    @Override
//    public int getItemCount(){
////        return 0;
//        return mItemList.size() + mItemList2.size();
////            return mItemList.size();
////        try {
////            return mItemList.size();
////        } catch (Exception ex){return 0;}
//    }
//
//    public class FoodViewHolder extends RecyclerView.ViewHolder {
//        TextView name;
//        //ImageView image;
//        TextView calorie;
//        public FoodViewHolder(View itemView){
//            super(itemView);
//
//            name = itemView.findViewById(R.id.catalogue);
//            //image = itemView.findViewById(R.id.img);
//            calorie = itemView.findViewById(R.id.kcal);
//        }
//
//        public void populate(Item item){
//            name.setText(item.getName());
//            calorie.setText(item.getCalorie());
//
//        }
//    }
//
//    public class ActivityViewHolder extends RecyclerView.ViewHolder {
//        TextView name;
//        //ImageView image;
//        TextView calorie;
//
//        public ActivityViewHolder(View itemView){
//            super(itemView);
//
//            name = itemView.findViewById(R.id.activityCatalogue);
//            //image = itemView.findViewById(R.id.img);
//            calorie = itemView.findViewById(R.id.activityKcal);
//        }
//
//        public void populate(Activity activity){
//            name.setText(activity.getActivityName());
//            //image.setImageDrawable(activity.getActivityImg(R.drawable.cycling));
//            calorie.setText(activity.getCalorieActivity());
//        }
//    }

}






