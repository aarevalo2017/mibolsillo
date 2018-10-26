package duoc.cl.mibolsillo;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SmsTokenActivity extends Activity implements View.OnKeyListener {
  EditText txtC1, txtC2, txtC3, txtC4, txtC5, txtC6;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sms_token);

    txtC1 = findViewById(R.id.txtC1);
    txtC2 = findViewById(R.id.txtC2);
    txtC3 = findViewById(R.id.txtC3);
    txtC4 = findViewById(R.id.txtC4);
    txtC5 = findViewById(R.id.txtC5);
    txtC6 = findViewById(R.id.txtC6);

    txtC1.setOnKeyListener(this);
    txtC2.setOnKeyListener(this);
    txtC3.setOnKeyListener(this);
    txtC4.setOnKeyListener(this);
    txtC5.setOnKeyListener(this);
    txtC6.setOnKeyListener(this);
  }

  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {
    Toast.makeText(this, "" + event.getAction(), Toast.LENGTH_SHORT).show();
    if (-1 < keyCode && keyCode < 17 && event.getAction() == 0)
      switch (v.getId()) {
        case R.id.txtC1:
          txtC2.requestFocus();
          break;
        case R.id.txtC2:
          txtC3.requestFocus();
          break;
        case R.id.txtC3:
          txtC4.requestFocus();
          break;
        case R.id.txtC4:
          txtC5.requestFocus();
          break;
        case R.id.txtC5:
          txtC6.requestFocus();
          break;
      }
    return false;
  }
}
