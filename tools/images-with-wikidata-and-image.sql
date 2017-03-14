-- Usage: sqlite3 all.sqlitedb < images-with-wikidata-and-image.sql
SELECT wikidata, image from wikivoyage_listings WHERE wikidata != '' AND image != '';
