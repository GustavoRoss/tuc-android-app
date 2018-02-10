package br.com.fabricadeprogramador.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.fabricadeprogramador.model.ItemCesta;
import br.com.fabricadeprogramador.tuc.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by guguh on 19/11/2016.
 */

public class ProductAdapterList extends ArrayAdapter<ItemCesta> {


    public ProductAdapterList(Context context, List<ItemCesta> itens) {
        super(context, R.layout.item_product_view, itens);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_product_view, null);
            holder = new ViewHolder();
            ButterKnife.bind(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemCesta basket = getItem(position);
        if (basket != null) {
            holder.productDescription.setText(basket.getProduto().getDescricao());
            holder.productPrice.setText(basket.getProduto().getValor().toString());
           // holder.productImage.setImageResource(basket.getProduto().getImage());
            holder.productAmount.setText(String.valueOf(basket.getQuantidade()));
        }


        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.product_description)
        TextView productDescription;
       // @Bind(R.id.product_image)
        //ImageView productImage;
        @Bind(R.id.product_price)
        TextView productPrice;
        @Bind(R.id.product_qtd)
        TextView productAmount;
        /*
        @OnClick(R.id.btnShareZapLst)
            public void compartilharWhatsapp() {
        }
        */
    }
}
