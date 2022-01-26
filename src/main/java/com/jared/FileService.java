package com.jared;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class FileService {

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
}
