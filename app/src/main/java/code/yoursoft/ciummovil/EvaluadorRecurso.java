package code.yoursoft.ciummovil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EvaluadorRecurso extends AppCompatActivity implements InterfaceRecurso
{

    Funciones link;
    DBManager linkDB;
    Context context;

    //Dialog dialog;
    AlertDialog.Builder dialog;

    Button boton_ok_ficha;

    List<Indicador> lista_indicadores=new ArrayList<>();
    Spinner spinner_indicadores;
    ArrayAdapter<String> adaptador_indicadores;
    List<String> INDICADORES=new ArrayList<>();

    List<Indicador> lista_indicadores_agregados= new ArrayList<>();
    Spinner spinner_indicadores_agregados;
    ArrayAdapter<String> adaptador_indicadores_agregados;
    List<String> INDICADORES_AGREGADOS=new ArrayList<>();

    Spinner spiner_acciones;
    ArrayAdapter<String> adaptador_acciones;
    List<String> acciones=new ArrayList<>();

    List<Accion> listaAcciones = new ArrayList<>();

    ImageButton boton_agregar_indicador;
    ImageButton boton_borrar_indicador;
    EditText input_hallazgo;
    TextView text_count;

    String accion_tomada="";
    int id_accion_tomada=0;

    Menu menu;
    MenuItem item;

    private CoordinatorLayout coordinatorLayout;

    private RelativeLayout layout_indicadores;
    private RelativeLayout layout_hallazgo;

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


    Clues clues_selected;
    Usuarios user_signed=null;

    Evaluacion evaluacion_recurso;
    int idEvaluacionrecurso=0;
    int nivel_cone=0;

    int id_indicador_selected=0;
    int position_indicador_selected=0;



    RecyclerView recycler_evaluador;
    AdaptadorEvaluadorRecurso adaptadorER;
    AdaptadorEvaluadorRecursoCerrado adaptadorERCerrado;

    boolean cambios=false;
    boolean caso_error_guardar=false;

    List<DataEvaluadorRecurso> der_temp;

    ProgressDialog progress_guardar_evaluacion;
    ProgressDialog progress_cargar_criterios;
    ProgressDialog progress_avance_validacion;

    List<LugarVerificacion> listaLV = new ArrayList<>();
    ///Lista de objetos que llevan las preguntas/criterios para el indicador
    List<DataEvaluadorRecurso> DER = new ArrayList<>();
    List<EvaluacionRecursoCriterio> LERC = new ArrayList<>();


    MenuItem menuGuardar ;
    MenuItem menuCerrar ;
    MenuItem menuFirmar ;
    MenuItem menuFicha ;

    MenuItem menuTransferencia ;

    int cerrado=0;

    private Button boton_indicador_seleccionado;

    public void avance_validacion()
    {
        new AvanceValidacionAsync().execute();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    public void onRestart()
    {
        super.onRestart();

        revisionBotones();
    }

    public void onStart()
    {
        super.onStart();

        //new AvanceValidacionAsync().execute();
    }


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this.getApplicationContext();
        link = new Funciones();
        linkDB=new DBManager(this);
        user_signed = linkDB.getSignedUser();

        if(user_signed==null)
        {
            link.goLogin(this);
        }

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle vars = getIntent().getExtras();
        if (vars != null)
        {
            idEvaluacionrecurso = vars.getInt("idEvaluacionRecurso");
        }

        setContentView(R.layout.layout_evaluador_recurso);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        //layout_hallazgo=(LinearLayout)this.findViewById(R.id.layout_hallazgo);


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_evaluador_recurso);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                confirmSalir();

            }
        });



        System.out.println("ID EVALUACION ENVIAR : "+idEvaluacionrecurso+".]]]");

        evaluacion_recurso= linkDB.getEvaluacion("RECURSO", idEvaluacionrecurso, linkDB.openDB());

        if(evaluacion_recurso==null)
        {
            System.out.println("ERROR EVALUACION RECURSO REGRESO NULL....]]]");
        }


        cerrado = evaluacion_recurso.getCerrado();

        clues_selected=linkDB.getClues(evaluacion_recurso.clues);
        nivel_cone=clues_selected.idCone;


        getSupportActionBar().setTitle(clues_selected.clues);
        toolbar.setSubtitle(evaluacion_recurso.fechaEvaluacion);

        boton_agregar_indicador = (ImageButton) findViewById(R.id.boton_agregar_indicador);
        boton_borrar_indicador = (ImageButton) findViewById(R.id.image_borrar_indicador);

        boton_indicador_seleccionado = (Button) findViewById(R.id.boton_indicador_seleccionado);

        layout_hallazgo  = (RelativeLayout) findViewById(R.id.layout_hallazgo);

        //layout_hallazgo.setBackground(link.backgroundWithBorder(getResources().getColor(R.color.accent),getResources().getColor(R.color.PrimaryDarkColor)));

        layout_hallazgo.setVisibility(View.GONE);

////************************************************************************************************************

        //lista_indicadores.add(new Indicador(-1, " : : ", " : : ", "", ""));
        lista_indicadores.addAll(linkDB.getIndicadoresEvaluacionRecurso(nivel_cone));

        if(lista_indicadores.size()>0)
        {
            for(int i=0; i<lista_indicadores.size(); i++)
            {
                INDICADORES.add(String.valueOf(lista_indicadores.get(i).codigo + " - " + lista_indicadores.get(i).nombre));
            }
        }else{
            System.out.println("ERROR: LISTA DE INDICADORES REGRESÓ VACIA ]]]");
        }


        lista_indicadores_agregados.add(new Indicador(-1, "Seleccione indicador", " : : ", "", "",""));
        //System.out.println("LISTA INDICADORES AGREGADOS : " + lista_indicadores_agregados.size());
        lista_indicadores_agregados.addAll(linkDB.getIndicadoresAgregadosRecurso(idEvaluacionrecurso));
        //System.out.println("LISTA INDICADORES AGREGADOS AFTER GET DB: " + lista_indicadores_agregados.size());

        if(lista_indicadores_agregados.size()>0)
        {
            for(int i=0; i<lista_indicadores_agregados.size(); i++)
            {
                System.out.println("PASADAS LLENADO IND AGREGADOS : "+i);

                if( i == 0 )
                {
                    INDICADORES_AGREGADOS.add(String.valueOf( lista_indicadores_agregados.get(i).codigo) );
                }else{
                        INDICADORES_AGREGADOS.add(String.valueOf( lista_indicadores_agregados.get(i).codigo+" - "+lista_indicadores_agregados.get(i).nombre) );
                     }

                System.out.println("PASADAS LLENADO IND AGREGADOS P2 : "+i);
            }
        }else{
                System.out.println("ERROR: LISTA DE INDICADORES AGREGADOS REGRESÓ VACIA ]]]");
             }

///***********************************************************************************************************

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



        adaptador_indicadores = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, INDICADORES);

        spinner_indicadores = (Spinner) findViewById(R.id.spinner_indicadores);
        spinner_indicadores.setAdapter(adaptador_indicadores);


        adaptador_indicadores_agregados = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, INDICADORES_AGREGADOS);

        spinner_indicadores_agregados = (Spinner) findViewById(R.id.spinner_indicadores_agregados);
        spinner_indicadores_agregados.setAdapter(adaptador_indicadores_agregados);


        adaptador_acciones = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, acciones );

        spiner_acciones = (Spinner) findViewById(R.id.spinner_accion);
        spiner_acciones.setAdapter(adaptador_acciones);

        input_hallazgo = (EditText) findViewById(R.id.input_hallazgo);

        spiner_acciones.setVisibility(View.GONE);
        input_hallazgo.setVisibility(View.GONE);









        text_count= (TextView) findViewById(R.id.text_count);
        recycler_evaluador = (RecyclerView) findViewById(R.id.recycler_evaluador_recurso);

        LinearLayoutManager linearLM = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLM.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_evaluador.setLayoutManager(linearLM);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);

        recycler_evaluador.addItemDecoration(itemDecoration);
        recycler_evaluador.setItemAnimator(new DefaultItemAnimator());




        LERC = linkDB.getEvaluacionRecursoCriterio(idEvaluacionrecurso, id_indicador_selected);


        try {  //Se coloca el adaptador, se valida la excepcion por si fuera vacio el adaptador
                adaptadorER = new AdaptadorEvaluadorRecurso( DER, getApplicationContext(),EvaluadorRecurso.this,cerrado);
                recycler_evaluador.setAdapter(adaptadorER);

            } catch (NullPointerException err) {
                                                    err.printStackTrace();
                                                }


