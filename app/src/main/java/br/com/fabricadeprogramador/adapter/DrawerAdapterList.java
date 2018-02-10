package br.com.fabricadeprogramador.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import br.com.fabricadeprogramador.api.TucClientApi;
import br.com.fabricadeprogramador.view.DrawerItem;
import br.com.fabricadeprogramador.tuc.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cliente on 19/11/2016.
 */
public class DrawerAdapterList extends ArrayAdapter<DrawerItem> {

    public static final int TYPE1 = 0;
    public static final int TYPE2 = 1;
    private Bitmap logo;

    Context context;
    public DrawerAdapterList(Context context, List<DrawerItem> itemList, Bitmap logo) {
        super(context, R.layout.navdrawer_list, itemList);
        this.logo = logo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemType = getItemViewType(position);

        if (itemType == TYPE2) {
            final viewHolder holder;

            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.navdrawer_list, null);
                holder = new viewHolder();
                ButterKnife.bind(holder, convertView);
                convertView.setTag(holder);

            } else {
                holder = (viewHolder) convertView.getTag();
            }

            DrawerItem nItem = getItem(position);
            if (nItem != null) {
                holder.imagem.setImageResource(nItem.getImagem());
                holder.itemNome.setText(nItem.getItemNome());
            }

        }else{
            final fstHolder holder;

            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.navdrawer_fst, null);
                holder = new fstHolder();
                ButterKnife.bind(holder, convertView);
                convertView.setTag(holder);

            } else {
                holder = (fstHolder) convertView.getTag();
            }

            holder.nome.setText("ESTABELECIMENTO");
            holder.logo.setImageBitmap(logo);
        }
        return convertView;
    }

    class viewHolder {
        @Bind(R.id.tv_ndl_itemName)
        TextView itemNome;

        @Bind(R.id.iv_ndl_itemIcon)
        ImageView imagem;
    }

    class fstHolder{
        @Bind(R.id.tv_ndf_nome)
        TextView nome;

        @Bind(R.id.iv_nd_logo)
        ImageView logo;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE1;
        } else {
            return TYPE2;
        }
    }
}
