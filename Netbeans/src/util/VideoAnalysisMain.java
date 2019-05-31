/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author technowings-pc
 */
public class VideoAnalysisMain {

    public ArrayList<Integer[]> groupNumbers(ArrayList<Integer> numbersDurations) {
        ArrayList<Integer[]> arr = new ArrayList<Integer[]>();
          for (int i = 0; i < numbersDurations.size(); i++) {

            System.out.println("Pair " + numbersDurations.get(i));

        }
        if(numbersDurations.size()==0){
            return null;
        }
        Integer prev = numbersDurations.get(0);
        Integer start = numbersDurations.get(0);
        for (int i = 1; i < numbersDurations.size(); i++) {
            Integer current = numbersDurations.get(i);
            if (current - prev > 15) {
                if(prev-start>=1){
                arr.add(new Integer[]{start, prev});
                }
                start = current;
            }
            prev = current;
        }
        arr.add(new Integer[]{start, prev});
        for (int i = 0; i < arr.size(); i++) {
            Integer[] a = (Integer[]) arr.get(i);
            System.out.println("Pair " + a[0] + " " + a[1]);

        }
        return arr;
    }
  public  int totalWidth = 600;
    public    int totalHeight = 50;
    public void drawImage(ArrayList<Integer[]> numbersDurations, long totalDuration) {
    
        BufferedImage bi2 =
                new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D big = (Graphics2D) bi2.getGraphics();
        big.setColor(Color.RED);
        big.drawRect(0, 0, totalWidth - 1, totalHeight - 1);

        for (int i = 0; numbersDurations!=null&&i < numbersDurations.size(); i++) {
            Integer[] a = (Integer[]) numbersDurations.get(i);
            float pix1 = (float) a[0] * totalWidth / (float) totalDuration;
            float pix2 = (float) a[1] * totalWidth / (float) totalDuration;

            big.setPaint(Color.GREEN);
            big.fillRect((int) pix1, 1, (int) (pix2 - pix1), totalHeight - 2);
            System.out.println("[Start1 Start2  "+a[0]+","+a[1]+"] [X1 X2 = "+pix1+" "+pix2+"]");
//                     big.drawLine((int) pix1, 1, (int) pix2, totalHeight-1);
//           if(i==3){
//                   break;
//           }
        }
        try {
            ImageIO.write(bi2, "png", new FileOutputStream("progress.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
    public static void main(String[] args) {
        new VideoAnalysisMain().testDrawImage();
    }
    public void testDrawImage() {
        ArrayList<Integer> arr = new ArrayList<Integer>();

        arr.add(1193);
        arr.add(1194);
        arr.add(1195);
        arr.add(1196);
        arr.add(1197);
        arr.add(1355);
        arr.add(1358);
        arr.add(1364);
        arr.add(1365);
        arr.add(1366);
        arr.add(1399);
        arr.add(1400);
        arr.add(1401);
        arr.add(1402);
        arr.add(1405);
        arr.add(1406);
        arr.add(1407);
        arr.add(1411);
        arr.add(1413);
        arr.add(1414);
        arr.add(1415);
        arr.add(1416);
        arr.add(1417);
        arr.add(1419);
        arr.add(1420);
        arr.add(142);
        arr.add(1422);
        arr.add(1423);
        arr.add(1742);
        arr.add(1743);
        arr.add(1959);
        arr.add(1960);
        arr.add(1971);
        arr.add(1974);
        arr.add(1984);
        arr.add(1985);
        arr.add(1986);
        arr.add(1989);
        arr.add(1990);
        arr.add(1991);
        arr.add(1996);
        arr.add(2250);
        arr.add(2630);
        arr.add(2631);

     
//        ArrayList<Integer[]> num = groupNumbers(arr);
        ArrayList<Integer[]> num = new ArrayList<Integer[]>();
num.add(new Integer[]{0,2});
num.add(new Integer[]{33,34});
num.add(new Integer[]{53,63});
num.add(new Integer[]{78,106});
num.add(new Integer[]{133,280});
num.add(new Integer[]{291,303});
num.add(new Integer[]{315,544});
num.add(new Integer[]{556,592});
num.add(new Integer[]{622,640});
num.add(new Integer[]{656,669});
num.add(new Integer[]{689,702});
num.add(new Integer[]{732,733});
num.add(new Integer[]{748,766});
num.add(new Integer[]{787,799});
num.add(new Integer[]{810,882});
num.add(new Integer[]{984,984});
num.add(new Integer[]{1034,1037});
num.add(new Integer[]{1054,1057});
num.add(new Integer[]{1118,1118});
num.add(new Integer[]{1150,1151});
num.add(new Integer[]{1178,1181});
num.add(new Integer[]{1193,1197});
num.add(new Integer[]{1355,1366});
num.add(new Integer[]{1399,1423});
num.add(new Integer[]{1742,1743});
num.add(new Integer[]{1959,1960});
num.add(new Integer[]{1971,1996});
num.add(new Integer[]{2250,2250});
num.add(new Integer[]{2630,2631});



       drawImage(num, 3220);
    }
}
