import org.opencv.core.Rect;

/**
 * @author Tops
 * @version v1.0 on on 2017/11/24 0024.
 */
public class RectYComparator extends RectComparator {
    /**
     * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
     */
    @Override
    public int compare(Rect o1, Rect o2) {

        if(o1.y > o2.y){
            return 1;
        }
        else if(o1.y < o2.y){
            return -1;
        }
        return 0;
    }
}
