# Elasticsearch Demo với Spring Boot & Java 21

Demo ứng dụng tìm kiếm sản phẩm nước hoa sử dụng **Spring Boot 3.x**, **Elasticsearch 8.x**, và **PostgreSQL**. Ứng dụng đồng bộ dữ liệu trực tiếp từ PostgreSQL sang Elasticsearch (không dùng Kafka).

## 🎯 Tính Năng

- ✅ CRUD operations cho Product
- ✅ Full-text search với Elasticsearch
- ✅ Fuzzy matching (chịu lỗi chính tả)
- ✅ Prefix search (autocomplete)
- ✅ Wildcard search (tìm substring)
- ✅ Advanced filtering (price range, gender, sillage, longevity)
- ✅ Real-time indexing (đồng bộ ngay sau mỗi thay đổi)
- ✅ Bulk reindex endpoint
- ✅ Docker Compose setup

## 🛠️ Tech Stack

- **Java**: 21
- **Spring Boot**: 3.2.1
- **Database**: PostgreSQL 15
- **Search Engine**: Elasticsearch 8.11.0
- **Build Tool**: Gradle / Maven
- **Testing**: JUnit 5, Testcontainers

## 📁 Cấu Trúc Project

```
src/main/java/com/demo/search/
├── entity/
│   ├── Product.java
│   └── enums/
│       ├── Gender.java
│       ├── Sillage.java
│       └── Longevity.java
├── document/
│   └── ProductDocument.java
├── repository/
│   ├── ProductRepository.java (JPA)
│   └── ProductSearchRepository.java (Elasticsearch)
├── service/
│   ├── ProductService.java
│   ├── IndexService.java
│   └── SearchService.java
├── controller/
│   └── ProductController.java
├── dto/
│   ├── CreateProductRequest.java
│   ├── UpdateProductRequest.java
│   ├── SearchRequest.java
│   ├── ProductDto.java
│   └── ReindexResponse.java
└── SearchDemoApplication.java
```

## 🚀 Hướng Dẫn Setup

### Prerequisites

- Java 21 (JDK)
- Docker & Docker Compose
- Gradle hoặc Maven

### Bước 1: Clone và Build Project

```bash
cd java-elasticsearch-demo

# Nếu dùng Gradle
./gradlew clean build

# Nếu dùng Maven
./mvnw clean package
```

### Bước 2: Start Docker Services

```bash
docker-compose up -d
```

Kiểm tra services:

```bash
# PostgreSQL
docker ps | grep perfume-postgres

# Elasticsearch
curl http://localhost:9200
# Response: { "name" : "...", "cluster_name" : "docker-cluster", ... }

# Kibana
open http://localhost:5601
```

### Bước 3: Run Application

```bash
# Nếu dùng Gradle
./gradlew bootRun

# Nếu dùng Maven
./mvnw spring-boot:run
```

Application sẽ chạy tại `http://localhost:8080`

### Bước 4: Test API

```bash
# Health check
curl http://localhost:8080/api/products
```

## 📚 API Documentation

### 1. Create Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sauvage Dior",
    "description": "A fresh and powerful fragrance with notes of bergamot and pepper",
    "price": 2500000,
    "gender": "MALE",
    "sillage": "STRONG",
    "longevity": "LONG_LASTING",
    "brand": "Dior",
    "concentration": "EDT"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Sauvage Dior",
  "description": "A fresh and powerful fragrance with notes of bergamot and pepper",
  "price": 2500000,
  "gender": "MALE",
  "sillage": "STRONG",
  "longevity": "LONG_LASTING",
  "brand": "Dior",
  "concentration": "EDT",
  "createdAt": "2024-01-30T10:00:00",
  "updatedAt": "2024-01-30T10:00:00"
}
```

### 2. Get All Products

```bash
curl http://localhost:8080/api/products
```

### 3. Get Product by ID

```bash
curl http://localhost:8080/api/products/1
```

### 4. Update Product

```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sauvage Elixir Dior",
    "price": 3500000
  }'
```

### 5. Delete Product

```bash
curl -X DELETE http://localhost:8080/api/products/1
```

### 6. Search Products

#### Basic Text Search
```bash
# Tìm với từ chính xác
curl "http://localhost:8080/api/products/search?query=sauvage"

