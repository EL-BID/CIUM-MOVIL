package code.yoursoft.ciummovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import android.widget.Toolbar;

import android.content.DialogInterface.OnCancelListener;

public class Login extends AppCompatActivity {

    private ProgressDialog dialog;
    private ProgressDialog dialog_login;
    // private ProgressDialog dialog_update;
   // private updateAsync lanzador;

    Funciones link = new Funciones();
    DBManager linkDB;
    Context context;
    EditText campo_usuario;
    EditText campo_password;


    Button boton_accesar;
    Button boton_sincronizar;
    Snackbar barra_snack;
    private CoordinatorLayout coordinatorLayout;
    private RelativeLayout layout_loading;

    String usuario;
    String password;

    ProgressDialog barProgressDialog;
    Handler updateBarHandler;

    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        linkDB=new DBManager(context);

        setContentView(R.layout.layout_login);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        layout_loading = (RelativeLayout) findViewById(R.id.layout_loading);
        layout_loading.setVisibility(View.GONE);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Resources res = getResources();
        int verde = res.getColor(R.color.PrimaryColor);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.getIndeterminateDrawable().setColorFilter(verde, PorterDuff.Mode.MULTIPLY);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Descargando...");
        dialog.setTitle("Progreso");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);

        dialog_login = new ProgressDialog(this);
        dialog_login.setMessage("Verificando, espere...");
        dialog_login.setTitle("Acceso Cium");
        dialog_login.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog_login.setCancelable(true);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_48dp);
        ///toolbar.setTitle(" CIUM - Acceso");
        getSupportActionBar().setTitle(" CIUM");
        //toolbar.setSubtitle("Sub");
        toolbar.setLogo(R.drawable.logociumandroid32);




        campo_usuario = (EditText) findViewById(R.id.input_usuario);
        campo_password= (EditText) findViewById(R.id.input_password);

        boton_accesar= (Button) findViewById(R.id.boton_login);
        boton_accesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                link.ocultarTeclado(getApplicationContext(),campo_usuario);
                String user=null;
                String pass=null;
                int errors=0;

                user=campo_usuario.getText().toString().trim();
                pass=campo_password.getText().toString().trim();

                if(user.equals("") || user==null)
                {
                    link.showSnackBar(getApplicationContext(), coordinatorLayout, "Ingrese todos sus datos", "ERROR");
                    errors++;
                }
                if(pass.equals("") || pass==null) {
                    link.showSnackBar(getApplicationContext(), coordinatorLayout,"Ingrese todos sus datos","ERROR");
                    errors++;
                }
                if(errors>0)
                {

                }else{
                         boton_accesar.setEnabled(false);
                         layout_loading.setVisibility(View.VISIBLE);
                         new HiloLogin().execute(user, pass);
                     }


            }
        });



/////////////////////                     C H E C K       V E R S I O N
////***************************************************************************************************************************************************
///****************************************************************************************************************************************************

        Version esta_version = new Version(0,"", 1.6, 1, "Opciones de resincronización",1,"2018-03-12 13:55:00","0000-00-00 00:00:00","0000-00-00 00:00:00");


        Version version_actual= linkDB.getVersionInstalada();

        //linkDB.vaciarVersiones();
        //linkDB.insertarVersion( esta_version,linkDB.openDB());

        if(version_actual==null)
        {
            linkDB.insertarVersion( esta_version,linkDB.openDB());
        }else{

                   if(esta_version.getVersionApp() > version_actual.getVersionApp())
                   {
                       linkDB.vaciarVersiones();
                       linkDB.insertarVersion( esta_version,linkDB.openDB());
                   }

             }

