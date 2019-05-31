package util.opencv;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class BlobSpotRemoval {

    public static void main(String[] args) {
        System.out.println("STARTING...\n");
        String STR = "D:\\work\\project\\BrainMRI\\matlab\\Brain_Tumor\\Brain_Tumor\\Brain_Tumor_Code\\Benign\\1Perfect.jpg";
        IplImage RawImage = cvLoadImage(STR, CV_LOAD_IMAGE_COLOR);


        System.out.println("Width " + RawImage.width() + " " + RawImage.height());
        spotRemoval(RawImage);
        System.out.println("ALL DONE");
    }
    int MIN_AREA = 6;

    public static IplImage spotRemoval(IplImage RawImage) {
        int MinArea = 6;
        int ErodeCount = 0;
        int DilateCount = 0;



        // Read an image.
//        for(int k = 0; k < 7; k++)
//        {
//            RawImage = cvLoadImage("BlackBalls.jpg"); MinArea = 250; ErodeCount = 0; DilateCount = 1;
//            else if(k == 1) { RawImage = cvLoadImage("Shapes1.jpg"); MinArea = 6; ErodeCount = 0; DilateCount = 1; }
//            else if(k == 2) { RawImage = cvLoadImage("Shapes2.jpg"); MinArea = 250; ErodeCount = 0; DilateCount = 1; }
//            else if(k == 3) { RawImage = cvLoadImage("Blob1.jpg"); MinArea = 2800; ErodeCount = 1; DilateCount = 1; }
//            else if(k == 4) { RawImage = cvLoadImage("Blob2.jpg"); MinArea = 2800; ErodeCount = 1; DilateCount = 1; }

        System.out.println(cvGetSize(RawImage));
        MinArea = 60;
        ErodeCount = 1;
        DilateCount = 1;
//            else if(k == 6) { RawImage = cvLoadImage("Rice.jpg"); MinArea = 30; ErodeCount = 2; DilateCount = 1; }
//        ShowImage(RawImage, "RawImage", 512);
        cvRectangle(RawImage, cvPoint(50, 50), cvPoint(100, 100), CvScalar.GREEN);
//        cvRectangle(RawImage, cvPoint(50, 50), cvPoint(100, 100), CvScalar.GREEN,CV_FILLED,0,0);

//        ShowImage(RawImage, "WorkingImage", 512);
        System.out.println("Dilation done");
//        JOptionPane.showMessageDialog(null, "Test");
        IplImage GrayImage = null;
        if (RawImage.nChannels() >= 3) {
            GrayImage = cvCreateImage(cvGetSize(RawImage), IPL_DEPTH_8U, 1);
            cvCvtColor(RawImage, GrayImage, CV_BGR2GRAY);
        } else if (RawImage.nChannels() == 1) {
            GrayImage=RawImage;
        }
        //ShowImage(GrayImage, "GrayImage", 512);

        IplImage BWImage = cvCreateImage(cvGetSize(GrayImage), IPL_DEPTH_8U, 1);
        cvThreshold(GrayImage, BWImage, 155, 255, CV_THRESH_BINARY);
        //ShowImage(BWImage, "BWImage");

        IplImage WorkingImage = cvCreateImage(cvGetSize(BWImage), IPL_DEPTH_8U, 1);
        cvErode(BWImage, WorkingImage, null, ErodeCount);
        cvDilate(WorkingImage, WorkingImage, null, DilateCount);
//        ShowImage(WorkingImage, "WorkingImage", 512);
//        System.out.println("Dilation done");
//        JOptionPane.showMessageDialog(null, "Test");
        //cvSaveImage("Working.jpg", WorkingImage);
        //PrintGrayImage(WorkingImage, "WorkingImage");
        //BinaryHistogram(WorkingImage);
//        cvSaveImage("D:/b4.bmp", WorkingImage);
        Blobs Regions = new Blobs();
        Regions.BlobAnalysis(
                WorkingImage, // image
                -1, -1, // ROI start col, row
                -1, -1, // ROI cols, rows
                1, // border (0 = black; 1 = white)
                MinArea);                   // minarea
        Regions.PrintRegionData();
        System.out.println("Blob done");
        for (int i = 1; i <= Blobs.MaxLabel; i++) {
            double[] Region = Blobs.RegionData[i];
            int Parent = (int) Region[Blobs.BLOBPARENT];
            int Color = (int) Region[Blobs.BLOBCOLOR];
            int area = (int) Region[Blobs.BLOBAREA];
            int MinX = (int) Region[Blobs.BLOBMINX];
            int MaxX = (int) Region[Blobs.BLOBMAXX];
            int MinY = (int) Region[Blobs.BLOBMINY];
            int MaxY = (int) Region[Blobs.BLOBMAXY];
            System.out.println("area " + area);
            Highlight(WorkingImage, MinX, MinY, MaxX, MaxY, 1);
        }

//        ShowImage(WorkingImage, "RawImage", 512);
        cvSaveImage("D:/a.bmp", WorkingImage);
        cvReleaseImage(GrayImage);
        GrayImage = null;
        cvReleaseImage(BWImage);
        BWImage = null;
//        cvReleaseImage(WorkingImage);
//        WorkingImage = null;
////        }
//        cvReleaseImage(RawImage);
//        RawImage = null;
        return WorkingImage;
    }

    // Versions with 2, 3, and 4 parms respectively
    public static void ShowImage(IplImage image, String caption) {
        CvMat mat = image.asCvMat();
        int width = mat.cols();
        if (width < 1) {
            width = 1;
        }
        int height = mat.rows();
        if (height < 1) {
            height = 1;
        }
        double aspect = 1.0 * width / height;
        if (height < 128) {
            height = 128;
            width = (int) (height * aspect);
        }
        if (width < 128) {
            width = 128;
        }
        height = (int) (width / aspect);
        ShowImage(image, caption, width, height);
    }

    public static void ShowImage(IplImage image, String caption, int size) {
        if (size < 128) {
            size = 128;
        }
        CvMat mat = image.asCvMat();
        int width = mat.cols();
        if (width < 1) {
            width = 1;
        }
        int height = mat.rows();
        if (height < 1) {
            height = 1;
        }
        double aspect = 1.0 * width / height;
        if (height != size) {
            height = size;
            width = (int) (height * aspect);
        }
        if (width != size) {
            width = size;
        }
        height = (int) (width / aspect);
        ShowImage(image, caption, width, height);
    }

    public static void ShowImage(IplImage image, String caption, int width, int height) {
        CanvasFrame canvas = new CanvasFrame(caption, 1);   // gamma=1
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        canvas.setCanvasSize(width, height);
        OpenCVFrameConverter converter = new OpenCVFrameConverter.ToIplImage();
        canvas.showImage(converter.convert(image));
    }

    public static void Highlight(IplImage image, int[] inVec) {
        Highlight(image, inVec[0], inVec[1], inVec[2], inVec[3], 1);
    }

    public static void Highlight(IplImage image, int[] inVec, int Thick) {
        Highlight(image, inVec[0], inVec[1], inVec[2], inVec[3], Thick);
    }

    public static void Highlight(IplImage image, int xMin, int yMin, int xMax, int yMax) {
        Highlight(image, xMin, yMin, xMax, yMax, 1);
    }

    public static void Highlight(IplImage image, int xMin, int yMin, int xMax, int yMax, int Thick) {
        CvPoint pt1 = cvPoint(xMin, yMin);
        CvPoint pt2 = cvPoint(xMax, yMax);
        CvScalar color = cvScalar(255, 0, 0, 0);       // blue [green] [red]
//        cvRectangle(image, pt1, pt2, color, Thick, 4, 0);
//           cvRectangle(image, pt1, pt2, CvScalar.RED, CV_FILLED, 0, 0);
        int[] pt = new int[]{xMin + 20, yMin + 20};
        CvFont font = cvFont(1, 1);
        CvSize textSize = new CvSize(18);
        int width = xMax - xMin;
        int heigh = yMax - yMin;
//            cvRectangle(image, cvPoint(100, 100), cvPoint(400, 400), CV_RGB(255,0,0), CV_FILLED,0, 16);
//        cvRectangle(image, cvPoint(100, 100), cvPoint(200, 200), CvScalar.GREEN, CV_FILLED, CV_AA, 16);

        if (width < 60 && heigh < 60) {
            pt1 = cvPoint(xMin - 1, yMin - 1);
            pt2 = cvPoint(xMax + 1, yMax + 1);
//            cvRectangle(image, pt1, pt2, color);
            cvRectangle(image, pt1, pt2, CvScalar.WHITE, 0, 0, 0);
//             cvPutText(image, "[fill" + width + "," + heigh + "]", pt, font, CvScalar.BLACK);
            System.out.println("calling Filled ");
        } else {
//            cvPutText(image, "[" + width + "," + heigh + "]", pt, font, CvScalar.BLACK);
        }
//          
    }

    public static void PrintGrayImage(IplImage image, String caption) {
        int size = 512; // impractical to print anything larger
        CvMat mat = image.asCvMat();
        int cols = mat.cols();
        if (cols < 1) {
            cols = 1;
        }
        int rows = mat.rows();
        if (rows < 1) {
            rows = 1;
        }
        double aspect = 1.0 * cols / rows;
        if (rows > size) {
            rows = size;
            cols = (int) (rows * aspect);
        }
        if (cols > size) {
            cols = size;
        }
        rows = (int) (cols / aspect);
        PrintGrayImage(image, caption, 0, cols, 0, rows);
    }

    public static void PrintGrayImage(IplImage image, String caption, int MinX, int MaxX, int MinY, int MaxY) {
        int size = 512; // impractical to print anything larger
        CvMat mat = image.asCvMat();
        int cols = mat.cols();
        if (cols < 1) {
            cols = 1;
        }
        int rows = mat.rows();
        if (rows < 1) {
            rows = 1;
        }

        if (MinX < 0) {
            MinX = 0;
        }
        if (MinX > cols) {
            MinX = cols;
        }
        if (MaxX < 0) {
            MaxX = 0;
        }
        if (MaxX > cols) {
            MaxX = cols;
        }
        if (MinY < 0) {
            MinY = 0;
        }
        if (MinY > rows) {
            MinY = rows;
        }
        if (MaxY < 0) {
            MaxY = 0;
        }
        if (MaxY > rows) {
            MaxY = rows;
        }

        System.out.println("\n" + caption);
        System.out.print("   +");
        for (int icol = MinX; icol < MaxX; icol++) {
            System.out.print("-");
        }
        System.out.println("+");

        for (int irow = MinY; irow < MaxY; irow++) {
            if (irow < 10) {
                System.out.print(" ");
            }
            if (irow < 100) {
                System.out.print(" ");
            }
            System.out.print(irow);
            System.out.print("|");
            for (int icol = MinX; icol < MaxX; icol++) {
                int val = (int) mat.get(irow, icol);
                String C = " ";
                if (val == 0) {
                    C = "*";
                }
                System.out.print(C);
            }
            System.out.println("|");
        }
        System.out.print("   +");
        for (int icol = MinX; icol < MaxX; icol++) {
            System.out.print("-");
        }
        System.out.println("+");
    }

    public static void PrintImageProperties(IplImage image) {
        CvMat mat = image.asCvMat();
        int cols = mat.cols();
        int rows = mat.rows();
        int depth = mat.depth();
        System.out.println("ImageProperties for " + image + " : cols=" + cols + " rows=" + rows + " depth=" + depth);
    }

    public static float BinaryHistogram(IplImage image) {
        CvScalar Sum = cvSum(image);
        float WhitePixels = (float) (Sum.getVal(0) / 255);
        CvMat mat = image.asCvMat();
        float TotalPixels = mat.cols() * mat.rows();
        //float BlackPixels = TotalPixels - WhitePixels;
        return WhitePixels / TotalPixels;
    }

    // Counterclockwise small angle rotation by skewing - Does not stretch border pixels
    public static IplImage SkewGrayImage(IplImage Src, double angle) // angle is in radians
    {
        //double radians = - Math.PI * angle / 360.0;   // Half because skew is horizontal and vertical
        double sin = -Math.sin(angle);
        double AbsSin = Math.abs(sin);

        int nChannels = Src.nChannels();
        if (nChannels != 1) {
            System.out.println("ERROR: SkewGrayImage: Require 1 channel: nChannels=" + nChannels);
            System.exit(1);
        }

        CvMat SrcMat = Src.asCvMat();
        int SrcCols = SrcMat.cols();
        int SrcRows = SrcMat.rows();

        double WidthSkew = AbsSin * SrcRows;
        double HeightSkew = AbsSin * SrcCols;

        int DstCols = (int) (SrcCols + WidthSkew);
        int DstRows = (int) (SrcRows + HeightSkew);

        CvMat DstMat = cvCreateMat(DstRows, DstCols, CV_8UC1);  // Type matches IPL_DEPTH_8U
        cvSetZero(DstMat);
        cvNot(DstMat, DstMat);

        for (int irow = 0; irow < DstRows; irow++) {
            int dcol = (int) (WidthSkew * irow / SrcRows);
            for (int icol = 0; icol < DstCols; icol++) {
                int drow = (int) (HeightSkew - HeightSkew * icol / SrcCols);
                int jrow = irow - drow;
                int jcol = icol - dcol;
                if (jrow < 0 || jcol < 0 || jrow >= SrcRows || jcol >= SrcCols) {
                    DstMat.put(irow, icol, 255);
                } else {
                    DstMat.put(irow, icol, (int) SrcMat.get(jrow, jcol));
                }
            }
        }

        IplImage Dst = cvCreateImage(cvSize(DstCols, DstRows), IPL_DEPTH_8U, 1);
        Dst = DstMat.asIplImage();
        return Dst;
    }

    public static IplImage TransposeImage(IplImage SrcImage) {
        CvMat mat = SrcImage.asCvMat();
        int cols = mat.cols();
        int rows = mat.rows();
        IplImage DstImage = cvCreateImage(cvSize(rows, cols), IPL_DEPTH_8U, 1);
        cvTranspose(SrcImage, DstImage);
        cvFlip(DstImage, DstImage, 1);
        return DstImage;
    }
}
