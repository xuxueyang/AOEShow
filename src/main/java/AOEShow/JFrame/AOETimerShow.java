package AOEShow.JFrame;

import AOEShow.AOEStatic;
import AOEShow.AOETool;
import AOEShow.Model.AOEPoint;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by UKi_Hi on 2018/4/29.
 */
public class AOETimerShow extends JPanel {
    private Button dayAddButton;
    private Button resetButton;
    private Button autoAddButton;
    private Button autoInitData;

    private boolean autoAddDay=false;

    private int[][] DefaultDistanceArr;
    boolean isNeedDraw = true;
    private AOEPoint[] aoePoints;
    private AOEPoint[] result;
    public AOEPoint getPointByIndex(int index){
        for(int i=0;i<aoePoints.length;i++){
            if(aoePoints[i].getIndex()==index)
                return  aoePoints[i];
        }
        return  null;
    }

    public AOETimerShow(){
        //init 节点
        this.aoePoints = AOETool.initPointByArr(AOEStatic.DefaultDistanceArr);        //创建定时器任务
        //添加一个button
        dayAddButton = new Button("next day");
        dayAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addOneDay();
            }
        });
        this.setLayout(new BorderLayout());
//        this.add(dayAddButton,BorderLayout.SOUTH);
        resetButton = new Button("reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultDistanceArr = AOEStatic.DefaultDistanceArr;
                reset();
            }
        });
        autoAddButton = new Button("AutoRun");
        autoAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autoAddDay = !autoAddDay;
            }
        });
        autoInitData = new Button("Auto Init Data");
        autoInitData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultDistanceArr = AOETool.randDistance(AOEStatic.DefaultPointNum);
                reset();
            }
        });
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,20));
        jPanel.add(dayAddButton);
        jPanel.add(resetButton);
        jPanel.add(autoAddButton);
        jPanel.add(autoInitData);


        this.add(jPanel,BorderLayout.NORTH);
        javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(autoAddDay){
                    addOneDay();
                }
            }
        });
        timer.start();
    }
    private void reset(){
        this.aoePoints = AOETool.initPointByArr(DefaultDistanceArr);
        isNeedDraw = true;
//        this.aoePoints=result;
        repaint();
    }
    public void addOneDay(){
        //TODO 拆分
        //TODO 只有当invoke变化时，才停止（自动演示的时候）
        boolean isAllInvoke = true;
        for(int i=0;i<aoePoints.length;i++){
            if(!aoePoints[i].isInvoke()){
                isAllInvoke=false;
                break;
            }
        }
        if(isAllInvoke){
            //保存结果
            result=aoePoints;
            isNeedDraw=false;
            calVl();
        }
        //第一个激活的是Ve，最早开始时间，第二个激活的是：最晚开始时间Vl。从前往后先算最早开始时间
        //TODO bug:getCurrentTime被多条线共用了。而且因为图，每次getCurrentTime被多加了
        for(int i=0;i<aoePoints.length;i++){
            Iterator iter = aoePoints[i].getNextIter();
            while (iter.hasNext()){
                AOEPoint nextPoint  = (AOEPoint)iter.next();
                int day = aoePoints[i].getNextDayByIndex(nextPoint.getIndex());
                //如果两点均为激活，跳过
                //在基础上再绘制一条完成状态线,如果是非激活才行
                if(aoePoints[i].isInvoke()){
                    if(!nextPoint.isInvoke()){
                        //TODO Invoke用图像方式计算、ve更偏算法计算
                        if(nextPoint.getPrvToCurrentTime(aoePoints[i].getIndex())<=day-1)
                        {
                            nextPoint.setPreToCurrentTime(aoePoints[i].getIndex(),nextPoint.getPrvToCurrentTime(aoePoints[i].getIndex())+1);
                        }
                    }
                }

            }

        }

        //更新状态
        //应该是根据每个点，通过Index获取到导向的点判断Day和CurrentTIme是不是相同，全相同才设为激活
        isAllInvoke = true;
        for(int i=0;i<aoePoints.length;i++){
            AOEPoint aoePoint = aoePoints[i];
            Map<Integer, Integer> currentTimeMap = aoePoint.getCurrentTimeMap();
            Iterator iterator = currentTimeMap.keySet().iterator();
            int size=currentTimeMap.size();
            while(iterator.hasNext()){
                int index = (Integer) iterator.next();
                AOEPoint prv = getPointByIndex(index);
                if(!prv.isInvoke()||prv.getVe()<0){
                    aoePoint.setInvoke(false);
                    continue;
                }
                int day = prv.getNextDayByIndex(aoePoint.getIndex());
                if(aoePoint.getPrvToCurrentTime(prv.getIndex())==day){
                    int max = Math.max(prv.getVe()+day,aoePoint.getVe());
                    aoePoint.setVe(max);
                    size--;
                }
            }
            //如果全部满足，则设为激活状态
            if(size==0){
                //TODO 这里需要停止，或者输出状态
                aoePoint.setInvoke(true);
            }else{
                isAllInvoke = false;
            }

        }
//        System.out.println(isAllInvoke);
        //如果全部Invoke那么计算一下Vl

        this.repaint();
    }
    public void calVl(){
        //计算Vl
        //其实就是反向计算一直，不过这次不需要等待了
        AOETool.initVl(aoePoints);
        for(int i=aoePoints.length-1;i>=0;i--){
            Iterator iter = aoePoints[i].getPrvIter();
            while (iter.hasNext()){
                AOEPoint prvPoint  = (AOEPoint)iter.next();
                int day = prvPoint.getNextDayByIndex(aoePoints[i].getIndex());
                //如果两点均为激活，跳过
                //在基础上再绘制一条完成状态线,如果是非激活才行
                if(aoePoints[i].isVlInvoke()){
                    if(!prvPoint.isVlInvoke()){
                        if(aoePoints[i].getToPrvVlTime(prvPoint.getIndex())<=day-1)
                        {
                            aoePoints[i].setToPrvVlTime(prvPoint.getIndex(),aoePoints[i].getToPrvVlTime(prvPoint.getIndex())+1);
                        }
                    }
                }

            }
        }
        for(int i=0;i<aoePoints.length;i++){
            Iterator iter = aoePoints[i].getNextIter();
            boolean isAllInvoke = true;
            while (iter.hasNext()){
                AOEPoint nextPoint  = (AOEPoint)iter.next();
                int day = aoePoints[i].getNextDayByIndex(nextPoint.getIndex());
                if(nextPoint.isVlInvoke()){
                    if(!aoePoints[i].isVlInvoke()){
                        if(nextPoint.getToPrvVlTime(aoePoints[i].getIndex())==day){
                            int min = Math.min(nextPoint.getVl()-day,aoePoints[i].getVl());
                            aoePoints[i].setVl(min);
                        }else{
                            isAllInvoke = false;
                        }
                    }
                }else {
                    isAllInvoke = false;
                }
            }
            if(isAllInvoke){
                //TODO 这里需要停止，或者输出状态
                aoePoints[i].setVlInvoke(true);
            }
        }

    }
