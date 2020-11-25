package main;

import checker.Checker;
import checker.Checkstyle;
import common.Constants;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();


        //TODO add here the entry point to your implementation
        List<ActorInputData> actorsInputData = new ArrayList<ActorInputData>();
        actorsInputData = input.getActors();

        List<MovieInputData> movieInputData = new ArrayList<MovieInputData>();
        movieInputData = input.getMovies();

        List<SerialInputData> serialInputData = new ArrayList<SerialInputData>();
         serialInputData = input.getSerials();

        //actorsInputData.forEach((v) -> System.out.println(v));

        List<UserInputData> usersInputData = new ArrayList<UserInputData>();
        usersInputData = input.getUsers();

        List<ActionInputData> actionInputData = new ArrayList<ActionInputData>();
        actionInputData = input.getCommands();

        for (int i = 0; i < actionInputData.size(); i++) {
            if (actionInputData.get(i).getActionType().equals("command")) {
                if (actionInputData.get(i).getType().equals("favorite")) {

                    int id = actionInputData.get(i).getActionId();
                    String username = actionInputData.get(i).getUsername();
                    String title = actionInputData.get(i).getTitle();

                    for (int j = 0; j < usersInputData.size(); j++) {
                        if (usersInputData.get(j).getUsername().equals(username)) {
                          String message = usersInputData.get(j).addFavorite(title);
                            arrayResult.add(fileWriter.writeFile(id, null, message));
                            break;

                        }
                    }

                } else if (actionInputData.get(i).getType().equals("view")) {
                    int id = actionInputData.get(i).getActionId();
                    String username = actionInputData.get(i).getUsername();
                    String title = actionInputData.get(i).getTitle();

                    for (int j = 0; j < usersInputData.size(); j++) {
                        if (usersInputData.get(j).getUsername().equals(username)) {
                            String message = usersInputData.get(j).viewShow(title);
                            arrayResult.add(fileWriter.writeFile(id, null, message));
                            break;

                        }
                    }

                } else {
                    int id = actionInputData.get(i).getActionId();
                    String username = actionInputData.get(i).getUsername();
                    String title = actionInputData.get(i).getTitle();
                    Double grade = actionInputData.get(i).getGrade();
                    int season = actionInputData.get(i).getSeasonNumber();

                    for (int j = 0; j < usersInputData.size(); j++) {
                        if (usersInputData.get(j).getUsername().equals(username)) {
                            String message = usersInputData.get(j).addRating(title, season, grade);
                            arrayResult.add(fileWriter.writeFile(id, null, message));
                            break;

                        }
                    }

                }

            } else if (actionInputData.get(i).getActionType().equals("query")) {
                if (actionInputData.get(i).getObjectType().equals("actors")) {
                    if (actionInputData.get(i).getCriteria().equals("average")) {
                        int number = actionInputData.get(i).getNumber();
                        String sortType = actionInputData.get(i).getSortType();
                        int id = actionInputData.get(i).getActionId();
                        String message = input.sortBestActors(number, sortType);
                        arrayResult.add(fileWriter.writeFile(id, null, message));
                    } else if (actionInputData.get(i).getCriteria().equals("awards")) {

                        String sortType = actionInputData.get(i).getSortType();
                        int id = actionInputData.get(i).getActionId();
                        List<List<String>> filters = actionInputData.get(i).getFilters();
                        List<String> awards = filters.get(filters.size() - 1);
                        String message = input.actorsbyAwards(awards, sortType);
                        arrayResult.add(fileWriter.writeFile(id, null, message));
                    } else if (actionInputData.get(i).getCriteria().equals("filter_description")) {
                        String sortType = actionInputData.get(i).getSortType();
                        int id = actionInputData.get(i).getActionId();
                        List<List<String>> filters = actionInputData.get(i).getFilters();
                        List<String> words = filters.get(filters.size() - 2);
                        String message = input.actorsbyKeywords(words, sortType);
                        arrayResult.add(fileWriter.writeFile(id, null, message));
                    }
                } else if (actionInputData.get(i).getObjectType().equals("movies")) {
                    if (actionInputData.get(i).getCriteria().equals("ratings")) {
                        int number = actionInputData.get(i).getNumber();
                        String sortType = actionInputData.get(i).getSortType();
                        int id = actionInputData.get(i).getActionId();
                        List<List<String>> filters = actionInputData.get(i).getFilters();
                        String year = filters.get(0).get(0);
                        List<String> genres = filters.get(1);
                        String message = input.bestRatedMovies(year, genres, sortType, number);
                        arrayResult.add(fileWriter.writeFile(id, null, message));

                    } else if (actionInputData.get(i).getCriteria().equals("favorite")) {
                            int number = actionInputData.get(i).getNumber();
                            String sortType = actionInputData.get(i).getSortType();
                            int id = actionInputData.get(i).getActionId();
                            List<List<String>> filters = actionInputData.get(i).getFilters();
                            String year = filters.get(0).get(0);
                            List<String> genres = filters.get(1);
                            input.markFavoriteShows();
                            String message
                                    = input.mostFavoriteMovies(year, genres, sortType, number);
                            arrayResult.add(fileWriter.writeFile(id, null, message));
                    } else if (actionInputData.get(i).getCriteria().equals("longest")) {
                            int number = actionInputData.get(i).getNumber();
                            String sortType = actionInputData.get(i).getSortType();
                            int id = actionInputData.get(i).getActionId();
                            List<List<String>> filters = actionInputData.get(i).getFilters();
                            String year = filters.get(0).get(0);
                            List<String> genres = filters.get(1);

                            String message = input.longestMovies(year, genres, sortType, number);
                            arrayResult.add(fileWriter.writeFile(id, null, message));

                    } else if (actionInputData.get(i).getCriteria().equals("most_viewed")) {
                            int number = actionInputData.get(i).getNumber();
                            String sortType = actionInputData.get(i).getSortType();
                            int id = actionInputData.get(i).getActionId();
                            List<List<String>> filters = actionInputData.get(i).getFilters();
                            String year = filters.get(0).get(0);
                            List<String> genres = filters.get(1);

                            String message
                                    = input.mostViewdMovies(year, genres, sortType, number);
                            arrayResult.add(fileWriter.writeFile(id, null, message));

                        }
                    } else if (actionInputData.get(i).getObjectType().equals("shows")) {
                        if (actionInputData.get(i).getCriteria().equals("ratings")) {
                            int number = actionInputData.get(i).getNumber();
                            String sortType = actionInputData.get(i).getSortType();
                            int id = actionInputData.get(i).getActionId();
                            List<List<String>> filters = actionInputData.get(i).getFilters();
                            String year = filters.get(0).get(0);
                            List<String> genres = filters.get(1);

                            String message = input.bestRatedShow(year, genres, sortType, number);
                            arrayResult.add(fileWriter.writeFile(id, null, message));

                        } else if (actionInputData.get(i).getCriteria().equals("favorite")) {
                                int number = actionInputData.get(i).getNumber();
                                String sortType = actionInputData.get(i).getSortType();
                                int id = actionInputData.get(i).getActionId();
                                List<List<String>> filters = actionInputData.get(i).getFilters();
                                String year = filters.get(0).get(0);
                                List<String> genres = filters.get(1);
                                input.markFavoriteShows();
                                String message =
                                        input.mostFavoriteShow(year, genres, sortType, number);
                                arrayResult.add(fileWriter.writeFile(id, null, message));
                        } else if (actionInputData.get(i).getCriteria().equals("longest")) {
                            int number = actionInputData.get(i).getNumber();
                            String sortType = actionInputData.get(i).getSortType();
                            int id = actionInputData.get(i).getActionId();
                            List<List<String>> filters = actionInputData.get(i).getFilters();
                            String year = filters.get(0).get(0);
                            List<String> genres = filters.get(1);
                            String message = input.longestShow(year, genres, sortType, number);
                            arrayResult.add(fileWriter.writeFile(id, null, message));
                        } else if (actionInputData.get(i).getCriteria().equals("most_viewed")) {
                            int number = actionInputData.get(i).getNumber();
                            String sortType = actionInputData.get(i).getSortType();
                            int id = actionInputData.get(i).getActionId();
                            List<List<String>> filters = actionInputData.get(i).getFilters();
                            String year = filters.get(0).get(0);
                            List<String> genres = filters.get(1);
                            String message = input.mostViewdShows(year, genres, sortType, number);
                            arrayResult.add(fileWriter.writeFile(id, null, message));
                        }
                    } else if (actionInputData.get(i).getObjectType().equals("users")) {
                        if (actionInputData.get(i).getCriteria().equals("num_ratings")) {
                            int number = actionInputData.get(i).getNumber();
                            String sortType = actionInputData.get(i).getSortType();
                            int id = actionInputData.get(i).getActionId();
                            String message = input.mostActiveUsers(number, sortType);
                            arrayResult.add(fileWriter.writeFile(id, null, message));
                        }

                    }

                }
            }
        //arrayResult.add(fileWriter.writeFile(2, null, "error"));
        fileWriter.closeJSON(arrayResult);
    }
}
