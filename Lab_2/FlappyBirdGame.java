package Lab_2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class FlappyBirdGame extends JPanel implements ActionListener, KeyListener {
    // Kích thước cửa sổ
    private static final int WIDTH = 360;
    private static final int HEIGHT = 640;

    // Điểm cao nhất (lưu trong file)
    private static int highScore = 0;
    private static final String HIGHSCORE_FILE = "highscore.dat";

    // Các thông số chim
    private int birdX = 80;
    private int birdY = HEIGHT / 2;
    private int birdVelocity = 0;
    private final int GRAVITY = 1;
    private final int JUMP = -12;
    private static final int BIRD_WIDTH = 40;
    private static final int BIRD_HEIGHT = 40;

    // Ống (pipe)
    private ArrayList<Pipe> pipes;
    private final int PIPE_WIDTH = 60;
    private final int PIPE_GAP = 150;          // khoảng cách giữa ống trên và dưới
    private final int PIPE_SPACING = 200;       // khoảng cách giữa các cặp ống
    private int pipeSpeed = 3;
    private Random random;

    // Hình ảnh
    private Image background;
    private Image birdImage;
    private Image topPipeImage;
    private Image bottomPipeImage;

    // Game loop
    private Timer gameTimer;
    private boolean gameRunning = true;
    private int score = 0;

    // Nút chơi lại
    private JButton restartButton;

    public FlappyBirdGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        setLayout(null);   // để đặt nút ở vị trí tuyệt đối

        // Đọc điểm cao nhất
        loadHighScore();

        // Tải hình ảnh
        try {
            background = ImageIO.read(new File("Lab_2/image/flappybirdbg.png"));
            birdImage = ImageIO.read(new File("Lab_2/image/flappybird.png"));
            topPipeImage = ImageIO.read(new File("Lab_2/image/toppipe.png"));
            bottomPipeImage = ImageIO.read(new File("Lab_2/image/bottompipe.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        random = new Random();
        pipes = new ArrayList<>();
        addPipe(WIDTH);
        addPipe(WIDTH + PIPE_SPACING);

        // Tạo nút "Play Again"
        restartButton = new JButton("Play Again");
        restartButton.setFont(new Font("Arial", Font.BOLD, 18));
        restartButton.setBounds(WIDTH / 2 - 80, HEIGHT / 2 + 60, 160, 40);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> restartGame());
        add(restartButton);

        // Timer game loop
        gameTimer = new Timer(1000 / 60, this);
        gameTimer.start();
    }

    // Thêm một cặp ống mới (ống trên ngẫu nhiên, ống dưới tự động cách khoảng PIPE_GAP)
    private void addPipe(int x) {
        // Đảm bảo ống trên không quá thấp hoặc quá cao
        int minTopHeight = 50;
        int maxTopHeight = HEIGHT - PIPE_GAP - 50;
        int topHeight = random.nextInt(maxTopHeight - minTopHeight + 1) + minTopHeight;
        // int x = WIDTH;

        // Ống trên
        pipes.add(new Pipe(x, 0, topHeight, true));
        // Ống dưới (bắt đầu từ topHeight + PIPE_GAP)
        pipes.add(new Pipe(x, topHeight + PIPE_GAP, HEIGHT - topHeight - PIPE_GAP, false));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            updateGame();
        }
        repaint();
    }

    private void updateGame() {
        // 1. Vật lý chim
        birdVelocity += GRAVITY;
        birdY += birdVelocity;

        // Biên trên
        if (birdY <= 0) {
            birdY = 0;
            birdVelocity = 0;
        }
        // Biên dưới (chạm đất)
        if (birdY >= HEIGHT - BIRD_HEIGHT) {
            birdY = HEIGHT - BIRD_HEIGHT;
            gameOver();
        }

        // 2. Di chuyển ống
        for (Pipe p : pipes) {
            p.x -= pipeSpeed;
        }
        pipes.removeIf(p -> p.x + PIPE_WIDTH < 0);

        // Thêm ống mới khi cần
        if (!pipes.isEmpty() && pipes.get(pipes.size() - 1).x <= WIDTH - PIPE_SPACING) {
            addPipe(WIDTH);
        }

        // 3. Va chạm với ống
        Rectangle birdRect = new Rectangle(birdX, birdY, BIRD_WIDTH, BIRD_HEIGHT);
        for (Pipe p : pipes) {
            Rectangle pipeRect = new Rectangle(p.x, p.y, PIPE_WIDTH, p.height);
            if (birdRect.intersects(pipeRect)) {
                gameOver();
                return;
            }
        }

        // 4. Tính điểm (mỗi cặp ống được tính một lần)
        for (Pipe p : pipes) {
            if (!p.scored && p.isTop && p.x + PIPE_WIDTH < birdX) {
                p.scored = true;
                score++;
                // Cập nhật điểm cao nhất
                if (score > highScore) {
                    highScore = score;
                    saveHighScore();
                }
            }
        }
    }

    private void gameOver() {
        gameRunning = false;
        gameTimer.stop();
        restartButton.setVisible(true);
        requestFocusInWindow(); // giữ focus để nhận phím R
    }

    private void restartGame() {
        // Reset trạng thái
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        score = 0;
        pipes.clear();
        addPipe(WIDTH);
        addPipe(WIDTH + PIPE_SPACING);
        gameRunning = true;
        gameTimer.start();
        restartButton.setVisible(false);
        repaint();
    }

    // Lưu điểm cao nhất
    private void saveHighScore() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(HIGHSCORE_FILE))) {
            dos.writeInt(highScore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHighScore() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(HIGHSCORE_FILE))) {
            highScore = dis.readInt();
        } catch (IOException e) {
            highScore = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Vẽ nền
        if (background != null) {
            g.drawImage(background, 0, 0, WIDTH, HEIGHT, this);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

        // Vẽ ống
        for (Pipe p : pipes) {
            if (p.isTop) {
                Image img = topPipeImage;
                if (img != null) {
                    g.drawImage(img, p.x, p.y, PIPE_WIDTH, p.height, this);
                } else {
                    g.setColor(Color.GREEN);
                    g.fillRect(p.x, p.y, PIPE_WIDTH, p.height);
                }
            } else {
                Image img = bottomPipeImage;
                if (img != null) {
                    g.drawImage(img, p.x, p.y, PIPE_WIDTH, p.height, this);
                } else {
                    g.setColor(Color.GREEN);
                    g.fillRect(p.x, p.y, PIPE_WIDTH, p.height);
                }
            }
        }

        // Vẽ chim
        if (birdImage != null) {
            g.drawImage(birdImage, birdX, birdY, BIRD_WIDTH, BIRD_HEIGHT, this);
        } else {
            g.setColor(Color.YELLOW);
            g.fillRect(birdX, birdY, BIRD_WIDTH, BIRD_HEIGHT);
        }

        // Hiển thị điểm
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("Score: " + score, 20, 50);
        g.drawString("Best: " + highScore, 20, 90);

        // Màn hình Game Over (có khung viền)
        if (!gameRunning) {
            // Lớp phủ tối
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // Kích thước khung
            int panelWidth = 260;
            int panelHeight = 200;
            int panelX = (WIDTH - panelWidth) / 2;
            int panelY = (HEIGHT - panelHeight) / 2 - 20; // dịch lên một chút để nhường chỗ cho nút

            // Vẽ khung nền trắng mờ và viền
            g.setColor(new Color(30, 30, 30, 220));
            g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
            g.setColor(Color.WHITE);
            g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);

            // Vẽ chữ GAME OVER
            g.setFont(new Font("Arial", Font.BOLD, 28));
            String gameOverText = "GAME OVER";
            int textWidth = g.getFontMetrics().stringWidth(gameOverText);
            g.drawString(gameOverText, panelX + (panelWidth - textWidth) / 2, panelY + 50);

            // Điểm hiện tại và cao nhất
            g.setFont(new Font("Arial", Font.BOLD, 22));
            String scoreText = "Score: " + score;
            String bestText = "Best: " + highScore;
            int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
            int bestWidth = g.getFontMetrics().stringWidth(bestText);
            g.drawString(scoreText, panelX + (panelWidth - scoreWidth) / 2, panelY + 100);
            g.drawString(bestText, panelX + (panelWidth - bestWidth) / 2, panelY + 135);

            // Ghi chú (căn giữa trong khung)
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            String note = "Press R or click button";
            int noteWidth = g.getFontMetrics().stringWidth(note);
            g.drawString(note, panelX + (panelWidth - noteWidth) / 2, panelY + 170);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (gameRunning) {
            if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
                birdVelocity = JUMP;
            }
        } else {
            if (key == KeyEvent.VK_R) {
                restartGame();
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    // Lớp Pipe lưu thông tin
    class Pipe {
        int x;
        int y;
        int height;
        boolean isTop;
        boolean scored;

        Pipe(int x, int y, int height, boolean isTop) {
            this.x = x;
            this.y = y;
            this.height = height;
            this.isTop = isTop;
            this.scored = false;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBirdGame game = new FlappyBirdGame();
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}