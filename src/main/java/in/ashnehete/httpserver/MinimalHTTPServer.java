package in.ashnehete.httpserver;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MinimalHTTPServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/execute", new ExecuteHandler());
        server.createContext("/checkpoint", new CheckpointHandler());
        server.createContext("/restore", new RestoreHandler());
        server.start();
        System.out.println("Started HTTP Server on port 8000...");
    }
}