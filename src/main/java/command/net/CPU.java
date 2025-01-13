package command.net;

import java.util.Scanner;

public class CPU {
    int A = 0;
    int X = 0;
    int Y = 0;
    int PC = 0xF000;
    int S = 0xFF;
    int P = 0b00000100;
    boolean debug = false;
    Scanner pause;
    Memory mem;

    public CPU(Memory mem, boolean debug) {
        this.mem = mem;
        this.debug = debug;
        if (debug) {
            pause = new Scanner(System.in);
        }
    }

    public void debugPrint(String msg) {
        if (debug) {
            System.out.println(msg);
        }
    }

    public void setStatusRegister(int bits) {
        P = P | bits;
    }

    public int getStatusRegister(int bits) {
        if ((P & bits) == bits){
            return 1;
        } else {
            return 0;
        }
    }

    private void setFlags(int val, int bits){
        P &= bits; // Zero Out Flags
        if ((bits & 0b10000000) == 0b00000000){
            P |= (val >= 0x100 ? 0x80 : 0x00); // C
        }
        if ((bits & 0b01000000) == 0b00000000){
            P |= (val != 0 ? 0x00 : 0x40); // Z
        }
        if ((bits & 0b00000010) == 0b00000000){
            P |= (val < 0 || val > 0xFF ? 0x02 : 0x00); // V
        }
        if ((bits & 0b00000001) == 0b00000000){
            P |= ((val & 0x80) == 0x80 ? 0x01 : 0x00); // N
        }

        System.out.println(Integer.toBinaryString(P));
    }

    public void execute(int op, int arg0, int arg1){
        switch (op) {
            case 0x08 -> PHP();
            case 0x0A -> ASL();
            case 0x10 -> BPL(arg0);
            case 0x28 -> PLP();
            case 0x30 -> BMI(arg0);
            case 0x48 -> PHA();
            case 0x4C -> JMP(arg0, arg1);
            case 0x50 -> BVC(arg0);
            case 0x61 -> wordADC(arg0);
            case 0x65 -> memADC(arg0);
            case 0x68 -> PLA();
            case 0x69 -> ADC(arg0);
            case 0x6D -> ADC(arg0, arg1);
            case 0x70 -> BVS(arg0);
            case 0x71 -> yWordADC(arg0);
            case 0x75 -> xMemADC(arg0);
            case 0x79 -> yADC(arg0, arg1);
            case 0x7D -> xADC(arg0, arg1);
            case 0x81 -> xWordSTA(arg0);
            case 0x84 -> STY(arg0);
            case 0x85 -> STA(arg0);
            case 0x86 -> STX(arg0);
            case 0x88 -> DEY();
            case 0x8A -> TXA();
            case 0x8C -> STY(arg0, arg1);
            case 0x8D -> STA(arg0, arg1);
            case 0x8E -> STX(arg0, arg1);
            case 0x90 -> BCC(arg0);
            case 0x91 -> yWordSTA(arg0);
            case 0x94 -> xSTY(arg0);
            case 0x95 -> xSTA(arg0);
            case 0x96 -> ySTX(arg0);
            case 0x9A -> TXS();
            case 0x98 -> TYA();
            case 0x99 -> ySTA(arg0, arg1);
            case 0x9D -> xSTA(arg0, arg1);
            case 0xA0 -> LDY(arg0);
            case 0xA1 -> xWordLDA(arg0);
            case 0xA2 -> LDX(arg0);
            case 0xA4 -> memLDY(arg0);
            case 0xA5 -> memLDA(arg0);
            case 0xA6 -> memLDX(arg0);
            case 0xA8 -> TAY();
            case 0xA9 -> LDA(arg0);
            case 0xAA -> TAX();
            case 0xAC -> LDY(arg0, arg1);
            case 0xAD -> LDA(arg0, arg1);
            case 0xAE -> LDX(arg0, arg1);
            case 0xB0 -> BCS(arg0);
            case 0xB1 -> yWordLDA(arg0);
            case 0xB4 -> xMemLDY(arg0);
            case 0xB5 -> xMemLDA(arg0);
            case 0xB6 -> yMemLDX(arg0);
            case 0xB9 -> yLDA(arg0, arg1);
            case 0xBA -> TSX();
            case 0xBC -> xLDY(arg0, arg1);
            case 0xBD -> xLDA(arg0, arg1);
            case 0xBE -> yLDX(arg0, arg1);
            case 0xC8 -> INY();
            case 0xCA -> DEX();
            case 0xD0 -> BNE(arg0);
            case 0xE1 -> wordSBC(arg0);
            case 0xE5 -> memSBC(arg0);
            case 0xE8 -> INX();
            case 0xE9 -> SBC(arg0);
            case 0xED -> SBC(arg0, arg1);
            case 0xF0 -> BEQ(arg0);
            case 0xF1 -> yWordSBC(arg0);
            case 0xF5 -> xMemSBC(arg0);
            case 0xF9 -> ySBC(arg0, arg1);
            case 0xFD -> xSBC(arg0, arg1);
            default -> System.out.println("Illegal OPcode Detected: " + op);
        }

        int temp = 0;
        if (A < 0){
            temp = A+1;
            A = 0xFF-temp;
        }
        if (A > 0xFF){
            temp = A - (0xFF+1);
            A = temp;
        }
        if (X < 0){
            temp = X+1;
            X = 0xFF-temp;
        }
        if (X > 0xFF){
            temp = X - (0xFF+1);
            X = temp;
        }
        if (Y < 0){
            temp = Y+1;
            Y = 0xFF-temp;
        }
        if (Y > 0xFF){
            temp = Y - (0xFF+1);
            Y = temp;
        }
        if (S < 0){
            temp = S+1;
            S = 0xFF-temp;
        }
        if (S > 0xFF){
            temp = S - (0xFF+1);
            S = temp;
        }
        if (P < 0){
            temp = P+1;
            P = 0xFF-temp;
        }
        if (P > 0xFF){
            temp = P - (0xFF+1);
            P = temp;
        }
        if (PC < 0){
            temp = PC+1;
            PC = 0xFFFF-temp;
        }
        if (PC > 0xFFFF){
            temp = PC - (0xFFFF+1);
            PC = temp;
        }
        mem.overflow();
        if (debug) {
            String paused = pause.nextLine();
        }
    }

