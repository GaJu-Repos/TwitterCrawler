import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class UseExample {
    public static void main(String[] args) throws TwitterException {

        final CrawlerAPI crawler = new CrawlerAPI("----INSERT-KEY----",
                "----INSERT-KEY----",
                "----INSERT-KEY----",
                "----INSERT-KEY----"
        );

        int output = crawler.getCountOfTweetsLastHour("IoT");
        System.out.println("Counted tweets: " + output);
        output = crawler.getCountOfTwitterUsers("IoT");
        System.out.println("Counted user: " + output);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<String> output = null;
                try {
                    output = crawler.getRelevantTweets(null);
                    for (String text : output) {
                        System.out.println(text);
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 0L, 1000*60*60);
    }
}
