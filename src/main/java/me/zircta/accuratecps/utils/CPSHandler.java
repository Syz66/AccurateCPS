package me.zircta.accuratecps.utils;

public class CPSHandler {
    public static CPSHandler INSTANCE;
    public int leftCps = 0;
    public int rightCps = 0;

    public int getLeftCps() {
        return leftCps;
    }

    public int getRightCps() {
        return rightCps;
    }

    static {
        INSTANCE = new CPSHandler();
    }
}
