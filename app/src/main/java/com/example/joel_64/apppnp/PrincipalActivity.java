package com.example.joel_64.apppnp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MuralFragment.OnFragmentInteractionListener,
        AlertasFragment.OnFragmentInteractionListener,
        CategoriasAlertasFragment.OnFragmentInteractionListener,
        DenunciasFragment.OnFragmentInteractionListener,
        ComisariasFragment.OnFragmentInteractionListener,
        ReclamosFragment.OnFragmentInteractionListener,
        DirectorioFragment.OnFragmentInteractionListener,
        ZonasFragment.OnFragmentInteractionListener,
        ConfiguracionFragment.OnFragmentInteractionListener{
    //Atributos
    private Fragment fragmento = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Vista por defecto
        fragmento = new MuralFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, fragmento).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int cant;
        AdminSQLiteOpenHelper manager = new AdminSQLiteOpenHelper(this,"administracion",null,1);
        SQLiteDatabase db = manager.getWritableDatabase();
        Cursor rows_usuario = db.rawQuery("SELECT * FROM usuario WHERE id=1",null);
        switch(item.getItemId())
        {
            //Limpiar Memoria
            case R.id.Menu_Opc1:
                if (rows_usuario.moveToFirst()) {
                    cant = db.delete("usuario", "id=1", null);
                }
                db.close();
                volverLogin();
                return true;
            //Configurar Usuario
            case R.id.Menu_Opc2:
                return true;
            //Cerrar Sesion
            case R.id.Menu_Opc3:
                if (rows_usuario.moveToFirst())
                {
                    ContentValues nuevoregistro = new ContentValues();
                    nuevoregistro.put("idbd","");
                    nuevoregistro.put("nombres","");
                    nuevoregistro.put("apellidos","");
                    nuevoregistro.put("dni","");
                    nuevoregistro.put("clave","");
                    nuevoregistro.put("direccion","");
                    nuevoregistro.put("estado","0");
                    cant = db.update("usuario",nuevoregistro,"id=1",null);
                }
                db.close();
                volverLogin();
                return true;
            default:
                db.close();
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean fragmentTransaccion = false;
        switch (item.getItemId()) {
            case R.id.navigation_mural:
                fragmentTransaccion = true;
                fragmento = new MuralFragment();
                break;
            case R.id.navigation_alertas:
                fragmentTransaccion = true;
                fragmento = new CategoriasAlertasFragment();
                break;
            case R.id.navigation_denuncias:
                fragmentTransaccion = true;
                fragmento = new DenunciasFragment();
                break;
            case R.id.navigation_comisarias:
                fragmentTransaccion = true;
                fragmento = new ComisariasFragment();
                break;
            case R.id.navigation_reclamos:
                fragmentTransaccion = true;
                fragmento = new ReclamosFragment();
                break;
            case R.id.navigation_zonas_incidencia:
                fragmentTransaccion = true;
                fragmento = new ZonasFragment();
                break;
            case R.id.navigation_directorio:
                fragmentTransaccion = true;
                fragmento = new DirectorioFragment();
                break;
            case R.id.navigation_configuracion:
                fragmentTransaccion = true;
                fragmento = new ConfiguracionFragment();
                break;
            default:
                fragmentTransaccion = false;
                break;
        }
        if (fragmentTransaccion) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, fragmento).commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void volverLogin(){
        Intent login = new Intent(PrincipalActivity.this,LoginActivity.class);
        startActivity(login);
    }
}
