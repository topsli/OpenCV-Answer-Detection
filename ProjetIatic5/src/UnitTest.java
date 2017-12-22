import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UnitTest {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load the library openCv3.0
//		DetectResult detect = new DetectResult();
//		Path p = Paths.get("C:\\Users\\Jiravat\\Pictures\\xx.jpg");
//		File f = new File("C:\\Hello\\AnotherFolder\\The File Name.PDF");
//		String file = p.getFileName().toString();
//		detect.setImageUrl(f.getName());
//		detect.Processing();
//		System.out.println("id :"+detect.studentId);
//		System.out.println("Score :"+detect.score);

		matchTmp("F:\\WorkSpace\\OpenProjects\\OpenCV-Answer-Detection\\ProjetIatic5\\inputs\\engExamPic.jpg");
	}

	public static void matchTmp(String imageUrl){

		int match_method = Imgproc.TM_CCOEFF_NORMED;

		String tamplateUrl = "F:\\WorkSpace\\OpenProjects\\OpenCV-Answer-Detection\\ProjetIatic5\\tamplete2.jpg";

		Mat inputMat = Imgcodecs.imread(imageUrl, Imgproc.COLOR_BGR2GRAY); //input

		//matcing tamplate
		Mat modeImage = Imgcodecs.imread(tamplateUrl, Imgproc.COLOR_BGR2GRAY);

		// / Create the result matrix
		int result_cols = inputMat.cols() - modeImage.cols() + 1;
		int result_rows = inputMat.rows() - modeImage.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

		// / Do the Matching and Normalize
		Imgproc.matchTemplate(inputMat, modeImage, result, 0);

		Core.MinMaxLocResult maxr = Core.minMaxLoc(result);



		Point maxp = maxr.maxLoc;
		Point minPoint = maxr.minLoc;



		Point minVP = new Point(minPoint.x,maxr.maxVal);
		Point maxVP = new Point(maxp.x,maxr.maxVal);

//		Imgproc.rectangle(inputMat, minPoint, new Point(minPoint.x + modeImage.cols(), minPoint.y + modeImage.rows())
//				, new Scalar(0, 255,0), -1);

		Imgproc.rectangle(inputMat, new Point(minPoint.x,minPoint.y), new Point(minPoint.x+60,minPoint.y+60), new Scalar(0,255,0));

		Imgproc.rectangle(inputMat, new Point(maxp.x + 20,maxp.y + 20), new Point(maxp.x+80,maxp.y+80), new Scalar(0,255,0));


		Imgproc.rectangle(inputMat, new Point(minVP.x + 20,minVP.y + 20), new Point(minVP.x+80,minVP.y+80), new Scalar(0,255,0));


		Imgproc.rectangle(inputMat, new Point(maxVP.x + 20,maxVP.y + 20), new Point(maxVP.x+80,maxVP.y+80), new Scalar(0,255,0));
//		Imgproc.rectangle(inputMat, maxp, new Point(minPoint.x + modeImage.cols(), minPoint.y + modeImage.rows())
//				, new Scalar(0, 255,0), -1);

//
//		rectangle(dstImage, minPoint, Point(minPoint.x + modeImage.cols, minPoint.y + modeImage.rows)
//				, Scalar(theRNG().uniform(0, 255), theRNG().uniform(0, 255), theRNG().uniform(0, 255)), 3, 8);
//		imshow("【匹配后的计算过程图像】", dstImage);

		showResult(inputMat,"result.jpg");
	}


	public static void showResult(Mat img,String fileName) {
		Imgcodecs.imwrite("F:\\WorkSpace\\OpenProjects\\OpenCV-Answer-Detection\\ProjetIatic5\\outputs\\"+fileName, img);
	}

}
