package code.yoursoft.ciummovil;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class EvaluadorCalidad extends AppCompatActivity implements  InterfaceCalidad{


    MenuItem indicacion;

    MenuItem guardar;
    MenuItem cerrar;
    MenuItem transferencia;
    MenuItem firmar;
    MenuItem ver_ficha;


    void updateAsyncAvance(int respondidas, int preguntas,double promedio_expediente)
    {
        class OneShotTask implements Runnable
        {
            int respondidas;
            int preguntas;
            double promedio_expediente;

            OneShotTask(int respondidas, int preguntas, double promedio_expediente)
            {
                this.respondidas = respondidas;
                this.preguntas = preguntas;
                this.promedio_expediente = promedio_expediente;
            }
            public void run()
            {
                text_count.setText(respondidas + " / " + preguntas);
                text_promedio_expediente.setText(link.imprimirDouble(promedio_expediente)+" % ");
            }
        }

        runOnUiThread(new OneShotTask(respondidas,preguntas, promedio_expediente));
    }



    void updateAsyncPromedioIndicador(double promedio_indicador)
    {
        class OneShotTask implements Runnable
        {
            double promedio;

            OneShotTask(double promedio)
            {
                this.promedio = promedio;
            }
            public void run()
            {
                text_promedio_indicador.setText(""+link.imprimirDouble(promedio)+" %");
            }
        }

        runOnUiThread(new OneShotTask(promedio_indicador));
    }


    void updateAsyncBotones(boolean [] array_status)
    {
        class OneShotTask implements Runnable
        {
            boolean [] array_status;


            OneShotTask(boolean [] array_status)
            {
                this.array_status = array_status;
            }
            public void run()
            {

                guardar.setVisible(array_status[0]);
                cerrar.setVisible(array_status[1]);
                firmar.setVisible(array_status[2]);
                transferencia.setVisible(array_status[3]);
                indicacion.setVisible(array_status[4]);

            }
        }

        runOnUiThread(new OneShotTask(array_status));
    }





    public void avance_validacion()
    {
        System.out.println(" >>>>>>>>>>>>     LLAMADO A MAIN ACTIVITY DESDE ADAPTADOR   v2 .");

        final ProgressDialog progress_actualizar_count = ProgressDialog.show(EvaluadorCalidad.this, "Por favor espere", "Validando respuestas ...",true);
        progress_actualizar_count.setCancelable(false);


        new Thread(new Runnable()
        {
            public void run()
            {
////*******************************************************************************************************************************************************************************
                System.out.println(" ---  ---  ---  ---  ---  ---  ---  >  >  >  >  >  >  >  >   ENTRANDO A SET COUNT");

                double promedio_expediente = 0.0;

                int preguntas = 0;
                int respondidas = 0;
                int aprobadas = 0;
                int negativas = 0;
                int nas=0;

                int changes=0;


                            if(id_indicador_selected > 0)
                            {
                                ///Calculo del promedio del indicador.

                                if(id_evaluacion_calidad_registro_selected > 0)
                                {

                                    ///*****************************************************************************
                                    int array[];

                                    Evaluacion evax= linkDB.getEvaluacion("CALIDAD", evaluacion_calidad.id, linkDB.openDB());


                                    array = adaptadorEC.contarPreguntas();

                                    ///si esta cerrada, pedir count a segundo adaptador ...

                                    preguntas = array[0];
                                    respondidas = array[1];
                                    aprobadas = array[2];
                                    negativas = array[3];
                                    nas=array[4];

                                    changes=array[5];

                                    if(changes==1){
                                        cambios=true;
                                        System.out.println("-----  CAMBIOS : TRUE  [ OK ]");
                                    }else{
                                            cambios=false;
                                            System.out.println("-----  CAMBIOS : FALSE [ X ]");
                                        }


                                    double temp = (double) aprobadas / (preguntas-nas);
                                    promedio_expediente = ( temp ) * 100;

                                    System.out.println("PREGUNTAS: " + array[0] + ", APROBADAS: " + array[2] + ", NEGATIVAS:  "+array[3]+" ,NAS: " + array[4] + ", PROMEDIO : [ " + promedio_expediente + " ]");

                                    updateAsyncAvance(respondidas,preguntas,promedio_expediente);

                                    ///Calculo del promedio del indicador.
                                    List<EvaluacionCalidadRegistro> lista_expedientes = new ArrayList<>();
                                    lista_expedientes = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion_calidad.id,id_indicador_selected);

                                    double promedio_indicador=0;
                                    double suma_promedios=0;
                                    int total_expedientes=0;

                                    for(int i=0; i<lista_expedientes.size(); i++)
                                    {
                                        EvaluacionCalidadRegistro ecr =  lista_expedientes.get(i);
                                        int idECR = lista_expedientes.get(i).getId();
                                        total_expedientes++;

                                        if( idECR != id_evaluacion_calidad_registro_selected )
                                        {
                                            suma_promedios+= ecr.getPromedio();
                                        }
                                    }

                                    suma_promedios+=promedio_expediente;
                                    promedio_indicador = ( suma_promedios / (double) total_expedientes );


                                    updateAsyncPromedioIndicador( promedio_indicador );

                                    if(promedio_indicador < 80)
                                    {
                                        runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                text_promedio_indicador.setTextColor(getResources().getColor(R.color.error_color));
                                            }
                                        });
                                    }else{
                                        runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                text_promedio_indicador.setTextColor(getResources().getColor(R.color.PrimaryColor));
                                            }
                                        });
                                    }




                                    if(negativas>0)
                                    {
                                        requiere_hallazgo=true;
                                    }else{
                                        requiere_hallazgo=false;
                                    }


                                    if (requiere_hallazgo)
                                    {
                                        runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                spinner_acciones.setVisibility(View.VISIBLE);
                                                input_hallazgo.setVisibility(View.VISIBLE);
                                            }
                                        });

                                    } else {
                                        runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                spinner_acciones.setVisibility(View.GONE);
                                                input_hallazgo.setVisibility(View.GONE);
                                            }
                                        });
                                    }

                                    ///********************************************************************************


                                }else{

                                        System.out.println("DENTRO INDICADOR SELECCIONADO ( CASO DE SOLO INDICADOR )  ");

                                        List<EvaluacionCalidadRegistro> lista_expedientes = new ArrayList<>();
                                        lista_expedientes = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion_calidad.id,id_indicador_selected);

                                        double promedio_indicador=0;
                                        double suma_promedios=0;
                                        int total_expedientes=0;

                                        for(int i=0; i<lista_expedientes.size(); i++)
                                        {
                                            EvaluacionCalidadRegistro ecr =  lista_expedientes.get(i);
                                            int idECR = lista_expedientes.get(i).getId();
                                            total_expedientes++;

                                            suma_promedios+= ecr.getPromedio();

                                        }

                                        promedio_indicador = ( suma_promedios / (double) total_expedientes );

                                        updateAsyncPromedioIndicador(promedio_indicador);


                                        System.out.println("DENTRO INDICADOR SELECCIONADO ( CASO DE SOLO INDICADOR ). PROMEDIO INDICADOR : [" + link.imprimirDouble(promedio_indicador) + "] ");



                                       }///FIN ELSE



                            }else{

                                System.out.println("DENTRO INDICADOR SELECCIONADO  NULL ( CASO NULL TODO )  ");

                                    runOnUiThread(new Runnable()
                                    {
                                        public void run()
                                        {
                                            text_promedio_indicador.setText(" -- ");
                                            text_promedio_expediente.setText(" -- %");
                                            text_count.setText("- / -");
                                            text_promedio_expediente.setTextColor(getResources().getColor(R.color.black));

                                            System.out.println("DENTRO INDICADOR NULL  . PROMEDIO INDICADOR : [] ");

                                            spinner_acciones.setVisibility(View.GONE);
                                            input_hallazgo.setVisibility(View.GONE);
                                        }
                                    });

                                }



                    //}///fin si no hay expediente seleccionado



///****************************************     I N I C I O   DE  REVISION DE BOTONES
////*******************************************************************************************************************************************************************************

                ///  GUARDAR, CERRAR , FIRMAR , TRANSFERENCIA, INDICACIONES_INDICADOR
                final boolean status[]={false,false,false,false,false};

                Evaluacion evaluacion=linkDB.getEvaluacion("CALIDAD",idEvaluacionCalidad,linkDB.openDB());


                int id_evaluacion=idEvaluacionCalidad;
                int preguntas_pendientes=0;

                List<Indicador> listaI = new ArrayList<>();

                //listaI =link

                listaI = linkDB.getIndicadoresAgregadosCalidad(idEvaluacionCalidad);

                for(int i=0; i<listaI.size(); i++)
                {
                    int id_indicador=listaI.get(i).id;

                    List<EvaluacionCalidadRegistro> expedientes = linkDB.getExpedientesEvaluacionCalidadRegistro(idEvaluacionCalidad,id_indicador);

                    for(int j=0; j<expedientes.size(); j++ )
                    {
                        int id_ecr = expedientes.get(j).id;

                        if( expedientes.get(j).avanceCriterio < expedientes.get(j).totalCriterio )
                        {
                            preguntas_pendientes++;
                        }

                    }/// fin expedientes

                }///fin de la lista de indicadores pertenecientes a la evaluacion


                if(evaluacion.cerrado==1)
                {
                    status[0] = false;
                    status[1] = false;
                    status[2] = false;
                    status[3] = false;

                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            boton_agregar_indicador.setVisibility(View.INVISIBLE);
                            boton_agregar_expediente.setVisibility(View.INVISIBLE);

                            image_borrar_indicador.setVisibility(View.INVISIBLE);
                            image_borrar_expediente.setVisibility(View.INVISIBLE);
                        }
                    });



                    if(evaluacion.firma.equals(""))
                    {
                        status[2] = true;
                    }else{
                            status[2] = false;
                         }

                }else{   ///  EVALUACION NO ESTA CERRADA

                                runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        boton_agregar_indicador.setVisibility(View.VISIBLE);
                                        boton_agregar_expediente.setVisibility(View.VISIBLE);

                                        image_borrar_indicador.setVisibility(View.VISIBLE);
                                        image_borrar_expediente.setVisibility(View.VISIBLE);
                                    }
                                });

                                if(preguntas_pendientes > 0)  /// EXISTEN PREGUNTAS SIN RESPONDER Y GUARDAR
                                {

                                    System.out.println("-------------------------------->     EXISTEN CRITERIOS SIN RESPONDER !!!!!!!!!!!!!!!! ----------------");

                                    status[0] = true;
                                    status[1] = false;
                                    status[2] = false;
                                    status[3] = true;

                                }else{  ///   S I TODAS LAS PREGUNTAS TIENEN RESPUESTAS GUARDADAS

                                        status[0] = true;
                                        status[1] = true;
                                        status[2] = false;
                                        status[3] = true;

                                     }


                     }

                System.out.println("-------------------------------->     I D   INDICADOR  : [ "+id_indicador_selected+" ] ----------------");
                System.out.println("-------------------------------->     I D   EXPEDIENTE : [ "+id_evaluacion_calidad_registro_selected+" ] ----------------");

                if(id_evaluacion_calidad_registro_selected>0)
                {
                    if(evaluacion.cerrado==0)
                    {
                        status[0] = true;
                    }else{ status[0] = false; }

                }else{
                        status[0] = false ;
                    }


                if(id_indicador_selected>0)
                {
                    Indicador i_temp=linkDB.getIndicador(id_indicador_selected);

                    if(i_temp.indicacion != null)
                    {
                        status[4] = true;
                    }else {
                                status[4] = false;
                          }
                }else {
                        status[4] = false;
                      }



                System.out.println("-------------------------------->    ARRAY DE STATUS BOTONES : [ "+status.toString()+" ] ----------------");

                updateAsyncBotones(status);

                /*
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        guardar.setVisible(status[0]);
                        cerrar.setVisible(status[1]);
                        firmar.setVisible(status[2]);
                        transferencia.setVisible(status[3]);

                        ////  MANDAR ESTO A METODO CON CLASE ONE SHOT IGUAL QUE TEXT AVANCE
                    }
                });
                */

////*******************************************    F I N   DE REVISION DE BOTONES
////*******************************************************************************************************************************************************************************

                progress_actualizar_count.dismiss();

            }
        }).start();


    }







    Funciones link;
    DBManager linkDB;
    Context context;
    AlertDialog.Builder dialog;
    AlertDialog.Builder dialog_muestras;
    Button boton_ok_ficha;

    List<Indicador> lista_indicadores=new ArrayList<>();
    //Spinner spinner_indicadores;
    //ArrayAdapter<String> adaptador_indicadores;
    List<String> INDICADORES=new ArrayList<>();

    List<Indicador> lista_indicadores_agregados= new ArrayList<>();
    Spinner spinner_indicadores_agregados;
    ArrayAdapter<String> adaptador_indicadores_agregados;
    List<String> INDICADORES_AGREGADOS=new ArrayList<>();


    Spinner spinner_acciones;
    ArrayAdapter<String> adaptador_acciones;
    List<String> acciones=new ArrayList<>();

    List<Accion> listaAcciones = new ArrayList<>();

    Spinner spinner_expedientes;
    ArrayAdapter<String> adaptador_expedientes;
    List<String> EXPEDIENTES = new ArrayList<>();
    List<EvaluacionCalidadRegistro> lista_expedientes = new ArrayList<>();


    EditText input_hallazgo;
    TextView text_count;
    TextView text_promedio_expediente;
    TextView text_promedio_indicador;

    String accion_tomada="";
    int id_accion_tomada=0;

    Menu menu;
    MenuItem item;


    private CoordinatorLayout coordinatorLayout;
    private LinearLayout layout_hallazgo;

    private RelativeLayout layout_indicadores;
    private RelativeLayout layout_label_indicador;
    private RelativeLayout layout_expedientes;

    TextView text_nombre;
    TextView text_clave;
    TextView text_ubicacion;
    TextView text_jurisdiccion;
    TextView text_municipio;
    TextView text_localidad;
    TextView text_tipologia;
    TextView text_tipo;

    ImageView image_firma;
    TextView text_responsable_info;

    TextView text_indicaciones;

    Clues clues_selected;
    Usuarios user_signed=null;

    Evaluacion evaluacion_calidad;
    int idEvaluacionCalidad=0;
    int nivel_cone=0;




    int id_indicador_selected=0;
    int position_indicador_selected=0;

    int last_id_indicador_selected=0;
    int last_position_indicador_selected=0;

    int position_expediente_selected=0;
    String expediente_selected=null;
    int id_evaluacion_calidad_registro_selected=0;

    int last_position_expediente_selected=0;
    int last_id_ecr_selected=0;



    int id_expediente_selected; // mismo que id_evaluacon_calidad_registro
    EvaluacionCalidadRegistro ECR_Selected=null;


    RecyclerView recycler_evaluador;
    AdaptadorEvaluadorCalidad adaptadorEC;


    boolean mostrar_agregar=false;
    boolean requiere_hallazgo=false;

    ImageButton boton_agregar_indicador;

    ///Button boton_guardar_expediente;

    ImageButton boton_agregar_expediente;

    ImageButton image_borrar_indicador;
    ImageButton image_borrar_expediente;




    ///EditText edit_expediente;

    boolean cambios=false;
    boolean caso_error_guardar=false;

    boolean caso_indicador_cancelar=false;
    boolean setBackData=false;


    List<DataEvaluadorCalidad> dec_temp;


    List<CriterioValidacionRespuesta> respuestasValidacionExpediente = new ArrayList<>();



    ProgressDialog progress_dialog_guardar;
    ProgressDialog progress_refresh_indicadores_agregados;
    ProgressDialog progress_cargar_criterios;


    List<DataEvaluadorCalidad> DEC;


    int cerrado=0;








    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        context = this.getApplicationContext();
        link = new Funciones();
        linkDB=new DBManager(context);
        user_signed = linkDB.getSignedUser();

        if(user_signed==null)
        {
            link.goLogin(this);
        }

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle vars = getIntent().getExtras();
        if (vars != null)
        {
            idEvaluacionCalidad = vars.getInt("idEvaluacionCalidad");
        }

        setContentView(R.layout.layout_evaluador_calidad);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        //layout_hallazgo=(LinearLayout)this.findViewById(R.id.layout_hallazgo);

        layout_indicadores = (RelativeLayout) findViewById(R.id.layout_indicadores);
        layout_label_indicador = (RelativeLayout) findViewById(R.id.layout_labels_indicador);
        layout_expedientes = (RelativeLayout) findViewById(R.id.layout_expedientes);

        //layout_agregar_expediente.setVisibility(View.GONE);

        boton_agregar_indicador = (ImageButton) findViewById(R.id.image_agregar_indicador);

        boton_agregar_expediente = (ImageButton) findViewById(R.id.boton_agregar_expediente);

        //boton_guardar_expediente = (Button) findViewById(R.id.boton_guardar_expediente);
        image_borrar_indicador = (ImageButton) findViewById(R.id.boton_borrar_indicador);
        image_borrar_expediente = (ImageButton) findViewById(R.id.boton_borrar_expediente);

        //edit_expediente = (EditText) findViewById(R.id.input_expediente);



        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_evaluador_calidad);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmSalir();

            }
        });



        System.out.println("ID EVALUACION ENVIAR : " + idEvaluacionCalidad + ".]]]");

        evaluacion_calidad= linkDB.getEvaluacion("CALIDAD", idEvaluacionCalidad, linkDB.openDB());
        if(evaluacion_calidad==null)
            {
              System.out.println("ERROR EVALUACION CALIDAD REGRESO NULL....]]]");
            }
        clues_selected=linkDB.getClues(evaluacion_calidad.clues);
        nivel_cone=clues_selected.idCone;

        cerrado = evaluacion_calidad.getCerrado();

        getSupportActionBar().setTitle(clues_selected.clues);
        toolbar.setSubtitle(evaluacion_calidad.fechaEvaluacion);


        //lista_indicadores.add(new Indicador(-1, " : : ", " : : ", "", ""));
