package com.githang.finddifferent;

import com.android.chimpchat.adb.AdbBackend;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.IChimpImage;
import com.android.chimpchat.core.TouchPressType;

import java.awt.image.BufferedImage;

/**
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 */
public class AdbHelper {
    private final AdbBackend mAdbBackend = new AdbBackend();
    private IChimpDevice mChimpDevice;

    public void waitForConnection() {
        mChimpDevice = mAdbBackend.waitForConnection();
    }

    public void disconnect() {
        mChimpDevice.dispose();
    }

    /**
     * 截图
     */
    public BufferedImage snapshot() {
        IChimpImage img;
        // 当尝试次数太多时不再尝试。
        int tryTimes = 0;
        do {
            System.out.println("截图中..");
            img = mChimpDevice.takeSnapshot();
            tryTimes++;
        } while (img == null && tryTimes < 15);
        if (img == null) {
            throw new RuntimeException("try to much times to take snapshot but failed");
        }
        return img.getBufferedImage();
    }

    public void click(int x, int y) {
        mChimpDevice.touch(x, y, TouchPressType.DOWN_AND_UP);
    }
}
