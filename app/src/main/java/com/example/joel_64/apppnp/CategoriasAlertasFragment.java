package com.example.joel_64.apppnp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoriasAlertasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoriasAlertasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriasAlertasFragment extends Fragment {
    //Atributos
    Fragment fragmento;
    GridView menu;
    MenuAdaptador adaptador;
    String[] items_titulos = new String[]{
            "ACCIDENTES",
            "TRANSITO",
            "CRIMEN ORGANIZADO",
            "DELINCUENCIA Y VANDALISMO",
            "DESASTRES NATURALES",
            "INCENDIOS",
            "COMERCIO ILICITO",
            "EMERGENCIA HUMANITARIA"
    };
    int[] items_imagenes = new int[]{
            R.drawable.policia_corriendo_white,
            R.drawable.policia_corriendo_white,
            R.drawable.policia_corriendo_white,
            R.drawable.policia_corriendo_white,
            R.drawable.policia_corriendo_white,
            R.drawable.policia_corriendo_white,
            R.drawable.policia_corriendo_white,
            R.drawable.policia_corriendo_white
    };
    int[] items_color = new int[]{
            R.color.cardFlat01,
            R.color.cardFlat02,
            R.color.cardFlat03,
            R.color.cardFlat04,
            R.color.cardFlat05,
            R.color.cardFlat06,
            R.color.cardFlat07,
            R.color.cardFlat08,
            R.color.cardFlat09,
            R.color.cardFlat010,
            R.color.cardFlat011,
            R.color.cardFlat012,
            R.color.cardFlat013,
            R.color.cardFlat014,
            R.color.cardFlat015,
            R.color.cardFlat016
    };
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CategoriasAlertasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriasAlertasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriasAlertasFragment newInstance(String param1, String param2) {
        CategoriasAlertasFragment fragment = new CategoriasAlertasFragment();
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
        return inflater.inflate(R.layout.fragment_categorias_alertas, container, false);
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
