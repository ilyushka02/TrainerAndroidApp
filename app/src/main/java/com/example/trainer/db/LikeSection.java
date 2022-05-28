package com.example.trainer.db;

public class LikeSection {
    public static String KEY = "LIKE_SECTION";
    public String id, id_user, id_section;

    public LikeSection() {
    }

    public LikeSection(String str_id, String str_id_user, String str_section) {
        this.id = str_id;
        this.id_user = str_id_user;
        this.id_section = str_section;
    }

}
