/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class FileHelper {

    public static File[] getFileList(String dirPath) {
        File f = new File(dirPath);
        try {
            System.out.println("Canonical Path " + f.getCanonicalPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        FilenameFilter textFilter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".txt")) {
                    return true;
                } else {
                    return false;
                }
            }
        };


        File[] a = f.listFiles(textFilter);
        if (a != null) {
            System.out.println(" Got Files " + a.length);
        }
        return a;
    }

    public static void serializeObject(HashMap props) {
        try {
            ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream("configure"));
            ois.writeObject(props);
            ois.flush();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<String[]>> getFilesCombo(String DIR_PATH) {
        File[] files = getFileList(DIR_PATH);
        String combo = "";
        ArrayList<ArrayList<String[]>> all = new ArrayList<ArrayList<String[]>>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().indexOf("comments") != -1) {
                ArrayList<String[]> data = parseFile(files[i].getAbsolutePath());
                all.add(data);
                System.out.println("Got Data " + data);
            }
        }
        return all;
    }

    public static HashMap loadFileMap(String fileName) {
        File f = new File(fileName);
        System.out.println(f.getAbsolutePath());
        StringBuffer arr = readFileContent(f.getAbsolutePath());
        System.out.println(" arr " + arr.toString());
        String[] tokens = arr.toString().split("\n");
        HashMap map = new HashMap();
        for (int i = 0; i < tokens.length; i++) {
            String string = tokens[i];
            map.put(string.trim().toLowerCase(), 1);
        }
        System.out.println("map " + map);
        return map;
    }

    public static HashMap loadAffineMap(String fileName) {
        File f = new File(fileName);
        System.out.println(f.getAbsolutePath());
        StringBuffer arr = readFileContent(f.getAbsolutePath());
        System.out.println(" arr " + arr.toString());
        String[] tokens = arr.toString().split("\n");
        HashMap map = new HashMap();
        for (int i = 0; i < tokens.length; i++) {
            String[] string = tokens[i].split("\\s+");
            if (string.length >= 2) {
                int weiatage = 0;
                String key = string[0];

                weiatage = StringHelper.n2i(string[1]);

                map.put(key.trim().toLowerCase(), weiatage);
            }
        }
        System.out.println("map " + map);
        return map;
    }

    public static ArrayList<String[]> parseFile(String fileName) {
        ArrayList<String[]> arr = new ArrayList<String[]>();
        StringBuffer sb = readFileContent(fileName);
        String[] tokens = sb.toString().split("\\|1234\\|");
        for (int i = 0; i < tokens.length; i++) {
            String string = tokens[i];
            String[] keyTweet = string.split("\\|\\|");
              arr.add(keyTweet);
           

        }

        return arr;
    }

    public static StringBuffer readFileContent(String filepath) {

        InputStream is = null;
        int i;
        char c;
        StringBuffer sb = new StringBuffer();
        try {
            File f=new File(filepath);
            try{
            System.out.println("File Path "+f.getCanonicalPath());
            }catch(Exception e){
                
            }
            // new input stream created
            is = new FileInputStream(filepath);

            System.out.println("Characters printed:");
            byte[] b = new byte[1024];
            // reads till the end of the stream
            while ((i = is.read(b)) != -1) {
                // converts integer to character

                String s = new String(b);
                sb.append(s);
            }
        } catch (Exception e) {

            // if any I/O error occurs
            e.printStackTrace();
        } finally {

            // releases system resources associated with this stream
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return sb;
    }

    public static HashMap deserializeObject() {
        HashMap hmp = new HashMap();
        try {
            File f = new File("configure");
            if (f.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("configure"));
                Object obj = ois.readObject();
                if (obj instanceof HashMap) {
                    hmp = (HashMap) obj;
                }
                ois.close();
            } else {
                hmp = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hmp;
    }

    public static void setEnteredValues(Class c, HashMap hmp) {
        try {
            System.out.println("hmp " + hmp);
            Field[] arr = c.getFields();
            for (int i = 0; arr != null && i < arr.length; i++) {
                Field field = arr[i];
                System.out.println(field.getName());
                String value = StringHelper.n2s(hmp.get(field.getName()));

                if (value.length() > 0 && field.toString().indexOf("final") == -1) {

                    if (field.getType() == String.class) {
                        field.set(null, value);
                    } else if (field.getType() == boolean.class) {
                        field.setBoolean(null, StringHelper.n2b(value));
                    } else if (field.getType() == int.class) {
                        field.setInt(null, StringHelper.n2i(value));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap getClassFields(Class c) {
        HashMap hmp = new HashMap();
        try {

            Field[] arr = c.getFields();
            for (int i = 0; arr != null && i < arr.length; i++) {
                Field field = arr[i];
                String value = field.getName();
                Object o = field.get(null);
                hmp.put(field.getName(), o.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hmp;
    }
}
