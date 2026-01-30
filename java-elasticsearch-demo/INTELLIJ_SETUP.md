# 🎯 IntelliJ IDEA Setup - Hướng Dẫn Chi Tiết

## ✅ Checklist Trước Khi Bắt Đầu

- [ ] Docker Desktop đã cài và đang chạy
- [ ] Java 21 đã cài (kiểm tra: `java -version`)
- [ ] IntelliJ IDEA (Community hoặc Ultimate)
- [ ] Đã có folder `java-elasticsearch-demo`

---

## 📝 Bước 1: Import Project Vào IntelliJ

### Option A: Open Existing Project

1. **File → Open**
2. Chọn folder `java-elasticsearch-demo`
3. Click **OK**
4. IntelliJ sẽ tự động detect Gradle project
5. Chọn **Trust Project** khi được hỏi

### Option B: Import từ Version Control

Nếu project từ Git:
1. **File → New → Project from Version Control**
2. Paste URL hoặc chọn local folder
3. Click **Clone**

---

## 🔧 Bước 2: Configure Project SDK (QUAN TRỌNG!)

### 2.1 Set Project JDK

1. **File → Project Structure** (hoặc `Cmd+;` / `Ctrl+Alt+Shift+S`)
2. Chọn **Project** ở sidebar trái
3. **SDK:** Chọn Java 21
   - Nếu không có, click **Add SDK → Download JDK**
   - Chọn version **21**, vendor: **Amazon Corretto** hoặc **Oracle OpenJDK**
   - Click **Download**
4. **Language level:** Chọn **21**
5. Click **Apply** → **OK**

### 2.2 Set Module JDK

1. Vẫn trong **Project Structure**
2. Chọn **Modules** ở sidebar trái
3. Chọn module `java-elasticsearch-demo.main`
4. **Module SDK:** Chọn **Project SDK (21)**
5. Click **Apply** → **OK**

---

## ⚙️ Bước 3: Enable Annotation Processing (CHO LOMBOK)

**QUAN TRỌNG:** Nếu bỏ qua bước này, Lombok sẽ không work!

1. **File → Settings** (Windows/Linux) hoặc **IntelliJ IDEA → Preferences** (Mac)
   - Shortcut: `Ctrl+Alt+S` (Windows/Linux) hoặc `Cmd+,` (Mac)

2. Tìm: **Build, Execution, Deployment → Compiler → Annotation Processors**

3. Check các options sau:
   - ✅ **Enable annotation processing**
   - ✅ **Obtain processors from project classpath**
   - **Store generated sources relative to:** Chọn **Module content root**

4. Click **Apply** → **OK**

---

## 🐘 Bước 4: Sync Gradle Dependencies

### 4.1 Refresh Gradle

1. Mở **Gradle tool window** (View → Tool Windows → Gradle)
2. Click icon **Reload All Gradle Projects** (icon tròn 2 mũi tên)
3. Đợi download dependencies (có thể mất 2-5 phút lần đầu)

### 4.2 Verify Dependencies

Trong Gradle tool window, expand:
```
java-elasticsearch-demo
  └── Dependencies
      ├── compileClasspath
      ├── runtimeClasspath
      └── testCompileClasspath
```

Check các dependencies quan trọng:
- ✅ spring-boot-starter-web
- ✅ spring-boot-starter-data-jpa
- ✅ spring-boot-starter-data-elasticsearch
- ✅ postgresql
- ✅ lombok

---

## 🐳 Bước 5: Start Docker Services

### 5.1 Open Terminal Trong IntelliJ

1. **View → Tool Windows → Terminal** (hoặc `Alt+F12`)
2. Verify bạn đang ở folder `java-elasticsearch-demo`
3. Run:

```bash
# Start Docker services
docker-compose up -d

# Check status
docker-compose ps
```

**Expected output:**
```
NAME                     STATUS
elasticsearch            Up
kibana                   Up
postgres                 Up
```

### 5.2 Wait for Services

```bash
# Đợi 30 giây để Elasticsearch khởi động
sleep 30

# Verify Elasticsearch
curl http://localhost:9200
```