/*
        lista_indicadores.addAll(linkDB.getIndicadoresEvaluacionCalidad(nivel_cone));

        if(lista_indicadores.size()>0)
        {
            for(int i=0; i<lista_indicadores.size(); i++)
            {
                INDICADORES.add(String.valueOf(lista_indicadores.get(i).codigo + " - " + lista_indicadores.get(i).nombre));
            }
        }else{
                  System.out.println("ERROR: LISTA DE INDICADORES REGRESÓ VACIA ]]]");
             }

        //refreshSpinnerIndicadoresAgregados();

        listaAcciones = linkDB.getAcciones();

        if(listaAcciones.size()>0)
        {

            acciones.add("RESOLUTIVA");
            for(int i=0; i<listaAcciones.size(); i++)
            {
                if(listaAcciones.get(i).getTipo().toString().equals("R"))
                {
                    acciones.add(String.valueOf(listaAcciones.get(i).getNombre()));
                }
            }


            acciones.add("SEGUIMIENTO");
            for(int i=0; i<listaAcciones.size(); i++)
            {
                if(listaAcciones.get(i).getTipo().toString().equals("S"))
                {
                    acciones.add(String.valueOf(listaAcciones.get(i).getNombre()));
                }
            }

        }else{
                 System.out.println("ERROR: LISTA DE ACCIONES REGRESÓ VACIA ]]]");
             }

*/



        listaAcciones = linkDB.getAcciones();

        if(listaAcciones.size()>0)
        {

            acciones.add("RESOLUTIVA");
            for(int i=0; i<listaAcciones.size(); i++)
            {
                if(listaAcciones.get(i).getTipo().toString().equals("R"))
                {
                    acciones.add(String.valueOf(listaAcciones.get(i).getNombre()));
                }
            }


            acciones.add("SEGUIMIENTO");
            for(int i=0; i<listaAcciones.size(); i++)
            {
                if(listaAcciones.get(i).getTipo().toString().equals("S"))
                {
                    acciones.add(String.valueOf(listaAcciones.get(i).getNombre()));
                }
            }

        }else{
            System.out.println("ERROR: LISTA DE ACCIONES REGRESÓ VACIA ]]]");
        }


        //adaptador_indicadores = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, INDICADORES);
        //spinner_indicadores = (Spinner) findViewById(R.id.spinner_indicadores);
        //spinner_indicadores.setAdapter(adaptador_indicadores);


        adaptador_indicadores_agregados = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, INDICADORES_AGREGADOS);

        spinner_indicadores_agregados = (Spinner) findViewById(R.id.spinner_indicadores_agregados);
        spinner_indicadores_agregados.setAdapter(adaptador_indicadores_agregados);


        ////////    refreshSpinnerIndicadoresAgregados();


        adaptador_expedientes = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, EXPEDIENTES);

        spinner_expedientes = (Spinner) findViewById(R.id.spinner_expedientes);
        spinner_expedientes.setAdapter(adaptador_expedientes);



        adaptador_acciones = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, acciones );

        spinner_acciones = (Spinner) findViewById(R.id.spinner_accion);
        spinner_acciones.setAdapter(adaptador_acciones);


        input_hallazgo = (EditText) findViewById(R.id.input_hallazgo);

        spinner_acciones.setVisibility(View.GONE);
        input_hallazgo.setVisibility(View.GONE);

        dialog = new AlertDialog.Builder(this);


        text_count= (TextView) findViewById(R.id.text_count);
        text_promedio_expediente= (TextView) findViewById(R.id.text_promedio_expediente);
        text_promedio_indicador= (TextView) findViewById(R.id.text_resultado_indicador);





        recycler_evaluador = (RecyclerView) findViewById(R.id.recycler_evaluador_calidad);

        final LinearLayoutManager linearLM = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                                  linearLM.setOrientation(LinearLayoutManager.VERTICAL);

        recycler_evaluador.setLayoutManager(linearLM);
        recycler_evaluador.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);

        recycler_evaluador.addItemDecoration(itemDecoration);
        recycler_evaluador.setItemAnimator(new DefaultItemAnimator());


            listaAcciones = linkDB.getAcciones();

            if(listaAcciones.size()>0)
            {

                acciones.add("RESOLUTIVA");
                for(int i=0; i<listaAcciones.size(); i++)
                {
                    if(listaAcciones.get(i).getTipo().toString().equals("R"))
                    {
                        acciones.add(String.valueOf(listaAcciones.get(i).getNombre()));
                    }
                }


                acciones.add("SEGUIMIENTO");
                for(int i=0; i<listaAcciones.size(); i++)
                {
                    if(listaAcciones.get(i).getTipo().toString().equals("S"))
                    {
                        acciones.add(String.valueOf(listaAcciones.get(i).getNombre()));
                    }
                }

            }else{
                    System.out.println("ERROR: LISTA DE ACCIONES REGRESÓ VACIA ]]]");
                }



        spinner_acciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                accion_tomada = acciones.get(position);

                //link.printToast("SELECCIONADA 1 ------->>  ACCION: "+accion_tomada+", ID ACCION: "+id_accion_tomada+".",context);


                for (int i = 0; i < listaAcciones.size(); i++)
                {
                    if (accion_tomada.equals(listaAcciones.get(i).getNombre()))
                    {
                        id_accion_tomada = listaAcciones.get(i).getId();
                        //link.printToast("SELECCIONADA 2 ------->>  ACCION: "+accion_tomada+", ID ACCION: "+id_accion_tomada+".",context);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });





        boton_agregar_indicador.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialogAgregarIndicadores();
            }
        });


        boton_agregar_expediente.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                showDialogAgregarExpediente();

            }
        });



/*
        spinner_indicadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
*/


///*************************************************************************************************
///*************************************************************************************************
        spinner_indicadores_agregados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id)
            {
                if (cambios)
                {
                    if (setBackData)
                    {
                        //id_indicador_selected = last_id_indicador_selected;
                        //position_indicador_selected = last_position_indicador_selected;

                        id_indicador_selected = lista_indicadores_agregados.get(position).getId();
                        position_indicador_selected = position;

                        setBackData=false;
                        ///NO REFRESQUES NADA, YA DEJALO ASI :)
                    }else {

                            ///*************************************************************************************************
                            AlertDialog.Builder builder = new AlertDialog.Builder(EvaluadorCalidad.this);

                            builder.setCancelable(false);
                            builder.setMessage("Existen cambios sin guardar. \n¿Esta seguro que desea cambiar de indicador ?")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {

                                            setBackData = false;
                                            cambios = false;

                                            id_indicador_selected = lista_indicadores_agregados.get(position).getId();
                                            position_indicador_selected = position;

                                            refreshSpinnerExpedientes(id_indicador_selected,false);
                                            //************************************************************************************


                                            if(id_indicador_selected>0) {
                                                                            crearDialogIndicaciones();
                                                                        }


                                        }
                                    })
                                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            setBackData = true;
                                            spinner_indicadores_agregados.setSelection(last_position_indicador_selected);

                                            dialog.cancel();
                                        }
                                    }).show();
                            ///*************************************************************************************************

                        }

                }else{
                        id_indicador_selected = lista_indicadores_agregados.get(position).getId();
                        position_indicador_selected = position;

                        setBackData=false;

                        refreshSpinnerExpedientes(id_indicador_selected,false);
                        spinner_indicadores_agregados.setSelection(position);

                        if(id_indicador_selected>0) {
                                                        crearDialogIndicaciones();
                                                    }

                     }

                last_id_indicador_selected = id_indicador_selected;
                last_position_indicador_selected = position_indicador_selected;


                //setCount();

                if(id_indicador_selected>0)
                {
                    layout_label_indicador.setVisibility(View.VISIBLE);
                }else{
                        layout_label_indicador.setVisibility(View.INVISIBLE);
                     }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

///*************************************************************************************************
///*************************************************************************************************


        spinner_expedientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id)
            {

                ///*************************************************************************************************
                if(cambios)
                {
                    if(setBackData)
                    {
                        id_evaluacion_calidad_registro_selected = lista_expedientes.get(position).getId();
                        position_expediente_selected = position;

                        expediente_selected = lista_expedientes.get(position).expediente;

                        setBackData=false;

                    }else{
                            ///*************************************************************************************************
                            AlertDialog.Builder builder = new AlertDialog.Builder(EvaluadorCalidad.this);

                            builder.setCancelable(false);
                            builder.setMessage("Existen cambios sin guardar. Se perderan algunas respuestas \n\r¿Esta seguro que desea cambiar de Expediente ?")
                                    .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {

                                            setBackData=false;
                                            cambios = false;

                                            id_evaluacion_calidad_registro_selected = lista_expedientes.get(position).getId();
                                            position_expediente_selected = position;

                                            expediente_selected = lista_expedientes.get(position).expediente;

                                            //cargarCriteriosExpediente();
                                            new CargarCriteriosExpedienteAsync().execute();

                                        }
                                    })
                                    .setNegativeButton("Cancelar",

                                            new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog,int id)
                                        {
                                            setBackData=true;
                                            spinner_expedientes.setSelection(last_position_expediente_selected);

                                            dialog.cancel();
                                        }
                                    }).show();
                            ///*************************************************************************************************
                         }

                }else{

                        id_evaluacion_calidad_registro_selected = lista_expedientes.get(position).getId();
                        position_expediente_selected = position;

                        expediente_selected=lista_expedientes.get(position).expediente;

                        //cargarCriteriosExpediente();
                        new CargarCriteriosExpedienteAsync().execute();

                      }


                last_id_ecr_selected = id_evaluacion_calidad_registro_selected;
                last_position_expediente_selected = position_expediente_selected;

                expediente_selected = lista_expedientes.get(position_expediente_selected).expediente;

                ///*************************************************************************************************


            }///  F I N   SPINNER ITEM SELECTED

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///cumple




///*************************************************************************************************
///*************************************************************************************************



        image_borrar_expediente.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(id_evaluacion_calidad_registro_selected > 0)
                {

                    ///*************************************************************************************************
                    AlertDialog.Builder builder = new AlertDialog.Builder( EvaluadorCalidad.this,R.style.TemaDialogMaterial );

                    builder.setCancelable(false);
                    builder.setMessage("¿ Confirma borrar el expediente del indicador ?")
                            .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int id)
                                {

                                    borrarExpedienteDeIndicador();

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



                }else{
                         link.showSnackBar(context,coordinatorLayout,"Seleccione expediente valido para borrar","ERROR");
                     }


            }
        });


///*************************************************************************************************
///*************************************************************************************************


        image_borrar_indicador.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(id_indicador_selected>0)
                {


                    ///*************************************************************************************************
                    AlertDialog.Builder builder = new AlertDialog.Builder( EvaluadorCalidad.this,R.style.TemaDialogMaterial );

                    builder.setCancelable(false);
                    builder.setMessage("¿ Confirma borrar el indicador y sus expedientes de la evaluación ?")
                            .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int id)
                                {

                                    borrarIndicadorDeEvaluacion();

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


                }else{
                         link.showSnackBar(context,coordinatorLayout,"Seleccione indicador valido para borrar","ERROR");
                     }

            }
        });


///*************************************************************************************************
///*************************************************************************************************



        recycler_evaluador.addOnItemTouchListener(new RecyclerItemClickListener(this, recycler_evaluador, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, final int position)
            {

                if(evaluacion_calidad.cerrado==0)
                {
                    List<DataEvaluadorCalidad> respuestas_live = adaptadorEC.getRespuestas();

                    int tv = respuestas_live.get(position).tieneValidacion;

                    if(tv==1)
                    {
                        showDialogValidarCriterio(respuestas_live.get(position).idCriterio);
                    }
                }




            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                // ...
            }
        }));


        refreshSpinnerIndicadoresAgregados();


    }    /////***************************  F I N   M E T O D O    O N  C R E A T E



///*************************************************************************************************
///*************************************************************************************************

    public void onStart()
    {
        super.onStart();



            lista_indicadores.addAll(linkDB.getIndicadoresEvaluacionCalidad(nivel_cone));

            if(lista_indicadores.size()>0)
            {
                for(int i=0; i<lista_indicadores.size(); i++)
                {
                    INDICADORES.add(String.valueOf(lista_indicadores.get(i).codigo + " - " + lista_indicadores.get(i).nombre));
                }
            }else{
                    System.out.println("ERROR: LISTA DE INDICADORES REGRESÓ VACIA ]]]");
                }

            //refreshSpinnerIndicadoresAgregados();

        /*
            listaAcciones = linkDB.getAcciones();

            if(listaAcciones.size()>0)
            {

                acciones.add("RESOLUTIVA");
                for(int i=0; i<listaAcciones.size(); i++)
                {
                    if(listaAcciones.get(i).getTipo().toString().equals("R"))
                    {
                        acciones.add(String.valueOf(listaAcciones.get(i).getNombre()));
                    }
                }


                acciones.add("SEGUIMIENTO");
                for(int i=0; i<listaAcciones.size(); i++)
                {
                    if(listaAcciones.get(i).getTipo().toString().equals("S"))
                    {
                        acciones.add(String.valueOf(listaAcciones.get(i).getNombre()));
                    }
                }

            }else{
                    System.out.println("ERROR: LISTA DE ACCIONES REGRESÓ VACIA ]]]");
                }


        */




    }

///*************************************************************************************************
///*************************************************************************************************
    public void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

///*************************************************************************************************
///*************************************************************************************************

    private class ShowAvance extends AsyncTask<String, Integer, Integer>
    {
        protected Integer doInBackground(String... datos)
        {
             int errors=0;

             try {
                   Thread.sleep(300);
                 ///METER THREAD COMPLETO AQUI Y DESTRUIRLO
                 } catch (InterruptedException e)
                   { e.printStackTrace(); }


            return errors;
        }

        protected void onProgressUpdate(Integer... values)
        {
        }

        protected void onPreExecute()
        {
        }

        protected void onPostExecute(Integer errors)
        {

            //setCount();
            
        }

        protected void onCancelled() {

            link.showSnackBar(getApplicationContext(),coordinatorLayout,"Cancelado","ERROR");
        }

    }///FIN CLASS DESCARGA ASYNC

///*************************************************************************************************
///*************************************************************************************************



    private class GuardarEvaluacionAsync extends AsyncTask<String, Integer, String[]>
    {
        String hallazgo_input_value="";

        protected void onPreExecute()
        {
            progress_dialog_guardar = new ProgressDialog(EvaluadorCalidad.this);
            progress_dialog_guardar.setTitle("Guardar");
            progress_dialog_guardar.setMessage("Guardando criterios ...");
            progress_dialog_guardar.setCancelable(false);
            progress_dialog_guardar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress_dialog_guardar.setIndeterminate(true);

            progress_dialog_guardar.show();
        }

        protected String[] doInBackground( String... datos )
        {
            int errors=0;
            String messages="";

            String data[] = {"0",""};

            hallazgo_input_value = datos[0];


            List<DataEvaluadorCalidad> respuestas = new ArrayList<>();
            respuestas = adaptadorEC.getRespuestas();


            int array[]=adaptadorEC.contarPreguntas();
            int preguntas=array[0];
            int respondidas=array[1];
            int aprobadas=array[2];
            int negativas=array[3];
            int nas=array[4];


            if(requiere_hallazgo==true)
            {

                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        hallazgo_input_value = input_hallazgo.getText().toString().trim();
                    }
                });


                System.out.println("\n \n --------------------------------     HALLAZGO INPUT VALUE: [ "+hallazgo_input_value+" ] -----------------------------------\n \n");

                if(hallazgo_input_value.equals(""))
                {
                    errors++;
                    messages+=" * Escriba el hallazgo. \n";
                }

                if (accion_tomada.equals("RESOLUTIVA") || accion_tomada.equals("SEGUIMIENTO") )
                {
                    errors++;
                    messages+=" * Seleccione una Accion valida. ";
                }


            }


            if(errors==0)
            {
                 if(requiere_hallazgo==true)
                {

                    Hallazgo hallazgo =linkDB.getHallazgo("CALIDAD",idEvaluacionCalidad,id_indicador_selected,expediente_selected);

                    if(hallazgo==null)
                    {
                        hallazgo=new Hallazgo(
                                                    0,
                                                    evaluacion_calidad.id,
                                                    "CALIDAD",
                                                    id_indicador_selected,
                                                    expediente_selected,
                                                    user_signed.id,
                                                    id_accion_tomada,
                                                    0,
                                                    0,
                                                    hallazgo_input_value,
                                                    link.getFecha(),
                                                    "0000-00-00 00:00:00",
                                                    "0000-00-00 00:00:00"
                                             );

                        linkDB.insertarHallazgo(hallazgo,linkDB.openDB());

                    }else{
                            hallazgo.setIdAccion(id_accion_tomada);
                            hallazgo.setDescripcion(hallazgo_input_value);
                            hallazgo.setModificadoAl(link.getFecha());

                            linkDB.actualizarHallazgo(hallazgo,linkDB.openDB());
                         }

                    //input_hallazgo.setText("");
                    //spinner_acciones.setSelection(0);

                }else{  // SE BORRAN HALLAZGOS ESCRITOS QUE AL FINAL SE DESCARTAN
                            Hallazgo hallazgo =linkDB.getHallazgo("CALIDAD",idEvaluacionCalidad,id_indicador_selected,expediente_selected);

                            if(hallazgo!=null)
                            {
                                hallazgo.setBorradoAl(link.getFecha());
                                linkDB.actualizarHallazgo(hallazgo,linkDB.openDB());

                                runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        input_hallazgo.setText("");
                                        spinner_acciones.setSelection(0);
                                    }
                                });
                            }
                     }


                // recorrido de array de respuestas del adaptador del recyclerview

                for (int i = 0; i < respuestas.size(); i++)
                {

                    EvaluacionCalidadCriterio ecc_temp = linkDB.getEvaluacionCalidadCriterio(respuestas.get(i).idEvaluacionCalidad,
                            respuestas.get(i).idIndicador,
                            respuestas.get(i).idCriterio,
                            respuestas.get(i).idEvaluacionCalidadRegistro);

                    if(ecc_temp == null && respuestas.get(i).respuesta >= 0 && respuestas.get(i).modificado==1)
                    {
                        EvaluacionCalidadCriterio respuesta = new EvaluacionCalidadCriterio(
                                0,
                                respuestas.get(i).idEvaluacionCalidad,
                                respuestas.get(i).idCriterio,
                                respuestas.get(i).idIndicador,
                                respuestas.get(i).respuesta,
                                respuestas.get(i).idEvaluacionCalidadRegistro,
                                link.getFecha(),
                                "0000-00-00 00:00:00",
                                "0000-00-00 00:00:00"

                        );

                        int insert = linkDB.insertarEvaluacionCalidadCriterio(respuesta, linkDB.openDB());
                    }
                    if(ecc_temp != null && respuestas.get(i).respuesta >= 0 && respuestas.get(i).modificado==1)
                    {

                        EvaluacionCalidadCriterio respuesta = new EvaluacionCalidadCriterio(
                                respuestas.get(i).getIdEvaluacionCalidadCriterio(),
                                respuestas.get(i).getIdEvaluacionCalidad(),
                                respuestas.get(i).idCriterio,
                                respuestas.get(i).idIndicador,
                                respuestas.get(i).respuesta,
                                respuestas.get(i).idEvaluacionCalidadRegistro,
                                link.getFecha(),
                                link.getFecha(),
                                "0000-00-00 00:00:00"

                        );

                        linkDB.actualizarEvaluacionCalidadCriterio(respuesta, linkDB.openDB());

                    }



                    //si fué modificada la respuesta validar update ó insert
                    if (respuestas.get(i).modificado == 1)
                    {
                        // si id no existe, se insertará registro


                        /*

                        if (respuestas.get(i).idEvaluacionCalidadCriterio == 0)
                        {
                            EvaluacionCalidadCriterio respuesta = new EvaluacionCalidadCriterio(
                                                                                                    0,
                                                                                                    respuestas.get(i).idEvaluacionCalidad,
                                                                                                    respuestas.get(i).idCriterio,
                                                                                                    respuestas.get(i).idIndicador,
                                                                                                    respuestas.get(i).respuesta,
                                                                                                    respuestas.get(i).idEvaluacionCalidadRegistro,
                                                                                                    link.getFecha(),
                                                                                                    "0000-00-00 00:00:00",
                                                                                                    "0000-00-00 00:00:00"

                                                                                               );

                            int insert = linkDB.insertarEvaluacionCalidadCriterio(respuesta, linkDB.openDB());


                        } else { //update

                                    EvaluacionCalidadCriterio respuesta = new EvaluacionCalidadCriterio(
                                                                                                            respuestas.get(i).getIdEvaluacionCalidadCriterio(),
                                                                                                            respuestas.get(i).getIdEvaluacionCalidad(),
                                                                                                            respuestas.get(i).idCriterio,
                                                                                                            respuestas.get(i).idIndicador,
                                                                                                            respuestas.get(i).respuesta,
                                                                                                            respuestas.get(i).idEvaluacionCalidadRegistro,
                                                                                                            link.getFecha(),
                                                                                                            link.getFecha(),
                                                                                                            "0000-00-00 00:00:00"

                                                                                                       );

                            linkDB.actualizarEvaluacionCalidadCriterio(respuesta, linkDB.openDB());

                            //actualizar expediente EvaluacionCalidadRegistro // ya no aqui solo una vez fuera de else
                        }//FIN ELSE INSERTAR
                        */

                        //solo un vz update exp aqui
                        int cumple;
                        if(negativas>0)
                        { cumple=0; }else{ cumple=1; }


                        double temp = (double) aprobadas / (preguntas-nas);
                        double promedio = ( temp ) * 100;

                        EvaluacionCalidadRegistro ecr_temp =  linkDB.getEvaluacionCalidadRegistro(id_evaluacion_calidad_registro_selected);

                        ecr_temp.setCumple(cumple);
                        ecr_temp.setPromedio(promedio);
                        ecr_temp.setTotalCriterio(preguntas);
                        ecr_temp.setAvanceCriterio(respondidas);

                        ecr_temp.setModificadoAl(link.getFecha());

                        linkDB.actualizarEvaluacionCalidadRegistro(ecr_temp, linkDB.openDB());


                    }//FIN IF RESPUESTAS MODIFICADA -- INSERT O UPDATE REALIZADO

                }///   FIN FOR RECORRIDO DE RESPUESTAS O CRITERIOS EN EL RECYCLER



                data[0]=errors+"";
                data[1]=messages;


                //revisionBotones();


                cambios=false;
                adaptadorEC.cambios=false;
                caso_indicador_cancelar=false;


                // FIN IF ERRORS  == 0
            }else{

                    data[0]=errors+"";
                    data[1]=messages;

                 }


            return data;
        }



        protected void onProgressUpdate(Integer... values)
        {

        }


        protected void onPostExecute(String[] data)
        {

            int errors= Integer.parseInt(data[0]);
            String messages = data[1];

            progress_dialog_guardar.dismiss();

            if(errors>0)
            {
                link.showSnackBar(getApplicationContext(),coordinatorLayout,messages, "ERROR");
            }else {
                      link.showSnackBar(getApplicationContext(),coordinatorLayout,"Evaluacion guardada correctamente", "INFO");
                  }

            avance_validacion();

        }

        protected void onCancelled()
        {
            link.showSnackBar(getApplicationContext(),coordinatorLayout,"Cancelado","ERROR");
        }

    }///FIN CLASS DESCARGA ASYNC