///*************************************************************************************************


        spiner_acciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                accion_tomada = acciones.get(position);

                for (int i = 0; i < listaAcciones.size(); i++)
                {
                    if (accion_tomada.equals(listaAcciones.get(i).getNombre()))
                    {
                        id_accion_tomada = listaAcciones.get(i).getId();
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


///*************************************************************************************************

        spinner_indicadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


///*************************************************************************************************

        spinner_indicadores_agregados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                    if(cambios)
                    {

                        der_temp = adaptadorER.getRespuestas();

                        preguntarCambioIndicador( position_indicador_selected, id_indicador_selected,
                                                  position, lista_indicadores_agregados.get(position).id , der_temp);

                    }else{

                            if(caso_error_guardar)
                            {

                                adaptadorER.updateDataRecycler(der_temp,cerrado);
                                caso_error_guardar=false;
                                cambios=true;

                                System.out.println("----- NO CAMBIOS, CASO ERROR DE GUARDADO ... ");

                            }else{


                                    System.out.println("------ NO  CAMBIOS, NO ERROR  ");

                                    position_indicador_selected = position;
                                    id_indicador_selected = lista_indicadores_agregados.get(position).id;

                                    cargarCriteriosRecycler();

                                    ///posible causa perdida
                                    if(id_indicador_selected > 0)
                                    {
                                        der_temp = adaptadorER.getRespuestas();
                                    }

                                 }


                         }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


///***********************************************************************************************


        recycler_evaluador.addOnItemTouchListener(new RecyclerItemClickListener(this, recycler_evaluador, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                new ShowAvance().execute();
                //setCount();
            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                // ...
            }
        }));




///***********************************************************************************************


        boton_borrar_indicador.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(id_indicador_selected > 0)
                {
                    borrarIndicadorSeleccionado();
                }else{
                      link.showSnackBar(context,coordinatorLayout,"Seleccione indicador valido para borrar","ERROR");
                     }



            }
        });


///***********************************************************************************************
    }


///***********************************************************************************************


    private class ShowAvance extends AsyncTask<String, Integer, Integer>
    {
        protected Integer doInBackground(String... datos)
        {
             int errors=0;
             try {

                 System.out.println("ANTES DE DORMIR EL THREAD ...");

                   Thread.sleep(300);

                 System.out.println("DESPUES DE DORMIR EL THREAD ...");

                 } catch (InterruptedException e)
                   {
                       System.out.println("ERROR EJECUTANDO THREAD PARA UPDATE DE CONTEO ...");
                       e.printStackTrace();
                   }
/*
            runOnUiThread(new Runnable()
            {
                public void run()
                {

                    setCount();
                }
            });
*/
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
            System.out.println("ON POST EXECUTE SHOW AVANCE ...");
            setCount();
        }

        protected void onCancelled()
        {
            link.showSnackBar(getApplicationContext(),coordinatorLayout,"Cancelado","ERROR");
        }

    }///FIN CLASS SHOW AVANCE ASYNC



///***********************************************************************************************


    public void refreshSpinnerIndicadoresAgregados()
    {
        lista_indicadores_agregados.clear();
        INDICADORES_AGREGADOS.clear();

        lista_indicadores_agregados.add(new Indicador(-1, "Seleccione indicador", " : : ", "", "",""));
        lista_indicadores_agregados.addAll(linkDB.getIndicadoresAgregadosRecurso(idEvaluacionrecurso));

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
        spinner_indicadores_agregados.setSelection(0,true);

    }

///***********************************************************************************************


    public void checkHallasgos()
    {
        // link.printToast("EVALUACION: [ " + idEvaluacionrecurso + " ] INDICADOR : [ " + id_indicador_selected + " ]", this);


        Hallazgo hallazgo_set= linkDB.getHallazgo("RECURSO", idEvaluacionrecurso, id_indicador_selected,null);

        if(hallazgo_set!=null)
        {
            layout_hallazgo.setVisibility(View.VISIBLE);

            input_hallazgo.setText(hallazgo_set.descripcion);

            int id_accion_selected = hallazgo_set.getIdAccion();
            String nombre_accion = "";

            for( int i=0; i < listaAcciones.size(); i++ )
            {
                if( listaAcciones.get(i).getId()==id_accion_selected )
                {
                    nombre_accion = listaAcciones.get(i).getNombre();
                }
            }

            for(int j=0; j<acciones.size(); j++)
            {
                if(nombre_accion.equals(acciones.get(j)))
                {
                    spiner_acciones.setSelection(j);
                }
            }



        }else{
                    input_hallazgo.setText("");
                    spiner_acciones.setSelection(0);

                    layout_hallazgo.setVisibility(View.GONE);
                    //link.printToast("HALLAZGO NULL PARA ESTE INDICADOR", this);
             }

    }


///***********************************************************************************************


    public void revisionBotones()
    {

        MenuItem guardar = menu.findItem(R.id.guardar_evaluacion);
        MenuItem cerrar = menu.findItem(R.id.cerrar_evaluacion);
        MenuItem firmar = menu.findItem(R.id.firmar_evaluacion);
        MenuItem ver_ficha = menu.findItem(R.id.ficha_tecnica);

        MenuItem transferencia = menu.findItem(R.id.transferencia);


        Evaluacion evaluacion=linkDB.getEvaluacion("RECURSO", idEvaluacionrecurso, linkDB.openDB());

        int id_evaluacion=idEvaluacionrecurso;

        int total_preguntas=0;
        int preguntas_pendientes=0;

        List<Indicador> listaI = new ArrayList<>();
        listaI = linkDB.getIndicadoresAgregadosRecurso(idEvaluacionrecurso);



        if(listaI.size() > 0 )
        {
            for(int i=0; i<listaI.size(); i++)
            {
                int id_indicador=listaI.get(i).id;

                List<LugarVerificacion> listaLV = new ArrayList<>();
                listaLV = linkDB.getLugaresVerificacion(nivel_cone, id_indicador);

                for (int j = 0; j < listaLV.size(); j++)
                {
                    int id_lugar = listaLV.get(j).id;

                    List<Criterio> listaC=new ArrayList<>();
                    listaC = linkDB.getCriterios(nivel_cone, id_indicador,id_lugar);

                    for (int h = 0; h < listaC.size(); h++)
                    {
                        int id_criterio = listaC.get(h).id;

                        total_preguntas++;
                        EvaluacionRecursoCriterio erc=linkDB.getEvaluacionRecursoCriterio(id_evaluacion,id_indicador,id_criterio);

                        if(erc==null)
                        {
                            preguntas_pendientes++;
                        }

                    }///fin de la lista de criterios

                }//fin lista de lugares de verificacion

            }///fin de la lista de indicadores pertenecientes a la evaluacion

            int preguntas_contestadas = total_preguntas - preguntas_pendientes;


            if(evaluacion.cerrado==1)
            {
                boton_agregar_indicador.setVisibility(View.GONE);
                boton_borrar_indicador.setVisibility(View.GONE);

                guardar.setVisible(false);
                cerrar.setVisible(false);

                if(evaluacion.firma.equals(""))
                {
                    firmar.setVisible(true);
                }else{
                       firmar.setVisible(false);
                     }

                transferencia.setVisible(false);
                //enviar.setVisible(true);
                //recibir.setVisible(false);

            }else{// Si la evaluación no esta cerrada

                        boton_agregar_indicador.setVisibility(View.VISIBLE);

                        transferencia.setVisible(true);
                        //enviar.setVisible(true);
                        //recibir.setVisible(true);


                        if(id_indicador_selected > 0)
                            {

                                if(preguntas_contestadas > 0)
                                {
                                    guardar.setVisible(true);
                                }else{
                                        guardar.setVisible(false);
                                     }

                                text_count.setVisibility(View.VISIBLE);
                                boton_borrar_indicador.setVisibility(View.VISIBLE);

                            }else {
                                        guardar.setVisible(false);
                                        text_count.setVisibility(View.GONE);
                                        boton_borrar_indicador.setVisibility(View.GONE);
                                  }

                            if(preguntas_pendientes > 0)
                            {
                                cerrar.setVisible(false);
                                firmar.setVisible(false);
                            }else{
                                    cerrar.setVisible(true);
                                    firmar.setVisible(false);
                                 }

                 }///fin else si la evaluacion no est cerrada





        }else{/// Si no hay indicadores agregados

                guardar.setVisible(false);
                cerrar.setVisible(false);
                firmar.setVisible(false);

                //transferencia.setVisible(false);



                //enviar.setVisible(false);
                //recibir.setVisible(true);

                text_count.setVisibility(View.GONE);
                boton_borrar_indicador.setVisibility(View.GONE);
                boton_agregar_indicador.setVisibility(View.VISIBLE);

                if(id_indicador_selected > 0)
                {
                    int array[] = adaptadorER.contarPreguntas();
                    int preguntas = array[0];
                    int respondidas = array[1];
                    int aprobadas = array[2];
                    int negativas = array[3];

                    if( respondidas > 0 )
                    {
                        guardar.setVisible(true);
                    }else{
                            guardar.setVisible(false);
                         }


                    text_count.setVisibility(View.VISIBLE);
                    boton_borrar_indicador.setVisibility(View.VISIBLE);
                }else {
                        guardar.setVisible(false);
                        text_count.setVisibility(View.GONE);
                        boton_borrar_indicador.setVisibility(View.GONE);
                      }




             } //// fin si no se hay indicadores agregados


    }



