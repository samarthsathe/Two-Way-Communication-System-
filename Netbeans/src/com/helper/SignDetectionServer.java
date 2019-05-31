/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helper;

import java.net.*;
import java.io.*;
import util.ServerConstants;

public class SignDetectionServer {

    public static void main(String[] args) {
        checkSign("C:\\work\\project\\ASL\\Netbeans\\Android-Phone-Images\\1558790519664.png");
    }

    public static String checkSign(String path) {
        String hostname = ServerConstants.url;
        int port = ServerConstants.port;

        try (Socket socket = new Socket(hostname, port)) {

            OutputStream output = socket.getOutputStream();
            byte[] data = path.getBytes();
            output.write(data);

            InputStream input = socket.getInputStream();
            StringBuffer sb = new StringBuffer();
               data = new byte[1024];
            int len = input.read(data);
            if (len != -1) {
                System.out.println(new String(data, 0, len));
                return (new String(data, 0, len));

            } else {
                return "unRecognized";
            }
        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
        return null;
    }
}