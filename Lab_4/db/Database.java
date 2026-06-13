package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String JDBC_URL = "jdbc:h2:./lab4db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    static {
        try {
            init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASS);
    }

    private static void init() throws SQLException {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS products ("
                    + "id IDENTITY PRIMARY KEY,"
                    + "name VARCHAR(255) NOT NULL,"
                    + "brand VARCHAR(255) DEFAULT '',"
                    + "description CLOB,"
                    + "price DECIMAL(12,2) NOT NULL,"
                    + "quantity INT NOT NULL DEFAULT 0,"
                    + "image_path VARCHAR(500) DEFAULT ''"
                    + ")");
            addColumnIfMissing(c, "BRAND", "brand VARCHAR(255) DEFAULT ''");
            addColumnIfMissing(c, "IMAGE_PATH", "image_path VARCHAR(500) DEFAULT ''");
            seedSampleProducts(c);
        }
    }

    private static void addColumnIfMissing(Connection c, String columnName, String columnDefinition) throws SQLException {
        DatabaseMetaData meta = c.getMetaData();
        try (ResultSet rs = meta.getColumns(null, null, "PRODUCTS", columnName)) {
            if (!rs.next()) {
                try (Statement s = c.createStatement()) {
                    s.execute("ALTER TABLE products ADD COLUMN " + columnDefinition);
                }
            }
        }
    }

    private static void seedSampleProducts(Connection c) throws SQLException {
        String[][] products = {
                {"4DFWD PULSE SHOES", "Adidas", "This product is excluded from all promotional discounts and offers.", "160.00", "12", "images/img1.png"},
                {"FORUM MID SHOES", "Adidas", "Classic basketball style meets modern comfort.", "100.00", "15", "images/img2.png"},
                {"SUPERNOVA SHOES", "Adidas", "NMD City Stock 2 - Lightweight cushioning.", "150.00", "10", "images/img3.png"},
                {"ADIDAS RUNNING", "Adidas", "NMD City Stock 2 - Experience the future.", "160.00", "8", "images/img4.png"},
                {"ULTRABOOST 22", "Adidas", "Responsive Boost technology for energy return.", "120.00", "14", "images/img5.png"},
                {"4DFWD RED", "Adidas", "High-performance running shoes.", "160.00", "9", "images/img6.png"},
                {"STAN SMITH SHOES", "Adidas", "Timeless look, effortless style, and everyday versatility.", "95.00", "20", "images/img2.png"},
                {"SUPERSTAR CLASSIC", "Adidas", "The iconic shell-toe shoe that started it all.", "100.00", "18", "images/img4.png"},
                {"NMD_R1 V2", "Adidas", "Modern performance meets retro-inspired style.", "150.00", "11", "images/img5.png"},
                {"TERREX SWIFT R3", "Adidas", "Built for the trail with Gore-Tex waterproof protection.", "140.00", "7", "images/img1.png"},
                {"GALAXY 6 SHOES", "Adidas", "Comfortable running shoes for your daily miles.", "60.00", "25", "images/img3.png"},
                {"DURAMO SPEED", "Adidas", "Lightweight and breathable for faster training sessions.", "90.00", "16", "images/img6.png"},
                {"QUESTAR SHOES", "Adidas", "Padded collar and responsive cushioning for all-day comfort.", "75.00", "22", "images/img3.png"},
                {"OZWEEGO", "Adidas", "Bold 90s-inspired design with modern Adiprene cushioning.", "120.00", "13", "images/img5.png"},
                {"FORUM LOW SHOES", "Adidas", "More than just a shoe, it's a statement of style.", "110.00", "19", "images/img2.png"}
        };

        String existsSql = "SELECT COUNT(*) FROM products WHERE name = ?";
        String insertSql = "INSERT INTO products(name, brand, description, price, quantity, image_path) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement exists = c.prepareStatement(existsSql);
             PreparedStatement insert = c.prepareStatement(insertSql)) {
            for (String[] product : products) {
                exists.setString(1, product[0]);
                try (ResultSet rs = exists.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        continue;
                    }
                }

                insert.setString(1, product[0]);
                insert.setString(2, product[1]);
                insert.setString(3, product[2]);
                insert.setDouble(4, Double.parseDouble(product[3]));
                insert.setInt(5, Integer.parseInt(product[4]));
                insert.setString(6, product[5]);
                insert.executeUpdate();
            }
        }
    }
}
