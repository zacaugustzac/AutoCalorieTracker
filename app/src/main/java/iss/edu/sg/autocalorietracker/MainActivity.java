package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private BarChart barChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private boolean hasAxesNames = true;
    private LimitLine ll1;
    private ImageView menuIcon;
    private ImageView photoIcon;

    //variables for menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hooks
        drawerLayout =findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menu);
        photoIcon = findViewById(R.id.photo);
        photoIcon.setOnClickListener(this);


        //tool bar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawer();


        barChart = findViewById(R.id.barChart);

        //set x value
        String[] days = new String[]{"", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        //set value of each column
        BarDataSet barDataSet = new BarDataSet(calories(), "Calories");
        barDataSet.setColors(getResources().getColor(R.color.green));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setDrawValues(false);


        BarData barData = new BarData(barDataSet);

        barChart.setDragEnabled(true);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.animateY(1000);
        barChart.setDrawBorders(false);

        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();

        rightAxis.setEnabled(false);
        barChart.setDrawGridBackground(false);
        xAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(false);


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

    //set the drawer
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    //set the item in menu bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_plan:
                Intent intent1 = new Intent(MainActivity.this, PlanActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_reminders:
                Intent intent2 = new Intent(MainActivity.this, ReminderActivity.class);
                startActivity(intent2);
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


    @Override
    public void onClick(View v) {
            Intent intent= new Intent(this,CaptureActivity.class);
            startActivity(intent);

    }
    private ArrayList<BarEntry> calories() {
        ArrayList<BarEntry> calories = new ArrayList<>();
        calories.add(new BarEntry(1, 450));
        calories.add(new BarEntry(2, 650));
        calories.add(new BarEntry(3, 560));
        calories.add(new BarEntry(4, 400));
        calories.add(new BarEntry(5, 750));
        calories.add(new BarEntry(6, 650));
        calories.add(new BarEntry(7, 450));
        return calories;
    }

}