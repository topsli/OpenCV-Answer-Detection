import java.util.ArrayList;
import java.util.List;

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

public class DetectResult {
	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	String studentId;
	List<Integer> answers = new ArrayList<Integer>();
	String imageUrl;
	int score = 0;
	int isChecked = 0;
	
	public DetectResult(){
		this.score = 0;
	}
	
	public void Processing(){
		List<Integer> studentNumber = new ArrayList<Integer>();
		String number = "00000000";
		String[] testStudentNumber = number.split("");
		for(int i = 0; i < testStudentNumber.length; i++) {
			studentNumber.add(Integer.parseInt(testStudentNumber[i]));
		}
		
		Mat image = Imgcodecs.imread(imageUrl, Imgproc.COLOR_BGR2GRAY);
		//Imgproc.resize(image,image,new Size(1653,2339));
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

	    //check if it's checked
	    Rect numberRect = new Rect(new Point(70,120), new Point(220,40));
	    Imgproc.rectangle(image, new Point(70,120), new Point(220,40), new Scalar(255,0,0));
	    //Imgcodecs.imwrite("C:/Users/Jiravat/Pictures/output.png", image);
	    Mat numberRectMat = imageA.submat(numberRect);
    	double colorNumber = (double)Core.countNonZero(numberRectMat)/(numberRectMat.size().width*numberRectMat.size().height); // you want execute ??ok
    	System.out.println(colorNumber);
    	if(colorNumber < 0.9){
    		isChecked = 1;
    	}
    	else{
    		isChecked = 0;
    	}
	    
	    for(int i=contours.size()-1; i>0 ;i--){
	        if (Imgproc.contourArea(contours.get(i)) > 50 ){
	        	
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            int answer = 1;
	            if ((rect.height > 50 && rect.height < 70) && (rect.width > 120 && rect.width < 200))	            
	            {
	            	Rect bigRect = rect.clone();
	            	Imgproc.rectangle(image, new Point(bigRect.x-10,bigRect.y-10), new Point(bigRect.x+bigRect.width+10,bigRect.y+bigRect.height+10), new Scalar(0,255,0));
	            	
	            	Rect rect1 = new Rect(bigRect.x,bigRect.y,bigRect.width/3,bigRect.height);
	            	Rect rect2 = new Rect(bigRect.x+bigRect.width/3,bigRect.y,bigRect.width/3,bigRect.height);
	            	Rect rect3 = new Rect(bigRect.x+(bigRect.width/3*2),bigRect.y,bigRect.width/3,bigRect.height);
	            	Rect[] rects ={rect1, rect2, rect3};
	            	for (int b =0;b<rects.length;b++){
	            	Mat boxMat = imageA.submat(rects[b]);
	            	
	            	double color = (double)Core.countNonZero(boxMat)/(boxMat.size().width*boxMat.size().height);
	            	if(color > 0.8){
	            		Imgproc.rectangle(image, new Point(rects[b].x,rects[b].y), new Point(rects[b].x+rects[b].width,rects[b].y+rects[b].height), new Scalar(255,0,0));
	            	}
	            	else{
	            		Imgproc.rectangle(image, new Point(rects[b].x,rects[b].y), new Point(rects[b].x+rects[b].width,rects[b].y+rects[b].height), new Scalar(0,0,255));
	            		answer = b+1;
	            		score += b*10;
	            	}
	            	}
	            	answers.add(answer);
	            	System.out.println("Question :"+question+" Answer is :"+answer);
	            	question++;
	            } 
	            
	            if ((rect.height > 22 && rect.height < 24) && (rect.width > 23 && rect.width < 26))	       
	            {
	            	Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255,0,0));
            		
	            	if(oldRect == null){
	            		oldRect = rect;
	            	}
	            	if (rect.x < first_x){
	            		first_x = rect.x;
	            	}
	            	if(rect.y - oldRect.y > 30){
	            		row_Student++;
	            		col_Student = 0;
	            		if(last_x - oldRect.x > 68){
	            			for(int l=0;l<(last_x- oldRect.x)/70;l++){
		            			studentNumber.set(7-l, row_Student-1);
		            		}
	            		}
	            	}
	            	if(rect.x - first_x > 68 && col_Student == 0){
	            		for(int l=0;l<(rect.x - first_x)/70;l++){
	            			studentNumber.set(l, row_Student);
	            			col_Student++;
	            		}
	            		
	            	}
	            	else if(rect.x - oldRect.x >  135){
	            		for(int l=0;l<(rect.x - oldRect.x- 68)/70;l++){
	            			studentNumber.set(col_Student, row_Student);
	            			col_Student++;
	            		}
	            	}
	            	col_Student++;
	            	oldRect = rect;
	            }
	        }	      
	    }
	    studentId = "";
	    for(int i=0;i<studentNumber.size();i++){
	    	studentId+=studentNumber.get(i);
	    }
	    System.out.print("Student Number is: "+studentId);
	    //Imgcodecs.imwrite("C:/output.png", image);	
	}
	
	public String getStudentId(){
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
		Mat image = Imgcodecs.imread(imageUrl);
		this.isChecked = 1;
		//System.out.println("print score = "+score);
		Imgproc.putText(image, ""+score ,new Point(70,120),1,7,new Scalar(0, 0, 255),7); 
		Imgcodecs.imwrite(imageUrl, image);
	}

	public int getScore() {
		return score;
	}
	
	
	
}
