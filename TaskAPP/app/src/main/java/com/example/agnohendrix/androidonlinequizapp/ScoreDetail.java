package com.example.agnohendrix.androidonlinequizapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.agnohendrix.androidonlinequizapp.Model.QuestionScore;
import com.example.agnohendrix.androidonlinequizapp.ViewHolder.ScoreDetailViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ScoreDetail extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference question_score;


    RecyclerView scoreList;
    LinearLayoutManager layoutManager;

    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder> adapter;

    String viewUser="";
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");


        scoreList = findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);



        if(getIntent()!=null)
            viewUser = getIntent().getStringExtra("viewUser");

        if(!viewUser.isEmpty())
        {
            loadScoreDetail(viewUser);
        }

    }

    private void loadScoreDetail(String viewUser)
    {

        Query query = question_score.orderByChild("user").equalTo(viewUser);
        FirebaseRecyclerOptions<QuestionScore> options =
                new FirebaseRecyclerOptions.Builder<QuestionScore>().setQuery(query,QuestionScore.class).build();


        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ScoreDetailViewHolder holder, int position, @NonNull QuestionScore model) {

                    holder.txt_name.setText(model.getCategoryName());
                    holder.txt_score.setText(model.getScore()+"/"+ model.getCategoryKol());

            }

            @NonNull
            @Override
            public ScoreDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.score_detail_layout, viewGroup, false);
                return new ScoreDetailViewHolder(view);
            }
        };
        scoreList.setAdapter(adapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }
}
