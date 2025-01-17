package command.net;

import java.io.IOException;

public class Atari {
    Memory mem;
    CPU cpu;
    Video vid;

    public Atari(String rom, boolean debug) throws IOException {
        mem = new Memory();
        cpu  = new CPU(mem, debug);
        mem.loadROM(rom);
        vid = new Video(mem, debug);
    }

    public void run(){
        long timeStamp = 0;
        int tiaCycles = 0;
        while(true){
            if (System.nanoTime() - timeStamp > 279) {
                timeStamp = System.nanoTime();
                vid.draw();
                tiaCycles++;

                if (tiaCycles >= 3 & !cpu.busy) {
                    int op = mem.readMem(cpu.PC);
                    int arg0 = mem.readMem(cpu.PC+1);
                    int arg1 = mem.readMem(cpu.PC+2);

                    cpu.execute(op, arg0, arg1);
                }
            }
        }
    }
}
