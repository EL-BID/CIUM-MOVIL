package code.yoursoft.ciummovil;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothCalidad extends AppCompatActivity
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
    private AdaptadorDeviceCalidad adaptador;

    //BroadcastReceiver discoveryResult;
    private Handler handler = new Handler();

    ProgressDialog progress_ejecutar_transferencia;



    BroadcastReceiver discoveryResult = new BroadcastReceiver()
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
    TextView text_status, text_clues,text_indicadores, text_expedientes, text_criterios;



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
    String TIPO_EVALUACION="";
    CheckBox checkbox;

    Handler puente;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bluetooth_calidad);

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
            TIPO_EVALUACION= vars.getString("TIPO_EVALUACION");
        }

        evaluacion= linkDB.getEvaluacion("CALIDAD", idEvaluacion,linkDB.openDB());
        if(evaluacion==null)
        {
            System.out.println("ERROR EVALUACION CALIDAD REGRESO NULL....]]]");
        }
        clues_selected=linkDB.getClues(evaluacion.clues);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bluetooth);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


        Resources res = getResources();
        int verde = res.getColor(R.color.PrimaryColor);

        progress_bar.getIndeterminateDrawable().setColorFilter(verde, PorterDuff.Mode.MULTIPLY);



        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        //configureBluetooth();
        bluetooth = BluetoothAdapter.getDefaultAdapter();




        dispositivos = new ArrayList<Device>();
        adaptador = new AdaptadorDeviceCalidad(this,dispositivos);
        list = (ListView) findViewById(R.id.list_discovered);
        list.setAdapter(adaptador);



        image_status = (ImageView) findViewById(R.id.image_status);
        text_status = (TextView)findViewById(R.id.text_status);
        text_clues = (TextView)findViewById(R.id.text_clues);
        text_indicadores = (TextView)findViewById(R.id.text_indicadores);
        text_expedientes = (TextView)findViewById(R.id.text_expedientes);
        text_criterios = (TextView)findViewById(R.id.text_criterios);

        boton_actualizar = (Button) findViewById(R.id.boton_actualizar);
        boton_enviar = (Button) findViewById(R.id.boton_enviar);

        getSupportActionBar().setTitle("Bluetooth");





