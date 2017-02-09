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
	
	Boolean checkAllNot(List<Integer> testStudentNumber){
		for(int i=0;i<testStudentNumber.size();i++){
			if(testStudentNumber.get(i) == -1){
				return true;
			}
		}
		return false;
	}
	
	public DetectResult(){
		this.score = 0;
	}
	
	public void Processing(){
		List<Integer> studentNumber = new ArrayList<Integer>();
		String number = "99999999";
		String[] testStudentNumber = number.split("");
		for(int i = 0; i < testStudentNumber.length; i++) {
			//studentNumber.add(Integer.parseInt(testStudentNumber[i]));
			studentNumber.add(-1);
		}
		
		// Consider the image for processing
	    Mat image = Imgcodecs.imread(imageUrl, Imgproc.COLOR_BGR2GRAY); //input picture
	    Imgproc.resize(image,image,new Size(1653,2338));
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
	    int first_x = 277; //269
	    int last_x = 778; //774	    
	    
	  //check if it's checked
	    Rect numberRect = new Rect(new Point(70,120), new Point(220,40));
	    Imgproc.rectangle(image, new Point(70,120), new Point(220,40), new Scalar(255,0,0));
	    //Imgcodecs.imwrite("C:/Users/Jiravat/Pictures/output.png", image);
	    Mat numberRectMat = imageA.submat(numberRect);
    	double colorNumber = (double)Core.countNonZero(numberRectMat)/(numberRectMat.size().width*numberRectMat.size().height); 
    	System.out.println(colorNumber);
    	if(colorNumber < 0.9){
    		isChecked = 1;
    	}
    	else{
    		isChecked = 0;
    	}
	    
	    for(int i=contours.size()-1; i>0 ;i--){
	        if (Imgproc.contourArea(contours.get(i)) > 50){
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            int answer = 1;
	            if ((rect.height > 20 && rect.height < 48) && (rect.width > 120 && rect.width < 200))	            
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
	            	
	            	double color = (double)Core.countNonZero(boxMat)/(boxMat.size().width*boxMat.size().height);
	            	if(color > 0.7){
	            		//It's check box 
	            		Imgproc.rectangle(image, new Point(rects[b].x,rects[b].y), new Point(rects[b].x+rects[b].width,rects[b].y+rects[b].height), new Scalar(255,0,0));
	            	}
	            	else{
	            		Imgproc.rectangle(image, new Point(rects[b].x,rects[b].y), new Point(rects[b].x+rects[b].width,rects[b].y+rects[b].height), new Scalar(0,0,255));
	            		answer = b+1;
	            		score += b*10;
	            	}
	            	}
	            	System.out.println("Question :"+question+" Answer is :"+answer);
	            	answers.add(answer);
	            	question++;
	            } 
	            
	            if ((rect.height > 21 && rect.height < 25) && (rect.width > 21 && rect.width < 25) && (rect.y<550) && checkAllNot(studentNumber))  //here i fix it      
	            {
	            	Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0,0,255));
	            	if(oldRect == null){
	            		oldRect = rect;
	            	}
	            	if(rect.y - oldRect.y > 60){
	            		for(int l=0;l<8;l++){
            				//System.out.println("last_x - oldRect :" + (last_x - oldRect.x) + (7-l) + (row_Student-1));
            				System.out.println("9 add " + (l)  + " " + (row_Student+1));
	            			if(studentNumber.get(l) == -1)studentNumber.set(l, row_Student+1);
	            		}
	            	}
	            	if(rect.y - oldRect.y > 30){
	            		row_Student++;
	            		col_Student = 0;
	            		
	            		if(last_x - oldRect.x > 63){
	            			//System.out.println("3 add " + ((int)Math.floor((last_x - oldRect.x)/63.0)) + " " + (row_Student-1));
	            			int col_toAdd = ((int)Math.round((last_x - oldRect.x)/68.0));
	            			for(int l=0;l<col_toAdd;l++){
	            				//System.out.println("last_x - oldRect :" + (last_x - oldRect.x) + (7-l) + (row_Student-1));
	            				System.out.println("3 add " + (7-l)  + " " + (row_Student-1));
		            			if(studentNumber.get(7-l) == -1)studentNumber.set(7-l, row_Student-1);
		            		}
	            		}
	            	}
	            	if (rect.x < first_x && col_Student == 0){
	            		first_x = rect.x;	
	            	}
	            	if(rect.x - first_x > 64  && col_Student == 0){
	            		//System.out.println(Math.ceil((rect.x - first_x)/64.0)-1);
	            		int colToAdd = (int) (col_Student + Math.round((rect.x - first_x)/68.0));
	            		
	            		for(int l=0;l<colToAdd;l++){
	            			System.out.println("1 add "+(l)+" "+(row_Student));
		            		
		            		if(studentNumber.get(l) == -1)studentNumber.set(l, row_Student);
		            		col_Student++;
	            		}
//	            		for(int l=0;l<(rect.x - first_x)/66;l++){
//	            			System.out.println((rect.x - first_x)/66);
//	            			if(studentNumber.get(l) == -1)studentNumber.set(l, row_Student);
//	            			System.out.println("1 add "+(l)+" "+(row_Student));
//	            			col_Student++;
//	            		}
	            	}
	            	else if(rect.x - oldRect.x > 120){// && rect.x - oldRect.x < 160){
	            		System.out.println(rect.x - oldRect.x);
	            		for(int l=0;l<Math.round((rect.x - oldRect.x- 43)/68);l++){
	            			studentNumber.set(col_Student, row_Student);
	            			System.out.println("2 add "+(col_Student)+" "+(row_Student));
	            			col_Student++;
	            		}
	            	}
	            	col_Student++;
	            	oldRect = rect;
	            }
	        }	      
	    }
	    System.out.print("Student Number is: ");
	    studentId = "";
	    for(int i=0;i<studentNumber.size();i++){
	    	if(studentNumber.get(i) == -1){
	    		studentNumber.set(i, 9);
	    	}
	    	System.out.print(""+studentNumber.get(i));
	    	studentId+=studentNumber.get(i);
	    }
	    Imgcodecs.imwrite("C:/Users/Benreghai/Desktop/Inputs/output.jpg", image); //ouput 
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
