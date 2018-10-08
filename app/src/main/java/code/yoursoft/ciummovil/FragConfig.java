package code.yoursoft.ciummovil;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FragConfig extends Fragment
{


    ImageButton boton_check;
    private ProgressDialog dialog_descarga;


    TextView text_status;
    TextView text_version_actual;

   TextView titulo_nueva_version ;
    TextView text_nueva_version ;




    private CoordinatorLayout coordinatorLayout;
    private RelativeLayout layout_loading;

    private RelativeLayout layout_nueva_version;




    ProgressBar progress_bar_check;



    private Usuarios user_signed;
    private DBManager linkDB;
    private Funciones link;


    private boolean  mostrar_dialog;

    AlertDialog.Builder dialog;
    Button boton_ok;
    Context context;

    Button boton_actualizar;
    Version nueva_version_encontrada=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        final View rootView = inflater.inflate(R.layout.layout_config, container, false);

        linkDB=new DBManager(getActivity());
        link=new Funciones();
        user_signed=linkDB.getSignedUser();

        if(user_signed==null)
        {
            link.goLogin(getActivity());
        }

        context = getActivity();

        boton_check = (ImageButton) rootView.findViewById(R.id.boton_checar_actualizaciones);



        dialog_descarga = new ProgressDialog(getActivity());
        dialog_descarga.setMessage("Actualizando datos, espere...");
        dialog_descarga.setTitle("Actualizacion");
        dialog_descarga.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog_descarga.setCancelable(false);


        progress_bar_check = (ProgressBar) rootView.findViewById(R.id.progress_bar_check);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);

        layout_loading = (RelativeLayout) rootView.findViewById(R.id.layout_loading);
        layout_loading.setVisibility(View.GONE);

        layout_nueva_version = (RelativeLayout) rootView.findViewById(R.id.layout_nueva_version);
        layout_nueva_version.setVisibility(View.GONE);

        boton_actualizar = (Button) rootView.findViewById(R.id.boton_actualizar_cium);


        Resources res = getResources();
        int verde = res.getColor(R.color.PrimaryColor);



        text_status = (TextView) rootView.findViewById(R.id.text_status);
        text_version_actual = (TextView) rootView.findViewById(R.id.text_version_actual);


        titulo_nueva_version = (TextView) rootView.findViewById(R.id.titulo_nueva_version);
        text_nueva_version = (TextView) rootView.findViewById(R.id.text_nueva_version);





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




        boton_check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ///*************************************************************************************************
                morphingBoton("BUSCAR", "BUSCANDO");
                desactivarBotones();


                new BuscarActualizaciones().execute();
                ///*************************************************************************************************



            }
        });

        boton_actualizar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ///*************************************************************************************************
                AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() ,R.style.TemaDialogMaterial);

                builder.setCancelable(false);
                builder.setMessage("¿ Esta seguro que desea actualizar CIUM Movil?")
                        .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                morphingBoton("ACTUALIZAR", "ACTUALIZANDO APP");
                                desactivarBotones();

                                if(nueva_version_encontrada!=null)
                                {
                                    new DescargarUpdate().execute("http://api.cium.saludchiapas.gob.mx"+nueva_version_encontrada.getPath());
                                }else {
                                            morphingBoton("ACTUALIZAR", "NORMAL");
                                            desactivarBotones();
                                            lanzarMensaje("No hay nueva versión disponible.");
                                      }

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,int id)
                            {
                                dialog.cancel();
                            }
                        }).show();
                ///*************************************************************************************************

            }
        });






        setVersion();


        return rootView;
    }




    public void setVersion()
    {
        String version_actual="";

        Version version = linkDB.getVersionInstalada();

        if(version != null)
        {
            version_actual="Version Instalada: "+version.getVersionApp()+".\nPublicada el "+link.imprimirFecha(version.getCreadoAl()+".\nInfo: "+version.getDescripcion()+".");

        }else{
                 version_actual="Version Instalada: Unknown.";
             }

        text_version_actual.setText(version_actual);
    }



    private class BuscarActualizaciones extends AsyncTask<String, Integer, Version>
    {

        Funciones link = new Funciones();

        String json_versiones_string=null;
        JSONObject json_versiones = new JSONObject();

        int errors=0;
        String messages="";


        protected Version doInBackground(String... datos)
        {

            Version nueva_version = null ;

            try {

                     json_versiones_string=link.getCatalogo("VersionApp","GET","",user_signed.getEmail(),user_signed.accessToken);

                     if(json_versiones_string!=null)
                     {
                         json_versiones = new JSONObject(json_versiones_string);

                         JSONArray array_versiones = json_versiones.getJSONArray("data");

                         Version version_instalada = linkDB.getVersionInstalada();

                         for (int i=0; i<array_versiones.length();i++)
                         {
                                 JSONObject version_json=array_versiones.getJSONObject(i);

                                 Version version_temp=new Version(version_json.getInt("id"),
                                                                  version_json.getString("path"),
                                                                  version_json.getDouble("versionApp"),
                                                                  version_json.getInt("versionDB"),
                                                                  version_json.getString("descripcion"),
                                                                  0,
                                                                  version_json.getString("creadoAl"),
                                                                  version_json.getString("modificadoAl"),
                                                                  version_json.getString("borradoAl")
                                                                 );
                             if(version_instalada!=null)
                             {
                                 if(version_instalada.getVersionApp()<version_temp.getVersionApp())
                                 {
                                     nueva_version = version_temp;
                                 }
                             }

                         }

                     }else {
                             errors++;
                           }


                } catch (Exception e) {
                                        e.printStackTrace();
                                      }

            return nueva_version;
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

        protected void onPostExecute(Version version)
        {

            if(version!=null)
            {
                nueva_version_encontrada = version;

                layout_nueva_version.setVisibility(View.VISIBLE);

                titulo_nueva_version.setText("Nueva Version : ["+version.getVersionApp()+"]");
                text_nueva_version.setText("Publicada el: "+version.getCreadoAl()+",\n Novedades: "+version.getDescripcion()+".");

            }else {

                    lanzarMensaje("No se encontró nueva versión.");


                  }

            morphingBoton("BUSCAR","NORMAL");
            activarBotones();


        }

        protected void onCancelled(String mensaje)
        {
            link.showSnackBar(getActivity(), coordinatorLayout, "Actualizacion cancelada.", "ERROR");
        }
    }///FIN CLASS DESCARGA ASYNC






    private class DescargarUpdate extends AsyncTask<String, Integer, String>
    {

        Funciones link = new Funciones();

        int errors=0;
        String messages="";


        protected String doInBackground(String... datos)
        {
            String path = "/sdcard/CIUM.apk";
            try {
                URL url = new URL(datos[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(path);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1)
                {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("YourApp", "Well that didn't work out so well...");
                Log.e("YourApp", e.getMessage());
            }
            return path;
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

        protected  void onPostExecute(String path)
        {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive" );
            Log.d("Lofting", "About to install new .apk");
            /// this.context.startActivity(i);
            ///Intent intent = new Intent(getActivity(), FragConfig.class);
            startActivity(i);

        }

        protected void onCancelled(String mensaje)
        {
            link.showSnackBar(getActivity(), coordinatorLayout, "Actualizacion cancelada.", "ERROR");
        }
    }///FIN CLASS DESCARGA ASYNC









    public void activarBotones()
    {
        boton_check.setEnabled(true);
        boton_actualizar.setEnabled(true);

    }


    public void desactivarBotones()
    {
        boton_check.setEnabled(false);
        boton_actualizar.setEnabled(false);
    }



    public void morphingBoton(String boton, String estado)
    {

        if(boton.equals("BUSCAR"))
        {
            if(estado.equals("NORMAL"))
            {
                boton_check.setVisibility(View.VISIBLE);
                progress_bar_check.setVisibility(View.GONE);

                setVersion();

            }else{
                    boton_check.setVisibility(View.GONE);
                    progress_bar_check.setVisibility(View.VISIBLE);

                    text_version_actual.setText("Buscando actualizaciones...");

                 }
        }


        if(boton.equals("ACTUALIZAR"))
        {
            if(estado.equals("NORMAL"))
            {
                boton_check.setVisibility(View.VISIBLE);
                progress_bar_check.setVisibility(View.GONE);

            }else{
                boton_check.setVisibility(View.GONE);
                progress_bar_check.setVisibility(View.VISIBLE);

                text_version_actual.setText("Actualizando CIUM ...");

            }
        }



    }///FIN MORPHING BOTON




    public void lanzarMensaje(String mensaje)
    {

        Snackbar sb =link.getSnackBar(context,coordinatorLayout,mensaje,"ERROR");
        sb.show();

    }

}  ///FIN FRAGMENT



