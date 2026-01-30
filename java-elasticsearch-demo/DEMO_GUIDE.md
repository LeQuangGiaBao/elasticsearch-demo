# 🎬 Hướng Dẫn Demo - Step by Step

## Yêu Cầu Trước Khi Bắt Đầu

### 1. Kiểm tra Java 21

```bash
java -version
```

**Kết quả mong đợi:**
```
openjdk version "21.0.x"
```

**Nếu chưa có Java 21:**
- **MacOS:** `brew install openjdk@21`
- **Ubuntu:** `sudo apt install openjdk-21-jdk`
- **Windows:** Download từ [Adoptium](https://adoptium.net/)

### 2. Kiểm tra Docker

```bash
docker --version
docker-compose --version
```

**Nếu chưa có Docker:**
- Download từ [Docker Desktop](https://www.docker.com/products/docker-desktop/)

---

## 📋 Bước 1: Copy Project Ra Ngoài

```bash
# Copy thư mục java-elasticsearch-demo ra Desktop
cp -r java-elasticsearch-demo ~/Desktop/

# Hoặc nơi bạn muốn
cp -r java-elasticsearch-demo /path/to/your/workspace/

# Vào thư mục
cd ~/Desktop/java-elasticsearch-demo
```

---

## 🐳 Bước 2: Start Docker Services

```bash
# Start PostgreSQL + Elasticsearch + Kibana
docker-compose up -d
```

**Output mong đợi:**
```
Creating network "java-elasticsearch-demo_default" with the default driver
Creating perfume-postgres ... done
Creating perfume-elasticsearch ... done
Creating perfume-kibana ... done
```

### Đợi 30 giây để Elasticsearch khởi động

```bash
# Đợi
sleep 30

# Kiểm tra Elasticsearch
curl http://localhost:9200
```

**Nếu thành công, bạn sẽ thấy:**
```json
{
  "name" : "...",
  "cluster_name" : "docker-cluster",
  "version" : {
    "number" : "8.11.0"
  }
}
```

### Kiểm tra tất cả services

```bash
docker-compose ps
```

**Phải thấy 3 containers đang chạy:**
```
NAME                    STATUS
perfume-postgres        Up
perfume-elasticsearch   Up
perfume-kibana          Up
```

**Nếu có container bị lỗi:**
```bash
# Xem logs
docker-compose logs [service-name]

# Restart
docker-compose restart
```

---

## 🔨 Bước 3: Build Project

### Option A: Sử dụng Gradle (Khuyến nghị)

```bash
# Cấp quyền execute
chmod +x gradlew

# Build (bỏ qua tests để nhanh hơn)
./gradlew clean build -x test
```

### Option B: Sử dụng Maven

```bash
# Cấp quyền execute
chmod +x mvnw

# Build
./mvnw clean package -DskipTests
```

**Output mong đợi:**
```
BUILD SUCCESSFUL
```

---

## 🚀 Bước 4: Run Application

### Option A: Gradle

```bash
./gradlew bootRun
```

### Option B: Maven

```bash
./mvnw spring-boot:run
```

**Output mong đợi:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.1)

[...] Started SearchDemoApplication in X seconds
```

**Application đang chạy tại:** `http://localhost:8080`

---

## 🎯 Bước 5: Tạo Sample Data

**Mở terminal mới** (giữ terminal cũ chạy application)

```bash
# Vào thư mục project
cd ~/Desktop/java-elasticsearch-demo

# Cấp quyền execute
chmod +x scripts/create-sample-data.sh

# Chạy script
./scripts/create-sample-data.sh
```

**Script sẽ tạo 15 products:** Dior, Chanel, Tom Ford, Versace, etc.

**Output mong đợi:**
```
Creating sample products...
✓ Created: Sauvage Dior
✓ Created: Bleu de Chanel
✓ Created: Oud Wood Tom Ford
...
Done! Created 15 products.
```

---

## 🎪 Bước 6: Demo Các Tính Năng

### Demo 1: Xem Tất Cả Products

```bash
curl http://localhost:8080/api/products | jq
```

**Giải thích:** Lấy danh sách tất cả products từ database

### Demo 2: Basic Search

```bash
curl "http://localhost:8080/api/products/search?query=dior" | jq
```

**Giải thích:** Tìm tất cả products có chữ "dior"

### Demo 3: Fuzzy Search (Tính năng HOT! 🔥)

```bash
# Gõ SAI chính tả: "sauvaje" thay vì "sauvage"
curl "http://localhost:8080/api/products/search?query=sauvaje" | jq
```

**Kết quả:** VẪN TÌM ĐƯỢC "Sauvage Dior"!

**Giải thích:** Elasticsearch sử dụng fuzzy matching, chịu lỗi chính tả

### Demo 4: Prefix Search (Autocomplete)

```bash
# Chỉ gõ 3 ký tự đầu
curl "http://localhost:8080/api/products/search?query=sau" | jq
```

**Kết quả:** Tìm được "Sauvage Dior"

**Giải thích:** Prefix matching cho autocomplete feature

### Demo 5: Wildcard Search (Substring)

```bash
# Tìm substring ở giữa
curl "http://localhost:8080/api/products/search?query=vage" | jq
```

**Kết quả:** Tìm được "Sauvage Dior"

**Giải thích:** Wildcard search cho phép tìm substring ở bất kỳ đâu

### Demo 6: Filter Theo Giá

```bash
# Tìm products từ 2 triệu đến 3 triệu
curl "http://localhost:8080/api/products/search?minPrice=2000000&maxPrice=3000000" | jq
```

**Giải thích:** Range query trên field price

### Demo 7: Filter Theo Gender

```bash
# Tìm nước hoa nam
curl "http://localhost:8080/api/products/search?gender=MALE" | jq
```

**Giải thích:** Term query cho exact matching

### Demo 8: Combined Search (Cực mạnh! 💪)

```bash
# Tìm "dior" + nam + giá 2-5 triệu + toả hương mạnh
curl "http://localhost:8080/api/products/search?query=dior&gender=MALE&minPrice=2000000&maxPrice=5000000&sillage=STRONG" | jq
```

**Giải thích:** Kết hợp text search + multiple filters

### Demo 9: Create New Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Custom Fragrance",
    "description": "A unique blend of citrus and wood",
    "price": 1800000,
    "gender": "UNISEX",
    "sillage": "MODERATE",
    "longevity": "MODERATE",
    "brand": "Custom Brand",
    "concentration": "EDP"
  }' | jq
