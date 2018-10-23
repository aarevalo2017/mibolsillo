package duoc.cl.mibolsillo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CategoriaActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnFCategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        btnFCategoria = (Button)findViewById(R.id.btnFCategoria);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFCategoria:

        }
    }
}
