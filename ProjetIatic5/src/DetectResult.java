import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
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

import com.atul.JavaOpenCV.Imshow;

public class DetectResult {
	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	String studentId;
	List<Integer> answers = new ArrayList<Integer>();
	String imageUrl;
	int score = 0;
	int isChecked = 0;
	String tamplateUrl = "C:/Users/Jiravat/Documents/inputs/tamplate.png";
	int match_method = Imgproc.TM_CCOEFF;
	
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
	
	public static void showResult(Mat img) {
		Imgcodecs.imwrite("C:/Users/Jiravat/Documents/output.jpg", img);
	}
	
	public void Processing(){
		int resultWidth = 1653;
	    int resultHeight = 2338;
		
		List<Integer> studentNumber = new ArrayList<Integer>();
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

        // / Localizing the best match with minMaxLoc
        MinMaxLocResult mmr = Core.minMaxLoc(result);

        Point matchLoc;
        if (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED) {
            matchLoc = mmr.minLoc;
        } else {
            matchLoc = mmr.maxLoc;
        }
        
        Mat resultMatchTamplete = inputMat.clone();
        
     // / Show me what you got
        Imgproc.rectangle(resultMatchTamplete, matchLoc, new Point(matchLoc.x + templ.cols(),
                matchLoc.y + templ.rows()), new Scalar(0, 255, 0));
        
        showResult(resultMatchTamplete);
	    
	    //Perspective Transform
	    Mat outputMat = new Mat(resultWidth,resultHeight, CvType.CV_8UC1);

	    List<Point> src_pnt = new ArrayList<Point>();
	    
	    
	    
	    Point p0 = new Point(75.0, 75.0);
	    src_pnt.add(p0);
	    Point p1 = new Point(75.0, 100.0);
	    src_pnt.add(p1);
	    Point p2 = new Point(100.0, 100.0);
	    src_pnt.add(p2);
	    Point p3 = new Point(100.0, 75.0);
	    src_pnt.add(p3);
	    Mat startM = Converters.vector_Point2f_to_Mat(src_pnt);

	    List<Point> dst_pnt = new ArrayList<Point>();
	    Point p4 = new Point(0, 0);
	    dst_pnt.add(p4);
	    Point p5 = new Point(0, resultHeight);
	    dst_pnt.add(p5);
	    Point p6 = new Point(resultWidth, resultHeight);
	    dst_pnt.add(p6);
	    Point p7 = new Point(resultWidth, 0);
	    dst_pnt.add(p7);
	    Mat endM = Converters.vector_Point2f_to_Mat(dst_pnt);

	    Mat M = new Mat(3, 3, CvType.CV_32F);
	    Core.perspectiveTransform(startM, endM, M);

	    Size size = new Size(200.0, 200.0);
	    //Scalar scalar = new Scalar(50.0);

	    Imgproc.warpPerspective(inputMat, outputMat, M, size, Imgproc.INTER_LINEAR + Imgproc.CV_WARP_FILL_OUTLIERS);

	    Mat image = outputMat.clone();
	    //showResult(image);
	    
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
	    int row_Student = 0;
	    Rect oldRect = null;
	    int first_x = 277; //269
	    int last_x = 778; //774	    
	    
	    Rect numberRect = new Rect(new Point(70,120), new Point(220,40));
	    Imgproc.rectangle(image, new Point(70,120), new Point(220,40), new Scalar(255,0,0));
	    Mat numberRectMat = imageA.submat(numberRect);
    	double colorNumber = (double)Core.countNonZero(numberRectMat)/(numberRectMat.size().width*numberRectMat.size().height); 
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
	            		//La case cochée
	            		Imgproc.rectangle(image, new Point(rects[b].x,rects[b].y), new Point(rects[b].x+rects[b].width,rects[b].y+rects[b].height), new Scalar(255,0,0));
	            	}
	            	else{
	            		Imgproc.rectangle(image, new Point(rects[b].x,rects[b].y), new Point(rects[b].x+rects[b].width,rects[b].y+rects[b].height), new Scalar(0,0,255));
	            		answer = b+1;
	            		score += b*10;
	            	}
	            	}
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
	            			if(studentNumber.get(l) == -1)studentNumber.set(l, row_Student+1);
	            		}
	            	}
	            	if(rect.y - oldRect.y > 30){
	            		row_Student++;
	            		col_Student = 0;
	            		if(last_x - oldRect.x > 63){
	            			int col_toAdd = ((int)Math.round((last_x - oldRect.x)/68.0));
	            			for(int l=0;l<col_toAdd;l++){
		            			if(studentNumber.get(7-l) == -1)studentNumber.set(7-l, row_Student-1);
		            		}
	            		}
	            	}
	            	if (rect.x < first_x && col_Student == 0){
	            		first_x = rect.x;	
	            	}
	            	if(rect.x - first_x > 64  && col_Student == 0){
	            		int colToAdd = (int) (col_Student + Math.round((rect.x - first_x)/68.0));
	            		
	            		for(int l=0;l<colToAdd;l++){		            		
		            		if(studentNumber.get(l) == -1)studentNumber.set(l, row_Student);
		            		col_Student++;
	            		}
	            	}
	            	else if(rect.x - oldRect.x > 120){
	            		for(int l=0;l<Math.round((rect.x - oldRect.x- 43)/68);l++){
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
	    	if(studentNumber.get(i) == -1){
	    		studentNumber.set(i, 9);
	    	}
	    	studentId+=studentNumber.get(i);
	    }	
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
		Imgproc.putText(image, ""+score ,new Point(70,120),1,7,new Scalar(0, 0, 255),7); 
		Imgcodecs.imwrite(imageUrl, image);
	}

	public int getScore() {
		return score;
	}
	
	
	
}
