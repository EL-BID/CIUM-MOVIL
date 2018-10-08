package code.yoursoft.ciummovil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import java.util.List;







public class Home extends AppCompatActivity
{


    CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView texto_email;
    private TextView texto_nombre;

    Bundle bundle;
    Context context;
    JSONObject user_signed;

    private Funciones link;
    private DBManager linkDB;

    private Usuarios user_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = this.getApplicationContext();

        link = new Funciones();
        linkDB = new DBManager(context);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.inflateHeaderView(R.layout.header);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);



        texto_email = (TextView) header.findViewById(R.id.email_header);
        texto_nombre = (TextView)  header.findViewById(R.id.username_header);

        user_sesion= linkDB.getSignedUser();

        if(user_sesion==null)
          {
             link.goLogin(this);
          }else{

                    String email=user_sesion.getEmail();
                    String nombre = user_sesion.getApellidoPaterno()+" "+user_sesion.getApellidoMaterno()+" "+user_sesion.getNombres();


                 texto_email.setText(email);
                 texto_nombre.setText(nombre);

               }

        List<Clues> clues = linkDB.getListaClues("");


/*
        Usuarios user_test = linkDB.getUser("hernandezjzonal2pn@gmail.com");

        List<Evaluacion> evaluaciones=linkDB.getEvaluaciones("RECURSO","",user_test.getId());

        for(int i=0; i<evaluaciones.size();i++)
        {
            Evaluacion item_er=evaluaciones.get(i);

            item_er.setIdUsuario(user_sesion.getId());
            item_er.setSincronizado(0);

            linkDB.actualizarEvaluacionRecurso(item_er,linkDB.openDB());
        }

        List<Evaluacion> evaluaciones2=linkDB.getEvaluaciones("CALIDAD","",user_test.getId());

        for(int i=0; i<evaluaciones2.size();i++)
        {
            Evaluacion item_ec=evaluaciones2.get(i);

            item_ec.setIdUsuario(user_sesion.getId());
            item_ec.setSincronizado(0);

            linkDB.actualizarEvaluacionCalidad(item_ec,linkDB.openDB());
        }


*/


        if(clues.size()<=0)
        {
            Bundle bundle = new Bundle();
            String bandera = "NUEVO";
            bundle.putString("BANDERA",bandera);


            getSupportActionBar().setTitle("Sincronización");
            toolbar.setSubtitle("");
            FragSync fragment100 = new FragSync();
            fragment100.setArguments(bundle);

            android.support.v4.app.FragmentTransaction fragmentTransaction100 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction100.replace(R.id.frame, fragment100);
            fragmentTransaction100.commit();


        }else{

                getSupportActionBar().setTitle("Dashboard");
                toolbar.setSubtitle("");
                //Toast.makeText(getApplicationContext(), "Inicio Selected", Toast.LENGTH_SHORT).show();
                FragInicio fragment101 = new FragInicio();
                android.support.v4.app.FragmentTransaction fragmentTransaction101 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction101.replace(R.id.frame, fragment101);
                fragmentTransaction101.commit();

             }




        //printToast("PERFIL : "+link.getPerfil(bundle.getString("email").toString(),bundle.getString("accessToken"),bundle.getString("refreshToken")));

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;


                    case R.id.inicio:

                        getSupportActionBar().setTitle("Dashboard");
                        toolbar.setSubtitle("");
                        //Toast.makeText(getApplicationContext(), "Inicio Selected", Toast.LENGTH_SHORT).show();
                        FragInicio fragment = new FragInicio();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commit();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.recursos:
                        //Toast.makeText(getApplicationContext(), "Recursos Selected", Toast.LENGTH_SHORT).show();

                        //toolbar.setTitle("Cium - Recursos");

                        getSupportActionBar().setTitle("Recursos");
                        toolbar.setSubtitle("");

                        FragRecursos fragment2 = new FragRecursos();
                        android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frame, fragment2);
                        fragmentTransaction2.commit();
                        return true;
                    case R.id.calidad:


                        getSupportActionBar().setTitle("Calidad");
                        toolbar.setSubtitle("");
                        //Toast.makeText(getApplicationContext(), "Calidad Selected", Toast.LENGTH_SHORT).show();
                        FragCalidad fragment3 = new FragCalidad();
                        android.support.v4.app.FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.frame, fragment3);
                        fragmentTransaction3.commit();
                        return true;


                    case R.id.sync:
                        //Toast.makeText(getApplicationContext(), "Zona Selected", Toast.LENGTH_SHORT).show();

                        getSupportActionBar().setTitle("Sincronización");
                        toolbar.setSubtitle("");
                        //Toast.makeText(getApplicationContext(), "Config Selected", Toast.LENGTH_SHORT).show();
                        FragSync fragment6 = new FragSync();
                        android.support.v4.app.FragmentTransaction fragmentTransaction6 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction6.replace(R.id.frame, fragment6);
                        fragmentTransaction6.commit();
                        return true;