///*************************************************************************************************
///*************************************************************************************************
private class RefreshSpinnerIndicadoresAsync extends AsyncTask<String, Integer, Integer>
{

        protected void onPreExecute()
        {
            progress_refresh_indicadores_agregados = ProgressDialog.show(EvaluadorCalidad.this, "Por favor espere", "Cargando Indicadores ...",true);
            progress_refresh_indicadores_agregados.setCancelable(false);
        }


        protected Integer doInBackground(String... datos)
        {
            int errors=0;
            String messages="";

            String data[]={""+errors,messages};




            lista_indicadores_agregados.clear();
            INDICADORES_AGREGADOS.clear();

            lista_indicadores_agregados.add(new Indicador(-1, "Seleccione indicador", " : : ", "", "",""));
            lista_indicadores_agregados.addAll(linkDB.getIndicadoresAgregadosCalidad(idEvaluacionCalidad));

            if(lista_indicadores_agregados.size()>0)
            {
                for(int i=0; i<lista_indicadores_agregados.size(); i++)
                {
                    if(i==0)
                    {
                        INDICADORES_AGREGADOS.add(String.valueOf( lista_indicadores_agregados.get(i).codigo) );
                    }else{
                        INDICADORES_AGREGADOS.add(String.valueOf( lista_indicadores_agregados.get(i).codigo+" - "+lista_indicadores_agregados.get(i).nombre) );
                    }
                }
            }else{
                System.out.println("ERROR: LISTA DE INDICADORES AGREGADOS REGRESÓ VACIA ]]]");
            }

            adaptador_indicadores_agregados.notifyDataSetChanged();




            return errors;
        }

        protected void onProgressUpdate(Integer... values)
        {
        }


        protected void onPostExecute(Integer errors)
        {
            progress_refresh_indicadores_agregados.dismiss();
            spinner_indicadores_agregados.setSelection(0,true);
            //setCount();

        }

        protected void onCancelled()
        {
            link.showSnackBar(getApplicationContext(),coordinatorLayout,"Refresh Indicadores cancelado","ERROR");
        }

}///FIN CLASS REFRESH INDICADORES IN ASYNC




///*************************************************************************************************
///*************************************************************************************************

    public void refreshSpinnerIndicadoresAgregados()
    {
        new RefreshSpinnerIndicadoresAsync().execute();

        //HACER ESTE METODO EN CLASS ASYNC , GUARDAR EL VALOR FINAL DE LAS CONDICIONES EN VARS
        // Y AL FINAL EN ON POST EXECUTE EJECUTAR

        /*
        progress_refresh_indicadores_agregados = ProgressDialog.show(EvaluadorCalidad.this, "Por favor espere", "Cargando Indicadores ...",true);
        progress_refresh_indicadores_agregados.setCancelable(false);

        new Thread(new Runnable()
        {
            public void run()
            {


 /////*************************************************************************************************************************************************************

            lista_indicadores_agregados.clear();
            INDICADORES_AGREGADOS.clear();

            lista_indicadores_agregados.add(new Indicador(-1, "Seleccione indicador", " : : ", "", "",""));
            lista_indicadores_agregados.addAll(linkDB.getIndicadoresAgregadosCalidad(idEvaluacionCalidad));

            if(lista_indicadores_agregados.size()>0)
            {
                for(int i=0; i<lista_indicadores_agregados.size(); i++)
                {
                    if(i==0)
                    {
                        INDICADORES_AGREGADOS.add(String.valueOf( lista_indicadores_agregados.get(i).codigo) );
                    }else{
                            INDICADORES_AGREGADOS.add(String.valueOf( lista_indicadores_agregados.get(i).codigo+" - "+lista_indicadores_agregados.get(i).nombre) );
                         }
                }
            }else{
                    System.out.println("ERROR: LISTA DE INDICADORES AGREGADOS REGRESÓ VACIA ]]]");
                 }

                adaptador_indicadores_agregados.notifyDataSetChanged();
            //spinner_indicadores_agregados.setSelection(0,true);
///*****************************************************************************************************************************************************************

                progress_refresh_indicadores_agregados.dismiss();

            }
        }).start();
*/

    }



///*************************************************************************************************
///*************************************************************************************************




    public void refreshSpinnerExpedientes(int id_indicador_selected, boolean especial)
    {

        //se llena el spinner de expedientes grabados en la base de datos

        if( id_indicador_selected > 0)
        {
            layout_expedientes.setVisibility(View.VISIBLE);

            lista_expedientes.clear();
            lista_expedientes.add( new EvaluacionCalidadRegistro(
                                                                    -1,
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    "Seleccione expediente",
                                                                    0,
                                                                    0.0,
                                                                    0,
                                                                    0,
                                                                    "",
                                                                    "",
                                                                    "" ) );

            lista_expedientes.addAll(linkDB.getExpedientesEvaluacionCalidadRegistro(idEvaluacionCalidad, id_indicador_selected));

            EXPEDIENTES.clear();


            for (int i = 0; i < lista_expedientes.size(); i++)
            {
                EXPEDIENTES.add(String.valueOf(lista_expedientes.get(i).getExpediente()));
            }

            adaptador_expedientes.notifyDataSetChanged();


            if(especial==true)
            {
                spinner_expedientes.setSelection(EXPEDIENTES.size()-1,true);
            }else {
                    spinner_expedientes.setSelection(0,true);
                  }

        }else{
                lista_expedientes.clear();
                lista_expedientes.add(new EvaluacionCalidadRegistro(
                                                                        -1,
                                                                        0,
                                                                        0,
                                                                        0,
                                                                        "Seleccione expediente",
                                                                        0,
                                                                        0.0,
                                                                        0,
                                                                        0,
                                                                        "",
                                                                        "",
                                                                        ""));

                EXPEDIENTES.clear();
                EXPEDIENTES.add("Seleccione expediente");
                adaptador_expedientes.notifyDataSetChanged();

                layout_expedientes.setVisibility(View.INVISIBLE);

        }

    }// FIN REFRESH SPINNER EXPEDIENTES




///*************************************************************************************************
///*************************************************************************************************


/*

    public void checkHallasgos()
    {
        //link.printToast("EVALUACION: [ " + idEvaluacionCalidad + " ] INDICADOR : [ " + id_indicador_selected + " ]", this);



        Hallazgo hallazgo_set= linkDB.getHallazgo("CALIDAD",idEvaluacionCalidad,id_indicador_selected,expediente_selected);

        if(hallazgo_set!=null)
        {
            input_hallazgo.setText(hallazgo_set.descripcion);

            int id_accion_selected = hallazgo_set.getIdAccion();
            String nombre_accion = "";

            for(int i=0; i<listaAcciones.size(); i++)
            {
                if(listaAcciones.get(i).getId()==id_accion_selected)
                {
                    nombre_accion = listaAcciones.get(i).getNombre();
                }
            }

            for(int j=0; j<acciones.size(); j++)
            {
                if(nombre_accion.equals(acciones.get(j)))
                {
                    spinner_acciones.setSelection(j);
                }
            }



        }else{
                    input_hallazgo.setText("");
                    spinner_acciones.setSelection(0);
                  ///  link.printToast("HALLAZGO NULL PARA ESTE INDICADOR", this);
             }

    }

*/

///*************************************************************************************************
///*************************************************************************************************


    public void revisionBotones()
    {



        Evaluacion evaluacion=linkDB.getEvaluacion("CALIDAD",idEvaluacionCalidad,linkDB.openDB());


        int id_evaluacion=idEvaluacionCalidad;
        int preguntas_pendientes=0;

        List<Indicador> listaI = new ArrayList<>();

        //listaI =link

        listaI = linkDB.getIndicadoresAgregadosCalidad(idEvaluacionCalidad);

        for(int i=0; i<listaI.size(); i++)
        {
            int id_indicador=listaI.get(i).id;

            List<EvaluacionCalidadRegistro> expedientes = linkDB.getExpedientesEvaluacionCalidadRegistro(idEvaluacionCalidad,id_indicador);

            for(int j=0; j<expedientes.size(); j++ )
            {
                int id_ecr = expedientes.get(j).id;

                List<LugarVerificacion> listaLV = new ArrayList<>();
                listaLV = linkDB.getLugaresVerificacion(nivel_cone,id_indicador);

                for (int k = 0; k < listaLV.size(); k++)
                {
                    int id_lugar = listaLV.get(k).id;

                    List<Criterio> listaC = new ArrayList<>();
                    listaC = linkDB.getCriterios(nivel_cone, id_indicador, id_lugar);

                    for (int h = 0; h < listaC.size(); h++)
                    {
                        int id_criterio = listaC.get(h).id;

                        //EvaluacionRecursoCriterio erc = linkDB.getEvaluacionRecursoCriterio(id_evaluacion,id_indicador,id_criterio);

                        EvaluacionCalidadCriterio ecc = linkDB.getEvaluacionCalidadCriterio(id_evaluacion, id_indicador, id_criterio, id_ecr);

                        if (ecc == null)
                        {
                            preguntas_pendientes++;
                        }

                    }///fin de la lista de criterios

                }//fin lista de lugares de verificacion

            }/// fin expedientes

        }///fin de la lista de indicadores pertenecientes a la evaluacion


        if(evaluacion.cerrado==1)
        {
            guardar.setVisible(false);
            cerrar.setVisible(false);
            transferencia.setVisible(false);

            boton_agregar_indicador.setVisibility(View.INVISIBLE);
            boton_agregar_expediente.setVisibility(View.INVISIBLE);

            image_borrar_indicador.setVisibility(View.INVISIBLE);
            image_borrar_expediente.setVisibility(View.INVISIBLE);

            if(evaluacion.firma.equals(""))
            {
                firmar.setVisible(true);
            }else{
                    firmar.setVisible(false);
                 }

        }else{

                    boton_agregar_indicador.setVisibility(View.VISIBLE);
                    boton_agregar_expediente.setVisibility(View.VISIBLE);

                    image_borrar_indicador.setVisibility(View.VISIBLE);
                    image_borrar_expediente.setVisibility(View.VISIBLE);

                   if(preguntas_pendientes > 0)
                    {
                        guardar.setVisible(true);

                        //dos invertidos
                        cerrar.setVisible(true);
                        firmar.setVisible(true);

                        transferencia.setVisible(true);

                    }else{
                               guardar.setVisible(true);
                               cerrar.setVisible(true);
                               firmar.setVisible(false);

                                transferencia.setVisible(true);
                         }


             }

        if(id_evaluacion_calidad_registro_selected>0)
        {
            guardar.setVisible(true);
        }else{
            guardar.setVisible(false);
        }



        if(id_indicador_selected>0)
        {
            Indicador i_temp=linkDB.getIndicador(id_indicador_selected);

            if(i_temp.indicacion != null)
            {
                indicacion.setVisible(true);
            }else {
                    indicacion.setVisible(false);
                  }

        }else {
                 indicacion.setVisible(false);
              }
    }////         F I N   R E V I S I O N    B O T O N E S

///*************************************************************************************************
///*************************************************************************************************


  /*


    public void setCount()
    {
        System.out.println(" ---  ---  ---  ---  ---  ---  ---  >  >  >  >  >  >  >  >   ENTRANDO A SET COUNT");

        String [] CONTADORES = {"O.O","0.0","0/0","NO"};


        double promedio_expediente = 0.0;

        int preguntas = 0;
        int respondidas = 0;
        int aprobadas = 0;
        int negativas = 0;
        int nas=0;

        int changes=0;


        ///revisionBotones();///      CHECAR SI ES NECESARIO AQUI O AL FINAL DEL CONTEO


        if(id_evaluacion_calidad_registro_selected > 0)
        {
                //dec_temp = adaptadorEC.getRespuestas();

                int array[];

                Evaluacion evax= linkDB.getEvaluacion("CALIDAD", evaluacion_calidad.id, linkDB.openDB());

                if(evax.cerrado==1)
                {
                            array = adaptadorECCerrado.contarPreguntas();

                }else{
                            array = adaptadorEC.contarPreguntas();
                     }

                preguntas = array[0];
                respondidas = array[1];
                aprobadas = array[2];
                negativas = array[3];
                nas=array[4];

                changes=array[5];

              if(changes==1){
                                cambios=true;
                                System.out.println("-----  CAMBIOS : TRUE  [ OK ]");
                            }else{
                                   cambios=false;
                                   System.out.println("-----  CAMBIOS : FALSE [ X ]");
                                 }


                double temp = (double) aprobadas / (preguntas-nas);
                promedio_expediente = ( temp ) * 100;

                System.out.println("PREGUNTAS: " + array[0] + ", APROBADAS: " + array[2] + ", NEGATIVAS:  "+array[3]+" ,NAS: " + array[4] + ", PROMEDIO : [ " + promedio_expediente + " ]");


                text_count.setText(respondidas + " / " + preguntas);
                text_promedio_expediente.setText(link.imprimirDouble( promedio_expediente )+" % ");


                CONTADORES [1]=link.imprimirDouble( promedio_expediente )+"";
                CONTADORES [2]=respondidas + " / " + preguntas;

            ///Calculo del promedio del indicador.

            List<EvaluacionCalidadRegistro> lista_expedientes = new ArrayList<>();
            lista_expedientes = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion_calidad.id,id_indicador_selected);

            double promedio_indicador=0;
            double suma_promedios=0;
            int total_expedientes=0;

            for(int i=0; i<lista_expedientes.size(); i++)
            {
                EvaluacionCalidadRegistro ecr =  lista_expedientes.get(i);
                int idECR = lista_expedientes.get(i).getId();
                total_expedientes++;

                if(idECR != id_evaluacion_calidad_registro_selected)
                {
                    suma_promedios+= ecr.getPromedio();
                }
            }

            suma_promedios+=promedio_expediente;
            promedio_indicador = ( suma_promedios / (double) total_expedientes );

            /// fin de calculo del promedio del indicador

            text_promedio_indicador.setText("Resultado del indicador: "+link.imprimirDouble(promedio_indicador)+" %");


            CONTADORES [0] = link.imprimirDouble(promedio_indicador)+"";



            if(negativas>0)
            {
                requiere_hallazgo=true;
            }else{
                   requiere_hallazgo=false;
                 }



                if(negativas>0)
                {
                    text_promedio_expediente.setTextColor(getResources().getColor(R.color.error_color));
                }else{
                        text_promedio_expediente.setTextColor(getResources().getColor(R.color.PrimaryColor));
                     }

                if (requiere_hallazgo)
                {
                    spinner_acciones.setVisibility(View.VISIBLE);
                    input_hallazgo.setVisibility(View.VISIBLE);
                } else {
                          spinner_acciones.setVisibility(View.GONE);
                          input_hallazgo.setVisibility(View.GONE);
                       }

        }else{



                    if(id_indicador_selected > 0)
                    {
                        List<EvaluacionCalidadRegistro> lista_expedientes = new ArrayList<>();
                        lista_expedientes = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion_calidad.id,id_indicador_selected);

                        double promedio_indicador=0;
                        double suma_promedios=0;
                        int total_expedientes=0;

                        for(int i=0; i<lista_expedientes.size(); i++)
                        {
                            EvaluacionCalidadRegistro ecr =  lista_expedientes.get(i);
                            int idECR = lista_expedientes.get(i).getId();
                            total_expedientes++;

                            suma_promedios+= ecr.getPromedio();

                        }

                        promedio_indicador = ( suma_promedios / (double) total_expedientes );

                        if(promedio_indicador < 80.00)
                        {
                            requiere_hallazgo=true;
                        }else{
                            requiere_hallazgo=false;
                        }



                        text_promedio_expediente.setText("0.0 %");
                        text_count.setText("0 / 0");
                        text_promedio_expediente.setTextColor(getResources().getColor(R.color.black));



                        if(respondidas>0) {
                            text_promedio_indicador.setText("Resultado del indicador: " + link.imprimirDouble(promedio_indicador) + " %");
                        }else{text_promedio_indicador.setText("Resultado del indicador: 0.0 %");}

                        System.out.println("DENTRO INDICADOR SELECCIONADO . PROMEDIO INDICADOR : [" + link.imprimirDouble(promedio_indicador) + "] ");


                    }else{

                                text_promedio_indicador.setText("Resultado del indicador:");
                                text_promedio_expediente.setText("0.0 %");
                                text_count.setText("0 / 0");
                                text_promedio_expediente.setTextColor(getResources().getColor(R.color.black));

                                System.out.println("DENTRO INDICADOR NULL  . PROMEDIO INDICADOR : [] ");

                                spinner_acciones.setVisibility(View.GONE);
                                input_hallazgo.setVisibility(View.GONE);

                          }



             }

        revisionBotones();



    }

    */

