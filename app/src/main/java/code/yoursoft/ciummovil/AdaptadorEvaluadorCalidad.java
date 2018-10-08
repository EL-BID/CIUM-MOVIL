package code.yoursoft.ciummovil;


import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorEvaluadorCalidad extends RecyclerView.Adapter<ViewHolderEC>
{



    private List<DataEvaluadorCalidad> paletas;
    private List<EvaluacionCalidadCriterio> respuestas_db;
    private List<EvaluacionCalidadCriterio> respuestas_live;

    private List<CriterioValidacionRespuesta>  respuestas_validacion;

    Funciones link = new Funciones();
    public boolean cambios=false;


        /// RECIBIR RESPUESTAS DE BASE DATOS
        //  VACIAR  A RESPUESTAS EN VIVO
        //  CONFORME A EVENTOS, ACTUALIZAR LISTA EN VIVO
        /// CREAR METODO QUE DEVUELVA LAS RESPUESTAS EN VIVO Y LAS ENTRGUE A LA UI (EVALUADOR_RECURSO)

/*
    private AdapterCallback mAdapterCallback;

    public interface AdapterCallback
    {
        void onMethodCallback();
    }
*/

    private InterfaceCalidad listener;

    Resources res;
    int primary;
    int rojo;
    int blanco;
    int gris;

    int cerrado = 0;



    public AdaptadorEvaluadorCalidad(List<DataEvaluadorCalidad> paletas, List<CriterioValidacionRespuesta> respuestas_validacion,InterfaceCalidad listener,Context context,int cerrado)
    {
        this.paletas = new ArrayList<>();
        this.paletas.addAll(paletas);

        this.respuestas_validacion= new ArrayList<>();
        this.respuestas_validacion.addAll(respuestas_validacion);

        this.listener = listener;

        this.cerrado = cerrado;

        this.res = context.getResources();
        this.primary = res.getColor(R.color.PrimaryColor);
        this.rojo = res.getColor(R.color.rojo);
        this.blanco = res.getColor(R.color.white);
        this.gris = res.getColor(R.color.cardview_light_background);


    }

    @Override
    public ViewHolderEC onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_ec, viewGroup, false);

        return new ViewHolderEC(itemView);
    }

    @Override
        public void onBindViewHolder(final ViewHolderEC pVH, int i)
    {
        printRespuestas();

        final DataEvaluadorCalidad paleta = paletas.get(i);

        int valor_aprobado = paleta.respuesta;

        Boolean validar=false;


        if(paleta.tieneValidacion==1)
        {
            //pVH.radio_group.setClickable(false);

            pVH.radio_si.setFocusable(false);
            pVH.radio_no.setFocusable(false);
            pVH.radio_na.setFocusable(false);


            //pVH.radio_no.setClickable(false);
            //pVH.radio_na.setClickable(false);
        }



        System.out.println("\n  **********  [ SE CREA VIEWHOLDER [ "+i+" ], VALOR APROBADO -->  [" + valor_aprobado + "]");

        //System.out.println("\n  **********  [ INTO VIEWHOLDER [ "+i+" ], ID CRITERIO -->["+paleta.idCriterio+" ]  idECC [" + paleta.getIdEvaluacionCalidadCriterio() + "]");

        pVH.radio_group.check(valor_aprobado);


        if(paleta.tipoItem.equals("LUGAR"))
        {
            //pVH.tipo_item.setText("Lugar de verificación : ");
            pVH.tipo_item.setVisibility(View.GONE);
            pVH.nombre_lugar.setText(paleta.nombreLugarVerificacion);

            pVH.nombre_criterio.setVisibility(View.GONE);
            pVH.nombre_lugar.setVisibility(View.VISIBLE);
            pVH.radio_group.setVisibility(View.GONE);

            pVH.radio_si.setChecked(false);
            pVH.radio_no.setChecked(false);
            pVH.radio_na.setChecked(false);


            pVH.layout_botones.setVisibility(View.GONE);

        }


        if(paleta.tipoItem.equals("CRITERIO"))
        {
               //pVH.tipo_item.setText("Criterio : ");
               pVH.tipo_item.setVisibility(View.GONE);
               pVH.nombre_criterio.setText(paleta.nombreCriterio);

               pVH.nombre_lugar.setVisibility(View.GONE);
               pVH.nombre_criterio.setVisibility(View.VISIBLE);

                if(cerrado==0)
                {
                    pVH.layout_botones.setVisibility(View.GONE);
                    pVH.radio_group.setVisibility(View.VISIBLE);

                    if(paleta.habilitarNa==0)
                    {
                        pVH.radio_na.setVisibility(View.GONE);
                    }else{
                        pVH.radio_na.setVisibility(View.VISIBLE);
                    }

                }else {
                            pVH.layout_botones.setVisibility(View.VISIBLE);
                            pVH.radio_group.setVisibility(View.GONE);
                        }
        }




        if(valor_aprobado==0)
        {
            if (cerrado==0)
            {
                pVH.radio_si.setChecked(false);
                pVH.radio_no.setChecked(true);
                pVH.radio_na.setChecked(false);
            }else {
                pVH.boton_yes.setImageResource(R.drawable.ic_check_black_24dp);
                pVH.boton_no.setImageResource(R.drawable.ic_clear_black_24dp);

                pVH.boton_yes.setBackgroundColor(gris);
                pVH.boton_no.setBackgroundColor(rojo);
            }
        }

        if(valor_aprobado==1)
        {
            if(cerrado == 0)
            {
                pVH.radio_si.setChecked(true);
                pVH.radio_no.setChecked(false);
                pVH.radio_na.setChecked(false);
            }else {
                pVH.boton_yes.setImageResource(R.drawable.ic_check_black_24dp);
                pVH.boton_no.setImageResource(R.drawable.ic_clear_black_24dp);

                pVH.boton_yes.setBackgroundColor(primary);
                pVH.boton_no.setBackgroundColor(gris);
            }
        }


        if(valor_aprobado==2)
        {

            if(cerrado==0)
            {
                pVH.radio_si.setChecked(false);
                pVH.radio_no.setChecked(false);
                pVH.radio_na.setChecked(true);
            }else{
                pVH.boton_yes.setBackgroundColor(gris);
                pVH.boton_no.setBackgroundColor(gris);

                pVH.boton_yes.setImageResource(R.drawable.ic_remove_black_24dp);
                pVH.boton_no.setImageResource(R.drawable.ic_remove_black_24dp);
            }


        }


        if(valor_aprobado==-1)
        {
            pVH.radio_si.setChecked(false);
            pVH.radio_no.setChecked(false);
            pVH.radio_na.setChecked(false);
        }
        //pVH.radio_group.check(valor_aprobado);


        pVH.radio_si.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                if(paleta.tieneValidacion==1)
                {
                    pVH.radio_si.setChecked(false);
                    link.printConsola(" D E S CHECKEO RADIO SI");

                    link.printConsola("VALOR RESPUESTA AL DESCHECKEAR SI : [ "+paleta.respuesta+" ] ");
                    switch (paleta.respuesta)
                    {
                        case 0:
                            pVH.radio_no.setChecked(true);
                            link.printConsola(" CHECKEO RADIO NO");

                            break;
                        case 1:
                            pVH.radio_si.setChecked(true);
                            link.printConsola(" CHECKEO RADIO SI");

                            break;
                        case 2:
                            pVH.radio_na.setChecked(true);
                            link.printConsola(" CHECKEO RADIO N/A");

                            break;
                    }

                }else{

                            cambios=true;
                            ///link.printToast("CLICK YES BUTTON WITH CLICK", pVH.radio_si.getContext());
                            int id_evaluacion_c = paleta.idEvaluacionCalidad;
                            int id_indicador = paleta.idIndicador;
                            int id_criterio = paleta.idCriterio;

                            int pos = 0;

                            for (int i = 0; i < paletas.size(); i++)
                            {
                                if (id_indicador == paletas.get(i).idIndicador
                                        && id_criterio == paletas.get(i).idCriterio
                                        && id_evaluacion_c == paletas.get(i).idEvaluacionCalidad)
                                {
                                    pos = i;
                                }

                            }//FIN FOR RESPUESTAS DB Ó ACTUALES

                            //notifyItemChanged(i);
                            System.out.println("\n   POS [" + pos + "] SE ACTUALIZARÁ -->  RESPUESTA [ 0 ]");

                            paletas.set(pos, new DataEvaluadorCalidad(
                                                                            paletas.get(pos).idEvaluacionCalidad,
                                                                            "CRITERIO",
                                                                            paletas.get(pos).idIndicador,
                                                                            paletas.get(pos).idLugarVerificacion,
                                                                            paletas.get(pos).nombreLugarVerificacion,
                                                                            paletas.get(pos).idCriterio,
                                                                            paletas.get(pos).nombreCriterio,
                                                                            paletas.get(pos).habilitarNa,
                                                                            paletas.get(pos).tieneValidacion,
                                                                            1, ///respuesta si
                                                                            paletas.get(pos).idEvaluacionCalidadCriterio,
                                                                            paletas.get(pos).idEvaluacionCalidadRegistro,
                                                                            1
                                                                    ));
                            //notifyItemChanged(pos);

                            printRespuestas();
                            listener.avance_validacion();

                      }



            }
        });

        pVH.radio_no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(paleta.tieneValidacion==1)
                {
                    pVH.radio_no.setChecked(false);

                    switch (paleta.respuesta)
                    {
                        case 0:
                            pVH.radio_no.setChecked(true);

                            break;
                        case 1:
                            pVH.radio_si.setChecked(true);

                            break;
                        case 2:
                            pVH.radio_na.setChecked(true);

                            break;
                    }

                }else{

                        cambios=true;

                        int id_evaluacion_r = paleta.idEvaluacionCalidad;
                        int id_indicador = paleta.idIndicador;
                        int id_criterio = paleta.idCriterio;


                        int pos = 0;

                        for (int i = 0; i < paletas.size(); i++)
                        {
                            if (id_indicador == paletas.get(i).idIndicador
                                    && id_criterio == paletas.get(i).idCriterio
                                    && id_evaluacion_r == paletas.get(i).idEvaluacionCalidad)
                            {
                                pos = i;
                            }

                        }//FIN FOR RESPUESTAS DB Ó ACTUALES

                        //notifyItemChanged(i);

                        System.out.println("\n   POS [" + pos + "] SE ACTUALIZARÁ -->  RESPUESTA [ 1 ]");

                        paletas.set(pos, new DataEvaluadorCalidad(

                                paletas.get(pos).idEvaluacionCalidad,
                                "CRITERIO",
                                paletas.get(pos).idIndicador,
                                paletas.get(pos).idLugarVerificacion,
                                paletas.get(pos).nombreLugarVerificacion,
                                paletas.get(pos).idCriterio,
                                paletas.get(pos).nombreCriterio,
                                paletas.get(pos).habilitarNa,
                                paletas.get(pos).tieneValidacion,
                                0, ///resouesta no
                                paletas.get(pos).idEvaluacionCalidadCriterio,
                                paletas.get(pos).idEvaluacionCalidadRegistro,
                                1

                        ));
                        //notifyItemChanged(pos);


                        printRespuestas();

                        listener.avance_validacion();

                     }


            }
        });


        pVH.radio_na.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(paleta.tieneValidacion==1)
                 {
                    pVH.radio_na.setChecked(false);

                     switch (paleta.respuesta)
                     {
                         case 0:
                             pVH.radio_no.setChecked(true);

                             break;
                         case 1:

                             pVH.radio_si.setChecked(true);

                             break;
                         case 2:
                             pVH.radio_na.setChecked(true);

                             break;
                     }

                 }else{

                        cambios=true;

                        int id_evaluacion_r = paleta.idEvaluacionCalidad;
                        int id_indicador = paleta.idIndicador;
                        int id_criterio = paleta.idCriterio;


                        int pos = 0;

                        for (int i = 0; i < paletas.size(); i++)
                        {
                            if (    id_indicador == paletas.get(i).idIndicador
                                    && id_criterio == paletas.get(i).idCriterio
                                    && id_evaluacion_r == paletas.get(i).idEvaluacionCalidad)
                            {
                                pos = i;
                            }

                        }//FIN FOR RESPUESTAS DB Ó ACTUALES


                        System.out.println("\n   POS [" + pos + "] SE ACTUALIZARÁ -->  RESPUESTA [ 2 ]");
                        paletas.set(pos, new DataEvaluadorCalidad(

                                paletas.get(pos).idEvaluacionCalidad,
                                "CRITERIO",
                                paletas.get(pos).idIndicador,
                                paletas.get(pos).idLugarVerificacion,
                                paletas.get(pos).nombreLugarVerificacion,
                                paletas.get(pos).idCriterio,
                                paletas.get(pos).nombreCriterio,
                                paletas.get(pos).habilitarNa,
                                paletas.get(pos).tieneValidacion,
                                2,
                                paletas.get(pos).idEvaluacionCalidadCriterio,
                                paletas.get(pos).idEvaluacionCalidadRegistro,
                                1

                        ));



                        printRespuestas();

                        listener.avance_validacion();

                     }

            }
        });



        pVH.radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                //link.printToast("CAMBIO DE VALOR DETECTADO -- " + paleta.nombre_criterio, group.getContext());
            }
        });





        /*
        pVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        mAdapterCallback.onMethodCallback();
                    } catch (ClassCastException exception) {
                    // do something
                }
            }
        });
        */



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

        int array []={ preguntas,respondidas,aprobadas,negativas,nas,changes };

        return array;
    }


    public void printRespuestas()
    {
        System.out.println("\n ------ INICIO IMPRESION RESPUESTAS LIVE :   -------->  ");

        for(int j=0; j<paletas.size();j++)
        {
            System.out.println( "ID EVAL. CALIDAD : "+paletas.get(j).idEvaluacionCalidad+
                            ", ID INDICADOR: "+paletas.get(j).idIndicador+
                            ", ID CRITERIO : ["+paletas.get(j).idCriterio+"], ID ECalCri : ["+ paletas.get(j).idEvaluacionCalidadCriterio+"]," +
                            "  VALUE --> ["+paletas.get(j).respuesta+"]  --> MODIFICADO : [ "+paletas.get(j).modificado+" ]");

        }
        System.out.println("\n <----- FIN IMPRESION RESPUESTAS LIVE -----");





    }


    public void updateDataRecycler(List<DataEvaluadorCalidad> new_preguntas, List<CriterioValidacionRespuesta> respuestas_validacion,int cerrado)
    {
        this.paletas.clear();
        this.paletas.addAll(new_preguntas);

        this.respuestas_validacion.clear();
        this.respuestas_validacion.addAll(respuestas_validacion);

        this.cerrado = cerrado;

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



 class ViewHolderEC  extends RecyclerView.ViewHolder
{

    protected TextView nombre_lugar;
    protected TextView nombre_criterio;
    protected TextView tipo_item;

    //protected CardView card;

    protected RadioGroup radio_group;
    protected RadioButton radio_si;
    protected RadioButton radio_no;
    protected RadioButton radio_na;

    protected LinearLayout layout_botones;
    protected ImageButton boton_yes;
    protected ImageButton boton_no;


    public ViewHolderEC(View itemView)
    {
        super(itemView);

        nombre_lugar = (TextView) itemView.findViewById(R.id.nombre_lugar);
        nombre_criterio = (TextView) itemView.findViewById(R.id.nombre_criterio);

        tipo_item = (TextView) itemView.findViewById(R.id.tipo_item);

        radio_group = (RadioGroup) itemView.findViewById(R.id.radio_group);
        radio_si = (RadioButton) itemView.findViewById(R.id.si);
        radio_no = (RadioButton) itemView.findViewById(R.id.no);
        radio_na = (RadioButton) itemView.findViewById(R.id.na);

        layout_botones = (LinearLayout) itemView.findViewById(R.id.layout_botones_cerrado);
        boton_yes = (ImageButton) itemView.findViewById(R.id.imageButtonYes);
        boton_no = (ImageButton) itemView.findViewById(R.id.imageButtonNo);

        //card = (CardView) itemView;
    }

}


interface InterfaceCalidad {

    public void avance_validacion();

}
