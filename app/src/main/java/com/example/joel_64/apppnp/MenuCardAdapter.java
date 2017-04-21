package com.example.joel_64.apppnp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Joel-64 on 21/04/2017.
 */

public class MenuCardAdapter extends BaseAdapter{
    LayoutInflater inflador;
    String[] titulos;
    int[] imagenes;
    int[] colores;
    Context contexto;
    TextView item_text;
    ImageView item_img;
    CardView card;
    public MenuCardAdapter(String[] titulos, int[] imagenes, int[] colores, Context contexto) {
        this.titulos = titulos;
        this.imagenes = imagenes;
        this.colores = colores;
        this.contexto = contexto;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View item = inflador.inflate(R.layout.adapter_menu_card,null);
        card = (CardView) item.findViewById(R.id.cardView);
        item_text = (TextView) item.findViewById(R.id.titulo_categoria_alerta);
        item_img = (ImageView) item.findViewById(R.id.icono_categoria_alerta);
        card.setBackgroundResource(colores[i]);
        item_text.setBackgroundResource(colores[i]);
        item_text.setText(titulos[i]);
        item_img.setImageResource(imagenes[i]);
        return item;
    }
}
