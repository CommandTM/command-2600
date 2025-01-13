package command.net;

public class Main {
    public static void main(String[] args) {
        try {
            Atari atari = new Atari("C:\\Users\\ce3300\\Downloads\\Inaccuracy2.bin", false);
            atari.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}