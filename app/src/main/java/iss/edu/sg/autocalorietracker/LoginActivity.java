package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private CheckBox remembercheck;
    private String ROOT_URL ;
    private String keywordpass="passTemp";
    private boolean newuser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgotpsw = findViewById(R.id.forgotpsw);
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        setListeners();

        ROOT_URL= getString(R.string.address)+"/api/user/authenticate";

        emailfield=findViewById(R.id.email);
        passfield=findViewById(R.id.password);
        resultTextView=findViewById(R.id.result);
        remembercheck=findViewById(R.id.checkbox_remember);

        SharedPreferences sharedPref=getSharedPreferences("user_data",Context.MODE_PRIVATE);
        String remember=sharedPref.getString("remember","no");

        if(remember.contentEquals("yes")){
            String email=sharedPref.getString("email",null);
            String password=sharedPref.getString("password",null);
            emailfield.getEditText().setText(email);
            passfield.getEditText().setText(password);
            remembercheck.setChecked(true);
        }

        Intent in=getIntent();
        String email=in.getStringExtra("email");
        newuser=in.getBooleanExtra("new",false);
        if(email!=null){
            emailfield.getEditText().setText(email);
        }

    }

    private void setListeners() {
        forgotpsw.setOnClickListener(this);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        String em=emailfield.getEditText().getText().toString();
        switch (view.getId()) {
            case R.id.signIn:
                String pass=passfield.getEditText().getText().toString();
                submitLogin(em, pass,newuser);
                break;

            case R.id.signUp:
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;

            case R.id.forgotpsw:
                intent = new Intent(LoginActivity.this, EmailActivity.class);
                intent.putExtra("email",em);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        SharedPreferences sharedPref=getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_remember:
                if (checked){
                    editor.putString("remember","yes");
                    editor.commit();
                }
                // Put some meat on the sandwich
            else{
                    editor.putString("remember","no");
                    editor.commit();
            }
                // Remove the meat
                break;
        }
    }

    public void submitLogin(String name, String password,boolean newuser){
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
        String url = ROOT_URL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            try {
                                System.out.println("response successfully");
                                String pass = response.getString("password");
                                String email = response.getString("email");
                                if(pass.contains(keywordpass)){
                                    Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                                    intent.putExtra("email",email);
                                    startActivity(intent);
                                }else {
                                    double calorierec = response.getDouble("recommendedCalories");
                                    double calThreshold=response.getDouble("reminderCalories");
                                    System.out.println("email= " + email);
                                    SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("password", pass);
                                    editor.putFloat("calorie", (float) calorierec);
                                    editor.putFloat("threshold",(float)calThreshold);
                                    editor.putString("email", email);
                                    editor.commit();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("new",newuser);
                                    startActivity(intent);
                                }
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