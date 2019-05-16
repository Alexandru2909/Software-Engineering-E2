package com.frontend.backend.ARGuide.webParserV3;


public class RunningThread extends Thread{
    private AutoUpdateClass autoUpdateClass;
    private SignalType signalType;
    public RunningThread(AutoUpdateClass autoUpdateClass,SignalType signal){
        this.autoUpdateClass=autoUpdateClass;
        this.signalType=signal;
    }
    public void run(){
        while(true){
            System.out.println("rulare");
            if(autoUpdateClass.runDataCollector()==false){//false ->trebuie efectuat update
                signalType.setSignalValue(true);
                break;
            }
            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }

  
}
