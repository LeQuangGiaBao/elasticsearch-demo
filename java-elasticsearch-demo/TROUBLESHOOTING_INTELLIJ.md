# 🔧 IntelliJ Troubleshooting - Giải Quyết Lỗi

## 🎯 Checklist Nhanh - Check Theo Thứ Tự

### ✅ 1. Java SDK

```
File → Project Structure → Project
  → SDK: Phải là Java 21
  → Language level: 21
```

**Verify:**
```bash
java -version
# Phải show: java version "21"
```

---

### ✅ 2. Annotation Processing (Cho Lombok)

```
File → Settings/Preferences
  → Build, Execution, Deployment
    → Compiler
      → Annotation Processors
        ✅ Enable annotation processing
```

**Test:** Nếu không có, code sẽ báo lỗi:
- `Cannot resolve symbol 'log'`
- `Cannot find symbol method getData()`
- `@Data`, `@Getter`, `@Setter` không work

---

### ✅ 3. Lombok Plugin

```
File → Settings → Plugins
  → Search: "Lombok"
  → Click Install (nếu chưa có)
  → Restart IntelliJ
```

---

### ✅ 4. Gradle Sync

```
View → Tool Windows → Gradle
  → Click icon ⟳ (Reload All Gradle Projects)
```

**Wait for:** "BUILD SUCCESSFUL"

---

### ✅ 5. Docker Services Running

```bash
docker-compose ps
```

**Expected output:**
```
NAME              STATUS
elasticsearch     Up
kibana            Up
postgres          Up
```

**If not:**
```bash
docker-compose up -d
sleep 30
```

---

### ✅ 6. PostgreSQL Connection

```bash
# Test connection
docker exec -it perfume-postgres psql -U admin -d perfume_db

# Should enter psql shell
# Type: \q to exit
```

**Or test via curl:**
```bash
psql -h localhost -p 5432 -U admin -d perfume_db
# Password: admin123
```

---

### ✅ 7. Elasticsearch Running

```bash
curl http://localhost:9200
```

**Expected:** JSON response with cluster info

**If failed:**
```bash
docker-compose logs elasticsearch
docker-compose restart elasticsearch
sleep 30
curl http://localhost:9200
```

---

### ✅ 8. Application.yml Correct

Check: `src/main/resources/application.yml`

Should have:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/perfume_db
    username: admin
    password: admin123

  elasticsearch:
    uris: http://localhost:9200

server:
  port: 8080
```

---

## 🔥 Common Errors & Solutions

### ❌ Error 1: "Cannot resolve symbol 'lombok'"

**Cause:** Lombok plugin or annotation processing not enabled

**Solution:**
1. **Install Lombok plugin** (Settings → Plugins)
2. **Enable annotation processing** (Settings → Annotation Processors)
3. **Reload Gradle**
4. **Restart IntelliJ**

---

### ❌ Error 2: "java: error: release version 21 not available"

**Cause:** Wrong Java version

**Solution:**

1. **Check installed Java:**
   ```bash
   /usr/libexec/java_home -V
   # or: java -version
   ```

2. **Download Java 21** if not found:
   - In IntelliJ: `File → Project Structure → Add SDK → Download JDK`
   - Or from: https://adoptium.net/

3. **Set in IntelliJ:**
   ```
   File → Project Structure → Project → SDK: Java 21
   ```

4. **Set Gradle JVM:**
   ```
   File → Settings → Build Tools → Gradle
     → Gradle JVM: Project SDK (21)
   ```

---

### ❌ Error 3: "Connection refused: connect to localhost:5432"

**Cause:** PostgreSQL not running

**Solution:**

```bash
# Check Docker
docker-compose ps postgres

# If not Up:
docker-compose up -d postgres

# Check logs
docker-compose logs postgres

# Test connection
psql -h localhost -p 5432 -U admin -d perfume_db
```

**Still failed?** Check port conflict:
```bash
lsof -i :5432
# Kill if something else using port
```

---

### ❌ Error 4: "Connection refused: localhost:9200"

**Cause:** Elasticsearch not ready

**Solution:**

```bash
# Check status
docker-compose ps elasticsearch

# Check logs
docker-compose logs elasticsearch

# Wait longer (Elasticsearch takes time)
sleep 30

# Restart if needed
docker-compose restart elasticsearch
sleep 30

# Test
curl http://localhost:9200
```

**Common Elasticsearch issues:**
- Takes 30-60 seconds to fully start
- Needs minimum 512MB RAM
- Check Docker Desktop memory settings

---

### ❌ Error 5: "Port 8080 already in use"

**Cause:** Another app using port 8080

**Solution:**

```bash
# Find what's using 8080
lsof -i :8080

# Kill it
kill -9 <PID>

# Or change port in application.yml:
server:
  port: 8081
```

---

### ❌ Error 6: "Could not resolve all dependencies"

**Cause:** Network/internet issue, Gradle cache problem

**Solution:**

```bash
# Clear Gradle cache
./gradlew clean --refresh-dependencies

# In IntelliJ, reload Gradle
View → Tool Windows → Gradle → ⟳ Reload
```

**Behind corporate proxy?**
```
File → Settings → HTTP Proxy
  → Manual proxy configuration
  → Enter your proxy details
