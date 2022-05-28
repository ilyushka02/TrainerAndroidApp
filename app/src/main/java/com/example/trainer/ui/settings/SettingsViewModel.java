package com.example.trainer.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Разработчик: Насурллаев Илья" +
                "\nПо всем вопросам обращаться на почту: ilyaevgenievi4@mail.ru\n" +
                "Куратор проекта: Щербатюк М.С");
    }

    public LiveData<String> getText() {
        return mText;
    }
}