///*************************************************************************************************


        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View view, final int index, long arg3)
            {


                AsyncTask<Integer, Integer, Integer> connectTask = new AsyncTask<Integer, Integer, Integer>()
                {

                    @Override
                    protected Integer doInBackground(Integer... params)
                    {
                        int res=0;

                        try {
                              //BluetoothDevice device = dispositivos_encontrados.get(params[0]);
                              BluetoothDevice device = dispositivos.get(params[0]).getBluetoothDevice();

                              socket = device.createRfcommSocketToServiceRecord(uuid);
                              socket.connect();
                              res=1;


                            } catch (IOException e) {
                                                      Log.d("BLUETOOTH_CLIENT", e.getMessage());
                                                    }
                        return res;
                    }

                    protected void onProgressUpdate(Integer... values)
                    {
                    }
                    @Override
                    protected void onPostExecute(Integer result)
                    {

                        if(result==1)
                        {
                            if(socket.isConnected())
                            {
                                arrancarHiloEscucha();
                                pintarStatus("CONECTADO A", socket.getRemoteDevice().getName());

                            }else {
                                    cerrarSocket();
                                    pintarStatus("CONEXION FALLO", "");
                                  }
                        }else{
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
            public void onClick(View v)
            {


                System.out.println("\n \n \n --------- SOCKET AL ENVIAR : [ " + socket.hashCode() + " ] -------");

                if(socket.isConnected())
                {
                    pintarStatus("ENVIANDO", "");
//METER EN UN HILO EL ENVIO
                    enviarDatos(socket,getEvaluacionEnviarBluetooth());

                }else{

                       link.showSnackBar(getApplicationContext(),coordinatorLayout,"Se desconectó","ERROR");
                       cerrarSocket();
                       pintarStatus("CONEXION CERRADA","");
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
                        es_mensaje=true;

                        eliminarDatosEnviados();

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

                }/// F I N    S WI T C H



                //if(es_mensaje==false && datos_validos==true)
                if( es_mensaje==false )
                {
                    try{
                            JSONObject json = new JSONObject(datos);

                            int indicadoresTemp = json.getInt("INDICADORES");
                            int expedientesTemp = json.getInt("EXPEDIENTES");
                            int criteriosTemp = json.getInt("CRITERIOS");

                            showDialogAceptarTransferencia(datos,indicadoresTemp,expedientesTemp,criteriosTemp);

                        }catch(JSONException err){
                                                    System.out.println("ERROR CONVIRTIENDO CONTADRES DE DATOS...");
                                                 }


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

            pintarStatus("ESCUCHANDO", "");
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


        bsl = new BluetoothSocketListener(socket, handler, messageText);
        messageListener = new Thread(bsl);
        messageListener.start();



    }

///*************************************************************************************************

    private void enviarDatos(BluetoothSocket socket, String msg)
    {
        OutputStream outStream;
        try {
               outStream = socket.getOutputStream();

               byte[] byteString = (msg + "J").getBytes();
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
                    link.showSnackBar(this, coordinatorLayout, "Reiniciando conexiones", "INFO");
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

                    AsyncTask<Integer, Void, BluetoothSocket> acceptThread = new AsyncTask<Integer, Void, BluetoothSocket>()
                    {
                                @Override
                                protected BluetoothSocket doInBackground(Integer ...params)
                                {
                                    try {
                                          socket = btserver.accept(params[0]*1000);
                                          return socket;
                                        } catch (IOException e) {
                                                                  Log.d("BLUETOOTH", e.getMessage());
                                                                }
                                    return null;
                                }
                                @Override
                                protected void onPostExecute(BluetoothSocket result)
                                {
                                    if (result != null)
                                    {
                                        arrancarHiloEscucha();
                                        pintarStatus("CONECTADO POR", socket.getRemoteDevice().getName());

                                    }else {
                                             pintarStatus("CONEXION FALLO","");
                                             cerrarSocket();
                                             link.showSnackBar(getApplicationContext(),coordinatorLayout,"No se pudo recibir conexion","ERROR");
                                          }

                                }
                    };

                    cerrarSocket();
                    acceptThread.execute(resultCode);

                } catch (IOException e) {
                                          Log.d("BLUETOOTH", e.getMessage());
                                        }
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
        private TextView textView;
        private Handler handler;

        public BluetoothSocketListener(BluetoothSocket socket, Handler handler, TextView textView)
        {

            this.socket = socket;
            this.textView = textView;
            this.handler = handler;
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
                            if(token.equals("J")){ seguir_leyendo=false; }

                            while ( seguir_leyendo==true )
                            {

                                message = message + new String(buffer, 0, bytesRead);
                                bytesRead = instream.read(buffer);

                                System.out.println(" [ IN WHILE ] TAMAÑO DE BUFER ------> [" + bytesRead + "] MENSAJE ARMADO --> DIM [ "+message.length()+" ]  ");

                                String trama = new String(buffer, 0, bytesRead);
                                String fin= trama.substring( trama.length()-1 , trama.length() );

                                System.out.println(" [ TRAMA ] --> [ "+trama+" ] \n");
                                System.out.println(" [ ULTIMO CARACTER ] --> [ "+fin+" ]");

                                if(fin.equals("J")){ seguir_leyendo=false; }

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


    public String getEvaluacionEnviarBluetooth()
    {

        int errors=0;
        String messages="";
        String evaluacion_string="";

        int x_indicadores = 0;
        int x_expedientes = 0;
        int x_criterios = 0;
        int x_respondidos = 0;


        JSONObject evaluacion_json = new JSONObject();

                JSONArray array_registros = new JSONArray();
                JSONObject registro = new JSONObject();

                JSONArray array_criterios = new JSONArray();
                JSONObject criterio;


                JSONArray array_hallazgos = new JSONArray();
                JSONObject hallazgo_json;

                JSONObject objeto_padre_hallazgos = new JSONObject();


                JSONArray array_cvr_total = new JSONArray();
                JSONArray array_cvr = new JSONArray();
                JSONObject cvr_json;


                List<Indicador> indicadores = new ArrayList<>();

                indicadores = linkDB.getIndicadoresAgregadosCalidad(evaluacion.id);

                for (int i = 0; i < indicadores.size(); i++)
                {

                    int id_indicador = indicadores.get(i).getId();
                    x_indicadores++;

                    List<EvaluacionCalidadRegistro> lista_ecr = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion.id, id_indicador);

                    for (int j = 0; j < lista_ecr.size(); j++)
                    {

                        array_criterios = new JSONArray();
                        List<EvaluacionCalidadCriterio> lista_ecc = linkDB.getEvaluacionCalidadCriterio(lista_ecr.get(j).id);

                        for (int k = 0; k < lista_ecc.size(); k++)
                        {
                            criterio = new JSONObject();

                            try {
                                    criterio.put("idCriterio", lista_ecc.get(k).idCriterio);
                                    criterio.put("idIndicador", lista_ecc.get(k).idIndicador);
                                    criterio.put("aprobado", lista_ecc.get(k).aprobado);

                                    array_criterios.put(criterio);

                                    x_criterios++;

                                } catch (JSONException e) {
                                                              e.printStackTrace();
                                                          }
                        }


                        registro = new JSONObject();

                        try {
                                registro.put("idIndicador", lista_ecr.get(j).idIndicador);
                                registro.put("expediente", lista_ecr.get(j).expediente);
                                registro.put("columna", 0);
                                registro.put("cumple", lista_ecr.get(j).cumple);
                                registro.put("promedio", lista_ecr.get(j).promedio);
                                registro.put("totalCriterio", lista_ecr.get(j).totalCriterio);

                                registro.put("avanceCriterio", lista_ecr.get(j).avanceCriterio);

                                registro.put("criterios", array_criterios);

                                x_expedientes++;

                            } catch (JSONException e) {
                                                          e.printStackTrace();
                                                      }

                        array_registros.put(registro);


                        /////// ADICION ENVIAR RESPUESTAS VALIDACIONES *****************************
                        ///////*********************************************************************

                        List<CriterioValidacionRespuesta> lista_cvr = linkDB.getListaCriterioValidacionRespuestaTotalPorEvaluacion("CALIDAD",evaluacion.id,lista_ecr.get(j).expediente);

                        array_cvr = new JSONArray();
                        ///System.out.print("TAMANO DE LISTA DE VALIDACIONES : "+lista_cvr.size()+"."+lista_cvr.toString()+"----->");

                        for(int zz=0; zz<lista_cvr.size(); zz++)
                        {
                            cvr_json = new JSONObject();
                            try {
                                cvr_json.put("expediente",lista_cvr.get(zz).getExpediente());
                                cvr_json.put("idCriterio",lista_cvr.get(zz).getIdCriterio());
                                cvr_json.put("idCriterioValidacion",lista_cvr.get(zz).getIdCriterioValidacion());
                                cvr_json.put("tipo",lista_cvr.get(zz).getTipo());
                                cvr_json.put("respuesta1",lista_cvr.get(zz).getRespuesta1());
                                cvr_json.put("respuesta2",lista_cvr.get(zz).getRespuesta2());

                                array_cvr.put(cvr_json);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                errors++;
                                messages += "Error consiguiendo las validaciones de criterios.";
                            }

                        }

                        array_cvr_total.put(array_cvr);


                        ///////*********************************************************************


                        Hallazgo hallazgo = linkDB.getHallazgo("CALIDAD",evaluacion.id, id_indicador,lista_ecr.get(j).getExpediente());

                        if (hallazgo != null)
                        {
                            hallazgo_json = new JSONObject();

                            try {
                                    hallazgo_json.put("descripcion", hallazgo.getDescripcion());
                                    hallazgo_json.put("idAccion", hallazgo.getIdAccion());
                                    hallazgo_json.put("idIndicador", hallazgo.getIdIndicador());
                                    hallazgo_json.put("expediente", hallazgo.getExpediente());

                                    array_hallazgos.put(hallazgo_json);

                                } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            errors++;
                                                            messages += "Error consiguiendo hallazgos";
                                                          }
                        }


                        ///////*********************************************************************

                    }  /// FIN LISTA REGISTROS







                }// fin for indicadores evaluacion

                try {
                        evaluacion_json.put("id", evaluacion.getId());
                        evaluacion_json.put("idUsuario", evaluacion.getIdUsuario());

                        evaluacion_json.put("tipo","CALIDAD");

                        evaluacion_json.put("clues", evaluacion.getClues());
                        evaluacion_json.put("fechaEvaluacion", evaluacion.getFechaEvaluacion());
                        evaluacion_json.put("cerrado", evaluacion.getCerrado());
                        evaluacion_json.put("firma", evaluacion.getFirma());
                        evaluacion_json.put("responsable", evaluacion.getResponsable());

                        evaluacion_json.put("registros", array_registros);
                        evaluacion_json.put("hallazgos", array_hallazgos);

                        evaluacion_json.put("criterio_respuestas",array_cvr_total);

                        evaluacion_json.put("INDICADORES",x_indicadores);
                        evaluacion_json.put("EXPEDIENTES",x_expedientes);
                        evaluacion_json.put("CRITERIOS", x_criterios);

                        //array_evaluaciones_post.put(evaluacion_json);

                    } catch (JSONException e) {
                                                e.printStackTrace();
                                                errors++;
                                                messages += "Error consiguiendo detalles de evaluación " + evaluacion.clues;
                                              }

          ///  }   // fin for recorrido del total de evaluaciones

             System.out.println("EVALUACIONES POST A ENVIAR : " + evaluacion_json.toString());

             //git chch   ramirez.esquinca@gmail.com     mimoj98i
             //return evaluacion_string;

        return evaluacion_json.toString();

    }



///************************************************************************************************


    public void contarEvaluacion()
    {
        int x_indicadores=0;
        int x_criterios=0;
        int x_expedientes=0;
        int x_respondidos=0;


        int nivel_cone = linkDB.getNivelCone(evaluacion.clues);

        List<Indicador> indicadores = new ArrayList<>();


        indicadores= linkDB.getIndicadoresAgregadosCalidad(evaluacion.id);

        for(int i=0; i<indicadores.size(); i++)
        {
            x_indicadores++;
            int idIndicador=indicadores.get(i).getId();

            List<EvaluacionCalidadRegistro> lista_ecr = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion.id, idIndicador);
            for (int j = 0; j < lista_ecr.size(); j++)
            {
                x_expedientes++;
                int idECR=lista_ecr.get(j).getId();

                List<LugarVerificacion> listaLV = new ArrayList<>();
                listaLV = linkDB.getLugaresVerificacion(nivel_cone,idIndicador);


                for (int k = 0; k < listaLV.size(); k++)
                {
                    int idLV=listaLV.get(k).getId();

                    List<Criterio> listaC = new ArrayList<>();
                    listaC = linkDB.getCriterios(nivel_cone, idIndicador, idLV);

                    for (int z = 0; z < listaC.size(); z++)
                    {
                        x_criterios++;

                        int idCriterio=listaC.get(z).getId();

                        EvaluacionCalidadCriterio eval = linkDB.getEvaluacionCalidadCriterio(evaluacion.id,idIndicador,idCriterio,idECR);

                        if (eval != null)
                        {
                            x_respondidos++;
                        }

                    }


                }
            }

        }

    text_clues.setText("Clues : "+evaluacion.clues);
    text_indicadores.setText("Indicadores : "+x_indicadores);
    text_expedientes.setText("Expedientes : "+x_expedientes);
    text_criterios.setText("Criterios : "+x_respondidos+" / "+x_criterios);

    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);

        return true;
    }

///****************************************************************************************************

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

            if(MODO.equals("EMISOR"))
            {
               buscarDispositivos();
            }else{
                    escucharConexion();
                 }

            pintarStatus("REINICIAR","");

            return true;
        }






        return super.onOptionsItemSelected(item);
    }

//**************************************************************************************************
    public void showDialogAceptarTransferencia(final String datos,int indicadores,int expedientes,int criterios)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmar Transferencia").setMessage("Se recibiran: \n"+indicadores+" indicadores, \n"+expedientes+" expedientes, \n"+criterios+" criterios .\n \n¿ Desea continuar ?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //ejecutarTransferencia(datos);
                        new EjecutarTransferenciaAsync().execute(datos);
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

//**************************************************************************************************



///*****************************************************************************************************************************************************
///*****************************************************************************************************************************************************


    private class EjecutarTransferenciaAsync extends AsyncTask<String, Integer, String[]>
    {
        protected void onPreExecute()
        {

            progress_ejecutar_transferencia = new ProgressDialog(BluetoothCalidad.this);
            progress_ejecutar_transferencia.setTitle("Bluetooth");
            progress_ejecutar_transferencia.setMessage("Recibiendo evaluación...");
            progress_ejecutar_transferencia.setCancelable(false);
            progress_ejecutar_transferencia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress_ejecutar_transferencia.setIndeterminate(true);

            progress_ejecutar_transferencia.show();

        }

        protected String[] doInBackground(String... datos)
        {

            String response []={"0",".","0","0","0"};



            ///*******************************************************************************************************************************************

            int ERRORS=0;
            String error_tipo="";

            try {
                //System.out.println("----------------- INICIA TRAMA RECIBIDA -----------------\n");
                //System.out.println("-----------------------[[ "+params[0].toString()+" ]]");
                //System.out.println("------------------- TERMINA TRAMA RECIBIDA ---------------\n");

                JSONObject json = new JSONObject(datos[0].toString());

                String clues_recibida = json.getString("clues");
                String tipo_evaluacion = json.getString("tipo");

                if(clues_recibida.equals(evaluacion.clues))
                { }else{
                    ERRORS++;
                    error_tipo="CLUES DISTINTA";
                }

                if(tipo_evaluacion.equals("CALIDAD"))
                { }else{
                    ERRORS++;
                    error_tipo="TIPO EVALUACION DIFERENTE";
                }




                JSONArray array_registros=json.getJSONArray("registros");


                for(int i=0; i<array_registros.length(); i++)
                {
                    JSONObject json_registro = array_registros.getJSONObject(i);

                    int idIndicador = json_registro.getInt("idIndicador");
                    String expediente = json_registro.getString("expediente");
                    int columna = json_registro.getInt("columna");
                    int cumple = json_registro.getInt("cumple");
                    Double promedio = json_registro.getDouble("promedio");
                    int totalCriterio = json_registro.getInt("totalCriterio");
                    int avanceCriterio = json_registro.getInt("avanceCriterio");

                    EvaluacionCalidadRegistro ecr = linkDB.getEvaluacionCalidadRegistro(evaluacion.id, idIndicador, expediente);
                    int idECR=0;

                    if(ecr == null)
                    {
                        ///insertar nuevo evaluacion calidad registro osea expediente nuevo
                        EvaluacionCalidadRegistro ecr_insertar = new EvaluacionCalidadRegistro(
                                0,
                                evaluacion.id,
                                idIndicador,
                                0,
                                expediente,
                                cumple,
                                promedio,
                                totalCriterio,
                                avanceCriterio,
                                link.getFecha(),
                                "0000-00-00 00:00:00",
                                "0000-00-00 00:00:00"
                        );

                        idECR = linkDB.insertarEvaluacionCalidadRegistro(ecr_insertar,linkDB.openDB());

                    }else{
                        idECR = ecr.getId();

                        if(getSobreescribir())
                        {
                            ecr.setCumple(cumple);
                            ecr.setPromedio(promedio);
                            ecr.setTotalCriterio(totalCriterio);
                            ecr.setAvanceCriterio(avanceCriterio);
                            ecr.setModificadoAl(link.getFecha());

                            linkDB.actualizarEvaluacionCalidadRegistro(ecr,linkDB.openDB());
                        }
                    }


                    JSONArray array_criterios= json_registro.getJSONArray("criterios");
                    for(int j=0; j<array_criterios.length(); j++)
                    {
                        JSONObject json_criterio = array_criterios.getJSONObject(j);

                        int idCriterio2 = json_criterio.getInt("idCriterio");
                        int idIndicador2 = json_criterio.getInt("idIndicador");
                        int aprobado = json_criterio.getInt("aprobado");

                        EvaluacionCalidadCriterio ecc = linkDB.getEvaluacionCalidadCriterio(evaluacion.id,idIndicador2,idCriterio2,idECR);

                        if(ecc == null)
                        {
                            EvaluacionCalidadCriterio ecc_insertar = new EvaluacionCalidadCriterio(
                                    0,
                                    evaluacion.id,
                                    idCriterio2,
                                    idIndicador2,
                                    aprobado,
                                    idECR,
                                    link.getFecha(),
                                    "0000-00-00 00:00:00",
                                    "0000-00-00 00:00:00"
                            );

                            linkDB.insertarEvaluacionCalidadCriterio(ecc_insertar,linkDB.openDB());

                        }else{
                            if(getSobreescribir())
                            {
                                ecc.setAprobado(aprobado);
                                ecc.setModificadoAl(link.getFecha());

                                linkDB.actualizarEvaluacionCalidadCriterio(ecc, linkDB.openDB());
                            }

                        }

                    }



                }



                JSONArray array_cvr_total=json.getJSONArray("criterio_respuestas");
                JSONArray array_cvr;

                for (int g=0; g<array_cvr_total.length(); g++)
                {
                    array_cvr = array_cvr_total.getJSONArray(g);

                    for(int h=0; h<array_cvr.length(); h++)
                    {

                        JSONObject cvr_json = array_cvr.getJSONObject(h);

                        CriterioValidacionRespuesta cvr_insert = new CriterioValidacionRespuesta(

                                0,
                                idEvaluacion,
                                cvr_json.getString("expediente"),
                                cvr_json.getInt("idCriterio"),
                                cvr_json.getInt("idCriterioValidacion"),
                                cvr_json.getString("tipo"),
                                cvr_json.getString("respuesta1"),
                                cvr_json.getString("respuesta2"),
                                link.getFecha(),
                                link.getFecha(),
                                "0000-00-00 00:00:00"
                        );

                        linkDB.insertarCriterioValidacionRespuesta(cvr_insert,linkDB.openDB());
                    }
                }



                JSONArray array_hallazgos=json.getJSONArray("hallazgos");

                for(int i=0; i<array_hallazgos.length(); i++)
                {
                    JSONObject json_hallazgo = array_hallazgos.getJSONObject(i);

                    String descripcion = json_hallazgo.getString("descripcion");
                    int idAccion = json_hallazgo.getInt("idAccion");
                    int idIndicador = json_hallazgo.getInt("idIndicador");
                    String expediente = json_hallazgo.getString("expediente");


                    Hallazgo hallazgo_temp=linkDB.getHallazgo("CALIDAD",evaluacion.id,idIndicador,expediente);

                    if(hallazgo_temp == null)
                    {
                        Hallazgo hallazgo_insertar=new Hallazgo(
                                0,
                                evaluacion.id,
                                "CALIDAD",
                                idIndicador,
                                expediente,
                                user_signed.id,
                                idAccion,
                                0,
                                0,
                                descripcion,
                                link.getFecha(),
                                "0000-00-00 00:00:00",
                                "0000-00-00 00:00:00"
                        );
                        linkDB.insertarHallazgo(hallazgo_insertar,linkDB.openDB());
                    }else{

                        if(getSobreescribir())
                        {
                            hallazgo_temp.setIdAccion(idAccion);
                            hallazgo_temp.setDescripcion(descripcion);
                            hallazgo_temp.setModificadoAl(link.getFecha());

                            linkDB.actualizarHallazgo(hallazgo_temp,linkDB.openDB());

                        }

                    }

                }///fin hallazgos



                ////************************************************************************************




                List<Indicador> listaI = new ArrayList<>();

                //listaI =linkDB.getIndicadoresEvaluacionRecurso(nivel_cone);

                listaI = linkDB.getIndicadoresAgregadosCalidad(evaluacion.id);

                for(int i=0; i<listaI.size(); i++)
                {
                    int id_indicador=listaI.get(i).id;

                    List<EvaluacionCalidadRegistro> expedientes = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion.id, id_indicador);

                    for(int j=0; j<expedientes.size(); j++ )
                    {
                        int id_ecr = expedientes.get(j).id;

                        int preguntas = 0;
                        int respondidas = 0;
                        int preguntas_pendientes=0;
                        int aprobadas = 0;
                        int negativas = 0;
                        int nas = 0;

                        int nivel_cone=linkDB.getNivelCone(evaluacion.clues);
                        List<LugarVerificacion> listaLV = new ArrayList<>();
                        listaLV = linkDB.getLugaresVerificacion(nivel_cone,id_indicador);

                        for (int k = 0; k < listaLV.size(); k++)
                        {
                            int id_lugar = listaLV.get(k).id;

                            List<Criterio> listaC = new ArrayList<>();
                            listaC = linkDB.getCriterios(nivel_cone,id_indicador, id_lugar);

                            for (int h = 0; h < listaC.size(); h++)
                            {
                                int id_criterio = listaC.get(h).id;

                                preguntas++;

                                EvaluacionCalidadCriterio ecc = linkDB.getEvaluacionCalidadCriterio(evaluacion.id, id_indicador, id_criterio, id_ecr);

                                if (ecc == null)
                                {
                                    preguntas_pendientes++;
                                }else{
                                    respondidas ++;

                                    if(ecc.aprobado==1)
                                    {
                                        aprobadas++;
                                    }
                                    if(ecc.aprobado==0)
                                    {
                                        negativas++;
                                    }
                                    if(ecc.aprobado==2)
                                    {
                                        nas++;
                                    }

                                }

                            }///fin de la lista de criterios

                        }//fin lista de lugares de verificacion


                        ///AQUI SE ACTUALIZA CADA EXPEDIENTE POR INDICADOR
                        int cumple;
                        if(negativas>0)
                        { cumple=0; }else{ cumple=1; }

                        double promedio=( (aprobadas+nas) / preguntas ) * 100;
                        EvaluacionCalidadRegistro ecr_temp =  linkDB.getEvaluacionCalidadRegistro(id_ecr);

                        ecr_temp.setCumple(cumple);
                        ecr_temp.setPromedio(promedio);
                        ecr_temp.setTotalCriterio(preguntas);

                        ecr_temp.setModificadoAl(link.getFecha());

                        linkDB.actualizarEvaluacionCalidadRegistro(ecr_temp, linkDB.openDB());


                    }/// fin expedientes

                }///fin de la lista de indicadores pertenecientes a la evaluacion




                ////***********************************************************************************




            } catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                        ERRORS++;
                                    }



            ///************************************************************************************************************************************************************


            int x_indicadores=0;
            int x_criterios=0;
            int x_expedientes=0;
            int x_respondidos=0;

            int total_criterio=0;
            int avance_criterio=0;

            int nivel_cone = linkDB.getNivelCone(evaluacion.clues);
            List<Indicador> indicadores = new ArrayList<>();
            indicadores= linkDB.getIndicadoresAgregadosCalidad(evaluacion.id);

            for(int i=0; i<indicadores.size(); i++)
            {
                x_indicadores++;
                int idIndicador=indicadores.get(i).getId();

                List<EvaluacionCalidadRegistro> lista_ecr = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion.id, idIndicador);

                for (int j = 0; j < lista_ecr.size(); j++)
                {
                    x_expedientes++;
                    int idECR=lista_ecr.get(j).getId();

                    List<LugarVerificacion> listaLV = new ArrayList<>();
                    listaLV = linkDB.getLugaresVerificacion(nivel_cone,idIndicador);

                    total_criterio=0;
                    avance_criterio=0;

                    for (int k = 0; k < listaLV.size(); k++)
                    {
                        int idLV=listaLV.get(k).getId();

                        List<Criterio> listaC = new ArrayList<>();
                        listaC = linkDB.getCriterios(nivel_cone, idIndicador, idLV);

                        for (int z = 0; z < listaC.size(); z++)
                        {
                            x_criterios++;
                            total_criterio++;

                            int idCriterio=listaC.get(z).getId();

                            EvaluacionCalidadCriterio eval = linkDB.getEvaluacionCalidadCriterio(evaluacion.id,idIndicador,idCriterio,idECR);

                            if (eval != null)
                            {
                                x_respondidos++;
                                avance_criterio++;
                            }
                        }
                    }

                    EvaluacionCalidadRegistro expediente = lista_ecr.get(j);

                    expediente.setAvanceCriterio(avance_criterio);
                    linkDB.actualizarEvaluacionCalidadRegistro(expediente,linkDB.openDB());

                    ////UPDATE EVALUACION CALIDAD REGISTRO AQUI

                }

            }

            ///*************************************************************************************************************************************************************
            response[0]=""+ERRORS;
            response[1]=""+error_tipo;
            response[2]=""+x_indicadores;
            response[3]=""+x_expedientes;
            response[4]=x_respondidos+" / "+x_criterios;


            return response;
        }




        protected void onProgressUpdate(Integer... values)
        {
        }

        protected void onPostExecute(String[] status)
        {
            progress_ejecutar_transferencia.dismiss();
            int ERRORS=Integer.parseInt(status[0]);
            String error_tipo=status[1];



            text_clues.setText("Clues : "+evaluacion.clues);
            text_indicadores.setText("Indicadores : "+status[2]);
            text_expedientes.setText("Expedientes : "+status[3]);
            text_criterios.setText("Criterios : "+status[4]);


            if (ERRORS == 0)
            {
                System.out.println("------- EVALUACION RECIBIDA CORRECTAMENTE -------");

                enviarDatos(socket, "RECIBIDO");

                System.out.println("-- SOCKET QUE ENVIO : [ " + socket.getRemoteDevice().getName() + " ]");

                pintarStatus("RECIBIDO", "");
                //contarEvaluacion();
                link.showSnackBar(getApplicationContext(), coordinatorLayout, "Evaluación recibida correctamente", "INFO");


            }else {
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
                    pintarStatus("ENVIO FALLO","");
                }

                cerrarSocket();
                link.showSnackBar(getApplicationContext(),coordinatorLayout,"Error recibiendo evaluación","ERROR");
            }


        }

        protected void onCancelled()
        {
            link.showSnackBar(BluetoothCalidad.this,coordinatorLayout,"Cancelado","ERROR");
        }

    }///FIN CLASS REFRESH INDICADORES IN ASYNC




