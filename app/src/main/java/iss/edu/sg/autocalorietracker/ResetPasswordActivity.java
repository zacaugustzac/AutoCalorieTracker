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

public class ResetPasswordActivity extends AppCompatActivity {
    private Button reset;
    private TextInputLayout emailfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO implement the backend part
        Intent intent=getIntent();
        String email=intent.getStringExtra("email");
        reset =findViewById(R.id.reset);
        if(email!=null){
            emailfield.getEditText().setText(email);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        reset.setOnClickListener(v-> {
                //resetPass(email);
            });
    }



}