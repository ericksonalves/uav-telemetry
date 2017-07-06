package com.github.ericksonalves.uavtelemetry.controller;

public class Controller {
    private static Controller sInstance = null;
    private boolean mIsListening;

    public static Controller getInstance() {
        if (sInstance == null) {
            sInstance = new Controller();
        }
        return sInstance;
    }

    private Controller() {
        mIsListening = false;
    }

    public boolean isListening() {
        return mIsListening;
    }

    public void startListening() {
        mIsListening = true;
    }

    public void stopListening() {
    }
}
