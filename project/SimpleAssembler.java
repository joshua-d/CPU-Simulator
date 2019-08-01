package project;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleAssembler implements Assembler {

	private boolean readingCode = true;
	
	private Instruction makeCode(String[] parts) {
		
		Instruction instr = null;
		
		if (Instruction.NO_ARG_MNEMONICS.contains(parts[0])) {
			
			int opPart = 8*Instruction.OPCODES.get(parts[0]);
			
			opPart += Instruction.numOnes(opPart)%2;
			
			instr = new Instruction((byte)opPart,0);
			
		}
		else {
			
			int flags = 0;
			
			if (parts[1].charAt(0) == 'M') {
				
				flags = 2;
				parts[1] = parts[1].substring(1);
				
			}
			else if (parts[1].charAt(0) == 'N') {
				
				flags = 4;
				parts[1] = parts[1].substring(1);
				
			}
			else if (parts[1].charAt(0) == 'J') {
				
				flags = 6;
				parts[1] = parts[1].substring(1);
				
			}
			
			int arg = Integer.parseInt(parts[1],16);
			int opPart = 8*Instruction.OPCODES.get(parts[0]) + flags;
			
			opPart += Instruction.numOnes(opPart)%2;;
			
			instr = new Instruction((byte)opPart,arg);
			
		}
		
		return instr;
		
	}
	
	private DataPair makeData(String[] parts) {
		
		return new DataPair(Integer.parseInt(parts[0], 16), Integer.parseInt(parts[1], 16));
		
	}
	
	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		Map<Boolean, List<String>> lists = null;
		try (Stream<String> lines = Files.lines(Paths.get(inputFileName))) {
			lists = lines
				.filter(line -> line.trim().length() > 0)
				.map(line -> line.trim())
				.peek(line -> {if(line.toUpperCase().equals("DATA")) readingCode = false;})
				.map(line -> line.trim())
				.collect(Collectors.partitioningBy(line -> readingCode));
//				System.out.println("true List " + lists.get(true)); // these lines can be uncommented 
//				System.out.println("false List " + lists.get(false)); // for checking the code
		} catch (IOException e) {
			e.printStackTrace();
		}
		lists.get(false).remove("DATA"); // some programs will not have DATA but that is not an error
		
		List<Instruction> outputCode = lists.get(true).stream()
				.map(line -> line.split("\\s+"))
				.map(this::makeCode) // note how we use an instance method
				.collect(Collectors.toList());
		
		List<DataPair> outputData = lists.get(false).stream()
				.map(line -> line.split("\\s+"))
				.map(this::makeData) // note how we use an instance method
				.collect(Collectors.toList());
		
		try {
			
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(outputFileName)));
			
			for (Instruction instr : outputCode) {
				
				out.writeInt(instr.opcode);
				out.writeInt(instr.arg);
				
			}
			
			out.writeInt(-1);
			
			for (DataPair pair : outputData) {
				
				out.writeInt(pair.address);
				out.writeInt(pair.value);
				
			}
			
			out.close();
			
		}
		catch (FileNotFoundException e) {
			
			e.printStackTrace();
			
		}
		catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		return 0;
	
	}
	
	public static void main(String[] args) {
	    StringBuilder error = new StringBuilder();
	    System.out.println("Enter the name of the file without extension: ");
	// TYPE IN factorial
	    try (Scanner keyboard = new Scanner(System.in)) { 
	        String filename = keyboard.nextLine();
	        int i = new SimpleAssembler().assemble(filename + ".pasm", 
	                filename + ".pexe", error);
	        System.out.println("result = " + i);
	  }
	    
	}
	
}
