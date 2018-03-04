package lipnus.com.hmtr.retro.Response;

/**
 * Created by Sunpil on 2018-02-27.
 */

public class AnswerBasic {

    public int choice_pk;
    public String choice;
    public String custom;
    public String information;

    //chatter4에서만 쓰는 변수
    public String result;
    public double next_seq;
    public int root_seq;

    public AnswerBasic(int choice_pk, String choice, String custom, String information) {
        this.choice_pk = choice_pk;
        this.choice = choice;
        this.custom = custom;
        this.information = information;
    }

    public AnswerBasic(int choice_pk, String choice, String custom, String information, String result, double next_seq, int root_seq) {
        this.choice_pk = choice_pk;
        this.choice = choice;
        this.custom = custom;
        this.information = information;
        this.result = result;
        this.next_seq = next_seq;
        this.root_seq = root_seq;
    }
}
