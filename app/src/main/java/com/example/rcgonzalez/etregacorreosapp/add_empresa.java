package com.example.rcgonzalez.etregacorreosapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by Pmeneses on 16/05/2016.
 */



public class add_empresa extends AppCompatActivity {

    EditText txtnombre;
    RadioButton rbestado,rbdetalle;
    CheckBox ckAviso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cliente);
        txtnombre=(EditText)findViewById(R.id.txtcliente);
        rbdetalle=(RadioButton)findViewById(R.id.rbdetalle);
        rbestado=(RadioButton)findViewById(R.id.rbestado);
        ckAviso=(CheckBox)findViewById(R.id.cknotifi);
        /*rbestado.setChecked(false);
        rbdetalle.setChecked(false);*/
    }

    public void btn_add_click(View v)
    {
        String tipo_noti="";
        if(rbdetalle.isChecked()==true) {
            tipo_noti="Detalle";
        }
        else if (rbestado.isChecked()==true)
        {
            tipo_noti="Estado";
        }
        else
        {
            Toast.makeText(this,"Seleccione el tipo de notificacion para el nuevo cliente", Toast.LENGTH_LONG).show();
            return;
        }
        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        Database usdbh =
                new Database(this, "DBEntregas", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        try
        {
            int aviso=0;
            if(ckAviso.isChecked())
            {
                aviso=1;
            }
            /*insertamos el telefono y nombre*/
            db.execSQL("INSERT INTO empresa (Nombre,Notificacion,Aviso) " +
                    "VALUES ('" + txtnombre.getText().toString() + "', '" + tipo_noti +"',"+aviso+")");
            //Cerramos la base de datos
            db.close();
            txtnombre.setText("");
            rbestado.setChecked(false);
            rbdetalle.setChecked(false);
            Toast.makeText(this,"Cliente agregado", Toast.LENGTH_LONG).show();
        }
        catch (SQLException ex)
        {
            Toast.makeText(this,"Error cuando se intentaba guardar el cliente, intentelo de nuevo", Toast.LENGTH_LONG).show();
        }
    }
}
