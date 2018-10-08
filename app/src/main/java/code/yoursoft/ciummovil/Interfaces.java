package code.yoursoft.ciummovil;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class Interfaces {


}


class Usuarios
{
    public int id;
    public String username;
    public String email;
    public String nombres;
    public String apellidoPaterno;
    public String apellidoMaterno;
    public String avatar;
    public int loginActivo;
    public String accessToken;
    public String refeshToken;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }





    public String getAvatar() {

        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getApellidoMaterno() {

        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {

        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getNombres()
    {
        return nombres;
    }

    public void setNombres(String nombres)
    {
        this.nombres = nombres;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLoginActivo() {
        return loginActivo;
    }

    public void setLoginActivo(int loginActivo) {
        this.loginActivo = loginActivo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefeshToken() {
        return refeshToken;
    }

    public void setRefeshToken(String refeshToken) {
        this.refeshToken = refeshToken;
    }

    public Usuarios( int id,String username, String email, String nombres,
                     String apellidoPaterno, String apellidoMaterno,String avatar,int loginActivo,String accessToken,
                     String refreshToken)
      {
          super();

          this.id=id;

          this.username=username;
          this.email=email;
          this.nombres=nombres;

          this.apellidoPaterno=apellidoPaterno;
          this.apellidoMaterno=apellidoMaterno;
          this.avatar=avatar;
          this.loginActivo=loginActivo;
          this.accessToken=accessToken;
          this.refeshToken=refreshToken;
      }


}






///////    CLASE CLUES PARSE
class Clues
{
    public String clues;
    public String nombre;
    public String domicilio;
    public String codigoPostal;
    public String entidad;
    public String municipio;
    public String localidad;
    public String jurisdiccion;
    public String claveJurisdiccion;
    public String institucion;
    public String tipoUnidad;
    public String estatus;
    public String estado;
    public String tipologia;
    public String cone;
    public String latitud;
    public String longitud;
    public int idCone;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClues() {

        return clues;
    }

    public void setClues(String clues) {
        this.clues = clues;
    }

    public String getCone() {
        return cone;
    }

    public void setCone(String cone) {
        this.cone = cone;
    }

    public String getTipologia() {

        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getEstado() {

        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstatus() {

        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getTipoUnidad() {

        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public String getInstitucion() {

        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getJurisdiccion() {

        return jurisdiccion;
    }

    public void setJurisdiccion(String jurisdiccion) {
        this.jurisdiccion = jurisdiccion;
    }

    public String getLocalidad() {

        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getMunicipio() {

        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEntidad() {

        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getCodigoPostal() {

        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getDomicilio() {

        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getClaveJurisdiccion() {
        return claveJurisdiccion;
    }

    public void setClaveJurisdiccion(String claveJurisdiccion) {
        this.claveJurisdiccion = claveJurisdiccion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getIdCone() {
        return idCone;
    }

    public void setIdCone(int idCone) {
        this.idCone = idCone;
    }

    public Clues(String clues, String nombre, String domicilio,String codigoPostal,
                 String entidad, String municipio, String localidad,String jurisdiccion,
                 String claveJurisdiccion,String institucion,String tipoUnidad,String estatus,String estado,
                 String tipologia,String cone,String latitud, String longitud,int idCone)
    {
       super();

        this.clues=clues;
        this.nombre=nombre;
        this.domicilio=domicilio;
        this.codigoPostal=codigoPostal;
        this.entidad=entidad;
        this.municipio=municipio;

        this.localidad=localidad;
        this.jurisdiccion=jurisdiccion;
        this.claveJurisdiccion=claveJurisdiccion;
        this.institucion=institucion;
        this.tipoUnidad=tipoUnidad;
        this.estatus=estatus;
        this.estado=estado;
        this.tipologia=tipologia;
        this.cone=cone;
        this.latitud=latitud;
        this.longitud=longitud;

        this.idCone=idCone;


    }


}/// FIN CLASE CLUES



class Cone
{
    public int id;
    public String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Cone(int id,String nombre)
    {
        super();

        this.id=id;
        this.nombre=nombre;

    }


}


class Indicador
{



    public int id;
    public String codigo;
    public String nombre;
    public String color;
    public String categoria;
    public String indicacion;

    public Indicador(int id, String codigo, String nombre, String color, String categoria, String indicacion)
    {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.color = color;
        this.categoria = categoria;
        this.indicacion = indicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIndicacion() {
        return indicacion;
    }

    public void setIndicacion(String indicacion) {
        this.indicacion = indicacion;
    }
}///FIN CLASS INDICADOR


class Criterio
{
    public int id;
    public String nombre;
    public int habilitarNoAplica;
    public int tieneValidacion;
    public String orden;

    public Criterio(int id, String nombre, int habilitarNoAplica, int tieneValidacion, String orden)
    {
        this.id = id;
        this.nombre = nombre;
        this.habilitarNoAplica = habilitarNoAplica;
        this.tieneValidacion = tieneValidacion;
        this.orden = orden;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getHabilitarNoAplica() {
        return habilitarNoAplica;
    }

    public void setHabilitarNoAplica(int habilitarNoAplica) {
        this.habilitarNoAplica = habilitarNoAplica;
    }

    public int getTieneValidacion() {
        return tieneValidacion;
    }

    public void setTieneValidacion(int tieneValidacion) {
        this.tieneValidacion = tieneValidacion;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }
}

class IndicadorCriterio
{

    public int id;
    public int idCriterio;
    public int idIndicador;
    public int idLugarVerificacion;

    public IndicadorCriterio(int id, int idCriterio, int idIndicador, int idLugarVerificacion)
    {
        super();

        this.id = id;
        this.idCriterio = idCriterio;
        this.idIndicador = idIndicador;
        this.idLugarVerificacion = idLugarVerificacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    public int getIdLugarVerificacion() {
        return idLugarVerificacion;
    }

    public void setIdLugarVerificacion(int idLugarVerificacion) {
        this.idLugarVerificacion = idLugarVerificacion;
    }
}


class ConeIndicadorCriterio
{

    public int id;
    public int idCone;
    public int idIndicadorCriterio;

    public ConeIndicadorCriterio(int id, int idCone, int idIndicadorCriterio)
    {
        super();

        this.id = id;
        this.idCone = idCone;
        this.idIndicadorCriterio = idIndicadorCriterio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCone() {
        return idCone;
    }

    public void setIdCone(int idCone) {
        this.idCone = idCone;
    }

    public int getIdIndicadorCriterio() {
        return idIndicadorCriterio;
    }

    public void setIdIndicadorCriterio(int idIndicadorCriterio) {
        this.idIndicadorCriterio = idIndicadorCriterio;
    }
}







class LugarVerificacion
{
    public int id;
    public String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LugarVerificacion(int id, String nombre)
    {
        super();

        this.id = id;
        this.nombre = nombre;

    }
}

class Perfil
{

    public int id;
    public String nombre;
    public String email;
    public String apellidoPaterno;
    public String apellidoMaterno;
    public String avatar;
    public String access_token;
    public String refresh_token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Perfil(int id, String nombre, String email, String apellidoPaterno, String apellidoMaterno, String avatar,String access_token, String refresh_token)
    {

        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.avatar = avatar;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }


}


class Evaluacion
 {

     public int id;
     public int idServer;
     public int idUsuario;
     public String clues;
     public String fechaEvaluacion;
     public int cerrado;
     public String firma;
     public String responsable;
     public String emailResponsable;
     public int sincronizado;
     public int compartido;
     public int compartidoFull;
     public String creadoAl;
     public String modificadoAl;
     public String borradoAl;

     public Evaluacion(int id,int idServer, int idUsuario, String clues, String fechaEvaluacion, int cerrado, String firma, String responsable,String emailResponsable, int sincronizado, int compartido, int compartidoFull, String creadoAl, String modificadoAl, String borradoAl)
     {
         super();

         this.id = id;
         this.idServer=idServer;
         this.idUsuario = idUsuario;
         this.clues = clues;
         this.fechaEvaluacion = fechaEvaluacion;
         this.cerrado = cerrado;
         this.firma = firma;
         this.responsable = responsable;
         this.emailResponsable = emailResponsable;
         this.sincronizado = sincronizado;
         this.compartido = compartido;
         this.compartidoFull = compartidoFull;
         this.creadoAl = creadoAl;
         this.modificadoAl = modificadoAl;
         this.borradoAl = borradoAl;
     }

     public int getId() {
         return id;
     }

     public void setId(int id) {
         this.id = id;
     }

     public int getIdServer() {
         return idServer;
     }

     public void setIdServer(int idServer) {
         this.idServer = idServer;
     }

     public int getIdUsuario() {
         return idUsuario;
     }

     public void setIdUsuario(int idUsuario) {
         this.idUsuario = idUsuario;
     }

     public String getClues() {
         return clues;
     }

     public void setClues(String clues) {
         this.clues = clues;
     }

     public String getFechaEvaluacion() {
         return fechaEvaluacion;
     }

     public void setFechaEvaluacion(String fechaEvaluacion) {
         this.fechaEvaluacion = fechaEvaluacion;
     }

     public int getCerrado() {
         return cerrado;
     }

     public void setCerrado(int cerrado) {
         this.cerrado = cerrado;
     }

     public String getFirma() {
         return firma;
     }

     public void setFirma(String firma) {
         this.firma = firma;
     }

     public String getResponsable() {
         return responsable;
     }

     public void setResponsable(String responsable) {
         this.responsable = responsable;
     }

     public String getEmailResponsable() {
         return emailResponsable;
     }

     public void setEmailResponsable(String emailResponsable) {
         this.emailResponsable = emailResponsable;
     }

     public String getCreadoAl() {
         return creadoAl;
     }

     public int getSincronizado() {
         return sincronizado;
     }

     public void setSincronizado(int sincronizado) {
         this.sincronizado = sincronizado;
     }

     public int getCompartido() {
         return compartido;
     }

     public void setCompartido(int compartido) {
         this.compartido = compartido;
     }

     public int getCompartidoFull() {
         return compartidoFull;
     }

     public void setCompartidoFull(int compartidoFull) {
         this.compartidoFull = compartidoFull;
     }

     public void setCreadoAl(String creadoAl) {
         this.creadoAl = creadoAl;
     }

     public String getModificadoAl() {
         return modificadoAl;
     }

     public void setModificadoAl(String modificadoAl) {
         this.modificadoAl = modificadoAl;
     }

     public String getBorradoAl() {
         return borradoAl;
     }

     public void setBorradoAl(String borradoAl) {
         this.borradoAl = borradoAl;
     }


 }




class EvaluacionDetalles
{

    public int id;
    public int idUsuario;
    public String usuarioNombre;
    public String clues;
    public String cluesNombre;
    public String cluesDomicilio;
    public String cluesJurisdiccion;
    public String cluesMunicipio;
    public String cluesLocalidad;
    public String fechaEvaluacion;
    public int cerrado;
    public int sincronizado;
    public int compartido;
    public int compartidoFull;
    public String firma;
    public String responsable;

    public int indicadores;
    public int expedientes;
    public int criterios;
    public int avanceCriterios;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;


    public EvaluacionDetalles(int id, int idUsuario, String usuarioNombre, String clues, String cluesNombre, String cluesDomicilio, String cluesJurisdiccion, String cluesMunicipio, String cluesLocalidad, String fechaEvaluacion, int cerrado, int sincronizado, int compartido, int compartidoFull, String firma, String responsable, int indicadores, int expedientes, int criterios, int avanceCriterios, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.usuarioNombre = usuarioNombre;
        this.clues = clues;
        this.cluesNombre = cluesNombre;
        this.cluesDomicilio = cluesDomicilio;
        this.cluesJurisdiccion = cluesJurisdiccion;
        this.cluesMunicipio = cluesMunicipio;
        this.cluesLocalidad = cluesLocalidad;
        this.fechaEvaluacion = fechaEvaluacion;
        this.cerrado = cerrado;
        this.sincronizado = sincronizado;
        this.compartido = compartido;
        this.compartidoFull = compartidoFull;
        this.firma = firma;
        this.responsable = responsable;
        this.indicadores = indicadores;
        this.expedientes = expedientes;
        this.criterios = criterios;
        this.avanceCriterios = avanceCriterios;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getClues() {
        return clues;
    }

    public void setClues(String clues) {
        this.clues = clues;
    }

    public String getCluesNombre() {
        return cluesNombre;
    }

    public void setCluesNombre(String cluesNombre) {
        this.cluesNombre = cluesNombre;
    }

    public String getCluesDomicilio() {
        return cluesDomicilio;
    }

    public void setCluesDomicilio(String cluesDomicilio) {
        this.cluesDomicilio = cluesDomicilio;
    }

    public String getCluesJurisdiccion() {
        return cluesJurisdiccion;
    }

    public void setCluesJurisdiccion(String cluesJurisdiccion) {
        this.cluesJurisdiccion = cluesJurisdiccion;
    }

    public String getCluesMunicipio() {
        return cluesMunicipio;
    }

    public void setCluesMunicipio(String cluesMunicipio) {
        this.cluesMunicipio = cluesMunicipio;
    }

    public String getCluesLocalidad() {
        return cluesLocalidad;
    }

    public void setCluesLocalidad(String cluesLocalidad) {
        this.cluesLocalidad = cluesLocalidad;
    }

    public String getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(String fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public int getCerrado() {
        return cerrado;
    }

    public void setCerrado(int cerrado) {
        this.cerrado = cerrado;
    }

    public int getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(int sincronizado) {
        this.sincronizado = sincronizado;
    }

    public int getCompartido() {
        return compartido;
    }

    public void setCompartido(int compartido) {
        this.compartido = compartido;
    }

    public int getCompartidoFull() {
        return compartidoFull;
    }

    public void setCompartidoFull(int compartidoFull) {
        this.compartidoFull = compartidoFull;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public int getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(int indicadores) {
        this.indicadores = indicadores;
    }

    public int getExpedientes() {
        return expedientes;
    }

    public void setExpedientes(int expedientes) {
        this.expedientes = expedientes;
    }

    public int getCriterios() {
        return criterios;
    }

    public void setCriterios(int criterios) {
        this.criterios = criterios;
    }

    public int getAvanceCriterios() {
        return avanceCriterios;
    }

    public void setAvanceCriterios(int avanceCriterios) {
        this.avanceCriterios = avanceCriterios;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }






}/// fin class Evaluacion Detalles




class EvaluacionRecursoIndicador
{
    public int id;
    public int idEvaluacionRecurso;
    public int idIndicador;
    public int totalCriterio;
    public int avanceCriterio;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;

    public EvaluacionRecursoIndicador(int id, int idEvaluacionRecurso, int idIndicador, int totalCriterio, int avanceCriterio, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.idEvaluacionRecurso = idEvaluacionRecurso;
        this.idIndicador = idIndicador;
        this.totalCriterio = totalCriterio;
        this.avanceCriterio = avanceCriterio;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvaluacionRecurso() {
        return idEvaluacionRecurso;
    }

    public void setIdEvaluacionRecurso(int idEvaluacionRecurso) {
        this.idEvaluacionRecurso = idEvaluacionRecurso;
    }

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    public int getTotalCriterio() {
        return totalCriterio;
    }

    public void setTotalCriterio(int totalCriterio) {
        this.totalCriterio = totalCriterio;
    }

    public int getAvanceCriterio() {
        return avanceCriterio;
    }

    public void setAvanceCriterio(int avanceCriterio) {
        this.avanceCriterio = avanceCriterio;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}



class EvaluacionRecursoCriterio
{

    public int id;
    public int id_evaluacion_recurso;
    public int id_criterio;
    public int id_indicador;
    public int aprobado;
    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;

    public EvaluacionRecursoCriterio(int id, int id_evaluacion_recurso, int id_criterio,
                                     int id_indicador, int aprobado,
                                     String creadoAl, String modificadoAl, String borradoAl)
    {
        this.id = id;
        this.id_evaluacion_recurso = id_evaluacion_recurso;
        this.id_criterio = id_criterio;
        this.id_indicador = id_indicador;
        this.aprobado = aprobado;

        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }


    public int getId()
    {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_evaluacion_recurso() {
        return id_evaluacion_recurso;
    }

    public void setId_evaluacion_recurso(int id_evaluacion_recurso) {
        this.id_evaluacion_recurso = id_evaluacion_recurso;
    }

    public int getId_criterio() {
        return id_criterio;
    }

    public void setId_criterio(int id_criterio) {
        this.id_criterio = id_criterio;
    }

    public int getId_indicador() {
        return id_indicador;
    }

    public void setId_indicador(int id_indicador) {
        this.id_indicador = id_indicador;
    }

    public int getAprobado() {
        return aprobado;
    }

    public void setAprobado(int aprobado) {
        this.aprobado = aprobado;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}


class DataEvaluadorRecurso
{
    public int id_evaluacion_recurso;
    public String tipo_item;
    public int id_indicador;
    public int id_lugar_verificacion;
    public String nombre_lugar_verificacion;
    public int id_criterio;
    public String nombre_criterio;
    public int habilitar_na;
    public int respuesta;
    public int id_erc;
    public int modificado;

    public DataEvaluadorRecurso(int id_evaluacion_recurso,String tipo_item,
                                int id_indicador, int id_lugar_verificacion,
                                String nombre_lugar_verificacion, int id_criterio,
                                String nombre_criterio, int habilitar_na,
                                int respuesta,int id_erc, int modificado) {

        this.id_evaluacion_recurso=id_evaluacion_recurso;
        this.tipo_item = tipo_item;
        this.id_indicador=id_indicador;
        this.id_lugar_verificacion = id_lugar_verificacion;
        this.nombre_lugar_verificacion = nombre_lugar_verificacion;
        this.id_criterio = id_criterio;
        this.nombre_criterio = nombre_criterio;
        this.habilitar_na = habilitar_na;
        this.respuesta=respuesta;
        this.id_erc=id_erc;
        this.modificado=modificado;

    }

    public int getId_evaluacion_recurso() {
        return id_evaluacion_recurso;
    }

    public void setId_evaluacion_recurso(int id_evaluacion_recurso) {
        this.id_evaluacion_recurso = id_evaluacion_recurso;
    }

    public String getTipoItem()
    {
        return tipo_item;
    }

    public void setTipoItem(String tipo_item)
    {
        this.tipo_item = tipo_item;
    }

    public int getId_indicador() {
        return id_indicador;
    }

    public void setId_indicador(int id_indicador) {
        this.id_indicador = id_indicador;
    }

    public int getId_lugar_verificacion()
    {
        return id_lugar_verificacion;
    }

    public void setId_lugar_verificacion(int id_lugar_verificacion) {
        this.id_lugar_verificacion = id_lugar_verificacion;
    }

    public String getNombre_lugar_verificacion() {
        return nombre_lugar_verificacion;
    }

    public void setNombre_lugar_verificacion(String nombre_lugar_verificacion) {
        this.nombre_lugar_verificacion = nombre_lugar_verificacion;
    }

    public int getId_criterio() {
        return id_criterio;
    }

    public void setId_criterio(int id_criterio) {
        this.id_criterio = id_criterio;
    }

    public String getNombre_criterio() {
        return nombre_criterio;
    }

    public void setNombre_criterio(String nombre_criterio) {
        this.nombre_criterio = nombre_criterio;
    }

    public int getHabilitar_na() {
        return habilitar_na;
    }

    public void setHabilitar_na(int habilitar_na) {
        this.habilitar_na = habilitar_na;
    }


    public int getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }

    public int getId_erc() {
        return id_erc;
    }

    public void setId_erc(int id_erc) {
        this.id_erc = id_erc;
    }

    public int getModificado() {
        return modificado;
    }

    public void setModificado(int modificado) {
        this.modificado = modificado;
    }
}


class DataEvaluadorCalidad
{
    public int idEvaluacionCalidad;
    public String tipoItem;
    public int idIndicador;
    public int idLugarVerificacion;
    public String nombreLugarVerificacion;
    public int idCriterio;
    public String nombreCriterio;
    public int habilitarNa;
    public int tieneValidacion;
    public int respuesta;
    public int idEvaluacionCalidadCriterio;
    public int idEvaluacionCalidadRegistro;

    public int modificado;


    public DataEvaluadorCalidad(int idEvaluacionCalidad, String tipoItem, int idIndicador, int idLugarVerificacion, String nombreLugarVerificacion, int idCriterio, String nombreCriterio, int habilitarNa, int tieneValidacion,int respuesta, int idEvaluacionCalidadCriterio, int idEvaluacionCalidadRegistro, int modificado) {
        this.idEvaluacionCalidad = idEvaluacionCalidad;
        this.tipoItem = tipoItem;
        this.idIndicador = idIndicador;
        this.idLugarVerificacion = idLugarVerificacion;
        this.nombreLugarVerificacion = nombreLugarVerificacion;
        this.idCriterio = idCriterio;
        this.nombreCriterio = nombreCriterio;
        this.habilitarNa = habilitarNa;

        this.tieneValidacion = tieneValidacion;

        this.respuesta = respuesta;
        this.idEvaluacionCalidadCriterio = idEvaluacionCalidadCriterio;
        this.idEvaluacionCalidadRegistro = idEvaluacionCalidadRegistro;
        this.modificado = modificado;
    }


    public int getIdEvaluacionCalidad() {
        return idEvaluacionCalidad;
    }

    public void setIdEvaluacionCalidad(int idEvaluacionCalidad) {
        this.idEvaluacionCalidad = idEvaluacionCalidad;
    }

    public String getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(String tipoItem) {
        this.tipoItem = tipoItem;
    }

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    public int getIdLugarVerificacion() {
        return idLugarVerificacion;
    }

    public void setIdLugarVerificacion(int idLugarVerificacion) {
        this.idLugarVerificacion = idLugarVerificacion;
    }

    public String getNombreLugarVerificacion() {
        return nombreLugarVerificacion;
    }

    public void setNombreLugarVerificacion(String nombreLugarVerificacion) {
        this.nombreLugarVerificacion = nombreLugarVerificacion;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public String getNombreCriterio() {
        return nombreCriterio;
    }

    public void setNombreCriterio(String nombreCriterio) {
        this.nombreCriterio = nombreCriterio;
    }

    public int getHabilitarNa() {
        return habilitarNa;
    }

    public void setHabilitarNa(int habilitarNa) {
        this.habilitarNa = habilitarNa;
    }

    public int getTieneValidacion() {
        return tieneValidacion;
    }

    public void setTieneValidacion(int tieneValidacion) {
        this.tieneValidacion = tieneValidacion;
    }

    public int getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }

    public int getIdEvaluacionCalidadCriterio() {
        return idEvaluacionCalidadCriterio;
    }

    public void setIdEvaluacionCalidadCriterio(int idEvaluacionCalidadCriterio) {
        this.idEvaluacionCalidadCriterio = idEvaluacionCalidadCriterio;
    }

    public int getIdEvaluacionCalidadRegistro() {
        return idEvaluacionCalidadRegistro;
    }

    public void setIdEvaluacionCalidadRegistro(int idEvaluacionCalidadRegistro) {
        this.idEvaluacionCalidadRegistro = idEvaluacionCalidadRegistro;
    }

    public int getModificado() {
        return modificado;
    }

    public void setModificado(int modificado) {
        this.modificado = modificado;
    }
}










    class EvaluacionCalidadCriterio
{

    public int id;
    public int idEvaluacionCalidad;
    public int idCriterio;
    public int idIndicador;
    public int aprobado;
    public int idEvaluacionCalidadRegistro;
    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;


    public EvaluacionCalidadCriterio(int id, int idEvaluacionCalidad, int idCriterio, int idIndicador, int aprobado, int idEvaluacionCalidadRegistro, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.idEvaluacionCalidad = idEvaluacionCalidad;
        this.idCriterio = idCriterio;
        this.idIndicador = idIndicador;
        this.aprobado = aprobado;
        this.idEvaluacionCalidadRegistro = idEvaluacionCalidadRegistro;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvaluacionCalidad() {
        return idEvaluacionCalidad;
    }

    public void setIdEvaluacionCalidad(int idEvaluacionCalidad) {
        this.idEvaluacionCalidad = idEvaluacionCalidad;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    public int getAprobado() {
        return aprobado;
    }

    public void setAprobado(int aprobado) {
        this.aprobado = aprobado;
    }

    public int getIdEvaluacionCalidadRegistro() {
        return idEvaluacionCalidadRegistro;
    }

    public void setIdEvaluacionCalidadRegistro(int idEvaluacionCalidadRegistro) {
        this.idEvaluacionCalidadRegistro = idEvaluacionCalidadRegistro;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}





class EvaluacionCalidadRegistro
{
   public int id;
   public int idEvaluacionCalidad;
   public int idIndicador;
   public int columna;
   public String expediente;
   public int cumple;
   public Double promedio;
   public int totalCriterio;

    public int avanceCriterio;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;

    public EvaluacionCalidadRegistro(int id, int idEvaluacionCalidad, int idIndicador, int columna, String expediente, int cumple, Double promedio, int totalCriterio, int avanceCriterio, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.idEvaluacionCalidad = idEvaluacionCalidad;
        this.idIndicador = idIndicador;
        this.columna = columna;
        this.expediente = expediente;
        this.cumple = cumple;
        this.promedio = promedio;
        this.totalCriterio = totalCriterio;
        this.avanceCriterio = avanceCriterio;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvaluacionCalidad() {
        return idEvaluacionCalidad;
    }

    public void setIdEvaluacionCalidad(int idEvaluacionCalidad) {
        this.idEvaluacionCalidad = idEvaluacionCalidad;
    }

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public int getCumple() {
        return cumple;
    }

    public void setCumple(int cumple) {
        this.cumple = cumple;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public int getTotalCriterio() {
        return totalCriterio;
    }

    public void setTotalCriterio(int totalCriterio) {
        this.totalCriterio = totalCriterio;
    }

    public int getAvanceCriterio() {
        return avanceCriterio;
    }

    public void setAvanceCriterio(int avanceCriterio) {
        this.avanceCriterio = avanceCriterio;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}


/////*********************************************************************************************************************************



class Hallazgo
{
    public int id;
    public int idEvaluacion;
    public String categoriaEvaluacion;
    public int idIndicador;
    public String expediente;
    public int idUsuario;
    public int idAccion;
    public int idPlazoAccion;
    public int resuelto;
    public String descripcion;
    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;

    public Hallazgo(int id, int idEvaluacion, String categoriaEvaluacion, int idIndicador,String expediente, int idUsuario, int idAccion, int idPlazoAccion, int resuelto, String descripcion, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.idEvaluacion = idEvaluacion;
        this.categoriaEvaluacion = categoriaEvaluacion;
        this.idIndicador = idIndicador;
        this.expediente = expediente;
        this.idUsuario = idUsuario;
        this.idAccion = idAccion;
        this.idPlazoAccion = idPlazoAccion;
        this.resuelto = resuelto;
        this.descripcion = descripcion;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public String getCategoriaEvaluacion() {
        return categoriaEvaluacion;
    }

    public void setCategoriaEvaluacion(String categoriaEvaluacion) {
        this.categoriaEvaluacion = categoriaEvaluacion;
    }

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdAccion() {
        return idAccion;
    }

    public void setIdAccion(int idAccion) {
        this.idAccion = idAccion;
    }

    public int getIdPlazoAccion() {
        return idPlazoAccion;
    }

    public void setIdPlazoAccion(int idPlazoAccion) {
        this.idPlazoAccion = idPlazoAccion;
    }

    public int getResuelto() {
        return resuelto;
    }

    public void setResuelto(int resuelto) {
        this.resuelto = resuelto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}



class Seguimiento
{

    public int id;
    public int idUsuario;
    public int idHallazgo;
    public String descripcion;
    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;

    public Seguimiento(int id, int idUsuario, int idHallazgo, String descripcion, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idHallazgo = idHallazgo;
        this.descripcion = descripcion;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdHallazgo() {
        return idHallazgo;
    }

    public void setIdHallazgo(int idHallazgo) {
        this.idHallazgo = idHallazgo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}


class Accion
{
    public int id;
    public String nombre;
    public String tipo;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;

    public Accion(int id, String nombre, String tipo, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}





class ConeClues
{

    public String clues;
    public int idCone;

    public int getIdCone() {
        return idCone;
    }

    public void setIdCone(int idCone) {
        this.idCone = idCone;
    }

    public String getClues() {

        return clues;
    }

    public void setClues(String clues) {
        this.clues = clues;
    }

    public ConeClues(String clues, int idCone)
    {
        super();
        this.clues=clues;
        this.idCone=idCone;

    }






}//  FIN CONECLUES




class IndicadorValidacion
{
    public int id;
    public int idIndicador;
    public String pregunta1;
    public String operadorAritmetico;
    public String pregunta2;
    public String unidadMedida;
    public String operadorLogico;
    public String valorComparativo;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;

    public IndicadorValidacion(int id, int idIndicador, String pregunta1, String operadorAritmetico, String pregunta2, String unidadMedida, String operadorLogico, String valorComparativo, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.idIndicador = idIndicador;
        this.pregunta1 = pregunta1;
        this.operadorAritmetico = operadorAritmetico;
        this.pregunta2 = pregunta2;
        this.unidadMedida = unidadMedida;
        this.operadorLogico = operadorLogico;
        this.valorComparativo = valorComparativo;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    public String getPregunta1() {
        return pregunta1;
    }

    public void setPregunta1(String pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public String getOperadorAritmetico() {
        return operadorAritmetico;
    }

    public void setOperadorAritmetico(String operadorAritmetico) {
        this.operadorAritmetico = operadorAritmetico;
    }

    public String getPregunta2() {
        return pregunta2;
    }

    public void setPregunta2(String pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getOperadorLogico() {
        return operadorLogico;
    }

    public void setOperadorLogico(String operadorLogico) {
        this.operadorLogico = operadorLogico;
    }

    public String getValorComparativo() {
        return valorComparativo;
    }

    public void setValorComparativo(String valorComparativo) {
        this.valorComparativo = valorComparativo;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}
////  FIN CLASS VALIDACION







class IndicadorPregunta
{
    public String id;
    public int idIndicador;
    public String nombre;
    public String tipo;
    public int constante;
    public String valorConstante;
    public int fechaSistema;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;


    public IndicadorPregunta(String id, int idIndicador, String nombre, String tipo, int constante, String valorConstante, int fechaSistema, String creadoAl, String modificadoAl, String borradoAl)
    {
        this.id = id;
        this.idIndicador = idIndicador;
        this.nombre = nombre;
        this.tipo = tipo;
        this.constante = constante;
        this.valorConstante = valorConstante;
        this.fechaSistema = fechaSistema;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getConstante() {
        return constante;
    }

    public void setConstante(int constante) {
        this.constante = constante;
    }

    public String getValorConstante() {
        return valorConstante;
    }

    public void setValorConstante(String valorConstante) {
        this.valorConstante = valorConstante;
    }

    public int getFechaSistema() {
        return fechaSistema;
    }

    public void setFechaSistema(int fechaSistema) {
        this.fechaSistema = fechaSistema;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }



}/// FIN CLASS INDICADOR-PREGUNTA







class CriterioValidacion
{
    public int id;
    public int idCriterio;
    public String pregunta1;
    public String operadorAritmetico;
    public String pregunta2;
    public String unidadMedida;
    public String operadorLogico;
    public String valorComparativo;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;


    public CriterioValidacion(int id, int idCriterio, String pregunta1, String operadorAritmetico, String pregunta2, String unidadMedida, String operadorLogico, String valorComparativo, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.idCriterio = idCriterio;
        this.pregunta1 = pregunta1;
        this.operadorAritmetico = operadorAritmetico;
        this.pregunta2 = pregunta2;
        this.unidadMedida = unidadMedida;
        this.operadorLogico = operadorLogico;
        this.valorComparativo = valorComparativo;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public String getPregunta1() {
        return pregunta1;
    }

    public void setPregunta1(String pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public String getOperadorAritmetico() {
        return operadorAritmetico;
    }

    public void setOperadorAritmetico(String operadorAritmetico) {
        this.operadorAritmetico = operadorAritmetico;
    }

    public String getPregunta2() {
        return pregunta2;
    }

    public void setPregunta2(String pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getOperadorLogico() {
        return operadorLogico;
    }

    public void setOperadorLogico(String operadorLogico) {
        this.operadorLogico = operadorLogico;
    }

    public String getValorComparativo() {
        return valorComparativo;
    }

    public void setValorComparativo(String valorComparativo) {
        this.valorComparativo = valorComparativo;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}



class CriterioPregunta {
    public String id;
    public int idCriterio;
    public String nombre;
    public String tipo;
    public int constante;
    public String valorConstante;
    public int fechaSistema;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;

    public CriterioPregunta(String id, int idCriterio, String nombre, String tipo, int constante, String valorConstante, int fechaSistema, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.idCriterio = idCriterio;
        this.nombre = nombre;
        this.tipo = tipo;
        this.constante = constante;
        this.valorConstante = valorConstante;
        this.fechaSistema = fechaSistema;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getConstante() {
        return constante;
    }

    public void setConstante(int constante) {
        this.constante = constante;
    }

    public String getValorConstante() {
        return valorConstante;
    }

    public void setValorConstante(String valorConstante) {
        this.valorConstante = valorConstante;
    }

    public int getFechaSistema() {
        return fechaSistema;
    }

    public void setFechaSistema(int fechaSistema) {
        this.fechaSistema = fechaSistema;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}





class CriterioValidacionRespuesta
{

    public int id;
    public int idEvaluacion;
    public String expediente;
    public int idCriterio;
    public int idCriterioValidacion;
    public String tipo;
    public String respuesta1;
    public String respuesta2;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;

    public CriterioValidacionRespuesta(int id, int idEvaluacion, String expediente, int idCriterio, int idCriterioValidacion, String tipo, String respuesta1, String respuesta2, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.idEvaluacion = idEvaluacion;
        this.expediente = expediente;
        this.idCriterio = idCriterio;
        this.idCriterioValidacion = idCriterioValidacion;
        this.tipo = tipo;
        this.respuesta1 = respuesta1;
        this.respuesta2 = respuesta2;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public int getIdCriterioValidacion() {
        return idCriterioValidacion;
    }

    public void setIdCriterioValidacion(int idCriterioValidacion) {
        this.idCriterioValidacion = idCriterioValidacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRespuesta1() {
        return respuesta1;
    }

    public void setRespuesta1(String respuesta1) {
        this.respuesta1 = respuesta1;
    }

    public String getRespuesta2() {
        return respuesta2;
    }

    public void setRespuesta2(String respuesta2) {
        this.respuesta2 = respuesta2;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}










class IndicadorPreguntaRespuesta
{
    public int idValidacion;
    public int idIndicador;
    public String idPregunta;

}






class Config
{

    public int id;
    public String seccion;
    public int idUsuario;
    public String fechaAccion;
    public String status;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;

    public Config(int id, String seccion, int idUsuario, String fechaAccion, String status, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.seccion = seccion;
        this.idUsuario = idUsuario;
        this.fechaAccion = fechaAccion;
        this.status = status;

        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(String fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}





class Device
{
    public BluetoothDevice bluetoothDevice;
    public String nombre;
    public String mac;

    public Device(BluetoothDevice bluetoothDevice,String nombre, String mac)
    {
        super();
        this.bluetoothDevice = bluetoothDevice;
        this.nombre = nombre;
        this.mac = mac;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getMac()
    {
        return mac;
    }

    public void setMac(String mac)
    {
        this.mac = mac;
    }
}


class Version
{
    public int id;
    public String path;
    public double versionApp;
    public int versionDB;
    public String descripcion;

    public int instalado;

    public String creadoAl;
    public String modificadoAl;
    public String borradoAl;


    public Version(int id, String path, double versionApp, int versionDB, String descripcion, int instalado, String creadoAl, String modificadoAl, String borradoAl) {
        this.id = id;
        this.path = path;
        this.versionApp = versionApp;
        this.versionDB = versionDB;
        this.descripcion = descripcion;
        this.instalado = instalado;
        this.creadoAl = creadoAl;
        this.modificadoAl = modificadoAl;
        this.borradoAl = borradoAl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(double versionApp) {
        this.versionApp = versionApp;
    }

    public int getVersionDB() {
        return versionDB;
    }

    public void setVersionDB(int versionDB) {
        this.versionDB = versionDB;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getInstalado() {
        return instalado;
    }

    public void setInstalado(int instalado) {
        this.instalado = instalado;
    }

    public String getCreadoAl() {
        return creadoAl;
    }

    public void setCreadoAl(String creadoAl) {
        this.creadoAl = creadoAl;
    }

    public String getModificadoAl() {
        return modificadoAl;
    }

    public void setModificadoAl(String modificadoAl) {
        this.modificadoAl = modificadoAl;
    }

    public String getBorradoAl() {
        return borradoAl;
    }

    public void setBorradoAl(String borradoAl) {
        this.borradoAl = borradoAl;
    }
}







////***********************************************************************************************
/////**********************************************************************************************

class AdaptadorListaIndicadores extends ArrayAdapter<Indicador>
{

    private final Context context;
    private final List<Indicador> itemsArrayList;

    public AdaptadorListaIndicadores(Context context, List<Indicador> itemsArrayList)
    {
        super(context, R.layout.layout_row_indicador, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_row_indicador, parent, false);

        TextView codigo = (TextView) rowView.findViewById(R.id.text_view_codigo);
        TextView nombre = (TextView) rowView.findViewById(R.id.text_view_nombre);

        codigo.setText(itemsArrayList.get(position).getCodigo());
        nombre.setText(itemsArrayList.get(position).getNombre());

        return rowView;
    }
}

/////************************************************************************************************
///**************************************************************************************************





























