package code.yoursoft.ciummovil;

        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.Fragment;
        import android.support.v4.widget.SwipeRefreshLayout;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.view.ActionMode;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.RelativeLayout;

        import java.util.ArrayList;
        import java.util.List;

public class FragRecursos extends Fragment
{

    FloatingActionButton nueva_evaluacion;
    RecyclerView recyclerRecurso;
    AdaptadorListaEvaluacionesRecurso adaptadorRecurso;
    List<EvaluacionDetalles> lista_recurso;

    ActionMode actionMode;

    Toolbar toolbar;
    Menu menu;
    Funciones link = new Funciones();
    DBManager linkDB;

    boolean MODO_SELECCIONADOR;

    Usuarios user_signed;

    CoordinatorLayout coordinatorLayout;

    ProgressDialog progress_listar_evaluaciones;
    private SwipeRefreshLayout swipeContenedor;



    @Override
    public void onStart()
    {
        super.onStart();

        //new CargarListaEvaluacionesAsync().execute();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        //new CargarListaEvaluacionesAsync().execute();
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        linkDB = new DBManager(getActivity());
        user_signed = linkDB.getSignedUser();

        if(user_signed==null)
        {
            link.goLogin(this.getActivity());
        }



        MODO_SELECCIONADOR=false;
        final View rootView = inflater.inflate(R.layout.layout_recursos, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //rootView.setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

        swipeContenedor = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContenedor);
        swipeContenedor.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new CargarListaEvaluacionesAsyncClean().execute();
            }
        });

        swipeContenedor.setColorSchemeResources(android.R.color.holo_blue_bright,
                                                android.R.color.holo_green_light,
                                                android.R.color.holo_orange_light,
                                                android.R.color.holo_red_light);



        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);




        recyclerRecurso = (RecyclerView) rootView.findViewById(R.id.recycler_list_recursos);

        LinearLayoutManager linearLM = new LinearLayoutManager(this.getActivity());
        linearLM.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerRecurso.setLayoutManager(linearLM);

        linkDB = new DBManager(getActivity().getApplicationContext());


        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);


        lista_recurso = new ArrayList<>();

        try {
               //lista_recurso = link.listarBusquedaEvaluaciones("RECURSO","",user_signed.id,this.getActivity());

               adaptadorRecurso = new AdaptadorListaEvaluacionesRecurso(lista_recurso,getActivity());

               recyclerRecurso.setAdapter(adaptadorRecurso);

               recyclerRecurso.addItemDecoration(itemDecoration);
               recyclerRecurso.setItemAnimator(new DefaultItemAnimator());

            }catch (NullPointerException err){
                                                err.printStackTrace();
                                                System.out.println("ERROR COLOCANDO ANIMACION ...");

                                             }



        recyclerRecurso.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerRecurso, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                if (adaptadorRecurso.getSelectedItemCount() > 0)
                {
                    MODO_SELECCIONADOR = true;
                } else {
                         MODO_SELECCIONADOR = false;
                       }

                refreshStatusToolbar();

                if (adaptadorRecurso.estaSeleccionado(position) || MODO_SELECCIONADOR == true)
                {
                    adaptadorRecurso.toggleSelection(position);
                    refreshStatusToolbar();

                    return;
                }

                Intent lanzador = new Intent(getActivity(), EvaluadorRecurso.class);
                lanzador.putExtra("idEvaluacionRecurso", lista_recurso.get(position).id);

                startActivity(lanzador);
            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                ToggleSelection(position);
                refreshStatusToolbar();
            }

        }));



        new CargarListaEvaluacionesAsync().execute();



       return rootView;
    }

    public void refreshStatusToolbar()
    {
        int n=0;

        try {
               n = adaptadorRecurso.getSelectedItemCount();
            }catch (NullPointerException err){ err.printStackTrace(); }

        String plural="";

        if( n > 0)
        {

            if(n>1){ plural="s"; }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(n+" Seleccionada"+plural);

        }else{
               ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
             }
        refreshStatusMenu();

    }


    ///*************************************************************************************************
    public void confirmarEliminar(int evaluaciones)
    {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

            builder.setMessage(" ¿ Eliminar "+evaluaciones+" evaluaciones de Recursos ? ")
                    .setPositiveButton("Eliminar", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            ejecutarBorrar();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.cancel();
                        }
                    }).show();

    }
