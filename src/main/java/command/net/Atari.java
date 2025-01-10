package command.net;

import java.io.IOException;

public class Atari {
    Memory mem;
    CPU cpu;

    public Atari(String rom, boolean debug) throws IOException {
        mem = new Memory();
        cpu  = new CPU(mem, debug);
        mem.loadROM(rom);
    }

    public void run(){
        while(true){
            int op = mem.readMem(cpu.PC);
            int arg0 = mem.readMem(cpu.PC+1);
            int arg1 = mem.readMem(cpu.PC+2);

            cpu.execute(op, arg0, arg1);
        }
    }
}
