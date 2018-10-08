package code.yoursoft.ciummovil;

import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DBManager extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cium";

    public DBManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override

    public void onCreate(SQLiteDatabase db)
    {

        String CREATE_TABLE_USUARIO= "CREATE TABLE Usuario (id INTEGER, "+
                "username VARCHAR(255), email VARCHAR (255), "+
                "nombres VARCHAR (255), apellidoPaterno VARCHAR (255), apellidoMaterno VARCHAR (255),"+
                "avatar VARCHAR (455),loginActivo INTEGER, accessToken VARCHAR (300), refreshToken VARCHAR(1000))";

        String CREATE_TABLE_CLUES="CREATE TABLE Clues( clues VARCHAR (50), nombre VARCHAR (255),"+
                "domicilio VARCHAR (255), codigoPostal VARCHAR (255), entidad VARCHAR (255),"+
                "municipio VARCHAR (255),localidad VARCHAR (255), jurisdiccion VARCHAR (255),"+
                "claveJurisdiccion VARCHAR (255), institucion VARCHAR (255), tipoUnidad VARCHAR (255),"+
                "estatus VARCHAR (255), estado VARCHAR (255), tipologia VARCHAR (255), cone VARCHAR (255), "+
                "latitud VARCHAR (50), longitud VARCHAR (50), "+
                "idCone INTEGER)";

        String CREATE_TABLE_CONE="CREATE TABLE Cone (id INTEGER,nombre VARCHAR(255))";

        String CREATE_TABLE_CONE_CLUES="CREATE TABLE ConeClues (clues VARCHAR (50),idCone INTEGER)";

        String CREATE_TABLE_INDICADOR="CREATE TABLE Indicador (id INTEGER, codigo VARCHAR (6),nombre VARCHAR(255),color TEXT(22), categoria TEXT(45), indicacion TEXT(400) )";

        String CREATE_TABLE_CRITERIO="CREATE TABLE Criterio (id INTEGER, nombre VARCHAR (255), habilitarNoAplica INTEGER, tieneValidacion INTEGER, orden VARCHAR(45) )";

        String CREATE_TABLE_LUGAR_VERIFICACION="CREATE TABLE LugarVerificacion (id INT, nombre VARCHAR (150) )";

        String CREATE_TABLE_INDICADOR_CRITERIO="CREATE TABLE IndicadorCriterio (id INTEGER, idCriterio INTEGER(10), idIndicador INTEGER(10),"+
                                                "idLugarVerificacion INTEGER(10))";

        String CREATE_TABLE_CONE_INDICADOR_CRITERIO="CREATE TABLE ConeIndicadorCriterio (id INTEGER PRIMARY KEY AUTOINCREMENT,idCone INT(10),idIndicadorCriterio INT (10) )";

        String CREATE_TABLE_EVALUACION_RECURSO = "CREATE TABLE EvaluacionRecurso (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "idServer INT, idUsuario INT,"+
                "clues VARCHAR(45),fechaEvaluacion VARCHAR(45),cerrado INT,firma TEXT,"+
                "responsable VARCHAR(150),emailResponsable VARCHAR(150), sincronizado INT, compartido INT, compartidoFull INT,"+
                "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30))";


        String CREATE_TABLE_EVALUACION_RECURSO_INDICADOR="CREATE TABLE EvaluacionRecursoIndicador (id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "idEvaluacionRecurso INT(11), idIndicador INT(11), totalCriterio INT(11), avanceCriterio INT(11), "+
                "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";


        String CREATE_TABLE_EVALUACION_RECURSO_CRITERIO="CREATE TABLE EvaluacionRecursoCriterio (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "idEvaluacionRecurso INT,idCriterio INT,idIndicador INT,aprobado INT,"+
                "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";


        String CREATE_TABLE_EVALUACION_CALIDAD="CREATE TABLE EvaluacionCalidad (id INTEGER PRIMARY KEY AUTOINCREMENT, idServer INT,idUsuario INT,"+
                "clues VARCHAR(45),fechaEvaluacion VARCHAR(45),cerrado INT,firma TEXT,"+
                "responsable VARCHAR(150),emailResponsable VARCHAR(150),sincronizado INT, compartido INT, compartidoFull INT,creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30))";

        String CREATE_TABLE_EVALUACION_CALIDAD_CRITERIO="CREATE TABLE EvaluacionCalidadCriterio (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "idEvaluacionCalidad INT,idCriterio INT,idIndicador INT,aprobado INT, idEvaluacionCalidadRegistro INT,"+
                "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";

        String CREATE_TABLE_EVALUACION_CALIDAD_REGISTRO="CREATE TABLE EvaluacionCalidadRegistro (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "idEvaluacionCalidad INT, idIndicador INT, columna INT, expediente VARCHAR(50), cumple INT, promedio DECIMAL(15,2), totalCriterio INT, "+
                "avanceCriterio INTEGER, creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";


        String CREATE_TABLE_HALLAZGO="CREATE TABLE Hallazgo(id INTEGER PRIMARY KEY AUTOINCREMENT, idEvaluacion INT, categoriaEvaluacion VARCHAR(50),"+
                "idIndicador INT, expediente VARCHAR(25), idUsuario INT, idAccion INT, idPlazoAccion INT, resuelto INT, descripcion VARCHAR(500),"+
                "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";

        String CREATE_TABLE_SEGUIMIENTO="CREATE TABLE Seguimiento(id INTEGER PRIMARY KEY AUTOINCREMENT, idUsuario INT, idHallazgo INT,"+
                "descripcion VARCHAR(500), creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30))";


        String CREATE_TABLE_ACCION="CREATE TABLE Accion (id INTEGER, nombre VARCHAR(250), tipo VARCHAR (5),"+
                "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";



        String CREATE_TABLE_INDICADOR_VALIDACION="CREATE TABLE IndicadorValidacion (id INTEGER, idIndicador INT, pregunta1 VARCHAR(250), operadorAritmetico VARCHAR(50), "+
                "pregunta2 VARCHAR(250), unidadMedida VARCHAR(50), operadorLogico VARCHAR(50), valorComparativo VARCHAR(50), "+
                "creadoAl VARCHAR(30), modificadoAl VARCHAR(30), borradoAl VARCHAR(30) )";

        String CREATE_TABLE_INDICADOR_PREGUNTA ="CREATE TABLE IndicadorPregunta (id VARCHAR(50), idIndicador INT, nombre VARCHAR(250),"+
                "tipo VARCHAR(50), constante INT, valorConstante VARCHAR(250),fechaSistema INT,"+
                "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";



        String CREATE_TABLE_CRITERIO_VALIDACION="CREATE TABLE CriterioValidacion (id INTEGER, idCriterio INT, pregunta1 VARCHAR(250), operadorAritmetico VARCHAR(50), "+
                "pregunta2 VARCHAR(250), unidadMedida VARCHAR(50), operadorLogico VARCHAR(50), valorComparativo VARCHAR(50), "+
                "creadoAl VARCHAR(30), modificadoAl VARCHAR(30), borradoAl VARCHAR(30) )";

        String CREATE_TABLE_CRITERIO_PREGUNTA ="CREATE TABLE CriterioPregunta (id VARCHAR(50), idCriterio INT, nombre VARCHAR(250),"+
                "tipo VARCHAR(50), constante INT, valorConstante VARCHAR(250),fechaSistema INT,"+
                "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";


        String CREATE_TABLE_CRITERIO_VALIDACION_RESPUESTA="CREATE TABLE CriterioValidacionRespuesta (id INTEGER PRIMARY KEY AUTOINCREMENT, idEvaluacion INTEGER,"+
                "expediente VARCHAR(250), idCriterio INTEGER, idCriterioValidacion INTEGER, tipo VARCHAR(50), respuesta1 VARCHAR(50), respuesta2 VARCHAR(50),"+
                "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";





        String CREATE_TABLE_CONFIG="CREATE TABLE Config (id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                                             "seccion VARCHAR(250), idUsuario INT, fechaAccion VARCHAR (5), status VARCHAR (5), "+
                                                             "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";

        String CREATE_TABLE_VERSION="CREATE TABLE Version(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "path TEXT, versionApp DOUBLE, versionDB INT, descripcion TEXT, instalado INT,"+
                "creadoAl VARCHAR(30),modificadoAl VARCHAR (30),borradoAl VARCHAR(30) )";



        String CREATE_TABLE_CHECK_VERSION="CREATE TABLE CheckVersion(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                                                        "fechaCheck TEXT," +
                                                                        "versionInstalada DOUBLE, " +
                                                                        "versionNuevaEncontrada DOUBLE, " +
                                                                        "descripcion TEXT, " +
                                                                        "instalado INT,"+
                                                                        "creadoAl VARCHAR(30)," +
                                                                        "modificadoAl VARCHAR (30)," +
                                                                        "borradoAl VARCHAR(30) )";








    try {


            db.execSQL(CREATE_TABLE_USUARIO);
            db.execSQL(CREATE_TABLE_CLUES);
            db.execSQL(CREATE_TABLE_CONE);
            db.execSQL(CREATE_TABLE_CONE_CLUES);
            db.execSQL(CREATE_TABLE_INDICADOR);
            db.execSQL(CREATE_TABLE_CRITERIO);
            db.execSQL(CREATE_TABLE_LUGAR_VERIFICACION);
            db.execSQL(CREATE_TABLE_INDICADOR_CRITERIO);
            db.execSQL(CREATE_TABLE_CONE_INDICADOR_CRITERIO);

            db.execSQL(CREATE_TABLE_EVALUACION_RECURSO);
            db.execSQL(CREATE_TABLE_EVALUACION_RECURSO_INDICADOR);
            db.execSQL(CREATE_TABLE_EVALUACION_RECURSO_CRITERIO);

            db.execSQL(CREATE_TABLE_EVALUACION_CALIDAD);
            db.execSQL(CREATE_TABLE_EVALUACION_CALIDAD_CRITERIO);
            db.execSQL(CREATE_TABLE_EVALUACION_CALIDAD_REGISTRO);

            db.execSQL(CREATE_TABLE_HALLAZGO);
            db.execSQL(CREATE_TABLE_SEGUIMIENTO);
            db.execSQL(CREATE_TABLE_ACCION);

            db.execSQL(CREATE_TABLE_INDICADOR_VALIDACION);
            db.execSQL(CREATE_TABLE_INDICADOR_PREGUNTA);

            db.execSQL(CREATE_TABLE_CRITERIO_VALIDACION);
            db.execSQL(CREATE_TABLE_CRITERIO_PREGUNTA);

            db.execSQL(CREATE_TABLE_CRITERIO_VALIDACION_RESPUESTA);

            db.execSQL(CREATE_TABLE_CONFIG);

            db.execSQL(CREATE_TABLE_VERSION);

    }catch (SQLiteException err){System.out.print("-------------->> >> >>   ERRROR SQLITE :"+err.toString()+".  <<<<-- "); }

    }


     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //db.execSQL("ALTER TABLE EvaluacionCalidadRegistro ADD avanceCriterio INTEGER AFTER totalCriterio");
        onCreate(db);
    }


    public int getUsersCount()
    {
        String countQuery = "SELECT  * FROM Usuario";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public Usuarios getUser(String email)
    {
        Usuarios user = null;

        String selectQuery = "SELECT  * FROM Usuario WHERE email='"+email+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            user = new Usuarios(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getInt(7),cursor.getString(8), cursor.getString(9));

            cursor.close();

        }else {
                 return null;
              }

        return user;
    }

    public Indicador getIndicador(int idIndicador)
    {
        Indicador indicador = null;

        String query = "SELECT  * FROM Indicador WHERE id="+idIndicador;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            indicador = new Indicador(cursor.getInt(0),
                                        cursor.getString(1),
                                        cursor.getString(2),
                                        cursor.getString(3),
                                        cursor.getString(4),cursor.getString(5) );

            cursor.close();
            //db.close();
        }else {
                return null;
              }

        return indicador;
    }

    public Clues getClues(String clues_buscar)
    {
        Clues clues = null;

        String selectQuery = "SELECT  * FROM Clues WHERE clues =\'"+clues_buscar.toUpperCase().trim()+"\'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
              clues = new Clues(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                                cursor.getString(3), cursor.getString(4), cursor.getString(5),
                                cursor.getString(6), cursor.getString(7), cursor.getString(8),
                                cursor.getString(9), cursor.getString(10), cursor.getString(11),
                                cursor.getString(12), cursor.getString(13), cursor.getString(14),
                                cursor.getString(15),cursor.getString(16), cursor.getInt(17)
                                );

            cursor.close();
            //db.close();
        }else {
                  return null;
              }

        return clues;
    }

    public JSONObject getAllClues(String buscar)throws JSONException
    {

        String query;
        if(buscar==null || buscar.equals(""))
        {
            query = "SELECT * FROM Clues";
        }else {
                 query = "SELECT * FROM Clues WHERE clues LIKE \'%"+buscar.toUpperCase().trim()+"%\' OR nombre LIKE \'%"+buscar.toUpperCase().trim()+"%\' OR localidad LIKE \'%"+buscar.toUpperCase().trim()+"%\' OR municipio LIKE \'%"+buscar.toUpperCase().trim()+"%\' ";
              }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int n = cursor.getCount();

        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();


        if (cursor.moveToFirst()) {
            do {

                obj = new JSONObject();
                try {
                        obj.put("clues", cursor.getString(0));
                        obj.put("nombre", cursor.getString(1));
                        obj.put("domicilio", cursor.getString(2));
                        obj.put("codigoPostal", cursor.getString(3));
                        obj.put("entidad", cursor.getString(4));
                        obj.put("municipio", cursor.getString(5));
                        obj.put("localidad", cursor.getString(6));
                        obj.put("jurisdiccion", cursor.getString(7));
                        obj.put("claveJurisdiccion", "*"+cursor.getString(8));
                        obj.put("institucion", cursor.getString(9));
                        obj.put("tipoUnidad", cursor.getString(10));
                        obj.put("estatus", cursor.getString(11));
                        obj.put("estado", cursor.getString(12));
                        obj.put("tipologia", cursor.getString(13));
                        obj.put("cone", cursor.getString(14));
                        obj.put("latitud", cursor.getString(15));
                        obj.put("longitud", cursor.getString(16));
                        obj.put("idCone", cursor.getInt(17));

                    } catch (JSONException e) {
                                                 e.printStackTrace();
                                              }
                jsonArray.put(obj);

            } while (cursor.moveToNext());
        }



        JSONObject  objeto_send = new JSONObject();
        objeto_send.put("clues", jsonArray);

        return objeto_send;

    }


    public List<Clues> getListaClues(String buscar)
    {
        List<Clues> lista_clues = new ArrayList<>();

        String query;
        if(buscar==null || buscar.equals(""))
        {
            query = "SELECT * FROM Clues";
        }else {
            //query = "SELECT * FROM Clues WHERE clues LIKE \'%"+buscar.toUpperCase().trim()+"%\' OR nombre LIKE \'%"+buscar.toUpperCase().trim()+"%\' OR localidad LIKE \'%"+buscar.toUpperCase().trim()+"%\' OR municipio LIKE \'%"+buscar.toUpperCase().trim()+"%\' ";

            query = "SELECT * FROM Clues WHERE replace(replace(replace(replace(replace( upper(nombre), 'Á','A'), 'É','E'), 'Í','I'),'Ó','O'), 'Ú','U') LIKE '%"+buscar.toUpperCase().trim()+"%'";
    }



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int n = cursor.getCount();

        if (cursor.moveToFirst()) {
            do {

                try {


                    lista_clues.add(new Clues(

                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9),
                            cursor.getString(10),
                            cursor.getString(11),
                            cursor.getString(12),
                            cursor.getString(13),
                            cursor.getString(14),
                            cursor.getString(15),
                            cursor.getString(16),
                            cursor.getInt(17)

                    ));

                } catch (Exception e) {
                                        e.printStackTrace();
                                        }


            } while (cursor.moveToNext());
        }

        return lista_clues;
    }



    public Evaluacion getEvaluacion(String tipo_evaluacion,int idEvaluacion,SQLiteDatabase db)
    {
        Evaluacion eval = null;
        String query;

        if(tipo_evaluacion.equals("RECURSO"))
        {
            query = "SELECT  * FROM EvaluacionRecurso WHERE id="+idEvaluacion+" AND borradoAl='0000-00-00 00:00:00'";
        }else{
               query = "SELECT  * FROM EvaluacionCalidad WHERE id="+idEvaluacion+" AND borradoAl='0000-00-00 00:00:00'";
             }

        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            eval = new Evaluacion(
                                    cursor.getInt(0), cursor.getInt(1),cursor.getInt(2),
                                    cursor.getString(3), cursor.getString(4), cursor.getInt(5),
                                    cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9),
                                    cursor.getInt(10),cursor.getInt(11),
                                    cursor.getString(12), cursor.getString(13), cursor.getString(14) ) ;

            cursor.close();
            //db.close();
        }else {
            return null;
        }


        return eval;


    }
    public Evaluacion getEvaluacion(String tipo_evaluacion,String where,int idEvaluacion,SQLiteDatabase db)
    {
        Evaluacion eval = null;
        String query;

        if(tipo_evaluacion.equals("RECURSO"))
        {
            query = "SELECT  * FROM EvaluacionRecurso "+where+" AND borradoAl='0000-00-00 00:00:00'";
        }else{
            query = "SELECT  * FROM EvaluacionCalidad "+where+" AND borradoAl='0000-00-00 00:00:00'";
        }

        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            eval = new Evaluacion(
                    cursor.getInt(0), cursor.getInt(1),cursor.getInt(2),
                    cursor.getString(3), cursor.getString(4), cursor.getInt(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9),
                    cursor.getInt(10),cursor.getInt(11),
                    cursor.getString(12), cursor.getString(13), cursor.getString(14) ) ;

            cursor.close();
            //db.close();
        }else {
            return null;
        }


        return eval;


    }



    public List<Evaluacion> getEvaluaciones(String tipoEvaluacion,String buscar,int id_usuario)
    {
        List <Evaluacion> eval = new ArrayList<>();
        String query = "";

        if(tipoEvaluacion.equals("RECURSO"))
        {
            if (buscar == null || buscar.equals("")) {
                query = "SELECT * FROM EvaluacionRecurso WHERE idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00' ORDER BY id DESC";
            } else {
                query = "SELECT * FROM EvaluacionRecurso WHERE idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }else{
                if (buscar == null || buscar.equals("")) {
                    query = "SELECT * FROM EvaluacionCalidad WHERE idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00' ORDER BY id DESC";
                } else {
                    query = "SELECT * FROM Evaluacioncalidad WHERE idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
                }
             }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
          do{
            eval.add(new Evaluacion(
                                    cursor.getInt(0), cursor.getInt(1),cursor.getInt(2),
                                    cursor.getString(3), cursor.getString(4), cursor.getInt(5),
                                    cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9),
                                    cursor.getInt(10),cursor.getInt(11),
                                    cursor.getString(12), cursor.getString(13), cursor.getString(14) ) );

            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }
        /*
        else {
            return null;
        }
        */

        return eval;


    }

    public List<Evaluacion> getEvaluacionesCerradas(String tipoEvaluacion,String buscar,int id_usuario)
    {
        List <Evaluacion> eval = new ArrayList<>();
        String query = "";

        if(tipoEvaluacion.equals("RECURSO"))
        {
            if (buscar == null || buscar.equals("")) {
                query = "SELECT * FROM EvaluacionRecurso WHERE idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00' ORDER BY id DESC";
            } else {
                query = "SELECT * FROM EvaluacionRecurso WHERE idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }else{
            if (buscar == null || buscar.equals("")) {
                query = "SELECT * FROM EvaluacionCalidad WHERE idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00' ORDER BY id DESC";
            } else {
                query = "SELECT * FROM Evaluacioncalidad WHERE idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                eval.add(new Evaluacion(
                        cursor.getInt(0), cursor.getInt(1),cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4), cursor.getInt(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9),
                        cursor.getInt(10),cursor.getInt(11),
                        cursor.getString(12), cursor.getString(13), cursor.getString(14) ) );

            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }
        /*
        else {
            return null;
        }
        */

        return eval;


    }



    public List<Evaluacion> getEvaluacionesParaPost(String tipoEvaluacion,String buscar,int id_usuario)
    {
        List <Evaluacion> eval = new ArrayList<>();
        String query = "";

        if(tipoEvaluacion.equals("RECURSO"))
        {
            if (buscar == null || buscar.equals(""))
            {
                query = "SELECT * FROM EvaluacionRecurso WHERE idServer=0 AND cerrado=1 AND firma!='' AND idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00'";
            } else {
                query = "SELECT * FROM EvaluacionRecurso WHERE idServer=0 AND cerrado=1 AND firma!='' AND idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }else{
            if (buscar == null || buscar.equals("")) {
                query = "SELECT * FROM EvaluacionCalidad WHERE idServer=0 AND cerrado=1 AND firma!='' AND idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00'";
            } else {
                query = "SELECT * FROM Evaluacioncalidad WHERE idServer=0 AND AND cerrado=1 AND firma!='' idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                eval.add(new Evaluacion(
                                        cursor.getInt(0), cursor.getInt(1),cursor.getInt(2),
                                        cursor.getString(3), cursor.getString(4), cursor.getInt(5),
                                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9),
                                        cursor.getInt(10),cursor.getInt(11),
                                        cursor.getString(12), cursor.getString(13), cursor.getString(14) ) );

            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }
        /*
        else {
            return null;
        }
        */

        return eval;


    }






    public List<Evaluacion> getEvaluacionesParaRePost(String tipoEvaluacion,String buscar,int id_usuario)
    {
        List <Evaluacion> eval = new ArrayList<>();
        String query = "";

        if(tipoEvaluacion.equals("RECURSO"))
        {
            if (buscar == null || buscar.equals(""))
            {
                query = "SELECT * FROM EvaluacionRecurso WHERE cerrado=1 AND firma!='' AND idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00'";
            } else {
                query = "SELECT * FROM EvaluacionRecurso WHERE cerrado=1 AND firma!='' AND idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }else{
            if (buscar == null || buscar.equals("")) {
                query = "SELECT * FROM EvaluacionCalidad WHERE cerrado=1 AND firma!='' AND idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00'";
            } else {
                query = "SELECT * FROM Evaluacioncalidad WHERE cerrado=1 AND firma!='' AND idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                eval.add(new Evaluacion(
                        cursor.getInt(0), cursor.getInt(1),cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4), cursor.getInt(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9),
                        cursor.getInt(10),cursor.getInt(11),
                        cursor.getString(12), cursor.getString(13), cursor.getString(14) ) );

            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }
        /*
        else {
            return null;
        }
        */

        return eval;


    }








    public List<Evaluacion> getEvaluacionesParaPut(String tipoEvaluacion,String buscar,int id_usuario)
    {
        List <Evaluacion> eval = new ArrayList<>();
        String query = "";

        if(tipoEvaluacion.equals("RECURSO"))
        {
            if (buscar == null || buscar.equals(""))
            {
                query = "SELECT * FROM EvaluacionRecurso WHERE idServer > 0 AND sincronizado=0 AND cerrado=1 AND firma!='' AND idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00'";
            } else {
                query = "SELECT * FROM EvaluacionRecurso WHERE idServer > 0 AND sincronizado=0 AND cerrado=1 AND firma!='' AND idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }else{
            if (buscar == null || buscar.equals("")) {
                query = "SELECT * FROM EvaluacionCalidad WHERE idServer > 0 AND sincronizado=0 AND cerrado=1 AND firma!='' AND idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00'";
            } else {
                query = "SELECT * FROM Evaluacioncalidad WHERE idServer > 0 AND sincronizado=0 AND AND cerrado=1 AND firma!='' idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                eval.add(new Evaluacion(
                                        cursor.getInt(0), cursor.getInt(1),cursor.getInt(2),
                                        cursor.getString(3), cursor.getString(4), cursor.getInt(5),
                                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9),
                                        cursor.getInt(10),cursor.getInt(11),
                                        cursor.getString(12), cursor.getString(13), cursor.getString(14) ) );

            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }
        /*
        else {
            return null;
        }
        */

        return eval;


    }





    public List<EvaluacionDetalles> getEvaluacionesDetalles(String tipoEvaluacion,String buscar,int id_usuario)
    {
        List <Evaluacion> eval = new ArrayList<>();

        List <EvaluacionDetalles> lista_detalles = new ArrayList<>();

        String query = "";

        if(tipoEvaluacion.equals("RECURSO"))
        {
            if (buscar == null || buscar.equals("")) {
                query = "SELECT * FROM EvaluacionRecurso WHERE idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00'";
            } else {
                query = "SELECT * FROM EvaluacionRecurso WHERE idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }else{
            if (buscar == null || buscar.equals("")) {
                query = "SELECT * FROM EvaluacionCalidad WHERE idUsuario="+id_usuario+" AND borradoAl='0000-00-00 00:00:00'";
            } else {
                query = "SELECT * FROM Evaluacioncalidad WHERE idUsuario="+id_usuario+" AND nombre LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR clues LIKE \'%" + buscar.toUpperCase().trim() + "%\' OR fecha = \'" + buscar.toUpperCase().trim() + "\' AND borradoAl='0000-00-00 00:00:00'";
            }
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                Evaluacion eval_temp = new Evaluacion(
                                                        cursor.getInt(0), cursor.getInt(1),cursor.getInt(2),
                                                        cursor.getString(3), cursor.getString(4), cursor.getInt(5),
                                                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9),
                                                        cursor.getInt(10),cursor.getInt(11),
                                                        cursor.getString(12), cursor.getString(13), cursor.getString(14)  );


                Clues clues_temp = getClues(eval_temp.clues);

                /// GET DETALLES DE EVALUACION (INDICADORES AGREGADOS,CRITERIOS )

                int indicadores=0;
                int expedientes=0;
                int criterios=0;
                int respondidas=0;

                if(tipoEvaluacion.equals("RECURSO"))
                {

                    List<Indicador> indicadores_agregados_recurso = getIndicadoresAgregadosRecurso(eval_temp.getId());
                    List<LugarVerificacion> listaLV = new ArrayList<>();
                    List<Criterio> listaC = new ArrayList<>();

                    for(int i=0; i<indicadores_agregados_recurso.size(); i++)
                    {
                        listaLV = getLugaresVerificacion(clues_temp.getIdCone(), indicadores_agregados_recurso.get(i).getId() );

                        for(int j=0; j<listaLV.size(); j++)
                        {
                            listaC = getCriterios(clues_temp.getIdCone(), indicadores_agregados_recurso.get(i).getId(), listaLV.get(j).getId());

                            for(int k=0; k<listaC.size(); k++)
                            {
                                EvaluacionRecursoCriterio eval_rc = getEvaluacionRecursoCriterio(eval_temp.getId(), indicadores_agregados_recurso.get(i).getId(), listaC.get(k).getId());

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

                            List<Indicador> indicadores_agregados_calidad = getIndicadoresAgregadosCalidad(eval_temp.getId());
                            List<LugarVerificacion> listaLV = new ArrayList<>();
                            List<Criterio> listaC = new ArrayList<>();

                            for(int i=0; i<indicadores_agregados_calidad.size(); i++)
                            {
                                //GET EXPEDIENTE AGREGADOS AQUI Y HACER FOR PARA EMBEBER ESTO
                                //List<EvaluacionCalidadRegistro> lista_expedientes = getExpedientesEvaluacionCalidadRegistro(eval_temp.getId());
                                List<EvaluacionCalidadRegistro> lista_expedientes = getExpedientesEvaluacionCalidadRegistro(eval_temp.getId(),indicadores_agregados_calidad.get(i).getId());
                                System.out.println("----------------->>>>>>>>>>>   EXPEDIENTES ["+lista_expedientes.size()+"]");

                                expedientes+=lista_expedientes.size();

                                for(int h=0; h<lista_expedientes.size(); h++)
                                {
                                    listaLV = getLugaresVerificacion(clues_temp.getIdCone(), indicadores_agregados_calidad.get(i).getId());

                                    for (int j = 0; j < listaLV.size(); j++)
                                    {
                                        listaC = getCriterios(clues_temp.getIdCone(), indicadores_agregados_calidad.get(i).getId(), listaLV.get(j).getId());

                                        for (int k = 0; k < listaC.size(); k++)
                                        {
                                            EvaluacionCalidadCriterio eval_cc = getEvaluacionCalidadCriterio(eval_temp.getId(), indicadores_agregados_calidad.get(i).getId(), listaC.get(k).getId(),lista_expedientes.get(h).getId());

                                            if (eval_cc != null)
                                            {
                                                respondidas++;
                                            }

                                            criterios++;
                                        }
                                    }

                                    //expedientes++;
                                }

                                    indicadores++;
                            }



                       }// FIN ELSE ( EVALUACION ES DE TIPO CALIDAD)









                lista_detalles.add(new EvaluacionDetalles(
                                                            eval_temp.id,
                                                            eval_temp.idUsuario,
                                                                getNombreUsuario(eval_temp.idUsuario),
                                                            eval_temp.clues,
                                                            clues_temp.nombre,
                                                            clues_temp.domicilio,
                                                            clues_temp.jurisdiccion,
                                                            clues_temp.municipio,
                                                            clues_temp.localidad,
                                                            eval_temp.fechaEvaluacion,
                                                            eval_temp.cerrado,
                                                            eval_temp.sincronizado,
                                                            eval_temp.compartido,
                                                            eval_temp.compartidoFull,
                                                            eval_temp.firma,
                                                            eval_temp.responsable,
                                                                                    indicadores,
                                                                                    expedientes,
                                                                                    criterios,
                                                                                    respondidas,
                                                            eval_temp.creadoAl,
                                                            eval_temp.modificadoAl,
                                                            eval_temp.borradoAl
                                                    )
                                            );








            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }
        /*
        else {
                return null;
             }
        */

        return lista_detalles;


    }


    public List<EvaluacionRecursoCriterio> getEvaluacionRecursoCriterio(int id_evaluacion)
    {
        List <EvaluacionRecursoCriterio> eval = new ArrayList<>();
        String query = "";

        query = "SELECT * FROM EvaluacionRecursoCriterio WHERE idEvaluacionRecurso="+id_evaluacion;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do{
                eval.add(new EvaluacionRecursoCriterio(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)));

            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }else {
            return eval;
        }

        return eval;
    }


    public List<EvaluacionRecursoIndicador> getEvaluacionRecursoIndicador(int id_evaluacion)
    {
        List <EvaluacionRecursoIndicador> eval = new ArrayList<>();
        String query = "";

        query = "SELECT * FROM EvaluacionRecursoIndicador WHERE idEvaluacionRecurso="+id_evaluacion+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do{
                eval.add(new EvaluacionRecursoIndicador(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)));

            } while (cursor.moveToNext());

            cursor.close();
            // db.close();
        }else {
            return eval;
        }

        return eval;
    }




    public List<EvaluacionRecursoCriterio> getEvaluacionRecursoCriterio(int id_evaluacion, int id_indicador)
    {
        List <EvaluacionRecursoCriterio> eval = new ArrayList<>();
        String query = "";

        query = "SELECT * FROM EvaluacionRecursoCriterio WHERE idEvaluacionRecurso="+id_evaluacion+" AND idIndicador="+id_indicador;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do{
                eval.add(new EvaluacionRecursoCriterio(
                                                        cursor.getInt(0),
                                                        cursor.getInt(1),
                                                        cursor.getInt(2),
                                                        cursor.getInt(3),
                                                        cursor.getInt(4),
                                                        cursor.getString(5),
                                                        cursor.getString(6),
                                                        cursor.getString(7)));

            } while (cursor.moveToNext());

            cursor.close();
           // db.close();
        }else {
                return eval;
              }

        return eval;
    }



    public EvaluacionRecursoIndicador getEvaluacionRecursoIndicador(int id_evaluacion, int id_indicador)
    {
        EvaluacionRecursoIndicador eval ;
        String query = "";

        query = "SELECT * FROM EvaluacionRecursoIndicador WHERE idEvaluacionRecurso="+id_evaluacion+" AND idIndicador="+id_indicador+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            eval = new EvaluacionRecursoIndicador(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            );

            cursor.close();

        }else {
                    return null;
              }

        return eval;
    }




    public EvaluacionRecursoCriterio getEvaluacionRecursoCriterio(int id_evaluacion, int id_indicador,int id_criterio)
    {
        EvaluacionRecursoCriterio eval ;
        String query = "";

        query = "SELECT * FROM EvaluacionRecursoCriterio WHERE idEvaluacionRecurso="+id_evaluacion+" AND idIndicador="+id_indicador+" AND idCriterio="+id_criterio+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
                eval = new EvaluacionRecursoCriterio(
                                                            cursor.getInt(0),
                                                            cursor.getInt(1),
                                                            cursor.getInt(2),
                                                            cursor.getInt(3),
                                                            cursor.getInt(4),
                                                            cursor.getString(5),
                                                            cursor.getString(6),
                                                            cursor.getString(7)
                                                    );

                cursor.close();

        }else {
                return null;
              }

        return eval;
    }

    public EvaluacionCalidadCriterio getEvaluacionCalidadCriterio(int id_evaluacion, int id_indicador,int id_criterio,int idEvaluacionCalidadRegistro)
    {
        EvaluacionCalidadCriterio eval ;
        String query = "";

        query = "SELECT * FROM EvaluacionCalidadCriterio WHERE idEvaluacionCalidad="+id_evaluacion+" AND idIndicador="+id_indicador+" AND idCriterio="+id_criterio+" AND idEvaluacionCalidadRegistro="+idEvaluacionCalidadRegistro+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {

            eval = new EvaluacionCalidadCriterio(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );



            cursor.close();

        }else {
                    return null;
              }

        return eval;
    }



    public List<EvaluacionCalidadCriterio> getEvaluacionCalidadCriterio(int idEvaluacionCalidadRegistro)
    {
        List<EvaluacionCalidadCriterio> lista = new ArrayList<>();
        String query = "";

        query = "SELECT * FROM EvaluacionCalidadCriterio WHERE idEvaluacionCalidadRegistro="+idEvaluacionCalidadRegistro+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{

                    lista.add(new EvaluacionCalidadCriterio(
                                cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getInt(2),
                                cursor.getInt(3),
                                cursor.getInt(4),
                                cursor.getInt(5),
                                cursor.getString(6),
                                cursor.getString(7),
                                cursor.getString(8)
                             ) ) ;

            } while (cursor.moveToNext());



            cursor.close();

        }

        return lista;
    }







    public List<EvaluacionCalidadRegistro> getExpedientesEvaluacionCalidadRegistro(int idEvaluacionCalidad, int idIndicador)
    {
        List<EvaluacionCalidadRegistro>  lista = new ArrayList<>();


        String query = "";

        query = "SELECT * FROM EvaluacionCalidadRegistro WHERE idEvaluacionCalidad="+idEvaluacionCalidad+" AND idIndicador="+idIndicador+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                lista.add( new EvaluacionCalidadRegistro(
                                                            cursor.getInt(0),//ID
                                                            cursor.getInt(1),//nombre
                                                            cursor.getInt(2),
                                                            cursor.getInt(3),
                                                            cursor.getString(4),
                                                            cursor.getInt(5),
                                                            cursor.getDouble(6),
                                                            cursor.getInt(7),
                                                            cursor.getInt(8),
                                                            cursor.getString(9),
                                                            cursor.getString(10),
                                                            cursor.getString(11)

                                                    ));

            } while (cursor.moveToNext());

            cursor.close();

        }


        return lista;
    }



    public List<EvaluacionCalidadRegistro> getExpedientesEvaluacionCalidadRegistro(int idEvaluacionCalidad)
    {
        List<EvaluacionCalidadRegistro>  lista = new ArrayList<>();


        String query = "";

        query = "SELECT * FROM EvaluacionCalidadRegistro WHERE idEvaluacionCalidad="+idEvaluacionCalidad+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                lista.add(new EvaluacionCalidadRegistro(
                                                        cursor.getInt(0),//ID
                                                        cursor.getInt(1),//nombre
                                                        cursor.getInt(2),
                                                        cursor.getInt(3),
                                                        cursor.getString(4),
                                                        cursor.getInt(5),
                                                        cursor.getDouble(6),
                                                        cursor.getInt(7),
                                                        cursor.getInt(8),
                                                        cursor.getString(9),
                                                        cursor.getString(10),
                                                        cursor.getString(11)

                                                    ));

            } while (cursor.moveToNext());

            cursor.close();

        }


        return lista;
    }


    public EvaluacionCalidadRegistro getEvaluacionCalidadRegistro(int idECR)
    {

        EvaluacionCalidadRegistro  ecr;


        String query = "";

        query = "SELECT * FROM EvaluacionCalidadRegistro WHERE id="+idECR+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
                ecr = new EvaluacionCalidadRegistro(
                                                        cursor.getInt(0),//ID
                                                        cursor.getInt(1),//nombre
                                                        cursor.getInt(2),
                                                        cursor.getInt(3),
                                                        cursor.getString(4),
                                                        cursor.getInt(5),
                                                        cursor.getDouble(6),
                                                        cursor.getInt(7),
                                                        cursor.getInt(8),
                                                        cursor.getString(9),
                                                        cursor.getString(10),
                                                        cursor.getString(11)

                                                    );

            cursor.close();

         }else{
              return null;
             }


        return ecr;
    }


