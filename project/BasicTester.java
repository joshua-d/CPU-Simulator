package project;

import java.util.Arrays;

public class BasicTester {
	public static void main(String[] args) {
		Machine machine = new Machine(() -> System.out.println("halt called"));
		for(int i = 0; i < Memory.DATA_SIZE; i++)
			machine.setData(i, 3*i);		
		System.out.println(Arrays.toString(machine.getData(0,20)));
		Instruction instr = new Instruction((byte)0,0);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("NOP");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00001001,0);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("NOT");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00010001,0);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("HALT");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00011011,5);
		Instruction.checkParity(instr);
		machine.setAccum(10);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JUMP 5 immediate addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00011000,25);
		Instruction.checkParity(instr);
		machine.setAccum(10);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JUMP 25 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00011101,10);
		Instruction.checkParity(instr);
		machine.setAccum(10);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JUMP 10 indirect addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00011110,11);
		Instruction.checkParity(instr);
		machine.setAccum(10);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JUMP 11 special addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00100010,5);
		Instruction.checkParity(instr);
		machine.setAccum(0);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JMPZ 5 immediate addressing acc 0");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00100001,25);
		Instruction.checkParity(instr);
		machine.setAccum(0);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JMPZ 25 direct addressing acc 0");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00100100,10);
		Instruction.checkParity(instr);
		machine.setAccum(0);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JMPZ 10 indirect addressing acc 0");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00100111,11);
		Instruction.checkParity(instr);
		machine.setAccum(0);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JMPZ 11 special addressing acc 0");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00100010,5);
		Instruction.checkParity(instr);
		machine.setAccum(1);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JMPZ 5 immediate addressing acc > 0");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00100001,25);
		Instruction.checkParity(instr);
		machine.setAccum(1);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JMPZ 25 direct addressing acc > 0");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00100100,10);
		Instruction.checkParity(instr);
		machine.setAccum(1);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JMPZ 10 indirect addressing acc > 0");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00100111,11);
		Instruction.checkParity(instr);
		machine.setAccum(1);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("JMPZ 11 special addressing acc > 0");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00101000,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("LOD 12 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00101011,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("LOD 12 immediate addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00101101,3);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("LOD 3 indirect addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00110000,3);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("STO 3 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00110101,2);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("STO 2 indirect addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00111001,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("AND 12 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00111010,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("AND 12 immediate addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00111001,12);
		Instruction.checkParity(instr);
		machine.setAccum(0);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("AND 12 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00111010,12);
		Instruction.checkParity(instr);
		machine.setAccum(0);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("AND 12 immediate addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00111001,0);
		Instruction.checkParity(instr);
		machine.setAccum(10);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("AND 0 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b00111010,0);
		Instruction.checkParity(instr);
		machine.setAccum(10);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("AND 0 immediate addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01000001,10);
		Instruction.checkParity(instr);
		machine.setAccum(3);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("CMPL 10 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01000001,10);
		Instruction.checkParity(instr);
		machine.setData(10, -2);
		machine.setAccum(0);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("CMPL 10 (set to -2) direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01001000,10);
		Instruction.checkParity(instr);
		machine.setAccum(3);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("CMPZ 10 (set to -2) direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01001000,10);
		Instruction.checkParity(instr);
		machine.setData(10, 2);
		machine.setAccum(3);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("CMPZ 10 (set to 2) direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01001000,10);
		Instruction.checkParity(instr);
		machine.setData(10, 0);
		machine.setAccum(3);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("CMPZ 10 (set to 0) direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01010000,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("ADD 12 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01010011,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("ADD 12 immediate addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01010101,4);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("ADD 4 indirect addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01011001,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("SUB 12 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01011010,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("SUB 12 immediate addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01011100,4);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("SUB 4 indirect addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01100000,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("MUL 12 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01100011,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("MUL 12 immediate addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01100101,4);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("MUL 4 indirect addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01101001,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("DIV 12 direct addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01101010,12);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("DIV 12 immediate addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		instr = new Instruction((byte)0b01101100,4);
		Instruction.checkParity(instr);
		machine.ACTION.get(instr.opcode/8).accept(instr);
		System.out.println("DIV 4 indirect addressing");
		System.out.println("Acc: " + machine.getAccum() + ", PC: " + machine.getPC());
		System.out.println(Arrays.toString(machine.getData(0,20)));

		int count = 0;
		try {
			instr = new Instruction((byte)0b00000001, 0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count + "-NOP parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-NOP parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00000011,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-NOP flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-NOP flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00000010,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-NOP parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-NOP parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00000101,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-NOP flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-NOP flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00000100,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-NOP parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-NOP parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00000110,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-NOP flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-NOP flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00000111,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-NOP parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-NOP parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00001000,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-NOT parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-NOT parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00001010,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-NOT flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-NOT flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00001011,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-NOT parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-NOT parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00001100,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-NOT flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-NOT flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00001101,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-NOT parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-NOT parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00001111,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-NOT flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-NOT flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00001110,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-NOT parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-NOT parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00010000,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-HALT parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-HALT parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00010010,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-HALT flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-HALT flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00010011,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-HALT parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-HALT parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00010100,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-HALT flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-HALT flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00010101,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-HALT parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-HALT parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00010111,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-HALT flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-HALT flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00010110,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-HALT parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-HALT parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00011001,12);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-JUMP parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-JUMP parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00011010,12);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-JUMP parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-JUMP parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00011100,12);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-JUMP parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-JUMP parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00011111,12);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-JUMP parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-JUMP parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00100000,12);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-JMPZ parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-JMPZ parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00100011,12);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-JMPZ parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-JMPZ parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00100101,12);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-JMPZ parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-JMPZ parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00100110,12);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-JMPZ parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-JMPZ parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00101001,12);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-LOD parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-LOD parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00101010,12);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-LOD parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-LOD parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00101100,3);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-LOD parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-LOD parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00101110,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-LOD flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-LOD flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00101111,0);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-LOD parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-LOD parity check OK");
		}

		try {
			instr = new Instruction((byte)0b00110001,3);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-STO parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-STO parity check OK");
		}
			
		try {
			instr = new Instruction((byte)0b00110100,3);
			Instruction.checkParity(instr);
			throw new RuntimeException(++count +"-STO parity check FAILED>>>>>>>>>>>>>");
		} catch (ParityCheckException e) {
			System.out.println(++count + "-STO parity check OK");
		}
			
		try {
			instr = new Instruction((byte)0b00110011,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-STO flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-STO flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00110110,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-STO flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-STO flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00111100,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-AND flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-AND flags check OK");
		}

		try {
			instr = new Instruction((byte)0b00111111,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-AND flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-AND flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01000010,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-CMPL flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-CMPL flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01000100,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-CMPL flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-CMPL flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01000111,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-CMPL flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-CMPL flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01001011,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-CMPZ flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-CMPZ flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01001101,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-CMPZ flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-CMPZ flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01001110,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-CMPZ flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-CMPZ flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01010110,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-ADD flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-ADD flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01011111,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-SUB flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-SUB flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01100110,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-MUL flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-MUL flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01101111,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-DIV flag check FAILED>>>>>>>>>>>>>");
		} catch (IllegalInstructionException e) {
			System.out.println(++count + "-DIV flags check OK");
		}

		try {
			instr = new Instruction((byte)0b01101010,0);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-DIV zero div immed check FAILED>>>>>>>>>>>>>");
		} catch (DivideByZeroException e) {
			System.out.println(++count + "-DIV zero div immed check OK");
		}

		try {
			instr = new Instruction((byte)0b01101001,10);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-DIV zero div dir check FAILED>>>>>>>>>>>>>");
		} catch (DivideByZeroException e) {
			System.out.println(++count + "-DIV zero div dir check OK");
		}

		machine.setData(2, 10);
		try {
			instr = new Instruction((byte)0b01101100,2);
			Instruction.checkParity(instr);
			machine.ACTION.get(instr.opcode/8).accept(instr);
			throw new RuntimeException(++count +"-DIV zero div indir check FAILED>>>>>>>>>>>>>");
		} catch (DivideByZeroException e) {
			System.out.println(++count + "-DIV zero div indir check OK");
		}
	}
}