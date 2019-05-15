package com.example.agnohendrix.androidonlinequizapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.agnohendrix.androidonlinequizapp.R;


public class ScoreDetailViewHolder extends RecyclerView.ViewHolder
{

    public TextView txt_name, txt_score;

    public ScoreDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name1);
        txt_score = itemView.findViewById(R.id.txt_score1);

    }
}
