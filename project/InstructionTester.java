package project;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InstructionTester {

	Machine machine = new Machine(() ->  {});
	int[] dataCopy = new int[Memory.DATA_SIZE];
	int accInit;
	int pcInit;

	@BeforeEach
	public void setup() {
		for (int i = 0; i < Memory.DATA_SIZE; i++) {
			dataCopy[i] = -5*Memory.DATA_SIZE + 10*i;
			machine.setData(i, -5*Memory.DATA_SIZE + 10*i);
			// Initially the machine will contain a known spread
			// of different numbers: 
			// -2560, -2550, -2540, ..., 0, 10, 20, ..., 2550 
			// This allows us to check that the instructions do 
			// not corrupt machine unexpectedly.
			// 0 is at index 256
		}
		// prefill with 10 NOP instructions
		for(int i = 0; i < 10; i++)
			machine.addCode(new Instruction((byte)0, 0));
		accInit = 7;
		pcInit = 4;
	}


	@Test 
	// NOP only increments the program counter
	public void testNOP(){
		Instruction instr = new Instruction((byte)0b00000000,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		//Test machine is not changed
		assertArrayEquals(dataCopy, machine.getData());
		//Test program counter incremented
		assertAll (
				() -> assertEquals(pcInit+1, machine.getPC(), "Program counter incremented"),
				//Test accumulator untouched
				() -> assertEquals(accInit, machine.getAccum(), "Accumulator unchanged")
				);
	}

	// Verify parity checking is working
	@Test
	public void testNOPbadParity() {
		Instruction instr = new Instruction((byte)0b00000001,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());	
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testNOPimmedBadParity(){
		Instruction instr = new Instruction((byte)0b00000010,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());		
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Check NOP cannot have an immediate flag
	public void testNOPimmedIllegal(){
		Instruction instr = new Instruction((byte)0b00000011,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (01)", exception.getMessage());
	}

	@Test
	// Verify parity checking is working
	public void testNOPindirBadParity(){
		Instruction instr = new Instruction((byte)0b00000100,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Check NOP cannot have an indirect flag
	public void testNOPindirIllegal(){
		Instruction instr = new Instruction((byte)0b00000101,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (10)", exception.getMessage());
	}

	@Test 
	// Check NOP cannot have the special flags for Jumps
	public void testNOPflags6Illegal(){
		Instruction instr = new Instruction((byte)0b00000110,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testNOPbadFlagsBadParity(){
		Instruction instr = new Instruction((byte)0b00000111,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Check NOT with accumulator greater than 0 gives false
	public void testNOTaccumGT0() {
		Instruction instr = new Instruction((byte)0b00001001,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1,
						machine.getPC()),
				//Accumulator is 1
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check NOT with accumulator equal to 0 gives true
	public void testNOTaccumEQ0() {
		Instruction instr = new Instruction((byte)0b00001001,0);
		accInit = 0;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Accumulator is 1
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test
	// Check NOT with accumulator less than 0 gives false
	public void testNOTaccumLT0() {
		Instruction instr = new Instruction((byte)0b00001001,0);
		accInit = -5;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Accumulator is 1
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testNOTdirectBadParity(){
		Instruction instr = new Instruction((byte)0b00001000,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Check NOT cannot have an immediate flag
	public void testNOTimmedIllegal(){
		Instruction instr = new Instruction((byte)0b00001010,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (01)", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testNOTimmedBadParity(){
		Instruction instr = new Instruction((byte)0b00001011,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Check NOT cannot have an indirect flag
	public void testNOTindirIllegal(){
		Instruction instr = new Instruction((byte)0b00001100,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (10)", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testNOTindirBadParity(){
		Instruction instr = new Instruction((byte)0b00001101,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Check NOT cannot have an special flags for Jump instructions
	public void testNOTbadFlags(){
		Instruction instr = new Instruction((byte)0b00001111,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testNOTBadFlagsAndParity(){
		Instruction instr = new Instruction((byte)0b00001110,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Check HALT accumulator greater than 0 gives false
	public void testHALT() {
		Instruction instr = new Instruction((byte)0b00010001,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll(
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit, machine.getPC()),
				//Accumulator is 1
				() -> assertEquals(accInit, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testHALTdirectBadParity(){
		Instruction instr = new Instruction((byte)0b00010000,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Check HALT cannot have an immediate flag
	public void testHALTimmedIllegal(){
		Instruction instr = new Instruction((byte)0b00010010,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (01)", exception.getMessage());
	}

	@Test
	// Verify parity checking is working
	public void testHALTimmedBadParity(){
		Instruction instr = new Instruction((byte)0b00010011,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Check HALT cannot have an indirect flag
	public void testHALTindirIllegal(){
		Instruction instr = new Instruction((byte)0b00010100,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (10)", exception.getMessage());
	}

	@Test
	// Verify parity checking is working
	public void testHALTindirBadParity(){
		Instruction instr = new Instruction((byte)0b00010101,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Check HALT cannot have special flags for Jump instructions
	public void testHALTbadFlags(){
		Instruction instr = new Instruction((byte)0b00010111,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testHALTBadFlagsAndParity(){
		Instruction instr = new Instruction((byte)0b00010110,0);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the relative jump is done correctly, when
	// address is the argument
	public void testJUMPdirect() {
		Instruction instr = new Instruction((byte)0b00011000,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should have added 12 to the program counter
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(pcInit + 12, machine.getPC()),
				() -> assertEquals(accInit, machine.getAccum())
				);
	}
	@Test 
	// Verify parity checking is working
	public void testJUMPdirectBadParity() {
		Instruction instr = new Instruction((byte)0b00011001,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the immediate jump is done correctly, when
	// address is the argument
	public void testJUMPimmed() {
		Instruction instr = new Instruction((byte)0b00011011,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should have added 12 to the program counter
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(12, machine.getPC()),
				() -> assertEquals(accInit, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testJUMPimmedBadParity() {
		Instruction instr = new Instruction((byte)0b00011010,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the indirect jump is done correctly, when
	// address is the argument
	public void testJUMPindir() {
		Instruction instr = new Instruction((byte)0b00011101,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should have added data[260] = -2560+2600 = 40 to program counter
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(pcInit + 40, machine.getPC()),
				() -> assertEquals(accInit, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testJUMPindirBadParity() {
		Instruction instr = new Instruction((byte)0b00011100,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the special jump is done correctly, when
	// address is the argument
	public void testJUMPspecial() {
		Instruction instr = new Instruction((byte)0b00011110,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should have set the program counter to data[260] = 40
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(40, machine.getPC()),
				() -> assertEquals(accInit, machine.getAccum())
				);
	}

	// Verify parity checking is working
	public void testJUMPspecialBadParity() {
		Instruction instr = new Instruction((byte)0b00011111,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the direct JMPZ is done correctly, when
	// accumulator = 0
	public void testJMPZaccEQ0direct() {
		Instruction instr = new Instruction((byte)0b00100001,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(0);
		machine.step();
		assertAll (
				//Should have added 12 to the program counter
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(pcInit + 12, machine.getPC()),
				//accumulator unchanged
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testJMPZaccEQ0directBadParity() {
		Instruction instr = new Instruction((byte)0b00100000,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(0);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the immediate JMPZ is done correctly, when
	// accumulator = 0
	public void testJMPZaccEQ0immed() {
		Instruction instr = new Instruction((byte)0b00100010,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(0);
		machine.step();
		assertAll (
				//Should have set the program counter to 12
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(12, machine.getPC()),
				//accumulator unchanged
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Verify parity checking is working
	public void testJMPZaccEQ0immedBadParity() {
		Instruction instr = new Instruction((byte)0b00100011,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(0);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the indirect JMPZ is done correctly, when
	// accumulator = 0
	public void testJMPZaccEQ0indir() {
		Instruction instr = new Instruction((byte)0b00100100,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(0);
		machine.step();
		assertAll (
				//Should have added data[260] = -2560+2600 = 40 to program counter
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(pcInit + 40, machine.getPC()),
				//accumulator unchanged
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Verify parity checking is working
	public void testJMPZaccEQ0indirBadParity() {
		Instruction instr = new Instruction((byte)0b00100101,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(0);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the special JMPZ is done correctly, when
	// accumulator = 0
	public void testJMPZaccEQ0special() {
		Instruction instr = new Instruction((byte)0b00100111,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(0);
		machine.step();
		assertAll (
				//Should have set the program counter to data[260] = 40
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(40, machine.getPC()),
				//accumulator unchanged
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Verify parity checking is working
	public void testJMPZaccEQ0specialBadParity() {
		Instruction instr = new Instruction((byte)0b00100110,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the direct JMPZ is done correctly, when
	// accumulator not 0
	public void testJMPZdirect() {
		Instruction instr = new Instruction((byte)0b00100001,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator not zero, should increment program counter
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//accumulator unchanged
				() -> assertEquals(accInit, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testJMPZdirectBadParity() {
		Instruction instr = new Instruction((byte)0b00100000,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the immediate JMPZ is done correctly, when
	// accumulator not 0
	public void testJMPZimmed() {
		Instruction instr = new Instruction((byte)0b00100010,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator not zero, should increment program counter
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//accumulator unchanged
				() -> assertEquals(accInit, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testJMPZimmedBadParity() {
		Instruction instr = new Instruction((byte)0b00100011,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the indirect JMPZ is done correctly, when
	// accumulator not 0
	public void testJMPZindir() {
		Instruction instr = new Instruction((byte)0b00100100,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator not zero, should increment program counter
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//accumulator unchanged
				() -> assertEquals(accInit, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testJMPZindirBadParity() {
		Instruction instr = new Instruction((byte)0b00100101,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// this test checks whether the special JMPZ is done correctly, when
	// accumulator not 0
	public void testJMPZspecial() {
		Instruction instr = new Instruction((byte)0b00100111,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator not zero, should increment program counter
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//accumulator unchanged
				() -> assertEquals(accInit, machine.getAccum())
				);
	}

	@Test
	// Verify parity checking is working
	public void testJMPZspecialBadParity() {
		Instruction instr = new Instruction((byte)0b00100110,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(0);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether LOD is correct with direct addressing
	public void testLODdirect(){
		Instruction instr = new Instruction((byte)0b00101000,12);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should load data[12] = -2560+120 into the accumulator
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(-2560+120, machine.getAccum())
				);
	}

	@Test
	// Verify parity checking is working
	public void testLODdirectBadParity(){
		Instruction instr = new Instruction((byte)0b00101001,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether LOD is correct with immediate addressing
	public void testLODimmed(){
		Instruction instr = new Instruction((byte)0b00101011,12);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should load 12 into the accumulator
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(12, machine.getAccum())
				);
	}

	@Test
	// Verify parity checking is working
	public void testLODimmedBadParity(){
		Instruction instr = new Instruction((byte)0b00101010,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether LOD is correct with indirect addressing
	public void testLODindirect() {
		Instruction instr = new Instruction((byte)0b00101101,260);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should load data[data[260]] = data[-2560+2600]] = data[40] = -2560 + 400
				//into the accumulator
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(-2560+400, machine.getAccum())
				);
	}	

	@Test 
	// Verify parity checking is working
	public void testLODindirectBadParity() {
		Instruction instr = new Instruction((byte)0b00101100,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test
	// Check LOD cannot have special flags for Jump instructions
	public void testLODBadflags() {
		Instruction instr = new Instruction((byte)0b00101110,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}	

	@Test
	// Verify parity checking is working
	public void testLODBadflagsAndParity() {
		Instruction instr = new Instruction((byte)0b00101111,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test
	// Test whether STO is correct with direct addressing
	public void testSTOdirect() {
		Instruction instr = new Instruction((byte)0b00110000,12);
		accInit = 567;
		dataCopy[12] = 567;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Test machine is changed correctly
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(567, machine.getAccum())
				);
	}

	@Test
	// Verify parity checking is working
	public void testSTOdirectBadParity() {
		Instruction instr = new Instruction((byte)0b00110001,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Check STO cannot have immediate addressing
	public void testSTOimmedIllegal() {
		Instruction instr = new Instruction((byte)0b00110011,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (01)", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testSTOimmedBadParity() {
		Instruction instr = new Instruction((byte)0b00110010,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether STO is correct with indirect addressing
	public void testSTOindir() {
		Instruction instr = new Instruction((byte)0b00110101,260);
		accInit = 567;
		//Should store accum at data[data[260]] = data[=2560+2600] = data[40]
		dataCopy[40] = 567;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Test machine is changed correctly
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(567, machine.getAccum())
				);
	}

	@Test
	// Verify parity checking is working
	public void testSTOindirBadParity() {
		Instruction instr = new Instruction((byte)0b00110100,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testSTOflags6BadParity() {
		Instruction instr = new Instruction((byte)0b00110111,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Check STO cannot have special flags for Jump instructions
	public void testSTOflags6Illegal() {
		Instruction instr = new Instruction((byte)0b00110110,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}

	@Test
	// Check AND direct when accum and arg equal to 0 gives false
	public void testANDdirectAccEQ0argEQ0() {
		Instruction instr = new Instruction((byte)0b00111001,256);
		accInit = 0;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator and data[256] = -2560+2560 both 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check AND direct when accum > 0 and arg equal to 0 gives false
	public void testANDdirectAccGT0argEQ0() {
		Instruction instr = new Instruction((byte)0b00111001,256);
		accInit = 10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator > 0 and data[256] = -2560+2560 = 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check AND direct when accum < 0 and arg equal to 0 gives false
	public void testANDdirectAccLT0argEQ0() {
		Instruction instr = new Instruction((byte)0b00111001,256);
		accInit = -10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator > 0 and data[256] = -2560+2560 = 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check AND direct when accum = 0 and arg > 0 gives false
	public void testANDdirectAccEQ0argGT0() {
		Instruction instr = new Instruction((byte)0b00111001,257);
		accInit = 0;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = 0 and data[257] = -2560+2570 = 10
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check AND direct when accum > 0 and arg > 0 gives true
	public void testANDdirectAccGT0argGT0() {
		Instruction instr = new Instruction((byte)0b00111001,257);
		accInit = 10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = 10 and data[257] = -2560+2570 = 10
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test
	// Check AND direct when accum < 0 and arg > 0 gives true
	public void testANDdirectAccLT0argGT0() {
		Instruction instr = new Instruction((byte)0b00111001,257);
		accInit = -10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = -10 and data[257] = -2560+2570 = 10
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test
	// Check AND direct when accum = 0 and arg < 0 gives false
	public void testANDdirectAccEQ0argLT0() {
		Instruction instr = new Instruction((byte)0b00111001,255);
		accInit = 0;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = 0 and data[255] = -2560+2550 = -10
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check AND direct when accum > 0 and arg < 0 gives true
	public void testANDdirectAccGT0argLT0() {
		Instruction instr = new Instruction((byte)0b00111001,255);
		accInit = 10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = 10 and data[255] = -2560+2550 = -10
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test
	// Check AND direct when accum < 0 and arg < 0 gives true
	public void testANDdirectAccLT0argLT0() {
		Instruction instr = new Instruction((byte)0b00111001,255);
		accInit = -10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = -10 and data[255] = -2560+2550 = -10
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testANDdirectBadParity() {
		Instruction instr = new Instruction((byte)0b00111000,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test
	// Check AND immediate when accum and arg equal to 0 gives false
	public void testANDimmedAccEQ0argEQ0() {
		Instruction instr = new Instruction((byte)0b00111010,0);
		accInit = 0;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = 0 and arg = 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check AND immediate when accum > 0 and arg equal to 0 gives false
	public void testANDimmedAccGT0argEQ0() {
		Instruction instr = new Instruction((byte)0b00111010,0);
		accInit = 10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = 10 and arg = 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check AND immediate when accum < 0 and arg equal to 0 gives false
	public void testANDimmedAccLT0argEQ0() {
		Instruction instr = new Instruction((byte)0b00111010,0);
		accInit = -10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = -10 and arg = 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check AND immediate when accum = 0 and arg > 0 gives false
	public void testANDimmedAccEQ0argGT0() {
		Instruction instr = new Instruction((byte)0b00111010,12);
		accInit = 0;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = 0 and arg = 12
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check AND immediate when accum > 0 and arg > 0 gives true
	public void testANDimmedAccGT0argGT0() {
		Instruction instr = new Instruction((byte)0b00111010,12);
		accInit = 10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = 10 and arg = 12
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test
	// Check AND immediate when accum < 0 and arg > 0 gives true
	public void testANDimmedAccLT0argGT0() {
		Instruction instr = new Instruction((byte)0b00111010,12);
		accInit = -10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = -10 and arg = 12
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test
	// Check AND immediate when accum = 0 and arg < 0 gives false
	public void testANDimmedAccEQ0argLT0() {
		Instruction instr = new Instruction((byte)0b00111010,-12);
		accInit = 0;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = 0 and arg = -12
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check AND immediate when accum > 0 and arg < 0 gives true
	public void testANDimmedAccGT0argLT0() {
		Instruction instr = new Instruction((byte)0b00111010,-12);
		accInit = 10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = 10 and arg = -12
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test
	// Check AND immediate when accum < 0 and arg < 0 gives true
	public void testANDimmedAccLT0argLT0() {
		Instruction instr = new Instruction((byte)0b00111010,-12);
		accInit = -10;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//accumulator = -10 and arg = -12
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test
	// Verify parity checking is working
	public void testANDimmedBadParity() {
		Instruction instr = new Instruction((byte)0b00111011,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test 
	// Check AND cannot have indirect addressing
	public void testANDindirIllegal() {
		Instruction instr = new Instruction((byte)0b00111100,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (10)", exception.getMessage());
	}

	@Test
	// Verify parity checking is working
	public void testANDindirBadParity() {
		Instruction instr = new Instruction((byte)0b00111101,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test 
	// Check AND cannot have special flags for Jump instructions
	public void testANDbadFlags() {
		Instruction instr = new Instruction((byte)0b00111111,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}

	@Test
	// Verify parity checking is working
	public void testANDbadFlagsAndParity() {
		Instruction instr = new Instruction((byte)0b00111110,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test
	// Check CMPL when comparing less than 0 gives true
	public void testCMPLmemLT0() {
		Instruction instr = new Instruction((byte)0b01000001,200);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				// data[200] = -2560+2000 < 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test
	// Check CMPL when comparing 0 gives false
	public void testCMPLmemEQ0() {
		Instruction instr = new Instruction((byte)0b01000001,256);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				// data[200] = -2560+2000 = 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check CMPL when comparing greater than 0 gives false
	public void testCMPLmemGT0() {
		Instruction instr = new Instruction((byte)0b01000001,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				// data[200] = -2560+2600 > 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test 
	// Check that CMPL cannot have immediate flag
	public void testCMPLbadImmedFlags() {
		Instruction instr = new Instruction((byte)0b01000010,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (01)", exception.getMessage());
	}

	@Test 
	// Check that CMPL cannot have indirect flag
	public void testCMPLbadIndirFlags() {
		Instruction instr = new Instruction((byte)0b01000100,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (10)", exception.getMessage());
	}

	@Test 
	// Check that CMPL cannot have special flag for Jump instruction
	public void testCMPLbadFlags() {
		Instruction instr = new Instruction((byte)0b01000111,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testCMPLbadParity() {
		Instruction instr = new Instruction((byte)0b01000000,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Verify parity checking is working
	public void testCMPLimmedBadParity() {
		Instruction instr = new Instruction((byte)0b01000011,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Verify parity checking is working
	public void testCMPLindirBadParity() {
		Instruction instr = new Instruction((byte)0b01000101,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Verify parity checking is working
	public void testCMPLbadFlagsAndParity() {
		Instruction instr = new Instruction((byte)0b01000110,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Check CMPZ when comparing less than 0 gives false
	public void testCMPZmemLT0() {
		Instruction instr = new Instruction((byte)0b01001000,200);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				// data[200] = -2560+2000 < 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check CMPZ when comparing 0 gives true
	public void testCMPZmemEQ0() {
		Instruction instr = new Instruction((byte)0b01001000,256);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				// data[200] = -2560+2560 = 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(1, machine.getAccum())
				);
	}

	@Test
	// Check CMPZ when comparing greater than 0 gives false
	public void testCMPZmemGT0() {
		Instruction instr = new Instruction((byte)0b01001000,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				// data[260] = -2560+2600 > 0
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(0, machine.getAccum())
				);
	}

	@Test
	// Check that CMPZ cannot have immediate flag
	public void testCMPZbadImmedFlags() {
		Instruction instr = new Instruction((byte)0b01001011,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (01)", exception.getMessage());
	}

	@Test
	// Check that CMPZ cannot have indirect flag
	public void testCMPZbadIndirFlags() {
		Instruction instr = new Instruction((byte)0b01001101,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (10)", exception.getMessage());
	}

	@Test 
	// Check that CMPZ cannot have special flag for Jump instruction
	public void testCMPZbadFlags() {
		Instruction instr = new Instruction((byte)0b01001110,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testCMPZbadParity() {
		Instruction instr = new Instruction((byte)0b01001001,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Verify parity checking is working
	public void testCMPZimmedBadParity() {
		Instruction instr = new Instruction((byte)0b01001010,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testCMPZindirBadParity() {
		Instruction instr = new Instruction((byte)0b01001100,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Verify parity checking is working
	public void testCMPZbadFlagsAndParity() {
		Instruction instr = new Instruction((byte)0b01001111,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether ADD is correct with direct addressing
	public void testADDdirect(){
		Instruction instr = new Instruction((byte)0b01010000,12);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should add data[12] = -2560+120 to the accumulator
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit-2560+120, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testADDdirectBadParity(){
		Instruction instr = new Instruction((byte)0b01010001,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether ADD is correct with immediate addressing
	public void testADDimmed(){
		Instruction instr = new Instruction((byte)0b01010011,12);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should add 12 to the accumulator
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit+12, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testADDimmedBadParity(){
		Instruction instr = new Instruction((byte)0b01010010,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether ADD is correct with indirect addressing
	public void testADDindirect() {
		Instruction instr = new Instruction((byte)0b01010101,260);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Note data[260] = -2560+2600 = 40
				//Should add data[data[260]] = data[40] = -2560 + 400
				//to the accumulator
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit-2560+400, machine.getAccum())
				);
	}	

	@Test 
	// Verify parity checking is working
	public void testADDindirectBadParity() {
		Instruction instr = new Instruction((byte)0b01010100,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test 
	// Check ADD cannot have special flags for Jump instructions
	public void testADDBadflags() {
		Instruction instr = new Instruction((byte)0b01010110,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}	

	@Test 
	// Verify parity checking is working
	public void testADDBadflagsAndParity() {
		Instruction instr = new Instruction((byte)0b01010111,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test
	// Test whether SUB is correct with direct addressing
	public void testSUBdirect(){
		Instruction instr = new Instruction((byte)0b01011001,12);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should subtract data[12] = -2560+120 from the accumulator
				//to the accumulator
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit-(-2560+120), machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testSUBdirectBadParity(){
		Instruction instr = new Instruction((byte)0b01011000,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether SUB is correct with immediate addressing
	public void testSUBimmed(){
		Instruction instr = new Instruction((byte)0b01011010,12);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should subtract 12 from the accumulator
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit-12, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testSUBimmedBadParity(){
		Instruction instr = new Instruction((byte)0b01011011,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether SUB is correct with indirect addressing
	public void testSUBindirect() {
		Instruction instr = new Instruction((byte)0b01011100,260);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Note data[260] = -2560+2600 = 40
				//Should subtract data[data[260]] = data[40] = -2560 + 400
				//from the accumulator
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit-(-2560+400), machine.getAccum())
				);
	}	

	@Test 
	// Verify parity checking is working
	public void testSUBindirectBadParity() {
		Instruction instr = new Instruction((byte)0b01011101,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test 
	// Check SUB cannot have special flags for Jump instructions
	public void testSUBBadflags() {
		Instruction instr = new Instruction((byte)0b01011111,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}	

	@Test 
	// Verify parity checking is working
	public void testSUBBadflagsAndParity() {
		Instruction instr = new Instruction((byte)0b01011110,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test
	// Test whether load is correct with direct addressing
	public void testMULdirect(){
		Instruction instr = new Instruction((byte)0b01100000,12);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Note data[12] = -2560+120
				//Should multiply accumulator by -2560+120 
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit*(-2560+120), machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testMULdirectBadParity(){
		Instruction instr = new Instruction((byte)0b01100001,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether load is correct with immediate addressing
	public void testMULimmed(){
		Instruction instr = new Instruction((byte)0b01100011,12);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should multiply accumulator by 12 
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit*12, machine.getAccum())
				);
	}

	@Test 
	// Verify parity checking is working
	public void testMULimmedBadParity(){
		Instruction instr = new Instruction((byte)0b01100010,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether load is correct with indirect addressing
	public void testMULindirect() {
		Instruction instr = new Instruction((byte)0b01100101,260);
		accInit = 27;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should multiply accumulator by data[-2560+2600] = data[40] = -2560 + 400 
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit*(-2560+400), machine.getAccum())
				);
	}	

	@Test 
	// Verify parity checking is working
	public void testMULindirectBadParity() {
		Instruction instr = new Instruction((byte)0b01100100,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test
	// Check MUL cannot have special flags for Jump instructions
	public void testMULBadflags() {
		Instruction instr = new Instruction((byte)0b01100110,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}	

	@Test
	// Verify parity checking is working
	public void testMULBadflagsAndParity() {
		Instruction instr = new Instruction((byte)0b01100111,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test
	// Test whether DIV is correct with direct addressing
	public void testDIVdirect(){
		Instruction instr = new Instruction((byte)0b01101001,260);
		accInit = 275;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		//Should divide accumulator by data[260]  
		//Change data[260] and dataCopy[260] to 12
		machine.setData(260, 12);
		dataCopy[260] = 12;
		machine.step();
		assertAll (
				//Should divide accumulator by data[260] was set to 12  
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit/12, machine.getAccum())
				);
	}

	@Test 
	// Test for divide by zero with direct addressing
	public void testDIVzeroDirect(){
		Instruction instr = new Instruction((byte)0b01101001,260);
		accInit = 275;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		//Should divide accumulator by data[260]  
		//Change data[260] to 0
		machine.setData(260, 0);
		Throwable exception = assertThrows(DivideByZeroException.class,
				() -> machine.step());
		assertEquals("Zero Division", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testDIVdirectBadParity(){
		Instruction instr = new Instruction((byte)(byte)0b01101000,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether DIV is correct with immediate addressing
	public void testDIVimmed(){
		Instruction instr = new Instruction((byte)0b01101010,12);
		accInit = 275;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		machine.step();
		assertAll (
				//Should divide accumulator by 12
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit/12, machine.getAccum())
				);
	}

	@Test 
	// Test for divide by zero with immediate addressing
	public void testDIVZeroImmed(){
		Instruction instr = new Instruction((byte)0b01101010,0);
		accInit = 275;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		Throwable exception = assertThrows(DivideByZeroException.class,
				() -> machine.step());
		assertEquals("Zero Division", exception.getMessage());
	}

	@Test 
	// Verify parity checking is working
	public void testDIVimmedBadParity(){
		Instruction instr = new Instruction((byte)0b01101011,12);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}

	@Test
	// Test whether DIV is correct with indirect addressing
	public void testDIVindirect() {
		Instruction instr = new Instruction((byte)0b01101100,260);
		accInit = 275;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		// should divide accumulator by 
		// data[data[260]] = data[-2560+2600] = data[40]
		// change data[40] and dataCopy[40] to 12
		machine.setData(40, 12);
		dataCopy[40] = 12;
		machine.step();
		assertAll (
				//Should divide accumulator by data[40] set to 12
				//Test machine is not changed
				() -> assertArrayEquals(dataCopy, machine.getData()), 
				//Test program counter incremented
				() -> assertEquals(pcInit + 1, machine.getPC()),
				//Test accumulator modified
				() -> assertEquals(accInit/12, machine.getAccum())
				);
	}	

	@Test 
	// Test for divide by zero with indirect addressing
	public void testDIVzeroIndirect() {
		Instruction instr = new Instruction((byte)0b01101100,260);
		accInit = 275;
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		machine.setAccum(accInit);
		//Should divide accumulator by data[data[260] = 
		//data[-2560+2600] = data[40]
		//change data[40] to 0
		machine.setData(40, 0);
		Throwable exception = assertThrows(DivideByZeroException.class,
				() -> machine.step());
		assertEquals("Zero Division", exception.getMessage());
	}	

	@Test 
	// Verify parity checking is working
	public void testDIVindirectBadParity() {
		Instruction instr = new Instruction((byte)0b01101101,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

	@Test 
	// Check DIV cannot have special flags for Jump instructions
	public void testDIVBadflags() {
		Instruction instr = new Instruction((byte)0b01101111,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(IllegalInstructionException.class,
				() -> machine.step());
		assertEquals("Illegal flags for this instruction: (11)", exception.getMessage());
	}	

	@Test
	// Verify parity checking is working
	public void testDIVBadflagsAndParity() {
		Instruction instr = new Instruction((byte)0b01101110,260);
		machine.setCode(pcInit, instr);
		machine.setPC(pcInit);
		Throwable exception = assertThrows(ParityCheckException.class,
				() -> machine.step());
		assertEquals("Flags = 'This instruction is corrupted'", exception.getMessage());
	}	

}

