package com.example.rcgonzalez.etregacorreosapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Pmeneses on 24/05/2016.
 */
public class Modems extends AppCompatActivity {

    EditText txtnombre,txtnumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_modem);
        txtnumero=(EditText)findViewById(R.id.txtphone);
        txtnombre=(EditText)findViewById(R.id.txtnombre);
    }

    public void btn_add_click(View v)
    {
        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        Database usdbh =
                new Database(this, "DBEntregas", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        try
        {
            /*insertamos el telefono y nombre*/
            db.execSQL("INSERT INTO modem(nombre,numero) " +
                    "VALUES ('" + txtnombre.getText().toString() + "', '" + txtnumero.getText().toString() +"')");
            //Cerramos la base de datos
            db.close();
            txtnombre.setText("");
            txtnumero.setText("");
            Toast.makeText(this,"Modem agregado", Toast.LENGTH_LONG).show();
        }
        catch (SQLException ex)
        {
            Toast.makeText(this,"Error cuando se intentaba agregar el modem, intentelo de nuevo", Toast.LENGTH_LONG).show();
        }
    }
}