///***********************************************************************************************

    public void setCount()
    {

    }


    ///::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    private class GuardarEvaluacionAsync extends AsyncTask<String, Integer, String[]>
    {
        protected void onPreExecute()
        {
            progress_guardar_evaluacion = new ProgressDialog(EvaluadorRecurso.this);
            progress_guardar_evaluacion.setTitle("Guardar");
            progress_guardar_evaluacion.setMessage("Guardando evaluación ...");
            progress_guardar_evaluacion.setCancelable(false);
            progress_guardar_evaluacion.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress_guardar_evaluacion.setIndeterminate(true);

            progress_guardar_evaluacion.show();
        }

        protected String[] doInBackground(String... datos)
        {
            String status[]={"0",""};
            int errors=0;
            String messages="";


            String hallazgo_valor=datos[0];
            String accion_seleccionada=datos[1];


            List<DataEvaluadorRecurso> respuestas = new ArrayList<>();
            respuestas = adaptadorER.getRespuestas();

            int array[]=adaptadorER.contarPreguntas();
            int preguntas=array[0];
            int respondidas=array[1];
            int aprobadas=array[2];
            int negativas=array[3];
            int nas=array[4];



            if( negativas > 0 )
            {

                if(hallazgo_valor.equals(""))
                {
                    errors++;
                    messages+=" * Escriba el hallazgo. \n";
                }

                if (accion_seleccionada.equals("RESOLUTIVA") || accion_seleccionada.equals("SEGUIMIENTO") )
                {
                    errors++;
                    messages+=" * Seleccione una Accion valida. ";
                }

            }



            if(errors==0)
            {
                if(negativas>0)
                {

                    Hallazgo hallazgo =linkDB.getHallazgo("RECURSO",idEvaluacionrecurso,id_indicador_selected,null);

                    if(hallazgo==null)
                    {
                        hallazgo=new Hallazgo(
                                                    0,
                                                    evaluacion_recurso.id,
                                                    "RECURSO",
                                                    id_indicador_selected,
                                                    null,
                                                    user_signed.id,
                                                    id_accion_tomada,
                                                    0,
                                                    0,
                                                    hallazgo_valor,
                                                    link.getFecha(),
                                                    "0000-00-00 00:00:00",
                                                    "0000-00-00 00:00:00"
                                            );

                        linkDB.insertarHallazgo(hallazgo,linkDB.openDB());

                    }else{
                            hallazgo.setIdAccion(id_accion_tomada);
                            hallazgo.setDescripcion(hallazgo_valor);
                            hallazgo.setModificadoAl(link.getFecha());

                            linkDB.actualizarHallazgo(hallazgo,linkDB.openDB());
                         }


                }else{
                        Hallazgo hallazgo =linkDB.getHallazgo("RECURSO",idEvaluacionrecurso,id_indicador_selected,null);

                        if(hallazgo!=null)
                        {
                            hallazgo.setBorradoAl(link.getFecha());
                            linkDB.actualizarHallazgo(hallazgo,linkDB.openDB());
                        }
                    }

                for (int i = 0; i < respuestas.size(); i++)
                {
                    //si fué modificada la respuesta validar update ó insert
                    if (respuestas.get(i).modificado == 1)
                    {
                        // si id no existe, se insertará registro
                        if (respuestas.get(i).id_erc == 0)
                        {
                            EvaluacionRecursoCriterio respuesta = new EvaluacionRecursoCriterio(
                                                                                                    0,
                                                                                                    respuestas.get(i).id_evaluacion_recurso,
                                                                                                    respuestas.get(i).id_criterio,
                                                                                                    respuestas.get(i).id_indicador,
                                                                                                    respuestas.get(i).respuesta,
                                                                                                    link.getFecha(),
                                                                                                    "0000-00-00 00:00:00",
                                                                                                    "0000-00-00 00:00:00"
                                                                                                );
                            int insert = linkDB.insertarEvaluacionRecursoCriterio(respuesta, linkDB.openDB());

                        } else { //update
                                    EvaluacionRecursoCriterio respuesta = new EvaluacionRecursoCriterio(
                                                                                                            respuestas.get(i).id_erc,
                                                                                                            respuestas.get(i).id_evaluacion_recurso,
                                                                                                            respuestas.get(i).id_criterio,
                                                                                                            respuestas.get(i).id_indicador,
                                                                                                            respuestas.get(i).respuesta,
                                                                                                            link.getFecha(),
                                                                                                            "0000-00-00 00:00:00",
                                                                                                            "0000-00-00 00:00:00"
                                                                                                        );

                                    linkDB.actualizarEvaluacionRecursoCriterio(respuesta, linkDB.openDB());

                                }//FIN ELSE INSERTAR

                    }//FIN IF :

                }

                ContentValues ava = getTotalCriterioIndicador(idEvaluacionrecurso,id_indicador_selected);
                EvaluacionRecursoIndicador eri_temp = linkDB.getEvaluacionRecursoIndicador(idEvaluacionrecurso,id_indicador_selected);

                eri_temp.setTotalCriterio(ava.getAsInteger("totalCriterio"));
                eri_temp.setAvanceCriterio(ava.getAsInteger("avanceCriterio"));



                linkDB.actualizarEvaluacionRecursoIndicador(eri_temp,linkDB.openDB());

                cambios=false;
                adaptadorER.cambios=false;

                //revisionBotones();

                messages="Evaluación guardada correctamente.";

            }// fin errors = 0


            status[0]=""+errors;
            status[1]=""+messages;

            return status;
        }

        protected void onProgressUpdate(Integer... values)
        {
        }

        protected void onPostExecute(String status[])
        {
            progress_guardar_evaluacion.dismiss();

            link.printConsola("VALOR CAMBIOS (on post execute guardar eval async v1)--> :  ["+cambios+"]");
            cambios=false;
            link.printConsola("VALOR CAMBIOS (metodo preguntar cambio indicador v2)--> :  ["+cambios+"]");



            int errors=Integer.parseInt(status[0]);
            String messages=status[1];

            if(errors==0)
            {
                link.showSnackBar(getApplicationContext(),coordinatorLayout,""+messages,"INFO");
            }else{
                    link.showSnackBar(getApplicationContext(),coordinatorLayout,""+messages,"ERROR");
                 }

            new AvanceValidacionAsync().execute();
        }

        protected void onCancelled()
        {
            link.showSnackBar(getApplicationContext(),coordinatorLayout,"Cancelado","ERROR");
        }

    }///FIN CLASS REFRESH INDICADORES IN ASYNC

