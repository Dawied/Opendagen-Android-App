package com.fps.opendagen;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.lightcurb.sdk.Constants;
import com.lightcurb.sdk.LightcurbSDK;
import com.lightcurb.sdk.model.Beacon;
import com.lightcurb.sdk.model.Promotion;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

import nl.alledaags.device.Bluetooth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PromotionsSeenFragment.OnListFragmentInteractionListener {

    public String userId = "";
    public Map<String, String> promotionsSeen;
    private int curViewId = -1;
    private Promotion curPromotion = null;
    private String curPromotionUri = "";
    private FloatingActionButton fab;
    private TextView fabText;
    private LightcurbSDK lightcurbSDK;
    private static final String LOGTAG = "OpendagenApp";

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOGTAG, "Opendagen App Started");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sharedPref.getString(getString(R.string.PREF_USER_ID), "");

        // Haal de promotions die we al gezien hebben op uit de sharedpreferences
        String strPromotionsSeen = sharedPref.getString(getString(R.string.PROMOTIONSSEEN), "");
        promotionsSeen = Util.jsonStringToPromotionMap(strPromotionsSeen);

        // Initialiseer de floating action button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabText = (TextView) findViewById(R.id.fab_text);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayView(R.id.nav_promotions);
            }
        });

        // Initialiseer de drawer en navigationview
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Controleer permissions en start hierna de lightcurb SDK
        checkPermissionAndInitLightcurbSDK();

        // Toon het home scherm
        displayView(R.id.nav_home);
    }

    // Toon de gekozen view in een fragment
    public void displayView(int viewId) {
        curViewId = viewId;
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_promotions:
                fragment = new PromotionFragment();
                Bundle bundle = new Bundle();

                if (!curPromotionUri.isEmpty()) {
                    bundle.putString(getString(R.string.CURPROMOTIONURI), curPromotionUri);
                } else {
                    bundle.putString(getString(R.string.CURPROMOTIONURI), "");
                }

                fragment.setArguments(bundle);
                title  = "Live";
                showFab(false);

                break;
            case R.id.nav_home:
                fragment = new HomeFragment();
                title  = "Open dagen";
                showFab(true);
                break;
            case R.id.nav_promotions_seen:
                fragment = new PromotionsSeenFragment();
                title  = "Bezocht";
                showFab(true);
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                title  = "Opendagen Info";
                showFab(true);
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    /**
     * onBackPressed()
     * Als de drawer open is dan deze sluiten, als de current view niet
     * de home view is, dan naar de home view en anders de app sluiten
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        /*
        else if (curViewId == R.id.nav_promotions_seen) {
            displayView();
        }
        */
        else if (curViewId != R.id.nav_home) {
                displayView(R.id.nav_home);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_promotions) {
            displayView(item.getItemId());
            return true;
        } else if (id == R.id.nav_home) {
            displayView(item.getItemId());
            return true;
        } else if (id == R.id.nav_promotions_seen) {
            displayView(item.getItemId());
            return true;
        } else if (id == R.id.nav_about) {
            displayView(item.getItemId());
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // Controleer permissions, indien ok dan lightcurb SDK initialiseren, anders wachten
    // op permission en in onRequestPermissionsResult de SDK initialiseren
    private void checkPermissionAndInitLightcurbSDK() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasFineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        initLightcurbSDK();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    initLightcurbSDK();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "FINE Location Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initLightcurbSDK() {
        lightcurbSDK = new LightcurbSDK(this.getApplicationContext());


        lightcurbSDK.setOnBeaconSearchListener(new LightcurbSDK.OnBeaconSearchListener() {
            @Override
            public void onBeaconSearchCompleted(String s) {
                Log.i(LOGTAG, "search");
            }

            @Override
            public void onBeaconSearchFailed(Map<String, Object> map) {

            }
        });

        // Blijkbaar is deze listener nodig omdat anders de SDK over de kop gaat. We doen er
        // verder niets mee.
        lightcurbSDK.setOnBeaconsChangedListener(new LightcurbSDK.OnBeaconsChangedListener()
        {
            @Override
            public void onBeaconsChanged(ArrayList<Beacon> beacons) {
            }
        });

        lightcurbSDK.startBeaconMonitoring();
    }

    private BroadcastReceiver lightcurbServiceReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle extras = intent.getExtras();
            Promotion promotion = extras.getParcelable("promotion");

            Log.i(LOGTAG, "PromotionReceived: " + promotion.name);

            if (curPromotionUri.isEmpty() || !curPromotionUri.equals(promotion.uri)) {
                curPromotion = promotion;
                curPromotionUri = promotion.uri;

                // Als deze promotion nog niet gezien is dan opslaan in Map en in sharedpreferences
                if (!promotionsSeen.containsKey(curPromotion.name)) {
                    promotionsSeen.put(curPromotion.name, Util.promotionToJson(curPromotion));
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    String strPromotionsSeen = Util.promotionMapToJsonString(promotionsSeen);
                    editor.putString(getString(R.string.PROMOTIONSSEEN), strPromotionsSeen);
                    editor.commit();
                }

                // Als we de promotions view zelf staan
                // dan automatisch overschakelen naar een nieuwe promotion
                if (curViewId == R.id.nav_promotions) {
                    displayView(R.id.nav_promotions);
                }
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();

        IntentFilter apiManagerFilter = new IntentFilter();
        apiManagerFilter.addAction(Constants.PROMOTION_RECEIVED);
        registerReceiver(lightcurbServiceReceiver, apiManagerFilter);

        if (lightcurbSDK != null) {
            lightcurbSDK.onResume();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        unregisterReceiver(lightcurbServiceReceiver);

        if (lightcurbSDK != null) {
            lightcurbSDK.onPause();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        String strPromotionsSeen = Util.promotionMapToJsonString(promotionsSeen);
        savedInstanceState.putString(getString(R.string.PROMOTIONSSEEN), strPromotionsSeen);

        savedInstanceState.putInt(getString(R.string.CURVIEWID), curViewId);
        savedInstanceState.putString(getString(R.string.CURPROMOTIONURI), curPromotionUri);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String strPromotionsSeen = savedInstanceState.getString(getString(R.string.PROMOTIONSSEEN));
        promotionsSeen = Util.jsonStringToPromotionMap(strPromotionsSeen);

        curPromotionUri = savedInstanceState.getString(getString(R.string.CURPROMOTIONURI));
        curViewId = savedInstanceState.getInt(getString(R.string.CURVIEWID));
        if (curViewId >= 0) {
            displayView(curViewId);
        }
    }

    @Override
    public void onListFragmentInteraction(String uri) {

        StaticPromotionFragment fragment = new StaticPromotionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.CURPROMOTIONURI), uri);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        //ft.addToBackStack(null);
        ft.commit();

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Bezocht");
        }
    }

    private void showFab(boolean show) {
        if (show) {
            fab.setVisibility(View.VISIBLE);
            fabText.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
            fabText.setVisibility(View.GONE);
        }
    }
}
