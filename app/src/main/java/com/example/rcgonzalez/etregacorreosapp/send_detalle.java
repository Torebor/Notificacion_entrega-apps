package com.example.rcgonzalez.etregacorreosapp;

import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by Pmeneses on 17/05/2016.
 */
public class send_detalle extends AppCompatActivity implements View.OnClickListener{

    String Cliente;
    String Codigo;
    String destinatario;

    EditText txtreceptor, txtcedula, txtincidencia;
    RadioButton ckentregada, cknoentregada, cknotificada;
    FloatingActionButton btnenviar, btnscan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_send_detalle);
        txtreceptor = (EditText) findViewById(R.id.txtreceptor);
        txtcedula = (EditText) findViewById(R.id.txtcedula);
        txtincidencia = (EditText) findViewById(R.id.txtincidencia);
        btnenviar = (FloatingActionButton) findViewById(R.id.btn_listo);
        btnscan = (FloatingActionButton) findViewById(R.id.btn_scan);
        ckentregada = (RadioButton) findViewById(R.id.rbeefec);
        cknoentregada = (RadioButton) findViewById(R.id.rbefalli);
        cknotificada = (RadioButton) findViewById(R.id.rbnotifi);
        btnscan.setOnClickListener(this);
        btnenviar.setOnClickListener(this);
        /*cargamos los párametros que nos envian*/
        Codigo=(String)getIntent().getExtras().getSerializable("Codigo");
        Cliente=(String)getIntent().getExtras().getSerializable("Cliente");
        destinatario=(String)getIntent().getExtras().getSerializable("destinatario");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_scan:
                if(ckentregada.isChecked())
                {
                    IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                    scanIntegrator.initiateScan();
                }
                else
                {
                    Toast.makeText(this, "Marque el resultado de la entrega como efectiva", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_listo:
                if(ckentregada.isChecked())
                {
                    Toast.makeText(getApplicationContext(),"En espera de desarrollo :(",Toast.LENGTH_SHORT).show();
                }
                else if(cknoentregada.isChecked())
                {
                    Toast.makeText(getApplicationContext(),"En espera de desarrollo :(",Toast.LENGTH_SHORT).show();
                }
                else if(cknotificada.isChecked())
                {
                    sms_send(destinatario, Codigo+";" + Cliente +";"+"Nt" + ";");
                    this.finish();
                }
                break;
        }
    }

    private void sms_send(String numero, String cuerpo)
    {
        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numero,null,cuerpo,null,null);
            Toast.makeText(getApplicationContext(),"SMS Enviado",Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Se produjo el siguiente error " + e.toString() + " Cuando se intentaba enviar el mensaje",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void ckopciones_click(View v) {
        boolean checked = ((RadioButton) v).isChecked();

        if (v.getId() == R.id.rbeefec) {
            if (checked) {
                txtreceptor.setEnabled(true);
                txtcedula.setEnabled(true);
                txtincidencia.setEnabled(false);
                txtincidencia.setText("");
                txtreceptor.requestFocus();
            }
        }
        if (v.getId() == R.id.rbefalli) {
            if (checked) {
                txtreceptor.setEnabled(false);
                txtcedula.setEnabled(false);
                txtincidencia.setEnabled(true);
                txtreceptor.setText("");
                txtcedula.setText("");
                txtincidencia.requestFocus();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            txtcedula.setText(scanContent);
        } else {
            /*Toast toast = Toast.makeText(getApplicationContext(),
    		        "No se escaneo ningun dato", Toast.LENGTH_SHORT);
    		    toast.show(); */
        }
    }
}
