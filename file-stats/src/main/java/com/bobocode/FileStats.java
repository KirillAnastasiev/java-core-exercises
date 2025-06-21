package com.bobocode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link FileStats} provides an API that allow to get character statistic based on text file. All whitespace characters
 * are ignored.
 */
public class FileStats {

    private final Path file;

    private Map<Character, Long> charactersFrequencyStatistics;
    private Character mostPopularCharacter;

    private FileStats(String fileName) throws FileStatsException {
        if (Objects.isNull(fileName)) {
            throw new FileStatsException("Argument 'fileName' is null");
        }

        Path path = createPathFromFileName(fileName);
        if (Files.notExists(path)) {
            throw new FileStatsException("File '" + fileName + "' not exists");
        }

        this.file = path;
    }

    private Path createPathFromFileName(String fileName) throws FileStatsException {
        try {
            URL fileURL = FileStats.class.getClassLoader().getResource(fileName);
            if (Objects.isNull(fileURL)) {
                throw new FileStatsException("Cannot find file '" + fileName + "'");
            }

            return Paths.get(fileURL.toURI());
        } catch (URISyntaxException e) {
            throw new FileStatsException("Cannot open file: '" + fileName + "'", e);
        }
    }

    /**
     * Creates a new immutable {@link FileStats} objects using data from text file received as a parameter.
     *
     * @param fileName input text file name
     * @return new FileStats object created from text file
     */
    public static FileStats from(String fileName) {
        return new FileStats(fileName);
    }

    /**
     * Returns a number of occurrences of the particular character.
     *
     * @param character a specific character
     * @return a number that shows how many times this character appeared in a text file
     */
    public int getCharCount(char character) {
        if (Objects.isNull(charactersFrequencyStatistics)) {
            initCharactersFrequencyStatistics();
        }

        return charactersFrequencyStatistics.get(character).intValue();
    }

    /**
     * Returns a character that appeared most often in the text.
     *
     * @return the most frequently appeared character
     */
    public char getMostPopularCharacter() {
        if (Objects.isNull(mostPopularCharacter)) {
            initMostPopularCharacter();
        }

        return mostPopularCharacter;
    }

    /**
     * Returns {@code true} if this character has appeared in the text, and {@code false} otherwise
     *
     * @param character a specific character to check
     * @return {@code true} if this character has appeared in the text, and {@code false} otherwise
     */
    public boolean containsCharacter(char character) {
        if (Objects.isNull(charactersFrequencyStatistics)) {
            initCharactersFrequencyStatistics();
        }

        return charactersFrequencyStatistics.containsKey(character);
    }

    private void initCharactersFrequencyStatistics() {
        try {
            charactersFrequencyStatistics = Files.readString(file)
                    .chars()
                    .filter(cp -> !Character.isWhitespace(cp))
                    .mapToObj(cp -> (char) cp)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        } catch (IOException e) {
            throw new FileStatsException("Cannot read file '" + file.getFileName() + "'");
        }
    }

    private void initMostPopularCharacter() {
        if (Objects.isNull(charactersFrequencyStatistics)) {
            initCharactersFrequencyStatistics();
        }

        mostPopularCharacter = charactersFrequencyStatistics.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new FileStatsException("There are no characters in file '" + file + "'"));
    }
}
