package lipnus.com.hmtr.retro.Response;

import java.util.List;

/**
 * Created by Sunpil on 2018-02-27.
 */

public class ChattingBasic {

    public String type;
    public double sequence;
    public int script_pk;
    public String script;
    public int experienced;

    public List<AnswerBasic> answer;

    public ChattingBasic(String type, double sequence, int script_pk, String script, int experienced, List<AnswerBasic> answer) {
        this.type = type;
        this.sequence = sequence;
        this.script_pk = script_pk;
        this.script = script;
        this.experienced = experienced;
        this.answer = answer;
    }
}
