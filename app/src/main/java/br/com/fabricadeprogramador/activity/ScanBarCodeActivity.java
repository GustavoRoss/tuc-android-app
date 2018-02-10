package br.com.fabricadeprogramador.activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.zxing.Result;
import br.com.fabricadeprogramador.tuc.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
public class ScanBarCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    public final String CONSTANTE = "00000000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        setupToolbar();
    }
    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        scannerView.setAutoFocus(true);
    }
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
    /**
     * modificado por Gustavo e implementado por Rafael
     */
    //@RequiresApi(api = Build.VERSION_CODES.N)
    public String receberCodigo(String codigo) {
        String codigoInteiro = CONSTANTE + codigo.substring(1, 5);
        String digitoVerificador = String.valueOf(calcularDigitoVerificador(codigoInteiro));
        return codigoInteiro + digitoVerificador;
    }
    /**
     * modificado e implementado por Rafael
     */
    //@RequiresApi(api = Build.VERSION_CODES.N)
    protected int calcularDigitoVerificador(String first12digits) {
        char[] char12digits = first12digits.toCharArray();
        int[] ean13 = {1,3};
        int sum = 0;
        for(int i = 0 ; i<char12digits.length; i++){
            sum += Character.getNumericValue(char12digits[i]) * ean13[i%2];
        }

        int checksum = 10 - sum%10;
        if(checksum == 10){
            checksum = 0;
        }
        return checksum;
    }
    /**
     * modificado por Gustavo e Rafael
     */
    //@RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void handleResult(Result result) {
        String codigoCallback = result.getText();

        if (codigoCallback.startsWith("2")) {
            Double precoProdutopesavel = Double.valueOf(codigoCallback.substring(7,12))/100;
            String resultadoProdutoPesavel = receberCodigo(codigoCallback);
            Intent it = new Intent();
            it.putExtra("cod", resultadoProdutoPesavel);
            it.putExtra("preco", String.valueOf(precoProdutopesavel));
            setResult(RESULT_OK, it);
            finish();
        }else {
            Intent it = new Intent();
            it.putExtra("cod", result.getText());
            it.putExtra("preco", "");
            setResult(RESULT_OK, it);
            finish();
        }
    }
}