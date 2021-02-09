package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CaptureActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private String ROOT_URL;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath;
    ImageView imageView;
    TextView textView;
    TextView name;
    TextView cal;
    ProgressBar progress;
    private Button historyview;
    private ImageView homebut;
    private ImageButton camera;
    private int cameracode=5;
    private String imageabs;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private static final String C_ID="88888";
    private static final String C_NAME="channel for notif";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        ROOT_URL = "http://"+getString(R.string.address)+":8080/api/image/predict";

        drawerLayout =findViewById(R.id.drawer_layout);
        drawerLayout =findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.nav_view);

        //initializing views
        imageView =  findViewById(R.id.imageView);
        textView =  findViewById(R.id.textview);
        name=findViewById(R.id.foodname);
        cal=findViewById(R.id.foodcalorie);
        homebut=findViewById(R.id.menu);
        historyview=findViewById(R.id.buttonhistory);
        historyview.setOnClickListener(this);
        camera=findViewById(R.id.cameralink);
        camera.setOnClickListener(this);
        progress=findViewById(R.id.wait);

        //adding click listener to button
        findViewById(R.id.buttonUploadImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(CaptureActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(CaptureActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(CaptureActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");
                        showFileChooser();
                }


            }
        });

        //tool bar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawer();

    }

    private void createNotificationChannel(){
        int importance= NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel= new NotificationChannel(C_ID,C_NAME,importance);
        channel.setDescription("notification for the reminder of the app");

        NotificationManager notifmanager=getSystemService(NotificationManager.class);
        notifmanager.createNotificationChannel(channel);
    }

    private void createNotification(){

        NotificationCompat.Builder builder= new  NotificationCompat.Builder(this,C_ID);

        Intent intent= new Intent(this,HistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);

        builder.setSmallIcon(R.drawable.logo)
                .setContentTitle("Reminder")
                .setContentText("your calorie intake level today is close to the daily recommended intake")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pi);
        Notification notif=builder.build();

        int notifid=111;
        NotificationManagerCompat mgr=NotificationManagerCompat.from(this);
        mgr.notify(notifid,notif);

    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                Intent intent0 = new Intent(this, MainActivity.class);
                startActivity(intent0);
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
            case R.id.nav_share:
                Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_rate:
                break;
            case R.id.nav_logout:
                Intent intent3 = new Intent(this, FlashActivity.class);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(CaptureActivity.this);
        navigationView.setCheckedItem(R.id.nav_home);

        homebut.setOnClickListener(new View.OnClickListener(){
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

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private boolean closeToThreshold(double currentfood){
        SharedPreferences sharedPref=getSharedPreferences("user_data",Context.MODE_PRIVATE);
        Float threshold=sharedPref.getFloat("threshold",0);
        Float cal=sharedPref.getFloat("calorie",0);
        Float totalintake=sharedPref.getFloat("totalIntake",0);
        if((cal-totalintake-currentfood)<threshold){
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            //filePath = getPath(picUri);
            System.out.println(picUri);
            filePath=Environment.DIRECTORY_DOWNLOADS;
            if (filePath != null) {
                try {

                    Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    textView.setText("File Selected");
                    Thread th= new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadBitmap(bitmap);
                        }
                    });
                    th.start();
                    progress.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(
                        CaptureActivity.this,"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }else if(requestCode==cameracode){
            if(resultCode==RESULT_OK){
                bitmap= BitmapFactory.decodeFile(imageabs);
                bitmap=getResizedBitmap(bitmap, 500);
                imageView.setImageBitmap(bitmap);
                progress.setVisibility(View.VISIBLE);
                textView.setText("File Selected");
                Thread th= new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadBitmap(bitmap);
                    }
                });
                th.start();
            }
        }

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            Uri uri = FileProvider.getUriForFile(this, "iss.edu.sg.autocalorietracker", createTempFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, cameracode);
        }
    }

    private File createTempFile(){
        File path=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File tmpfile= File.createTempFile("takephoto",".png",path);
            imageabs=tmpfile.getAbsolutePath();

            return tmpfile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String fname=obj.getString("name");
                            double fcal=obj.getDouble("calorie");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    name.setText(fname);
                                    cal.setText(fcal+" cal");
                                    textView.setText("File Submitted");
                                    progress.setVisibility(View.INVISIBLE);
                                    if(closeToThreshold(fcal)){
                                        createNotificationChannel();
                                        createNotification();
                                    }
                                }
                            });
                            //Toast.makeText(getApplicationContext(), name+" "+cal, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                SharedPreferences sharedPref=getSharedPreferences("user_data",Context.MODE_PRIVATE);
                String username=sharedPref.getString("email",null);
//                String username="ZAC@GMAIL.COM";
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(username+"_"+imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    @Override
    public void onClick(View v) {
        if(v==camera){
            takePhoto();
        }else if(v==historyview){
            Intent intent= new Intent(this,HistoryActivity.class);
            intent.putExtra("date",System.currentTimeMillis());
            startActivity(intent);
        }
    }
}