# 🏗️ Build Project Với Maven

## ⚠️ Yêu Cầu

Để build project bằng Maven, bạn cần có:
- **Java 21** (JDK)
- **Maven 3.9+** (optional nếu dùng wrapper)

---

## 🚀 Option 1: Build Trong IntelliJ (Khuyến Nghị!)

### Bước 1: Mở Maven Tool Window

```
View → Tool Windows → Maven
```

### Bước 2: Run Maven Goals

Trong Maven tool window:
```
java-elasticsearch-demo
  └── Lifecycle
      ├── clean (double-click)
      └── package (double-click)
```

Hoặc sử dụng Maven toolbar:
1. Click icon ▶️ **Execute Maven Goal**
2. Gõ: `clean package -DskipTests`
3. Enter

### Bước 3: Verify Build

Check console output:
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

Build artifacts sẽ ở:
```
target/elasticsearch-demo-0.0.1-SNAPSHOT.jar
```

---

## 🐳 Option 2: Build Với Docker (Không Cần Cài Java!)

### Bước 1: Tạo Dockerfile Build

```bash
cd java-elasticsearch-demo
```

Tạo file `Dockerfile.build`:

```dockerfile
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests
```

### Bước 2: Build Bằng Docker

```bash
# Build Docker image
docker build -f Dockerfile.build -t java-elasticsearch-demo-builder .

# Extract JAR file
docker create --name temp-builder java-elasticsearch-demo-builder
docker cp temp-builder:/app/target/elasticsearch-demo-0.0.1-SNAPSHOT.jar ./target/
docker rm temp-builder
```

### Bước 3: Verify

```bash
ls -lh target/*.jar
# Should see: elasticsearch-demo-0.0.1-SNAPSHOT.jar
```

---

## 🖥️ Option 3: Build Với Maven CLI (Cần Cài Maven)

### Nếu Bạn Đã Cài Maven:

```bash
# Check Maven installed
mvn --version

# Build
cd java-elasticsearch-demo
mvn clean package -DskipTests
```

### Nếu Chưa Cài Maven:

**MacOS:**
```bash
brew install maven
```

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install maven
```

**Windows:**
1. Download từ: https://maven.apache.org/download.cgi
2. Extract vào `C:\Program Files\Maven`
3. Add to PATH: `C:\Program Files\Maven\bin`

---

## 🔧 Option 4: Sử dụng Maven Wrapper (mvnw)

Project đã có Maven wrapper script. Bạn cần có Java 21:

```bash
# Cấp quyền
chmod +x mvnw

# Build
./mvnw clean package -DskipTests
```

**Nếu lỗi "JAVA_HOME not found":**

```bash
# Set JAVA_HOME (MacOS/Linux)
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Hoặc (Linux)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk

# Verify
echo $JAVA_HOME
java -version

# Build lại
./mvnw clean package -DskipTests
```

---

## ✅ Verify Build Thành Công

### Check Build Output

```bash
ls -lh target/

# Expected files:
# elasticsearch-demo-0.0.1-SNAPSHOT.jar
# elasticsearch-demo-0.0.1-SNAPSHOT.jar.original
# classes/
# maven-status/
```

### Run JAR File

```bash
# Make sure Docker services running
docker-compose up -d

# Run the JAR
java -jar target/elasticsearch-demo-0.0.1-SNAPSHOT.jar
```

**Expected console:**
```
Started SearchDemoApplication in X.XXX seconds
```

---

## 🎯 Maven Commands Cheat Sheet

```bash
# Clean build artifacts
mvn clean

# Compile only
mvn compile

# Run tests
mvn test

# Package without tests
mvn package -DskipTests

# Clean + package
mvn clean package -DskipTests

# Install to local repo
mvn clean install -DskipTests

# Show dependencies
mvn dependency:tree

# Download all dependencies
mvn dependency:resolve

