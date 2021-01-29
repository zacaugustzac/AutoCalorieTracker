package iss.edu.sg.autocalorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context context;


    public void ImageAdapter (Context context){

        this.context = context;

    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.historyitem, null);
            imageView = new ImageView(this.context);


        }
        else imageView = (ImageView)convertView;

        imageView.setBackgroundResource(R.drawable.back);

        return imageView;
    }
}