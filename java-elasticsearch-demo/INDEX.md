# 📚 Documentation Index

## 🚀 Bắt Đầu Nhanh

### Cho IntelliJ Users (Khuyến nghị!)

1. **[INTELLIJ_QUICK.md](INTELLIJ_QUICK.md)** ⚡ (3 phút)
   - Import project
   - Enable Lombok
   - Run application
   - **Start here nếu dùng IntelliJ!**

2. **[INTELLIJ_SETUP.md](INTELLIJ_SETUP.md)** 📖 (10 phút)
   - Hướng dẫn chi tiết từng bước
   - Screenshots descriptions
   - Pro tips & shortcuts
   - **Đọc nếu gặp vấn đề với IntelliJ**

3. **[TROUBLESHOOTING_INTELLIJ.md](TROUBLESHOOTING_INTELLIJ.md)** 🔧
   - Common errors & solutions
   - Debug checklist
   - Verify everything works
   - **Đọc khi gặp lỗi**

---

### Cho Terminal/Command Line Users

1. **[START_HERE.md](START_HERE.md)** 🎬 (5 phút)
   - Giải quyết lỗi "no configuration file provided"
   - Maven vs Gradle setup
   - Quick commands
   - **Bắt đầu từ đây nếu không dùng IntelliJ**

2. **[SETUP.md](SETUP.md)** ⚙️ (10 phút)
   - Setup Maven/Gradle wrapper
   - Troubleshooting chi tiết
   - All options explained
   - **Đọc nếu cần setup từ đầu**

---

## 🎯 Demo & Testing

1. **[DEMO_QUICK.md](DEMO_QUICK.md)** ⚡ (5 phút)
   - Demo nhanh fuzzy search
   - Basic testing scenarios
   - Quick verification
   - **Perfect cho demo nhanh**

2. **[DEMO_GUIDE.md](DEMO_GUIDE.md)** 📋 (15 phút)
   - Demo chi tiết từng feature
   - Explain cho non-tech audience
   - Advanced scenarios
   - Postman collection usage
   - **Dùng cho presentation đầy đủ**

---

## 📖 Documentation Đầy Đủ

1. **[README.md](README.md)** 📚
   - Overview project
   - Architecture
   - Full API documentation
   - Tech stack details
   - **Main documentation file**

2. **[ARCHITECTURE.md](ARCHITECTURE.md)** 🏗️
   - System design
   - Component interaction
   - Data flow
   - Technical decisions
   - **Cho technical audience**

3. **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** 📊
   - High-level overview
   - Features summary
   - Key highlights
   - **Executive summary**

---

## 🔧 Setup Files

1. **[QUICKSTART.md](QUICKSTART.md)**
   - Minimal setup steps
   - Get running ASAP

2. **[FILES_CHECKLIST.md](FILES_CHECKLIST.md)**
   - All files in project
   - File purposes
   - Verification checklist

---

## 🛠️ Configuration Files

| File | Purpose |
|------|---------|
| `docker-compose.yml` | Docker services config |
| `build.gradle` | Gradle build config |
| `pom.xml` | Maven build config |
| `application.yml` | Spring Boot config |
| `application-test.yml` | Test environment config |
| `.env.example` | Environment variables template |

---

## 📜 Scripts

| File | Purpose |
|------|---------|
| `scripts/create-sample-data.sh` | Create demo products |
| `scripts/test-search.sh` | Test search functionality |
| `gradlew` | Gradle wrapper (Unix) |
| `mvnw` | Maven wrapper (Unix) |

---

## 🎓 Recommended Reading Order

### Scenario 1: "Tôi dùng IntelliJ, muốn chạy ngay"

```
1. INTELLIJ_QUICK.md (3 min)
2. DEMO_QUICK.md (5 min)
3. Done! ✅
```

---

### Scenario 2: "Tôi dùng terminal, không dùng IDE"

```
1. START_HERE.md (5 min)
2. SETUP.md (if needed)
3. DEMO_QUICK.md (5 min)
4. Done! ✅
```

---

### Scenario 3: "Tôi gặp lỗi khi run"

