package iss.edu.sg.autocalorietracker;

public class Person {
    String email;
    String password;
    String year;
    String activity;
    String gender;
    String avgheight;
    String avgweight;
    String reminderKcal;

    public Person(String email,String password,String year,String activity,String gender,String avgheight,String avgweight){
        this.email=email;
        this.activity=activity;
        this.password=password;
        this.year=year;
        this.gender=gender;
        this.avgheight=avgheight;
        this.avgweight=avgweight;
    }

    public Person(String email, String year, String activity, String gender, String avgheight, String avgweight) {
        this.email = email;
        this.year = year;
        this.activity = activity;
        this.gender = gender;
        this.avgheight = avgheight;
        this.avgweight = avgweight;
    }

    public Person(String email, String reminderKcal) {
        this.email = email;
        this.reminderKcal = reminderKcal;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getYear() {
        return year;
    }

    public String getActivity() {
        return activity;
    }

    public String getGender() {
        return gender;
    }

    public String getAvgheight() {
        return avgheight;
    }

    public String getAvgweight() {
        return avgweight;
    }

    public String getReminderKcal() {
        return reminderKcal;
    }
}
