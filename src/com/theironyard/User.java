package com.theironyard;

import java.util.ArrayList;

/**
 * Created by noellemachin on 2/27/16.
 */
public class User {
    int id;
    String userName;
    String password;
    ArrayList<Recipe> recipeList = new ArrayList<>();

    public User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return password;
    }

    public void setPassWord(String passWord) {
        this.password = passWord;
    }

    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }
}
