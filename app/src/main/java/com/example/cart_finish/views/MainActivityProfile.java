package com.example.cart_finish.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cart_finish.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityProfile extends AppCompatActivity {

        private ImageView btnLogout;
        private TextView nameUser, emailUser;
        private LinearLayout myorders;
        private LinearLayout mycart;
        private LinearLayout history;
        private LinearLayout info;
        private FirebaseUser firebaseUser;
        GoogleSignInOptions gso;
        GoogleSignInClient gsc;
        BottomNavigationView bottomNavigationView;

        @SuppressLint({"SetTextI18n", "MissingInflatedId"})
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_profile);

            btnLogout = findViewById(R.id.logout);
            nameUser = findViewById(R.id.name_user);
            emailUser = findViewById(R.id.email_user);
            myorders = (LinearLayout) findViewById(R.id.myorders);
            mycart = (LinearLayout) findViewById(R.id.mycart);
            history = (LinearLayout) findViewById(R.id.history);
            info = (LinearLayout) findViewById(R.id.info);

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            gsc = GoogleSignIn.getClient(this,gso);

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account!=null){
                String personName = account.getDisplayName();
                String personEmail = account.getEmail();
                nameUser.setText(personName);
                emailUser.setText(personEmail);
            }else{
                nameUser.setText("Undefine User");
                emailUser.setText("undefine_email_auth");
                Toast.makeText(getApplicationContext(), "USER NOT FOUND!", Toast.LENGTH_SHORT).show();
            }

            if (firebaseUser!=null){
                nameUser.setText(firebaseUser.getDisplayName());
                emailUser.setText(firebaseUser.getEmail());
            }else{
                nameUser.setText("Undefine User");
                emailUser.setText("undefine_email_auth");
                Toast.makeText(getApplicationContext(), "USER NOT FOUND!", Toast.LENGTH_SHORT).show();
            }



            btnLogout.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivityLogin.class));
                finish();
            });

            myorders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myorders = new Intent(getApplicationContext(), MainActivityMyOrder.class);
                    startActivity(myorders);
                }
            });

            mycart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mycart = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(mycart);
                }

            });

            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent history = new Intent(getApplicationContext(), MainActivityHistory.class);
                    startActivity(history);
                }
            });

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent info = new Intent(getApplicationContext(), MainActivityMyInfo.class);
                    startActivity(info);
                }
            });

            bottomNavigationView = findViewById(R.id.navigation_bottom);

            bottomNavigationView.setSelectedItemId(R.id.cartmenu);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.cartmenu:
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(0,0);
                            return true;

                        case R.id.reviewmenu:
                            startActivity(new Intent(getApplicationContext(),MainActivityReview.class));
                            overridePendingTransition(0,0);
                            return true;

                        case R.id.scannermenu:
                            startActivity(new Intent(getApplicationContext(),MainActivityScanner.class));
                            overridePendingTransition(0,0);
                            return true;

                        case R.id.profilemenu:
                            return true;
                    }

                    return false;
                }
            });
        }


    }