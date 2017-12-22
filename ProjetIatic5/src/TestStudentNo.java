/**
 * @author Tops
 * @version v1.0 on on 2017/11/23 0023.
 * http://www.cnblogs.com/zendu/p/6694386.html
 */


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class TestStudentNo {



    /**
     * @param imgPath
     * @return String
     */
    public static String decode(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();

            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hints.put(DecodeHintType.PURE_BARCODE,Boolean.TRUE);

            result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 编码
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
    public static void encode(String contents, int width, int height, String imgPath) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        try {

            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();

            hints.put(EncodeHintType.CHARACTER_SET,Boolean.TRUE);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.EAN_13, codeWidth, height, hints);




            MatrixToImageWriter.writeToFile(bitMatrix, "png", new File(imgPath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
//        String imgPath = "f:/zxing_code9.png";
//        // 小浣熊干脆面的条形码
//        String contents =  "6902120160113"   ;//"6925303713003";
//
//        int width = 650, height = 240;
//        encode(contents, width, height, imgPath);

        String imgPath = "F:\\WorkSpace\\OpenProjects\\OpenCV-Answer-Detection\\ProjetIatic5\\outputs\\area1.jpg";
        String decodeContent = decode(imgPath);
        System.out.println("The code as follow:");
        System.out.println(decodeContent);

    }


}
