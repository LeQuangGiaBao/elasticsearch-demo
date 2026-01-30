# Architecture Overview

## System Architecture

```
┌─────────────┐
│   Client    │
│ (Postman/   │
│  Browser)   │
└──────┬──────┘
       │ HTTP/REST
       ▼
┌──────────────────────────────────────┐
│     Spring Boot Application          │
│  ┌────────────────────────────────┐  │
│  │   Controller Layer             │  │
│  │  - ProductController           │  │
│  └────────────┬───────────────────┘  │
│               │                      │
│  ┌────────────▼───────────────────┐  │
│  │   Service Layer                │  │
│  │  - ProductService (Business)   │  │
│  │  - IndexService (Indexing)     │  │
│  │  - SearchService (Search)      │  │
│  └─────┬──────────────────┬───────┘  │
│        │                  │          │
│  ┌─────▼──────┐    ┌─────▼────────┐ │
│  │ Repository │    │ ES Repository│ │
│  │   (JPA)    │    │ (Elastic)    │ │
│  └─────┬──────┘    └─────┬────────┘ │
└────────┼─────────────────┼──────────┘
         │                 │
    ┌────▼─────┐    ┌─────▼──────────┐
    │PostgreSQL│    │ Elasticsearch  │
    │          │    │                │
    └──────────┘    └────────────────┘
```

## Data Flow

### 1. Create Product Flow

```
User Request
    ↓
ProductController.create()
    ↓
ProductService.create()
    ├→ ProductRepository.save()  → PostgreSQL
    └→ IndexService.index()      → Elasticsearch
    ↓
Response to User
```

### 2. Search Flow

```
User Search Request
    ↓
ProductController.search()
    ↓
SearchService.search()
    ├→ Build BoolQuery (prefix/wildcard/fuzzy)
    ├→ Add Filters (price/gender/etc)
    └→ ElasticsearchOperations.search()
    ↓
Elasticsearch Query
    ↓
Results returned to User
```

### 3. Update Product Flow

```
User Update Request
    ↓
ProductController.update()
    ↓
ProductService.update()
    ├→ Find existing Product
    ├→ Update fields
    ├→ ProductRepository.save()  → PostgreSQL
    └→ IndexService.index()      → Elasticsearch (Re-index)
    ↓
Response to User
```

## Component Details

### Controller Layer

**Responsibilities:**
- HTTP request/response handling
- Input validation
- DTO conversion
- Error handling

**Key Classes:**
- `ProductController` - REST endpoints for product operations

### Service Layer

**ProductService:**
- Business logic for CRUD operations
- Transaction management
- Orchestrates between Repository and IndexService

**IndexService:**
- Manages Elasticsearch indexing
- Handles index creation/deletion
- Bulk reindexing operations

**SearchService:**
- Builds complex Elasticsearch queries
- Implements search strategies (prefix/wildcard/fuzzy)
- Applies filters and pagination

### Repository Layer

**ProductRepository (JPA):**
- PostgreSQL data access
- CRUD operations
- Custom queries

**ProductSearchRepository (Elasticsearch):**
- Elasticsearch document operations
- Index management

### Domain Model

**Product (Entity):**
- PostgreSQL table mapping
- JPA annotations
- Lifecycle hooks

**ProductDocument:**
- Elasticsearch document mapping
- Field type definitions
- Analyzers configuration

## Design Patterns

### 1. Repository Pattern
```java
ProductRepository (Interface)
    ↓
Spring Data JPA Implementation
    ↓
Database Access
```

### 2. Service Layer Pattern
```java
Controller → Service → Repository
```

### 3. DTO Pattern
```java
Entity ↔ DTO ↔ Controller
```

### 4. Factory Pattern
```java
ProductDocument.fromEntity(Product)
ProductDto.fromEntity(Product)
```

## Search Strategy

### Query Boosting

1. **Prefix Match** (boost = 3.0)
   - Highest priority
   - Used for autocomplete
   - Example: "sau" matches "Sauvage"

2. **Wildcard Match** (boost = 2.0)
   - Medium priority
   - Substring matching
   - Example: "*vage*" matches "Sauvage"

