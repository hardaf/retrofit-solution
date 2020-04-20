package id.putraprima.retrofit.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("nama_resep")
    @Expose
    private String namaResep;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("bahan")
    @Expose
    private String bahan;
    @SerializedName("langkah_pembuatan")
    @Expose
    private String langkahPembuatan;
    @SerializedName("foto")
    @Expose
    private String foto;

    public Recipe(int id, String namaResep, String deskripsi, String bahan, String langkahPembuatan, String foto) {
        this.id = id;
        this.namaResep = namaResep;
        this.deskripsi = deskripsi;
        this.bahan = bahan;
        this.langkahPembuatan = langkahPembuatan;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaResep() {
        return namaResep;
    }

    public void setNamaResep(String namaResep) {
        this.namaResep = namaResep;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public String getLangkahPembuatan() {
        return langkahPembuatan;
    }

    public void setLangkahPembuatan(String langkahPembuatan) {
        this.langkahPembuatan = langkahPembuatan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

}

