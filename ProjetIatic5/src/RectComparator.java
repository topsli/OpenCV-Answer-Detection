import org.opencv.core.Rect;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tops
 * @version v1.0 on on 2017/11/24 0024.
 */
public  class RectComparator implements Comparator<Rect> {

    private int middleX;

    public RectComparator(){

    }

    public RectComparator(int middleX) {
        this.middleX = middleX;
    }

    /**
     * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
     */
    @Override
    public int compare(Rect o1, Rect o2) {
        System.out.println( "o1("+o1.x + ","+o1.y +")");
        System.out.println( "o2("+o2.x + ","+o2.y +")");
         if(middleX > 0) {
             if(o1.x > middleX && o2.x + 20 < middleX){
                 return 1;
             }
             if(o2.x > middleX && o1.x + 20 < middleX){
                 return -1;
             }
         }

        if(o1.y - o2.y > 15){
            return 1;
        }
        else if(o2.y -  o1.y > 15 ){
            return -1;
        }
        else if(o1.x - o2.x > 15 ){
            return 1;
        }
        else if( o2.x - o1.x > 15 ){
            return -1;
        }
        return 0;
    }

    public  boolean checkDuplicate(Rect rect,List<Rect> list) {

        for(int i = 0; i < list.size(); i++){

            Rect rm = list.get(i);
            if(rm.equals(rect)){
                continue;
            }

            if(  Math.abs(rm.x - rect.x)  < 15  && Math.abs(rm.y - rect.y) < 15
                    && Math.abs(rm.x - rect.x)  > 2 && Math.abs(rm.y - rect.y) > 2
                    ){
                return true;
            }
        }

        return false;
    }
}
