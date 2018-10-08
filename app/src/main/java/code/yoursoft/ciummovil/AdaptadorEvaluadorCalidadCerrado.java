package code.yoursoft.ciummovil;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzhl.runOnUiThread;


public class AdaptadorEvaluadorCalidadCerrado extends RecyclerView.Adapter<ViewHolderECCerrado>
{

    private List<DataEvaluadorCalidad> paletas;
    private List<EvaluacionCalidadCriterio> respuestas_db;
    private List<EvaluacionCalidadCriterio> respuestas_live;

    Funciones link=new Funciones();
    public boolean cambios=false;

    Context context;

    Resources res;
    int primary;
    int rojo;
    int blanco;
    int gris;



    /// RECIBIR RESPUESTAS DE BASE DATOS
    //  VACIAR  A RESPUESTAS EN VIVO
    //  CONFORME A EVENTOS, ACTUALIZAR LISTA EN VIVO
    /// CREAR METODO QUE DEVUELVA LAS RESPUESTAS EN VIVO Y LAS ENTRGUE A LA UI (EVALUADOR_RECURSO)


    public AdaptadorEvaluadorCalidadCerrado(List<DataEvaluadorCalidad> paletas, Context context  )
    {
        this.paletas = new ArrayList<>();
        this.paletas.addAll(paletas);

        this.context=context;


        this.res = context.getResources();
        this.primary = res.getColor(R.color.PrimaryColor);
        this.rojo = res.getColor(R.color.rojo);
        this.blanco = res.getColor(R.color.white);
        this.gris = res.getColor(R.color.cardview_light_background);

    }