```

---

### ❌ Error 7: "HikariPool-1 - Exception during pool initialization"

**Cause:** Database config wrong or DB not available

**Solution:**

1. **Check docker-compose.yml:**
   ```yaml
   postgres:
     environment:
       POSTGRES_DB: perfume_db
       POSTGRES_USER: admin
       POSTGRES_PASSWORD: admin123
   ```

2. **Check application.yml matches**

3. **Recreate database:**
   ```bash
   docker-compose down -v
   docker-compose up -d
   sleep 30
   ```

---

### ❌ Error 8: "NoSuchMethodError" or "NoClassDefFoundError"

**Cause:** Dependency version conflict, corrupted cache

**Solution:**

```bash
# Clean everything
./gradlew clean

# Delete .gradle folder
rm -rf ~/.gradle/caches

# Rebuild
./gradlew build --refresh-dependencies
```

**In IntelliJ:**
```
File → Invalidate Caches / Restart
  → Invalidate and Restart
```

---

### ❌ Error 9: Application Starts But Search Returns Empty

**Cause:** Data not indexed to Elasticsearch

**Solution:**

1. **Check if products exist in PostgreSQL:**
   ```bash
   curl http://localhost:8080/api/products
   # Should return array of products
   ```

2. **Check Elasticsearch index:**
   ```bash
   curl http://localhost:9200/products/_search?pretty
   ```

3. **If empty, trigger reindex:**
   ```bash
   curl -X POST http://localhost:8080/api/products/reindex
   ```

4. **Or create sample data:**
   ```bash
   ./scripts/create-sample-data.sh
   ```

---

### ❌ Error 10: "Spring Boot Application Main Class Not Found"

**Cause:** Run configuration not set correctly

**Solution:**

1. **Delete old run config:**
   ```
   Run → Edit Configurations
     → Select old config → Delete (-)
   ```

2. **Create new:**
   ```
   Open: SearchDemoApplication.java
   Right-click class name
     → Run 'SearchDemoApplication.main()'
   ```

3. **Or create manually:**
   ```
   Run → Edit Configurations → + → Spring Boot
     Main class: com.demo.search.SearchDemoApplication
     Module: java-elasticsearch-demo.main
     JRE: 21
   ```

---

## 🔍 Debug Mode

### Enable Debug Logging

Edit `application.yml`:

```yaml
logging:
  level:
    com.demo.search: DEBUG
    org.springframework.data.elasticsearch: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

### Check Application Logs

**In IntelliJ Run window:**
- Look for errors in red
- Check "Caused by:" for root cause
- Search for "ERROR" or "WARN"

**Key logs to check:**
```
✅ Started SearchDemoApplication in X seconds
✅ Tomcat started on port(s): 8080
✅ HikariPool-1 - Start completed
✅ Elasticsearch cluster info: ...
```

---

## 🧪 Verify Everything Works

### Test Checklist

```bash
# 1. Docker services
docker-compose ps
# All should be "Up"

# 2. PostgreSQL
psql -h localhost -p 5432 -U admin -d perfume_db
# Should connect (password: admin123)

# 3. Elasticsearch
curl http://localhost:9200
# Should return JSON

# 4. Application health
curl http://localhost:8080/api/products
# Should return array ([] if no data, that's ok)

# 5. Create test product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "description": "Test",
    "price": 1000000,
    "gender": "UNISEX",
    "sillage": "MODERATE",
    "longevity": "MODERATE"
  }'

# 6. Search immediately
curl "http://localhost:8080/api/products/search?query=test"
# Should find the product

# 7. Test fuzzy search
curl "http://localhost:8080/api/products/search?query=tets"
# Should still find "test" (typo tolerance)
```

---

## 🎯 Still Not Working?

### Collect Debug Info

```bash
# Java version
java -version

# Docker version
docker --version
docker-compose --version

# Docker services
docker-compose ps

# Docker logs
docker-compose logs --tail=50

# Elasticsearch health
curl http://localhost:9200/_cluster/health?pretty

# PostgreSQL connection
docker exec perfume-postgres pg_isready

# Check ports
lsof -i :8080
lsof -i :5432
lsof -i :9200
```

### Nuclear Option: Clean Everything

```bash
# Stop everything
docker-compose down -v

# Clean Gradle
./gradlew clean

# Remove containers and volumes
docker system prune -a --volumes

# Start fresh
docker-compose up -d
sleep 60

# Rebuild
./gradlew clean build

# Run
./gradlew bootRun
```

---

## 📚 Additional Resources

- **IntelliJ Setup:** [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)
- **Quick Start:** [INTELLIJ_QUICK.md](INTELLIJ_QUICK.md)
- **General Setup:** [SETUP.md](SETUP.md)
- **Demo Guide:** [DEMO_GUIDE.md](DEMO_GUIDE.md)

---

## 💬 Debug Tips

1. **Read error messages carefully** - Usually tells you exactly what's wrong
2. **Check logs** - Docker logs, application logs
3. **One thing at a time** - Fix one error before moving to next
4. **Google error message** - Likely someone else had same issue
5. **Check versions** - Java 21, Spring Boot 3.2.1, Elasticsearch 8.11

---

**Need more help?** Check full documentation in README.md
