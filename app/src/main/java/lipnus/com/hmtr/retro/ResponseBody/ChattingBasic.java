package lipnus.com.hmtr.retro.ResponseBody;

import java.util.List;

/**
 * Created by Sunpil on 2018-02-27.
 */

public class ChattingBasic {

    public String type;
    public int sequence;
    public int script_pk;
    public String script;
    public String category;
    public int experienced;
    public List<AnswerBasic> answer;

    public ChattingBasic(String type, int sequence, int script_pk, String script, String category, int experienced, List<AnswerBasic> answer) {
        this.type = type;
        this.sequence = sequence;
        this.script_pk = script_pk;
        this.script = script;
        this.category = category;
        this.experienced = experienced;
        this.answer = answer;
    }
}
