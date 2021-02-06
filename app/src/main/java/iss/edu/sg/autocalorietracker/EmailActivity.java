package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        ROOT_URL = "http://"+getString(R.string.address)+":8080/api/user/reset?email=";
        //TODO implement the backend part
        Intent intent=getIntent();
        String email=intent.getStringExtra("email");
        emailfield=findViewById(R.id.email);
        if(email!=null){
            emailfield.getEditText().setText(email);
        }
        sendEmail=findViewById(R.id.email);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        sendEmail.setOnClickListener(v-> {
            resetPass(email);
        });
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(EmailActivity.this,"Email sent!",Toast.LENGTH_SHORT).show();
        Intent intent = null;
        intent = new Intent(EmailActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
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
        queue.add(stringRequest);
    }
}