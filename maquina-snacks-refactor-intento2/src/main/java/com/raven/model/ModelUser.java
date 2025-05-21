package com.raven.model;

public class ModelUser {

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        // Corregido: userID en lugar de id
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public ModelUser(int userID, String userName, String email, String password, String verifyCode) {
        // Utilizamos el ID proporcionado en lugar de autoincrementar
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.verifyCode = verifyCode;
    }

    public ModelUser(int userID, String userName, String email, String password) {
        // Utilizamos el ID proporcionado en lugar de autoincrementar
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public ModelUser() {
        // Constructor vacío necesario para Gson
    }

    private int userID;
    private String userName;
    private String email;
    private String password;
    private String verifyCode;
}