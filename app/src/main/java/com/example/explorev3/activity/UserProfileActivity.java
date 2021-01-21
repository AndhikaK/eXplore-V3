package com.example.explorev3.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.explorev3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private String userName, userEmail;

    private TextView tvUserName;
    private ImageView editProfile, profile;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");
        userEmail = intent.getStringExtra("USER_EMAIL");

        tvUserName = findViewById(R.id.user_name_profile);
        editProfile = findViewById(R.id.edit_profile);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        profile = findViewById(R.id.user_avatar_profile);
        Picasso.get().load(intent.getStringExtra("USER_AVATAR")).into(profile);

        tvUserName.setText(userName);

        btnLogout.setOnClickListener(this);
        editProfile.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                userLogout();
                break;
            case R.id.edit_profile:
                Intent intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra("USER_NAME", userName);
                intent.putExtra("USER_EMAIL", userEmail);

                startActivity(intent);
                break;
        }
    }

    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(UserProfileActivity.this, MainSwipeActivity.class));
    }
}