package lipnus.com.hmtr.retro.ResponseBody;

/**
 * Created by Sunpil on 2018-02-12.
 */

public class GroupExist {

    //입력한 설문조사 그룹이 서버에 존재하는지 확인(open, close, none)
    public final String result;

    public GroupExist(String result) {
        this.result = result;
    }
}
