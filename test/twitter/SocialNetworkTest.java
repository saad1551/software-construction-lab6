/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test 
    public void testGuessFollowsGraphNoMention() {
        Tweet tweet1 = Tweet(1, 'saad', 'great game! deserving win',  Instant.now());
        Tweet tweet2 = Tweet(2, 'ali', 'must visit this place',  Instant.now());

        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        tweets.add(tweet1);
        tweets.add(tweet2);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    @Test
    public void testGuessFollowsGraphIdentifyUsers() {
        Tweet tweet1 = Tweet(1, 'farhan', 'hello, @saad', Instant.now());
        Tweet tweet2 = Tweet(2, 'waqas', 'cant believe @farhan follows me', Instant.now());

        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        tweets.add(tweet1);
        tweets.add(tweet2);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        assertTrue('expected user @saad', followsGraph['farhan'].contains('saad'));
        assertTrue('expected user@farhan', followsGraph['waqas'].contains('farhan'));
    }

    @Test
    public void testGuessFollowsGraphMultipleUsers() {
        Tweet tweet1 = Tweet(1, 'athar', 'great work by @rohaan and @bilal', Instant.now());

        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        tweets.add(tweet1);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        assertTrue('expected user @rohaan', followsGraph['athar'].contains('rohaan'));
        assertTrue('expected user @bilal', followsGraph['athar'].contains('bilal'));
    }

    @Test
    public void testGuessFollowsGraphMultipleTweetsBySameAuthor() {
        Tweet tweet1 = Tweet(1, 'hamza', 'nice to finally meet @muzammil and @mustafa', Instant.now());
        Tweet tweet2 = Tweet(2, 'maria', 'great day with @saad and @hafsa', Instant.now());

        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        tweets.add(tweet1);
        tweets.add(tweet2);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        assertTrue('expected user muzammil associated with hamza', followsGraph['hamza'].contains('muzammil'));
        assertTrue('expected user mustafa associated with hamza', followsGraph['hamza'].contains('mustafa'));
        assertTrue('expected user saad associated with maria', followsGraph['maria'].contains('saad'));
        assertTrue('expected user hafsa associated with maria', followsGraph['maria'].contains('hafsa'));
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    @Test
    public void testInfluencersOneUserNoFollowers() {
        Tweet tweet1 = Tweet(1, 'hamza', 'wow! what a batter', Instant.now());
        
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        tweets.add(tweet1);

        List<String> influencers = SocialNetwork.influencers(tweets);

        assertTrue('expected empty list', influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleInfluencer() {
        Tweet tweet1 = Tweet(1, 'hamza', 'wow! @kohli is a great cricketer', Instant.now());

        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        tweets.add(tweet1);

        List<String> influencers = SocialNetwork.influencers(tweets);

        assertTrue('expected Kohli as the only influencer', influencers.size() == 1 && influencers.contains('kohli'));
    }

    @Test
    public void testInfluencersMultipleVaryingFollowers() {
        Tweet tweet1 = Tweet(1, 'hamza', 'wow! @kohli is a great cricketer', Instant.now());
        Tweet tweet2 = Tweet(2, 'ali', 'loved meeting @sarfaraz', Instant.now());
        Tweet tweet3 = Tweet(3, 'wasay', 'just witnessed the greatness of @sarfaraz', Instant.now());

        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        tweets.add(tweet1);
        tweets.add(tweet2);
        tweets.add(tweet3);

        List<String> influencers = SocialNetwork.influencers(tweets);

        assertTrue('expected only two influencers', influencers.size == 2);

        assertTrue('expected sarfaraz as the top influencer', influencers[0] == 'sarfaraz');
        assertTrue('expected kohli as the second influencer', influencers[1] == 'kohli');
    }

    @Test
    public void testInfluencersMultipleSameFollowers() {
        Tweet tweet1 = Tweet(1, 'hamza', 'wow! @kohli is a great cricketer', Instant.now());
        Tweet tweet2 = Tweet(2, 'ali', 'loved meeting @sarfaraz', Instant.now());

        tweets.add(tweet1);
        tweets.add(tweet2);

        List<String> influencers = SocialNetwork.influencers(tweets);

        assertTrue('expected only two influencers', influencers.size == 2);

        assertTrue('expected sarfaraz as an influencer', influencers.contains('sarfaraz'));
        assertTrue('expected kohli as an influencer', influencers.contains('kohli'));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
