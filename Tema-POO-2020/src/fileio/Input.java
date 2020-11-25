package fileio;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The class contains information about input
 * <p>
 * DO NOT MODIFY
 */
public final class Input {
    /**
     * List of actors
     */
    private final List<ActorInputData> actorsData;
    /**
     * List of users
     */
    private final List<UserInputData> usersData;
    /**
     * List of commands
     */
    private final List<ActionInputData> commandsData;
    /**
     * List of movies
     */
    private final List<MovieInputData> moviesData;
    /**
     * List of serials aka tv shows
     */
    private final List<SerialInputData> serialsData;

    public Input() {
        this.actorsData = null;
        this.usersData = null;
        this.commandsData = null;
        this.moviesData = null;
        this.serialsData = null;
    }

    public Input(final List<ActorInputData> actors, final List<UserInputData> users,
                 final List<ActionInputData> commands,
                 final List<MovieInputData> movies,
                 final List<SerialInputData> serials) {
        this.actorsData = actors;
        this.usersData = users;
        this.commandsData = commands;
        this.moviesData = movies;
        this.serialsData = serials;
    }

    public List<ActorInputData> getActors() {
        return actorsData;
    }

    public List<UserInputData> getUsers() {
        return usersData;
    }

    public List<ActionInputData> getCommands() {
        return commandsData;
    }

    public List<MovieInputData> getMovies() {
        return moviesData;
    }

    public List<SerialInputData> getSerials() {
        return serialsData;
    }

    /**
     * returns the number of seasons of the serial named title
     */
    public int getSeasonsNumber(final String title) {
        int seasonsNumber = 0;
        for (int t = 0; t < serialsData.size(); t++) {
            if (serialsData.get(t).getTitle().equals(title)) {
                seasonsNumber = serialsData.get(t).getNumberSeason();
                break;
            }
        }
        return seasonsNumber;
    }

    /**
     * marks favorite shows
     */
    public void markFavoriteShows() {
        for (int i = 0; i < usersData.size(); i++) {
            for (int j = 0; j < usersData.get(i).getFavoriteMovies().size(); j++) {
                String title = usersData.get(i).getFavoriteMovies().get(j);
                for (int k = 0; k < moviesData.size(); k++) {
                    if (moviesData.get(k).getTitle().equals(title)) {
                        moviesData.get(k).markFavorite();
                        break;
                    }
                }
                for (int k = 0; k < serialsData.size(); k++) {
                    if (serialsData.get(k).getTitle().equals(title)) {
                        serialsData.get(k).markFavorite();
                        break;
                    }
                }
            }
        }
    }

    /**
     * returns the first number actors in sortType order of their avrage rating
     */
    public String sortBestActors(final int number, final String sortType) {
        ArrayList<String> actors = new ArrayList<String>();
        ArrayList<ActorInputData> actorsInputData = new ArrayList<ActorInputData>(actorsData);
        //for each actor in actorsInputData
        for (int i = 0; i < actorsInputData.size(); i++) {
            ActorInputData actor = actorsInputData.get(i);
            //for each film in filmography
            for (int j = 0; j < actor.getFilmography().size(); j++) {
                String title = actor.getFilmography().get(j);
                // for each user
                for (int k = 0; k < usersData.size(); k++) {
                    UserInputData user = usersData.get(k);
                    if (user.getRatedMovies().get(title) != null) {
                        actor.addRating(user.getRatedMovies().get(title));
                    }
                    if (user.getRatedSerials().get(title) != null) {
                        int seasonsNumber = getSeasonsNumber(title);
                        HashMap<Integer, Double> ratings = user.getRatedSerials().get(title);
                        Set<Integer> seasons = ratings.keySet();
                        for (Integer season : seasons) {
                            actor.addRating(ratings.get(season));
                        }
                        for (int t = 0; t < seasonsNumber - seasons.size(); t++) {
                            actor.addRating(0.0);
                        }
                    }
                }
            }
            actor.setAverageRating();
        }
        if (sortType.equals("asc")) {
            Collections.sort(actorsInputData, new AverageAscendingSortActor());
        } else {
            Collections.sort(actorsInputData, new AverageDescendingSortActor());
        }
        int count = 0;
        for (int i = 0; i < actorsInputData.size(); i++) {
            if (actorsInputData.get(i).getAverageRating() > 0) {
                actors.add(actorsInputData.get(i).getName());
                count += 1;
                if (count == number) {
                    break;
                }
            }

        }
        String message = "Query result: " + actors.toString();
        return message;
    }

