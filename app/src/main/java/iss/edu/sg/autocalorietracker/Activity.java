package iss.edu.sg.autocalorietracker;

import android.graphics.drawable.Drawable;

public class Activity {
    private Long id;
    private Drawable activityImg;
    private String activityName;
    private String calorieActivity;

    public Activity(Long id, Drawable activityImg, String activityName, String calorieActivity) {
        this.id = id;
        this.activityImg = activityImg;
        this.activityName = activityName;
        this.calorieActivity = calorieActivity;
    }

    public Long getId() {
        return id;
    }

    public Drawable getActivityImg(int cycling) {
        return activityImg;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getCalorieActivity() {
        return calorieActivity;
    }

}
