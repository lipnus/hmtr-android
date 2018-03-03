package lipnus.com.hmtr.chatting;

/**
 * Created by Sunpil on 2018-02-26.
 */

public class MultiAnswerListViewItem {

    public int choice_pk;
    public String choice;
    public String custom;
    public String information;
    public int checkValue; //-1,0,1

    public MultiAnswerListViewItem(int choice_pk, String choice, String custom, String information, int checkValue) {
        this.choice_pk = choice_pk;
        this.choice = choice;
        this.custom = custom;
        this.information = information;
        this.checkValue = checkValue;
    }
}