**Expected:** JSON response với cluster info

---

## ▶️ Bước 6: Run Application Trong IntelliJ

### Option A: Run Main Class (Recommended)

1. Mở file: `src/main/java/com/demo/search/SearchDemoApplication.java`

2. Click chuột phải vào class `SearchDemoApplication`

3. Chọn **Run 'SearchDemoApplication.main()'**
   - Hoặc click icon ▶️ màu xanh bên cạnh class name
   - Hoặc `Ctrl+Shift+F10` (Windows/Linux) / `Ctrl+Shift+R` (Mac)

4. Đợi application start

**Expected console output:**
```
Started SearchDemoApplication in X.XXX seconds
```

### Option B: Tạo Run Configuration Manually

Nếu không thấy icon ▶️:

1. **Run → Edit Configurations**

2. Click **+** (Add New Configuration)

3. Chọn **Spring Boot**

4. Fill in:
   - **Name:** `SearchDemoApplication`
   - **Main class:** `com.demo.search.SearchDemoApplication`
   - **Use classpath of module:** Chọn `java-elasticsearch-demo.main`
   - **JRE:** Chọn Java 21

5. Click **Apply** → **OK**

6. Click icon ▶️ ở toolbar hoặc `Shift+F10`

---

## 🧪 Bước 7: Verify Application Running

### 7.1 Check Console Logs

Trong **Run tool window**, check:
```
✅ Started SearchDemoApplication
✅ Tomcat started on port(s): 8080
✅ No errors in red
```

### 7.2 Test API

Mở terminal mới trong IntelliJ:

```bash
# Test health
curl http://localhost:8080/api/products

# Expected: [] (empty array - normal, chưa có data)
```

---

## 📊 Bước 8: Create Sample Data

### 8.1 Run Script

Trong terminal của IntelliJ:

```bash
# Cấp quyền
chmod +x scripts/create-sample-data.sh

# Run script
./scripts/create-sample-data.sh
```

### 8.2 Verify Data Created

```bash
# Get all products
curl http://localhost:8080/api/products | jq

# Search test
curl "http://localhost:8080/api/products/search?query=dior"
```

---

## 🎯 Bước 9: Test Fuzzy Search (Demo Feature)

```bash
# Test 1: Typo tolerance
curl "http://localhost:8080/api/products/search?query=sauvaje"
# ↑ Gõ SAI "sauvaje" nhưng VẪN tìm được "Sauvage"!

# Test 2: Autocomplete
curl "http://localhost:8080/api/products/search?query=sau"
# ↑ Chỉ gõ 3 ký tự

# Test 3: Combined filters
curl "http://localhost:8080/api/products/search?query=dior&gender=MALE&minPrice=2000000"
```

---

## 🐛 Troubleshooting

### ❌ Error: "Cannot resolve symbol 'lombok'"

**Giải pháp:**

1. Install Lombok Plugin:
   - **File → Settings → Plugins**
   - Search: **Lombok**
   - Click **Install**
   - Restart IntelliJ

2. Enable Annotation Processing (xem Bước 3)

3. Reload Gradle project

---

### ❌ Error: "Unsupported Java version"

**Giải pháp:**

1. Check Project SDK:
   - **File → Project Structure → Project**
   - SDK phải là **Java 21**

2. Check Gradle JVM:
   - **File → Settings → Build, Execution, Deployment → Build Tools → Gradle**
   - **Gradle JVM:** Chọn **Project SDK (Java 21)**

3. Reload Gradle

---

### ❌ Error: "Connection refused: localhost:5432"

**PostgreSQL không chạy!**

**Giải pháp:**

```bash
# Check Docker
docker-compose ps

# Nếu không có postgres, restart
docker-compose down
docker-compose up -d

# Check logs
docker-compose logs postgres
```

---

### ❌ Error: "Connection refused: localhost:9200"

**Elasticsearch không chạy!**

**Giải pháp:**

```bash
# Check Elasticsearch
curl http://localhost:9200

# Nếu failed:
docker-compose restart elasticsearch

# Wait 30 seconds
sleep 30

# Try again
curl http://localhost:9200
```

---

