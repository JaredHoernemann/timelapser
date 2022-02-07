package com.jared;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class FileService {
    
    private static final String COPIED_FILES_DIR = "target/copied-files/";
    
    public static List<File> copyFiles(List<File> originalFiles) {
        List<File> copies = new ArrayList<>();
        for (File file : originalFiles) {
            File c = copyFile(file);
            copies.add(c);
        }
        return copies;
    }
    
    public static File copyFile(File original) {
        FileService.ensureDirectoryExists(COPIED_FILES_DIR);
        
        try {
            String filePath = COPIED_FILES_DIR + original.getName();
            File copy = new File(filePath);
            FileUtils.copyFile(original, copy);
            System.out.println("Copied file: " + copy.getName());
            return copy;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to copy file: " + original);
        }
    }

    public static boolean ensureFilePathExists(String filePath) {
        boolean created;
        try {
            File file = new File(filePath);

            String dirPath = file.getParent();
            ensureDirectoryExists(dirPath);

            created = file.createNewFile();
            return created;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    public static void ensureDirectoryExists(String directoryPath) {
        String[] fileParts = directoryPath.split(Pattern.quote(File.separator));

        if (fileParts[fileParts.length - 1].contains(".")) {
            fileParts = Arrays.copyOf(fileParts, fileParts.length - 1);
            directoryPath = String.join(File.separator, fileParts);
        }

        File file = new File(directoryPath);
        if (file.isFile()) {
            file = file.getParentFile();
        } else if (file.isDirectory() && file.exists()) {
            return;
        }
        boolean directoryMade = file.mkdirs();
        if (!directoryMade) {
            System.out.println(("Failed to create directory: " + directoryPath));
        } else {
            System.out.println("Created directory: " + directoryPath);
        }
    }


    /**
     * Returns all files in a directory as a list of File objects.
     *
     * @param directoryPath String path of directory.
     * @return List of File objects.
     */
    public static List<File> getAllFilesInDirectory(String directoryPath) {
        File dir = new File(directoryPath);
        try {
            return Arrays.asList(Objects.requireNonNull(dir.listFiles()));
        } catch (NullPointerException e) {
            throw new IllegalStateException("Directory not found: " + directoryPath);
        }
    }

    /**
     * Returns True if no files are present in a given directory.
     *
     * @param directoryPath String path of directory to check, eg. src/test/resources/
     * @return Boolean True if empty, false if not empty.
     */
    public static Boolean isDirectoryEmpty(String directoryPath) {
        Boolean empty = null;
        try {
            empty = Files.list(Paths.get(directoryPath)).findAny().isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return empty;
    }


}
