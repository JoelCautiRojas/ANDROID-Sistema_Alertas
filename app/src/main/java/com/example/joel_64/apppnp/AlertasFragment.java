package com.example.joel_64.apppnp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlertasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlertasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertasFragment extends Fragment {
    private String BASE_URL = "http://aktechnologysolutions.pe.hu";
    private static String APP_DIRECTORY = "PoliciaPeruanaApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";
    public final int MY_FOTO = 100;
    public final int GALERY_IMAGE = 200;
    public String ruta;
    private File archivo;
    public Bitmap image_bitmap = null;
    public Bitmap final_bitmap = null;
    TextView nombres, apellidos, direccion, telefono, email, categoria;
    EditText asunto, mensaje;
    Button cancelar_alerta, enviar_alerta, seleccion_imagen_alerta;
    ImageView imagen;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AlertasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlertasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlertasFragment newInstance(String param1, String param2) {
        AlertasFragment fragment = new AlertasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_alertas, container, false);
        //Castear Views
        cancelar_alerta = (Button) vista.findViewById(R.id.cancelar_alerta);
        enviar_alerta = (Button) vista.findViewById(R.id.enviar_alerta);
        seleccion_imagen_alerta = (Button) vista.findViewById(R.id.seleccion_imagen_alerta);
        nombres = (TextView) vista.findViewById(R.id.nombres_alerta);
        apellidos = (TextView) vista.findViewById(R.id.apellidos_alerta);
        direccion = (TextView) vista.findViewById(R.id.direccion_alerta);
        telefono = (TextView) vista.findViewById(R.id.telefono_alerta);
        email = (TextView) vista.findViewById(R.id.email_alerta);
        categoria = (TextView) vista.findViewById(R.id.categoria_alerta);
        asunto = (EditText) vista.findViewById(R.id.asunto_alerta);
        mensaje = (EditText) vista.findViewById(R.id.mensaje_alerta);
        imagen = (ImageView) vista.findViewById(R.id.imagen_alerta);
        // Añadiendo gradientes
        cancelar_alerta.setBackgroundResource(R.drawable.gradiente_botones);
        enviar_alerta.setBackgroundResource(R.drawable.gradiente_botones);
        // Recuperando argumentos
        Bundle bundle = getArguments();
        // Extrayendo SQLite
        AdminSQLiteOpenHelper manager = new AdminSQLiteOpenHelper(getActivity().getApplicationContext(),"administracion",null,1);
        SQLiteDatabase db = manager.getWritableDatabase();
        Cursor rows_usuario = db.rawQuery("SELECT * FROM usuario WHERE id=1",null);
        if(rows_usuario.moveToFirst())
        {
            nombres.setText(rows_usuario.getString(2));
            apellidos.setText(rows_usuario.getString(3));
            direccion.setText(rows_usuario.getString(7));
            email.setText(rows_usuario.getString(5));
            telefono.setText(rows_usuario.getString(8));
            if(bundle != null)
            {
                categoria.setText(bundle.getString("categoria"));
            }
            else
            {
                categoria.setText("Ninguna Categoria");
            }
        }
        else
        {
            Toast.makeText(getContext(),"Error, datos de memoria vacios",Toast.LENGTH_SHORT).show();
        }
        cancelar_alerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, new CategoriasAlertasFragment()).commit();
            }
        });
        enviar_alerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarDatosServidor();
            }
        });
        seleccion_imagen_alerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });
        return vista;
    }

    private void enviarDatosServidor() {

    }

    private void seleccionarImagen() {
        final CharSequence[] opciones = {"Tomar Foto","Elejir de Galeria","Cancelar"};
        final AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
        dialogo.setTitle("Elije una opcion");
        dialogo.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i)
                {
                    case 0:
                        ruta = "";
                        archivo = null;
                        File carpeta = new File(Environment.getExternalStorageDirectory(),MEDIA_DIRECTORY);
                        boolean isDirectoryCreated = carpeta.exists();
                        if(!isDirectoryCreated)
                        {
                            isDirectoryCreated = carpeta.mkdirs();
                        }
                        if(isDirectoryCreated)
                        {
                            Long timestamp = System.currentTimeMillis();
                            String imageName = timestamp.toString()+".jpg";
                            ruta = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator+imageName;
                            archivo = new File(ruta);
                            Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            camara.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(archivo));
                            startActivityForResult(camara,MY_FOTO);
                        }
                        break;
                    case 1:
                        ruta = "";
                        archivo = null;
                        Intent galeria = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galeria.setType("image/*");
                        startActivityForResult(galeria.createChooser(galeria,"Selecciona una App de imagen"),GALERY_IMAGE);
                        break;
                    case 2:
                        dialogInterface.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        dialogo.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch(requestCode)
            {
                case MY_FOTO:
                    image_bitmap = null;
                    MediaScannerConnection.scanFile(getActivity().getApplicationContext(), new String[]{ruta}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + s + ":");
                            Log.i("ExternalStorage", "-> Uri = " + uri);
                        }
                    });
                    ContentResolver cr = getActivity().getContentResolver();
                    try{
                        image_bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, Uri.fromFile(archivo));
                        int rotate = 0;
                        ExifInterface ei = new ExifInterface(archivo.getAbsolutePath());
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
                        switch (orientation)
                        {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                final_bitmap = rotarImagen(image_bitmap,90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                final_bitmap = rotarImagen(image_bitmap,180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                final_bitmap = rotarImagen(image_bitmap,270);
                                break;
                        }
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    imagen.setImageBitmap(final_bitmap);
                    break;
                case GALERY_IMAGE:
                    image_bitmap = null;
                    Uri path = data.getData();
                    String segmentoruta[] = path.getLastPathSegment().split(":");
                    String id = segmentoruta[0];
                    final String[] columnasimagenes = {MediaStore.Images.Media.DATA};
                    final String imageOrderBy = null;
                    String state = Environment.getExternalStorageState();
                    if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
                    {
                        path = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                    }
                    else
                    {
                        path = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    }
                    Cursor imgs = getActivity().getContentResolver().query(path,columnasimagenes,MediaStore.Images.Media._ID + "=" + id,null,imageOrderBy);
                    //Cursor imgs = getActivity().managedQuery(path, columnasimagenes, MediaStore.Images.Media._ID + "=" + id,null,imageOrderBy);
                    if(imgs.moveToFirst())
                    {
                        ruta = imgs.getString(imgs.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    archivo = new File(ruta);
                    ContentResolver cr2 = getActivity().getContentResolver();
                    image_bitmap = null;
                    try{
                        image_bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr2,Uri.fromFile(archivo));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    imagen.setImageBitmap(image_bitmap);
                    break;
            }
        }
    }

    private static Bitmap rotarImagen(Bitmap imagen, int i) {
        Matrix matriz = new Matrix();
        matriz.postRotate(i);
        return Bitmap.createBitmap(imagen,0,0,imagen.getWidth(),imagen.getHeight(),matriz,true);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
