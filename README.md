# TwitterCrawler
TwitterCrawler is a Java API for searching through Twitter using the Twitter API twitter4j.

## Installation
Add the compiled Java project to your project to use the API.
Important: You need a Twitter developer account and API keys to access Twitter.
Since this is a whole java program. It includes an example, UseExample.java.
When using the pure API in your project you may delete the UseExample.java file before installing and importing the CrawlerAPI to your project.

## Usage

```java
CrawlerAPI(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret)
```
Create a new Object with the four API keys to use the functions below.


```java
int getCountOfTweetsLastHour (String content)
```
This function returns the count of the tweets in the last hour for a specific topic.


```java
int getCountOfTwitterUsers (String content)
```
This function returns the count of user talking about a specific topic in the last hour.


```java
ArrayList<String> getRelevantTweets (String content)
```
This function returns a list of relevant tweets of the last hour containing the tweet content and the author.


## Example description
The UseExample.java file is showing an example of searching for relevant tweets in a periodic time. 
The periodic time is currently every hour. It is possible to extend the API to other time intervals, e.g every minute, every day. 

## Links
[Twitter4j](twitter4j.org)

