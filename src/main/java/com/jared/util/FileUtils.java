package com.jared.util;

import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileUtils {

    private static final long ONE_HOUR_MILLIS = 3600000; //num millis in one hour

    public static List<File> copyFiles(List<File> originalFiles) {
        List<File> copies = new ArrayList<>();
        for (File file : originalFiles) {
            File c = copyFile(file);
            copies.add(c);
        }
        return copies;
    }

    public static String createTempDirectory(String prefix) {
        try {
            return Files.createTempDirectory("timelapse").toFile().getAbsolutePath() + "/";
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public static List<File> filterOnlyPictures(List<File> files) {
        return files.stream()
                .filter(f -> f.getName().endsWith(".png")
                        || f.getName().endsWith(".jpg")
                        || f.getName().endsWith("JPG")
                        || f.getName().endsWith("PNG"))
                .collect(Collectors.toList());
    }

    public static List<File> sortFilesByLastModified(List<File> files) {
        /*
        sort files by last modified
         */
        File[] array = new File[files.size()];
        files.toArray(array);
        Arrays.sort(array, Comparator.comparingLong(File::lastModified));
        return Arrays.asList(array);
    }

    public static List<File> getMostRecentFiles(List<File> files, int numHours) {
        long cutOffMillis = System.currentTimeMillis() - (numHours * ONE_HOUR_MILLIS);

        List<File> mostRecentPics = new ArrayList<>();
        for (File f : files) {
            long lastModifiedMillis = ImageUtils.getLastModifiedMillis(f);
            if (lastModifiedMillis >= cutOffMillis) {
                mostRecentPics.add(f);
            }
        }
        return mostRecentPics;
    }

    public static File copyFile(File original) {
        try {
            String tmpDir = Files.createTempDirectory("timelapse").toFile().getAbsolutePath() + "/";
            System.out.println(tmpDir);
            return copyFile(original, tmpDir, "");
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    
    public static File copyFile(File original, String toDir, String toName) {
        FileUtils.ensureDirectoryExists(toDir);

        String filePath;
        try {
            if (Strings.isNullOrEmpty(toName)) {
                filePath = toDir + original.getName();
            } else {
                filePath = toDir + toName;
            }
            File copy = new File(filePath);
            org.apache.commons.io.FileUtils.copyFile(original, copy);
            System.out.println("Copied file " + original.getName() + " to: " + copy.getAbsolutePath());
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

    public static void moveFile(String source, String target) {
        try {
            Path temp = Files.move(Paths.get(source), Paths.get(target));
            System.out.println("Moved file: " + source + " -> " + target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a text file and writes list of Strings to it with new lines after each.
     *
     * @param textLines List of String values.
     * @param filePath  String file path of text file being created.
     */
    public static void writeToFile(List<String> textLines, String filePath) {
        File file = new File(filePath);
        try {
            org.apache.commons.io.FileUtils.writeLines(file, UTF_8.toString(), textLines);
            System.out.println("Wrote to file: " + filePath);
        } catch (IOException e) {
        }
    }

    /**
     * Writes a String to a file.
     *
     * @param string   String value to write.
     * @param filePath String file path to write to.
     */
    public static void writeToFile(String string, String filePath) {
        //uses overloaded method logging statements
        List<String> list = new ArrayList<>();
        list.add(string);
        writeToFile(list, filePath);
    }


    /**
     * Note: the javadoc of Files.readAllLines says it's intended for small
     * files. But its implementation uses buffering, so it's likely good
     * even for fairly large files.
     *
     * @param filePath Name of the file to read, including the path
     * @return List of Strings representing lines in the file
     */
    public static List<String> readSmallTextFile(String filePath) {
        return readSmallTextFile(filePath, UTF_8);
    }

    /**
     * Note: the javadoc of Files.readAllLines says it's intended for small
     * files. But its implementation uses buffering, so it's likely good
     * even for fairly large files.
     *
     * @param filePath Name of the file to read, including the path
     * @param charset  {@link Charset} Charset to read text file as.
     * @return List of Strings representing lines in the file
     */
    private static List<String> readSmallTextFile(String filePath, Charset charset) {
        try {
            Path path = Paths.get(filePath);
            return Files.readAllLines(path, charset);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }


    /**
     * Returns text in a file as a String.
     *
     * @param filePath String path of file.
     * @return String text within file.
     */
    public static String readFileAsString(String filePath) {
        List<String> fileLines = readSmallTextFile(filePath);
        StringBuilder allLines = new StringBuilder();

        for (String line : fileLines) {
            allLines.append(line);
        }

        return allLines.toString();
    }


    /**
     * Deletes all files in a directory.
     *
     * @param directoryPath String path of directory.
     */
    public static void deleteAllFilesInDirectory(String directoryPath) {
        List<File> files = getAllFilesInDirectory(directoryPath);

        int count = 0;
        for (File file : files) {
            boolean deleted = file.delete();
            if (deleted) {
                count++;
            }
        }
    }
}
