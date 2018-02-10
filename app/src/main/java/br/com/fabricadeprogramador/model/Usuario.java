package br.com.fabricadeprogramador.model;


import lombok.Data;

@Data
public class Usuario {
    private Integer id;
    private String email;
    private String senha;
}
