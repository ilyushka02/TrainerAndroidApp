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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.trainer.AllerDialogs.UploadImageDialog;
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

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference users;
    private StorageReference storageRef;
    private Calendar date = Calendar.getInstance();
    private EditText username_f, username_l, username_s, birthday, phone;
    private Button takeImg, updateUser;
    private Spinner gender;
    private ImageView update_img;
    private User user;
    private Uri uploadPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getSupportActionBar().setTitle("Редактирование профиля");
        initialization();
        getDataBase();
    }

    //Инициализация компонентов
    private void initialization() {
        //Поиск элементов по id
        username_f = findViewById(R.id.Update_firststName);
        username_l = findViewById(R.id.Update_lastName);
        username_s = findViewById(R.id.Update_secondName);
        birthday = findViewById(R.id.Update_Birthday);
        gender = findViewById(R.id.Update_Gender);
        phone = findViewById(R.id.Update_Phone);
        update_img = findViewById(R.id.Update_imgProfile);
        updateUser = findViewById(R.id.Update_saveUser);
        takeImg = findViewById(R.id.Update_takeImg);
        //Создание масок
        Function.createPatternForPhoneNumber(phone);
        Function.createPatternForDateBirthday(birthday);
        //Заполнение выпадаюдщего списка
        Function.createArrayForSpinner(gender, this);
        //Установка слушателей нажатия
        birthday.setOnClickListener(this);
        updateUser.setOnClickListener(this);
        takeImg.setOnClickListener(this);
        //Инициализация элементов firebase
        storageRef = FirebaseStorage.getInstance().getReference(UserActivity.userID);
        users = FirebaseDatabase.getInstance().getReference(User.USER_KEY);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Update_Birthday:
                setDate(view);
                break;
            case R.id.Update_takeImg:
                openGallery(view);
                break;
            case R.id.Update_saveUser:
                saveUser();
                break;
        }
    }

    public void back(View view) {
        finish();
    }

    //Получение даннхы из БД
    private void getDataBase() {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.child(UserActivity.userID).getValue(User.class);
                if (!user.image.isEmpty()) Picasso.get().load(user.image).into(update_img);
                username_f.setText(user.first);
                username_l.setText(user.last);
                username_s.setText(user.second);
                birthday.setText(user.data_birthday);
                phone.setText(user.phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //открытие галлереи
    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    //Обработка получения изображения
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (data != null) && (data.getData() != null)) {
            if (resultCode == RESULT_OK) {
                update_img.setImageURI(data.getData());
                uploadImg();
            }
        }
    }

    //Выгрузка изображения на сервер БД
    private void uploadImg() {
        Bitmap bitmap = ((BitmapDrawable) update_img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        StorageReference usRef = storageRef.child("image" + System.currentTimeMillis());
        UploadTask up = usRef.putBytes(bytes);
        Task<Uri> uriTask = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return usRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadPath = task.getResult();
            }
        });
    }


    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(UpdateActivity.this, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    //Установка выбранного значения в текстовое поле
    private void setInitialDate() {
        birthday.setText(DateUtils.formatDateTime(this,
                date.getTimeInMillis(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    //Выгрузка данных пользователя в БД и их обновление
    private void saveUser() {
        String last_name = "", first_name = "", second_name = "", email = user.email, str_phone = "", str_gender = "", str_birthday = "", imageURI = "";
        //Если данные изменились, то перезаписываем, иначе оставляем
        if (!user.last.equals(username_l.getText().toString().trim()))
            last_name = username_l.getText().toString().trim();
        else last_name = user.last;
        if (!user.first.equals(username_f.getText().toString().trim()))
            first_name = username_f.getText().toString().trim();
        else first_name = user.first;
        if (!user.second.equals(username_s.getText().toString().trim()))
            second_name = username_s.getText().toString().trim();
        else second_name = user.second;
        if (!user.phone.equals(phone.getText().toString().trim()))
            str_phone = phone.getText().toString().trim();
        else str_phone = user.phone;
        if (!user.gender.equals(gender.getSelectedItem().toString().trim()))
            str_gender = gender.getSelectedItem().toString().trim();
        else str_gender = user.gender;
        if (!user.data_birthday.equals(birthday.getText().toString().trim()))
            str_birthday = birthday.getText().toString().trim();
        else str_birthday = user.data_birthday;
        if (uploadPath != null) {
            if (!user.image.equals(uploadPath.toString()))
                imageURI = uploadPath.toString();
        } else {
            createDialog();
            imageURI = user.image;
        }

        User user = new User(UserActivity.userID, last_name, first_name, second_name, email, str_phone, str_gender, str_birthday, imageURI);
        users.child(UserActivity.userID).setValue(user);

        Toast toast = Toast.makeText(this, "Success", LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //Создания alert-диалога с сообщением ошибки загрузки
    public void createDialog() {
        FragmentManager manager = getSupportFragmentManager();
        getSupportFragmentManager();
        UploadImageDialog myDialogFragment = new UploadImageDialog();
        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }
}