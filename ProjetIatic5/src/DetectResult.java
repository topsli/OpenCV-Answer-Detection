import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
	List<Integer> answers = new ArrayList<Integer>();
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
		Imgcodecs.imwrite("C:/Users/Jiravat/Documents/"+fileName, img);
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
//        Mat dst = inputMat.clone();
        
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
        
        //showResult(dst, "outputGetPoint.jpg");
	    
        
	    //Perspective Transform
	    Mat outputMat = new Mat(resultWidth,resultHeight, CvType.CV_8UC1);

	    
//	    System.out.println(src_pnt2);
	    
	    
//	    Point p0 = new Point(75.0, 75.0);
//	    src_pnt.add(p0);
//	    Point p1 = new Point(75.0, 100.0);
//	    src_pnt.add(p1);
//	    Point p2 = new Point(100.0, 100.0);
//	    src_pnt.add(p2);
//	    Point p3 = new Point(100.0, 75.0);
//	    src_pnt.add(p3);
	    
//	    Point tmp = src_pnt.get(3);
//	    src_pnt.set(3, src_pnt.get(2));
//	    src_pnt.set(2, src_pnt.get(1));
//	    src_pnt.set(1, tmp);
	    
	    //List<Point> src_pnt = new ArrayList<Point>();
//	    src_pnt.add(src_pnt2.get(0));
//	    src_pnt.add(src_pnt2.get(1));
//	    src_pnt.add(src_pnt2.get(2));
//	    src_pnt.add(src_pnt2.get(4));
	    
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
	    
//	    System.out.println(src_pnt);
//	    System.out.println(dst_pnt);

	    Mat M = Imgproc.getPerspectiveTransform(startM, endM);
//	    Mat M = new Mat(3, 3, CvType.CV_32F);
//	    Core.perspectiveTransform(startM, endM, M);

	    Size size = new Size(inputMat.cols(), inputMat.rows());
//	    Scalar scalar = new Scalar(50.0);

	    Imgproc.warpPerspective(inputMat, outputMat, M, size);
	    
	    image = outputMat.clone();
	    //showResult(image, "outputPerSpectiveTest.jpg");
	    
	    Imgproc.resize(image,image,new Size(1653,2338)); //mise en echelle
	    Mat imageHSV = new Mat(image.size(), CvType.CV_8UC4);
	    Mat imageBlurr = new Mat(image.size(),CvType.CV_8UC4);
	    Mat imageA = new Mat(image.size(), CvType.CV_32F);
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5); 
	    Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    
	    int question = 0;
	    int col_Student = 0;
//	    int row_Student = 0;
//	    Rect oldRect = null;
//	    int first_x = 277; //269
//	    int last_x = 778; //774	    
	    
	    
	    //check number in top left if colorNumber<0.9 mean ischecked
