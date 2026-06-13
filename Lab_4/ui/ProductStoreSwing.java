package ui;

import dao.ProductDao;
import model.Product;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductStoreSwing extends JFrame {
    private final ProductDao dao = new ProductDao();
    private final Map<String, ImageIcon> iconCache = new HashMap<>();

    private List<Product> products;
    private AlphaPanel leftDetailPanel;
    private JLabel leftImageLabel;
    private JLabel leftNameLabel;
    private JLabel leftPriceLabel;
    private JLabel leftBrandLabel;
    private JTextArea leftDescArea;
    private JPanel rightGridPanel;
    private ProductCard selectedComponent = null;

    public ProductStoreSwing() {
        loadProductsFromDatabase();
        setupUI();
        if (!products.isEmpty()) {
            updateLeftPanel(products.get(0));
            if (rightGridPanel.getComponentCount() > 0 && rightGridPanel.getComponent(0) instanceof ProductCard) {
                selectedComponent = (ProductCard) rightGridPanel.getComponent(0);
                selectedComponent.setSelected(true);
            }
        }
    }

    private void loadProductsFromDatabase() {
        try {
            products = dao.findAll();
        } catch (SQLException ex) {
            products = List.of();
            showError(ex);
        }
    }

    private void setupUI() {
        setTitle("Product Store Assignment - Lab 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(20, 0));

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

        leftImageLabel = new JLabel();
        leftImageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        gbc.insets = new Insets(0, 0, 30, 0);
        contentPanel.add(leftImageLabel, gbc);

        leftNameLabel = new JLabel();
        leftNameLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        gbc.insets = new Insets(5, 0, 5, 0);
        contentPanel.add(leftNameLabel, gbc);

        leftPriceLabel = new JLabel();
        leftPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbc.insets = new Insets(5, 0, 5, 0);
        contentPanel.add(leftPriceLabel, gbc);

        leftBrandLabel = new JLabel();
        leftBrandLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        leftBrandLabel.setForeground(Color.GRAY);
        gbc.insets = new Insets(5, 0, 15, 0);
        contentPanel.add(leftBrandLabel, gbc);

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

        rightGridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        rightGridPanel.setBackground(new Color(245, 245, 245));
        rightGridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        for (Product p : products) {
            ProductCard card = new ProductCard(p);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedComponent != null) {
                        selectedComponent.setSelected(false);
                    }
                    selectedComponent = card;
                    selectedComponent.setSelected(true);
                    animateTransition(p);
                }
            });
            rightGridPanel.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(rightGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(leftDetailPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void animateTransition(Product p) {
        final int duration = 200;
        final int delay = 16;
        Timer timer = new Timer(delay, null);
        final long startTime = System.currentTimeMillis();

        timer.addActionListener(new java.awt.event.ActionListener() {
            boolean updated = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                float t = Math.min(1.0f, elapsed / (float) duration);
                float alpha = (float) Math.sin(t * Math.PI);

                leftDetailPanel.setAlpha(alpha);
                leftDetailPanel.repaint();

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
        leftImageLabel.setIcon(loadIcon(p.getImagePath(), 350, 250));
        leftNameLabel.setText(p.getName());
        leftPriceLabel.setText(formatPrice(p.getPrice()));
        leftBrandLabel.setText(blankToDefault(p.getBrand(), "Unknown brand"));
        leftDescArea.setText(blankToDefault(p.getDescription(), ""));
    }

    private ImageIcon loadIcon(String imagePath, int width, int height) {
        if (imagePath == null || imagePath.isBlank()) {
            return null;
        }

        String key = imagePath + ":" + width + "x" + height;
        if (iconCache.containsKey(key)) {
            return iconCache.get(key);
        }

        File imageFile = resolveImageFile(imagePath);
        if (imageFile == null) {
            iconCache.put(key, null);
            return null;
        }

        ImageIcon original = new ImageIcon(imageFile.getAbsolutePath());
        if (original.getIconWidth() <= 0) {
            iconCache.put(key, null);
            return null;
        }

        Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaled);
        iconCache.put(key, icon);
        return icon;
    }

    private File resolveImageFile(String imagePath) {
        File direct = new File(imagePath);
        if (direct.exists()) {
            return direct;
        }

        File fromProjectRoot = new File("Lab_4", imagePath);
        if (fromProjectRoot.exists()) {
            return fromProjectRoot;
        }

        return null;
    }

    private String formatPrice(double price) {
        return String.format(Locale.US, "$%.2f", price);
    }

    private String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class AlphaPanel extends JPanel {
        private float alpha = 1.0f;

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paint(g2d);
            g2d.dispose();
        }
    }

    private class ProductCard extends JPanel {
        private static final Color BORDER_COLOR = new Color(220, 220, 220);
        private static final Color HOVER_COLOR = new Color(180, 180, 180);
        private static final Color SELECTED_COLOR = new Color(100, 150, 200);

        private boolean selected = false;

        ProductCard(Product p) {
            setLayout(new GridBagLayout());
            setBackground(Color.WHITE);
            setBorder(createBorder(BORDER_COLOR, 1));
            setPreferredSize(new Dimension(200, 260));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.insets = new Insets(3, 5, 3, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            JLabel nameLabel = new JLabel(p.getName());
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
            gbc.gridy = 0;
            gbc.weighty = 0.1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(nameLabel, gbc);

            String description = blankToDefault(p.getDescription(), "");
            String shortDesc = description.length() > 20 ? description.substring(0, 18) + "..." : description;
            JLabel descLabel = new JLabel(shortDesc);
            descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            descLabel.setForeground(Color.LIGHT_GRAY);
            gbc.gridy = 1;
            gbc.weighty = 0.1;
            add(descLabel, gbc);

            JLabel imgLabel = new JLabel(loadIcon(p.getImagePath(), 160, 110));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridy = 2;
            gbc.weighty = 0.6;
            gbc.fill = GridBagConstraints.BOTH;
            add(imgLabel, gbc);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);

            JLabel brandLabel = new JLabel(blankToDefault(p.getBrand(), "Unknown"));
            brandLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            brandLabel.setForeground(Color.GRAY);

            JLabel priceLabel = new JLabel(formatPrice(p.getPrice()));
            priceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            priceLabel.setForeground(new Color(60, 60, 60));

            bottomPanel.add(brandLabel, BorderLayout.WEST);
            bottomPanel.add(priceLabel, BorderLayout.EAST);

            gbc.gridy = 3;
            gbc.weighty = 0.2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(bottomPanel, gbc);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!selected) {
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

        private Border createBorder(Color color, int thickness) {
            return BorderFactory.createCompoundBorder(
                    new LineBorder(color, thickness, true),
                    new EmptyBorder(10, 10, 10, 10));
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            if (selected) {
                setBorder(createBorder(SELECTED_COLOR, 2));
            } else {
                setBorder(createBorder(BORDER_COLOR, 1));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductStoreSwing().setVisible(true));
    }
}
