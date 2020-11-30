package fileio;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Information about a movie, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput {
    /**
     * Duration in minutes of a season
     */
    private final int duration;
    /**
     * how many times has been the movie added to a user's favourite list
     */
    private int favorite = 0;
    /**
     * the rating for the movie
     * at first, the ratings added by users sum up to rating, so it stores the sum of all ratings
     * after setRating is applied, rating stores the average rating for the movie
     */
    private Double rating = 0.0;
    /**
     * the number of ratings for the movie
     */
    private Integer ratingsNumber = 0;
    /**
     * the number of views for the movies
     */
    private Integer numberOfViews = 0;
    /**
     * the position of the movie in database
     */
    private  int position = 0;

    public MovieInputData(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n ";
    }

    public int getFavorite() {
        return this.favorite;
    }

    /**
     * increases favorite value => marks the video as favorite
     **/
    public void markFavorite() {
        this.favorite++;
    }

    /**
     * adds a rating of a movie
     * @param rate is the rating to be added
     * sum up the rate to rating and increases the number of ratings
     */
    public void addRating(final Double rate) {
        int ratings = this.getRatingsNumber();
        ratings++;
        this.setRatingsNumber(ratings);
        this.rating += rate;
    }

    /**
     * calculates the average rating of a movie and stores it in rating
     */
    public void setRating() {

        if (this.getRatingsNumber() > 0) {
            this.rating = this.getRating() / this.getRatingsNumber();
        } else {
            this.rating = 0.0;
        }
    }

    /**
     * resets the rating
     */
    public void resetRating() {
        this.rating = 0.0;
        this.ratingsNumber = 0;
    }

    /**
     * increases the number of views of the movie by views
     */
    public void addNumberOfViews(final Integer views) {
        this.numberOfViews += views;
    }

    public Integer getNumberOfViews() {
        return numberOfViews;
    }
    public void setNumberOfViews(final Integer views) {
        this.numberOfViews = 0;
    }
    public Double getRating() {
        return this.rating;
    }
    public Integer getRatingsNumber() {
        return  this.ratingsNumber;
    }
    public void setRatingsNumber(final Integer number) {
        this.ratingsNumber = number;
    }
    public void setPosition(final int position) {
        this.position = position;
    }
    public int getPosition() {
        return this.position;
    }

}

class FavoriteAscendingSortMovie implements Comparator<MovieInputData> {
    @Override
    public int compare(final MovieInputData movie1, final MovieInputData movie2) {
        if (movie1.getFavorite() < movie2.getFavorite()) {
            return -1;
        } else if (movie1.getFavorite() > movie2.getFavorite()) {
            return 1;
        } else {
            return movie1.getTitle().compareTo(movie2.getTitle());
        }
    }
}

class RatingAscendingSortMovie implements Comparator<MovieInputData> {
    @Override
    public int compare(final MovieInputData movie1, final MovieInputData movie2) {
        if (movie1.getRating() < movie2.getRating()) {
            return -1;
        } else if (movie1.getRating() > movie2.getRating()) {
            return 1;
        } else {
            if (movie1.getTitle().compareTo(movie2.getTitle()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
class RatingDescendingSortMovie implements Comparator<MovieInputData> {
    @Override
    public int compare(final MovieInputData movie1, final MovieInputData movie2) {
        if (movie1.getRating() < movie2.getRating()) {
            return 1;
        } else if (movie1.getRating() > movie2.getRating()) {
            return -1;
        } else {
            if (movie1.getTitle().compareTo(movie2.getTitle()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}

class DurationAscendingSortMovie implements Comparator<MovieInputData> {
    @Override
    public int compare(final MovieInputData movie1, final MovieInputData movie2) {
        if (movie1.getDuration() < movie2.getDuration()) {
            return -1;
        } else if (movie1.getDuration() > movie2.getDuration()) {
            return 1;
        } else {
            if (movie1.getTitle().compareTo(movie2.getTitle()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}

class ViewsAscendingSortMovie implements Comparator<MovieInputData> {
    @Override
    public int compare(final MovieInputData movie1, final MovieInputData movie2) {
        if (movie1.getNumberOfViews() < movie2.getNumberOfViews()) {
            return -1;
        } else if (movie1.getNumberOfViews() > movie2.getNumberOfViews()) {
            return 1;
        } else {
            if (movie1.getTitle().compareTo(movie2.getTitle()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}

