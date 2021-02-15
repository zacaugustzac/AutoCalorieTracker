package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

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
import com.github.mikephil.charting.components.Legend;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ImageView menuIcon;
    private ImageView photoIcon;

    //variables for menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //for weekly chart
    private ComboLineColumnChartView chart;
    private ComboLineColumnChartData data;
    private List<Integer> calorieHistory = new ArrayList<>();
    private TextView textChartDateRangeView;
    private LocalDate lastDayForChart;
    private double userRecommendedCalories;
    private Button nextWeekButton;
    private Button lastWeekButton;
    private String useremail;

    private String URL_RETRIEVEUSER;
    private String URL_RETRIEVECALORIE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get email
        SharedPreferences sharedPref=getSharedPreferences("user_data", Context.MODE_PRIVATE);
        useremail=sharedPref.getString("email",null);

        URL_RETRIEVEUSER="http://" + getString(R.string.address) + ":8080/weekly/getUser?email=";
        URL_RETRIEVECALORIE="http://" + getString(R.string.address) + ":8080/weekly/getDailyCalories?date=";

        Intent in=getIntent();
        boolean newuser=in.getBooleanExtra("new",false);
        if(newuser){

            AlertDialog.Builder dlg= new AlertDialog.Builder(this)
                    .setTitle("Suggestion")
                    .setMessage("Would you like to update your profile to make the recommended calories calculation more precise?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("yes!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(MainActivity.this,ProfileActivity.class);
                            startActivity(intent);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("maybe later", null)
                    .setIcon(android.R.drawable.ic_dialog_alert);
                    dlg.show();
        }


        //get data from db and draw chart
        lastDayForChart = LocalDate.now();
        getUserFromDB(useremail);
        getCaloriesFromDB(lastDayForChart, useremail);
        nextWeekButton = findViewById(R.id.NextWeekButton);
        nextWeekButton.setOnClickListener(this);
        checkNextWeekButton(lastDayForChart);
        lastWeekButton = findViewById(R.id.LastWeekButton);
        lastWeekButton.setOnClickListener(this);

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
    }

    public void lastWeekButton(LocalDate lastDay){

    }

    public void checkNextWeekButton(LocalDate lastDay){
        if (lastDay.compareTo(LocalDate.now())==0)       {
            nextWeekButton.setVisibility(View.INVISIBLE);
        } else {
            nextWeekButton.setVisibility(View.VISIBLE);
        }
    }

    public void getUserFromDB(String useremail){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url=URL_RETRIEVEUSER + useremail;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("USER Response: "+ response);
                        try {
                            JSONObject result = new JSONObject(response);
                            userRecommendedCalories = result.getDouble("recommendedCalories");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(MainActivity.this,"User Retrieved successfully "+ userRecommendedCalories,Toast.LENGTH_SHORT).show();
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(MainActivity.this, "Something wrong happens(User)", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void getCaloriesFromDB(LocalDate date, String useremail){
        textChartDateRangeView = findViewById(R.id.textChartDateRangeView);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url=URL_RETRIEVECALORIE + date + "&email=" + useremail;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        calorieHistory.clear();
                        System.out.println("Response is: "+ response);
                        String res = response.substring(1,response.length()-1);
                        String str[] = res.split(",");
                        for(String i : str){
                            calorieHistory.add(Integer.parseInt(i));
                        }
                        SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putFloat("totalIntake", Float.valueOf(str[str.length-1]));
                        editor.commit();
                        drawComboChart();
//                        Toast.makeText(MainActivity.this,"Retrieved successfully",Toast.LENGTH_SHORT).show();
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(MainActivity.this, "Something wrong happens(Calories)", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void drawComboChart() {
        chart = findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        generateData();
        changeDateRange();
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
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_plan:
                Intent intent1 = new Intent(this, PlanActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_reminders:
                Intent intent2 = new Intent(this, ReminderActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                Intent intent3 = new Intent(this, FlashActivity.class);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        if (v == photoIcon) {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivity(intent);
        } else if (v == lastWeekButton){
            lastDayForChart = lastDayForChart.minusDays(7);//change last day for chart
            getCaloriesFromDB(lastDayForChart,useremail);//update chart
            checkNextWeekButton(lastDayForChart);//update nextweekbutton
        } else if (v == nextWeekButton){
            lastDayForChart = lastDayForChart.plusDays(7); //change last day for chart
            getCaloriesFromDB(lastDayForChart,useremail); //update chart
            checkNextWeekButton(lastDayForChart); //update nextweekbutton
        }
    }


    private void changeDateRange(){
        textChartDateRangeView = findViewById(R.id.textChartDateRangeView);
        String formattedLastDayForChart = lastDayForChart.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        LocalDate sixDaysAgo = lastDayForChart.minusDays(6);
        String formattedSixDaysAgo = sixDaysAgo.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        textChartDateRangeView.setText(formattedSixDaysAgo+" - "+formattedLastDayForChart);
    }

    private void generateData() {
        // Chart looks the best when line data and column data have similar maximum viewports.
        data = new ComboLineColumnChartData(generateColumnData(), generateLineData());


        //axis
        List<AxisValue> axisValuesForX = new ArrayList<>();
        List<AxisValue> axisValuesForY = new ArrayList<>();
        AxisValue tempAxisValue;

        String[] daysOfTheWeek = {"M","T","W","T","F","S","S"};
        switch (lastDayForChart.getDayOfWeek().toString()) {
            case "MONDAY":
                daysOfTheWeek = new String[] {"T","W","T","F","S","S","M"};
                break;
            case "TUESDAY":
                daysOfTheWeek = new String[] {"W","T","F","S","S","M","T"};
                break;
            case "WEDNESDAY":
                daysOfTheWeek = new String[] {"T","F","S","S","M","T","W"};
                break;
            case "THURSDAY":
                daysOfTheWeek = new String[] {"F","S","S","M","T","W","T"};
                break;
            case "FRIDAY":
                daysOfTheWeek = new String[] {"S","S","M","T","W","T","F"};
                break;
            case "SATURDAY":
                daysOfTheWeek = new String[] {"S","M","T","W","T","F","S"};
                break;
            case "SUNDAY":
                daysOfTheWeek = new String[] {"M","T","W","T","F","S","S"};
                break;
        }

        for (int i = 0; i <= 6; i += 1){
            tempAxisValue = new AxisValue(i);
            tempAxisValue.setLabel(daysOfTheWeek[i]);
            axisValuesForX.add(tempAxisValue);
        }
        //round up to nearest 500 of either highest daily calorie or recommended calories
        double top = (500*Math.ceil(Math.max(Collections.max(calorieHistory),userRecommendedCalories)/500));

        for (int i = 0; i <= top; i += 500){
            tempAxisValue = new AxisValue(i);
            tempAxisValue.setLabel(i+"");
            axisValuesForY.add(tempAxisValue);
        }

        //set max y value, max x value, fixed viewport,
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = (float)top;
        v.left = -0.5f;
        v.right = 6.5f;


        Axis axisX = new Axis(axisValuesForX);
        Axis axisY = new Axis(axisValuesForY);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chart.setComboLineColumnChartData(data);


        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        chart.setViewportCalculationEnabled(false);
    }

    private LineChartData generateLineData() {

        //Average line
        List<Line> lines = new ArrayList<>();
        int AverageCalories = (int)calorieHistory.stream().mapToInt(val->val).average().orElse(0.0);
        for (int i = 0; i < 1; ++i) {
            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < 7; ++j) {
                values.add(new PointValue(j, AverageCalories));
            }
            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setCubic(false);
            line.setHasLabels(false);
            line.setHasLines(true);
            line.setHasPoints(false);
            lines.add(line);

        }

        //Recommended Line
        List<PointValue> values = new ArrayList<>();
        for (int k = 0; k < 7; ++k) {
            values.add(new PointValue(k, (float)userRecommendedCalories));
        }
        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[1]);
        line.setCubic(false);
        line.setHasLabels(false);
        line.setHasLines(true);
        line.setHasPoints(false);
        lines.add(line);
        LineChartData lineChartData = new LineChartData(lines);

        return lineChartData;

    }

    private ColumnChartData generateColumnData() {
        int numColumns = 7;
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;

        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            values.add(new SubcolumnValue(calorieHistory.get(i), ChartUtils.COLOR_GREEN));
            columns.add(new Column(values));
        }

        ColumnChartData columnChartData = new ColumnChartData(columns);
        return columnChartData;
    }

    private class ValueTouchListener implements ComboLineColumnChartOnValueSelectListener {

        @Override
        public void onValueDeselected() {
        }

        @Override
        public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            LocalDate[] listOfDates = new LocalDate[7];
            for (int i=0; i<7;i++){
                listOfDates[i] = lastDayForChart.minusDays(6-i);
            }


            //change to long epochMillis
            Instant instant = listOfDates[columnIndex].atStartOfDay(ZoneId.systemDefault()).toInstant();
            Long timeInMillis = instant.toEpochMilli();

            //send intent
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("date",timeInMillis);
            System.out.println("listOfDates[columnIndex]" + listOfDates[columnIndex]);
            startActivity(intent);
        }

        @Override
        public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value) {
        }
    }
}