package com.example.dr_pet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dr_pet.Model.Account;
import com.example.dr_pet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_password2);
        Button signupButton = findViewById(R.id.signup_button);
        TextView loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                if (user.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                    signupEmail.setError("Email is invalid");
                    return;
                }

                if (pass.isEmpty()) {
                    signupPassword.setError("Password cannot be empty");
                    return;
                }

                if (!pass.equals(confirmPass)) {
                    confirmPassword.setError("Passwords do not match");
                    return;
                }

                auth.createUserWithEmailAndPassword(user, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> verifyTask) {
                                                    if (verifyTask.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, "Sign up successful. Please verify your email.", Toast.LENGTH_LONG).show();
                                                        auth.signOut();
                                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(SignUpActivity.this, "SignUp Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }
}