package fileio;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private final Map<ActorsAwards, Integer> awards;
    /**
     * holds the sum of the ratings of movies and serials in which the actor has played
     */
    private Double sumOfRatings;
    /**
     * holds the average rating for an actor
     */
    private Double averageRating;
    /**
     * total number of ratings
     */
    private int ratingsNumber;
    /**
     * total number of awards
     */
    private Integer awardsNumber;

    public ActorInputData(final String name, final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
        sumOfRatings = 0.0;
        this.ratingsNumber = 0;
        this.averageRating = 0.0;
        this.awardsNumber = 0;
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

    /**
     * adds a rating to an actor
     * @param rating is the rating to be added
     */
    public void addRating(final Double rating) {
       this.ratingsNumber++;
       this.sumOfRatings += rating;
    }

    /**
     * calculates the average rating of an actor after adding all ratings for videos
     */
    public void setAverageRating() {
        if (this.ratingsNumber > 0) {
            this.averageRating = this.sumOfRatings / this.ratingsNumber;
        } else {
            this.averageRating = 0.0;
        }
    }

    /**
     * resets the sumOfRatings, averageRating and ratingsNumber to 0
     */
    public void resetRating() {
        this.averageRating = 0.0;
        this.sumOfRatings = 0.0;
        this.ratingsNumber = 0;
    }

    public Double getAverageRating() {
        return averageRating;
    }
    /**
     * calculates the awards number for an actor
     */
    public void calculateAwardsNumber() {
       Set<ActorsAwards> keys = awards.keySet();
        this.awardsNumber = 0;
        for (ActorsAwards key : keys) {
            this.awardsNumber += awards.get(key);
        }
    }

    public Integer getAwardsNumber() {
        return awardsNumber;
    }

    /**
     * @param words is the list of words that need to be searched for
     * @return true if the all words are found in the actor's career description; otherwise false is
     * returned
     */
    public Boolean hasKeywords(final List<String> words) {
        HashMap<String, Integer> map = new HashMap<>();
        String[] listOfWords = getCareerDescription().split("\\W");
        for (String word : listOfWords) {
                map.putIfAbsent(word.toLowerCase(), 1);
        }

        for (String word : words) {
            String lowerCaseWord = word.toLowerCase();
            if (map.get(lowerCaseWord) == null) {
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
            return actor1.getName().compareTo(actor2.getName());
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
            return actor1.getName().compareTo(actor2.getName());
        }
    }
}
