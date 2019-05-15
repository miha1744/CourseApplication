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
import com.example.agnohendrix.androidonlinequizapp.Model.QuestionScore;
import com.example.agnohendrix.androidonlinequizapp.Model.Ranking;
import com.example.agnohendrix.androidonlinequizapp.ViewHolder.RankingViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RankingFragment extends Fragment {
    View myFragment;

    FirebaseDatabase database;
    DatabaseReference questionScore, rankingtbl;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    int sum=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Получаем всю информацию из FireBase
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        //Данные Ranking
        rankingtbl = database.getReference("Ranking");
        rankingtbl.keepSynced(true);
        questionScore.keepSynced(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Назначаем layout для этого фрагмента
        myFragment = inflater.inflate(R.layout.fragment_ranking, container, false);

        rankingList = (RecyclerView) myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);

        //БД orderbychild сортит лист по возростанию меняем направление
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);


        updateRanking(Common.currentUser.getUserName(), new RankingCallback<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingtbl.child(ranking.getUserName()).setValue(ranking);
            }
        });


        FirebaseRecyclerOptions<Ranking> options =
                new FirebaseRecyclerOptions.Builder<Ranking>()
                    .setQuery(rankingtbl.orderByChild("score"),Ranking.class).build();

        //Берем данные и расставляем в списке по поряжкеу
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(options) {

            @NonNull
            @Override
            public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_ranking, viewGroup, false);
                RankingViewHolder viewHolder = new RankingViewHolder(view);
                return viewHolder;
            }


            @Override
            protected void onBindViewHolder(@NonNull RankingViewHolder holder, int position, @NonNull final Ranking model) {
                holder.ranking_name.setText(model.getUserName());
                holder.ranking_score.setText(String.valueOf(model.getScore()));

                if(model.getUserName().equals(Common.currentUser.getUserName())){
                    holder.itemView.setBackgroundColor(Color.GREEN);
                }

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(),ScoreDetail.class);
                        scoreDetail.putExtra("viewUser", model.getUserName());
                        startActivity(scoreDetail);
                    }
                });
            }
        };
        if(Common.currentUser.getUserName() == "Guest" && Common.currentUser.getPassword() == "Guest" && Common.currentUser.getEmail() == "Guest") {
            Toast.makeText(getContext(), "Не получается сохранить данные оффлайн", Toast.LENGTH_LONG).show();
        }

        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
        adapter.startListening();
        return myFragment;
    }

    private void showRanking() {

        rankingtbl.orderByChild("score")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            Ranking local = data.getValue(Ranking.class);
                            Log.d("DEBUG", local.getUserName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    //Увеличивае очки Usera DataSnapshot - Данные которые мы получаем
    private void updateRanking(final String userName, final RankingCallback<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum+=Integer.parseInt(ques.getScore());

                        }
                        //
                        if(Common.currentUser.getUserName() != "Guest" && Common.currentUser.getPassword() != "Guest" && Common.currentUser.getEmail() != "Guest") {
                            Ranking ranking = new Ranking(userName, sum);
                            callback.callBack(ranking);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