    @Override
    public ViewHolderECCerrado onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_ec_cerrado, viewGroup, false);

        return new ViewHolderECCerrado(itemView);
    }

    @Override
        public void onBindViewHolder(final ViewHolderECCerrado pVH, int i)
    {

        printRespuestas();


        final DataEvaluadorCalidad paleta = paletas.get(i);
        int valor_aprobado=paleta.respuesta;

        System.out.println("\n  **********  [ SE CREA VIEWHOLDER [ "+i+" ], VALOR APROBADO -->  [" + valor_aprobado + "]");
        pVH.radio_group.check(valor_aprobado);


        if(paleta.tipoItem.equals("LUGAR"))
        {

            //// pVH.tipo_item.setText("Lugar de verificación : ");

            pVH.tipo_item.setVisibility(View.GONE);
            pVH.nombre_lugar.setText(paleta.nombreLugarVerificacion);

            pVH.nombre_criterio.setVisibility(View.GONE);
            pVH.nombre_lugar.setVisibility(View.VISIBLE);
            pVH.radio_group.setVisibility(View.GONE);

            pVH.radio_si.setChecked(false);
            pVH.radio_no.setChecked(false);
            pVH.radio_na.setChecked(false);


            pVH.boton_yes.setVisibility(View.GONE);
            pVH.boton_no.setVisibility(View.GONE);

        }

        if(paleta.tipoItem.equals("CRITERIO"))
        {

               //pVH.tipo_item.setText("Criterio : ");

               pVH.tipo_item.setVisibility(View.GONE);
               pVH.nombre_criterio.setText(paleta.nombreCriterio);

               pVH.nombre_lugar.setVisibility(View.GONE);
               pVH.nombre_criterio.setVisibility(View.VISIBLE);
               pVH.radio_group.setVisibility(View.GONE);

                if(paleta.habilitarNa==0)
                {
                    pVH.radio_na.setVisibility(View.GONE);
                }else{
                       pVH.radio_na.setVisibility(View.VISIBLE);
                     }

            pVH.boton_yes.setVisibility(View.VISIBLE);
            pVH.boton_no.setVisibility(View.VISIBLE);

        }

        if(valor_aprobado==0)
        {
            pVH.boton_yes.setImageResource(R.drawable.ic_check_box_outline_blank_white_24dp);
            pVH.boton_no.setImageResource(R.drawable.ic_clear_black_24dp);

            pVH.boton_yes.setBackgroundColor(blanco);
            pVH.boton_no.setBackgroundColor(rojo);

        }

        if(valor_aprobado==1)
        {
            pVH.boton_yes.setImageResource(R.drawable.ic_check_black_24dp);
            pVH.boton_no.setImageResource(R.drawable.ic_check_box_outline_blank_white_24dp);

            pVH.boton_yes.setBackgroundColor(primary);
            pVH.boton_no.setBackgroundColor(blanco);
        }

        if(valor_aprobado==2)
        {
            pVH.boton_yes.setBackgroundColor(blanco);
            pVH.boton_no.setBackgroundColor(blanco);

            pVH.boton_yes.setImageResource(R.drawable.ic_remove_black_24dp);
            pVH.boton_no.setImageResource(R.drawable.ic_remove_black_24dp);
        }


        //pVH.radio_group.check(valor_aprobado);

    }

    @Override
    public int getItemCount()
    {
        return paletas.size();
    }

    public List<DataEvaluadorCalidad> getRespuestas()
    {
        List<DataEvaluadorCalidad> respuestas = new ArrayList<>();
        respuestas.addAll(this.paletas);

        return respuestas;
    }



    public int [] contarPreguntas()
    {
        int preguntas=0;
        int respondidas=0;
        int aprobadas=0;
        int negativas=0;
        int nas=0;

        int changes=0;

        if(cambios==true){changes=1;}else{changes=0;}


        for(int i=0; i<paletas.size(); i++)
        {
            if(paletas.get(i).tipoItem.equals("CRITERIO"))
            {
                preguntas++;

                if(paletas.get(i).respuesta==1)
                {
                    aprobadas++;
                    respondidas++;
                }
                if(paletas.get(i).respuesta==0)
                {
                    negativas++;
                    respondidas++;
                }
                if(paletas.get(i).respuesta==2)
                {
                    nas++;
                    respondidas++;
                }

            }

        }

        int array []={preguntas,respondidas,aprobadas,negativas,nas,changes};

        return array;
    }


    public void printRespuestas()
    {

        System.out.println("\n ------ INICIO IMPRESION RESPUESTAS LIVE :   -------->  ");
        for(int j=0; j<paletas.size();j++)
        {
            System.out.println( "ID EVAL. REC : "+paletas.get(j).idEvaluacionCalidad+
                            ", ID INDICADOR: "+paletas.get(j).idIndicador+
                            ", ID CRITERIO : ["+paletas.get(j).idCriterio+"] VALUE --> ["+paletas.get(j).respuesta+"]");

        }
        System.out.println("\n <----- FIN IMPRESION RESPUESTAS LIVE -----");

    }


    public void updateDataRecycler(List<DataEvaluadorCalidad> new_preguntas)
    {
        this.paletas.clear();
        this.paletas.addAll(new_preguntas);

        notifyDataSetChanged();
    }


    public void addItem(DataEvaluadorCalidad item, int index)
    {
        this.paletas.add(item);
        notifyItemInserted(index);
    }

    public void deleteItem(int index)
    {
        this.paletas.remove(index);
        notifyItemRemoved(index);
    }



}



 class ViewHolderECCerrado  extends RecyclerView.ViewHolder
{

    protected TextView nombre_lugar;
    protected TextView nombre_criterio;
    protected TextView tipo_item;

    //protected CardView card;

    protected RadioGroup radio_group;
    protected RadioButton radio_si;
    protected RadioButton radio_no;
    protected RadioButton radio_na;


    protected ImageButton boton_yes;
    protected ImageButton boton_no;


    public ViewHolderECCerrado(View itemView)
    {
        super(itemView);

        nombre_lugar = (TextView) itemView.findViewById(R.id.nombre_lugar);
        nombre_criterio = (TextView) itemView.findViewById(R.id.nombre_criterio);

        tipo_item = (TextView) itemView.findViewById(R.id.tipo_item);

        radio_group = (RadioGroup) itemView.findViewById(R.id.radio_group);
        radio_si = (RadioButton) itemView.findViewById(R.id.si);
        radio_no = (RadioButton) itemView.findViewById(R.id.no);
        radio_na = (RadioButton) itemView.findViewById(R.id.na);


        boton_yes = (ImageButton) itemView.findViewById(R.id.imageButtonYes);
        boton_no = (ImageButton) itemView.findViewById(R.id.imageButtonNo);

        //card = (CardView) itemView;
    }

}