# IE303 - Thực Hành

<<<<<<< HEAD
Repository này dùng để lưu trữ các bài **thực hành môn IE303** trong quá trình học.

## Thông tin sinh viên

* **Họ tên:** Phạm Phúc Hơn
* **Môn học:** IE303

---

## Giới thiệu

Repository này được tạo để lưu trữ các bài thực hành, bài tập và ví dụ code trong môn **IE303**.
Các bài thực hành được tổ chức theo từng thư mục để dễ quản lý và theo dõi.

---

## Cấu trúc thư mục

```
IE303_Thuc_Hanh
│
├── Lab01/
│   └── (nội dung bài thực hành 1)
│
├── Lab02/
│   └── (nội dung bài thực hành 2)
│
├── Lab03/
│   └── (nội dung bài thực hành 3)
│
└── README.md
```

---

## Nội dung các bài thực hành

| Bài   | Nội dung                         | Trạng thái      |
| ----- | -------------------------------- | --------------- |
| Lab01 | Giới thiệu và cài đặt môi trường | ✔ Hoàn thành    |
| Lab02 | Bài tập thực hành                | ✔ Hoàn thành    |
| Lab03 | Bài tập thực hành                | ✔ Hoàn thành    |

---

## Công nghệ sử dụng

* Java / Python / (tùy bài thực hành)
* Visual Studio Code
* Git & GitHub

---

## Mục đích repository

* Lưu trữ các bài thực hành
* Theo dõi quá trình học tập
* Chia sẻ code và tài liệu liên quan

---

## Ghi chú

Repository này chỉ phục vụ mục đích học tập.

---

✍️ **Author:** Phạm Phúc Hòn
=======
Repository này lưu các bài thực hành môn **IE303**. Mỗi bài thực hành được đặt trong một thư mục riêng để dễ theo dõi, biên dịch và chạy thử.

## Thông tin

- Sinh viên: Phạm Phúc Hơn
- Môn học: IE303
- Ngôn ngữ chính: Java
- IDE đề xuất: Visual Studio Code

## Cấu trúc thư mục

```text
IE303_Thuc_Hanh/
├── Lab_1/
│   ├── Bai1.java
│   ├── Bai2.java
│   ├── Bai3.java
│   ├── Bai4.java
│   └── assignments.ipynb
├── Lab_2/
│   ├── FlappyBirdGame.java
│   ├── assignments.ipynb
│   └── image/
├── Lab_3/
│   ├── ProductStoreSwing.java
│   ├── assignment.ipynb
│   └── images/
├── Lab_4/
│   ├── TestRunner.java
│   ├── README.md
│   ├── dao/
│   ├── db/
│   ├── images/
│   ├── lib/
│   ├── model/
│   └── ui/
└── README.md
```

## Nội dung các bài thực hành

| Bài | Nội dung | Ghi chú |
| --- | --- | --- |
| Lab 1 | Các bài tập Java cơ bản | Gồm `Bai1.java` đến `Bai4.java` |
| Lab 2 | Game Flappy Bird bằng Java Swing | Có thư mục ảnh trong `Lab_2/image` |
| Lab 3 | Giao diện Product Store bằng Java Swing | Dữ liệu sản phẩm đang nằm trong code |
| Lab 4 | Product Store dùng CSDL | Lưu và truy vấn sản phẩm từ H2 Database |

## Yêu cầu môi trường

- JDK 11 trở lên.
- PowerShell hoặc terminal tương đương.
- Visual Studio Code hoặc IDE Java khác.
- Riêng Lab 4 đã có sẵn thư viện H2 tại `Lab_4/lib/h2.jar`.

## Chạy nhanh Lab 4

Lab 4 là bài hoàn thiện theo yêu cầu: **xây dựng CSDL để lưu thông tin sản phẩm và truy vấn sản phẩm từ CSDL**.

```powershell
cd Lab_4
javac -encoding UTF-8 -cp ".;lib\h2.jar" -d . TestRunner.java model\Product.java db\Database.java dao\ProductDao.java ui\ProductStoreSwing.java
java -cp ".;lib\h2.jar" ui.ProductStoreSwing
```

Chạy test console:

```powershell
cd Lab_4
java -cp ".;lib\h2.jar" TestRunner
```

## Ghi chú

- Các file `.class` là kết quả sau khi biên dịch Java.
- File CSDL của Lab 4 là `Lab_4/lab4db.mv.db`.
- Repository này phục vụ mục đích học tập và thực hành.
>>>>>>> 5920db6 (Add Product Store application with H2 Database integration)
