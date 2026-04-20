import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ProductStoreSwing extends JFrame {

    private List<Product> products;
    private AlphaPanel leftDetailPanel;
    private JLabel leftImageLabel, leftNameLabel, leftPriceLabel, leftBrandLabel;
    private JTextArea leftDescArea;
    private JPanel rightGridPanel;
    private ProductCard selectedComponent = null;

    public ProductStoreSwing() {
        initProducts();
        setupUI();
        if (!products.isEmpty()) {
            updateLeftPanel(products.get(0));
        }
    }

    private void initProducts() {
        products = new ArrayList<>();
        products.add(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "images/img1.png"));
        products.add(new Product("FORUM MID SHOES", "$100.00", "Adidas", "Classic basketball style meets modern comfort.", "images/img2.png"));
        products.add(new Product("SUPERNOVA SHOES", "$150.00", "Adidas", "NMD City Stock 2 - Lightweight cushioning.", "images/img3.png"));
        products.add(new Product("ADIDAS RUNNING", "$160.00", "Adidas", "NMD City Stock 2 - Experience the future.", "images/img4.png"));
        products.add(new Product("ULTRABOOST 22", "$120.00", "Adidas", "Responsive Boost technology for energy return.", "images/img5.png"));
        products.add(new Product("4DFWD RED", "$160.00", "Adidas", "High-performance running shoes.", "images/img6.png"));
        products.add(new Product("STAN SMITH SHOES", "$95.00", "Adidas", "Timeless look, effortless style, and everyday versatility.", "images/img2.png"));
        products.add(new Product("SUPERSTAR CLASSIC", "$100.00", "Adidas", "The iconic shell-toe shoe that started it all.", "images/img4.png"));
        products.add(new Product("NMD_R1 V2", "$150.00", "Adidas", "Modern performance meets retro-inspired style.", "images/img5.png"));
        products.add(new Product("TERREX SWIFT R3", "$140.00", "Adidas", "Built for the trail with Gore-Tex waterproof protection.", "images/img1.png"));
        products.add(new Product("GALAXY 6 SHOES", "$60.00", "Adidas", "Comfortable running shoes for your daily miles.", "images/img3.png"));
        products.add(new Product("DURAMO SPEED", "$90.00", "Adidas", "Lightweight and breathable for faster training sessions.", "images/img6.png"));
        products.add(new Product("QUESTAR SHOES", "$75.00", "Adidas", "Padded collar and responsive cushioning for all-day comfort.", "images/img3.png"));
        products.add(new Product("OZWEEGO", "$120.00", "Adidas", "Bold 90s-inspired design with modern Adiprene cushioning.", "images/img5.png"));
        products.add(new Product("FORUM LOW SHOES", "$110.00", "Adidas", "More than just a shoe, it's a statement of style.", "images/img2.png"));
    }

    private void setupUI() {
        setTitle("Product Store Assignment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(20, 0));

        // ===== PANEL TRÁI (CHI TIẾT) =====
        leftDetailPanel = new AlphaPanel();
        leftDetailPanel.setLayout(new BorderLayout());
        leftDetailPanel.setBackground(Color.WHITE);
        leftDetailPanel.setBorder(new EmptyBorder(50, 40, 50, 40));
        leftDetailPanel.setPreferredSize(new Dimension(450, 800));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // 1. Ảnh sản phẩm
        leftImageLabel = new JLabel();
        leftImageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        gbc.insets = new Insets(0, 0, 30, 0); // Khoảng cách dưới ảnh tăng lên 30px (thay thế separator)
        contentPanel.add(leftImageLabel, gbc);

        // 2. Tên sản phẩm
        leftNameLabel = new JLabel();
        leftNameLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        gbc.insets = new Insets(5, 0, 5, 0);
        contentPanel.add(leftNameLabel, gbc);

        // 3. Giá
        leftPriceLabel = new JLabel();
        leftPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbc.insets = new Insets(5, 0, 5, 0);
        contentPanel.add(leftPriceLabel, gbc);

        // 4. Thương hiệu
        leftBrandLabel = new JLabel();
        leftBrandLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        leftBrandLabel.setForeground(Color.GRAY);
        gbc.insets = new Insets(5, 0, 15, 0);
        contentPanel.add(leftBrandLabel, gbc);

        // 5. Mô tả
        leftDescArea = new JTextArea();
        leftDescArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        leftDescArea.setLineWrap(true);
        leftDescArea.setWrapStyleWord(true);
        leftDescArea.setEditable(false);
        leftDescArea.setFocusable(false);
        leftDescArea.setOpaque(false);
        leftDescArea.setForeground(Color.GRAY);
        leftDescArea.setMaximumSize(new Dimension(380, 200));
        gbc.insets = new Insets(5, 0, 5, 0);
        contentPanel.add(leftDescArea, gbc);

        leftDetailPanel.add(contentPanel, BorderLayout.CENTER);

        // ===== PANEL PHẢI (LƯỚI SẢN PHẨM) =====
        // Lưới 3 cột, khoảng cách 15px
        rightGridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        rightGridPanel.setBackground(new Color(245, 245, 245));
        rightGridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        for (Product p : products) {
            ProductCard card = new ProductCard(p);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedComponent != null)
                        selectedComponent.setSelected(false);
                    selectedComponent = card;
                    selectedComponent.setSelected(true);
                    animateTransition(p);   // Gọi hiệu ứng khi click
                }
            });
            rightGridPanel.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(rightGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(leftDetailPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Hiệu ứng fade mượt mà (dùng hàm sin để tạo easing)
    private void animateTransition(Product p) {
        final int DURATION = 200;   // Tổng thời gian 200ms
        final int DELAY = 16;       // ~60 FPS
        Timer timer = new Timer(DELAY, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(new ActionListener() {
            boolean updated = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                float t = Math.min(1.0f, elapsed / (float) DURATION);

                // Ease in-out: alpha tăng dần rồi giảm dần (dùng sin)
                float alpha = (float) Math.sin(t * Math.PI);

                leftDetailPanel.setAlpha(alpha);
                leftDetailPanel.repaint();

                // Khi alpha xuống gần 0 → cập nhật nội dung panel trái
                if (!updated && alpha <= 0.05f) {
                    updated = true;
                    updateLeftPanel(p);
                }

                if (t >= 1.0f) {
                    leftDetailPanel.setAlpha(1.0f);
                    leftDetailPanel.repaint();
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void updateLeftPanel(Product p) {
        leftImageLabel.setIcon(p.getLargeIcon());
        leftNameLabel.setText(p.getName());
        leftPriceLabel.setText(p.getPrice());
        leftBrandLabel.setText(p.getBrand());
        leftDescArea.setText(p.getDesc());
    }

    // Panel hỗ trợ alpha để vẽ với độ trong suốt
    private static class AlphaPanel extends JPanel {
        private float alpha = 1.0f;
        public void setAlpha(float a) { this.alpha = a; }
        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paint(g2d);
            g2d.dispose();
        }
    }

    // Lớp dữ liệu sản phẩm
    private static class Product {
        private String name, price, brand, desc, imgPath;
        public Product(String n, String p, String b, String d, String path) {
            this.name = n; this.price = p; this.brand = b; this.desc = d; this.imgPath = path;
        }
        public String getName() { return name; }
        public String getPrice() { return price; }
        public String getBrand() { return brand; }
        public String getDesc() { return desc; }
        public ImageIcon getLargeIcon() {
            return new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(350, 250, Image.SCALE_SMOOTH));
        }
        public ImageIcon getSmallIcon() {
            return new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(160, 110, Image.SCALE_SMOOTH));
        }
    }

    // Card sản phẩm với bố cục canh giữa, chiều cao cố định
        private class ProductCard extends JPanel {
        private static final Color BORDER_COLOR = new Color(220, 220, 220);
        private static final Color HOVER_COLOR = new Color(180, 180, 180);
        private static final Color SELECTED_COLOR = new Color(100, 150, 200);

        private boolean selected = false;

        public ProductCard(Product p) {
            setLayout(new GridBagLayout());
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(10, 10, 10, 10)));
            setPreferredSize(new Dimension(200, 260)); // Chiều cao cố định tránh bị kéo giãn

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.insets = new Insets(3, 5, 3, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            // Tên sản phẩm
            JLabel nameLabel = new JLabel(p.getName());
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
            // nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridy = 0;
            gbc.weighty = 0.1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(nameLabel, gbc);

            // Mô tả ngắn
            String shortDesc = p.getDesc().length() > 20
                    ? p.getDesc().substring(0, 18) + "..."
                    : p.getDesc();
            JLabel descLabel = new JLabel(shortDesc);
            descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            descLabel.setForeground(Color.LIGHT_GRAY);
            // descLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridy = 1;
            gbc.weighty = 0.1;
            add(descLabel, gbc);

            // Ảnh sản phẩm
            JLabel imgLabel = new JLabel(p.getSmallIcon());
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridy = 2;
            gbc.weighty = 0.6;      // Chiếm 60% chiều cao card
            gbc.fill = GridBagConstraints.BOTH;
            add(imgLabel, gbc);

            // Panel cuối: hãng bên trái, giá bên phải
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);

            JLabel brandLabel = new JLabel(p.getBrand());
            brandLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            brandLabel.setForeground(Color.GRAY);

            JLabel priceLabel = new JLabel(p.getPrice());
            priceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            priceLabel.setForeground(new Color(60, 60, 60));

            bottomPanel.add(brandLabel, BorderLayout.WEST);
            bottomPanel.add(priceLabel, BorderLayout.EAST);

            gbc.gridy = 3;
            gbc.weighty = 0.2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(bottomPanel, gbc);

            // Hiệu ứng hover
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!selected) { // Chỉ đổi border nếu chưa được chọn
                        setBorder(createBorder(HOVER_COLOR, 1));
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!selected) {
                        setBorder(createBorder(BORDER_COLOR, 1));
                    }
                }
            });
        }

        // Hàm tiện ích tạo border
        private Border createBorder(Color color, int thickness) {
            return BorderFactory.createCompoundBorder(
                    new LineBorder(color, thickness, true),
                    new EmptyBorder(10, 10, 10, 10));
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            if (selected) {
                setBorder(createBorder(SELECTED_COLOR, 2)); // Viền xanh dày 2px
            } else {
                setBorder(createBorder(BORDER_COLOR, 1));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductStoreSwing::new);
    }
}