### ❌ Error: "Port 8080 already in use"

**Giải pháp:**

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change port in application.yml:
# server.port: 8081
```

---

### ❌ Build Failed: "Could not resolve dependencies"

**Giải pháp:**

1. **Check internet connection**

2. **Clear Gradle cache:**
   ```bash
   ./gradlew clean --refresh-dependencies
   ```

3. **Reload Gradle project** trong IntelliJ

4. **Check proxy settings** (nếu ở công ty):
   - **File → Settings → Appearance & Behavior → System Settings → HTTP Proxy**

---

### ❌ Application Starts But Returns Empty Results

**Giải pháp:**

1. **Check Docker services:**
   ```bash
   docker-compose ps
   # All services should be "Up"
   ```

2. **Check Elasticsearch index:**
   ```bash
   curl http://localhost:9200/_cat/indices
   # Should see "products" index
   ```

3. **Re-run sample data script:**
   ```bash
   ./scripts/create-sample-data.sh
   ```

4. **Trigger manual reindex:**
   ```bash
   curl -X POST http://localhost:8080/api/products/reindex
   ```

---

## 🎓 IntelliJ Pro Tips

### Hot Reload (Dev Mode)

Enable auto-restart khi code thay đổi:

1. Add dependency vào `build.gradle`:
   ```gradle
   developmentOnly 'org.springframework.boot:spring-boot-devtools'
   ```

2. **File → Settings → Build, Execution, Deployment → Compiler**
   - ✅ **Build project automatically**

3. **File → Settings → Advanced Settings**
   - ✅ **Allow auto-make to start even if developed application is currently running**

4. Restart application

### Database Tool Window

IntelliJ Ultimate có built-in database tool:

1. **View → Tool Windows → Database**

2. **+** → **Data Source** → **PostgreSQL**

3. Fill in:
   - **Host:** localhost
   - **Port:** 5432
   - **Database:** perfume_db
   - **User:** admin
   - **Password:** admin123

4. Test Connection → OK

### HTTP Client

Test API trực tiếp trong IntelliJ:

1. Tạo file: `test-api.http`

2. Add content:
   ```http
   ### Get all products
   GET http://localhost:8080/api/products

   ### Search with typo
   GET http://localhost:8080/api/products/search?query=sauvaje

   ### Create product
   POST http://localhost:8080/api/products
   Content-Type: application/json

   {
     "name": "Test Product",
     "description": "Test Description",
     "price": 1000000,
     "gender": "UNISEX",
     "sillage": "MODERATE",
     "longevity": "MODERATE"
   }
   ```

3. Click ▶️ icon bên cạnh mỗi request

### Useful Shortcuts

- **Run application:** `Shift+F10`
- **Debug application:** `Shift+F9`
- **Stop application:** `Ctrl+F2`
- **Find class:** `Ctrl+N` / `Cmd+O`
- **Search everywhere:** `Shift Shift` (double Shift)
- **Terminal:** `Alt+F12`
- **Project structure:** `Ctrl+Alt+Shift+S`

---

## 📚 Next Steps

Application đang chạy thành công? Great! 🎉

1. **Import Postman Collection:**
   - File: `postman_collection.json`
   - Test API với GUI

2. **Open Kibana:**
   - URL: http://localhost:5601
   - Visualize Elasticsearch data

3. **Read Demo Guide:**
   - [DEMO_GUIDE.md](DEMO_GUIDE.md) - Full demo scenarios
   - [DEMO_QUICK.md](DEMO_QUICK.md) - Quick 5-min demo

4. **Explore Code:**
   - `ProductService.java` - Business logic
   - `SearchService.java` - Search implementation
   - `ProductController.java` - REST API

---

## 🎬 Ready to Demo

Your IntelliJ setup is complete! Application is running at:

- **API:** http://localhost:8080/api/products
- **Health check:** http://localhost:8080/actuator/health
- **Kibana:** http://localhost:5601
- **Elasticsearch:** http://localhost:9200

**Test fuzzy search now:**
```bash
curl "http://localhost:8080/api/products/search?query=sauvaje"
```

Happy coding! 🚀
