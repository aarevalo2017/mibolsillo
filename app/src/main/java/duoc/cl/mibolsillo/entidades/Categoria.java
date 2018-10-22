package duoc.cl.mibolsillo.entidades;

import java.util.Date;

public class Categoria {
  private int id;
  private String nombre;
  private int usuario_id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getUsuario_id() {
    return usuario_id;
  }

  public void setUsuario_id(int usuario_id) {
    this.usuario_id = usuario_id;
  }

}
