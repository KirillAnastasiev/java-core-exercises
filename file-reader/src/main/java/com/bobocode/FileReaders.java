package com.bobocode;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * {@link FileReaders} privides an API that allow to read whole file into a {@link String} by file name.
 */
public class FileReaders {

    /**
     * Returns a {@link String} that contains whole text from the file specified by name.
     *
     * @param fileName a name of a text file
     * @return string that holds whole file content
     */
    public static String readWholeFile(String fileName) {
        try {
            return Files.readString(createPathFromFileName(fileName));
        } catch (IOException | URISyntaxException e) {
            throw new FileReaderException("Cannot read file: " + fileName, e);
        }
    }

    private static Path createPathFromFileName(String fileName) throws URISyntaxException {
        Objects.requireNonNull(fileName);
        URI fileURI = FileReaders.class
                .getClassLoader()
                .getResource(fileName)
                .toURI();

        return Paths.get(fileURI);
    }
}
