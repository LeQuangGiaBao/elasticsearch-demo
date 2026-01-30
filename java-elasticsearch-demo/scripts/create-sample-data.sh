#!/bin/bash

echo "Creating sample perfume products..."

# Array of famous perfumes
declare -a products=(
  '{"name":"Sauvage Dior","description":"A fresh and powerful fragrance with notes of bergamot and pepper","price":2500000,"gender":"MALE","sillage":"STRONG","longevity":"LONG_LASTING","brand":"Dior","concentration":"EDT"}'
  '{"name":"Bleu de Chanel","description":"Woody aromatic fragrance with citrus notes","price":3000000,"gender":"MALE","sillage":"STRONG","longevity":"LONG_LASTING","brand":"Chanel","concentration":"EDP"}'
  '{"name":"Dior Homme Intense","description":"Intense woody fragrance with iris and lavender","price":2800000,"gender":"MALE","sillage":"MODERATE","longevity":"LONG_LASTING","brand":"Dior","concentration":"EDP"}'
  '{"name":"Acqua di Gio Profumo","description":"Marine aromatic fragrance with incense and patchouli","price":2200000,"gender":"MALE","sillage":"MODERATE","longevity":"MODERATE","brand":"Giorgio Armani","concentration":"EDP"}'
  '{"name":"One Million","description":"Spicy leather fragrance with cinnamon and rose","price":1800000,"gender":"MALE","sillage":"STRONG","longevity":"LONG_LASTING","brand":"Paco Rabanne","concentration":"EDT"}'
  '{"name":"Chanel No.5","description":"Iconic floral aldehyde fragrance","price":3500000,"gender":"FEMALE","sillage":"STRONG","longevity":"LONG_LASTING","brand":"Chanel","concentration":"EDP"}'
  '{"name":"Miss Dior","description":"Fresh floral fragrance with rose and patchouli","price":2700000,"gender":"FEMALE","sillage":"MODERATE","longevity":"MODERATE","brand":"Dior","concentration":"EDT"}'
  '{"name":"Flowerbomb","description":"Floral explosion with orchid and patchouli","price":2600000,"gender":"FEMALE","sillage":"STRONG","longevity":"LONG_LASTING","brand":"Viktor & Rolf","concentration":"EDP"}'
  '{"name":"La Vie Est Belle","description":"Gourmand floral with iris and praline","price":2400000,"gender":"FEMALE","sillage":"MODERATE","longevity":"MODERATE","brand":"Lancome","concentration":"EDP"}'
  '{"name":"Coco Mademoiselle","description":"Fresh oriental fragrance with citrus and patchouli","price":3200000,"gender":"FEMALE","sillage":"STRONG","longevity":"LONG_LASTING","brand":"Chanel","concentration":"EDP"}'
  '{"name":"CK One","description":"Citrus aromatic unisex fragrance","price":1200000,"gender":"UNISEX","sillage":"INTIMATE","longevity":"WEAK","brand":"Calvin Klein","concentration":"EDT"}'
  '{"name":"Le Labo Santal 33","description":"Woody spicy unisex fragrance with sandalwood","price":4500000,"gender":"UNISEX","sillage":"MODERATE","longevity":"LONG_LASTING","brand":"Le Labo","concentration":"EDP"}'
  '{"name":"Tom Ford Black Orchid","description":"Oriental chypre unisex fragrance","price":4200000,"gender":"UNISEX","sillage":"STRONG","longevity":"ETERNAL","brand":"Tom Ford","concentration":"EDP"}'
  '{"name":"Versace Eros","description":"Fresh aromatic fougere for men","price":1900000,"gender":"MALE","sillage":"STRONG","longevity":"MODERATE","brand":"Versace","concentration":"EDT"}'
  '{"name":"Yves Saint Laurent Y","description":"Fresh fougere with bergamot and sage","price":2100000,"gender":"MALE","sillage":"MODERATE","longevity":"MODERATE","brand":"YSL","concentration":"EDT"}'
)

# Create each product
for product in "${products[@]}"; do
  echo "Creating product..."
  curl -X POST http://localhost:8080/api/products \
    -H "Content-Type: application/json" \
    -d "$product" \
    -w "\nHTTP Status: %{http_code}\n\n"
  sleep 0.5
done

echo "Sample data creation completed!"
echo "Total products created: ${#products[@]}"
