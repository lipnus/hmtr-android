package lipnus.com.hmtr.retro.Response;

/**
 * Created by LIPNUS on 2018-03-08.
 */

public class ServerInfo {

    public int version;
    public String nickname;
    public String imgpath;
    public int count_basic;
    public int count_behavior;
    public int count_aptitude;
    public int count_balance;

    public ServerInfo(int version, String nickname, String imgpath, int count_basic, int count_behavior, int count_aptitude, int count_balance) {
        this.version = version;
        this.nickname = nickname;
        this.imgpath = imgpath;
        this.count_basic = count_basic;
        this.count_behavior = count_behavior;
        this.count_aptitude = count_aptitude;
        this.count_balance = count_balance;
    }
}
