package lipnus.com.hmtr;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Sunpil on 2018-02-18.
 */

public class GlobalApplication extends Application {

    public static String serverPath = "http://ec2-13-125-164-178.ap-northeast-2.compute.amazonaws.com:9000";

    public static int userinfo_pk=1818;
    public static double sequence=0;
    public static String category ="basic";

    public static String answerType="single"; //챕터3에서만 사용

    public static String facePath = "http://www.yonhapnewstv.co.kr/contents/mpic/YH/2016/09/22/MYH20160922007300038.jpg";
    public static String npcName = "박보검";

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
