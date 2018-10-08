package code.yoursoft.ciummovil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by macbookpro on 18/08/15.
 */

public class Funciones
{

    public String URL_OAUTH = "http://187.217.219.54/oauth/access_token";
    //public String URL_OAUTH = "http://192.168.1.18/SSA_MATERIAL/oauth2-server/public";


  //public String URL_SIGNIN = "http://api.cium.saludchiapas.gob.mx/signin";///"http://187.217.219.55/signin";  //api.cium.saludchiapas.gob.mx/

    public String URL_SIGNIN = "http://api.cium.saludchiapas.gob.mx/signin";
    //public String URL_SIGNIN = "http://192.168.1.18/Cium_2017/APIRESTfull/public//signin";


    public String URL_PERFIL = "http://187.217.219.54/v1/perfil";
    //public String URL_PERFIL = "http://192.168.1.18/SSA_MATERIAL/oauth2-server/public/v1/perfil";


    public String URL_API = "http://api.cium.saludchiapas.gob.mx/v1";
    //public String URL_API = "http://192.168.1.18/Cium_2017/APIRESTfull/public/";


    //String url = "http://192.168.1.8/SSA_MATERIAL/APIRESTfull/public/api/v1/EvaluacionRecurso";


    ///************** GET TOKEN ******************
    public String getToken(String usuario,String password)
    {
        URL url;
        HttpURLConnection connection = null;

        String parametros_token="";

        try {

            parametros_token = "grant_type=" + URLEncoder.encode("password", "UTF-8") +
                    "&client_id=" + URLEncoder.encode("1324576890", "UTF-8") +
                    "&client_secret=" + URLEncoder.encode("0897645321", "UTF-8") +
                    "&username=" + URLEncoder.encode(usuario, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");




        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            url = new URL(URL_OAUTH);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametros_token.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
            wr.writeBytes (parametros_token);
            wr.flush ();
            wr.close ();

            InputStream is = connection.getInputStream();
            String json_string = inputstreamToString(is);

            String token="";

            try {

                JSONObject jsonRootObject = new JSONObject(json_string);
                token=jsonRootObject.getString("access_token");

                } catch (JSONException e) {
                                            e.printStackTrace();
                                          }


            //return token;
            return json_string;

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection    != null) {
                connection.disconnect();
            }
        }
    }
    ///**************FIN GET TOKEN****

