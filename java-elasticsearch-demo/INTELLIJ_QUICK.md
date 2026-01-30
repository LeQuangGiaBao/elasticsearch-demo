# ⚡ IntelliJ Quick Start - 3 Phút

## 🎯 Nhanh Nhất: 3 Bước

### 1️⃣ Import Project (30 giây)

```
File → Open → Chọn folder java-elasticsearch-demo → OK
```

Chọn **Trust Project** khi được hỏi

---

### 2️⃣ Enable Lombok (30 giây)

**Settings/Preferences** (`Ctrl+Alt+S` / `Cmd+,`)

```
Build, Execution, Deployment
  → Compiler
    → Annotation Processors
      ✅ Enable annotation processing
```

**Apply → OK**

---

### 3️⃣ Start Docker & Run (2 phút)

**Terminal trong IntelliJ** (`Alt+F12`):

```bash
# Start Docker
docker-compose up -d
sleep 30

# Verify
docker-compose ps
```

**Run Application:**

1. Mở: `src/main/java/com/demo/search/SearchDemoApplication.java`
2. Click ▶️ icon bên cạnh class name
3. Hoặc: `Ctrl+Shift+F10` / `Ctrl+Shift+R`

**Đợi console show:**
```
Started SearchDemoApplication in X seconds
```

---

## ✅ Test Ngay

**Terminal mới:**

```bash
# Create sample data
chmod +x scripts/create-sample-data.sh
./scripts/create-sample-data.sh

# Test fuzzy search
curl "http://localhost:8080/api/products/search?query=sauvaje"
```

**Expected:** Tìm được "Sauvage Dior" dù gõ sai! 🎉

---

## 🐛 Gặp Lỗi?

### ❌ "Cannot resolve symbol 'lombok'"

**Fix nhanh:**

1. **Settings → Plugins**
2. Search: **Lombok** → **Install**
3. **Restart IntelliJ**
4. **Reload Gradle** (icon ⟳ trong Gradle tool window)

---

### ❌ "Connection refused: localhost:5432"

**Docker chưa chạy!**

```bash
docker-compose up -d
sleep 30
```

---

### ❌ "Port 8080 already in use"

**Kill process:**

```bash
lsof -i :8080
kill -9 <PID>
```

---

### ❌ "Unsupported Java version"

**Set Java 21:**

```
File → Project Structure
  → Project
    → SDK: Java 21
```

Nếu không có Java 21:
```
Add SDK → Download JDK → Version 21 → Download
```

---

## 📚 Need More Help?

- **Hướng dẫn chi tiết:** [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)
- **Troubleshooting:** [SETUP.md](SETUP.md)
- **Demo guide:** [DEMO_GUIDE.md](DEMO_GUIDE.md)

---

## 🎯 Verification Checklist

Trước khi demo, check:

- [ ] Console shows: "Started SearchDemoApplication"
- [ ] `curl http://localhost:8080/api/products` works
- [ ] Docker: `docker-compose ps` all services "Up"
- [ ] Sample data: `curl http://localhost:8080/api/products` returns array

---

**All green?** Go demo! 🚀

**Test fuzzy search:**
```bash
curl "http://localhost:8080/api/products/search?query=sauvaje"
```
