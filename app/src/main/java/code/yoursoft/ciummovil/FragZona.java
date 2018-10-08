package code.yoursoft.ciummovil;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * A fragment that launches other parts of the demo application.
 */
public class FragZona extends Fragment
{

    MapView mMapView;
    private GoogleMap googleMap;

    DBManager linkDB;
    Funciones link = new Funciones();

    Spinner spinner;
    ArrayAdapter<String> adaptadorSpinner;

    String [] OPCIONES = {"Todas las Clues", "Evaluaciones Recurso", "Evaluaciones Calidad"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        linkDB = new DBManager(getActivity().getApplicationContext());

        // inflat and return the layout
        View v = inflater.inflate(R.layout.layout_zona, container, false);

        mMapView = (MapView) v.findViewById(R.id.mapFragment);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                                    e.printStackTrace();
                                  }

        googleMap = mMapView.getMap();

        try {

            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(16.7461501, -93.2028425)).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            adaptadorSpinner = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, OPCIONES);
            spinner = (Spinner) v.findViewById(R.id.spinnerSelector);
            spinner.setAdapter(adaptadorSpinner);

        }catch (NullPointerException err){ link.printToast("Hay un problema con el GPS del dispositivo", getActivity()); }

    try {
        cargarTodasClues();
    }catch (NullPointerException err){ link.printToast("Hay un problema con el GPS del dispositivo.", getActivity()); }

        // Perform any camera updates here
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public void cargarTodasClues()
    {
        List<Clues> listaClues= linkDB.getListaClues("");

        for(int i=0; i<listaClues.size(); i++)
        {
            Clues clues = listaClues.get(i);
            LatLng point=null;

            try {

                    System.out.println("PRE - PARSING LATITUD"+clues.getLatitud()+",  LONGITUD : "+clues.getLongitud()+".");
                    point = new LatLng(Double.parseDouble(clues.getLatitud()), Double.parseDouble(clues.getLongitud()));

                }catch (NumberFormatException err_number){
                                                            point= null; System.out.println("ERROR PARSEANDO LATITUD Y LONGITUD");
                                                         }

            if (point != null)
            {
                googleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(clues.getClues())
                        .snippet(clues.getNombre() + "\n" + clues.getLocalidad() + "\n" + clues.getMunicipio() + "\n" + clues.getTipoUnidad() + "\n")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            }else{ System.out.println("ERROR. POINT INCOMPLETO PARA : "+clues.getClues());}

        }


    }




}///FIN CLASS FRAGZONA
















/*

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;

        import java.util.List;

public class FragZona extends Fragment
{

    private GoogleMap googleMap;
    private int mapType = GoogleMap.MAP_TYPE_NORMAL;

    SupportMapFragment mapFragment=null;
    SupportMapFragment fragment=null;
    private GoogleMap map;

    DBManager linkDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        linkDB = new DBManager(getActivity());


        FragmentManager fragmentManager = getChildFragmentManager();
        //fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);


        //FragmentManager fragmentManager = getFragmentManager();
        mapFragment =  (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);

        if (mapFragment == null)
        {
            mapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.map, mapFragment).commit();
        }


        googleMap = mapFragment.getMap();

        LatLng point_default = new LatLng(14.898728, -92.277938);


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);





        LatLng cameraLatLng = point_default;
        float cameraZoom = 10;

        if(savedInstanceState != null)
        {
            mapType = savedInstanceState.getInt("map_type", GoogleMap.MAP_TYPE_NORMAL);
            double savedLat = savedInstanceState.getDouble("lat");
            double savedLng = savedInstanceState.getDouble("lng");
            cameraLatLng = new LatLng(savedLat, savedLng);

            cameraZoom = savedInstanceState.getFloat("zoom", 10);
        }

        googleMap.setMapType(mapType);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, cameraZoom));
        googleMap.setMyLocationEnabled(true);


        cargarTodasClues();



        return inflater.inflate(R.layout.layout_zona, container, false);
    }

/////CLAVE API KEY GMSAPS FOR CIUM MOVIL:

    //////  AIzaSyBRmT6hZq6AgyYmRAhksDxSL8qXy9CYr9U



    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);

        if (fragment == null)
        {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (map == null)
        {

        }
    }



    public void cargarTodasClues()
    {
        List<Clues> listaClues= linkDB.getListaClues("");

        for(int i=0; i<listaClues.size(); i++)
        {
            Clues clues = listaClues.get(i);
            LatLng point=null;

            try {

                  System.out.println("PRE - PARSING LATITUD"+clues.getLatitud()+",  LONGITUD : "+clues.getLongitud()+".");

                  point = new LatLng(Double.parseDouble(clues.getLatitud()), Double.parseDouble(clues.getLongitud()));

                }catch (NumberFormatException err_number){ point= null; System.out.println("ERROR PARSEANDO LATITUD Y LONGITUD");
            }

            if (point != null) {
                googleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(clues.getClues())
                        .snippet(clues.getNombre() + "\n" + clues.getLocalidad() + "\n" + clues.getMunicipio() + "\n" + clues.getTipoUnidad() + "\n")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }else{ System.out.println("ERROR. POINT INCOMPLETO PARA : "+clues.getClues());}



        }


    }


}

*/