    // region CPU Memory and Register Transfers
    // region Register/Immediate to Register Transfer
    private void TAY(){
        Y = A;
        debugPrint("Y: " + A);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(Y, 0b10111110);
        // clk(2)???
    }

    public void TAX(){
        X = A;
        debugPrint("X: " + X);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(X, 0b10111110);
        // clk(2)???
    }

    public void TSX(){
        X = S;
        debugPrint("X: " + X);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(X, 0b10111110);
        // clk(2)???
    }

    public void TYA(){
        A = Y;
        debugPrint("A: " + A);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk)2)???
    }

    public void TXA(){
        A = X;
        debugPrint("A: " + A);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk)2)???
    }

    public void TXS(){
        S = X;
        debugPrint("S: " + S);
        PC += 1;
        debugPrint("PC: " + PC);
        // clk(2);
    }

    public void LDA(int arg0){
        A = arg0;
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk(2)???
    }

    public void LDX(int arg0){
        X = arg0;
        debugPrint("X: " + X);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(X, 0b10111110);
        // clk)2)???
    }

    public void LDY(int arg0){
        Y = arg0;
        debugPrint("Y: " + Y);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(Y, 0b10111110);
        // clk)2)???
    }
    //endregion
    // region Load Register from Memory
    public void memLDA(int arg0){
        A = mem.readMem(arg0);
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk(3)???
    }

    public void xMemLDA(int arg0){
        A = mem.readMem(arg0 + X);
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk(4)???
    }

    public void LDA(int arg0, int arg1){
        A = mem.readMem((arg1 << 8) + arg0);
        debugPrint("A: " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk(4)???
    }

    public void xLDA(int arg0, int arg1){
        A = mem.readMem((arg1 << 8) + arg0 + X);
        debugPrint("A: " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk(4*)???
    }

    public void yLDA(int arg0, int arg1){
        A = mem.readMem((arg1 << 8) + arg0 + Y);
        debugPrint("A: " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk(4*)???
    }

    public void xWordLDA(int arg0){
        A = mem.readMem((mem.readMem(arg0 + X + 1) << 8) + mem.readMem(arg0 + X));
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk(6)???
    }

    public void yWordLDA(int arg0){
        A = mem.readMem(((mem.readMem(arg0 + 1) << 8) + mem.readMem(arg0 + 1)) + Y);
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk(5*)???
    }

    public void memLDX(int arg0){
        X = mem.readMem(arg0);
        debugPrint("X: " + X);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(X, 0b10111110);
        // clk(3)???
    }

    public void yMemLDX(int arg0){
        X = mem.readMem(arg0 + Y);
        debugPrint("X: " + X);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(X, 0b10111110);
        // clk(4)???
    }

    public void LDX(int arg0, int arg1){
        X = mem.readMem((arg1 << 8) + arg0);
        debugPrint("X: " + X);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(X, 0b10111110);
        // clk(4)???
    }

    public void yLDX(int arg0, int arg1){
        X = mem.readMem((arg1 << 8) + arg0 + Y);
        debugPrint("X: " + X);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(X, 0b10111110);
        // clk(4*)???
    }

    public void memLDY(int arg0){
        Y = mem.readMem(arg0);
        debugPrint("Y: " + Y);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(Y, 0b10111110);
        // clk(3)???
    }

    public void xMemLDY(int arg0){
        Y = mem.readMem(arg0 + X);
        debugPrint("Y: " + Y);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(Y, 0b10111110);
        // clk(4)???
    }

    public void LDY(int arg0, int arg1){
        Y = mem.readMem((arg1 << 8) + arg0);
        debugPrint("Y: " + Y);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(Y, 0b10111110);
        // clk(4)???
    }

    public void xLDY(int arg0, int arg1){
        Y = mem.readMem((arg1 << 8) + arg0 + X);
        debugPrint("Y: " + Y);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(Y, 0b10111110);
        // clk(4*)???
    }
    // endregion
    // region Store Register in Memory
    public void STA(int arg0){
        mem.writeMem(arg0, A);
        debugPrint("MEM (" + arg0 + "): " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        // clk(3)???
    }

    public void xSTA(int arg0){
        mem.writeMem(arg0+X, A);
        debugPrint("MEM (" + (arg0+X) + "): " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        // clk(4)???
    }

    public void STA(int arg0, int arg1){
        mem.writeMem((arg1 << 8) + arg0, A);
        debugPrint("MEM (" + ((arg1 << 8) + arg0) + "): " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        // clk(4)???
    }

    public void xSTA(int arg0, int arg1){
        mem.writeMem(((arg1 << 8) + arg0) + X, A);
        debugPrint("MEM (" + (((arg1 << 8) + arg0)+X) + "): " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        // clk(5)???
    }

    public void ySTA(int arg0, int arg1){
        mem.writeMem(((arg1 << 8) + arg0) + Y, A);
        debugPrint("MEM (" + (((arg1 << 8) + arg0)+Y) + "): " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        // clk(5)???
    }

    public void xWordSTA(int arg0){
        mem.writeMem((mem.readMem(arg0 + X + 1) << 8) + mem.readMem(arg0 + X), A);
        debugPrint("MEM (" + (mem.readMem(arg0 + X + 1) << 8) + mem.readMem(arg0 + X) + "): " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        // clk(6)???
    }

    public void yWordSTA(int arg0){
        mem.writeMem(((mem.readMem(arg0 + 1) << 8) + mem.readMem(arg0 + 1)) + Y, A);
        debugPrint("MEM (" + (((mem.readMem(arg0 + 1) << 8) + mem.readMem(arg0 + 1)) + Y) + "): " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        // clk(6)???
    }

    public void STX(int arg0){
        mem.writeMem(arg0, X);
        debugPrint("MEM (" + arg0 + "): " + X);
        PC += 2;
        debugPrint("PC: " + PC);
        // clk(3)???
    }

    public void ySTX(int arg0){
        mem.writeMem(arg0+Y, X);
        debugPrint("MEM (" + (arg0+Y) + "): " + X);
        PC += 2;
        debugPrint("PC: " + PC);
        // clk(4)???
    }

    public void STX(int arg0, int arg1){
        mem.writeMem((arg1 << 8) + arg0, X);
        debugPrint("MEM (" + ((arg1 << 8) + arg0) + "): " + X);
        PC += 3;
        debugPrint("PC: " + PC);
        // clk(4)???
    }

    public void STY(int arg0){
        mem.writeMem(arg0, Y);
        debugPrint("MEM (" + arg0 + "): " + Y);
        PC += 2;
        debugPrint("PC: " + PC);
        // clk(3)???
    }

    public void xSTY(int arg0){
        mem.writeMem(arg0+X, Y);
        debugPrint("MEM (" + (arg0+X) + "): " + Y);
        PC += 2;
        debugPrint("PC: " + PC);
        // clk(4)???
    }

    public void STY(int arg0, int arg1){
        mem.writeMem((arg1 << 8) + arg0, Y);
        debugPrint("MEM (" + ((arg1 << 8) + arg0) + "): " + Y);
        PC += 3;
        debugPrint("PC: " + PC);
        // clk(4)???
    }
    // endregion
    // region Push/Pull
    public void PHA(){
        mem.writeMem(S, A);
        debugPrint("MEM (" + S + "): " + A);
        S--;
        debugPrint("S: " + S);
        PC += 1;
        debugPrint("PC: " + PC);
        // clk(3)???
    }

    public void PHP(){
        mem.writeMem(S, P);
        debugPrint("MEM (" + S + "): " + P);
        S--;
        debugPrint("S: " + S);
        PC += 1;
        debugPrint("PC: " + PC);
        // clk(3)???
    }

    public void PLA(){
        S++;
        debugPrint("S: " + S);
        A = mem.readMem(S);
        debugPrint("A: " + A);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(A, 0b10111110);
        // clk(4)???
    }

    public void PLP(){
        S++;
        debugPrint("S: " + S);
        P = mem.readMem(S);
        debugPrint("P: " + P);
        PC += 1;
        debugPrint("PC: " + PC);
        // clk(4)???
    }
    // endregion
    // endregion

    // region CPU Arithmetic/Logical Operations
    // region Add Memory to Accumulator with Carry
    private void ADC(int arg0){
        A = A + getStatusRegister(0b10000000) + arg0;
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(2)???
    }

    private void memADC(int arg0){
        A = A + getStatusRegister(0b10000000) + mem.readMem(arg0);
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(3)???
    }

    private void xMemADC(int arg0){
        A = A + getStatusRegister(0b10000000) + mem.readMem(arg0+X);
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(4)???
    }

    private void ADC(int arg0, int arg1){
        A = A + getStatusRegister(0b10000000) + mem.readMem((arg1 << 8) + arg0);
        debugPrint("A: " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(4)???
    }

    private void xADC(int arg0, int arg1){
        A = A + getStatusRegister(0b10000000) + mem.readMem(((arg1 << 8) + arg0)+X);
        debugPrint("A: " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(4)???
    }

    private void yADC(int arg0, int arg1){
        A = A + getStatusRegister(0b10000000) + mem.readMem(((arg1 << 8) + arg0)+Y);
        debugPrint("A: " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(4)???
    }

    private void wordADC(int arg0){
        A = A + getStatusRegister(0b10000000) + mem.readMem((mem.readMem(arg0+X+1) << 8) + mem.readMem(arg0+X));
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(6)???
    }

    private void yWordADC(int arg0){
        A = A + getStatusRegister(0b10000000) + mem.readMem(((mem.readMem(arg0+1) << 8) + mem.readMem(arg0))+Y);
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(5)???
    }
    // endregion
    // region Subtract Memory from Accumulator with Borrow
    private void SBC(int arg0){
        A = A + getStatusRegister(0b10000000) - 1 - arg0;
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(2)???
    }

    private void memSBC(int arg0){
        A = A + getStatusRegister(0b10000000) - 1 - mem.readMem(arg0);
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(3)???
    }

    private void xMemSBC(int arg0){
        A = A + getStatusRegister(0b10000000) - 1 - mem.readMem(arg0+X);
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(4)???
    }

    private void SBC(int arg0, int arg1){
        A = A + getStatusRegister(0b10000000) - 1 - mem.readMem((arg1 << 8) + arg0);
        debugPrint("A: " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(4)???
    }

    private void xSBC(int arg0, int arg1){
        A = A + getStatusRegister(0b10000000) - 1 - mem.readMem(((arg1 << 8) + arg0)+X);
        debugPrint("A: " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(4)???
    }

    private void ySBC(int arg0, int arg1){
        A = A + getStatusRegister(0b10000000) - 1 - mem.readMem(((arg1 << 8) + arg0)+Y);
        debugPrint("A: " + A);
        PC += 3;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(4)???
    }

    private void wordSBC(int arg0){
        A = A + getStatusRegister(0b10000000) - 1 - mem.readMem((mem.readMem(arg0+X+1) << 8) + mem.readMem(arg0+X));
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(6)???
    }

    private void yWordSBC(int arg0){
        A = A + getStatusRegister(0b10000000) - 1 - mem.readMem(((mem.readMem(arg0+1) << 8) + mem.readMem(arg0))+Y);
        debugPrint("A: " + A);
        PC += 2;
        debugPrint("PC: " + PC);
        setFlags(A,  0b00111100);
        // clk(5)???
    }
    // endregion
    // region Logical AND Memory with Accumulator
    // endregion
    // region Exclusive-OR Memory with Accumulator
    // endregion
    // region Logical OR Memory with Accumulator
    // endregion
    // region Compare
    // endregion
    // region Bit Test
    // endregion
    // region Increment by One
    private void INX(){
        X++;
        debugPrint("X: " + X);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(X, 0b10111110);
        // clk(2)???
    }

    private void INY(){
        Y++;
        debugPrint("Y: " + Y);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(Y, 0b10111110);
        // clk(2)???
    }
    // endregion
    // region Decrement by One
    public void DEX(){
        X--;
        debugPrint("X: " + X);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(X, 0b10111110);
        // clk(2)???
    }

    public void DEY(){
        Y--;
        debugPrint("Y: " + Y);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(Y, 0b10111110);
        // clk(2)???
    }
    // endregion
    //endregion

    // region CPU Rotate and Shift Instructions
    // region Shift Left Logical/Arithmetic
    private void ASL(){
        A = A << 1;
        debugPrint("A: " + A);
        PC += 1;
        debugPrint("PC: " + PC);
        setFlags(A, 0b00111110);
        // clk(2)???
    }
    // endregion
    // region Shift Right Logical
    // endregion
    // region Rotate Left through Carry
    // endregion
    // region Rotate Right through Carry
    // endregion
    // endregion

    // region CPU Jump and Control Instructions
    // region Normal Jumps & Subroutine Calls/Returns
    private void JMP(int arg0, int arg1){
        PC = (arg0 << 8) + arg1;
        debugPrint("PC: " + PC);
        // clk(3)???
    }
    // endregion
    // region Conditional Branches
    private void BPL(int arg0){
        if ((P & 0b00000001) == 0b00000000){
            PC += ((byte)arg0);
            debugPrint("PC: " + PC);
        } else {
            PC += 2;
            debugPrint("PC: " + PC);
        }
        // clk(?)???
    }

    private void BMI(int arg0){
        if ((P & 0b00000001) == 0b00000001){
            PC += ((byte)arg0);
            debugPrint("PC: " + PC);
        } else {
            PC += 2;
            debugPrint("PC: " + PC);
        }
        // clk(?)???
    }

    private void BVC(int arg0){
        if ((P & 0b00000010) == 0b00000000){
            PC += ((byte)arg0);
            debugPrint("PC: " + PC);
        } else {
            PC += 2;
            debugPrint("PC: " + PC);
        }
        // clk(?)???
    }

    private void BVS(int arg0){
        if ((P & 0b00000010) == 0b00000010){
            PC += ((byte)arg0);
            debugPrint("PC: " + PC);
        } else {
            PC += 2;
            debugPrint("PC: " + PC);
        }
        // clk(?)???
    }

    private void BCC(int arg0){
        if ((P & 0b10000000) == 0b00000000){
            PC += ((byte)arg0);
            debugPrint("PC: " + PC);
        } else {
            PC += 2;
            debugPrint("PC: " + PC);
        }
        // clk(?)???
    }

    private void BCS(int arg0){
        if ((P & 0b10000000) == 0b10000000){
            PC += ((byte)arg0);
            debugPrint("PC: " + PC);
        } else {
            PC += 2;
            debugPrint("PC: " + PC);
        }
        // clk(?)???
    }

    private void BNE(int arg0){
        if ((P & 0b01000000) == 0b00000000){
            PC += ((byte)arg0);
            debugPrint("PC: " + PC);
        } else {
            PC += 2;
            debugPrint("PC: " + PC);
        }
        // clk(?)???
    }

    private void BEQ(int arg0){
        if ((P & 0b01000000) == 0b01000000){
            PC += ((byte)arg0);
            debugPrint("PC: " + PC);
        } else {
            PC += 2;
            debugPrint("PC: " + PC);
        }
        // clk(?)???
    }
    // endregion
    // region Interrupts, Exceptions, Breakpoints
    // endregion
    // region CPU Control
    // endregion
    // region No Operation
    // endregion
    // endregion
}
