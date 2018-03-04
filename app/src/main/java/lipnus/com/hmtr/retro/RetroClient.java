package lipnus.com.hmtr.retro;


import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import lipnus.com.hmtr.retro.Response.ChattingBasic;
import lipnus.com.hmtr.retro.Response.GroupExist;
import lipnus.com.hmtr.retro.Response.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sonchangwoo on 2017. 1. 6..
 */

public class RetroClient {

    private RetroBaseApiService apiService;
    public static String baseUrl = RetroBaseApiService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static RetroClient INSTANCE = new RetroClient(mContext);
    }

    public static RetroClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    private RetroClient(Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public RetroClient createBaseApi() {
        apiService = create(RetroBaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }



    public void postFirst(HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postGroup(parameters).enqueue(new Callback<GroupExist>() {
            @Override
            public void onResponse(Call<GroupExist> call, Response<GroupExist> response) {
                if (response.isSuccessful()) {
                    Log.e("VOVO", "받은내용: " + response.body());
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<GroupExist> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void postGroup (HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postGroup(parameters).enqueue(new Callback<GroupExist>() {
            @Override
            public void onResponse(Call<GroupExist> call, Response<GroupExist> response) {
                if (response.isSuccessful()) {
                    Log.e("VOVO", "받은내용: " + response.body());
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<GroupExist> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void postUser (HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postUser(parameters).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.e("VOVO", "받은내용: " + response.body());
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    //기본인적사항
    public void postBasic (HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postBasic(parameters).enqueue(new Callback<ChattingBasic>() {
            @Override
            public void onResponse(Call<ChattingBasic> call, Response<ChattingBasic> response) {
                if (response.isSuccessful()) {
                    Log.e("VOVO", "받은내용: " + response.body());
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ChattingBasic> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    //학습행동유형
    public void postBehavior (HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postBehavior(parameters).enqueue(new Callback<ChattingBasic>() {
            @Override
            public void onResponse(Call<ChattingBasic> call, Response<ChattingBasic> response) {
                if (response.isSuccessful()) {
                    Log.e("VOVO", "받은내용: " + response.body());
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ChattingBasic> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    //학습적성유형
    public void postAptitude (HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postAptitude(parameters).enqueue(new Callback<ChattingBasic>() {
            @Override
            public void onResponse(Call<ChattingBasic> call, Response<ChattingBasic> response) {
                if (response.isSuccessful()) {
                    Log.e("VOVO", "받은내용: " + response.body());
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ChattingBasic> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    //밸런스자가측정
    public void postBalance (HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postBalance(parameters).enqueue(new Callback<ChattingBasic>() {
            @Override
            public void onResponse(Call<ChattingBasic> call, Response<ChattingBasic> response) {
                if (response.isSuccessful()) {
                    Log.e("VOVO", "받은내용: " + response.body());
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ChattingBasic> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


}
