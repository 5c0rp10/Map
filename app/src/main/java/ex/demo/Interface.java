package ex.demo;



import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;


public interface Interface {

    @FormUrlEncoded
    @POST
    Observable<Response<Location>> fetchLocationData(@Url String url, @Field("apiKey") String apiKey, @Field("userId") String userId);
}
