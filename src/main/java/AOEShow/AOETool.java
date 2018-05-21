package AOEShow;

import AOEShow.Model.AOEPoint;

import java.awt.*;
import java.util.Random;

/**
 * Created by UKi_Hi on 2018/4/29.
 */
public class AOETool {

    public static  int[][] randDistance(int n){
        //初始化随机
        int[][] distanceArr = new int[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                //i为行，j为列，i<j时置为-1，i=j为0，i>j为随时（默认数字)
                //默认一个点最多连三个，最少连一个
                Random random1 = new Random();
                int K = random1.nextInt();
                int K_index = Math.floorMod(K,3)+1;
                if(i>j||j>i+K_index){
                    distanceArr[i][j]=-1;
                }
                else if(i==j){
                    distanceArr[i][j]=0;
                }else {
                    Random random2 = new Random();
                    int i1 = random2.nextInt();
                    int index = Math.floorMod(i1,AOEStatic.distanceRange.length);
                    distanceArr[i][j]=AOEStatic.distanceRange[index];
                }

            }
        }
        return distanceArr;
    }
    //初始化了节点
    public static AOEPoint[] initPointByArr(int[][] distanceArr){
        //第一点，默认窗口中间
        AOEPoint[] aoePoints = new AOEPoint[distanceArr.length];

        for(int i=0;i<distanceArr.length;i++){

            int[] distance = distanceArr[i];
            //对一排从小到大排序，便于从上往下绘制
//            Arrays.sort(distance);
            for(int j=0;j<distance.length;j++){
                //如果为0，则为自身，跳过
                if(distanceArr[i][j]==0){
                    //初始化第一个point
                    if(aoePoints[i]==null){
                        aoePoints[i]=new AOEPoint(AOEStatic.FirstPointX,AOEStatic.FirstPointY);
                        aoePoints[i].setInvoke(true);
                        aoePoints[i].setIndex(i+1);
                        aoePoints[i].setVe(0);
                        aoePoints[i].setVl(0);
                    }
                    continue;
                }
                //如果为-1，那么不可达，跳过
                if(distanceArr[i][j]==-1)
                    continue;
                //其他情况，计算角度
                //为避免重复绘制，所以点需要标识，绘制了的话就不需要初始化了
                if(aoePoints[j]==null){
                    int index = aoePoints[i].getNextNum();
                    Point point = AOETool.calPointByIndex(aoePoints[i].getX(),aoePoints[i].getY(),index,distanceArr[i][j]);
                    aoePoints[j] = new AOEPoint(point);
                    aoePoints[j].setIndex(j+1);
                }
                aoePoints[i].addNext(aoePoints[j],distanceArr[i][j]);
                aoePoints[j].addPrv(aoePoints[i],distanceArr[i][j]);

                aoePoints[j].setPreToCurrentTime(aoePoints[i].getIndex(),0);
                aoePoints[j].setToPrvVlTime(aoePoints[i].getIndex(),0);


            }
        }
        return aoePoints;
    }
    //初始化计算vl节点
    public static void initVl(AOEPoint[] aoePoints){
        //Ve最大的就是节点
        int endPoint = -1;
        int maxVe = -1;
        for(int i=0;i<aoePoints.length;i++){
            if(maxVe<=aoePoints[i].getVe()){
                maxVe = aoePoints[i].getVe();
                endPoint = i;
            }
        }
        aoePoints[endPoint].setVlInvoke(true);
        aoePoints[endPoint].setVl(maxVe);
    }

    /*
    需要根据初始矩阵绘制点
    以后根据List点，重新绘制(以及绘制线）
     */

    //需要一个方法计算角度
    public static  Point calPointByIndex(int x, int y, int index, int day){
        //根据默认角度计算x的值（从上往下）
        int length = AOEStatic.DefaultDistance*day;
        double angle = 1.0*AOEStatic.DefaultAngle*index/180*Math.PI;
        int offx = new Long(Math.round(length*Math.sin(angle))).intValue();
        int offy = - new Long(Math.round(length*Math.cos(angle))).intValue();
        return new Point(x+offx,y+offy);
    }
}
