package br.com.fabricadeprogramador.activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import br.com.fabricadeprogramador.adapter.DrawerAdapterList;
import br.com.fabricadeprogramador.api.TucClientApi;
import br.com.fabricadeprogramador.model.Empresa;
import br.com.fabricadeprogramador.model.Produto;
import br.com.fabricadeprogramador.view.DrawerItem;
import br.com.fabricadeprogramador.model.ItemCesta;
import br.com.fabricadeprogramador.repository.CestaRepository;
import br.com.fabricadeprogramador.tuc.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_BARCODE = 10;
    @Bind(R.id.et_cb)
    EditText codigoBarra;
    @Bind(R.id.tvQuantidade)
    TextView quantidade;
    @Bind(R.id.tvDescricao)
    TextView descricao;
    @Bind(R.id.tvValor)
    TextView valor;
    @Bind(R.id.lvDrawer)
    ListView drawer;
    @Bind(R.id.tb_am_toolbar)
    Toolbar toolbar;
    @Bind(R.id.lyDrawer)
    DrawerLayout drawerLayout;
    @Bind(R.id.ly_am_itemAtr)
    LinearLayout itemAtr;
    @Bind(R.id.imgProduto)
    ImageView imgproduto;
    @Bind(R.id.btn_less)
    ImageButton btn_less;
    @Bind(R.id.btn_more)
    ImageButton btn_more;

    Bitmap mercadologo;

    Produto produtoSelecionado;
    Empresa empresaEncontrada;

    public String precoPesavel;
    public String precoCompart;

    @OnClick(R.id.btn_camera)
    public void lerCodigoBarra() {
//        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.initiateScan();
        Intent irParaCodigoBarra = new Intent(this, ScanBarCodeActivity.class);
        startActivityForResult(irParaCodigoBarra, REQUEST_BARCODE);
        //https://github.com/zxing/zxing/wiki/Scanning-Via-Intent
    }
    @OnClick(R.id.btn_exit)
    public void exitItemAtr() {
        itemAtr.setVisibility(View.GONE);
    }
    @OnItemClick(R.id.lvDrawer)
    public void lvDrawerClick(int position) {
        switch (position) {
            case 1:
                irParaMapa();
                break;
            case 2:
                minhaCesta();
                break;
            case 3:
                irParaHistorico();
                break;
        }
    }
    @OnClick(R.id.btn_less)
    public void diminuirQtd() {
        if (Double.parseDouble(quantidade.getText().toString()) > 1) {
            int novaQuantidade = Integer.parseInt(quantidade.getText().toString()) - 1;
            quantidade.setText("" + novaQuantidade);
            double novoValor = Double.parseDouble(valor.getText().toString()) - produtoSelecionado.getValor();
            //valor.setText("" + novoValor);
            valor.setText((String.format(Locale.US, "%.2f", novoValor)));
        } else {
            //Toast.makeText(this, "A quantidade deve ser maior que 1", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_more)
    public void aumentarQtd() {
        int novaQuantidade = Integer.parseInt(quantidade.getText().toString()) + 1;
        quantidade.setText("" + novaQuantidade);
        double novoValor = Double.parseDouble(valor.getText().toString()) + produtoSelecionado.getValor();
        //valor.setText("" + novoValor);
        valor.setText((String.format(Locale.US, "%.2f", novoValor)));
    }
    private void irParaMapa() {

        //ImageView imgmercado;


        Bitmap mBitmap = mercadologo;
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "imgmercado", null);
        Uri uri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Estou aqui no\n*" + empresaEncontrada.getRazaosocial() + "* da\n" + empresaEncontrada.getEndereco() + ",\n" + "que está com várias promoções !"+"\nAinda não tem o App _*TUC*_?\nInstale agora: https://i.ytimg.com/vi/y_7yT0m0o3M/hqdefault.jpg");
        shareIntent.setType("*/*");
        shareIntent.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(shareIntent, "Compartilha Produto no WApp"));

    }
    private void minhaCesta() {
        Intent irParaCesta = new Intent(this, CestaActivity.class);
        startActivity(irParaCesta);
    }
    private void irParaHistorico() {
        Intent irParaHistorico = new Intent(this, HistoricoActivity.class);
        startActivity(irParaHistorico);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        buscarEmpresa();
        montarToolbar();
        montarDrawer();
        deixarInvisivel();
    }
    private void montarToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_toolbar_menu));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }
    private void montarDrawer() {
        List<DrawerItem> itensDrawer = new ArrayList<>();
        itensDrawer.add(null);
        itensDrawer.add(new DrawerItem(R.drawable.ic_drawer_map, "Compartilhar Local"));
        itensDrawer.add(new DrawerItem(R.drawable.ic_drawer_cesta, "Meu Carrinho"));
        //itensDrawer.add(new DrawerItem(R.drawable.ic_drawer_cesta, "Histórico de Compras"));
        DrawerAdapterList listAdapter = new DrawerAdapterList(this, itensDrawer, mercadologo);
        drawer.setAdapter(listAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_BARCODE) {
            // handle scan result
            if (intent!=null && intent.getStringExtra("cod") != null) {
                //O tão esperado som do "TUC"
                MediaPlayer mp;
                mp = MediaPlayer.create(MainActivity.this, R.raw.beep1);
                mp.start();
                if(!intent.getStringExtra("preco").equals("")){
                    //valor.setText(intent.getStringExtra("preco"));
                    precoPesavel = intent.getStringExtra("preco");
                }
                codigoBarra.setText(intent.getStringExtra("cod"));
                //Toast.makeText(this, ""+ codigoBarra.getText(), Toast.LENGTH_LONG).show();
                buscar();
            } else {
                Toast.makeText(this, "Código não reconhecido. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void buscar() {
        String cod = codigoBarra.getText().toString();
        //Chamada para API
        Call<Produto> call = TucClientApi.getTucApi().buscarProduto(cod);
        //Retorno da API
        call.enqueue(new Callback<Produto>() {
            //Tratando o retorno com Sucesso
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {
                Produto produtoEncontrado = response.body();
                carregarProdutoNaTela(produtoEncontrado);
            }
            //Tratando a Falha
            @Override
            public void onFailure(Call<Produto> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Produto não encontrado", Toast.LENGTH_SHORT).show();
            }
        });
        Call<ResponseBody> imagemProduto = TucClientApi.getTucApi().buscarImagemProduto(cod);
        imagemProduto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    byte[] bytes = response.body().bytes();
                    ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
                    Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
                    carregarImagemNaTela(bitmap);
                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(MainActivity.this,"Imagem não pode ser carregada", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void buscarEmpresa() {
        //Chamada para API
        Call<Empresa> call = TucClientApi.getTucApi().buscarDadosEmpresa();
        //Retorno da API
        call.enqueue(new Callback<Empresa>() {
            //Tratando o retorno com Sucesso
            @Override
            public void onResponse(Call<Empresa> call, Response<Empresa> response) {
                empresaEncontrada = response.body();
             }
            //Tratando a Falha
            @Override
            public void onFailure(Call<Empresa> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Estabelecimento sem TUC", Toast.LENGTH_SHORT).show();
            }
        });
        Call<ResponseBody> imagemLogo = TucClientApi.getTucApi().buscarLogoEmpresa();
        imagemLogo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    byte[] bytes = response.body().bytes();
                    ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
                    Bitmap bitmap2 = BitmapFactory.decodeStream(arrayInputStream);
                    mercadologo = bitmap2;
                    montarDrawer();

                } catch (Exception e) {
                    //Bitmap hightech = BitmapFactory.decodeResource(getResources(), R.drawable.walmart);
                    //mercadologo = hightech;
                    Toast.makeText(MainActivity.this,"Estabelecimento não pode ser carregado", Toast.LENGTH_SHORT).show();
                    montarDrawer();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Bitmap hightech = BitmapFactory.decodeResource(getResources(), R.drawable.hightechlogo);
                mercadologo = hightech;
                montarDrawer();
            }
        });
    }


    public void carregarProdutoNaTela(Produto produtoSelecionado) {
        this.produtoSelecionado = produtoSelecionado;
        if (produtoSelecionado != null) {
            itemAtr.setVisibility(View.VISIBLE);
            //Display
            descricao.setText(produtoSelecionado.getDescricao());
            //valor.setText(produtoSelecionado.getValor().toString());
            precoCompart = String.format(Locale.US, "%.2f", produtoSelecionado.getValor());

            if (codigoBarra.getText().toString().startsWith("0")) {
                valor.setText(precoPesavel);
                produtoSelecionado.setValor(Double.valueOf(precoPesavel));
                quantidade.setVisibility(View.GONE);
                btn_less.setVisibility(View.GONE);
                btn_more.setVisibility(View.GONE);
            } else {
                valor.setText((String.format(Locale.US, "%.2f", produtoSelecionado.getValor())));
            }

            quantidade.setText("1");

        } else {
            Toast.makeText(this, "Produto não encontrado!", Toast.LENGTH_SHORT).show();
            descricao.setText("");
            valor.setText("");
            deixarInvisivel();
        }
        limparCodigo();
    }
    private void carregarImagemNaTela(Bitmap img) {
        imgproduto.setImageBitmap(img);

    }
    private void limparCodigo() {
        codigoBarra.setText("");
    }
    private void limparDescValor() {
        descricao.setText("");
        valor.setText("");
    }
    private void deixarInvisivel() {
        itemAtr.setVisibility(View.GONE);
    }


    // DESATIVADO BOTAO PARA ADICIONAR A CESTA

    @OnClick(R.id.btnAddCesta)
    public void adicionarCesta() {
        if (produtoSelecionado != null) {
            if (quantidade.getText().length() != 0) {
                int qtd = Integer.parseInt(quantidade.getText().toString());
                ItemCesta itemCesta = new ItemCesta(produtoSelecionado, qtd);
                CestaRepository.adicionarCesta(itemCesta);
                //total = total + (qtd * produtoSelecionado.getValor().doubleValue());
                //Toast.makeText(this, "Total Parcial: " + total, Toast.LENGTH_SHORT).show();
                produtoSelecionado = null;
                deixarInvisivel();
                limparDescValor();
                imgproduto.setImageBitmap(null);

                quantidade.setVisibility(View.VISIBLE);
                btn_less.setVisibility(View.VISIBLE);
                btn_more.setVisibility(View.VISIBLE);

                Toast.makeText(this, "Produto Adicionado com Sucesso !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Digite a Quantidade !", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //

    @OnClick(R.id.btnShareZap)
    public void compartilharWhatsapp() {

        Drawable mDrawable = imgproduto.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "imgproduto", null);
        Uri uri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Veja esta promoção do TUC aqui no\n*" + empresaEncontrada.getRazaosocial() + "* da\n" + empresaEncontrada.getEndereco() + "\n" + descricao.getText() + "\npor R$: *" + precoCompart + "*. \nAinda não tem o App _*TUC*_?\nInstale agora: https://i.ytimg.com/vi/y_7yT0m0o3M/hqdefault.jpg");
        shareIntent.setType("*/*");
        shareIntent.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(shareIntent, "Compartilha Produto no WApp"));

    }

    @OnClick(R.id.btnShare)
    public void compartilharExterno() {
        /*Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Dá uma olhada nesta promoção \naqui do Mercado XXXX da Av. ZZZZZZZZ:  \n"+ descricao.getText() + "\nR$: " + precoCompart + ". \nAinda não tem o App TUC ?\nInstale agora: https://i.ytimg.com/vi/y_7yT0m0o3M/hqdefault.jpg");
        sendIntent.setType("text/plain");
        //sendIntent.setPackage("com.whatsapp");
        startActivity(sendIntent);*/

        Drawable mDrawable = imgproduto.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "imgproduto", null);
        Uri uri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Veja esta promoção do TUC aqui no\n" + empresaEncontrada.getRazaosocial() + " da\n" + empresaEncontrada.getEndereco() + "\n" + descricao.getText() + "\npor R$: " + precoCompart + ". \nAinda não tem o App TUC ?\nInstale agora: https://i.ytimg.com/vi/y_7yT0m0o3M/hqdefault.jpg");
        shareIntent.setType("*/*");
        startActivity(Intent.createChooser(shareIntent, "Compartilha Produto"));
    }

}
