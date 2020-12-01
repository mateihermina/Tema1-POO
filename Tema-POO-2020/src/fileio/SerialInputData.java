package fileio;

import entertainment.Season;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

/**
 * Information about a tv show, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class SerialInputData extends ShowInput {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Season> seasons;
    /**
     * how many times has been the movie added to a user's favourite list
     */
    private int favorite = 0;
    /**
     * stores the average rating for the serial
     */
    private  Double rating = 0.0;
    /**
     * stores the number of ratings and the sum of ratings for each season
     */
    private final HashMap<Integer, HashMap<Integer, Double>> ratingsOfSeasons;
    private int totalDuration = 0;
    /**
     * the number of views
     */
    private Integer numberOfViews = 0;
    /**
     * the position in database
     */
    private  int position = 0;
    public SerialInputData(final String title, final ArrayList<String> cast,
                           final ArrayList<String> genres,
                           final int numberOfSeasons, final ArrayList<Season> seasons,
                           final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        ratingsOfSeasons = new HashMap<>();
        for (int i = 1; i <= numberOfSeasons; i++) {
            HashMap<Integer, Double> rate = new HashMap<>();
            rate.put(0, 0.0);
            ratingsOfSeasons.put(i, rate);
        }

    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    /**
     * @param season is the season of the serial
     * @return the number of ratings for season
     */
    public Integer numberOfRatingsForSeason(final Integer season) {
        HashMap<Integer, Double> map = this.ratingsOfSeasons.get(season);
        Set<Integer> numberOfRatings = map.keySet();
        ArrayList<Integer> ratingsNum = new ArrayList<>(numberOfRatings);
        return ratingsNum.get(0);
    }

    /**
     * adds a rating to a season
     * @param season is the season that has been rated
     * @param rate is the rating
     */
    public void addRating(final Integer season, final Double rate) {
        Integer number = numberOfRatingsForSeason(season);
        Double seasonRating = this.ratingsOfSeasons.get(season).get(number);
        this.ratingsOfSeasons.get(season).remove(number, seasonRating);
        seasonRating += rate;
        number++;
        this.ratingsOfSeasons.get(season).put(number, seasonRating);
        this.ratingsOfSeasons.replace(season, this.ratingsOfSeasons.get(season));

    }

    /**
     * calculates the average rating for the serial
     */
    public void setRating() {
        Double averageRate = 0.0;
        int num = 0;
        for (int i = 1; i <= numberOfSeasons; i++) {
            Integer number = numberOfRatingsForSeason(i);
            Double rate = this.ratingsOfSeasons.get(i).get(number);
            if (number > 0) {
                averageRate += rate;
                num += number;
            } else if (number == 0) {
                num++;
            }
        }
        this.rating = averageRate / num;
    }
    public Double getRating() {
        return rating;
    }

    /**
     * Calculates the duration of a serial
     */
    public void setTotalDuration() {
        for (Season season : seasons) {
            this.totalDuration += season.getDuration();
        }
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    /**
     * sets the number of views to 0
     */
    public void setNumberOfViews() {
        this.numberOfViews = 0;
    }

    @Override
    public String toString() {
        return "SerialInputData{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
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
     * Increases the number of views
     * @param views is the number of views that need to be added
     */
    public void addNumberOfViews(final Integer views) {
        this.numberOfViews += views;
    }
    public Integer getNumberOfViews() {
        return numberOfViews;
    }
    public void setPosition(final int position) {
        this.position = position;
    }
    public int getPosition() {
        return this.position;
    }

    /**
     * resets the rating
     */
    public void resetRating() {
        this.rating = 0.0;
        for (int i = 1; i <= numberOfSeasons; i++) {
            HashMap<Integer, Double> rate = new HashMap<>();
            rate.put(0, 0.0);
            ratingsOfSeasons.replace(i, rate);
        }
    }

}

class FavoriteDescendingSortSerial implements Comparator<SerialInputData> {
    @Override
    public int compare(final SerialInputData serial1, final SerialInputData serial2) {
        if (serial1.getFavorite() < serial2.getFavorite()) {
            return 1;
        } else if (serial1.getFavorite() > serial2.getFavorite()) {
            return -1;
        } else {
            return serial1.getTitle().compareTo(serial2.getTitle());
        }
    }
}
class FavoriteAscendingSortSerial implements Comparator<SerialInputData> {
    @Override
    public int compare(final SerialInputData serial1, final SerialInputData serial2) {
        if (serial1.getFavorite() < serial2.getFavorite()) {
            return -1;
        } else if (serial1.getFavorite() > serial2.getFavorite()) {
            return 1;
        } else {
            return serial1.getTitle().compareTo(serial2.getTitle());
        }
    }
}

class RatingAscendingSortSerial implements Comparator<SerialInputData> {
    @Override
    public int compare(final SerialInputData serial1, final SerialInputData serial2) {
        if (serial1.getRating() < serial2.getRating()) {
            return -1;
        } else if (serial1.getRating() > serial2.getRating()) {
            return 1;
        } else {
            return serial1.getTitle().compareTo(serial2.getTitle());
        }
    }
}

class RatingDescendingSortSerial implements Comparator<SerialInputData> {
    @Override
    public int compare(final SerialInputData serial1, final SerialInputData serial2) {
        if (serial1.getRating() < serial2.getRating()) {
            return 1;
        } else if (serial1.getRating() > serial2.getRating()) {
            return -1;
        } else {
            return serial1.getTitle().compareTo(serial2.getTitle());
        }
    }
}

class DurationAscendingSortSerial implements Comparator<SerialInputData> {
    @Override
    public int compare(final SerialInputData serial1, final SerialInputData serial2) {
        if (serial1.getTotalDuration() < serial2.getTotalDuration()) {
            return -1;
        } else if (serial1.getTotalDuration() > serial2.getTotalDuration()) {
            return 1;
        } else {
            return serial1.getTitle().compareTo(serial2.getTitle());
        }
    }
}

class DurationDescendingSortSerial implements Comparator<SerialInputData> {
    @Override
    public int compare(final SerialInputData serial1, final SerialInputData serial2) {
        if (serial1.getTotalDuration() < serial2.getTotalDuration()) {
            return 1;
        } else if (serial1.getTotalDuration() > serial2.getTotalDuration()) {
            return -1;
        } else {
            return serial1.getTitle().compareTo(serial2.getTitle());
        }
    }
}

class ViewAscendingSortSerial implements Comparator<SerialInputData> {
    @Override
    public int compare(final SerialInputData serial1, final SerialInputData serial2) {
        if (serial1.getNumberOfViews() < serial2.getNumberOfViews()) {
            return -1;
        } else if (serial1.getNumberOfViews() > serial2.getNumberOfViews()) {
            return 1;
        } else {
            return serial1.getTitle().compareTo(serial2.getTitle());
        }
    }
}

