package com.example.rcgonzalez.etregacorreosapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class Principal extends AppCompatActivity implements View.OnClickListener {

    EditText txtcodigo;
    TextView tvmodem;
    FloatingActionButton btnenviar, btngetcontact, btnscan;
    int menu_select=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_principal);
        txtcodigo= (EditText)findViewById(R.id.txt_codigo);
        btnenviar=(FloatingActionButton)findViewById(R.id.btn_send);
        btnscan=(FloatingActionButton)findViewById(R.id.btn_scan_p);
        btngetcontact=(FloatingActionButton)findViewById(R.id.btn_get_modem);
        tvmodem=(TextView)findViewById(R.id.tvcontact);
        btnscan.setOnClickListener(this);
        btngetcontact.setOnClickListener(this);
        btnenviar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_scan_p:
                    IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                    scanIntegrator.initiateScan();
                break;
            case R.id.btn_send:
                 if(txtcodigo.getText().toString().equals(""))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(),
                        "Ingrese el codigo que sera enviado", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                 else if(tvmodem.getText().toString().equals("No se ha seleccionado ningun modem"))
                 {
                     Toast toast = Toast.makeText(getApplicationContext(),
                             "Seleccione el contacto o modem destinatario", Toast.LENGTH_SHORT);
                     toast.show();
                 }
                 else
                 {
                     registerForContextMenu(btnenviar);
                     openContextMenu(btnenviar);
                 }
                break;
            case R.id.btn_get_modem:
                registerForContextMenu(btngetcontact);
                openContextMenu(btngetcontact);
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()== R.id.btn_send) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Seleccione el cliente");
            Database usdbh = new Database(this, "DBEntregas", null, 1);
            SQLiteDatabase db = usdbh.getReadableDatabase();
            Cursor resul=db.rawQuery("select * from empresa ORDER BY nombre",null);
            try {
                if(resul.moveToFirst())
                {
                    do
                    {
                        menu.add(menu.NONE, resul.getInt(0), menu.NONE, resul.getString(1));
                    }while(resul.moveToNext());
                    menu_select=1;
                }
                else {
                    Toast.makeText(this,"No se ha encontrado ningun cliente para enviar el codigo", Toast.LENGTH_LONG).show();
                }
            }
            catch (SQLException ex) {
                Toast.makeText(this,"Error al leer la base de datos intentelo de nuevo", Toast.LENGTH_LONG).show();
            }
            //String[] menuItems = {"Claro Nicaragua","Policia Nacional"};
            /*for (int i = 0; i<menuItems.length; i++) {
                menu.add(menu.NONE, i, i, menuItems[i]);
            }*/
        }
        else if (v.getId() == R.id.btn_get_modem) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Seleccione el Modem");
            Database usdbh = new Database(this, "DBEntregas", null, 1);
            SQLiteDatabase db = usdbh.getReadableDatabase();
            Cursor resul = db.rawQuery("select * from modem", null);
            try {
                if (resul.moveToFirst()) {
                    do {
                        menu.add(menu.NONE, resul.getInt(0), menu.NONE, resul.getString(1));
                    } while (resul.moveToNext());
                    menu_select=2;
                } else {
                    Toast.makeText(this, "No se encontro ningun Modem, le sugerimos agregar a la lista", Toast.LENGTH_LONG).show();
                }
            } catch (SQLException ex) {
                Toast.makeText(this, "Error al leer la base de datos intentelo de nuevo", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null)
        {
            String scanContent = scanningResult.getContents();
            //String scanFormat = scanningResult.getFormatName()
            txtcodigo.setText(scanContent);
        }
        else {
    		/*Toast toast = Toast.makeText(getApplicationContext(),
    		        "No se escaneo ningun dato", Toast.LENGTH_SHORT);
    		    toast.show(); */
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opciones_generales, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add_empresa) {
            Intent i = new Intent(this, add_empresa.class);
            startActivity(i);
        }
        else if(id==R.id.action_add_modem)
        {
            Intent i = new Intent(this, Modems.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(menu_select==1)
        {
        Database usdbh = new Database(this, "DBEntregas", null, 1);
        SQLiteDatabase db = usdbh.getReadableDatabase();
        Cursor resul=db.rawQuery("select * from empresa where id="+id+"",null);
        try {
            if(resul.moveToFirst())
            {
                do
                {
                    String notify=resul.getString(2);
                    /*lanzamos el activity dependiendo de el tipo de notificacion*/
                    if(notify.equals("Detalle"))
                    {
                        Intent i = new Intent(this, send_detalle.class);
                        i.putExtra("Cliente", resul.getString(1));
                        i.putExtra("Codigo", txtcodigo.getText().toString());
                        i.putExtra("destinatario",tvmodem.getText().toString());
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(this, send_estado.class);
                        i.putExtra("Cliente", resul.getString(1));
                        i.putExtra("Aviso", resul.getString(3));
                        i.putExtra("Codigo", txtcodigo.getText().toString());
                        i.putExtra("destinatario",tvmodem.getText().toString());
                        startActivity(i);
                    }
                }while(resul.moveToNext());
            }
            else {
                Toast.makeText(this,"No se ha encontrado ningun cliente para enviar el codigo", Toast.LENGTH_LONG).show();
            }
        }
        catch (SQLException ex) {
            Toast.makeText(this,"Error al leer la base de datos intentelo de nuevo", Toast.LENGTH_LONG).show();
            }
        }
        else if(menu_select==2)
        {
            Database usdbh = new Database(this, "DBEntregas", null, 1);
            SQLiteDatabase db = usdbh.getReadableDatabase();
            Cursor resul=db.rawQuery("select * from modem where id="+id+"",null);
            try {
                if(resul.moveToFirst())
                {
                    do
                {
                    tvmodem.setText(resul.getString(2));
                }while(resul.moveToNext());
            }
                else {
                    Toast.makeText(this,"Ocurrio un error al obtener modem", Toast.LENGTH_LONG).show();
                }
            }
            catch (SQLException ex) {
                Toast.makeText(this,"Error al leer la base de datos intentelo de nuevo", Toast.LENGTH_LONG).show();
            }
        }
        menu_select=0;
        return true;
    }

}
