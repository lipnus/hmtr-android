package lipnus.com.hmtr.retro.ResponseBody;

/**
 * Created by sonchangwoo on 2017. 1. 6..
 */

public class ResponseGet {

    public final int userId;
    public final int id;
    public final String title;
    public final String body;

    public ResponseGet(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

}
