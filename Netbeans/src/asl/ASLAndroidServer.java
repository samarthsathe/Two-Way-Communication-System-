package asl;

import com.helper.SkinColorClassifier;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import util.opencv.OpenCVHelper;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author technowings
 */
public class ASLAndroidServer {

    public static void main(String[] args) {
        final ASLAndroidServer server = new ASLAndroidServer();
        new Thread() {

            @Override
            public void run() {
                super.run();
                server.startAndroidServer();
            }
        }.start();
        new Thread() {

            @Override
            public void run() {
                super.run();
                server.startResponseServer();
            }
        }.start();

    }
    int ANDROID_SERVER_PORT = 13085;
    int portNoResponse = 13086;
    String detectFace = "";

    public ASLAndroidServer() {
    }

    public void startAndroidServer() {
        try {

            ServerSocket serverSocket = new ServerSocket(ANDROID_SERVER_PORT);

            System.out.println("Server started: " + System.currentTimeMillis() + " on port no " + ANDROID_SERVER_PORT);


            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Got Connection ");
                detectFace = "IN-PROGRESS";
                final InputStream inputStream = socket.getInputStream();
                BufferedImage image = null;
                String name = "Android-Phone-Images/" + System.currentTimeMillis() + ".jpg";
                try {

                    image = ImageIO.read(inputStream);
                    FileOutputStream fos = new FileOutputStream(new File(name));
                    ImageIO.write(image, "jpg", fos);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    socket.close();
                    continue;

                }

                System.out.println("image " + image);
                System.out.println("Going for matching");
                File localFile = new File(name);
                image = ImageIO.read(localFile);
                SkinColorClassifier sc = new SkinColorClassifier();
                BufferedImage bi3 = sc.applySkinColor(image);

                if (sc.totalSkinArea > 100) {
                    detectFace = sendImagePythonServer(localFile.getAbsolutePath());
                    String charStr = detectFace.split("#")[2];
                    localFile.renameTo(new File(localFile.getAbsolutePath() + "/" + charStr + "_" + localFile.getName()));
                } else {
                    detectFace = "No-Gesture";
                    System.err.println("No Gesture Found");//                OutputStream out = socket.getOutputStream();
//                System.out.println("Got Response " + detectFace);
//                out.write(detectFace.getBytes());
//                out.close();
                }
                inputStream.close();

                socket.close();
                System.out.println("Closing Socket");
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void startResponseServer() {
        try {

            ServerSocket serverSocket = new ServerSocket(portNoResponse);

            System.out.println("Response Server started: " + System.currentTimeMillis() + " on port no " + portNoResponse);


            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Response  Got Connection ");
                OutputStream out = socket.getOutputStream();
                System.out.println("Response  Got Response " + detectFace);
                out.write(detectFace.getBytes());
                out.close();
                socket.close();
                System.out.println("Response  Closing Socket");
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String sendImagePythonServer(String filePath) {
        String resp = "";
        try {
            int PYTHON_PORT = 7813;
            Socket socket = new Socket("localhost", PYTHON_PORT);
            java.io.OutputStream os = socket.getOutputStream();
            System.out.println("Sending started");
            //            ImageIO.write(bi, "jpeg", os);
            os.write(filePath.getBytes());
            java.io.InputStream is = socket.getInputStream();
            System.out.println("Receiving started");
            byte[] a = new byte[1024];
            int len = is.read(a);
            resp = new String(a, 0, len);
            System.out.println("Response is " + resp);
            is.close();
            os.close();
            socket.close();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return resp;
    }
}
