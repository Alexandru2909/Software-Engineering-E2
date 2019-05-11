package webParserV3;

public class SignalType {
    private Boolean value;
    public SignalType(){
        this.value=false;
    }
    public void setSignalValue(boolean value){
        this.value=value;
    }
    public boolean UpdateRequired(){
        return this.value;
    }
}
