package br.com.fabricadeprogramador.model;

import java.io.Serializable;
import java.util.Locale;

import br.com.fabricadeprogramador.model.Produto;
import lombok.Data;


@Data
public class ItemCesta implements Serializable {

    private Produto produto;
    private int quantidade;

    public ItemCesta(){
        produto = new Produto();
    }
    public ItemCesta(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }


    @Override
    public String toString() {
        return getQuantidade() + "   " + getProduto().getDescricao() + "   R$: " + String.format(Locale.US, "%.2f", getProduto().getValor());
    }
}