//******************************************************************************************************************************************************
//******************************************************************************************************************************************************




public void ejecutarTransferencia(String datos)
{


    ///**********************************************************************************************

    int ERRORS=0;
    String error_tipo="";

    try {
        //System.out.println("----------------- INICIA TRAMA RECIBIDA -----------------\n");
        //System.out.println("-----------------------[[ "+params[0].toString()+" ]]");
        //System.out.println("------------------- TERMINA TRAMA RECIBIDA ---------------\n");

        JSONObject json = new JSONObject(datos.toString());

        String clues_recibida = json.getString("clues");
        String tipo_evaluacion = json.getString("tipo");

        if(clues_recibida.equals(evaluacion.clues))
        { }else{
            ERRORS++;
            error_tipo="CLUES DISTINTA";
        }

        if(tipo_evaluacion.equals("CALIDAD"))
        { }else{
            ERRORS++;
            error_tipo="TIPO EVALUACION DIFERENTE";
        }




        JSONArray array_registros=json.getJSONArray("registros");


        for(int i=0; i<array_registros.length(); i++)
        {
            JSONObject json_registro = array_registros.getJSONObject(i);

            int idIndicador = json_registro.getInt("idIndicador");
            String expediente = json_registro.getString("expediente");
            int columna = json_registro.getInt("columna");
            int cumple = json_registro.getInt("cumple");
            Double promedio = json_registro.getDouble("promedio");
            int totalCriterio = json_registro.getInt("totalCriterio");
            int avanceCriterio = json_registro.getInt("avanceCriterio");

            EvaluacionCalidadRegistro ecr = linkDB.getEvaluacionCalidadRegistro(evaluacion.id, idIndicador, expediente);
            int idECR=0;

            if(ecr == null)
            {
                ///insertar nuevo evaluacion calidad registro osea expediente nuevo
                EvaluacionCalidadRegistro ecr_insertar = new EvaluacionCalidadRegistro(
                                                                                            0,
                                                                                            evaluacion.id,
                                                                                            idIndicador,
                                                                                            0,
                                                                                            expediente,
                                                                                            cumple,
                                                                                            promedio,
                                                                                            totalCriterio,
                                                                                            avanceCriterio,
                                                                                            link.getFecha(),
                                                                                            "0000-00-00 00:00:00",
                                                                                            "0000-00-00 00:00:00"
                                                                                    );

                idECR = linkDB.insertarEvaluacionCalidadRegistro(ecr_insertar,linkDB.openDB());

            }else{
                    idECR = ecr.getId();

                    if(getSobreescribir())
                    {
                        ecr.setCumple(cumple);
                        ecr.setPromedio(promedio);
                        ecr.setTotalCriterio(totalCriterio);
                        ecr.setAvanceCriterio(avanceCriterio);
                        ecr.setModificadoAl(link.getFecha());

                        linkDB.actualizarEvaluacionCalidadRegistro(ecr,linkDB.openDB());
                    }
                }


            JSONArray array_criterios= json_registro.getJSONArray("criterios");
            for(int j=0; j<array_criterios.length(); j++)
            {
                JSONObject json_criterio = array_criterios.getJSONObject(j);

                int idCriterio2 = json_criterio.getInt("idCriterio");
                int idIndicador2 = json_criterio.getInt("idIndicador");
                int aprobado = json_criterio.getInt("aprobado");

                EvaluacionCalidadCriterio ecc = linkDB.getEvaluacionCalidadCriterio(evaluacion.id,idIndicador2,idCriterio2,idECR);

                if(ecc == null)
                {
                    EvaluacionCalidadCriterio ecc_insertar = new EvaluacionCalidadCriterio(
                                                                                                    0,
                                                                                                    evaluacion.id,
                                                                                                    idCriterio2,
                                                                                                    idIndicador2,
                                                                                                    aprobado,
                                                                                                    idECR,
                                                                                                    link.getFecha(),
                                                                                                    "0000-00-00 00:00:00",
                                                                                                    "0000-00-00 00:00:00"
                                                                                            );

                    linkDB.insertarEvaluacionCalidadCriterio(ecc_insertar,linkDB.openDB());

                }else{
                        if(getSobreescribir())
                        {
                            ecc.setAprobado(aprobado);
                            ecc.setModificadoAl(link.getFecha());

                            linkDB.actualizarEvaluacionCalidadCriterio(ecc, linkDB.openDB());
                        }

                    }

            }



        }



        JSONArray array_cvr_total=json.getJSONArray("criterio_respuestas");
        JSONArray array_cvr;

        for (int g=0; g<array_cvr_total.length(); g++)
        {
            array_cvr = array_cvr_total.getJSONArray(g);

            for(int h=0; h<array_cvr.length(); h++)
            {

                JSONObject cvr_json = array_cvr.getJSONObject(h);

                CriterioValidacionRespuesta cvr_insert = new CriterioValidacionRespuesta(

                        0,
                        idEvaluacion,
                        cvr_json.getString("expediente"),
                        cvr_json.getInt("idCriterio"),
                        cvr_json.getInt("idCriterioValidacion"),
                        cvr_json.getString("tipo"),
                        cvr_json.getString("respuesta1"),
                        cvr_json.getString("respuesta2"),
                        link.getFecha(),
                        link.getFecha(),
                        "0000-00-00 00:00:00"
                );

                linkDB.insertarCriterioValidacionRespuesta(cvr_insert,linkDB.openDB());
            }
        }



        JSONArray array_hallazgos=json.getJSONArray("hallazgos");

        for(int i=0; i<array_hallazgos.length(); i++)
        {
            JSONObject json_hallazgo = array_hallazgos.getJSONObject(i);

            String descripcion = json_hallazgo.getString("descripcion");
            int idAccion = json_hallazgo.getInt("idAccion");
            int idIndicador = json_hallazgo.getInt("idIndicador");
            String expediente = json_hallazgo.getString("expediente");


            Hallazgo hallazgo_temp=linkDB.getHallazgo("CALIDAD",evaluacion.id,idIndicador,expediente);

            if(hallazgo_temp == null)
            {
                Hallazgo hallazgo_insertar=new Hallazgo(
                        0,
                        evaluacion.id,
                        "CALIDAD",
                        idIndicador,
                        expediente,
                        user_signed.id,
                        idAccion,
                        0,
                        0,
                        descripcion,
                        link.getFecha(),
                        "0000-00-00 00:00:00",
                        "0000-00-00 00:00:00"
                );
                linkDB.insertarHallazgo(hallazgo_insertar,linkDB.openDB());
            }else{

                if(getSobreescribir())
                {
                    hallazgo_temp.setIdAccion(idAccion);
                    hallazgo_temp.setDescripcion(descripcion);
                    hallazgo_temp.setModificadoAl(link.getFecha());

                    linkDB.actualizarHallazgo(hallazgo_temp,linkDB.openDB());

                }

            }

        }///fin hallazgos



        ////************************************************************************************




        List<Indicador> listaI = new ArrayList<>();

        //listaI =linkDB.getIndicadoresEvaluacionRecurso(nivel_cone);

        listaI = linkDB.getIndicadoresAgregadosCalidad(evaluacion.id);

        for(int i=0; i<listaI.size(); i++)
        {
            int id_indicador=listaI.get(i).id;

            List<EvaluacionCalidadRegistro> expedientes = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion.id, id_indicador);

            for(int j=0; j<expedientes.size(); j++ )
            {
                int id_ecr = expedientes.get(j).id;

                int preguntas = 0;
                int respondidas = 0;
                int preguntas_pendientes=0;
                int aprobadas = 0;
                int negativas = 0;
                int nas = 0;

                int nivel_cone=linkDB.getNivelCone(evaluacion.clues);
                List<LugarVerificacion> listaLV = new ArrayList<>();
                listaLV = linkDB.getLugaresVerificacion(nivel_cone,id_indicador);

                for (int k = 0; k < listaLV.size(); k++)
                {
                    int id_lugar = listaLV.get(k).id;

                    List<Criterio> listaC = new ArrayList<>();
                    listaC = linkDB.getCriterios(nivel_cone,id_indicador, id_lugar);

                    for (int h = 0; h < listaC.size(); h++)
                    {
                        int id_criterio = listaC.get(h).id;

                        preguntas++;

                        EvaluacionCalidadCriterio ecc = linkDB.getEvaluacionCalidadCriterio(evaluacion.id, id_indicador, id_criterio, id_ecr);

                        if (ecc == null)
                        {
                            preguntas_pendientes++;
                        }else{
                            respondidas ++;

                            if(ecc.aprobado==1)
                            {
                                aprobadas++;
                            }
                            if(ecc.aprobado==0)
                            {
                                negativas++;
                            }
                            if(ecc.aprobado==2)
                            {
                                nas++;
                            }

                        }

                    }///fin de la lista de criterios

                }//fin lista de lugares de verificacion


                ///AQUI SE ACTUALIZA CADA EXPEDIENTE POR INDICADOR
                int cumple;
                if(negativas>0)
                { cumple=0; }else{ cumple=1; }

                double promedio=( (aprobadas+nas) / preguntas ) * 100;
                EvaluacionCalidadRegistro ecr_temp =  linkDB.getEvaluacionCalidadRegistro(id_ecr);

                ecr_temp.setCumple(cumple);
                ecr_temp.setPromedio(promedio);
                ecr_temp.setTotalCriterio(preguntas);

                ecr_temp.setModificadoAl(link.getFecha());

                linkDB.actualizarEvaluacionCalidadRegistro(ecr_temp, linkDB.openDB());


            }/// fin expedientes

        }///fin de la lista de indicadores pertenecientes a la evaluacion




        ////***********************************************************************************















    } catch (JSONException e)
    {
        e.printStackTrace();
        ERRORS++;
    }


    if (ERRORS == 0)
    {
        System.out.println("------- EVALUACION RECIBIDA CORRECTAMENTE -------");

        enviarDatos(socket, "RECIBIDO");


        System.out.println("-- SOCKET QUE ENVIO : [ " + socket.getRemoteDevice().getName() + " ]");


        pintarStatus("RECIBIDO", "");
        contarEvaluacion();
        link.showSnackBar(getApplicationContext(), coordinatorLayout, "Evaluación recibida correctamente", "INFO");






    }else {
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
                    pintarStatus("ENVIO FALLO","");
                }

                cerrarSocket();
                link.showSnackBar(getApplicationContext(),coordinatorLayout,"Error recibiendo evaluación","ERROR");
        }
    ///***************************************************************************************************

 }