**Dùng IntelliJ:**
```
1. TROUBLESHOOTING_INTELLIJ.md
2. Check specific error
3. Follow solution
```

**Dùng Terminal:**
```
1. SETUP.md → Troubleshooting section
2. START_HERE.md → Still Having Issues
```

---

### Scenario 4: "Tôi cần demo cho team"

```
1. DEMO_GUIDE.md (setup & practice)
2. ARCHITECTURE.md (understand design)
3. postman_collection.json (import & use)
4. Ready to present! 🎉
```

---

### Scenario 5: "Tôi cần hiểu technical details"

```
1. README.md → Overview
2. ARCHITECTURE.md → Deep dive
3. Source code walkthrough:
   - SearchService.java
   - ProductService.java
   - ProductController.java
```

---

## 🎯 Quick Reference

### URLs

- **Application:** http://localhost:8080
- **API Base:** http://localhost:8080/api/products
- **Elasticsearch:** http://localhost:9200
- **Kibana:** http://localhost:5601

### Commands

```bash
# Start Docker
docker-compose up -d

# Run (Maven)
./mvnw spring-boot:run

# Run (Gradle)
./gradlew bootRun

# Create data
./scripts/create-sample-data.sh

# Test search
curl "http://localhost:8080/api/products/search?query=dior"

# Stop
docker-compose down
```

---

## 🆘 Where to Get Help

| Issue | Read |
|-------|------|
| Can't start IntelliJ | [INTELLIJ_SETUP.md](INTELLIJ_SETUP.md) |
| IntelliJ errors | [TROUBLESHOOTING_INTELLIJ.md](TROUBLESHOOTING_INTELLIJ.md) |
| Terminal setup issues | [SETUP.md](SETUP.md) |
| General errors | [START_HERE.md](START_HERE.md) |
| Need to demo | [DEMO_GUIDE.md](DEMO_GUIDE.md) |
| API questions | [README.md](README.md) |
| Architecture questions | [ARCHITECTURE.md](ARCHITECTURE.md) |

---

## 📊 Documentation Map

```
Root Documentation
│
├── 🚀 Getting Started
│   ├── IntelliJ Path
│   │   ├── INTELLIJ_QUICK.md (Start here!)
│   │   ├── INTELLIJ_SETUP.md (Details)
│   │   └── TROUBLESHOOTING_INTELLIJ.md (Errors)
│   │
│   └── Terminal Path
│       ├── START_HERE.md (Start here!)
│       └── SETUP.md (Details)
│
├── 🎯 Demo & Testing
│   ├── DEMO_QUICK.md (5 min demo)
│   └── DEMO_GUIDE.md (Full demo)
│
├── 📖 Reference
│   ├── README.md (Main docs)
│   ├── ARCHITECTURE.md (Technical)
│   └── PROJECT_SUMMARY.md (Overview)
│
└── 🛠️ Utilities
    ├── QUICKSTART.md
    ├── FILES_CHECKLIST.md
    └── INDEX.md (This file)
```

---

## 🎓 Tips for Readers

1. **Don't read everything!** Pick the path that matches your situation
2. **Start with Quick guides** - Get running first, learn details later
3. **Use Ctrl+F** - Search for your specific error message
4. **Follow links** - Docs cross-reference each other
5. **Try commands** - Best way to learn is by doing

---

## 📝 Document Status

| File | Status | Last Updated |
|------|--------|--------------|
| INTELLIJ_QUICK.md | ✅ Complete | Latest |
| INTELLIJ_SETUP.md | ✅ Complete | Latest |
| TROUBLESHOOTING_INTELLIJ.md | ✅ Complete | Latest |
| START_HERE.md | ✅ Complete | Latest |
| SETUP.md | ✅ Complete | Latest |
| DEMO_QUICK.md | ✅ Complete | Latest |
| DEMO_GUIDE.md | ✅ Complete | Latest |
| README.md | ✅ Complete | Latest |
| ARCHITECTURE.md | ✅ Complete | Latest |

---

**Happy coding! 🚀**

Need help? Start with the appropriate Quick guide above!
