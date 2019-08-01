package project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Memory {

	public static final int DATA_SIZE = 512;
	private int[] data = new int[DATA_SIZE];
	
	public static final int CODE_SIZE = 256;
	private List<Instruction> code = new ArrayList<>();
	private int changedDataIndex = -1;
	
	int[] getData(int min, int max) {
		
		return Arrays.copyOfRange(data, min, max);
		
	}
	
	int[] getData() {
		
		return data;
		
	}
	
	int getData(int index) {
		
		return data[index];
		
	}
	
	int getChangedDataIndex() {
		
		return changedDataIndex;
		
	}
	
	List<Instruction> getCode() {
		
		return code;
		
	}
	
	void setData(int index, int value) {
		
		if (index >= 0 && index < DATA_SIZE) {
			
			data[index] = value;
			changedDataIndex = index;
			
		}
		
		//Else throw exception?
		
	}
	
	void clearData() {
		
		for (int i = 0; i < data.length; i++) {
			
			data[i] = 0;
			
		}
		
		changedDataIndex = -1;
		
	}
	
	Instruction getCode(int index) {
		
		if (index >= 0 && index < code.size()) {
			
			return code.get(index);
			
		}
		else {
			
			throw new CodeAccessException("Illegal access to code");
			
		}
		
	}
	
	public Instruction[] getCode(int min, int max) {
		
		if (min >= 0 && max >= min && max < code.size()) {
		
			Instruction[] temp = {};
			temp = code.toArray(temp);
			return Arrays.copyOfRange(temp, min, max); 
			
		}
		else {
			
			throw new CodeAccessException("Illegal access to code");
			
		}
		
	}
	
	void addCode(Instruction value) {
		
		if (code.size() < CODE_SIZE) {
			
			code.add(value);
			
		}
		
	}
	
	void setCode(int index, Instruction instr) {
		
		if (index >= 0 && index < code.size()) {
			
			code.set(index, instr);
			
		}
		else {
			
			throw new CodeAccessException("Illegal access to code");
			
		}
		
	}
	
	void clearCode() {
		
		code.clear();
		
	}
	
	int getProgramSize() {
		
		return code.size();
		
	}
	
}
