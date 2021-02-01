package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.TimeZone;
import java.time.LocalDate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> mItemList;
    //test  test

//
//    private List<Integer> images;
//    private List<String> name;
//    private List<String> calorie;
//    private List<String> timestamp;

    private Button share,edit,delete;

    //variables for menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ImageView menuIcon;
    private Button share1;
    private Button editText;
    private Button remove;
    private TextView datenow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        datenow=findViewById(R.id.editDate);
        datenow.setText(""+LocalDate.now());
        //hooks
        drawerLayout =findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menu);

        //Navigation Drawer Menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawer();

//        mRecyclerView = findViewById(R.id.GridView);
        //TODO need to be replaced by session instead of hardcoding
        System.out.println("it is calling create item list");
        creatItemList(LocalDate.now(),"ZAC@GMAIL.COM");
//        buildRecyclerView();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        name = new ArrayList<>();
//        images = new ArrayList<>();
//        calorie = new ArrayList<>();
//        timestamp = new ArrayList<>();
//        addImages();
//        addNames();
//        addCalories();
//        addTimestamps();

//        Adapter adapter = new Adapter(images, name, calorie, timestamp, this);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
//        itemList.setLayoutManager(gridLayoutManager);
//        itemList.setAdapter(adapter);
//        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
//            @Override
//            public void onShareClick(int position) {
//
//            }
//
//            @Override
//            public void onEditClick(int position) {
//
//            }
//
//            @Override
//            public void onDeleteClick(int position) {
//                removeItem(position);
//            }
//        });
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.GridView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        Adapter mAdapter = new Adapter(mItemList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onShareClick(int position) {

            }

            @Override
            public void onEditClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                mItemList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });
    }





    //set the drawer
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }


    //set the item in menu bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                Intent intent0 = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent0);
                break;
            case R.id.nav_profile:
                Intent intent1 = new Intent(HistoryActivity.this, ProfileActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_plan:
                Intent intent2 = new Intent(HistoryActivity.this, PlanActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_reminders:
                Intent intent3 = new Intent(HistoryActivity.this, RemindersActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_share:
                Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_rate:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void creatItemList(LocalDate date,String email){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url="http://10.0.2.2:8080/history/getTodayHistory?date="+date+"&email="+email;
        System.out.println("url="+url);
        mItemList=new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: "+ response.toString());
                        JSONArray result= null;

                        try {
                            result = new JSONArray(response.toString());

                            for(int x=0;x<result.length();x++){
                               JSONObject ans=result.getJSONObject(x);
//                               String name=ans.getJSONObject("food").getString("name");
//                               double calorie=ans.getJSONObject("food").getDouble("calorie");
                                String name=ans.getString("foodName");
                                double calorie=ans.getDouble("calorie");
                               Long time=ans.getLong("epochTime");
                                Date date = new Date(time);
                                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                format.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                                String formatted = format.format(date);


                              mItemList.add(new Item(ans.getString("url"),name,""+calorie,formatted));
                              buildRecyclerView();

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(HistoryActivity.this,"Retrieved successfully",Toast.LENGTH_SHORT).show();
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(HistoryActivity.this, "Something wrong happens", Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(stringRequest);



//        mItemList=new ArrayList<>();
        //mItemList.add(new Item("http://localhost:8080/api/image/ZAC@GMAIL.COM_1612085120335.png","Egg(boild)","78 kcal","7am"));
        //mItemList.add(new Item(R.drawable.egg,"Egg(boild)","78 kcal","7am"));
    }


//    private void addImages() {
//        images.add(R.drawable.egg);
//        images.add(R.drawable.egg);
//        images.add(R.drawable.egg);
//    }
//
//    private void addNames() {
//        name.add("Egg(boild)");
//        name.add("Egg(boild)");
//        name.add("Egg(boild)");
//    }
//
//    private void addCalories() {
//        calorie.add("78 kcal");
//        calorie.add("78 kcal");
//        calorie.add("78 kcal");
//    }
//
//    private void addTimestamps() {
//        timestamp.add("7am");
//        timestamp.add("7am");
//        timestamp.add("7am");
//    }

}