///***************************************************************************************************************

    public EvaluacionCalidadRegistro getEvaluacionCalidadRegistro(int idEvaluacionCalidad,int idIndicador,String expediente)
    {

        EvaluacionCalidadRegistro  ecr;


        String query = "";

        query = "SELECT * FROM EvaluacionCalidadRegistro WHERE idEvaluacionCalidad="+idEvaluacionCalidad+" AND idIndicador="+idIndicador+" AND expediente='"+expediente+"' AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            ecr = new EvaluacionCalidadRegistro(
                                                    cursor.getInt(0),//ID
                                                    cursor.getInt(1),//nombre
                                                    cursor.getInt(2),
                                                    cursor.getInt(3),
                                                    cursor.getString(4),
                                                    cursor.getInt(5),
                                                    cursor.getDouble(6),
                                                    cursor.getInt(7),
                                                    cursor.getInt(8),
                                                    cursor.getString(9),
                                                    cursor.getString(10),
                                                    cursor.getString(11)
                                                );

            cursor.close();
         }else{
            return null;
        }


        return ecr;
    }

///***************************************************************************************************************

    public boolean existeEvaluacionCalidadRegistro(int idEvaluacionCalidad,int idIndicador,String expediente)
    {

       boolean existe = false;


        String query = "";

        query = "SELECT * FROM EvaluacionCalidadRegistro WHERE idEvaluacionCalidad="+idEvaluacionCalidad+" AND idIndicador="+idIndicador+" AND expediente=\'"+expediente+"\' AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            existe=true;

        }else{
               existe = false;
             }

        cursor.close();


        return existe;
    }



    public List<Indicador> getIndicadoresAgregadosRecurso(int idEvaluacionRecurso)
    {
        List<Indicador>  lista_indicadores = new ArrayList<>();


        String query = "";

        query = "SELECT * FROM EvaluacionRecursoIndicador WHERE idEvaluacionRecurso="+idEvaluacionRecurso+" AND borradoAl='0000-00-00 00:00:00' GROUP BY idIndicador";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do{

                Indicador item= getIndicador(cursor.getInt(2));

                lista_indicadores.add( item );

            } while (cursor.moveToNext());

            cursor.close();
        }

        /*
        query = "SELECT * FROM EvaluacionRecursoCriterio WHERE idEvaluacionRecurso="+idEvaluacionRecurso+" AND borradoAl='0000-00-00 00:00:00' GROUP BY idIndicador";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do{

                Indicador item= getIndicador(cursor.getInt(3));

                lista_indicadores.add( item );

              } while (cursor.moveToNext());

            cursor.close();
        }
        */


        return lista_indicadores;
    }



    public List<Indicador> getIndicadoresAgregadosCalidad(int idEvaluacionCalidad)
    {
        List<Indicador>  lista_indicadores = new ArrayList<>();


        String query = "";

        query = "SELECT * FROM EvaluacionCalidadRegistro WHERE idEvaluacionCalidad="+idEvaluacionCalidad+" AND borradoAl='0000-00-00 00:00:00' GROUP BY idIndicador";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{

                Indicador item= getIndicador(cursor.getInt(2));

                lista_indicadores.add( item );

            } while (cursor.moveToNext());

            cursor.close();
         }


        return lista_indicadores;
    }






    public Hallazgo getHallazgo(String categoria,int id_evaluacion, int id_indicador,String expediente)
    {
        Hallazgo hallazgo ;
        String query = "";

        if(categoria.equals("RECURSO"))
        {
            query = "SELECT * FROM Hallazgo WHERE categoriaEvaluacion='"+categoria+"' AND idEvaluacion="+id_evaluacion+" AND idIndicador="+id_indicador+" AND borradoAl='0000-00-00 00:00:00'";

        }else {
                query = "SELECT * FROM Hallazgo WHERE categoriaEvaluacion='"+categoria+"' AND idEvaluacion="+id_evaluacion+" AND idIndicador="+id_indicador+" AND expediente='"+expediente+"' AND borradoAl='0000-00-00 00:00:00'";

              }


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {

            hallazgo = new Hallazgo(
                                        cursor.getInt(0),
                                        cursor.getInt(1),
                                        cursor.getString(2),
                                        cursor.getInt(3),
                                        cursor.getString(4),
                                        cursor.getInt(5),
                                        cursor.getInt(6),
                                        cursor.getInt(7),
                                        cursor.getInt(8),
                                        cursor.getString(9),

                                        cursor.getString(10),
                                        cursor.getString(11),
                                        cursor.getString(12)
                                );

            cursor.close();
         }else {
            return null;
        }

        return hallazgo;
    }




    public Seguimiento getSeguimiento(int id_hallazgo)
    {
        Seguimiento seguimiento ;
        String query = "";

        query = "SELECT * FROM Seguimiento WHERE idHallazgo="+id_hallazgo;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {

            seguimiento = new Seguimiento(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),

                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );

            cursor.close();
         }else {
            return null;
        }

        return seguimiento;
    }


    public List<Seguimiento> getSeguimientos()
    {
        List<Seguimiento> seguimiento = new ArrayList<>() ;
        String query = "";

        query = "SELECT * FROM Seguimiento";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {

            do{
                    seguimiento.add( new Seguimiento(
                            cursor.getInt(0),
                            cursor.getInt(1),
                            cursor.getInt(2),
                            cursor.getString(3),

                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6)
                    ) );


             } while (cursor.moveToNext());

        cursor.close();

        }

        return seguimiento;
    }


    public Accion getAccion(int id_accion)
    {
        Accion accion ;
        String query = "";

        query = "SELECT * FROM Accion WHERE id="+id_accion;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {

            accion = new Accion(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),

                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );



            cursor.close();
         }else {
            return null;
        }

        return accion;
    }

    public List<Accion> getAcciones()
    {
        List<Accion> acciones = new ArrayList<>() ;
        String query = "";

        query = "SELECT * FROM Accion ORDER BY tipo";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do{


                acciones.add( new Accion(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),

                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ) );

               } while (cursor.moveToNext());

                cursor.close();

        }

        return acciones;
    }



