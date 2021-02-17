package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    private String ROOT_URL_delete;
    private String ROOT_URL_recommend;
    private String ROOT_URL_history ;
    //variables for menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ImageView menuIcon;
    private Adapter mAdapter;
    private Adapter2 mAdapter2;
    private TextView datenow;
    private TextView totalcalorie;
    private TextView remcalorie;
    private TextView subtitlerecommendation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ROOT_URL_delete = getString(R.string.address)+"/history/deleteImage/";
        ROOT_URL_recommend = getString(R.string.address)+"/api/food/getSuggestion?remainder=";
        ROOT_URL_history = getString(R.string.address)+"/history/getTodayHistory?date=";

        datenow = findViewById(R.id.editDate);
        datenow.setText("" + LocalDate.now());
        totalcalorie = findViewById(R.id.textView16);
        remcalorie = findViewById(R.id.textView18);
        //hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menu);
        subtitlerecommendation=findViewById(R.id.textView20);
        SharedPreferences sharedPref=getSharedPreferences("user_data",Context.MODE_PRIVATE);
        String useremail=sharedPref.getString("email",null);

        Intent intent=getIntent();
        Long currentDate=intent.getLongExtra("date",System.currentTimeMillis());
        LocalDate date = Instant.ofEpochMilli(currentDate).atZone(ZoneId.systemDefault()).toLocalDate();
        datenow.setText("" + date);

        //Navigation Drawer Menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawer();

        System.out.println("it is calling create item list");
        buildRecyclerView();

        retrieveItemList(date, useremail);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    public void buildRecyclerView() {
        mItemList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.GridView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Adapter(mItemList,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onShareClick(int position) {
                //TODO how to test this on real facebook/ instagram app to make sure it is working
                Item a= mItemList.get(position);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                String address = getString(R.string.address);
                String imageurl=a.getImage().replace("http://localhost:8080",address);
                File img=downloadSharedImage(imageurl);

                Uri picUri= FileProvider.getUriForFile(HistoryActivity.this,"iss.edu.sg.autocalorietracker",img);
                shareIntent.putExtra(Intent.EXTRA_STREAM,picUri);
                shareIntent.setType("image/png");

                Intent chooser=Intent.createChooser(shareIntent, "Send To");
                List<ResolveInfo> resInfoList = HistoryActivity.this.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    HistoryActivity.this.grantUriPermission(packageName, picUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                startActivity(chooser);

            }

            @Override
            public void onEditClick(int position) {
                Item a= mItemList.get(position);
                Intent intent=new Intent(HistoryActivity.this,HistoryEditActivity.class);
                intent.putExtra("item",a);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                Item a= mItemList.get(position);
                Long id=a.getId();
                deleteImage(id,position);
                mItemList.remove(position);
                double currentsum=Double.parseDouble(totalcalorie.getText().toString());
                currentsum=currentsum-Double.parseDouble(a.getCalorie());
                totalcalorie.setText(""+currentsum);
                double curremainder=Double.parseDouble(remcalorie.getText().toString().split(" Kcal")[0]);
                curremainder+=Double.parseDouble(a.getCalorie());

                mAdapter.notifyItemRemoved(position);
                String currentdate=datenow.getText().toString();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate now=LocalDate.parse(currentdate,format);

                if(now.compareTo(LocalDate.now())==0){
                    showRecommendation(curremainder);
                }

            }
        });

        mItemList2 = new ArrayList<>();
        mRecyclerView2 = findViewById(R.id.GridView2);
        mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView2.setHasFixedSize(true);
        mAdapter2 = new Adapter2(mItemList2,this);
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
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_rate:
                break;

            case R.id.nav_logout:
                Intent intent4 = new Intent(this, FlashActivity.class);
                startActivity(intent4);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deleteImage(Long id, int position) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ROOT_URL_delete + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(HistoryActivity.this, "removed successfully", Toast.LENGTH_SHORT).show();
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
        String url = ROOT_URL_recommend + remainder;
        System.out.println("url=" + url);

        remcalorie.setText(remainder+" Kcal left for today");
        remcalorie.setVisibility(View.VISIBLE);
        subtitlerecommendation.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: " + response.toString());
                        JSONArray result = null;
                        try {
                            result = new JSONArray(response);
                            mItemList2.clear();
                            for (int x = 0; x < result.length(); x++) {
                                JSONObject ans = result.getJSONObject(x);
                                String name = ans.getString("foodName");
                                String url = ans.getString("url");
                                double calorie = ans.getDouble("calorie");
                                mItemList2.add(new Item(url, name, "" + calorie));
                            }
                            mAdapter2.notifyDataSetChanged();
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
        String url = ROOT_URL_history + date + "&email=" + email;
        System.out.println("url=" + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: " + response.toString());
                        JSONArray result = null;
                        double sum = 0;
                        double threshold=Double.valueOf(remcalorie.getText().toString().split(" Kcal")[0]);
                        try {
                            result = new JSONArray(response);

                            for (int x = 0; x < result.length(); x++) {
                                JSONObject ans = result.getJSONObject(x);
                                String name = ans.getString("foodName");
                                Long id = ans.getLong("id");
                                double calorie = ans.getDouble("calorie");
                                sum += calorie;
                                threshold = ans.getJSONObject("dailyHistory").getJSONObject("user").getDouble("recommendedCalories");
                                Long time = ans.getLong("epochDateUpload");
                                Date date = new Date(time);
                                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                format.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                                String formatted = format.format(date);
                                mItemList.add(new Item(id, ans.getString("url"), name, "" + calorie, formatted));
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        totalcalorie.setText("" + sum);
                        double remainder = threshold - sum;

                        System.out.println(date);
                        System.out.println(LocalDate.now());
                        if(date.compareTo(LocalDate.now())==0){
                            showRecommendation(remainder);
                        }
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

    public File downloadSharedImage(String url){
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imagename=url.split("/image/")[1];
        File file = new File(directory, imagename);
        storeImageInStorage(url, file);
        return file;
    }

    public void storeImageInStorage(String imgurl, File file) {
        Log.d("path", file.toString());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            convertImage(imgurl).compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    public Bitmap convertImage(String url) throws IOException {
        System.out.println(url);
        URL urlimg = new URL(url);
        URLConnection conn = urlimg.openConnection();
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        conn.connect();
        InputStream in = conn.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        in.close();
        return bitmap;

    }


}