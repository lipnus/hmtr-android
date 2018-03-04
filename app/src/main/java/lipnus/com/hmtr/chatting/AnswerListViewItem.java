package lipnus.com.hmtr.chatting;

/**
 * Created by Sunpil on 2018-02-26.
 */

public class AnswerListViewItem {

    public int choice_pk;
    public String choice;
    public String custom;
    public String information;

    //챕터4에서만 사용
    public String result;
    public double next_seq;
    public int root_seq;


    public AnswerListViewItem(int choice_pk, String choice, String custom, String information) {
        this.choice_pk = choice_pk;
        this.choice = choice;
        this.custom = custom;
        this.information = information;
    }

    public AnswerListViewItem(int choice_pk, String choice, String custom, String information, String result, double next_seq, int root_seq) {
        this.choice_pk = choice_pk;
        this.choice = choice;
        this.custom = custom;
        this.information = information;
        this.result = result;
        this.next_seq = next_seq;
        this.root_seq = root_seq;
    }
}
