package com.asif047.firebasechatasif;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword;
    private Button btnSignUp, btnSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edittext_name);
        edtEmail = findViewById(R.id.edittext_email);
        edtPassword = findViewById(R.id.edittext_password);

        btnSignUp = findViewById(R.id.button_sign_up);
        btnSignIn = findViewById(R.id.button_sign_in);


        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            finish();
            startActivity( new Intent(getApplicationContext(), WelcomeActivity.class));
        }

      btnSignIn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              callsignin(edtEmail.getText().toString(), edtPassword.getText().toString());
          }
      });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callsignup(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });

    }



    private void callsignup( String email, String password) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "Signed up failed", Toast.LENGTH_SHORT).show();

                        } else {
                            userProfile();
                            Toast.makeText(MainActivity.this, "Signed up Successful", Toast.LENGTH_SHORT).show();
                            Log.e("TESTING:", "Created Account");
                        }


                    }
                });


    }


    private void userProfile() {

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(edtName.getText().toString().trim())
                    //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {
                                Log.e("TESTING2:", "User Profile Updated");
                            }

                        }
                    });
        }

    }




    private void callsignin(String email, String password) {
        mAuth.signInWithEmailAndPassword( email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("##TESTING3:", "Sign in Successful "+task.isSuccessful());

                        if(!task.isSuccessful()) {
                            Log.e("TESTING4:", "SignInWithEmail: Failed", task.getException());
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }
                });
    }



}
