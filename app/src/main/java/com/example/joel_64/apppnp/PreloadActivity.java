package com.example.joel_64.apppnp;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PreloadActivity extends AppCompatActivity {
    //Declaracion de Atributos
    private String BASE_URL = "http://aktechnologysolutions.pe.hu/";
    private final int MY_PERMISSIONS = 100;
    private ProgressDialog barprog;
    public ConstraintLayout milayout;
    public TextView mensaje_fail;
    public SQLiteDatabase db;
    public Cursor rows_usuario;
    //VAriables de sesion
    String sesion_idbd;
    String sesion_nombres;
    String sesion_apellidos;
    String sesion_dni;
    String sesion_email;
    String sesion_clave;
    String sesion_direccion;
    String sesion_estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);
        mensaje_fail = (TextView) findViewById(R.id.mensaje_fail);
        milayout = (ConstraintLayout) findViewById(R.id.Layout_preload);
        // Inicializacion SQLite
        AdminSQLiteOpenHelper manager = new AdminSQLiteOpenHelper(getApplicationContext(),"administracion",null,1);
        db = manager.getWritableDatabase();
        rows_usuario = db.rawQuery("SELECT * FROM usuario WHERE id=1",null);
        if(mayRequestStoragePermission())
        {
            iniciar();
        }
        else
        {
            mensaje_fail.setText("La aplicacion no puede iniciar, debe aceptar los permisos requeridos");
        }
    }

    private boolean mayRequestStoragePermission() {
        //Verificacion de los permisos para usar GPS, camara y la memoria interna del dispositivo
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }
        else
        {
            if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            {
                return true;
            }
            else
            {
                if(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(CAMERA) || shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION))
                {
                    Snackbar.make(milayout, "Necesita proporcionar permisos para la aplicacion.", Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{
                                    WRITE_EXTERNAL_STORAGE,
                                    CAMERA,
                                    ACCESS_FINE_LOCATION
                            },MY_PERMISSIONS);
                        }
                    }).show();
                }
                else
                {
                    requestPermissions(new String[]{
                            WRITE_EXTERNAL_STORAGE,
                            CAMERA,
                            ACCESS_FINE_LOCATION
                    },MY_PERMISSIONS);
                }
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case MY_PERMISSIONS:
                if((grantResults.length == 1) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED))
                {
                    Toast.makeText(getApplicationContext(), "Permisos Aceptados", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder abuilder = new AlertDialog.Builder(PreloadActivity.this);
                    abuilder.setTitle("Permisos Negados");
                    abuilder.setMessage("Necesitas aprobar los permisos de la APP");
                    abuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent config = new Intent();
                            config.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",getPackageName(),null);
                            config.setData(uri);
                            startActivity(config);
                        }
                    });
                    abuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
                    abuilder.show();
                }
                break;
            default:
                break;
        }
    }

    private void iniciar() {
        // Barra de Progreso
        barprog = new ProgressDialog(PreloadActivity.this);
        barprog.setCancelable(false);
        barprog.setMessage("Cargando...");
        barprog.setMax(100);
        barprog.setProgress(0);
        barprog.show();
        if (rows_usuario.moveToFirst())
        {
            if("on".equals(rows_usuario.getString(8)))
            {
                AsyncHttpClient cliente = new AsyncHttpClient();
                RequestParams parametros = new RequestParams();
                parametros.add("email",rows_usuario.getString(5));
                parametros.add("clave",rows_usuario.getString(6));
                parametros.add("key","WEFDHG345DSF764BV7hgD4SD2");
                cliente.post(getApplicationContext(), BASE_URL + "ValidacionLogin", parametros, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode==200)
                        {
                            barprog.dismiss();
                            try {
                                JSONArray jsmatriz = new JSONArray(new String(responseBody));
                                if(jsmatriz.length() > 0)
                                {
                                    JSONObject usuario = jsmatriz.getJSONObject(0);
                                    // Cargando variables de sesion
                                    sesion_idbd =  usuario.getString("id");
                                    sesion_nombres = usuario.getString("nombres");
                                    sesion_apellidos = usuario.getString("apellidos");
                                    sesion_dni = usuario.getString("dni");
                                    sesion_email = usuario.getString("correo");
                                    sesion_clave = usuario.getString("clave");
                                    sesion_direccion = usuario.getString("direccion");
                                    sesion_estado = "on";
                                    // Actualizando SQLite
                                    try
                                    {
                                        ContentValues nuevoregistro = new ContentValues();
                                        nuevoregistro.put("idbd",sesion_idbd);
                                        nuevoregistro.put("nombres",sesion_nombres);
                                        nuevoregistro.put("apellidos",sesion_apellidos);
                                        nuevoregistro.put("dni",sesion_dni);
                                        nuevoregistro.put("correo",sesion_email);
                                        nuevoregistro.put("clave",sesion_clave);
                                        nuevoregistro.put("direccion",sesion_direccion);
                                        nuevoregistro.put("estado",sesion_estado);
                                        int cant = db.update("usuario",nuevoregistro,"id=1",null);
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    db.close();
                                    iniciarPrincipal();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Datos no validos.",Toast.LENGTH_SHORT).show();
                                    iniciarLogin();
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
                        iniciarLogin();
                    }
                });
            }
            else
            {
                barprog.dismiss();
                iniciarLogin();
            }
        }
        else
        {
            barprog.dismiss();
            iniciarLogin();
        }
        db.close();
    }

    private void iniciarLogin() {
        Intent intlog = new Intent(PreloadActivity.this,LoginActivity.class);
        startActivity(intlog);
    }

    private void iniciarPrincipal() {
        Intent intmenu = new Intent(PreloadActivity.this,PrincipalActivity.class);
        startActivity(intmenu);
    }
}
