package com.example.trainer;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trainer.db.LikeSection;
import com.example.trainer.db.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SectionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView section_name, section_day, section_time, trainer_name, trainer_phone, trainer_email;
    private String section_id;
    private ImageView avatar_trainer;
    private DatabaseReference trainer;
    private DatabaseReference like_section;
    private String trainer_id;
    private User user;
    private Button back;
    private int FLAG_CHECK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        getSupportActionBar().setTitle("Секция");
        FLAG_CHECK = 0;
        initialization();
        getIntentData();
        getDataBase();
    }

    //Инициализация компонентов
    private void initialization() {
        section_name = findViewById(R.id.Section_name);
        section_day = findViewById(R.id.Section_day);
        section_time = findViewById(R.id.Section_time);
        trainer_name = findViewById(R.id.Section_nameTrainer);
        trainer_phone = findViewById(R.id.Section_phone);
        trainer_email = findViewById(R.id.Section_email);
        avatar_trainer = findViewById(R.id.Section_imgProfile);
        back = findViewById(R.id.Section_back);
        back.setOnClickListener(this);
        //Инициализация элементов firebase
        trainer = FirebaseDatabase.getInstance().getReference("TRAINER");
        like_section = FirebaseDatabase.getInstance().getReference(LikeSection.KEY);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            section_id = intent.getStringExtra("id");
            section_name.setText(intent.getStringExtra("name"));
            section_time.setText(intent.getStringExtra("time"));
            section_day.setText(intent.getStringExtra("day"));
            trainer_id = intent.getStringExtra("trainer");
        }
    }


    //Получение данных из БД
    private void getDataBase() {
        trainer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.child(trainer_id).getValue(User.class);
                if (!user.image.isEmpty()) Picasso.get().load(user.image).into(avatar_trainer);
                trainer_name.setText(user.last + " " + user.first + " " + user.second);
                trainer_email.setText(user.email);
                trainer_phone.setText(user.phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Section_back:
                finish();
                break;
        }
    }

    public void like(View view) {
        if (FLAG_CHECK < 1) {
            LikeSection ls = new LikeSection(UserActivity.userID, UserActivity.userID, section_id);
            like_section.child(UserActivity.userID).setValue(ls);
            FLAG_CHECK++;
            Toast toast = Toast.makeText(this, "Запись прошла успешно", LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this, "Вы уже записались", LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    public void openPhone(View view) {
        String dial = "tel:" + trainer_phone.getText();
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }

    public void sectionOpenAddress(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=Южное ш., 2Г, Нижний Новгород, Россия"));
        startActivity(intent);
    }

    public void dislike(View view) {
            LikeSection ls = new LikeSection(UserActivity.userID, UserActivity.userID, "");
            like_section.child(UserActivity.userID).setValue(ls);

            Toast toast = Toast.makeText(this, "Вы отписались от секции", LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
    }
}