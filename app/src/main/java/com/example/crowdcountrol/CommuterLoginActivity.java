package com.example.crowdcountrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommuterLoginActivity extends AppCompatActivity {

    private EditText in_email, in_password;
    private Button btn_login, btn_register;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commuter_login);

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    Intent intent = new Intent(CommuterLoginActivity.this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        in_email = (EditText) findViewById(R.id.email);
        in_password = (EditText) findViewById(R.id.password);

        btn_login = (Button) findViewById(R.id.login_btn);
        btn_register = (Button) findViewById(R.id.register_btn);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = in_email.getText().toString();
                final String password = in_password.getText().toString();

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CommuterLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(CommuterLoginActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            String user_id = auth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Passengers").child(user_id);
                            current_user_db.setValue(true);

                            Toast.makeText(CommuterLoginActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = in_email.getText().toString();
                final String password = in_password.getText().toString();

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(CommuterLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(CommuterLoginActivity.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            String user_id = auth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Passengers").child(user_id);
                            current_user_db.setValue(true);

                            Toast.makeText(CommuterLoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        auth.removeAuthStateListener(authListener);
    }
}
