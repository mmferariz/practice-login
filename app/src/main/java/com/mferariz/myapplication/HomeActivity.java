package com.mferariz.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.mferariz.myapplication.controller.UserController;
import com.mferariz.myapplication.databinding.ActivityHomeBinding;
import com.mferariz.myapplication.model.Session;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    private TextView txtEmail;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        userController = new UserController(this);
        View view = binding.navView.getHeaderView(0);
        txtEmail = view.findViewById(R.id.txt_email);
        setSessionData();


        Button logoutButton = view.findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(view1 -> {
            userController.clearSession();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        });
        //avatar = view.findViewById(R.id.avatar);
        //TextView txtOpc = view.findViewById(R.id.txt_opc);

    }

    private void setSessionData() {
        Session sesion = userController.getSession();
        txtEmail.setText(sesion.getEmail());
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}