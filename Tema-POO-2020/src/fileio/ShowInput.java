package fileio;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * General information about show (video), retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public abstract class ShowInput {
    /**
     * Show's title
     */
    private final String title;
    /**
     * The year the show was released
     */
    private final int year;
    /**
     * Show casting
     */
    private final ArrayList<String> cast;
    /**
     * Show genres
     */
    private final ArrayList<String> genres;

    public ShowInput(final String title, final int year,
                     final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    /**
     * marks a video as favorite
     **/
    public abstract void markFavorite();

    /**
     * gets the favorite number
     */
    public abstract int getFavorite();
    /**
     * gets the rating for a video
     **/
    public abstract Double getRating();
    /**
     * gets the position in data base
     **/
    public abstract int getPosition();

    /**
     * gets the number of viwes for a video
     */
    public abstract Integer getNumberOfViews();

}
class RatingDescendingSort implements Comparator<ShowInput> {

    public int compare(final ShowInput video1, final ShowInput video2) {
        if (video1.getRating() < video2.getRating()) {
            return 1;
        } else if (video1.getRating() > video2.getRating()) {
            return -1;
        } else {
            if (video1.getPosition() < video2.getPosition()) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
class RatingAscendingSort implements Comparator<ShowInput> {

    public int compare(final ShowInput video1, final ShowInput video2) {
        if (video1.getRating() < video2.getRating()) {
            return -1;
        } else if (video1.getRating() > video2.getRating()) {
            return 1;
        } else {
            return video1.getTitle().compareTo(video2.getTitle());
        }
    }
}
class FavoriteDescendingSort implements Comparator<ShowInput> {

    public int compare(final ShowInput video1, final ShowInput video2) {
        if (video1.getFavorite() < video2.getFavorite()) {
            return 1;
        } else if (video1.getFavorite() > video2.getFavorite()) {
            return -1;
        } else {
            if (video1.getPosition() < video2.getPosition()) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
