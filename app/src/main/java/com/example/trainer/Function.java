package com.example.trainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class Function extends AppCompatActivity {
    //Флаг проверки сосотояния сети;
    public static boolean FLAG_CONNECTING_NETWORK;

    ///////////////////////////////////////////
    //Функции для открытия новых активностей//
    ///////////////////////////////////////////

    ///////////////////////////////////
    //Функции для создания паттернов//
    /////////////////////////////////
    public static void createPatternForDateBirthday(EditText date) {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("__.__.____");
        FormatWatcher formatWatcher = new MaskFormatWatcher(
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(date);
    }

    public static void createPatternForSMSCode(EditText sms_code) {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("___-___");
        FormatWatcher formatWatcher = new MaskFormatWatcher(
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(sms_code);
    }

    public static void createPatternForPhoneNumber(EditText number) {
        FormatWatcher formatWatcher = new MaskFormatWatcher(
                MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        );
        formatWatcher.installOn(number);
    }

    //////////////////////////////////////////////
    //Функции для заполнения выпадающего списка//
    ////////////////////////////////////////////
    public static void createArrayForSpinner(Spinner spinner, Activity activity) {
        String[] gender_array = {"не выбрано", "мужской", "женский"};
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, gender_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
