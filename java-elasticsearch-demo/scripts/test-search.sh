#!/bin/bash

echo "=== Testing Elasticsearch Search ==="
echo ""

echo "1. Basic text search (query=dior):"
curl -s "http://localhost:8080/api/products/search?query=dior" | python3 -m json.tool
echo ""
echo ""

echo "2. Fuzzy search with typo (query=sauvaje):"
curl -s "http://localhost:8080/api/products/search?query=sauvaje" | python3 -m json.tool
echo ""
echo ""

echo "3. Price range filter (2000000-3000000):"
curl -s "http://localhost:8080/api/products/search?minPrice=2000000&maxPrice=3000000" | python3 -m json.tool
echo ""
echo ""

echo "4. Gender filter (MALE):"
curl -s "http://localhost:8080/api/products/search?gender=MALE" | python3 -m json.tool
echo ""
echo ""

echo "5. Combined filters (query=chanel&gender=FEMALE):"
curl -s "http://localhost:8080/api/products/search?query=chanel&gender=FEMALE" | python3 -m json.tool
echo ""
echo ""

echo "6. Sillage filter (STRONG):"
curl -s "http://localhost:8080/api/products/search?sillage=STRONG" | python3 -m json.tool
echo ""
echo ""

echo "=== Search tests completed ==="
