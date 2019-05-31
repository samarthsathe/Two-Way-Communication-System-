package server;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.util.ExecuteDOSCommand;
import com.util.ServerConstants;
import com.util.SimpleCryptoAndroidJava;
import com.util.StringHelper;
import gui.forms.MainFrame;
import gui.forms.TextSpeech;

public class LanMonitoringClient {

    public static BufferedImage screenshot = null;

    public static void main(String[] args) throws IOException {
        launchLanMonitoringClient();
    }

    public static void launchLanMonitoringClient() throws IOException {
        InetSocketAddress addr = new InetSocketAddress(9988);
        HttpServer server = HttpServer.create(addr, 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("LanMonitoringClient  is listening on port 9988");
        ServerConstants.MY_HOST_NAME = ExecuteDOSCommand.getCommandOutput(
                "hostname", 0).toString();
    }
}

class MyHandler implements HttpHandler {

    public ExecuteDOSCommand ecmd = new ExecuteDOSCommand();

    public void handle(HttpExchange exchange) {
        OutputStream responseBody = exchange.getResponseBody();
        try {
            String uri = exchange.getRequestURI().getQuery();

            if (uri != null) {
                uri = URLDecoder.decode(uri);
            }
            System.out.println("URI " + uri);
            HashMap parameters = new HashMap();
            try {
                String tokens[] = uri.split("&");
                System.out.println("tokens " + tokens.length);
                for (String keyvalue : tokens) {
                    String key = keyvalue.split("=")[0];
                    String value = keyvalue.split("=")[1];
                    System.out.println(key + "=" + value);
                    parameters.put(key, value);
                }
            } catch (Exception e) {
            }
            String methodParameter = StringHelper.n2s(parameters.get("method"));
            String valueParameter = StringHelper.n2s(parameters.get("value"));
            String messageParameter = StringHelper.n2s(parameters.get("message"));

            System.out.println("methodParameter= " + methodParameter);
            System.out.println("messageParameter= " + messageParameter);

            Headers responseHeaders = exchange.getResponseHeaders();
            if (!methodParameter.equalsIgnoreCase("takeScreenshot")) {
                responseHeaders.set("Content-Type", "text/plain");
            }
            exchange.sendResponseHeaders(200, 0);
            StringBuffer sb = new StringBuffer();
            sb = ecmd.getCommandData(methodParameter,
                    valueParameter, messageParameter);
            System.out.println("Response = " + sb);
            responseBody.write(sb.toString().getBytes());

            System.out.print("Response Sent..............\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                responseBody.flush();
                responseBody.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}