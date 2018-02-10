package br.com.fabricadeprogramador.api;


import java.util.List;

import br.com.fabricadeprogramador.model.Empresa;
import br.com.fabricadeprogramador.model.Localizacao;
import br.com.fabricadeprogramador.model.Produto;
import br.com.fabricadeprogramador.model.Usuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Contem a declaração de todas as rotas para a API
 * Contem configuração do IP da API
 * Configura o Serializador Json
 * Configura os mapeamentos para a API (Rotas) e os tipos de Callback
 * Qualquer classe que desejar "falar" com a APi deverá chamar o "Roteador Restful" Retrofit
 */
public class TucClientApi {


    //public static final String BASE_URL = "http://192.168.43.129:8080";
    //public static final String BASE_URL = "http://192.168.25.17:8080";
    //public static final String BASE_URL = "http://192.168.0.163:8080";
    //public static final String BASE_URL = "http://192.168.0.166:8080";
    public static final String BASE_URL = "http://192.168.43.188:8080";



    public static TucServiceApi tucServiceApi;


    /**
     * Define todas as rotas para requisição para a API
     * As classes de serviço irão implementar os callbacks das requisições
     */
    public interface TucServiceApi {


        // ### Parte do TUC  Carregamento de produto e foto

        /**
         * Busca Imagem do Produto por Código de Barras
         **/
        @GET("/produtos/imagem/{codigobarra}")
        Call<ResponseBody> buscarImagemProduto(@Path("codigobarra") String codigo);


        /**
         * Busca Imagem da Empresa
         * **/
        @GET("/empresa/logo")
        Call<ResponseBody> buscarLogoEmpresa();

        /**
         * Busca Dados da Empresa
         * **/
        @GET("/empresa/dados")
        Call<Empresa> buscarDadosEmpresa();


        /**
         * Busca dados do Produto
         **/
        @GET("/produtos/{codigobarra}")
        Call<Produto> buscarProduto(@Path("codigobarra") String codigoBarra);

        /**
         * Busca dados de Localizacao
         **/
        @GET("/localizacoes")
        Call<List<Localizacao>> buscarLocalizacao();


        // ### Cadastro  de usuario e RECUPERAR SENNHA

        @FormUrlEncoded
        @POST("/login/public/autenticar")
        Call<Usuario> login(@Field("email") String email, @Field("senha") String senha);

        @FormUrlEncoded
        @POST("/usuario/public/recuperarsenha")
        Call<Response> recuperarSenha(@Field("email") String email);

        @FormUrlEncoded
        @POST("/usuario/public/verificarcodigo")
        Call<Response> verificarCodigo(@Field("email") String email, @Field("codigo") Integer codigoVerificacao);

        @FormUrlEncoded
        @POST("/usuario/private/alterarsenha")
        Call<Response> alterarSenha(@Field("email") String email, @Field("senha") String password, @Field("codigo") Integer code);

        @POST("/onlinemeeting/private/save")
        Call<Response> salvarUsuario(@Body Usuario usuario);

    }


    /**
     * Instanciando  objeto de Rotas implementado pelo Retrofit
     *
     * @return
     */
    public static TucServiceApi getTucApi() {
        if (tucServiceApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            //Instancia uma implementação do TucServiceApi
            return retrofit.create(TucServiceApi.class);

        }
        return tucServiceApi;
    }


}