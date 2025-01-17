package command.net;

public class Main {
    public static void main(String[] args) {
        try {
            Atari atari = new Atari("C:\\Users\\ce3300\\Downloads\\MovingVideo.bin", false);
            atari.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}