package com.example.agnohendrix.androidonlinequizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agnohendrix.androidonlinequizapp.ViewHolder.CategoryViewHolder;
import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.example.agnohendrix.androidonlinequizapp.Interface.ItemClickListener;
import com.example.agnohendrix.androidonlinequizapp.Model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class TopicsFragment extends Fragment {

    View myFragment;

    RecyclerView listCategory;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference categories;

    public static TopicsFragment newInstance(){
        TopicsFragment topicsFragment = new TopicsFragment();
        return topicsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //При создание Фрагмента инициализируем данными  из нашей БД
        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");
    }

    //Что отображать при вызове фрагмента
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //Создаем View с помощью inflater аналог SetContetntView
        myFragment = inflater.inflate(R.layout.fragment_topics, container, false);


        //Получаем доступ в элементам
        listCategory = myFragment.findViewById(R.id.listCategory);
        listCategory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager((container.getContext()));
        listCategory.setLayoutManager(layoutManager);



        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(categories,Category.class)
                        .build();


        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options

        ) {
            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_layout, viewGroup, false);
                CategoryViewHolder viewHolder = new CategoryViewHolder(view);
                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final Category model) {

                holder.category_name.setText(model.getName());

                //С помощью бибилиотеки Picasso Загружаем изображение в category_image
                Picasso.get().load(model.getImage()).into(holder.category_image);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //После нажатия о открывае
                        Intent startGame = new Intent(getActivity(), Start.class);
                        Common.categoryId = adapter.getRef(position).getKey();
                        Common.categoryName=model
                                .getName();
                        startGame.putExtra("Category", model.getName());
                        startGame.putExtra("Image", model.getImage());
                        startActivity(startGame);
                    }
                });
            }

        };

        adapter.notifyDataSetChanged();
        listCategory.setAdapter(adapter);
        adapter.startListening();
        //Отдаем view системе
        return myFragment;
    }

}
