package aplclass;

import client.Post;
import prg.A;
import prg.P;
import prg.V;

import java.util.TimerTask;

public class ReplicateTimerTask extends TimerTask {

    @Override
    public void run() {
        // время по 3-му часовому поясу в формате HH24MI
        P.SQLEXECT("SELECT (TO_NUMBER(TO_CHAR(SYS_EXTRACT_UTC(SYSTIMESTAMP), 'HH24MI')) + 300) AS HOUR FROM DUAL", "UTC3",false);
        int utc3Hour = A.GETVALN("UTC3.HOUR");
        A.CLOSE("UTC3");

        // запускать автоматически онлайн обмен до 18.30 и после 20.30
        if (utc3Hour < 1830 || utc3Hour > 2030) {
            System.out.printf("Start rkv010 in %d%n", utc3Hour);
            Post post = Post.getInstance();
            post.setParam("Query", "035");
            String PARAM = "010~" + V.USER_FIO + "~" + V.ARM;
            post.setParam("P1", PARAM);
            post.SendDataWithResult(null, 1);
        }
    }

}
