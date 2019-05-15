package com.example.agnohendrix.androidonlinequizapp.Common;

import com.example.agnohendrix.androidonlinequizapp.Model.Question;
import com.example.agnohendrix.androidonlinequizapp.Model.User;

import java.util.ArrayList;
import java.util.List;

//Храним глобальные переменные
public class  Common {
    public static String categoryId, categoryName;
    public static User currentUser;
    public static List<Question> questionList = new ArrayList<>();
    public static List<Integer> questionFlags = new ArrayList<Integer>();
}
