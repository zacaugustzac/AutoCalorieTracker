package iss.edu.sg.autocalorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<Integer> images;
    List<String> name;
    List<String> calorie;
    List<String> timestamp;
    LayoutInflater inflater;

    public Adapter(List<Integer> images, List<String> name, List<String> calorie, List<String> timestamp, Context context) {
        this.images = images;
        this.name = name;
        this.calorie = calorie;
        this.timestamp = timestamp;
        this.inflater = inflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.historyitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(name.get(position));
        holder.images.setImageResource(images.get(position));
        holder.calorie.setText(calorie.get(position));
        holder.timestamp.setText(timestamp.get(position));
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return name.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView images;
        TextView calorie;
        TextView timestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.catalogue);
            images = itemView.findViewById(R.id.img);
            calorie = itemView.findViewById(R.id.kcal);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }

}
