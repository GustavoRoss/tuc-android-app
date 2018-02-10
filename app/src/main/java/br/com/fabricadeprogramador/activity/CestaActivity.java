package br.com.fabricadeprogramador.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import br.com.fabricadeprogramador.adapter.ProductAdapterList;
import br.com.fabricadeprogramador.bd.BdManager;
import br.com.fabricadeprogramador.model.ItemCesta;
import br.com.fabricadeprogramador.repository.CestaRepository;
import br.com.fabricadeprogramador.tuc.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;

public class CestaActivity extends Activity {

    List<ItemCesta> cestaList  =  CestaRepository.getCestaList();
    public ArrayAdapter<ItemCesta> myAdapter;

    BdManager bdManager = new BdManager(this);

    @Bind(R.id.lvCesta)
    ListView lvCesta;

    @Bind(R.id.tv_ValorTotal)
    TextView valorTotal;

    @OnClick(R.id.btn_ac_clean)
    void cleanCart(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CestaActivity.this);
        builder.setMessage("Deseja esvaziar o Carrinho ?")

                .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cestaList.clear();
                        Toast.makeText(CestaActivity.this, "  Carrinho vazio !", Toast.LENGTH_SHORT).show();
                        myAdapter.notifyDataSetChanged();
                        calculaTotal();
                    }
                })
                .setNegativeButton(android.R.string.no, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnItemLongClick(R.id.lvCesta)
    boolean onItemLongClick(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CestaActivity.this);
        builder.setMessage("Deseja remover este Produto ?")
                .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nomeProduto = cestaList.get(position).getProduto().getDescricao();
                        cestaList.remove(position);
                        Toast.makeText(CestaActivity.this, "" + nomeProduto + " removido !", Toast.LENGTH_SHORT).show();
                        myAdapter.notifyDataSetChanged();
                        calculaTotal();
                    }
                })
                .setNegativeButton(android.R.string.no, null);

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta);
        ButterKnife.bind(this);
        ProductAdapterList productAdapterList = new ProductAdapterList(this, cestaList);
        lvCesta.setAdapter(productAdapterList);
        myAdapter=productAdapterList;
        calculaTotal();

    }
    public void calculaTotal() {
        Double calculoTotal = 0.0;
        for (int i=0; i<cestaList.size(); i++){
            calculoTotal = calculoTotal + (cestaList.get(i).getQuantidade() * cestaList.get(i).getProduto().getValor());
        }
        valorTotal.setText(String.format(Locale.US, "%.2f", calculoTotal));

    }
}