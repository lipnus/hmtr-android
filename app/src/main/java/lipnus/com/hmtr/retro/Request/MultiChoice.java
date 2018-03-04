package lipnus.com.hmtr.retro.Request;

/**
 * Created by Sunpil on 2018-03-04.
 */

public class MultiChoice {

    public int question_pk;
    public int answer;

    public MultiChoice(int question_pk, int answer) {
        this.question_pk = question_pk;
        this.answer = answer;
    }
}
