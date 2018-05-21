package AOEShow;

import java.awt.*;
/**
 * Created by UKi_Hi on 2018/4/29.
 */
public class AOEStatic {
    public static Color DefaultPointColor = Color.red;
    public static Color InvokePointColor = Color.CYAN;
    public static Color VlInvokePointColor = Color.YELLOW;


    public static Color DefaultLineColor = Color.GRAY;
    public static Color InvokeLineColor = Color.BLUE;
    public static Color VlInvokeLineColor = Color.MAGENTA;

    public static Color DefaultStringColor = Color.GRAY;


    public static int DefaultAngle = 50;
    //像素
    public static int DefaultDistance = 50;
    //半径
    public static int DefaultRadius = 10;

    public static int FirstPointX = 500;
    public static int FirstPointY = 350;

    //主窗口大小
    public static int mainWidth = 1500;
    public static int mainHeight = 1000;

    //随机距离
    public static int DefaultPointNum = 7;
    public static int[] distanceRange = {1,2,3,4,5,6};
    public static int[][] DefaultDistanceArr = {
            {0,3,2,6,-1,-1,-1},
            {-1,0,-1,2,4,-1,-1},
            {-1,-1,0,1,-1,3,-1},
            {-1,-1,-1,0,1,-1,-1},
            {-1,-1,-1,-1,0,-1,3},
            {-1,-1,-1,-1,-1,0,4},
            {-1,-1,-1,-1,-1,-1,0},
    };

}
