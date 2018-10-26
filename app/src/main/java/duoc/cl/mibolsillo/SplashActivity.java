package duoc.cl.mibolsillo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import duoc.cl.mibolsillo.api.BolsilloApiAdapter;
import duoc.cl.mibolsillo.entidades.Usuario;
import duoc.cl.mibolsillo.util.Constantes;
import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

  Intent intent;
  ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    new Handler().postDelayed(() -> {
      SharedPreferences prefs = getSharedPreferences(Constantes.PREF_DATA, MODE_PRIVATE);
      String correo = prefs.getString(Constantes.PREF_CORREO, null);
      String password = prefs.getString(Constantes.PREF_PASSWORD, null);
      if (correo != null && password != null) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando Sesión");
        progressDialog.show();
        loginRequest(correo, password);
      } else {
        iniciarLogin();
      }
    }, 1500);
  }

  private void loginRequest(String correo, String password) {
    Call<Usuario> loginCall = BolsilloApiAdapter.getApiService().login(correo, password);
    loginCall.enqueue(new LoginCallback());
  }

  private void iniciarMain() {
    intent = new Intent(SplashActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
  }

  private void iniciarLogin() {
    intent = new Intent(SplashActivity.this, LoginActivity.class);
    startActivity(intent);
    finish();
  }

  private class LoginCallback implements retrofit2.Callback<Usuario> {
    @Override
    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
      progressDialog.hide();
      if (response.isSuccessful()) {
        Usuario usuario = response.body();
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(Constantes.EXT_NOMBRE, usuario.getNombre());
        intent.putExtra(Constantes.EXT_MAIL, usuario.getCorreo());
        startActivity(intent);
        finish();
      } else {
        iniciarLogin();
      }
    }

    @Override
    public void onFailure(Call<Usuario> call, Throwable t) {
      progressDialog.hide();
      iniciarLogin();
    }
  }
}
