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
    private int favorite = 0;
    private Double rating = 0.0;
    private Integer ratingsNumber = 0;
    private Integer numberOfViews = 0;
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
     * increases favorite value
     **/
    public void markFavorite() {
        this.favorite++;
    }

    /**
     * adds a rating of a movie
     */
    public void addRating(final Double rate) {
        int ratings = this.getRatingsNumber();
        ratings++;
        this.setRatingsNumber(ratings);
        this.rating += rate;
    }
    /**
     * sets the rating of a movie
     */
    public void setRating() {

        if (this.getRatingsNumber() > 0) {
            this.rating = this.getRating() / this.getRatingsNumber();
        } else {
            this.rating = 0.0;
        }
    }

    /**
     * increases the number of views of the movie by viwes
     */
    public void addNumberOfViews(final Integer views) {
        this.numberOfViews += views;
    }
    public Integer getNumberOfViews() {
        return numberOfViews;
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
class FavoriteDesscendingSortMovie implements Comparator<MovieInputData> {
    @Override
    public int compare(final MovieInputData movie1, final MovieInputData movie2) {
        if (movie1.getFavorite() < movie2.getFavorite()) {
            return 1;
        } else if (movie1.getFavorite() > movie2.getFavorite()) {
            return -1;
        } else {
            return movie1.getTitle().compareTo(movie2.getTitle());
        }
    }
}
class RatingAscendingSortMovie implements Comparator<MovieInputData> {

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
class DurationDescendingSortMovie implements Comparator<MovieInputData> {

    public int compare(final MovieInputData movie1, final MovieInputData movie2) {
        if (movie1.getDuration() < movie2.getDuration()) {
            return 1;
        } else if (movie1.getDuration() > movie2.getDuration()) {
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
class ViewsAscendingSortMovie implements Comparator<MovieInputData> {

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
class ViewsDescendingSortMovie implements Comparator<MovieInputData> {

    public int compare(final MovieInputData movie1, final MovieInputData movie2) {
        if (movie1.getNumberOfViews() < movie2.getNumberOfViews()) {
            return 1;
        } else if (movie1.getNumberOfViews() > movie2.getNumberOfViews()) {
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
