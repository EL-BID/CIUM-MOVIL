package code.yoursoft.ciummovil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bluetooth extends AppCompatActivity
{

    /// 6 fotos tamaño cartilla 35 x 45 mm, color, camisa blanca, rev. papel mate
    /// ACTA ORIGINAL,
    /// CERT. ESTUDIOS
    /// IFE
    /// CURP


    private static int DISCOVERY_REQUEST = 1;
    private ArrayList<BluetoothDevice> dispositivos_encontrados;

    private BluetoothAdapter bluetooth;
    private BluetoothSocket socket;
    private UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");

    List<BluetoothSocket> SOCKETS = new ArrayList<>();

    private BluetoothSocketListener bsl=null;
    Thread messageListener=null;

    private ArrayAdapter<BluetoothDevice> aa;
    private ListView list;

    ArrayList<Device> dispositivos;
    private AdaptadorDevice adaptador;

   //BroadcastReceiver discoveryResult;
    private Handler handler = new Handler();

    private EditText text_view;



    BroadcastReceiver discoveryResult =  new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            BluetoothDevice remoteDevice;
            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (bluetooth.getBondedDevices().contains(remoteDevice))
            {
                dispositivos.add(new Device(remoteDevice, remoteDevice.getName(), remoteDevice.getAddress()));
                adaptador.notifyDataSetChanged();

                System.out.println("\n \n REMOTE DEVICE : [ "+remoteDevice+" ,  "+remoteDevice.getName()+" ] \n");
            }
        }
    };


    EditText messageText;
    TextView text_status, text_clues,text_indicadores, text_criterios;



    ImageView image_status;
    Button boton_actualizar;
    Button boton_enviar;

    LinearLayout layout_bottom;
    LinearLayout layout_enviar;
    RelativeLayout layout_loading;
    RelativeLayout layout_sobreescribir;

    ProgressBar progress_bar;
    CoordinatorLayout coordinatorLayout;


    Evaluacion evaluacion;
    Funciones link;
    DBManager linkDB;


    Clues clues_selected;
    Usuarios user_signed;
    int idEvaluacion;


    boolean SOBREESCRIBIR;

    String MODO="";
    int FULL=0;
    String TIPO_EVALUACION="";
    String DATOS_ENVIADOS="";
    CheckBox checkbox;

    Handler puente;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bluetooth);

        link = new Funciones();
        linkDB = new DBManager(this);

        user_signed = linkDB.getSignedUser();

        if(user_signed==null)
        {
            link.goLogin(this);
        }

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle vars = getIntent().getExtras();
        if (vars != null)
        {
            idEvaluacion = vars.getInt("idEvaluacion");
            MODO=vars.getString("MODO");
            FULL=vars.getInt("FULL");
            TIPO_EVALUACION= vars.getString("TIPO_EVALUACION");
        }

        if(FULL==1)
        {
            evaluacion=null;
            clues_selected = null;

        }else {
                evaluacion = linkDB.getEvaluacion("RECURSO", idEvaluacion, linkDB.openDB());
                clues_selected=linkDB.getClues(evaluacion.clues);
              }

        if(evaluacion==null)
        {
            System.out.println("ERROR EVALUACION RECURSO REGRESO NULL....]]]");
        }




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bluetooth);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                finish();

            }
        });



        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        layout_bottom.setVisibility(View.GONE);

        layout_enviar = (LinearLayout) findViewById(R.id.layout_enviar);
        layout_enviar.setVisibility(View.GONE);

        layout_loading = (RelativeLayout) findViewById(R.id.layout_loading);
        layout_loading.setVisibility(View.GONE);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        layout_sobreescribir = (RelativeLayout) findViewById(R.id.layout_sobreescribir);
        checkbox = (CheckBox) findViewById(R.id.check_sobreescribir);


        final Resources res = getResources();
        int verde = res.getColor(R.color.PrimaryColor);

        progress_bar.getIndeterminateDrawable().setColorFilter(verde, PorterDuff.Mode.MULTIPLY);



        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        //configureBluetooth();
        bluetooth = BluetoothAdapter.getDefaultAdapter();




        dispositivos = new ArrayList<Device>();
        adaptador = new AdaptadorDevice(this,dispositivos);
        list = (ListView) findViewById(R.id.list_discovered);
        list.setAdapter(adaptador);



        image_status = (ImageView) findViewById(R.id.image_status);
        text_status = (TextView)findViewById(R.id.text_status);
        text_clues = (TextView)findViewById(R.id.text_clues);
        text_indicadores = (TextView)findViewById(R.id.text_indicadores);
        text_criterios = (TextView)findViewById(R.id.text_criterios);

        boton_actualizar = (Button) findViewById(R.id.boton_actualizar);
        boton_enviar = (Button) findViewById(R.id.boton_enviar);

        getSupportActionBar().setTitle("Bluetooth");



        text_view = (EditText) findViewById(R.id.TEXT_VIEW);





///*************************************************************************************************


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, final int index, long arg3) {


                AsyncTask<Integer, Integer, Integer> connectTask = new AsyncTask<Integer, Integer, Integer>() {

                    @Override
                    protected Integer doInBackground(Integer... params) {
                        int res = 0;

                        try {
                            //BluetoothDevice device = dispositivos_encontrados.get(params[0]);
                            BluetoothDevice device = dispositivos.get(params[0]).getBluetoothDevice();

                            socket = device.createRfcommSocketToServiceRecord(uuid);
                            socket.connect();
                            res = 1;


                        } catch (IOException e) {
                            System.out.println("ERROR CONECTANDO : "+e.toString()+".");
                            Log.d("BLUETOOTH_CLIENT", e.getMessage());
                        }
                        return res;
                    }

                    protected void onProgressUpdate(Integer... values) {
                    }

                    @Override
                    protected void onPostExecute(Integer result) {

                        if (result == 1)
                        {
                            if (socket.isConnected())
                            {
                                arrancarHiloEscucha();
                                pintarStatus("CONECTADO A", socket.getRemoteDevice().getName());

                            } else {
                                cerrarSocket();
                                pintarStatus("CONEXION FALLO", "");
                            }
                        } else {
                            cerrarSocket();
                            pintarStatus("CONEXION FALLO", "");
                        }


                    }
                };

                cerrarSocket();
                pintarStatus("CONECTANDO", dispositivos.get(index).getNombre());

                connectTask.execute(index);

            }
        });


