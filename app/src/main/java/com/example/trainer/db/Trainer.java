package com.example.trainer.db;

public class Trainer {
    public static String USER_KEY = "TRAINER";
    public String id, last, first, second, id_section,email, phone, gender, data_birthday, image;

    public Trainer() {
    }

    public Trainer(String id, String last_Name, String first_Name, String second_Name,String strId_section, String email, String phone, String gender, String data_birthday, String imageURI) {
        this.id = id;
        this.last = last_Name;
        this.first = first_Name;
        this.second = second_Name;
        this.id_section = strId_section;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.data_birthday = data_birthday;
        this.image = imageURI;
    }

}
