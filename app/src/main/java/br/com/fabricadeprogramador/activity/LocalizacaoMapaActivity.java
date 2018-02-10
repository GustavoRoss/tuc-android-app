package br.com.fabricadeprogramador.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.List;

import br.com.fabricadeprogramador.api.TucClientApi;
import br.com.fabricadeprogramador.model.Localizacao;
import br.com.fabricadeprogramador.tuc.R;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalizacaoMapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    ProgressDialog dialog;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);


    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.dialog = ProgressDialog.show(LocalizacaoMapaActivity.this, "Aguarde...", "Estamos procurando os mercados mais próximos...", true);

        Call<List<Localizacao>> listaLocalizacoes = TucClientApi.getTucApi().buscarLocalizacao();
        listaLocalizacoes.enqueue(new Callback<List<Localizacao>>() {
            @Override
            public void onResponse(Call<List<Localizacao>> call, Response<List<Localizacao>> response) {
                dialog.dismiss();
                List<Localizacao> localizacoes = response.body();
                carregarLocalizacoes(localizacoes);


            }

            @Override
            public void onFailure(Call<List<Localizacao>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(LocalizacaoMapaActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void carregarLocalizacoes(List<Localizacao> localizacoes) {
        for (Localizacao loc : localizacoes) {

            LatLng pontos = new LatLng(loc.getLatitude(),loc.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(pontos));

        }
    }


}