///*************************************************************************************************

        boton_actualizar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buscarDispositivos();
            }
        });

///*************************************************************************************************

        boton_enviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {


                System.out.println("\n \n \n --------- SOCKET AL ENVIAR : [ " + socket.hashCode() + " ] -------");

                if (socket.isConnected())
                {
                    pintarStatus("ENVIANDO", "");

                    enviarDatos(socket, getEvaluacionEnviar());

                } else {

                    link.showSnackBar(getApplicationContext(), coordinatorLayout, "Se desconectó", "ERROR");
                    cerrarSocket();
                    pintarStatus("CONEXION CERRADA", "");
                }


            }
        });

///*************************************************************************************************

        puente = new Handler()
        {

            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);

                boolean es_mensaje=false;

                String datos = msg.obj.toString();

                System.out.println("DATOS : [ "+datos+" ] --- DATOS RECORTADOS : [ "+datos.substring(0, datos.length() - 1)+" ]");

                datos=datos;

                ///text_view.setText("-> "+datos+" <-");

                switch((String) datos )
                {

                    case "BUSCANDO":

                        pintarStatus("BUSCANDO","");
                        es_mensaje=true;
                        break;
                    case "ESCUCHANDO":
                        pintarStatus("ESCUCHANDO","");
                        es_mensaje=true;

                        break;
                    case "CONECTANDO":
                        pintarStatus("CONECTANDO","");
                        es_mensaje=true;

                        break;
                    case "CONECTADO A":
                        pintarStatus("CONECTADO A","");
                        es_mensaje=true;

                        break;
                    case "CONECTADO POR":
                        pintarStatus("CONECTADO POR","");
                        es_mensaje=true;

                        break;
                    case "ENVIANDO":
                        pintarStatus("ENVIANDO","");
                        es_mensaje=true;

                        break;
                    case "RECIBIENDO":
                        pintarStatus("RECIBIENDO","");
                        es_mensaje=true;

                        break;
                    case "RECIBIDO":

                        pintarStatus("RECIBIDO","");

                        eliminarDatosEnviados();

                        es_mensaje=true;

                        break;
                    case "REINICIAR":
                        pintarStatus("REINICIAR","");
                        es_mensaje=true;

                        break;
                    case "CONEXION CERRADA":
                        pintarStatus("CONEXION CERRADA","");
                        es_mensaje=true;

                        break;
                    case "CONEXION FALLO":
                        pintarStatus("CONEXION FALLO","");
                        es_mensaje=true;
                        break;
                    case "ENVIO FALLO":
                        pintarStatus("ENVIO FALLO","");
                        es_mensaje=true;
                        break;
                    case "ENVIO FALLO 1":
                        pintarStatus("ENVIO FALLO 1","");
                        es_mensaje=true;
                        break;
                    case "ENVIO FALLO 2":
                        pintarStatus("ENVIO FALLO 2","");
                        es_mensaje=true;
                        break;


                }/// F I N    S WI T C H



                //if(es_mensaje==false && datos_validos==true)
                if( es_mensaje==false )
                {

                    //showDialogPrint(datos);

                    try{

                        //datos = datos.replace("\\\"","'");

                        JSONObject json = new JSONObject(datos);
                        //JSONObject json = new JSONObject(datos.substring(1,datos.length()-1));


                        int indicadoresTemp= json.getInt("INDICADORES");
                        int criteriosTemp = json.getInt("CRITERIOS");

                        showDialogAceptarTransferencia(datos,indicadoresTemp,criteriosTemp);

                       }catch(JSONException err){
                                                    err.printStackTrace();
                                                    System.out.println("ERROR CONVIRTIENDO CONTADORES DE DATOS...");
                                                }


                    ////****************************************************************************

                    ///*************************************************************************************

                }// FIN   DESCARGA DE EVALUACIONES


            }
        };

///*************************************************************************************************

        if(MODO.equals("EMISOR"))
        {
            layout_sobreescribir.setVisibility(View.GONE);
            toolbar.setSubtitle("Enviar evaluación");

            buscarDispositivos();

            pintarStatus("BUSCANDO", "");

        }
        if(MODO.equals("RECEPTOR"))
        {
            layout_sobreescribir.setVisibility(View.VISIBLE);
            toolbar.setSubtitle("Recibir evaluación");

            escucharConexion();


        }

        contarEvaluacion();

    }   ///   F I N     O N    C R E A T E

///*************************************************************************************************

    public void buscarDispositivos()
    {

        dispositivos.clear();
        registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        if(bluetooth.isDiscovering()) {  bluetooth.cancelDiscovery();  }
        bluetooth.startDiscovery();

    }

///*************************************************************************************************

    public void escucharConexion()
    {

        Intent disc;
        disc = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(disc, DISCOVERY_REQUEST);

    }

///*************************************************************************************************


    private void iniciarDiscovery()
    {
      try {
            if (bluetooth.isDiscovering())
              {
                bluetooth.cancelDiscovery();
              }

              bluetooth.startDiscovery();

          }catch(Exception err){ err.printStackTrace(); }

    }

///*************************************************************************************************


    private void pararDiscovery()
    {
        try {

            if (bluetooth.isDiscovering())
            {
                bluetooth.cancelDiscovery();
            }

        }catch(Exception err){ err.printStackTrace(); }

    }

///*************************************************************************************************

    private void encenderBluetooth()
    {
        Intent intent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, 1);
    }
