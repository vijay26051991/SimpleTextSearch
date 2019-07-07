package com.text.search;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.text.search.model.FileData;

/**
 * @author vijaykumar.s 7/7/2019
 */
public class Main {

    public static final String QUIT_COMMAND = ":quit";

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No directory given to index.");
        }
        final File indexableDirectory = new File(args[0]);
        Scanner keyboard = new Scanner(System.in);
        File[] files = indexableDirectory.listFiles();
        List<FileData> indexedFiles = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            File inputFile = files[i];
            FileData fileData = new FileData(inputFile.getName(), args[0], inputFile.length(), inputFile.exists(), inputFile);
            indexedFiles.add(fileData);
        }
        while (true) {
            System.out.print("search> ");
            final String line = keyboard.nextLine();
            if (line.equalsIgnoreCase(QUIT_COMMAND)) {
                System.exit(1);
            }
            final Map<String, Integer> rankMap = new HashMap<>();
            indexedFiles.forEach(fileData -> {
                        fileData.clear();
                        final List<String> nonCacheWords = new CopyOnWriteArrayList<>();
                        String[] inputArray = line.split(" ");
                        for (int i = 0; i < inputArray.length; i++) {
                            if (fileData.searchWordsInCache(inputArray[i])) {
                                fileData.incrementWordsCount();
                            } else {
                                nonCacheWords.add(inputArray[i]);
                            }
                        }
                        try {
                            fileData.addWordsInCache(nonCacheWords, fileData.getFile());
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        rankMap.put(fileData.getFileName(), findRank(fileData, inputArray));
                    }
            );
            showRank(rankMap);
        }
    }

    private static void showRank(Map<String, Integer> rankMap) {
        Optional<Map.Entry<String, Integer>> isTextFount
                = rankMap.entrySet().stream().parallel().filter(stringIntegerEntry -> stringIntegerEntry.getValue() > 0).findAny();
        if (isTextFount.isPresent()) {
            rankMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " " + stringIntegerEntry.getValue() + "" + "%"));
        } else {
            System.out.println("no matches found");
        }
    }

    private static int findRank(FileData fileData, String[] inputArray) {
        return (fileData.getNoOfWordsFound() * 100) / inputArray.length;
    }
}
