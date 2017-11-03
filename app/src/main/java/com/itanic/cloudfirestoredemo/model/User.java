package com.itanic.cloudfirestoredemo.model;

/**
 * Created by Pratik on 27-Oct-17.
 */

public class User {

    private String name;
    private String language;
    Hobbies hobbies;

    public Hobbies getHobbies() {
        return hobbies;
    }

    public void setHobbies(Hobbies hobbies) {
        this.hobbies = hobbies;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
