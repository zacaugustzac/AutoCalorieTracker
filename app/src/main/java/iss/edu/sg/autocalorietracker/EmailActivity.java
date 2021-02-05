package iss.edu.sg.autocalorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        sentEmail = findViewById(R.id.sentEmail);
        sentEmail.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(EmailActivity.this,"Email sent!",Toast.LENGTH_SHORT).show();
        Intent intent = null;
        intent = new Intent(EmailActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }
}