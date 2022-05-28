package com.example.trainer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trainer.db.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trainer.databinding.ActivityUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference users;
    private StorageReference storageRef;
    private TextView lastName, firstName, secondName;
    private ImageView avatar1;
    public static String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarUser.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        initialization();
        getDataBase();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_contacts, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void initialization() {
        //Поиск элементов по id
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        firstName = (TextView) headerView.findViewById(R.id.userFirstName);
        lastName = (TextView) headerView.findViewById(R.id.userLastName);
        secondName = (TextView) headerView.findViewById(R.id.userSecondName);
        avatar1 = (ImageView) headerView.findViewById(R.id.userAvatar);
        mAuth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance().getReference(User.USER_KEY);
        //Получение id авторизованного пользователя
        userID = mAuth.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference(userID);
    }

    //Получение данных из БД
    private void getDataBase() {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(userID).getValue(User.class);
                if (!user.image.isEmpty()) Picasso.get().load(user.image).into(avatar1);
                firstName.setText(user.first);
                lastName.setText(user.last);
                secondName.setText(user.second);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /////////////////////////////////////////////////
    //Функции для подстановки номера в поле вызова//
    ///////////////////////////////////////////////
    public void openPhone1(View view) {
        String dial = "tel:+7(910)880-87-98";
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }

    public void openPhone2(View view) {
        String dial = "tel:+7(910)880-96-47";
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }
}