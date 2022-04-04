package com.jared.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandUtils {
    
    private static final long INIT_MILLIS = System.currentTimeMillis();

    /**
     * Executes a command and returns the response as a String.
     *
     * @param command   String command to execute.
     * @param logOutput Input True if logging of the response output is desired, else input False.
     * @return The response String returned by the command.
     */
    public static String executeCommand(String command, boolean logOutput) {
        System.out.println("Command: " + command);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true); //outputs errors and normal output to the same stream

        processBuilder.command("cmd.exe", "/c", command);

        StringBuilder stringBuilder = new StringBuilder();
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (logOutput) {
                    System.out.println(line);
                }
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void executeCommandLogToFile(String command, String logFile) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true); //outputs errors and normal output to the same stream
        
        String filePath  = "target/command-logs/"  + INIT_MILLIS + "/" + System.currentTimeMillis() + "_" + logFile;
        FileService.ensureFilePathExists(filePath);
        processBuilder.redirectOutput(new File(filePath));

        System.out.println("Command: " + command);
        processBuilder.command("cmd.exe", "/c", command);

        try {
            Process process = processBuilder.start();
            process.getOutputStream();
            process.waitFor(); //wait for the process to finish before continuing
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }
}
