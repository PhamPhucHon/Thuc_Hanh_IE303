# Lab 4 - Product Store với H2 Database

## Mục tiêu

Lab 4 được xây dựng dựa trên bài thực hành 03. Mục tiêu chính là chuyển dữ liệu sản phẩm từ dạng lưu trực tiếp trong code sang lưu trong cơ sở dữ liệu, sau đó truy vấn sản phẩm từ cơ sở dữ liệu để hiển thị lên giao diện.

Yêu cầu bài làm:

> Dựa trên bài thực hành 03, xây dựng CSDL để lưu thông tin sản phẩm và truy vấn sản phẩm từ CSDL.

## Công nghệ sử dụng

- Java Swing: xây dựng giao diện quản lý sản phẩm.
- JDBC: kết nối và thao tác với CSDL.
- H2 embedded database: lưu dữ liệu sản phẩm dạng file local.

## CSDL đang dùng

Lab 4 hiện dùng **H2 embedded database**.

Thông tin kết nối:

| Thuộc tính | Giá trị |
| --- | --- |
| Driver/JAR | `lib/h2.jar` |
| JDBC URL | `jdbc:h2:./lab4db;AUTO_SERVER=TRUE` |
| User | `sa` |
| Password | Rỗng |
| File CSDL | `lab4db.mv.db` |

## Cấu trúc thư mục

```text
Lab_4/
├── TestRunner.java
├── README.md
├── dao/
│   └── ProductDao.java
├── db/
│   └── Database.java
├── images/
│   ├── img1.png
│   ├── img2.png
│   ├── img3.png
│   ├── img4.png
│   ├── img5.png
│   └── img6.png
├── lib/
│   └── h2.jar
├── model/
│   └── Product.java
├── ui/
│   └── ProductStoreSwing.java
└── lab4db.mv.db        # được tạo sau khi chạy chương trình
```

## Vai trò các file

- `model/Product.java`: lớp model biểu diễn sản phẩm.
- `db/Database.java`: tạo kết nối H2, tạo bảng `products`, thêm cột còn thiếu nếu DB cũ chưa có và seed dữ liệu mẫu.
- `dao/ProductDao.java`: lớp DAO thực hiện truy vấn danh sách sản phẩm từ CSDL.
- `ui/ProductStoreSwing.java`: giao diện Swing hiển thị danh sách và chi tiết sản phẩm lấy từ CSDL.
- `TestRunner.java`: chương trình kiểm thử nhanh bằng console.

## Bảng products

Chương trình tự tạo bảng `products` khi chạy nếu bảng chưa tồn tại.

| Cột | Kiểu dữ liệu | Ý nghĩa |
| --- | --- | --- |
| `id` | `IDENTITY PRIMARY KEY` | Mã sản phẩm tự tăng |
| `name` | `VARCHAR(255)` | Tên sản phẩm |
| `brand` | `VARCHAR(255)` | Thương hiệu |
| `description` | `CLOB` | Mô tả sản phẩm |
| `price` | `DECIMAL(12,2)` | Giá sản phẩm |
| `quantity` | `INT` | Số lượng |
| `image_path` | `VARCHAR(500)` | Đường dẫn ảnh sản phẩm |

## Chức năng chính

- Lưu thông tin sản phẩm vào CSDL H2.
- Truy vấn danh sách sản phẩm từ CSDL.
- Hiển thị chi tiết sản phẩm gồm ảnh, tên, thương hiệu, giá, số lượng và mô tả.

## Biên dịch và chạy giao diện

Mở PowerShell tại thư mục gốc project:

```powershell
cd Lab_4
javac -encoding UTF-8 -cp ".;lib\h2.jar" -d . TestRunner.java model\Product.java db\Database.java dao\ProductDao.java ui\ProductStoreSwing.java
java -cp ".;lib\h2.jar" ui.ProductStoreSwing
```

## Kiểm thử bằng console

```powershell
cd Lab_4
javac -encoding UTF-8 -cp ".;lib\h2.jar" -d . TestRunner.java model\Product.java db\Database.java dao\ProductDao.java ui\ProductStoreSwing.java
java -cp ".;lib\h2.jar" TestRunner
```

`TestRunner` sẽ kiểm tra việc lưu và truy vấn dữ liệu bằng cách:

- In toàn bộ danh sách sản phẩm trong CSDL.
