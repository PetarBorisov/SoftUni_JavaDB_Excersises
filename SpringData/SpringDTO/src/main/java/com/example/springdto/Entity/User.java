package com.example.springdto.Entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity{

    private String email;
    private String password;
    private String fullName;
    private Boolean isAdmin;
    private Set<Game> listGames;

    public User(){

    }

    @Column
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "full_name")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<Game> getListGames() {
        return listGames;
    }

    public void setListGames(Set<Game> listGames) {
        this.listGames = listGames;
    }

    @Column(name = "is_admin")
    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