///*************************************************************************************************
///*************************************************************************************************
     

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_evaluador_calidad, menu);
        this.menu = menu;


        this.indicacion = menu.findItem(R.id.indicacion);

        this.guardar = menu.findItem(R.id.guardar_evaluacion);
        this.cerrar = menu.findItem(R.id.cerrar_evaluacion);
        this.transferencia = menu.findItem(R.id.transferencia);
        this.firmar = menu.findItem(R.id.firmar_evaluacion);
        this.ver_ficha = menu.findItem(R.id.ficha_tecnica);


        revisionBotones();

        //item_create= menu.findItem(R.id.action_create);
        //item_create.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home_back/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.indicacion)
        {
            crearDialogIndicaciones();
        }

        if (id == R.id.generar_muestra)
        {
            crearDialogMuestras();
        }



        if (id == R.id.transferencia)
        {
            showDialogAccionBluetooth();
        }


        if (id == R.id.ficha_tecnica)
        {
            //dialog.show();

            crearDialogFichaTecnica();

        }

        if (id == R.id.firmar_evaluacion)
        {
            Intent lanzador = new Intent(EvaluadorCalidad.this, FirmaEvaluacion.class);
            lanzador.putExtra( "idEvaluacion", evaluacion_calidad.id);
            lanzador.putExtra( "tipoEvaluacion", "CALIDAD");
            System.out.println("");
            startActivity(lanzador);

        }

        if (id == R.id.cerrar_evaluacion)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("¿ Confirma cerrar la evaluación ?")
            .setPositiveButton("Si",  new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    cerrarEvaluacion();
                }
            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog,int id)
                        {
                            dialog.cancel();
                        }
                    }).show();


        }


        if(id == R.id.guardar_evaluacion)
        {

            //guardarEvaluacion();


            new GuardarEvaluacionAsync().execute(input_hallazgo.getText().toString().trim());

        }


        return super.onOptionsItemSelected(item);
    }


///*************************************************************************************************
///*************************************************************************************************


    public void rellenarFicha()
     {

         text_nombre.setText(clues_selected.nombre);
         text_clave.setText(clues_selected.clues);
         text_ubicacion.setText(clues_selected.domicilio);
         text_jurisdiccion.setText(clues_selected.jurisdiccion);
         text_municipio.setText(clues_selected.municipio);
         text_localidad.setText(clues_selected.localidad);
         text_tipologia.setText(clues_selected.tipologia);
         text_tipo.setText(clues_selected.tipoUnidad);

         if(evaluacion_calidad.cerrado==1)
         {
             text_responsable_info.setText(evaluacion_calidad.responsable);

             if(evaluacion_calidad.firma.equals(""))
             {}else{
                 image_firma.setImageBitmap(link.decodeBase64(evaluacion_calidad.firma));
             }

         }else{

         }

     }



///*************************************************************************************************
///*************************************************************************************************




    public void cerrarEvaluacion()
    {
        Evaluacion eval=linkDB.getEvaluacion("CALIDAD",evaluacion_calidad.id,linkDB.openDB());

        linkDB.cerrarEvaluacion("CALIDAD", eval, linkDB.openDB());

        evaluacion_calidad = eval;

        cerrado = evaluacion_calidad.getCerrado();

        //revisionBotones();

        //cargarCriteriosExpediente();
        new CargarCriteriosExpedienteAsync().execute();

        link.showSnackBar(getApplicationContext(), coordinatorLayout, "Evaluación cerrada correctamente !", "INFO");

    }

///**************************************************************************************************

    public void showDialogAccionBluetooth()
    {

        List<String> OPCIONES = new ArrayList<>();
        OPCIONES.add("Transferir esta evaluación");
        OPCIONES.add("Aceptar transferencias para completar esta evaluación");



        final CharSequence[] LISTA = OPCIONES.toArray(new String[ OPCIONES.size() ]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Seleccione una acción");
        builder.setItems(LISTA, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {

                if (position == 0)
                {
                    goTransferirBluetooth();
                }


                if (position == 1)
                {
                    goRecibirBluetooth();
                }


            }
        });
        AlertDialog alert = builder.create();

        Window window = alert.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        alert.show();
    }


/////**********************************************************************************************

    /////**********************************************************************************************
    public void goTransferirBluetooth()
    {
        if(cambios)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Existen cambios sin guardar.\n¿ Quieres continuar al envio por bluetooth ?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent lanzador = new Intent(EvaluadorCalidad.this, BluetoothCalidad.class);
                            lanzador.putExtra("idEvaluacion", evaluacion_calidad.id);
                            lanzador.putExtra("MODO", "EMISOR");
                            lanzador.putExtra("TIPO_EVALUACION", "CALIDAD");
                            System.out.println("");
                            startActivity(lanzador);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        }else {

            Intent lanzador = new Intent(EvaluadorCalidad.this, BluetoothCalidad.class);
            lanzador.putExtra( "idEvaluacion", evaluacion_calidad.id);
            lanzador.putExtra( "MODO", "EMISOR");
            lanzador.putExtra( "TIPO_EVALUACION", "CALIDAD");
            System.out.println("");
            startActivity(lanzador);
        }

    }
    ///**************************************************************************************************
    public void goRecibirBluetooth()
    {
        if(cambios)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Existen cambios sin guardar.\n¿ Quieres continuar a la recepción bluetooth ?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent lanzador = new Intent(EvaluadorCalidad.this, BluetoothCalidad.class);
                            lanzador.putExtra("idEvaluacion", evaluacion_calidad.id);
                            lanzador.putExtra("MODO", "RECEPTOR");
                            lanzador.putExtra("TIPO_EVALUACION", "CALIDAD");
                            System.out.println("");
                            startActivity(lanzador);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        }else {
            Intent lanzador = new Intent(EvaluadorCalidad.this, BluetoothCalidad.class);
            lanzador.putExtra( "idEvaluacion", evaluacion_calidad.id);
            lanzador.putExtra( "MODO", "RECEPTOR");
            lanzador.putExtra( "TIPO_EVALUACION", "CALIDAD");
            System.out.println("");
            startActivity(lanzador);
        }

    }

///**************************************************************************************************

///*************************************************************************************************
///*************************************************************************************************


    public void showDialogAgregarIndicadores()
    {



        final CharSequence[] LISTA = INDICADORES.toArray(new String[ INDICADORES.size() ]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Seleccione un indicador");
        builder.setItems(LISTA, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {

                Indicador indicador_agregar = lista_indicadores.get(position);

                boolean existe = false;

                for (int i = 0; i < lista_indicadores_agregados.size(); i++)
                {

                    if (indicador_agregar.id == lista_indicadores_agregados.get(i).id) {
                        existe = true;
                        System.out.println("YA EXISTE ESTE INDICADOR : " + indicador_agregar.codigo);
                    }

                }

                if (existe == false)
                {
                    System.out.println(" N O   EXISTE ESTE INDICADOR : " + indicador_agregar.codigo + ", SE AGREGARA...");

                    lista_indicadores_agregados.add(indicador_agregar);
                    INDICADORES_AGREGADOS.add(indicador_agregar.codigo + " - " + indicador_agregar.nombre);

                    adaptador_indicadores_agregados.notifyDataSetChanged();
                }

                spinner_indicadores_agregados.setSelection(lista_indicadores_agregados.size() - 1, true);


            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }





///********************************************************************************************************************************************
///********************************************************************************************************************************************




    public void showDialogAgregarExpediente()
    {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this,R.style.TemaDialogMaterial);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.nuevo_expediente_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edit_expediente_dialog = (EditText) dialogView.findViewById(R.id.input_expediente);
        final TextView text_message = (TextView) dialogView.findViewById(R.id.text_message);

        final LinearLayout linear_content = (LinearLayout) dialogView.findViewById(R.id.linear_content);


        final Calendar calendar;
        final TextView dateView;
        final int year, month, day;

        final SimpleDateFormat dateFormatter;

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        EditText ed;
        final List<EditText> array_edits_p1 = new ArrayList<EditText>();
        final List<EditText> array_edits_p2 = new ArrayList<EditText>();

        TextInputLayout til;
        List<TextInputLayout> array_til_p1 = new ArrayList<TextInputLayout>();
        List<TextInputLayout> array_til_p2 = new ArrayList<TextInputLayout>();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,5,10,0);

        List<IndicadorValidacion> validaciones = linkDB.getValidaciones(id_indicador_selected);

        if(validaciones.size()>0)
        {
            for(int i=0; i<validaciones.size(); i++)
            {
                linear_content.addView(link.getSeparador(EvaluadorCalidad.this));

                String valor1=null;
                String valor2=null;

                IndicadorValidacion validacion = validaciones.get(i);

                String operador_aritmetico = validacion.operadorAritmetico;
                String unidad_medida = validacion.unidadMedida;
                String operador_logico = validacion.operadorLogico;
                double valor_comparativo = Double.parseDouble(validacion.valorComparativo);

                ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

                IndicadorPregunta pregunta1 = linkDB.getIndicadorPregunta(validacion.pregunta1);

                if(pregunta1.tipo.equals("date"))
                {
                    if(pregunta1.constante==1)
                    {
                        valor1=pregunta1.valorConstante;

                        if(pregunta1.fechaSistema==1)
                        {
                            ed = link.getEditText(pregunta1.nombre,10000+i,false,EvaluadorCalidad.this);
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);
                            array_edits_p1.add(ed);
                            linear_content.addView(til);

                            ed.setText(link.getFechaCorta());

                        }else {
                                    ed = link.getEditText(pregunta1.nombre,10000+i,false,EvaluadorCalidad.this);
                                    til = link.getTIL(EvaluadorCalidad.this);

                                    til.addView(ed);
                                    array_edits_p1.add(ed);
                                    linear_content.addView(til);

                                    ed.setText(pregunta1.valorConstante);
                              }


                    }else{
                                    ed = link.getEditText(pregunta1.nombre,10000+i,false,EvaluadorCalidad.this);
                                    til = link.getTIL(EvaluadorCalidad.this);

                                    til.addView(ed);

                                    array_edits_p1.add(ed);
                                    linear_content.addView(til);

                                    final EditText finalEd = ed;
                                    ed.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            DatePickerDialog  mdiDialog =new DatePickerDialog(EvaluadorCalidad.this,R.style.MyDatePickerDialogTheme,new DatePickerDialog.OnDateSetListener()
                                            {
                                                @Override
                                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                                {
                                                    Calendar date = Calendar.getInstance();
                                                    date.set(year, monthOfYear, dayOfMonth);

                                                    finalEd.setText(dateFormatter.format(date.getTime()));
                                                }
                                            }, year, month, day);

                                            mdiDialog.show();
                                        }
                                    });

                         }

                }else{ link.printToast("TIPO DIFERENTE DE DATE...",getApplicationContext()); }

                // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                IndicadorPregunta pregunta2 = linkDB.getIndicadorPregunta(validacion.pregunta2);

                if(pregunta2.tipo.equals("date"))
                {
                    if(pregunta2.constante==1)
                    {
                        valor2=pregunta2.valorConstante;

                        if(pregunta2.fechaSistema==1)
                        {
                            ed = link.getEditText(pregunta2.nombre,10000+i,false,EvaluadorCalidad.this);
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);
                            array_edits_p2.add(ed);
                            linear_content.addView(til);

                            ed.setText(link.getFechaCorta());

                        }else {
                                    ed = link.getEditText(pregunta2.nombre,10000+i,false,EvaluadorCalidad.this);
                                    til = link.getTIL(EvaluadorCalidad.this);

                                    til.addView(ed);
                                    array_edits_p2.add(ed);
                                    linear_content.addView(til);

                                    ed.setText(pregunta2.valorConstante);
                              }


                    }else{
                        ed = link.getEditText(pregunta2.nombre,10000+i,false,EvaluadorCalidad.this);
                        til = link.getTIL(EvaluadorCalidad.this);

                        til.addView(ed);

                        array_edits_p2.add(ed);
                        linear_content.addView(til);

                        final EditText finalEd = ed;
                        ed.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                DatePickerDialog  mdiDialog =new DatePickerDialog(EvaluadorCalidad.this,R.style.MyDatePickerDialogTheme,new DatePickerDialog.OnDateSetListener()
                                {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                    {
                                        Calendar date = Calendar.getInstance();
                                        date.set(year, monthOfYear, dayOfMonth);

                                        finalEd.setText(dateFormatter.format(date.getTime()));
                                    }
                                }, year, month, day);

                                mdiDialog.show();
                            }
                        });

                    }

                }else{ link.printToast("SOLO SE SOPORTA TIPO FECHA ...",getApplicationContext()); }


                linear_content.addView(link.getSeparador(EvaluadorCalidad.this));
            }

        }////  FIN   IF   VALIDACIONES

        linear_content.setVisibility(View.GONE);

    ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


        dialogBuilder.setTitle("Agregar expediente");

        dialogBuilder.setPositiveButton("Agregar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //do something with edt.getText().toString();
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                dialog.cancel();
            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();


/////***********************************************************************************************

        b.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                int errors = 0;
                String messages = "";

                Boolean closeDialog = false;
                Boolean validar = false;


                String nuevo_expediente = edit_expediente_dialog.getText().toString();

                if (nuevo_expediente.equals(""))
                {
                    errors++;
                    messages += "Escriba el expediente. \n";
                } else {
                            if (linkDB.existeEvaluacionCalidadRegistro(idEvaluacionCalidad, id_indicador_selected, nuevo_expediente))
                            {
                                errors++;
                                messages += "Ya existe el expediente " + nuevo_expediente.toUpperCase()+". \n";
                            }else{


                                    List<IndicadorValidacion> validaciones = linkDB.getValidaciones(id_indicador_selected);

                                    if(validaciones.size()>0)
                                    {
                                        validar=true;
                                        linear_content.setVisibility(View.VISIBLE);

                                        List<String> array_valores = new ArrayList<String>();


                                        for(int i=0; i<validaciones.size(); i++)
                                        {

                                            IndicadorValidacion validacion_x= validaciones.get(i);

                                            String operador_aritmetico = validacion_x.operadorAritmetico;
                                            String unidad_medida = validacion_x.unidadMedida;
                                            String operador_logico = validacion_x.operadorLogico;
                                            String valor_comparativo = validacion_x.valorComparativo;

                                            IndicadorPregunta pregunta1 = linkDB.getIndicadorPregunta(validacion_x.pregunta1);
                                            String valor1=array_edits_p1.get(i).getText().toString();

                                            IndicadorPregunta pregunta2 = linkDB.getIndicadorPregunta(validacion_x.pregunta2);
                                            String valor2=array_edits_p2.get(i).getText().toString();

                                            int res_operacion_aritmetica=0;

                                            int faltantes=0;
                                            Boolean aplica=false;

                                            if(valor1.equals("")){faltantes++;}
                                            if(valor2.equals("")){faltantes++;}

                                            if(faltantes==0)
                                            {
                                                if(unidad_medida.equals("days"))
                                                {
                                                    if(operador_aritmetico.equals("+"))
                                                    {
                                                        res_operacion_aritmetica =   link.sumarFechasV2(valor1,valor2) ;
                                                        link.printConsola("--------------- > > FN V2   OPERACION ARITMETICA : [ SUMA ] | [ ("+valor1+"),("+valor2+") ] |RESULTADO :["+res_operacion_aritmetica+"]");
                                                    }else {
                                                            res_operacion_aritmetica =  link.restarFechasV2(valor1,valor2) ;
                                                            link.printConsola("--------------- > >FN V2   OPERACION ARITMETICA : [ RESTA ] | [ ("+valor1+"),("+valor2+") ] | RESULTADO :["+res_operacion_aritmetica+"]");
                                                          }

                                                    switch (operador_logico)
                                                    {
                                                        case "<":

                                                            if( res_operacion_aritmetica < Integer.parseInt(valor_comparativo) )
                                                            {
                                                                aplica=true;
                                                            }else{ aplica=false; }
                                                            link.printConsola("RES. OP. ARITMETICA: [ "+res_operacion_aritmetica+" ] OP. LOGICO : [ < ] VAL. COMPARATIVO: [ "+valor_comparativo+" ]");

                                                            break;

                                                        case ">":

                                                            if( res_operacion_aritmetica > Integer.parseInt(valor_comparativo) )
                                                            {
                                                               aplica=true;
                                                            }else { aplica=false; }
                                                            link.printConsola("RES. OP. ARITMETICA: [ "+res_operacion_aritmetica+" ] OP. LOGICO : [ > ] VAL. COMPARATIVO: [ "+valor_comparativo+" ]");

                                                            break;

                                                        case "<=":

                                                            if( res_operacion_aritmetica <= Integer.parseInt(valor_comparativo) )
                                                            {
                                                                aplica=true;
                                                            }else{ aplica=false; }
                                                            link.printConsola("RES. OP. ARITMETICA: [ "+res_operacion_aritmetica+" ] OP. LOGICO : [ <= ] VAL. COMPARATIVO: [ "+valor_comparativo+" ]");

                                                            break;

                                                        case ">=":

                                                            if( res_operacion_aritmetica >= Integer.parseInt(valor_comparativo) )
                                                            {
                                                                aplica=true;
                                                            }else { aplica=false; }
                                                            link.printConsola("RES. OP. ARITMETICA: [ "+res_operacion_aritmetica+" ] OP. LOGICO : [ >= ] VAL. COMPARATIVO: [ "+valor_comparativo+" ]");

                                                            break;

                                                        case "<>":

                                                            if( res_operacion_aritmetica != Integer.parseInt(valor_comparativo) )
                                                            {
                                                                aplica=true;
                                                            }else{ aplica=false; }
                                                            link.printConsola("RES. OP. ARITMETICA: [ "+res_operacion_aritmetica+" ] OP. LOGICO : [ <> ] VAL. COMPARATIVO: [ "+valor_comparativo+" ]");

                                                            break;

                                                        case "=":

                                                            if( res_operacion_aritmetica == Integer.parseInt(valor_comparativo) )
                                                            {
                                                               aplica=true;
                                                            }else{ aplica=false; }
                                                            link.printConsola("RES. OP. ARITMETICA: [ "+res_operacion_aritmetica+" ] OP. LOGICO : [ == ] VAL. COMPARATIVO: [ "+valor_comparativo+" ]");

                                                            break;
                                                    }

                                                    if(res_operacion_aritmetica < 0)
                                                    {
                                                        aplica=false;
                                                    }



                                                    if(aplica==true)
                                                    {

                                                    }else{
                                                        errors++;
                                                        messages+="El Expediente no cumple, ingrese otro.";
                                                    }

                                                }

                                            }else{
                                                    errors++;
                                                    messages+="Faltan datos \n";
                                                 }

                                        }
                                    }//// FIN IF VALIDACIONES > CERO


                                }///FIN ELSE NO ERRORES DEL EXP (SIN VALID..)
                       }




                if (errors == 0)
                {

                    //CONSEGUIR EL TOTAL DE CRITEERIOS
                    int temp_total_criterios=0;
                    List<LugarVerificacion> lista_lv = linkDB.getLugaresVerificacion(nivel_cone,id_indicador_selected);

                    for (int i=0; i<lista_lv.size(); i++)
                    {
                        List<Criterio> lista_c = linkDB.getCriterios(nivel_cone,id_indicador_selected, lista_lv.get(i).id);
                        temp_total_criterios += lista_c.size();
                    }





                    EvaluacionCalidadRegistro insertar = new EvaluacionCalidadRegistro(
                                                                                        0,
                                                                                        idEvaluacionCalidad,
                                                                                        id_indicador_selected,
                                                                                        0,
                                                                                        nuevo_expediente,
                                                                                        0,
                                                                                        0.0,
                                                                                        temp_total_criterios,
                                                                                        0,
                                                                                        link.getFecha(),
                                                                                        "0000-00-00 00:00:00",
                                                                                        "0000-00-00 00:00:00"
                                                                                      );

                    link.printConsola("[][][][][][] )))))))))))    ------------ ANTES DE INSERTAR  EXPEDIENTE ......---------------_____>>>>>>>>>>>>>>>>>>>");
                    linkDB.insertarEvaluacionCalidadRegistro(insertar, linkDB.openDB());

                    link.ocultarTeclado(context, edit_expediente_dialog);
                    edit_expediente_dialog.setText("");

                    //refreshSpinnerExpedientes(id_indicador_selected);
                    //spinner_expedientes.setSelection(EXPEDIENTES.size() - 1, true);
                    //mostrar_agregar = false;

                    closeDialog=true;

                } else {
                            link.ocultarTeclado(context, edit_expediente_dialog);
                            text_message.setVisibility(View.VISIBLE);
                            text_message.setText(messages);

                            closeDialog=false;
                            //link.showSnackBar(context, coordinatorLayout, messages, "ERROR");
                            //setCount();
                       }


                if (closeDialog)
                {
                    b.dismiss();
                    System.out.print("-----------------------DESPUES DE DISSSMIS DIALOG AGREGAR EXPEDIENTE ----->");

                    refreshSpinnerExpedientes(id_indicador_selected,true);
                    //spinner_expedientes.setSelection(EXPEDIENTES.size() - 1, true);/// AQUI OCURRE EL ERROR

                    //CREAR METODO ESPECIAL DE REFESH SPINNER EXPEDIENTES QUE LLAME A UN ASYNC TASK PARA REFRESCAR Y TERMINANDO SELECCIONAR EL ULTIMO AGREGADO ,
                    mostrar_agregar = false;

                    System.out.print("-----------------------DESPUES DE REFRESCAR SPINNER EXPEDIENTES DISSSMIS DIALOG AGREGAR EXPEDIENTE ----->");

                }


            }///fin oncklick
        });
        //////// FIN BOTON POSITIVO NUEVO EXPEDIENTE



        b.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                b.dismiss();

                /*


                edit_expediente_dialog.setText("");

            List<IndicadorValidacion> validaciones3 = linkDB.getValidaciones(id_indicador_selected);

                for (int i=0; i<validaciones3.size(); i++)
                {
                    for (int j = 0; j < array_edits_p1.size(); j++)
                    {
                        IndicadorPregunta p1=linkDB.getIndicadorPregunta(validaciones3.get(j).pregunta1);
                        if(p1.tipo.equals("date"))
                        {
                            if(p1.constante==1)
                            {
                                if(p1.fechaSistema==1)
                                {
                                    array_edits_p1.get(j).setText(link.getFechaCorta());
                                }else {
                                        array_edits_p1.get(j).setText(p1.valorConstante.toString());
                                      }


                            }else{
                                   array_edits_p1.get(j).setText("");
                                 }

                        }

                        ///else{link.printToast("SOLO SE SOPORTA TIPO FECHA ...",getApplicationContext());}




                        IndicadorPregunta p2=linkDB.getIndicadorPregunta(validaciones3.get(j).pregunta2);
                        if(p2.tipo.equals("date"))
                        {
                            if(p2.constante==1)
                            {
                                if(p2.fechaSistema==1)
                                {
                                    array_edits_p2.get(j).setText(link.getFechaCorta());
                                }else {
                                        array_edits_p2.get(j).setText(p2.valorConstante.toString());
                                      }
                            }else{
                                    array_edits_p2.get(j).setText("");
                                 }

                        }
                        ///else{link.printToast("SOLO SE SOPORTA TIPO FECHA ...",getApplicationContext());}

                    }
                }
                text_message.setText("");
                text_message.setVisibility(View.INVISIBLE);


                */


            }///fin onclick
        });//////// FIN BOTON NEGATIVE NUEVO EXPEDIENTE