///::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


    ///***********************************************************************************************

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_evaluador_recurso, menu);
        this.menu = menu;


        menuGuardar = menu.findItem(R.id.guardar_evaluacion);
        menuCerrar = menu.findItem(R.id.cerrar_evaluacion);
        menuFirmar = menu.findItem(R.id.firmar_evaluacion);
        menuFicha = menu.findItem(R.id.ficha_tecnica);

        menuTransferencia = menu.findItem(R.id.transferencia);


        new AvanceValidacionAsync().execute();

        //revisionBotones();

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

        if (id == R.id.transferencia)
        {
            showDialogAccionBluetooth();
        }

        /*

        if (id == R.id.enviar_bluetooth)
        {
            if(cambios)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Existen cambios sin guardar.\n¿ Quieres continuar al envio por bluetooth ?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Intent lanzador = new Intent(EvaluadorRecurso.this, Bluetooth.class);
                                lanzador.putExtra( "idEvaluacion", evaluacion_recurso.id);
                                lanzador.putExtra( "MODO", "EMISOR");
                                lanzador.putExtra( "TIPO_EVALUACION", "RECURSO");
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

                Intent lanzador = new Intent(EvaluadorRecurso.this, Bluetooth.class);
                lanzador.putExtra( "idEvaluacion", evaluacion_recurso.id);
                lanzador.putExtra( "MODO", "EMISOR");
                lanzador.putExtra( "TIPO_EVALUACION", "RECURSO");
                System.out.println("");
                startActivity(lanzador);
            }


        }

        if (id == R.id.recibir_bluetooth)
        {

            if(cambios)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Existen cambios sin guardar.\n¿ Quieres continuar a la recepción bluetooth ?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Intent lanzador = new Intent(EvaluadorRecurso.this, Bluetooth.class);
                                lanzador.putExtra( "idEvaluacion", evaluacion_recurso.id);
                                lanzador.putExtra( "MODO", "RECEPTOR");
                                lanzador.putExtra( "TIPO_EVALUACION", "RECURSO");
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
                    Intent lanzador = new Intent(EvaluadorRecurso.this, Bluetooth.class);
                    lanzador.putExtra( "idEvaluacion", evaluacion_recurso.id);
                    lanzador.putExtra( "MODO", "RECEPTOR");
                    lanzador.putExtra( "TIPO_EVALUACION", "RECURSO");
                    System.out.println("");
                    startActivity(lanzador);
                  }

        }

        */


        if (id == R.id.ficha_tecnica)
        {
            //dialog.show();

            crearDialogFichaTecnica();
            //dialog.show();
        }

        if (id == R.id.firmar_evaluacion)
        {
            Intent lanzador = new Intent(EvaluadorRecurso.this, FirmaEvaluacion.class);
            lanzador.putExtra( "idEvaluacion", evaluacion_recurso.id);
            lanzador.putExtra( "tipoEvaluacion", "RECURSO");
            System.out.println("");
            startActivity(lanzador);

            finish();

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
            new GuardarEvaluacionAsync().execute(input_hallazgo.getText().toString().trim(), accion_tomada);

        }////FIN OPCION GUARDAR EVALUACION


        return super.onOptionsItemSelected(item);
    }


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

         if(evaluacion_recurso.cerrado==1)
         {
             text_responsable_info.setText(evaluacion_recurso.responsable);

             if(evaluacion_recurso.firma.equals(""))
             {}else{
                 image_firma.setImageBitmap(link.decodeBase64(evaluacion_recurso.firma));
             }

         }else{

              }

     }

    public void cerrarEvaluacion()
    {
        Evaluacion eval=linkDB.getEvaluacion("RECURSO", evaluacion_recurso.id, linkDB.openDB());

        linkDB.cerrarEvaluacion("RECURSO", eval, linkDB.openDB());
        evaluacion_recurso = linkDB.getEvaluacion("RECURSO",evaluacion_recurso.id,linkDB.openDB());
        cerrado=evaluacion_recurso.getCerrado();
        revisionBotones();
        cargarCriteriosRecycler();

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

                if (position == 0) {
                    goTransferirBluetooth();
                }


                if (position == 1) {
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
    public void goTransferirBluetooth()
        {
            if(cambios)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Existen cambios sin guardar.\n¿ Quieres continuar al envio por bluetooth ?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Intent lanzador = new Intent(EvaluadorRecurso.this, Bluetooth.class);
                                lanzador.putExtra("idEvaluacion", evaluacion_recurso.id);
                                lanzador.putExtra("MODO", "EMISOR");



                                /// todo se envia full cero para que borre lo enviado, no debe mandar las q ya estan cerradas.
                                lanzador.putExtra("FULL", 0);



                                lanzador.putExtra("TIPO_EVALUACION", "RECURSO");
                                System.out.println("");
                                startActivity(lanzador);

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

                Intent lanzador = new Intent(EvaluadorRecurso.this, Bluetooth.class);
                lanzador.putExtra("idEvaluacion", evaluacion_recurso.id);
                lanzador.putExtra( "MODO", "EMISOR");

                /// todo se envia full cero para que borre lo enviado, no debe mandar las q ya estan cerradas.
                lanzador.putExtra("FULL", 0);

                lanzador.putExtra( "TIPO_EVALUACION", "RECURSO");
                System.out.println("");
                startActivity(lanzador);

                finish();
            }

        }
///**************************************************************************************************
    public void goRecibirBluetooth()
    {
        if(cambios)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Existen cambios sin guardar.\n¿ Quieres continuar a la recepción bluetooth ?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Intent lanzador = new Intent(EvaluadorRecurso.this, Bluetooth.class);
                            lanzador.putExtra("idEvaluacion", evaluacion_recurso.id);
                            lanzador.putExtra("MODO", "RECEPTOR");
                            lanzador.putExtra("FULL", 0);
                            lanzador.putExtra("TIPO_EVALUACION", "RECURSO");
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
            Intent lanzador = new Intent(EvaluadorRecurso.this, Bluetooth.class);
            lanzador.putExtra( "idEvaluacion", evaluacion_recurso.id);
            lanzador.putExtra( "MODO", "RECEPTOR");
            lanzador.putExtra("FULL", 0);
            lanzador.putExtra( "TIPO_EVALUACION", "RECURSO");
            System.out.println("");
            startActivity(lanzador);
        }

    }

