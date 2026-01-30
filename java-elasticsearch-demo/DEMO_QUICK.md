# 🚀 Demo Nhanh - 5 Phút

## Bước 1: Chuẩn Bị (1 phút)

```bash
# Copy project ra ngoài
cp -r java-elasticsearch-demo ~/Desktop/
cd ~/Desktop/java-elasticsearch-demo

# Start Docker
docker-compose up -d

# Đợi 30 giây
sleep 30
```

## Bước 2: Chạy Application (1 phút)

```bash
# Build & Run
./gradlew bootRun
```

Đợi thấy message: `Started SearchDemoApplication`

## Bước 3: Tạo Data (1 phút)

**Mở terminal mới:**

```bash
cd ~/Desktop/java-elasticsearch-demo
chmod +x scripts/create-sample-data.sh
./scripts/create-sample-data.sh
```

## Bước 4: Test Ngay! (2 phút)

### Test 1: Fuzzy Search (Tính năng hot! 🔥)

```bash
# Gõ SAI chính tả
curl "http://localhost:8080/api/products/search?query=sauvaje"
```

**Kết quả:** VẪN TÌM ĐƯỢC "Sauvage Dior"!

### Test 2: Autocomplete

```bash
# Chỉ gõ 3 ký tự
curl "http://localhost:8080/api/products/search?query=sau"
```

**Kết quả:** Tìm được "Sauvage"

### Test 3: Combined Filters

```bash
# Text + Price + Gender
curl "http://localhost:8080/api/products/search?query=dior&gender=MALE&minPrice=2000000&maxPrice=5000000"
```

### Test 4: Create & Search Real-time

```bash
# Tạo product mới
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Demo Product",
    "description": "Test",
    "price": 2000000,
    "gender": "UNISEX",
    "sillage": "MODERATE",
    "longevity": "MODERATE"
  }'

# Search ngay lập tức
curl "http://localhost:8080/api/products/search?query=demo"
```

**Kết quả:** Tìm thấy ngay! (Real-time sync)

---

## 🎯 Demo Cho Non-Tech

**Giải thích đơn giản:**

1. **PostgreSQL** = Kho chính lưu data
2. **Elasticsearch** = Công cụ tìm kiếm thông minh
3. **Fuzzy Search** = Tìm được cả khi gõ sai chính tả
4. **Real-time Sync** = Tìm kiếm ngay sau khi thêm data

**Ví dụ thực tế:**
- Google search - gõ sai vẫn suggest đúng
- E-commerce search bar - autocomplete
- Shopee/Lazada - search sản phẩm với filters

---

## 📊 URLs Quan Trọng

- **API:** http://localhost:8080/api/products
- **Elasticsearch:** http://localhost:9200
- **Kibana:** http://localhost:5601

---

## 🧹 Dọn Dẹp

```bash
# Ctrl+C để stop application
# Sau đó:
docker-compose down
```

---

**Xem chi tiết:** `DEMO_GUIDE.md`
