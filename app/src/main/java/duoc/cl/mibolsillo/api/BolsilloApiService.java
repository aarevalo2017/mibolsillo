package duoc.cl.mibolsillo.api;

import java.util.List;

import duoc.cl.mibolsillo.entidades.Categoria;
import duoc.cl.mibolsillo.entidades.Gasto;
import duoc.cl.mibolsillo.entidades.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BolsilloApiService {

  @FormUrlEncoded
  @POST("login")
  Call<Usuario> login(@Field("correo") String correo, @Field("password") String password);

  @GET("usuario/{id}/categorias")
  Call<List<Categoria>> getCategorias(@Path("id") int id);

  @GET("usuario/{id}/gastos")
  Call<List<Gasto>> getGastos(@Path("id") int id);

  @POST("categoria")
  Call<Categoria> agregarCategoria(@Body Categoria categoria);

  @POST("gasto")
  Call<Gasto> agregarGasto(@Body Gasto gasto);

  @POST("usuario")
  Call<Usuario> agregarUsuario(@Body Usuario usuario);
}
