package code.yoursoft.ciummovil;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorListaEvaluacionesRecurso extends RecyclerView.Adapter<ViewHolderEvaluacionRecurso>
{

    private SparseBooleanArray selectedItems;
    private List<EvaluacionDetalles> paletas;
    private Context context;

    Funciones link = new Funciones();
    //DBManager linkDB=new DBManager();

    public AdaptadorListaEvaluacionesRecurso(List<EvaluacionDetalles> palettes, Context context)
    {
        this.paletas = new ArrayList<>();
        this.paletas.addAll(palettes);
        this.context= context;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ViewHolderEvaluacionRecurso onCreateViewHolder(ViewGroup viewGroup, int i)
    {
         View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_recursos, viewGroup, false);

        return new ViewHolderEvaluacionRecurso(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderEvaluacionRecurso paletaViewHolder, int i) {

        EvaluacionDetalles paleta = paletas.get(i);

        paletaViewHolder.clues.setText( i+1 +" "+ paleta.getClues());

        Resources res = context.getResources();
        int primary = res.getColor(R.color.PrimaryColor);
        int accent_100 = res.getColor(R.color.Accent100);
        int naranja = res.getColor(R.color.naranja);
        int gris = res.getColor(R.color.cardview_light_background);

        if(selectedItems.get(i))
        {
            paletaViewHolder.background.setBackgroundColor(accent_100);
        }else{
                paletaViewHolder.background.setBackgroundColor(gris);
             }




        if(paleta.cerrado==0)
        {

            paletaViewHolder.cerrado.setImageResource(R.drawable.ic_create_black_24dp);
            paletaViewHolder.cerrado.setColorFilter(naranja);

        }else{
                paletaViewHolder.cerrado.setImageResource(R.drawable.ic_check_white_24dp);
                paletaViewHolder.cerrado.setColorFilter(primary);
             }

        if(paleta.firma.equals(""))
        {
            paletaViewHolder.firmado.setImageResource(R.drawable.firmar_24);
            paletaViewHolder.firmado.setColorFilter(naranja);

        }else{
                paletaViewHolder.firmado.setImageResource(R.drawable.firmar_24);
                paletaViewHolder.firmado.setColorFilter(primary);
             }

        if(paleta.getSincronizado() == 1)
        {
            paletaViewHolder.sincronizado.setImageResource(R.drawable.ic_sync_black_24dp);
            paletaViewHolder.sincronizado.setColorFilter(primary);

        }else{
                paletaViewHolder.sincronizado.setImageResource(R.drawable.ic_sync_disabled_black_24dp);
                paletaViewHolder.sincronizado.setColorFilter(naranja);
             }





        paletaViewHolder.nombre.setText(paleta.getCluesNombre());
        paletaViewHolder.jurisdiccion.setText(paleta.getCluesJurisdiccion());

        paletaViewHolder.text_indicadores.setText(""+paleta.indicadores);
        paletaViewHolder.text_criterios.setText(paleta.avanceCriterios+" / "+paleta.criterios);

        paletaViewHolder.usuario.setText(paleta.getUsuarioNombre());
        paletaViewHolder.fecha.setText(link.imprimirFecha( paleta.getFechaEvaluacion() ));
        //paletaViewHolder.card.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
    }

    @Override
    public int getItemCount()
    {
        return paletas.size();
    }

    public void updateDataRecycler(List<EvaluacionDetalles> new_data)
    {

        this.paletas.clear();
        this.paletas.addAll(new_data);
        notifyDataSetChanged();
    }

    public void removerEvaluacion(int position)
    {
        paletas.remove(position);
        //notifyItemRemoved(position);
    }

    public void notificarEvaluacionRemovida(int position)
    {
        notifyItemRemoved(position);
    }

    public EvaluacionDetalles getItem(int position)
    {
        return paletas.get(position);
    }

    public void toggleSelection(int pos)
    {
        if (selectedItems.get(pos, false))
        {
            selectedItems.delete(pos);
        }
        else {
               selectedItems.put(pos, true);
             }
        notifyItemChanged(pos);
    }

    public void removerSeleccion(int pos)
    {
        selectedItems.delete(pos);
        notifyItemChanged(pos);
    }

    public void resetearSelecciones()
    {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount()
    {
        return selectedItems.size();
    }


    public List<Integer> getItemsSeleccinados()
    {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++)
        {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public boolean estaSeleccionado(int pos)
    {
        return selectedItems.get(pos);
    }



}////  F I N      C L A S E



 class ViewHolderEvaluacionRecurso extends RecyclerView.ViewHolder
{

    protected TextView clues;

    protected ImageView cerrado;
    protected ImageView firmado;
    protected ImageView sincronizado;

    protected TextView nombre;
    protected TextView jurisdiccion;
    protected TextView usuario;
    protected TextView fecha;

    protected TextView text_indicadores;
    protected TextView text_criterios;

    protected LinearLayout background;
    //protected CardView card;

    public ViewHolderEvaluacionRecurso(View itemView)
    {
        super(itemView);

        background = (LinearLayout) itemView.findViewById(R.id.background_item);
        clues = (TextView) itemView.findViewById(R.id.clave_clues);
        nombre = (TextView) itemView.findViewById(R.id.nombre_criterio);

        cerrado = (ImageView) itemView.findViewById(R.id.status_icon);
        firmado = (ImageView) itemView.findViewById(R.id.status_icon2);
        sincronizado = (ImageView) itemView.findViewById(R.id.status_icon3);

        jurisdiccion = (TextView) itemView.findViewById(R.id.jurisdiccion);

        text_indicadores = (TextView) itemView.findViewById(R.id.text_indicadores);
        text_criterios = (TextView) itemView.findViewById(R.id.text_criterios);

        usuario = (TextView) itemView.findViewById(R.id.evaluador);
        fecha = (TextView) itemView.findViewById(R.id.fecha);

       // card = (CardView) itemView;
    }

}