/*
    public JSONObject getEvaluacionesRecurso(String buscar)throws JSONException
    {

        String query;
        int id=0;

        if(buscar==null || buscar.equals(""))
        {
            query = "SELECT * FROM EvaluacionRecurso";
        }else {
                query = "SELECT * FROM EvaluacionRecurso WHERE nombre LIKE \'%"+buscar.toUpperCase().trim()+"%\' OR usuario LIKE \'%"+buscar.toUpperCase().trim()+"%\' OR fecha = \'"+buscar.toUpperCase().trim()+"\'";
              }



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int n = cursor.getCount();

        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();


        if (cursor.moveToFirst()) {
            do {


                obj = new JSONObject();
                try {

                    obj.put("id", cursor.getInt(0));
                    obj.put("idUsuario", cursor.getInt(1));
                    obj.put("clues", cursor.getString(2));
                    obj.put("fechaEvaluacion", cursor.getString(3));
                    obj.put("cerrado", cursor.getInt(4));
                    obj.put("firma", cursor.getString(5));
                    obj.put("responsable", cursor.getString(6));
                    obj.put("creadoAl", cursor.getString(7));
                    obj.put("modificadoAl", cursor.getString(8));
                    obj.put("borradoAl", cursor.getString(9));


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(obj);



            } while (cursor.moveToNext());
        }



        JSONObject  objeto_send = new JSONObject();
        objeto_send.put("evaluacionesRecurso", jsonArray);

        return objeto_send;

    }
*/

