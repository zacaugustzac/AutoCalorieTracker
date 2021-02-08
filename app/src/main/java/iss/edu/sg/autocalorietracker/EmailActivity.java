package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sendEmail;
    private TextInputLayout emailfield;
    private String ROOT_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        ROOT_URL = "http://"+getString(R.string.address)+":8080/api/user/reset?email=";
        //TODO implement the backend part
        Intent intent=getIntent();
        String email=intent.getStringExtra("email");
        sendEmail=findViewById(R.id.sentEmail);
        sendEmail.setOnClickListener(this);
        emailfield=findViewById(R.id.emailspace);
        if(email!=null){
            emailfield.getEditText().setText(email);
        }




    }

    public void resetPass(String email){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ROOT_URL + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(EmailActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(EmailActivity.this, "request has been sent", Toast.LENGTH_SHORT).show();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Toast.makeText(EmailActivity.this, "Something wrong happens", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v==sendEmail){
            String email=emailfield.getEditText().getText().toString();
            resetPass(email);
        }
    }
}