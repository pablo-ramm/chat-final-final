package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

/*
    representation of the USER table that exists already
    in the autonomous database
 */

@Entity
@Table(name = "USER")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "REGISTERED_TS")
    private OffsetDateTime registeredTimestamp;

    @Column(name = "ACTIVE")
    private boolean active;

    public UserModel() {}

    public UserModel(int id, String username, String email, OffsetDateTime registeredTimestamp, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.registeredTimestamp = registeredTimestamp;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OffsetDateTime getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    public void setRegisteredTimestamp(OffsetDateTime registeredTimestamp) {
        this.registeredTimestamp = registeredTimestamp;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registeredTimestamp=" + registeredTimestamp +
                ", active=" + active +
                '}';
    }
}
