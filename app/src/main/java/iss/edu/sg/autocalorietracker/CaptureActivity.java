package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CaptureActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String ROOT_URL = "http://10.0.2.2:8080/api/image/predict";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath;
    ImageView imageView;
    TextView textView;
    TextView name;
    TextView cal;
    private Button historyview;
    private ImageButton homebut;
    private ImageButton camera;
    private int cameracode=5;
    private String imageabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        //initializing views
        imageView =  findViewById(R.id.imageView);
        textView =  findViewById(R.id.textview);
        name=findViewById(R.id.foodname);
        cal=findViewById(R.id.foodcalorie);
        historyview=findViewById(R.id.buttonhistory);
        historyview.setOnClickListener(this);
        homebut=findViewById(R.id.home);
        homebut.setOnClickListener(this);
        historyview.setOnClickListener(this);
        camera=findViewById(R.id.cameralink);
        camera.setOnClickListener(this);

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
                    if(textView.getText().equals("File Selected")){
                        uploadBitmap(bitmap);

                    }else{
                        showFileChooser();
                    }

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

                    textView.setText("File Selected");
                    Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    uploadBitmap(bitmap);
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
                imageView.setImageBitmap(bitmap);
                textView.setText("File Selected");
            }
        }

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
                            name.setText(fname);
                            cal.setText(fcal+" cal");
                            textView.setText("File Submitted");

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
                String username="kevin";
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(username+"_"+imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    @Override
    public void onClick(View v) {
        if(v==camera){
            takePhoto();
        }else if(v==historyview){
//            Intent intent= new Intent(this,historyActivity.class);
//            startActivity(intent);
        }else if(v==homebut){
            Intent intent= new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}