package code.yoursoft.ciummovil;



import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragCalidad extends Fragment
{


    RecyclerView recycler_calidad;
    AdaptadorListaEvaluacionesCalidad adaptador_calidad;
    List<EvaluacionDetalles> lista_calidad;



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

    }

    @Override
    public void onResume()
    {
        super.onResume();


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
        final View rootView = inflater.inflate(R.layout.layout_calidad, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //rootView.setSupportActionBar(toolbar);

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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

        recycler_calidad = (RecyclerView) rootView.findViewById(R.id.recycler_list_calidad);

        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);

        LinearLayoutManager linearLM = new LinearLayoutManager(this.getActivity());
        linearLM.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_calidad.setLayoutManager(linearLM);

        linkDB = new DBManager(getActivity().getApplicationContext());


        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);


        lista_calidad = new ArrayList<>();

        try {

            //lista_calidad = link.listarBusquedaEvaluaciones("CALIDAD","",user_signed.id,getActivity());

            //lista_calidad = linkDB.getEvaluacionesDetalles("CALIDAD","",user_signed.id);
            adaptador_calidad = new AdaptadorListaEvaluacionesCalidad(lista_calidad,getActivity());

            recycler_calidad.setAdapter(adaptador_calidad);

            recycler_calidad.addItemDecoration(itemDecoration);
            recycler_calidad.setItemAnimator(new DefaultItemAnimator());

        }catch (NullPointerException err){
            err.printStackTrace();
            System.out.println("ERROR COLOCANDO ANIMACION ...");

        }



        recycler_calidad.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recycler_calidad, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                if (adaptador_calidad.getSelectedItemCount() > 0)
                {
                    MODO_SELECCIONADOR = true;
                } else {
                          MODO_SELECCIONADOR = false;
                       }

                refreshStatusToolbar();

                if (adaptador_calidad.estaSeleccionado(position) || MODO_SELECCIONADOR == true)
                {
                    adaptador_calidad.toggleSelection(position);
                    refreshStatusToolbar();

                    return;
                }

                Intent lanzador = new Intent(getActivity(), EvaluadorCalidad.class);
                lanzador.putExtra("idEvaluacionCalidad", lista_calidad.get(position).id);

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
            n = adaptador_calidad.getSelectedItemCount();
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


    public void refreshStatusMenu()
    {
        MenuItem nuevo = menu.findItem(R.id.nueva_evaluacion);
        MenuItem borrar = menu.findItem(R.id.borrar_seleccion);
        MenuItem deseleccionar = menu.findItem(R.id.deselecionar);

        int n=0;
        try {
            n = adaptador_calidad.getSelectedItemCount();
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
        adaptador_calidad.toggleSelection(idx);
        //String title = getString(R.string.selected_count, adapter.getSelectedItemCount());
        //actionMode.setTitle(title);
    }


    ///*************************************************************************************************
    public void confirmarEliminar(int evaluaciones)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        builder.setMessage("¿ Eliminar "+evaluaciones+" evaluaciones de Calidad ? ")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        ejecutarBorrar();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();

    }
///*************************************************************************************************

    public void ejecutarBorrar()
    {
        List<Integer> seleccionados= adaptador_calidad.getItemsSeleccinados();

        System.out.println("SELECCIONADOS : [ " + seleccionados.size() + " ]");

        for(int i=0; i<seleccionados.size(); i++)
        {
            EvaluacionDetalles eval = adaptador_calidad.getItem(seleccionados.get(i));

            System.out.println("EVALUACION  ID : [ " + eval.getId() + " ]");

            Evaluacion eval_calidad = linkDB.getEvaluacion("CALIDAD", eval.getId(), linkDB.openDB());
            eval_calidad.setBorradoAl(link.getFecha());

            linkDB.actualizarEvaluacionCalidad(eval_calidad, linkDB.openDB());

        }

        adaptador_calidad.resetearSelecciones();
        refreshStatusToolbar();


        try {
            lista_calidad = link.listarBusquedaEvaluaciones("CALIDAD", "", user_signed.id,this.getActivity().getApplicationContext());

            System.out.println("LISTA CALIDAD ACTUALIZADA : [ " + lista_calidad.size() + " ]");
            adaptador_calidad.updateDataRecycler(lista_calidad);

        }catch (NullPointerException err){ err.printStackTrace(); }
    }


/////************************************************************************************************

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_calidad, menu);

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
            Config config_clues = linkDB.getConfig("CLUES",user_signed.id);

            if(config_clues != null)
            {
                Intent lanzador = new Intent(getActivity(), NuevaEvaluacion.class);
                lanzador.putExtra("TIPO_EVALUACION", "CALIDAD");
                startActivity(lanzador);
            }else{
                    link.showSnackBar(getActivity(),coordinatorLayout,"Por favor actualize las CLUES","ERROR");
                 }
        }
        if (id == R.id.borrar_seleccion)
        {
            List<Integer> seleccionados= adaptador_calidad.getItemsSeleccinados();
            confirmarEliminar((int) seleccionados.size());
        }
        if (id == R.id.deselecionar)
        {
            adaptador_calidad.resetearSelecciones();
            refreshStatusToolbar();
        }

        return super.onOptionsItemSelected(item);
    }




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

            lista_calidad = link.listarBusquedaEvaluaciones("CALIDAD","",user_signed.id, getActivity());

            return errors;
        }

        protected void onProgressUpdate(Integer... values)
        {
        }

        protected void onPostExecute(Integer errors)
        {
            adaptador_calidad.updateDataRecycler(lista_calidad);
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

            lista_calidad = link.listarBusquedaEvaluaciones("CALIDAD","",user_signed.id, getActivity());

            return errors;
        }

        protected void onProgressUpdate(Integer... values)
        {
        }

        protected void onPostExecute(Integer errors)
        {
            adaptador_calidad.updateDataRecycler(lista_calidad);
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