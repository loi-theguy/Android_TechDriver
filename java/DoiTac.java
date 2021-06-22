package com.example.doan2;

public class DoiTac {
    public static final String BAN="1";
    public static final String KHONG_BAN="0";
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String diaChi;
    private String soDienThoai;
    private String ngaySinh;
    private String CMND;
    private String maLoai;
    private String hinhDaiDien;
    private String bienSoXe;
    private String dangBan;
    private String ngayThamGia;
    private String viDoHienTai;
    private String kinhDoHienTai;

    public DoiTac() {
    }

    public DoiTac(String tenDangNhap, String matKhau, String hoTen, String diaChi, String soDienThoai, String ngaySinh, String CMND, String maLoai, String hinhDaiDien, String bienSoXe, String dangBan, String ngayThamGia, String viDoHienTai, String kinhDoHienTai) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.ngaySinh = ngaySinh;
        this.CMND = CMND;
        this.maLoai = maLoai;
        this.hinhDaiDien = hinhDaiDien;
        this.bienSoXe = bienSoXe;
        this.dangBan = dangBan;
        this.ngayThamGia = ngayThamGia;
        this.viDoHienTai = viDoHienTai;
        this.kinhDoHienTai = kinhDoHienTai;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getCMND() {
        return CMND;
    }

    public void setCMND(String CMND) {
        this.CMND = CMND;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getHinhDaiDien() {
        return hinhDaiDien;
    }

    public void setHinhDaiDien(String hinhDaiDien) {
        this.hinhDaiDien = hinhDaiDien;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public String getDangBan() {
        return dangBan;
    }

    public void setDangBan(String dangBan) {
        this.dangBan = dangBan;
    }

    public String getNgayThamGia() {
        return ngayThamGia;
    }

    public void setNgayThamGia(String ngayThamGia) {
        this.ngayThamGia = ngayThamGia;
    }

    public String getViDoHienTai() {
        return viDoHienTai;
    }

    public void setViDoHienTai(String viDoHienTai) {
        this.viDoHienTai = viDoHienTai;
    }

    public String getKinhDoHienTai() {
        return kinhDoHienTai;
    }

    public void setKinhDoHienTai(String kinhDoHienTai) {
        this.kinhDoHienTai = kinhDoHienTai;
    }
}
