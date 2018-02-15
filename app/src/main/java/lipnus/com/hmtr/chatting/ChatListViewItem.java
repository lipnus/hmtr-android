package lipnus.com.hmtr.chatting;


/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class ChatListViewItem {

    public String npcName;
    public String npcFaceImg;
    public String npcScript;
    public String userScript;
    public String date;

    public ChatListViewItem(String npcName, String npcFaceImg, String npcScript, String userScript, String date) {
        this.npcName = npcName;
        this.npcFaceImg = npcFaceImg;
        this.npcScript = npcScript;
        this.userScript = userScript;
        this.date = date;
    }
}