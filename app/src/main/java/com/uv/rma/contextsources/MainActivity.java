package com.uv.rma.contextsources;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.uv.rma.ContextDBC.ContextServices;
import com.uv.rma.ContextDBC.ListenerService;
import com.uv.rma.ContextDBC.WrapperService;
import com.uv.rma.ContextDBC.WrapperUbService;


public class MainActivity extends ActionBarActivity {
    WrapperService x;
    WrapperUbService y;
    ListenerService z;
    Intent sUb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         x = new WrapperService();
         y = new WrapperUbService();
        z = new  ListenerService();

        startService(new Intent(getBaseContext(), WrapperService.class));
        startService(new Intent(getBaseContext(), WrapperUbService.class));
        startService(new Intent(getBaseContext(), ListenerService.class));

     /*   StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        try {
            ContextServices WSSoap = new ContextServices();
            String respuesta = WSSoap.getResultado("mexico");
            Toast.makeText(getApplicationContext(), respuesta,
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(),
                    Toast.LENGTH_LONG).show();
        }
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    protected void onPause() {
        stopService(new Intent(getBaseContext(), WrapperService.class));
        stopService(new Intent(getBaseContext(), WrapperUbService.class));
        stopService(new Intent(getBaseContext(), ListenerService.class));

        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        x.cerrarData();
        y.cerrarData();
        super.onPause();
    }


}
