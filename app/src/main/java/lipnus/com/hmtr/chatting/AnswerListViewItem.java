package lipnus.com.hmtr.chatting;

/**
 * Created by Sunpil on 2018-02-26.
 */

public class AnswerListViewItem {

    public int choice_pk;
    public String choice;
    public String custom;
    public String information;


    public AnswerListViewItem(int choice_pk, String choice, String custom, String information) {
        this.choice_pk = choice_pk;
        this.choice = choice;
        this.custom = custom;
        this.information = information;
    }
}
