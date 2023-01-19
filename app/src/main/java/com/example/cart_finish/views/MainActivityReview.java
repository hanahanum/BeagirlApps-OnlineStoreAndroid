package com.example.cart_finish.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cart_finish.R;
import com.example.cart_finish.utils.adapter.ProdukItemAdapter;
import com.example.cart_finish.utils.model.ProdukItem;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

public class MainActivityReview extends AppCompatActivity {

        private ImageView btnTrigger, cartImageView, backbtn;
        private FirebaseUser firebaseUser;
        private TextView textName;
        GoogleSignInOptions gso;
        GoogleSignInClient gsc;
        BottomNavigationView bottomNavigationView;
        ImageView ulasan1, ulasan2, ulasan3;



        @SuppressLint({"SetTextI18n", "MissingInflatedId", "WrongViewCast"})
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_review);

            btnTrigger = findViewById(R.id.btn_trigger);
            backbtn = findViewById(R.id.backbtn);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("Beagirl Notification", "Notification", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }

            btnTrigger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivityReview.this, "Beagirl Notification");
                    builder.setContentTitle("My title");
                    builder.setContentText("Hi,This is Beagirl App, Shop Now!");
                    builder.setSmallIcon(R.drawable.ic_baseline_add_shopping_cart_24);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivityReview.this);
                    managerCompat.notify(1, builder.build());


                }
            });

            initializeVariables();

            ulasan1 = findViewById(R.id.ulasan1);
            ulasan2 = findViewById(R.id.ulasan2);
            ulasan3 = findViewById(R.id.ulasan3);

            ulasan1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoUrl("https://www.youtube.com/watch?v=mFDLWrnTVL0");
                }
            });

            ulasan2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoUrl("https://www.youtube.com/watch?v=U-asdyL-dzQ");
                }
            });

            ulasan3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoUrl("https://www.youtube.com/watch?v=JLMJfpffy_c");
                }
            });

            bottomNavigationView.setSelectedItemId(R.id.reviewmenu);
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

                            return true;

                        case R.id.scannermenu:
                            startActivity(new Intent(getApplicationContext(),MainActivityScanner.class));
                            overridePendingTransition(0,0);
                            return true;

                        case R.id.profilemenu:
                            startActivity(new Intent(getApplicationContext(),MainActivityProfile.class));
                            overridePendingTransition(0,0);
                            return true;
                    }

                    return false;
                }
            });

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            gsc = GoogleSignIn.getClient(this,gso);

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account!=null){
                String personName = account.getDisplayName();
                textName.setText(personName);
            }else{
                textName.setText("Login Gagal");
            }


            if(firebaseUser!=null){
                textName.setText(firebaseUser.getDisplayName());
            }else{
                textName.setText("Login Gagal");
            }

            cartImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivityReview.this, CartActivity.class));
                    finish();
                }

            });

            backbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivityReview.this, MainActivity.class));
                    finish();
                }
            });

        }


    private void initializeVariables(){
            textName = findViewById(R.id.usernamereview);
            bottomNavigationView = findViewById(R.id.navigation_bottom);
            btnTrigger = findViewById(R.id.btn_trigger);
            cartImageView = findViewById(R.id.cartV);
        }

    private void gotoUrl(String s) {
            Uri uri = Uri.parse(s);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}