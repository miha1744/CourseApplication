package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.example.agnohendrix.androidonlinequizapp.Model.QuestionScore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Done extends AppCompatActivity {

    Button btnTry;
    TextView txtScore;
    TextView txtCorrectQuestions;

    FirebaseDatabase database;
    DatabaseReference question_score;

    //При создании активити вызываем метод
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Загружаем лейаут
        setContentView(R.layout.activity_done);

        //Назначаем кнопки и текствью
        btnTry = findViewById(R.id.btnTryAgain);
        txtScore = findViewById(R.id.txtTotalScore);
        txtCorrectQuestions = findViewById(R.id.txtTotalQuestions);

        //Получаем экземпляр  БД
        database = FirebaseDatabase.getInstance();

        //Получаем доступ к Question_Score
        question_score = database.getReference("Question_Score");


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int score = extras.getInt("SCORE");
            int correct = extras.getInt("CORRECT");
            int totalQuestions = extras.getInt("TOTAL");

            txtScore.setText(String.format("Результат: %d", score));
            txtCorrectQuestions.setText(String.format("Верно: %d/%d", correct, totalQuestions));

            //Upload to DB
            if (Common.currentUser.getUserName() != "Guest" && Common.currentUser.getPassword() != "Guest" && Common.currentUser.getEmail() != "Guest") {
                question_score.child(String.format("%s_%s", Common.currentUser.getUserName(), Common.categoryId))
                        .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName(), Common.categoryId),
                                Common.currentUser.getUserName(),
                                String.valueOf(score),
                                Common.categoryId,
                                Common.categoryName,String.valueOf(totalQuestions)));
            }
        }
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Запускаем активитити Home
                Intent intent = new Intent(Done.this, HomeUser.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
