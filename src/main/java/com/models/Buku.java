package com.models;

public class Buku {
    private String id_buku;
    private String nama_buku;
    private String penulis;
    private String penerbit;
    private Integer tahun_terbit;

    public void setIdBuku(String id_buku) {
        this.id_buku = id_buku;
    }

    public String getIdBuku() {
        return id_buku;
    }

    public void setNamaBuku(String nama_buku) {
        this.nama_buku = nama_buku;
    }

    public String getNamaBuku() {
        return nama_buku;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setTahunTerbit(Integer tahun_terbit) {
        this.tahun_terbit = tahun_terbit;
    }

    public Integer getTahunTerbit() {
        return tahun_terbit;
    }
}