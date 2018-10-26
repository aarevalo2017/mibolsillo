package duoc.cl.mibolsillo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import duoc.cl.mibolsillo.R;
import duoc.cl.mibolsillo.entidades.Gasto;

public class GastoFilaAdapter extends BaseAdapter {

  Context context;
  List<Gasto> data;
  private static LayoutInflater inflater = null;

  public GastoFilaAdapter(Context context, List<Gasto> data) {
    // TODO Auto-generated constructor stub
    this.context = context;
    this.data = data;
    inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    return data.size();
  }

  @Override
  public Object getItem(int position) {
    // TODO Auto-generated method stub
    return data.get(position);
  }

  @Override
  public long getItemId(int position) {
    // TODO Auto-generated method stub
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // TODO Auto-generated method stub
    View vi = convertView;
    if (vi == null)
      vi = inflater.inflate(R.layout.gasto_row, null);
    TextView lblDescripcionGasto = vi.findViewById(R.id.lblDescripcionGasto);
    TextView lblMontoGasto = vi.findViewById(R.id.lblMontoGasto);
    TextView lblFechaGasto = vi.findViewById(R.id.lblFechaGasto);
    lblDescripcionGasto.setText(data.get(position).getDescripcion());
    lblMontoGasto.setText("$ " + data.get(position).getMonto());

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    lblFechaGasto.setText(df.format(data.get(position).getFecha()));
    return vi;
  }
}
