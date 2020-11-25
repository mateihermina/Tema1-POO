package fileio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Information about an user, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class UserInputData {
    /**
     * User's username
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * The history of the movies seen
     */
    private final Map<String, Integer> history;
    /**
     * Movies added to favorites
     */
    private final ArrayList<String> favoriteMovies;
    /**
     * The ratings for the serials seen and rated
     */
    private Map<String, HashMap<Integer, Double>> ratedSerials =
                                                new HashMap<String, HashMap<Integer, Double>>();
    /**
     * The ratings for the movies seen and rated
     */
    private Map<String, Double> ratedMovies = new HashMap<String, Double>();
    private Integer numberofRatings = 0;

    public UserInputData(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteMovies = favoriteMovies;
        this.history = history;
    }

    public String getUsername() {

        return username;
    }

    public Map<String, Integer> getHistory() {

        return history;
    }

    public String getSubscriptionType() {

        return subscriptionType;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public Map<String, HashMap<Integer, Double>> getRatedSerials() {
        return ratedSerials;
    }

    public Map<String, Double> getRatedMovies() {
        return ratedMovies;
    }

    @Override
    public String toString() {
        return "UserInputData{" + "username='"
                + username + '\'' + ", subscriptionType='"
                + subscriptionType + '\'' + ", history="
                + history + ", favoriteMovies="
                + favoriteMovies + '}';
    }

    /**
     * Adds a movie to favorites
     */
    public String addFavorite(final String movie) {
        String res = new String("");
        if (this.history.get(movie) == null) {
            res += "error -> ";
            res += movie;
            res += " is not seen";
            return res;
        } else {
            Integer seen = this.history.get(movie);
            if (seen > 0) {
                for (int i = 0; i < favoriteMovies.size(); i++) {
                    if (favoriteMovies.get(i).equals(movie)) {
                        res += "error -> ";
                        res += movie;
                        res += " is already in favourite list";
                        return res;
                    }
                }
                this.favoriteMovies.add(movie);
                res += "success -> ";
                res += movie;
                res += " was added as favourite";
            }
        }

        return  res;
    }

    /**
     * Marks a video as viewd
     * If video was already viewd, the number of views is incremented
     * */
    public String viewShow(final String title) {
        String message = new String("");
        if (this.history.get(title) == null) {
            this.history.put(title, 1);
            Integer views = 1;
            message += "success -> " + title + " was viewed with total views of "
                    + views.toString();
        } else {
            Integer views = this.history.get(title);
            views++;
            this.history.replace(title, views);
            message += "success -> " + title + " was viewed with total views of "
                    + views.toString();
        }

        return message;
    }

    /**
     * Rates a movie or a serial that hase been viwed
     * */
    public String addRating(final String title, final int seasonNumber, final Double rating) {
        String message = new String("");
        if (history.get(title) != null) {
            /** video marked as viewed **/

            if (seasonNumber == 0) {
                /** movie **/
                if (ratedMovies.get(title) == null) {
                    ratedMovies.put(title, rating);
                    message += "success -> " + title + " was rated with "
                            + rating.toString() + " by " + username;
                } else {
                    message += "error -> " + title + " has been already rated";
                }

            } else { /** serial **/
                if (ratedSerials.get(title) != null) {
                    if (ratedSerials.get(title).get(seasonNumber) == null) {
                        ratedSerials.get(title).put(seasonNumber, rating);
                        message += "success -> " + title + " was rated with "
                                + rating.toString() + " by " + username;
                    } else {
                        message += "error -> " + title +  " has been already rated";
                    }

                } else {
                    HashMap<Integer, Double> seasonsRating = new HashMap<Integer, Double>();
                    seasonsRating.put(seasonNumber, rating);
                    ratedSerials.put(title, seasonsRating);
                    message += "success -> " + title + " was rated with "
                            + rating.toString() + " by " + username;
                }

                }

            } else {
            message += "error -> " + title +  " is not seen";
        }
        return message;
    }

    public void setNumberofRatings() {
        Integer number = 0;
        number += this.ratedMovies.size();
        Set<String> keys = this.ratedSerials.keySet();
        for (String key : keys) {
            number += this.ratedSerials.get(key).size();
        }
        this.numberofRatings = number;

    }

    public Integer getNumberOfRatings() {
        return this.numberofRatings;
    }
}
class RatingsAscendingSortUser implements Comparator<UserInputData> {

    public int compare(final UserInputData user1, final UserInputData user2) {
        if (user1.getNumberOfRatings() < user2.getNumberOfRatings()) {
            return -1;
        } else if (user1.getNumberOfRatings() > user2.getNumberOfRatings()) {
            return 1;
        } else {
            if (user1.getUsername().compareTo(user2.getUsername()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
class RatingsDescendingSortUser implements Comparator<UserInputData> {

    public int compare(final UserInputData user1, final UserInputData user2) {
        if (user1.getNumberOfRatings() < user2.getNumberOfRatings()) {
            return 1;
        } else if (user1.getNumberOfRatings() > user2.getNumberOfRatings()) {
            return -1;
        } else {
            if (user1.getUsername().compareTo(user2.getUsername()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