    ///************** GET LOGIN ******************
    public String getLogin(String email,String password)
    {
        URL url;
        HttpURLConnection connection = null;

         String parametros_login="";

        try {
                parametros_login = "&email=" + URLEncoder.encode(email, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                                                       e.printStackTrace();
                                                     } catch (IOException e) {
                                                                              e.printStackTrace();
                                                                             }
        try {

                url = new URL(URL_SIGNIN);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                connection.setRequestProperty("Content-Length", "" + Integer.toString(parametros_login.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches (false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //connection.connect();

                DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
                wr.writeBytes(parametros_login);

                wr.flush ();
                wr.close ();

                InputStream is = connection.getInputStream();
                String json_string = inputstreamToString(is);

                String token="";

                try {
                       JSONObject jsonRootObject = new JSONObject(json_string);
                       token=jsonRootObject.getString("access_token");
                    } catch (JSONException e) {
                                                 e.printStackTrace();
                                              }

                //return token;
                return json_string;

             } catch (Exception e) {
                                        e.printStackTrace();
                                        return null;
                                    } finally {

            if(connection    != null) {
                connection.disconnect();
            }
        }
    }
    ///**************FIN GET LOGIN****

    public void checkLogin(Context context)
    {
        Usuarios user=null;

    }

    public void goLogin(Context context)
    {
        Intent lanzador = new Intent(context, Login.class);
        //Login.this.startActivity(lanzador);
        context.startActivity(lanzador);
    }



    ///************** GET PERFIL ******************




    public String getPerfil(String email,String acces_token) throws Exception
    {
         URL obj = new URL(URL_PERFIL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + acces_token);
        con.setRequestProperty("X-Usuario", email);

        int responseCode = con.getResponseCode();

        System.out.println("Response Code : " + responseCode);
        String imp= "MENSAJE RESPUESTA CODE: "+con.getResponseMessage();

        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    ///**************FIN GET PERFIL****




    public String getCatalogo(String path,String metodo, String parametro,String email_user,String acces_token) throws Exception
    {
        String url2 = URL_API+"/"+path+""+parametro;

        printConsola("URL: ["+url2+"]");
        URL obj = new URL(url2);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod(metodo);
        con.setRequestProperty("Authorization", "Bearer " + acces_token);
        con.setRequestProperty("X-Usuario", email_user);

        int responseCode = con.getResponseCode();

        System.out.println("Response Code : " + responseCode);
        String imp= "MENSAJE RESPUESTA CODE: "+con.getResponseMessage();

        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }///fin descargar catalogo






///*******************************************************************************************************

    public String postEvaluacionesRecurso(String evaluaciones,String email_user,String acces_token)
    {

        System.out.println("JSON A ENVIAR : "+evaluaciones.toString());

        StringBuffer response;
        try{

                    String url = URL_API+"/"+"EvaluacionRecurso";
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "application/json");
                    con.setRequestProperty("Authorization", "Bearer "+acces_token);
                    con.setRequestProperty("X-Usuario", ""+email_user);
                    con.setRequestProperty("X-Movil", ""+1);
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    con.connect();

                    byte[] outputBytes = evaluaciones.getBytes("UTF-8");
                    OutputStream os = con.getOutputStream();
                    os.write(outputBytes);
                    os.close();

                    int responseCode = con.getResponseCode();

                    System.out.println("RESPONSE CODE : " + responseCode);
                    System.out.println("MENSAJE RESPUESTA : "+con.getResponseMessage());


                    if(responseCode <= 202)
                    {
                        System.out.println("COLECTANDO MENSAJE DEL SERVER... ");
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;

                        response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null)
                        {
                            response.append(inputLine);
                        }
                        in.close();


                        System.out.println("FIN RECOLECCION MENSAJE DEL SERVER: "+response.toString()+" .");

                    }else{
                           System.out.println("REGRESANDO NULL POR CODE ERROR... ");
                        System.out.println("ERROR MESSAGE... "+con.getResponseMessage());
                           return  null;
                         }

                    System.out.println("RESPUESTA DEL SERVIDOR : [[  " + response.toString() + " ]]");



        } catch (IOException e) {
                                    System.out.println("REGRESANDO NULL POR EXCEPTION ERROR... ");
                                    e.printStackTrace();
                                    return null;
                                }
        System.out.println("REGRESANDO TODO OK DEL SERVER... ");
        return response.toString();
    }///fin descargar catalogo


///*******************************************************************************************************

///*******************************************************************************************************

    public String postEvaluacionesCalidad(String evaluaciones,String email_user,String acces_token)
    {

        System.out.println("JSON A ENVIAR : "+evaluaciones.toString());

        StringBuffer response;
        try{

            String url = URL_API+"/"+"EvaluacionCalidad";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer "+acces_token);
            con.setRequestProperty("X-Usuario", email_user);
            con.setRequestProperty("X-Movil", ""+1);
            con.setDoInput(true);
            con.setDoOutput(true);

            con.connect();



            byte[] outputBytes = evaluaciones.getBytes("UTF-8");
            OutputStream os = con.getOutputStream();
            os.write(outputBytes);
            os.close();

            int responseCode = con.getResponseCode();

            System.out.println("Response Code : " + responseCode);
            System.out.println("MENSAJE RESPUESTA CODE: "+con.getResponseMessage());


            if(responseCode <= 202)
            {
                System.out.println("COLECTANDO MENSAJE DEL SERVER... ");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("FIN RECOLECCION MENSAJE DEL SERVER... ");
            }else{
                System.out.println("REGRESANDO NULL POR CODE ERROR... ");
                return  null;
            }

            System.out.println("RESPUESTA DEL SERVIDOR : [[  " + response.toString() + " ]]");



        } catch (IOException e) {
            System.out.println("REGRESANDO NULL POR EXCEPTION ERROR... ");
            e.printStackTrace();
            return null;
        }
        System.out.println("REGRESANDO TODO OK DEL SERVER... ");
        return response.toString();
    }///fin descargar catalogo


///*******************************************************************************************************
///*******************************************************************************************************

    public String rePostEvaluacionesRecurso(String evaluaciones,String email_user,String acces_token)
    {

        System.out.println("JSON A ENVIAR : "+evaluaciones.toString());

        StringBuffer response;
        try{

            String url = URL_API+"/"+"RecursoResincronizacion";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer "+acces_token);
            con.setRequestProperty("X-Usuario", ""+email_user);
            con.setRequestProperty("X-Movil", ""+1);
            con.setDoInput(true);
            con.setDoOutput(true);

            con.connect();

            byte[] outputBytes = evaluaciones.getBytes("UTF-8");
            OutputStream os = con.getOutputStream();
            os.write(outputBytes);
            os.close();

            int responseCode = con.getResponseCode();

            System.out.println("RESPONSE CODE : " + responseCode);
            System.out.println("MENSAJE RESPUESTA : "+con.getResponseMessage());


            if(responseCode <= 202)
            {
                System.out.println("COLECTANDO MENSAJE DEL SERVER... ");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                in.close();


                System.out.println("FIN RECOLECCION MENSAJE DEL SERVER: "+response.toString()+" .");

            }else{
                System.out.println("REGRESANDO NULL POR CODE ERROR... ");
                System.out.println("ERROR MESSAGE... "+con.getResponseMessage());
                return  null;
            }

            System.out.println("RESPUESTA DEL SERVIDOR : [[  " + response.toString() + " ]]");



        } catch (IOException e) {
            System.out.println("REGRESANDO NULL POR EXCEPTION ERROR... ");
            e.printStackTrace();
            return null;
        }
        System.out.println("REGRESANDO TODO OK DEL SERVER... ");
        return response.toString();
    }///fin descargar catalogo


///*******************************************************************************************************

///*******************************************************************************************************

    public String rePostEvaluacionesCalidad(String evaluaciones,String email_user,String acces_token)
    {

        System.out.println("JSON A ENVIAR CALIDAD : "+evaluaciones.toString());

        StringBuffer response;
        try{

            String url = URL_API+"/"+"CalidadResincronizacion";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer "+acces_token);
            con.setRequestProperty("X-Usuario", email_user);
            con.setRequestProperty("X-Movil", ""+1);
            con.setDoInput(true);
            con.setDoOutput(true);

            con.connect();



            byte[] outputBytes = evaluaciones.getBytes("UTF-8");
            OutputStream os = con.getOutputStream();
            os.write(outputBytes);
            os.close();

            int responseCode = con.getResponseCode();

            System.out.println("Response Code : " + responseCode);
            System.out.println("MENSAJE RESPUESTA CODE: "+con.getResponseMessage());


            if(responseCode <= 202)
            {
                System.out.println("COLECTANDO MENSAJE DEL SERVER... ");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("FIN RECOLECCION MENSAJE DEL SERVER... ");
            }else{
                System.out.println("REGRESANDO NULL POR CODE ERROR... ");
                return  null;
            }

            System.out.println("RESPUESTA DEL SERVIDOR : [[  " + response.toString() + " ]]");



        } catch (IOException e) {
            System.out.println("REGRESANDO NULL POR EXCEPTION ERROR... ");
            e.printStackTrace();
            return null;
        }
        System.out.println("REGRESANDO TODO OK DEL SERVER... ");
        return response.toString();
    }///fin descargar catalogo


///*******************************************************************************************************














    public String putEvaluacionesRecurso(String evaluaciones,String email_user,String acces_token) throws Exception
    {

        System.out.println("JSON A ENVIAR : "+evaluaciones.toString());
        StringBuffer response;

        try{
                String url = URL_API+"/"+"EvaluacionRecurso";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "Bearer 0sCjjP4Xuvs07Uc4ZQzgT72jNHET3hbSL1sJN4DN");
                con.setRequestProperty("X-Usuario", "ramirez.esquinca@gmail.com");
                con.setDoInput(true);
                con.setDoOutput(true);

                con.connect();

                byte[] outputBytes = evaluaciones.getBytes("UTF-8");
                OutputStream os = con.getOutputStream();
                os.write(outputBytes);
                os.close();

                int responseCode = con.getResponseCode();

                System.out.println("Response Code : " + responseCode);
                System.out.println("MENSAJE RESPUESTA CODE: "+con.getResponseMessage());

                if(responseCode <= 202 )
                {

                    BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }
                    in.close();
                    System.out.println("COLECTANDO MENSAJE DEL SERVER ");

                }else{
                        System.out.println("REGRESANDO NULL POR CODE ERROR... ");
                        return  null;
                     }

                System.out.println("RESPUESTA DEL SERVIDOR : [[  "+response.toString()+" ]]");


        } catch (IOException e) {
                                    e.printStackTrace();
                                    System.out.println("REGRESANDO NULL POR EXCEPTION ERROR... ");
                                    return null;
                                }

        return response.toString();
    }///fin descargar catalogo





///*******************************************************************************************************
/*
    public String enviarEvaluacionRecurso(String usuario,String evaluaciones,String acces_token,String refresh_token)
    {
        URL url;
        HttpURLConnection connection = null;

        String targetURL= "http://187.217.219.55/cium/api/v1/EvaluacionRecurso/";
        String parametros_token="";

        try {
                    parametros_token = "Authorization=" + URLEncoder.encode("Bearer "+acces_token, "UTF-8") +
                                        "&X-Usuario=" + URLEncoder.encode(usuario, "UTF-8");

             } catch (UnsupportedEncodingException e) {
                                                         e.printStackTrace();
                                                       } catch (IOException e) {
                                                                                 e.printStackTrace();
                                                                               }


        try {

            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            //connection.setRequestProperty("Content-Length", "" + Integer.toString(parametros_token.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            //connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());

            wr.writeBytes(parametros_token);


            JSONObject json_body = new JSONObject();
            json_body.put("evaluaciones", evaluaciones);



            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(json_body.toString());
            writer.close();
            outputStream.close();


            //String string_temp= json_body.toString();
            //byte [] data = string_temp.getBytes("UTF-8");

            //wr.write(data);
                        //wr.writeBytes(json_body);

            wr.flush ();
            wr.close();


            //InputStream is = connection.getInputStream();
            //String json_string = inputstreamToString(is);




            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null)
            {
                response.append(line);
                response.append('\r');

            }
            rd.close();



            System.out.println(" RESPUESTA DEL SERVIDOR :"+response.toString());





            return response.toString();

        } catch (Exception e) {
                                    e.printStackTrace();
                                    return null;
                              } finally{
                                             if(connection    != null) {  connection.disconnect();  }
                                       }
    }
*/

/////**************************************************************************************************

/*
    public String enviarEvaluacionesRecurso2(String usuario,String evaluaciones,String acces_token,String refresh_token)
    {

        try {

            URL url = new URL("http://187.217.219.55/cium/api/v1/EvaluacionRecurso"
                    + "&Authorization=Bearer "+acces_token
                    + "&X-Usuario="+usuario);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            //String input = "{ \"snippet\": {\"playlistId\": \"WL\",\"resourceId\": {\"videoId\": \""+videoId+"\",\"kind\": \"youtube#video\"},\"position\": 0}}";

            String input = evaluaciones.toString();

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }


      return "-----";

    }

*/

 /*

    public void enviarEvaluacionRecurso3(String usuario,String evaluaciones,String acces_token,String refresh_token) throws IOException {


        String otherParametersUrServiceNeed =  "Authorization=Bearer "+acces_token+"&X-Usuario="+usuario;
        String request = "http://187.217.219.55/cium/api/v1/EvaluacionRecurso";

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        ///connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
        connection.setUseCaches (false);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
        wr.writeBytes(otherParametersUrServiceNeed);

        JSONObject jsonParam = new JSONObject();
        try {
                jsonParam.put("evaluaciones", evaluaciones);

            } catch (JSONException e) {
                                          e.printStackTrace();
                                       }

        wr.writeBytes(jsonParam.toString());

        wr.flush();
        wr.close();


    }

    public void enviarEvaluacionRecurso4(String usuario,String evaluaciones,String acces_token,String refresh_token)
    {

        String parametros_token="";

        try {
            parametros_token = "Authorization=" + URLEncoder.encode("Bearer "+acces_token, "UTF-8") +
                    "&X-Usuario=" + URLEncoder.encode(usuario, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpURLConnection client = null;
        try {

            URL url = new URL("http://187.217.219.55/cium/api/v1/EvaluacionRecurso");
            client = (HttpURLConnection) url.openConnection();
            client.setDoOutput(true);
            client.setDoInput(true);
            client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            client.setRequestProperty("Authorization","Bearer "+acces_token);
            client.setRequestProperty("X-Usuario", usuario);
            //client.setRequestProperty("Content-Length", "" + Integer.toString(parametros_token.getBytes().length));

            client.setRequestMethod("POST");
            //client.setFixedLengthStreamingMode(request.toString().getBytes("UTF-8").length);
            client.connect();

            Log.d("doInBackground(Request)", evaluaciones.toString());

            OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream());
            String output = evaluaciones.toString();
            writer.write(output);
            writer.flush();
            writer.close();

            InputStream input = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            Log.d("doInBackground(Resp)", result.toString());
            JSONObject response = new JSONObject(result.toString());
            System.out.println("RESPUESTA SERVER : [[ "+response.toString()+" ]]");


        } catch (JSONException e) {
            e.printStackTrace();
            //this.e = e;
        } catch (IOException e) {
            e.printStackTrace();
            //this.e = e;
        } finally {

            if (client != null) {
                client.disconnect();
            }
        }

    }



*/



////******************************************************************************************************


    public List<EvaluacionDetalles> listarBusquedaEvaluaciones(String tipoEvaluacion,String buscar,int id_usuario,Context context)
    {
        List<EvaluacionDetalles> evaluaciones_detalles = new ArrayList<>();
        DBManager linkDB=new DBManager(context);
        int dim=0;


        List<Evaluacion> evaluaciones=linkDB.getEvaluaciones(tipoEvaluacion,buscar,id_usuario);



        dim=evaluaciones.size();

        for(int i=0; i<evaluaciones.size();i++)
        {
            Evaluacion item_er=evaluaciones.get(i);
            Evaluacion eval_temp = evaluaciones.get(i);

            Clues clues_temp=linkDB.getClues(item_er.clues);

            /// GET DETALLES DE EVALUACION (INDICADORES AGREGADOS,CRITERIOS )

            int indicadores=0;
            int expedientes=0;
            int criterios=0;
            int respondidas=0;

            if(tipoEvaluacion.equals("RECURSO"))
            {

                List<Indicador> indicadores_agregados_recurso = linkDB.getIndicadoresAgregadosRecurso(eval_temp.getId());
                List<LugarVerificacion> listaLV = new ArrayList<>();
                List<Criterio> listaC = new ArrayList<>();

                for(int p=0; p<indicadores_agregados_recurso.size(); p++)
                {
                    listaLV = linkDB.getLugaresVerificacion(clues_temp.getIdCone(), indicadores_agregados_recurso.get(p).getId() );

                    for(int j=0; j<listaLV.size(); j++)
                    {
                        listaC = linkDB.getCriterios(clues_temp.getIdCone(), indicadores_agregados_recurso.get(p).getId(), listaLV.get(j).getId());

                        for(int k=0; k<listaC.size(); k++)
                        {
                            EvaluacionRecursoCriterio eval_rc = linkDB.getEvaluacionRecursoCriterio(eval_temp.getId(), indicadores_agregados_recurso.get(p).getId(), listaC.get(k).getId());

                            if(eval_rc != null)
                            {
                                respondidas++;
                            }

                            criterios++;
                        }
                    }
                    indicadores++;
                }


            }else {

                        List<Indicador> indicadores_agregados_calidad = linkDB.getIndicadoresAgregadosCalidad(eval_temp.getId());
                        List<LugarVerificacion> listaLV = new ArrayList<>();
                        List<Criterio> listaC = new ArrayList<>();

                        for(int p=0; p<indicadores_agregados_calidad.size(); p++)
                        {
                            //GET EXPEDIENTE AGREGADOS AQUI Y HACER FOR PARA EMBEBER ESTO
                            List<EvaluacionCalidadRegistro> lista_expedientes = linkDB.getExpedientesEvaluacionCalidadRegistro(eval_temp.getId(),indicadores_agregados_calidad.get(p).getId());

                            for(int h=0; h<lista_expedientes.size(); h++)
                            {
                                criterios += lista_expedientes.get(h).totalCriterio;
                                respondidas += lista_expedientes.get(h).avanceCriterio;

                                expedientes++;
                            }

                            indicadores++;
                        }



            }// FIN ELSE ( EVALUACION ES DE TIPO CALIDAD)







            evaluaciones_detalles.add(new EvaluacionDetalles(
                                                                item_er.id,
                                                                item_er.idUsuario,
                                                                linkDB.getNombreUsuario(item_er.idUsuario),
                                                                item_er.clues,
                                                                clues_temp.nombre,
                                                                clues_temp.domicilio,
                                                                clues_temp.jurisdiccion,
                                                                clues_temp.municipio,
                                                                clues_temp.localidad,
                                                                item_er.fechaEvaluacion,
                                                                item_er.cerrado,
                                                                item_er.sincronizado,
                                                                item_er.getCompartido(),
                                                                item_er.getCompartidoFull(),
                                                                item_er.firma,
                                                                item_er.responsable,
                                                                                            indicadores,
                                                                                            expedientes,
                                                                                            criterios,
                                                                                            respondidas,
                                                                item_er.creadoAl,
                                                                item_er.modificadoAl,
                                                                item_er.borradoAl
                                                            )
                                     );
        }


        return evaluaciones_detalles;

/*
        if(dim<=0)
        {
            return null;
        }else {
            return evaluaciones_detalles;
        }
*/

    }



    public static GradientDrawable backgroundWithBorder(int bgcolor, int brdcolor)
    {

        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(bgcolor);
        gdDefault.setStroke(2, brdcolor);
        gdDefault.setCornerRadii(new float[]{1, 1, 0, 0, 0, 0, 1, 1});

        return gdDefault;

    }




    public void ocultarTeclado(Context context,EditText edit)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }





