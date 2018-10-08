package code.yoursoft.ciummovil;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragSync extends Fragment {

    AnimationDrawable animation = null;

    ImageButton boton_actualizar_clues;
    ImageButton boton_enviar_evaluaciones;
    ImageButton boton_sinc_evaluaciones_recurso;
    ImageButton boton_sinc_evaluaciones_calidad;
    private ProgressDialog dialog_descarga;


    TextView text_status;
    TextView text_ultima_actualizacion_clues;

    TextView text_conteo_calidad;
    TextView text_fecha_calidad;

    TextView text_conteo_recursos;
    TextView text_fecha_recursos;







    private CoordinatorLayout coordinatorLayout;
    private RelativeLayout layout_loading;

    ProgressBar progress_bar_clues;
    ProgressBar progress_bar_recursos;
    ProgressBar progress_bar_calidad;

    private String json_rs_clues;
    private String json_rs_cone;
    private String json_rs_indicador;
    private String json_rs_criterio;
    private String json_rs_indicador_criterio;
    private String json_rs_cone_indicador_criterio;
    private String json_rs_lugar_verificacion;
    private String json_rs_accion;


    private Usuarios user_signed;
    private DBManager linkDB;
    private Funciones link;


    private boolean  mostrar_dialog;

    AlertDialog.Builder dialog;
    Button boton_ok;
    Context context;

    ProgressDialog progress_sync;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        final View rootView = inflater.inflate(R.layout.layout_sync, container, false);

        setHasOptionsMenu(true);

        linkDB=new DBManager(getActivity());
        link=new Funciones();
        user_signed=linkDB.getSignedUser();

        if(user_signed==null)
        {
            link.goLogin(getActivity());
        }

        context = getActivity();

        boton_actualizar_clues = (ImageButton) rootView.findViewById(R.id.boton_actualizar_clues);
        boton_sinc_evaluaciones_recurso = (ImageButton) rootView.findViewById(R.id.boton_sincronizar_evaluaciones_recurso);
        boton_sinc_evaluaciones_calidad = (ImageButton) rootView.findViewById(R.id.boton_sincronizar_evaluaciones_calidad);



        dialog_descarga = new ProgressDialog(getActivity());
        dialog_descarga.setMessage("Actualizando datos, espere...");
        dialog_descarga.setTitle("Actualizacion");
        dialog_descarga.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog_descarga.setCancelable(false);


        progress_bar_clues = (ProgressBar) rootView.findViewById(R.id.progress_bar_clues);
        progress_bar_recursos = (ProgressBar) rootView.findViewById(R.id.progress_bar_recursos);
        progress_bar_calidad = (ProgressBar) rootView.findViewById(R.id.progress_bar_calidad);

        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);

        layout_loading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);
        layout_loading.setVisibility(View.GONE);


        Resources res = getResources();
        int verde = res.getColor(R.color.PrimaryColor);



        text_status = (TextView) rootView.findViewById(R.id.text_status);
        text_ultima_actualizacion_clues = (TextView) rootView.findViewById(R.id.text_ultima_actualiacion_clues);
        text_conteo_recursos = (TextView) rootView.findViewById(R.id.text_conteo_recursos);
        text_fecha_recursos = (TextView) rootView.findViewById(R.id.text_fecha_recursos);

        text_conteo_calidad = (TextView) rootView.findViewById(R.id.text_conteo_calidad);
        text_fecha_calidad = (TextView) rootView.findViewById(R.id.text_fecha_calidad);








        try{
              String bandera = this.getArguments().getString("BANDERA");
              if(bandera.equals("NUEVO"))
              {
                  dialog = new AlertDialog.Builder(context, R.style.TemaDialogMaterial);

                  LayoutInflater inflater_alert = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                  View view = inflater_alert.inflate(R.layout.layout_mensaje_actualizar, null);
                  dialog.setView(view);

                  dialog.setTitle("Bienvenido").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which)
                      {
                          dialog.cancel();
                          dialog.dismiss();
                      }
                  });



                  dialog.show();
              }

           }catch (NullPointerException err){}




        boton_actualizar_clues.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ///*************************************************************************************************
                AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() ,R.style.TemaDialogMaterial);

                builder.setCancelable(false);
                builder.setMessage("¿ Esta seguro que desea actualizar los cátalogos ?")
                        .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                morphingBoton("CLUES", "DESCARGANDO");
                                desactivarBotones();

                                new ActualizarCatalogos().execute();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }).show();
                ///*************************************************************************************************






            }
        });


        boton_sinc_evaluaciones_recurso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                ///*************************************************************************************************
                AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(),R.style.TemaDialogMaterial );

                builder.setCancelable(false);
                builder.setMessage("¿ Confirma el envio de evaluaciones de Recursos ?")
                        .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                morphingBoton("RECURSOS", "ENVIANDO");
                                desactivarBotones();

                                new postEvaluacionesRecurso().execute();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }).show();
                ///*************************************************************************************************



            }
        });


        boton_sinc_evaluaciones_calidad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ///*************************************************************************************************
                AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(),R.style.TemaDialogMaterial );

                builder.setCancelable(false);
                builder.setMessage("¿ Confirma el envio de evaluaciones de Calidad ?")
                        .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                morphingBoton("CALIDAD", "ENVIANDO");
                                desactivarBotones();

                                new postEvaluacionesCalidad().execute();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }).show();
                ///*************************************************************************************************


            }
        });


        setConteos();


        return rootView;
    }






    ///***********************************************************************************************

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_sincronizacion, menu);
        super.onCreateOptionsMenu(menu,inflater);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.resincronizar_recursos)
        {
            ///*************************************************************************************************
            AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(),R.style.TemaDialogMaterial );

            builder.setCancelable(false);
            builder.setMessage("¿ Confirma que desea resincronizar las evaluaciones de Recursos ?")
                    .setPositiveButton("Si",  new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            //morphingBoton("RECURSOS", "ENVIANDO");
                            //desactivarBotones();

                            new rePostEvaluacionesRecurso().execute();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    }).show();
            ///*************************************************************************************************
        }


        if (id == R.id.resincronizar_procesos)
        {
            ///*************************************************************************************************
            AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(),R.style.TemaDialogMaterial );

            builder.setCancelable(false);
            builder.setMessage("¿ Confirma que desea resincronizar las evaluaciones de Calidad ?")
                    .setPositiveButton("Si",  new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            //morphingBoton("CALIDAD", "ENVIANDO");
                            ///desactivarBotones();

                            new rePostEvaluacionesCalidad().execute();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    }).show();
            ///*************************************************************************************************
        }


        return super.onOptionsItemSelected(item);
    }
































    public void setConteos()
    {
        int total_recursos=0;
        int sincronizadas_recursos=0;

        int total_calidad=0;
        int sincronizadas_calidad=0;

        String ultima_actualizacion_clues="";
        String ultima_actualizacion_recursos="";
        String ultima_actualizacion_calidad="";

        List<Evaluacion> lista_recursos = linkDB.getEvaluaciones("RECURSO","",user_signed.id);

        total_recursos = lista_recursos.size();

        for(int i=0; i < lista_recursos.size(); i++)
        {
            Evaluacion er_temp = lista_recursos.get(i);
            if(er_temp.getSincronizado() > 0)
            {
                sincronizadas_recursos++;
            }

        }



        List<Evaluacion> lista_calidad = linkDB.getEvaluaciones("CALIDAD","",user_signed.id);

        total_calidad = lista_calidad.size();

        for(int j=0; j < lista_calidad.size(); j++)
        {
            Evaluacion ec_temp = lista_calidad.get(j);

            if(ec_temp.getSincronizado() > 0)
            {
                sincronizadas_calidad++;
            }
        }

        Config config_clues = linkDB.getConfig("CLUES",user_signed.id);
        if(config_clues != null)
        {
            ultima_actualizacion_clues=config_clues.fechaAccion;

        }else{

             }



        Config config_recursos = linkDB.getConfig("RECURSOS",user_signed.id);
        if(config_recursos != null)
        {
            ultima_actualizacion_recursos=config_recursos.fechaAccion;

        }else{

             }




        Config config_calidad = linkDB.getConfig("CALIDAD",user_signed.id);
        if(config_calidad != null)
        {
            ultima_actualizacion_calidad=config_calidad.fechaAccion;
        }else{

             }


        text_ultima_actualizacion_clues.setText("Ult. sinc: "+link.imprimirFecha( ultima_actualizacion_clues ));

        text_conteo_recursos.setText("Sincronizadas: "+sincronizadas_recursos+".\nNo Sincronizadas: "+(total_recursos-sincronizadas_recursos));
        text_fecha_recursos.setText("Ult. sinc: "+link.imprimirFecha( ultima_actualizacion_recursos ));


        text_conteo_calidad.setText("Sincronizadas: "+sincronizadas_calidad+".\nNo Sincronizadas: "+(total_calidad-sincronizadas_calidad));
        text_fecha_calidad.setText("Ult. sinc: "+link.imprimirFecha( ultima_actualizacion_calidad ));


    }




    private class ActualizarCatalogos extends AsyncTask<String, Integer, JSONObject> {

        Funciones link = new Funciones();
        JSONObject json_status = new JSONObject();

        int errors_update=0;
        String messages_errors="";


        protected JSONObject doInBackground(String... datos)
        {


            try {

                json_status.put("errors",errors_update);
                json_status.put("messages", "Actualización de catalogos correcta.");


                 //dialog_descarga.setMessage("Descargando Clues...");
                 json_rs_clues = link.getCatalogo("Clues", "GET" , "",user_signed.email,user_signed.accessToken );

                if(json_rs_clues!=null)
                {
                   JSONObject jso_clues=new JSONObject(json_rs_clues);

                    System.out.println("JSON CLUES RECIVED: " + json_rs_clues);

                   int total=jso_clues.getInt("total");

                   JSONArray array_data_clues = jso_clues.getJSONArray("data");
                    Clues clues;
                    SQLiteDatabase db;
                    DBManager linkDB;


                    linkDB= new DBManager(getActivity());
                    db=linkDB.openDB();
                    linkDB.resetTableDB("Clues");


                   for(int i=0;i<total;i++)
                    {
                        JSONObject item_data_clues=array_data_clues.getJSONObject(i);

                        boolean insert=true;

                        try {


                            JSONObject cone = item_data_clues.getJSONObject("cone_clues");
                            insert =true;

                            clues=new Clues(item_data_clues.getString("clues"),
                                    item_data_clues.getString("nombre"),
                                    item_data_clues.getString("domicilio"),
                                    item_data_clues.getString("codigoPostal"),
                                    item_data_clues.getString("entidad"),
                                    item_data_clues.getString("municipio"),
                                    item_data_clues.getString("localidad"),
                                    item_data_clues.getString("jurisdiccion"),
                                    item_data_clues.getString("claveJurisdiccion"),
                                    item_data_clues.getString("institucion"),
                                    item_data_clues.getString("tipoUnidad"),
                                    item_data_clues.getString("estatus"),
                                    item_data_clues.getString("estado"),
                                    item_data_clues.getString("tipologia"),
                                    item_data_clues.getString("cone"),
                                    item_data_clues.getString("latitud"),
                                    item_data_clues.getString("longitud"),
                                    cone.getInt("idCone"));


                            linkDB.insertarClues(clues, db);
                            System.out.println("Insertando registro de clues con CONE");

                           }catch (JSONException err){

                                                        insert=false;
                            System.out.println("Omitiendo registro de clues Sin CONE    X:");

                                                    }



                    }
                    db.close();



                }else{
                            errors_update++;
                            messages_errors+="Error en descarga de Clues";

                            //HACER UNA FUNCION QUE EXTRAIGA EL ERROR DEL HEADER RESPONSE
                            // CUANDO NO SE PUEDA PARSEAR EL DATA ARRAY PORQUE LA PETICION FUÉ INCORRECTA.

                            json_status.put("errors",errors_update);
                            json_status.put("messages",messages_errors);

                            dialog_descarga.dismiss();
                     }




                 //dialog_descarga.setMessage("Descargando Cones...");
                 //cambiarTituloDialog("Descargando Cones...");
                 json_rs_cone = link.getCatalogo("Cone", "GET" , "",user_signed.email,user_signed.accessToken );

                if(json_rs_cone!=null)
                {
                    JSONObject jso_cone=new JSONObject(json_rs_cone);

                    System.out.println("JSON CONE RECIVED: " + json_rs_cone);

                    int total=jso_cone.getInt("total");

                    JSONArray array_data_cone = jso_cone.getJSONArray("data");
                    Cone cone;
                    SQLiteDatabase db;
                    DBManager linkDB;


                    linkDB= new DBManager(getActivity());
                    db=linkDB.openDB();
                    linkDB.resetTableDB("Cone");


                    for(int i=0;i<array_data_cone.length();i++)
                    {
                        JSONObject item_data_cone=array_data_cone.getJSONObject(i);

                        cone=new Cone(item_data_cone.getInt("id"),
                                item_data_cone.getString("nombre") );

                        linkDB.insertarCone(cone, db);

                    }
                    db.close();



                }else{
                    errors_update++;
                    messages_errors+="Error en descarga de Cones";

                    json_status.put("errors",errors_update);
                    json_status.put("messages",messages_errors);

                    dialog_descarga.dismiss();
                }



                 //dialog_descarga.setMessage("Descargando Indicadores...");
                 json_rs_indicador = link.getCatalogo("Indicador", "GET" ,"",user_signed.email,user_signed.accessToken );



                if(json_rs_indicador!=null)
                {
                    JSONObject jso_indicador=new JSONObject(json_rs_indicador);

                    System.out.println("JSON INDICADOR RECIVED: " + json_rs_indicador);

                    int total = jso_indicador.getInt("total");

                    JSONArray array_data_indicador = jso_indicador.getJSONArray("data");
                    Indicador indicador;
                    SQLiteDatabase db;
                    DBManager linkDB;


                    linkDB= new DBManager(getActivity());
                    db=linkDB.openDB();
                    linkDB.resetTableDB("Indicador");

                    linkDB.resetTableDB("IndicadorValidacion");
                    linkDB.resetTableDB("IndicadorPregunta");
                    linkDB.resetTableDB("CriterioValidacion");
                    linkDB.resetTableDB("CriterioPregunta");


                    for(int i=0;i<array_data_indicador.length();i++)
                    {
                        JSONObject item_data_indicador=array_data_indicador.getJSONObject(i);

                        indicador=new Indicador(item_data_indicador.getInt("id"),
                                                item_data_indicador.getString("codigo"),
                                                item_data_indicador.getString("nombre"),
                                                item_data_indicador.getString("color"),
                                                item_data_indicador.getString("categoria"),
                                                item_data_indicador.getString("indicacion")
                                                );

                        linkDB.insertarIndicador(indicador, db);



                        JSONArray array_validaciones = item_data_indicador.getJSONArray("indicador_validaciones");


                        System.out.println("ARRAY VALIDACIONES SIZE : -->>>>>>>>>>>>>>>>><<<<<>>> : " + array_validaciones.length()+".");

                        for(int j=0;  j<array_validaciones.length(); j++)
                        {
                            JSONObject item_validacion = array_validaciones.getJSONObject(j);
                            System.out.println("ARRAY VALIDACIONES POSICION : -->>>>>>>>>>>>>>>>><<<<<>>> : " +j+".");

                            IndicadorValidacion validacion = new IndicadorValidacion( item_validacion.getInt("id"),
                                                                    item_validacion.getInt("idIndicador"),
                                                                    item_validacion.getString("pregunta1").toString(),
                                                                    item_validacion.getString("operadorAritmetico").toString(),
                                                                    item_validacion.getString("pregunta2").toString(),
                                                                    item_validacion.getString("unidadMedida").toString(),
                                                                    item_validacion.getString("operadorLogico").toString(),
                                                                    item_validacion.getString("valorComparativo").toString(),
                                                                    item_validacion.getString("creadoAl").toString(),
                                                                    item_validacion.getString("modificadoAl").toString(),
                                                                    "0000-00-00 00:00:00" );

                            linkDB.insertarIndicadorValidacion(validacion);
                        }


                        JSONArray array_preguntas = item_data_indicador.getJSONArray("indicador_preguntas");


                        System.out.println("ARRAY VALIDACIONES SIZE : -->>>>>>>>>>>>>>>>><<<<<>>> : " + array_validaciones.length()+".");

                        for(int j=0;  j<array_preguntas.length(); j++)
                        {
                            JSONObject item_pregunta = array_preguntas.getJSONObject(j);

                            IndicadorPregunta pregunta = new IndicadorPregunta( item_pregunta.getString("id"),
                                                                                item_pregunta.getInt("idIndicador"),
                                                                                item_pregunta.getString("nombre"),
                                                                                item_pregunta.getString("tipo"),
                                                                                item_pregunta.getInt("constante"),
                                                                                item_pregunta.getString("valorConstante"),
                                                                                item_pregunta.getInt("fechaSistema"),
                                                                                item_pregunta.getString("creadoAl"),
                                                                                item_pregunta.getString("modificadoAl"),
                                                                                 "0000-00-00 00:00:00"  );

                            linkDB.insertarIndicadorPregunta(pregunta,db);
                        }



                    }

                    db.close();

                }else{
                            errors_update++;
                            messages_errors+="Error en descarga de Indicadores";

                            json_status.put("errors",errors_update);
                            json_status.put("messages",messages_errors);

                            dialog_descarga.dismiss();
                     }





                 //dialog_descarga.setMessage("Descargando Criterios...");
                json_rs_criterio = link.getCatalogo("Criterio", "GET" , "",user_signed.email,user_signed.accessToken );

                if(json_rs_criterio!=null)
                {
                    JSONObject jso_criterio=new JSONObject(json_rs_criterio);

                    System.out.println("JSON CRITERIO RECIVED: " + json_rs_criterio);

                    int total=jso_criterio.getInt("total");

                    JSONArray array_data_criterio = jso_criterio.getJSONArray("data");
                    Criterio criterio;
                    SQLiteDatabase db;
                    DBManager linkDB;


                    linkDB= new DBManager(getActivity());
                    db=linkDB.openDB();
                    linkDB.resetTableDB("Criterio");
                    linkDB.resetTableDB("IndicadorCriterio");
                    linkDB.resetTableDB("ConeIndicadorCriterio");

                    linkDB.resetTableDB("CriterioValidacion");
                    linkDB.resetTableDB("CriterioPregunta");

                    ///******************************************** C R I T E R I O S **************
                    ///*****************************************************************************


                    for(int i=0;i<array_data_criterio.length();i++)
                    {
                        JSONObject item_data_criterio=array_data_criterio.getJSONObject(i);

                        int idCriterio=item_data_criterio.getInt("id");
                        int hna=0;
                        int tv=0;
                        String orden="";

                        link.printConsola("---------------------->>>>  ITEM CRITERIO ------>>  "+item_data_criterio.toString());

                        try {
                               hna = item_data_criterio.getInt("habilitarNoAplica");
                            }catch (NumberFormatException err){hna=0;}

                        try {
                              tv = item_data_criterio.getInt("tieneValidacion");
                            }catch (NumberFormatException err){tv=0;}

                        try {
                                orden = item_data_criterio.getString("orden");
                                link.printConsola("ORDEN ===== >>"+orden+"   ]");
                            }catch (NullPointerException err){ orden ="0"; }

                        criterio=new Criterio(idCriterio,
                                              item_data_criterio.getString("nombre"),
                                              hna,
                                              tv,
                                              orden
                                             );

                        linkDB.insertarCriterio(criterio, db);


                        ///************************************ I N D I C A D O R E S **************
                        ///*************************************************************************

                        JSONArray array_data_indicadores = item_data_criterio.getJSONArray("indicadores");
                        for(int j=0;j<array_data_indicadores.length();j++)
                        {

                            JSONObject item_data_indicador=array_data_indicadores.getJSONObject(j);

                            int idIndicador=item_data_indicador.getInt("id");

                            //SACAR OBJETO LUGAR-VERIFICACION PARA ARMAR LOS 3 CAMPOS DE INDICADOR-CRITERIO
                            //   idCriterio    idIndicador    idLugarVerificacion

                            JSONObject lugar_verificacion=item_data_indicador.getJSONObject("lugarVerificacion");

                            int idLugarVerificacion=lugar_verificacion.getInt("id");



                            int idIndicadorCriterio=0;
                            JSONArray array_data_cones = item_data_indicador.getJSONArray("cones");

                            for(int k=0;k<array_data_cones.length();k++)
                            {
                                JSONObject item_data_cone=array_data_cones.getJSONObject(k);

                                int idCone=item_data_cone.getInt("id");
                                idIndicadorCriterio=item_data_cone.getInt("idIndicadorCriterio");

                                ///INSERTAR AQUI ConeIndicadorCriterio
                                ConeIndicadorCriterio cic = new ConeIndicadorCriterio(0,
                                                                                      idCone,
                                                                                       idIndicadorCriterio);
                                linkDB.insertarConeIndicadorCriterio(cic, db);

                            }//FIN FOR RECORRER CONES



                            JSONObject pivot_json= item_data_indicador.getJSONObject("pivot");

                            int idIndicadorCriterio_ = pivot_json.getInt("id");
                            int idCriterio_ = pivot_json.getInt("idCriterio");
                            int idIndicador_ = pivot_json.getInt("idIndicador");
                            int idLugarVerificacion_ = pivot_json.getInt("idLugarVerificacion");

                            IndicadorCriterio indicadorCriterio=new IndicadorCriterio(
                                                                                        idIndicadorCriterio_,
                                                                                        idCriterio_,
                                                                                        idIndicador_,
                                                                                        idLugarVerificacion_);
                            linkDB.insertarIndicadorCriterio(indicadorCriterio,db);




                            //inserción en tabla IndicadorCriterio
                            /*
                            IndicadorCriterio indicadorCriterio=new IndicadorCriterio(
                                                                                        idIndicadorCriterio,
                                                                                        idCriterio,
                                                                                        idIndicador,
                                                                                        idLugarVerificacion);
                            linkDB.insertarIndicadorCriterio(indicadorCriterio,db);
                            */



                        }//FIN FOR RECORRIDO ARRAY INDICADORES



                        JSONArray array_validaciones = item_data_criterio.getJSONArray("criterio_validaciones");

                        for(int x=0;  x<array_validaciones.length(); x++)
                        {
                            JSONObject item_validacion = array_validaciones.getJSONObject(x);

                            CriterioValidacion validacion = new CriterioValidacion( item_validacion.getInt("id"),
                                                                                        item_validacion.getInt("idCriterio"),
                                                                                        item_validacion.getString("pregunta1").toString(),
                                                                                        item_validacion.getString("operadorAritmetico").toString(),
                                                                                        item_validacion.getString("pregunta2").toString(),
                                                                                        item_validacion.getString("unidadMedida").toString(),
                                                                                        item_validacion.getString("operadorLogico").toString(),
                                                                                        item_validacion.getString("valorComparativo").toString(),
                                                                                        item_validacion.getString("creadoAl").toString(),
                                                                                        item_validacion.getString("modificadoAl").toString(),
                                                                                        "0000-00-00 00:00:00" );

                            linkDB.insertarCriterioValidacion(validacion);
                        }



                        JSONArray array_preguntas = item_data_criterio.getJSONArray("criterio_preguntas");

                        for(int y=0;  y<array_preguntas.length(); y++)
                        {
                            JSONObject item_pregunta = array_preguntas.getJSONObject(y);

                            CriterioPregunta pregunta = new CriterioPregunta( item_pregunta.getString("id"),
                                                                                item_pregunta.getInt("idCriterio"),
                                                                                item_pregunta.getString("nombre"),
                                                                                item_pregunta.getString("tipo"),
                                                                                item_pregunta.getInt("constante"),
                                                                                item_pregunta.getString("valorConstante"),
                                                                                item_pregunta.getInt("fechaSistema"),
                                                                                item_pregunta.getString("creadoAl"),
                                                                                item_pregunta.getString("modificadoAl"),
                                                                                "0000-00-00 00:00:00" );

                            linkDB.insertarCriterioPregunta(pregunta,db);
                        }




















                    }//FIN FOR ARRAY CRITERIOS

                    db.close();



                }else{
                    errors_update++;
                    messages_errors+="Error en descarga de Criterios";

                    json_status.put("errors",errors_update);
                    json_status.put("messages",messages_errors);

                    dialog_descarga.dismiss();
                }




                 //dialog_descarga.setMessage("Descargando Indicadores - Criterios...");
                 json_rs_lugar_verificacion = link.getCatalogo("LugarVerificacion", "GET" , "",user_signed.email,user_signed.accessToken );


                if(json_rs_lugar_verificacion!=null)
                {
                    JSONObject jso_lugar_verificacion=new JSONObject(json_rs_lugar_verificacion);

                    System.out.println("JSON LUGAR VERIFICACION CRITERIO RECIVED: " + json_rs_lugar_verificacion);

                    int total=jso_lugar_verificacion.getInt("total");

                    JSONArray array_data_lugar_verificacion = jso_lugar_verificacion.getJSONArray("data");
                    LugarVerificacion lugarVerificacion;
                    SQLiteDatabase db;
                    DBManager linkDB;


                    linkDB= new DBManager(getActivity());
                    db=linkDB.openDB();
                    linkDB.resetTableDB("LugarVerificacion");


                    for(int i=0;i<array_data_lugar_verificacion.length();i++)
                    {
                        JSONObject item_data_lugar_verificacion=array_data_lugar_verificacion.getJSONObject(i);

                        lugarVerificacion=new LugarVerificacion(
                                item_data_lugar_verificacion.getInt("id"),
                                item_data_lugar_verificacion.getString("nombre")
                        );

                        linkDB.insertarLugarVerificacion(lugarVerificacion, db);

                    }
                    db.close();



                }else{
                    errors_update++;
                    messages_errors+="Error en descarga de Lugares de verificación ";

                    json_status.put("errors",errors_update);
                    json_status.put("messages",messages_errors);

                    dialog_descarga.dismiss();
                }

                //dialog_descarga.setMessage("Descargando Indicadores - Criterios...");
                json_rs_accion = link.getCatalogo("Accion", "GET" , "",user_signed.email,user_signed.accessToken );


                if(json_rs_accion!=null)
                {
                    JSONObject jso_accion=new JSONObject(json_rs_accion);

                    System.out.println("JSON ACCION RECIVED: " + json_rs_accion);

                    int total=jso_accion.getInt("total");

                    JSONArray array_data_accion = jso_accion.getJSONArray("data");
                    Accion accion;
                    SQLiteDatabase db;
                    DBManager linkDB;


                    linkDB= new DBManager(getActivity());
                    db=linkDB.openDB();
                    linkDB.resetTableDB("Accion");


                    for(int i=0;i<array_data_accion.length();i++)
                    {
                        JSONObject item_data_accion=array_data_accion.getJSONObject(i);

                        accion=new Accion(
                                item_data_accion.getInt("id"),
                                item_data_accion.getString("nombre"),
                                item_data_accion.getString("tipo"),
                                item_data_accion.getString("creadoAl"),
                                item_data_accion.getString("modificadoAl"),
                                item_data_accion.getString("borradoAl")
                        );

                        linkDB.insertarAccion(accion, db);

                    }
                    db.close();



                }else{
                    errors_update++;
                    messages_errors+="Error en descarga de Acciones ";

                    json_status.put("errors",errors_update);
                    json_status.put("messages", messages_errors);

                    dialog_descarga.dismiss();
                    //layout_loading.setVisibility(View.GONE);
                }


                }catch(Exception e)
                    {
                         e.printStackTrace();
                         errors_update++;
                         messages_errors+="Error critico de Actualización ";
                         dialog_descarga.dismiss();
                    }

            // publishProgress(i * 10);
            return json_status;
        }

        protected void onProgressUpdate(Integer... values)
         {
            //int progreso = values[0].intValue();
            //dialog_descarga.setProgress(progreso);
         }

        protected void onPreExecute()
        {
            layout_loading.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(JSONObject status)
        {



            morphingBoton("CLUES", "NORMAL");
            activarBotones();




            int errors=0;
            String messages="";
            String status_config="";

                try {
                          errors=status.getInt("errors");
                          messages=status.getString("messages");

                          if(errors>0)
                          {
                              status_config="ERROR";
                              link.showSnackBar(getActivity(), coordinatorLayout, messages, "ERROR");
                              layout_loading.setVisibility(View.GONE);
                          }else{
                                  status_config="OK";
                                  link.showSnackBar(getActivity(),coordinatorLayout,messages,"INFO");
                                  layout_loading.setVisibility(View.GONE);
                               }

                    } catch (JSONException e)
                                        {

                                            e.printStackTrace();
                                            link.showSnackBar(getActivity(), coordinatorLayout, "Error leyendo respuestas.", "ERROR");
                                            layout_loading.setVisibility(View.GONE);
                                        }




            Config config = linkDB.getConfig("CLUES",user_signed.id);

            if(config != null)
            {
                config.setFechaAccion(link.getFecha());
                config.setModificadoAl(link.getFecha());
                config.setStatus(status_config);

                linkDB.actualizarConfig(config);
            }else{
                        Config new_config = new Config(
                                                        0,
                                                        "CLUES",
                                                        user_signed.id,
                                                        link.getFecha(),
                                                        link.getFecha(),
                                                        status_config,
                                                        "0000-00-00 00:00:00",
                                                        "0000-00-00 00:00:00"
                                                        );

                        linkDB.insertarConfig(new_config);

                 }


            setConteos();
        }

        protected void onCancelled(String mensaje)
        {
            link.showSnackBar(getActivity(), coordinatorLayout, "Actualizacion cancelada.", "ERROR");
        }
    }///FIN CLASS DESCARGA ASYNC













///// ***************************************************************************************************************
///// ***************************************************************************************************************

    /////    ENVIAR EVALUACIONES NUEVAS AL SERVIDOR        P O S T     R E C U R S O S

///// ***************************************************************************************************************
///// ***************************************************************************************************************


    private class postEvaluacionesRecurso extends AsyncTask<String, String [], String []>
    {
        protected String [] doInBackground(String... datos)
        {
            int errors=0;
            String messages="";

            List<Evaluacion> EVALUACIONES = new ArrayList<>();

            //EVALUACIONES = linkDB.getEvaluaciones("RECURSO","",user_signed.getId());
            EVALUACIONES = linkDB.getEvaluacionesParaPost( "RECURSO","",user_signed.getId() );

            JSONObject json_main_post=new JSONObject();
            JSONArray array_evaluaciones_post=new JSONArray();


            if(EVALUACIONES.size() > 0)
            {


                for (int z = 0; z < EVALUACIONES.size(); z++)
                {

                    Evaluacion evaluacion = EVALUACIONES.get(z);

                    JSONObject evaluacion_json = new JSONObject();
                    JSONArray array_criterios = new JSONArray();
                    JSONObject criterio_json;

                    JSONArray array_hallazgos = new JSONArray();
                    JSONObject hallazgo_json;


                    int nivel_cone = linkDB.getNivelCone(evaluacion.clues);


                    List<Indicador> indicadores = new ArrayList<>();
                    List<LugarVerificacion> listaLV = new ArrayList<>();

                    indicadores = linkDB.getIndicadoresAgregadosRecurso(evaluacion.id);

                    for (int i = 0; i < indicadores.size(); i++) {

                        int id_indicador = indicadores.get(i).getId();

                        List<EvaluacionRecursoCriterio> listaERC = new ArrayList<>();
                        listaERC = linkDB.getEvaluacionRecursoCriterio(evaluacion.id, id_indicador);

                        System.out.print("EVALUACION : " + evaluacion.id + ", INDICADOR :" + id_indicador + ".");
                        System.out.print("( " + "CRITERIOS ENCONTRADOS PARA INDICADOR :" + id_indicador + " : " + listaERC.size() + " )");

                        for (int j = 0; j < listaERC.size(); j++) {
                            EvaluacionRecursoCriterio eval = listaERC.get(j);

                            if (eval != null) {
                                criterio_json = new JSONObject();

                                try {
                                    criterio_json.put("idIndicador", eval.getId_indicador());
                                    criterio_json.put("idCriterio", eval.getId_criterio());
                                    criterio_json.put("aprobado", eval.getAprobado());

                                    array_criterios.put(criterio_json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    errors++;
                                    messages += "Error consiguiendo criterios";
                                }
                            }
                        }


                        Hallazgo hallazgo = linkDB.getHallazgo("RECURSO",evaluacion.id, id_indicador,null);

                        if (hallazgo != null) {
                            hallazgo_json = new JSONObject();

                            try {

                                hallazgo_json.put("descripcion", hallazgo.getDescripcion());
                                hallazgo_json.put("idAccion", hallazgo.getIdAccion());
                                hallazgo_json.put("idIndicador", hallazgo.getIdIndicador());

                                array_hallazgos.put(hallazgo_json);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                errors++;
                                messages += "Error consiguiendo hallazgos";
                            }
                        }
                    }// fin for indicadores evaluacion

                    try {
                        evaluacion_json.put("id", evaluacion.getId());
                        evaluacion_json.put("idUsuario", evaluacion.getIdUsuario());
                        evaluacion_json.put("clues", evaluacion.getClues());
                        evaluacion_json.put("fechaEvaluacion", evaluacion.getFechaEvaluacion());
                        evaluacion_json.put("cerrado", evaluacion.getCerrado());

                        evaluacion_json.put("firma", evaluacion.getFirma());


                        evaluacion_json.put("responsable", evaluacion.getResponsable());

                        evaluacion_json.put("email", evaluacion.getEmailResponsable());

                        evaluacion_json.put("criterios", array_criterios);
                        evaluacion_json.put("hallazgos", array_hallazgos);

                        System.out.println("CLUES : " + evaluacion.clues + ", idServer: " + evaluacion.idServer);

                        if (evaluacion.idServer == 0) {
                            System.out.println("EVALUACIONES POST AGREGADA : " + evaluacion.clues);
                            array_evaluaciones_post.put(evaluacion_json);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        errors++;
                        messages += "Error consiguiendo detalles de evaluación " + evaluacion.clues;
                    }

                }   // fin for recorrido del total de evaluaciones


                try {
                    json_main_post.put("evaluaciones", array_evaluaciones_post);
                } catch (JSONException e) {
                    e.printStackTrace();
                    errors++;
                    messages += "Error empaquetando evaluaciones de recurso para sincronizar";
                }

                System.out.println("EVALUACIONES POST A ENVIAR : " + json_main_post.toString());

                String json_server_post = "";


                //git chch
                //ramirez.esquinca@gmail.com
                //mimoj98i

                try {

                     System.out.println("CREDENCIALES A ENVIAR : USUARIO: "+user_signed.email+", ACCESS TOKEN: "+user_signed.accessToken);

                    json_server_post = link.postEvaluacionesRecurso(json_main_post.toString(), user_signed.email, user_signed.accessToken);

                    if (json_server_post != null)
                    {
                        JSONObject respuesta = new JSONObject(json_server_post);
                        JSONArray array_respuesta = respuesta.getJSONArray("data");

                        for (int i = 0; i < array_evaluaciones_post.length(); i++)
                        {
                            JSONObject eval_enviada = array_evaluaciones_post.getJSONObject(i);

                            int id = eval_enviada.getInt("id");
                            String clues = eval_enviada.getString("clues");
                            String fechaEvaluacion = eval_enviada.getString("fechaEvaluacion");
                            String responsable = eval_enviada.getString("responsable");
                            String emailResponsable = eval_enviada.getString("email");



                            //recorrido de busqueda de cada evaluacion enviada en las evaluaciones recibidas para sincronizar id`s
                            for (int j = 0; j < array_respuesta.length(); j++)
                            {
                                JSONObject eval_recibida = array_respuesta.getJSONObject(j);

                                int id_server = eval_recibida.getInt("id");
                                int idUsuario2 = eval_recibida.getInt("idUsuario");
                                String clues2 = eval_recibida.getString("clues");
                                String fechaEvaluacion2 = eval_recibida.getString("fechaEvaluacion");
                                int cerrado2 = eval_recibida.getInt("cerrado");
                                String firma2 = eval_recibida.getString("firma");
                                String responsable2 = eval_recibida.getString("responsable");
                                String emailResponsable2 = eval_recibida.getString("email");

                                String creadoAl2 = eval_recibida.getString("creadoAl");
                                String modificadoAl2 = eval_recibida.getString("modificadoAl");
                                String borradoAl2 = "0000-00-00 00:00:00";

                                //coincidencia de la evaluacion enviada y la evaluacion ya sincronizada recibida del servidor
                                if (clues.equals(clues2) && fechaEvaluacion.equals(fechaEvaluacion2))
                                {
                                    Evaluacion eval_actual = linkDB.getEvaluacion("RECURSO",id,linkDB.openDB());

                                    Evaluacion eval_update = new Evaluacion(id, id_server, idUsuario2, clues2, fechaEvaluacion2,
                                                                            cerrado2, firma2, responsable2, emailResponsable2,1,
                                                                            eval_actual.getCompartido(),eval_actual.getCompartidoFull(),
                                                                            creadoAl2, modificadoAl2, borradoAl2);


                                    linkDB.actualizarEvaluacionRecurso(eval_update, linkDB.openDB());

                                    System.out.println("EVALUACION ACTUALIZADA CON DATOS DEL SERVER ...");
                                }else{
                                        System.out.println("EVALUACION  N OOO    ACTUALIZADA CON DATOS DEL SERVER ...");
                                     }

                            } //fin busqueda de cada evaluacion enviada en las evaluaciones recibidas

                        }//fin recorrido evaluaciones enviadas

                    } else {
                        errors++;
                        messages += "Error enviando evaluaciones de recurso.";
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    errors++;
                    messages += "Error recibiendo mensaje de envio de evaluaciones de recurso.";
                }

            }else{
                   //errors ++;
                   messages="No hay evaluaciones para sincronizar";
                 }


                String [] response = {""+errors,messages};


            return response;
        }





        protected void onProgressUpdate(Integer... values) { }

        protected void onPreExecute()
        {
            layout_loading.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String [] result)
        {
            int errors= Integer.parseInt(result[0]);
            String messages= result[1];

            String status_config="";


            layout_loading.setVisibility(View.GONE);
            if(errors>0)
            {
                status_config="ERROR";
                link.showSnackBar(getActivity(),coordinatorLayout, ""+messages,"ERROR");
            }else{
                   status_config="OK";
                   link.showSnackBar(getActivity(),coordinatorLayout, "Todas las evaluaciones de Recurso enviadas.","INFO");
                 }


            Config config = linkDB.getConfig("RECURSOS",user_signed.id);

            if(config != null)
            {
                config.setFechaAccion(link.getFecha());
                config.setModificadoAl(link.getFecha());
                config.setStatus(status_config);

                linkDB.actualizarConfig(config);
            }else{
                        Config new_config = new Config(
                                                        0,
                                                        "RECURSOS",
                                                        user_signed.id,
                                                        link.getFecha(),
                                                        link.getFecha(),
                                                        status_config,
                                                        "0000-00-00 00:00:00",
                                                        "0000-00-00 00:00:00"
                                                      );

                        linkDB.insertarConfig(new_config);
                  }

            ///text_status.setText("Enviando evaluaciones de recursos ...");

            setConteos();


            morphingBoton("RECURSOS", "NORMAL");
            activarBotones();


            //new putEvaluacionesRecurso().execute();


        }

        protected void onCancelled()
        {
            link.showSnackBar(getActivity(),coordinatorLayout, "Cancelado","ERROR");
        }

    }///FIN CLASS POST EVALUACIONES RECURSO







///// ***************************************************************************************************************
///// ***************************************************************************************************************

    /////         P O S T     EVALUACIONES NUEVAS     C A L I D A D    AL SERVIDOR

///// ***************************************************************************************************************
///// ***************************************************************************************************************


    private class postEvaluacionesCalidad extends AsyncTask<String, String [], String []>
    {
        protected String [] doInBackground(String... datos)
        {
            int errors=0;
            String messages="";

            List<Evaluacion> EVALUACIONES = new ArrayList<>();
            EVALUACIONES = linkDB.getEvaluacionesParaPost("CALIDAD", "", user_signed.getId());

            System.out.println(" [][][][][][][]    EVALUACIONES ENCONTRADAS DE CALIDAD : "+EVALUACIONES.size()+".]" );

            final JSONObject json_main_post=new JSONObject();
            JSONArray array_evaluaciones_post=new JSONArray();


            if(EVALUACIONES.size() > 0)
            {


                for (int a = 0; a < EVALUACIONES.size(); a++)
                {

                    Evaluacion evaluacion = EVALUACIONES.get(a);
                    JSONObject evaluacion_json = new JSONObject();

                    JSONArray array_registros = new JSONArray();
                    JSONObject registro = new JSONObject();

                    JSONArray array_criterios = new JSONArray();
                    JSONObject criterio;


                    JSONArray array_hallazgos = new JSONArray();

                    JSONObject hallazgo_json;

                    JSONObject objeto_padre_hallazgos = new JSONObject();
                    JSONObject objeto_indicador_hallazgos;


                    JSONArray array_cvr_total = new JSONArray();
                    JSONArray array_cvr = new JSONArray();
                    JSONObject cvr_json;

                    JSONArray array_expediente_criterio = new JSONArray();
                    JSONObject expediente_criterio_json;


                    ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

                        JSONArray ARRAY_EXPEDIENTES = new JSONArray();
                        JSONArray ARRAY_CRITERIOS = new JSONArray();
                        JSONArray ARRAY_VALIDACIONES = new JSONArray();


                    ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

                    List<Indicador> indicadores = new ArrayList<>();

                    indicadores = linkDB.getIndicadoresAgregadosCalidad(evaluacion.id);

                    int hallazgosPorExpediente=0;

                    for (int i = 0; i < indicadores.size(); i++)
                    {
                        int id_indicador = indicadores.get(i).getId();

                        List<EvaluacionCalidadRegistro> lista_ecr = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion.id, id_indicador);

                        objeto_indicador_hallazgos = new JSONObject();
                        hallazgosPorExpediente=0;

                        ///:::::  RECORRIDO DE    E X P E D I E N T E S
                        for (int j = 0; j < lista_ecr.size(); j++)
                        {
                                //ARRAY_EXPEDIENTES = new JSONArray();

                                ARRAY_CRITERIOS = new JSONArray();

                                array_criterios = new JSONArray();
                                List<EvaluacionCalidadCriterio> lista_ecc = linkDB.getEvaluacionCalidadCriterio(lista_ecr.get(j).id);

                                ///:::: RECORRIDO DE   C R I T E R I O S

                                for (int k = 0; k < lista_ecc.size(); k++)
                                {
                                    criterio = new JSONObject();

                                    try {
                                            criterio.put("idCriterio", lista_ecc.get(k).idCriterio);
                                            criterio.put("idIndicador", lista_ecc.get(k).idIndicador);
                                            criterio.put("aprobado", lista_ecc.get(k).aprobado);

                                            array_criterios.put(criterio);
                                        } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                  }
                                    ///::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


                                    List<CriterioValidacionRespuesta> lista_cvr = linkDB.getListaCriterioValidacionRespuestaTotalPorCriterio("CALIDAD",evaluacion.id,lista_ecr.get(j).expediente,lista_ecc.get(k).idCriterio);
                                    //array_cvr = new JSONArray();
                                    ARRAY_VALIDACIONES = new JSONArray();
                                    int cvr_counts=0;

                                    link.printConsola("TAMAÑO DE VALIDACIONES PARA ESTE CRITERIO : [ "+lista_cvr.size()+"]");

                                    for(int zz=0; zz<lista_cvr.size(); zz++)
                                    {
                                        cvr_json = new JSONObject();
                                        cvr_counts++;

                                            try {
                                                    cvr_json.put("expediente",lista_cvr.get(zz).getExpediente());
                                                    cvr_json.put("idCriterio",lista_cvr.get(zz).getIdCriterio());
                                                    cvr_json.put("idCriterioValidacion",lista_cvr.get(zz).getIdCriterioValidacion());
                                                    cvr_json.put("tipo",lista_cvr.get(zz).getTipo());
                                                    cvr_json.put("respuesta1",lista_cvr.get(zz).getRespuesta1());
                                                    cvr_json.put("respuesta2",lista_cvr.get(zz).getRespuesta2());

                                                    //array_cvr.put(cvr_json);
                                                    ARRAY_VALIDACIONES.put(cvr_json);

                                                } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                                errors++;
                                                                                messages += "Error consiguiendo las validaciones de criterios.";
                                                                            }
                                    }

                                    ARRAY_CRITERIOS.put(ARRAY_VALIDACIONES);

                                    if(cvr_counts>0)
                                    {
                                        //array_cvr_total.put(array_cvr);  // antes de tercera col
                                        //ARRAY_CRITERIOS.put(ARRAY_VALIDACIONES);
                                    }

                                    ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

                                }//FIN FOR EVALUACION CALIDAD CRITERIO

                                //array_cvr_total.put(array_expediente_criterio);
                                ARRAY_EXPEDIENTES.put(ARRAY_CRITERIOS);


                                registro = new JSONObject();

                                try {
                                        registro.put("idIndicador", lista_ecr.get(j).idIndicador);
                                        registro.put("expediente", lista_ecr.get(j).expediente);
                                        registro.put("columna", 0);
                                        registro.put("cumple", lista_ecr.get(j).cumple);
                                        registro.put("promedio", lista_ecr.get(j).promedio);
                                        registro.put("totalCriterio", lista_ecr.get(j).totalCriterio);

                                        registro.put("criterios", array_criterios);

                                    } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                              }


                                array_registros.put(registro);

                            /*
                            List<CriterioValidacionRespuesta> lista_cvr = linkDB.getListaCriterioValidacionRespuestaTotalPorEvaluacion("CALIDAD",evaluacion.id,lista_ecr.get(j).expediente);
                            array_cvr = new JSONArray();
                            int cvr_counts=0;

                            for(int zz=0; zz<lista_cvr.size(); zz++)
                            {
                                cvr_json = new JSONObject();
                                cvr_counts++;
                                try {
                                        cvr_json.put("expediente",lista_cvr.get(zz).getExpediente());
                                        cvr_json.put("idCriterio",lista_cvr.get(zz).getIdCriterio());
                                        cvr_json.put("idCriterioValidacion",lista_cvr.get(zz).getIdCriterioValidacion());
                                        cvr_json.put("tipo",lista_cvr.get(zz).getTipo());
                                        cvr_json.put("respuesta1",lista_cvr.get(zz).getRespuesta1());
                                        cvr_json.put("respuesta2",lista_cvr.get(zz).getRespuesta2());

                                        array_cvr.put(cvr_json);

                                    } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    errors++;
                                                                    messages += "Error consiguiendo las validaciones de criterios.";
                                                              }
                            }

                            if(cvr_counts>0)
                            {
                                array_cvr_total.put(array_cvr);
                            }

                            */
                            //array_cvr_total.put(array_cvr);



                            Hallazgo hallazgo = linkDB.getHallazgo("CALIDAD",evaluacion.id, id_indicador,lista_ecr.get(j).getExpediente());

                            if (hallazgo != null)
                            {
                                hallazgo_json = new JSONObject();
                                hallazgosPorExpediente++;

                                try {
                                        hallazgo_json.put("descripcion", hallazgo.getDescripcion());
                                        hallazgo_json.put("idAccion", hallazgo.getIdAccion());
                                        hallazgo_json.put("idIndicador", hallazgo.getIdIndicador());

                                        hallazgo_json.put("expediente", hallazgo.getExpediente());
                                        //array_hallazgos.put(hallazgo_json);
                                        objeto_indicador_hallazgos.put(""+lista_ecr.get(j).getExpediente(),hallazgo_json);

                                    } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    errors++;
                                                                    messages += "Error consiguiendo hallazgos";
                                                              }
                            }






                        }  /// FIN FOR LISTA REGISTROS EXPEDIENTES


                        if(hallazgosPorExpediente>0)
                        {
                                try {
                                    objeto_padre_hallazgos.put("" + id_indicador, objeto_indicador_hallazgos);

                                    } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                        }


                    }// fin for indicadores evaluacion


                    try {

                            evaluacion_json.put("id", evaluacion.getId());
                            evaluacion_json.put("idUsuario", evaluacion.getIdUsuario());
                            evaluacion_json.put("clues", evaluacion.getClues());
                            evaluacion_json.put("fechaEvaluacion", evaluacion.getFechaEvaluacion());
                            evaluacion_json.put("cerrado", evaluacion.getCerrado());
                            evaluacion_json.put("firma", evaluacion.getFirma().substring(0,10));
                            evaluacion_json.put("responsable", evaluacion.getResponsable());

                        evaluacion_json.put("email", evaluacion.getEmailResponsable());

                            evaluacion_json.put("registros", array_registros);
                            //evaluacion_json.put("hallazgos", array_hallazgos);
                            evaluacion_json.put("hallazgos",objeto_padre_hallazgos);

                            //evaluacion_json.put("criterio_respuestas", array_cvr_total);
                        evaluacion_json.put("criterio_respuestas",ARRAY_EXPEDIENTES);





                            array_evaluaciones_post.put(evaluacion_json);


                         } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        errors++;
                                                        messages += "Error consiguiendo detalles de evaluación " + evaluacion.clues;
                                                    }

                }   // fin for recorrido del total de evaluaciones


                try {
                        json_main_post.put("evaluaciones", array_evaluaciones_post);

                    } catch (JSONException e) {
                                                e.printStackTrace();
                                                errors++;
                                                messages += "Error empaquetando evaluaciones de recurso para sincronizar";
                                              }

                System.out.println("------> > > >   []  []  []  []   EVALUACIONES CALIDAD  POST A ENVIAR : " + json_main_post.toString());

                String json_server_post = "";




                try {

                    //json_server_post = link.postEvaluacionesCalidad(json_main_post.toString(), user_signed.email, user_signed.accessToken);

/*
                    final String abc =json_main_post.toString();
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    context);
                            alertDialogBuilder.setTitle("TEST");
                            alertDialogBuilder
                                    .setMessage(""+abc.toString())
                                    .setCancelable(false)
                                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {

                                        }
                                    })
                                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {

                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }
                    });


*/



                    if (json_server_post != null)
                    {
                        JSONObject respuesta = new JSONObject(json_server_post);
                        JSONArray array_respuesta = respuesta.getJSONArray("data");

                        for (int i = 0; i < array_evaluaciones_post.length(); i++)
                        {
                            JSONObject eval_enviada = array_evaluaciones_post.getJSONObject(i);

                            int id = eval_enviada.getInt("id");
                            String clues = eval_enviada.getString("clues");
                            String fechaEvaluacion = eval_enviada.getString("fechaEvaluacion");
                            String responsable = eval_enviada.getString("responsable");


                            //recorrido de busqueda de cada evaluacion enviada en las evaluaciones recibidas para sincronizar id`s
                            for (int j = 0; j < array_respuesta.length(); j++)
                            {
                                JSONObject eval_recibida = array_respuesta.getJSONObject(j);

                                int id_server = eval_recibida.getInt("id");
                                int idUsuario2 = eval_recibida.getInt("idUsuario");
                                String clues2 = eval_recibida.getString("clues");
                                String fechaEvaluacion2 = eval_recibida.getString("fechaEvaluacion");
                                int cerrado2 = eval_recibida.getInt("cerrado");
                                String firma2 = eval_recibida.getString("firma");
                                String responsable2 = eval_recibida.getString("responsable");
                                String emailResponsable2 = eval_recibida.getString("email");

                                String creadoAl2 = eval_recibida.getString("creadoAl");
                                String modificadoAl2 = eval_recibida.getString("modificadoAl");

                                //String borradoAl2 = eval_recibida.getString("borradoAl");
                                String borradoAl2 = "0000-00-00 00:00:00";

                                //coincidencia de la evaluacion enviada y la evaluacion ya sincronizada recibida del servidor
                                if (clues.equals(clues2) && fechaEvaluacion.equals(fechaEvaluacion2))
                                {
                                    Evaluacion eval_actual = linkDB.getEvaluacion("CALIDAD",id,linkDB.openDB());

                                    Evaluacion eval_update = new Evaluacion(id, id_server, idUsuario2, clues2, fechaEvaluacion2,
                                                                            cerrado2, firma2, responsable2, emailResponsable2,1,
                                                                            eval_actual.getCompartido(),eval_actual.getCompartidoFull(),
                                                                            creadoAl2, modificadoAl2, borradoAl2);

                                    linkDB.actualizarEvaluacionCalidad(eval_update, linkDB.openDB());
                                }

                            } //fin busqueda de cada evaluacion enviada en las evaluaciones recibidas

                        }//fin recorrido evaluaciones enviadas

                    } else {
                                errors++;
                                messages += "Error enviando evaluaciones de calidad.";
                           }


                } catch (Exception e) {
                                        e.printStackTrace();
                                        errors++;
                                        messages += "Error recibiendo mensaje de envio de evaluaciones de calidad.";
                                      }




            }else{
                   //errors ++;
                   messages="Todas las evaluaciones enviadas";
                 }


            String [] response = {""+errors,messages};


             return response;

        }  ///  f i n    d o I n B a c k g r o u n d



        protected void onProgressUpdate(Integer... values) { }

        protected void onPreExecute()
        {

            layout_loading.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String [] result)
        {

            int errors= Integer.parseInt(result[0]);
            String messages= result[1];


            String status_config="";

            layout_loading.setVisibility(View.GONE);
            boton_sinc_evaluaciones_calidad.setEnabled(true);

            if(errors>0)
            {
                status_config="ERROR";
                link.showSnackBar(getActivity(),coordinatorLayout, messages+"","ERROR");
            }else{
                    status_config="OK";
                    link.showSnackBar(getActivity(),coordinatorLayout, "Evaluaciones de Calidad enviadas.","INFO");
                 }


            Config config = linkDB.getConfig("CALIDAD",user_signed.id);

            if(config != null)
            {
                config.setFechaAccion(link.getFecha());
                config.setModificadoAl(link.getFecha());
                config.setStatus(status_config);

                linkDB.actualizarConfig(config);
            }else{
                    Config new_config = new Config(
                                                    0,
                                                    "CALIDAD",
                                                    user_signed.id,
                                                    link.getFecha(),
                                                    link.getFecha(),
                                                    status_config,
                                                    "0000-00-00 00:00:00",
                                                    "0000-00-00 00:00:00"
                                                  );

                    linkDB.insertarConfig(new_config);
                 }

            setConteos();

            morphingBoton("CALIDAD", "NORMAL");
            activarBotones();

            // new putEvaluacionesCalidad().execute();


        }

        protected void onCancelled()
        {
            link.showSnackBar(getActivity(),coordinatorLayout, "Cancelado","ERROR");
        }

    }///FIN CLASS POST EVALUACIONES C A L I D A D















