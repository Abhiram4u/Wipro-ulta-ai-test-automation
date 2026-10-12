package com.ulta.automation.mock;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Built-in Lightweight HTTP Server providing an isolated, reliable mock Ulta testbed.
 * Ensures tests can run deterministically in any offline, proxy, or CI/CD environment
 * without external bot-detection (Akamai/Cloudflare) interference.
 */
public class LocalUltaMockServer {
    private static final Logger logger = LogManager.getLogger(LocalUltaMockServer.class);
    private static HttpServer server;
    private static final int PORT = 8089;
    private static boolean isRunning = false;

    public static synchronized void startServer() {
        if (isRunning && server != null) {
            return;
        }
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", new StaticFileHandler());
            server.setExecutor(null); // default executor
            server.start();
            isRunning = true;
            logger.info("LocalUltaMockServer successfully started on http://localhost:" + PORT + "/");
        } catch (IOException e) {
            logger.error("Failed to start LocalUltaMockServer on port " + PORT, e);
            throw new RuntimeException("Could not start local mock server", e);
        }
    }

    public static synchronized void stopServer() {
        if (server != null && isRunning) {
            server.stop(0);
            isRunning = false;
            logger.info("LocalUltaMockServer stopped.");
        }
    }

    public static boolean isRunning() {
        return isRunning;
    }

    private static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI requestURI = exchange.getRequestURI();
            String path = requestURI.getPath();

            if (path == null || path.equals("/")) {
                path = "/index.html";
            }

            // Remove leading slash
            String resourcePath = "mock_web" + path;
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);

            // Fallback to local filesystem if not found in classpath JAR
            if (is == null) {
                File localFile = new File("src/main/resources/" + resourcePath);
                if (localFile.exists()) {
                    is = new FileInputStream(localFile);
                }
            }

            if (is == null) {
                String response = "<html><body><h1>404 Not Found: " + path + "</h1></body></html>";
                byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(404, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
                return;
            }

            byte[] content = is.readAllBytes();
            is.close();

            String contentType = "text/html; charset=UTF-8";
            if (path.endsWith(".css")) {
                contentType = "text/css";
            } else if (path.endsWith(".js")) {
                contentType = "application/javascript";
            } else if (path.endsWith(".json")) {
                contentType = "application/json";
            }

            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, content.length);
            OutputStream os = exchange.getResponseBody();
            os.write(content);
            os.close();
        }
    }

    public static void main(String[] args) {
        startServer();
        System.out.println("Mock server running on http://localhost:8089/. Press Enter to stop.");
        try {
            System.in.read();
        } catch (IOException ignored) {}
        stopServer();
    }
}