/////***********************************************************************************************

    }////FIN METODO AGREGAR EXPEDIENTE NUEVO

/////***********************************************************************************************
///*************************************************************************************************







    ///********************************************************************************************************************************************
    ///********************************************************************************************************************************************




    public void showDialogValidarCriterio(final int id_criterio_validar)
    {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this,R.style.TemaDialogMaterial);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_validacion_criterio, null);
        dialogBuilder.setView(dialogView);

        final TextView text_message = (TextView) dialogView.findViewById(R.id.text_message);

        final LinearLayout linear_content = (LinearLayout) dialogView.findViewById(R.id.linear_content);


        final Calendar calendar;
        final int year, month, day, hour, minute;

        final SimpleDateFormat dateFormatter;

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        EditText ed;
        final List<EditText> array_edits_p1 = new ArrayList<EditText>();
        final List<EditText> array_edits_p2 = new ArrayList<EditText>();

        TextInputLayout til;
        List<TextInputLayout> array_til_p1 = new ArrayList<TextInputLayout>();
        List<TextInputLayout> array_til_p2 = new ArrayList<TextInputLayout>();

        RadioGroup rg;
        RadioButton rb_si, rb_no;
        TextView text_view_p1,text_view_p2;

        final List<RadioButton> array_radios_si_p1 = new ArrayList<RadioButton>();
        final List<RadioButton> array_radios_no_p1 = new ArrayList<RadioButton>();

        final List<RadioButton> array_radios_si_p2 = new ArrayList<RadioButton>();
        final List<RadioButton> array_radios_no_p2 = new ArrayList<RadioButton>();


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,5,10,0);

        List<CriterioValidacion> validaciones = linkDB.getCriterioValidaciones(id_criterio_validar);


///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
       /////
        /////      I N I C I A N      V  A  L  I  D  A  C  I  O  N  E  S    ( C R E A C I Ó N  DE  F O R M U L A R I O S )
        /////
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


        if(validaciones.size()>0)
        {
            for(int i=0; i<validaciones.size(); i++)
            {
                linear_content.addView(link.getSeparador(EvaluadorCalidad.this));

                String valor1="";
                String valor2="";

                CriterioValidacion validacion = validaciones.get(i);

                String operador_aritmetico = validacion.operadorAritmetico;
                String unidad_medida = validacion.unidadMedida;
                String operador_logico = validacion.operadorLogico;

                    System.out.println(" > > > > > > > VALOR COMPARATIVO: [ "+validacion.valorComparativo+" ]");
                    //double valor_comparativo = Double.parseDouble(validacion.valorComparativo);

                String valor_comparativo = validacion.valorComparativo;


                ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                //CriterioValidacionRespuesta cvr_temp = linkDB.getCriterioValidacionRespuesta("CALIDAD",idEvaluacionCalidad,expediente_selected,id_criterio_validar, validacion.id);

                CriterioValidacionRespuesta cvr_temp = null;
                //// REVISAR CON UN FOR EN LISTA DE RESPUESTAS YA RECOGIDA A LA HORA DE CARGAR EXPEDIENTE

                for(int t=0; t<respuestasValidacionExpediente.size(); t++)
                {
                    if( (respuestasValidacionExpediente.get(t).idEvaluacion == idEvaluacionCalidad) &&
                            (respuestasValidacionExpediente.get(t).expediente.equals(expediente_selected) ) &&
                            (respuestasValidacionExpediente.get(t).idCriterio == id_criterio_validar) &&
                            (respuestasValidacionExpediente.get(t).idCriterioValidacion== validacion.id ) )
                             {

                                 cvr_temp = respuestasValidacionExpediente.get(t);

                             }

                }



                if(cvr_temp!=null)
                {
                    valor1 = cvr_temp.respuesta1;
                    valor2 = cvr_temp.respuesta2;

                }else{
                        valor1 = "";
                        valor2 = "";
                     }


                CriterioPregunta pregunta1 = linkDB.getCriterioPregunta(validacion.pregunta1);

                //// CONSEGUIR VALORES DE VALIDACION DESPUES DE SABER DE QUE TIPO ES LA PREGUNTA.
                //// PARA PODER HACER EL PARSEO DEL VALOR COMPARATIVO




///****************************************************************************************************************************************************

                if(pregunta1.tipo.equals("date"))
                {
                    if(pregunta1.constante==1)
                    {
                        //valor1=pregunta1.valorConstante;

                        if(pregunta1.fechaSistema==1)
                        {
                            ed = link.getEditText(pregunta1.nombre,10000+i,false,EvaluadorCalidad.this);
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);
                            array_edits_p1.add(ed);
                            linear_content.addView(til);

                            if(valor1.equals(""))
                            {
                                ed.setText(link.getFechaCorta());
                            }else{ed.setText(valor1);}


                        }else {
                                ed = link.getEditText(pregunta1.nombre,10000+i,false,EvaluadorCalidad.this);
                                til = link.getTIL(EvaluadorCalidad.this);

                                til.addView(ed);
                                array_edits_p1.add(ed);
                                linear_content.addView(til);

                                if(valor1.equals(""))
                                {
                                    ed.setText(pregunta1.valorConstante);
                                }else{ed.setText(valor1);}

                               // ed.setText(pregunta1.valorConstante);

                              }


                    }else{
                            ed = link.getEditText(pregunta1.nombre,10000+i,false,EvaluadorCalidad.this);
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);

                            array_edits_p1.add(ed);
                            linear_content.addView(til);

                            final EditText finalEd = ed;

                            if(valor1.equals(""))
                            {
                                ed.setText("");
                            }else{ed.setText(valor1);}


                            ed.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    DatePickerDialog  mdiDialog =new DatePickerDialog(EvaluadorCalidad.this,R.style.MyDatePickerDialogTheme,new DatePickerDialog.OnDateSetListener()
                                    {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                        {
                                            Calendar date = Calendar.getInstance();
                                            date.set(year, monthOfYear, dayOfMonth);

                                            finalEd.setText(dateFormatter.format(date.getTime()));
                                        }
                                    }, year, month, day);

                                    mdiDialog.show();
                                }
                            });


                        }

                }

///****************************************************************************************************************************************************
//////NEW VALIDATION

                if(pregunta1.tipo.equals("time"))
                {
                    if( pregunta1.constante==1 )
                    {
                        //valor1=pregunta1.valorConstante;

                        if(pregunta1.fechaSistema==1)
                        {
                            ed = link.getEditText(pregunta1.nombre,11000+i,false,EvaluadorCalidad.this);
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);
                            array_edits_p1.add(ed);
                            linear_content.addView(til);

                            if(valor1.equals(""))
                            {
                                ed.setText(link.getHoraSistema());
                            }else{
                                    ed.setText(valor1);
                                 }


                        }else {
                                    ed = link.getEditText(pregunta1.nombre,11000+i,false,EvaluadorCalidad.this);
                                    til = link.getTIL(EvaluadorCalidad.this);

                                    til.addView(ed);
                                    array_edits_p1.add(ed);
                                    linear_content.addView(til);

                                    if(valor1.equals(""))
                                    {
                                        ed.setText(pregunta1.valorConstante);
                                    }else{ed.setText(valor1);}

                                    // ed.setText(pregunta1.valorConstante);
                               }


                    }else{
                            ed = link.getEditText(pregunta1.nombre,10000+i,false,EvaluadorCalidad.this);
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);

                            array_edits_p1.add(ed);
                            linear_content.addView(til);

                            final EditText finalEd = ed;

                            if(valor1.equals(""))
                            {
                                ed.setText("");
                            }else{ ed.setText(valor1); }


                            ed.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    TimePickerDialog mdiDialog =new TimePickerDialog(EvaluadorCalidad.this,R.style.MyDatePickerDialogTheme,new TimePickerDialog.OnTimeSetListener()
                                    {
                                        @Override
                                        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute)
                                        {
                                            finalEd.setText(selectedHour+":"+selectedMinute);
                                        }
                                    }, hour, minute, true);
                                    mdiDialog.setTitle("Seleccione hora");
                                    mdiDialog.show();
                                }
                            });


                    }

                }


///****************************************************************************************************************************************************

                if(pregunta1.tipo.equals("boolean"))
                {
                    //link.printToast("TIPO BOOLEAN DETECTED... ("+pregunta1.tipo+")...",getApplicationContext());

                            rg = link.getRadioGroup(EvaluadorCalidad.this);
                            text_view_p1 = link.getTexView(pregunta1.nombre,EvaluadorCalidad.this);

                            rb_si = link.getRadioButton("Si",10000+i,true,EvaluadorCalidad.this);
                            rg.addView(rb_si);
                            array_radios_si_p1.add(rb_si);

                            rb_no = link.getRadioButton("No",70000+i,true,EvaluadorCalidad.this);
                            rg.addView(rb_no);
                            array_radios_no_p1.add(rb_no);

                            linear_content.addView(text_view_p1);
                            linear_content.addView(rg);

                }

///****************************************************************************************************************************************************

                if(pregunta1.tipo.equals("number"))
                {

                    if(pregunta1.constante==1)
                    {
                        //valor1=pregunta1.valorConstante;
                        if(pregunta1.fechaSistema==1)
                        {
                            ed = link.getEditText(pregunta1.nombre,10000+i,true,EvaluadorCalidad.this);
                            ed.setInputType(InputType.TYPE_CLASS_NUMBER );
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);
                            array_edits_p1.add(ed);
                            linear_content.addView(til);

                            if(valor1.equals(""))
                            {
                                ed.setText(link.getFechaCorta());
                            }else{ed.setText(valor1);}

                        }else {
                                    ed = link.getEditText(pregunta1.nombre,10000+i,true,EvaluadorCalidad.this);
                                    ed.setInputType(InputType.TYPE_CLASS_NUMBER );
                                    til = link.getTIL(EvaluadorCalidad.this);

                                    til.addView(ed);
                                    array_edits_p1.add(ed);
                                    linear_content.addView(til);

                                    if(valor1.equals(""))
                                    {
                                        ed.setText(pregunta1.valorConstante);
                                    }else{ed.setText(valor1);}
                                    // ed.setText(pregunta1.valorConstante);
                              }

                    }else{
                                ed = link.getEditText(pregunta1.nombre,10000+i,true,EvaluadorCalidad.this);
                                ed.setInputType(InputType.TYPE_CLASS_NUMBER );
                                til = link.getTIL(EvaluadorCalidad.this);

                                til.addView(ed);

                                array_edits_p1.add(ed);
                                linear_content.addView(til);

                                final EditText finalEd = ed;

                                if(valor1.equals(""))
                                {
                                    ed.setText("");
                                }else{ed.setText(valor1);}
                        }
                }


///****************************************************************************************************************************************************
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                ///BLOQUE DE PREGUNTAS 2
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
///****************************************************************************************************************************************************


                CriterioPregunta pregunta2 = linkDB.getCriterioPregunta(validacion.pregunta2);

                if(pregunta2.tipo.equals("date"))
                {
                    if(pregunta2.constante==1)
                    {
                        //valor2=pregunta2.valorConstante;

                        if(pregunta2.fechaSistema==1)
                        {
                            ed = link.getEditText(pregunta2.nombre,10000+i,false,EvaluadorCalidad.this);
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);
                            array_edits_p2.add(ed);
                            linear_content.addView(til);

                            //ed.setText(link.getFechaCorta());

                            if(valor2.equals(""))
                            {
                                ed.setText(link.getFechaCorta());
                            }else{ed.setText(valor2);}



                        }else {
                                ed = link.getEditText(pregunta2.nombre,10000+i,false,EvaluadorCalidad.this);
                                til = link.getTIL(EvaluadorCalidad.this);

                                til.addView(ed);
                                array_edits_p2.add(ed);
                                linear_content.addView(til);

                                //ed.setText(pregunta2.valorConstante);

                                if(valor2.equals(""))
                                {
                                    ed.setText(pregunta2.valorConstante);
                                }else{ed.setText(valor2);}


                              }


                    }else{

                        ed = link.getEditText(pregunta2.nombre,10000+i,false,EvaluadorCalidad.this);
                        til = link.getTIL(EvaluadorCalidad.this);

                        til.addView(ed);

                        array_edits_p2.add(ed);
                        linear_content.addView(til);

                        final EditText finalEd = ed;

                        if(valor2.equals(""))
                        {
                            ed.setText("");
                        }else{ed.setText(valor2);}


                        ed.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                DatePickerDialog  mdiDialog =new DatePickerDialog(EvaluadorCalidad.this,R.style.MyDatePickerDialogTheme,new DatePickerDialog.OnDateSetListener()
                                {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                    {
                                        Calendar date = Calendar.getInstance();
                                        date.set(year, monthOfYear, dayOfMonth);

                                        finalEd.setText(dateFormatter.format(date.getTime()));
                                    }
                                }, year, month, day);

                                mdiDialog.show();
                            }
                        });

                    }

                }
///****************************************************************************************************************************************************

                //////NEW VALIDATION

                if(pregunta2.tipo.equals("time"))
                {
                    if(pregunta2.constante==1)
                    {
                        //valor1=pregunta1.valorConstante;

                        if(pregunta2.fechaSistema==1)
                        {
                            ed = link.getEditText(pregunta2.nombre,15000+i,false,EvaluadorCalidad.this);
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);
                            array_edits_p2.add(ed);
                            linear_content.addView(til);

                            if(valor2.equals(""))
                            {
                                ed.setText(link.getFechaCorta());
                            }else{ed.setText(valor2);}


                        }else {
                            ed = link.getEditText(pregunta2.nombre,15000+i,false,EvaluadorCalidad.this);
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);
                            array_edits_p2.add(ed);
                            linear_content.addView(til);

                            if(valor2.equals(""))
                            {
                                ed.setText(pregunta2.valorConstante);
                            }else{ed.setText(valor2);}

                            // ed.setText(pregunta1.valorConstante);

                        }


                    }else{
                        ed = link.getEditText(pregunta2.nombre,10000+i,false,EvaluadorCalidad.this);
                        til = link.getTIL(EvaluadorCalidad.this);

                        til.addView(ed);

                        array_edits_p2.add(ed);
                        linear_content.addView(til);

                        final EditText finalEd = ed;

                        if(valor2.equals(""))
                        {
                            ed.setText("");
                        }else{ed.setText(valor2);}


                        ed.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                TimePickerDialog mdiDialog =new TimePickerDialog(EvaluadorCalidad.this,R.style.MyDatePickerDialogTheme,new TimePickerDialog.OnTimeSetListener()
                                {
                                    @Override
                                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute)
                                    {
                                        finalEd.setText(selectedHour+":"+selectedMinute);
                                    }
                                }, hour, minute, true);
                                mdiDialog.setTitle("Seleccione hora");
                                mdiDialog.show();
                            }
                        });


                    }

                }


