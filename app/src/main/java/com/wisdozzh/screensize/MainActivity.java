package com.wisdozzh.screensize;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * <pre>
 *    author Wisdozzh
 *    blog：http://blog.csdn.net/zzh_receive
 *    github: https://github.com/Wisdozzh
 *    time： 2017-04-07 14:34
 *    disc：
 *          所谓屏幕尺寸指的是屏幕对角线的长度，单位是英寸 然而不同的屏幕尺寸是可以采用相同的分辨率的，而它们之间的区别在与密度（density）不同。
 *          下面先介绍一下密度的概念，DPI、PPI 屏幕密度与DPI这个概念紧密相连，DPI全拼是dots-per-inch,即每英寸的点数。也就是说，
 *          密度越大，每英寸容纳的点数就越多。
 * </pre>
 */
public class MainActivity extends AppCompatActivity {

    /*
     * DensityDpi 几个常用值
	 */
    public static int DENSITY_LOW = 120;
    public static int DENSITY_MEDIUM = 160;// 默认值
    public static int DENSITY_TV = 213;// TV专用
    public static int DENSITY_HIGH = 240;
    public static int DENSITY_XHIGH = 320;
    public static int DENSITY_400 = 400;
    public static int DENSITY_XXHIGH = 480;
    public static int DENSITY_XXXHIGH = 640;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvWidthPixels = (TextView) findViewById(R.id.tv_widthPixels);
        TextView tvHeightPixels = (TextView) findViewById(R.id.tv_heightPixels);
        TextView tvDensityDpi = (TextView) findViewById(R.id.tv_densityDpi);
        TextView tvXdpi = (TextView) findViewById(R.id.tv_xdpi);
        TextView tvYdpi = (TextView) findViewById(R.id.tv_ydpi);
        TextView tvDensity = (TextView) findViewById(R.id.tv_density);
        TextView tvScaledDensity = (TextView) findViewById(R.id.tv_scaledDensity);

        TextView tvMaxMemory = (TextView) findViewById(R.id.tv_max_memory);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        // 宽高像素
        getDisplayInfomation();
        tvWidthPixels.setText("屏幕宽度（像素）：" + metrics.widthPixels);
        tvHeightPixels.setText("屏幕高度（像素）：" + metrics.heightPixels);

        // Dpi
        tvDensityDpi.setText("屏幕密度DPI：" + metrics.densityDpi);
        tvXdpi.setText("屏幕X轴上真正的物理PPI：" + metrics.xdpi);
        tvYdpi.setText("屏幕Y轴上真正的物理PPI：" + metrics.ydpi);

        // dpi/160
        tvDensity.setText("屏幕密度：" + metrics.density);

        // 字体缩放比例
        tvScaledDensity.setText("字体缩放比例：" + metrics.scaledDensity);


        getScreenSizeOfDevice2();

        // 获取单Process最大可用内存
        int onePreocessMaxMemory = getOnePreocessMaxMemory();
        tvMaxMemory.setText("一个Process 只能使用" + onePreocessMaxMemory + "M内存");
    }

    /**
     * 分辨率
     */
    private void getDisplayInfomation() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        TextView tvSize = (TextView) findViewById(R.id.tv_size);
        tvSize.setText("应用屏幕高度：" + point.x + " x " + point.y);

        getWindowManager().getDefaultDisplay().getRealSize(point);
        TextView tvRealSize = (TextView) findViewById(R.id.tv_real_size);
        tvRealSize.setText("真实屏幕高度(分辨率)：" + point.x + " x " + point.y);
    }

    /**
     * 计算屏幕的尺寸 首先求对角线的长，单位为像素 然后用其除以密度（densityDpi）就可以的书对角线的长度
     * densityDpi是每英寸的点数（dots-per-inch）是打印机常用单位（因而也被称为打印分辨率）， 而不是每英寸的像素数
     */
    private void getScreenSizeOfDevice() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // pow 返回底数的指定次幂
        double x = Math.pow(width, 2);
        double y = Math.pow(height, 2);
        // 返回x的平方根
        double diagonal = Math.sqrt(x + y);

        int dens = dm.densityDpi;
        double screenInches = diagonal / (double) dens;
        TextView tvScreenInches = (TextView) findViewById(R.id.tv_screen_inches);
        tvScreenInches.setText("The screenInches " + screenInches);
    }

    /**
     * PPI Pixels per inch,这才是我要的每英寸的像素数（也被称为独享的采样率）。 屏幕X/Y轴上真正的物理PPI。
     * 为了保证获得正确的分辨率，我还是使用getRealSize去获得屏幕宽和高像素。
     */
    private void getScreenSizeOfDevice2() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        String screenInchesRound = roundLeaveOne(screenInches, 1);
        TextView tvRealScreenInches = (TextView) findViewById(R.id.tv_real_screen_inches);
        tvRealScreenInches.setText("屏幕尺寸：" + screenInchesRound + "英寸");
    }

    private String roundLeaveOne(double number, int leave) {
        BigDecimal bg = new BigDecimal(number);
        double result = bg.setScale(leave, BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(result);
    }

    public int getOnePreocessMaxMemory() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemoryByByte = runtime.maxMemory(); // byte
        int maxMemoryByMegabyte = (int) (maxMemoryByByte / (1024 * 1024));
        return maxMemoryByMegabyte;
    }
}
