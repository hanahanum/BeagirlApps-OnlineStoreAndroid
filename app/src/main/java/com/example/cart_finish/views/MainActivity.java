package com.example.cart_finish.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cart_finish.R;
import com.example.cart_finish.utils.adapter.ProdukItemAdapter;
import com.example.cart_finish.utils.model.ProdukCart;
import com.example.cart_finish.utils.model.ProdukItem;
import com.example.cart_finish.viewmodel.CartViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ProdukItemAdapter.ProdukClickedListeners {

    private RecyclerView recyclerView;
    private List<ProdukItem> produkItemList;
    private ProdukItemAdapter adapter;
    private CartViewModel viewModel;
    private List<ProdukCart> produkCartList;
    private CoordinatorLayout coordinatorLayout;
    private ImageView cartImageView, btnTrigger1;
    BottomNavigationView bottomNavigationView;
    private SearchView searchView;
    private FirebaseUser firebaseUser;
    private TextView textName;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables();
        setUpList();

        adapter.setProdukItemList(produkItemList);
        recyclerView.setAdapter(adapter);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                filterList(text);
                return true;
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
                        startActivity(new Intent(getApplicationContext(),MainActivityProfile.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null){
            String personName = account.getDisplayName();
            textName.setText(personName);
        }else {
            textName.setText("Undefine User");
        }

        if (firebaseUser!=null){
            textName.setText(firebaseUser.getDisplayName());
        }else {
            textName.setText("Undefine User");
        }

        cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        ImageSlider imageSlider = findViewById(R.id.imageslider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.image3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image5, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        btnTrigger1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Beagirl Notification");
                builder.setContentTitle("My title");
                builder.setContentText("Hi,This is Beagirl App, Shop Now!");
                builder.setSmallIcon(R.drawable.ic_baseline_add_shopping_cart_24);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                managerCompat.notify(1, builder.build());


            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getAllCartItems().observe(this, new Observer<List<ProdukCart>>() {
            @Override
            public void onChanged(List<ProdukCart> produkCarts) {
                produkCartList.addAll(produkCarts);
            }
        });
    }

    private void setUpList(){
        produkItemList.add(new ProdukItem("SKINTIFIC Skin Barrier, Repair", "SKINTIFIC", R.drawable.skintific, 139000));
        produkItemList.add(new ProdukItem("Wardah Hydra Rose Dewy Aqua Day Gel", "Wardah", R.drawable.wardah,68000));
        produkItemList.add(new ProdukItem("Simple Hydrating Light Moisturizer", "Simple", R.drawable.simple,78000));
        produkItemList.add(new ProdukItem("Scarlett Whitening Brighlty Ever After Day Cream", "Scarlett", R.drawable.scarlet,56000));
        produkItemList.add(new ProdukItem("Lotions and moisturizers Vaseline", "vaseline", R.drawable.vaseline,65000));
        produkItemList.add(new ProdukItem("Cetaphil Pro AD Skin Restoring 295 mL", "Cetaphil", R.drawable.cetaphil,120000));
        produkItemList.add(new ProdukItem("Azarine Oil Free Brightening Daily", "Azarine", R.drawable.azarine,48000));
        produkItemList.add(new ProdukItem("Ponds Juice Collection Gel moisturizer", "Ponds", R.drawable.pondbs,37000));
        produkItemList.add(new ProdukItem("Cerave Daily Moisturizing Lotions", "Cerafe", R.drawable.cerafe,250000));
        produkItemList.add(new ProdukItem("Originote Hyalucera Moisturizer", "Originote", R.drawable.originote,48000));
        produkItemList.add(new ProdukItem("NPURE Noni Probiotics Moisturizer", "NPURE", R.drawable.npure,80000));
        produkItemList.add(new ProdukItem("Florence by mills Brighten up Brightening toner", "Florence by mills", R.drawable.toner1, 300000));
        produkItemList.add(new ProdukItem("Eau De Parfume Burberry", "My Burberry", R.drawable.burberry, 900000));
        produkItemList.add(new ProdukItem("Giambattista Valli Mac Lipstick", "MAC", R.drawable.maclipstick, 300000));
        produkItemList.add(new ProdukItem("Maybelline the nude eyeshadow pallate makeup", "Maybelline", R.drawable.eyeshadowmeybeline, 250000));
        produkItemList.add(new ProdukItem("Skin Boost Sheet Mask Skincare from CAIA", "CAIA", R.drawable.sheetmask, 55000));
        produkItemList.add(new ProdukItem("Pure Color Envy Nighttime Rescue Lip Oil Serum", "Estee Lauder", R.drawable.lipbalm, 400000));
        produkItemList.add(new ProdukItem("A revitalizing treatment infused with precious flower oils", "Scarlett", R.drawable.scarlettbodywash, 75000));


    }

    private void filterList(String text) {
        List<ProdukItem> filteredList = new ArrayList<>();
        for (ProdukItem item : produkItemList){
            if (item.getProdukName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(this,"No data found", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }

    private void initializeVariables(){

        cartImageView = findViewById(R.id.cartIV);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        produkCartList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        produkItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.mainRecylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        searchView = findViewById(R.id.searchView1);
        textName = findViewById(R.id.name);
        adapter = new ProdukItemAdapter(this);
        btnTrigger1 = findViewById(R.id.btn_trigger1);

    }

    @Override
    public void onCardClicked(ProdukItem produk) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("produkItem", produk);
        startActivity(intent);
    }

    @Override
    public void onAddToCartBtnClicked(ProdukItem produkItem) {
        ProdukCart produkCart = new ProdukCart();
        produkCart.setProdukName(produkItem.getProdukName());
        produkCart.setProdukBrandName(produkItem.getProdukBrandName());
        produkCart.setProdukPrice(produkItem.getProdukPrice());
        produkCart.setProdukImage(produkItem.getProdukImage());

        final int[] quantity = {1};
        final int[] id = new int[1];

        if (!produkCartList.isEmpty()){

            for (int i=0;i<produkCartList.size();i++){
                if (produkCart.getProdukName().equals(produkCartList.get(i).getProdukName())){
                    quantity[0] = produkCartList.get(i).getQuantity();
                    quantity[0]++;
                    id[0] = produkCartList.get(i).getId();
                }
            }
        }

        if (quantity[0]==1){
            produkCart.setQuantity(quantity[0]);
            produkCart.setTotalItemPrice(quantity[0]*produkCart.getProdukPrice());
            viewModel.insertCartItem(produkCart);
        }else{
            viewModel.updateQuantity(id[0], quantity[0]);
            viewModel.updatePrice(id[0], quantity[0]*produkCart.getProdukPrice());
        }

        makeSnackBar("Item Added To Cart");
    }

    private void makeSnackBar(String msg){
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT)
                .setAction("Go to Cart", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this , CartActivity.class));
                    }
                }).show();
    }
}