    public String getFecha()
    {
        return new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public String getFechaCorta()
    {
        return new SimpleDateFormat( "yyyy-MM-dd",
                java.util.Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public String getHoraSistema()
    {
        return new SimpleDateFormat( "HH:mm", java.util.Locale.getDefault()).format(Calendar.getInstance().getTime());
    }



    public String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    public Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    ///********************************    INPUTSTRING TO STRING  *****************************

    private static String inputstreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    ///************************ FIN INPUTSTRING TO STRING ************************************

    ////*********************** PARSE JSON  **************************************************
    public String parserJson(String json_string)
    {

        String token = "";
        System.out.println("JSON DEVUELTO:  "+json_string);
        try {
            JSONObject jsonRootObject = new JSONObject(json_string);

            token=jsonRootObject.getString("access_token");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return token;

    }
    ////*********************** FIN PARSE JSON ************************************************


    public void printToast( String msg, Context context)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }



    public String imprimirFecha(String fecha)
    {
        String fecha_mexicana="";

        String asd="2015-02-02 00:00:00:00";

        try {
              String mes_print="";
              int mes = Integer.parseInt(fecha.substring(5, 7));

                if(mes==0){mes_print="00";}
                if(mes==1){mes_print="Ene";}
                if(mes==2){mes_print="Feb";}
                if(mes==3){mes_print="Mar";}
                if(mes==4){mes_print="Abr";}
                if(mes==5){mes_print="May";}
                if(mes==6){mes_print="Jun";}
                if(mes==7){mes_print="Jul";}
                if(mes==8){mes_print="Ago";}
                if(mes==9){mes_print="Sep";}
                if(mes==10){mes_print="Oct";}
                if(mes==11){mes_print="Nov";}
                if(mes==12){mes_print="Dic";}


              fecha_mexicana = fecha.substring(8, 10) + "-" + mes_print + "-" + fecha.substring(0, 4) + " / " + fecha.substring(11, 19);
            }catch (NullPointerException err) {
                                                 return "---- -- --";
                                              }catch (IndexOutOfBoundsException err)
                                                                                {
                                                                                    return "---- -- --";
                                                                                }
        return fecha_mexicana;
    }
   // 504999906673


    public int sumarFechas(String fecha01, String fecha02)
    {
        int dias=0;
        int errors=0;

        int anio1, mes1, dia1, anio2, mes2, dia2;
        Calendar c = Calendar.getInstance();

        try{
            anio1 = Integer.parseInt(fecha01.substring(0,4));
            mes1 = Integer.parseInt(fecha01.substring(5,7));
            dia1 = Integer.parseInt(fecha01.substring(8,10));

            anio2 = Integer.parseInt(fecha02.substring(0,4));
            mes2 = Integer.parseInt(fecha02.substring(5,7));
            dia2 = Integer.parseInt(fecha02.substring(8,10));



            Calendar fecha1 = new GregorianCalendar();
            fecha1.set(anio1, mes1, dia1);


            Calendar fecha2 = new GregorianCalendar();
            fecha2.set(anio2, mes2, dia2);

            c.setTimeInMillis(fecha1.getTime().getTime() + fecha2.getTime().getTime());

            dias = c.get(Calendar.DAY_OF_YEAR);


            return dias;

        }catch (NumberFormatException err){
                                            errors++;
                                            return -1;
                                          }
    }


    public int restarFechas(String fecha01, String fecha02)
    {
        int diasDif=0;
        int errors=0;

        int anio1, mes1, dia1, anio2, mes2, dia2;
        Calendar c = Calendar.getInstance();

        try{
                anio1 = Integer.parseInt(fecha01.substring(0,4));
                mes1 = Integer.parseInt(fecha01.substring(5,7));
                dia1 = Integer.parseInt(fecha01.substring(8,10));

                anio2 = Integer.parseInt(fecha02.substring(0,4));
                mes2 = Integer.parseInt(fecha02.substring(5,7));
                dia2 = Integer.parseInt(fecha02.substring(8,10));



                Calendar fecha1 = new GregorianCalendar();
                fecha1.set(anio1, mes1, dia1);


                Calendar fecha2 = new GregorianCalendar();
                fecha2.set(anio2, mes2, dia2);

                c.setTimeInMillis(fecha1.getTime().getTime() - fecha2.getTime().getTime());

                diasDif = c.get(Calendar.DAY_OF_YEAR);


            return diasDif;

            }catch (NumberFormatException err){
                                                 errors++;
                                                 return -1;
                                              }
    }

    public int restarFechasV2(String fecha01, String fecha02)
    {


        long dif_dias=0;
        long dif_ms=0;
        int errors=0;

        int anio1, mes1, dia1, anio2, mes2, dia2;
        Calendar c = Calendar.getInstance();

        try{
            anio1 = Integer.parseInt(fecha01.substring(0,4));
            mes1 = Integer.parseInt(fecha01.substring(5,7));
            dia1 = Integer.parseInt(fecha01.substring(8,10));

            anio2 = Integer.parseInt(fecha02.substring(0,4));
            mes2 = Integer.parseInt(fecha02.substring(5,7));
            dia2 = Integer.parseInt(fecha02.substring(8,10));

            String str_date_1 = anio1+"-"+mes1+"-"+dia1;
            String str_date_2 = anio2+"-"+mes2+"-"+dia2;

             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = (Date)formatter.parse(str_date_1);
            Date date2 = (Date)formatter.parse(str_date_2);


            dif_ms = date1.getTime() - date2.getTime();

            dif_dias = dif_ms / (1000 * 60 * 60 * 24);



            return (int) Math.abs(dif_dias);

        }catch (NumberFormatException err){
                                                errors++;
                                                return -1;
                                          } catch (ParseException e) {
                                                                        e.printStackTrace();
                                                                        errors++;
                                                                        return -1;
                                                                     }

    }




    public int sumarFechasV2(String fecha01, String fecha02)
    {


        long dif_dias=0;
        long dif_ms=0;
        int errors=0;

        int anio1, mes1, dia1, anio2, mes2, dia2;
        Calendar c = Calendar.getInstance();

        try{
            anio1 = Integer.parseInt(fecha01.substring(0,4));
            mes1 = Integer.parseInt(fecha01.substring(5,7));
            dia1 = Integer.parseInt(fecha01.substring(8,10));

            anio2 = Integer.parseInt(fecha02.substring(0,4));
            mes2 = Integer.parseInt(fecha02.substring(5,7));
            dia2 = Integer.parseInt(fecha02.substring(8,10));

            String str_date_1 = anio1+"-"+mes1+"-"+dia1;
            String str_date_2 = anio2+"-"+mes2+"-"+dia2;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = (Date)formatter.parse(str_date_1);
            Date date2 = (Date)formatter.parse(str_date_2);


            dif_ms = date1.getTime() + date2.getTime();

            dif_dias = dif_ms / (1000 * 60 * 60 * 24);



            return (int) Math.abs(dif_dias);

        }catch (NumberFormatException err){
            errors++;
            return -1;
        } catch (ParseException e) {
            e.printStackTrace();
            errors++;
            return -1;
        }

    }


    public int sumarHoras(String hora01, String hora02)
    {
        long dif_dias=0;
        long dif_ms=0;

        long dif_mins=0;

        int errors=0;

        Calendar c = Calendar.getInstance();

        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        try{
                String str_date_1 = anio+"-"+mes+"-"+dia+" "+hora01;
                String str_date_2 = anio+"-"+mes+"-"+dia+" "+hora02;

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                Date date1 = (Date)formatter.parse(str_date_1);
                Date date2 = (Date)formatter.parse(str_date_2);

                int minutos01 = (int) (( date1.getTime() / (1000*60)) % 60);
                int minutos02 = (int) (( date2.getTime() / (1000*60)) % 60);

                //int seconds = (int) ((milliseconds / 1000) % 60);
                //int minutes = (int) ((milliseconds / 1000) / 60);

                dif_ms = date1.getTime() + date2.getTime();
                dif_dias = dif_ms / (1000 * 60 * 60 * 24);

                dif_mins = minutos01 + minutos02;

            return (int) Math.abs(dif_mins);

        }catch (NumberFormatException err){
                                                errors++;
                                                return -1;
                                            } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                            errors++;
                                                                            return -1;
                                                                        }

    }




    public int restarHoras(String hora01, String hora02)
    {
        long dif_dias=0;
        long dif_ms=0;

        long dif_mins=0;

        int errors=0;






        Calendar c = Calendar.getInstance();

        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        try{
                String str_date_1 = anio+"-"+mes+"-"+dia+" "+hora01;
                String str_date_2 = anio+"-"+mes+"-"+dia+" "+hora02;

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                Date date1 = (Date)formatter.parse(str_date_1);
                Date date2 = (Date)formatter.parse(str_date_2);

                int minutos01 = (int) (( date1.getTime() / (1000*60)) % 60);
                int minutos02 = (int) (( date2.getTime() / (1000*60)) % 60);

                //int seconds = (int) ((milliseconds / 1000) % 60);
                //int minutes = (int) ((milliseconds / 1000) / 60);

                dif_ms = date1.getTime() - date2.getTime();
                dif_dias = dif_ms / (1000 * 60 * 60 * 24);

                dif_mins = minutos02 - minutos01;

                return (int) Math.abs(dif_mins);

            }catch (NumberFormatException err){

                                                errors++;
                                                return -1;

                                              } catch (ParseException e)
                                                                            {
                                                                                e.printStackTrace();
                                                                                errors++;
                                                                                return -1;
                                                                            }

    }







    public String imprimirDouble(double numero)
    {



        Locale locale = new Locale("es", "MX");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');

        String pattern = "#,##0.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);

        String number = decimalFormat.format(numero);

        System.out.println("[ NUMERO FORMATEADO :"+number+" ]");


        return number;
    }



    public static boolean esEmailValido(String email)
    {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
    }

    public TextInputLayout getTIL(Context context)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,5,10,0);

        TextInputLayout til = new TextInputLayout(context);
        til.setLayoutParams(params);
        til.setErrorEnabled(true);


        return til;
    }

