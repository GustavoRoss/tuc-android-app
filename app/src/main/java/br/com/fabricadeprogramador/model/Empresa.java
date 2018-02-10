package br.com.fabricadeprogramador.model;


import java.io.Serializable;

public class Empresa implements Serializable {

    private Integer id;
    private String cnpj;
    private String razaosocial;
    private String loja;
    private String endereco;
    private String telefone;
    private String email;

    private byte[] logo;

    public String getRazaosocial() {
        return razaosocial;
    }

    public void setRazaosocial(String razaosocial) {
        this.razaosocial = razaosocial;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getLoja() {
        return loja;
    }

    public void setLoja(String loja) {
        this.loja = loja;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }



    public Empresa(){

    }

    public Empresa(Integer id, String cnpj, String razaosocial, String loja, String endereco, String telefone, String email, byte[] logo) {
        this.id = id;
        this.cnpj = cnpj;
        this.razaosocial = razaosocial;
        this.loja = loja;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.logo = logo;
    }

}
