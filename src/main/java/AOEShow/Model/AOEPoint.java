package AOEShow.Model;

import java.awt.*;
import java.util.*;

/**
 * Created by UKi_Hi on 2018/4/29.
 */
public class AOEPoint {
    //数据结构
    //是否被激活
    private boolean isInvoke = false;
    //计算vl是否被激活
    private boolean isVlInvoke = false;
//    private boolean hasInit = false;
    private int x;
    private int y;
    private int index;
    //最早开始时间
    private int Ve = -1;
    //最晚完成时间
    private int Vl = Integer.MAX_VALUE;

    //得区分哪条线上的currentTime
//    private int currentTime = 0;
    //key 为Index，value为currentTime
    private Map<Integer,Integer> currentTimeMap = new HashMap<Integer,Integer>();
    //因为currentTime用map了，就不需要这个参数了
//    private boolean hasAddTime = false;
    private Map<Integer,Integer> vlTimeMap = new HashMap<Integer,Integer>();

    private Map prv = new HashMap<AOEPoint,Integer>();
    private Map next = new HashMap<AOEPoint,Integer>();

    public AOEPoint(){
        this(0,0);
    }
    public AOEPoint(Point point){
        this(point.x,point.y);
    }

    public AOEPoint(int x,int y ){
        this.x = x;
        this.y = y;
//        this.hasInit = true;
    }
    public boolean isInvoke() {
        return isInvoke;
    }

    public void setInvoke(boolean invoke) {
        isInvoke = invoke;
    }

    public void removeNext(AOEPoint aoePoint){
        this.next.remove(aoePoint);
    }
    public void removePrv(AOEPoint aoePoint){
        this.prv.remove(aoePoint);
    }
    public void addNext(AOEPoint aoePoint,int leastDay){
        this.next.put(aoePoint,leastDay);

    }
    public void addPrv(AOEPoint aoePoint,int leastDay){
        this.prv.put(aoePoint,leastDay);

    }
    public int getNextNum(){
        return this.next.size()+1;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

//    public boolean isHasInit() {
//        return hasInit;
//    }
    public Iterator getNextIter(){
        return this.next.keySet().iterator();
    }
    public Iterator getPrvIter(){
        return this.prv.keySet().iterator();
    }
    public Map<Integer,Integer> getCurrentTimeMap(){
        return this.currentTimeMap;
    }
    public Map<Integer,Integer> getVlTimeMap(){
        return this.vlTimeMap;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }



    public int getNextDayByIndex(int  index){
        //避免引用问题，根据point的Index唯一标识
        for(Object aoePoint:this.next.keySet()){
            if(((AOEPoint)aoePoint).getIndex()==index){
                return (Integer) this.next.get(aoePoint);
            }
        }
        return -1;
    }

    public int getPrvToCurrentTime(int index) {
        if(currentTimeMap.containsKey(index))
            return currentTimeMap.get(index);
        return 0;
    }

    public void setPreToCurrentTime(int index,int currentTime) {
        this.currentTimeMap.put(index,currentTime);
    }
    public int getToPrvVlTime(int index) {
        if(vlTimeMap.containsKey(index))
            return vlTimeMap.get(index);
        return 0;
    }

    public void setToPrvVlTime(int index,int currentTime) {
        this.vlTimeMap.put(index,currentTime);
    }
    public int getVe() {
        return Ve;
    }

    public void setVe(int ve) {
        Ve = ve;
    }

    public int getVl() {
        return Vl;
    }

    public void setVl(int vl) {
        Vl = vl;
    }

    public boolean isVlInvoke() {
        return isVlInvoke;
    }

    public void setVlInvoke(boolean vlInvoke) {
        isVlInvoke = vlInvoke;
    }

//    public boolean isHasAddTime() {
//        return hasAddTime;
//    }
//
//    public void setHasAddTime(boolean hasAddTime) {
//        this.hasAddTime = hasAddTime;
//    }
}