///****************************************************************************************************************************************************


                if(pregunta2.tipo.equals("boolean"))
                {

                    rg = link.getRadioGroup(EvaluadorCalidad.this);
                    text_view_p2 = link.getTexView(pregunta2.nombre,EvaluadorCalidad.this);

                    rb_si = link.getRadioButton("Si",10000+i,true,EvaluadorCalidad.this);
                    rg.addView(rb_si);
                    array_radios_si_p2.add(rb_si);

                    rb_no = link.getRadioButton("No",70000+i,true,EvaluadorCalidad.this);
                    rg.addView(rb_no);
                    array_radios_no_p2.add(rb_no);

                    linear_content.addView(text_view_p2);
                    linear_content.addView(rg);


                }//FIN TIPO boolean PREGUNTA 2

///****************************************************************************************************************************************************

                if(pregunta2.tipo.equals("number"))
                {

                    if(pregunta2.constante==1)
                    {
                        //valor2=pregunta2.valorConstante;
                        if(pregunta2.fechaSistema==1)
                        {
                            ed = link.getEditText(pregunta2.nombre,10000+i,true,EvaluadorCalidad.this);
                            ed.setInputType(InputType.TYPE_CLASS_NUMBER);
                            til = link.getTIL(EvaluadorCalidad.this);

                            til.addView(ed);
                            array_edits_p2.add(ed);
                            linear_content.addView(til);

                            //ed.setText(link.getFechaCorta());
                            if(valor2.equals(""))
                            {
                                ed.setText(link.getFechaCorta());
                            }else{ed.setText(valor2);}

                        }else {
                                    ed = link.getEditText(pregunta2.nombre,10000+i,true,EvaluadorCalidad.this);
                                    ed.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    til = link.getTIL(EvaluadorCalidad.this);

                                    til.addView(ed);
                                    array_edits_p2.add(ed);
                                    linear_content.addView(til);

                                    //ed.setText(pregunta2.valorConstante);
                                    if(valor2.equals(""))
                                    {
                                        ed.setText(pregunta2.valorConstante);
                                    }else{ed.setText(valor2);}
                                }


                    }else{
                                ed = link.getEditText(pregunta2.nombre,10000+i,true,EvaluadorCalidad.this);
                                ed.setInputType(InputType.TYPE_CLASS_NUMBER);
                                til = link.getTIL(EvaluadorCalidad.this);

                                til.addView(ed);

                                array_edits_p2.add(ed);
                                linear_content.addView(til);

                                final EditText finalEd = ed;

                                if(valor2.equals(""))
                                {
                                    ed.setText("");
                                }else{ed.setText(valor2);}
                         }

                }
///****************************************************************************************************************************************************




                if(!pregunta1.tipo.equals("date") && !pregunta1.tipo.equals("boolean") && !pregunta1.tipo.equals("number") && !pregunta1.tipo.equals("time"))
                {
                    link.printToast("TIPO DE VALIDACION [ "+pregunta1.tipo+" ] NO SOPORTADA ...",getApplicationContext());
                    link.printConsola("TIPO DE VALIDACION [ "+pregunta1.tipo+" ] NO SOPORTADA ...");
                }


                linear_content.addView(link.getSeparador(EvaluadorCalidad.this));
            }

        }
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        /////      F I N      V  A  L  I  D  A  C  I  O  N  E  S   ( C O N S T R U C C I O N   DE  F O R M U L A R I O S )
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


        ///linear_content.setVisibility(View.GONE);

        ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


        dialogBuilder.setTitle("Validaciones de Criterio");

        dialogBuilder.setPositiveButton("Validar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //do something with edt.getText().toString();
            }
        });

        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                dialog.cancel();
            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();


