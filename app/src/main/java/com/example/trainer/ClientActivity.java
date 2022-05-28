package com.example.trainer;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.trainer.AllerDialogs.UploadImageDialog;
import com.example.trainer.db.Trainer;
import com.example.trainer.db.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class ClientActivity extends AppCompatActivity{
    private DatabaseReference users;
    private TextView username, birthday, gender, email, phone;
    private ImageView avatar;
    private User user;
    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        getSupportActionBar().setTitle("Профиль спортсмена");
        initialization();
        getDataBase();
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            user_id = intent.getStringExtra("id");
        }
    }
    //Инициализация компонентов
    private void initialization() {
        //Поиск элементов по id
        username = (TextView) findViewById(R.id.ProfileUserName);
        birthday = (TextView) findViewById(R.id.ProfileBirthday);
        gender = (TextView) findViewById(R.id.ProfileGender);
        email = (TextView) findViewById(R.id.ProfileEmail);
        phone = (TextView) findViewById(R.id.ProfilePhone);
        avatar = (ImageView) findViewById(R.id.ProfileAvatar);
        users = FirebaseDatabase.getInstance().getReference(User.USER_KEY);
    }

    //Получение данных из БД
    private void getDataBase() {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.child(user_id).getValue(User.class);
                if (!user.image.isEmpty()) Picasso.get().load(user.image).into(avatar);
                username.setText(user.last + " " + user.first + " " + user.second);
                birthday.setText("дата рождения: " + user.data_birthday);
                gender.setText("пол: " + user.gender);
                email.setText(user.email);
                phone.setText(user.phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openPhone(View view) {
        String dial = "tel:" + phone.getText();
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }
}