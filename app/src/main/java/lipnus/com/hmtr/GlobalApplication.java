package lipnus.com.hmtr;

import android.app.Application;



/**
 * Created by Sunpil on 2018-02-18.
 */

public class GlobalApplication extends Application {

    public static String serverPath = "http://ec2-13-125-164-178.ap-northeast-2.compute.amazonaws.com:9000";

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
