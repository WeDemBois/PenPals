package com.github.wedemboys.penpals;

/**
 * Created by jakeo on 10/2/2017.
 */

public class User {

    public User(String username, String password, String email, String firstname, String lastname, String country, String lang, String gender, String interests) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.country = country;
        this.lang = lang;
        this.gender = gender;
        this.interests = interests;
    }

    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String country;
    private String lang;
    private String gender;
    private String interests;

    public User(){}

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCountry() {
        return country;
    }

    public String getLang() {
        return lang;
    }

    public String getGender() {
        return gender;
    }

    public String getInterests() {
        return interests;
    }

    public String getUsername() {
        return username;
    }

}
