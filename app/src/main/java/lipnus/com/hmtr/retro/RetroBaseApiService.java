package lipnus.com.hmtr.retro;


import java.util.HashMap;

import lipnus.com.hmtr.retro.Response.ChattingBasic;
import lipnus.com.hmtr.retro.Response.GroupExist;
import lipnus.com.hmtr.retro.Response.User;
import retrofit2.Call;
import retrofit2.http.Body;
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
    Call<GroupExist> postGroup(@FieldMap HashMap<String, Object> parameters);


    //사용자등록
    @POST("/android/user")
    Call<User> postUser(@Body HashMap<String, Object> parameters);


    //기본인적사항
    @POST("/android/chat_basicinfo")
    Call<ChattingBasic> postBasic(@Body HashMap<String, Object> parameters);

    //학습행동유형(기본인적사항과 같은 모델을 공유)
    @POST("/android/chat_behavior")
    Call<ChattingBasic> postBehavior(@Body HashMap<String, Object> parameters);

    //학습적성유형
    @POST("/android/chat_aptitude")
    Call<ChattingBasic> postAptitude(@Body HashMap<String, Object> parameters);

}
