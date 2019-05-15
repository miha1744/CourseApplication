package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.facebook.login.LoginManager;


public class HomeUser extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        bottomNavigationView = findViewById(R.id.navigationl);


        //На ботом навигатион вью назначаем лисенер и смотри
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Создаем переменную типа фрагмент
                Fragment selectedFragment = null;

                //смотрим на пункты меню с индентификаторами
                switch(menuItem.getItemId()){
                    case R.id.action_category:
                        selectedFragment = TopicsFragment.newInstance();
                        break;
                    case R.id.action_ranking:
                        selectedFragment = ResultFragment.newInstance();
                        break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        setDefaultFragment();


    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, TopicsFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(HomeUser.this, "Вернулись на страницу  входа", Toast.LENGTH_SHORT).show();
        Common.currentUser.unset();
        LoginManager.getInstance().logOut();
        Intent back = new Intent(HomeUser.this, MainActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(back);
        finish();
    }
}