    /**
     * gets the result for favorite movie query
     */
    public String mostFavoriteMovies(final String year, final List<String> generes,
                                     final String sortType, final int number) {
        Boolean found = false;
        ArrayList<MovieInputData> movies = new ArrayList<MovieInputData>();
        for (int i = 0; i < moviesData.size(); i++) {
            Integer yearNumber = new Integer(moviesData.get(i).getYear());
            if (yearNumber.toString().equals(year)) {
                List<String> intersectElements = moviesData.get(i).getGenres().stream()
                        .filter(generes::contains)
                        .collect(Collectors.toList());
                if (generes.equals(intersectElements)) {
                    movies.add(moviesData.get(i));
                }
            }
        }
        if (sortType.equals("asc")) {
            Collections.sort(movies, new FavoriteAscendingSortMovie());
        } else {
            Collections.sort(movies, new FavoriteDesscendingSortMovie());
        }
        ArrayList<String> moviesResult = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getFavorite() > 0) {
                moviesResult.add(movies.get(i).getTitle());
                count += 1;
                if (count == number) {
                    break;
                }
            }
        }
        String message = "Query result: " + moviesResult.toString();
        return message;

    }

    /**
     * gets the result for favorite show query
     */
    public String mostFavoriteShow(final String year, final List<String> generes,
                                   final String sortType, final int number) {
        Boolean found = false;
        ArrayList<SerialInputData> serials = new ArrayList<SerialInputData>();
        for (int i = 0; i < serialsData.size(); i++) {
            Integer yearNumber = new Integer(serialsData.get(i).getYear());
            if (yearNumber.toString().equals(year)) {
                List<String> intersectElements = serialsData.get(i).getGenres().stream()
                        .filter(generes::contains)
                        .collect(Collectors.toList());
                if (generes.equals(intersectElements)) {
                    serials.add(serialsData.get(i));
                }
            }
        }
        if (sortType.equals("asc")) {
            Collections.sort(serials, new FavoriteAscendingSortSerial());
        } else {
            Collections.sort(serials, new FavoriteDescendingSortSerial());
        }
        ArrayList<String> serialsReSult = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < serials.size(); i++) {
            if (serials.get(i).getFavorite() > 0) {
                serialsReSult.add(serials.get(i).getTitle());
                count += 1;
                if (count == number) {
                    break;
                }
            }
        }
        String message = "Query result: " + serialsReSult.toString();
        return message;

    }

    /**
     * gets the actors that have the awards specified in List awards
     */
    public String actorsbyAwards(final List<String> awards, final String sortType) {
        ArrayList<ActorInputData> actors = new ArrayList<ActorInputData>();
        for (int i = 0; i < actorsData.size(); i++) {
            Map<ActorsAwards, Integer> awardsMap = actorsData.get(i).getAwards();
            Set<ActorsAwards> keys = awardsMap.keySet();
            ArrayList<ActorsAwards> keyList = new ArrayList<ActorsAwards>(keys);
            ArrayList<ActorsAwards> inputAwards = new ArrayList<ActorsAwards>();
            for (int j = 0; j < awards.size(); j++) {
                inputAwards.add(ActorsAwards.valueOf(awards.get(j)));
            }
            ArrayList<ActorsAwards> intersectElements = (ArrayList<ActorsAwards>) keyList.stream()
                    .filter(inputAwards::contains)
                    .collect(Collectors.toList());
            if (intersectElements.equals(inputAwards)) {
                actorsData.get(i).calculateAwardsNumber(awards);
                actors.add(actorsData.get(i));

            }

        }
        if (sortType.equals("asc")) {
            Collections.sort(actors, new AwardsAscendingSort());
        } else {
            Collections.sort(actors, new AwardsDescendingSort());
        }
        ArrayList<String> actorNames = new ArrayList<String>();
        for (int i = 0; i < actors.size(); i++) {
            actorNames.add(actors.get(i).getName());
        }

        String message = "Query result: " + actorNames.toString();
        return message;
    }

    /**
     * gets the actors that have the keywords specified in List words in their description
     */
    public String actorsbyKeywords(final List<String> words, final String sortType) {
        ArrayList<String> actors = new ArrayList<String>();
        for (int i = 0; i < actorsData.size(); i++) {
            if (actorsData.get(i).hasKeywords(words)) {
                actors.add(actorsData.get(i).getName());
            }
        }
        if (sortType.equals("asc")) {
            Collections.sort(actors);
        } else {
            Collections.sort(actors, Collections.reverseOrder());
        }

        String message = "Query result: " + actors.toString();
        return message;
    }

    /**
     * gets the best rated movies sorted by sortType
     */
    public String bestRatedMovies(final String year, final List<String> generes,
                                  final String sortType, final int number) {
        //for each movie
        for (int i = 0; i < moviesData.size(); i++) {
            Integer yearNumber = new Integer(moviesData.get(i).getYear());
            if (yearNumber.toString().equals(year)) {
                List<String> intersectElements = moviesData.get(i).getGenres().stream()
                        .filter(generes::contains)
                        .collect(Collectors.toList());
                if (generes.equals(intersectElements)) {

                    String title = moviesData.get(i).getTitle();
                    //search the movie title in the ratings list of each user
                    for (int j = 0; j < usersData.size(); j++) {
                        Map<String, Double> ratedMovies = usersData.get(j).getRatedMovies();
                        if (ratedMovies.get(title) != null) {
                            Double rating = ratedMovies.get(title);
                            moviesData.get(i).addRating(rating);
                        }
                    }
                }
            }
        }
        ArrayList<MovieInputData> movies = new ArrayList<MovieInputData>();
        for (int i = 0; i < moviesData.size(); i++) {
            moviesData.get(i).setRating();
            if (moviesData.get(i).getRating() > 0) {
                movies.add(moviesData.get(i));
            }
        }
        if (sortType.equals("asc")) {
            Collections.sort(movies, new RatingAscendingSortMovie());
        } else {
            Collections.sort(movies, new RatingDescendingSortMovie());
        }
        ArrayList<String> movieTitles = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < movies.size(); i++) {

            movieTitles.add(movies.get(i).getTitle());
            count += 1;
            if (count == number) {
                break;
            }

        }
        String message = "Query result: " + movieTitles.toString();
        return message;

    }

    /**
     * gets the best rated serials sorted by sortType
     */
    public String bestRatedShow(final String year, final List<String> generes,
                                final String sortType, final int number) {
        //for each movie
        for (int i = 0; i < serialsData.size(); i++) {
            Integer yearNumber = new Integer(serialsData.get(i).getYear());
            if (yearNumber.toString().equals(year)) {
                List<String> intersectElements = serialsData.get(i).getGenres().stream()
                        .filter(generes::contains)
                        .collect(Collectors.toList());
                if (generes.equals(intersectElements)) {

                    String title = serialsData.get(i).getTitle();
                    //search the serial title in the ratings list of each user
                    for (int j = 0; j < usersData.size(); j++) {
                        Map<String, HashMap<Integer, Double>> ratedSerials
                                = usersData.get(j).getRatedSerials();
                        if (ratedSerials.get(title) != null) {
                            //Aici e diferiit
                            HashMap<Integer, Double> seasonsRate = ratedSerials.get(title);
                            Set<Integer> keys = seasonsRate.keySet();
                            ArrayList<Integer> ratedSeasons = new ArrayList<Integer>(keys);
                            for (int k = 0; k < ratedSeasons.size(); k++) {
                                Double rating = seasonsRate.get(ratedSeasons.get(k));
                                serialsData.get(i).addRating(ratedSeasons.get(k), rating);
                            }
                        }
                    }
                }
            }
        }
        ArrayList<SerialInputData> serials = new ArrayList<SerialInputData>();
        for (int i = 0; i < serialsData.size(); i++) {
            serialsData.get(i).setRating();
            if (serialsData.get(i).getRating() > 0) {
                serials.add(serialsData.get(i));
            }
        }
        if (sortType.equals("asc")) {
            Collections.sort(serials, new RatingAscendingSortserial());
        } else {
            Collections.sort(serials, new RatingDescendingSortSerial());
        }
        ArrayList<String> serialTitles = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < serials.size(); i++) {

            serialTitles.add(serials.get(i).getTitle());
            count += 1;
            if (count == number) {
                break;
            }

        }
        String message = "Query result: " + serialTitles.toString();
        return message;

    }

    /**
     * gets the longes movies sorted by sortType
     */
    public String longestMovies(final String year, final List<String> generes,
                                final String sortType, final int number) {
        ArrayList<MovieInputData> movies = new ArrayList<MovieInputData>();
        //for each movie
        for (int i = 0; i < moviesData.size(); i++) {
            Integer yearNumber = new Integer(moviesData.get(i).getYear());
            if (yearNumber.toString().equals(year)) {
                List<String> intersectElements = moviesData.get(i).getGenres().stream()
                        .filter(generes::contains)
                        .collect(Collectors.toList());
                if (generes.equals(intersectElements)) {
                    movies.add(moviesData.get(i));

                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(movies, new DurationAscendingSortMovie());
        } else {
            Collections.sort(movies, new DurationDescendingSortMovie());
        }
        ArrayList<String> movieTitles = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < movies.size(); i++) {

            movieTitles.add(movies.get(i).getTitle());
            count += 1;
            if (count == number) {
                break;
            }

        }
        String message = "Query result: " + movieTitles.toString();
        return message;

    }

    /**
     * gets the longest serials sorted by sortType
     */
    public String longestShow(final String year, final List<String> generes,
                              final String sortType, final int number) {
        ArrayList<SerialInputData> serials = new ArrayList<SerialInputData>();
        //for each movie
        for (int i = 0; i < serialsData.size(); i++) {
            Integer yearNumber = new Integer(serialsData.get(i).getYear());
            if (yearNumber.toString().equals(year)) {
                List<String> intersectElements = serialsData.get(i).getGenres().stream()
                        .filter(generes::contains)
                        .collect(Collectors.toList());
                if (generes.equals(intersectElements)) {
                    serialsData.get(i).setTotalDuration();
                    serials.add(serialsData.get(i));
                }
            }
        }


        if (sortType.equals("asc")) {
            Collections.sort(serials, new DurationAscendingSortserial());
        } else {
            Collections.sort(serials, new DurationDescendingSortserial());
        }
        ArrayList<String> serialTitles = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < serials.size(); i++) {

            serialTitles.add(serials.get(i).getTitle());
            count += 1;
            if (count == number) {
                break;
            }

        }
        String message = "Query result: " + serialTitles.toString();
        return message;

    }
    /**
     * gets the most viewd movies sorted by sortType
     */
    public String mostViewdMovies(final String year, final List<String> generes,
                                final String sortType, final int number) {
        ArrayList<MovieInputData> movies = new ArrayList<MovieInputData>();
        //for each movie
        for (int i = 0; i < moviesData.size(); i++) {
            Integer yearNumber = new Integer(moviesData.get(i).getYear());
            if (yearNumber.toString().equals(year)) {
                List<String> intersectElements = moviesData.get(i).getGenres().stream()
                        .filter(generes::contains)
                        .collect(Collectors.toList());
                if (generes.equals(intersectElements)) {
                    String title = moviesData.get(i).getTitle();
                    for (int j = 0; j < usersData.size(); j++) {
                        Map<String, Integer> history = usersData.get(j).getHistory();
                        if (history != null) {
                            if (history.get(title) != null) {
                                moviesData.get(i).addNumberOfViews(history.get(title));
                            }
                        }
                    }
                    if (moviesData.get(i).getNumberOfViews() > 0) {
                        movies.add(moviesData.get(i));
                    }
                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(movies, new ViewsAscendingSortMovie());
        } else {
            Collections.sort(movies, new ViewsDescendingSortMovie());
        }
        ArrayList<String> movieTitles = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < movies.size(); i++) {

            movieTitles.add(movies.get(i).getTitle());
            count += 1;
            if (count == number) {
                break;
            }

        }
        String message = "Query result: " + movieTitles.toString();
        return message;

    }
    /**
     * gets the most viewd serials sorted by sortType
     */
    public String mostViewdShows(final String year, final List<String> generes,
                                  final String sortType, final int number) {
        ArrayList<SerialInputData> serials = new ArrayList<SerialInputData>();
        //for each movie
        for (int i = 0; i < serialsData.size(); i++) {
            Integer yearNumber = new Integer(serialsData.get(i).getYear());
            if (yearNumber.toString().equals(year)) {
                List<String> intersectElements = serialsData.get(i).getGenres().stream()
                        .filter(generes::contains)
                        .collect(Collectors.toList());
                if (generes.equals(intersectElements)) {
                    String title = serialsData.get(i).getTitle();
                    for (int j = 0; j < usersData.size(); j++) {
                        Map<String, Integer> history = usersData.get(j).getHistory();
                        if (history != null) {
                            if (history.get(title) != null) {
                                serialsData.get(i).addNumberOfViews(history.get(title));
                            }
                        }
                    }
                    if (serialsData.get(i).getNumberOfViews() > 0) {
                        serials.add(serialsData.get(i));
                    }
                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(serials, new ViewAscendingSortserial());
        } else {
            Collections.sort(serials, new ViewDescendingSortserial());
        }
        ArrayList<String> movieTitles = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < serials.size(); i++) {

            movieTitles.add(serials.get(i).getTitle());
            count += 1;
            if (count == number) {
                break;
            }

        }
        String message = "Query result: " + movieTitles.toString();
        return message;

    }
    public String mostActiveUsers(int number, String sortType) {
        ArrayList<UserInputData> users = new ArrayList<UserInputData>();
        for (int i = 0; i < usersData.size(); i++) {
            usersData.get(i).setNumberofRatings();
            if(usersData.get(i).getNumberOfRatings() > 0) {
                users.add(usersData.get(i));
            }
        }
        if (sortType.equals("asc")) {
            Collections.sort(users, new RatingsAscendingSortUser());
        } else {
            Collections.sort(users, new RatingsDescendingSortUser());
        }
        ArrayList<String> usernames = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < users.size(); i++) {

            usernames.add(users.get(i).getUsername());
            count += 1;
            if (count == number) {
                break;
            }

        }
        String message = "Query result: " + usernames.toString();
        return message;


    }

}
