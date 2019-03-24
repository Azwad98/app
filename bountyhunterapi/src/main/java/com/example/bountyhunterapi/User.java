package com.example.bountyhunterapi;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {

    @SerializedName("id")
    private int id;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("active")
    private boolean active;
    @SerializedName("verified")
    private boolean verified;
    @SerializedName("created_at")
    private Date created_at;
    @SerializedName("updated_at")
    private Date updated_at;

    public User(int id, String firstName, String lastName, String username, String password, String email, boolean active, boolean verified, Date created_at, Date updated_at) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.active = active;
        this.verified = verified;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isVerified() {
        return this.verified;
    }

    public Date getCreated_at() {
        return this.created_at;
    }

    public Date getUpdated_at() {
        return this.updated_at;
    }

}
