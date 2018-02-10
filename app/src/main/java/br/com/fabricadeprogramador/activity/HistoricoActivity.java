package br.com.fabricadeprogramador.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import br.com.fabricadeprogramador.adapter.ProductAdapterList;
import br.com.fabricadeprogramador.bd.BdManager;
import br.com.fabricadeprogramador.model.ItemCesta;
import br.com.fabricadeprogramador.model.Produto;
import br.com.fabricadeprogramador.repository.CestaRepository;
import br.com.fabricadeprogramador.tuc.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;

public class HistoricoActivity extends Activity {


    List<ItemCesta> historicoList;
    public ArrayAdapter<ItemCesta> myAdapter;


    @Bind(R.id.lv_ah_historico)
    ListView lvHistorico;

    public HistoricoActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        ButterKnife.bind(this);
        makeHistorico();
        ProductAdapterList productAdapterList = new ProductAdapterList(this, historicoList);
        lvHistorico.setAdapter(productAdapterList);
        myAdapter=productAdapterList;

    }

    private void makeHistorico(){
        BdManager bdManager = new BdManager(this);
        historicoList = bdManager.findAlltoItemCesta();
    }
}
