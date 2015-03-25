package com.example.damien.pi_set;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;


final class ConnectionList {
    PrintWriter out;
    String login;
    ConnectionList tail;
    ConnectionList(String l, PrintWriter h, ConnectionList tl){
        login = l;
        out = h;
        tail = tl;
    }
}
public class ServerActivity extends ActionBarActivity {

    private TextView serverStatus;

    // DEFAULT IP
    public static String SERVERIP = "10.0.2.15";

    // DESIGNATE A PORT
    public static final int SERVERPORT = 8080;



    private Handler handler = new Handler();

    private ServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        while(true) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_server);

            serverStatus = (TextView) findViewById(R.id.server_status);

            SERVERIP = getLocalIpAddress();

            Thread fst = null;
            try {
                fst = new ServerThread();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fst.start();
        }
    }

    public class ServerThread extends Thread {

        private ServerSocket serverSocket;

        public ServerThread() throws IOException {
            serverSocket = new ServerSocket(SERVERPORT);

            serverSocket.setSoTimeout(10000);
        }

        public void run() {
            while (true) {
                try {
                    System.out.println("Waiting for client on port " +
                            serverSocket.getLocalPort() + "...");
                    Socket server = serverSocket.accept();
                    System.out.println("Just connected to "
                            + server.getRemoteSocketAddress());

                    DataInputStream in =
                            new DataInputStream(server.getInputStream());

                    DataOutputStream out =
                            new DataOutputStream(server.getOutputStream());

                    out.writeUTF("Connection successful");

                    while(true){
                        // C'est ici qu'on peut agir comme on le veut.
                        break;
                    }

                    server.close();

                } catch (SocketTimeoutException s) {
                    System.out.println("Socket timed out!");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    // GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            // MAKE SURE YOU CLOSE THE SOCKET UPON EXITING
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}