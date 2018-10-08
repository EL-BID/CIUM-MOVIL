package code.yoursoft.ciummovil;


import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorClues extends RecyclerView.Adapter<ViewHolderClues> {

    private List<Clues> paletas;



    public AdaptadorClues(List<Clues> paletas)
      {
        this.paletas = new ArrayList<>();
        this.paletas.addAll(paletas);
      }

    @Override
    public ViewHolderClues onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_clues, viewGroup, false);

        return new ViewHolderClues(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderClues paletaViewHolder, int i)
    {

        Clues paleta = paletas.get(i);

        paletaViewHolder.clues.setText(paleta.getNombre());
        paletaViewHolder.detalle1.setText(paleta.getClues());
        paletaViewHolder.detalle2.setText(paleta.getLocalidad() + ", " + paleta.getMunicipio());
        paletaViewHolder.detalle3.setText(paleta.getCone() + "," + paleta.getTipoUnidad());
        paletaViewHolder.card.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
    }

    @Override
    public int getItemCount() {
        return paletas.size();
    }

    public void updateDataRecycler(List<Clues> new_data)
    {

        this.paletas.clear();
        this.paletas.addAll(new_data);
        notifyDataSetChanged();
    }

}
    class ViewHolderClues extends RecyclerView.ViewHolder{

        protected TextView clues;
        protected TextView detalle1;
        protected TextView detalle2;
        protected TextView detalle3;

        //protected ImageView status_icon;
        protected CardView card;

        public ViewHolderClues(View itemView) {
            super(itemView);

            clues = (TextView) itemView.findViewById(R.id.clave_clues);
            detalle1 = (TextView) itemView.findViewById(R.id.nombre_criterio);
            detalle2 = (TextView) itemView.findViewById(R.id.tipo_item);
            detalle3 = (TextView) itemView.findViewById(R.id.detalle2);

            //status_icon=(ImageView) itemView.findViewById(R.id.status_icon);
            //status_icon.setImageResource(R.drawable.logo);
            card = (CardView) itemView;
        }



    }


