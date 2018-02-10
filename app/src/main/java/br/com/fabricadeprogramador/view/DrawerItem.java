package br.com.fabricadeprogramador.view;

import java.io.Serializable;

import lombok.Data;


@Data
public class DrawerItem implements Serializable {

    private int imagem;
    private String itemNome;

    public DrawerItem(int imagem, String itemNome){
        this.imagem = imagem;
        this.itemNome = itemNome;
    }


}
