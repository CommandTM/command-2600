package command.net;

public class Main {
    public static void main(String[] args) {
        try {
            Atari atari = new Atari("C:\\Users\\ce3300\\PycharmProjects\\qLearning\\.venv\\Lib\\site-packages\\ale_py\\roms\\air_raid.bin", false);
            atari.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}