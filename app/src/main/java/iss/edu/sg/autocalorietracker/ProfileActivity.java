package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //variables for menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView menuIcon;
    private ImageButton yearBtn, genderBtn, heightBtn, weightBtn, activityBtn;
    private Button resetPassword, save;
    private TextView email, height, weight, activity, year, gender;
    private String UpdateURL;
    private String RetrieveURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UpdateURL=getString(R.string.address)+"/api/user/update";
        RetrieveURL=getString(R.string.address)+"/api/user/view?email=";
        //hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menu);

        heightBtn = findViewById(R.id.heightBtn);
        weightBtn = findViewById(R.id.weightBtn);
        activityBtn = findViewById(R.id.activityBtn);
        yearBtn = findViewById(R.id.yearBtn);
        genderBtn = findViewById(R.id.genderBtn);
        resetPassword = findViewById(R.id.resetPassword);
        save = findViewById(R.id.save);
        email = findViewById(R.id.email);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        activity = findViewById(R.id.activity);
        year = findViewById(R.id.year);
        gender = findViewById(R.id.gender);

        SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String useremail = sharedPref.getString("email", null);

        retrieveUser(useremail);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setListeners();

        //setBtns
//        setButtons();

        //tool bar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawer();
    }

    private void retrieveUser(String useremail) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = RetrieveURL + useremail;
        System.out.println("url=" + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: " + response.toString());
                        JSONObject result = null;

                        try {
                            result = new JSONObject(response);

                            String emailVal = result.getString("email");
                            email.setText(emailVal);
                            String genderVal = result.getString("gender");
                            gender.setText(genderVal);
                            String yearVal = result.getString("birthYear");
                            year.setText(yearVal);
                            String heightVal = result.getString("height");
                            height.setText(heightVal);
                            String weightVal = result.getString("weight");
                            weight.setText(weightVal);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(HistoryActivity.this,"Retrieved successfully",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(ProfileActivity.this, "Something wrong happens", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void setListeners() {

        OnClick onClick = new OnClick();
        heightBtn.setOnClickListener(onClick);
        weightBtn.setOnClickListener(onClick);
        activityBtn.setOnClickListener(onClick);
        yearBtn.setOnClickListener(onClick);
        genderBtn.setOnClickListener(onClick);
        resetPassword.setOnClickListener(onClick);
        save.setOnClickListener(onClick);

    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.heightBtn:
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProfileActivity.this);
                    bottomSheetDialog.setContentView(R.layout.row_add_item);

                    EditText newHeight = bottomSheetDialog.findViewById(R.id.newValue);
                    newHeight.setText(height.getText());
                    Button update = bottomSheetDialog.findViewById(R.id.update);

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(newHeight.getText().toString())) {
                                String value = newHeight.getText().toString();
                                height.setText(value);
                                bottomSheetDialog.dismiss();
                            } else {
                                Toast.makeText(v.getContext(), "Value required", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    bottomSheetDialog.show();
                    break;

                case R.id.weightBtn:
                    BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(ProfileActivity.this);
                    bottomSheetDialog1.setContentView(R.layout.row_add_item);

                    EditText newWeight = bottomSheetDialog1.findViewById(R.id.newValue);
                    newWeight.setText(weight.getText());
                    TextView title = bottomSheetDialog1.findViewById(R.id.title);
                    title.setText("Key in Your Weight Value");
                    TextView unit = bottomSheetDialog1.findViewById(R.id.unit);
                    unit.setText("kg");
                    Button update1 = bottomSheetDialog1.findViewById(R.id.update);

                    update1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(newWeight.getText().toString())) {
                                String value = newWeight.getText().toString();
                                weight.setText(value);
                                bottomSheetDialog1.dismiss();
                            } else {
                                Toast.makeText(v.getContext(), "Value required", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    bottomSheetDialog1.show();
                    break;


                case R.id.activityBtn:
                    BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(ProfileActivity.this);
                    bottomSheetDialog2.setContentView(R.layout.row_add_item2);

                    RadioGroup group = bottomSheetDialog2.findViewById(R.id.group);
                    group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.radio1:
                                    activity.setText("Sedentary");
                                    bottomSheetDialog2.dismiss();
                                    break;
                                case R.id.radio2:
                                    activity.setText("Moderate");
                                    bottomSheetDialog2.dismiss();
                                    break;
                                case R.id.radio3:
                                    activity.setText("Active");
                                    bottomSheetDialog2.dismiss();
                                    break;
                            }
                        }
                    });

                    bottomSheetDialog2.show();
                    break;

                case R.id.genderBtn:
                    BottomSheetDialog bottomSheetDialog3 = new BottomSheetDialog(ProfileActivity.this);
                    bottomSheetDialog3.setContentView(R.layout.row_add_item3);

                    RadioGroup group1 = bottomSheetDialog3.findViewById(R.id.group);


                    group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId) {
                                case R.id.radio1:
                                    gender.setText("Male");
                                    bottomSheetDialog3.dismiss();
                                    break;
                                case R.id.radio2:
                                    gender.setText("Female");
                                    bottomSheetDialog3.dismiss();
                                    break;

                            }
                        }
                    });

                    bottomSheetDialog3.show();
                    break;

                case R.id.yearBtn:
                    BottomSheetDialog bottomSheetDialog4 = new BottomSheetDialog(ProfileActivity.this);
                    bottomSheetDialog4.setContentView(R.layout.row_add_item);

                    EditText newYear = bottomSheetDialog4.findViewById(R.id.newValue);
                    newYear.setText(year.getText());
                    TextView title1 = bottomSheetDialog4.findViewById(R.id.title);
                    title1.setText("Key in Your Birth Year");
                    TextView unit1 = bottomSheetDialog4.findViewById(R.id.unit);
                    unit1.setText("");
                    Button update2 = bottomSheetDialog4.findViewById(R.id.update);

                    update2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(newYear.getText().toString())) {
                                String value = newYear.getText().toString();
                                year.setText(value);
                                bottomSheetDialog4.dismiss();
                            } else {
                                Toast.makeText(v.getContext(), "Value required", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    bottomSheetDialog4.show();
                    break;

                case R.id.resetPassword:
                    Intent intent1 = new Intent(ProfileActivity.this, ResetPasswordActivity.class);
                    startActivity(intent1);

                case R.id.save:
                    String emailVal = email.getText().toString();
                    String genderVal = gender.getText().toString();
                    String yearVal = year.getText().toString();
                    String heightVal = height.getText().toString();
                    String weightVal = weight.getText().toString();
                    String activityVal = activity.getText().toString();

                    Person p = new Person(emailVal, yearVal, activityVal, genderVal, heightVal, weightVal);
                    update(p);

                    Toast.makeText(ProfileActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    }

    private void update(Person p) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("email", p.getEmail());
            object.put("birthYear", p.getYear());
            object.put("activityLevel", p.getActivity());
            object.put("gender", p.getGender());
            object.put("weight", p.getAvgweight());
            object.put("height", p.getAvgheight());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = UpdateURL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("update successfully");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("update failed");

            }
        });
        requestQueue.add(jsonObjectRequest);
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
                Intent intent4 = new Intent(this, FlashActivity.class);
                startActivity(intent4);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}

