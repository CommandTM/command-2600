package command.net;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Video {
    BufferedImage frame;
    Memory mem;
    JFrame window;
    ImageIcon display;
    int x = 0;
    int y = 0;
    int player0x = 148;
    int pfIndex;
    int play0Index;
    int currentPF;
    boolean pf;
    boolean debug = false;
    Color coluPF;
    Color coluP0;

    public Video(Memory mem, boolean debug) {
        frame = new BufferedImage(228, 262, BufferedImage.TYPE_INT_RGB);
        this.mem = mem;
        this.debug = debug;

        window = new JFrame("Command 2600");
        window.setBounds(0, 0, 228, 262);
        display = new ImageIcon(frame);
        window.getContentPane().add(new JLabel(display));
        window.setVisible(true);
        System.out.println("Go!");
        pfIndex = 0;
        play0Index = 0;
        currentPF = 0;
        pf = false;
    }

    public void draw(){
        int colorMem = mem.readMem(0x08);
        int color = colorMem & 0b00001111;
        int luminance = (colorMem & 0b11110000) >> 4;
        coluPF = AtariColor.values()[color].color;
        for (int i = 0; i < luminance; i++) {
            coluPF = coluPF.brighter();
        }

        colorMem = mem.readMem(0x06);
        color = colorMem & 0b00001111;
        luminance = (colorMem & 0b11110000) >> 4;
        coluP0 = AtariColor.values()[color].color;
        for (int i = 0; i < luminance; i++) {
            coluP0.brighter();
        }

        if (y == 0 && x == 0){
            mem.writeMem(0x00, 0b01000000);
        } else if (y == 3 && x == 0){
            mem.writeMem(0x00, 0b00000000);
            mem.writeMem(0x01, 0b01000000);
        }

        if (y == 38){
            mem.writeMem(0x10, 0b00000000);
        }

        if (y > 37 && x > 68 && y < 232){
            if (pfIndex == 0){
                pf = getPF(currentPF);
            }

            if (pf){
                frame.setRGB(x, y, coluPF.getRGB());
            } else {
                frame.setRGB(x, y, Color.black.getRGB());
            }

            pfIndex++;
            if (pfIndex > 3){
                pfIndex = 0;
                currentPF++;
                if (currentPF > 19){
                    currentPF = 0;
                }
            }

            if (x >= player0x && x < player0x + 8){
                if (x == player0x){
                    play0Index = 0;
                }
                if (getBit(mem.readMem(0x1B), play0Index)){
                    frame.setRGB(x, y, coluP0.getRGB());
                }
            }
        }

        x++;
        if (x > 227){
            x = 0;
            y++;
            currentPF = 0;
            pfIndex = 0;
            if (y > 261){
                y = 0;
                display = new ImageIcon(frame);
                window.repaint();
                /*
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < frame.getHeight(); i++){
                    for (int j = 0; j < frame.getWidth(); j++){
                        frame.setRGB(j, i, Color.BLACK.getRGB());
                    }
                }
                 */
                            /*
                            File output = new File("output.png");
                            try {
                                ImageIO.write(frame, "png", output);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            System.exit(0);
                            */
            }
        }
    }

    private boolean getPF(int index){
        int bits = 0;
        if (index < 4){
            bits = mem.readMem(0x0D);
        } else if (index < 12){
            index -= 4;
            bits = mem.readMem(0x0E);
        } else {
            index -= 12;
            bits = mem.readMem(0x0F);
        }
        String temp = Integer.toBinaryString(bits);
        while (temp.length() < 8){
            temp = "0" + temp;
        }
        return temp.charAt(7-index) == '1';
    }

    private boolean getBit(int num, int index){
        String temp = Integer.toBinaryString(num);
        while (temp.length() < 8){
            temp = "0" + temp;
        }
        return temp.charAt(index) == '1';
    }

    private void debugPrint(String msg){
        if (debug){
            System.out.println(msg);
        }
    }
}
