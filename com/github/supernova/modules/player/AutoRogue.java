package com.github.supernova.modules.player;

import com.github.supernova.modules.Module;
import com.github.supernova.value.impl.NumberValue;

public class AutoRogue extends Module {

    Timer delayTimer = new Timer();
    NumberValue delayValue = new NumberValue("Delay (ms)", 100, 10, 10000, 10);
    NumberValue amountValue = new NumberValue("Amount", 48, 1, 200, 1);

    private boolean runningThread = false;
    private void runThread() {
        if(runningThread) return;
        runningThread = true;
        new Thread(){
            @Override
            public void run() {
                while(!this.isInterrupted() || isEnabled()) {

                }
            }
        }.start();
    }
}
