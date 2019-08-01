package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FullAssembler implements Assembler {

	private boolean readingCode;
	
	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {

		readingCode = true;
		
		if (error == null) {
			
			throw new IllegalArgumentException("Coding error: The error buffer is null");
			
		}
		
		
		List<String> lines = new ArrayList<>();
		
		try (Scanner in = new Scanner(new File(inputFileName))) {
			
			while (in.hasNextLine()) {
				
				lines.add(in.nextLine());
				
			}
			
		} catch (FileNotFoundException e) {
			
			error.append("Unable to open the source file\n");
			return -1;
			
		}
		
		
		boolean hasPrevBlank = false;
		int currentLine = 0;
		int currentBlankLine = 0;
		int retLine = -1;
		
		for (String line : lines) {
			currentLine++;
			
			//Blank line
			if (line.trim().length() == 0 && !hasPrevBlank) {
				
				hasPrevBlank = true;
				currentBlankLine = currentLine;
				
			}
			else if (hasPrevBlank && line.trim().length() != 0) {
				
				error.append("Error at line " + currentBlankLine + ": Illegal blank line\n");
				retLine = currentBlankLine;
				hasPrevBlank = false;
				
			}
			
			//Blank space
			char[] lineChars = line.toCharArray();
			
			if (line.trim().length() != 0 && (lineChars[0] == ' ' || lineChars[0] == '\t' && !hasPrevBlank)) {
				
				error.append("Error at line " + currentLine + ": Line begins with illegal blank space\n");
				retLine = currentLine;
				continue;
				
			}
			
			//DATA
			if (line.trim().toUpperCase().equals("DATA") && readingCode) {
				
				readingCode = false;
				
				if (!line.trim().equals("DATA")) {
					
					error.append("Error at line " + currentLine + ": DATA not in all uppercase\n");
					retLine = currentLine;
					continue;
					
				}
				
			}
			else if (line.trim().toUpperCase().equals("DATA") && !readingCode) {
				
				error.append("Error at line " + currentLine + ": Second DATA separator\n");
				retLine = currentLine;
				continue;
				
			}
			
			//Key Set
			String[] parts = line.trim().split("\\s+");
			
			if (line.trim().length() != 0 && !Instruction.OPCODES.keySet().contains(parts[0]) && readingCode) {
				
				if (Instruction.OPCODES.keySet().contains(parts[0].toUpperCase())) {
					
					error.append("Error at line " + currentLine + ": Mnemonic not in all uppercase\n");
					retLine = currentLine;
					continue;
					
				}
				else {
					
					error.append("Error at line " + currentLine + ": Invalid mnemonic\n");
					retLine = currentLine;
					continue;
					
				}
				
			}
			
			//No Argument
			else if (line.trim().length() != 0 && readingCode) {
				
				if (Instruction.NO_ARG_MNEMONICS.contains(parts[0]) && parts.length != 1) {
					
					error.append("Error at line " + currentLine + ": Mnemonic does not require an argument\n");
					retLine = currentLine;
					continue;
					
				}
				else if (!Instruction.NO_ARG_MNEMONICS.contains(parts[0]) && parts.length == 1) {
					
					error.append("Error at line " + currentLine + ": Mnemonic requires an argument\n");
					retLine = currentLine;
					continue;
					
				}
				else if (!Instruction.NO_ARG_MNEMONICS.contains(parts[0]) && parts.length > 2) {
					
					error.append("Error at line " + currentLine + ": Mnemonic has too many arguments\n");
					retLine = currentLine;
					continue;
					
				}
				else if (!Instruction.NO_ARG_MNEMONICS.contains(parts[0])) {
					
					//Hex number
					try {
						
						int flags = 0;
						
						if (parts[1].charAt(0) == 'M') {
							
							flags = 2;
							parts[1] = parts[1].substring(1);
							
							//Addressing errors
							if (!Instruction.IMM_MNEMONICS.contains(parts[0])) {
								
								error.append("Error at line " + currentLine + ": Mnemonic does not allow immediate mode\n");
								continue;
								
							}
							
						}
						else if (parts[1].charAt(0) == 'N') {
							
							flags = 4;
							parts[1] = parts[1].substring(1);
							
							if (!Instruction.IND_MNEMONICS.contains(parts[0])) {
								
								error.append("Error at line " + currentLine + ": Mnemonic does not allow indirect mode\n");
								continue;
								
							}
							
						}
						else if (parts[1].charAt(0) == 'J') {
							
							flags = 6;
							parts[1] = parts[1].substring(1);
							
							if (!Instruction.IMM_MNEMONICS.contains(parts[0])) {
								
								error.append("Error at line " + currentLine + ": Mnemonic does not allow special jump mode\n");
								continue;
								
							}
							
						}
						
						int arg = Integer.parseInt(parts[1],16);
						int opPart = 8*Instruction.OPCODES.get(parts[0]) + flags;
						
						opPart += Instruction.numOnes(opPart)%2;
						
					}
					catch (NumberFormatException e) {
						
						error.append("Error at line " + currentLine + ": Argument is not a hex number\n");
						retLine = currentLine;
						continue;
						
					}
					
				}
				
			}
			
			//Data lines
			if (!readingCode && !line.trim().equals("DATA") && !line.trim().equals("")) {
				
				boolean hasMissingValue = false;
				boolean hasIllegalAddress = false;
				boolean hasIllegalValue = false;
				
				if (parts.length != 2) {
					
					retLine = currentLine;
					hasMissingValue = true;
					
				}
				
				try {
					
					int address = Integer.parseInt(parts[0],16);
					
				}
				catch (NumberFormatException e) {
					
					retLine = currentLine;
					hasIllegalAddress = true;
					
				}
				
				try {
					
					int value = Integer.parseInt(parts[1],16);
					
				}
				catch (NumberFormatException e) {
					
					retLine = currentLine;
					hasIllegalValue = true;
					
				}
				catch (ArrayIndexOutOfBoundsException e) {};
				
				if (hasIllegalAddress && hasMissingValue) {
					
					error.append("Error at line " + currentLine + ": Data has non-numeric memory address and is missing value\n");
					continue;
					
				}
				else if (hasIllegalAddress && hasIllegalValue) {
					
					error.append("Error at line " + currentLine + ": Data has non-numeric memory address and non-numeric value\n");
					continue;
					
				}
				else if (hasIllegalValue) {
					
					error.append("Error at line " + currentLine + ": Data has non-numeric value\n");
					continue;
					
				}
				else if (hasIllegalAddress) {
					
					error.append("Error at line " + currentLine + ": Data has non-numeric memory address\n");
					continue;
					
				}
				else if (hasMissingValue) {
					
					error.append("Error at line " + currentLine + ": Data is missing value\n");
					continue;
					
				}
				
				
			}
			
			
		}
		
		//No errors found
		if (retLine == -1) {
				
			new SimpleAssembler().assemble(inputFileName, outputFileName, error);
			return 0;
			
		}
		
		//Errors found
		else {
			
			return retLine;
			
		}
		
	}
	
}
