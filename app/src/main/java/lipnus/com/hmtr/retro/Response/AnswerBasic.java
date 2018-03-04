package lipnus.com.hmtr.retro.Response;

/**
 * Created by Sunpil on 2018-02-27.
 */

public class AnswerBasic {

    public int choice_pk;
    public String choice;
    public String custom;
    public String information;

    public AnswerBasic(int choice_pk, String choice, String custom, String information) {
        this.choice_pk = choice_pk;
        this.choice = choice;
        this.custom = custom;
        this.information = information;
    }
}
