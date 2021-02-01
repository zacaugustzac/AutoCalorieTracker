package iss.edu.sg.autocalorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
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

//    private static String ipaddress="192.168.10.141";
//    private static final String ROOT_URL = "http://"+ipaddress+":8080/api/image/predict";
    private static final String ROOT_URL = "http://10.0.2.2:8080/api/image/predict"; //for android emulator
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
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
                Intent intent2 = new Intent(this, RemindersActivity.class);
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
                //TODO: later need to be replaced from the session instead of hardcoded here
                String username="ZAC@GMAIL.COM";
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
            startActivity(intent);
        }
    }
}