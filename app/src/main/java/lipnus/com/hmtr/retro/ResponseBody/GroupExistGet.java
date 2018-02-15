package lipnus.com.hmtr.retro.ResponseBody;

/**
 * Created by Sunpil on 2018-02-12.
 */

public class GroupExistGet {

    //입력한 설문조사 그룹이 서버에 존재하는지 확인
    public final String result;

    public GroupExistGet(String result) {
        this.result = result;
    }
}