///**************************************************************************************************

    public void showDialogAgregarIndicadores()
    {

        final CharSequence[] LISTA = INDICADORES.toArray(new String[ INDICADORES.size() ]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Seleccione un indicador");
        builder.setItems(LISTA, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int position)
            {

                Indicador indicador_agregar = lista_indicadores.get(position);

                boolean existe = false;

                for (int i = 0; i < lista_indicadores_agregados.size(); i++)
                {

                    if (indicador_agregar.id == lista_indicadores_agregados.get(i).id)
                    {
                        existe = true;
                        System.out.println("YA EXISTE ESTE INDICADOR : " + indicador_agregar.codigo);
                    }

                }

                if (existe == false)
                {
                    System.out.println(" N O   EXISTE ESTE INDICADOR : " + indicador_agregar.codigo + ", SE AGREGARA...");

                    lista_indicadores_agregados.add(indicador_agregar);
                    INDICADORES_AGREGADOS.add(indicador_agregar.codigo + " - " + indicador_agregar.nombre);

                    ContentValues cv = getTotalCriterioIndicador(idEvaluacionrecurso,indicador_agregar.id);

                    int tc = cv.getAsInteger("totalCriterio");
                    int ac = cv.getAsInteger("avanceCriterio");


                    EvaluacionRecursoIndicador eri = new EvaluacionRecursoIndicador(0,idEvaluacionrecurso,indicador_agregar.id,tc,ac,link.getFecha(),
                                                                                    "0000-00-00 00:00:00","0000-00-00 00:00:00");
                    linkDB.insertarEvaluacionRecursoIndicador(eri,linkDB.openDB());

                     adaptador_indicadores_agregados.notifyDataSetChanged();
                }

                spinner_indicadores_agregados.setSelection(lista_indicadores_agregados.size() - 1, true);


            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


/////************************************************************************************************
/////************************************************************************************************

    public void showDialogAgregarIndicadores2()
    {
            final List<String> INDICADORES=new ArrayList<>();

            for(int i=0;i<lista_indicadores.size();i++)
            {
                Indicador concepto = lista_indicadores.get(i);
                if(concepto!=null)
                {
                     INDICADORES.add(" "+concepto.getCodigo()+" - "+concepto.getNombre());
                }
            }
        ///*****************************************************************************************

        final Dialog dialog = new Dialog(EvaluadorRecurso.this);

        View view = getLayoutInflater().inflate(R.layout.layout_listview_dialog, null);

        ListView lv = (ListView) view.findViewById(R.id.list_view_dialog);
        AdaptadorListaIndicadores clad = new AdaptadorListaIndicadores(EvaluadorRecurso.this, lista_indicadores);
        lv.setAdapter(clad);

        //******************************************

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                Indicador indicador_agregar = lista_indicadores.get(position);

                boolean existe = false;

                for (int i = 0; i < lista_indicadores_agregados.size(); i++)
                {

                    if (indicador_agregar.id == lista_indicadores_agregados.get(i).id)
                    {
                        existe = true;
                        System.out.println("YA EXISTE ESTE INDICADOR : " + indicador_agregar.codigo);
                    }

                }

                if (existe == false)
                {
                    System.out.println(" N O   EXISTE ESTE INDICADOR : " + indicador_agregar.codigo + ", SE AGREGARA...");

                    lista_indicadores_agregados.add(indicador_agregar);
                    INDICADORES_AGREGADOS.add(indicador_agregar.codigo + " - " + indicador_agregar.nombre);

                    ContentValues cv = getTotalCriterioIndicador(idEvaluacionrecurso,indicador_agregar.id);

                    int tc = cv.getAsInteger("totalCriterio");
                    int ac = cv.getAsInteger("avanceCriterio");


                    EvaluacionRecursoIndicador eri = new EvaluacionRecursoIndicador(0,idEvaluacionrecurso,indicador_agregar.id,tc,ac,link.getFecha(),
                            "0000-00-00 00:00:00","0000-00-00 00:00:00");
                    linkDB.insertarEvaluacionRecursoIndicador(eri,linkDB.openDB());

                    adaptador_indicadores_agregados.notifyDataSetChanged();
                }


                setIndicadorSeleccionado(INDICADORES.get(position));
                dialog.dismiss();

            }});



        ///******************************************
        dialog.setTitle("Seleccione un Indicador");
        dialog.setContentView(view);
        //dialog.setView(view);

        dialog.show();


        ///*****************************************************************************************

    }

////***********************************************************************************************
/////**********************************************************************************************

    public void setIndicadorSeleccionado(String indicador)
    {
        String texto="";
        try {
            int size=indicador.length();

            if(size>=80)
            {
                texto = indicador.substring(0,80)+"...";
            }else {
                texto = indicador;
            }
        }catch (NullPointerException err){
            texto =" ERROR ";
        }

        boton_indicador_seleccionado.setText(texto);


    }

/////**********************************************************************************************
/////**********************************************************************************************
/*
    public void setIndicador()
    {
        if(cambios)
        {
            der_temp = adaptadorER.getRespuestas();
            preguntarCambioIndicador( position_indicador_selected, id_indicador_selected, position, lista_indicadores_agregados.get(position).id , der_temp);
        }else{
                if(caso_error_guardar)
                {
                    adaptadorER.updateDataRecycler(der_temp,cerrado);
                    caso_error_guardar=false;
                    cambios=true;

                    System.out.println("----- NO CAMBIOS, CASO ERROR DE GUARDADO ... ");

                }else{
                            System.out.println("------ NO  CAMBIOS, NO ERROR  ");

                            position_indicador_selected = position;
                            id_indicador_selected = lista_indicadores_agregados.get(position).id;

                            cargarCriteriosRecycler();

                            ///posible causa perdida
                            if(id_indicador_selected > 0)
                            {
                                der_temp = adaptadorER.getRespuestas();
                            }
                      }
        }
    }

*/
///************************************************************************************************
///************************************************************************************************
public void borrarIndicadorSeleccionado()
{

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("¿ Confirma eliminar el indicador y sus criterios de la evaluación ?")
            .setPositiveButton("Si",  new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {


                    List<EvaluacionRecursoCriterio> lista = linkDB.getEvaluacionRecursoCriterio(idEvaluacionrecurso);

                    for (int j = 0; j < lista.size(); j++)
                    {
                        EvaluacionRecursoCriterio erc_temp = lista.get(j);

                        if(erc_temp.id_indicador==id_indicador_selected)
                        {
                            erc_temp.setBorradoAl(link.getFecha());
                            linkDB.actualizarEvaluacionRecursoCriterio(erc_temp, linkDB.openDB());
                        }


                    }

                    EvaluacionRecursoIndicador eri=linkDB.getEvaluacionRecursoIndicador(idEvaluacionrecurso,id_indicador_selected);
                    eri.setBorradoAl(link.getFecha());

                    linkDB.actualizarEvaluacionRecursoIndicador(eri,linkDB.openDB());

                    refreshSpinnerIndicadoresAgregados();



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
///***********************************************************************************************


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


    rellenarFicha();

    dialog.show();


}

///*************************************************************************************************

    public void preguntarCambioIndicador(final int actual_position_indicador_selected, final int actual_id_indicador_selected,
                                         final int new_position_indicador_selected, final int new_id_indicador_selected, List<DataEvaluadorRecurso> lista)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        link.printConsola("VALOR CAMBIOS (metodo preguntar cambio indicador)--> :  ["+cambios+"]");


        builder.setCancelable(false);
        builder.setMessage("Existen cambios sin guardar. \n ¿ Deseas guardar antes de cambiar de indicador ?")
                .setPositiveButton("Guardar",  new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                        if( guardarEvaluacion() )
                        {

                            position_indicador_selected = new_position_indicador_selected;
                            id_indicador_selected = new_id_indicador_selected;

                            cargarCriteriosRecycler();

                            cambios = false;
                        }else {
                                cambios=false;
                                caso_error_guardar=true;
                                spinner_indicadores_agregados.setSelection(actual_position_indicador_selected);

                                //position_indicador_selected = actual_position_indicador_selected;
                                //id_indicador_selected = actual_id_indicador_selected;

                              }

                    }
                })
                .setNegativeButton("No Guardar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,int id)
                    {

                        position_indicador_selected = new_position_indicador_selected;
                        id_indicador_selected = new_id_indicador_selected;

                        cargarCriteriosRecycler();

                        cambios=false;

                        dialog.cancel();

                    }
                }).show();

    }

