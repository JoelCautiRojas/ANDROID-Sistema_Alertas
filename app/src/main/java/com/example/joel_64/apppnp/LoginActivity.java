package com.example.joel_64.apppnp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    //Declaracion de Atributos
    public String BASE_URL = "http://aktechnologysolutions.pe.hu/";
    public Button boton_iniciar, boton_registrarme;
    public CheckBox boton_recordarme;
    public EditText edt1, edt2;
    private ProgressDialog barprog;
    SQLiteDatabase db;
    Cursor rows_usuario;
    //Variables de sesion
    String sesion_idbd;
    String sesion_nombres;
    String sesion_apellidos;
    String sesion_dni;
    String sesion_email;
    String sesion_clave;
    String sesion_direccion;
    String sesion_telefono;
    String sesion_estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        boton_iniciar = (Button) findViewById(R.id.iniciar);
        boton_registrarme = (Button) findViewById(R.id.registrarme);
        boton_recordarme = (CheckBox) findViewById(R.id.check_recordarme);
        edt1 = (EditText) findViewById(R.id.email_input);
        edt2 = (EditText) findViewById(R.id.clave_input);
        boton_iniciar.setBackgroundResource(R.drawable.gradiente_botones);
        // Iniciar Datos
        AdminSQLiteOpenHelper manager = new AdminSQLiteOpenHelper(getApplicationContext(),"administracion",null,1);
        db = manager.getWritableDatabase();
        rows_usuario = db.rawQuery("SELECT * FROM usuario WHERE id=1",null);
        if (rows_usuario.moveToFirst())
        {
            edt1.setText(rows_usuario.getString(5));
        }
        else
        {
            edt1.setText("");
        }
        boton_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarLogin();
            }
        });
        boton_registrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intreg = new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(intreg);
            }
        });
    }
    private void validarLogin() {
        String post_email = edt1.getText().toString();
        String post_clave = edt2.getText().toString();
        if("".equals(post_email) || "".equals(post_clave))
        {
            Toast.makeText(getApplicationContext(),"No puede ingresar datos en blanco.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            // Barra de Progreso
            barprog = new ProgressDialog(LoginActivity.this);
            barprog.setCancelable(false);
            barprog.setMessage("Cargando...");
            barprog.setMax(100);
            barprog.setProgress(0);
            barprog.show();
            //Cliente HTTP
            AsyncHttpClient cliente =  new AsyncHttpClient();
            RequestParams parametros = new RequestParams();
            parametros.put("email",post_email);
            parametros.put("clave",post_clave);
            parametros.put("key","WEFDHG345DSF764BV7hgD4SD2");
            cliente.post(getApplicationContext(), BASE_URL + "ValidacionLogin", parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if(statusCode==200)
                    {
                        barprog.dismiss();
                        try {
                            JSONArray jsmatriz = new JSONArray(new String(responseBody));
                            if(jsmatriz.length() > 0)
                            {
                                // Cargando variables de sesion
                                JSONObject usuario = jsmatriz.getJSONObject(0);
                                if("0".equals(usuario.getString("estado")))
                                {
                                    Toast.makeText(getApplicationContext(),"Verifique su e-mail para activar su cuenta.",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    sesion_idbd = usuario.getString("id");
                                    sesion_nombres = usuario.getString("nombres");
                                    sesion_apellidos = usuario.getString("apellidos");
                                    sesion_dni = usuario.getString("dni");
                                    sesion_email = usuario.getString("correo");
                                    sesion_clave = usuario.getString("clave");
                                    sesion_direccion = usuario.getString("direccion");
                                    sesion_telefono = usuario.getString("telefono");
                                    if(boton_recordarme.isChecked())
                                    {
                                        sesion_estado = "on";
                                    }
                                    else
                                    {
                                        sesion_estado = "off";
                                    }
                                    if(rows_usuario.moveToFirst())
                                    {
                                        // Si existe registro, modificar por los nuevos datos ingresados
                                        ContentValues nuevoregistro = new ContentValues();
                                        nuevoregistro.put("idbd",sesion_idbd);
                                        nuevoregistro.put("nombres",sesion_nombres);
                                        nuevoregistro.put("apellidos",sesion_apellidos);
                                        nuevoregistro.put("dni",sesion_dni);
                                        nuevoregistro.put("correo",sesion_email);
                                        nuevoregistro.put("clave",sesion_clave);
                                        nuevoregistro.put("direccion",sesion_direccion);
                                        nuevoregistro.put("estado",sesion_estado);
                                        nuevoregistro.put("telefono",sesion_telefono);
                                        int cant = db.update("usuario",nuevoregistro,"id=1",null);
                                    }
                                    else
                                    {
                                        // Si no existe registro, crear nuevo registro.
                                        ContentValues nuevoregistro = new ContentValues();
                                        nuevoregistro.put("id",1);
                                        nuevoregistro.put("idbd",sesion_idbd);
                                        nuevoregistro.put("nombres",sesion_nombres);
                                        nuevoregistro.put("apellidos",sesion_apellidos);
                                        nuevoregistro.put("dni",sesion_dni);
                                        nuevoregistro.put("correo",sesion_email);
                                        nuevoregistro.put("clave",sesion_clave);
                                        nuevoregistro.put("direccion",sesion_direccion);
                                        nuevoregistro.put("estado",sesion_estado);
                                        nuevoregistro.put("telefono",sesion_telefono);
                                        db.insert("usuario",null,nuevoregistro);
                                    }
                                    db.close();
                                    iniciarPrincipal();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Datos no validos.",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    barprog.dismiss();
                    Toast.makeText(getApplicationContext(),"Error, sin conexion al servidor.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void iniciarPrincipal() {
        Intent iramenu = new Intent(this,PrincipalActivity.class);
        startActivity(iramenu);
    }
}