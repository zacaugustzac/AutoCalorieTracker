package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class PlanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView, mRecyclerView2;
    private RecyclerView.LayoutManager mLayoutManager, mLayoutManager2;
    private ArrayList<Item> mItemList;
    private ArrayList<Activity> mItemList2;
    private String ROOT_URL_plan;
    private Adapter4 mAdapter;
    private Adapter4 mAdapter2;
    private TextView datenow, datetmr, todayKcal, tmrKcal;
    //variables for menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView menuIcon;
    private LocalDate today;
    private LocalDate tomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        ROOT_URL_plan = "http://" + getString(R.string.address) + ":8080/plan/showTodayPlanAndroid?email=";

        todayKcal = findViewById(R.id.todayKcal);
        tmrKcal = findViewById(R.id.tmrKcal);

        //get today date, tml date and email
        today = LocalDate.now();
        datenow = findViewById(R.id.todayDate);
        datenow.setText("" + today  + "(Today)");
        tomorrow = LocalDate.now().plusDays(1);
        datetmr = findViewById(R.id.tmrDate);
        Calendar cal1 = new GregorianCalendar();
        cal1.add(Calendar.DATE, 1);
        datetmr.setText(new SimpleDateFormat("yyyy/MM/dd").format(cal1.getTime()) + "(Tomorrow)");

        SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String useremail = sharedPref.getString("email", null);

        //hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menu);

        //tool bar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawer();

        System.out.println("it is calling create item list");
        buildRecyclerView();

        retrieveItemList(today, useremail);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    //pass the data and useremail
    private void retrieveItemList(LocalDate date, String email) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ROOT_URL_plan + email + "&date=" + date;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: " + response.toString());
                        JSONObject result = null;
                        try {
                            result = new JSONObject(response);
                            //activity
                            String activity = result.getString("activity");
                            System.out.println("activity is: " + activity);

                            //food
                            JSONArray resultFoodImages = new JSONArray();
                            resultFoodImages = result.getJSONArray("resultFoodImages");
                            System.out.println("resultFoodImages is: " + resultFoodImages);
                            for(int i = 0; i<resultFoodImages.length();i++){
                                System.out.println("resultFoodImages get obj is: " + resultFoodImages.getJSONObject(i));
                                String foodurl = resultFoodImages.getJSONObject(i).getString("url");
                                String foodname = resultFoodImages.getJSONObject(i).getString("foodName");
                                String foodcalorie = resultFoodImages.getJSONObject(i).getString("calorie");
                                String fooddate = null;
                                System.out.println("food1url is: " + foodurl);
                                System.out.println("food1name is: " + foodname);
                                System.out.println("food1calorie is: " + foodcalorie);
                                mItemList.add(new Item((long)1, foodurl, foodname, "" + foodcalorie, fooddate));
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(PlanActivity.this, "retrieved successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(PlanActivity.this, "Something wrong happens", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    //build the recyclerview
    private void buildRecyclerView() {
        mItemList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.GridView);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new Adapter4(mItemList, mItemList2, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mItemList2 = new ArrayList<>();
        mRecyclerView2 = findViewById(R.id.GridView2);
        mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView2.setHasFixedSize(true);
        mAdapter2 = new Adapter4(mItemList, mItemList2, this);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setAdapter(mAdapter2);
    }

    //set the drawer
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }


    //set the item in menu bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intent0 = new Intent(this, MainActivity.class);
                startActivity(intent0);
                break;
            case R.id.nav_profile:
                Intent intent1 = new Intent(this, ProfileActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_plan:
                Intent intent2 = new Intent(this, PlanActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_reminders:
                Intent intent3 = new Intent(this, ReminderActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_logout:
                Intent intent4 = new Intent(this, FlashActivity.class);
                startActivity(intent4);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}