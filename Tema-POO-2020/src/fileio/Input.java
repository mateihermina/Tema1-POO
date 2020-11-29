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
     * returns the first number actors in sortType order of their average rating
     */
    public String sortBestActors(final int number, final String sortType) {
        for(int i = 0; i < actorsData.size(); i++) {
            actorsData.get(i).setRating(0.0);
            actorsData.get(i).setRatingsNumber(0);
        }
        ArrayList<ActorInputData> actors = new ArrayList<ActorInputData>();
        ArrayList<ActorInputData> actorsInputData = new ArrayList<ActorInputData>(actorsData);
        //for each actor in actorsInputData
        for (int i = 0; i < actorsInputData.size(); i++) {
            ActorInputData actor = actorsInputData.get(i);
            //for each film in filmography
            for (int j = 0; j < actor.getFilmography().size(); j++) {
                String title = actor.getFilmography().get(j);
                // for each user
                Integer numberOfRatings = 0;
                Double rate = 0.0;
                for (int k = 0; k < usersData.size(); k++) {
                    UserInputData user = usersData.get(k);
                    if (user.getRatedMovies().get(title) != null) {
                        //actor.addRating(user.getRatedMovies().get(title));
                        rate += user.getRatedMovies().get(title);
                        numberOfRatings ++;
                    }
                    else if (user.getRatedSerials().get(title) != null) {
                        int seasonsNumber = getSeasonsNumber(title);
                        HashMap<Integer, Double> ratings = user.getRatedSerials().get(title);
                        Set<Integer> seasons = ratings.keySet();
                        for (Integer season : seasons) {
                            //actor.addRating(ratings.get(season));
                            rate += ratings.get(season);
                            numberOfRatings ++;
                        }
                        for (int t = 0; t < seasonsNumber - seasons.size(); t++) {
                            //actor.addRating(0.0);
                            rate += 0.0;
                            numberOfRatings ++;
                        }
                    }
                    //calculez media filmului

                }
                if(!numberOfRatings.equals(0)) {
                    System.out.println("LOOP: "+ actor.getName() + " "+ rate.toString()+ numberOfRatings);
                    actor.addRating(rate / numberOfRatings);
                }
            }
            actor.setAverageRating();
            System.out.println(actor.getAverageRating().toString());
            if(actor.getAverageRating() > 0) {
                actors.add(actor);
            }
        }
        for (int i = 0; i < actors.size(); i++) {
            System.out.println(actors.get(i).getName() + " " + actors.get(i).getAverageRating());
        }
        System.out.println("\n\n\n");
        if (sortType.equals("asc")) {
            Collections.sort(actors, new AverageAscendingSortActor());
        } else {
            // iar cu reversed
            Collections.sort(actors, new AverageAscendingSortActor().reversed());
        }
        ArrayList<String> actorNames = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < actors.size(); i++) {
            if (actors.get(i).getAverageRating() > 0) {
                actorNames.add(actors.get(i).getName());
                count += 1;
                if (count == number) {
                    break;
                }
            }

        }
        String message = "Query result: " + actorNames.toString();
        return message;
    }

    /**
     * gets the result for favorite movie query
     */
    public String mostFavoriteMovies(final String year, final List<String> generes,
                                     final String sortType, final int number) {
        markFavoriteShows();
        Boolean found = false;
        ArrayList<MovieInputData> movies = new ArrayList<MovieInputData>();
        for (int i = 0; i < moviesData.size(); i++) {
            Integer yearNumber = new Integer(moviesData.get(i).getYear());
            if(year != null) {
                if (yearNumber.toString().equals(year)) {
                    //System.out.printf(yearNumber.toString());

                    if(generes.get(0)!=null) {
                        //System.out.println(generes.toString());
                        List<String> intersectElements = moviesData.get(i).getGenres().stream()
                                .filter(generes::contains)
                                .collect(Collectors.toList());
                        Collections.sort(intersectElements);
                        Collections.sort(generes);
                        if (generes.equals(intersectElements)) {
                            movies.add(moviesData.get(i));
                        }
                    }  else {
                        System.out.println("MITIII");
                        movies.add(moviesData.get(i));
                    }
                }
            } else {
                if(generes.get(0)!=null) {
                    List<String> intersectElements = moviesData.get(i).getGenres().stream()
                            .filter(generes::contains)
                            .collect(Collectors.toList());
                    Collections.sort(intersectElements);
                    Collections.sort(generes);
                    if (generes.equals(intersectElements)) {
                        movies.add(moviesData.get(i));
                    }
                }  else {
                    movies.add(moviesData.get(i));
                }
            }
        }
        //System.out.println(movies + "Hermina"+"\n");
        if (sortType.equals("asc")) {
            Collections.sort(movies, new FavoriteAscendingSortMovie());
        } else {
            Collections.sort(movies, new FavoriteAscendingSortMovie().reversed());
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
    public String mostFavoriteShow(final String year, final List<String> genres,
                                   final String sortType, final int number) {
        Boolean found = false;
        ArrayList<SerialInputData> serials = new ArrayList<SerialInputData>();
        for (int i = 0; i < serialsData.size(); i++) {
            if (year != null) {

                Integer yearNumber = new Integer(serialsData.get(i).getYear());
                if (yearNumber.toString().equals(year)) {
                    if(genres.get(0) != null) {
                        List<String> intersectElements = serialsData.get(i).getGenres().stream()
                                .filter(genres::contains)
                                .collect(Collectors.toList());
                        Collections.sort(intersectElements);
                        Collections.sort(genres);
                        if (genres.equals(intersectElements)) {
                            serials.add(serialsData.get(i));
                        }
                    } else {
                        serials.add(serialsData.get(i));
                    }
                }
            } else {
                if(genres.get(0) != null) {
                    List<String> intersectElements = serialsData.get(i).getGenres().stream()
                            .filter(genres::contains)
                            .collect(Collectors.toList());
                    Collections.sort(intersectElements);
                    Collections.sort(genres);
                    if (genres.equals(intersectElements)) {
                        serials.add(serialsData.get(i));
                    }
                } else {
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
            List<String> actorAwards = new ArrayList<String>();
            for(ActorsAwards key : keys) {
                actorAwards.add(key.toString());
            }
            System.out.println(actorsData.get(i).getName() + " "+ actorAwards.toString());
            /*ArrayList<ActorsAwards> keyList = new ArrayList<ActorsAwards>(keys);
            ArrayList<ActorsAwards> inputAwards = new ArrayList<ActorsAwards>();
            for (int j = 0; j < awards.size(); j++) {
                inputAwards.add(ActorsAwards.valueOf(awards.get(j)));
            }
            ArrayList<ActorsAwards> intersectElements = (ArrayList<ActorsAwards>) keyList.stream()
                    .filter(inputAwards::contains)
                    .collect(Collectors.toList());
            if (intersectElements.equals(inputAwards)) {
                System.out.println("MEWWWWW");
                actorsData.get(i).calculateAwardsNumber();
                actors.add(actorsData.get(i));

            }*/
            List<String> intersectElements = actorAwards.stream()
                    .filter(awards::contains)
                    .collect(Collectors.toList());
            //System.out.println(intersectElements.toString());
            Collections.sort(awards);
            Collections.sort(intersectElements);
            if (intersectElements.equals(awards)) {
                //System.out.println("MEWWWWW: " + actorsData.get(i).getName());
                actorsData.get(i).calculateAwardsNumber();
                actors.add(actorsData.get(i));

            }

        }
        for(int i = 0; i < actors.size(); i++) {
            System.out.println(actors.get(i).getName()+" "+actors.get(i).getAwardsNumber());
        }
        System.out.println("\n\n");
        if (sortType.equals("asc")) {
            Collections.sort(actors, new AwardsAscendingSort());
        } else {
            // nu stiu daca e bine dar asa pare
            Collections.sort(actors, new AwardsAscendingSort().reversed());
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
        // am adaugat aici
        for(int i = 0; i < moviesData.size(); i++) {
            moviesData.get(i).resetRating();
        }
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
        for(int i = 0; i < serialsData.size(); i++) {
            serialsData.get(i).resetRating();
        }
        //for each movie
        for (int i = 0; i < serialsData.size(); i++) {
            Integer yearNumber = new Integer(serialsData.get(i).getYear());
            if (year !=null) {
                if (yearNumber.toString().equals(year)) {
                    if(generes.get(0) != null) {
                        List<String> intersectElements = serialsData.get(i).getGenres().stream()
                                .filter(generes::contains)
                                .collect(Collectors.toList());
                        Collections.sort(intersectElements);
                        Collections.sort(generes);
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
                    } else {
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
            } else {
                if(generes.get(0) != null) {
                    List<String> intersectElements = serialsData.get(i).getGenres().stream()
                            .filter(generes::contains)
                            .collect(Collectors.toList());
                    Collections.sort(intersectElements);
                    Collections.sort(generes);
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
                } else {
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
            if (year != null) {
                Integer yearNumber = new Integer(moviesData.get(i).getYear());
                if (yearNumber.toString().equals(year)) {
                    if (generes.get(0) != null) {
                        List<String> intersectElements = moviesData.get(i).getGenres().stream()
                                .filter(generes::contains)
                                .collect(Collectors.toList());
                        Collections.sort(intersectElements);
                        Collections.sort(generes);
                        if (generes.equals(intersectElements)) {
                            movies.add(moviesData.get(i));

                        }
                    } else {
                        movies.add(moviesData.get(i));
                    }
                }
            } else {
                if (generes.get(0) != null) {
                    List<String> intersectElements = moviesData.get(i).getGenres().stream()
                            .filter(generes::contains)
                            .collect(Collectors.toList());
                    Collections.sort(intersectElements);
                    Collections.sort(generes);
                    if (generes.equals(intersectElements)) {
                        movies.add(moviesData.get(i));

                    }
                } else {
                    movies.add(moviesData.get(i));
                }
            }
        }

        if (sortType.equals("asc")) {
            Collections.sort(movies, new DurationAscendingSortMovie());
        } else {
            // iar cu reverse
            Collections.sort(movies, new DurationAscendingSortMovie().reversed());
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
    public String mostViewdMovies(final String year, final List<String> genres,
                                final String sortType, final int number) {
        ArrayList<MovieInputData> movies = new ArrayList<MovieInputData>();
        //for each movie
        for (int i = 0; i < moviesData.size(); i++) {
            if(year != null) {
                Integer yearNumber = new Integer(moviesData.get(i).getYear());
                if (yearNumber.toString().equals(year)) {
                    if(genres.get(0) != null) {
                        List<String> intersectElements = moviesData.get(i).getGenres().stream()
                                .filter(genres::contains)
                                .collect(Collectors.toList());
                        Collections.sort(intersectElements);
                        Collections.sort(genres);
                        if (genres.equals(intersectElements)) {
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
                    } else {
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
            } else {
                if(genres.get(0) != null) {
                    List<String> intersectElements = moviesData.get(i).getGenres().stream()
                            .filter(genres::contains)
                            .collect(Collectors.toList());
                    Collections.sort(intersectElements);
                    Collections.sort(genres);
                    if (genres.equals(intersectElements)) {
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
                } else {
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
            // iar cred ca e cu reversed
            Collections.sort(movies, new ViewsAscendingSortMovie().reversed());
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
            if (year != null) {
                Integer yearNumber = new Integer(serialsData.get(i).getYear());
                if (yearNumber.toString().equals(year)) {
                    if(generes.get(0) != null) {
                        List<String> intersectElements = serialsData.get(i).getGenres().stream()
                                .filter(generes::contains)
                                .collect(Collectors.toList());
                        Collections.sort(intersectElements);
                        Collections.sort(generes);
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
                    } else {
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
            } else {
                if(generes.get(0) != null) {
                    List<String> intersectElements = serialsData.get(i).getGenres().stream()
                            .filter(generes::contains)
                            .collect(Collectors.toList());
                    Collections.sort(intersectElements);
                    Collections.sort(generes);
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
                } else {
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
            // cu reverse
            Collections.sort(serials, new ViewAscendingSortserial().reversed());
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

    /**
     * gets the most active number users and sorts them by sorTYpe
     */
    public String mostActiveUsers(final int number, final String sortType) {
        ArrayList<UserInputData> users = new ArrayList<UserInputData>();
        for (int i = 0; i < usersData.size(); i++) {
            usersData.get(i).setNumberofRatings();
            if (usersData.get(i).getNumberOfRatings() > 0) {
                users.add(usersData.get(i));
            }
        }
        if (sortType.equals("asc")) {
            Collections.sort(users, new RatingsAscendingSortUser());
        } else {
            Collections.sort(users, new RatingsAscendingSortUser().reversed());
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

    /**
     * return the standard recommendation
     */
    public String getStandardRecommendation(final String username) {
        String message = new String("StandardRecommendation result: ");
        for (int i = 0; i < usersData.size(); i++) {
            if (usersData.get(i).getUsername().equals(username)) {
                for (int j = 0; j < moviesData.size(); j++) {
                    String title = moviesData.get(j).getTitle();
                    if (usersData.get(i).getHistory().get(title) == null) {
                        message += title;
                        return message;

                    }
                }
                for (int j = 0; j < serialsData.size(); j++) {
                    String title = serialsData.get(j).getTitle();
                    if (usersData.get(i).getHistory().get(title) == null) {
                        message += title;
                        return message;

                    }
                }
            }
        }
        message = new String("StandardRecommendation cannot be applied!");
        return message;
    }

    /**
     * returns a list of videos sorted by their raiting in descending order
     */
    public ArrayList<ShowInput> getVideosByRating() {
        ArrayList<ShowInput> videos = new ArrayList<ShowInput>();
        for (int i = 0; i < moviesData.size(); i++) {
            String title = moviesData.get(i).getTitle();
            for (int j = 0; j < usersData.size(); j++) {
                Map<String, Double> movieRatings = usersData.get(j).getRatedMovies();
                if (movieRatings != null ) {
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
        // Trebuie facut si pentru serial
        for (int i = 0; i < serialsData.size(); i++) {
            serialsData.get(i).setRating();
            serialsData.get(i).setPosition(moviesData.size() + i - 1);
            videos.add(serialsData.get(i));
        }
        Collections.sort(videos, new RatingDescendingSort());
        return videos;
    }

    /**
     * returns the best unseen movie for the user username
     */
    public String getBestUnseen(final String username) {
        String message = new String("BestRatedUnseenRecommendation result: ");
        // am adaugat aici
        for(int i = 0; i < moviesData.size(); i++) {
            moviesData.get(i).resetRating();
        }
        for(int i = 0; i < serialsData.size(); i++) {
            serialsData.get(i).resetRating();
        }
        ArrayList<ShowInput> videos = getVideosByRating();
        System.out.println("BEST UNSEEEEEN");
        for (int i = 0; i < videos.size(); i++) {
            System.out.println(videos.get(i).getTitle() + " "+ videos.get(i).getRating());
        }
        System.out.println("\n\n\n");
        for (int i = 0; i < usersData.size(); i++) {
           if (usersData.get(i).getUsername().equals(username)) {
               for (int j = 0; j < videos.size(); j++) {
                   String title = videos.get(j).getTitle();
                   if (usersData.get(i).getHistory().get(title) == null) {
                       message += title;
                       return message;
                   }
               }
           }
        }
        message = new String("BestRatedUnseenRecommendation cannot be applied!");
        return message;
    }

    /**
     * gets the most popular genre first video
     */
    public String getMostPopularVideo(final String username) {
        for (int i = 0; i < usersData.size(); i++) {
            if (usersData.get(i).getUsername().equals(username)) {
                if (usersData.get(i).getSubscriptionType().equals("BASIC")) {
                    String message
                            = new String("PopularRecommendation cannot be applied!");
                    return message;
                } else {
                    break;
                }
            }
        }
        String message = "PopularRecommendation result: ";
        for(int i = 0; i < moviesData.size(); i++)
            moviesData.get(i).setNumberOfViews(0);
        for(int i = 0; i < serialsData.size(); i++)
            serialsData.get(i).setNumberOfViews(0);
        ArrayList<ShowInput> videos = new ArrayList<ShowInput>();
        for (int i = 0; i < moviesData.size(); i++) {
            String title = moviesData.get(i).getTitle();
            for (int j = 0; j < usersData.size(); j++) {
                if (usersData.get(j).getHistory().get(title) != null) {
                    moviesData.get(i).addNumberOfViews(usersData.get(j).getHistory().get(title));
                }
            }
            videos.add(moviesData.get(i));
        }
        for (int i = 0; i < serialsData.size(); i++) {
            String title = serialsData.get(i).getTitle();
            for (int j = 0; j < usersData.size(); j++) {
                if (usersData.get(j).getHistory().get(title) != null) {
                    serialsData.get(i).addNumberOfViews(usersData.get(j).getHistory().get(title));
                }
            }
            videos.add(serialsData.get(i));
        }
        //return videos;

        // for each genre I have the number of views
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < videos.size(); i++) {
            Integer views = videos.get(i).getNumberOfViews();
            ArrayList<String> genres = videos.get(i).getGenres();
            for (int j = 0; j < genres.size(); j++) {
                if (map.get(genres.get(j)) == null) {
                    map.put(genres.get(j), views);
                } else {
                    Integer numberOfViews = map.get(genres.get(j));
                    numberOfViews += views;
                    map.replace(genres.get(j), numberOfViews);
                }
            }
        }
        Set<String> keys = map.keySet();
        ArrayList<Integer> views = new ArrayList<>();
        for (String key : keys) {
            views.add(map.get(key));
        }
        Collections.sort(views, Collections.reverseOrder());
        for (int i = 0; i < usersData.size(); i++) {
            if (usersData.get(i).getUsername().equals(username)) {
                for (int j = 0; j < views.size(); j++) {
                    for (String key : keys) {
                        if (map.get(key).equals(views.get(j))) {
                            String genre = key;
                            for (int k = 0; k < videos.size(); k++) {
                                String title = videos.get(k).getTitle();
                                ArrayList<String> videoGenre = videos.get(k).getGenres();
                                for (int t = 0; t < videoGenre.size(); t++) {
                                    if (videoGenre.get(t).equals(genre)) {
                                        if (usersData.get(i).getHistory().get(title) == null) {
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
        }
        message = new String("PopularRecommendation cannot be applied!");
       return message;
    }
    /**
     * returns a list of videos sorted by their favorite number in descending order
     */
    public ArrayList<ShowInput> getVideosByFavorite() {
        markFavoriteShows();
        ArrayList<ShowInput> videos = new ArrayList<ShowInput>();
        for (int i = 0; i < moviesData.size(); i++) {
            moviesData.get(i).setPosition(i);
            videos.add(moviesData.get(i));
        }
        for (int i = 0; i < serialsData.size(); i++) {
            serialsData.get(i).setPosition(moviesData.size() + i - 1);
            videos.add(serialsData.get(i));
        }
        Collections.sort(videos, new FavoriteDescendingSort());
        return videos;
    }

    /**
     * gets the first most favorite movie that user has not viewd
     */
    public String getMostFavorite(final String username) {
        for (int i = 0; i < usersData.size(); i++) {
            if (usersData.get(i).getUsername().equals(username)) {
                if (usersData.get(i).getSubscriptionType().equals("BASIC")) {
                    String message
                            = new String("FavoriteRecommendation cannot be applied!");
                    return message;
                } else {
                    break;
                }
            }
        }
        ArrayList<ShowInput> videos = getVideosByFavorite();
        String message = new String("FavoriteRecommendation result: ");
        for (int i = 0; i < usersData.size(); i++) {
            if (usersData.get(i).getUsername().equals(username)) {
                for (int j = 0; j < videos.size(); j++) {
                    String title = videos.get(j).getTitle();
                    if (usersData.get(i).getHistory().get(title) == null) {
                        message += title;
                        return message;
                    }
                }
            }
        }
        message = new String("FavoriteRecommendation cannot be applied!");
        return message;

    }
    /**
     * gets the search list
     */
    public String getSearchList(final String username, String genre) {
        for (int i = 0; i < usersData.size(); i++) {
            if (usersData.get(i).getUsername().equals(username)) {
                if (usersData.get(i).getSubscriptionType().equals("BASIC")) {
                    String message
                            = new String("SearchRecommendation cannot be applied!");
                    return message;
                } else {
                    break;
                }
            }
        }
        ArrayList<ShowInput> videos = getVideosByRating();
        Collections.sort(videos, new RatingAscendingSort());
        String message = new String("SearchRecommendation result: ");
        ArrayList<String> videoTitles = new ArrayList<String>();
        for (int i = 0; i < usersData.size(); i++) {
            if (usersData.get(i).getUsername().equals(username)) {
                for (int j = 0; j < videos.size(); j++) {
                    String title = videos.get(j).getTitle();
                    ArrayList<String> videoGenre = videos.get(j).getGenres();
                    for(int k = 0; k < videoGenre.size(); k++)
                        if(videoGenre.get(k).equals(genre)) {
                            if (usersData.get(i).getHistory().get(title) == null) {
                                videoTitles.add(title);
                            }
                        }
                }
            }
        }
        if(videoTitles.size() > 0) {
            message += videoTitles.toString();
        } else {
            message = new String("SearchRecommendation cannot be applied!");
        }
        return message;

    }
}
