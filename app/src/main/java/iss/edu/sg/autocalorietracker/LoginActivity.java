package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button forgotpsw, signIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgotpsw = findViewById(R.id.forgotpsw);
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        setListeners();
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
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
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
}