```

**Giải thích:** Tạo product mới → Tự động lưu vào PostgreSQL + index vào Elasticsearch

### Demo 10: Verify Real-time Indexing

```bash
# Search ngay sau khi create
curl "http://localhost:8080/api/products/search?query=custom" | jq
```

**Kết quả:** Tìm thấy ngay "My Custom Fragrance"!

**Giải thích:** Đồng bộ real-time từ PostgreSQL sang Elasticsearch

---

## 🎨 Bước 7: Xem Data Trong Kibana

1. Mở browser: http://localhost:5601
2. Đợi Kibana load (~1 phút)
3. Click **"Explore on my own"**
4. Sidebar → **Dev Tools**
5. Paste query:

```json
GET products/_search
{
  "query": {
    "match_all": {}
  }
}
```

6. Click ▶️ **Run**

**Bạn sẽ thấy:** Tất cả products trong Elasticsearch

---

## 📊 Bước 8: Import Postman Collection

1. Mở Postman
2. Click **Import**
3. Chọn file `postman_collection.json`
4. Bạn sẽ có **7 requests** sẵn sàng:
   - Create Product
   - Get All Products
   - Get Product by ID
   - Update Product
   - Delete Product
   - Search Products
   - Reindex All

5. Click vào từng request và **Send** để test

---

## 🎬 Script Demo Tự Động

Chạy script này để demo tất cả features:

```bash
chmod +x scripts/test-search.sh
./scripts/test-search.sh
```

**Output:**
```
=== Testing Elasticsearch Search ===

1. Basic Search (query=sauvage)
Found: 1 results

2. Fuzzy Search (typo: sauvaje)
Found: 1 results ✓ (Typo tolerance works!)

3. Prefix Search (query=sau)
Found: 1 results ✓ (Autocomplete works!)

4. Price Range (2M-3M)
Found: X results

5. Gender Filter (MALE)
Found: X results

6. Combined Search
Found: X results
```

---

## 🔍 Giải Thích Chi Tiết Cho Audience

### Kiến Trúc

```
Client (Postman/curl)
    ↓ HTTP Request
Spring Boot Controller
    ↓