# Fuzzy search (chịu lỗi chính tả)
curl "http://localhost:8080/api/products/search?query=sauvaje"

# Prefix search (autocomplete)
curl "http://localhost:8080/api/products/search?query=sau"
```

#### Price Range Filter
```bash
curl "http://localhost:8080/api/products/search?minPrice=2000000&maxPrice=3000000"
```

#### Gender Filter
```bash
curl "http://localhost:8080/api/products/search?gender=MALE"
```

#### Combined Filters
```bash
curl "http://localhost:8080/api/products/search?query=dior&gender=MALE&minPrice=2000000&maxPrice=5000000&sillage=STRONG"
```

#### Pagination
```bash
curl "http://localhost:8080/api/products/search?query=perfume&page=0&size=10"
```

### 7. Reindex All Products

```bash
curl -X POST http://localhost:8080/api/products/reindex
```

**Response:**
```json
{
  "indexedCount": 50,
  "status": "SUCCESS",
  "message": "Successfully reindexed 50 products"
}
```

## 🔍 Search Strategy

Ứng dụng sử dụng 3 loại query với boosting khác nhau:

### 1. Prefix Match (boost = 3.0)
- Ưu tiên cao nhất cho autocomplete
- Match từ đầu chuỗi
- Ví dụ: "sau" → "Sauvage"

### 2. Wildcard (boost = 2.0)
- Tìm substring ở bất kỳ vị trí nào
- Ví dụ: "vage" → "Sauvage"

### 3. Fuzzy Match (boost = 1.0)
- Chịu lỗi chính tả với Levenshtein distance
- Ví dụ: "sauvaje" → "Sauvage"

**Query DSL Example:**

```json
{
  "bool": {
    "should": [
      { "prefix": { "name": { "value": "sau", "boost": 3.0 } } },
      { "wildcard": { "name": { "value": "*sau*", "boost": 2.0 } } },
      { "fuzzy": { "name": { "value": "sau", "fuzziness": "AUTO" } } }
    ],
    "filter": [
      { "range": { "price": { "gte": 2000000, "lte": 5000000 } } },
      { "term": { "gender": "MALE" } }
    ],
    "minimum_should_match": 1
  }
}
```

## 🧪 Testing

### Run Unit Tests

```bash
# Gradle
./gradlew test

# Maven
./mvnw test
```

### Run Integration Tests

```bash
# Gradle
./gradlew integrationTest

# Maven
./mvnw verify
```

Project sử dụng **Testcontainers** để chạy PostgreSQL và Elasticsearch trong Docker containers khi test.

## 📊 Data Model

### Enums

**Gender:**
- `MALE`
- `FEMALE`
- `UNISEX`

**Sillage (độ toả hương):**
- `INTIMATE` - Gần cơ thể
- `MODERATE` - Vừa phải
- `STRONG` - Toả xa

**Longevity (độ lưu hương):**
- `WEAK` - 1-2 giờ
- `MODERATE` - 3-5 giờ
- `LONG_LASTING` - 6-12 giờ
- `ETERNAL` - >12 giờ

### Database Schema

```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(15,2) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    sillage VARCHAR(50) NOT NULL,
    longevity VARCHAR(50) NOT NULL,
    brand VARCHAR(255),
    concentration VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

### Elasticsearch Mapping

```json
{
  "properties": {
    "id": { "type": "keyword" },
    "name": { "type": "text", "analyzer": "standard" },
    "description": { "type": "text", "analyzer": "standard" },
    "price": { "type": "double" },
    "gender": { "type": "keyword" },
    "sillage": { "type": "keyword" },
    "longevity": { "type": "keyword" },
    "brand": { "type": "keyword" },
    "concentration": { "type": "keyword" }
  }
}
```

## 🔄 Sync Strategy

### Real-time Sync (No Kafka)

Ứng dụng đồng bộ dữ liệu **đồng bộ và trực tiếp** sau mỗi thao tác:

```java
@Transactional
public Product create(CreateProductRequest request) {
    Product product = productRepository.save(entity);  // 1. Save to PostgreSQL
    indexService.indexProduct(product);                // 2. Index to Elasticsearch
    return product;
}
```

