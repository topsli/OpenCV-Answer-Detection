import java.util.*;
import java.util.regex.Pattern;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

public class DetectResult {
	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	List<Integer> studentNumber = new ArrayList<Integer>();
	String studentId;
	List<String> answers = new ArrayList<String>();

	List<Rect> answerPoints = new ArrayList<>();

	String imageUrl;
	int score = 0;
	int isChecked = 0;
	String tamplateUrl = "tamplete2.jpg";
	int match_method = Imgproc.TM_CCOEFF_NORMED;
	int isStudentMark = 0;
	Mat image;

	Boolean checkAllNot(List<Integer> testStudentNumber){
		for(int i=0;i<testStudentNumber.size();i++){
			if(testStudentNumber.get(i) == -1){
				return true;
			}
		}
		return false;
	}

	List<Integer> getStudentsIdFromRect(List<Rect> inputRect,List<Integer> studentNumber_){
		//int[] yX = {138, 177, 216, 255, 291, 331, 369, 409, 448, 487};
		ArrayList<Integer> xList = new ArrayList<Integer>();
		xList.addAll(Arrays.asList(0,1,2,3,4,5,6,7));
//		List<Rect> d1 = new ArrayList<Rect>();
//		List<Rect> d2 = new ArrayList<Rect>();
//		List<Rect> d3 = new ArrayList<Rect>();
//		List<Rect> d4 = new ArrayList<Rect>();
//		List<Rect> d5 = new ArrayList<Rect>();
//		List<Rect> d6 = new ArrayList<Rect>();
//		List<Rect> d7 = new ArrayList<Rect>();
//		List<Rect> d8 = new ArrayList<Rect>();
		int row=0;
		List<Object> toRemove = new ArrayList<Object>();
		int yOld = inputRect.get(0).y;
		for(int i=0;i<inputRect.size();i++){
			if(inputRect.get(i).y - yOld > 30){
//				System.out.println(xList);
				//System.out.println(toRemove);
				xList.removeAll(toRemove);
//				System.out.println(xList);
				for(int x=0;x<xList.size();x++){
					studentNumber_.set(xList.get(x), row);
//					System.out.println("set studentId:"+ xList.get(x)+" "+row);
				}
				toRemove.clear();
				xList.clear();
				xList.addAll(Arrays.asList(0,1,2,3,4,5,6,7));
				row++;
				yOld = inputRect.get(i).y;
			}
			//if(inputRect.get(i).x > 225 && inputRect.get(i).x < 235){
			if(inputRect.get(i).x > 200 && inputRect.get(i).x < 275){
				toRemove.add(0);
//				d1.add(inputRect.get(i));
			}
			//if(inputRect.get(i).x > 300 && inputRect.get(i).x < 310){
			if(inputRect.get(i).x > 275 && inputRect.get(i).x < 350){
				toRemove.add(1);
//				d2.add(inputRect.get(i));
			}
			//if(inputRect.get(i).x > 375 && inputRect.get(i).x < 380){
			if(inputRect.get(i).x > 350 && inputRect.get(i).x < 425){
				toRemove.add(2);
//				d3.add(inputRect.get(i));
			}
			//if(inputRect.get(i).x > 445 && inputRect.get(i).x < 455){
			if(inputRect.get(i).x > 425 && inputRect.get(i).x < 500){
				toRemove.add(3);
//				d4.add(inputRect.get(i));
			}
			//if(inputRect.get(i).x > 520 && inputRect.get(i).x < 530){
			if(inputRect.get(i).x > 500 && inputRect.get(i).x < 575){
				toRemove.add(4);
//				d5.add(inputRect.get(i));
			}
			//if(inputRect.get(i).x > 590 && inputRect.get(i).x < 605){
			if(inputRect.get(i).x > 575 && inputRect.get(i).x < 640){
				toRemove.add(5);
//				d6.add(inputRect.get(i));
			}
			//if(inputRect.get(i).x > 665 && inputRect.get(i).x < 680){
			if(inputRect.get(i).x > 640 && inputRect.get(i).x < 720){
				toRemove.add(6);
//				d7.add(inputRect.get(i));
			}
			//if(inputRect.get(i).x > 740 && inputRect.get(i).x < 755){
			if(inputRect.get(i).x > 720 && inputRect.get(i).x < 775){
				toRemove.add(7);
//				d8.add(inputRect.get(i));
			}

		}

		return studentNumber_;

	}

