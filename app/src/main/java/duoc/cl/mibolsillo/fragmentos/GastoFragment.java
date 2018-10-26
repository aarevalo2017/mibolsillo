package duoc.cl.mibolsillo.fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import duoc.cl.mibolsillo.R;
import duoc.cl.mibolsillo.api.BolsilloApiAdapter;
import duoc.cl.mibolsillo.entidades.Gasto;
import duoc.cl.mibolsillo.util.Constantes;
import duoc.cl.mibolsillo.util.GastoFilaAdapter;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GastoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GastoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GastoFragment extends Fragment {
  View view;
  ListView lstGastos;
  ArrayList<String> listaInfo = new ArrayList<>();
  ProgressDialog progressDialog;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public GastoFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment GastoFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static GastoFragment newInstance(String param1, String param2) {
    GastoFragment fragment = new GastoFragment();
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
    view = inflater.inflate(R.layout.fragment_gasto, container, false);
    progressDialog = new ProgressDialog(view.getContext());
    progressDialog.setMessage("Cargando Categorías...");
    progressDialog.show();
    lstGastos = view.findViewById(R.id.lstGastos);
    lstGastos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(view.getContext(), "Position : " + position, Toast.LENGTH_SHORT).show();
      }
    });
    obtenerGastos();
    return view;
  }

  private void obtenerGastos() {
    SharedPreferences prefs = this.getActivity().getSharedPreferences(Constantes.PREF_DATA, MODE_PRIVATE);
    int id = prefs.getInt(Constantes.PREF_ID, 0);
    if (id > 0) {
      Call<List<Gasto>> gastosCall = BolsilloApiAdapter.getApiService().getGastos(id);
      gastosCall.enqueue(new GastosCallback());
    }
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

  private class GastosCallback implements retrofit2.Callback<List<Gasto>> {
    @Override
    public void onResponse(Call<List<Gasto>> call, Response<List<Gasto>> response) {
      progressDialog.hide();
      if (response.isSuccessful()) {
        List<Gasto> gastos = response.body();
        listaInfo.clear();
        if (gastos.isEmpty()) {
          Snackbar.make(view, "No hay Gastos", Snackbar.LENGTH_LONG)
                  .setAction("Action", null).show();
          return;
        }
        for (Gasto gasto : gastos) {
          listaInfo.add(gasto.getDescripcion());
        }
        lstGastos.setAdapter(new GastoFilaAdapter(view.getContext(), gastos));
//        ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_expandable_list_item_1, listaInfo);
//        lstGastos.setAdapter(adapter);
      } else {
        Toast.makeText(view.getContext(), "Error al consultar", Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onFailure(Call<List<Gasto>> call, Throwable t) {
      Toast.makeText(view.getContext(), "Error de comunicación.", Toast.LENGTH_SHORT).show();
    }
  }
}