/*
    public JSONObject getEvaluacionesRecurso(int idEvaluacionRecurso)throws JSONException
    {

        String query;
        query = "SELECT * FROM EvaluacionRecurso WHERE id="+idEvaluacionRecurso;



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int n = cursor.getCount();

        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();


        if (cursor.moveToFirst()) {
            do {


                obj = new JSONObject();
                try {

                    obj.put("id", cursor.getInt(0));
                    obj.put("idUsuario", cursor.getInt(1));
                    obj.put("clues", cursor.getString(2));
                    obj.put("fechaEvaluacion", cursor.getString(3));
                    obj.put("cerrado", cursor.getInt(4));
                    obj.put("firma", cursor.getString(5));
                    obj.put("responsable", cursor.getString(6));
                    obj.put("creadoAl", cursor.getString(7));
                    obj.put("modificadoAl", cursor.getString(8));
                    obj.put("borradoAl", cursor.getString(9));


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(obj);



            } while (cursor.moveToNext());
        }



        JSONObject  objeto_send = new JSONObject();
        objeto_send.put("evaluacionesRecurso", jsonArray);

        return objeto_send;

    }
*/

    public List<Indicador> getIndicadoresEvaluacionRecurso(int idCone)
    {
          List<Indicador> listaI = new ArrayList<>();

          String query3="SELECT DISTINCT IC.idIndicador FROM ConeIndicadorCriterio CIC JOIN IndicadorCriterio IC WHERE \n"+
                  "CIC.idCone="+idCone+" AND CIC.idIndicadorCriterio=IC.id";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query3, null);

        if (cursor.moveToFirst()) {
            do {

                Indicador item=getIndicador(cursor.getInt(0));

                if(item.categoria.equals("RECURSO"))
                {

                    listaI.add(new Indicador(
                                            item.id,
                                            item.codigo,
                                            item.nombre,
                                            item.color,
                                            item.categoria,item.indicacion) );
                }

               } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
       // db.close();


        return listaI;
    }


    public List<Indicador> getIndicadoresEvaluacionCalidad(int idCone)
    {
        List<Indicador> listaI = new ArrayList<>();

        String query3="SELECT DISTINCT IC.idIndicador FROM ConeIndicadorCriterio CIC JOIN IndicadorCriterio IC WHERE \n"+
                "CIC.idCone="+idCone+" AND CIC.idIndicadorCriterio=IC.id";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query3, null);

        if (cursor.moveToFirst()) {
            do {

                Indicador item=getIndicador(cursor.getInt(0));

                if(item.categoria.equals("CALIDAD"))
                {

                    listaI.add(new Indicador(
                            item.id,
                            item.codigo,
                            item.nombre,
                            item.color,
                            item.categoria,item.indicacion) );
                }

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
       /// db.close();


        return listaI;
    }

/*
    public List<LugarVerificacion> getLugaresVerificacion(int nivel_cone, int id_indicador)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        List<LugarVerificacion> listaLV = new ArrayList<>();

        String query_root="SELECT * FROM ConeIndicadorCriterio WHERE idCone="+nivel_cone;
        Cursor cursor_root = db.rawQuery(query_root, null);

        int roots=0;
        int lugares=0;

        if (cursor_root.moveToFirst())
        {
            do {
                 roots++;
                 int idIndicadorCriterio=cursor_root.getInt(2);

                    String query = "SELECT * WHERE id="+idIndicadorCriterio+" AND idIndicador=" + id_indicador + " GROUP BY idLugarVerificacion";
                    Cursor cursor = db.rawQuery(query, null);

                    if (cursor.moveToFirst())
                    {
                        do {
                              lugares++;
                                String query2 = "SELECT * FROM LugarVerificacion WHERE id=" + cursor.getInt(3);
                                Cursor cursor2 = db.rawQuery(query2, null);

                                if (cursor2.moveToFirst())
                                {
                                    listaLV.add(new LugarVerificacion(cursor2.getInt(0), cursor2.getString(1)));
                                }
                                cursor2.close();

                            } while (cursor.moveToNext());
                    }
                    cursor.close();

               }while (cursor_root.moveToNext());
        }
        cursor_root.close();
        db.close();


        System.out.println("- - - >     CONE-INDICADOR-CRITERIOS: "+roots+", LUGARES : "+lugares+". )))");

        return listaLV;
    }
*/


    public List<LugarVerificacion> getLugaresVerificacion(int nivel_cone, int id_indicador)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        List<LugarVerificacion> listaLV = new ArrayList<>();



        int roots=0;
        int lugares=0;


        String query = "SELECT IC.id,IC.idCriterio,IC.idIndicador,IC.idLugarVerificacion,LV.nombre FROM "+
                        "coneIndicadorCriterio CIC, IndicadorCriterio IC, lugarVerificacion LV  WHERE CIC.idCone="+nivel_cone+
                        " AND CIC.idIndicadorCriterio=IC.id AND IC.idIndicador=" + id_indicador +
                        " AND IC.idLugarVerificacion=LV.id GROUP BY IC.idLugarVerificacion";


        /*

        String query ="SELECT ic.id as id, c.id as idCriterio, ic.idIndicador, lv.id as idlugarVerificacion, c.nombre as criterio, lv.nombre as lugarVerificacion FROM ConeIndicadorCriterio cic" +
                " left join IndicadorCriterio ic on ic.id = cic.idIndicadorCriterio" +
                " left join Criterio c on c.id = ic.idCriterio" +
                " left join LugarVerificacion lv on lv.id = ic.idlugarVerificacion" +
                " WHERE cic.idCone = "+nivel_cone+" AND ic.idIndicador = "+ id_indicador +" GROUP BY lv.id";

*/
                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst())
                {
                    do {
                        lugares++;
                        String query2 = "SELECT * FROM LugarVerificacion WHERE id=" + cursor.getInt(3);
                        Cursor cursor2 = db.rawQuery(query2, null);

                        if (cursor2.moveToFirst())
                        {
                            listaLV.add(new LugarVerificacion(cursor2.getInt(0), cursor2.getString(1)));
                        }
                        cursor2.close();

                    } while (cursor.moveToNext());
                }
                cursor.close();


       // db.close();


        System.out.println("- - - >     CONE-INDICADOR-CRITERIOS: "+roots+", LUGARES : "+lugares+". )))");

        return listaLV;
    }





    public List<Criterio> getCriterios(int nivel_cone,int id_indicador,int id_lugar_verificacion)
    {
        List<Criterio> listaC = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query_root="SELECT * FROM ConeIndicadorCriterio WHERE idCone="+nivel_cone;
        Cursor cursor_root = db.rawQuery(query_root, null);

        int roots=0;
        int lugares=0;

        if (cursor_root.moveToFirst())
        {
            do {

                roots++;
                int idIndicadorCriterio = cursor_root.getInt(2);

                String query = "SELECT * FROM IndicadorCriterio WHERE id="+idIndicadorCriterio+" AND idIndicador=" + id_indicador + " AND idLugarVerificacion=" + id_lugar_verificacion;
                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst())
                {
                    do {

                        String query2 = "SELECT * FROM Criterio WHERE id=" + cursor.getInt(1)+" ORDER BY orden";
                        Cursor cursor2 = db.rawQuery(query2, null);

                        if (cursor2.moveToFirst())
                        {
                            listaC.add(new Criterio(
                                                        cursor2.getInt(0),
                                                        cursor2.getString(1),
                                                        cursor2.getInt(2),
                                                        cursor2.getInt(3) ,
                                                        cursor2.getString(4) )
                                                    );
                        }
                        cursor2.close();

                    } while (cursor.moveToNext());
                }

                cursor.close();
            } while (cursor_root.moveToNext());
        }

        cursor_root.close();
        //db.close();

        return listaC;
    }



    public List<IndicadorValidacion> getValidaciones(int idIndicador)
    {

        List<IndicadorValidacion> validaciones= new ArrayList<>() ;
        String query = "";

        query = "SELECT * FROM IndicadorValidacion WHERE idIndicador="+idIndicador;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            System.out.print("\n UNA VALIDACION ENCONTRADA ---->>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            do {
                 IndicadorValidacion validacion = new IndicadorValidacion(
                                                         cursor.getInt(0),
                                                         cursor.getInt(1),
                                                         cursor.getString(2),
                                                         cursor.getString(3),
                                                         cursor.getString(4),
                                                         cursor.getString(5),
                                                         cursor.getString(6),
                                                         cursor.getString(7),
                                                         cursor.getString(8),
                                                         cursor.getString(9),
                                                         cursor.getString(10)
                                                        );
                validaciones.add(validacion);

                System.out.print("\n UNA VALIDACION ENCONTRADA ---->>>>>>>>>>>>>>>>>>>>>>>>>>>>");
               }while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }

        System.out.print("\nVALIDACIONES LENGTH: ("+validaciones.size()+") ---->>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return validaciones;
    }

    public IndicadorPregunta getIndicadorPregunta(String idIndicadorPregunta)
    {

        IndicadorPregunta indicadorPregunta=null ;
        String query = "";

        query = "SELECT * FROM IndicadorPregunta WHERE id='"+idIndicadorPregunta+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
                indicadorPregunta = new IndicadorPregunta(
                                                            cursor.getString(0),
                                                            cursor.getInt(1),
                                                            cursor.getString(2),
                                                            cursor.getString(3),
                                                            cursor.getInt(4),
                                                            cursor.getString(5),
                                                            cursor.getInt(6),
                                                            cursor.getString(7),
                                                            cursor.getString(8),
                                                            cursor.getString(9)
                                                          );

            cursor.close();
            //db.close();
        }

        return indicadorPregunta;
    }




    public List<CriterioValidacion> getCriterioValidaciones(int idCriterio)
    {

        List<CriterioValidacion> validaciones= new ArrayList<>() ;
        String query = "";

        query = "SELECT * FROM CriterioValidacion WHERE idCriterio="+idCriterio;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                CriterioValidacion validacion = new CriterioValidacion(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10)
                );
                validaciones.add(validacion);

            }while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }

        return validaciones;
    }





    public CriterioPregunta getCriterioPregunta(String idCriterioPregunta)
    {

        CriterioPregunta criterioPregunta=null ;
        String query = "";

        query = "SELECT * FROM CriterioPregunta WHERE id='"+idCriterioPregunta+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            criterioPregunta = new CriterioPregunta(
                    cursor.getString(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9)
            );

            cursor.close();
            //db.close();
        }

        return criterioPregunta;
    }










    public String getNombreClues(String buscar)
    {
        String nombre = null;

        String selectQuery = "SELECT  * FROM Clues WHERE clues =\'"+buscar.toUpperCase().trim()+"\'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            nombre =  cursor.getString(1).toString();

            cursor.close();
            //db.close();
        }else {
            return "--";
        }


        return nombre;
    }

    public String getDomicilioClues(String buscar)
    {
        String domicilio = null;

        String selectQuery = "SELECT  * FROM Clues WHERE clues =\'"+buscar.toUpperCase().trim()+"\'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            domicilio =  cursor.getString(2).toString();

            cursor.close();
            //db.close();
        }else {
            return "--";
        }


        return domicilio;
    }

    public int getNivelCone(String clues)
    {
        int id_cone=0;

        String selectQuery = "SELECT  * FROM Clues WHERE clues =\'"+clues.toUpperCase().trim()+"\'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            id_cone =  cursor.getInt(17);

            cursor.close();
            //db.close();
        }else {
            return 0;
        }


        return id_cone;
    }


    public String getJurisdiccionClues(String buscar)
    {
        String jurisdiccion = null;

        String selectQuery = "SELECT  * FROM Clues WHERE clues =\'"+buscar.toUpperCase().trim()+"\'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            jurisdiccion =  cursor.getString(7).toString();

            cursor.close();
            //db.close();
        }else {
                    return "--";
              }


        return jurisdiccion;
    }



    public String getNombreUsuario(int buscar)
    {
        String nombre = null;

        String selectQuery = "SELECT  * FROM Usuario WHERE id ="+buscar;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            nombre =  cursor.getString(3).toString()+" "+cursor.getString(4).toString()+" "+cursor.getString(5).toString();

            cursor.close();
            //db.close();
        }else {
            return "--";
        }


        return nombre;
    }


    public Usuarios getSignedUser()
    {
        Usuarios user = null;

        String selectQuery = "SELECT  * FROM Usuario WHERE loginActivo=1 LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            user = new Usuarios(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                                cursor.getString(3), cursor.getString(4), cursor.getString(5),
                                cursor.getString(6), cursor.getInt(7),cursor.getString(8), cursor.getString(9));

            cursor.close();
            //db.close();
        }else {
            return null;
        }


        return user;
    }

    public SQLiteDatabase openDB()
    {

        return this.getWritableDatabase();
    }

    public void closeDB(SQLiteDatabase db)
    {
        db.close();
    }


    public int insertarUsuario(Usuarios usuario,SQLiteDatabase db)
      {
          //SQLiteDatabase db = this.getWritableDatabase();
          ContentValues values = new ContentValues();

          System.out.println("BASE ABIERTA A INSERTAR : " + db.toString() + ".");

          values.put("id", usuario.id);
          values.put("username",usuario.username);
          values.put("email",usuario.email);
          values.put("nombres",usuario.nombres);
          values.put("apellidoPaterno",usuario.apellidoPaterno);
          values.put("apellidoMaterno",usuario.apellidoMaterno);
          values.put("avatar", usuario.avatar);
          values.put("loginActivo", usuario.loginActivo);
          values.put("accessToken", usuario.accessToken);
          values.put("refreshToken", usuario.refeshToken);

          long usuario_Id = db.insert("Usuario", null, values);
          System.out.println("SE insertó REGISTRO:" + usuario.username + " , " + usuario_Id + " . OK");
          //db.close(); // Closing database connection
          return (int) usuario_Id;
       }

    public int insertarClues(Clues clues, SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("clues",clues.clues);
        values.put("nombre",clues.nombre);
        values.put("domicilio",clues.domicilio);
        values.put("codigoPostal", clues.codigoPostal);
        values.put("entidad", clues.entidad);
        values.put("municipio",clues.municipio);
        values.put("localidad",clues.localidad);
        values.put("jurisdiccion",clues.jurisdiccion);
        values.put("institucion",clues.institucion);
        values.put("tipoUnidad",clues.tipoUnidad);
        values.put("estatus",clues.estatus);
        values.put("estado",clues.estado);
        values.put("tipologia",clues.tipologia);
        values.put("cone",clues.cone);
        values.put("latitud",clues.latitud);
        values.put("longitud",clues.longitud);
        values.put("idCone", clues.idCone);

        long clues_Id=db.insert("Clues",null,values);

        return (int) clues_Id;
    }

    public int insertarCone(Cone cone,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("id",cone.id);
        values.put("nombre",cone.nombre);

        long cone_Id=db.insert("Cone",null,values);

        return (int) cone_Id;
    }

    public int insertarIndicador(Indicador indicador,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("id",indicador.id);
        values.put("codigo",indicador.codigo);
        values.put("nombre",indicador.nombre);
        values.put("color",indicador.color);
        values.put("categoria",indicador.categoria);
        values.put("indicacion",indicador.indicacion);

        long indicador_Id=db.insert("Indicador",null,values);

        return (int) indicador_Id;
    }

    public int insertarIndicadorValidacion(IndicadorValidacion validacion)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id",validacion.id);
        values.put("idIndicador",validacion.idIndicador);
        values.put("pregunta1",validacion.pregunta1);
        values.put("operadorAritmetico",validacion.operadorAritmetico);
        values.put("pregunta2",validacion.pregunta2);
        values.put("unidadMedida",validacion.unidadMedida);
        values.put("operadorLogico",validacion.operadorLogico);
        values.put("valorComparativo",validacion.valorComparativo);
        values.put("creadoAl",validacion.creadoAl);
        values.put("modificadoAl",validacion.modificadoAl);
        values.put("borradoAl","0000-00-00 00:00:00");

        System.out.println(":::::::::::   ::: VALUES VALIDACION : "+values.toString()+". <<-- ");

        long validacion_Id=0;

        try {
                validacion_Id = db.insert("IndicadorValidacion",null,values);
                System.out.println("INSERTADA LA VALIDACION "+validacion.id+" , ID INDICADOR : "+validacion.idIndicador+" -->>>>>>>>>>>>>>>>><<<<<>>> : ID GENERADO: " +validacion_Id+".");

            }catch (SQLiteException err){ System.out.print("ERROR SQLITE INSERTANDO VALIDACION :"+err.toString() ); }



        return (int) validacion_Id;
    }

    public int insertarCriterioValidacion(CriterioValidacion validacion)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id",validacion.id);
        values.put("idCriterio",validacion.idCriterio);
        values.put("pregunta1",validacion.pregunta1);
        values.put("operadorAritmetico",validacion.operadorAritmetico);
        values.put("pregunta2",validacion.pregunta2);
        values.put("unidadMedida",validacion.unidadMedida);
        values.put("operadorLogico",validacion.operadorLogico);
        values.put("valorComparativo",validacion.valorComparativo);
        values.put("creadoAl",validacion.creadoAl);
        values.put("modificadoAl",validacion.modificadoAl);
        values.put("borradoAl","0000-00-00 00:00:00");

        System.out.println(":::::::::::   ::: VALUES VALIDACION : "+values.toString()+". <<-- ");

        long validacion_Id=0;

        try {
                validacion_Id = db.insert("CriterioValidacion",null,values);
                System.out.println("INSERTADA LA VALIDACION "+validacion.id+" , ID INDICADOR : "+validacion.idCriterio+" -->>>>>>>>>>>>>>>>><<<<<>>> : ID GENERADO: " +validacion_Id+".");

            }catch (SQLiteException err){ System.out.print("ERROR SQLITE INSERTANDO VALIDACION :"+err.toString() ); }



        return (int) validacion_Id;
    }



    public int insertarIndicadorPregunta(IndicadorPregunta indicadorPregunta,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("id",indicadorPregunta.id);
        values.put("idIndicador",indicadorPregunta.idIndicador);
        values.put("nombre",indicadorPregunta.nombre);
        values.put("tipo",indicadorPregunta.tipo);
        values.put("constante",indicadorPregunta.constante);
        values.put("valorConstante",indicadorPregunta.valorConstante);
        values.put("fechaSistema",indicadorPregunta.fechaSistema);
        values.put("creadoAl",indicadorPregunta.creadoAl);
        values.put("modificadoAl",indicadorPregunta.modificadoAl);
        values.put("borradoAl","0000-00-00 00:00:00");


        long validacion_Id=db.insert("IndicadorPregunta",null,values);

        return (int) validacion_Id;
    }

    public int insertarCriterioPregunta(CriterioPregunta criterioPregunta,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("id",criterioPregunta.id);
        values.put("idCriterio",criterioPregunta.idCriterio);
        values.put("nombre",criterioPregunta.nombre);
        values.put("tipo",criterioPregunta.tipo);
        values.put("constante",criterioPregunta.constante);
        values.put("valorConstante",criterioPregunta.valorConstante);
        values.put("fechaSistema",criterioPregunta.fechaSistema);
        values.put("creadoAl",criterioPregunta.creadoAl);
        values.put("modificadoAl",criterioPregunta.modificadoAl);
        values.put("borradoAl","0000-00-00 00:00:00");


        long validacion_Id=db.insert("CriterioPregunta",null,values);

        return (int) validacion_Id;
    }





    public int insertarCriterio(Criterio criterio,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();


        int criterio1=criterio.id;
        String nombre = criterio.nombre;
        int hna= criterio.habilitarNoAplica;




        values.put("id",criterio1);
        values.put("nombre",criterio.nombre);
        values.put("habilitarnoAplica",criterio.habilitarNoAplica);
        values.put("tieneValidacion",criterio.tieneValidacion);
        values.put("orden",criterio.orden);

        long criterio_Id=db.insert("Criterio",null,values);

        return (int) criterio_Id;

    }


    public int insertarIndicadorCriterio(IndicadorCriterio indicadorCriterio,SQLiteDatabase db)
    {

        ContentValues values = new ContentValues();

        values.put("id",indicadorCriterio.id);
        values.put("idCriterio",indicadorCriterio.idCriterio);
        values.put("idIndicador",indicadorCriterio.idIndicador);
        values.put("idLugarVerificacion",indicadorCriterio.idLugarVerificacion);

        long indicadorCriterio_Id=db.insert("IndicadorCriterio",null,values);

        return (int) indicadorCriterio_Id;

    }

    public int insertarConeIndicadorCriterio(ConeIndicadorCriterio coneIndicadorCriterio, SQLiteDatabase db)
    {

        ContentValues values = new ContentValues();

        //values.put("id",coneIndicadorCriterio.id);
        values.put("idCone",coneIndicadorCriterio.idCone);
        values.put("idIndicadorCriterio",coneIndicadorCriterio.idIndicadorCriterio);

        long ConeIndicadorCriterio_Id=db.insert("ConeIndicadorCriterio",null,values);

        return (int) ConeIndicadorCriterio_Id;


    }


    public int insertarLugarVerificacion(LugarVerificacion lugarVerificacion, SQLiteDatabase db)
    {

        ContentValues values = new ContentValues();

        values.put("id",lugarVerificacion.id);
        values.put("nombre",lugarVerificacion.nombre);


        long lugarVerificacion_Id=db.insert("LugarVerificacion",null,values);

        return (int) lugarVerificacion_Id;

    }

    public int insertarEvaluacion(String tipo_evaluacion,Evaluacion evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idServer",evaluacion.getIdServer());
        values.put("idUsuario",evaluacion.getIdUsuario());
        values.put("clues",evaluacion.getClues());
        values.put("fechaEvaluacion",evaluacion.getFechaEvaluacion());
        values.put("cerrado",evaluacion.getCerrado());
        values.put("firma",evaluacion.getFirma());
        values.put("responsable",evaluacion.getResponsable());
        values.put("sincronizado",evaluacion.getSincronizado());
        values.put("compartido",evaluacion.getCompartido());
        values.put("compartidoFull",evaluacion.getCompartidoFull());
        values.put("creadoAl",evaluacion.getCreadoAl());
        values.put("modificadoAl",evaluacion.getModificadoAl());
        values.put("borradoAl",evaluacion.getBorradoAl());




        long evaluacion_id;

        if(tipo_evaluacion.equals("RECURSO"))
        {
            evaluacion_id=db.insert("EvaluacionRecurso",null,values);
        }else {
                evaluacion_id = db.insert("EvaluacionCalidad", null, values);
              }

        return (int) evaluacion_id;

    }

    public int insertarEvaluacionRecursoIndicador(EvaluacionRecursoIndicador eri,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idEvaluacionRecurso", eri.getIdEvaluacionRecurso());
        values.put("idIndicador",eri.getIdIndicador());
        values.put("totalCriterio",eri.getTotalCriterio());
        values.put("avanceCriterio",eri.getAvanceCriterio());

        values.put("creadoAl",eri.getCreadoAl());
        values.put("modificadoAl",eri.getModificadoAl());
        values.put("borradoAl",eri.getBorradoAl());

        long erc_id=0;

        erc_id = db.insert("EvaluacionRecursoIndicador", null, values);

        return (int) erc_id;
    }

    public int insertarEvaluacionRecursoCriterio(EvaluacionRecursoCriterio evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idEvaluacionRecurso", evaluacion.getId_evaluacion_recurso());
        values.put("idCriterio",evaluacion.getId_criterio());
        values.put("idIndicador",evaluacion.getId_indicador());
        values.put("aprobado",evaluacion.getAprobado());
        values.put("creadoAl",evaluacion.getCreadoAl());
        values.put("modificadoAl",evaluacion.getModificadoAl());
        values.put("borradoAl",evaluacion.getBorradoAl());


        long erc_id=0;

        erc_id = db.insert("EvaluacionRecursoCriterio", null, values);


        return (int) erc_id;

    }


    public int insertarEvaluacionCalidadCriterio(EvaluacionCalidadCriterio evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idEvaluacionCalidad", evaluacion.getIdEvaluacionCalidad());
        values.put("idCriterio",evaluacion.getIdCriterio());
        values.put("idIndicador",evaluacion.getIdIndicador());
        values.put("aprobado",evaluacion.getAprobado());
        values.put("idEvaluacionCalidadRegistro",evaluacion.getIdEvaluacionCalidadRegistro());
        values.put("creadoAl",evaluacion.getCreadoAl());
        values.put("modificadoAl",evaluacion.getModificadoAl());
        values.put("borradoAl",evaluacion.getBorradoAl());


        long ecc_id=0;

        ecc_id = db.insert("EvaluacionCalidadCriterio", null, values);


        return (int) ecc_id;

    }

    public int insertarCriterioValidacionRespuesta(CriterioValidacionRespuesta cvr,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        //values.put("id", cvr.id);
        values.put("idEvaluacion",cvr.idEvaluacion);
        values.put("expediente",cvr.expediente);
        values.put("idCriterio",cvr.idCriterio);
        values.put("idCriterioValidacion",cvr.idCriterioValidacion);
        values.put("tipo",cvr.tipo);
        values.put("respuesta1",cvr.respuesta1);
        values.put("respuesta2",cvr.respuesta2);

        values.put("creadoAl",cvr.creadoAl);
        values.put("modificadoAl",cvr.modificadoAl);
        values.put("borradoAl",cvr.borradoAl);

        long ecc_id=0;

        ecc_id = db.insert("CriterioValidacionRespuesta",null,values);


        return (int) ecc_id;

    }


    public int actualizarCriterioValidacionRespuesta(CriterioValidacionRespuesta cvr,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("id", cvr.id);
        values.put("idEvaluacion",cvr.idEvaluacion);
        values.put("expediente",cvr.expediente);
        values.put("idCriterio",cvr.idCriterio);
        values.put("idCriterioValidacion",cvr.idCriterioValidacion);
        values.put("tipo",cvr.tipo);
        values.put("respuesta1",cvr.respuesta1);
        values.put("respuesta2",cvr.respuesta2);

        values.put("creadoAl",cvr.creadoAl);
        values.put("modificadoAl",cvr.modificadoAl);
        values.put("borradoAl",cvr.borradoAl);


        long ecc_id=cvr.id;

        //ecc_id = db.insert("EvaluacionCalidadCriterio", null, values);
        db.update("CriterioValidacionRespuesta", values, "CriterioValidacionRespuesta.id" + "= ?", new String[] { String.valueOf(cvr.id) });


        return (int) ecc_id;

    }


    public List<CriterioValidacionRespuesta> getListaCriterioValidacionRespuesta(String tipoEvaluacion,int idEvaluacion,String expediente, int idCriterio, int idCriterioValidacion)
    {
        List <CriterioValidacionRespuesta> lista = new ArrayList<>();
        String query = "";

        query = "SELECT * FROM CriterioValidacionRespuesta WHERE tipo='"+tipoEvaluacion+"' AND idEvaluacion="+idEvaluacion+" AND expediente='"+expediente+"' AND idCriterio="+idCriterio+" AND idCriterioValidacion="+idCriterioValidacion+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        //selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                lista.add(new CriterioValidacionRespuesta(
                                                            cursor.getInt(0),
                                                            cursor.getInt(1),
                                                            cursor.getString(2),
                                                            cursor.getInt(3),
                                                            cursor.getInt(4),
                                                            cursor.getString(5),
                                                            cursor.getString(6),
                                                            cursor.getString(7),

                                                            cursor.getString(8),
                                                            cursor.getString(9),
                                                            cursor.getString(10)  ) );

            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }

        return lista;
    }

    public List<CriterioValidacionRespuesta> getListaCriterioValidacionRespuestaParaAdaptador(String tipoEvaluacion,int idEvaluacion,String expediente)
    {
        List <CriterioValidacionRespuesta> lista = new ArrayList<>();
        String query = "";

        query = "SELECT * FROM CriterioValidacionRespuesta WHERE tipo='"+tipoEvaluacion+"' AND idEvaluacion="+idEvaluacion+" AND expediente='"+expediente+"' AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        //selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                lista.add(new CriterioValidacionRespuesta(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),

                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10)  ) );

            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }

        return lista;
    }

    public List<CriterioValidacionRespuesta> getListaCriterioValidacionRespuestaTotalPorEvaluacion( String tipoEvaluacion,int idEvaluacion,String expediente )
    {
        List <CriterioValidacionRespuesta> lista = new ArrayList<>();
        String query = "";

        query = "SELECT * FROM CriterioValidacionRespuesta WHERE tipo='"+tipoEvaluacion+"' AND idEvaluacion="+idEvaluacion+" AND expediente='"+expediente+"' AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        //selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                CriterioValidacionRespuesta cvr = new CriterioValidacionRespuesta(
                                                                                    cursor.getInt(0),
                                                                                    cursor.getInt(1),
                                                                                    cursor.getString(2),
                                                                                    cursor.getInt(3),
                                                                                    cursor.getInt(4),
                                                                                    cursor.getString(5),
                                                                                    cursor.getString(6),
                                                                                    cursor.getString(7),

                                                                                    cursor.getString(8),
                                                                                    cursor.getString(9),
                                                                                    cursor.getString(10)  );

                System.out.print("VALOR CVR  : "+ cursor.getInt(0)+". \n");
                System.out.print("VALOR CVR  : "+ cursor.getInt(1)+". \n");
                System.out.print("VALOR CVR  : "+ cursor.getInt(2)+". \n");
                System.out.print("VALOR CVR  : "+ cursor.getInt(3)+". \n");
                System.out.print("VALOR CVR  : "+ cursor.getInt(4)+". \n");
                System.out.print("VALOR CVR  : "+ cursor.getInt(5)+". \n");



                System.out.print("VALOR CVR DESDE BASE : "+ cvr.toString()+".");

                lista.add(cvr);

            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }

        return lista;
    }


    public List<CriterioValidacionRespuesta> getListaCriterioValidacionRespuestaTotalPorCriterio( String tipoEvaluacion,int idEvaluacion,String expediente, int idCriterio )
    {
        List <CriterioValidacionRespuesta> lista = new ArrayList<>();
        String query = "";

        query = "SELECT * FROM CriterioValidacionRespuesta WHERE tipo='"+tipoEvaluacion+"' AND idEvaluacion="+idEvaluacion+" AND expediente='"+expediente+"' AND idCriterio="+idCriterio+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        //selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
            do{
                CriterioValidacionRespuesta cvr = new CriterioValidacionRespuesta(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),

                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10)  );

                System.out.print("VALOR CVR  : "+ cursor.getInt(0)+". \n");
                System.out.print("VALOR CVR  : "+ cursor.getInt(1)+". \n");
                System.out.print("VALOR CVR  : "+ cursor.getInt(2)+". \n");
                System.out.print("VALOR CVR  : "+ cursor.getInt(3)+". \n");
                System.out.print("VALOR CVR  : "+ cursor.getInt(4)+". \n");
                System.out.print("VALOR CVR  : "+ cursor.getInt(5)+". \n");



                System.out.print("VALOR CVR DESDE BASE : "+ cvr.toString()+".");

                lista.add(cvr);

            } while (cursor.moveToNext());

            cursor.close();
            //db.close();
        }

        return lista;
    }



    public CriterioValidacionRespuesta getCriterioValidacionRespuesta(String tipoEvaluacion,int idEvaluacion,String expediente, int idCriterio, int idCriterioValidacion)
    {
        CriterioValidacionRespuesta cvr = null;
        String query = "";

        query = "SELECT * FROM CriterioValidacionRespuesta WHERE tipo='"+tipoEvaluacion+"' AND idEvaluacion="+idEvaluacion+" AND expediente='"+expediente+"' AND idCriterio="+idCriterio+" AND idCriterioValidacion="+idCriterioValidacion+" AND borradoAl='0000-00-00 00:00:00'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        //selectQuery,selectedArguments

        if (cursor.moveToFirst())
        {
                cvr = new CriterioValidacionRespuesta(
                                                        cursor.getInt(0),
                                                        cursor.getInt(1),
                                                        cursor.getString(2),
                                                        cursor.getInt(3),
                                                        cursor.getInt(4),
                                                        cursor.getString(5),
                                                        cursor.getString(6),
                                                        cursor.getString(7),

                                                        cursor.getString(8),
                                                        cursor.getString(9),
                                                        cursor.getString(10)  );
        }

        cursor.close();
        //db.close();


        return cvr;
    }



    public int actualizarEvaluacionCalidadCriterio(EvaluacionCalidadCriterio evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idEvaluacionCalidad", evaluacion.getIdEvaluacionCalidad());
        values.put("idCriterio",evaluacion.getIdCriterio());
        values.put("idIndicador",evaluacion.getIdIndicador());
        values.put("aprobado",evaluacion.getAprobado());
        values.put("idEvaluacionCalidadRegistro",evaluacion.getIdEvaluacionCalidadRegistro());
        values.put("creadoAl",evaluacion.getCreadoAl());
        values.put("modificadoAl",evaluacion.getModificadoAl());
        values.put("borradoAl",evaluacion.getBorradoAl());


        long ecc_id=evaluacion.id;

        //ecc_id = db.insert("EvaluacionCalidadCriterio", null, values);
        db.update("EvaluacionCalidadCriterio", values, "EvaluacionCalidadCriterio.id" + "= ?", new String[] { String.valueOf(evaluacion.id) });


        return (int) ecc_id;

    }



    public int insertarEvaluacionCalidadRegistro(EvaluacionCalidadRegistro evaluacion,SQLiteDatabase db)
    {

        ContentValues values = new ContentValues();

            values.put("idEvaluacionCalidad", evaluacion.getIdEvaluacionCalidad());
            values.put("idIndicador",evaluacion.getIdIndicador());
            values.put("columna",evaluacion.getColumna());
            values.put("expediente",evaluacion.getExpediente());
            values.put("cumple",evaluacion.getCumple());
            values.put("promedio",evaluacion.getPromedio());
            values.put("totalCriterio",evaluacion.getTotalCriterio());
            values.put("avanceCriterio",evaluacion.getAvanceCriterio());
            values.put("creadoAl",evaluacion.getCreadoAl());
            values.put("modificadoAl",evaluacion.getModificadoAl());
            values.put("borradoAl",evaluacion.getBorradoAl());


        long ecr_id=0;

        ecr_id = db.insert("EvaluacionCalidadRegistro", null, values);

        return (int) ecr_id;

    }



    public int actualizarEvaluacionCalidadRegistro(EvaluacionCalidadRegistro evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

            values.put("idEvaluacionCalidad", evaluacion.getIdEvaluacionCalidad());
            values.put("idIndicador",evaluacion.getIdIndicador());
            values.put("columna",evaluacion.getColumna());
            values.put("expediente",evaluacion.getExpediente());
            values.put("cumple",evaluacion.getCumple());
            values.put("promedio",evaluacion.getPromedio());
            values.put("totalCriterio",evaluacion.getTotalCriterio());
            values.put("avanceCriterio",evaluacion.getAvanceCriterio());
            values.put("creadoAl",evaluacion.getCreadoAl());
            values.put("modificadoAl",evaluacion.getModificadoAl());
            values.put("borradoAl",evaluacion.getBorradoAl());


        long ecr_id=evaluacion.id;

        //ecr_id = db.insert("EvaluacionCalidadRegistro", null, values);

        db.update("EvaluacionCalidadRegistro", values, "EvaluacionCalidadRegistro.id" + "= ?", new String[] { String.valueOf(evaluacion.id) });



        return (int) ecr_id;

    }




    public int insertarHallazgo(Hallazgo hallazgo,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        //values.put("id",hallazgo.getId());
        values.put("idEvaluacion",hallazgo.getIdEvaluacion());
        values.put("categoriaEvaluacion",hallazgo.getCategoriaEvaluacion());
        values.put("idIndicador",hallazgo.getIdIndicador());
        values.put("expediente",hallazgo.getExpediente());
        values.put("idUsuario",hallazgo.getIdUsuario());
        values.put("idAccion",hallazgo.getIdAccion());
        values.put("idPlazoAccion",hallazgo.getIdPlazoAccion());
        values.put("resuelto",hallazgo.getResuelto());
        values.put("descripcion",hallazgo.getDescripcion());

        values.put("creadoAl",hallazgo.getCreadoAl());
        values.put("modificadoAl",hallazgo.getModificadoAl());
        values.put("borradoAl",hallazgo.getBorradoAl());


        long hallazgo_id=0;

        hallazgo_id = db.insert("Hallazgo", null, values);


        return (int) hallazgo_id;

    }

    public int actualizarHallazgo(Hallazgo hallazgo,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        //values.put("id",hallazgo.getId());
        values.put("idEvaluacion",hallazgo.getIdEvaluacion());
        values.put("categoriaEvaluacion",hallazgo.getCategoriaEvaluacion());
        values.put("idIndicador",hallazgo.getIdIndicador());
        values.put("expediente",hallazgo.getExpediente());
        values.put("idUsuario",hallazgo.getIdUsuario());
        values.put("idAccion",hallazgo.getIdAccion());
        values.put("idPlazoAccion",hallazgo.getIdPlazoAccion());
        values.put("resuelto",hallazgo.getResuelto());
        values.put("descripcion",hallazgo.getDescripcion());

        values.put("creadoAl",hallazgo.getCreadoAl());
        values.put("modificadoAl",hallazgo.getModificadoAl());
        values.put("borradoAl",hallazgo.getBorradoAl());

        long hallazgo_id=0;

        db.update("Hallazgo", values, "Hallazgo.id" + "= ?", new String[] { String.valueOf(hallazgo.id) });

        return (int) hallazgo_id;

    }


    public int actualizarUsuario(Usuarios usuario,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        //values.put("id",hallazgo.getId());
        values.put("id",usuario.getId());
        values.put("username",usuario.getUsername());
        values.put("email",usuario.getEmail());
        values.put("nombres",usuario.getNombres());
        values.put("apellidoPaterno",usuario.getApellidoPaterno());
        values.put("apellidoMaterno",usuario.getApellidoMaterno());
        values.put("avatar",usuario.getAvatar());
        values.put("loginActivo",usuario.getLoginActivo());
        values.put("accessToken",usuario.getAccessToken());
        values.put("refreshToken",usuario.getRefeshToken());

        //values.put("creadoAl",hallazgo.getCreadoAl());
        //values.put("modificadoAl",hallazgo.getModificadoAl());
        //values.put("borradoAl",hallazgo.getBorradoAl());


        long usuario_id=usuario.getId();

        SQLiteDatabase db2 = this.getWritableDatabase();

        db2.update("Usuario", values, "Usuario.id" + "= ?", new String[] { String.valueOf(usuario.id) });



        return (int) usuario_id;

    }




    public int insertarSeguimiento(Seguimiento seguimiento,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("id",seguimiento.getId());
        values.put("idUsuario",seguimiento.getIdUsuario());
        values.put("idHallazgo",seguimiento.getIdHallazgo());
        values.put("descripcion",seguimiento.getDescripcion());


        values.put("creadoAl",seguimiento.getCreadoAl());
        values.put("modificadoAl",seguimiento.getModificadoAl());
        values.put("borradoAl",seguimiento.getBorradoAl());


        long seguimiento_id=0;

        seguimiento_id = db.insert("Seguimiento", null, values);


        return (int) seguimiento_id;

    }

    public int insertarAccion(Accion accion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("id",accion.getId());
        values.put("nombre",accion.getNombre());
        values.put("tipo",accion.getTipo());


        values.put("creadoAl",accion.getCreadoAl());
        values.put("modificadoAl",accion.getModificadoAl());
        values.put("borradoAl",accion.getBorradoAl());


        long accion_id=0;

        accion_id = db.insert("Accion", null, values);


        return (int) accion_id;

    }




    public Config getConfig(String seccion, int idUsuario)
    {
        Config config = null;
        String query="";

        if(seccion.equals("CLUES"))
        {
            query = "SELECT  * FROM Config WHERE seccion=\'"+seccion+"\'";
        }else{
                query = "SELECT  * FROM Config WHERE seccion=\'"+seccion+"\' AND idUsuario="+idUsuario;
             }


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            config = new Config(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getString(3),
                            cursor.getString(4),

                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7));

            cursor.close();
            //db.close();
        }else {
                return null;
              }


        return config;
    }


    public int insertarConfig(Config config)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put("id",config.getId());
        values.put("seccion", config.getSeccion());
        values.put("idUsuario", config.getIdUsuario());
        values.put("fechaAccion",config.getFechaAccion());
        values.put("status",config.getStatus());


        values.put("creadoAl",config.getCreadoAl());
        values.put("modificadoAl",config.getModificadoAl());
        values.put("borradoAl",config.getBorradoAl());


        long config_id=0;

        config_id = db.insert("Config", null, values);


        return (int) config_id;

    }


    public int actualizarConfig(Config config)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id",config.getId());
        values.put("seccion", config.getSeccion());
        values.put("idUsuario", config.getIdUsuario());
        values.put("fechaAccion",config.getFechaAccion());
        values.put("status",config.getStatus());


        values.put("creadoAl",config.getCreadoAl());
        values.put("modificadoAl",config.getModificadoAl());
        values.put("borradoAl",config.getBorradoAl());


        long config_id=0;

        db.update("Config", values, "Config.id" + "= ?", new String[] { String.valueOf(config.id) });

        return (int) config_id;

    }





    public int actualizarEvaluacionRecurso(Evaluacion evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idServer",evaluacion.getIdServer());
        values.put("idUsuario",evaluacion.getIdUsuario());
        values.put("clues",evaluacion.getClues());
        values.put("fechaEvaluacion",evaluacion.getFechaEvaluacion());
        values.put("cerrado",evaluacion.getCerrado());
        values.put("firma",evaluacion.getFirma());
        values.put("responsable",evaluacion.getResponsable());
        values.put("sincronizado",evaluacion.getSincronizado());
        values.put("creadoAl",evaluacion.getCreadoAl());
        values.put("modificadoAl",evaluacion.getModificadoAl());
        values.put("borradoAl",evaluacion.getBorradoAl());


        long er_id=0;

        //erc_id = db.insert("EvaluacionRecursoCriterio", null, values);


        db.update("EvaluacionRecurso", values, "EvaluacionRecurso.id" + "= ?", new String[] { String.valueOf(evaluacion.id) });

        System.out.println("EVALUACION RECURSO EDITADA [[[ "+evaluacion.getFirma()+" ]]]");

        return (int) er_id;

    }

    public int actualizarEvaluacionCalidad(Evaluacion evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idServer",evaluacion.getIdServer());
        values.put("idUsuario",evaluacion.getIdUsuario());
        values.put("clues",evaluacion.getClues());
        values.put("fechaEvaluacion",evaluacion.getFechaEvaluacion());
        values.put("cerrado",evaluacion.getCerrado());
        values.put("firma",evaluacion.getFirma());
        values.put("responsable",evaluacion.getResponsable());
        values.put("sincronizado",evaluacion.getSincronizado());
        values.put("creadoAl",evaluacion.getCreadoAl());
        values.put("modificadoAl",evaluacion.getModificadoAl());
        values.put("borradoAl",evaluacion.getBorradoAl());


        long erc_id=0;



        db.update("EvaluacionCalidad", values, "EvaluacionCalidad.id" + "= ?", new String[] { String.valueOf(evaluacion.id) });

        System.out.println("EVALUACION RECURSO EDITADA [[[ "+evaluacion.getFirma()+" ]]]");


        return (int) erc_id;

    }


    public int actualizarEvaluacionRecursoIndicador(EvaluacionRecursoIndicador evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idEvaluacionRecurso",evaluacion.getIdEvaluacionRecurso());
        values.put("idIndicador",evaluacion.getIdIndicador());
        values.put("totalCriterio",evaluacion.getTotalCriterio());
        values.put("avanceCriterio",evaluacion.getAvanceCriterio());

        values.put("creadoAl",evaluacion.getCreadoAl());
        values.put("modificadoAl",evaluacion.getModificadoAl());
        values.put("borradoAl",evaluacion.getBorradoAl());


        long erc_id=0;

        db.update("EvaluacionRecursoIndicador", values, "EvaluacionRecursoIndicador.id" + "= ?", new String[] { String.valueOf(evaluacion.id) });



        return (int) erc_id;

    }

    public int actualizarEvaluacionRecursoCriterio(EvaluacionRecursoCriterio evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idEvaluacionRecurso",evaluacion.getId_evaluacion_recurso());
        values.put("idCriterio",evaluacion.getId_criterio());
        values.put("idIndicador",evaluacion.getId_indicador());
        values.put("aprobado",evaluacion.getAprobado());
        values.put("creadoAl",evaluacion.getCreadoAl());
        values.put("modificadoAl",evaluacion.getModificadoAl());
        values.put("borradoAl",evaluacion.getBorradoAl());


        long erc_id=0;

        //erc_id = db.insert("EvaluacionRecursoCriterio", null, values);


        db.update("EvaluacionRecursoCriterio", values, "EvaluacionRecursoCriterio.id" + "= ?", new String[] { String.valueOf(evaluacion.id) });



        return (int) erc_id;

    }


    public int cerrarEvaluacion(String tipo,Evaluacion evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idUsuario",evaluacion.getIdUsuario());
        values.put("clues",evaluacion.getClues());
        values.put("fechaEvaluacion",evaluacion.getFechaEvaluacion());
        values.put("cerrado",1);
        values.put("firma",evaluacion.getFirma());
        values.put("responsable",evaluacion.getResponsable());
        values.put("creadoAl",evaluacion.getCreadoAl());
        values.put("modificadoAl",evaluacion.getModificadoAl());
        values.put("borradoAl",evaluacion.getBorradoAl());


        long erc_id=0;



        if(tipo.equals("RECURSO")) {
            db.update("EvaluacionRecurso", values, "EvaluacionRecurso.id" + "= ?", new String[]{String.valueOf(evaluacion.id)});
        }else{
            db.update("EvaluacionCalidad", values, "EvaluacionCalidad.id" + "= ?", new String[]{String.valueOf(evaluacion.id)});
        }


        return (int) erc_id;

    }

    public int firmarEvaluacion(String tipo,Evaluacion evaluacion,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        values.put("idUsuario", evaluacion.getIdUsuario());
        values.put("clues",evaluacion.getClues());
        values.put("fechaEvaluacion",evaluacion.getFechaEvaluacion());
        values.put("cerrado",evaluacion.getCerrado());
        values.put("firma",evaluacion.getFirma());
        values.put("responsable",evaluacion.getResponsable());
        values.put("emailResponsable",evaluacion.getEmailResponsable());

        System.out.println("---------------------------  CLUES    : "+evaluacion.getClues()+" .");
        System.out.println("---------------------------  RESPONSABLE EN FIRMA DB METODO : "+evaluacion.getResponsable()+" .");
        System.out.println("---------------------------  EMAIL RESPONSABLE EN FIRMA DB METODO : "+evaluacion.getEmailResponsable().toString()+" .");
        System.out.println("---------------------------  FIRMA DB METODO : "+evaluacion.getFirma()+" .");

        values.put("creadoAl",evaluacion.getCreadoAl());
        values.put("modificadoAl",evaluacion.getModificadoAl());
        values.put("borradoAl",evaluacion.getBorradoAl());


        long erc_id=0;


        try {

            if (tipo.equals("RECURSO")) {
                db.update("EvaluacionRecurso", values, "EvaluacionRecurso.id" + "= ?", new String[]{String.valueOf(evaluacion.id)});
            } else {
                db.update("EvaluacionCalidad", values, "EvaluacionCalidad.id" + "= ?", new String[]{String.valueOf(evaluacion.id)});
            }
        }catch (SQLiteException err)
        {
            System.out.println("---------------------------  ERROR ACTUALIZANDO FIRMA : "+err.getMessage()+" .");

            err.printStackTrace();
        }


        return (int) erc_id;

    }




    //db.update(Student.TABLE, values, Student.KEY_ID + "= ?", new String[] { String.valueOf(student.student_ID) });.