	List<Point> getFourPoint(List<Point> pointList){
		Point topleft = null;
		int topleftMax =0;
		Point topright = null;
		int toprightMax =0;
		Point downleft = null;
		int downleftMax =0;
		Point downright = null;
		int downrightMax =0;
		for(int i=0;i<pointList.size();i++){
			Point tmp = pointList.get(i);
			if(topleft == null || (tmp.x-topleft.x)+(tmp.y-topleft.y) < topleftMax ){
				topleft = tmp;
				topleftMax = ((int)((tmp.x-topleft.x)+(tmp.y-topleft.y)));
//				System.out.println("set topleft to :"+topleft);
			}
			if(topright == null || (tmp.x-topright.x)+(topright.y-tmp.y) > toprightMax){
				topright = tmp;
				toprightMax = ((int)((tmp.x-topright.x)+(topright.y-tmp.y)));
//				System.out.println("set topright to :"+topright);
			}
			if(downleft == null || (downleft.x-tmp.x)+(tmp.y-downleft.y) > downleftMax){
				downleft = tmp;
				downleftMax = ((int)((downleft.x-tmp.x)+(tmp.y-downleft.y)));
//				System.out.println("set downleft to :"+downleft);
			}
			if(downright == null ||  (tmp.x-downright.x)+(tmp.y-downright.y) > downrightMax ){
				downright = tmp;
				downrightMax = ((int)((tmp.x-downright.x)+(tmp.y-downright.y)));
//				System.out.println("set downright to :"+downright);
			}
		}
//		System.out.println(topleft+":"+ topright+":"+ downright+":"+ downleft);
		List<Point> fourPoint = new ArrayList<Point>(Arrays.asList(topleft, topright, downright, downleft));
//		fourPoint.add(topleft);
//		fourPoint.add(topright);
//		fourPoint.add(downright);
//		fourPoint.add(downleft);
		return fourPoint;
	}

	public DetectResult(){
		this.score = 0;
	}

	public static void showResult(Mat img,String fileName) {
		String filePath = "F:\\WorkSpace\\OpenProjects\\OpenCV-Answer-Detection\\ProjetIatic5\\outputs\\"+fileName;
		Imgcodecs.imwrite(filePath, img);
	}

	public static String outputFile(Mat img,String fileName){
		String filePath = "F:\\WorkSpace\\OpenProjects\\OpenCV-Answer-Detection\\ProjetIatic5\\outputs\\"+fileName;
//		Imgcodecs.imwrite(filePath, img);
		return filePath;
	}

	public void Processing(){
		int resultWidth = 1653;
		int resultHeight = 2338;


		String number = "99999999";
		String[] testStudentNumber = number.split("");
		for(int i = 0; i < testStudentNumber.length; i++) {
			studentNumber.add(-1);
		}

		Mat inputMat = Imgcodecs.imread(imageUrl, Imgproc.COLOR_BGR2GRAY); //input

		//matcing tamplate
		Mat templ = Imgcodecs.imread(tamplateUrl, Imgproc.COLOR_BGR2GRAY);

		// / Create the result matrix
		int result_cols = inputMat.cols() - templ.cols() + 1;
		int result_rows = inputMat.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

		// / Do the Matching and Normalize
		Imgproc.matchTemplate(inputMat, templ, result, match_method);
		Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
		Imgproc.threshold(result, result, 0.1, 1, Imgproc.THRESH_TOZERO);
		double threshold = 0.95;
		double maxval;
        Mat dst = inputMat.clone();

		List<Point> src_pnt2 = new ArrayList<Point>();

		int numOfPoint = 0;
		while(true)
		{
			Core.MinMaxLocResult maxr = Core.minMaxLoc(result);
			Point maxp = maxr.maxLoc;
			maxval = maxr.maxVal;
			Point maxop = new Point(maxp.x + templ.width(), maxp.y + templ.height());
			if(maxval >= threshold)
			{
				//System.out.println("Template Matches with input image");

//                Imgproc.rectangle(dst, maxp, new Point(maxp.x + templ.cols(),
//                        maxp.y + templ.rows()), new Scalar(0, 255, 0),5);
				Imgproc.rectangle(result, maxp, new Point(maxp.x + templ.cols(),
						maxp.y + templ.rows()), new Scalar(0, 255, 0),-1);


				double midX = lerp(maxp.x, maxop.x, .5);
				double midY = lerp(maxp.y, maxop.y, .5);

//                Imgproc.circle(dst, new Point(midX, midY), 5, new Scalar(0, 255, 0), 5);
//                Imgproc.circle(result, new Point(midX, midY), 5, new Scalar(0, 255, 0), 5);
				src_pnt2.add(new Point(midX, midY));
				numOfPoint++;


			}else{
				break;
			}
		}

		showResult(dst, "outputGetPoint.jpg");


		//Perspective Transform
		Mat outputMat = new Mat(resultWidth,resultHeight, CvType.CV_8UC1);
		List<Point> src_pnt = getFourPoint(src_pnt2);
		Mat startM = Converters.vector_Point2f_to_Mat(src_pnt);

		List<Point> dst_pnt = new ArrayList<Point>();
		Point p4 = new Point(0, 0);
		dst_pnt.add(p4);

		Point p5 = new Point(inputMat.cols(), 0);
		dst_pnt.add(p5);

		Point p6 = new Point(inputMat.cols(), inputMat.rows());
		dst_pnt.add(p6);

		Point p7 = new Point(0, inputMat.rows());
		dst_pnt.add(p7);


		Mat endM = Converters.vector_Point2f_to_Mat(dst_pnt);
		Mat M = Imgproc.getPerspectiveTransform(startM, endM);

		Size size = new Size(inputMat.cols(), inputMat.rows());
		Imgproc.warpPerspective(inputMat, outputMat, M, size);

		image = outputMat.clone();
		//showResult(image, "outputPerSpectiveTest.jpg");

		Imgproc.resize(image,image,new Size(1653,2338)); //mise en echelle
		Mat imageHSV = new Mat(image.size(), CvType.CV_8UC4);
		Mat imageBlurr = new Mat(image.size(),CvType.CV_8UC4);
		Mat imageA = new Mat(image.size(), CvType.CV_32F);
		Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
		Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,9, 5);
		Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);

		int question = 0;
		int col_Student = 0;

		List<Rect> listRects = new ArrayList<Rect>();
		List<Rect> areaRectList = new ArrayList<Rect>();

		List<Rect> resultRectList = new ArrayList<Rect>();
		int numOfStudentId = 0;

		for(int i=0; i<contours.size() ;i++){
			//check contours

			if (Imgproc.contourArea(contours.get(i)) > 600){

				Rect rect = Imgproc.boundingRect(contours.get(i));
				// Mat boxArea = image.submat(rect);
				if(rect.width > 120) {
					areaRectList.add(rect);
				}

			}
		}

		int middleX = imageA.width() / 2;
		System.out.println("middleX:"+middleX);
		RectComparator comparator = new RectComparator(middleX);
		areaRectList.sort(comparator);

		for(int i = 0;i<areaRectList.size();i++) {
			Rect rm = areaRectList.get(i);
			if( !comparator.checkDuplicate(rm,resultRectList )){
				if (rm.x == 0){
					continue;
				}
				resultRectList.add(rm);
				//Mat boxArea = image.submat(rm);
//				showResult(boxArea, "area" + i + ".jpg");
			}
		}

		List<Map<Integer,String >> listenAnswer = new ArrayList<>();

		for(int j = 0; j < resultRectList.size(); j++){
			Rect rm = resultRectList.get(j);
			Mat boxArea = image.submat(rm);
			showResult(boxArea, "area" + j+ ".jpg");
			String filePath = outputFile(boxArea,"area" + j + ".jpg");
			if(j== 0 ){
				studentId = ZXingUtil.decode(filePath);
			}
			else if( 1 < j && j < 5){
				//how to detect the choose question areas
				listenAnswer.add(DetectUtil.detectAnswer(filePath));
			}
		}

		for(Map<Integer,String> map :listenAnswer){
			for(Map.Entry entry:map.entrySet()){
//				System.out.println("num:"+entry.getKey() +", answer:"+entry.getValue());
				answers.add((String) entry.getValue());
			}
		}


