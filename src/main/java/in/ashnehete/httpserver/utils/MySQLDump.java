package in.ashnehete.httpserver.utils;

import java.util.ArrayList;
import java.util.List;

public class MySQLDump {

    public static List<String> getMySQLCheckpointCommand(String host, String username, String password, String dest) {
        List<String> command = new ArrayList<>();
        command.add("mysqldump");
        command.add("-u");
        command.add(username);
        if (password != null) {
            command.add("-p");
            command.add(password);
        }
        command.add("-h");
        command.add(host);
        command.add("--all-databases");
        command.add("--result-file=" + dest);
        return command;
    }

    public static List<String> getMySQLRestoreCommand(String host, String username, String password) {
        List<String> command = new ArrayList<>();
        command.add("mysql");
        command.add("-u");
        command.add(username);
        if (password != null) {
            command.add("-p");
            command.add(password);
        }
        command.add("-h");
        command.add(host);
        return command;
    }
}
