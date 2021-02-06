package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner year, level;
    private ImageView weight1, weight2, weight3, height1, height2, height3;
    private TextView w1, w2, w3, h1, h2, h3;
    private RadioGroup radioGroup, radioWeight, radioHeight;
    private Button save, signIn;
    private String ROOT_URL ;

    private TextInputLayout email;
    private TextInputLayout password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        year = findViewById(R.id.year);
        level = findViewById(R.id.level);
        radioGroup = findViewById(R.id.radioGroup);
        radioWeight = findViewById(R.id.radioWeight);
        radioHeight = findViewById(R.id.radioHeight);

        ROOT_URL= "http://"+getString(R.string.address)+":8080/api/user/register";

        w1 = findViewById(R.id.w1);
        w2 = findViewById(R.id.w2);
        w3 = findViewById(R.id.w3);
        h1 = findViewById(R.id.h1);
        h2 = findViewById(R.id.h2);
        h3 = findViewById(R.id.h3);
        weight1 = findViewById(R.id.weight1);
        weight2 = findViewById(R.id.weight2);
        weight3 = findViewById(R.id.weight3);
        height1 = findViewById(R.id.height1);
        height2 = findViewById(R.id.height2);
        height3 = findViewById(R.id.height3);

        save = findViewById(R.id.save);
        save.setOnClickListener(this);
        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        populateSpinnerYear();
        populateSpinnerLevel();
        setRadio();
    }

    private void setRadio() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:

                        weight1.setImageResource(R.drawable.m1);
                        weight2.setImageResource(R.drawable.male);
                        weight3.setImageResource(R.drawable.m3);

                        height1.setImageResource(R.drawable.male);
                        height2.setImageResource(R.drawable.male);
                        height3.setImageResource(R.drawable.male);

                        w1.setText("<60kg");
                        w2.setText("60kg-75kg");
                        w3.setText(">75kg");
                        h1.setText("<160cm");
                        h2.setText("160cm-175cm");
                        h3.setText(">175cm");
                        break;
                    case R.id.female:
                        weight1.setImageResource(R.drawable.f1);
                        weight2.setImageResource(R.drawable.female);
                        weight3.setImageResource(R.drawable.f3);

                        height1.setImageResource(R.drawable.female);
                        height2.setImageResource(R.drawable.female);
                        height3.setImageResource(R.drawable.female);

                        w1.setText("<50kg");
                        w2.setText("50kg-65kg");
                        w3.setText(">65kg");
                        h1.setText("<155cm");
                        h2.setText("155cm-170cm");
                        h3.setText(">170cm");
                        break;
                }

            }
        });
    }

    private void populateSpinnerLevel() {
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_level));
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(levelAdapter);

    }


    private void populateSpinnerYear() {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1960; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        Spinner spinYear = (Spinner) findViewById(R.id.year);
        spinYear.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.save:
                String mail=email.getEditText().getText().toString();
                String pass=password.getEditText().getText().toString();
                String yearval= year.getSelectedItem().toString();
                String activity=level.getSelectedItem().toString();

                int genderid= radioGroup.getCheckedRadioButtonId();
                RadioButton genderbut=findViewById(genderid);
                String genderval=genderbut.getText().toString();
                System.out.println(genderval);

                int heightid= radioHeight.getCheckedRadioButtonId();
                RadioButton heightbut=findViewById(heightid);
                String heightval=heightbut.getText().toString();
                String h= estimateHeight(genderval,heightval);
                System.out.println(h);

                int weightid= radioWeight.getCheckedRadioButtonId();
                RadioButton weightbut=findViewById(weightid);
                String weightval=weightbut.getText().toString();
                String w= estimateWeight(genderval,weightval);
                System.out.println(w);

                Person p = new Person(mail,pass,yearval,activity,genderval,h,w);
                register(p);
                //Toast.makeText(SignUpActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                break;
            case R.id.signIn:
                intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    public void register(Person p){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("email",p.getEmail());
            object.put("password",p.getPassword());
            object.put("birthYear",p.getYear());
            object.put("activityLevel",p.getActivity());
            object.put("gender",p.getGender());
            object.put("weight",p.getAvgweight());
            object.put("height",p.getAvgheight());
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
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("registration error");
                //resultTextView.setVisibility(View.VISIBLE);
                //resultTextView.setText("Registration Failed");

            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public String estimateWeight(String gender, String buttontext){
        if(gender.contentEquals("Male")){
            if(buttontext.contains("<")){
                return "55";
            }else if(buttontext.contains("-")){
                return "68";
            }else{
                return "80";
            }
        }else{
            if(buttontext.contains("<")){
                return "45";
            }else if(buttontext.contains("-")){
                return "58";
            }else{
                return "70";
            }
        }
    }

    public String estimateHeight(String gender, String buttontext){
        if(gender.contentEquals("Male")){
            if(buttontext.contains("<")){
                return "155";
            }else if(buttontext.contains("-")){
                return "168";
            }else{
                return "180";
            }
        }else{
            if(buttontext.contains("<")){
                return "150";
            }else if(buttontext.contains("-")){
                return "163";
            }else{
                return "175";
            }
        }
    }

    class Person{
        String email;
        String password;
        String year;
        String activity;
        String gender;
        String avgheight;
        String avgweight;

        public Person(String email,String password,String year,String activity,String gender,String avgheight,String avgweight){
            this.email=email;
            this.activity=activity;
            this.password=password;
            this.year=year;
            this.gender=gender;
            this.avgheight=avgheight;
            this.avgweight=avgweight;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getYear() {
            return year;
        }

        public String getActivity() {
            return activity;
        }

        public String getGender() {
            return gender;
        }

        public String getAvgheight() {
            return avgheight;
        }

        public String getAvgweight() {
            return avgweight;
        }
    }
}