package com.example.ecommerceproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.dto.RegisterAccountModel;
import com.example.ecommerceproject.entities.Account;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.UUID;

import lombok.NonNull;

public class RegisterAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        getSupportActionBar().hide();

        //init db
        db = AppDatabase.getInstance(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.txtLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));
                finish();
            }
        });

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.txtEmail);
                EditText password = findViewById(R.id.txtPassword);
                EditText name = findViewById(R.id.txtName);
                EditText confirmPassword = findViewById(R.id.txtConfirmPassword);

                RegisterAccountModel registerAccountModel = new RegisterAccountModel()
                        .setEmail(email.getText().toString())
                        .setName(name.getText().toString())
                        .setPassword(password.getText().toString())
                        .setConfirmPassword(confirmPassword.getText().toString());

                if (isValid(registerAccountModel)) {
                    register(registerAccountModel);
                }
            }
        });

    }

    private void register(RegisterAccountModel registerAccountModel) {
        mAuth.createUserWithEmailAndPassword(registerAccountModel.getEmail(), registerAccountModel.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterAccountActivity.this, "Register successfully", Toast.LENGTH_LONG).show();
                            db.accountDao().insertAll(new Account()
                                    .setId(user.getUid())
                                    .setEmail(registerAccountModel.getEmail())
                                    .setName(registerAccountModel.getName()));
                            startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterAccountActivity.this, "Register fail", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private boolean isValid(RegisterAccountModel registerAccountModel) {
        boolean result = true;
        Account account = db.accountDao().getByEmail(registerAccountModel.getEmail());

        if (Strings.isEmptyOrWhitespace(registerAccountModel.getEmail())) {
            ((TextView) findViewById(R.id.txtErrorEmail)).setText("Please input email.");
            result = false;
        }
        if (account != null) {
            ((TextView) findViewById(R.id.txtErrorEmail)).setText("This email has been registered.");
            result = false;
        }
        if (Strings.isEmptyOrWhitespace(registerAccountModel.getName().toString())) {
            ((TextView) findViewById(R.id.txtErrorName)).setText("Please input name.");
            result = false;
        }
        if (Strings.isEmptyOrWhitespace(registerAccountModel.getPassword())) {
            ((TextView) findViewById(R.id.txtErrorPassword)).setText("Please input password.");
            result = false;
        }
        if (Strings.isEmptyOrWhitespace(registerAccountModel.getConfirmPassword())) {
            ((TextView) findViewById(R.id.txtErrorConfirmPassword)).setText("Please input confirm password.");
            result = false;
        }
        if (!Objects.equals(registerAccountModel.getPassword(), registerAccountModel.getPassword())) {
            ((TextView) findViewById(R.id.txtErrorConfirmPassword)).setText("Confirm password is not match with password.");
            result = false;
        }
        return result;
    }
}