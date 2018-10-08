package code.yoursoft.ciummovil;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FirmaEvaluacion extends AppCompatActivity implements OnClickListener
{

    private final String tag = "FirmaEvaluacion";


    private EditText edit_nombre,edit_email;
    private Button boton_limpiar, boton_firmar;
    private DrawingView drawingView;

    CoordinatorLayout coordinatorLayout;




    Evaluacion evaluacion;
    Funciones link;
    DBManager linkDB;


    Clues clues_selected;
    Usuarios user_signed;
    int idEvaluacion;

    String tipoEvaluacion;

    int write=0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


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
            tipoEvaluacion = vars.getString("tipoEvaluacion");
        }

        if(tipoEvaluacion.equals("RECURSO"))
        {
            evaluacion = linkDB.getEvaluacion("RECURSO", idEvaluacion, linkDB.openDB());
            if (evaluacion == null) {
                System.out.println("ERROR EVALUACION RECURSO REGRESO NULL....]]]");
            }
            clues_selected = linkDB.getClues(evaluacion.clues);
        }else{
                evaluacion = linkDB.getEvaluacion("CALIDAD", idEvaluacion, linkDB.openDB());
                if (evaluacion == null) {
                    System.out.println("ERROR EVALUACION CALIDAD REGRESO NULL....]]]");
                }
                clues_selected = linkDB.getClues(evaluacion.clues);

             }


        if (savedInstanceState == null) {

            setContentView(R.layout.layout_firma);

            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_firma);
            setSupportActionBar(toolbar);

            toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(tipoEvaluacion.equals("RECURSO"))
                    {
                        Intent lanzador = new Intent(getApplicationContext(), EvaluadorRecurso.class);
                        lanzador.putExtra("idEvaluacionRecurso", evaluacion.id);

                        startActivity(lanzador);
                        finish();
                    }else{
                        Intent lanzador = new Intent(getApplicationContext(), EvaluadorCalidad.class);
                        lanzador.putExtra("idEvaluacionCalidad", evaluacion.id);

                        startActivity(lanzador);
                        finish();
                    }

                }
            });

            getSupportActionBar().setTitle(clues_selected.clues);
            toolbar.setSubtitle(evaluacion.fechaEvaluacion);

            drawingView = (DrawingView) findViewById(R.id.drawing);

            edit_nombre = (EditText) findViewById(R.id.edit_nombre_responsable);
            edit_email = (EditText) findViewById(R.id.edit_email_responsable);




            boton_limpiar = (Button) findViewById(R.id.boton_borrar);
            boton_limpiar.setOnClickListener(this);

            boton_firmar = (Button) findViewById(R.id.boton_firmar);
            boton_firmar.setOnClickListener(this);

        }
    }


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.layout_firma, menu);
//		return true;
//	}


    @Override
    public void onClick(View v) {

    if (v == boton_limpiar) {

            drawingView.reset();
            drawingView.setBackground(null);

            write=0;

        } else if (v == boton_firmar) {

            if(drawingView.write==1)
            {
                int errors=0;
                String messages="";

                String nombre="";
                nombre = edit_nombre.getText().toString().trim();

                String email="";
                email = edit_email.getText().toString().trim();

                if(nombre.equals(""))
                {
                    errors++;
                    messages+="Escriba nombre del responsable.\n";
                }
                if(email.equals(""))
                {
                    errors++;
                    messages+="Escriba el email del responsable.";
                }else{
                       if(link.esEmailValido(email))
                       {
                       }else{
                                errors++;
                                messages+="Escriba una dirección de correo valida.";
                            }

                     }

                if(errors == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setMessage("¿ Confirma guardar la firma ?").setPositiveButton("Si",  new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    guardarFirma();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog,int id)
                                {
                                    dialog.cancel();
                                }
                            }).show();

                }else{
                        link.showSnackBar(getApplicationContext(),coordinatorLayout,messages,"ERROR");
                     }



            }else{
                   link.showSnackBar(getApplicationContext(),coordinatorLayout,"Ninguna firma detectada !","ERROR");
                 }


        }

    }


    public void guardarFirma()
    {
        String nombre="";
        nombre = edit_nombre.getText().toString().trim();

        String email="";
        email = edit_email.getText().toString().trim();

        drawingView.setDrawingCacheEnabled(true);
        Bitmap bm = drawingView.getDrawingCache();


        String firma = link.encodeTobase64(bm);
        SQLiteDatabase db = linkDB.openDB();

        Evaluacion eval= new Evaluacion(evaluacion.id,evaluacion.idServer,evaluacion.idUsuario,
                                        evaluacion.clues,evaluacion.fechaEvaluacion,evaluacion.cerrado,
                                        firma,nombre,email,evaluacion.sincronizado,evaluacion.getCompartido(),evaluacion.getCompartidoFull(),
                                        evaluacion.creadoAl,link.getFecha(),evaluacion.borradoAl);

        System.out.println("---------------------------  EMAIL RESPONSABLE FIRMA antes de insert : "+email+" .");


        if(tipoEvaluacion.equals("RECURSO"))
        {
            linkDB.firmarEvaluacion("RECURSO", eval, db);
        }else{
                linkDB.firmarEvaluacion("CALIDAD", eval, db);
             }


        link.showSnackBar(getApplicationContext(), coordinatorLayout, "Evaluación firmada", "INFO");

        if(tipoEvaluacion.equals("RECURSO"))
        {
            Intent lanzador = new Intent(getApplicationContext(), EvaluadorRecurso.class);
            lanzador.putExtra("idEvaluacionRecurso", evaluacion.id);

            startActivity(lanzador);
            finish();
        }else{
                Intent lanzador = new Intent(getApplicationContext(), EvaluadorCalidad.class);
                lanzador.putExtra("idEvaluacionCalidad", evaluacion.id);

                startActivity(lanzador);
                finish();
            }


    }




}
