package project;

public class Test {

	public static void main(String[] args) {
		
		byte op = 7;
		
		Instruction inst = new Instruction(op, 6);
		
		Instruction.checkParity(inst);
		
	}
	
}