///*************************************************************************************************


    private void arrancarHiloEscucha()
    {
        ///list.setVisibility(View.GONE);
        //layout_bottom.setVisibility(View.GONE);

        try {
                if (messageListener != null)
                {
                    messageListener.interrupt();
                    System.out.println("------> HILO ESCUCHA CERRADO -------"+messageListener.getId());
                }

            }catch (NullPointerException err){  err.printStackTrace();  }


        bsl = null;

        bsl = new BluetoothSocketListener(socket);
        messageListener = new Thread(bsl);
        messageListener.start();

        System.out.println("------> NUEVO HILO ESCUCHA ANRRANCADO -------" + messageListener.getId());


    }

///*************************************************************************************************

    private void enviarDatos(BluetoothSocket socket, String msg)
    {
        OutputStream outStream;
        try {

               outStream = socket.getOutputStream();

               byte[] byteString = (msg + "Ç").getBytes();
                    //byte[] byteString = (msg).getBytes();
               //byteString[ byteString.length - 1] = 0;
               outStream.write(byteString);

               System.out.println("---------> SE ENVIO [ " + msg + " ]-------" );

            } catch (IOException e) {
                                       Log.d("BLUETOOTH_COMMS", e.getMessage());
                                    }
    }


///*************************************************************************************************

    public void cerrarSocket()
    {
        try {
              if(socket != null)
                {
                    if(socket.isConnected())
                    {enviarDatos(socket,"CONEXION CERRADA");}

                    System.out.println("\n \n \n --------- SOCKET A CERRAR : [ " + socket.hashCode() + " ] -------");
                    socket.close();
                    socket=null;
                    link.showSnackBar(this, coordinatorLayout, "Conexión cerrada", "INFO");
                }
            } catch (IOException e) {

                                        e.printStackTrace();
                                     }
    }

