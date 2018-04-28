package lipnus.com.hmtr;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Sunpil on 2018-02-18.
 */

public class GlobalApplication extends Application {

    public static String serverPath = "http://ec2-13-125-164-178.ap-northeast-2.compute.amazonaws.com:9000";

    public static int userinfo_pk=0;
    public static double sequence=0;

    public static String category ="basic";
    public static String answerType="single"; //챕터3에서만 사용

    public static int delayTime=1000;
    public static int customDelayTime =1000;

    public static String facePath = "https://t1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/2ycP/image/K6EbOKj2CCsHMmYfTD6JLWfW9s8.jpg";
    public static String npcName = "토리";

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/NanumGothic.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
