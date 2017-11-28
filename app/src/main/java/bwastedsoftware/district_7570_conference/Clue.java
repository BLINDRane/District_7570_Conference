package bwastedsoftware.district_7570_conference;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

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
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", Title);
        result.put("instruction", Instruction);

        return result;
    }
}
