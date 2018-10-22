package duoc.cl.mibolsillo.api;

import java.util.List;

import duoc.cl.mibolsillo.entidades.Categoria;
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

  @GET("usuario/{id}/categoria")
  Call<List<Categoria>> getCategorias(@Path("id") int id);

  @POST("categoria")
  Call<Categoria> addCategoria(@Body Categoria categoria);

  @POST("usuario")
  Call<Usuario> agregarUsuario(@Body Usuario usuario);
}
