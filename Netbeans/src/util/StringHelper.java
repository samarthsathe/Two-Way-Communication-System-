package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringHelper {
    public static String preProcess(String videoName){
          videoName = videoName.replaceAll("\\t", "");
            videoName = videoName.replaceAll("\\n", "");
            videoName = videoName.replaceAll(" ", "");
            return videoName;
    }
    public static Date convertDate(String str_date) {
        Date date = null;
        try {
            DateFormat formatter;

            formatter = new SimpleDateFormat("dd/MM/yy,HH:mm:SS");
            date = (Date) formatter.parse(str_date);
            System.out.println("Today is " + date);
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
        }
        return date;
    }

    public static String formatdate(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM HH:mm:ss");
        String s = formatter.format(new Date());
        return s;
    }  

    public static String emptyToStringNull(String d) {
        String ret = d;
        if (ret.equals("")) {
            ret = "NULL";
        }
        return ret;
    }
   public static String converTime(int duration) {
//        int duration=(int) (grabber.getLengthInTime() / 1000000);
        int min = duration / 60;
        int sec = duration % 60;
        String t = "";
        String mins=min+"";
        if (mins .length() == 1) {
            mins="0"+min;

        }
        if (sec + "".length() == 1) {
            t = mins + ":0" + sec;
        } else {
            t = mins + ":" + sec;
        }
        return t;
    }
    public static String n2s(String d) {
        String ret = d;
        if (ret == null) {
            ret = "";
        }
ret=ret.trim();
        return ret;
    }

    public static String n2s(Object d) {
        String dual = "";
        if (d == null) {
            dual = "";
        } else {
            dual = d.toString().trim();
        }

        return dual;
    }
  public static float n2f(Object d) {
          float i = 0;
        if (d != null) {
            String dual = d.toString().trim();
            try {
                i = new Float(dual).floatValue();
            } catch (Exception e) {
                System.out.println("Unable to find float value");
            }
        }
        return i;
    }
    public static boolean n2b(Object d) {
        boolean dual = false;
        if (d == null) {
            dual = false;
        } else {
            try {
                dual = new Boolean(d.toString()).booleanValue();
            } catch (Exception e) {
            }
        }

        return dual;
    }

    public static int n2i(Object d) {
        int i = 0;
        if (d != null) {
            String dual = d.toString().trim();
            try {
                i = new Integer(dual).intValue();
            } catch (Exception e) {
                System.out.println("Unable to find integer value");
            }
        }
        return i;
    }
     public static long n2l(Object d) {
        long i = 0;
        if (d != null) {
            String dual = d.toString().trim();
            try {
                i = new Long(dual).longValue();
            } catch (Exception e) {
                System.out.println("Unable to find integer value");
            }
        }
        return i;
    }

    public static float usualRound(float f) {
        try {
            String flo = (f + "").substring(0, (f + "").indexOf(".") + 3);
            f = new Float(flo).floatValue();
        } catch (Exception e) {
        }
        float result = ((float) ((int) (100 * f))) / 100;
        System.out.println(100 * f);
        if (((int) (1000 * f) % 10) < 5) {
            return result;
        }
        return (result + 0.01f);
    }

    public static void runcommand(String s) {
        try {
            System.out.println("Running command "+s);
            Process p = Runtime.getRuntime().exec(s);   //"cmd /C dir"
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String CovertIdAry2String(int[] id_array) {
        if (id_array != null) {
            String inStr = "";
            for (int i = 0; i < id_array.length; i++) {
                inStr += id_array[i];
                if ((i + 1) < id_array.length) {
                    inStr += ", ";
                }
            }
            return (inStr.equals("") ? null : inStr);
        }
        return null;
    }

    public static Date StringToDate(String args) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        try {
            today = df.parse(args);
            System.out.println("Today = " + df.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return today;
    }

    public static String DateFormatToDDMMYYYY(Date today) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String stoday = "";
        try {
            if (today != null) {
                stoday = df.format(today);
            }
            System.out.println("Today = " + df.format(today));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stoday;
    }

    public static void main(String[] args) {
        StringHelper.StringToDate("20/12/2009");
        StringHelper.DateFormatToDDMMYYYY(new Date());
     
    }
}
