package br.com.fabricadeprogramador.repository;


import java.util.ArrayList;
import java.util.List;

import br.com.fabricadeprogramador.model.ItemCesta;

public class CestaRepository {


    private static List<ItemCesta> cestaList = new ArrayList<>();

    public static void adicionarCesta(ItemCesta itemCesta) {
        cestaList.add(itemCesta);
    }

    public static List<ItemCesta> getCestaList() {
        return cestaList;
    }
}
