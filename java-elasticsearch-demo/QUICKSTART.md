# Quick Start Guide

Hướng dẫn chạy nhanh dự án trong 5 phút.

## Bước 1: Kiểm tra Prerequisites

```bash
# Kiểm tra Java version (cần Java 21)
java -version

# Kiểm tra Docker
docker --version
docker-compose --version
```

## Bước 2: Start Docker Services

```bash
# Vào thư mục project
cd java-elasticsearch-demo

# Start PostgreSQL, Elasticsearch, Kibana
docker-compose up -d

# Đợi ~30 giây để services khởi động
sleep 30

# Kiểm tra Elasticsearch
curl http://localhost:9200
```

## Bước 3: Build và Run Application

### Option A: Sử dụng Gradle

```bash
# Build project
./gradlew clean build -x test

# Run application
./gradlew bootRun
```

### Option B: Sử dụng Maven

```bash
# Build project
./mvnw clean package -DskipTests

# Run application
./mvnw spring-boot:run
```

## Bước 4: Tạo Sample Data

```bash
# Mở terminal mới

# Cấp quyền execute cho script
chmod +x scripts/create-sample-data.sh

# Chạy script
./scripts/create-sample-data.sh
```

## Bước 5: Test Search

```bash
# Cấp quyền execute
chmod +x scripts/test-search.sh

# Test các loại search
./scripts/test-search.sh
```

## Test Thủ Công

### Tạo Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sauvage Dior",
    "description": "A fresh and powerful fragrance",
    "price": 2500000,
    "gender": "MALE",
    "sillage": "STRONG",
    "longevity": "LONG_LASTING",
    "brand": "Dior",
    "concentration": "EDT"
  }'
```

### Search với Typo (Fuzzy)

```bash
curl "http://localhost:8080/api/products/search?query=sauvaje"
```

### Search với Filters

```bash
curl "http://localhost:8080/api/products/search?gender=MALE&minPrice=2000000&maxPrice=3000000"
```

## Truy cập UIs

- **Kibana**: http://localhost:5601
- **Elasticsearch**: http://localhost:9200
- **Application**: http://localhost:8080

## Xem Logs

```bash
# Application logs
# (xem trong terminal đang chạy ./gradlew bootRun)

# Elasticsearch logs
docker logs perfume-elasticsearch

# PostgreSQL logs
docker logs perfume-postgres
```

## Dọn dẹp

```bash
# Stop application (Ctrl+C)

# Stop và xóa containers
docker-compose down

# Xóa cả volumes (data)
docker-compose down -v
```

## Troubleshooting

### Lỗi: Port 8080 đã được sử dụng

```bash
# Đổi port trong application.yml
server:
  port: 8081
```

### Lỗi: Elasticsearch connection refused

```bash
# Restart Elasticsearch
docker-compose restart elasticsearch

# Đợi 30 giây
sleep 30

# Kiểm tra lại
curl http://localhost:9200
```

### Lỗi: Build failed

```bash
# Clean và build lại
./gradlew clean build --refresh-dependencies
```

## Next Steps

1. Xem chi tiết trong [README.md](README.md)
2. Import Postman collection từ `postman_collection.json`
3. Xem test cases trong `src/test/java/`
4. Khám phá Kibana để visualize data

## Useful Commands

```bash
# Reindex tất cả products
curl -X POST http://localhost:8080/api/products/reindex

# Xem tất cả products
curl http://localhost:8080/api/products

# Xem Elasticsearch indices
curl http://localhost:9200/_cat/indices?v

# Xem mapping của index
curl http://localhost:9200/products/_mapping
```
