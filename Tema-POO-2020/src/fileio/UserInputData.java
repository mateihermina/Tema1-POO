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
    private final Map<String, HashMap<Integer, Double>> ratedSerials = new HashMap<>();
    /**
     * The ratings for the movies seen and rated
     */
    private final Map<String, Double> ratedMovies = new HashMap<>();
    /**
     * the number of ratings
     */
    private Integer numberOfRatings = 0;

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
     * adds a movie to favorite list
     * @param movie is the movie to be added to favorite list
     * @return the result for favorite command
     */
    public String addFavorite(final String movie) {
        String res = "";
        if (this.history.get(movie) == null) {
            res += "error -> ";
            res += movie;
            res += " is not seen";
            return res;
        } else {
            Integer seen = this.history.get(movie);
            if (seen > 0) {
                for (String favoriteMovie : favoriteMovies) {
                    if (favoriteMovie.equals(movie)) {
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
     * adds a video to history
     * @param title is the title of the show viewed
     * @return the result for view command
     */
    public String viewShow(final String title) {
        String message = "";
        if (this.history.get(title) == null) {
            this.history.put(title, 1);
            int views = 1;
            message += "success -> " + title + " was viewed with total views of "
                    + views;
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
     * Rates a movie or a serial that hase been viewed
     * @param title is the title of the video
     * @param seasonNumber is the number of the season of the video if the video is a serial (is 0
     * for movies)
     * @param rating is the rating of the video
     * @return the result for rating command
     */
    public String addRating(final String title, final int seasonNumber, final Double rating) {
        String message = "";
        if (history.get(title) != null) {
            /*video marked as viewed */

            if (seasonNumber == 0) {
                /* movie */
                if (ratedMovies.get(title) == null) {
                    ratedMovies.put(title, rating);
                    message += "success -> " + title + " was rated with "
                            + rating.toString() + " by " + username;
                } else {
                    message += "error -> " + title + " has been already rated";
                }

            } else { /* serial */
                if (ratedSerials.get(title) != null) {
                    if (ratedSerials.get(title).get(seasonNumber) == null) {
                        ratedSerials.get(title).put(seasonNumber, rating);
                        message += "success -> " + title + " was rated with "
                                + rating.toString() + " by " + username;
                    } else {
                        message += "error -> " + title +  " has been already rated";
                    }

                } else {
                    HashMap<Integer, Double> seasonsRating = new HashMap<>();
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

    /**
     * Calculates the number of ratings that a user has given to movies and serials
     */
    public void setNumberOfRatings() {
        int number = 0;
        number += this.ratedMovies.size();
        Set<String> keys = this.ratedSerials.keySet();
        for (String key : keys) {
            number += this.ratedSerials.get(key).size();
        }
        this.numberOfRatings = number;

    }

    public Integer getNumberOfRatings() {
        return this.numberOfRatings;
    }
}
class RatingsAscendingSortUser implements Comparator<UserInputData> {

    public int compare(final UserInputData user1, final UserInputData user2) {
        if (user1.getNumberOfRatings() < user2.getNumberOfRatings()) {
            return -1;
        } else if (user1.getNumberOfRatings() > user2.getNumberOfRatings()) {
            return 1;
        } else {
            return user1.getUsername().compareTo(user2.getUsername());
        }
    }
}