    public EditText getEditText(String hint, int id, boolean focusable, Context context)
    {
        EditText editText = new EditText(context);
        editText.setHint(hint);
        editText.setFocusable(focusable);
        editText.setId(id);


        return editText;
    }

    public RadioGroup getRadioGroup( Context context)
    {
        RadioGroup radioButton = new RadioGroup(context);
         radioButton.setOrientation(LinearLayout.HORIZONTAL);

        return radioButton;
    }

    public RadioButton getRadioButton(String text,int id,boolean focusable,Context context)
    {
        RadioButton radioButton = new RadioButton(context);
        radioButton.setId(id);
        radioButton.setText(text);
        radioButton.setPadding(10,5,20,5);

        return radioButton;
    }


    public TextView getTexView(String text,Context context)
    {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setPadding(5,10,5,5);

        return textView;
    }

    public View getSeparador(Context context)
    {
        View view;
        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_separator, null);

        return view;
    }

    public View getSeparadorInvisible(Context context)
    {
        View view;
        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_separator_invisible, null);

        return view;
    }

    public void printConsola(String text)
    {
        System.out.println("[:]--[^]Y[^]--[:]     "+text+"\n [] \n");
    }




    public  void showSnackBar(Context context, CoordinatorLayout layout,String message,String tipo)
    {

        //final View viewPos = findViewById(R.id.coordinatorLayout);
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        if(tipo.equals("INFO")) {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.PrimaryColor));
        }
        if (tipo.equals("ERROR"))
        {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.error_color));
        }
        snackbar.show();

    }


    public Snackbar getSnackBar(Context context, CoordinatorLayout layout,String message,String tipo)
    {
        //final View viewPos = findViewById(R.id.coordinatorLayout);
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();

        if(tipo.equals("OK")) {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.Green500));
        }
        if(tipo.equals("INFO")) {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.naranja));
        }
        if (tipo.equals("ERROR"))
        {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.error_color));
        }

        CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)snackBarView.getLayoutParams();
        params.gravity = Gravity.TOP;

        return snackbar;

    }




}


/*

Launching application: code.yoursoft.ciummovil/code.yoursoft.ciummovil.Home_back.
DEVICE SHELL COMMAND: am start  -n "code.yoursoft.ciummovil/code.yoursoft.ciummovil.Home_back" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
Error: Adb refused a command

* */