//    private List<AOEPoint> list = new ArrayList<AOEPoint>();

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘制点，定制定时器
        paintPoint(g);
    }
    private void paintPoint(Graphics g){
        //根据矩阵绘制点
        for(int i=0;i<aoePoints.length;i++){
            int x = aoePoints[i].getX();
            int y = aoePoints[i].getY();
            Color color;
            if(aoePoints[i].isInvoke())
                color = AOEStatic.InvokePointColor;
            else
                color = AOEStatic.DefaultPointColor;
            if(aoePoints[i].isVlInvoke())
                color = AOEStatic.VlInvokePointColor;
            //绘制实心点
            g.setColor(color);
            g.fillOval(x,y,AOEStatic.DefaultRadius*2,AOEStatic.DefaultRadius*2);
            //绘制文字
            g.setColor(Color.BLACK);
            g.drawString("V"+aoePoints[i].getIndex(),x+5,y-10);
            //绘制VE
            if(aoePoints[i].isInvoke()){
                g.setColor(Color.BLACK);
                g.drawString("Ve:"+aoePoints[i].getVe(),x+5,y-25);
            }
            if(aoePoints[i].isVlInvoke()){
                //绘制vl
                g.setColor(Color.BLACK);
                g.drawString("Vl:"+aoePoints[i].getVl(),x+5,y-40);
            }
            //绘制每个点与下一个点之间的线
            Iterator iter = aoePoints[i].getNextIter();
            while (iter.hasNext()){
                AOEPoint nextPoint  = (AOEPoint)iter.next();

                g.setColor(AOEStatic.DefaultLineColor);
                int x1=aoePoints[i].getX()+AOEStatic.DefaultRadius;
                int y1=aoePoints[i].getY()+AOEStatic.DefaultRadius;
                int x2=nextPoint.getX()+AOEStatic.DefaultRadius;
                int y2=nextPoint.getY()+AOEStatic.DefaultRadius;
                g.drawLine(x1,y1 , x2,y2);
                g.fillOval((int)(x2-(x2-x1)*0.1)-5,(int)(y2-(y2-y1)*0.1)-5,10,10);
                //绘制文字
                g.setColor(AOEStatic.DefaultStringColor);
                int day = aoePoints[i].getNextDayByIndex(nextPoint.getIndex());
                g.drawString("day:"+day,(x1+x2)/2-15,(y1+y2)/2);

                //在基础上再绘制一条完成状态线,如果是非激活才行
                if(aoePoints[i].isInvoke()){
                    //是不是一直需要绘制蓝色的

//                    if(!nextPoint.isInvoke()||){
                    if(isNeedDraw){
                        //index:4x1：208 y1: 327 x2: 260 y2: 570 currentTime2 day1___因为一条线上刚开始，另一条线的currentTime不为0，导致的
                    int x22=(int)(x1+(x2-x1+0.0)*nextPoint.getPrvToCurrentTime(aoePoints[i].getIndex())/day);
                    int y22=(int)(y1+(y2-y1+0.0)*nextPoint.getPrvToCurrentTime(aoePoints[i].getIndex())/day);
                    g.setColor(AOEStatic.InvokeLineColor);
//                    g.fillOval(x2-5,y2-5,10,10);
                    g.drawLine(x1,y1,x22,y22);
//                        System.out.print("index:"+nextPoint.getIndex()+"x1："+ x1 +" y1: " + y1 + " x2: "+x2 + " y2: "+y2 );
//                        System.out.print(" currentTime: "+nextPoint.getCurrentTime(aoePoints[i].getIndex()));
//                        System.out.print(" day: "+day);
//                        System.out.print(" VE: "+nextPoint.getVe());
//                        System.out.print("\n");
                    }
                }
            }
            //计算vl
            Iterator iterPrv = aoePoints[i].getPrvIter();
            while (iterPrv.hasNext()){
                AOEPoint prvPoint  = (AOEPoint)iterPrv.next();
                int day = prvPoint.getNextDayByIndex(aoePoints[i].getIndex());
                if(aoePoints[i].isVlInvoke()){
                    int x2=prvPoint.getX()+AOEStatic.DefaultRadius;
                    int y2=prvPoint.getY()+AOEStatic.DefaultRadius;
                    int x1=aoePoints[i].getX()+AOEStatic.DefaultRadius;
                    int y1=aoePoints[i].getY()+AOEStatic.DefaultRadius;
                    int x22=(int)(x1+(x2-x1+0.0)*aoePoints[i].getToPrvVlTime(prvPoint.getIndex())/day);
                    int y22=(int)(y1+(y2-y1+0.0)*aoePoints[i].getToPrvVlTime(prvPoint.getIndex())/day);
                    g.setColor(AOEStatic.VlInvokeLineColor);
                    g.drawLine(x1,y1,x22,y22);

                }
            }
            //绘制节点信息
            g.setColor(Color.black);
            int a00=AOEStatic.mainWidth/aoePoints.length/2;
            int b00=AOEStatic.mainHeight/4;
            g.drawString("节点名称      ve      vl",a00,b00);
            for(int ii=0;ii<aoePoints.length;ii++){
                b00+=AOEStatic.mainHeight/aoePoints.length/5;
                g.drawString("V"+aoePoints[ii].getIndex()+"               "+aoePoints[ii].getVe()+"        "+aoePoints[ii].getVl(),a00,b00);

            }
            //如果所有节点的VL均计算完毕，那么绘制关键路径
            int num  = aoePoints.length;
            for(int ii=0;ii<aoePoints.length;ii++){
                if(aoePoints[ii].isVlInvoke()){
                    num--;
                }
            }
            int a0=AOEStatic.mainWidth/aoePoints.length*3;int b0=AOEStatic.mainHeight*2/3;
            if(num==0){
                g.setColor(Color.black);
                while (num<aoePoints.length-1){
                    //绘制关键路径
                    AOEPoint aoePoint  = aoePoints[num];
                    String str;
                    if(num!=0)
                        str = "==> V"+aoePoint.getIndex();
                    else
                        str = "关键路径：V"+aoePoint.getIndex();
                    g.drawString(str,a0,b0);
                    a0+=AOEStatic.mainWidth/aoePoints.length/2.0;
                    //查找下一个点
                    Iterator nextIter = aoePoint.getNextIter();
                    while (nextIter.hasNext()){
                        AOEPoint tmpPoint = (AOEPoint) nextIter.next();
                        if(tmpPoint.getVl()==tmpPoint.getVe()&&tmpPoint.getVl()>0){
                            num = tmpPoint.getIndex()-1;
                            break;
                        }
                    }
                }
                g.drawString(" ==> V"+aoePoints[num].getIndex(),a0,b0);
            }
        }
    }
}