//		if(numOfStudentId > 7){
//			isStudentMark = 1;
//			studentNumber = getStudentsIdFromRect(listRects,studentNumber);
//			studentId = "";
//			for(int i=0;i<studentNumber.size();i++){
//				if(studentNumber.get(i) == -1){
//					studentNumber.set(i, 9);
//				}
//				studentId+=studentNumber.get(i);
//			}
//		}else{
//			studentId = "Student Id Not Found";
//		}
//	    showResult(image, "outputAnswer.jpg");

//		int areaHeight = 0;
//		for(int j = 0; j<  answerPoints.size(); j++){
//
//			if(j < answerPoints.size() - 1 )
//				areaHeight =   answerPoints.get(j+1).y  -    answerPoints.get(j).y ;
//			else
//				areaHeight = image.height() - answerPoints.get(j).y - 10;
//			Mat boxArea1 = image.submat(new Rect(  answerPoints.get(j).x - 15 ,  answerPoints.get(j).y  - 15, image.width() -  answerPoints.get(j).x  - 50 , areaHeight));
//			showResult(boxArea1, "answerArea" +(j+1)+ ".jpg");
//		}
	}

	double lerp(double a, double b, double f)
	{
		return a + f * (b - a);
	}

	public String getStudentId(){
//		studentId = "";
//		System.out.println(studentNumber);
//	    for(int i=0;i<8;i++){
//	    	if(studentNumber.get(i) == -1){
//	    		studentNumber.set(i, 9);
//	    		studentId = "Student Id Missing";
//	    		return studentId;
//	    	}
//	    	studentId+=studentNumber.get(i);
//	    }
		return studentId;
	}


	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public String toString(){
		return imageUrl;
	}

	public int getIsChecked(){
		return isChecked;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void writeScore(){
		//Mat image = Imgcodecs.imread(imageUrl);
		this.isChecked = 1;
		String[] strings = imageUrl.split(Pattern.quote("."));
		String urlToOut = strings[0]+"Checked."+strings[1];
		Imgproc.putText(image, ""+score ,new Point(70,120),1,7,new Scalar(0, 0, 255),7);
		Imgcodecs.imwrite(urlToOut, image);
	}

	public int getScore() {
		score = 0;
//		for(int a=0;a<answers.size();a++){
//			score+=(answers.get(a)-1)*10;
//		}
		return score;
	}



}
