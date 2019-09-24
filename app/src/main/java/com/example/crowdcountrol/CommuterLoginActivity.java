package com.example.crowdcountrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CommuterLoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button btn_login, btn_register;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commuter_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        btn_login = (Button) findViewById(R.id.login_btn);
        btn_register = (Button) findViewById(R.id.register_btn);
    }
}