# Update dependencies
mvn clean install -U
```

---

## 🐛 Troubleshooting

### ❌ Error: "mvn: command not found"

**Solution:** Maven chưa được cài hoặc không có trong PATH

```bash
# Check PATH
echo $PATH

# Try which
which mvn

# Install Maven (see Option 3 above)
```

---

### ❌ Error: "JAVA_HOME is not defined correctly"

**Solution:**

```bash
# Find Java installation
/usr/libexec/java_home -V   # MacOS
update-alternatives --list java  # Linux

# Set JAVA_HOME
export JAVA_HOME=/path/to/java-21

# Add to ~/.bashrc or ~/.zshrc to make permanent
echo 'export JAVA_HOME=/path/to/java-21' >> ~/.bashrc
source ~/.bashrc
```

---

### ❌ Error: "Failed to execute goal ... compilation failure"

**Causes:**
- Wrong Java version
- Missing dependencies
- Syntax errors in code

**Solution:**

```bash
# Verify Java version
java -version
# Must be Java 21

# Clean and rebuild
mvn clean install -U -DskipTests

# If still fails, check error logs carefully
```

---

### ❌ Error: "Connection refused: localhost:5432" During Build

**Solution:**

Build không cần database running nếu skip tests:

```bash
mvn clean package -DskipTests
```

Nếu muốn run tests:
```bash
# Start Docker first
docker-compose up -d
sleep 30

# Then build with tests
mvn clean package
```

---

### ❌ Error: "Could not resolve dependencies"

**Solution:**

```bash
# Clear local Maven repo
rm -rf ~/.m2/repository

# Rebuild
mvn clean install -U
```

**Or specific packages:**
```bash
rm -rf ~/.m2/repository/org/springframework
rm -rf ~/.m2/repository/co/elastic
mvn clean package -DskipTests
```

---

## 🎓 Maven vs Gradle

Project này support cả hai:

| Feature | Maven | Gradle |
|---------|-------|--------|
| Config file | `pom.xml` | `build.gradle` |
| Build command | `mvn package` | `gradle build` |
| Wrapper | `./mvnw` | `./gradlew` |
| Speed | Slower | Faster |
| IDE support | Excellent | Excellent |

**Choose Maven if:**
- Bạn quen với Maven
- Team dùng Maven standard
- Cần XML config explicit

**Choose Gradle if:**
- Muốn build nhanh hơn
- Prefer DSL syntax (Groovy/Kotlin)
- Cần flexibility cao

---

## 📊 Build Artifacts Explained

Sau khi build thành công, trong `target/`:

| File | Purpose |
|------|---------|
| `*.jar` | Executable JAR file (Spring Boot fat JAR) |
| `*.jar.original` | Original JAR without dependencies |
| `classes/` | Compiled .class files |
| `maven-status/` | Build metadata |
| `maven-archiver/` | JAR metadata |

**Main artifact:**
```
elasticsearch-demo-0.0.1-SNAPSHOT.jar
```

This is a "fat JAR" containing:
- Your application code
- All dependencies
- Embedded Tomcat server

Can run standalone:
```bash
java -jar target/elasticsearch-demo-0.0.1-SNAPSHOT.jar
```

---

## 🚀 Next Steps

Build thành công? Great!

### Run Application

```bash
# Start Docker services
docker-compose up -d

# Run JAR
java -jar target/elasticsearch-demo-0.0.1-SNAPSHOT.jar
```

### Or Run in IntelliJ

```
Open: SearchDemoApplication.java
Click: ▶️ Run
```

### Create Sample Data

```bash
./scripts/create-sample-data.sh
```

### Test API

```bash
curl "http://localhost:8080/api/products/search?query=dior"
```

---

## 📚 Additional Resources

- **Maven Official Docs:** https://maven.apache.org/guides/
- **Spring Boot with Maven:** https://spring.io/guides/gs/maven/
- **IntelliJ Maven Support:** https://www.jetbrains.com/help/idea/maven-support.html

---

**Happy building! 🎉**
