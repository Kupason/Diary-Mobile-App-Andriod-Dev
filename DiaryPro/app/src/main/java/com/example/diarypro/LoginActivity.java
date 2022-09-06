package com.example.diarypro;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText= findViewById(R.id.password_edit_text);
        loginBtn= findViewById(R.id.login_icon_btn);
        progressBar= findViewById(R.id.progress_bar);
        createAccountTextView= findViewById(R.id.create_account_btn);


        loginBtn.setOnClickListener((v)-> loginUser());
        createAccountTextView.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,AccountActivity.class)));
    }
    void loginUser(){


            String email = emailEditText.getText().toString();
            String password= passwordEditText.getText().toString();

            boolean isValidated = validateData(email,password);
            if(!isValidated){
                return;
            }
            loginAccountInFirebase(email,password);
    }

    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(true);
                if(task.isSuccessful()){
                    //login successful
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to the main activity
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }else{
                    Utility.showToast(LoginActivity.this,"Email not verified,Please check email and verify");
                    }

                }else{
                    //login failed
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());


                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email,String password){
        //Validate user s data

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password length must be 6 or more");
            return false;
        }

        return true;
    }
}