/////***********************************************************************************************************************************************************************************
/////**************************************************************      B O T O N    V A L I D A R        *****************************************************************************
/////***********************************************************************************************************************************************************************************

        b.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int errors = 0;
                String messages = "";

                Boolean closeDialog = false;
                Boolean validar = false;

                int faltantes=0;

                int NEGATIVAS=0;

                String CASO="()()()()()()()(()()()()()()   --->>>>>   N I N G U N O   C O M P A T I B L E       ----- >>>    xxxxxxxxxxxxxxxxxxxxxxxxxx";

                        List<CriterioValidacion> validaciones = linkDB.getCriterioValidaciones(id_criterio_validar);

                        if(validaciones.size()>0)
                        {
                            validar=true;

                            List<String> array_valores = new ArrayList<String>();
                            int c1=0;
                            int c2=0;


                            for(int i=0; i<validaciones.size(); i++)
                            {

                                CriterioValidacion validacion_x= validaciones.get(i);

                                String operador_aritmetico = validacion_x.operadorAritmetico;
                                String unidad_medida = validacion_x.unidadMedida;
                                String operador_logico = validacion_x.operadorLogico;

                                String valor_comparativo = validacion_x.valorComparativo;

                                String valor1="";
                                String valor2="";

                                CriterioPregunta pregunta1 = linkDB.getCriterioPregunta(validacion_x.pregunta1);
                                //String valor1=array_edits_p1.get(i).getText().toString();
                                CriterioPregunta pregunta2 = linkDB.getCriterioPregunta(validacion_x.pregunta2);
                                //String valor2=array_edits_p2.get(i).getText().toString();

                                int res_operacion_aritmetica=0;


                                if(pregunta1.tipo.equals("date") || pregunta1.tipo.equals("number") || pregunta1.tipo.equals("time"))
                                {
                                    valor1=array_edits_p1.get(c1).getText().toString();
                                    valor2=array_edits_p2.get(c1).getText().toString();
                                    c1++;
                                }

                                if(pregunta1.tipo.equals("boolean"))
                                {
                                    boolean radio_si_p1 = array_radios_si_p1.get(c2).isChecked();
                                    boolean radio_no_p1 = array_radios_no_p1.get(c2).isChecked();

                                    if(radio_si_p1){ valor1="1"; link.printConsola("RADIO SI 1 -> TRUE"); }
                                    if(radio_no_p1){ valor1="0"; link.printConsola("RADIO NO 1 -> TRUE"); }

                                    if(radio_si_p1==false && radio_no_p1==false){ faltantes++; }


                                    boolean radio_si_p2 = array_radios_si_p2.get(c2).isChecked();
                                    boolean radio_no_p2 = array_radios_no_p2.get(c2).isChecked();

                                    if(radio_si_p2){ valor2="1"; link.printConsola("RADIO SI 2 -> TRUE"); }
                                    if(radio_no_p2){ valor2="0"; link.printConsola("RADIO NO 2 -> TRUE"); }

                                    if(radio_si_p2==false && radio_no_p2==false){ faltantes++; }

                                    ///error en faltantes, esta mal calculado
                                    c2++;
                                }

                                link.printConsola("\n VALORES STEP 1 . VALOR 1 : ["+valor1+"] , "+"VALOR 2 : ["+valor2+"]");

                                Boolean aplica=false;

                                if(valor1.equals("")){ faltantes++; }
                                if(valor2.equals("")){ faltantes++; }


                                if(faltantes==0)
                                {



/////*********************************************************************************************************************************************************************
                                    ///:::::::::  T I P O   T I M E
/////*********************************************************************************************************************************************************************


                                    if(pregunta1.tipo.equals("time"))
                                    {
                                        CASO="--------------->>>   TIME Y  MINUTOS ";

                                        if(validacion_x.unidadMedida.equals("mins"))
                                        {
                                            if(operador_aritmetico.equals("+"))
                                            {
                                                res_operacion_aritmetica = link.sumarHoras(valor1,valor2);
                                            }else {
                                                        res_operacion_aritmetica = link.restarHoras(valor1,valor2);
                                                  }


                                            link.printConsola("]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]  > > >  VALOR 1 : ( "+valor1+" ) , VALOR 2: ( "+valor2+" ) . RES OP. ARITMETICA (TIME) : ( "+res_operacion_aritmetica+" ). VALOR COMPARATIVO : ( "+valor_comparativo+" ) .");
                                        }


                                        switch (operador_logico)
                                        {
                                            case "<":

                                                if( res_operacion_aritmetica < Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else{ aplica=false; }

                                                break;

                                            case ">":

                                                if( res_operacion_aritmetica > Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else { aplica=false; }

                                                break;

                                            case "<=":

                                                if( res_operacion_aritmetica <= Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else{ aplica=false; }

                                                break;

                                            case ">=":

                                                if( res_operacion_aritmetica >= Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else { aplica=false; }

                                                break;

                                            case "<>":

                                                if( res_operacion_aritmetica != Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else{ aplica=false; }

                                                break;

                                            case "=":

                                                if( res_operacion_aritmetica == Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else{ aplica=false; }

                                                break;
                                        }

                                        if(res_operacion_aritmetica < 0)
                                        {
                                            aplica=false;
                                        }


                                        if(aplica==true)
                                        {

                                        }else{
                                                errors++;
                                                NEGATIVAS++;
                                                messages+="El Criterio no cumple !!";
                                            }


                                        ////// VERIFICAR SI ESTA VALIDACION ESTA GUARDADA PARA HACER INSERT OR UPDATE

                                        CriterioValidacionRespuesta cvr = linkDB.getCriterioValidacionRespuesta("CALIDAD",idEvaluacionCalidad,expediente_selected,id_criterio_validar,validacion_x.getId());

                                        if(cvr==null)
                                        {
                                            CriterioValidacionRespuesta cvr_temp = new CriterioValidacionRespuesta(0,idEvaluacionCalidad,expediente_selected,id_criterio_validar,validacion_x.id,
                                                    "CALIDAD",valor1,valor2,link.getFecha(),"0000-00-00 00:00:00","0000-00-00 00:00:00");

                                            linkDB.insertarCriterioValidacionRespuesta(cvr_temp,linkDB.openDB());

                                        }else{
                                            cvr.setRespuesta1(valor1);
                                            cvr.setRespuesta2(valor2);
                                            cvr.setModificadoAl(link.getFecha());

                                            linkDB.actualizarCriterioValidacionRespuesta(cvr,linkDB.openDB());
                                        }



                                    }///FIN  T I P O   T I M E



/////*********************************************************************************************************************************************************************
                                    ///::::::::: T I P O    D A T E
/////*********************************************************************************************************************************************************************


                                    if(  pregunta1.tipo.equals("date") )
                                    {
                                        CASO="--------------->>>   D A T E     Y     D I A S ";


                                        if(validacion_x.unidadMedida.equals("days"))
                                        {
                                            if(operador_aritmetico.equals("+"))
                                            {
                                                res_operacion_aritmetica = link.sumarFechas(valor1,valor2);
                                             }else {
                                                        res_operacion_aritmetica = link.restarFechas(valor1,valor2);
                                                   }
                                        }

                                        if(validacion_x.unidadMedida.equals("weeks"))
                                        {
                                            if(operador_aritmetico.equals("+"))
                                            {
                                                res_operacion_aritmetica = (int) ( link.sumarFechas(valor1,valor2) / 7 );
                                            }else {
                                                    res_operacion_aritmetica = (int) (link.restarFechas(valor1,valor2) / 7);
                                                  }
                                        }


                                        switch (operador_logico)
                                        {
                                            case "<":

                                                if( res_operacion_aritmetica < Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else{ aplica=false; }

                                                break;

                                            case ">":

                                                if( res_operacion_aritmetica > Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else { aplica=false; }

                                                break;

                                            case "<=":

                                                if( res_operacion_aritmetica <= Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else{ aplica=false; }

                                                break;

                                            case ">=":

                                                if( res_operacion_aritmetica >= Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else { aplica=false; }

                                                break;

                                            case "<>":

                                                if( res_operacion_aritmetica != Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else{ aplica=false; }

                                                break;

                                            case "=":

                                                if( res_operacion_aritmetica == Integer.parseInt(valor_comparativo) )
                                                {
                                                    aplica=true;
                                                }else{ aplica=false; }

                                                break;
                                        }

                                        if(res_operacion_aritmetica < 0)
                                        {
                                            aplica=false;
                                        }

                                        if(aplica==true)
                                        {
                                        }else{
                                                errors++;
                                                NEGATIVAS++;
                                                messages+="El Criterio no cumple !!";
                                             }


                                        ////// VERIFICAR SI ESTA VALIDACION ESTA GUARDADA PARA HACER INSERT OR UPDATE

                                        CriterioValidacionRespuesta cvr = linkDB.getCriterioValidacionRespuesta("CALIDAD",idEvaluacionCalidad,expediente_selected,id_criterio_validar,validacion_x.getId());

                                        if(cvr==null)
                                        {
                                            CriterioValidacionRespuesta cvr_temp = new CriterioValidacionRespuesta(0,idEvaluacionCalidad,expediente_selected,id_criterio_validar,validacion_x.id,
                                                                                        "CALIDAD",valor1,valor2,link.getFecha(),"0000-00-00 00:00:00","0000-00-00 00:00:00");

                                            linkDB.insertarCriterioValidacionRespuesta(cvr_temp,linkDB.openDB());

                                        }else{
                                                cvr.setRespuesta1(valor1);
                                                cvr.setRespuesta2(valor2);
                                                cvr.setModificadoAl(link.getFecha());

                                                linkDB.actualizarCriterioValidacionRespuesta(cvr,linkDB.openDB());
                                             }

                                    }///  FIN IF TIPO   D A T E




/////*********************************************************************************************************************************************************************
                                    ///::::::::: T I P O    B O O L E A N
/////*********************************************************************************************************************************************************************


                                    if( pregunta1.tipo.equals("boolean") )
                                    {
                                        CASO="--------------->>>   B O O L E A M   ";


                                        if(operador_aritmetico.equals("+"))
                                        {
                                            res_operacion_aritmetica = Integer.parseInt(valor1) + Integer.parseInt(valor2);
                                        }else {
                                                  res_operacion_aritmetica = Integer.parseInt(valor1) - Integer.parseInt(valor2);
                                              }


                                        List<String> lista_valores_comparativos = new ArrayList<String>();

                                        link.printConsola("()()()()()()()()() ---->< T I P O     B O O L E A N     D E T E C T A D O -----()()()()() ");
                                        link.printConsola("RES. O. ARITMETICA: [ "+res_operacion_aritmetica+" ] ");
                                        link.printConsola("OPERADOR LOGICO : [ "+operador_logico+" ] ");



                                        String acumulado="";
                                        int acumular=0;

                                        for(int pos=0; pos<valor_comparativo.length(); pos++)
                                        {
                                            if( valor_comparativo.charAt(pos) == 'O' || valor_comparativo.charAt(pos) == 'o' )
                                            {
                                                //nada
                                            }else{
                                                        if(valor_comparativo.charAt(pos) == '-')
                                                        {
                                                            acumular=1;
                                                            acumulado+=valor_comparativo.charAt(pos);
                                                        }else{
                                                                if(acumular==1)
                                                                {
                                                                    acumulado+=valor_comparativo.charAt(pos);
                                                                    lista_valores_comparativos.add( acumulado );
                                                                    acumular=0;

                                                                }else{
                                                                        lista_valores_comparativos.add(""+valor_comparativo.charAt(pos));
                                                                     }
                                                             }
                                                 }
                                        }








                                        if(operador_logico.equals("<"))
                                        {
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica < Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(operador_logico.equals(">"))
                                        {
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica > Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(operador_logico.equals("<="))
                                        {
                                            //aplica = ( res_operacion_aritmetica <= Integer.parseInt(valor_comparativo) );
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica <= Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(operador_logico.equals(">="))
                                        {
                                            //aplica = ( res_operacion_aritmetica >= Integer.parseInt(valor_comparativo) );
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica >= Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(operador_logico.equals("<>"))
                                        {
                                            //aplica = ( res_operacion_aritmetica != Integer.parseInt(valor_comparativo) );
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica != Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(operador_logico.equals("="))
                                        {
                                            //aplica = ( res_operacion_aritmetica == Integer.parseInt(valor_comparativo) );
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica == Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }


                                        if(aplica==true)
                                        {

                                        }else{
                                                errors++;
                                                NEGATIVAS++;
                                                messages+="El Criterio no cumple !!";
                                            }


                                        ////// VERIFICAR SI ESTA VALIDACION ESTA GUARDADA PARA HACER INSERT OR UPDATE

                                        CriterioValidacionRespuesta cvr = linkDB.getCriterioValidacionRespuesta("CALIDAD",idEvaluacionCalidad,expediente_selected,id_criterio_validar,validacion_x.getId());

                                        if(cvr==null)
                                        {
                                            CriterioValidacionRespuesta cvr_temp = new CriterioValidacionRespuesta(0,idEvaluacionCalidad,expediente_selected,id_criterio_validar,validacion_x.id,
                                                    "CALIDAD",valor1,valor2,link.getFecha(),"0000-00-00 00:00:00","0000-00-00 00:00:00");

                                            linkDB.insertarCriterioValidacionRespuesta(cvr_temp,linkDB.openDB());

                                        }else{
                                                    cvr.setRespuesta1(valor1);
                                                    cvr.setRespuesta2(valor2);
                                                    cvr.setModificadoAl(link.getFecha());

                                                    linkDB.actualizarCriterioValidacionRespuesta(cvr,linkDB.openDB());
                                             }

                                    }///   FIN IF TIPO BOOLEAN







/////*********************************************************************************************************************************************************************
                                    ///::::::::: T I P O    N U M B E R
/////*********************************************************************************************************************************************************************


                                    if( pregunta1.tipo.equals("number") )
                                    {

                                        CASO="--------------->>>  N U M B E R   ";

                                        if(operador_aritmetico.equals("+"))
                                        {
                                            res_operacion_aritmetica = Integer.parseInt( valor1) + Integer.parseInt( valor2 );
                                         }else {
                                                    res_operacion_aritmetica = Integer.parseInt( valor1) - Integer.parseInt( valor2 );
                                                 }


                                        List<String> lista_valores_comparativos = new ArrayList<String>();



                                        link.printConsola("()()()()()()()()() ---->< T I P O     N U M B E R      D E T E C T A D O -----()()()()() ");
                                        link.printConsola("RES. O. ARITMETICA: [ "+res_operacion_aritmetica+" ] ");
                                        link.printConsola("OPERADOR LOGICO : [ "+operador_logico+" ] ");

                                        for(int pos=0; pos<valor_comparativo.length(); pos++)
                                        {
                                            if( valor_comparativo.charAt(pos) == 'O' || valor_comparativo.charAt(pos) == 'o' )
                                            {
                                            }else{
                                                lista_valores_comparativos.add(""+valor_comparativo.charAt(pos));
                                                link.printConsola("VALORES COMPARATIVO : [ "+valor_comparativo.charAt(pos)+" ] ");
                                            }
                                        }




                                        if(operador_logico.equals("<"))
                                        {
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica < Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(operador_logico.equals(">"))
                                        {
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica > Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(operador_logico.equals("<="))
                                        {
                                            //aplica = ( res_operacion_aritmetica <= Integer.parseInt(valor_comparativo) );
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica <= Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(operador_logico.equals(">="))
                                        {
                                            //aplica = ( res_operacion_aritmetica >= Integer.parseInt(valor_comparativo) );
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica >= Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(operador_logico.equals("<>"))
                                        {
                                            //aplica = ( res_operacion_aritmetica != Integer.parseInt(valor_comparativo) );
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica != Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(operador_logico.equals("="))
                                        {
                                            //aplica = ( res_operacion_aritmetica == Integer.parseInt(valor_comparativo) );
                                            for(int pos=0; pos < lista_valores_comparativos.size(); pos++)
                                            {
                                                if( res_operacion_aritmetica == Integer.parseInt(lista_valores_comparativos.get(pos)) )
                                                {
                                                    aplica = true;
                                                }
                                            }
                                        }

                                        if(aplica==true)
                                        {

                                        }else{
                                            errors++;
                                            NEGATIVAS++;
                                            messages+="El Criterio no cumple !!";
                                        }


                                        ////// VERIFICAR SI ESTA VALIDACION ESTA GUARDADA PARA HACER INSERT OR UPDATE

                                        CriterioValidacionRespuesta cvr = linkDB.getCriterioValidacionRespuesta("CALIDAD",idEvaluacionCalidad,expediente_selected,id_criterio_validar,validacion_x.getId());

                                        if(cvr==null)
                                        {
                                            CriterioValidacionRespuesta cvr_temp = new CriterioValidacionRespuesta(0,idEvaluacionCalidad,expediente_selected,id_criterio_validar,validacion_x.id,
                                                                                                                    "CALIDAD",valor1,valor2,link.getFecha(),"0000-00-00 00:00:00","0000-00-00 00:00:00");

                                            linkDB.insertarCriterioValidacionRespuesta(cvr_temp,linkDB.openDB());

                                        }else{
                                            cvr.setRespuesta1(valor1);
                                            cvr.setRespuesta2(valor2);
                                            cvr.setModificadoAl(link.getFecha());

                                            linkDB.actualizarCriterioValidacionRespuesta(cvr,linkDB.openDB());
                                        }

                                    }///   FIN IF TIPO   N U M B E R






                                }else{
                                        errors++;
                                        messages+="Responda todas las preguntas \n";
                                     }

                            }/// FIN FOR VALIDACIONES RECORRIDAS

       ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
           /// F I N   D E   E J E C U C I O N   D E    V A L I D A C I O N E S
       ///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::




                            //CriterioValidacionRespuesta cvr_temp= linkDB.getListaCriterioValidacionRespuesta("CALIDAD",idEvaluacionCalidad,expediente_selected,id_criterio_validar,);
                            ////VERIFICAR VALOR DEL CRITERIO EN EL ADAPTADOR

                            List<DataEvaluadorCalidad> respuestas_temp = adaptadorEC.getRespuestas();

                            System.out.print("RESPUESTAS DEVUELTAS POR ADAPTADPR DE CALIDAD :  "+respuestas_temp.size()+".");

                            if(errors==0)
                            {
                                link.printConsola(" ERRORES AL FINAL DE VALIDACION : ["+errors+"]");

                                for(int i=0; i<respuestas_temp.size(); i++)
                                {
                                    if(respuestas_temp.get(i).idCriterio == id_criterio_validar)
                                    {
                                        respuestas_temp.get(i).respuesta=1;
                                        respuestas_temp.get(i).modificado=1;
                                    }
                                }
                            }else {
                                    link.printConsola("ERRORES AL FINAL DE VALIDACION : ["+errors+"]");

                                        for(int i=0; i<respuestas_temp.size(); i++)
                                        {
                                            if(respuestas_temp.get(i).idCriterio == id_criterio_validar)
                                            {
                                                respuestas_temp.get(i).respuesta=0;
                                                respuestas_temp.get(i).modificado=1;
                                            }
                                        }
                                  }


                            adaptadorEC.updateDataRecycler(respuestas_temp,respuestasValidacionExpediente,cerrado);

                            cambios=true;
                            adaptadorEC.cambios=true;
                            //caso_indicador_cancelar=false;

                            ///setCount();

                            link.printConsola(" ----_>>>>>>>>>>   C A S O    :   "+CASO);

                        }//// FIN IF VALIDACIONES > CERO



                link.printConsola("FALTANTES AL FINAL DE VALIDACION : ["+faltantes+"]");

                if(faltantes==0)
                {
                    closeDialog=true;
                }else{
                        text_message.setVisibility(View.VISIBLE);
                        text_message.setText(messages);

                        closeDialog=false;
                     }






                if (closeDialog)
                {
                    b.dismiss();
                    avance_validacion();

                }




            }///fin oncklick
        });
        //////// FIN BOTON POSITIVO NUEVO EXPEDIENTE



        b.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                List<DataEvaluadorCalidad> list_refresh = adaptadorEC.getRespuestas();
                adaptadorEC.updateDataRecycler(list_refresh,respuestasValidacionExpediente,cerrado);

                cambios=false;
                adaptadorEC.cambios=false;

                b.dismiss();

                /*
                List<CriterioValidacion> validaciones3 = linkDB.getCriterioValidaciones(id_criterio_validar);

                for (int i=0; i<validaciones3.size(); i++)
                {
                    for (int j = 0; j < array_edits_p1.size(); j++)
                    {
                        CriterioPregunta p1=linkDB.getCriterioPregunta(validaciones3.get(j).pregunta1);
                        if(p1.tipo.equals("date"))
                        {
                            if(p1.constante==1)
                            {
                                if(p1.fechaSistema==1)
                                {
                                    array_edits_p1.get(j).setText(link.getFechaCorta());
                                }else {
                                        array_edits_p1.get(j).setText(p1.valorConstante.toString());
                                      }


                            }else{
                                    array_edits_p1.get(j).setText("");
                                 }

                        }else{link.printToast("SOLO SE SOPORTA TIPO FECHA ...",getApplicationContext());}




                        CriterioPregunta p2=linkDB.getCriterioPregunta(validaciones3.get(j).pregunta2);

                        if(p2.tipo.equals("date"))
                        {
                            if(p2.constante==1)
                            {
                                if(p2.fechaSistema==1)
                                {
                                    array_edits_p2.get(j).setText(link.getFechaCorta());
                                }else {
                                    array_edits_p2.get(j).setText(p2.valorConstante.toString());
                                }
                            }else{
                                array_edits_p2.get(j).setText("");
                            }

                        }else{link.printToast("SOLO SE SOPORTA TIPO FECHA ...",getApplicationContext());}

                    }
                }
                text_message.setText("");
                text_message.setVisibility(View.INVISIBLE);


                */


            }///fin onclick
        });//////// FIN BOTON NEGATIVE VALIDAR CRITERIO







/////***********************************************************************************************

    }////FIN METODO VALIDAR CRITERIO

/////***********************************************************************************************
///*************************************************************************************************





















    public void crearDialogFichaTecnica()
    {

        dialog = new AlertDialog.Builder(this,R.style.TemaDialogMaterial);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_ficha_tecnica, null);
        dialog.setView(view);

        dialog.setTitle("Ficha Técnica").setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                dialog.dismiss();
            }
        });

        text_nombre= (TextView) view.findViewById(R.id.nombre_clues);
        text_clave= (TextView) view.findViewById(R.id.clave_clues);
        text_ubicacion= (TextView) view.findViewById(R.id.ubicacion_clues);
        text_jurisdiccion= (TextView) view.findViewById(R.id.jurisdiccion_clues);
        text_municipio= (TextView) view.findViewById(R.id.municipio_clues);
        text_localidad= (TextView) view.findViewById(R.id.localidad_clues);
        text_tipologia= (TextView) view.findViewById(R.id.tipologia_clues);
        text_tipo= (TextView) view.findViewById(R.id.tipo_clues);

        text_responsable_info = (TextView) view.findViewById(R.id.text_responsable);
        image_firma = (ImageView) view.findViewById(R.id.image_view_firma);

        image_firma.setVisibility(View.INVISIBLE);


        rellenarFicha();

        dialog.show();


    }
/////***********************************************************************************************
///*************************************************************************************************

    public void crearDialogIndicaciones()
    {

        boolean show = false;

        dialog = new AlertDialog.Builder(this,R.style.TemaDialogMaterial);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_indicaciones, null);
        dialog.setView(view);

        text_indicaciones= (TextView) view.findViewById(R.id.indicaciones);

        String code="";

        if(id_indicador_selected>0)
        {
            Indicador i_temp=linkDB.getIndicador(id_indicador_selected);
            code = i_temp.codigo;

            if( !( i_temp.indicacion.equals("null") ) )
            {
                text_indicaciones.setText(i_temp.indicacion);
                show=true;
            }else {
                    text_indicaciones.setText("-:-");
                    show=false;
                  }

        }else {
                 code = "";
                 text_indicaciones.setText("-:-");
                 show=false;
              }

        dialog.setTitle("Indicaciones "+code).setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                dialog.dismiss();
            }
        });



        if(show==true) {
                            dialog.show();
                        }




    }

///*************************************************************************************************
///*************************************************************************************************

    /////***********************************************************************************************
///*************************************************************************************************

    public void crearDialogMuestras()
    {


         dialog_muestras = new AlertDialog.Builder(this,R.style.TemaDialogMaterial);

        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutInflater inflater = LayoutInflater.from(this);


        View view = inflater.inflate(R.layout.layout_muestras, null);
        dialog_muestras.setView(view);

        final EditText edit_cantidad_expedientes = (EditText) view.findViewById(R.id.input_total_expedientes);
        final TextInputLayout til_cantidad = (TextInputLayout) view.findViewById(R.id.til_total_expedientes);

        final TextView inicia_en   = (TextView) view.findViewById(R.id.inicia_en);
        final TextView saltos_cada = (TextView) view.findViewById(R.id.saltos_cada);

        TextWatcher validadorExpedientes = new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (filtroLongitud())
                {
                    til_cantidad.setErrorEnabled(false);
                    int total_expedientes = 0;

                    try{
                            total_expedientes = Integer.parseInt( edit_cantidad_expedientes.getText().toString().trim() );

                            if(total_expedientes <= 20)
                            {
                                inicia_en.setText("Evaluar los "+total_expedientes+" expedientes.");
                                saltos_cada.setText("");
                            }else{

                                final int min = 1;
                                final int max = total_expedientes;
                                Random r = new Random();

                                int random = r.nextInt((max - min) + 1) + min;

                                inicia_en.setText("Inicie en el expediente : "+random);
                                saltos_cada.setText("Saltos cada "+(total_expedientes / 20)+" expedientes.");
                            }

                       }catch (NumberFormatException err){
                                                            til_cantidad.setError("Ingrese un total de expedientes valido");
                                                            inicia_en.setText("Inicie en el expediente : ??");
                                                            saltos_cada.setText("Saltos cada ?? expedientes.");
                                                         }
                }else{
                        til_cantidad.setError("Ingrese el total de expedientes");
                        inicia_en.setText("Inicie en el expediente : ??");
                        saltos_cada.setText("Saltos cada ?? expedientes.");
                     }
            }

            private boolean filtroLongitud()
            {
                return edit_cantidad_expedientes.getText().toString().trim().length() > 0;
            }
        };

        edit_cantidad_expedientes.addTextChangedListener(validadorExpedientes);

        dialog_muestras.setTitle("Obtener región de muestra").setPositiveButton("Cerrar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog_muestras, int which)
            {
                dialog_muestras.cancel();
                dialog_muestras.dismiss();
            }
        });

        dialog_muestras.show();

    }



///*************************************************************************************************
///*************************************************************************************************


    public boolean  guardarEvaluacion()
    {
        boolean retornar=false;


        List<DataEvaluadorCalidad> respuestas = new ArrayList<>();
        respuestas = adaptadorEC.getRespuestas();

        //SQLiteDatabase db=linkDB.openDB();


        int array[]=adaptadorEC.contarPreguntas();
        int preguntas=array[0];
        int respondidas=array[1];
        int aprobadas=array[2];
        int negativas=array[3];
        int nas=array[4];

        int errors=0;
        String messages="";

        //if( negativas > 0 )

        if(requiere_hallazgo==true)
        {
            String hallazgo=input_hallazgo.getText().toString();

            System.out.println("\n \n HALLAZGO INPUT VALUE: [ "+hallazgo+" ] \n \n");

            if(hallazgo.equals(""))
            {
                errors++;
                messages+=" * Escriba el hallazgo. \n";
            }

            if (accion_tomada.equals("RESOLUTIVA") || accion_tomada.equals("RESOLUTIVA") )
            {
                errors++;
                messages+=" * Seleccione una Accion valida. ";
            }


            if (errors > 0)
            {
                retornar=false;

                link.showSnackBar(getApplicationContext(),coordinatorLayout,messages,"ERROR");

            }

        }


        if(errors==0)
        {
            //if(negativas>0)
            if(requiere_hallazgo==true)
            {

                Hallazgo hallazgo =linkDB.getHallazgo("CALIDAD",idEvaluacionCalidad,id_indicador_selected,expediente_selected);

                if(hallazgo==null)
                {
                    hallazgo=new Hallazgo(
                                            0,
                                            evaluacion_calidad.id,
                                            "CALIDAD",
                                            id_indicador_selected,
                                            expediente_selected,
                                            user_signed.id,
                                            id_accion_tomada,
                                            0,
                                            0,
                                            input_hallazgo.getText().toString().trim(),
                                            link.getFecha(),
                                            "0000-00-00 00:00:00",
                                            "0000-00-00 00:00:00"
                                         );

                    linkDB.insertarHallazgo(hallazgo,linkDB.openDB());

                }else{
                            hallazgo.setIdAccion(id_accion_tomada);
                            hallazgo.setDescripcion(input_hallazgo.getText().toString().trim());
                            hallazgo.setModificadoAl(link.getFecha());

                            linkDB.actualizarHallazgo(hallazgo,linkDB.openDB());
                     }

                //input_hallazgo.setText("");
                //spinner_acciones.setSelection(0);

            }else{
                        Hallazgo hallazgo =linkDB.getHallazgo("CALIDAD",idEvaluacionCalidad,id_indicador_selected,expediente_selected);

                        if(hallazgo!=null)
                        {
                            hallazgo.setBorradoAl(link.getFecha());
                            linkDB.actualizarHallazgo(hallazgo,linkDB.openDB());

                            input_hallazgo.setText("");
                            spinner_acciones.setSelection(0);
                        }
                 }


            // recorrido de array de respuestas del adaptador del recyclerview

            for (int i = 0; i < respuestas.size(); i++)
            {
                //si fué modificada la respuesta validar update ó insert
                if (respuestas.get(i).modificado == 1)
                {
                    // si id no existe, se insertará registro
                    if (respuestas.get(i).idEvaluacionCalidadCriterio == 0)
                    {
                        EvaluacionCalidadCriterio respuesta = new EvaluacionCalidadCriterio(
                                0,
                                respuestas.get(i).idEvaluacionCalidad,
                                respuestas.get(i).idCriterio,
                                respuestas.get(i).idIndicador,
                                respuestas.get(i).respuesta,
                                respuestas.get(i).idEvaluacionCalidadRegistro,
                                link.getFecha(),
                                "0000-00-00 00:00:00",
                                "0000-00-00 00:00:00"

                        );
                        int insert = linkDB.insertarEvaluacionCalidadCriterio(respuesta, linkDB.openDB());


                    } else { //update

                                EvaluacionCalidadCriterio respuesta = new EvaluacionCalidadCriterio(
                                        respuestas.get(i).getIdEvaluacionCalidadCriterio(),
                                        respuestas.get(i).getIdEvaluacionCalidad(),
                                        respuestas.get(i).idCriterio,
                                        respuestas.get(i).idIndicador,
                                        respuestas.get(i).respuesta,
                                        respuestas.get(i).idEvaluacionCalidadRegistro,
                                        link.getFecha(),
                                        link.getFecha(),
                                        "0000-00-00 00:00:00"

                                );

                        linkDB.actualizarEvaluacionCalidadCriterio(respuesta, linkDB.openDB());

                        //actualizar expediente EvaluacionCalidadRegistro // ya no aqui solo una vez fuera de else
                    }//FIN ELSE INSERTAR


                    //solo un vz update exp aqui
                    int cumple;
                    if(negativas>0)
                    { cumple=0; }else{ cumple=1; }


                    double temp = (double) aprobadas / (preguntas-nas);
                    double promedio = ( temp ) * 100;

                    EvaluacionCalidadRegistro ecr_temp =  linkDB.getEvaluacionCalidadRegistro(id_evaluacion_calidad_registro_selected);

                    ecr_temp.setCumple(cumple);
                    ecr_temp.setPromedio(promedio);
                    ecr_temp.setTotalCriterio(preguntas);

                    ecr_temp.setModificadoAl(link.getFecha());

                    linkDB.actualizarEvaluacionCalidadRegistro(ecr_temp, linkDB.openDB());


                    retornar = true;
                }//FIN IF :

            }
            //db.close();






            revisionBotones();
            link.showSnackBar(getApplicationContext(),coordinatorLayout,"Evaluacion guardada correctamente", "INFO");

            cambios=false;
            adaptadorEC.cambios=false;
            caso_indicador_cancelar=false;





            retornar = true;
        }// fin errors = 0

        // db.close();





        return retornar;
    }


///*************************************************************************************************
///*************************************************************************************************



    public class HiloGuardar extends AsyncTask<Void, Void, Void>
    {
        public void onPreExecute()
        {
            progress_dialog_guardar.show();

        }



        protected Void doInBackground(Void... params)
        {
                //realizar la operación aquí
            guardarEvaluacion();

            return null;
        }

        public void onPostExecute(Void unused)
        {
            revisionBotones();
            link.showSnackBar(getApplicationContext(),coordinatorLayout,"Evaluacion guardada correctamente", "INFO");

            cambios=false;
            adaptadorEC.cambios=false;
            caso_indicador_cancelar=false;


            progress_dialog_guardar.dismiss();
        }

    }

///*************************************************************************************************
///*************************************************************************************************


    private class CargarCriteriosExpedienteAsync extends AsyncTask<String, Integer, Integer>
    {

        protected void onPreExecute()
        {
            link.printConsola(" (**) (**) (**) (**) (**) (**) (**) (**) (**) (**)   CARGAR CRITERIOS CHECKPOINT     1     (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) ");

            progress_cargar_criterios = ProgressDialog.show(EvaluadorCalidad.this, "Por favor espere", " Cargando Criterios ...",true);
            progress_cargar_criterios.setCancelable(false);

            link.printConsola(" (**) (**) (**) (**) (**) (**) (**) (**) (**) (**)   CARGAR CRITERIOS CHECKPOINT     2     (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) ");

        }

        protected Integer doInBackground(String... datos)
        {

///****************************************************************************************************************************************************************************************************************************
            link.printConsola(" (**) (**) (**) (**) (**) (**) (**) (**) (**) (**)   CARGAR CRITERIOS CHECKPOINT     3     (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) ");


            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    input_hallazgo.setText("");
                    spinner_acciones.setSelection(0);
                }
            });



            EvaluacionCalidadRegistro ECR = lista_expedientes.get(position_expediente_selected);
            id_evaluacion_calidad_registro_selected = ECR.getId();
            expediente_selected = ECR.expediente;

            if (ECR.id > 0)
            {

                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        recycler_evaluador.setVisibility(View.VISIBLE);
                    }
                });




                List<LugarVerificacion> listaLV = new ArrayList<>();
                //Lista de objetos que llevan las preguntas/criterios para el indicador

                //List<DataEvaluadorCalidad> DEC = new ArrayList<>();
                DEC = new ArrayList<>();


                try {  //Se coloca el adaptador, se valida la excepcion por si fuera vacio el adaptador

                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {

                            adaptadorEC = new AdaptadorEvaluadorCalidad(DEC,respuestasValidacionExpediente,EvaluadorCalidad.this,EvaluadorCalidad.this,cerrado);

                            recycler_evaluador.setAdapter(adaptadorEC);

                            RecyclerView.ItemDecoration itemDecoration2 = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);

                            recycler_evaluador.addItemDecoration(itemDecoration2);
                            recycler_evaluador.setItemAnimator(new DefaultItemAnimator());

                        }
                    });




                } catch (NullPointerException err){
                    err.printStackTrace();
                }

                listaLV = linkDB.getLugaresVerificacion(nivel_cone,id_indicador_selected);
                String tipo_item = "LUGAR";
                int posicion_item = 0;

                for (int i = 0; i < listaLV.size(); i++)
                {

                    DEC.add(new DataEvaluadorCalidad(
                            idEvaluacionCalidad,
                            "LUGAR",
                            id_indicador_selected,
                            listaLV.get(i).id,
                            listaLV.get(i).nombre,
                            0,
                            "--",
                            400,
                            0,///tiene validacion
                            -1,
                            0,
                            0,
                            0
                    ));

                    System.out.println("TIPO: LUGAR, ID:" + listaLV.get(i).id + ", NOMBRELV: " + listaLV.get(i).nombre);

                    List<Criterio> listaC = new ArrayList<>();
                    listaC = linkDB.getCriterios(nivel_cone,id_indicador_selected, listaLV.get(i).id);

                    for (int j = 0; j < listaC.size(); j++)
                    {

                        EvaluacionCalidadCriterio eval = linkDB.getEvaluacionCalidadCriterio(
                                idEvaluacionCalidad,
                                id_indicador_selected,
                                listaC.get(j).id,
                                id_evaluacion_calidad_registro_selected  );

                        //validar compatibilidad de expediente para no repetir los mismos respuestas a cada expediente
                        // en el recycler

                        int respuesta = -1;
                        int id_ecc = 0;

                        if (eval == null)
                        {

                        } else {
                            respuesta = eval.getAprobado();
                            id_ecc = eval.getId();
                        }

                        DEC.add(new DataEvaluadorCalidad(
                                idEvaluacionCalidad,
                                "CRITERIO",
                                id_indicador_selected,
                                listaLV.get(i).id,
                                listaLV.get(i).nombre,
                                listaC.get(j).id,
                                listaC.get(j).nombre,
                                listaC.get(j).habilitarNoAplica,
                                listaC.get(j).tieneValidacion,
                                respuesta,
                                id_ecc,
                                id_evaluacion_calidad_registro_selected,///        ID CALIDAD REGISTRO PENDIENTE CONSEGUIRLO /// CONSEGUIDO DE ID SELECTED ON SPINNER
                                0
                        ));


                        System.out.println("TIPO: CRITERIO, ID:" + listaC.get(j).id + ", NOMBRECRITERIO: " + listaC.get(j).nombre);
                    }
                    // 1.- EN POSICION CERO SE AGREGA LUGAR DE VERIF. A LA LISTA DE DATOS DEL ADAPTADOR.
                    // 2.- SE LEEN TODOS LOS CRITERIOS DE ESE LUGAR DE VERIFICACION Y EL INDICADOR
                    //     SELECCIONADO (id_indicador)
                    // 3.- RECORRER LISTA DE CRITERIOS LANZANDO CADA UNO A LA LISTA DE DATOS DEL ADAPTADOR

                } //FIN FOR LISTA DE LUGARES DE VERIFICACION

                /// System.out.println("LERC DIM : " + LERC.size());
                /// adaptadorEC.updateDataRecycler(DEC);


                Evaluacion eva= linkDB.getEvaluacion("CALIDAD",evaluacion_calidad.id,linkDB.openDB());

                respuestasValidacionExpediente = linkDB.getListaCriterioValidacionRespuestaParaAdaptador("CALIDAD",idEvaluacionCalidad,expediente_selected);


                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        adaptadorEC.updateDataRecycler(DEC,respuestasValidacionExpediente,cerrado);
                    }
                });


                // setCount();
                // checkHallasgos();


            } else {//FIN IF ID MAYOR QUE CERO

                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    System.out.println(" EXPEDIENTE NULL , ID_ECR_SELECTED: "+id_evaluacion_calidad_registro_selected+".");
                                    recycler_evaluador.setVisibility(View.GONE);
                                    //input_hallazgo.setText("");
                                    //spinner_acciones.setSelection(0);

                                    //input_hallazgo.setVisibility(View.GONE);
                                    ///spinner_acciones.setVisibility(View.GONE);
                                    //setCount();
                                    //System.out.println("DESPUES DE SET_COUNT() CON ID ECR NULL");

                                }
                            });
                     }

