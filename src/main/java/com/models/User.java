package com.models;

public class User {
    private String id_user;
    private String username;
    private String password;
    private String nama_lengkap;
    private String alamat;
    private String kode;

    public void setIdUser(String id_user) {
        this.id_user = id_user;
    }

    public String getIdUser() {
        return id_user;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setNamaLengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getNamaLengkap() {
        return nama_lengkap;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }
}