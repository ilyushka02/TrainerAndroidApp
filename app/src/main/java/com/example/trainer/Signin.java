package com.example.trainer;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainer.AllerDialogs.ErrorConnectorInNetwork;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends AppCompatActivity implements View.OnClickListener {
    private Button btn_back;
    private EditText field_login, filed_password;
    private FirebaseAuth mAuth;
    private String login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();
        initialization();
    }

    //Инициализация компонентов
    private void initialization() {
        //Поиск элементов по id
        btn_back = findViewById(R.id.signin_btn);
        field_login = findViewById(R.id.textFieldLogin);
        filed_password = findViewById(R.id.textFieldPassword);
        //Инициализация FirebaseAuthentication
        mAuth = FirebaseAuth.getInstance();
        //Установка слушателей нажатия
        btn_back.setOnClickListener(this);
        //Проверка состояния сети
        isNetworkAvailable(this);
    }

    //Обработчик нажатий
    @Override
    public void onClick(View v) {
        if (isNetworkAvailable(this)) {
            switch (v.getId()) {
                case R.id.signin_btn:
                    authorization();
                    break;
            }
        } else {
            createDialog();
        }
    }

    //Проверка состояния сети
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //(WiFi, 3G, LTE, etc.)
        NetworkInfo[] info = connectivity.getAllNetworkInfo();

        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    Function.FLAG_CONNECTING_NETWORK = true;
                    return true;
                } else {
                    Function.FLAG_CONNECTING_NETWORK = false;
                    createDialog();
                    return false;
                }
            }
        }
        Function.FLAG_CONNECTING_NETWORK = false;
        return false;
    }

    //Создания alert-диалога с сообщением ошибки подключения
    public void createDialog() {
        FragmentManager manager = getSupportFragmentManager();
        getSupportFragmentManager();
        ErrorConnectorInNetwork myDialogFragment = new ErrorConnectorInNetwork();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }

    //Авторизация
    private void authorization() {
        login = field_login.getText().toString().trim();
        password = filed_password.getText().toString().trim();
        if (login.isEmpty() || password.isEmpty()) {
            Snackbar.make(findViewById(R.id.body_signin_page), "Логин и пароль не могут быть пустыми!", BaseTransientBottomBar.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(login, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                openUserActivity(Signin.this);
                                finish();
                            } else {
                                Toast toast = Toast.makeText(Signin.this, "Не правильный логин или пароль!", LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                    });
        }
    }

    //Переход  к пользовательской Activity
    private void openUserActivity(Context context) {
        Intent intent = new Intent(context, UserActivity.class);
        context.startActivity(intent);
    }
}