///****************************************************************************************************************************************************************************************************************************
/////********************************************    H A L L A Z G O S


            ///EL CHEQUEO DE HALLAZGOS DEBE SER POR EXPEDIENTE


            link.printConsola(" (**) (**) (**) (**) (**) (**) (**) (**) (**) (**)   CARGAR CRITERIOS CHECKPOINT     4     (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) ");




            final Hallazgo hallazgo_set= linkDB.getHallazgo("CALIDAD",idEvaluacionCalidad,id_indicador_selected,expediente_selected);

            if(hallazgo_set!=null)
            {

                runOnUiThread(new Runnable()
                {
                    public void run()
                    {

                        input_hallazgo.setVisibility(View.VISIBLE);
                        spinner_acciones.setVisibility(View.VISIBLE);


                        input_hallazgo.setText(hallazgo_set.descripcion);
                    }
                });


                int id_accion_selected = hallazgo_set.getIdAccion();
                String nombre_accion = "";

                for(int i=0; i<listaAcciones.size(); i++)
                {
                    if(listaAcciones.get(i).getId()==id_accion_selected)
                    {
                        nombre_accion = listaAcciones.get(i).getNombre();
                    }
                }

                for(int j=0; j<acciones.size(); j++)
                {
                    if(nombre_accion.equals(acciones.get(j)))
                    {

                        final int finalJ = j;
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                spinner_acciones.setSelection(finalJ);
                            }
                        });
                    }
                }



            }else{

                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        input_hallazgo.setText("");
                        spinner_acciones.setSelection(0);

                        input_hallazgo.setVisibility(View.GONE);
                        spinner_acciones.setVisibility(View.GONE);
                    }
                });
                ///  link.printToast("HALLAZGO NULL PARA ESTE INDICADOR", this);
            }



///****************************************************************************************************************************************************************************************************************************
            return 0;
        }

        protected void onProgressUpdate(Integer... values)
        {

        }

        protected void onCancelled()
        {
            progress_cargar_criterios.dismiss();

            link.showSnackBar(getApplicationContext(),coordinatorLayout,"Cancelado","ERROR");
        }


        protected void onPostExecute(Integer errors)
        {
            link.printConsola(" (**) (**) (**) (**) (**) (**) (**) (**) (**) (**)   CARGAR CRITERIOS CHECKPOINT     5     (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) ");

            progress_cargar_criterios.dismiss();

            link.printConsola(" (**) (**) (**) (**) (**) (**) (**) (**) (**) (**)   CARGAR CRITERIOS CHECKPOINT     6     (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) ");


            avance_validacion();

            link.printConsola(" (**) (**) (**) (**) (**) (**) (**) (**) (**) (**)   CARGAR CRITERIOS CHECKPOINT     1     (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) (**) ");

            //setCount();
        }


    }///FIN CLASS DESCARGA ASYNC

///*************************************************************************************************
///*************************************************************************************************



    public void cargarCriteriosExpediente()
    {

        new CargarCriteriosExpedienteAsync().execute();

   /*

        if(progress_cargar_criterios!=null){ progress_cargar_criterios.dismiss(); progress_cargar_criterios=null;}

        progress_cargar_criterios = ProgressDialog.show(EvaluadorCalidad.this, "Por favor espere", " Cargando Criterios ...",true);
        progress_cargar_criterios.setCancelable(false);


        new Thread(new Runnable()
        {
            public void run()
            {


///****************************************************************************************************************************************************************************************************************************
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        input_hallazgo.setText("");
                        spinner_acciones.setSelection(0);
                    }
                });



        EvaluacionCalidadRegistro ECR = lista_expedientes.get(position_expediente_selected);
        id_evaluacion_calidad_registro_selected = ECR.getId();
        expediente_selected = ECR.expediente;

        if (ECR.id > 0)
        {

            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    recycler_evaluador.setVisibility(View.VISIBLE);
                }
            });




            List<LugarVerificacion> listaLV = new ArrayList<>();
            //Lista de objetos que llevan las preguntas/criterios para el indicador

            //List<DataEvaluadorCalidad> DEC = new ArrayList<>();
            DEC = new ArrayList<>();


            try {  //Se coloca el adaptador, se valida la excepcion por si fuera vacio el adaptador

                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {

                            adaptadorEC = new AdaptadorEvaluadorCalidad(DEC,respuestasValidacionExpediente,EvaluadorCalidad.this);

                            recycler_evaluador.setAdapter(adaptadorEC);

                            RecyclerView.ItemDecoration itemDecoration2 = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);

                            recycler_evaluador.addItemDecoration(itemDecoration2);
                            recycler_evaluador.setItemAnimator(new DefaultItemAnimator());

                        }
                    });




                } catch (NullPointerException err){
                                                    err.printStackTrace();
                                                  }

            listaLV = linkDB.getLugaresVerificacion(nivel_cone,id_indicador_selected);
            String tipo_item = "LUGAR";
            int posicion_item = 0;

            for (int i = 0; i < listaLV.size(); i++)
            {

                DEC.add(new DataEvaluadorCalidad(
                                                    idEvaluacionCalidad,
                                                    "LUGAR",
                                                    id_indicador_selected,
                                                    listaLV.get(i).id,
                                                    listaLV.get(i).nombre,
                                                    0,
                                                    "--",
                                                    400,
                                                    0,///tiene validacion
                                                    -1,
                                                    0,
                                                    0,
                                                    0
                                                ));

                System.out.println("TIPO: LUGAR, ID:" + listaLV.get(i).id + ", NOMBRELV: " + listaLV.get(i).nombre);

                List<Criterio> listaC = new ArrayList<>();
                listaC = linkDB.getCriterios(nivel_cone,id_indicador_selected, listaLV.get(i).id);

                for (int j = 0; j < listaC.size(); j++)
                {

                    EvaluacionCalidadCriterio eval = linkDB.getEvaluacionCalidadCriterio(
                                                                                            idEvaluacionCalidad,
                                                                                            id_indicador_selected,
                                                                                            listaC.get(j).id,
                                                                                            id_evaluacion_calidad_registro_selected  );

                    //validar compatibilidad de expediente para no repetir los mismos respuestas a cada expediente
                    // en el recycler

                    int respuesta = -1;
                    int id_ecc = 0;

                    if (eval == null)
                    {

                    } else {
                                respuesta = eval.getAprobado();
                                id_ecc = eval.getId();
                            }

                    DEC.add(new DataEvaluadorCalidad(
                                                            idEvaluacionCalidad,
                                                            "CRITERIO",
                                                            id_indicador_selected,
                                                            listaLV.get(i).id,
                                                            listaLV.get(i).nombre,
                                                            listaC.get(j).id,
                                                            listaC.get(j).nombre,
                                                            listaC.get(j).habilitarNoAplica,
                                                            listaC.get(j).tieneValidacion,
                                                            respuesta,
                                                            id_ecc,
                                                            id_evaluacion_calidad_registro_selected,///        ID CALIDAD REGISTRO PENDIENTE CONSEGUIRLO /// CONSEGUIDO DE ID SELECTED ON SPINNER
                                                            0
                                                    ));


                    System.out.println("TIPO: CRITERIO, ID:" + listaC.get(j).id + ", NOMBRECRITERIO: " + listaC.get(j).nombre);
                }
                // 1.- EN POSICION CERO SE AGREGA LUGAR DE VERIF. A LA LISTA DE DATOS DEL ADAPTADOR.
                // 2.- SE LEEN TODOS LOS CRITERIOS DE ESE LUGAR DE VERIFICACION Y EL INDICADOR
                //     SELECCIONADO (id_indicador)
                // 3.- RECORRER LISTA DE CRITERIOS LANZANDO CADA UNO A LA LISTA DE DATOS DEL ADAPTADOR

            } //FIN FOR LISTA DE LUGARES DE VERIFICACION

            /// System.out.println("LERC DIM : " + LERC.size());
            /// adaptadorEC.updateDataRecycler(DEC);


            Evaluacion eva= linkDB.getEvaluacion("CALIDAD",evaluacion_calidad.id,linkDB.openDB());

            respuestasValidacionExpediente = linkDB.getListaCriterioValidacionRespuestaParaAdaptador("CALIDAD",idEvaluacionCalidad,expediente_selected);


            if(eva.cerrado==1)
            {
                try {  //Se coloca el adaptador, se valida la excepcion por si fuera vacio el adaptador


                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    adaptadorECCerrado = new AdaptadorEvaluadorCalidadCerrado( DEC, getApplicationContext());
                                    recycler_evaluador.setAdapter(adaptadorECCerrado);

                                    adaptadorECCerrado.updateDataRecycler(DEC);

                                }
                            });


                } catch (NullPointerException err) {
                    err.printStackTrace();
                }

            }else{

                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            adaptadorEC.updateDataRecycler(DEC,respuestasValidacionExpediente);
                        }
                    });
                }


           // setCount();
           // checkHallasgos();


        } else {//FIN IF ID MAYOR QUE CERO



                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                System.out.println(" EXPEDIENTE NULL , ID_ECR_SELECTED: "+id_evaluacion_calidad_registro_selected+".");
                                recycler_evaluador.setVisibility(View.GONE);
                                //input_hallazgo.setText("");
                                //spinner_acciones.setSelection(0);

                                //input_hallazgo.setVisibility(View.GONE);
                                ///spinner_acciones.setVisibility(View.GONE);
                                //setCount();
                                System.out.println("DESPUES DE SET_COUNT() CON ID ECR NULL");

                            }
                        });
              }

///****************************************************************************************************************************************************************************************************************************
/////********************************************    H A L L A Z G O S


                ///EL CHEQUEO DE HALLAZGOS DEBE SER POR EXPEDIENTE



                final Hallazgo hallazgo_set= linkDB.getHallazgo("CALIDAD",idEvaluacionCalidad,id_indicador_selected,expediente_selected);

                if(hallazgo_set!=null)
                {

                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {

                            input_hallazgo.setVisibility(View.VISIBLE);
                            spinner_acciones.setVisibility(View.VISIBLE);


                            input_hallazgo.setText(hallazgo_set.descripcion);
                        }
                    });


                    int id_accion_selected = hallazgo_set.getIdAccion();
                    String nombre_accion = "";

                    for(int i=0; i<listaAcciones.size(); i++)
                    {
                        if(listaAcciones.get(i).getId()==id_accion_selected)
                        {
                            nombre_accion = listaAcciones.get(i).getNombre();
                        }
                    }

                    for(int j=0; j<acciones.size(); j++)
                    {
                        if(nombre_accion.equals(acciones.get(j)))
                        {

                            final int finalJ = j;
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    spinner_acciones.setSelection(finalJ);
                                }
                            });
                        }
                    }



                }else{

                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                input_hallazgo.setText("");
                                spinner_acciones.setSelection(0);

                                input_hallazgo.setVisibility(View.GONE);
                                spinner_acciones.setVisibility(View.GONE);
                            }
                        });
                        ///  link.printToast("HALLAZGO NULL PARA ESTE INDICADOR", this);
                    }



///****************************************************************************************************************************************************************************************************************************
                progress_cargar_criterios.dismiss();

                //avance_validacion();

            }
        }).start();



*/

        //revisionBotones();




    }


    ///*************************************************************************************************


    public void confirmSalir()
    {

        if(cambios) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Existen cambios sin guardar.\n¿ Quieres salir de la evaluación ?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        }else {
            finish();
        }
    }
///*************************************************************************************************

    public void borrarIndicadorDeEvaluacion()
    {
        for (int i = 0; i < lista_expedientes.size(); i++)
        {
            EvaluacionCalidadRegistro ecr = lista_expedientes.get(i);

            if (ecr.getId() > 0)
            {

                List<EvaluacionCalidadCriterio> lista = linkDB.getEvaluacionCalidadCriterio(ecr.getId());

                for (int j = 0; j < lista.size(); j++)
                {
                    EvaluacionCalidadCriterio ecc_temp = lista.get(j);
                    ecc_temp.setBorradoAl(link.getFecha());
                    linkDB.actualizarEvaluacionCalidadCriterio(ecc_temp, linkDB.openDB());
                }

                // se borra el expediente de la tabla EvaluacionCalidadRegistro
                EvaluacionCalidadRegistro ecr2 = linkDB.getEvaluacionCalidadRegistro(ecr.getId());
                ecr2.setBorradoAl(link.getFecha());
                linkDB.actualizarEvaluacionCalidadRegistro(ecr2, linkDB.openDB());

                //refreshSpinnerExpedientes(id_indicador_selected);
                //spinner_expedientes.setSelection(0, true);
                //link.showSnackBar(context, coordinatorLayout, "Expediente borrado correctamente", "INFO");

            }


        }

        refreshSpinnerIndicadoresAgregados();
    }

///*************************************************************************************************

    public void borrarExpedienteDeIndicador()
    {
        List<EvaluacionCalidadCriterio> lista = linkDB.getEvaluacionCalidadCriterio(id_evaluacion_calidad_registro_selected);

        for (int i=0; i<lista.size(); i++)
        {
            EvaluacionCalidadCriterio ecc_temp = lista.get(i);
            ecc_temp.setBorradoAl(link.getFecha());
            linkDB.actualizarEvaluacionCalidadCriterio(ecc_temp,linkDB.openDB());
        }

        // se borra el expediente de la tabla EvaluacionCalidadRegistro
        EvaluacionCalidadRegistro ecr = linkDB.getEvaluacionCalidadRegistro(id_evaluacion_calidad_registro_selected);
        ecr.setBorradoAl(link.getFecha());
        linkDB.actualizarEvaluacionCalidadRegistro(ecr, linkDB.openDB());

        refreshSpinnerExpedientes(id_indicador_selected,false);
        spinner_expedientes.setSelection(0, true);
        link.showSnackBar(context, coordinatorLayout, "Expediente borrado correctamente", "INFO");
    }

///*************************************************************************************************

///*************************************************************************************************
/////*****************************************************************************************************


}//FIN CLASE EVALUADOR RECURSO


