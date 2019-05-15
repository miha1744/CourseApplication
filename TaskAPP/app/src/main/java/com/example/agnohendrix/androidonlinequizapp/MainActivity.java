package com.example.agnohendrix.androidonlinequizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agnohendrix.androidonlinequizapp.Common.Common;
import com.example.agnohendrix.androidonlinequizapp.Model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoginStatusCallback;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    MaterialEditText editNewUserName, editNewPassword, editNewEmail; //Для регистрации

    MaterialEditText editUserName, editPassword; //Для входа

    Button btnSignUp, btnSignIn, btnOffline;


    FirebaseDatabase database;
    DatabaseReference users;
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Получаем экземпляр нашей Бд
        database = FirebaseDatabase.getInstance();

        //Получаем доступ к Users
        users = database.getReference("Users");


        editUserName = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        //При нажатии на кнопку метод SignUp
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignupDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(editUserName.getText().toString(), editPassword.getText().toString());
            }
        });
        btnOffline = findViewById(R.id.btn_offline);

        //Лисенер для кнопки
        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Common.currentUser = new User("Guest", "Guest", "Guest");
                //запусскаем новое активити
                Intent homeActivity = new Intent(MainActivity.this, Home.class);
                startActivity(homeActivity);
                //В лог прописываем куда попали
                Log.d("Home", "Guest");
                //finish();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void signIn(final String user, final String pwd) {
            users.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Checks if User exists in database

                    if (dataSnapshot.child(user).exists()) {
                        if (!user.isEmpty()) {
                            if (user.equals("Admin")) {
                                Log.d("Admin", user + " " + pwd);
                                User login = dataSnapshot.child(user).getValue(User.class);
                                if (login.getPassword().equals(pwd)) {
                                    Common.currentUser = login;
                                    Intent adminActivity = new Intent(MainActivity.this, AdminActivity.class);
                                    startActivity(adminActivity);
                                }
                            } else {

                                User login = dataSnapshot.child(user).getValue(User.class);
                                //Checks password for inserted user
                                if (login.getPassword().equals(pwd)) {
                                    Intent homeActivity = new Intent(MainActivity.this, HomeUser.class);
                                    Common.currentUser = login;

                                    startActivity(homeActivity);

                                    Log.d("Home", "Home");
                                    //finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Неправильный пароль", Toast.LENGTH_SHORT).show();
                                    Log.d("Homepwd", "Wrongpwd");
                                }
                            }
                        }
                    } else
                        Toast.makeText(MainActivity.this, "Пользователя не существует!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

    }



    private void showSignupDialog() {
        //Создаем диалоговое окно и назначаем заголовок сообщение
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Регистрация");
        alertDialog.setMessage("Пожалуйста, заполните всю информацию латинскими буквами");

        //
        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        //Назначаем лейауты, находи элементы экрана
        editNewUserName = sign_up_layout.findViewById(R.id.editNewUserName);
        editNewPassword = sign_up_layout.findViewById(R.id.editNewPassword);
        editNewEmail = sign_up_layout.findViewById(R.id.editNewEmail);


        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        //2 кнопки назад и вперед
        alertDialog.setNegativeButton("НАЗАД", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                //Закрываем диалоговое окно
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("ЗАРЕГИСТРИРОВАТЬСЯ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Создаем пользователя
                final User user = new User(editNewUserName.getText().toString(), editNewPassword.getText().toString(), editNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        //Если существует выводим сообщение
                        if(dataSnapshot.child(user.getUserName()).exists())
                            //Всплывающее сообщение, время - шорт
                                Toast.makeText(MainActivity.this, "Пользовательно с таким именем уже существует!", Toast.LENGTH_SHORT).show();
                        else{
                            //Если нету то регаем
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this, "Вы успешно зарегестрированы!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //Выключ
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
