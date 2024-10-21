/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
        public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        // Create a map to store the social network where the key is the author, and the value is a set of users they follow
        Map<String, Set<String>> followsGraph = new HashMap<>();

        // Pattern to extract @mentions (case-insensitive)
        Pattern mentionPattern = Pattern.compile("@(\\w+)", Pattern.CASE_INSENSITIVE);

        // Iterate through each tweet to find mentions
        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase();  // Get the author's username in lowercase
            String tweetText = tweet.getText();  // Get the text of the tweet
            
            // Create a matcher to find @mentions in the tweet text
            Matcher matcher = mentionPattern.matcher(tweetText);
            
            // Set to hold unique mentioned users
            Set<String> mentionedUsers = new HashSet<>();

            // Find all the mentions in the tweet text
            while (matcher.find()) {
                String mentionedUser = matcher.group(1).toLowerCase();  // Extract the mentioned username in lowercase
                mentionedUsers.add(mentionedUser);  // Add it to the set of mentioned users
            }

            // If there are mentioned users, update the follows graph
            if (!mentionedUsers.isEmpty()) {
                // Get the set of people the author follows, if it exists
                Set<String> follows = followsGraph.getOrDefault(author, new HashSet<>());

                // Add each mentioned user to the author's follow list
                follows.addAll(mentionedUsers);

                // Put the updated set of follows back into the graph
                followsGraph.put(author, follows);
            }
        }

        // Return the constructed follows graph
        return followsGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
         // Step 1: Initialize a map to store the follower count for each user
        Map<String, Integer> followerCounts = new HashMap<>();
        
        // Step 2: Iterate over the followsGraph to count the followers for each user
        for (Map.Entry<String, Set<String>> entry : followsGraph.entrySet()) {
            String follower = entry.getKey();
            Set<String> following = entry.getValue();
            
            // Each person in 'following' set is a follower of 'follower'
            for (String followedUser : following) {
                // Update the follower count for 'followedUser'
                followerCounts.put(followedUser, followerCounts.getOrDefault(followedUser, 0) + 1);
            }
        }

        // Step 3: Sort the users based on follower count in descending order
        List<String> sortedInfluencers = followerCounts.entrySet()
            .stream()
            .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        // Step 4: Return the sorted list of influencers
        return sortedInfluencers;
    }

}
