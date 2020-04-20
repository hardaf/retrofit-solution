package id.putraprima.retrofit.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Error {
    @SerializedName("name")
    @Expose
    private List<String> name = null;

    @SerializedName("email")
    @Expose
    private List<String> email = null;

    @SerializedName("password")
    @Expose
    private List<String> password = null;

    @SerializedName("nama_resep")
    @Expose
    private List<String> resep = null;

    @SerializedName("deskripsi")
    @Expose
    private List<String> deskripsi = null;

    @SerializedName("bahan")
    @Expose
    private List<String> bahan = null;

    @SerializedName("langkah_pembuatan")
    @Expose
    private List<String> cara = null;

    @SerializedName("foto")
    @Expose
    private List<String> foto = null;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getEmail() {
            return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    public List<String> getResep() {
        return resep;
    }

    public void setResep(List<String> resep) {
        this.resep = resep;
    }

    public List<String> getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(List<String> deskripsi) {
        this.deskripsi = deskripsi;
    }

    public List<String> getBahan() {
        return bahan;
    }

    public void setBahan(List<String> bahan) {
        this.bahan = bahan;
    }

    public List<String> getCara() {
        return cara;
    }

    public void setCara(List<String> cara) {
        this.cara = cara;
    }

    public List<String> getFoto() {
        return foto;
    }

    public void setFoto(List<String> foto) {
        this.foto = foto;
    }
}
