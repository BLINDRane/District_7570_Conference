package bwastedsoftware.district_7570_conference;

/**
 * Created by wyatt_ofn2t9u on 11/14/2017.
 */

public class Clue {

private String Title;
private String Instruction;

    Clue(){

    }

    Clue(String Title, String Instruction){
        this.Title = Title;
        this.Instruction = Instruction;
    }

    public String getTitle(){
        return Title;
    }

    public void setTitle(String Title){
        this.Title = Title;
    }

    public String getInstruction(){
        return Instruction;
    }

    public void setInstruction(String Instruction){
        this.Instruction = Instruction;
    }
}
