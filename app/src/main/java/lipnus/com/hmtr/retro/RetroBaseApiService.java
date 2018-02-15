package lipnus.com.hmtr.retro;


import java.util.HashMap;

import lipnus.com.hmtr.retro.ResponseBody.GroupExistGet;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by sonchangwoo on 2017. 1. 1..
 */

public interface RetroBaseApiService {

    final String Base_URL = "http://ec2-13-125-164-178.ap-northeast-2.compute.amazonaws.com:9000";

    @FormUrlEncoded
    @POST("/android/group")
    Call<GroupExistGet> postFirst(@FieldMap HashMap<String, Object> parameters);

}