///*************************************************************************************************

    public void cargarCriteriosRecycler()
    {

        new CargarCriteriosRecyclerAsync().execute();

/*
        List<LugarVerificacion> listaLV = new ArrayList<>();

        ///Lista de objetos que llevan las preguntas/criterios para el indicador
        List<DataEvaluadorRecurso> DER = new ArrayList<>();
        List<EvaluacionRecursoCriterio> LERC = new ArrayList<>();
        LERC = linkDB.getEvaluacionRecursoCriterio(idEvaluacionrecurso, id_indicador_selected);

        System.out.println("DESPUES DE LLENAR LERC , DIM LERC : " + LERC.size());

        try {  //Se coloca el adaptador, se valida la excepcion por si fuera vacio el adaptador
                adaptadorER = new AdaptadorEvaluadorRecurso( DER, getApplicationContext());
                recycler_evaluador.setAdapter(adaptadorER);

                RecyclerView.ItemDecoration itemDecoration2 = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);

                recycler_evaluador.addItemDecoration(itemDecoration2);
                recycler_evaluador.setItemAnimator(new DefaultItemAnimator());

            } catch (NullPointerException err) {
                                                    err.printStackTrace();
                                                }

        listaLV = linkDB.getLugaresVerificacion(nivel_cone,id_indicador_selected);
        System.out.println("DESPUES DE GET LUGARES VERIF.: LUGAR VERIF: " +nivel_cone+", ID INDICADOR: "+id_indicador_selected+".");

        String tipo_item = "LUGAR";
        int posicion_item = 0;

        for (int i = 0; i < listaLV.size(); i++)
        {

            DER.add(new DataEvaluadorRecurso(
                                                idEvaluacionrecurso,
                                                "LUGAR",
                                                id_indicador_selected,
                                                listaLV.get(i).id,
                                                listaLV.get(i).nombre,
                                                0,
                                                "--",
                                                400,
                                                -1,
                                                0,
                                                0
                                        ));

            System.out.println("TIPO: LUGAR, ID:" + listaLV.get(i).id + ", NOMBRELV: " + listaLV.get(i).nombre);

            List<Criterio> listaC = new ArrayList<>();
            listaC = linkDB.getCriterios(nivel_cone, id_indicador_selected, listaLV.get(i).id);

            for (int j = 0; j < listaC.size(); j++)
            {

                EvaluacionRecursoCriterio eval = linkDB.getEvaluacionRecursoCriterio(idEvaluacionrecurso,
                        id_indicador_selected, listaC.get(j).id);

                int respuesta = -1;
                int id_erc = 0;

                if (eval == null) {

                } else {
                    respuesta = eval.getAprobado();
                    id_erc = eval.getId();
                }

                DER.add(new DataEvaluadorRecurso(
                        idEvaluacionrecurso,
                        "CRITERIO",
                        id_indicador_selected,
                        listaLV.get(i).id,
                        listaLV.get(i).nombre,
                        listaC.get(j).id,
                        listaC.get(j).nombre,
                        listaC.get(j).habilitarNoAplica,
                        respuesta,
                        id_erc,
                        0
                ));
                System.out.println("TIPO: CRITERIO, ID:" + listaC.get(j).id + ", NOMBRECRITERIO: " + listaC.get(j).nombre);
            }
            // 1.- EN POSICION CERO SE AGREGA LUGAR DE VERIF. A LA LISTA DE DATOS DEL ADAPTADOR.
            // 2.- SE LEEN TODOS LOS CRITERIOS DE ESE LUGAR DE VERIFICACION Y EL INDICADOR
            //     SELECCIONADO (id_indicador)
            // 3.- RECORRER LISTA DE CRITERIOS LANZANDO CADA UNO A LA LISTA DE DATOS DEL ADAPTADOR

        } //FIN FOR LISTA DE LUGARES DE VERIFICACION

        System.out.println("LERC DIM : " + LERC.size());


        Evaluacion eva= linkDB.getEvaluacion("RECURSO",evaluacion_recurso.id,linkDB.openDB());

        if(eva.cerrado==1)
        {
            try {  //Se coloca el adaptador, se valida la excepcion por si fuera vacio el adaptador

                adaptadorERCerrado = new AdaptadorEvaluadorRecursoCerrado( DER, getApplicationContext());
                recycler_evaluador.setAdapter(adaptadorERCerrado);

                adaptadorERCerrado.updateDataRecycler(DER);


                } catch (NullPointerException err) {
                                                     err.printStackTrace();
                                                    }

        }else{
                 adaptadorER.updateDataRecycler(DER);
             }


        setCount();
        checkHallasgos();



*/


    }

 ///::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    private class AvanceValidacionAsync extends AsyncTask<String, Integer, ContentValues>
    {
        protected void onPreExecute()
        {

            progress_avance_validacion = new ProgressDialog(EvaluadorRecurso.this);
            progress_avance_validacion.setTitle("Por favor espere");
            progress_avance_validacion.setMessage("Validando evaluación ...");
            progress_avance_validacion.setCancelable(false);
            progress_avance_validacion.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress_avance_validacion.setIndeterminate(true);

            progress_avance_validacion.show();

        }

        protected ContentValues doInBackground(String... datos)
        {

            ContentValues response = new ContentValues();
            response.put("errors",0);
            response.put("messages","");


            response.put("totalCriterioIndicador",0);
            response.put("avanceCriterioIndicador",0);

            response.put("requiereHallazgo",false);

            response.put("guardar",false);
            response.put("cerrar",false);
            response.put("firmar",false);
            response.put("transferencia",false);
            response.put("ficha",false);


            int totalCriterioEvaluacion=0;
            int avanceCriterioEvaluacion=0;

            int totalCriterioSelected=0;
            int totalAvanceSelected=0;

            //sdsd LEER INDICADORES DE METODO NUEVO, POR ESO NO LO REOCNOCE EL VALIDER AL CARGAR NUEVO INDICADOR

            ArrayList<Indicador> indicadoresAgregados = (ArrayList<Indicador>) linkDB.getIndicadoresAgregadosRecurso(idEvaluacionrecurso);

            for(int i=0; i<indicadoresAgregados.size(); i++)
            {
                ContentValues cv = getTotalCriterioIndicador(idEvaluacionrecurso,indicadoresAgregados.get(i).getId());

                int tc=cv.getAsInteger("totalCriterio");
                int ac=cv.getAsInteger("avanceCriterio");

                totalCriterioEvaluacion += tc;
                avanceCriterioEvaluacion += ac;

            }

            link.printConsola("-------------> TOTAL CRITERIO EVAL : ["+totalCriterioEvaluacion+"]");
            link.printConsola("-------------> TOTAL AVANCE EVAL : ["+avanceCriterioEvaluacion+"]");

            Evaluacion eval=linkDB.getEvaluacion("RECURSO", idEvaluacionrecurso, linkDB.openDB());

            if(eval.cerrado==0)
            {
                response.put("guardar",true);
                response.put("transferencia",true);
            }else{
                    response.put("guardar",false);
                    response.put("firmar",true);
                    response.put("transferencia",false);

                    if(eval.getFirma().toString().equals(""))
                    {
                        response.put("firmar",true);
                    }else {
                            response.put("firmar",false);
                          }
                 }

            if( eval.cerrado==1 )
            {
                response.put("cerrar",false);
            }else {
                    if( (avanceCriterioEvaluacion==totalCriterioEvaluacion) && (totalCriterioEvaluacion>0) )
                    {
                        response.put("cerrar",true);
                    }
                  }

            if (id_indicador_selected>0)
            {
                response.put("guardar",true);
            }else {
                    response.put("guardar",false);
                  }




///****************************************************************************************************************************************************************
            if(id_indicador_selected > 0)
            {

                int array[];

                Evaluacion evax= linkDB.getEvaluacion("RECURSO", evaluacion_recurso.id, linkDB.openDB());


                    array = adaptadorER.contarPreguntas();




                int preguntas = array[0];
                int respondidas = array[1];
                int aprobadas = array[2];
                int negativas = array[3];

                int changes= array[5];

                if(changes==1)
                {
                    cambios=true;
                    System.out.println("-----  CAMBIOS : TRUE  [ OK ]");
                }else{
                        cambios=false;
                        System.out.println("-----  CAMBIOS : FALSE [ X ]");
                     }


                ////text_count.setText(respondidas + " / " + preguntas);
                ////text_count.setTextColor(getResources().getColor(R.color.monsoon));

                response.put("totalCriterioIndicador",preguntas);
                response.put("avanceCriterioIndicador",respondidas);


                if (negativas > 0)
                {
                    ////spiner_acciones.setVisibility(View.VISIBLE);
                    ////input_hallazgo.setVisibility(View.VISIBLE);

                    ////layout_hallazgo.setVisibility(View.VISIBLE);

                    response.put("requiereHallazgo",true);

                } else {

                            response.put("requiereHallazgo",false);
                    ////spiner_acciones.setVisibility(View.GONE);
                    ////input_hallazgo.setVisibility(View.GONE);
                    ////layout_hallazgo.setVisibility(View.GONE);
                        }

            }else{

                ////text_count.setText("0 / 0");
                ////text_count.setTextColor(getResources().getColor(R.color.monsoon));

                    response.put("totalCriterioIndicador",0);
                    response.put("avanceCriterioIndicador",0);

                ////spiner_acciones.setVisibility(View.GONE);
                ////input_hallazgo.setVisibility(View.GONE);
                ////layout_hallazgo.setVisibility(View.GONE);

                    response.put("requiereHallazgo",false);

                }


///****************************************************************************************************************************************************************
/*
            Hallazgo hallazgo_set= linkDB.getHallazgo("RECURSO", idEvaluacionrecurso, id_indicador_selected,null);

            if(hallazgo_set!=null)
            {
                ////layout_hallazgo.setVisibility(View.VISIBLE);
                ////input_hallazgo.setText(hallazgo_set.descripcion);

                response.put("requiereHallazgo",true);
                response.put("valorHallazgo",hallazgo_set.descripcion);

                int id_accion_selected = hallazgo_set.getIdAccion();
                String nombre_accion = "";

                for( int i=0; i < listaAcciones.size(); i++ )
                {
                    if( listaAcciones.get(i).getId()==id_accion_selected )
                    {
                        nombre_accion = listaAcciones.get(i).getNombre();
                    }
                }

                for(int j=0; j<acciones.size(); j++)
                {
                    if(nombre_accion.equals(acciones.get(j)))
                    {
                        ////spiner_acciones.setSelection(j);
                        response.put("seleccionarPosicionAccion",j);
                    }
                }



            }else{
                ////input_hallazgo.setText("");
                ////spiner_acciones.setSelection(0);

                ////layout_hallazgo.setVisibility(View.GONE);

                    response.put("valorHallazgo","");
                    response.put("seleccionarPosicionAccion",0);
                    response.put("requiereHallazgo",false);
                 }

*/
/////**************************************************************************************************************************************************************

            return response;
        }

        protected void onProgressUpdate(Integer... values)
        {

        }

        protected void onPostExecute(ContentValues response)
        {
            progress_avance_validacion.dismiss();


            menuGuardar.setVisible(response.getAsBoolean("guardar"));
            menuCerrar.setVisible(response.getAsBoolean("cerrar"));
            menuFirmar.setVisible(response.getAsBoolean("firmar"));
            menuTransferencia.setVisible(response.getAsBoolean("transferencia"));

            if(id_indicador_selected>0)
            {
                text_count.setText(response.getAsInteger("avanceCriterioIndicador") + " / " +response.getAsString("totalCriterioIndicador"));
                text_count.setTextColor(getResources().getColor(R.color.monsoon));
            }else {
                    text_count.setText("0 / 0");
                    text_count.setTextColor(getResources().getColor(R.color.monsoon));
                  }

                  link.printConsola("RESPONSE REQUIERE HALLAZGO ------> ["+response.getAsBoolean("requiereHallazgo")+"]");



            if (response.getAsBoolean("requiereHallazgo"))
            {
                layout_hallazgo.setVisibility(View.VISIBLE);
                spiner_acciones.setVisibility(View.VISIBLE);
                input_hallazgo.setVisibility(View.VISIBLE);

                //spiner_acciones.setSelection(response.getAsInteger("seleccionarPosicionAccion"));
                //input_hallazgo.setText(response.getAsString("valorHallazgo"));

            } else {

                        spiner_acciones.setSelection(0);
                        input_hallazgo.setText("");

                        layout_hallazgo.setVisibility(View.GONE);
                        spiner_acciones.setVisibility(View.GONE);
                        input_hallazgo.setVisibility(View.GONE);

                    }




        }

        protected void onCancelled()
        {
            link.showSnackBar(getApplicationContext(),coordinatorLayout,"Cancelado","ERROR");
        }

    }///FIN CLASS REFRESH INDICADORES IN ASYNC

