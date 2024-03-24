import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Genshin extends JFrame {
    private Image backgroundImage;
    private boolean appOpened = false;

    private static final String APP_PATH = "D:\\Program Files\\Genshin Impact\\launcher.exe";
    private static final String AUDIO_PATH = "app/go.wav";
    private static final String IMAGE_PATH = "app/go.gif";
    private static final String WEBSITE_URL = "https://ys.mihoyo.com";

    public Genshin() {
        setTitle("什么？！你也玩原神！");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int option = JOptionPane.showConfirmDialog(this, "确定要继续吗？", "温馨提示", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            loadResources();

            Timer timer = new Timer(5500, e -> {
                openOtherApp();
                new Thread(this::openWebsiteAfterDelay).start();
            });
            timer.setRepeats(false);
            timer.start();

            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Genshin::new);
    }

    private void loadResources() {
        try {
            // Load audio
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(AUDIO_PATH));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            // Load image
            backgroundImage = new ImageIcon(IMAGE_PATH).getImage();
            repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openOtherApp() {
        try {
            File file = new File(APP_PATH);
            if (file.exists()) {
                System.out.println("因为“权限”问题，会导致不能打开运行 Todo");
                ProcessBuilder pb = new ProcessBuilder(APP_PATH);
                pb.start();
                appOpened = true;
            } else {
                System.out.println("OOPS!You haven't GenSHin");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openWebsiteAfterDelay() {
        try {
            Thread.sleep(5900);
            if (!appOpened) {
                Desktop.getDesktop().browse(new URI(WEBSITE_URL));
                Timer closeTimer = new Timer(5000, e -> System.exit(0));
                closeTimer.setRepeats(false);
                closeTimer.start();
            }
        } catch (InterruptedException | URISyntaxException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        Image offscreen = createImage(getWidth(), getHeight());
        Graphics buffer = offscreen.getGraphics();
        buffer.setColor(Color.WHITE);
        buffer.fillRect(0, 0, getWidth(), getHeight());
        if (backgroundImage != null) {
            int width = getWidth();
            int height = getHeight();
            buffer.drawImage(backgroundImage, 0, 0, width, height, this);
        }
        g.drawImage(offscreen, 0, 0, this);
    }
}
