package duoc.cl.mibolsillo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import duoc.cl.mibolsillo.api.BolsilloApiAdapter;
import duoc.cl.mibolsillo.api.BolsilloApiService;
import duoc.cl.mibolsillo.entidades.Categoria;
import duoc.cl.mibolsillo.entidades.Gasto;
import duoc.cl.mibolsillo.fragmentos.CategoriaFragment;
import duoc.cl.mibolsillo.fragmentos.GastoFragment;
import duoc.cl.mibolsillo.fragmentos.MisDatosFragment;
import duoc.cl.mibolsillo.util.Constantes;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CategoriaFragment.OnFragmentInteractionListener,
        GastoFragment.OnFragmentInteractionListener,
        MisDatosFragment.OnFragmentInteractionListener {

  TextView lblNombre, lblCorreo;
  ProgressDialog progressDialog;
  int id;
  List<Categoria> listaCategorias = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    obtenerCategorias();

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        switch (getTitle().toString()) {
          case "Categorías":
            modalAddCategoria();
            break;
          case "Gastos":
            modalAddGasto();
            break;
        }
//        Toast.makeText(MainActivity.this, getTitle(), Toast.LENGTH_SHORT).show();
//        Snackbar.make(view, idUsuario, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
      }
    });

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    View headerView = navigationView.getHeaderView(0);
    lblNombre = headerView.findViewById(R.id.lblNombre);
    lblCorreo = headerView.findViewById(R.id.lblCorreo);
    lblNombre.setText(getIntent().getStringExtra(Constantes.EXT_NOMBRE));
    lblCorreo.setText(getIntent().getStringExtra(Constantes.EXT_MAIL));

    SharedPreferences prefs = getSharedPreferences(Constantes.PREF_DATA, MODE_PRIVATE);
    id = prefs.getInt(Constantes.PREF_ID, 0);
  }


  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      logout();
