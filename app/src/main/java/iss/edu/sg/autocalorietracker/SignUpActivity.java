package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner year;
    private ImageView weight1,weight2,weight3,height1,height2,height3;
    private TextView w1,w2,w3,h1,h2,h3;
    private RadioGroup radioGroup,radioWeight,radioHeight;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        year = findViewById(R.id.year);
        radioGroup = findViewById(R.id.radioGroup);
        radioWeight = findViewById(R.id.radioWeight);
        radioHeight = findViewById(R.id.radioHeight);

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

        populateSpinnerYear();
        setRadio();
    }

    private void setRadio() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male:
                        weight1.setImageDrawable(getResources().getDrawable(R.drawable.m1));
                        weight2.setImageDrawable(getResources().getDrawable(R.drawable.male));
                        weight3.setImageDrawable(getResources().getDrawable(R.drawable.m3));
                        height1.setImageDrawable(getResources().getDrawable(R.drawable.male));
                        height2.setImageDrawable(getResources().getDrawable(R.drawable.male));
                        height3.setImageDrawable(getResources().getDrawable(R.drawable.male));
                        w1.setText("50kg-60kg");
                        w2.setText("61kg-70kg");
                        w3.setText("71kg-80kg");
                        h1.setText("160cm-170cm");
                        h2.setText("171cm-180cm");
                        h3.setText("181cm-190cm");
                        break;
                    case R.id.female:
                        weight1.setImageDrawable(getResources().getDrawable(R.drawable.f1));
                        weight2.setImageDrawable(getResources().getDrawable(R.drawable.female));
                        weight3.setImageDrawable(getResources().getDrawable(R.drawable.f3));
                        height1.setImageDrawable(getResources().getDrawable(R.drawable.female));
                        height2.setImageDrawable(getResources().getDrawable(R.drawable.female));
                        height3.setImageDrawable(getResources().getDrawable(R.drawable.female));
                        w1.setText("40kg-50kg");
                        w2.setText("51kg-60kg");
                        w3.setText("61kg-70kg");
                        h1.setText("150cm-160cm");
                        h2.setText("161cm-170cm");
                        h3.setText("171cm-180cm");
                        break;
                }

            }
        });
    }


    private void populateSpinnerYear() {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1960; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        Spinner spinYear = (Spinner)findViewById(R.id.year);
        spinYear.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(SignUpActivity.this,"Saved",Toast.LENGTH_SHORT).show();
        Intent intent = null;
        intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}