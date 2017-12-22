/**
 * @author Tops
 * @version v1.0 on on 2017/11/23 0023.
 * http://www.cnblogs.com/zendu/p/6694386.html
 */
//
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
//#include "stdafx.h"
//#include "cv.h"
//#include "opencv2/imgproc/imgproc.hpp"
//#include "opencv2/highgui/highgui.hpp"
//#include "highgui.h"
//#include "cxcore.h"
//#include <string>
//#include <stdlib.h>
//#include <stdio.h>
//#include <vector>
//#include <map>
//using namespace std;
//using namespace cv;
import java.util.*;

import static org.opencv.core.CvType.CV_8U;
//
//class RectComp//Rect排序
//{
//    public:
//    Rect rm;
//    RectComp(Rect rms)
//    {
//        rm = rms;
//    }
//    bool operator < (const RectComp& ti) const
//    {
//        return rm.x < ti.rm.x;
//    }
//};
//
//        int main()
//        {
//
//        //装载图片
//        Mat srcImage1= imread("D:\\13.jpg");
//        Mat srcImage2,srcImage3,srcImage4,srcImage5;
//        //namedWindow("hello-1", 1);
//        //imshow("hello-1",srcImage1);
//        // cv::waitKey(0);
//        //图片变成灰度图片
//        cvtColor(srcImage1,srcImage2,CV_BGR2GRAY);
//        //imshow("hello-2",srcImage2);
//        // cv::waitKey(0);
//        //图片二值化
//        threshold(srcImage2,srcImage3,200,255,THRESH_BINARY_INV);
//        imshow("hello-3",srcImage3);
//        cv::waitKey(0);
//        //确定腐蚀和膨胀核的大小
//        Mat element = getStructuringElement(MORPH_RECT, Size(3, 3));
//        //腐蚀操作
//        erode(srcImage3,srcImage4,element);
//        //膨胀操作
//        dilate(srcImage4,srcImage5,element);
//
//        namedWindow("hello-5", 1);
//        imshow("hello-5", srcImage5 );
//        cv::waitKey(0);
//
//        //确定每张答题卡的ROI区域
//        Mat imag_ch1 = srcImage5(Rect(2,20,268,40));
//
//        namedWindow("img1", 1);
//        imshow("img1",imag_ch1);
//        cv::waitKey(0);
//
//        //提取已经涂好了的选项
//        std::vector<std::vector<cv::Point> > chapter1;
//        findContours(imag_ch1,chapter1,RETR_EXTERNAL,CHAIN_APPROX_SIMPLE);
//        Mat result(imag_ch1.size(), CV_8U , cv::Scalar(255)) ;
//        cv::drawContours(result,chapter1,-1,cv::Scalar(0));
//        namedWindow("resultImage", 1);
//        cv::imshow("resultImage" , result);
//
//        vector<RectComp>RectCompList;
//        for(int i = 0;i<chapter1.size();i++)
//        {
//        Rect rm= cv::boundingRect(cv::Mat(chapter1[i]));
//        RectComp *ti = new RectComp(rm);
//        RectCompList.push_back(*ti);
//        // printf("Rect %d x = %d,y = %d \n",i,rm.x,rm.y);
//        }
//        sort(RectCompList.begin(),RectCompList.end());
//        std::map<int,string>listenAnswer;
//        //判断这部分的答题卡是否都已涂上
//        for(int t = 0;t<RectCompList.size();t++)
//        {
//        if(RectCompList.at(t).rm.y<5)
//        {
//        listenAnswer[t] = "A";
//        }
//        else if((RectCompList.at(t).rm.y>5)&&(RectCompList.at(t).rm.y<16))
//        {
//        listenAnswer[t] = "B";
//        }
//        else if(RectCompList.at(t).rm.y>16)
//        {
//        listenAnswer[t] = "C";
//        }
//        printf("sorted %d x = %d,y = %d \n",t,RectCompList.at(t).rm.x,RectCompList.at(t).rm.y);
//        }
//
//        for(map<int,string>::iterator it = listenAnswer.begin();it!=listenAnswer.end();++it)
//        {
//        cout<<"num:"<<it->first+1<<","<<"answer:"<<it->second<<endl;
//        }
//
//        cv::waitKey(0);
//        return 0;
//        }

public class TestDetect {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load the library openCv3.0
        //装载图片
        Mat srcImage1= Imgcodecs.imread("F:\\test.jpg");
        Mat srcImage2 = new Mat(),srcImage3 = new Mat(),srcImage4 = new Mat(),srcImage5 = new Mat();


        //图片变成灰度图片
        Imgproc.cvtColor(srcImage1,srcImage2, Imgproc.COLOR_BGR2GRAY);
        showResult(srcImage2,"hello-2.jpg");
        //图片二值化
        Imgproc.threshold(srcImage2,srcImage3,200,255,Imgproc.THRESH_BINARY_INV);

        //确定腐蚀和膨胀核的大小
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));

        //腐蚀操作
        Imgproc.erode(srcImage3,srcImage4,element);
        //膨胀操作
        Imgproc.dilate(srcImage4,srcImage5,element);

        showResult(srcImage5,"hello-5.jpg");
        //确定每张答题卡的ROI区域
        Mat imag_ch1 = srcImage5.submat(new Rect(2,20,268,40));

        //提取已经涂好了的选项
        List<MatOfPoint> chapter1 = new ArrayList<MatOfPoint>();

        Imgproc.findContours(imag_ch1,chapter1, new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        Mat result= new Mat(imag_ch1.size(),CV_8U, new Scalar(255));
        Imgproc.drawContours(result,chapter1,-1,new Scalar(0));

        showResult(result,"resultImage.jpg");


        List<Rect> listRects = new ArrayList<Rect>();

        for(int j = 0;j<chapter1.size();j++){
            Rect rm = Imgproc.boundingRect( chapter1.get(j));
            listRects.add(rm);
        }
        RectXComparator comparator = new RectXComparator();
        listRects.sort(comparator);


        Map<Integer, String> listenAnswer = new HashMap<>();

        for(int i = 1;i<=listRects.size();i++){
            Rect rm=   listRects.get(i-1);

            Mat boxArea = result.submat(rm);

            System.out.println("area" + i+ ":"+ "(x:"+ rm.x +" y:" + rm.y +", width:" + rm.width +" height:" + rm.height +" )" );

            showResult(boxArea, "area" + i + ".jpg");

            if(rm.y<5){
                listenAnswer.put(i,"A");
            }
            else if(rm.y > 5 && rm.y < 16){
                listenAnswer.put(i,"B");
            }
            else if(rm.y > 16 && rm.y < 25){
                listenAnswer.put(i,"C");
            }
            else if(rm.y > 25){
                listenAnswer.put(i,"D");
            }
        }

        for(Map.Entry entry:listenAnswer.entrySet()){
            System.out.println("num:"+entry.getKey() +", answer:"+entry.getValue());
        }

    }


    public static void showResult(Mat img,String fileName) {
        Imgcodecs.imwrite("F:\\testImg\\"+fileName, img);
    }



}
