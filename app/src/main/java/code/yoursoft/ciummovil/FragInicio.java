package code.yoursoft.ciummovil;



        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import java.util.List;

public class FragInicio extends Fragment
{
    Toolbar toolbar;
    Menu menu;
    Funciones link = new Funciones();
    DBManager linkDB;

    boolean MODO_SELECCIONADOR;

    Usuarios user_signed;

    CoordinatorLayout coordinatorLayout;

    RelativeLayout layout_recursos;
    RelativeLayout layout_calidad;

    Button boton_recursos, boton_calidad;

    TextView text_evaluaciones_recursos, text_completas_recursos, text_sincronizadas_recursos;
    TextView text_evaluaciones_calidad, text_completas_calidad, text_sincronizadas_calidad;

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
        final View rootView = inflater.inflate(R.layout.layout_inicio, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //rootView.setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        linkDB = new DBManager(getActivity().getApplicationContext());

        boton_recursos = (Button) rootView.findViewById(R.id.boton_nueva_recursos);
        boton_calidad = (Button) rootView.findViewById(R.id.boton_nueva_calidad);

        text_evaluaciones_recursos = (TextView) rootView.findViewById(R.id.text_evaluaciones_recurso);
        text_completas_recursos = (TextView) rootView.findViewById(R.id.text_completas_recurso);
        text_sincronizadas_recursos = (TextView) rootView.findViewById(R.id.text_sincronizadas_recurso);

        text_evaluaciones_calidad = (TextView) rootView.findViewById(R.id.text_evaluaciones_calidad);
        text_completas_calidad = (TextView) rootView.findViewById(R.id.text_completas_calidad);
        text_sincronizadas_calidad = (TextView) rootView.findViewById(R.id.text_sincronizadas_calidad);

        layout_recursos = (RelativeLayout) rootView.findViewById(R.id.layout_dash_recursos);
        layout_calidad = (RelativeLayout) rootView.findViewById(R.id.layout_dash_calidad);

        layout_recursos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Recursos");

                FragRecursos fragment2 = new FragRecursos();
                FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.frame, fragment2);
                fragmentTransaction2.commit();

            }
        });

        layout_calidad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Calidad");


                FragCalidad fragment3 = new FragCalidad();
                FragmentTransaction fragmentTransaction3 = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.frame, fragment3);
                fragmentTransaction3.commit();

            }
        });




        boton_recursos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Config config_clues = linkDB.getConfig("CLUES",user_signed.id);

                if(config_clues != null)
                {
                    Intent lanzador = new Intent(getActivity(), NuevaEvaluacion.class);
                    lanzador.putExtra("TIPO_EVALUACION", "RECURSO");
                    startActivity(lanzador);
                }else{
                      link.showSnackBar(getActivity(),coordinatorLayout,"Por favor descargue los catalogos.","ERROR");
                     }

            }
        });



        boton_calidad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Config config_clues = linkDB.getConfig("CLUES",user_signed.id);

                if(config_clues != null)
                {
                    Intent lanzador = new Intent(getActivity(), NuevaEvaluacion.class);
                    lanzador.putExtra("TIPO_EVALUACION", "CALIDAD");
                    startActivity(lanzador);
                }else{
                    link.showSnackBar(getActivity(),coordinatorLayout,"Por favor descargue los catalogos.","ERROR");
                }

            }
        });



        cargarEstadisticas();


        return rootView;
    }



    public void cargarEstadisticas()
    {

        List<Evaluacion> lista_recursos = linkDB.getEvaluaciones("RECURSO","",user_signed.getId());

        int total_recursos=lista_recursos.size();
        int completas=0;
        int sincronizadas=0;


        for(int i=0; i<lista_recursos.size(); i++)
        {
            Evaluacion evaluacion= lista_recursos.get(i);

            if(evaluacion.getCerrado()==1 && !evaluacion.getFirma().equals(""))
            {
                completas++;
            }

            if(evaluacion.getSincronizado()==1)
            {
                sincronizadas++;
            }
        }

        text_evaluaciones_recursos.setText(""+total_recursos);
        text_completas_recursos.setText(""+completas);
        text_sincronizadas_recursos.setText(""+sincronizadas);


        List<Evaluacion> lista_calidad = linkDB.getEvaluaciones("CALIDAD","",user_signed.getId());

        int total_calidad = lista_calidad.size();
        int completas_calidad=0;
        int sincronizadas_calidad=0;


        for(int i=0; i<lista_calidad.size(); i++)
        {
            Evaluacion evaluacion = lista_calidad.get(i);

            if(evaluacion.getCerrado()==1 && !evaluacion.getFirma().equals(""))
            {
                completas_calidad++;
                //link.printToast("calidad cerrada",getActivity());
            }

            if(evaluacion.getSincronizado()==1)
            {
                sincronizadas_calidad++;
            }
        }

        text_evaluaciones_calidad.setText(""+total_calidad);
        text_completas_calidad.setText(""+completas_calidad);
        text_sincronizadas_calidad.setText(""+sincronizadas_calidad);



    }




}