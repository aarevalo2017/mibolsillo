package duoc.cl.mibolsillo.entidades;

import java.util.Date;

public class Gasto {
  private int id;
  private String descripcion;
  private Date fecha;
  private int monto;
  private int categoria_id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public int getMonto() {
    return monto;
  }

  public void setMonto(int monto) {
    this.monto = monto;
  }

  public int getCategoria_id() {
    return categoria_id;
  }

  public void setCategoria_id(int categoria_id) {
    this.categoria_id = categoria_id;
  }

}
