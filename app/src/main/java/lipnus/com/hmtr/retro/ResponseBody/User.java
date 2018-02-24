package lipnus.com.hmtr.retro.ResponseBody;

/**
 * Created by Sunpil on 2018-02-24.
 */

public class User {
    public String result;
    public String date;
    public int count;
    public int userinfo_pk;

    public User(String result, String date, int count, int userinfo_pk) {
        this.result = result;
        this.date = date;
        this.count = count;
        this.userinfo_pk = userinfo_pk;
    }
}
