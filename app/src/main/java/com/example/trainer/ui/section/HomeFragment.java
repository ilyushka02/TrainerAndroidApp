package com.example.trainer.ui.section;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.trainer.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trainer.SectionActivity;
import com.example.trainer.databinding.FragmentSectionBinding;
import com.example.trainer.db.Section;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    View root;
    private DatabaseReference section;
    private ListView listViewSection;
    private ArrayAdapter<String> adapter;
    private List<String> listSection;
    private List<Section> listTemp;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_section, container, false);
        initialization();
        getDataBase();
        progressBar.setVisibility(View.VISIBLE);
        setOnClickItem();
        return root;
    }

    //Инициализация компонентов
    private void initialization() {
        //Поиск элементов по id
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        listViewSection = (ListView) root.findViewById(R.id.sectionList);
        listSection = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, listSection);
        listViewSection.setAdapter(adapter);
        section = FirebaseDatabase.getInstance().getReference(Section.KEY);
    }

    //Получение данных из БД секции
    private void getDataBase() {
        section.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listSection.size() > 0)
                    listSection.clear();
                if (listTemp.size() > 0)
                    listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Section s = ds.getValue(Section.class);
                    assert s != null;
                    listSection.add(s.name);
                    listTemp.add(s);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void setOnClickItem() {
        listViewSection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Section section = listTemp.get(i);
                openSectionActivity(section);
            }
        });
    }

    private void openSectionActivity(Section s) {
        Intent intent = new Intent(this.getActivity(), SectionActivity.class);
        intent.putExtra("id", s.id);
        intent.putExtra("name", s.name);
        intent.putExtra("day", s.day);
        intent.putExtra("time", s.time);
        intent.putExtra("trainer", s.trainer);
        startActivity(intent);
    }
}