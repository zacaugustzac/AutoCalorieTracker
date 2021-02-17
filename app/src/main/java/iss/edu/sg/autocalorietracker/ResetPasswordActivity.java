package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {
    private Button reset;
    private TextInputLayout passfield;
    private TextInputLayout confirmfield;
    private String ROOT_URL;
    private TextView errormess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ROOT_URL=getString(R.string.address)+"/api/user/resetPass";
        Intent intent=getIntent();
        String email=intent.getStringExtra("email");

        reset =findViewById(R.id.reset);
        errormess=findViewById(R.id.error_message);
        passfield=findViewById(R.id.password);
        confirmfield=findViewById(R.id.confirm_password);

        reset.setOnClickListener(v-> {
            String pass=passfield.getEditText().getText().toString();
            String conpass=confirmfield.getEditText().getText().toString();
                if(pass.contentEquals(conpass)){
                    resetPassword(email,pass);
                }else{
                    errormess.setText("password does not match!");
                    errormess.setVisibility(View.VISIBLE);
                }

            });
    }


    public void resetPassword(String email,String password){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("email",email);
            object.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = ROOT_URL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response successfully");
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("reset password error");
                //resultTextView.setVisibility(View.VISIBLE);
                //resultTextView.setText("Registration Failed");

            }
        });
        requestQueue.add(jsonObjectRequest);

    }



}