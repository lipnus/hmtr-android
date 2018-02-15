package lipnus.com.hmtr.retro.RequestBody;

import java.util.HashMap;

/**
 * Created by sonchangwoo on 2017. 1. 6..
 */

public class RequestPut {

    public final int userId;
    public final int id;
    public final String title;
    public final String body;

    public RequestPut(HashMap<String, Object> parameters) {
        this.userId = (int) parameters.get("userId");
        this.id = (int) parameters.get("id");
        this.title = (String) parameters.get("title");
        this.body = (String) parameters.get("body");
    }
}
