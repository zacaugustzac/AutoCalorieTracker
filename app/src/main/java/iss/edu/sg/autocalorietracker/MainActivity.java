package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        move=findViewById(R.id.scan);
        move.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==move){
            Intent in= new Intent(this,CaptureActivity.class);
            startActivity(in);
        }
    }
}