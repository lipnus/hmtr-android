package lipnus.com.hmtr.retro;

/**
 * Created by sonchangwoo on 2017. 1. 6..
 */

public interface RetroCallback<T> {

    void onError(Throwable t);

    void onSuccess(int code, T receivedData);

    void onFailure(int code);

}