//      super.onBackPressed();
    }
  }

  private void logout() {
    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setTitle("Atención");
    alertDialog.setMessage("¿ Desea cerrar la sesión ?");
    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si", (dialog, which) -> {
      borrarPreferencias();
      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
      startActivity(intent);
      finish();
    });
    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> dialog.dismiss());
    alertDialog.show();
  }

  private void borrarPreferencias() {
    SharedPreferences prefs = getSharedPreferences(Constantes.PREF_DATA, MODE_PRIVATE);
    prefs.edit().clear().commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_cerrar_sesion) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    Fragment fragment = null;
    int id = item.getItemId();
    if (id == R.id.nav_categoria) {
      fragment = new CategoriaFragment();
      this.setTitle(getString(R.string.categorias));
    } else if (id == R.id.nav_gastos) {
      fragment = new GastoFragment();
      setTitle(R.string.gastos);
    } else if (id == R.id.nav_mis_datos) {
      fragment = new MisDatosFragment();
      setTitle(R.string.mis_datos);
    }
    if (fragment != null) {
      getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, fragment).commit();
    }
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onFragmentInteraction(Uri uri) {

  }

  public void cerrarSesión(MenuItem item) {
    logout();
  }

  public void modalAddCategoria() {
    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    LayoutInflater inflater = this.getLayoutInflater();
    alertDialog.setView(inflater.inflate(R.layout.add_categoria, null));
    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Agregar", (dialog, which) -> {
      progressDialog = new ProgressDialog(this);
      progressDialog.setMessage("Registrando Categoría");
      progressDialog.show();
      EditText nombre = alertDialog.findViewById(R.id.txtNombreCategoria);
      Categoria categoria = new Categoria();
      categoria.setNombre(nombre.getText().toString());
      categoria.setUsuario_id(id);
      addCategoria(categoria);
    });
    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", (dialog, which) -> dialog.dismiss());
    alertDialog.show();
  }

  private void modalAddGasto() {
    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    LayoutInflater inflater = this.getLayoutInflater();
    View myView = inflater.inflate(R.layout.add_gasto, null);
    alertDialog.setView(myView);
    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Agregar", (dialog, which) -> {
      progressDialog = new ProgressDialog(this);
      progressDialog.setMessage("Registrando Gasto");
      progressDialog.show();

      Spinner cboCategoria = myView.findViewById(R.id.cboCategoria);
      EditText txtDescripcionGasto = alertDialog.findViewById(R.id.txtDescripcionGasto);
      EditText txtGastoMonto = alertDialog.findViewById(R.id.txtGastoMonto);

      SharedPreferences prefs = getSharedPreferences(Constantes.PREF_DATA, MODE_PRIVATE);
      int id = prefs.getInt(Constantes.PREF_ID, 0);

      Gasto gasto = new Gasto();

      gasto.setCategoria_id(listaCategorias.get(cboCategoria.getSelectedItemPosition()).getId());
      gasto.setDescripcion(txtDescripcionGasto.getText().toString());
      gasto.setMonto(Integer.parseInt(txtGastoMonto.getText().toString()));
      gasto.setUsuario_id(id);
      /*)
      ********************************************************************************************
      Sucede que a pesar de estar parseando la fecha con el formato yyyy-MM-dd
      El body se envía de la siguiente forma
      {
        "categoria_id":2,
        "descripcion":"La Micro",
        "fecha":"Oct 26, 2018 12:50:29 AM",
        "id":0,
        "monto":650,
        "usuario_id":1
       }
       todos los campos se envían bien, excepto la fecha que debiera ser "yyyy-MM-dd"
      */
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      try {
        gasto.setFecha(df.parse(df.format(new Date())));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      //********************************************************************************************

      agregarGasto(gasto);
    });
    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", (dialog, which) -> dialog.dismiss());
    List<String> spinnerArray = new ArrayList<>();
    if (listaCategorias.isEmpty()) {
      Toast.makeText(this, "Debe tener al menos una Categoría.", Toast.LENGTH_SHORT).show();
      return;
    }
    for (Categoria c : listaCategorias) {
      spinnerArray.add(c.getNombre());
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    Spinner cboCategoria = myView.findViewById(R.id.cboCategoria);
    cboCategoria.setAdapter(adapter);
    alertDialog.show();
  }

  private void agregarGasto(Gasto gasto) {
    Call<Gasto> agregarGastoCall = BolsilloApiAdapter.getApiService().agregarGasto(gasto);
    agregarGastoCall.enqueue(new AgregarGastoCallback());
  }

  private void poblarSpinnerCategorias() {
    obtenerCategorias();
  }

  private void obtenerCategorias() {
    SharedPreferences prefs = getSharedPreferences(Constantes.PREF_DATA, MODE_PRIVATE);
    int id = prefs.getInt(Constantes.PREF_ID, 0);
    if (id > 0) {
      Call<List<Categoria>> categoriasCall = BolsilloApiAdapter.getApiService().getCategorias(id);
      categoriasCall.enqueue(new CategoriasCallback());
    }
  }

  private void addCategoria(Categoria categoria) {
    Call<Categoria> categoriaCall = BolsilloApiAdapter.getApiService().agregarCategoria(categoria);
    categoriaCall.enqueue(new CategoriaCallback());
  }

  private class CategoriaCallback implements retrofit2.Callback<Categoria> {
    @Override
    public void onResponse(Call<Categoria> call, Response<Categoria> response) {
      progressDialog.hide();
      if (response.isSuccessful()) {
        Toast.makeText(MainActivity.this, "Categoría agregada.", Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, new CategoriaFragment()).commit();
      } else {
        Toast.makeText(MainActivity.this, "Error en los datos enviados.", Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onFailure(Call<Categoria> call, Throwable t) {
      Toast.makeText(MainActivity.this, "Error al registrar usuario.", Toast.LENGTH_SHORT).show();
    }
  }

  private class CategoriasCallback implements retrofit2.Callback<List<Categoria>> {
    @Override
    public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
//      progressDialog.hide();
      if (response.isSuccessful()) {
        listaCategorias = response.body();
      }
      Toast.makeText(MainActivity.this, "" + listaCategorias.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call<List<Categoria>> call, Throwable t) {
      Toast.makeText(MainActivity.this, "Error de comunicación.", Toast.LENGTH_SHORT).show();
    }

  }

  private class AgregarGastoCallback implements retrofit2.Callback<Gasto> {
    @Override
    public void onResponse(Call<Gasto> call, Response<Gasto> response) {
      if (response.isSuccessful()) {
        Toast.makeText(MainActivity.this, "Gasto agregado.", Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_principal, new GastoFragment()).commit();
      } else {
        Toast.makeText(MainActivity.this, "Error en los datos enviados. " + response.code(), Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onFailure(Call<Gasto> call, Throwable t) {
      Toast.makeText(MainActivity.this, "Error de comunicación.", Toast.LENGTH_SHORT).show();
    }
  }
}
