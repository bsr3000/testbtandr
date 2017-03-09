package ishchenko.a.testbt;

import java.util.ArrayList;
import java.util.List;

import ishchenko.a.testbt.SupportClasses.Docs;
import ishchenko.a.testbt.SupportClasses.Group;
import ishchenko.a.testbt.SupportClasses.Position;
import ishchenko.a.testbt.SupportClasses.Token;
import ishchenko.a.testbt.SupportClasses.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by andrey on 07.03.17.
 */

public interface API {

    @FormUrlEncoded
    @POST("core/api-token-auth/")
    Call<Token> getToken(@Field("username") String username, @Field("password") String password);

    @GET("core/api/users/token/{token}/")
    Call<User> getCurrentUser(@Path("token") String token);

    @GET("core/api/users/")
    Call<List<User>> getUserList(@Query("ordering") String sort);

    @GET("core/api/users/")
    Call<List<User>> searchUser(@Query("search") String search);

    @GET("core/api/users/")
    Call<List<User>> filterUserList(@Query("group") Integer groupId, @Query("minsalary") Integer min, @Query("maxsalary") Integer max);

    @GET("core/api/users/{id}")
    Call<User> getUser(@Path("id") Integer id);

    @GET("core/api/groups/")
    Call<List<Group>> getGroupList();

    @GET("core/api/groups/{id}")
    Call<Group> getGroups(@Path("id") Integer id);

    @GET("core/api/positions/")
    Call<List<Position>> getPositions();

    @POST("core/api/users/")
    Call<User> createUser(@Body User user);

    @PUT("core/api/users/{id}/")
    Call<User> updateUser(@Path("id") Integer id, @Body User user);

    @GET("core/api/users/{id}/retrieve_docs/")
    Call<ArrayList<Docs>> getDocs(@Path("id") int id);

    @POST("core/api/users/{id}/create_docs/")
    Call<Docs> createDoc(@Path("id") int id);
}
