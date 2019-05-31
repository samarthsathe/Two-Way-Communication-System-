/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author technowings-pc
 */
public class VideoModel {
    private String videoName="";
    private long totalNoOfFrames = 0;
    private int fps = 0;
    private float totalDuration = 0;

    /**
     * @return the videoName
     */
    public String getVideoName() {
        return videoName;
    }

    /**
     * @param videoName the videoName to set
     */
    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    /**
     * @return the totalNoOfFrames
     */
    public long getTotalNoOfFrames() {
        return totalNoOfFrames;
    }

    /**
     * @param totalNoOfFrames the totalNoOfFrames to set
     */
    public void setTotalNoOfFrames(long totalNoOfFrames) {
        this.totalNoOfFrames = totalNoOfFrames;
    }

    /**
     * @return the fps
     */
    public int getFps() {
        return fps;
    }

    /**
     * @param fps the fps to set
     */
    public void setFps(int fps) {
        this.fps = fps;
    }

    /**
     * @return the totalDuration
     */
    public float getTotalDuration() {
        return totalDuration;
    }

    /**
     * @param totalDuration the totalDuration to set
     */
    public void setTotalDuration(float totalDuration) {
        this.totalDuration = totalDuration;
    }




}