Service Layer
    ├→ ProductService (Business Logic)
    │   ├→ Save to PostgreSQL (Primary Store)
    │   └→ IndexService → Elasticsearch (Search Engine)
    └→ SearchService
        └→ Query Elasticsearch
            - Prefix Match (boost=3.0)
            - Wildcard Match (boost=2.0)
            - Fuzzy Match (boost=1.0)
```

### Tại Sao Cần Elasticsearch?

**PostgreSQL (Relational DB):**
- ❌ Không có fuzzy search
- ❌ LIKE '%term%' rất chậm
- ❌ Không có ranking/scoring

**Elasticsearch (Search Engine):**
- ✅ Fuzzy matching (chịu lỗi chính tả)
- ✅ Full-text search với relevance scoring
- ✅ Sub-millisecond response time
- ✅ Scalable cho millions of documents

### Sync Strategy

**Hiện tại (Demo):**
```
Create/Update → PostgreSQL → Elasticsearch (Synchronous)
```

**Production (Khuyến nghị):**
```
Create/Update → PostgreSQL → Kafka → Consumer → Elasticsearch
```

---

## 📈 Advanced Demo (Optional)

### 1. Reindex Tất Cả

```bash
curl -X POST http://localhost:8080/api/products/reindex | jq
```

**Use case:** Khi thay đổi Elasticsearch mapping

### 2. Update Product

```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sauvage Elixir Dior",
    "price": 3500000
  }' | jq
```

**Verify:** Search lại để thấy thay đổi

### 3. Delete Product

```bash
curl -X DELETE http://localhost:8080/api/products/1
```

**Verify:** Search không còn thấy product này

### 4. Pagination

```bash
curl "http://localhost:8080/api/products/search?query=fragrance&page=0&size=5" | jq
```

---

## 🐛 Troubleshooting Demo

### Lỗi: Application không start

```bash
# Kiểm tra port 8080
lsof -i :8080

# Nếu bị chiếm, kill process hoặc đổi port trong application.yml
```

### Lỗi: Elasticsearch connection refused

```bash
# Kiểm tra Elasticsearch
curl http://localhost:9200

# Nếu lỗi, restart
docker-compose restart elasticsearch
sleep 30
```

### Lỗi: No results from search

```bash
# Check xem có data không
curl http://localhost:8080/api/products | jq

# Nếu không có, chạy lại create-sample-data.sh
./scripts/create-sample-data.sh

# Hoặc reindex
curl -X POST http://localhost:8080/api/products/reindex
```

### Lỗi: Build failed

```bash
# Check Java version
java -version

# Must be 21.x

# Clean và build lại
./gradlew clean build --refresh-dependencies
```

---

## 🎓 Key Talking Points Khi Demo

1. **Fuzzy Search là killer feature** - Show typo "sauvaje" → "sauvage"
2. **Real-time sync** - Create product → Search ngay lập tức
3. **Multiple search strategies** - Prefix/Wildcard/Fuzzy với boosting
4. **Production-ready architecture** - Service layer, DTOs, proper error handling
5. **Comprehensive testing** - Unit tests + Integration tests với Testcontainers
6. **Scalable** - Có thể add Kafka, clustering, caching

---

## 🎯 Demo Flow Khuyến Nghị (15 phút)

1. **(2 phút)** Giới thiệu kiến trúc với diagram
2. **(2 phút)** Show code structure - Entity/Service/Controller
3. **(3 phút)** Demo fuzzy search với typo - Highlight tính năng
4. **(2 phút)** Demo combined filters - Show flexibility
5. **(2 phút)** Create new product → Search ngay - Real-time sync
6. **(2 phút)** Show Kibana - Visualize data
7. **(2 phút)** Q&A

---

## 🧹 Dọn Dẹp Sau Demo

```bash
# Stop application (Ctrl+C trong terminal chạy bootRun)

# Stop Docker containers
docker-compose down

# Nếu muốn xóa hẳn data
docker-compose down -v
```

---

## 📞 Quick Help

**Application không chạy?**
1. Check Java version: `java -version`
2. Check Docker: `docker ps`
3. Check logs: Xem terminal output

**Search không trả về kết quả?**
1. Check data: `curl http://localhost:8080/api/products`
2. Reindex: `curl -X POST http://localhost:8080/api/products/reindex`

**Elasticsearch down?**
1. Restart: `docker-compose restart elasticsearch`
2. Check: `curl http://localhost:9200`

---

**Chúc bạn demo thành công!** 🚀
