package com.example.aas.advancedattendancesystem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import model_class.Attendance_database;
import model_class.Attendance_database_handler;
import model_class.LoginActivityManager;

public class Attendance_Sheet extends AppCompatActivity  {

    private ToggleButton hotspot;
    private Spinner spinner;
    private TextView ipInfo;
    private ListView listview;

    ArrayList<Attendance_database> presentStudents=new ArrayList<>();
    CustomArrayAdapter2 adapter1;
    Context context;
    LoginActivityManager manager;

    String[] items = new String[]{"CSE7A","CSE7B"};
    String Clientdevice_id = "", ClientrollNo = "",Clientclass="",selected;

    Thread socketServerThread;
    private final String TAG = getClass().getSimpleName();

    public WifiManager mWifiManager;
    ServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#003'>Attendance Sheet </font>"));
        setContentView(R.layout.activity_attendance__sheet);


        hotspot=(ToggleButton) findViewById(R.id.hotspottoggle);
        listview =(ListView) findViewById(R.id.listview);
        spinner=(Spinner) findViewById(R.id.spinner);
        ipInfo=(TextView) findViewById(R.id.ipinfo);
        context=getApplicationContext();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  selected= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        hotspot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    turnOnWifiHotspot();
                    startServer(selected);
                    while (getIpAddress().equals("")) {
                    }
                    ipInfo.setText(getIpAddress());
                }else{
                    stopServer();
                    turnOffWifiHotspot();
                }
            }
        });
    }


    private void turnOnWifiHotspot(){
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }

        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "AttendenceServer";
        wifiConfiguration.preSharedKey="password";
        wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        try {
            Method setWifiAPMethod = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            boolean apStatus = (Boolean) setWifiAPMethod.invoke(mWifiManager, wifiConfiguration, true);

            Method isWifiAPEnabled = mWifiManager.getClass().getMethod("isWifiApEnabled");

            Method getWifiAPStateMethod = mWifiManager.getClass().getMethod("getWifiApState");
            int apState = (Integer) getWifiAPStateMethod.invoke(mWifiManager);

            Method getWifiAPConfigMethod = mWifiManager.getClass().getMethod("getWifiApConfiguration");
            wifiConfiguration = (WifiConfiguration) getWifiAPConfigMethod.invoke(mWifiManager);

            Log.v("Client", "SSID\n" + wifiConfiguration.SSID + "\nPassword : " + wifiConfiguration.preSharedKey);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    private void startServer(String selected){
        socketServerThread = new Thread(new SocketServerThread(selected));
        socketServerThread.start();
    }


    private void stopServer(){
        if (serverSocket != null) {
            try {
                socketServerThread.interrupt();
                serverSocket.close();
                Toast.makeText(Attendance_Sheet.this, "ServerSocket CLOSED", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void turnOffWifiHotspot(){

        Method setWifiAPMethod;
        try {
            setWifiAPMethod = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            boolean apStatus = (Boolean) setWifiAPMethod.invoke(mWifiManager, null, false);

            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.SSID = "AttendenceServer";
            wifiConfiguration.preSharedKey=manager.getPassword() ;
            wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

                setWifiAPMethod = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                apStatus = (Boolean) setWifiAPMethod.invoke(mWifiManager, wifiConfiguration, true);

                Method isWifiAPEnabled = mWifiManager.getClass().getMethod("isWifiApEnabled");

                Method getWifiAPStateMethod = mWifiManager.getClass().getMethod("getWifiApState");
                int apState = (Integer) getWifiAPStateMethod.invoke(mWifiManager);

                Method getWifiAPConfigMethod = mWifiManager.getClass().getMethod("getWifiApConfiguration");
                wifiConfiguration = (WifiConfiguration) getWifiAPConfigMethod.invoke(mWifiManager);

                Log.v("Client", "SSID\n" + wifiConfiguration.SSID + "\nPassword : " + wifiConfiguration.preSharedKey);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serverSocket != null) {
            try {
                socketServerThread.interrupt();
                serverSocket.close();
                Toast.makeText(Attendance_Sheet.this, "ServerSocket CLOSED", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        turnOffWifiHotspot();
    }


    private class SocketServerThread extends Thread {
        static final int SOCKET_SERVER_PORT = 8080;
        String selectedClass;

        public SocketServerThread(String selected) {
            selectedClass=selected;
        }

        @Override
        public void run() {
            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            presentStudents.clear();
            Attendance_database_handler adh = new Attendance_database_handler(getApplicationContext());
            ArrayList<Attendance_database> listFromDB = adh.getAttendance_list(selectedClass);
            adh.updateTable(selectedClass);

            try {
                serverSocket = new ServerSocket(SOCKET_SERVER_PORT);

                while (true) {
                    socket = serverSocket.accept();
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    Clientdevice_id = dataInputStream.readUTF();
                    ClientrollNo = dataInputStream.readUTF();
                    Clientclass=dataInputStream.readUTF();

                    if (Clientdevice_id.equals("")|| ClientrollNo.equals("")||Clientclass.equals("")) {
                        dataOutputStream.writeUTF("404");

                    } else if(selectedClass.equals(Clientclass)){
                        boolean found=false;
                        for (int i = 0; i < listFromDB.size(); i++) {

                            Attendance_database addrow = new Attendance_database();

                            if (listFromDB.get(i).getRoll()==Integer.valueOf(ClientrollNo)) {

                                if (listFromDB.get(i).getDevice_id().equals(Clientdevice_id)) {

                                    dataOutputStream.writeUTF("420");

                                } else {

                                    addrow.setRoll(listFromDB.get(i).getRoll());
                                    addrow.setStudent_name(listFromDB.get(i).getStudent_name());
                                    addrow.setDevice_id(listFromDB.get(i).getDevice_id());
                                    presentStudents.add(addrow);
                                    adh.updateRecordP(selectedClass,listFromDB.get(i).getRoll());
                                    dataOutputStream.writeUTF("200");
                                }
                                found=true;
                                break;
                            }
                        }
                        if(found==false){
                            dataOutputStream.writeUTF("not found");
                        }
                        adh.close();
                    }
                    else {
                        dataOutputStream.writeUTF("It's not your turn");
                    }


                    Attendance_Sheet.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter1=new CustomArrayAdapter2(Attendance_Sheet.this,R.layout.attendance_custom_listview,presentStudents);
                            listview.setAdapter(adapter1);
                            adapter1.notifyDataSetChanged();
                            Toast.makeText(Attendance_Sheet.this, String.valueOf(presentStudents.size()), Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }


}

