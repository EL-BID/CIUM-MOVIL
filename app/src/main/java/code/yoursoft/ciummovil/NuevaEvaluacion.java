package code.yoursoft.ciummovil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NuevaEvaluacion extends AppCompatActivity {


    Funciones link;
    DBManager linkDB;
    Context context;
    Button boton_buscar;
    TextInputLayout til_buscar;
    EditText input_buscar;
    TextView text_message;


    MenuItem item_create;


    RecyclerView recycler;
    RecyclerView recycler_selected;
    AdaptadorClues adaptador_clues;
    AdaptadorCluesSelected adaptador_clues_selected;
    List<Clues> lista_clues;

    String CLUES_SELECTED=null;
    Usuarios user_signed=null;

    String tipo_evaluacion;



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

        setContentView(R.layout.layout_nueva_evaluacion);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle vars = getIntent().getExtras();
        if (vars != null)
        {
            tipo_evaluacion = vars.getString("TIPO_EVALUACION");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_nueva_recursos);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        getSupportActionBar().setTitle("Nueva Evaluación");

        if(tipo_evaluacion.equals("RECURSO"))
        {
            toolbar.setSubtitle("Recursos");
        }
        if(tipo_evaluacion.equals("CALIDAD"))
        {
            toolbar.setSubtitle("Procesos");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        til_buscar = (TextInputLayout) findViewById(R.id.til_buscar);
        input_buscar = (EditText) findViewById(R.id.input_unidad_buscar);
        text_message = (TextView) findViewById(R.id.text_message);
        text_message.setText("iniciando...");
        text_message.setVisibility(View.GONE);

        boton_buscar=(Button) findViewById(R.id.boton_buscar_unidad);
        boton_buscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                item_create.setVisible(false);

                String buscar=null;
                buscar = input_buscar.getText().toString().trim();
                til_buscar.setErrorEnabled(false);
                til_buscar.setError("");

                try {

                        //lista_clues = listarBusquedaClues(buscar);
                        lista_clues = linkDB.getListaClues(buscar);
                        if (lista_clues.size() <= 0)
                        {
                            recycler.setVisibility(View.GONE);
                            recycler_selected.setVisibility(View.GONE);

                            text_message.setVisibility(View.GONE);
                            link.ocultarTeclado(getApplicationContext(), input_buscar);

                            //text_message.setText("SIN RESULTADOS");
                            til_buscar.setErrorEnabled(true);
                            til_buscar.setError("No se encontraron resultados");
                        } else {
                                    text_message.setVisibility(View.GONE);
                                    recycler.setVisibility(View.VISIBLE);
                                    adaptador_clues.updateDataRecycler(lista_clues);
                                    link.ocultarTeclado(getApplicationContext(), input_buscar);
                                }

                     }catch (Exception err){
                                        System.out.println("ERROR CONSULTANDO : "+err.toString());
                                        err.printStackTrace();
                                      }


            }
        });

        recycler = (RecyclerView) findViewById(R.id.recycler_clues_buscar);
        recycler_selected = (RecyclerView) findViewById(R.id.recycler_clues_selected);

        LinearLayoutManager linearLM = new LinearLayoutManager(this);
        linearLM.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager linearLM2 = new LinearLayoutManager(this);
        linearLM.setOrientation(LinearLayoutManager.VERTICAL);

        recycler.setLayoutManager(linearLM);
        recycler_selected.setLayoutManager(linearLM2);


        lista_clues = listarBusquedaClues("");

        try {


            adaptador_clues = new AdaptadorClues(lista_clues);
            recycler.setAdapter(adaptador_clues);

            adaptador_clues_selected = new AdaptadorCluesSelected((lista_clues), getApplicationContext());
            recycler_selected.setAdapter(adaptador_clues_selected);
            recycler.setVisibility(View.GONE);
            recycler_selected.setVisibility(View.GONE);

        }catch (Exception err){
            link.printConsola("---->>>>EROR . CLUES VACIAS Ó PROBLEMA CON EL ADAPTADOR.");
        }





        recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, recycler, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                CLUES_SELECTED = lista_clues.get(position).getClues().toString();

                recycler.setVisibility(View.GONE);
                recycler_selected.setVisibility(View.VISIBLE);
                lista_clues=listarBusquedaClues(CLUES_SELECTED);
                adaptador_clues_selected.updateDataRecycler(lista_clues);

                text_message.setVisibility(View.VISIBLE);
                text_message.setText("Clues Seleccionada:");
                item_create.setVisible(true);

                System.out.println("CLUES SELECCIONADA : [[[ "+CLUES_SELECTED+" ]]]");

            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                // ...
            }
        }));









    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nueva_recursos, menu);

        item_create= menu.findItem(R.id.action_create);
        item_create.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home_back/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create)
        {
            int erros=0;

            Evaluacion evaluacion;

            int idUsuario=user_signed.getId();
            String clues=CLUES_SELECTED;
            String fechaEvaluacion=link.getFecha();
            int cerrado=0;
            String firma="";
            String responsable="";
            String email="";
            String creadoAl=link.getFecha();
            String modificadoAl="0000-00-00 00:00:00";
            String borradoAl="0000-00-00 00:00:00";

            evaluacion=new Evaluacion(0,0,idUsuario,clues,fechaEvaluacion,cerrado,firma,responsable,email,0,0,0,creadoAl,modificadoAl,borradoAl);
            SQLiteDatabase db=linkDB.openDB();
            int result=0;

            System.out.println("idServer: "+evaluacion.idServer+", idUsuario: "+idUsuario+", clues: "+evaluacion.clues+
                              ", fechaEvaluacion: "+evaluacion.fechaEvaluacion+", cerrado: "+evaluacion.fechaEvaluacion+
                              ", firma: "+evaluacion.firma+", responsable: "+evaluacion.responsable+", sincronizado: "+
                              evaluacion.sincronizado+".");


            List<Evaluacion> EVALUACIONES;


            if(tipo_evaluacion.equals("RECURSO"))
            {
                EVALUACIONES  = linkDB.getEvaluaciones("RECURSO", "", user_signed.getId());
            }else{  EVALUACIONES = linkDB.getEvaluaciones("CALIDAD", "", user_signed.getId());  }

            boolean existe = false;

            Date date= new Date();
            long fecha_actual = date.getTime();
            Evaluacion evaluacion_existente = null;

            if(EVALUACIONES.size() > 0)
            {
                for (int a = 0; a < EVALUACIONES.size(); a++)
                {
                    Evaluacion evaluacion_revisar = EVALUACIONES.get(a);
                    long fecha_evaluacion = 0;

                    Date fecha_convertida = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    try {
                            fecha_convertida = dateFormat.parse( evaluacion_revisar.getFechaEvaluacion() );
                        } catch(Exception e) {
                                                System.out.println("Error occurred"+ e.getMessage());
                                            }

                    fecha_evaluacion = fecha_convertida.getTime();
                    long diferencia = fecha_actual-fecha_evaluacion;

                    long segundos = diferencia / 1000;
                    long minutos = segundos / 60;
                    long horas = minutos / 60;
                    long dias = horas / 24;

                    System.out.println("---->>>>>>>>>>>>>>> CLUES SELECCIONADA : [[[ "+CLUES_SELECTED+" ]]]");
                    System.out.println("---->>>>>>>>>>>>>>> DIFERENCIA  MLS: [[[ "+diferencia+" ]]]    DIAS   :   [[ "+dias+" ]]");


                    if( (dias <= 7) && evaluacion_revisar.clues.equals(CLUES_SELECTED) )
                    {
                        existe = true;
                        evaluacion_existente = evaluacion_revisar;
                        break;
                    }

                }
            }



            System.out.println("VALOR  EXISTE  : [[[ "+String.valueOf(existe)+" ]]]");





            if(existe == false)
            {
                Intent lanzador;

                if(tipo_evaluacion.equals("RECURSO"))
                {
                    result = linkDB.insertarEvaluacion("RECURSO", evaluacion, db);
                    lanzador = new Intent(NuevaEvaluacion.this, EvaluadorRecurso.class);
                    lanzador.putExtra("idEvaluacionRecurso", result);
                }else {
                         result = linkDB.insertarEvaluacion("CALIDAD", evaluacion, db);
                         lanzador = new Intent(NuevaEvaluacion.this, EvaluadorCalidad.class);
                         lanzador.putExtra( "idEvaluacionCalidad", result);
                      }

                System.out.println("ID GENERADO DE NUEVA EVALUACION: [[[ "+result+" ]]]");

                startActivity(lanzador);
                finish();

            }else{

                        if(evaluacion_existente.sincronizado == 1)
                        {
                            ///*************************************************************************************************
                            AlertDialog.Builder builder = new AlertDialog.Builder(NuevaEvaluacion.this, R.style.TemaDialogMaterial);
                            builder.setCancelable(false);
                            final Evaluacion finalEvaluacion_existente = evaluacion_existente;
                            builder.setTitle("Aviso");


                            builder.setMessage("En los ultimos 7 días, usted ya evaluó esta CLUES.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).show();
                            ///*************************************************************************************************
                        }else{
                                    if( evaluacion_existente.firma.equals(""))
                                    {
                                        Intent lanzador;

                                        if(tipo_evaluacion.equals("RECURSO"))
                                        {
                                            Evaluacion eval_temp = evaluacion_existente;
                                            eval_temp.firma="";
                                            eval_temp.responsable="";
                                            eval_temp.cerrado=0;
                                            eval_temp.emailResponsable="";

                                            linkDB.firmarEvaluacion("RECURSO",eval_temp,linkDB.openDB());
                                            //linkDB.actualizarEvaluacionRecurso(eval_temp, linkDB.openDB());

                                            lanzador = new Intent(NuevaEvaluacion.this, EvaluadorRecurso.class);
                                            lanzador.putExtra("idEvaluacionRecurso", evaluacion_existente.getId());
                                        }else {

                                            Evaluacion eval_temp = evaluacion_existente;
                                            eval_temp.firma="";
                                            eval_temp.responsable="";
                                            eval_temp.cerrado=0;
                                            eval_temp.emailResponsable="";
                                            linkDB.firmarEvaluacion("CALIDAD",eval_temp,linkDB.openDB());
                                            //linkDB.actualizarEvaluacionCalidad(eval_temp, linkDB.openDB());

                                            lanzador = new Intent(NuevaEvaluacion.this, EvaluadorCalidad.class);
                                            lanzador.putExtra( "idEvaluacionCalidad", evaluacion_existente.getId());
                                        }

                                        startActivity(lanzador);
                                        finish();
                                    }else{
                                            ///*************************************************************************************************
                                            AlertDialog.Builder builder = new AlertDialog.Builder(NuevaEvaluacion.this, R.style.TemaDialogMaterial);
                                            builder.setCancelable(false);
                                            final Evaluacion finalEvaluacion_existente = evaluacion_existente;
                                            builder.setTitle("Advertencia !!!");

                                            builder.setMessage("Ya existe una evaluación para esta CLUES en este periodo. ¿ Desea continuar editando esta ? Tendrá que volver a recavar la firma del responsable nuevamente.")
                                                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int id) {


                                                            Intent lanzador;

                                                            if (tipo_evaluacion.equals("RECURSO")) {
                                                                Evaluacion eval_temp = finalEvaluacion_existente;
                                                                eval_temp.firma = "";
                                                                eval_temp.responsable = "";
                                                                eval_temp.cerrado = 0;
                                                                eval_temp.emailResponsable = "";

                                                                linkDB.firmarEvaluacion("RECURSO", eval_temp, linkDB.openDB());
                                                                //linkDB.actualizarEvaluacionRecurso(eval_temp, linkDB.openDB());

                                                                lanzador = new Intent(NuevaEvaluacion.this, EvaluadorRecurso.class);
                                                                lanzador.putExtra("idEvaluacionRecurso", finalEvaluacion_existente.getId());
                                                            } else {

                                                                Evaluacion eval_temp = finalEvaluacion_existente;
                                                                eval_temp.firma = "";
                                                                eval_temp.responsable = "";
                                                                eval_temp.cerrado = 0;
                                                                eval_temp.emailResponsable = "";
                                                                linkDB.firmarEvaluacion("CALIDAD", eval_temp, linkDB.openDB());
                                                                //linkDB.actualizarEvaluacionCalidad(eval_temp, linkDB.openDB());

                                                                lanzador = new Intent(NuevaEvaluacion.this, EvaluadorCalidad.class);
                                                                lanzador.putExtra("idEvaluacionCalidad", finalEvaluacion_existente.getId());
                                                            }

                                                            System.out.println("ID  EVALUACION  A CONTINUAR: [[[ " + finalEvaluacion_existente.getId() + " ]]]");

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
                                            ///*************************************************************************************************
                                         }


                             }


                      /*
                        if(evaluacion_existente.cerrado == 0 )
                        {
                            Intent lanzador;

                            if(tipo_evaluacion.equals("RECURSO"))
                            {
                                lanzador = new Intent(NuevaEvaluacion.this, EvaluadorRecurso.class);
                                lanzador.putExtra("idEvaluacionRecurso", evaluacion_existente.getId());
                            }else {

                                lanzador = new Intent(NuevaEvaluacion.this, EvaluadorCalidad.class);
                                lanzador.putExtra( "idEvaluacionCalidad", evaluacion_existente.getId());
                            }

                            startActivity(lanzador);
                            finish();
                        }else {



                              }
                      */

                 }



        }

        return super.onOptionsItemSelected(item);
    }



    private ArrayList<Clues> listarBusquedaClues(String buscar)
    {
        ArrayList<Clues> array_clues_result = new ArrayList<>();
        DBManager linkDB=new DBManager(this);

        JSONObject clues_result;
        JSONArray array_clues;
        int dim=0;

        try {

              clues_result=linkDB.getAllClues(buscar);

              System.out.println("RESULT QUERY CLUES ON DB : "+clues_result.toString());

              array_clues=clues_result.getJSONArray("clues");
              dim=array_clues.length();

              for(int i=0; i<array_clues.length();i++)
                {
                    JSONObject item_data_clues=array_clues.getJSONObject(i);

                    array_clues_result.add(new Clues(item_data_clues.getString("clues"),
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
                                                      item_data_clues.getInt("idCone") ));
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        if(dim<=0)
        {
            return null;
        }else {
                return array_clues_result;
              }

    }




}


