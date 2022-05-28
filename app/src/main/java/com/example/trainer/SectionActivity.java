package com.example.trainer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trainer.db.LikeSection;
import com.example.trainer.db.Section;
import com.example.trainer.db.Trainer;
import com.example.trainer.db.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SectionActivity extends AppCompatActivity {
    private DatabaseReference likeSection;
    private DatabaseReference users;
    private ListView listViewClient;
    private ArrayAdapter<String> adapter;
    private List<String> listClient;
    private List<User> listTemp;
    private ProgressBar progressBar;
    LikeSection ls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        getSupportActionBar().setTitle("Секция");
        initialization();
        getDataBaseSections();
        setOnClickItem();
        progressBar.setVisibility(View.VISIBLE);
    }

    //Инициализация компонентов
    private void initialization() {
        progressBar = (ProgressBar) findViewById(R.id.progressBarSection);
        listViewClient = (ListView) findViewById(R.id.clientList);
        listClient = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listClient);
        listViewClient.setAdapter(adapter);
        likeSection = FirebaseDatabase.getInstance().getReference(LikeSection.KEY);
        users = FirebaseDatabase.getInstance().getReference("USER");
    }

    private void getDataBaseSections() {
        likeSection.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ls = ds.getValue(LikeSection.class);
                    assert ls != null;
                    if (ls.id_section.equals(UserActivity.id_s)){
                        getDataBaseClient();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getDataBaseClient() {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(ls.id_user).getValue(User.class);
                listClient.add(user.last + " " + user.first);
                listTemp.add(user);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setOnClickItem() {
        listViewClient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = listTemp.get(i);
                openClientActivity(user);
            }
        });
    }

    private void openClientActivity(User u) {
        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra("id", u.id);
        startActivity(intent);
    }
}