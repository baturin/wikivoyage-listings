#!/bin/bash
# Please execute from wikivoyage-listings folder

# Generate SQLite database and statistics for each language
mkdir -p listings
for WVLANGUAGE in fr ru en de es it zh pt; do

  echo "=== LANGUAGE: $WVLANGUAGE ==="

  ./wikivoyage-listings.sh -generate -input-latest $WVLANGUAGE -output-filename listings/$WVLANGUAGE.sql -output-format sql
  cat listings/$WVLANGUAGE.sql | sqlite3 listings/$WVLANGUAGE.sqlitedb

  LISTINGS=`sqlite3 listings/$WVLANGUAGE.sqlitedb 'SELECT COUNT(id) FROM wikivoyage_listings;'`; echo "Listings: $LISTINGS"
  WIKIDATA=`sqlite3 listings/$WVLANGUAGE.sqlitedb 'SELECT COUNT(id) FROM wikivoyage_listings WHERE wikidata != "";'`; echo "Wikidata: $WIKIDATA"
  WIKIPEDIA=`sqlite3 listings/$WVLANGUAGE.sqlitedb 'SELECT COUNT(id) FROM wikivoyage_listings WHERE wikipedia != "";'`; echo "Wikipedia: $WIKIPEDIA"
done


echo "=== ALL LANGUAGES ==="

# Create database containing all languages
cp listings/en.sqlitedb listings/all.sqlitedb
sqlite3 listings/fr.sqlitedb 'UPDATE wikivoyage_listings SET id = id + 1000000;' # id must be unique
sqlite3 listings/ru.sqlitedb 'UPDATE wikivoyage_listings SET id = id + 2000000;'
sqlite3 listings/de.sqlitedb 'UPDATE wikivoyage_listings SET id = id + 3000000;'
sqlite3 listings/es.sqlitedb 'UPDATE wikivoyage_listings SET id = id + 4000000;'
sqlite3 listings/it.sqlitedb 'UPDATE wikivoyage_listings SET id = id + 5000000;'
sqlite3 listings/zh.sqlitedb 'UPDATE wikivoyage_listings SET id = id + 6000000;'
sqlite3 listings/pt.sqlitedb 'UPDATE wikivoyage_listings SET id = id + 7000000;'
sqlite3 listings/all.sqlitedb 'attach "listings/fr.sqlitedb" AS toMerge; INSERT INTO wikivoyage_listings SELECT * FROM toMerge.wikivoyage_listings;'
sqlite3 listings/all.sqlitedb 'attach "listings/ru.sqlitedb" AS toMerge; INSERT INTO wikivoyage_listings SELECT * FROM toMerge.wikivoyage_listings;'
sqlite3 listings/all.sqlitedb 'attach "listings/de.sqlitedb" AS toMerge; INSERT INTO wikivoyage_listings SELECT * FROM toMerge.wikivoyage_listings;'
sqlite3 listings/all.sqlitedb 'attach "listings/es.sqlitedb" AS toMerge; INSERT INTO wikivoyage_listings SELECT * FROM toMerge.wikivoyage_listings;'
sqlite3 listings/all.sqlitedb 'attach "listings/it.sqlitedb" AS toMerge; INSERT INTO wikivoyage_listings SELECT * FROM toMerge.wikivoyage_listings;'
sqlite3 listings/all.sqlitedb 'attach "listings/zh.sqlitedb" AS toMerge; INSERT INTO wikivoyage_listings SELECT * FROM toMerge.wikivoyage_listings;'
sqlite3 listings/all.sqlitedb 'attach "listings/pt.sqlitedb" AS toMerge; INSERT INTO wikivoyage_listings SELECT * FROM toMerge.wikivoyage_listings;'

# Statistics
ALL=`sqlite3 listings/all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "listing";'`; echo "Total number of listings, all languages: $ALL"

echo "Number of listings with Wikidata, per type:"
LISTING=`sqlite3 listings/all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "listing";'`; echo "Listing: $LISTING"
SEE=`sqlite3 listings/all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "see";'`; echo "See: $SEE"
DO=`sqlite3 listings/all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "do";'`; echo "Do: $DO"
BUY=`sqlite3 listings/all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "buy";'`; echo "Buy: $BUY"
EAT=`sqlite3 listings/all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "eat";'`; echo "Eat: $EAT"
DRINK=`sqlite3 listings/all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "drink";'`; echo "Drink: $DRINK"
SLEEP=`sqlite3 listings/all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "sleep";'`; echo "Sleep: $SLEEP"
VICINITY=`sqlite3 listings/all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "vicinity";'`; echo "Vicinity: $VICINITY"
DIPLO=`sqlite3 listings/all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "diplomatic-representation";'`; echo "Diplomatic representation: $DIPLO"