///::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::




///::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    private class CargarCriteriosRecyclerAsync extends AsyncTask<String, Integer, ContentValues>
    {

        ContentValues response = new ContentValues();

        protected void onPreExecute()
        {

            progress_cargar_criterios = new ProgressDialog(EvaluadorRecurso.this);
            progress_cargar_criterios.setTitle("Por favor espere");
            progress_cargar_criterios.setMessage("Cargando criterios ...");
            progress_cargar_criterios.setCancelable(false);
            progress_cargar_criterios.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress_cargar_criterios.setIndeterminate(true);

            progress_cargar_criterios.show();


        }

        protected ContentValues doInBackground(String... datos)
        {
            int errors=0;


            DER.clear();


            listaLV = linkDB.getLugaresVerificacion(nivel_cone,id_indicador_selected);
            System.out.println("DESPUES DE GET LUGARES VERIF.: LUGAR VERIF: " +nivel_cone+", ID INDICADOR: "+id_indicador_selected+".");

            String tipo_item = "LUGAR";
            int posicion_item = 0;

            for (int i = 0; i < listaLV.size(); i++)
            {

                DER.add(new DataEvaluadorRecurso(
                        idEvaluacionrecurso,
                        "LUGAR",
                        id_indicador_selected,
                        listaLV.get(i).id,
                        listaLV.get(i).nombre,
                        0,
                        "--",
                        400,
                        -1,
                        0,
                        0
                ));

                System.out.println("TIPO: LUGAR, ID:" + listaLV.get(i).id + ", NOMBRELV: " + listaLV.get(i).nombre);

                List<Criterio> listaC = new ArrayList<>();
                listaC = linkDB.getCriterios(nivel_cone, id_indicador_selected, listaLV.get(i).id);

                for (int j = 0; j < listaC.size(); j++)
                {

                    EvaluacionRecursoCriterio eval = linkDB.getEvaluacionRecursoCriterio(idEvaluacionrecurso,
                            id_indicador_selected, listaC.get(j).id);

                    int respuesta = -1;
                    int id_erc = 0;

                       if (eval == null){

                                        } else {
                                                    respuesta = eval.getAprobado();
                                                    id_erc = eval.getId();
                                                }

                    DER.add(new DataEvaluadorRecurso(
                                                            idEvaluacionrecurso,
                                                            "CRITERIO",
                                                            id_indicador_selected,
                                                            listaLV.get(i).id,
                                                            listaLV.get(i).nombre,
                                                            listaC.get(j).id,
                                                            listaC.get(j).nombre,
                                                            listaC.get(j).habilitarNoAplica,
                                                            respuesta,
                                                            id_erc,
                                                            0
                                                    ));
                    System.out.println("TIPO: CRITERIO, ID:" + listaC.get(j).id + ", NOMBRECRITERIO: " + listaC.get(j).nombre);
                }
                // 1.- EN POSICION CERO SE AGREGA LUGAR DE VERIF. A LA LISTA DE DATOS DEL ADAPTADOR.
                // 2.- SE LEEN TODOS LOS CRITERIOS DE ESE LUGAR DE VERIFICACION Y EL INDICADOR
                //     SELECCIONADO (id_indicador)
                // 3.- RECORRER LISTA DE CRITERIOS LANZANDO CADA UNO A LA LISTA DE DATOS DEL ADAPTADOR

            } //FIN FOR LISTA DE LUGARES DE VERIFICACION

            System.out.println("LERC DIM : " + LERC.size());

///***************************************************************************************************************************************************************************

            Hallazgo hallazgo_set= linkDB.getHallazgo("RECURSO", idEvaluacionrecurso, id_indicador_selected,null);

            if(hallazgo_set!=null)
            {
                ////layout_hallazgo.setVisibility(View.VISIBLE);
                ////input_hallazgo.setText(hallazgo_set.descripcion);

                response.put("requiereHallazgo",true);
                response.put("valorHallazgo",hallazgo_set.descripcion);

                int id_accion_selected = hallazgo_set.getIdAccion();
                String nombre_accion = "";

                for( int i=0; i < listaAcciones.size(); i++ )
                {
                    if( listaAcciones.get(i).getId()==id_accion_selected )
                    {
                        nombre_accion = listaAcciones.get(i).getNombre();
                    }
                }

                for(int j=0; j<acciones.size(); j++)
                {
                    if(nombre_accion.equals(acciones.get(j)))
                    {
                        ////spiner_acciones.setSelection(j);
                        response.put("seleccionarPosicionAccion",j);
                    }
                }

            }else{
                ////input_hallazgo.setText("");
                ////spiner_acciones.setSelection(0);

                ////layout_hallazgo.setVisibility(View.GONE);

                response.put("valorHallazgo","");
                response.put("seleccionarPosicionAccion",0);
                response.put("requiereHallazgo",false);
            }


////***************************************************************************************************************************************************************************
            return response;
        }

        protected void onProgressUpdate(Integer... values)
        {
        }

        protected void onPostExecute(ContentValues response)
        {

            progress_cargar_criterios.dismiss();



            if (response.getAsBoolean("requiereHallazgo"))
            {
                layout_hallazgo.setVisibility(View.VISIBLE);
                spiner_acciones.setVisibility(View.VISIBLE);
                input_hallazgo.setVisibility(View.VISIBLE);

                spiner_acciones.setSelection(response.getAsInteger("seleccionarPosicionAccion"));
                input_hallazgo.setText(response.getAsString("valorHallazgo"));

            } else {

                        spiner_acciones.setSelection(0);
                        input_hallazgo.setText("");

                        layout_hallazgo.setVisibility(View.GONE);
                        spiner_acciones.setVisibility(View.GONE);
                        input_hallazgo.setVisibility(View.GONE);

                    }






            Evaluacion eva= linkDB.getEvaluacion("RECURSO",evaluacion_recurso.id,linkDB.openDB());


            adaptadorER.updateDataRecycler(DER,cerrado);
            /*
            if(eva.cerrado==1)
            {
                try {  //Se coloca el adaptador, se valida la excepcion por si fuera vacio el adaptador

                        adaptadorERCerrado = new AdaptadorEvaluadorRecursoCerrado( DER, getApplicationContext());
                        recycler_evaluador.setAdapter(adaptadorERCerrado);

                        adaptadorERCerrado.updateDataRecycler(DER);


                    } catch (NullPointerException err) {
                                                            err.printStackTrace();
                                                        }

            }else{
                        adaptadorER.updateDataRecycler(DER,cerrado);
                 }

            */

            new AvanceValidacionAsync().execute();


        }

        protected void onCancelled()
        {
            link.showSnackBar(getApplicationContext(),coordinatorLayout,"Cancelado","ERROR");
        }

    }///FIN CLASS REFRESH INDICADORES IN ASYNC

