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
     * @param action is one of the actions given at input
     * @return an output message for an input action
     */
    public String getMessage(final ActionInputData action) {
        String message = "";
        switch (action.getActionType()) {
            case "command":
                message += applyCommand(action);
                break;
            case "query":
                message += applyQuery(action);
                break;
            case "recommendation":
                message += applyRecommendation(action);
                break;
            default:
                break;
        }

        return  message;
    }

    /**
     * applies command action
     * @param action is one of the actions given as input and has the type "command"
     * @return a particular message for each command
     */
    public String applyCommand(final ActionInputData action) {
        String message = "";
        String username = action.getUsername();
        String title = action.getTitle();
        if (action.getType().equals("favorite")) {
            for (UserInputData user : usersData) {
                if (user.getUsername().equals(username)) {
                    message += user.addFavorite(title);
                    break;
                }
            }
        } else if (action.getType().equals("view")) {
            for (UserInputData user : usersData) {
                if (user.getUsername().equals(username)) {
                    message += user.viewShow(title);
                    break;
                }
            }
        } else {
            Double grade = action.getGrade();
            int season = action.getSeasonNumber();

            for (UserInputData user : usersData) {
                if (user.getUsername().equals(username)) {
                    message += user.addRating(title, season, grade);
                    break;
                }
            }
        }

        return message;
    }

    /**
     * applies query action
     * @param action is one of the actions given as input and has the type "query"
     * @return a particular message for each query
     */
    public String applyQuery(final ActionInputData action) {
        String message = "";
        int number = action.getNumber();
        String sortType = action.getSortType();
        List<List<String>> filters = action.getFilters();
        String year;
        switch (action.getObjectType()) {
            case "actors":
                switch (action.getCriteria()) {
                    case "average":
                        message += sortBestActors(number, sortType);
                        break;
                    case "awards":
                        List<String> awards = filters.get(filters.size() - 1);
                        message += actorsByAwards(awards, sortType);
                        break;
                    case "filter_description":
                        List<String> words = filters.get(filters.size() - 2);
                        message += actorsByKeywords(words, sortType);
                        break;
                    default:
                        break;
                }
            case "movies":
                switch (action.getCriteria()) {

                    case "ratings":
                        year = filters.get(0).get(0);
                        List<String> genres = filters.get(1);
                        message += bestRatedMovies(year, genres, sortType, number);
                        break;
                    case "favorite":
                        year = filters.get(0).get(0);
                        genres = filters.get(1);
                        message += mostFavoriteMovies(year, genres, sortType, number);
                        break;
                    case "longest":
                        year = filters.get(0).get(0);
                        genres = filters.get(1);
                        message += longestMovies(year, genres, sortType, number);
                        break;
                    case "most_viewed":
                        year = filters.get(0).get(0);
                        genres = filters.get(1);
                        message += mostViewedMovies(year, genres, sortType, number);
                        break;
                    default:
                        break;
                }
                break;
            case "shows":
                switch (action.getCriteria()) {
                    case "ratings":
                        year = filters.get(0).get(0);
                        List<String> genres = filters.get(1);
                        message += bestRatedShow(year, genres, sortType, number);
                        break;
                    case "favorite":
                        year = filters.get(0).get(0);
                        genres = filters.get(1);
                        markFavoriteShows();
                        message = mostFavoriteShow(year, genres, sortType, number);
                        break;
                    case "longest":
                        year = filters.get(0).get(0);
                        genres = filters.get(1);
                        message += longestShow(year, genres, sortType, number);
                        break;
                    case "most_viewed":
                        year = filters.get(0).get(0);
                        genres = filters.get(1);
                        message += mostViewedShows(year, genres, sortType, number);
                        break;
                    default:
                        break;
                }
                break;
            case "users":
                if (action.getCriteria().equals("num_ratings")) {
                    message += mostActiveUsers(number, sortType);
                }
                break;
            default:
                break;
        }

        return message;
    }

    /**
     * applies recommendation action
     * @param action is one of the actions given as input and has the type "recommendation"
     * @return a particular message for each recommendation
     */
    public String applyRecommendation(final ActionInputData action) {
        String message = "";
        String username = action.getUsername();
        switch (action.getType()) {
            case "standard":
                message += getStandardRecommendation(username);
                break;
            case "best_unseen":
                message += getBestUnseen(username);
                break;
            case "popular":
                message += getMostPopularVideo(username);
                break;
            case "favorite":
                message += getMostFavorite(username);
                break;
            case "search":
                String genre = action.getGenre();
                message += getSearchList(username, genre);
                break;
            default:
                break;
        }

        return message;
    }

    /**
     * finds a serial by title and calculates the number of seasons for it
     * @param title -> the name of the serial
     * @return the number of seasons of the serial
     */
    public int getSeasonsNumber(final String title) {
        int seasonsNumber = 0;
        for (SerialInputData serial : serialsData) {
            if (serial.getTitle().equals(title)) {
                seasonsNumber = serial.getNumberSeason();
                break;
            }
        }

        return seasonsNumber;
    }

    /**
     * calculates for how many times a show has been added to a user's favourite list
     */
    public void markFavoriteShows() {
        for (UserInputData user : usersData) {
            for (int j = 0; j < user.getFavoriteMovies().size(); j++) {
                String title = user.getFavoriteMovies().get(j);
                for (MovieInputData movie : moviesData) {
                    if (movie.getTitle().equals(title)) {
                        movie.markFavorite();
                        break;
                    }
                }
                for (SerialInputData serial : serialsData) {
                    if (serial.getTitle().equals(title)) {
                        serial.markFavorite();
                        break;
                    }
                }
            }
        }
    }

    /**
     * resets the rating for each actor
     */
    public void resetActorRating() {
        for (ActorInputData actor : actorsData) {
            actor.resetRating();
        }
    }

    /**
     * sorts actors by their average rating
     * @param number is the number of actors required
     * @param sortType is the sorting order required
     * @return the result for average actors query
     */
    public String sortBestActors(final int number, final String sortType) {
        /*
         for each actor, I calculate the rating as the average of the ratings of their films from
         filmography
         */
        resetActorRating();
        ArrayList<ActorInputData> actors = new ArrayList<>();
        ArrayList<ActorInputData> actorsInputData = new ArrayList<>(actorsData);
        for (ActorInputData actor : actorsInputData) {
            /*
             I calculate the average rating for each film from filmography, then I calculate the
             average rating for the actor
             */
            for (int j = 0; j < actor.getFilmography().size(); j++) {
                String title = actor.getFilmography().get(j);
                Integer numberOfRatings = 0;
                Double rate = 0.0;
                for (UserInputData user : usersData) {
                    if (user.getRatedMovies().get(title) != null) {
                        rate += user.getRatedMovies().get(title);
                        numberOfRatings++;
                    } else if (user.getRatedSerials().get(title) != null) {
                        int seasonsNumber = getSeasonsNumber(title);
                        HashMap<Integer, Double> ratings = user.getRatedSerials().get(title);
                        Set<Integer> seasons = ratings.keySet();
                        for (Integer season : seasons) {
                            rate += ratings.get(season);
                            numberOfRatings++;
                        }
                        for (int t = 0; t < seasonsNumber - seasons.size(); t++) {
                            rate += 0.0;
                            numberOfRatings++;
                        }
                    }
                }
                if (!numberOfRatings.equals(0)) {
                    actor.addRating(rate / numberOfRatings);
                }
            }
            actor.setAverageRating();
            if (actor.getAverageRating() > 0) {
                actors.add(actor);
            }
        }

        if (sortType.equals("asc")) {
            actors.sort(new AverageAscendingSortActor());
        } else {
            actors.sort(new AverageAscendingSortActor().reversed());
        }

        ArrayList<String> actorNames = new ArrayList<>();
        int count = 0;
        for (ActorInputData actor : actors) {
            if (actor.getAverageRating() > 0) {
                actorNames.add(actor.getName());
                count += 1;
                if (count == number) {
                    break;
                }
            }

        }

        return "Query result: " + actorNames.toString();
    }

    /**
     * @param year is the year required
     * @param genres is an array with the genres required
     * @return a list of movies that have the specified filters
     */
    private ArrayList<MovieInputData> moviesByFilters(final String year,
                                                      final List<String> genres) {
        ArrayList<MovieInputData> movies = new ArrayList<>();
        for (MovieInputData moviesDatum : moviesData) {
            int yearNumber = moviesDatum.getYear();
            if (Integer.toString(yearNumber).equals(year) || year == null) {
                List<String> intersectElements = moviesDatum.getGenres().stream()
                        .filter(genres::contains).sorted().collect(Collectors.toList());
                Collections.sort(genres);
                Collections.sort(intersectElements);
                if (genres.equals(intersectElements) || genres.get(0) == null) {
                    movies.add(moviesDatum);
                }
            }
        }
        return movies;
    }

    /**
     * search movies by year and genres and sort them by favorite field
     * @param year is the year required
     * @param genres is an array with the genres required
     * @param sortType is the sorting order required
     * @param number is the number of movies required
     * @return the result for favorite movie query
     */
    public String mostFavoriteMovies(final String year, final List<String> genres,
                                     final String sortType, final int number) {
        markFavoriteShows();
        ArrayList<MovieInputData> movies = moviesByFilters(year, genres);

        if (sortType.equals("asc")) {
            movies.sort(new FavoriteAscendingSortMovie());
        } else {
            movies.sort(new FavoriteAscendingSortMovie().reversed());
        }

        ArrayList<String> moviesResult = new ArrayList<>();
        int count = 0;
        for (MovieInputData movie : movies) {
            if (movie.getFavorite() > 0) {
                moviesResult.add(movie.getTitle());
                count += 1;
                if (count == number) {
                    break;
                }
            }
        }

        return "Query result: " + moviesResult.toString();
    }

    /**
     * search serials by year and genres and sort them by favorite field
     * @param year is the year required
     * @param genres is an array with the genres required
     * @param sortType is the sorting order required
     * @param number is the number of movies required
     * @return the result for favorite show query
     */
    public String mostFavoriteShow(final String year, final List<String> genres,
                                   final String sortType, final int number) {
        ArrayList<SerialInputData> serials = new ArrayList<>();
        for (SerialInputData serialsDatum : serialsData) {
            int yearNumber = serialsDatum.getYear();
            if (Integer.toString(yearNumber).equals(year) || year == null) {
                List<String> intersectElements = serialsDatum.getGenres().stream()
                        .filter(genres::contains).sorted().collect(Collectors.toList());
                Collections.sort(genres);

                if (genres.equals(intersectElements) || genres.get(0) == null) {
                    serials.add(serialsDatum);
                }
            }
        }

        if (sortType.equals("asc")) {
            serials.sort(new FavoriteAscendingSortSerial());
        } else {
            serials.sort(new FavoriteDescendingSortSerial());
        }

        ArrayList<String> serialsResult = new ArrayList<>();
        int count = 0;
        for (SerialInputData serial : serials) {
            if (serial.getFavorite() > 0) {
                serialsResult.add(serial.getTitle());
                count += 1;
                if (count == number) {
                    break;
                }
            }
        }

        return "Query result: " + serialsResult.toString();
    }

    /**
     * search actors by awards and sort them by the number of their awards
     * @param awards is the list of awards required
     * @param sortType is the sorting order required
     * @return the result for actor awards query
     */
    public String actorsByAwards(final List<String> awards, final String sortType) {
        ArrayList<ActorInputData> actors = new ArrayList<>();
        for (ActorInputData actorsDatum : actorsData) {
            Map<ActorsAwards, Integer> awardsMap = actorsDatum.getAwards();
            Set<ActorsAwards> keys = awardsMap.keySet();
            List<String> actorAwards = new ArrayList<>();
            for (ActorsAwards key : keys) {
                actorAwards.add(key.toString());
            }

            List<String> intersectElements = actorAwards.stream()
                    .filter(awards::contains)
                    .collect(Collectors.toList());
            Collections.sort(awards);
            Collections.sort(intersectElements);

            if (intersectElements.equals(awards)) {
                actorsDatum.calculateAwardsNumber();
                actors.add(actorsDatum);

            }
        }

        if (sortType.equals("asc")) {
            actors.sort(new AwardsAscendingSort());
        } else {
            actors.sort(new AwardsAscendingSort().reversed());
        }

        ArrayList<String> actorNames = new ArrayList<>();
        for (ActorInputData actor : actors) {
            actorNames.add(actor.getName());
        }

        return "Query result: " + actorNames.toString();
    }

    /**
     * search the actors by words and sort them by their names
     * @param words is the list of words required
     * @param sortType is the sorting order required
     * @return the result for actor filter description query
     */
    public String actorsByKeywords(final List<String> words, final String sortType) {
        ArrayList<String> actors = new ArrayList<>();
        for (ActorInputData actorsDatum : actorsData) {
            if (actorsDatum.hasKeywords(words)) {
                actors.add(actorsDatum.getName());
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(actors);
        } else {
            actors.sort(Collections.reverseOrder());
        }

        return "Query result: " + actors.toString();
    }

    /**
     * resets the rating for all movies
     */
    public void resetMovieRating() {
        for (MovieInputData moviesDatum : moviesData) {
            moviesDatum.resetRating();
        }
    }

    /**
     * @param movies -> movies required
     * @param number is the number of movie titles required
     * @return a list of first movie titles from movies
     */
    private ArrayList<String> getMovieTitles(final ArrayList<MovieInputData> movies,
                                             final int number) {
        ArrayList<String> movieTitles = new ArrayList<>();
        int count = 0;
        for (MovieInputData movie : movies) {
            movieTitles.add(movie.getTitle());
            count += 1;
            if (count == number) {
                break;
            }
        }

        return movieTitles;
    }

    /**
     * search movies by year and genres and sort them by rating
     * @param year is the year required
     * @param genres is an array with the genres required
     * @param sortType is the sorting order required
     * @param number is the number of movies required
     * @return the result for rating movie query
     */
    public String bestRatedMovies(final String year, final List<String> genres,
                                  final String sortType, final int number) {
        resetMovieRating();
        for (MovieInputData moviesDatum : moviesData) {
            int yearNumber = moviesDatum.getYear();

            if (Integer.toString(yearNumber).equals(year) || year == null) {
                List<String> intersectElements = moviesDatum.getGenres().stream()
                        .filter(genres::contains).sorted().collect(Collectors.toList());
                Collections.sort(genres);

                if (genres.equals(intersectElements) || genres.get(0) == null) {
                    String title = moviesDatum.getTitle();
                    for (UserInputData usersDatum : usersData) {
                        Map<String, Double> ratedMovies = usersDatum.getRatedMovies();
                        if (ratedMovies.get(title) != null) {
                            Double rating = ratedMovies.get(title);
                            moviesDatum.addRating(rating);
                        }
                    }
                }
            }
        }

        ArrayList<MovieInputData> movies = new ArrayList<>();
        for (MovieInputData moviesDatum : moviesData) {
            moviesDatum.setRating();
            if (moviesDatum.getRating() > 0) {
                movies.add(moviesDatum);
            }
        }

        if (sortType.equals("asc")) {
            movies.sort(new RatingAscendingSortMovie());
        } else {
            movies.sort(new RatingDescendingSortMovie());
        }

        ArrayList<String> movieTitles = getMovieTitles(movies, number);
        return "Query result: " + movieTitles.toString();
    }

    /**
     * resets the rating for all serials
     */
    public void resetSerialRating() {
        for (SerialInputData serialsDatum : serialsData) {
            serialsDatum.resetRating();
        }
    }

    private ArrayList<String> getSerialTitles(final ArrayList<SerialInputData> serials,
                                              final int number) {
        ArrayList<String> serialTitles = new ArrayList<>();
        int count = 0;
        for (SerialInputData serial : serials) {
            serialTitles.add(serial.getTitle());
            count += 1;
            if (count == number) {
                break;
            }
        }
        return  serialTitles;
    }

    /**
     * search serials by year and genres and sort them by rating
     * @param year is the year required
     * @param genres is an array with the genres required
     * @param sortType is the sorting order required
     * @param number is the number of movies required
     * @return the result for rating show query
     */
    public String bestRatedShow(final String year, final List<String> genres,
                                final String sortType, final int number) {
        resetSerialRating();
        for (SerialInputData serialsDatum : serialsData) {
            int yearNumber = serialsDatum.getYear();
            if (Integer.toString(yearNumber).equals(year) || year == null) {
                List<String> intersectElements = serialsDatum.getGenres().stream()
                        .filter(genres::contains).sorted().collect(Collectors.toList());
                Collections.sort(genres);

                if (genres.equals(intersectElements) || genres.get(0) == null) {
                    String title = serialsDatum.getTitle();

                    for (UserInputData usersDatum : usersData) {
                        Map<String, HashMap<Integer, Double>> ratedSerials
                                = usersDatum.getRatedSerials();

                        if (ratedSerials.get(title) != null) {
                            HashMap<Integer, Double> seasonsRate = ratedSerials.get(title);
                            Set<Integer> keys = seasonsRate.keySet();
                            ArrayList<Integer> ratedSeasons = new ArrayList<>(keys);

                            for (Integer ratedSeason : ratedSeasons) {
                                Double rating = seasonsRate.get(ratedSeason);
                                serialsDatum.addRating(ratedSeason, rating);
                            }
                        }
                    }
                }
            }
        }

        ArrayList<SerialInputData> serials = new ArrayList<>();
        for (SerialInputData serialsDatum : serialsData) {
            serialsDatum.setRating();
            if (serialsDatum.getRating() > 0) {
                serials.add(serialsDatum);
            }
        }

        if (sortType.equals("asc")) {
            serials.sort(new RatingAscendingSortSerial());
        } else {
            serials.sort(new RatingDescendingSortSerial());
        }

        ArrayList<String> serialTitles = getSerialTitles(serials, number);
        return "Query result: " + serialTitles.toString();
    }

    /**
     * search movies by year and genres and sort them by duration
     * @param year is the year required
     * @param genres is an array with the genres required
     * @param sortType is the sorting order required
     * @param number is the number of movies required
     * @return the result for longest movie query
     */
    public String longestMovies(final String year, final List<String> genres,
                                final String sortType, final int number) {
        ArrayList<MovieInputData> movies = moviesByFilters(year, genres);
        if (sortType.equals("asc")) {
            movies.sort(new DurationAscendingSortMovie());
        } else {
            movies.sort(new DurationAscendingSortMovie().reversed());
        }

        ArrayList<String> movieTitles = getMovieTitles(movies, number);
        return "Query result: " + movieTitles.toString();
    }

    /**
     * search serials by year and genres and sort them by duration
     * @param year is the year required
     * @param genres is an array with the genres required
     * @param sortType is the sorting order required
     * @param number is the number of movies required
     * @return the result for longest movie query
     */
    public String longestShow(final String year, final List<String> genres,
                              final String sortType, final int number) {
        ArrayList<SerialInputData> serials = new ArrayList<>();
        for (SerialInputData serialsDatum : serialsData) {
            int yearNumber = serialsDatum.getYear();
            if (Integer.toString(yearNumber).equals(year)) {
                List<String> intersectElements = serialsDatum.getGenres().stream()
                        .filter(genres::contains)
                        .collect(Collectors.toList());
                if (genres.equals(intersectElements)) {
                    serialsDatum.setTotalDuration();
                    serials.add(serialsDatum);
                }
            }
        }

        if (sortType.equals("asc")) {
            serials.sort(new DurationAscendingSortSerial());
        } else {
            serials.sort(new DurationDescendingSortSerial());
        }

        ArrayList<String> serialTitles = getSerialTitles(serials, number);
        return "Query result: " + serialTitles.toString();
    }
    /**
     * search movies by year and genres and sort them by the number of views
     * @param year is the year required
     * @param genres is an array with the genres required
     * @param sortType is the sorting order required
     * @param number is the number of movies required
     * @return the result for most viewed movie query
     */
    public String mostViewedMovies(final String year, final List<String> genres,
                                final String sortType, final int number) {
        ArrayList<MovieInputData> movies = new ArrayList<>();
        for (MovieInputData moviesDatum : moviesData) {
            int yearNumber = moviesDatum.getYear();
            if (Integer.toString(yearNumber).equals(year) || year == null) {
                List<String> intersectElements = moviesDatum.getGenres().stream()
                        .filter(genres::contains).sorted().collect(Collectors.toList());
                Collections.sort(genres);

                if (genres.equals(intersectElements) || genres.get(0) == null) {
                    String title = moviesDatum.getTitle();

                    for (UserInputData usersDatum : usersData) {
                        Map<String, Integer> history = usersDatum.getHistory();

                        if (history != null) {
                            if (history.get(title) != null) {
                                moviesDatum.addNumberOfViews(history.get(title));
                            }
                        }
                    }

                    if (moviesDatum.getNumberOfViews() > 0) {
                        movies.add(moviesDatum);
                    }
                }
            }
        }

        if (sortType.equals("asc")) {
            movies.sort(new ViewsAscendingSortMovie());
        } else {
            movies.sort(new ViewsAscendingSortMovie().reversed());
        }

        ArrayList<String> movieTitles = getMovieTitles(movies, number);
        return "Query result: " + movieTitles.toString();
    }

    /**
     * search serials by year and genres and sort them by the number of views
     * @param year is the year required
     * @param genres is an array with the genres required
     * @param sortType is the sorting order required
     * @param number is the number of movies required
     * @return the result for most viewed show query
     */
    public String mostViewedShows(final String year, final List<String> genres,
                                 final String sortType, final int number) {
        ArrayList<SerialInputData> serials = new ArrayList<>();
        for (SerialInputData serialsDatum : serialsData) {
            int yearNumber = serialsDatum.getYear();

            if (Integer.toString(yearNumber).equals(year) || year == null) {
                List<String> intersectElements = serialsDatum.getGenres().stream()
                        .filter(genres::contains).sorted().collect(Collectors.toList());
                Collections.sort(genres);

                if (genres.equals(intersectElements) || genres.get(0) == null) {
                    String title = serialsDatum.getTitle();

                    for (UserInputData usersDatum : usersData) {
                        Map<String, Integer> history = usersDatum.getHistory();
                        if (history != null) {
                            if (history.get(title) != null) {
                                serialsDatum.addNumberOfViews(history.get(title));
                            }
                        }
                    }

                    if (serialsDatum.getNumberOfViews() > 0) {
                        serials.add(serialsDatum);
                    }
                }
            }
        }

        if (sortType.equals("asc")) {
            serials.sort(new ViewAscendingSortSerial());
        } else {
            serials.sort(new ViewAscendingSortSerial().reversed());
        }

        ArrayList<String> movieTitles = new ArrayList<>();
        int count = 0;
        for (ShowInput serial : serials) {
            movieTitles.add(serial.getTitle());
            count += 1;
            if (count == number) {
                break;
            }
        }

        return "Query result: " + movieTitles.toString();
    }

    /**
     * sort users by the number of ratings
     * @param number is the number of users required
     * @param sortType is the sorting order required
     * @return the result for num_ratings users query
     */
    public String mostActiveUsers(final int number, final String sortType) {
        ArrayList<UserInputData> users = new ArrayList<>();
        for (UserInputData usersDatum : usersData) {
            usersDatum.setNumberOfRatings();
            if (usersDatum.getNumberOfRatings() > 0) {
                users.add(usersDatum);
            }
        }

        if (sortType.equals("asc")) {
            users.sort(new RatingsAscendingSortUser());
        } else {
            users.sort(new RatingsAscendingSortUser().reversed());
        }

        ArrayList<String> usernames = new ArrayList<>();
        int count = 0;
        for (UserInputData user : users) {
            usernames.add(user.getUsername());
            count += 1;
            if (count == number) {
                break;
            }
        }

        return "Query result: " + usernames.toString();
    }

    /**
     * calculates the first movie or serial from database that the user has not viewed
     * @param username is the username required
     * @return the result for standard recommendation
     */
    public String getStandardRecommendation(final String username) {
        String message = "StandardRecommendation result: ";
        for (UserInputData usersDatum : usersData) {
            if (usersDatum.getUsername().equals(username)) {
                for (MovieInputData moviesDatum : moviesData) {
                    String title = moviesDatum.getTitle();
                    if (usersDatum.getHistory().get(title) == null) {
                        message += title;
                        return message;
                    }
                }
                for (SerialInputData serialsDatum : serialsData) {
                    String title = serialsDatum.getTitle();
                    if (usersDatum.getHistory().get(title) == null) {
                        message += title;
                        return message;
                    }
                }
            }
        }

        message = "StandardRecommendation cannot be applied!";
        return message;
    }

    /**
     * @return a list of videos sorted by their rating in descending order
     */
    public ArrayList<ShowInput> getVideosByRating() {
        resetSerialRating();
        resetMovieRating();
        ArrayList<ShowInput> videos = new ArrayList<>();
        for (int i = 0; i < moviesData.size(); i++) {
            String title = moviesData.get(i).getTitle();
            for (UserInputData user : usersData) {
                Map<String, Double> movieRatings = user.getRatedMovies();
                if (movieRatings != null) {
                    if (movieRatings.get(title) != null) {
                        Double rate = movieRatings.get(title);
                        moviesData.get(i).addRating(rate);
                    }
                }
            }
            moviesData.get(i).setRating();
            moviesData.get(i).setPosition(i);
            videos.add(moviesData.get(i));
        }

        for (int i = 0; i < serialsData.size(); i++) {
            String title = serialsData.get(i).getTitle();
            for (UserInputData user : usersData) {
                Map<String, HashMap<Integer, Double>> serialRatings
                        = user.getRatedSerials();
                if (serialRatings != null) {
                    if (serialRatings.get(title) != null) {
                        HashMap<Integer, Double> seasonRatings = serialRatings.get(title);
                        Set<Integer> keys = seasonRatings.keySet();
                        for (Integer key : keys) {
                            serialsData.get(i).addRating(key, seasonRatings.get(key));
                        }
                    }
                }
            }
            serialsData.get(i).setRating();
            serialsData.get(i).setPosition(moviesData.size() + i - 1);
            videos.add(serialsData.get(i));
        }

        videos.sort(new RatingDescendingSort());
        return videos;
    }

    /**
     * @param username is the username required
     * @param videos is an arraylist of videos
     * @return the title of the first video that the user has not seen
     */
    private String getVideoTitle(final String username, final ArrayList<ShowInput> videos) {
        String message = "";
        for (UserInputData usersDatum : usersData) {
            if (usersDatum.getUsername().equals(username)) {
                for (ShowInput video : videos) {
                    String title = video.getTitle();
                    if (usersDatum.getHistory().get(title) == null) {
                        message += title;
                        return message;
                    }
                }
            }
        }
        return  message;
    }

    /**
     * calculates the first movie or serial from database that the user has not seen and has the
     * best rating
     * @param username is the username required
     * @return the result for best_unseen recommendation
     */
    public String getBestUnseen(final String username) {
        String message = "BestRatedUnseenRecommendation result: ";
        ArrayList<ShowInput> videos = getVideosByRating();
        if (!getVideoTitle(username, videos).equals("")) {
            return message + getVideoTitle(username, videos);
        }

        message = "BestRatedUnseenRecommendation cannot be applied!";
        return message;
    }

    /**
     * resets the number of views for each video
     */
    public void resetViews() {
        for (MovieInputData moviesDatum : moviesData) {
            moviesDatum.setNumberOfViews();
        }
        for (SerialInputData serialsDatum : serialsData) {
            serialsDatum.setNumberOfViews();
        }
    }

    /**
     * calculates the number of views for each video
     * @return a list with all videos
     */
    public ArrayList<ShowInput> calculateNumberOfViews() {
        resetViews();
        ArrayList<ShowInput> videos = new ArrayList<>();
        for (MovieInputData moviesDatum : moviesData) {
            String title = moviesDatum.getTitle();
            for (UserInputData usersDatum : usersData) {
                if (usersDatum.getHistory().get(title) != null) {
                    moviesDatum.addNumberOfViews(usersDatum.getHistory().get(title));
                }
            }
            videos.add(moviesDatum);
        }

        for (SerialInputData serialsDatum : serialsData) {
            String title = serialsDatum.getTitle();
            for (UserInputData usersDatum : usersData) {
                if (usersDatum.getHistory().get(title) != null) {
                    serialsDatum.addNumberOfViews(usersDatum.getHistory().get(title));
                }
            }
            videos.add(serialsDatum);
        }
        return  videos;
    }

    /**
     * calculates the first movie or serial from database that the user has not seen and has the
     * most popular genre
     * @param username is the username required
     * @return the result for popular recommendation
     */
    public String getMostPopularVideo(final String username) {
        for (UserInputData usersDatum : usersData) {
            if (usersDatum.getUsername().equals(username)) {
                if (usersDatum.getSubscriptionType().equals("BASIC")) {
                    return "PopularRecommendation cannot be applied!";
                } else {
                    break;
                }
            }
        }

        String message = "PopularRecommendation result: ";
        ArrayList<ShowInput> videos = calculateNumberOfViews();
        /*
         calculate the number of views for each genre
         store the number of views for genres in a hashmap
         */
        HashMap<String, Integer> map = new HashMap<>();
        for (ShowInput showInput : videos) {
            Integer views = showInput.getNumberOfViews();
            ArrayList<String> genres = showInput.getGenres();
            for (String genre : genres) {
                if (map.get(genre) == null) {
                    map.put(genre, views);
                } else {
                    Integer numberOfViews = map.get(genre);
                    numberOfViews += views;
                    map.replace(genre, numberOfViews);
                }
            }
        }

        Set<String> keys = map.keySet();
        ArrayList<Integer> views = new ArrayList<>();
        for (String key : keys) {
            views.add(map.get(key));
        }
        views.sort(Collections.reverseOrder());

        for (UserInputData user : usersData) {
            if (user.getUsername().equals(username)) {
                for (Integer view : views) {
                    for (String key : keys) {
                        if (map.get(key).equals(view)) {
                            for (ShowInput video : videos) {
                                String title = video.getTitle();
                                ArrayList<String> videoGenre = video.getGenres();

                                for (String genre : videoGenre) {
                                    if (genre.equals(key) && user.getHistory().get(title) == null) {
                                        message += title;
                                        return message;

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        message = "PopularRecommendation cannot be applied!";
       return message;
    }

    /**
     * @return a list of videos sorted by their favorite number in descending order
     */
    public ArrayList<ShowInput> getVideosByFavorite() {
        markFavoriteShows();
        ArrayList<ShowInput> videos = new ArrayList<>();
        for (int i = 0; i < moviesData.size(); i++) {
            moviesData.get(i).setPosition(i);
            videos.add(moviesData.get(i));
        }
        for (int i = 0; i < serialsData.size(); i++) {
            serialsData.get(i).setPosition(moviesData.size() + i - 1);
            videos.add(serialsData.get(i));
        }
        videos.sort(new FavoriteDescendingSort());
        return videos;
    }

    /**
     * calculates the first movie or serial from database that the user has not seen and is the most
     * favourite
     * @param username is the username required
     * @return the result for favorite recommendation
     */
    public String getMostFavorite(final String username) {
        for (UserInputData usersDatum : usersData) {
            if (usersDatum.getUsername().equals(username)) {
                if (usersDatum.getSubscriptionType().equals("BASIC")) {
                    return "FavoriteRecommendation cannot be applied!";
                } else {
                    break;
                }
            }
        }

        ArrayList<ShowInput> videos = getVideosByFavorite();
        String message = "FavoriteRecommendation result: ";
        if (!getVideoTitle(username, videos).equals("")) {
            return message + getVideoTitle(username, videos);
        }

        message = "FavoriteRecommendation cannot be applied!";
        return message;
    }

    /**
     * @param username is the username required
     * @param genre is the genre required
     * @return the result for search recommendation
     */
    public String getSearchList(final String username, final String genre) {
        for (UserInputData usersDatum : usersData) {
            if (usersDatum.getUsername().equals(username)) {
                if (usersDatum.getSubscriptionType().equals("BASIC")) {
                    return "SearchRecommendation cannot be applied!";
                } else {
                    break;
                }
            }
        }

        ArrayList<ShowInput> videos = getVideosByRating();
        videos.sort(new RatingAscendingSort());

        String message = "SearchRecommendation result: ";
        ArrayList<String> videoTitles = new ArrayList<>();
        for (UserInputData usersDatum : usersData) {
            if (usersDatum.getUsername().equals(username)) {
                for (ShowInput video : videos) {
                    String title = video.getTitle();
                    ArrayList<String> videoGenre = video.getGenres();

                    for (String s : videoGenre) {
                        if (s.equals(genre)) {
                            if (usersDatum.getHistory().get(title) == null) {
                                videoTitles.add(title);
                            }
                        }
                    }
                }
            }
        }

        if (videoTitles.size() > 0) {
            message += videoTitles.toString();
        } else {
            message = "SearchRecommendation cannot be applied!";
        }
        return message;
    }
}
