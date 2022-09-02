package com.example.infoapp;

public class UserInformation {
    String mail;
    String adSoyad;
    String tip;
    String sifre;
    String universite;
    String bolum;
    String uid;
    String kullaniciAdi;

    public UserInformation( String adSoyad,String mail, String sifre, String tip,String universite,String bolum,String uid, String kullaniciAdi) {
        this.mail = mail;
        this.adSoyad = adSoyad;
        this.tip = tip;
        this.sifre = sifre;
        this.universite = universite;
        this.bolum = bolum;
        this.uid= uid;
        this.kullaniciAdi=kullaniciAdi;
    }

    public UserInformation() {}

    //setters
    public void setUniversite(String universite) {
        this.universite = universite;
    }
    public void setBolum(String bolum) {
        this.bolum = bolum;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }
    public void setTip(String tip) {
        this.tip = tip;
    }
    public void setSifre(String sifre) {
        this.sifre = sifre;
    }
    public void setUid(String uid){this.uid=uid;}
    public void setKullaniciAdi(String kullaniciAdi){this.kullaniciAdi=kullaniciAdi;}

    //getters
    public String getUniversite() {
        return universite;
    }
    public String getBolum() {
        return bolum;
    }
    public String getMail() {
        return mail;
    }
    public String getAdSoyad() {
        return adSoyad;
    }
    public String getSifre() {
        return sifre;
    }
    public String getTip() {
        return tip;
    }
    public String getUid(){return uid;}
    public String getkullaniciAdi(){return kullaniciAdi;}
}
