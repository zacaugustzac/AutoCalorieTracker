package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //variables for menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //hooks
        drawerLayout =findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //tool bar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);
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
                Intent intent0 = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent0);
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_plan:
                Intent intent1 = new Intent(ProfileActivity.this, PlanActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_reminders:
                Intent intent2 = new Intent(ProfileActivity.this, RemindersActivity.class);
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

}