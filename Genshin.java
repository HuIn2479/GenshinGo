import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Genshin extends JFrame {
    private Image backgroundImage;
    private boolean appOpened = false;
    private static final Logger LOGGER = Logger.getLogger(Genshin.class.getName());

    public Genshin() {
        setTitle("什么？！你也玩元神！");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int option = JOptionPane.showConfirmDialog(this, "确定要继续吗？", "温馨提示", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {

            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("./app/go.wav"))) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error loading audio file", ex);
            }

            loadImage();

            Timer timer = new Timer(5500, e -> {
                openOtherApp();
                new Thread(() -> {
                    try {
                        Thread.sleep(5900);
                        if (!appOpened) {
                            openWebsite();
                            Timer closeTimer = new Timer(5000, e1 -> System.exit(0));
                            closeTimer.setRepeats(false);
                            closeTimer.start();
                        }
                    } catch (InterruptedException ex) {
                        LOGGER.log(Level.SEVERE, "Thread interrupted", ex);
                    }
                }).start();
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

    private void loadImage() {
        backgroundImage = new ImageIcon("./app/go.gif").getImage();
        repaint();
    }

    private void openOtherApp() {
        try {
            String applicationPath = "D:\\Program Files\\Genshin Impact\\launcher.exe";
            LOGGER.log(Level.INFO, "因为“权限”问题，会导致不能打开运行 Todo");
            ProcessBuilder pb = new ProcessBuilder(applicationPath);
            pb.start();
            appOpened = true;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error opening other application", ex);
        }
    }

    private void openWebsite() {
        try {
            Desktop.getDesktop().browse(new URI("https://ys.mihoyo.com"));
        } catch (IOException | URISyntaxException e) {
            LOGGER.log(Level.SEVERE, "Error opening website", e);
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