//	    Rect numberRect = new Rect(new Point(70,120), new Point(220,40));
//	    //Imgproc.rectangle(image, new Point(70,120), new Point(220,40), new Scalar(255,0,0));
//	    Mat numberRectMat = inputMat.submat(numberRect);
//    	double colorNumber = (double)Core.countNonZero(numberRectMat)/(numberRectMat.size().width*numberRectMat.size().height); 
//    	if(colorNumber < 0.9){
//    		isChecked = 1;
//    	}
//    	else{
//    		isChecked = 0;
//    	}
	    
    	List<Rect> listRects = new ArrayList<Rect>();
    	
    	int numOfStudentId = 0;
    	
	    for(int i=contours.size()-1; i>0 ;i--){
	    	//check contours
	    	
        	
	        if (Imgproc.contourArea(contours.get(i)) > 600){
//	        	Rect _rect = Imgproc.boundingRect(contours.get(i));
//		    	Rect _bigRect = _rect.clone();
//		    	Rect _rect1 = new Rect(_bigRect.x,_bigRect.y,_bigRect.width/3,_bigRect.height);
//	        	Rect _rect2 = new Rect(_bigRect.x+_bigRect.width/3,_bigRect.y,_bigRect.width/3,_bigRect.height);
//	        	Rect _rect3 = new Rect(_bigRect.x+(_bigRect.width/3*2),_bigRect.y,_bigRect.width/3,_bigRect.height);
//	        	Rect[] _rects ={_rect1, _rect2, _rect3};
//	        	for (int b =0;b<_rects.length;b++){
//	        		Imgproc.rectangle(image, new Point(_rects[b].x,_rects[b].y), new Point(_rects[b].x+_rects[b].width,_rects[b].y+_rects[b].height), new Scalar(255,0,0));
//	        	}
	        	
	        	
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            int answer = 1;
	            //3box
//	            if ((rect.height > 45 && rect.height < 55) && (rect.width > 150 && rect.width < 200))
	            if ((rect.height > 40 && rect.height < 60) && (rect.width > 140 && rect.width < 210))	            
	            {
	            	Rect bigRect = rect.clone();
	            	
	            	Rect bigRectforgetline = bigRect.clone();
	            	bigRectforgetline.width = bigRectforgetline.width - 20;
	            	bigRectforgetline.x =  bigRectforgetline.x + 10;
	            	//get Line of big rect
//	            	Mat bigRectMat = imageA.submat(bigRectforgetline);
//	            	Mat resultbigRectMat = bigRectMat.clone();//
	            	//Imgproc.cvtColor(bigRectMat, bigRectMat, Imgproc.COLOR_YUV420sp2RGB, 4);
	                //Imgproc.cvtColor(bigRectMat, bigRectMat, Imgproc.COLOR_RGB2GRAY, 4);
	                
//	            	Imgproc.threshold(bigRectMat,bigRectMat, 253, 255, Imgproc.THRESH_BINARY_INV+Imgproc.THRESH_MASK); 
	            	//Imgproc.Canny(bigRectMat, bigRectMat,100,150, 3, true);
	            	
	            	
//	            	Mat lines = new Mat();
//	            	int thresholdline = -253;
//	                int minLineSize = 30;//30
//	                int lineGap = 6;
//	                Imgproc.HoughLinesP(bigRectMat, lines, 2, Math.PI/180, thresholdline, minLineSize, lineGap);
	                
	                
	                
	            	//draw line
//	            	System.out.println("Number Of Lines :"+lines.rows());
//	            	for (int x = 0; x < lines.rows(); x++) 
//	                {
//	                      double[] vec = lines.get(x, 0);
//	                      double x1 = vec[0], 
//	                             y1 = vec[1],
//	                             x2 = vec[2],
//	                             y2 = vec[3];
//	                      Point start = new Point(x1, y1);
//	                      Point end = new Point(x2, y2);
//
//	                      Imgproc.line(resultbigRectMat, start, end, new Scalar(255,0,0), 1);
//	                }
//	            	showResult(bigRectMat, "getLineinbox"+question+".jpg");
	            	
//	            	int numberQuestionBox = lines.rows()+1;
	            	int numberQuestionBox = 4;
	            	Imgproc.rectangle(image, new Point(bigRect.x-10,bigRect.y-10), new Point(bigRect.x+bigRect.width+10,bigRect.y+bigRect.height+10), new Scalar(0,255,0));
	            	//this part split bigbox to 3 boxs
	            	List<Rect> rects = new ArrayList<Rect>();
	            	int boxWidth = bigRect.width/numberQuestionBox;
	            	for(int r=0;r<numberQuestionBox;r++)
	            	{
	            		Rect rect1 = new Rect(bigRect.x+(boxWidth*r)+10,bigRect.y+5,boxWidth-20,bigRect.height-10);
	            		rects.add(rect1);
	            	}
//	            	Rect rect1 = new Rect(bigRect.x,bigRect.y,bigRect.width/3,bigRect.height);
//	            	Rect rect2 = new Rect(bigRect.x+bigRect.width/3,bigRect.y,bigRect.width/3,bigRect.height);
//	            	Rect rect3 = new Rect(bigRect.x+(bigRect.width/3*2),bigRect.y,bigRect.width/3,bigRect.height);
	            	
//	            	System.out.println(rects);
	            	List<Integer> colorNonZeros = new ArrayList<Integer>();
	            	for (int b =0;b<numberQuestionBox;b++){
		            	Mat boxMat = imageA.submat(rects.get(b));
		            	
		            	//Imgproc.threshold(boxMat,boxMat, 250, 255, Imgproc.THRESH_BINARY_INV+Imgproc.THRESH_OTSU); 
		            	//System.out.println(Core.countNonZero(boxMat));
		            	//colorNonZeros.add(Core.countNonZero(boxMat));
//		            	showResult(boxMat, "boxmat"+question+"box"+b+".jpg");
		            	float color = (float) (Core.countNonZero(boxMat)/(boxMat.size().width*boxMat.size().height));
//		            	System.out.println("Color:"+color);
		            	if(color < 0.85){
		            		//La case cochée
		            		Imgproc.rectangle(image, new Point(rects.get(b).x,rects.get(b).y), new Point(rects.get(b).x+rects.get(b).width,rects.get(b).y+rects.get(b).height), new Scalar(255,0,0));
		            		answer = b+1;
//		            		score += b*10;
		            	}
		            	else{
		            		Imgproc.rectangle(image, new Point(rects.get(b).x,rects.get(b).y), new Point(rects.get(b).x+rects.get(b).width,rects.get(b).y+rects.get(b).height), new Scalar(0,0,255));
		            		
		            	}
	            	}
//	            	int minIndex = 0;
//	                for (int m = 1; m < numberQuestionBox; m++) {
//	                    int newnumber = colorNonZeros.get(m);
//	                    if ((newnumber < colorNonZeros.get(minIndex))) {
//	                        minIndex = m;
//	                    }
//	                }
//	            	answers.add(minIndex+1);
	            	answers.add(answer);
	            	question++;
	            } 
	            
	           
//	            if ((rect.height > 26 && rect.height < 38) && (rect.width > 26 && rect.width < 38) && (rect.y<550) && checkAllNot(studentNumber))  //here i fix it      
	            if ((rect.height > 24 && rect.height < 45) && (rect.width > 24 && rect.width < 45) && (rect.y<550) && checkAllNot(studentNumber))  //here i fix it      
	            {
	            	//Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0,0,255));
	            	//check color
	            	Mat boxMat = imageA.submat(rect);
	            	double color = (double)Core.countNonZero(boxMat)/(boxMat.size().width*boxMat.size().height);
	            	//System.out.print(color+",");
	            	if(color > 0.6){
	            		Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0,255,0));
	            		//System.out.println("col :"+col_Student+" rect x:"+rect.x+" rect y:"+rect.y);
	            		listRects.add(rect);
	            		
	            	}
	            	else{
	            		numOfStudentId++;
	            		Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0,0,255));
	            		//studentNumber.set(col_Student, row_Student);
	            		//System.out.println("set :"+(8-col_Student)+","+(9-row_Student)+" col :"+col_Student+" rect x:"+rect.x+" rect y:"+rect.y);
	            	}
	            	
	            	col_Student++;