3. **Fuzzy Match** (boost = 1.0)
   - Lowest priority
   - Typo tolerance
   - Example: "sauvaje" matches "Sauvage"

### Filter Strategy

Filters are applied using `must` clause:
- `range` for price filtering
- `term` for exact matching (gender, sillage, etc.)

```json
{
  "bool": {
    "should": [/* boost queries */],
    "filter": [/* strict filters */],
    "minimum_should_match": 1
  }
}
```

## Synchronization Strategy

### Direct Sync (Current Implementation)

```
Write to PostgreSQL → Immediate Index to Elasticsearch
```

**Pros:**
- Simple implementation
- Immediate consistency
- Easy debugging

**Cons:**
- Blocking operation
- Performance impact if Elasticsearch is slow
- No retry mechanism

### Production Alternatives

#### 1. Async Sync
```java
@Async
public CompletableFuture<Void> indexProduct(Product product) {
    // Non-blocking indexing
}
```

#### 2. Event-Driven (Kafka)
```
Write to PostgreSQL
    ↓
Publish Event to Kafka
    ↓
Consumer reads event
    ↓
Index to Elasticsearch
```

#### 3. CDC (Change Data Capture)
```
PostgreSQL → Debezium → Kafka → Elasticsearch Sink
```

## Error Handling

### Strategy

1. **Controller Level:**
   - `@RestControllerAdvice`
   - Global exception handling
   - HTTP status mapping

2. **Service Level:**
   - Business exception throwing
   - Transaction rollback
   - Logging

3. **Repository Level:**
   - Data access exceptions
   - Connection errors

### Recovery

**Elasticsearch Failures:**
- Log error
- Continue with PostgreSQL operation
- Manual reindex endpoint available

**PostgreSQL Failures:**
- Throw exception
- Rollback transaction
- Return error to client

## Performance Considerations

### Database

1. **Indexing:**
```sql
CREATE INDEX idx_product_name ON products(name);
CREATE INDEX idx_product_price ON products(price);
```

2. **Connection Pool:**
```yaml
spring.datasource.hikari:
  maximum-pool-size: 10
  minimum-idle: 5
```

### Elasticsearch

1. **Bulk Operations:**
```java
searchRepository.saveAll(documents);
```

2. **Query Optimization:**
- Use filters over queries when possible
- Limit result size
- Implement pagination

### Caching

**Potential Implementation:**
```java
@Cacheable("popular-searches")
public List<Product> search(SearchRequest request) {
    // Cache frequent searches
}
```

## Security

### Current Implementation

- No authentication (demo purposes)
- Input validation with `@Valid`
- SQL injection prevention (JPA)

### Production Requirements

1. **Authentication:**
   - Spring Security
   - JWT tokens

2. **Authorization:**
   - Role-based access control (RBAC)
   - Method-level security

3. **Data Protection:**
   - HTTPS only
   - Sensitive data encryption
   - Rate limiting

## Scalability

### Horizontal Scaling

**Application:**
- Stateless design
- Deploy multiple instances
- Load balancer

**Database:**
- Read replicas
- Connection pooling
- Query optimization

**Elasticsearch:**
- Multi-node cluster
- Sharding strategy
- Replica configuration

### Vertical Scaling

- Increase JVM heap size
- More CPU cores
- Better I/O performance

## Monitoring

### Application Metrics

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
```

### Elasticsearch Metrics

- Query performance
- Index size
- Shard health

### Database Metrics

- Connection pool usage
- Query execution time
- Slow query log

## Testing Strategy

### Unit Tests

- Service layer logic
- DTO conversions
- Utility methods

### Integration Tests

- Repository operations
- Elasticsearch queries
- End-to-end flows

### Test Containers

```java
@Container
static PostgreSQLContainer<?> postgres = ...

@Container
static ElasticsearchContainer elasticsearch = ...
```

## Deployment

### Docker

```bash
docker-compose up -d
./gradlew bootRun
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: elasticsearch-demo
spec:
  replicas: 3
  ...
```

### Cloud Platforms

- AWS: ECS/EKS + RDS + OpenSearch
- Azure: AKS + Azure Database + Azure Search
- GCP: GKE + Cloud SQL + Elastic Cloud
