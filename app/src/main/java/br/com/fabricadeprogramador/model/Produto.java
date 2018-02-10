package br.com.fabricadeprogramador.model;

import java.io.Serializable;

/**
 * Created by Virmerson on 10/22/16.
 */


public class Produto implements Serializable {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    private int image;
    private String codigoBarra;
    private String descricao;
    private Double valor;

    public Produto() {
    }

    public Produto(Integer id, String codigoBarra, String descricao, Double valor, int image) {
        this.image = image;
        this.id = id;
        this.codigoBarra = codigoBarra;
        this.descricao = descricao;
        this.valor = valor;
    }

}
