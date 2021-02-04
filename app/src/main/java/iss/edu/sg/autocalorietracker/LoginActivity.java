package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button forgotpsw, signIn, signUp;
    private TextInputLayout emailfield;
    private TextInputLayout passfield;
    private TextView resultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgotpsw = findViewById(R.id.forgotpsw);
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        setListeners();

        emailfield=findViewById(R.id.email);
        passfield=findViewById(R.id.password);
        resultTextView=findViewById(R.id.result);

    }

    private void setListeners() {
        forgotpsw.setOnClickListener(this);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.signIn:
                String em=emailfield.getEditText().getText().toString();
                String pass=passfield.getEditText().getText().toString();
                submitLogin(em, pass);
                break;

            case R.id.signUp:
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;

            case R.id.forgotpsw:
                intent = new Intent(LoginActivity.this, EmailActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    public void submitLogin(String name, String password){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("email",name);
            object.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "http://10.0.2.2:8080/api/user/authenticate";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            try {
                                System.out.println("response successfully");

                                double calorierec=response.getDouble("recommendedCalories");
                                String email=response.getString("email");
                                System.out.println("email= "+email);
                                SharedPreferences sharedPref=getSharedPreferences("user_data",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPref.edit();
                                editor.putFloat("calorie",(float)calorierec);
                                editor.putString("email",email);
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultTextView.setVisibility(View.VISIBLE);
                resultTextView.setText("wrong username and/or password");
            }
        });
        requestQueue.add(jsonObjectRequest);

    }
}