///*************************************************************************************************



    public void refreshStatusMenu()
    {
        MenuItem nuevo = menu.findItem(R.id.nueva_evaluacion);
        MenuItem borrar = menu.findItem(R.id.borrar_seleccion);
        MenuItem deseleccionar = menu.findItem(R.id.deselecionar);

        int n=0;
        try {
               n = adaptadorRecurso.getSelectedItemCount();
            }catch (NullPointerException err){ err.printStackTrace(); }

            if (n > 0) {
                            nuevo.setVisible(false);
                            borrar.setVisible(true);
                            deseleccionar.setVisible(true);
                        } else {
                                    nuevo.setVisible(true);
                                    borrar.setVisible(false);
                                    deseleccionar.setVisible(false);
                               }
    }



    private void ToggleSelection(int idx)
    {
        adaptadorRecurso.toggleSelection(idx);
        //String title = getString(R.string.selected_count, adapter.getSelectedItemCount());
        //actionMode.setTitle(title);
    }





    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_recursos, menu);

        this.menu = menu;
        refreshStatusToolbar();


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nueva_evaluacion)
        {

            preguntarModoNuevaEvaluacion();

        }

        if (id == R.id.borrar_seleccion)
        {
            List<Integer> seleccionados= adaptadorRecurso.getItemsSeleccinados();
            confirmarEliminar((int) seleccionados.size());
        }

        if (id == R.id.deselecionar)
        {
            adaptadorRecurso.resetearSelecciones();
            refreshStatusToolbar();
        }

        return super.onOptionsItemSelected(item);
    }


    ///************************************************************************************************

    public void ejecutarBorrar()
    {

        List<Integer> seleccionados= adaptadorRecurso.getItemsSeleccinados();

        System.out.println("SELECCIONADOS : [ " + seleccionados.size() + " ]");

        for(int i=0; i<seleccionados.size(); i++)
        {
            EvaluacionDetalles eval = adaptadorRecurso.getItem(seleccionados.get(i));

            System.out.println("EVALUACION  ID : [ " + eval.getId() + " ]");

            Evaluacion eval_recurso = linkDB.getEvaluacion("RECURSO", eval.getId(), linkDB.openDB());
            eval_recurso.setBorradoAl(link.getFecha());

            linkDB.actualizarEvaluacionRecurso(eval_recurso, linkDB.openDB());
        }

        adaptadorRecurso.resetearSelecciones();
        refreshStatusToolbar();

        try {
            lista_recurso = link.listarBusquedaEvaluaciones("RECURSO", "", user_signed.id,this.getActivity().getApplicationContext());

            System.out.println("LISTA RECURSO ACTUALIZADA : [ " + lista_recurso.size() + " ]");
            adaptadorRecurso.updateDataRecycler(lista_recurso);

        }catch (NullPointerException err){ err.printStackTrace(); }

    }


    ///************************************************************************************************

    public void preguntarModoNuevaEvaluacion()
    {
        ///***** TEMP
        Config config_clues = linkDB.getConfig("CLUES",user_signed.id);
        if(config_clues != null)
        {
            Intent lanzador = new Intent(getActivity(), NuevaEvaluacion.class);
            lanzador.putExtra( "TIPO_EVALUACION","RECURSO" );
            startActivity(lanzador);
        }else{
            link.showSnackBar(getActivity(),coordinatorLayout,"Por favor actualize las CLUES","ERROR");
        }

        ///*********

        List<String> OPCIONES = new ArrayList<>();
        OPCIONES.add("Recibir por Bluetooth");
        OPCIONES.add("Manual");



        final CharSequence[] LISTA = OPCIONES.toArray(new String[ OPCIONES.size() ]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Seleccione una opción");
        builder.setItems(LISTA, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int position)
            {

                if (position == 0)
                {
                    Intent lanzador = new Intent(getActivity(), Bluetooth.class);
                    lanzador.putExtra("idEvaluacion", 0);
                    lanzador.putExtra("MODO", "RECEPTOR");
                    lanzador.putExtra("FULL", 1);
                    lanzador.putExtra("TIPO_EVALUACION", "RECURSO");

                    System.out.println("");
                    startActivity(lanzador);
                }


                if (position == 1)
                {
                    Config config_clues = linkDB.getConfig("CLUES",user_signed.id);
                    if(config_clues != null)
                    {
                        Intent lanzador = new Intent(getActivity(), NuevaEvaluacion.class);
                        lanzador.putExtra( "TIPO_EVALUACION","RECURSO" );
                        startActivity(lanzador);
                    }else{
                           link.showSnackBar(getActivity(),coordinatorLayout,"Por favor actualize las CLUES","ERROR");
                         }
                }


            }
        });
       // AlertDialog alert = builder.create();
    /*
        Window window = alert.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
    */

       // alert.show();
    }


/////**********************************************************************************************


///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    private class CargarListaEvaluacionesAsync extends AsyncTask<String, Integer, Integer>
    {
        protected void onPreExecute()
        {
            progress_listar_evaluaciones = new ProgressDialog(getActivity());
            progress_listar_evaluaciones.setTitle("Por favor espere");
            progress_listar_evaluaciones.setMessage("Leyendo evaluaciones ...");
            progress_listar_evaluaciones.setCancelable(false);
            progress_listar_evaluaciones.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress_listar_evaluaciones.setIndeterminate(true);

            progress_listar_evaluaciones.show();
        }

        protected Integer doInBackground(String... datos)
        {
            int errors=0;
            String messages="";

            String data[]={""+errors,messages};

            lista_recurso = link.listarBusquedaEvaluaciones("RECURSO","",user_signed.id, getActivity());

            return errors;
        }

        protected void onProgressUpdate(Integer... values)
        {
        }

        protected void onPostExecute(Integer errors)
        {
            adaptadorRecurso.updateDataRecycler(lista_recurso);
            progress_listar_evaluaciones.dismiss();
        }

        protected void onCancelled()
        {
            link.showSnackBar(getActivity(),coordinatorLayout,"Cancelado","ERROR");
        }

    }///FIN CLASS REFRESH INDICADORES IN ASYNC

///::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    private class CargarListaEvaluacionesAsyncClean extends AsyncTask<String, Integer, Integer>
    {
        protected void onPreExecute()
        {

        }

        protected Integer doInBackground(String... datos)
        {
            int errors=0;

            lista_recurso = link.listarBusquedaEvaluaciones("RECURSO","",user_signed.id, getActivity());

            return errors;
        }

        protected void onProgressUpdate(Integer... values)
        {
        }

        protected void onPostExecute(Integer errors)
        {
            adaptadorRecurso.updateDataRecycler(lista_recurso);
            swipeContenedor.setRefreshing(false);
        }

        protected void onCancelled()
        {
            link.showSnackBar(getActivity(),coordinatorLayout,"Cancelado","ERROR");
        }

    }///FIN CLASS REFRESH INDICADORES IN ASYNC

///::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
///:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::




}