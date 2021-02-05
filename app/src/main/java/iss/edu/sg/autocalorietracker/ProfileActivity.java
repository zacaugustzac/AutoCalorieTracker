package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //variables for menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView menuIcon;
    private ImageButton yearBtn, genderBtn, heightBtn, weightBtn, activityBtn;
    private Button resetPassword, save;
    private TextView height, weight, activity, year, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        activity = findViewById(R.id.activity);
        year = findViewById(R.id.year);
        gender = findViewById(R.id.gender);

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
                    newWeight.setText("55");
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
                                    activity.setText("Lightly");
                                    bottomSheetDialog2.dismiss();
                                    break;
                                case R.id.radio3:
                                    activity.setText("Moderately");
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
                    newYear.setText("2000");
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
                    break;
                default:
                    break;
            }

        }
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
}

