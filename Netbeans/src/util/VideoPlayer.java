package util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Samarth
 */
public class VideoPlayer {

    int frameRate;
    long lengthInFrames;
    long totalFrameTime;
    Java2DFrameConverter con;
    FFmpegFrameGrabber grabber;
    boolean breakloop = false;
    SwingWorker sw;
    JLabel picturePreview;
    JSlider progressSlider;
    String fileName;

    public VideoPlayer(JLabel picturePreview, JSlider progressSlider, String fileName) {
        this.picturePreview = picturePreview;
        this.progressSlider = progressSlider;
        this.fileName = fileName;
    }

    public void initilalize() {

        grabber = new FFmpegFrameGrabber(fileName);
        try {
            frameIndex = 0;
            grabber.start();
            frameRate = (int) grabber.getFrameRate();
            lengthInFrames = grabber.getLengthInFrames();
            con = new Java2DFrameConverter();
            progressSlider.setMaximum((int) (grabber.getLengthInTime() / 1000000));
            progressSlider.setMinimum(0);
            int duration = (int) (grabber.getLengthInTime() / 1000000);
            int i2 = (int) (grabber.getLengthInTime() / 1000000);
            System.out.println("Frame Rate: " + frameRate + " Number Of Frames: " + lengthInFrames + " Length: " + grabber.getLengthInTime());
        } catch (Exception ex) {
        }
    }
    int frameIndex = 0;

    public void runFrames() {


        sw = new SwingWorker() {

            @Override
            protected Object doInBackground() {

                while (!breakloop) {
                    try {

                        final Frame current_frame = grabber.grab();
                        if (current_frame == null || current_frame.image == null) {
                            continue;
                        }
                        if (current_frame != null) {
                            picturePreview.setIcon(new ImageIcon(con.convert(current_frame)));
                            progressSlider.setValue(frameIndex);
                            frameIndex++;
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return null;
            }
        };
        sw.execute();

    }

    public void forwardOrRev(int sliderVal) {
//        sliderVal = 200;
        int i = 0;
        breakloop = true;
//        if(sw!=null){
//
        sw.cancel(true);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(VideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (grabber != null) {
            try {
                grabber.stop();
                grabber.release();
            } catch (Exception ex) {
                ex.printStackTrace();
                ;
            }
        }

//        }else{
        initilalize();
//        }

        System.out.println("sliderVal " + sliderVal);
        try {
            while (i < sliderVal) {
                while (true) {
                    final Frame current_frame = grabber.grab();
                    if (current_frame == null || current_frame.image == null) {
                        continue;
                    }
                    break;
                }
                i++;
                System.out.println("I= " + grabber.getFrameNumber());
            }
            progressSlider.setValue(sliderVal);
            frameIndex = sliderVal;
            runFrames();
        } catch (Exception ex) {
            Logger.getLogger(VideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