**Ưu điểm:**
- ✅ Đơn giản, dễ debug
- ✅ Đảm bảo consistency ngay lập tức
- ✅ Không cần infrastructure phức tạp

**Nhược điểm:**
- ⚠️ Blocking - API chậm hơn nếu Elasticsearch down
- ⚠️ Không phù hợp cho high-traffic systems

**Cải tiến cho Production:**
- Sử dụng `@Async` cho indexing
- Thêm retry mechanism
- Implement queue (Kafka/RabbitMQ) cho scale lớn

## 🛠️ Troubleshooting

### Lỗi: Elasticsearch connection refused

```bash
# Kiểm tra Elasticsearch container
docker logs perfume-elasticsearch

# Restart container
docker-compose restart elasticsearch

# Kiểm tra health
curl http://localhost:9200/_cluster/health
```

### Lỗi: PostgreSQL connection failed

```bash
# Kiểm tra PostgreSQL container
docker logs perfume-postgres

# Connect để test
docker exec -it perfume-postgres psql -U admin -d perfume_db
```

### Lỗi: Build failed - Java version

```bash
# Kiểm tra Java version
java -version

# Output: openjdk version "21.0.x"
```

### Clear All Data

```bash
# Xóa tất cả containers và volumes
docker-compose down -v

# Start lại
docker-compose up -d
```

## 📈 Performance Tips

### 1. Index Settings

Thêm vào Elasticsearch config:

```json
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0,
    "analysis": {
      "analyzer": {
        "vietnamese_analyzer": {
          "type": "standard",
          "stopwords": "_vietnamese_"
        }
      }
    }
  }
}
```

### 2. Database Indexing

```sql
CREATE INDEX idx_product_name ON products(name);
CREATE INDEX idx_product_price ON products(price);
CREATE INDEX idx_product_gender ON products(gender);
```

### 3. Connection Pooling

Thêm vào `application.yml`:

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
```

## 📖 Learning Resources

### Elasticsearch
- [Elasticsearch Official Docs](https://www.elastic.co/guide/en/elasticsearch/reference/8.11/index.html)
- [Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/8.11/query-dsl.html)
- [Fuzzy Query](https://www.elastic.co/guide/en/elasticsearch/reference/8.11/query-dsl-fuzzy-query.html)

### Spring Data Elasticsearch
- [Spring Data Elasticsearch Reference](https://docs.spring.io/spring-data/elasticsearch/reference/)
- [Query Methods](https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/repositories/elasticsearch-repository-queries.html)

### Docker
- [Docker Compose File Reference](https://docs.docker.com/compose/compose-file/)
- [Elasticsearch Docker Setup](https://www.elastic.co/guide/en/elasticsearch/reference/8.11/docker.html)

## 🎓 Next Steps

1. **Add Authentication**: Spring Security với JWT
2. **Implement Caching**: Redis cho popular searches
3. **Add Aggregations**: Faceted search (count by brand, price ranges)
4. **Async Indexing**: `@Async` + CompletableFuture
5. **Add Monitoring**: Actuator + Prometheus + Grafana
6. **API Documentation**: Swagger/OpenAPI
7. **Add Suggestions**: Completion suggester cho autocomplete
8. **Multi-language**: Vietnamese analyzer support

## 📝 Sample Data

Chạy script sau để tạo sample data:

```bash
# Tạo 10 products
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/products \
    -H "Content-Type: application/json" \
    -d "{
      \"name\": \"Fragrance $i\",
      \"description\": \"Description for fragrance $i\",
      \"price\": $((1000000 + RANDOM % 4000000)),
      \"gender\": \"UNISEX\",
      \"sillage\": \"MODERATE\",
      \"longevity\": \"MODERATE\",
      \"brand\": \"Brand $((i % 3 + 1))\"
    }"
done
```

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Contact

- Email: demo@example.com
- GitHub: [@yourusername](https://github.com/yourusername)

---

**[Learning Focus: Elasticsearch Integration + CQRS Pattern]**
**[Mastery: L2→L3 (Apply → Optimize)]**
**[Skills: Spring Data Elasticsearch + Query DSL + Index Management]**