///*************************************************************************************************
    public void eliminarDatosEnviados()
    {



        List<Indicador> indicadores = new ArrayList<>();

        indicadores = linkDB.getIndicadoresAgregadosCalidad(evaluacion.id);

        for (int i = 0; i < indicadores.size(); i++)
        {
            int id_indicador = indicadores.get(i).getId();

            List<EvaluacionCalidadRegistro> lista_ecr = linkDB.getExpedientesEvaluacionCalidadRegistro(evaluacion.id, id_indicador);

            for (int j = 0; j < lista_ecr.size(); j++)
            {

                int id_registro_expediente=lista_ecr.get(j).id;

                List<EvaluacionCalidadCriterio> lista_ecc = linkDB.getEvaluacionCalidadCriterio(id_registro_expediente);

                for (int k = 0; k < lista_ecc.size(); k++)
                {
                    EvaluacionCalidadCriterio ecc_temp=lista_ecc.get(k);

                    ecc_temp.setBorradoAl(link.getFecha());
                    linkDB.actualizarEvaluacionCalidadCriterio(ecc_temp,linkDB.openDB());
                }

                EvaluacionCalidadRegistro ecr_temp=lista_ecr.get(j);

                ecr_temp.setBorradoAl(link.getFecha());
                linkDB.actualizarEvaluacionCalidadRegistro(ecr_temp,linkDB.openDB());

                Hallazgo hallazgo = linkDB.getHallazgo("CALIDAD",evaluacion.id, id_indicador,lista_ecr.get(j).getExpediente());

                if (hallazgo != null)
                {
                    hallazgo.setBorradoAl(link.getFecha());
                    linkDB.actualizarHallazgo(hallazgo,linkDB.openDB());
                }

            }  /// FIN LISTA REGISTROS




        }// fin for indicadores evaluacion


        contarEvaluacion();

    }

///*************************************************************************************************


}





class AdaptadorDeviceCalidad extends ArrayAdapter<Device>
{

    private final Context context;
    private final ArrayList<Device> itemsArrayList;

    public AdaptadorDeviceCalidad(Context context, ArrayList<Device> itemsArrayList) {

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

