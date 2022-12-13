package in.ashnehete.httpserver.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ProcessRuntime {

    public static ProcessResult executeCommand(List<String> command, String stdInFile) throws IOException, InterruptedException {
        StringBuilder result = new StringBuilder();

        ProcessBuilder builder = new ProcessBuilder(command);

        if (stdInFile != null) {
            builder.redirectInput(new File(stdInFile));
        }

        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int ret = process.waitFor();

        return new ProcessResult(result.toString(), ret);
    }

    public static boolean run(List<String> command, String stdInFile) {
        System.out.println("Command: " + command);
        ProcessResult result;
        try {
            result = ProcessRuntime.executeCommand(command, stdInFile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Command return value: " + result);
        return result.getRetCode() == 0;
    }

    public static boolean run(List<String> command) {
        return run(command, null);
    }
}
