package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.net.Uri;
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

import java.time.LocalDateTime;
import java.util.TimeZone;
import java.time.LocalDate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManager2;
    private ArrayList<Item> mItemList;
    private ArrayList<Item> mItemList2;
    //TODO later need to based session registration
    private String useremail = "ZAC@GMAIL.COM";

    //variables for menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ImageView menuIcon;
    private TextView datenow;
    private TextView totalcalorie;
    private TextView remcalorie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        datenow = findViewById(R.id.editDate);
        datenow.setText("" + LocalDate.now());
        totalcalorie = findViewById(R.id.textView16);
        remcalorie = findViewById(R.id.textView18);
        //hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menu);

        mRecyclerView = findViewById(R.id.GridView);
        mRecyclerView2 = findViewById(R.id.GridView2);

        //Navigation Drawer Menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawer();

        System.out.println("it is calling create item list");
        retrieveItemList(LocalDate.now(), useremail);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);




    }

    public void buildRecyclerView() {

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        Adapter mAdapter = new Adapter(mItemList);
        mRecyclerView.setAdapter(mAdapter);


        mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setHasFixedSize(true);
        Adapter2 mAdapter2 = new Adapter2(mItemList2);
        mRecyclerView2.setAdapter(mAdapter2);

        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onShareClick(int position) {
                //TODO how to test this on real facebook/ instagram app to make sure it is working
                Item a= mItemList.get(position);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                Uri picUri = Uri.parse(a.getImage());
                System.out.println(a.getImage());
                //"https://images.deliveryhero.io/image/fd-sg/Products/5514328.jpg?width=302"
                //.replace("localhost:8080","10.0.2.2:8080");
                shareIntent.putExtra(Intent.EXTRA_STREAM,picUri);
                shareIntent.setType("image/png");
                startActivity(Intent.createChooser(shareIntent, "Send To"));
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onEditClick(int position) {
                Item a= mItemList.get(position);
                Intent intent=new Intent(HistoryActivity.this,HistoryEditActivity.class);
                intent.putExtra("item",a);
                startActivity(intent);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDeleteClick(int position) {
                Item a= mItemList.get(position);
                Long id=a.getId();
                deleteImage(id,position);
                mItemList.remove(position);
                double currentsum=Double.valueOf(totalcalorie.getText().toString());
                currentsum=currentsum-Double.valueOf(a.getCalorie());
                totalcalorie.setText(""+currentsum);
                double curremainder=Double.valueOf(remcalorie.getText().toString().split(" Kcal")[0]);
                curremainder+=Double.valueOf(a.getCalorie().toString());
                remcalorie.setText(curremainder+" Kcal left for today");
                mAdapter.notifyItemRemoved(position);

            }
        });
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
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_rate:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deleteImage(Long id, int position) {
        //TODO in the java part later
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/history/deleteImage/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(HistoryActivity.this, "removed successfully", Toast.LENGTH_SHORT).show();
                        double curremainder=Double.valueOf(remcalorie.getText().toString().split(" Kcal")[0]);
                        showRecommendation(curremainder);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(HistoryActivity.this, "Something wrong happens", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void showRecommendation(double remainder) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/api/food/getSuggestion?remainder=" + remainder;
        System.out.println("url=" + url);
        mItemList2 = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray result = null;
                        try {
                            result = new JSONArray(response);

                            for (int x = 0; x < result.length(); x++) {
                                JSONObject ans = result.getJSONObject(x);
                                String name = ans.getString("foodName");
                                String url = ans.getString("url");
                                double calorie = ans.getDouble("calorie");
                                mItemList2.add(new Item(url, name, "" + calorie));

                            }
                            buildRecyclerView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(HistoryActivity.this, "retrieved successfully", Toast.LENGTH_SHORT).show();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(HistoryActivity.this, "Something wrong happens", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


    public void retrieveItemList(LocalDate date, String email) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/history/getTodayHistory?date=" + date + "&email=" + email;
        System.out.println("url=" + url);
        mItemList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: " + response.toString());
                        JSONArray result = null;
                        double sum = 0;
                        double threshold = 0;
                        try {
                            result = new JSONArray(response);

                            for (int x = 0; x < result.length(); x++) {
                                JSONObject ans = result.getJSONObject(x);
//                               String name=ans.getJSONObject("food").getString("name");
//                               double calorie=ans.getJSONObject("food").getDouble("calorie");
                                String name = ans.getString("foodName");
                                Long id = ans.getLong("id");
                                double calorie = ans.getDouble("calorie");
                                sum += calorie;
                                threshold = ans.getJSONObject("dailyHistory").getJSONObject("user").getDouble("recommendedCalories");
                                Long time = ans.getLong("epochTime");
                                Date date = new Date(time);
                                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                format.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                                String formatted = format.format(date);
                                mItemList.add(new Item(id, ans.getString("url"), name, "" + calorie, formatted));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        totalcalorie.setText("" + sum);
                        double remainder = threshold - sum;
                        remcalorie.setText(remainder + " Kcal left for today");
                        showRecommendation(remainder);
                        //Toast.makeText(HistoryActivity.this,"Retrieved successfully",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(HistoryActivity.this, "Something wrong happens", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


}