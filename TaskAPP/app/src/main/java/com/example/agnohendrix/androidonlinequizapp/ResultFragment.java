package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.example.agnohendrix.androidonlinequizapp.Interface.ItemClickListener;
import com.example.agnohendrix.androidonlinequizapp.Interface.RankingCallback;
import com.example.agnohendrix.androidonlinequizapp.Model.Question;
import com.example.agnohendrix.androidonlinequizapp.Model.QuestionScore;
import com.example.agnohendrix.androidonlinequizapp.Model.Ranking;
import com.example.agnohendrix.androidonlinequizapp.ViewHolder.RankingViewHolder;
import com.example.agnohendrix.androidonlinequizapp.ViewHolder.ScoreDetailViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ResultFragment extends Fragment {

    View myFragment;
    FirebaseDatabase database;
    DatabaseReference question_score, questions;
    int i =0;

    RecyclerView scoreList;
    LinearLayoutManager layoutManager;
    List<String> lst;

    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder> adapter;

    public static ResultFragment newInstance(){
        ResultFragment resultFragment = new ResultFragment();
        return resultFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Получаем всю информацию из FireBase
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");
        questions = database.getReference("Questions");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Назначаем layout для этого фрагмента
        myFragment = inflater.inflate(R.layout.fragment_result, container, false);

        scoreList = (RecyclerView) myFragment.findViewById(R.id.rankingList_forU);


        layoutManager = new LinearLayoutManager(getActivity());
        scoreList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        scoreList.setLayoutManager(layoutManager);
        loadScoreDetail(Common.currentUser.getUserName());
        return myFragment;
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
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }

}
