package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.util.ConnectionManager;
import com.util.ExecuteDOSCommand;
import com.util.ServerConstants;
import com.util.SimpleCryptoAndroidJava;
import gui.forms.MainFrame;

public class CommonLanMonitoringServer {

    public static MainFrame mainFrame = new MainFrame();

    public static void main(String[] args) throws IOException {
        mainFrame.setVisible(true);
        InetSocketAddress addr = new InetSocketAddress(9977);
        HttpServer server = HttpServer.create(addr, 0);
        server.createContext("/", new MyHandlerCommon());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Lan Monitoring Server is listening on port 9977");
        System.out.println("IP Address: " + InetAddress.getLocalHost().getHostAddress());
        ServerConstants.MY_HOST_NAME = ExecuteDOSCommand.getCommandOutput(
                "hostname", 0).toString();
        new Thread() {

            public void run() {
                try {
                    LanMonitoringClient.launchLanMonitoringClient();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        ;
        }.start();
		new Thread() {

            public void run() {
                try {
                    //ConnectionManager.getDatabasePath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

class MyHandlerCommon implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        try {
            long start = System.currentTimeMillis();
            String uri = exchange.getRequestURI().getQuery();

            System.out.println("Server URI " + uri);
            Headers responseHeaders = exchange.getResponseHeaders();
            if (uri != null && uri.indexOf("takeScreenshot") == -1) {
                responseHeaders.set("Content-Type", "text/plain");
            }
            exchange.sendResponseHeaders(200, 0);
            OutputStream responseBody = exchange.getResponseBody();
            if (uri != null) {
                
                if (uri != null && uri.indexOf("getData") != -1) {
                    String data = uri.substring(uri.lastIndexOf("=") + 1);
                    CommonLanMonitoringServer.mainFrame.jTabbedPane3.setSelectedIndex(1);
                    CommonLanMonitoringServer.mainFrame.jTextField1.setText(data + " ");
                    CommonLanMonitoringServer.mainFrame.voiceToText();
                }
            }
            responseBody.write("OK".getBytes());
            responseBody.close();
            long end = System.currentTimeMillis();
            System.out.print("..........Completing Response from here .............."
                    + (end - start));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void executeRequest(String uri, OutputStream responseBody) {
        URL u;
        try {
            System.out.println("URI is " + uri);
            u = new URL(uri);
            URLConnection uc = u.openConnection();
            InputStream is = uc.getInputStream();
            while (true) {
                byte[] b = new byte[1024 * 1];
                int length = is.read(b);
                if (length == -1) {
                    break;
                }
                responseBody.write(b, 0, length);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        } finally {
            try {
                responseBody.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.err.println(e1.getMessage());
            }
        }
    }
}