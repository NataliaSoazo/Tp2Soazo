package com.example.tp2soazo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import java.util.Date;

public class Contador extends Service {
    private static int bandera;

    public Contador() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("salida", "SERVICIO INICIADO");
        bandera = 1;
        //hilo
        Thread trabajador = new Thread(new Cuenta());
        trabajador.start();


        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bandera = 0;
        Log.d("salida","SERVICIO DESTRUIDO");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @SuppressLint("Range")
    public void leerMensajes(){
        Uri Mensajes = Uri.parse("content://sms/");

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Mensajes, null, null, null, null);

        String fecha,remitente,body;

        //StringBuilder stringBuilder = new StringBuilder();

        if(cursor.getCount() > 0){
            for (int i=0; i<5;i++){
                cursor.moveToPosition(i);
                if(cursor.moveToNext()){

                    fecha=cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    remitente=cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    body=cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    Log.d("Fecha",new Date(Long.parseLong(fecha)).toString());
                    Log.d("Remitente",remitente);
                    Log.d("Mensaje",body);

                }

            }
            Log.d("TE MOSTRÉ LOS ULTIMOS 5 MENSAJES",  "-----------------------------------------------------------------------");

        }
    }

    private class Cuenta implements Runnable{
        public void run(){

            while (bandera==1) {
                try {

                    leerMensajes();
                    Thread.sleep(9000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }


    }

}