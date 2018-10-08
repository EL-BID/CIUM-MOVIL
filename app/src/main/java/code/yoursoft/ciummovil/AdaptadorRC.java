package code.yoursoft.ciummovil;


import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorRC extends RecyclerView.Adapter<ViewHolderRC>
{

    private List<EvaluacionDetalles> paletas;
    //DBManager linkDB=new DBManager();

    public AdaptadorRC(List<EvaluacionDetalles> palettes)
    {
        this.paletas = new ArrayList<>();
        this.paletas.addAll(palettes);
    }

    @Override
    public ViewHolderRC onCreateViewHolder(ViewGroup viewGroup, int i)
    {
         View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_recursos, viewGroup, false);

        return new ViewHolderRC(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderRC paletaViewHolder, int i) {

        EvaluacionDetalles paleta = paletas.get(i);

        paletaViewHolder.clues.setText("[ "+paleta.getId()+" ] "+paleta.getClues());

        paletaViewHolder.cerrado.setImageResource(R.drawable.ic_create_black_24dp);



        paletaViewHolder.nombre.setText(paleta.getCluesNombre());
        paletaViewHolder.jurisdiccion.setText(paleta.getCluesJurisdiccion());
        paletaViewHolder.usuario.setText(paleta.getUsuarioNombre());
        paletaViewHolder.fecha.setText(paleta.getFechaEvaluacion());
        paletaViewHolder.card.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
    }

    @Override
    public int getItemCount() {
        return paletas.size();
    }

    public void updateDataRecycler(List<EvaluacionDetalles> new_data)
    {

        this.paletas.clear();
        this.paletas.addAll(new_data);
        notifyDataSetChanged();
    }

}



 class ViewHolderRC extends RecyclerView.ViewHolder
{

    protected TextView clues;
    protected ImageView cerrado;
    protected TextView nombre;
    protected TextView jurisdiccion;
    protected TextView usuario;
    protected TextView fecha;
    protected CardView card;

    public ViewHolderRC(View itemView)
    {
        super(itemView);

        clues = (TextView) itemView.findViewById(R.id.clave_clues);
        nombre = (TextView) itemView.findViewById(R.id.nombre_criterio);
        cerrado=(ImageView) itemView.findViewById(R.id.status_icon);
        jurisdiccion = (TextView) itemView.findViewById(R.id.jurisdiccion);
        usuario = (TextView) itemView.findViewById(R.id.evaluador);
        fecha = (TextView) itemView.findViewById(R.id.fecha);

        card = (CardView) itemView;
    }

}