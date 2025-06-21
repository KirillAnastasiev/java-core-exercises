package com.bobocode;

import javax.print.URIException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * {@link FileStats} provides an API that allow to get character statistic based on text file. All whitespace characters
 * are ignored.
 */
public class FileStats {

    private FileStats(String fileName) throws FileStatsException {
        if (Objects.isNull(fileName)) {
            throw new FileStatsException("Argument 'fileName' is null");
        }

        Path path = createPathFromFileName(fileName);
        if (Files.notExists(path)) {
            throw new FileStatsException("File '" + fileName + "' not exists");
        }
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
        throw new UnsupportedOperationException("It's your job to make it work!"); //todo
    }

    /**
     * Returns a character that appeared most often in the text.
     *
     * @return the most frequently appeared character
     */
    public char getMostPopularCharacter() {
        throw new UnsupportedOperationException("It's your job to make it work!"); //todo
    }

    /**
     * Returns {@code true} if this character has appeared in the text, and {@code false} otherwise
     *
     * @param character a specific character to check
     * @return {@code true} if this character has appeared in the text, and {@code false} otherwise
     */
    public boolean containsCharacter(char character) {
        throw new UnsupportedOperationException("It's your job to make it work!"); //todo
    }
}
