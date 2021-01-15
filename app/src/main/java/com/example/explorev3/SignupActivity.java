package com.example.explorev3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explorev3.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    TextView tvSignin;
    Button btnSignup;
    EditText edtName, edtEmailSignUp, edtPass, edtConfirmPass;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        tvSignin = findViewById(R.id.tv_signin_signup);
        tvSignin.setOnClickListener(this);

        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(this);

        edtName = (EditText) findViewById(R.id.edt_name_signup);
        edtEmailSignUp = (EditText) findViewById(R.id.edt_email_signup);
        edtPass = (EditText) findViewById(R.id.edt_password_signup);
        edtConfirmPass = (EditText) findViewById(R.id.edt_password_confirm_signup);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_signup);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signin_signup:
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                break;
            case R.id.btn_signup:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String email = edtEmailSignUp.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String confirmPass = edtConfirmPass.getText().toString().trim();

        if (name.isEmpty()) {
            edtName.setError("Name is required");
            edtName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmailSignUp.setError("Email is required!");
            edtEmailSignUp.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailSignUp.setError("Please provide a valid email!");
            edtEmailSignUp.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            edtPass.setError("Password is required!");
            edtPass.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            edtPass.setError("Password should be longer than 6 character!");
            edtPass.requestFocus();
            return;
        }

        if (confirmPass.isEmpty()) {
            edtConfirmPass.setError("Please confirm the password!");
            edtConfirmPass.requestFocus();
            return;
        }

        if (!confirmPass.equals(pass)) {
            edtConfirmPass.setError("Confirm password doesn't match");
            edtConfirmPass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(name, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText( SignupActivity.this, "User has been registered!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Sign up failed, try again ...", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SignupActivity.this, "Sign up failed, try again ...", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}