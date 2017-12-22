/**
 * @author Tops
 * @version v1.0 on on 2017/11/28 0028.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 用来识别EAN13方式编码的图片
 * 图片高度像素必须大于 30px，宽度像素必须大于 200px
 * 图片区域内只包含条码和空白部分，且左右两端的空白部分必须在 10%-20%左右
 * @author tangkf
 */
public class EAN13Reader {
    static int HEIGHT   = 6;    //截取的样本图像像素高度
    static int BW_MID   = 100;  //判断黑白的阀值

    static int [][] LEFT_TABLE_INT      = new int[] []{
            {3,2,1,1},{2,2,2,1},{2,1,2,2},{1,4,1,1},{1,1,3,2},
            {1,2,3,1},{1,1,1,4},{1,3,1,2},{1,2,1,3},{3,1,1,2},
            {1,1,2,3},{1,2,2,2},{2,2,1,2},{1,1,4,1},{2,3,1,1},
            {1,3,2,1},{4,1,1,1},{3,1,2,1},{3,1,2,1},{2,1,1,3}
    };
    static int [][] RIGHT_TABLE_INT     = new int[] []{
            {3,2,1,1},{2,2,2,1},{2,1,2,2},{1,4,1,1},{1,1,3,2},
            {1,2,3,1},{1,1,1,4},{1,3,1,2},{1,2,1,3},{3,1,1,2}
    };

    /**
     * 测试
     * @author tangkf
     * @param args
     */
    public static void main(String[] args) {
        long a  = System.currentTimeMillis();
        String src  = "f:/zxing_code5.png";
        BufferedImage img   = getImg(src);
        long b  = System.currentTimeMillis();
        System.out.println("read image cost time:"+(b-a)+" ms");
        System.out.println("result:"+getRealValue(img));
        long c  = System.currentTimeMillis();
        System.out.println("decode image cost time:"+(c-b)+" ms");
    }

    /**
     * 根据条码图像区域返回条码数据
     * @author tangkf
     * @param img 图像区域
     * @return
     */
    public static String getRealValue(BufferedImage img){
        int w   = img.getWidth();
        int h   = img.getHeight();
        int y   = (int)(h/2);
        if(y<HEIGHT){
            return "无法识别的条码";
        }

        if(h>HEIGHT*2) y= (int)(h/2)-(HEIGHT/2); else y = 1;

        StringBuffer valuestr   = new StringBuffer("");
        int chg = 0;    //记录颜色变化次数
        int gvn = 0;    //记录实际取值次数（每变化一次取值一次）
        int ln  = 0;    //记录同色连续像素的数量
        int uprv= -1;   //记录上次的像素取值
        int xdv = 0;    //参考底色，取前8个像素的颜色作为底色参考值
        int[]   gvalue  = new int[4];   //临时记录一组数字

        for(int i=0;i<w;i++){
            int v   = 0;    //记录取值为深色的次数

            //高度取HEIGHT个像素,并求平均值，如果平均值为浅色，那么取0，否则取1
            for(int j=y;j<y+HEIGHT;j++){
                int prgb    = getValue(img.getRGB(i,j));
                xdv += prgb;
                if(prgb<BW_MID-80) v+=1; //累加该列为深色的像素个数
            }
            int rv  = 0;
            if(i<8){
                //取参考颜色样本，默认都为0
                rv  = 0;
                BW_MID  = xdv/((i+1)*(HEIGHT)); //不断变换参考样本值
            }else{
                rv  = v>HEIGHT/2?1:0;    //n个以上的像素为深色，就表示该列为深色
            }

            if(rv==uprv){
                ln++;
            }else{
                chg++;  //变化次数+1
                if(chg>1 && chg<6){
                    //起始符号
                    gvalue[(chg-2)%4]   = ln;
                    if((chg-1)%4==0){
                        valuestr.append(getSTDValue(gvalue, LEFT_TABLE_INT));
                        //System.err.println(gvalue[0]+""+gvalue[1]+gvalue[2]+gvalue[3]);
                    }
                }
                if((chg>5 && chg<30)|| (chg>34 && chg<59)){
                    gvn++;
                    if(gvn%4==0){
                        gvalue[(gvn-1)%4]   = ln;
                        if(gvn<=24){
                            valuestr.append(getSTDValue(gvalue, LEFT_TABLE_INT));
                            //valuestr.append(ln+ " RV:"+ getSTDValue(gvalue, LEFT_TABLE_INT) + "\n");
                        }else{
                            valuestr.append(getSTDValue(gvalue, RIGHT_TABLE_INT));
                            //valuestr.append(ln+ " RV:"+ getSTDValue(gvalue, RIGHT_TABLE_INT) + "\n");
                        }
                    }else {
                        gvalue[(gvn-1)%4]   = ln;
                    }
                }
                ln  = 1;
            }
            uprv    = rv;
        }
        return valuestr.toString();
    }

    /**
     * 由一组样本与基准表通过方差和的方式去噪求值
     * @param mvalues
     * @param STDTABLE
     * @return
     */
    public static int getSTDValue(final int[] mvalues,int[][] STDTABLE){
        int rvalue  = 0;
        int fx  = 0;
        for(int i=0;i<STDTABLE.length;i++){
            int[] svalues   = LEFT_TABLE_INT[i];    //基准值
            int[] xvalue    = new int[4];
            for(int j=0;j<mvalues.length;j++){
                xvalue[j]   = (int)((double)mvalues[j]/svalues[j]+0.5);
            }
            int tmp = getFX(xvalue);

            if(i==0){
                fx  = tmp;
            }else{
                if(tmp<fx){
                    fx  = tmp;
                    rvalue  = i>9?i-10:i;
                }
            }
        }
        return rvalue;
    }

    /**
     * 求一组样本数的方差和(针对所有样本数都相同的情况简化算法)
     * @param numlist
     */
    public static int getFX(int[] numlist){
        if(numlist.length<1) return 0;
        int fx  = 0;
        double avg  = 0;
        for(int i=0;i<numlist.length;i++){
            avg += numlist[i];
        }
        avg = (int)((double)avg/numlist.length+0.5);

        for(int i=0;i<numlist.length;i++){
            fx  += (numlist[i]-avg)*(numlist[i]-avg);
        }
        return fx;
    }

    /**
     * 由色彩值返回单色信号 1 或 0
     * @param rgb
     * @return
     */
    public static int getValue(int rgb){
        int r   = (rgb>>16)&255;
        int g   = (rgb>>8)&255;
        int b   = rgb&255;
        double avg  = 0.299*r+0.587*g+0.114*b; //判断黑白
        return (int)avg;
    }

    /**
     * 读取图片
     * @param srcImg
     * @return
     */
    public static BufferedImage getImg(String srcImg){
        BufferedImage img   = null;
        try {
            img = ImageIO.read(new File(srcImg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}