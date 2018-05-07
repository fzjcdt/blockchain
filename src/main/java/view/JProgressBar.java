package view;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class JProgressBar {
    public static void generateProgressBar() {
        generateProgressBar(10);
    }

    public static void generateProgressBar(final int times) {
        generateProgressBar(times, "Load...");
    }

    public static void generateProgressBar(final int times, String msg) {
        JFrame frame = new JFrame("Progress");
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 1));


        JLabel label = new JLabel(msg);
        label.setHorizontalAlignment(SwingConstants.HORIZONTAL);
        frame.add(label);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        final javax.swing.JProgressBar progressBar = new javax.swing.JProgressBar();
        progressBar.setOrientation(javax.swing.JProgressBar.HORIZONTAL);
        progressBar.setSize(500, 100);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        panel.add(progressBar, BorderLayout.NORTH);

        frame.add(panel);

        final int interval = times <= 1 ? 100 : 100 / (times - 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= times; i++) {
                    progressBar.setValue(i * interval);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }).start();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            //设置本属性将改变窗口边框样式定义
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            //TODO exception
        }

        generateProgressBar();
    }
}