///// ***************************************************************************************************************
///// ***************************************************************************************************************

    /////    R E S I C R O N I Z A R      EVALUACIONES  AL SERVIDOR         R E   S Y N C         R E C U R S O S

///// ***************************************************************************************************************
///// ***************************************************************************************************************


    private class rePostEvaluacionesRecurso extends AsyncTask<String, String [], String []>
    {
        protected void onPreExecute()
        {
            progress_sync = new ProgressDialog(context);
            progress_sync.setTitle("Sincronización");
            progress_sync.setMessage("Resincronizando evaluaciones de recursos ...");
            progress_sync.setCancelable(false);
            progress_sync.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress_sync.setIndeterminate(true);

            progress_sync.show();
        }


        protected String [] doInBackground(String... datos)
        {
            int errors=0;
            String messages="";

            int evaluaciones_sincronizadas = 0;

            List<Evaluacion> EVALUACIONES = new ArrayList<>();

            //EVALUACIONES = linkDB.getEvaluaciones("RECURSO","",user_signed.getId());
            EVALUACIONES = linkDB.getEvaluacionesParaRePost( "RECURSO","",user_signed.getId() );

            JSONObject json_main_post=new JSONObject();
            JSONArray array_evaluaciones_post=new JSONArray();


            if(EVALUACIONES.size() > 0)
            {


                for (int z = 0; z < EVALUACIONES.size(); z++)
                {

                    Evaluacion evaluacion = EVALUACIONES.get(z);

                    JSONObject evaluacion_json = new JSONObject();
                    JSONArray array_criterios = new JSONArray();
                    JSONObject criterio_json;

                    JSONArray array_hallazgos = new JSONArray();
                    JSONObject hallazgo_json;


                    int nivel_cone = linkDB.getNivelCone(evaluacion.clues);


                    List<Indicador> indicadores = new ArrayList<>();
                    List<LugarVerificacion> listaLV = new ArrayList<>();

                    indicadores = linkDB.getIndicadoresAgregadosRecurso(evaluacion.id);

                    for (int i = 0; i < indicadores.size(); i++) {

                        int id_indicador = indicadores.get(i).getId();

                        List<EvaluacionRecursoCriterio> listaERC = new ArrayList<>();
                        listaERC = linkDB.getEvaluacionRecursoCriterio(evaluacion.id, id_indicador);

                        System.out.print("EVALUACION : " + evaluacion.id + ", INDICADOR :" + id_indicador + ".");
                        System.out.print("( " + "CRITERIOS ENCONTRADOS PARA INDICADOR :" + id_indicador + " : " + listaERC.size() + " )");

                        for (int j = 0; j < listaERC.size(); j++) {
                            EvaluacionRecursoCriterio eval = listaERC.get(j);

                            if (eval != null) {
                                criterio_json = new JSONObject();

                                try {
                                    criterio_json.put("idIndicador", eval.getId_indicador());
                                    criterio_json.put("idCriterio", eval.getId_criterio());
                                    criterio_json.put("aprobado", eval.getAprobado());

                                    array_criterios.put(criterio_json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    errors++;
                                    messages += "Error consiguiendo criterios";
                                }
                            }
                        }


                        Hallazgo hallazgo = linkDB.getHallazgo("RECURSO",evaluacion.id, id_indicador,null);

                        if (hallazgo != null) {
                            hallazgo_json = new JSONObject();

                            try {

                                hallazgo_json.put("descripcion", hallazgo.getDescripcion());
                                hallazgo_json.put("idAccion", hallazgo.getIdAccion());
                                hallazgo_json.put("idIndicador", hallazgo.getIdIndicador());

                                array_hallazgos.put(hallazgo_json);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                errors++;
                                messages += "Error consiguiendo hallazgos";
                            }
                        }
                    }// fin for indicadores evaluacion

                    try {

                            evaluacion_json.put("id", evaluacion.getId());
                            evaluacion_json.put("idUsuario", evaluacion.getIdUsuario());
                            evaluacion_json.put("clues", evaluacion.getClues());
                            evaluacion_json.put("fechaEvaluacion", evaluacion.getFechaEvaluacion());
                            evaluacion_json.put("cerrado", evaluacion.getCerrado());

                            evaluacion_json.put("firma", evaluacion.getFirma().substring(0,10));


                            evaluacion_json.put("responsable", evaluacion.getResponsable());

                            evaluacion_json.put("email", evaluacion.getEmailResponsable());

                            evaluacion_json.put("criterios", array_criterios);
                            evaluacion_json.put("hallazgos", array_hallazgos);

                            System.out.println("CLUES : " + evaluacion.clues + ", idServer: " + evaluacion.idServer);

                            if (evaluacion.idServer == 0) {
                                System.out.println("EVALUACIONES POST AGREGADA : " + evaluacion.clues);
                                array_evaluaciones_post.put(evaluacion_json);
                            }

                           ///// AQUI ENVIAR CADA evaluacion_json AL SERVIDOR DE FORMA INFDIVIDUAL

                        String json_server_post = "";
                        json_server_post = link.rePostEvaluacionesRecurso(evaluacion_json.toString(), user_signed.email, user_signed.accessToken);

                        if (json_server_post != null)
                        {
                            JSONObject respuesta = new JSONObject(json_server_post);

                            int status = respuesta.getInt("status");
                            if(status==201 || status==409)
                            {
                                JSONObject e = respuesta.getJSONObject("data");
                                int id_server = e.getInt("id");
                                Evaluacion eval_actual = linkDB.getEvaluacion("RECURSO",evaluacion.id,linkDB.openDB());

                                Evaluacion eval_update = new Evaluacion(evaluacion.id, id_server, eval_actual.idUsuario, eval_actual.clues, eval_actual.fechaEvaluacion,
                                                                        eval_actual.cerrado, eval_actual.firma, eval_actual.responsable, eval_actual.emailResponsable,1,
                                                                        eval_actual.getCompartido(),eval_actual.getCompartidoFull(),
                                                                        eval_actual.creadoAl, link.getFecha(), eval_actual.borradoAl);

                                linkDB.actualizarEvaluacionRecurso(eval_update, linkDB.openDB());
                                evaluaciones_sincronizadas++;

                            }else{
                                     //errors++;
                                 }

                        }else{
                                errors++;
                                messages+="Error del servidor";
                             }

                        } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    errors++;
                                                    messages += "Error consiguiendo detalles de evaluación " + evaluacion.clues;
                                                  }

                }   // fin for recorrido del total de evaluaciones



            }else{
                errors ++;
                messages="No hay evaluaciones para resincronizar";
            }


            String [] response = {""+errors,messages};


            return response;
        }





        protected void onProgressUpdate(Integer... values) { }

        /*
        protected void onPreExecute()
        {
            layout_loading.setVisibility(View.VISIBLE);
        }
        */

        protected void onPostExecute(String [] result)
        {
            progress_sync.dismiss();

            int errors= Integer.parseInt(result[0]);
            String messages= result[1];

            String status_config="";


            layout_loading.setVisibility(View.GONE);
            if(errors>0)
            {
                status_config="ERROR";
                link.showSnackBar(getActivity(),coordinatorLayout, ""+messages,"ERROR");
            }else{
                status_config="OK";
                link.showSnackBar(getActivity(),coordinatorLayout, "Todas las evaluaciones de Recurso enviadas.","INFO");
            }


            /*
            Config config = linkDB.getConfig("RECURSOS",user_signed.id);

            if(config != null)
            {
                config.setFechaAccion(link.getFecha());
                config.setModificadoAl(link.getFecha());
                config.setStatus(status_config);

                linkDB.actualizarConfig(config);
            }else{
                Config new_config = new Config(
                        0,
                        "RECURSOS",
                        user_signed.id,
                        link.getFecha(),
                        link.getFecha(),
                        status_config,
                        "0000-00-00 00:00:00",
                        "0000-00-00 00:00:00"
                );

                linkDB.insertarConfig(new_config);
            }
            */

            ///text_status.setText("Enviando evaluaciones de recursos ...");

            //setConteos();


            //morphingBoton("RECURSOS", "NORMAL");
            //activarBotones();


            //new putEvaluacionesRecurso().execute();


        }

        protected void onCancelled()
        {
            link.showSnackBar(getActivity(),coordinatorLayout, "Cancelado","ERROR");
        }

    }///FIN CLASS POST EVALUACIONES RECURSO