////************************************************************************************************
////************************************************************************************************


    public int insertarUniversal(final Object objeto_insertar)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        Class c1 = objeto_insertar.getClass();
        Field[] valueObjFields = c1.getDeclaredFields();

        for (int i = 0; i < valueObjFields.length; i++)
        {
            String nombre_campo = valueObjFields[i].getName();
            String tipo_campo = valueObjFields[i].getType().toString();

            valueObjFields[i].setAccessible(true);
            Object objeto = null;

            try {
                    objeto = valueObjFields[i].get(objeto_insertar);

                    System.out.println(" -- -- -- --  > > > NOMBRE CAMPO : [ "+nombre_campo+" ], TIPO : [ "+tipo_campo+" ], VALOR : [ "+objeto+" ]");

                    if(nombre_campo.equals("id") || nombre_campo.equals("$change"))
                    {}else {
                                values.put(nombre_campo, String.valueOf(objeto));
                            }

            } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
        }


        long id = db.insert(objeto_insertar.getClass().getSimpleName(),null,values);

        return (int) id;
    }
////************************************************************************************************
////************************************************************************************************



    public void signOut()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Usuarios user_out=getSignedUser();

        if( user_out != null )
        {
            user_out.setLoginActivo(0);
            actualizarUsuario(user_out,db);
        }

    }

    public void resetDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("Delete from Usuario");
        /*
        db.execSQL("Delete from Clues");
        db.execSQL("Delete from Clues");
        db.execSQL("Delete from Clues");
        db.execSQL("Delete from Clues");
        db.execSQL("Delete from Clues");
        db.execSQL("Delete from Clues");
        db.execSQL("Delete from Clues");
        db.execSQL("Delete from Clues");
        */
        //int filas=db.delete("Usuario",null,null);

    }

    public void resetTableDB(String tabla)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //db.execSQL("Delete from Usuario");
        db.execSQL("Delete from "+tabla);
        //int filas=db.delete("Usuario",null,null);

    }


    public List<String> getUnidades(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM Unidades";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        //db.close();

        // returning lables
        return list;
    }

    public Date stringtoDate(String fecha)
    {
        String startDate=fecha;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
                date = sdf1.parse(startDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Date sqlStartDate = new Date(date.getTime());

        return sqlStartDate;
    }




    public Version getVersion(int id_version)
    {
        Version version ;
        String query = "";

        query = "SELECT * FROM Version WHERE id="+id_version;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {

            version = new Version(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),

                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
             );


            cursor.close();
        }else {
            return null;
        }

        return version;
    }

    public Version getVersionInstalada()
    {
        Version version ;
        String query = "";

        query = "SELECT * FROM Version WHERE instalado=1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {

            version = new Version(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),

                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );


            cursor.close();
        }else {
            return null;
        }

        return version;
    }

    public List<Version> getVersiones()
    {
        List<Version> versiones = new ArrayList<>() ;
        String query = "";

        query = "SELECT * FROM Version";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do{


                versiones.add( new Version(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getInt(5),

                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8)
                ) );

            } while (cursor.moveToNext());

            cursor.close();

        }

        return versiones;
    }





    public int insertarVersion(Version version,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();

        //values.put("id",version.getId());
        values.put("path",version.getPath());
        values.put("versionApp",version.getVersionApp());
        values.put("versionDB",version.getVersionDB());
        values.put("descripcion",version.getDescripcion());
        values.put("instalado",version.getInstalado());



        values.put("creadoAl",version.getCreadoAl());
        values.put("modificadoAl",version.getModificadoAl());
        values.put("borradoAl",version.getBorradoAl());


        long accion_id=0;

        accion_id = db.insert("Version", null, values);


        return (int) accion_id;

    }


    public int actualizarVersion(Version version)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id",version.getId());
        values.put("path",version.getPath());
        values.put("versionApp",version.getVersionApp());
        values.put("versionDB",version.getVersionDB());
        values.put("descripcion",version.getDescripcion());
        values.put("instalado",version.getInstalado());



        values.put("creadoAl",version.getCreadoAl());
        values.put("modificadoAl",version.getModificadoAl());
        values.put("borradoAl",version.getBorradoAl());


        long config_id=0;

        db.update("Config", values, "Version.id" + "= ?", new String[] { String.valueOf(version.id) });

        return (int) config_id;

    }




    public void vaciarVersiones()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("Delete from Version");

    }







}