//	            	if(col_Student > 7){
//	            		row_Student++;
//	            		col_Student=0;
//	            	}
	            	
	            	
//	            	if(oldRect == null){
//	            		oldRect = rect;
//	            	}
//	            	if(rect.y - oldRect.y > 60){
//	            		for(int l=0;l<8;l++){
//	            			if(studentNumber.get(l) == -1)studentNumber.set(l, row_Student+1);
//	            		}
//	            	}
//	            	if(rect.y - oldRect.y > 30){
//	            		row_Student++;
//	            		col_Student = 0;
//	            		if(last_x - oldRect.x > 63){
//	            			int col_toAdd = ((int)Math.round((last_x - oldRect.x)/68.0));
//	            			for(int l=0;l<col_toAdd;l++){
//		            			if(studentNumber.get(7-l) == -1)studentNumber.set(7-l, row_Student-1);
//		            		}
//	            		}
//	            	}
//	            	if (rect.x < first_x && col_Student == 0){
//	            		first_x = rect.x;	
//	            	}
//	            	if(rect.x - first_x > 64  && col_Student == 0){
//	            		int colToAdd = (int) (col_Student + Math.round((rect.x - first_x)/68.0));
//	            		
//	            		for(int l=0;l<colToAdd;l++){		            		
//		            		if(studentNumber.get(l) == -1)studentNumber.set(l, row_Student);
//		            		col_Student++;
//	            		}
//	            	}
//	            	else if(rect.x - oldRect.x > 120){
//	            		for(int l=0;l<Math.round((rect.x - oldRect.x- 43)/68);l++){
//	            			studentNumber.set(col_Student, row_Student);
//	            			col_Student++;
//	            		}
//	            	}
//	            	col_Student++;
//	            	oldRect = rect;
	            }
	        }	      
	    }
	    
//	    System.out.println("listRects"+listRects);
//	    System.out.println("numOfStudentId:"+numOfStudentId);
	    if(numOfStudentId > 7){
        	isStudentMark = 1;
		    studentNumber = getStudentsIdFromRect(listRects,studentNumber);
		    studentId = "";
//		    System.out.println(studentNumber);
		    for(int i=0;i<studentNumber.size();i++){
		    	if(studentNumber.get(i) == -1){
		    		studentNumber.set(i, 9);
		    	}
		    	studentId+=studentNumber.get(i);
		    }
	    }else{
	    	studentId = "Student Id Not Found";
	    }
//	    showResult(image, "outputAnswer.jpg");
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

	public List<Integer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Integer> answers) {
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
		for(int a=0;a<answers.size();a++){
			score+=(answers.get(a)-1)*10;
		}
		return score;
	}
	
	
	
}
