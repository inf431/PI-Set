package com.example.damien.pi_set;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.lang.Integer;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientActivity extends ActionBarActivity {

    // DEFAULT IP
    public static String SERVERIP = "10.0.2.15";

    // DESIGNATE A PORT
    public static int SERVERPORT = 8080;

    private Socket clientSocket;


    static Socket establishConnection(String ip, int port) {
        try {
            return new Socket(ip, port);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Impossible de resoudre l'adresse");
        } catch (IOException e) {
            throw new RuntimeException("Impossible de se connecter a l'adresse");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Button connection = (Button) findViewById(R.id.connection);
        final EditText editIP = (EditText) findViewById(R.id.IP);
        final EditText editPort = (EditText) findViewById(R.id.port);
        connection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new ClientThread().start();

            }

        });


    }


    public class ClientThread extends Thread {

        final EditText editIP = (EditText) findViewById(R.id.IP);
        final EditText editPort = (EditText) findViewById(R.id.port);

        public void run() {
            SERVERIP = editIP.getText().toString();
            SERVERPORT = Integer.parseInt(editPort.getText().toString());

            establishConnection(SERVERIP, SERVERPORT);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
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
}
