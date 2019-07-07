package com.text.search.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Core properties about file.
 *
 * @author vijaykumar.s 08/07/2019.
 */
public class FileData extends FileMetadata {

    private Set<String> wordsInCache = new HashSet<>();

    private int noOfWordsFound;

    private File file;

    public FileData(String fileName, String directory, Long size, boolean fileExists, File file) {
        super(fileName, directory, size, fileExists);
        this.file = file;
    }

    /**
     * List of words which is not found in cache can be searched in the file and add it to the cache.
     *
     * @param words
     * @param file
     * @throws IOException
     */
    public void addWordsInCache(final List<String> words, final File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String fileText = line;
                words.forEach(s -> {
                    if (containsIgnoreCase(fileText, s)) {
                        wordsInCache.add(s.toLowerCase());
                        incrementWordsCount();
                        words.remove(s);
                    }
                });
            }
        }
    }

    /**
     * Search words in the cache menory to keep search faster.
     *
     * @param word
     * @return
     */
    public boolean searchWordsInCache(final String word) {
        if (wordsInCache.contains(word)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean containsIgnoreCase(String inputString, String subString) {
        return inputString.toLowerCase().contains(subString.toLowerCase());
    }


    public Set<String> getWordsInCache() {
        return wordsInCache;
    }

    public void incrementWordsCount() {
        noOfWordsFound = ++noOfWordsFound;
    }


    public Integer getNoOfWordsFound() {
        return noOfWordsFound;
    }

    public File getFile() {
        return file;
    }

    public void clear() {
        noOfWordsFound = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FileData fileData = (FileData) o;

        if (noOfWordsFound != fileData.noOfWordsFound) return false;
        return file.equals(fileData.file);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + noOfWordsFound;
        result = 31 * result + file.hashCode();
        return result;
    }
}
