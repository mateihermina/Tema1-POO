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
     * stores the number of ratings and the ratings for each season
     */
    private HashMap<Integer, HashMap<Integer, Double>> ratingsOfSeasons;
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
        ratingsOfSeasons = new HashMap<Integer, HashMap<Integer, Double>>();
        for (int i = 1; i <= numberOfSeasons; i++) {
            HashMap<Integer, Double> rate = new HashMap<Integer, Double>();
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
     * adds a rating to a season
     * @param season is the season that has been rated
     * @param rate is the rating
     */
    public void addRating(final Integer season, final Double rate) {
        HashMap<Integer, Double> map = this.ratingsOfSeasons.get(season);
        Set<Integer> numberOfRatings = map.keySet();
        ArrayList<Integer> ratingsNum = new ArrayList<Integer>(numberOfRatings);
        Integer number = ratingsNum.get(0);
        Double seasonRating = map.get(number);
        map.remove(number, seasonRating);
        seasonRating += rate;
        number++;
        map.put(number, seasonRating);
        this.ratingsOfSeasons.replace(season, map);

    }

    /**
     * calculates the average rating for the serial
     */
    public void setRating() {
        Double averageRate = 0.0;
        Integer num = 0;
        for (int i = 1; i <= numberOfSeasons; i++) {
            HashMap<Integer, Double> seasonRate = this.ratingsOfSeasons.get(i);
            Set<Integer> key = seasonRate.keySet();
            ArrayList<Integer> ratingNum = new ArrayList<Integer>(key);
            Integer number = ratingNum.get(0);
            Double rate = seasonRate.get(number);
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
        for (int i = 0; i < this.seasons.size(); i++) {
            this.totalDuration += this.seasons.get(i).getDuration();
        }
    }

    public int getTotalDuration() {
        return totalDuration;
    }
    public void setNumberOfViews(final Integer views) {
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
            HashMap<Integer, Double> rate = new HashMap<Integer, Double>();
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
            if (serial1.getTitle().compareTo(serial2.getTitle()) < 0) {
                return -1;
            } else {
                return 1;
            }
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
            if (serial1.getTitle().compareTo(serial2.getTitle()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}

class DurationAscendingSortserial implements Comparator<SerialInputData> {
    @Override
    public int compare(final SerialInputData serial1, final SerialInputData serial2) {
        if (serial1.getTotalDuration() < serial2.getTotalDuration()) {
            return -1;
        } else if (serial1.getTotalDuration() > serial2.getTotalDuration()) {
            return 1;
        } else {
            if (serial1.getTitle().compareTo(serial2.getTitle()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}

class DurationDescendingSortserial implements Comparator<SerialInputData> {
    @Override
    public int compare(final SerialInputData serial1, final SerialInputData serial2) {
        if (serial1.getTotalDuration() < serial2.getTotalDuration()) {
            return 1;
        } else if (serial1.getTotalDuration() > serial2.getTotalDuration()) {
            return -1;
        } else {
            if (serial1.getTitle().compareTo(serial2.getTitle()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}

class ViewAscendingSortserial implements Comparator<SerialInputData> {
    @Override
    public int compare(final SerialInputData serial1, final SerialInputData serial2) {
        if (serial1.getNumberOfViews() < serial2.getNumberOfViews()) {
            return -1;
        } else if (serial1.getNumberOfViews() > serial2.getNumberOfViews()) {
            return 1;
        } else {
            if (serial1.getTitle().compareTo(serial2.getTitle()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}

