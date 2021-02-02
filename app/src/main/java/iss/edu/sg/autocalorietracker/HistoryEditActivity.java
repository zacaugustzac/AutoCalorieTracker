package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HistoryEditActivity extends AppCompatActivity {
    public ImageView foodPic;
    public EditText foodName;
    public EditText foodCal;
    public Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_edit);
        Intent intent=getIntent();
        Item item=(Item) intent.getSerializableExtra("item");
        foodPic =findViewById(R.id.imageView);
        URL url = null;
        try {
            url = new URL(item.getImage().replace("localhost:8080","10.0.2.2:8080"));
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            foodPic.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name=item.getName();
        String calorie=item.getCalorie();
        foodName =findViewById(R.id.foodname);
        foodName.setText(name);
        foodCal =findViewById(R.id.foodcalorie);
        foodCal.setText(item.getCalorie());
        saveButton =findViewById(R.id.buttonsave);
        saveButton.setOnClickListener(v -> {
            updateImage(item.getId(),name,calorie);
        });

    }

    public void updateImage(Long id, String name, String calorie){
        //TODO should we consider post for this??
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8080/history/updateImage?id=" + id + "&name=" + name+ "&calorie=" + calorie;
        System.out.println("url=" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(HistoryEditActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(HistoryEditActivity.this, "Something wrong happens", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}