///::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::




    public boolean guardarEvaluacion()
    {

        boolean retornar=false;


        List<DataEvaluadorRecurso> respuestas = new ArrayList<>();
        respuestas = adaptadorER.getRespuestas();

        //SQLiteDatabase db=linkDB.openDB();


        int array[]=adaptadorER.contarPreguntas();
        int preguntas=array[0];
        int respondidas=array[1];
        int aprobadas=array[2];
        int negativas=array[3];
        int nas=array[4];

        int errors=0;
        String messages="";

        if( negativas > 0 )
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


            if(errors > 0)
            {

                link.showSnackBar(getApplicationContext(),coordinatorLayout,messages,"ERROR");

                retornar = false;

            }

        }







        if(errors==0)
        {
            if(negativas>0)
            {

                Hallazgo hallazgo =linkDB.getHallazgo("RECURSO",idEvaluacionrecurso,id_indicador_selected,null);

                if(hallazgo==null)
                {
                    hallazgo=new Hallazgo(
                            0,
                            evaluacion_recurso.id,
                            "RECURSO",
                            id_indicador_selected,
                            null,
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




            }else{
                    Hallazgo hallazgo =linkDB.getHallazgo("RECURSO",idEvaluacionrecurso,id_indicador_selected,null);

                    if(hallazgo!=null)
                    {

                        hallazgo.setBorradoAl(link.getFecha());

                        linkDB.actualizarHallazgo(hallazgo,linkDB.openDB());
                    }

            }

            for (int i = 0; i < respuestas.size(); i++) {
                //si fué modificada la respuesta validar update ó insert
                if (respuestas.get(i).modificado == 1) {
                    // si id no existe, se insertará registro
                    if (respuestas.get(i).id_erc == 0) {
                        EvaluacionRecursoCriterio respuesta = new EvaluacionRecursoCriterio(
                                0,
                                respuestas.get(i).id_evaluacion_recurso,
                                respuestas.get(i).id_criterio,
                                respuestas.get(i).id_indicador,
                                respuestas.get(i).respuesta,
                                link.getFecha(),
                                "0000-00-00 00:00:00",
                                "0000-00-00 00:00:00"
                        );
                        int insert = linkDB.insertarEvaluacionRecursoCriterio(respuesta, linkDB.openDB());

                    } else { //update

                        EvaluacionRecursoCriterio respuesta = new EvaluacionRecursoCriterio(
                                                                                            respuestas.get(i).id_erc,
                                                                                            respuestas.get(i).id_evaluacion_recurso,
                                                                                            respuestas.get(i).id_criterio,
                                                                                            respuestas.get(i).id_indicador,
                                                                                            respuestas.get(i).respuesta,
                                                                                            link.getFecha(),
                                                                                            "0000-00-00 00:00:00",
                                                                                            "0000-00-00 00:00:00"

                                                                                    );

                        linkDB.actualizarEvaluacionRecursoCriterio(respuesta, linkDB.openDB());

                    }//FIN ELSE INSERTAR

                }//FIN IF :

            }
            //db.close();
            cambios=false;
            revisionBotones();
            link.showSnackBar(getApplicationContext(), coordinatorLayout,"Evaluacion guardada correctamente", "INFO");

            retornar = true;
        }// fin errors = 0

        // db.close();




        return retornar;

    }/// FIN GUARDAR EVALUACION METODO





///*************************************************************************************************
    public void confirmSalir()
    {
        if(cambios) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Existen cambios sin guardar.\n¿ Quieres salir de la evaluación ?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        }).show();
            }else {
                        finish();
                  }
    }
///*************************************************************************************************

public ContentValues getTotalCriterioIndicador(int idEvaluacion,int idIndicador)
{
    ContentValues response = new ContentValues();
    int totalCriterio=0;
    int avanceCriterio=0;

    ArrayList<LugarVerificacion> listaLVX = (ArrayList<LugarVerificacion>) linkDB.getLugaresVerificacion(nivel_cone,idIndicador);

    for (int i = 0; i < listaLVX.size(); i++)
    {
        List<Criterio> listaCriterios = new ArrayList<>();
        listaCriterios = linkDB.getCriterios(nivel_cone, idIndicador, listaLVX.get(i).id);

        for (int j = 0; j < listaCriterios.size(); j++)
        {

            EvaluacionRecursoCriterio eval = linkDB.getEvaluacionRecursoCriterio(idEvaluacion,
                                                                                 idIndicador,
                                                                                 listaCriterios.get(j).id);

            totalCriterio++;

            if (eval == null)
            {
            } else {
                        avanceCriterio++;
                   }
         }

    } //FIN FOR LISTA DE LUGARES DE VERIFICACION

    response.put("totalCriterio",totalCriterio);
    response.put("avanceCriterio",avanceCriterio);


    link.printConsola("---------->EN METODO-> totalCriterio: ["+totalCriterio+"]");
    link.printConsola("---------->EN METODO-> avanceCriterio: ["+avanceCriterio+"]");


    return response;

}
////************************************************************************************************




}//FIN CLASE EVALUADOR RECURSO


