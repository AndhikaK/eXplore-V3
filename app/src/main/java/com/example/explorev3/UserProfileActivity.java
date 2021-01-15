package com.example.explorev3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    String userName, userEmail;

    TextView tvUserName, tvUserEmail;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");
        userEmail = intent.getStringExtra("USER_EMAIL");

        tvUserName = findViewById(R.id.user_name_profile);
        tvUserEmail = findViewById(R.id.user_email_profile);

        Log.d("Nah", "onCreate: userName = " + userName);

        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);

        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (R.id.btn_logout) {
            case R.id.btn_logout:
                userLogout();
                break;
        }
    }

    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
    }
}