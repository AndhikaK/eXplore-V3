package com.example.explorev3.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explorev3.R;
import com.example.explorev3.fragment.FavoritesFragment;
import com.example.explorev3.fragment.HomeFragment;
import com.example.explorev3.fragment.PostFragment;
import com.example.explorev3.fragment.SearchFragment;
import com.example.explorev3.pojo.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.PipedReader;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView nameTv;

    String userName, userEmail, userAvatar;

    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference reference;

    CircularImageView imgUserAvatar;

    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTv = findViewById(R.id.test);

        imgUserAvatar = findViewById(R.id.userPout);
        imgUserAvatar.setOnClickListener(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        // Get user data
        getUserData();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        getUserData();
    }

    private void getUserData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            userUID = user.getUid();

            // get user data from firebase realtime database
            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userProfile = snapshot.getValue(User.class);

                    if (userProfile != null) {
                        userName = userProfile.name;
                        userEmail = userProfile.email;
                        userAvatar = userProfile.avatar;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // No user is signed in
            Random rand = new Random();
            int randNum = rand.nextInt(1000);

            userUID = "Guest" + randNum;
            userName = "";
            userEmail = "";
        }

        nameTv.setText(userUID);
        Picasso.get().load(userAvatar).into(imgUserAvatar);
        Log.d("get user data", "getUserData: image url " + imgUserAvatar);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;

                        case R.id.nav_favorites:
                            selectedFragment = new FavoritesFragment();
                            break;

                        case R.id.nav_post:
                            selectedFragment = new PostFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userPout:
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("USER_NAME", userName);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                break;
        }
    }
}