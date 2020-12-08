package com.rku.google_signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText login_email,login_pass;
    private FirebaseAuth mAuth;
    ProgressDialog mLoadingbar;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        btn_login=findViewById(R.id.btn_login);
        login_email=findViewById(R.id.login_email);
        login_pass=findViewById(R.id.login_pass);
        preferences=getSharedPreferences("user",MODE_PRIVATE);
        editor=preferences.edit();

        String userPreference=preferences.getString("username","");
        if(!(userPreference.equals("")))
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("username",userPreference);
            startActivity(intent);
            finish();
        }

        mAuth=FirebaseAuth.getInstance();
        mLoadingbar=new ProgressDialog(LoginActivity.this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }

        });


    }

    public void Register(View view) {
        Intent intent=new Intent(LoginActivity.this,Registration.class);
        startActivity(intent);
    }
    private void checkCredentials()
    {
        final String Email=login_email.getText().toString();
        String password=login_pass.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
        {
            login_email.requestFocus();
            login_email.setError("Email address format is not valid");
            return;
        }
        else if(password.isEmpty()  || password.length()<7)
        {
            login_pass.requestFocus();
            login_pass.setError("Password is not valid");
            return;
        }
        else
        {
            mLoadingbar.setTitle("Login");
            mLoadingbar.setMessage("Please wait, while check your credentials");
            mLoadingbar.setCanceledOnTouchOutside(false);
            mLoadingbar.show();

            mAuth.signInWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        editor.putString("username",Email);
                        editor.commit();
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        mLoadingbar.dismiss();
                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username",Email);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}