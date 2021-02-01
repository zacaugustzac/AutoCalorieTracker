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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

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
        creatItemList();
        buildRecyclerView();

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


    public void creatItemList(){
        mItemList=new ArrayList<>();
        mItemList.add(new Item(R.drawable.egg,"Egg(boild)","78 kcal","7am"));
        mItemList.add(new Item(R.drawable.egg,"Egg(boild)","78 kcal","7am"));
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