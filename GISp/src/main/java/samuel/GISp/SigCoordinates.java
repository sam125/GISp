package samuel.GISp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.DrawableWrapper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.ads.MobileAds;
import java.util.Objects;

import samuel.GISpSendEmail.SendEmail;
import samuel.serversig.Help;


public class SigCoordinates extends AppCompatActivity {

    static final int REQUEST_LOCATION = 1;
    TcpClient mTcpClient;
    boolean controlo = true;
    LocationManager locationManager;
    LocationListener locationListenner;
    double longitudee;
    double latitudee;
    FloatingActionButton fab;
    private AdView mAdView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.print("teeeeeeeeeeeeees");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();


        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sig_coordinates);

        MobileAds.initialize(this,"ca-app-pub-6339015178348553~9382754984");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View view) {
                fab.setEnabled(false);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (controlo) {
                    ((AppCompatTextView) findViewById(R.id.connected)).setText("Connecting...");
                    new ConnectTask().execute("");

                    controlo = false;
                }
                local();
            }
        });





        final Button button = findViewById(R.id.Stop);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                fab.setEnabled(true);
                terminar();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //this.props.navigation.navigate('Second_screen');
        getMenuInflater().inflate(R.menu.menu_sig_coordinates, menu);
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

            Intent myIntent = new Intent(getBaseContext(), SendEmail.class);
            startActivity(myIntent);


                ///setContentView(R.layout.send_mail);


            }

        if (id == R.id.help) {

            Intent myIntent = new Intent(getBaseContext(), Help.class);
            startActivity(myIntent);


            ///setContentView(R.layout.send_mail);


        }



        return true;
        }



    @SuppressLint("WrongViewCast")
    void terminar() {
        controlo=true;
        if (mTcpClient != null) {
            mTcpClient.sendMessage(String.valueOf("92222205555558"));
        }
        ((AppCompatTextView) findViewById(R.id.connected)).setText("Disconnected");
    }
    @SuppressLint("WrongViewCast")
    void local() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
               ((AppCompatTextView) findViewById(R.id.connected)).setText("Cant guet the location");
        } else {
            ((AppCompatTextView) findViewById(R.id.connected)).setText("Connected");
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationListenner = new LocationListener() {
                @SuppressLint("WrongViewCast")
                @Override
                public void onLocationChanged(Location location) {
                    System.out.println(location.getLatitude() + " " + location.getLongitude());

                    String IPPC = ((EditText) findViewById(R.id.IPP)).getText().toString();
                    System.out.println(IPPC);

                    if (location != null) {
                        latitudee = location.getLatitude();
                        longitudee = location.getLongitude();
                    }
                    else {

                        ((AppCompatTextView) findViewById(R.id.connected)).setText("Cant guet the location");
                    }

                    //((AppCompatTextView) findViewById(R.id.connected)).setText("Connected");
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage(String.valueOf(latitudee));
                        mTcpClient.sendMessage(String.valueOf(longitudee));
                        locationManager.removeUpdates(this);
                    }


                    String lat=Double.toString(latitudee);
                    String lon=Double.toString(longitudee);
                    ((AppCompatTextView) findViewById(R.id.LocationLat)).setText(String.format("Latitude: " + lat));
                    ((AppCompatTextView) findViewById(R.id.LocationLong)).setText(String.format("Longitude:" + lon));





                    fab.setEnabled(true);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            };
            locationManager.requestLocationUpdates("gps", 10, 0, locationListenner);
        }

    }
/*
    void getLocation() {
        if( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
           Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);


            if (location != null){
                double latitudee = location.getLatitude();
                double longitudee = location.getLongitude();

                ((EditText)findViewById(R.id.LocationLat)).setText("Latitude: " + latitudee);
                ((EditText)findViewById(R.id.LocationLong)).setText("Longitude: " + longitudee);
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(String.valueOf(latitudee));
                    mTcpClient.sendMessage(String.valueOf(longitudee));
                }
                System.out.println(longitudee);
            } else {
                ((EditText)findViewById(R.id.LocationLat)).setText("Unable to find location.");
                ((EditText)findViewById(R.id.LocationLong)).setText("Unable to find location. ");
            }
        }

    }*/


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                // getLocation();
                local();
                break;
        }
    }



    public class ConnectTask extends AsyncTask<String, String, TcpClient> {
        @Override
        protected TcpClient doInBackground(String... message) {
            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            String IPPC = ((EditText) findViewById(R.id.IPP)).getText().toString();
            int PORTA = Integer.parseInt(((EditText) findViewById(R.id.port)).getText().toString());
            System.out.println(IPPC);
            mTcpClient.runn(IPPC, PORTA);
            return null;
        }

        @SuppressLint("WrongViewCast")
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....
            if (Objects.equals(values[0], String.valueOf(longitudee)) || Objects.equals(values[0], String.valueOf(longitudee))) {
                ((AppCompatTextView) findViewById(R.id.connected)).setText("Connected");
                System.out.println("connec");
                fab.setEnabled(true);
            }

            if (Objects.equals(values[0], "92222205555558")) {
                ((AppCompatTextView) findViewById(R.id.connected)).setText("Disconnected");
                mTcpClient.stopClient();
                System.out.println("Closed");
                controlo = true;
                fab.setEnabled(true);
            }
            if (!Objects.equals(values[0], String.valueOf(longitudee)) || !Objects.equals(values[0], String.valueOf(longitudee))) {
                controlo = true;
                mTcpClient.stopClient();
                fab.setEnabled(true);
            }


        }
    }



    public class SendMail {



    }
}
