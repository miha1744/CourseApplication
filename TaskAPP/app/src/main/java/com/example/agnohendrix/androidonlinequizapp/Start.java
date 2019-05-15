package com.example.agnohendrix.androidonlinequizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.example.agnohendrix.androidonlinequizapp.Model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class Start extends AppCompatActivity {

    Button btnPlay;
    Button btnReturn;

    FirebaseDatabase database;
    DatabaseReference questions;

    //Логика
    @Override
    protected void onCreate(Bundle savedInstanceState) {





        //Загружаем
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final Intent intent = getIntent();
        String image = intent.getStringExtra("Image");
        ImageView iv = findViewById(R.id.logo);
        Picasso.get().load(image).into(iv);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Start.this);
        alertDialog.setMessage("Вам дается 20 минут на прохождение теста.Будьте внимательными.");

        alertDialog.setNegativeButton("НАЗАД", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                //Закрываем диалоговое окно
                dialogInterface.dismiss();
            }
        });

        final TextView tv = findViewById(R.id.category_name_start);
        tv.setText(intent.getStringExtra("Category"));

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        loadQuestion(Common.categoryId);

        //При нажатии на кнопку начинаем тест

        btnPlay = findViewById(R.id.btn_play);
        btnReturn = findViewById(R.id.btn_return_to_main);

        alertDialog.setPositiveButton("ПРИСТУПИТЬ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(Start.this, Playing.class);
                intent.putExtra("Category",tv.getText());
                startActivity(intent);
                finish();
                dialogInterface.dismiss();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(Start.this, HomeUser.class);
                intent1.putExtra("Category", intent.getStringExtra("Category"));
                startActivity(intent1);
                finish();
            }
        });

    }

    private void loadQuestion(String categoryId) {

        if(Common.questionList.size() > 0)
            Common.questionList.clear();




        questions.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Question quest = postSnapshot.getValue(Question.class);
                    Common.questionList.add(quest);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        Common.questionFlags = new ArrayList<Integer>(Common.questionList.size());
        Collections.shuffle(Common.questionList);
    }
}
