package lipnus.com.hmtr;

import android.app.Application;



/**
 * Created by Sunpil on 2018-02-18.
 */

public class GlobalApplication extends Application {

    public static String serverPath = "http://ec2-13-125-164-178.ap-northeast-2.compute.amazonaws.com:9000";

    public static int userinfo_pk=1;
    public static int sequence=0;
    public static String category ="";

    public static String facePath = "http://img.yonhapnews.co.kr/etc/inner/KR/2017/10/19/AKR20171019092300003_01_i.jpg";
    public static String npcName = "공유";

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
