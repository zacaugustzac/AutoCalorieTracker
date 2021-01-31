package iss.edu.sg.autocalorietracker;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Item {

//    private Integer image;
    private String image;
    private String name;
    private String calorie;
    private String timestamp;

    public Item(String image, String name, String calorie, String timestamp) {
        this.image = image;
        this.name = name;
        this.calorie = calorie;
        this.timestamp = timestamp;
    }



    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
