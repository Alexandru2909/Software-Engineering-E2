package webParserV3;

public class AutoCheckClass  extends Thread{
    private AutoUpdateClass update;
    private FlagClass flag;
    public AutoCheckClass(AutoUpdateClass update,FlagClass flag){
        this.update=update;
        this.flag=flag;
    }
    public void run(){
        while(true){
            if(update.runDataCollector()==false){//update necesar
                flag.setSignal(true);
            }
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
            System.out.println("Verificarea ruleaza");
        }
    }
}
