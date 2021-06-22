package com.example.doan2;

public class KhachHang {
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String diaChi;
    private String soDienThoai;
    private String maLoai;
    private String hinhDaiDien;
    private String ngayThamGia;
    private String viDoHienTai;
    private String kinhDoHienTai;
    private String soTien;

    public KhachHang() {
    }

    public KhachHang(String tenDangNhap, String matKhau, String hoTen, String diaChi, String soDienThoai, String maLoai, String hinhDaiDien, String ngayThamGia, String viDoHienTai, String kinhDoHienTai, String soTien) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.maLoai = maLoai;
        this.hinhDaiDien = hinhDaiDien;
        this.ngayThamGia = ngayThamGia;
        this.viDoHienTai = viDoHienTai;
        this.kinhDoHienTai = kinhDoHienTai;
        this.soTien = soTien;
    }

    public String getSoTien() {
        return soTien;
    }

    public void setSoTien(String soTien) {
        this.soTien = soTien;
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
