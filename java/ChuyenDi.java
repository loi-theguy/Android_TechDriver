package com.example.doan2;

public class ChuyenDi {
    public static final String HOAN_THANH="1";
    public static final String CHUA_HOAN_THANH ="0";
    public static final String NOT_APPLICABLE="NA";
    private String diemBatDau, viDoDiemBatDau, kinhDoDiemBatDau, diemDen, viDoDiemDen,
    kinhDoDiemDen, soKm, giaTien, thoiGianBatDau, thoiGianKetThuc, maKhachHang, maDoiTac, trangThai;

    public ChuyenDi() {
    }

    public ChuyenDi(String diemBatDau, String viDoDiemBatDau, String kinhDoDiemBatDau, String diemDen, String viDoDiemDen, String kinhDoDiemDen, String soKm, String giaTien, String thoiGianBatDau, String thoiGianKetThuc, String maKhachHang, String maDoiTac, String trangThai) {
        this.diemBatDau = diemBatDau;
        this.viDoDiemBatDau = viDoDiemBatDau;
        this.kinhDoDiemBatDau = kinhDoDiemBatDau;
        this.diemDen = diemDen;
        this.viDoDiemDen = viDoDiemDen;
        this.kinhDoDiemDen = kinhDoDiemDen;
        this.soKm = soKm;
        this.giaTien = giaTien;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.maKhachHang = maKhachHang;
        this.maDoiTac = maDoiTac;
        this.trangThai = trangThai;
    }

    public String getDiemBatDau() {
        return diemBatDau;
    }

    public void setDiemBatDau(String diemBatDau) {
        this.diemBatDau = diemBatDau;
    }

    public String getViDoDiemBatDau() {
        return viDoDiemBatDau;
    }

    public void setViDoDiemBatDau(String viDoDiemBatDau) {
        this.viDoDiemBatDau = viDoDiemBatDau;
    }

    public String getKinhDoDiemBatDau() {
        return kinhDoDiemBatDau;
    }

    public void setKinhDoDiemBatDau(String kinhDoDiemBatDau) {
        this.kinhDoDiemBatDau = kinhDoDiemBatDau;
    }

    public String getDiemDen() {
        return diemDen;
    }

    public void setDiemDen(String diemDen) {
        this.diemDen = diemDen;
    }

    public String getViDoDiemDen() {
        return viDoDiemDen;
    }

    public void setViDoDiemDen(String viDoDiemDen) {
        this.viDoDiemDen = viDoDiemDen;
    }

    public String getKinhDoDiemDen() {
        return kinhDoDiemDen;
    }

    public void setKinhDoDiemDen(String kinhDoDiemDen) {
        this.kinhDoDiemDen = kinhDoDiemDen;
    }

    public String getSoKm() {
        return soKm;
    }

    public void setSoKm(String soKm) {
        this.soKm = soKm;
    }

    public String getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(String giaTien) {
        this.giaTien = giaTien;
    }

    public String getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(String thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public String getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(String thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaDoiTac() {
        return maDoiTac;
    }

    public void setMaDoiTac(String maDoiTac) {
        this.maDoiTac = maDoiTac;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