///****************************************************************************************************************************************************
//*****************************************************************************************************************************************************



        /// DETECTAMOS EN LA BD LOCAL SI EXISTE ALGUIÉN LOGUEADO

        Usuarios user_logued = linkDB.getSignedUser();

        if(user_logued==null)
        {


        }else{
               Intent lanzador = new Intent(Login.this, Home.class);
               Login.this.startActivity(lanzador);
               finish();
             }




        /// FIN REVISION USUARIO LOGUEADO

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.menu_login2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

            return true;
    }

    ///***********  F U N C I O N E S  **********************************


    public  void showSnackBar(String message)
    {

        final View viewPos = findViewById(R.id.coordinatorLayout);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.GRAY);
        snackbar.show();

    }

    ////*********** GET ******************


    /// *********** F I N     F U N C I O N E S  ****************************************


    private class HiloLogin extends AsyncTask<String, Integer, JSONObject>
    {
        private String email_login=null;
        private String access_token=null;
        private String refresh_token=null;

        private int id=0;
        private String nombre=null;
        private String apellidoPaterno=null;
        private String apellidoMaterno=null;
        private String avatar=null;

        private String json_login_string=null;
        private String json_perfil_string=null;

        private JSONObject json_signin=null;
        private JSONObject json_perfil=null;
        private JSONObject json_send=null;

        private JSONObject json_data_perfil=null;

        private int errors=0;
        private String error_messages="";

        Usuarios user=null;


        protected JSONObject doInBackground(String... datos)
        {
            email_login=datos[0].toString();

            try {

                  json_send = new JSONObject();
                  json_login_string = link.getLogin(datos[0].toString(), datos[1].toString());

                link.printConsola("JSON RESPONSE OF LOGIN   : "+json_login_string);


                } catch (Exception e) {
                                         e.printStackTrace();
                                      }

            if(json_login_string!=null)
            {
                try {

                    json_signin = new JSONObject(json_login_string);


                    access_token=json_signin.getString("access_token");
                    refresh_token=json_signin.getString("refresh_token");

                    json_send.put("email",email_login);
                    json_send.put("access_token",access_token);
                    json_send.put("refresh_token",refresh_token);

                    json_perfil_string = link.getPerfil(email_login, access_token);

                    link.printConsola("JSON RESPONSE OF PERFIL : "+json_perfil_string);


                    if(json_perfil_string!=null)
                     {

                         json_perfil = new JSONObject(json_perfil_string);

                         json_data_perfil = json_perfil.getJSONObject("data");

                         id=json_data_perfil.getInt("id");



                         nombre=json_data_perfil.getString("nombre");
                         apellidoPaterno=json_data_perfil.getString("apellido_paterno");
                         apellidoMaterno=json_data_perfil.getString("apellido_materno");


                         ////******************************************************************************************
                         ////    FAKE BLOCK
/*
                         nombre=json_data_perfil.getString("first_name");
                         apellidoPaterno=json_data_perfil.getString("last_name");
                         apellidoMaterno=" ";

*/
                         //*****************************************************************************************

                         avatar=json_data_perfil.getString("avatar");

                         json_send.put("id",id);
                         json_send.put("nombre",nombre);
                         json_send.put("apellidoPaterno",apellidoPaterno);
                         json_send.put("apellidoMaterno",apellidoMaterno);
                         json_send.put("avatar",avatar);

                         SQLiteDatabase db=linkDB.openDB();

                         /// 55/api/v1/Usuario

                         /*

                                http://187.217.219.55/cium/api/v1/Usuario?pagina=1&limite=25&columna=undefined&valor=jorp24@gmail.com&buscar=true&order=id
                          */

                         Usuarios user_existe=linkDB.getUser(email_login);

                         String parametro="?pagina=1&query="+email_login.toUpperCase();

                         JSONObject json_user;
                         int id_user_api = 0;

                        try {


                                String json_usuario = link.getCatalogo("usuarios", "GET", parametro, email_login, access_token);
                                System.out.println("JSON : "+json_usuario);

                                if (json_usuario != null)
                                {
                                    json_user = new JSONObject(json_usuario);

                                    //int status = json_user.getInt("status");
                                    //String msg = json_user.getString("messages");

                                    JSONArray data = json_user.getJSONArray("data");

                                    for (int z = 0; z < data.length(); z++)
                                    {
                                        JSONObject row_user_api = data.getJSONObject(z);
                                        id_user_api = row_user_api.getInt("id");

                                        System.out.println("USUARIO :"+email_login+",ID perfil: "+id+", ID (DEL REC. USUARIO):"+id_user_api+".");
                                    }

                                }
                             }catch (JSONException err){ err.printStackTrace();}

                         user=new Usuarios(id_user_api,"",email_login,nombre,apellidoPaterno,apellidoMaterno,avatar,1,access_token,refresh_token);


                         if(user_existe==null)
                         {
                             linkDB.insertarUsuario(user,db);
                         }else{
                                linkDB.actualizarUsuario(user,db);
                              }



                     }else{
                             errors++;
                             error_messages+="Error en perfil de usuario.\n";
                          }


                    } catch (JSONException e) {

                                                errors++;
                                                error_messages+="Error en autenticación.\n";
                                                e.printStackTrace();

                                              } catch (Exception e) {
                                                                            errors++;
                                                                            error_messages+="Error en autenticación.\n";
                                                                            e.printStackTrace();
                                                                      }


            }else {
                    errors++;
                    error_messages="Error de credenciales.\n";
                  }

            try {
                  json_send.put("errors",errors);
                  json_send.put("error_messages",error_messages);
                } catch (JSONException e) {
                                            e.printStackTrace();
                                          }




            return json_send;
        }

        protected void onProgressUpdate(Integer... values)
        {
            int progreso = values[0].intValue();
            dialog_login.setProgress(progreso);
        }

        protected void onPreExecute()
        {

            dialog_login.setOnCancelListener(new OnCancelListener()
            {
                public void onCancel(DialogInterface dialog)
                {
                    HiloLogin.this.cancel(true);
                }
            });

            //dialog_login.setProgress(0);
            //dialog_login.show();
        }

        protected void onPostExecute(JSONObject json) {

               String access_token="";
               String refresh_token="";

               int errors=0;
               String messages="";



            try {

                        errors=json.getInt("errors");
                        messages=json.getString("error_messages");

                        Perfil user_signed= new Perfil(json.getInt("id"),
                                                        json.getString("nombre"),
                                                        json.getString("email"),
                                                        json.getString("apellidoPaterno"),
                                                        json.getString("apellidoMaterno"),
                                                        json.getString("avatar"),
                                                        json.getString("access_token"),
                                                        json.getString("refresh_token"));


                        if(errors<=0)
                        {
                            //dialog_login.dismiss();
                            layout_loading.setVisibility(View.GONE);
                            lanzarHome(json);
                        }else{
                               //dialog_login.dismiss();
                               layout_loading.setVisibility(View.GONE);
                               boton_accesar.setEnabled(true);
                               link.showSnackBar(getApplicationContext(), coordinatorLayout, messages, "ERROR");
                             }

                    } catch (JSONException e) {
                        e.printStackTrace();
                         //dialog_login.dismiss();
                         layout_loading.setVisibility(View.GONE);
                         boton_accesar.setEnabled(true);
                         link.showSnackBar(getApplicationContext(),coordinatorLayout,"Error accesando","ERROR");

                    }



        }


        protected void onCancelled() {
            showSnackBar("Login cancelado.");
        }
    }///FIN CLASS DESCARGA ASYNC



    public void lanzarHome(JSONObject user)
    {
       showSnackBar("INICIANDO HOME DESDE LOGIN REMOTO...");
      /*
        try {
            printToast("EMAIL SEND: " + user.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

     */
        Intent lanzador = new Intent(Login.this, Home.class);
        lanzador.putExtra( "user_signed", user.toString());

        Login.this.startActivity(lanzador);
        finish();

    }









} /// CLASS  LOGIN2

