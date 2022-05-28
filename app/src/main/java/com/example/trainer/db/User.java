package com.example.trainer.db;

public class User {
    public static String USER_KEY = "USER";
    public String id, last, first, second, email, phone, gender, data_birthday, image;

    public User() {
    }

    public User(String id, String last_Name, String first_Name, String second_Name, String email, String phone, String gender, String data_birthday, String imageURI) {
        this.id = id;
        this.last = last_Name;
        this.first = first_Name;
        this.second = second_Name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.data_birthday = data_birthday;
        this.image = imageURI;
    }

}
