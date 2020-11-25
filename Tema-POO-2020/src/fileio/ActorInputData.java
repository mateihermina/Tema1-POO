package fileio;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Information about an actor, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class ActorInputData {
    /**
     * actor name
     */
    private String name;
    /**
     * description of the actor's career
     */
    private String careerDescription;
    /**
     * videos starring actor
     */
    private ArrayList<String> filmography;
    /**
     * awards won by the actor
     */
    private Map<ActorsAwards, Integer> awards;
    /**
     * avrage rating of movies ans serials
     */
    private Double averageRating = 0.0;
    /**
     * total number of ratings
     */
    private int ratingsNumber = 0;

    private Integer awardsNumber = 0;

    public ActorInputData(final String name, final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    @Override
    public String toString() {
        return "ActorInputData{"
                + "name='" + name + '\''
                + ", careerDescription='"
                + careerDescription + '\''
                + ", filmography=" + filmography + '}';
    }

    public void setRatingsNumber(final int ratingsNumber) {
        this.ratingsNumber = ratingsNumber;
    }

    public int getRatingsNumber() {
        return  this.ratingsNumber;
    }

    /**
     * adds a rating of a video
     */
    public void addRating(final Double rating) {
        int ratings = this.getRatingsNumber();
        ratings++;
        this.setRatingsNumber(ratings);
        this.averageRating += rating;
    }
    /**
     * sets the avrage rating of an actor after adding all ratings for videos
     */
    public void setAverageRating() {

        if (this.getRatingsNumber() > 0) {
            this.averageRating = this.getAverageRating() / this.getRatingsNumber();
        } else {
            this.averageRating = 0.0;
        }
    }
    public Double getAverageRating() {
        return averageRating;
    }
    /**
     * calcultes the awards number for an actor
     */
    public void calculateAwardsNumber(final List<String> awardsList) {
        for (int i = 0; i < awardsList.size(); i++) {
            if (ActorsAwards.valueOf(awardsList.get(i)) != null) {
                Integer number = awards.get(ActorsAwards.valueOf(awardsList.get(i)));
                awardsNumber += number;
            }
        }
    }

    public Integer getAwardsNumber() {
        return awardsNumber;
    }
    /**
     * returns true if all strings from words are in actor's career description
     */
    public Boolean hasKeywords(final List<String> words) {
        for (int i = 0; i < words.size(); i++) {
            String lowerCaseWord = words.get(i).toLowerCase();
            if (!this.getCareerDescription().toLowerCase().contains(lowerCaseWord)) {
                return false;
            }
        }
        return true;
    }

}

class AverageAscendingSortActor implements Comparator<ActorInputData> {

    public int compare(final ActorInputData actor1, final ActorInputData actor2) {
        if (actor1.getAverageRating() < actor2.getAverageRating()) {
            return -1;
        } else if (actor1.getAverageRating() > actor2.getAverageRating()) {
            return 1;
        } else {
            if (actor1.getName().compareTo(actor2.getName()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
class AverageDescendingSortActor implements Comparator<ActorInputData> {

    public int compare(final ActorInputData actor1, final ActorInputData actor2) {
        if (actor1.getAverageRating() < actor2.getAverageRating()) {
            return 1;
        } else if (actor1.getAverageRating() > actor2.getAverageRating()) {
            return -1;
        } else {
            if (actor1.getName().compareTo(actor2.getName()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
class AwardsDescendingSort implements Comparator<ActorInputData> {

    public int compare(final ActorInputData actor1, final ActorInputData actor2) {
        if (actor1.getAwardsNumber() < actor2.getAwardsNumber()) {
            return 1;
        } else if (actor1.getAwardsNumber() > actor2.getAwardsNumber()) {
            return -1;
        } else {
            if (actor1.getName().compareTo(actor2.getName()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
class AwardsAscendingSort implements Comparator<ActorInputData> {

    public int compare(final ActorInputData actor1, final ActorInputData actor2) {
        if (actor1.getAwardsNumber() < actor2.getAwardsNumber()) {
            return -1;
        } else if (actor1.getAwardsNumber() > actor2.getAwardsNumber()) {
            return 1;
        } else {
            if (actor1.getName().compareTo(actor2.getName()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