///*************************************************************************************************


    public void pintarStatus(String status, String device)
    {

            if (status.equals("BUSCANDO"))
            {
                list.setVisibility(View.VISIBLE);
                layout_bottom.setVisibility(View.VISIBLE);
                layout_enviar.setVisibility(View.GONE);

                layout_loading.setVisibility(View.VISIBLE);
                image_status.setVisibility(View.GONE);

                text_status.setText("Buscando dispositivos...");
            }

            if (status.equals("ESCUCHANDO"))
            {
                list.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                layout_enviar.setVisibility(View.GONE);

                layout_loading.setVisibility(View.VISIBLE);
                image_status.setVisibility(View.GONE);

                text_status.setText("Esperando conexión...");
            }

            if (status.equals("CONECTANDO"))
            {
                list.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                layout_enviar.setVisibility(View.GONE);

                layout_loading.setVisibility(View.VISIBLE);
                image_status.setVisibility(View.GONE);

                text_status.setText("Conectando a "+device+"...");
            }

            if (status.equals("CONECTADO A"))
            {
                layout_bottom.setVisibility(View.GONE);
                layout_enviar.setVisibility(View.VISIBLE);

                layout_loading.setVisibility(View.GONE);
                image_status.setVisibility(View.VISIBLE);
                image_status.setImageResource(R.drawable.ic_bluetooth_connected_black_24dp);

                Resources res = getResources();
                int verde = res.getColor(R.color.PrimaryColor);
                image_status.setColorFilter(verde);

                text_status.setText("Conectado a "+device);
            }


            if (status.equals("ENVIANDO"))
            {
                layout_enviar.setVisibility(View.GONE);
                layout_loading.setVisibility(View.VISIBLE);
                image_status.setVisibility(View.GONE);

                text_status.setText("Enviando evaluación ...");
            }

            if (status.equals("CONECTADO POR"))
            {
                layout_bottom.setVisibility(View.GONE);
                layout_enviar.setVisibility(View.GONE);

                layout_loading.setVisibility(View.GONE);
                image_status.setVisibility(View.VISIBLE);
                image_status.setImageResource(R.drawable.ic_bluetooth_connected_black_24dp);

                Resources res = getResources();
                int verde = res.getColor(R.color.PrimaryColor);
                image_status.setColorFilter(verde);

                text_status.setText("Conexion establecida por "+device);
            }

            if (status.equals("RECIBIENDO"))
            {
                layout_enviar.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                layout_loading.setVisibility(View.VISIBLE);
                image_status.setVisibility(View.GONE);

                text_status.setText("Recibiendo evaluación ...");
            }


            if (status.equals("RECIBIDO"))
            {
                layout_loading.setVisibility(View.GONE);
                image_status.setVisibility(View.VISIBLE);
                image_status.setImageResource(R.drawable.ic_check_white_24dp);

                Resources res = getResources();
                int verde = res.getColor(R.color.PrimaryColor);
                image_status.setColorFilter(verde);

                if(MODO.equals("EMISOR"))
                  {
                    text_status.setText("Evaluación enviada");
                  }else{ text_status.setText("Evaluación recibida"); }
            }


            if (status.equals("REINICIAR"))
            {

                if(MODO.equals("EMISOR"))
                {
                    list.setVisibility(View.VISIBLE);
                    layout_bottom.setVisibility(View.VISIBLE);
                    layout_enviar.setVisibility(View.GONE);

                    layout_loading.setVisibility(View.VISIBLE);
                    image_status.setVisibility(View.GONE);
                    text_status.setText("Buscando dispositivos...");

                }else{
                        list.setVisibility(View.GONE);
                        layout_bottom.setVisibility(View.GONE);
                        layout_enviar.setVisibility(View.GONE);

                        layout_loading.setVisibility(View.VISIBLE);
                        image_status.setVisibility(View.GONE);

                        text_status.setText("Esperando conexión...");
                     }

            }



            if (status.equals("CONEXION CERRADA"))
            {
                cerrarSocket();

                layout_loading.setVisibility(View.GONE);
                layout_enviar.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                list.setVisibility(View.GONE);

                image_status.setVisibility(View.VISIBLE);
                image_status.setImageResource(R.drawable.ic_bluetooth_disabled_black_24dp);

                Resources res = getResources();
                int rojo = res.getColor(R.color.error_color);
                image_status.setColorFilter(rojo);

                text_status.setText("Conexión cerrada");
            }

            if (status.equals("CONEXION FALLO"))
            {
                layout_loading.setVisibility(View.GONE);
                layout_enviar.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                list.setVisibility(View.GONE);

                image_status.setVisibility(View.VISIBLE);
                image_status.setImageResource(R.drawable.ic_bluetooth_disabled_black_24dp);

                Resources res = getResources();
                int rojo = res.getColor(R.color.error_color);
                image_status.setColorFilter(rojo);

                text_status.setText("La conexión no se pudo establecer");
            }

            if (status.equals("ENVIO FALLO"))
            {
                layout_loading.setVisibility(View.GONE);
                layout_enviar.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                list.setVisibility(View.GONE);

                image_status.setVisibility(View.VISIBLE);
                image_status.setImageResource(R.drawable.ic_error_outline_white_24dp);

                Resources res = getResources();
                int rojo = res.getColor(R.color.error_color);
                image_status.setColorFilter(rojo);

                if(MODO.equals("EMISOR"))
                {
                    text_status.setText("El envio falló");
                }else{ text_status.setText("La recepción falló"); }
            }

            if (status.equals("ENVIO FALLO 1"))
            {
                layout_loading.setVisibility(View.GONE);
                layout_enviar.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                list.setVisibility(View.GONE);

                image_status.setVisibility(View.VISIBLE);
                image_status.setImageResource(R.drawable.ic_error_outline_white_24dp);

                Resources res = getResources();
                int rojo = res.getColor(R.color.error_color);
                image_status.setColorFilter(rojo);

                if(MODO.equals("EMISOR"))
                {
                    text_status.setText("Clues distintas");
                }else{ text_status.setText("clues distintas"); }
            }

            if (status.equals("ENVIO FALLO 2"))
            {
                layout_loading.setVisibility(View.GONE);
                layout_enviar.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                list.setVisibility(View.GONE);

                image_status.setVisibility(View.VISIBLE);
                image_status.setImageResource(R.drawable.ic_error_outline_white_24dp);

                Resources res = getResources();
                int rojo = res.getColor(R.color.error_color);
                image_status.setColorFilter(rojo);

                if(MODO.equals("EMISOR"))
                {
                    text_status.setText("Tipo evaluación incompatible");
                }else{ text_status.setText("Tipo evaluación incompatible"); }
            }



            if (status.equals("NO RECEPCION"))
            {
                layout_sobreescribir.setVisibility(View.GONE);
                layout_loading.setVisibility(View.GONE);
                layout_enviar.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                list.setVisibility(View.GONE);

                image_status.setVisibility(View.VISIBLE);
                image_status.setImageResource(R.drawable.ic_error_outline_white_24dp);

                Resources res = getResources();
                int rojo = res.getColor(R.color.error_color);
                image_status.setColorFilter(rojo);

                if(MODO.equals("EMISOR"))
                {
                    text_status.setText("Conexión cancelada");
                }else{ text_status.setText("Conexión cancelada"); }
            }

////*******************************************************************************


    }/// FIN PRINT STATUS




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == DISCOVERY_REQUEST)
        {
            boolean isDiscoverable = resultCode > 0;


                if (isDiscoverable)
                {
                    String name = "bluetoothserver";
                    try {

                        final BluetoothServerSocket btserver = bluetooth.listenUsingRfcommWithServiceRecord(name, uuid);

                        AsyncTask<Integer, Void, BluetoothSocket> acceptThread = new AsyncTask<Integer, Void, BluetoothSocket>() {
                            @Override
                            protected BluetoothSocket doInBackground(Integer... params) {
                                try {
                                    socket = btserver.accept(params[0] * 1000);
                                    return socket;
                                } catch (IOException e) {
                                    System.out.println("ERROR RECIBIENDO NUEVA CONEXION: "+e.toString());
                                    Log.d("BLUETOOTH", e.getMessage());
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(BluetoothSocket result) {
                                if (result != null) {
                                    arrancarHiloEscucha();
                                    pintarStatus("CONECTADO POR", socket.getRemoteDevice().getName());

                                } else {
                                    pintarStatus("CONEXION FALLO", "");
                                    cerrarSocket();
                                    link.showSnackBar(getApplicationContext(), coordinatorLayout, "No se pudo recibir conexion", "ERROR");
                                }

                            }
                        };

                        cerrarSocket();
                        acceptThread.execute(resultCode);

                    } catch (IOException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    }

                    pintarStatus("ESCUCHANDO", "");

                }else{
                    //link.printToast("NO SE INICIA NODO RECEPTOR POR NEGATIVA DE USUARIO", getApplicationContext());
                    pintarStatus("NO RECEPCION","");
                 }

        }
    }







    private class MessagePoster implements Runnable
    {
        private TextView textView;
        private String message;

        public MessagePoster(TextView textView, String message)
        {
            this.textView = textView;
            this.message = message;
        }

        public void run()
        {
            textView.setText(message);
        }
    }


    private class BluetoothSocketListener implements Runnable
    {

        private BluetoothSocket socket;


        public BluetoothSocketListener(BluetoothSocket socket)
        {

            this.socket = socket;

        }


        public void run()
        {

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            try {

                    System.out.println("---------> HILO ESCUCHA ARRANCADO  MODO [ "+MODO+" ]-------" + messageListener.getId());

                    InputStream instream = socket.getInputStream();
                    int bytesRead = -1;
                    String message = "";
                    boolean seguir_leyendo=true;


                    while (true)
                    {
                        System.out.println("---------> HILO ESCUCHA DENTRO DE WHILE TRUE  MODO [ "+MODO+" ]-------");
                          message = "";
                          seguir_leyendo=true;

                        bytesRead = instream.read(buffer);///LEE UN BYTE

                        System.out.println("---------> HILO ESCUCHA MENSAJE RECIBIDO : TAMAÑO [ "+bytesRead+" ] -- CONTENIDO : ["+new String(buffer, 0, bytesRead)+"]--");

                        if (bytesRead != - 1)
                        {
                            System.out.println("---------> HILO ESCUCHA DENTRO DE IF : TAMAÑO [ "+bytesRead+" ] -- CONTENIDO : ["+new String(buffer, 0, bytesRead)+"]--");

                            // O J O     O J O     O J O
                            /// COMPARAR AQUI SI HAY MAS QUE LEER SINO DESDE AQUI LANZAR HANDLER

                            String lexema = new String(buffer, 0, bytesRead);
                            String token = lexema.substring(lexema.length() - 1, lexema.length());
                            if(token.equals("Ç")){ seguir_leyendo=false; }

                            while ( seguir_leyendo==true )
                            {

                                message = message + new String(buffer, 0, bytesRead);
                                bytesRead = instream.read(buffer);

                                System.out.println(" [ IN WHILE ] TAMAÑO DE BUFER ------> [" + bytesRead + "] MENSAJE ARMADO --> DIM [ "+message.length()+" ]  ");

                                String trama = new String(buffer, 0, bytesRead);
                                String fin= trama.substring( trama.length()-1 , trama.length() );

                                System.out.println(" [ TRAMA ] --> [ "+trama+" ] \n");
                                System.out.println(" [ ULTIMO CARACTER ] --> [ "+fin+" ]");

                                if(fin.equals("Ç")){ seguir_leyendo=false; }

                            }

                            //message = message + new String(buffer, 0, bytesRead - 1);
                            message = message + new String(buffer, 0, bytesRead);
                            System.out.println(" [ OUT WHILE ] TAMAÑO DE BUFER ------> [" + bytesRead + "] MENSAJE ARMADO --> DIM [ "+message.length()+" ]  ");

                            ///AQUI SE RESPONDE
                            ///*************************************************************************
                            Message msg=new Message();
                            msg.obj = message.substring(0,message.length()-1);
                            puente.sendMessage(msg);
                            System.out.println("-------    ENVIADO TO HANDLER ----- [ "+message.length()+" ] ");
                            //**************************************************************************

                            socket.getInputStream();
                        }else{

                            System.out.println("---------> HILO ESCUCHA MENSAJE RECIBIDO : TAMAÑO [ "+bytesRead+" ] -- CONTENIDO : ["+new String(buffer, 0, bytesRead)+"]--");

                             }
                        //socket.getInputStream();



                    }



            } catch (IOException e)
            {
                Log.d("BLUETOOTH_COMMS", e.getMessage());
            }



        }


    }

///*************************************************************************************************

    public boolean getSobreescribir()
    {
        boolean ok=false;

        if(checkbox.isChecked())
        {
            ok=true;
        }else{
               ok=false;
             }

        return ok;

    }

///*************************************************************************************************

    public String getEvaluacionEnviar()
    {
        System.out.println("INICIANDO ARMADO DE EVALUACION ENVIAR...");

        String evaluacion_string="";

        JSONObject json=new JSONObject();

        JSONArray array_evaluaciones=new JSONArray();
        JSONObject evaluacion_json;

        JSONArray array_hallazgos = new JSONArray();
        JSONObject hallazgo_json;

        JSONArray array_indicadores = new JSONArray();
        JSONObject indicador_json;


        int x_indicadores = 0;
        int x_criterios = 0;
        int x_respondidos = 0;

        int errors=0;
        String messages="";



        int nivel_cone = linkDB.getNivelCone(evaluacion.clues);

        List<Indicador> indicadores = new ArrayList<>();
        List<LugarVerificacion> listaLV = new ArrayList<>();

        //indicadores = linkDB.getIndicadoresEvaluacionRecurso(nivel_cone);

        indicadores = linkDB.getIndicadoresAgregadosRecurso(evaluacion.id);




        for (int i = 0; i < indicadores.size(); i++)
        {
            int id_indicador=indicadores.get(i).getId();

            x_indicadores++;

            listaLV = linkDB.getLugaresVerificacion(nivel_cone,indicadores.get(i).getId());
            List<Criterio> listaC = new ArrayList<>();

            for (int j = 0; j < listaLV.size(); j++)
            {
                listaC = linkDB.getCriterios(nivel_cone, indicadores.get(i).getId(), listaLV.get(j).getId());

                for (int k = 0; k < listaC.size(); k++)
                {
                    x_criterios++;

                    EvaluacionRecursoCriterio eval = linkDB.getEvaluacionRecursoCriterio(evaluacion.getId(),
                            indicadores.get(i).getId(), listaC.get(k).getId());

                    if (eval != null)
                    {

                        x_respondidos++;
                        evaluacion_json=new JSONObject();
                        try {
                                evaluacion_json.put("id",eval.getId());
                                evaluacion_json.put("id_evaluacion_recurso",eval.getId());
                                evaluacion_json.put("id_criterio",eval.getId_criterio());
                                evaluacion_json.put("id_indicador",eval.getId_indicador());
                                evaluacion_json.put("aprobado",eval.getAprobado());
                                evaluacion_json.put("creadoAl",eval.getCreadoAl());
                                evaluacion_json.put("modificadoAl",eval.getModificadoAl());
                                evaluacion_json.put("borradoAl",eval.getBorradoAl());

                                array_evaluaciones.put(evaluacion_json);

                            } catch (JSONException e) {  e.printStackTrace();}

                    }

                }


            }


            ArrayList<EvaluacionRecursoIndicador> indicadoresAgregados = (ArrayList<EvaluacionRecursoIndicador>) linkDB.getEvaluacionRecursoIndicador(idEvaluacion);

            for (int x=0;x<indicadoresAgregados.size(); x++)
            {
                 EvaluacionRecursoIndicador eri = indicadoresAgregados.get(x);

                    indicador_json = new JSONObject();
                    try {

                        indicador_json.put("id",eri.getId());
                        indicador_json.put("idEvaluacionRecurso",eri.getIdEvaluacionRecurso());
                        indicador_json.put("idIndicador",eri.getIdIndicador());
                        indicador_json.put("totalCriterio",eri.getTotalCriterio());
                        indicador_json.put("avanceCriterio",eri.getAvanceCriterio());

                        array_indicadores.put(indicador_json);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        errors++;
                        messages += "Error consiguiendo Indicadores";
                    }



            }


            Hallazgo hallazgo = linkDB.getHallazgo("RECURSO",evaluacion.id, id_indicador,null);

            if (hallazgo != null)
            {
                hallazgo_json = new JSONObject();

                try {
                        hallazgo_json.put("descripcion", hallazgo.getDescripcion());
                        hallazgo_json.put("idAccion", hallazgo.getIdAccion());
                        hallazgo_json.put("idIndicador", hallazgo.getIdIndicador());

                        array_hallazgos.put(hallazgo_json);

                } catch (JSONException e) {
                    e.printStackTrace();
                    errors++;
                    messages += "Error consiguiendo hallazgos";
                }
            }




            try {
                    json.put("CLUES",evaluacion.getClues());
                    json.put("TIPO","RECURSO");
                    json.put("FECHA_EVALUACION",evaluacion.fechaEvaluacion);
                    json.put("FIRMA",evaluacion.firma);
                    json.put("RESPONSABLE",evaluacion.responsable);

                    json.put("INDICADORES",x_indicadores);
                    json.put("CRITERIOS",x_respondidos);

                    System.out.println("Armando evaluacion enviar:... CLUES : " + evaluacion.getClues() + ".");

                    json.put("EVALUACIONES",array_evaluaciones);

                    json.put("indicadores", array_indicadores);
                    json.put("hallazgos", array_hallazgos);


                } catch (JSONException e) {
                                            System.out.println("ERROR ERROR ERROR Armando evaluacion enviar:... CLUES : "+evaluacion.getClues()+".");
                                            e.printStackTrace();
                                          }

        }

        evaluacion_string = json.toString();

        return evaluacion_string;
    }




///*************************************************************************************************


    public void contarEvaluacion()
    {
        int x_indicadores=0;
        int x_criterios=0;
        int x_respondidos=0;

        if(FULL==0) {

            int nivel_cone = linkDB.getNivelCone(evaluacion.clues);

            List<Indicador> indicadores = new ArrayList<>();
            List<LugarVerificacion> listaLV = new ArrayList<>();

            indicadores = linkDB.getIndicadoresAgregadosRecurso(evaluacion.id);

            for (int i = 0; i < indicadores.size(); i++) {
                x_indicadores++;

                int idIndicadorTemp = indicadores.get(i).getId();
                System.out.println("DENTRO DE CRITERIOS EN CONTEO... ID INDICADOR :" + idIndicadorTemp + ".");

                listaLV = linkDB.getLugaresVerificacion(nivel_cone, idIndicadorTemp);

                List<Criterio> listaC = new ArrayList<>();

                System.out.println("NIVEL CONE : " + nivel_cone + ". LISTA LUGARES VERIFIF. TAMAÑO :" + listaLV.size() + ".");

                for (int j = 0; j < listaLV.size(); j++) {
                    listaC = linkDB.getCriterios(nivel_cone, idIndicadorTemp, listaLV.get(j).getId());

                    for (int k = 0; k < listaC.size(); k++) {
                        x_criterios++;

                        EvaluacionRecursoCriterio eval = linkDB.getEvaluacionRecursoCriterio(evaluacion.getId(),
                                idIndicadorTemp,
                                listaC.get(k).getId());

                        if (eval != null) {
                            x_respondidos++;
                        }

                    }

                }

            }


            text_clues.setText("Clues : " + evaluacion.clues);
            text_indicadores.setText("Indicadores : " + x_indicadores);
            text_criterios.setText("Criterios : " + x_respondidos + " / " + x_criterios);
        }else{
                text_clues.setText("Clues : pendiente...");
                text_indicadores.setText("Indicadores : " + x_indicadores);
                text_criterios.setText("Criterios : " + x_respondidos + " / " + x_criterios);

             }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.reiniciar)
        {

            if(socket != null)
            {
                if(socket.isConnected())
                {
                    enviarDatos(socket, "CONEXION CERRADA");
                }
            }
            cerrarSocket();

            pintarStatus("REINICIAR", "");

            if(MODO.equals("EMISOR"))
            {
               buscarDispositivos();
            }else{
                    escucharConexion();
                 }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


///*************************************************************************************************

    public void showDialogAceptarTransferencia(final String datos,int indicadores,int criterios)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmar Transferencia").setMessage("Se recibiran: \n"+indicadores+" indicadores, \n"+criterios+" criterios .\n \n¿ Desea continuar ?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        ejecutarTransferencia(datos);
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
    }

///*************************************************************************************************

    public void showDialogPrint(final String datos)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("PRINT").setMessage("DATA RECEIVED : \n" + datos + " .FIN")
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

///*************************************************************************************************

    public void ejecutarTransferencia(String datos)
    {
        pintarStatus("RECIBIENDO","");

        String error_tipo="";


        int ERRORS=0;
        int ERRORS_PRE=0;

        JSONObject json=null;
        String clues_recibida ="";
        String tipo_evaluacion  ="";
        String fecha_evaluacion  ="";
        String firma  ="";
        String responsable ="" ;
        String emailResponsable ="" ;

        try {
                 json = new JSONObject(datos);

                 clues_recibida = json.getString("CLUES");
                 tipo_evaluacion = json.getString("TIPO");
                 fecha_evaluacion = json.getString("FECHA_EVALUACION");
                 firma = json.getString("FIRMA");
                 responsable = json.getString("RESPONSABLE");

            }catch(JSONException e) {
                                        e.printStackTrace();
                                        System.out.println("Error JSONException datos generales evaluacion");
                                        ERRORS++;
                                    }


///*************************************************************************************************
          if(FULL==1)
          {
              Usuarios user = linkDB.getSignedUser();

              Evaluacion new_evaluacion = new Evaluacion(0, 0, user.id, clues_recibida, fecha_evaluacion, 1, firma, responsable,"", 0, 0, 0, link.getFecha(), "0000-00-00 00:00:00", "0000-00-00 00:00:00");
              int id_new_eval = linkDB.insertarEvaluacion("RECURSO", new_evaluacion, linkDB.openDB());

              System.out.println("NUEVA EVALUACION GENERADA -- > ID: "+id_new_eval+" .");

              evaluacion = linkDB.getEvaluacion("RECURSO", id_new_eval, linkDB.openDB());
              idEvaluacion = evaluacion.id;

              if(evaluacion==null)
              {
                  System.out.println("NUEVA EVALUACION NULL .");
              }

              clues_selected = linkDB.getClues(evaluacion.clues);
          }
///*************************************************************************************************


            if (clues_recibida.equals(evaluacion.clues))
            {
            } else {
                    ERRORS++;
                    ERRORS_PRE++;
                    error_tipo = "CLUES DISTINTA";
                    System.out.println("Error clues distinta");
                    }

            if (tipo_evaluacion.equals("RECURSO"))
            {
            } else {
                    ERRORS++;
                    ERRORS_PRE++;
                    error_tipo = "TIPO EVALUACION DIFERENTE";
                    System.out.println("Error tipo evaluacion diferente");
                    }


            if (ERRORS_PRE == 0)
            {
                try{

                JSONArray array_evaluaciones = json.getJSONArray("EVALUACIONES");

                for (int i = 0; i < array_evaluaciones.length(); i++)
                {
                    JSONObject evaluacion = array_evaluaciones.getJSONObject(i);

                    EvaluacionRecursoCriterio eval = new EvaluacionRecursoCriterio(
                            evaluacion.getInt("id"),
                            idEvaluacion,
                            evaluacion.getInt("id_criterio"),
                            evaluacion.getInt("id_indicador"),
                            evaluacion.getInt("aprobado"),
                            evaluacion.getString("creadoAl"),
                            evaluacion.getString("modificadoAl"),
                            evaluacion.getString("borradoAl"));

                    EvaluacionRecursoCriterio existe = linkDB.getEvaluacionRecursoCriterio(idEvaluacion, eval.id_indicador, eval.id_criterio);

                    int id = 0;

                    if (existe == null)// si no existe respuesta para el criterio en el receptor
                    {
                        id = linkDB.insertarEvaluacionRecursoCriterio(eval, linkDB.openDB());

                    } else {// si ya existe respuesta para el criterio en el receptor

                                if (getSobreescribir() == true)
                                {
                                    id = linkDB.actualizarEvaluacionRecursoCriterio(eval, linkDB.openDB());
                                }
                            }


                                    /*
                                    if(id==0)
                                    {
                                        ERRORS++;
                                        error_tipo="NO SE PUDO GUARDAR CRITERIO";
                                        System.out.println("Error No se pudo guardar criterio");
                                    }
                                    */

                }

/////***********************************************************************************************

                JSONArray array_hallazgos = json.getJSONArray("hallazgos");

                for (int i = 0; i < array_hallazgos.length(); i++) {
                    JSONObject json_hallazgo = array_hallazgos.getJSONObject(i);

                    String descripcion = json_hallazgo.getString("descripcion");
                    int idAccion = json_hallazgo.getInt("idAccion");
                    int idIndicador = json_hallazgo.getInt("idIndicador");


                    Hallazgo hallazgo_temp = linkDB.getHallazgo("RECURSO", evaluacion.id, idIndicador,null);

                    if (hallazgo_temp == null) {
                        Hallazgo hallazgo_insertar = new Hallazgo(
                                0,
                                evaluacion.id,
                                "RECURSO",
                                idIndicador,
                                null,
                                user_signed.id,
                                idAccion,
                                0,
                                0,
                                descripcion,
                                link.getFecha(),
                                "0000-00-00 00:00:00",
                                "0000-00-00 00:00:00"
                        );
                        linkDB.insertarHallazgo(hallazgo_insertar, linkDB.openDB());
                    } else {

                        if (getSobreescribir()) {
                            hallazgo_temp.setIdAccion(idAccion);
                            hallazgo_temp.setDescripcion(descripcion);
                            hallazgo_temp.setModificadoAl(link.getFecha());

                            linkDB.actualizarHallazgo(hallazgo_temp, linkDB.openDB());

                        }

                    }

                }///fin hallazgos

///*************************************************************************************************

                    // ADICION DE INDICADORES RECIBIR BLUETO...


                    JSONArray array_indicadores = json.getJSONArray("indicadores");

                    for (int i = 0; i < array_indicadores.length(); i++)
                    {
                        JSONObject json_indicador = array_indicadores.getJSONObject(i);


                        int id=json_indicador.getInt("id");
                        int idEvaluacionRecurso=json_indicador.getInt("idEvaluacionRecurso");
                        int idIndicador=json_indicador.getInt("idIndicador");

                        int totalCriterio=json_indicador.getInt("totalCriterio");
                        int avanceCriterio=json_indicador.getInt("avanceCriterio");


                        //Hallazgo hallazgo_temp = linkDB.getHallazgo("RECURSO", evaluacion.id, idIndicador,null);

                        EvaluacionRecursoIndicador eri_temp = linkDB.getEvaluacionRecursoIndicador(evaluacion.id,idIndicador);

                        if (eri_temp == null) {
                                                    EvaluacionRecursoIndicador eri_insert = new EvaluacionRecursoIndicador(
                                                            0,
                                                            evaluacion.id,
                                                            idIndicador,
                                                            totalCriterio,
                                                            avanceCriterio,
                                                            link.getFecha(),
                                                            "0000-00-00 00:00:00",
                                                            "0000-00-00 00:00:00"
                                                    );
                            linkDB.insertarEvaluacionRecursoIndicador(eri_insert,linkDB.openDB());

                        } else {

                                if (getSobreescribir())
                                                {
                                                     int ac = getAvanceCriterioIndicador(evaluacion.id,idIndicador);
                                                     eri_temp.setAvanceCriterio(ac);

                                                    linkDB.actualizarEvaluacionRecursoIndicador(eri_temp,linkDB.openDB());
                                                }

                                }

                    }///fin indicadores

///*************************************************************************************************


            }catch(JSONException e)
            {
                e.printStackTrace();
                System.out.println("Error JSONException IN DoInBackground");
                ERRORS++;
            }




        }///fin if de errores de conincidencia de clues ó vacios.



        System.out.println("- - - > . RESULT IN POST EXECUTE: " + ERRORS + ".");

        if (ERRORS == 0)
        {
            System.out.println("------- EVALUACION RECIBIDA CORRECTAMENTE -------");

            enviarDatos(socket, "RECIBIDO");

            System.out.println("-- SOCKET QUE ENVIO : [ " + socket.getRemoteDevice().getName() + " ]");

            pintarStatus("RECIBIDO", "");
            contarEvaluacion();
            link.showSnackBar(getApplicationContext(), coordinatorLayout, "Evaluación recibida correctamente", "INFO");


        }else{
                    if(error_tipo.equals("CLUES DISTINTA"))
                    {
                        link.printToast("CLUES DISTINTA",getApplicationContext());

                        enviarDatos(socket, "ENVIO FALLO 1");
                        pintarStatus("ENVIO FALLO 1","");
                    }

                    if(error_tipo.equals("TIPO EVALUACION DIFERENTE"))
                    {
                        link.printToast("TIPO EVALUACION DIFERENTE",getApplicationContext());

                        enviarDatos(socket,"ENVIO FALLO 2");
                        pintarStatus("ENVIO FALLO 2","");
                    }

                    if(error_tipo.equals(""))
                    {
                        enviarDatos(socket,"ENVIO FALLO");
                        pintarStatus("ENVIO FALLO", "");

                        System.out.println("Error enviando. Tipo vacio");
                    }

                    cerrarSocket();
                    link.showSnackBar(getApplicationContext(),coordinatorLayout,"Error recibiendo evaluación","ERROR");
            }


    }

    //**********************************************************************************************


    public void eliminarDatosEnviados()
    {
        //********* BORRAR ENVIADOS

        if(FULL==0)
        {
            int nivel_cone = linkDB.getNivelCone(evaluacion.clues);
            List<Indicador> indicadores = new ArrayList<>();
            List<LugarVerificacion> listaLV = new ArrayList<>();

            indicadores = linkDB.getIndicadoresAgregadosRecurso(evaluacion.id);

            for (int i = 0; i < indicadores.size(); i++) {
                int id_indicador = indicadores.get(i).getId();

                listaLV = linkDB.getLugaresVerificacion(nivel_cone, indicadores.get(i).getId());
                List<Criterio> listaC = new ArrayList<>();

                for (int j = 0; j < listaLV.size(); j++)
                {
                    listaC = linkDB.getCriterios(nivel_cone, indicadores.get(i).getId(), listaLV.get(j).getId());

                    for (int k = 0; k < listaC.size(); k++)
                    {
                        EvaluacionRecursoCriterio eval = linkDB.getEvaluacionRecursoCriterio(evaluacion.getId(), indicadores.get(i).getId(), listaC.get(k).getId());

                        if (eval != null)
                        {
                            eval.setBorradoAl(link.getFecha());
                            linkDB.actualizarEvaluacionRecursoCriterio(eval, linkDB.openDB());
                        }
                    }
                }

                Hallazgo hallazgo = linkDB.getHallazgo("RECURSO", evaluacion.id, id_indicador,null);

                if (hallazgo != null)
                {
                    hallazgo.setBorradoAl(link.getFecha());
                    linkDB.actualizarHallazgo(hallazgo, linkDB.openDB());
                }

            }///FIN RECORRIDO INDICADORES AGREGADOS ELIMINADOS

            //evaluacion.setBorradoAl(link.getFecha());
            //linkDB.actualizarEvaluacionRecurso(evaluacion, linkDB.openDB());

            finish();

            ///******* FIN BORRADO ENVIADOS
        }

        contarEvaluacion();

    }

//*************************************************************************************************
///*************************************************************************************************

    public int getAvanceCriterioIndicador(int idEvaluacion, int idIndicador)
    {
        ContentValues response = new ContentValues();
        int totalCriterio=0;
        int avanceCriterio=0;


        int nivel_cone = linkDB.getNivelCone(evaluacion.clues);

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


        return avanceCriterio;

    }
////************************************************************************************************
////************************************************************************************************




}





class AdaptadorDevice extends ArrayAdapter<Device>
{

    private final Context context;
    private final ArrayList<Device> itemsArrayList;

    public AdaptadorDevice(Context context, ArrayList<Device> itemsArrayList) {

        super(context, R.layout.row_device, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_device, parent, false);


        TextView nombre = (TextView) rowView.findViewById(R.id.nombre);
        TextView mac = (TextView) rowView.findViewById(R.id.mac);


        nombre.setText(itemsArrayList.get(position).getNombre());
        mac.setText(itemsArrayList.get(position).getMac());


        return rowView;
    }
}

