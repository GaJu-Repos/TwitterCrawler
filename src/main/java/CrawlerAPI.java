import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author Gabor Juche
 */

public class CrawlerAPI {

    private Twitter twitter;

    private final TweetTimes time = TweetTimes.ONE_HOUR;
    private final int MAX_TWEETS = 100;
    private final String LANGUAGE = "en";
    private final String DEFAULT_CONTENT = "IoT";


    /**
     * Different extendable searching durations
     */
    public enum TweetTimes {
        ONE_HOUR(3600);

        private final int time;

        TweetTimes(int time) {
            this.time = time;
        }

        private Date getTime() {
            return new Date(System.currentTimeMillis() - this.time * 1000);
        }
    }

    /**
     * @param consumerKey
     * @param consumerSecret
     * @param accessToken
     * @param accessTokenSecret
     */
    CrawlerAPI(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);
        this.twitter = new TwitterFactory(cb.build()).getInstance();
    }

    private Query getQuery(String content) {
        Query query = new Query(content);
        query.setCount(MAX_TWEETS);
        query.setLang(LANGUAGE);
        query.setResultType(Query.ResultType.recent);
        return query;
    }

    /**
     * Get count of latest tweets for a specific topic in the last hour
     *
     * @param content topic to search for
     * @throws TwitterException
     */
    public int getCountOfTweetsLastHour(String content) throws TwitterException {
        if (content == null || content.trim().isEmpty()) {
            content = DEFAULT_CONTENT;
        }
        int counter = 0;
        long lastTweet;
        boolean isSearching = true;
        Date endOfSearch = time.getTime();
        Date tweetCreated;
        Query query = getQuery(content);
        while (isSearching) {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                tweetCreated = status.getCreatedAt();
                lastTweet = status.getId() - 1;
                counter++;
                if (tweetCreated.before(endOfSearch)) {
                    isSearching = false;
                    break;
                } else {
                    if (lastTweet != 0) {
                        query.setMaxId(lastTweet);
                    }
                }
            }
        }
        return counter;
    }


    /**
     * Get count of users which tweeted for a specific topic in the last hour
     *
     * @param content topic to search for
     * @throws TwitterException
     */
    public int getCountOfTwitterUsers(String content) throws TwitterException {
        if (content == null || content.trim().isEmpty()) {
            content = DEFAULT_CONTENT;
        }
        ArrayList<Long> users = new ArrayList<>();
        long lastTweet;
        Date tweetCreated;
        boolean isSearching = true;
        Date endOfSearch = time.getTime();
        Query query = getQuery(content);
        while (isSearching) {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                tweetCreated = status.getCreatedAt();
                lastTweet = status.getId() - 1;
                if (!users.contains(status.getUser().getId())) {
                    users.add(status.getUser().getId());
                }
                if (tweetCreated.before(endOfSearch)) {
                    isSearching = false;
                    break;
                } else {
                    query.setMaxId(lastTweet);
                }
            }
        }
        return users.size();
    }


    /**
     * Gets the tweets of a specific topic in the last hour
     *
     * @param content topic to search for
     * @return ArrayList<String> which contains the name of author and the tweet message
     * @throws TwitterException
     */
    public ArrayList<String> getRelevantTweets(String content) throws TwitterException {
        if (content == null || content.trim().isEmpty()) {
            content = DEFAULT_CONTENT;
        }
        ArrayList<String> tweetList = new ArrayList<>();
        long lastTweet;
        boolean isSearching = true;
        Date tweetCreated;
        Date endOfSearch = time.getTime();
        Query query = getQuery(content);
        while (isSearching) {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                tweetCreated = status.getCreatedAt();
                lastTweet = status.getId() - 1;
                tweetList.add("| @" + status.getUser().getScreenName() + " : " + status.getText());

                if (tweetCreated.before(endOfSearch)) {
                    isSearching = false;
                    break;
                } else {
                    if (lastTweet != 0) {
                        query.setMaxId(lastTweet);
                    }
                }
            }
        }
        return tweetList;
    }
}
