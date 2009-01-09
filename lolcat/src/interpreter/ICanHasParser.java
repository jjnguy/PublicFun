package interpreter;
import java.util.*;

public class ICanHasParser{

  private String program = null;

  public ICanHasParser(String in){
    program = in;
  }
  
  public String[] parse(){
    program = program.replaceAll("CAN HAS STDIO[?]\n", "CAN_HAS_SDIO\n"); //this will need to be changed to CAN_HAS later

    program = program.replaceAll("I HAS A *","I_HAS_A "); // variable declaration

    program = program.replaceAll("IM IN YR LOOP\n","IM_IN_YR_LOOP\n"); //loop start

    program = program.replaceAll("IM OUTTA YR LOOP\n","IM_OUTTA_YR_LOOP\n"); //loop end
    
    program = program.replaceAll("BTW[^\n]*\n", ""); //comments 
    return program.split("[ \t\n\f\r]"); //split by all whitespace characters
  
  
  }
  
  /* Just testing the parser: */
  public static void main(String[] args){
    String p = "HAI\n" +
"CAN HAS STDIO?\n" +
"I HAS A VAR\n" + 
"IM IN YR LOOP\n" +
	"VISIBLE VAR\n" +
	"KTHXBYE\n" +
"BTW IM OUTTA YR LOOP\n" +
"VISIBLE \"sandwich\"\n" +
"KTHXBYE";
    ICanHasParser face = new ICanHasParser(p);
    face.parse();
    System.out.println();
  }
}