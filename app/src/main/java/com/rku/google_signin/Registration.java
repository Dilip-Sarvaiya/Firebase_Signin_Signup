package com.rku.google_signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
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


public class Registration extends AppCompatActivity {

    Button btn;
    Button register_btn;

    private EditText uname,email,pass,con_pass;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        btn=findViewById(R.id.already_acc);
        uname=findViewById(R.id.uname);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.psw);
        con_pass=findViewById(R.id.con_psw);
        mAuth=FirebaseAuth.getInstance();
        mLoadingbar=new ProgressDialog(Registration.this);


        register_btn=findViewById(R.id.signup_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });
    }
    private void checkCredentials()
    {
        String username=uname.getText().toString();
        String Email=email.getText().toString();
        String password=pass.getText().toString();
        String con_password=con_pass.getText().toString();

        if(username.isEmpty()  || username.length()<7)
        {
            uname.requestFocus();
            uname.setError("Username is not valid");
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
        {
            email.requestFocus();
            email.setError("Email address format is not valid");
            return;
        }
        else if(password.isEmpty()  || password.length()<7)
        {
            pass.requestFocus();
            pass.setError("Password is not valid");
            return;
        }
        else if(!(con_password.equals(password)))
        {
            con_pass.requestFocus();
            con_pass.setError("Confirm password is not matched");
            return;
        }
        else
        {
            mLoadingbar.setTitle("Registration");
            mLoadingbar.setMessage("Please wait, while check your credentials");
            mLoadingbar.setCanceledOnTouchOutside(false);
            mLoadingbar.show();

            mAuth.createUserWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Registration.this, "Successfully Registration", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Registration.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(Registration.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}