///// ***************************************************************************************************************
///// ***************************************************************************************************************

    /////        R E     P   O  S   T      EVALUACIONES     C  A   L   I   D  A  D

///// ***************************************************************************************************************
///// ***************************************************************************************************************


    private class rePostEvaluacionesCalidad extends AsyncTask<String, String [], String []>
    {
        protected void onPreExecute()
        {
            progress_sync = new ProgressDialog(context);
            progress_sync.setTitle("Sincronización");
            progress_sync.setMessage("Resincronizando evaluaciones de Procesos ...");
            progress_sync.setCancelable(false);
            progress_sync.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress_sync.setIndeterminate(true);

            progress_sync.show();
        }


        protected String [] doInBackground(String... datos)
        {
            int errors=0;
            String messages="";
            int evaluaciones_sincronizadas = 0;

            List<Evaluacion> EVALUACIONES = new ArrayList<>();
            EVALUACIONES = linkDB.getEvaluacionesParaRePost("CALIDAD", "", user_signed.getId());

            System.out.println(" [][][][][][][]    EVALUACIONES ENCONTRADAS DE CALIDAD : "+EVALUACIONES.size()+".]" );

            final JSONObject json_main_post=new JSONObject();
            JSONArray array_evaluaciones_post=new JSONArray();


            if(EVALUACIONES.size() > 0)
            {


                for (int a = 0; a < EVALUACIONES.size(); a++)
                {

                    Evaluacion evaluacion = EVALUACIONES.get(a);
                    JSONObject evaluacion_json = new JSONObject();

                    JSONArray array_registros = new JSONArray();
                    JSONObject registro = new JSONObject();

                    JSONArray array_criterios = new JSONArray();
                    JSONObject criterio;


                    JSONArray array_hallazgos = new JSONArray();

                    JSONObject hallazgo_json;

                    JSONObject objeto_padre_hallazgos = new JSONObject();
                    JSONObject objeto_indicador_hallazgos;


                    JSONArray array_cvr_total = new JSONArray();
                    JSONArray array_cvr = new JSONArray();
                    JSONObject cvr_json;

                    JSONArray array_expediente_criterio = new JSONArray();
                    JSONObject expediente_criterio_json;


                    ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

                    JSONArray ARRAY_EXPEDIENTES = new JSONArray();
                    JSONArray ARRAY_CRITERIOS = new JSONArray();
                    JSONArray ARRAY_VALIDACIONES = new JSONArray();


                    ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

                    List<Indicador> indicadores = new ArrayList<>();

                    indicadores = linkDB.getIndicadoresAgregadosCalidad(evaluacion.id);

                    int hallazgosPorExpediente=0;

                    for (int i = 0; i < indicadores.size(); i++)
                    {
                        int id_indicador = indicadores.get(i).getId();

                        List<EvaluacionCalidadRegistro> lista_ecr = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion.id, id_indicador);

                        objeto_indicador_hallazgos = new JSONObject();
                        hallazgosPorExpediente=0;

                        ///:::::  RECORRIDO DE    E X P E D I E N T E S
                        for (int j = 0; j < lista_ecr.size(); j++)
                        {
                            //ARRAY_EXPEDIENTES = new JSONArray();

                            ARRAY_CRITERIOS = new JSONArray();

                            array_criterios = new JSONArray();
                            List<EvaluacionCalidadCriterio> lista_ecc = linkDB.getEvaluacionCalidadCriterio(lista_ecr.get(j).id);

                            ///:::: RECORRIDO DE   C R I T E R I O S

                            for (int k = 0; k < lista_ecc.size(); k++)
                            {
                                criterio = new JSONObject();

                                try {
                                    criterio.put("idCriterio", lista_ecc.get(k).idCriterio);
                                    criterio.put("idIndicador", lista_ecc.get(k).idIndicador);
                                    criterio.put("aprobado", lista_ecc.get(k).aprobado);

                                    array_criterios.put(criterio);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ///::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


                                List<CriterioValidacionRespuesta> lista_cvr = linkDB.getListaCriterioValidacionRespuestaTotalPorCriterio("CALIDAD",evaluacion.id,lista_ecr.get(j).expediente,lista_ecc.get(k).idCriterio);
                                //array_cvr = new JSONArray();
                                ARRAY_VALIDACIONES = new JSONArray();
                                int cvr_counts=0;

                                link.printConsola("TAMAÑO DE VALIDACIONES PARA ESTE CRITERIO : [ "+lista_cvr.size()+"]");

                                for(int zz=0; zz<lista_cvr.size(); zz++)
                                {
                                    cvr_json = new JSONObject();
                                    cvr_counts++;

                                    try {
                                        cvr_json.put("expediente",lista_cvr.get(zz).getExpediente());
                                        cvr_json.put("idCriterio",lista_cvr.get(zz).getIdCriterio());
                                        cvr_json.put("idCriterioValidacion",lista_cvr.get(zz).getIdCriterioValidacion());
                                        cvr_json.put("tipo",lista_cvr.get(zz).getTipo());
                                        cvr_json.put("respuesta1",lista_cvr.get(zz).getRespuesta1());
                                        cvr_json.put("respuesta2",lista_cvr.get(zz).getRespuesta2());

                                        //array_cvr.put(cvr_json);
                                        ARRAY_VALIDACIONES.put(cvr_json);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        errors++;
                                        messages += "Error consiguiendo las validaciones de criterios.";
                                    }
                                }

                                ARRAY_CRITERIOS.put(ARRAY_VALIDACIONES);

                                if(cvr_counts>0)
                                {
                                    //array_cvr_total.put(array_cvr);  // antes de tercera col
                                    //ARRAY_CRITERIOS.put(ARRAY_VALIDACIONES);
                                }

                                ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

                            }//FIN FOR EVALUACION CALIDAD CRITERIO

                            //array_cvr_total.put(array_expediente_criterio);
                            ARRAY_EXPEDIENTES.put(ARRAY_CRITERIOS);


                            registro = new JSONObject();

                            try {
                                    registro.put("idIndicador", lista_ecr.get(j).idIndicador);
                                    registro.put("expediente", lista_ecr.get(j).expediente);
                                    registro.put("columna", 0);
                                    registro.put("cumple", lista_ecr.get(j).cumple);
                                    registro.put("promedio", lista_ecr.get(j).promedio);
                                    registro.put("totalCriterio", lista_ecr.get(j).totalCriterio);

                                    registro.put("criterios", array_criterios);

                                } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                            array_registros.put(registro);


                            Hallazgo hallazgo = linkDB.getHallazgo("CALIDAD",evaluacion.id, id_indicador,lista_ecr.get(j).getExpediente());

                            if (hallazgo != null)
                            {
                                hallazgo_json = new JSONObject();
                                hallazgosPorExpediente++;

                                try {
                                    hallazgo_json.put("descripcion", hallazgo.getDescripcion());
                                    hallazgo_json.put("idAccion", hallazgo.getIdAccion());
                                    hallazgo_json.put("idIndicador", hallazgo.getIdIndicador());

                                    hallazgo_json.put("expediente", hallazgo.getExpediente());
                                    //array_hallazgos.put(hallazgo_json);
                                    objeto_indicador_hallazgos.put(""+lista_ecr.get(j).getExpediente(),hallazgo_json);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    errors++;
                                    messages += "Error consiguiendo hallazgos";
                                }
                            }






                        }  /// FIN FOR LISTA REGISTROS EXPEDIENTES


                        if(hallazgosPorExpediente>0)
                        {
                            try {
                                objeto_padre_hallazgos.put("" + id_indicador, objeto_indicador_hallazgos);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }// fin for indicadores evaluacion


                    try {

                        evaluacion_json.put("id", evaluacion.getId());
                        evaluacion_json.put("idUsuario", evaluacion.getIdUsuario());
                        evaluacion_json.put("clues", evaluacion.getClues());
                        evaluacion_json.put("fechaEvaluacion", evaluacion.getFechaEvaluacion());
                        evaluacion_json.put("cerrado", evaluacion.getCerrado());
                        evaluacion_json.put("firma", evaluacion.getFirma().substring(0,10));
                        evaluacion_json.put("responsable", evaluacion.getResponsable());

                        evaluacion_json.put("email", evaluacion.getEmailResponsable());

                        evaluacion_json.put("registros", array_registros);
                        //evaluacion_json.put("hallazgos", array_hallazgos);
                        evaluacion_json.put("hallazgos",objeto_padre_hallazgos);

                        //evaluacion_json.put("criterio_respuestas", array_cvr_total);
                        evaluacion_json.put("criterio_respuestas",ARRAY_EXPEDIENTES);





                        array_evaluaciones_post.put(evaluacion_json);


                        ////**********************************************************************************************************************************************************

                        String json_server_post = "";
                        json_server_post = link.rePostEvaluacionesCalidad(evaluacion_json.toString(), user_signed.email, user_signed.accessToken);

                        if (json_server_post != null)
                        {
                            JSONObject respuesta = new JSONObject(json_server_post);

                            int status = respuesta.getInt("status");
                            if(status==201 || status==409)
                            {
                                JSONObject e = respuesta.getJSONObject("data");
                                int id_server = e.getInt("id");
                                Evaluacion eval_actual = linkDB.getEvaluacion("CALIDAD",evaluacion.id,linkDB.openDB());

                                Evaluacion eval_update = new Evaluacion(evaluacion.id, id_server, eval_actual.idUsuario, eval_actual.clues, eval_actual.fechaEvaluacion,
                                        eval_actual.cerrado, eval_actual.firma, eval_actual.responsable, eval_actual.emailResponsable,1,
                                        eval_actual.getCompartido(),eval_actual.getCompartidoFull(),
                                        eval_actual.creadoAl, link.getFecha(), eval_actual.borradoAl);

                                linkDB.actualizarEvaluacionCalidad(eval_update, linkDB.openDB());
                                evaluaciones_sincronizadas++;

                            }else{
                                        //errors++;
                                 }

                        }else{
                            errors++;
                            messages+="Error del servidor";
                        }
                        ///***********************************************************************************************************************************************************




                    } catch (JSONException e) {
                        e.printStackTrace();
                        errors++;
                        messages += "Error consiguiendo detalles de evaluación " + evaluacion.clues;
                    }

                }   // fin for recorrido del total de evaluaciones


                try {
                        json_main_post.put("evaluaciones", array_evaluaciones_post);

                } catch (JSONException e) {
                    e.printStackTrace();
                    errors++;
                    messages += "Error empaquetando evaluaciones de procesos para sincronizar";
                }

                System.out.println("------> > > >   []  []  []  []   EVALUACIONES CALIDAD  POST A ENVIAR : " + json_main_post.toString());

                String json_server_post = "";





            }else{
                errors ++;
                messages="No hay evaluaciones para resincronizar";
            }


            String [] response = {""+errors,messages};


            return response;

        }  ///  f i n    d o I n B a c k g r o u n d



        protected void onProgressUpdate(Integer... values) { }



        protected void onPostExecute(String [] result)
        {

            int errors= Integer.parseInt(result[0]);
            String messages= result[1];

            progress_sync.dismiss();



            String status_config="";

            layout_loading.setVisibility(View.GONE);
            boton_sinc_evaluaciones_calidad.setEnabled(true);

            if(errors>0)
            {
                status_config="ERROR";
                link.showSnackBar(getActivity(),coordinatorLayout, messages+"","ERROR");
            }else{
                status_config="OK";
                link.showSnackBar(getActivity(),coordinatorLayout, "Evaluaciones de Calidad enviadas.","INFO");
            }


            Config config = linkDB.getConfig("CALIDAD",user_signed.id);

            if(config != null)
            {
                config.setFechaAccion(link.getFecha());
                config.setModificadoAl(link.getFecha());
                config.setStatus(status_config);

                linkDB.actualizarConfig(config);
            }else{
                Config new_config = new Config(
                        0,
                        "CALIDAD",
                        user_signed.id,
                        link.getFecha(),
                        link.getFecha(),
                        status_config,
                        "0000-00-00 00:00:00",
                        "0000-00-00 00:00:00"
                );

                linkDB.insertarConfig(new_config);
            }

            setConteos();

            morphingBoton("CALIDAD", "NORMAL");
            activarBotones();

        }

        protected void onCancelled()
        {
            link.showSnackBar(getActivity(),coordinatorLayout, "Cancelado","ERROR");
        }

    }///FIN CLASS RE POST EVALUACIONES C A L I D A D



























    public void activarBotones()
    {
        boton_actualizar_clues.setEnabled(true);
        boton_sinc_evaluaciones_recurso.setEnabled(true);
        boton_sinc_evaluaciones_calidad.setEnabled(true);

    }


    public void desactivarBotones()
    {
        boton_actualizar_clues.setEnabled(false);
        boton_sinc_evaluaciones_recurso.setEnabled(false);
        boton_sinc_evaluaciones_calidad.setEnabled(false);

    }



    public void morphingBoton(String boton, String estado)
    {

        if(boton.equals("CLUES"))
        {
            if(estado.equals("NORMAL"))
            {
                boton_actualizar_clues.setVisibility(View.VISIBLE);
                progress_bar_clues.setVisibility(View.GONE);

            }else{
                    boton_actualizar_clues.setVisibility(View.GONE);
                    progress_bar_clues.setVisibility(View.VISIBLE);

                    text_ultima_actualizacion_clues.setText("Descargando catálogos...");

                 }
        }


        if(boton.equals("RECURSOS"))
        {
            if(estado.equals("NORMAL"))
            {
                boton_sinc_evaluaciones_recurso.setVisibility(View.VISIBLE);
                progress_bar_recursos.setVisibility(View.GONE);

            }else{
                    boton_sinc_evaluaciones_recurso.setVisibility(View.GONE);
                    progress_bar_recursos.setVisibility(View.VISIBLE);

                    text_fecha_recursos.setText("Enviando evaluaciones...");

                }
        }


        if(boton.equals("CALIDAD"))
        {
            if(estado.equals("NORMAL"))
            {
                boton_sinc_evaluaciones_calidad.setVisibility(View.VISIBLE);
                progress_bar_calidad.setVisibility(View.GONE);

            }else{
                    boton_sinc_evaluaciones_calidad.setVisibility(View.GONE);
                    progress_bar_calidad.setVisibility(View.VISIBLE);

                    text_fecha_calidad.setText("Enviando evaluaciones...");

                }
        }




    }///FIN MORPHING BOTON











}  ///FIN FRAGMENT




    /*
    AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() ,R.style.TemaDialogMaterial);

builder.setCancelable(false);
        builder.setMessage("¿ Esta seguro que desea actualizar los cátalogos ?")
        .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener()
        {
@Override
public void onClick(DialogInterface dialog, int id)
        {
        morphingBoton("CLUES", "DESCARGANDO");
        desactivarBotones();

        new ActualizarCatalogos().execute();
        }
        })
        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
@Override
public void onClick(DialogInterface dialog,int id) {
        dialog.cancel();
        }
        }).show();
*/


