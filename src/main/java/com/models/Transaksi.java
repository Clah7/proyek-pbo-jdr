package com.models;

import java.util.Date;

public class Transaksi {
    private String id_transaksi;
    private String username;
    private String id_buku;
    private String status;
    private Date tanggal_peminjaman;
    private Date tanggal_pengembalian;

    public String getIdTransaksi() {
        return id_transaksi;
    }

    public void setIdTransaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdBuku() {
        return id_buku;
    }

    public void setIdBuku(String id_buku) {
        this.id_buku = id_buku;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTanggalPeminjaman() {
        return tanggal_peminjaman;
    }

    public void setTanggalPeminjaman(Date tanggal_peminjaman) {
        this.tanggal_peminjaman = tanggal_peminjaman;
    }

    public Date getTanggalPengembalian() {
        return tanggal_pengembalian;
    }

    public void setTanggalPengembalian(Date tanggal_pengembalian) {
        this.tanggal_pengembalian = tanggal_pengembalian;
    }
}
