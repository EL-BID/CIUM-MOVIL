package code.yoursoft.ciummovil;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorCluesSelected extends RecyclerView.Adapter<ViewHolderCluesSelected> {

    private List<Clues> paletas;
    private Context context;



    public AdaptadorCluesSelected(List<Clues> paletas, Context context) {
        this.paletas = new ArrayList<>();
        this.paletas.addAll(paletas);
        this.context= context;
    }

    @Override
    public ViewHolderCluesSelected onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_clues_selected, viewGroup, false);

        return new ViewHolderCluesSelected(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderCluesSelected paletaViewHolder, int i) {

        Clues paleta = paletas.get(i);

        Resources res = context.getResources();
        int primary = res.getColor(R.color.PrimaryColor);

        paletaViewHolder.nombre.setText(paleta.getNombre());
        paletaViewHolder.clues.setText(paleta.getClues());
        paletaViewHolder.jurisdiccion.setText(paleta.getJurisdiccion());
        paletaViewHolder.localidad.setText(paleta.getLocalidad() + ", MPO. " + paleta.getMunicipio());
        paletaViewHolder.ubicacion.setText(paleta.getDomicilio());
        paletaViewHolder.tipologia.setText(paleta.getTipologia());
        paletaViewHolder.tipo.setText(paleta.getTipoUnidad());
        paletaViewHolder.card.setCardBackgroundColor(primary);
    }

    @Override
    public int getItemCount() {
        return paletas.size();
    }

    public void updateDataRecycler(List<Clues> new_data) {

        this.paletas.clear();
        this.paletas.addAll(new_data);
        notifyDataSetChanged();
    }

}
    class ViewHolderCluesSelected extends RecyclerView.ViewHolder{

        protected TextView nombre;
        protected TextView clues;
        protected TextView jurisdiccion;
        protected TextView localidad;
        protected TextView ubicacion;
        protected TextView tipologia;
        protected TextView tipo;

        //protected ImageView status_icon;
        protected CardView card;

        public ViewHolderCluesSelected(View itemView)
        {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombre_criterio);
            clues = (TextView) itemView.findViewById(R.id.clave_clues);
            jurisdiccion = (TextView) itemView.findViewById(R.id.jurisdiccion_clues);
            localidad = (TextView) itemView.findViewById(R.id.tipo_clues);
            ubicacion = (TextView) itemView.findViewById(R.id.ubicacion_clues);
            tipologia = (TextView) itemView.findViewById(R.id.tipologia_clues);
            tipo = (TextView) itemView.findViewById(R.id.tipo_clues);

            //status_icon=(ImageView) itemView.findViewById(R.id.status_icon);
            //status_icon.setImageResource(R.drawable.logo);
            card = (CardView) itemView;
        }



    }


