package com.example.rcgonzalez.etregacorreosapp;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by Pmeneses on 25/05/2016.
 */
public class send_estado extends AppCompatActivity implements View.OnClickListener{


    String Cliente;
    String Codigo;
    String destinatario,aviso;
    RadioButton ckentregada, ckrechazo, ckfallecido,ckdirinsuf,ckcambiodomi,ckfueracober,ckdesconocido, cknoreclamado;
    FloatingActionButton btnenviar;
    RadioGroup rbopciones;
    EditText txtnumero;
    LinearLayout lycopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_send_status);
        ckentregada = (RadioButton) findViewById(R.id.rbeefec);
        ckrechazo = (RadioButton) findViewById(R.id.rbrezacho);
        ckfallecido = (RadioButton) findViewById(R.id.rbfallecido);
        ckdirinsuf = (RadioButton) findViewById(R.id.rbdirinsu);
        ckcambiodomi = (RadioButton) findViewById(R.id.rbcambiodomi);
        ckfueracober = (RadioButton) findViewById(R.id.rbfueracober);
        ckdesconocido = (RadioButton) findViewById(R.id.rbdesconocido);
        cknoreclamado = (RadioButton) findViewById(R.id.rbnoreclamado);
        btnenviar=(FloatingActionButton)findViewById(R.id.btn_listo);
        txtnumero=(EditText)findViewById(R.id.txtphone);
        lycopy=(LinearLayout)findViewById(R.id.lycopy);
        rbopciones=(RadioGroup)findViewById(R.id.rgopciones);
        btnenviar.setOnClickListener(this);
         /*cargamos los párametros que nos envian*/
        Codigo=(String)getIntent().getExtras().getSerializable("Codigo");
        Cliente=(String)getIntent().getExtras().getSerializable("Cliente");
        destinatario=(String)getIntent().getExtras().getSerializable("destinatario");
        aviso=(String)getIntent().getExtras().getSerializable("Aviso");
        if(aviso.equals("1"))
        {
            lycopy.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_listo:
                    String Text;
                    if(ckentregada.isChecked())
                    {
                        Text="1";
                    }
                    else
                    {
                        Text="0";
                    }
                RadioButton rb;
                if (rbopciones.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(this,"Seleccione en resultado de la entrega antes de enviar",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    rb = (RadioButton) findViewById(rbopciones.getCheckedRadioButtonId());
                }

                    if(aviso.equals("1"))
                    {
                        if(txtnumero.getText().toString().equals(""))
                        {
                            Toast.makeText(this,"Ingrese el numero de notificacion para el cliente "+Cliente,Toast.LENGTH_LONG).show();
                            txtnumero.requestFocus();
                            return;
                        }
                         /*Enviamos el mensaje a uno de los modems de correos de nicaragua*/
                        if(sms_send(destinatario,Codigo + ";" + rb.getText().toString()+";"+Cliente+";"+Text)==true)
                        {
                            /*Enviamos una copia al numero ingresado*/
                            if(sms_send(txtnumero.getText().toString(),Codigo+";"+Text)==true)
                            {
                                Toast.makeText(getApplicationContext(),"SMS Enviado",Toast.LENGTH_LONG).show();
                                this.finish();
                            }
                            else
                            {
                                int intento_envio=0;
                                do {
                                    if(sms_send(txtnumero.getText().toString(),Codigo+";"+Text)==true)
                                    {
                                        intento_envio=1;
                                    }
                                }while(intento_envio==0);
                                Toast.makeText(getApplicationContext(),"SMS Enviado",Toast.LENGTH_LONG).show();
                                this.finish();
                            }

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"No se pudo enviar el mensaje",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        if(sms_send(destinatario,Codigo + ";" + rb.getText().toString()+";"+Cliente+";"+Text)==true)
                        {
                            Toast.makeText(getApplicationContext(),"SMS Enviado",Toast.LENGTH_LONG).show();
                            this.finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"No se pudo enviar el Mensaje, intentelo de nuevo",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                break;
        }
    }

    /*private String get_num_absoluto()
    {
        String Numero="";
        Database usdbh = new Database(this, "DBEntregas", null, 1);
        SQLiteDatabase db = usdbh.getReadableDatabase();
        Cursor resul=db.rawQuery("select * from modem where nombre='CDN'",null);
        try {
            if(resul.moveToFirst())
            {
                do
                {
                    Numero=resul.getString(2);
                }while(resul.moveToNext());
            }
            else {
                Toast.makeText(this,"Ocurrio un error al obtener modem", Toast.LENGTH_LONG).show();
            }
        }
        catch (SQLException ex) {
            Toast.makeText(this,"Error al leer la base de datos intentelo de nuevo", Toast.LENGTH_LONG).show();
        }
        return Numero;
    }*/

    private boolean sms_send(String numero, String cuerpo)
    {
        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numero,null,cuerpo,null,null);
            return true;
            //this.finish();
        }
        catch(Exception e)
        {
           return false;
        }

    }
}
