package com.example.trainer.ui.settings;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trainer.R;
import com.example.trainer.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private View root;
    private Button exit;
    private Switch aSwitch;
    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        final TextView textView = binding.textSettings;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        initialization();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        return root;
    }

    private void initialization() {
        exit = (Button) root.findViewById(R.id.exit);
        exit.setOnClickListener(this);
        aSwitch = (Switch) root.findViewById(R.id.thems);
        aSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit:
                System.exit(0);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b == true){
            Toast toast = Toast.makeText(this.getActivity(), "Выбран", LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this.getActivity(), "Не выбран", LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
    }

}