/*
                    case R.id.hallazgos:
                        Toast.makeText(getApplicationContext(), "Pendientes Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    */
                    case R.id.config:

                        getSupportActionBar().setTitle("Configuración");
                        toolbar.setSubtitle("");
                        //Toast.makeText(getApplicationContext(), "Config Selected", Toast.LENGTH_SHORT).show();
                        FragConfig fragment7 = new FragConfig();
                        android.support.v4.app.FragmentTransaction fragmentTransaction7 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction7.replace(R.id.frame, fragment7);
                        fragmentTransaction7.commit();
                        return true;

                    /*
                    case R.id.manual:

                        Intent lanzador = new Intent(getApplicationContext(), ManualUsuario.class);

                        startActivity(lanzador);

                        return true;
                    */

                    case R.id.close_sesion:

                        showDialogComandoSalir();

                        return true;

                    default:
                        //Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer)
        {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();



    }

    @Override protected void onStart()
    {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_home, menu);

        MenuItem item_nueva= menu.findItem(R.id.nueva_evaluacion_recursos);
        item_nueva.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home_back/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }



    public Usuarios check_signed()
    {
        Usuarios user=linkDB.getSignedUser();

        return  user;
    }



    public void showDialogComandoSalir()
    {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_comando_salir_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edit_comando_dialog = (EditText) dialogView.findViewById(R.id.input_comando);
        final TextView text_message = (TextView) dialogView.findViewById(R.id.text_message);


        text_message.setVisibility(View.GONE);

        dialogBuilder.setTitle("Confirmación de salida");

        dialogBuilder.setPositiveButton("Salir", new DialogInterface.OnClickListener()
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
                Boolean closeDialog = false;

                int errors = 0;
                String messages = "";
                String comando = edit_comando_dialog.getText().toString();

                if (comando.equals(""))
                {
                    errors++;
                    messages += "Escriba la palabra \"salir\".";
                } else {
                             if(comando.equalsIgnoreCase("salir"))
                             {

                             }else{
                                    errors++;
                                    messages += "Escriba la palabra \"salir\" correctamente.";

                                  }

                       }

                if (errors == 0)
                {

                    link.ocultarTeclado(context, edit_comando_dialog);
                    edit_comando_dialog.setText("");
                    closeDialog=true;

                    linkDB.signOut();

                    if(check_signed()==null)
                    {
                        Intent lanzador = new Intent(Home.this, Login.class);
                        Home.this.startActivity(lanzador);
                        finish();
                    }else{
                        //printToast("Error cerrando sesion...");
                        link.showSnackBar(getApplicationContext(),coordinatorLayout,"Error cerrando sesion","INFO");
                    }

                } else {
                    link.ocultarTeclado(context, edit_comando_dialog);
                    text_message.setVisibility(View.VISIBLE);
                    text_message.setText(messages);

                    closeDialog=false;

                    //link.showSnackBar(context, coordinatorLayout, messages, "ERROR");
                    //setCount();
                }



                if (closeDialog)
                    b.dismiss();


            }
        });
/////***********************************************************************************************

    }





}
