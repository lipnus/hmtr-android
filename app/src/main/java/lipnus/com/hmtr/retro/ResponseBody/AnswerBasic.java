package lipnus.com.hmtr.retro.ResponseBody;

/**
 * Created by Sunpil on 2018-02-27.
 */

public class AnswerBasic {

    public int choice_pk;
    public String choice;
    public String custom;

    public AnswerBasic(int choice_pk, String choice, String custom) {
        this.choice_pk = choice_pk;
        this.choice = choice;
        this.custom = custom;
    }
}
