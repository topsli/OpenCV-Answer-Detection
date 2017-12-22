import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.opencv.core.CvType.CV_8U;

/**
 * @author Tops
 * @version v1.0 on on 2017/12/1 0001.
 */
public class DetectUtil {

    public static Map<Integer, String>  detectAnswer(String filePath) {
        Mat srcImage1= Imgcodecs.imread(filePath);
        Map<Integer, String> listenAnswer = new HashMap<>();
        Mat srcImage2 = new Mat(),srcImage3 = new Mat(),srcImage4 = new Mat(),srcImage5 = new Mat();

        //图片变成灰度图片
        Imgproc.cvtColor(srcImage1,srcImage2, Imgproc.COLOR_BGR2GRAY);
//        showResult(srcImage2,"hello-2.jpg");
        //图片二值化
        Imgproc.threshold(srcImage2,srcImage3,200,255,Imgproc.THRESH_BINARY_INV);

        //确定腐蚀和膨胀核的大小
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10));

        //腐蚀操作
        Imgproc.erode(srcImage3,srcImage4,element);
        //膨胀操作
        Imgproc.dilate(srcImage4,srcImage5,element);

//        showResult(srcImage5,"hello-5.jpg");
        //确定每张答题卡的ROI区域
        //srcImage5.submat(new Rect(118,1,srcImage1.width() - 120,srcImage1.height() - 10));
        Mat imag_ch1 =   srcImage5.submat(new Rect(35,1,srcImage1.width() - 40,srcImage1.height() - 10));

        //提取已经涂好了的选项
        List<MatOfPoint> chapter1 = new ArrayList<MatOfPoint>();

        Imgproc.findContours(imag_ch1,chapter1, new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        Mat result= new Mat(imag_ch1.size(),CV_8U, new Scalar(255));
        Imgproc.drawContours(result,chapter1,-1,new Scalar(0));

//        showResult(result,"resultImage.jpg");


        List<Rect> listRects = new ArrayList<Rect>();

        for(int j = 0;j<chapter1.size();j++){
            Rect rm = Imgproc.boundingRect( chapter1.get(j));
            if(rm.width < 10 ){
                continue;
            }

            listRects.add(rm);
        }
        RectComparator comparator = new RectYComparator();
        listRects.sort(comparator);

        int indexA = result.width() /4;
        int indexB = result.width() /2;
        int indexC = result.width()*3 / 4;
        int indexD = result.width();

        for(int i = 1;i<=listRects.size();i++){
            Rect rm=   listRects.get(i-1);

            Mat boxArea = result.submat(rm);
            System.out.println("area" + i+ ":"+ "(x:"+ rm.x +" y:" + rm.y +", width:" + rm.width +" height:" + rm.height +" )" );
//            showResult(boxArea, "area" + i + ".jpg");

            if(rm.x< indexA){
                listenAnswer.put(i,"A");
            }
            else if(rm.x >= indexA && rm.x < indexB){
                listenAnswer.put(i,"B");
            }
            else if(rm.x >= indexB && rm.x < indexC){
                listenAnswer.put(i,"C");
            }
            else if(rm.x >= indexC){
                listenAnswer.put(i,"D");
            }
            else {
                listenAnswer.put(i,"");
            }

//            if(rm.x<30){
//                listenAnswer.put(i,"A");
//            }
//            else if(rm.x >= 30 && rm.x < 70){
//                listenAnswer.put(i,"B");
//            }
//            else if(rm.x >= 70 && rm.x < 110){
//                listenAnswer.put(i,"C");
//            }
//            else if(rm.x >= 110){
//                listenAnswer.put(i,"D");
//            }
//            else {
//                listenAnswer.put(i,"");
//            }
        }

        for(Map.Entry entry:listenAnswer.entrySet()){
            System.out.println("num:"+entry.getKey() +", answer:"+entry.getValue());
        }

        return listenAnswer;
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load the library openCv3.0

        detectAnswer("F:\\WorkSpace\\OpenProjects\\OpenCV-Answer-Detection\\ProjetIatic5\\outputs\\area1.jpg");
    }

//    public static void showResult(Mat img,String fileName) {
//        Imgcodecs.imwrite("F:\\WorkSpace\\OpenProjects\\OpenCV-Answer-Detection\\ProjetIatic5\\outputs\\testAnswer\\"+fileName, img);
//    }
}
