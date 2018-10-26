package duoc.cl.mibolsillo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import duoc.cl.mibolsillo.api.BolsilloApiAdapter;
import duoc.cl.mibolsillo.entidades.Categoria;
import duoc.cl.mibolsillo.entidades.Usuario;
import duoc.cl.mibolsillo.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

  EditText txtMail, txtPass;
  ProgressDialog progressDialog;

  @Override
  public void onBackPressed() {
    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
    alertDialog.setTitle("Atención");
    alertDialog.setMessage("¿ Terminar aplicación ?");
    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si", (dialog, which) -> finish());
    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> dialog.dismiss());
    alertDialog.show();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    txtMail = findViewById(R.id.txtMail);
    txtPass = findViewById(R.id.txtPass);
  }

  public void login(View view) {
    try {
      progressDialog = new ProgressDialog(this);
      progressDialog.setMessage("Cargando...");
      progressDialog.show();
      String correo = txtMail.getText().toString();
      String password = txtPass.getText().toString();
      loginRequest(correo, password);
    } catch (Exception e) {
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  public void loginRequest(String correo, String password){
    Call<Usuario> loginCall = BolsilloApiAdapter.getApiService().login(correo, password);
    loginCall.enqueue(new LoginCallback());
  }

  public void registroUsuario(View view) {
    Intent intent = new Intent(this, RegistroActivity.class);
    startActivity(intent);
  }

  private class LoginCallback implements Callback<Usuario> {
    @Override
    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
      progressDialog.hide();
      if (response.isSuccessful()) {
        Usuario usuario = response.body();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(Constantes.EXT_NOMBRE, usuario.getNombre());
        intent.putExtra(Constantes.EXT_MAIL, usuario.getCorreo());
        SharedPreferences.Editor editor = getSharedPreferences(Constantes.PREF_DATA, MODE_PRIVATE).edit();
        editor.putInt(Constantes.PREF_ID, usuario.getId());
        editor.putString(Constantes.PREF_CORREO, usuario.getCorreo());
        editor.putString(Constantes.PREF_PASSWORD, txtPass.getText().toString());
        editor.commit();
        startActivity(intent);
        finish();
      } else {
        Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onFailure(Call<Usuario> call, Throwable t) {
      progressDialog.hide();
      Toast.makeText(LoginActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
    }
  }
}
