package ui;

import dao.ProductDao;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductStoreSwing extends JFrame {
    private final DefaultListModel<Product> listModel = new DefaultListModel<>();
    private final JList<Product> list = new JList<>(listModel);
    private final ProductDao dao = new ProductDao();

    private final JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel nameLabel = new JLabel("Select a product");
    private final JLabel brandLabel = new JLabel("");
    private final JLabel priceLabel = new JLabel("");
    private final JLabel quantityLabel = new JLabel("");
    private final JTextArea descriptionArea = new JTextArea();
    private final Map<String, ImageIcon> iconCache = new HashMap<>();

    public ProductStoreSwing() {
        super("Product Store - Lab 4");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(820, 520));
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(Color.WHITE);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        loadProductsFromDatabase();
        setLocationRelativeTo(null);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Product Store - dữ liệu truy vấn từ CSDL H2");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(title);

        return header;
    }

    private JSplitPane buildContent() {
        list.setCellRenderer(new ProductRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showDetails(list.getSelectedValue());
            }
        });

        JScrollPane listScroll = new JScrollPane(list);
        listScroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, new Color(230, 230, 230)));

        JPanel detail = new JPanel(new BorderLayout(16, 16));
        detail.setBackground(Color.WHITE);
        detail.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(247, 247, 247));
        imageLabel.setPreferredSize(new Dimension(380, 260));
        detail.add(imageLabel, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);

        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        brandLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        brandLabel.setForeground(new Color(100, 100, 100));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        quantityLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));

        descriptionArea.setEditable(false);
        descriptionArea.setFocusable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descriptionArea.setForeground(new Color(70, 70, 70));
        descriptionArea.setOpaque(false);

        info.add(nameLabel);
        info.add(Box.createVerticalStrut(6));
        info.add(brandLabel);
        info.add(Box.createVerticalStrut(14));
        info.add(priceLabel);
        info.add(Box.createVerticalStrut(4));
        info.add(quantityLabel);
        info.add(Box.createVerticalStrut(18));
        info.add(descriptionArea);

        detail.add(info, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, detail);
        split.setResizeWeight(0.36);
        split.setDividerLocation(360);
        split.setBorder(null);
        return split;
    }

    private void loadProductsFromDatabase() {
        try {
            listModel.clear();
            List<Product> products = dao.findAll();
            for (Product p : products) {
                listModel.addElement(p);
            }
            if (!listModel.isEmpty()) {
                list.setSelectedIndex(0);
            } else {
                showDetails(null);
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void showDetails(Product p) {
        if (p == null) {
            imageLabel.setIcon(null);
            imageLabel.setText("No product");
            nameLabel.setText("No product selected");
            brandLabel.setText("");
            priceLabel.setText("");
            quantityLabel.setText("");
            descriptionArea.setText("");
            return;
        }

        ImageIcon icon = loadIcon(p.getImagePath(), 360, 240);
        imageLabel.setIcon(icon);
        imageLabel.setText(icon == null ? "Image not found" : "");
        nameLabel.setText(p.getName());
        brandLabel.setText(blankToDefault(p.getBrand(), "Unknown brand"));
        priceLabel.setText(formatPrice(p.getPrice()));
        quantityLabel.setText("Quantity: " + p.getQuantity());
        descriptionArea.setText(blankToDefault(p.getDescription(), ""));
        descriptionArea.setCaretPosition(0);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductStoreSwing().setVisible(true));
    }

    private class ProductRenderer extends JPanel implements ListCellRenderer<Product> {
        private final JLabel thumbnail = new JLabel();
        private final JLabel title = new JLabel();
        private final JLabel meta = new JLabel();
        private final JLabel price = new JLabel();

        ProductRenderer() {
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            thumbnail.setPreferredSize(new Dimension(92, 64));
            thumbnail.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel text = new JPanel();
            text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
            text.setOpaque(false);

            title.setFont(new Font("SansSerif", Font.BOLD, 14));
            meta.setFont(new Font("SansSerif", Font.PLAIN, 12));
            price.setFont(new Font("SansSerif", Font.BOLD, 13));

            text.add(title);
            text.add(Box.createVerticalStrut(4));
            text.add(meta);
            text.add(Box.createVerticalStrut(4));
            text.add(price);

            add(thumbnail, BorderLayout.WEST);
            add(text, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Product> list, Product value, int index, boolean isSelected, boolean cellHasFocus) {
            thumbnail.setIcon(loadIcon(value.getImagePath(), 90, 60));
            title.setText(value.getName());
            meta.setText(blankToDefault(value.getBrand(), "Unknown brand") + " | x" + value.getQuantity());
            price.setText(formatPrice(value.getPrice()));

            Color bg = isSelected ? new Color(224, 236, 255) : Color.WHITE;
            Color fg = isSelected ? new Color(20, 45, 85) : new Color(40, 40, 40);
            setBackground(bg);
            title.setForeground(fg);
            meta.setForeground(new Color(100, 100, 100));
            price.setForeground(new Color(30, 30, 30));
            return this;
        }
    }
}
