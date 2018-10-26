package duoc.cl.mibolsillo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import duoc.cl.mibolsillo.api.BolsilloApiAdapter;
import duoc.cl.mibolsillo.entidades.Usuario;
import retrofit2.Call;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {
  EditText txtMail, txtNombre, txtTelefono, txtPass, txtPassConfirm;
  boolean masculino = true;
  ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registro);
    txtMail = findViewById(R.id.txtMail);
    txtNombre = findViewById(R.id.txtNombre);
    txtTelefono = findViewById(R.id.txtTelefono);
    txtPass = findViewById(R.id.txtPass);
    txtPassConfirm = findViewById(R.id.txtPassConfirm);
  }

  public void generoClicked(View view) {
//    boolean checked = ((RadioButton) view).isChecked();
    switch (view.getId()) {
      case R.id.rdMasculino:
        masculino = true;
        break;
      case R.id.rdFemenino:
        masculino = false;
        break;
    }
  }

  public void registrarUsuario(View view) {
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Registrando usuario");
    progressDialog.show();

    Usuario usuario = new Usuario();
    usuario.setCorreo(txtMail.getText().toString());
    usuario.setNombre(txtNombre.getText().toString());
    usuario.setTelefono(txtTelefono.getText().toString());
    usuario.setPassword(txtPass.getText().toString());
    usuario.setGenero(masculino ? 'M' : 'F');

    Call<Usuario> agregarUsuarioCall = BolsilloApiAdapter.getApiService().agregarUsuario(usuario);
    agregarUsuarioCall.enqueue(new agregarUsuarioCallback());
  }

  private class agregarUsuarioCallback implements retrofit2.Callback<Usuario> {
    @Override
    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
      progressDialog.hide();
      if (response.isSuccessful()){
        Usuario usuario = response.body();
        Toast.makeText(RegistroActivity.this, "Usuario agregado.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistroActivity.this, SmsTokenActivity.class);
        startActivity(intent);
        finish();
      }else{
        Toast.makeText(RegistroActivity.this, "Error en los datos enviados.", Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onFailure(Call<Usuario> call, Throwable t) {
      progressDialog.hide();
      Toast.makeText(RegistroActivity.this, "Error al registrar usuario.", Toast.LENGTH_SHORT).show();
    }
  }
}
