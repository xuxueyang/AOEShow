package AOEShow;

import AOEShow.JFrame.AOETimerShow;

import javax.swing.*;


/**
 * Created by UKi_Hi on 2018/4/29.
 */
public class AOEStart {
    public static  void main(String[] args){
        JFrame jFrame = new JFrame();
        jFrame.setSize(AOEStatic.mainWidth,AOEStatic.mainHeight);
        final AOETimerShow aoeTimerShow = new AOETimerShow();
        jFrame.setContentPane(aoeTimerShow);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
