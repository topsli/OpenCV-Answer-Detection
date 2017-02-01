import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class traitement {
	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	List<Integer> tabX = new ArrayList<Integer>();
	List<Integer> tabY = new ArrayList<Integer>();

	public traitement()
	{
		List<Integer> studentNumber = new ArrayList<Integer>();
	
		String number = "99999999";
		String[] testStudentNumber = number.split("");
		for(int i = 0; i < testStudentNumber.length; i++) {
			studentNumber.add(Integer.parseInt(testStudentNumber[i]));
		}
		
		// Consider the image for processing
	    Mat image = Imgcodecs.imread("C:/input.jpg", Imgproc.COLOR_BGR2GRAY); //input picture
	    Mat imageHSV = new Mat(image.size(), CvType.CV_8UC4);
	    Mat imageBlurr = new Mat(image.size(),CvType.CV_8UC4);
	    Mat imageA = new Mat(image.size(), CvType.CV_32F);
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5); 
	    Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    int question = 0;
	    int col_Student = 0;
	    int row_Student = 0;
	    Rect oldRect = null;
	    int first_x = 269;//269
	    int last_x = 774;//774
	    
	    //for(int i=0; i< contours.size();i++){
	    for(int i=contours.size()-1; i>0 ;i--){
	        if (Imgproc.contourArea(contours.get(i)) > 50 ){
	        	
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            int answer = 1;
	            if ((rect.height > 50 && rect.height < 70) && (rect.width > 120 && rect.width < 200))	            
	            {
	            	Rect bigRect = rect.clone();
	            	Imgproc.rectangle(image, new Point(bigRect.x-10,bigRect.y-10), new Point(bigRect.x+bigRect.width+10,bigRect.y+bigRect.height+10), new Scalar(0,255,0));
	            	
	            	//this part split bigbox to 3 boxs
	            	Rect rect1 = new Rect(bigRect.x,bigRect.y,bigRect.width/3,bigRect.height);
	            	Rect rect2 = new Rect(bigRect.x+bigRect.width/3,bigRect.y,bigRect.width/3,bigRect.height);
	            	Rect rect3 = new Rect(bigRect.x+(bigRect.width/3*2),bigRect.y,bigRect.width/3,bigRect.height);
	            	Rect[] rects ={rect1, rect2, rect3};
	            	for (int b =0;b<rects.length;b++){
	            	Mat boxMat = imageA.submat(rects[b]);
	            	
	            	//Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0,0,255));
	            	tabX.add(rects[b].x);
	            	tabY.add(rects[b].y);
	            	double color = (double)Core.countNonZero(boxMat)/(boxMat.size().width*boxMat.size().height); // you want execute ??ok
	            	//System.out.println(color);
	            	if(color > 0.8){
	            		//It'scheck box 
	            		Imgproc.rectangle(image, new Point(rects[b].x,rects[b].y), new Point(rects[b].x+rects[b].width,rects[b].y+rects[b].height), new Scalar(255,0,0));
	            		
	            	}
	            	else{
	            		Imgproc.rectangle(image, new Point(rects[b].x,rects[b].y), new Point(rects[b].x+rects[b].width,rects[b].y+rects[b].height), new Scalar(0,0,255));
	            		answer = b+1;
	            	}
	            	}
	            	System.out.println("Question :"+question+" Answer is :"+answer);
	            	question++;
	            } 
	            
	            if ((rect.height > 22 && rect.height < 24) && (rect.width > 23 && rect.width < 26))	 //im not sure about lengh if it mark  ok you can change it next time         
	            //if ((rect.height > 22 && rect.height < 25) && (rect.width > 23 && rect.width < 27))
	            {
	            	Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255,0,0));
            		
	            	if(oldRect == null){
	            		oldRect = rect;
	            		//first_x = rect.x;
	            		//System.out.println(first_x);
	            	}
	            	
	            	//System.out.println(row_Student+" "+col_Student+" "+(rect.x - oldRect.x));
	            	
	            	if (rect.x < first_x){
	            		first_x = rect.x;
	            		
	            	}
	            	
	            	
	            	if(rect.y - oldRect.y > 30){
	            		row_Student++;
	            		col_Student = 0;
	            		if(last_x - oldRect.x > 68){
	            			//System.out.println(last_x- oldRect.x);
	            			for(int l=0;l<(last_x- oldRect.x)/70;l++){
		            			studentNumber.set(7-l, row_Student-1);
		            			System.out.println("3 add "+(7-l)+" "+(row_Student-1));
		            			
		            		}
	            		}
	            		//System.out.println(oldRect.x);
	            	}
	            	if(rect.x - first_x > 68 && col_Student == 0){
	            		//System.out.println((rect.x - first_x));
	            		for(int l=0;l<(rect.x - first_x)/70;l++){
	            			studentNumber.set(l, row_Student);
	            			System.out.println("1 add "+(l)+" "+(row_Student));
	            			col_Student++;
	            		}
	            		
	            	}
	            	
	            	else if(rect.x - oldRect.x >  135){
	            		//System.out.println(row_Student+" "+(col_Student)+" "+(rect.x - oldRect.x));
	            		for(int l=0;l<(rect.x - oldRect.x- 68)/70;l++){
	            			studentNumber.set(col_Student, row_Student);
	            			System.out.println("2 add "+(col_Student)+" "+(row_Student));
	            			col_Student++;
	            		}
	            		//studentNumber.set(col_Student, row_Student);
	            	}
	            	col_Student++;
	            	
	            	//if(col_Student >=8){
	            	
	            	oldRect = rect;
//	            	if (studentNumber.get(col_Student) == row_Student){
//	            		
//	            		Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255,0,0));
//	            		
//	            	}
//	            	else{
//	            		Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0,0,255));
//		            	
//	            	}
//	            	Mat boxMat = imageA.submat(rect);
//	            	double color = (double)Core.countNonZero(boxMat)/(boxMat.size().width*boxMat.size().height); // you want execute ??ok
//	            	System.out.println(color);
//	            	if(color > 0.8){
//	            		//It's not check box 
//	            		Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255,0,0));
//	            		
//	            	}
//	            	else{
//	            		//It's check box
//	            		Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0,0,255));
//	            		studentNumber.add(col_Student, row_Student);
//	            	}
	            }
	        }	      
	    }
	    System.out.print("Student Number is: ");
	    for(int i=0;i<studentNumber.size();i++){
	    	System.out.print(""+studentNumber.get(i));
	    }
	    System.out.println();
	    Imgcodecs.imwrite("C:/output.png", image); //ouput I EXECUTE ??
	}
}
