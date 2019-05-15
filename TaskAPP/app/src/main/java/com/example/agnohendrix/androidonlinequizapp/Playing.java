package com.example.agnohendrix.androidonlinequizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

public class Playing extends AppCompatActivity implements View.OnClickListener{



    final static long INTERVAL = 1000;//1 секунда интервал
    // 60 секунд на заполнение опроса
    final static long START_TIME = 20*60*1000;
    private  long mTimeLeftInMillis = START_TIME;

    int size = Common.questionList.size();

    CountDownTimer mCountDown;

    int index=0, score=0, thisQuestion=0, totalQuestion=0;
    int[] myArray , myArraySec;
    public int kol;


    ImageView question_image;
    Button btnA, btnB, btnC, btnD , btnPrev, btnNxt;
    ImageButton  btnExt;
    TextView  txtQuestionNum, question_text, mTextTimer, NameCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myArray = new int[Common.questionList.size()];
        myArraySec = new int[Common.questionList.size()];

        Intent intent = getIntent();

        for (int i =0;i<size; i++)
        {
            myArray[i]=0;
            myArray[i]=0;
        }


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_playing);

        mTextTimer = findViewById(R.id.timerl);

        totalQuestion = Common.questionList.size();

        //View
        txtQuestionNum = findViewById(R.id.txtTotalQuestions);
        question_text = findViewById(R.id.question_text);
        question_image = findViewById(R.id.question_image);
        NameCategory = findViewById(R.id.title_play);
        String str = intent.getStringExtra("Category");
        NameCategory.setText(str);
        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);
        btnExt = findViewById(R.id.btn_ext);
        btnNxt = findViewById(R.id.btn_next);
        btnPrev = findViewById(R.id.btn_prev);
        btnExt = findViewById(R.id.btn_ext);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Playing.this);
        alertDialog.setMessage("Выходя из опроса, вы лишаетесь возможности получить результат по данному тесту");
        alertDialog.setNegativeButton("Вернуться к тесту", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                //Закрываем диалоговое окно
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("ПОКИНУТЬ ТЕСТ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intentl = new Intent(Playing.this, Home.class);
                startActivity(intentl);
                finish();
                dialogInterface.dismiss();
            }
        });



        btnExt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertDialog.show();
            }
        });



        btnA.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        btnA.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
                                        btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                                        btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                                        btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));

                                        myArraySec[index]=1;


                                        if(btnA.getText().equals(Common.questionList.get(index).getCorrectAnswer()))
                                        {
                                            myArray[index] = 1;
                                        }else
                                            myArray[index] = 0;

                                    }
                                });


        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));
                myArraySec[index]=2;


                if(btnB.getText().equals(Common.questionList.get(index).getCorrectAnswer()))
                {
                    myArray[index] = 1;
                }else
                    myArray[index] = 0;

            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));
                myArraySec[index]=3;


                if(btnC.getText().equals(Common.questionList.get(index).getCorrectAnswer()))
                {
                    myArray[index] = 1;
                }else
                    myArray[index] = 0;

            }
        });

        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
                myArraySec[index]=4;

                if(btnD.getText().equals(Common.questionList.get(index).getCorrectAnswer()))
                {
                    myArray[index] = 1;
                }else
                    myArray[index] = 0;
            }
        });


        btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showQuestion(++index);
            }
        });



            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(index!=0) {
                        showQuestionBack(--index);
                    }
                }
            });

        showQuestion(0);
        countTimer();

    }

    @Override
    public void onClick(View v)
    {
    }



    private void showQuestion(int i) {
        //пока не последний вопрос, загружаем новые
        if(i < totalQuestion){
            thisQuestion++;
            txtQuestionNum.setText(String.format("вопрос %d из %d", thisQuestion, totalQuestion));

            //Если картика есть, то загружаем ее, если ее нет то делаем область картинки невидимой
            if(Common.questionList.get(i).getIsImageQuestion().equals("true")){
                Picasso.get().load(Common.questionList.get(i).getImage()).into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setText(Common.questionList.get(i).getQuestion());
            } else {
                question_text.setText(Common.questionList.get(i).getQuestion());
                question_image.setVisibility(View.INVISIBLE);
            }

            btnA.setText(Common.questionList.get(i).getAnswerA());
            btnB.setText(Common.questionList.get(i).getAnswerB());
            btnC.setText(Common.questionList.get(i).getAnswerC());
            btnD.setText(Common.questionList.get(i).getAnswerD());

            if(index==totalQuestion-1)
            {
                btnNxt.setText("Закончить тест");
            }else
                btnNxt.setText("Следующий вопрос");

            if(index==0)
                btnPrev.setVisibility(View.INVISIBLE);
            else
                btnPrev.setVisibility(View.VISIBLE);

            if(myArraySec[i]==0)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));
            }else if(myArraySec[i]==1)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));

            }else if(myArraySec[i]==2)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));

            }else if(myArraySec[i]==3)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));
            }else if(myArraySec[i]==4)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
            }

        } else
            {
                kol =0;
                for (int j : myArray)
                {
                    if(j==1) {
                        kol++;
                    }
                }

                Intent intent = new Intent(Playing.this, Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE", kol);
                dataSend.putInt("TOTAL",totalQuestion);
                dataSend.putInt("CORRECT",kol);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();
        }
    }




    private void showQuestionBack(int i) {
        //пока не последний вопрос, загружаем новые
        if(i < totalQuestion && i>-1)
        {
            thisQuestion--;
            txtQuestionNum.setText(String.format("вопрос %d из %d", thisQuestion, totalQuestion));

            //Если картика есть, то загружаем ее, если ее нет то делаем область картинки невидимой
            if(Common.questionList.get(i).getIsImageQuestion().equals("true")){
                Picasso.get().load(Common.questionList.get(i).getImage()).into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setText(Common.questionList.get(i).getQuestion());
            } else {
                question_text.setText(Common.questionList.get(i).getQuestion());
                question_image.setVisibility(View.INVISIBLE);
            }

            btnA.setText(Common.questionList.get(i).getAnswerA());
            btnB.setText(Common.questionList.get(i).getAnswerB());
            btnC.setText(Common.questionList.get(i).getAnswerC());
            btnD.setText(Common.questionList.get(i).getAnswerD());

            if(myArraySec[i]==0)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));
            }else if(myArraySec[i]==1)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));

            }else if(myArraySec[i]==2)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));

            }else if(myArraySec[i]==3)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners));
            }else if(myArraySec[i]==4)
            {
                btnA.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnB.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnC.setBackground(getResources().getDrawable(R.drawable.round_corners));
                btnD.setBackground(getResources().getDrawable(R.drawable.round_corners_active));
            }

        }
    }



 private void countTimer()
 {
        if(mCountDown==null)
        {
            mCountDown = new CountDownTimer(START_TIME,1000)
            {
                @Override
                public void onTick(long l) {
                    mTextTimer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(l),
                            TimeUnit.MILLISECONDS.toSeconds(l) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                    mTimeLeftInMillis-=1000;


                }

                @Override
                public void onFinish() {

                }
            }.start();
        }else
        {
            mCountDown.cancel();
            mCountDown = new CountDownTimer(START_TIME,1000)
            {
                @Override
                public void onTick(long l) {
                    mTextTimer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(l),
                            TimeUnit.MILLISECONDS.toSeconds(l) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                    mTimeLeftInMillis-=1000;


                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
 }

}
