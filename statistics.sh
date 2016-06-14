#!/bin/bash

# Generate SQLite database and statistics for each language
for WVLANGUAGE in fr ru en de; do

  echo "=== LANGUAGE: $WVLANGUAGE ==="

  ./wikivoyage-listings.sh -generate -input-latest $WVLANGUAGE -output-filename $WVLANGUAGE.sql -output-format sql
  cat $WVLANGUAGE.sql | sqlite3 $WVLANGUAGE.sqlitedb

  LISTINGS=`sqlite3 $WVLANGUAGE.sqlitedb 'SELECT COUNT(id) FROM wikivoyage_listings;'`; echo "Listings: $LISTINGS"
  WIKIDATA=`sqlite3 $WVLANGUAGE.sqlitedb 'SELECT COUNT(id) FROM wikivoyage_listings WHERE wikidata != "";'`; echo "Wikidata: $WIKIDATA"
  WIKIPEDIA=`sqlite3 $WVLANGUAGE.sqlitedb 'SELECT COUNT(id) FROM wikivoyage_listings WHERE wikipedia != "";'`; echo "Wikipedia: $WIKIPEDIA"
done


echo "=== ALL LANGUAGES ==="

# Create database containing all languages
cp en.sqlitedb all.sqlitedb
sqlite3 fr.sqlitedb 'UPDATE wikivoyage_listings SET id = id + 1000000;' # id must be unique
sqlite3 ru.sqlitedb 'UPDATE wikivoyage_listings SET id = id + 2000000;'
sqlite3 de.sqlitedb 'UPDATE wikivoyage_listings SET id = id + 3000000;'
sqlite3 all.sqlitedb 'attach "fr.sqlitedb" AS toMerge; INSERT INTO wikivoyage_listings SELECT * FROM toMerge.wikivoyage_listings;'
sqlite3 all.sqlitedb 'attach "ru.sqlitedb" AS toMerge; INSERT INTO wikivoyage_listings SELECT * FROM toMerge.wikivoyage_listings;'
sqlite3 all.sqlitedb 'attach "de.sqlitedb" AS toMerge; INSERT INTO wikivoyage_listings SELECT * FROM toMerge.wikivoyage_listings;'

# Counts per type
echo "Number of listings with Wikidata, per type:"
LISTING=`sqlite3 all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "listing";'`; echo "Listing: $LISTING"
SEE=`sqlite3 all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "see";'`; echo "See: $SEE"
DO=`sqlite3 all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "do";'`; echo "Do: $DO"
BUY=`sqlite3 all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "buy";'`; echo "Buy: $BUY"
EAT=`sqlite3 all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "eat";'`; echo "Eat: $EAT"
DRINK=`sqlite3 all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "drink";'`; echo "Drink: $DRINK"
SLEEP=`sqlite3 all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "sleep";'`; echo "Sleep: $SLEEP"
VICINITY=`sqlite3 all.sqlitedb 'SELECT COUNT(*) FROM wikivoyage_listings WHERE wikidata != "" AND type == "vicinity";'`; echo "Vicinity: $VICINITY"
