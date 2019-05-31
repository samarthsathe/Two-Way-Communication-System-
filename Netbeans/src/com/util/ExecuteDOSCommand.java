package com.util;

import gui.forms.MainFrame;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import server.CommonLanMonitoringServer;

import server.LanMonitoringClient;

public class ExecuteDOSCommand {

    public static boolean scanon = false;


    // now will execute commands from cmd
    public StringBuffer getCommandData(String methodName,
            String commandParameter, String chatMessage) {
        System.out.println("key " + methodName);
        System.out.println("appName " + commandParameter);
        System.out.println("Message " + chatMessage);
        StringBuffer arr = new StringBuffer();
        try {

            if (methodName.equals("getData")) {
                System.out.println("Display Sign");
               CommonLanMonitoringServer.mainFrame.jTextField1.setText(chatMessage);
                CommonLanMonitoringServer.mainFrame.voiceToText();
                System.out.println("Done");

//			MessageWindow.ipAddress = IP;
//			new MessageWindow(IP, name, chatMessage.toString())
//					.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static StringBuffer getCommandOutput(String cmd, int skipLine) {
        StringBuffer sb = new StringBuffer();
        try {
            Process p = Runtime.getRuntime().exec(cmd);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(
                    p.getErrorStream()));

            // read the output from the command

            String s = null;
            for (int i = 0; i < skipLine; i++) {
                s = stdInput.readLine();
                System.out.println("skipline o/p:" + s);
            }
            while ((s = stdInput.readLine()) != null) {
                if (s.trim().length() > 0) {
                    s = s.replace("\\", "");
                    sb.append(s);

                    System.out.println("Command Output " + s);
                    sb.append('\n');
                }
            }
            // read any errors from the attempted command
            System.out.println("Command Executed...");
            // System.out.println("Here is the standard error of the command (if
            // any):\n");

            while ((s = stdError.readLine()) != null) {
                if (s.trim().length() > 0) {
                    System.out.println("ERROR Output " + s);
                }
            }

        } catch (NullPointerException e) {

            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return sb;
    }

    public static void main(String[] args) {
        new ExecuteDOSCommand().getCommandData("scan", "", "");
//		new ExecuteDOSCommand().getFilesList("C:/Program Files (x86)/Free MP3 Cutter/Easy Audio Cutter/Easy Audio Cutter/");
        new ExecuteDOSCommand().checkRemovableDisk();
//		StringBuffer str=getCommandOutput("taskkill", 3);
//		System.out.println("OPssafas");
//		System.out.println(str);
//		checkRemovableDisk();
    }

    public static String checkRemovableDisk() {
        String success = "";
        File[] roots = File.listRoots();
        for (int i = 0; i < roots.length; i++) {
            File f = roots[i];
            String systemType = FileSystemView.getFileSystemView().getSystemTypeDescription(f);
            String systemDisplayName = FileSystemView.getFileSystemView().getSystemDisplayName(f);
            System.out.println(systemType);
            System.out.println(systemDisplayName);
            if (systemType.equalsIgnoreCase("Removable Disk")) {
                if (systemDisplayName.length() == 0) {
                    success = "Pendrive Detected";
                } else {
                    success = systemDisplayName;
                }
                break;
            }
        }
        System.out.println("Op " + success);
        return success;
    }

    // Get Machine Drives
    public String[] getDrives() {
        // getting drive list from server
        File file[] = File.listRoots();
        String[] f = new String[file.length];
        int cnt = -1;
        for (File files : file) {
            // Skip Floppy drive
            if (!FileSystemView.getFileSystemView().isFloppyDrive(files)) {
                f[++cnt] = files.getPath().replace("\\", "");
                System.out.println("drive list " + f[cnt]);
            }


        }
        return f;

    }
    // Get Files in  a particular Folder - 

    public File[] getFilesList(String path) {
        File dir = new File(path);
//		if(dir.isHidden()){
//			return new String[]{};
//		}
        File[] children = dir.listFiles();

        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                if (children[i].isFile()) {
                    if (children[i].isHidden()) {
                        children[i] = null;
                    }
                } else {
                    try {
                        if (children[i].list() == null) {
                            children[i] = null;
                        } else {
                            System.out.println(children[i].getName());
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        children[i] = null;
                    }
                }
            }
        }
//		String[] files = dir.list();
        return children;
    }
    // Capture Screenshot

    public static BufferedImage takeScreenShot() {
        BufferedImage bufferedImage = null;
        try {
            Robot robot = new Robot();
            Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            bufferedImage = robot.createScreenCapture(captureSize);
        } catch (Exception e) {
            System.err.println("Someone call a doctor!");
        }
        return bufferedImage;
    }
}
