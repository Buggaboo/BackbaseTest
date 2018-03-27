package nl.stimsim.mobile.backbase;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Observable;
import java.util.Observer;

import nl.stimsim.mobile.backbase.model.CoordinateTrie;
import nl.stimsim.mobile.backbase.model.ViewModel;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, Observer {

    public static final String MAP_TAG = "MAP_TAG";
    private GoogleMap mMap;
    private ListFragment listFragment;
    private SupportMapFragment mapFragment;
    private CoordinateTrie selectedNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ViewModel.getInstance().addObserver(this);

        FragmentManager supportFragmentManager = getSupportFragmentManager();

        FragmentTransaction tx = supportFragmentManager.beginTransaction();
        listFragment = (ListFragment) supportFragmentManager.findFragmentByTag(ListFragment.TAG);
        if (listFragment == null) {
            listFragment = listFragment.newInstance();
            tx.add(R.id.nest, listFragment, ListFragment.TAG)
                    .addToBackStack(ListFragment.TAG)
                    .commit();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        hideSoftKeyBoard();

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng coordinates = new LatLng(selectedNode.coordLat, selectedNode.coordLong);
        mMap.addMarker(new MarkerOptions().position(coordinates).title(selectedNode.originalName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof CoordinateTrie) {
            // 0. set the coordinates first
            selectedNode = (CoordinateTrie) o;

            // 1. prepare the map
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction tx = supportFragmentManager.beginTransaction();
            mapFragment = (SupportMapFragment) supportFragmentManager.findFragmentByTag(MAP_TAG);
            if (mapFragment == null) {
                mapFragment = SupportMapFragment.newInstance();
                tx.add(R.id.nest, mapFragment, ListFragment.TAG)
                        .addToBackStack(ListFragment.TAG)
                        .commit();
            }

            // 2. load the map, onMapReady should kick in
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void onDestroy() {
        ViewModel.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 1) {
            super.onBackPressed();
        }else {
            finish();
        }
    }
}
