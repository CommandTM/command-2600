package command.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Memory {
    int[] memory = new int[65536];

    public void loadROM(String rom) throws IOException {
        File file = new File(rom);
        FileInputStream fis = new FileInputStream(file);

        int c;
        int addr = 0xF000;
        while(fis.available() > 0) {
            c = fis.read();
            memory[addr++] = c;
        }
        System.out.println("Loaded ROM to Memory");
    }

    public int readMem(int addr){
        return memory[addr];
    }

    public void writeMem(int addr, int val){
        memory[addr] = val;
    }

    public void overflow(){
        for (int i = 0; i < memory.length; i++) {
            int temp = 0;
            if (memory[i] < 0){
                temp = memory[i] + 1;
                memory[i] = 0xFF + temp;
            }

            if (memory[i] > 0xFF){
                temp = memory[i] - (0xFF + 1);
                memory[i] = temp;
            }
        }
    }
}
