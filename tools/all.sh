#!/bin/bash
# Please configure OUT folder variable then execute from wikivoyage-listings folder
# Run from project root

OUT=../wikivoyage.github.io

# Generate for each language
for WVLANGUAGE in fr ru en de es it zh pt; do
  echo "=== LANGUAGE: $WVLANGUAGE ==="

  for FORMAT in csv kml gpx osmand.gpx xml validation-report sql; do
    ./wikivoyage-listings.sh -generate -input-latest $WVLANGUAGE -output-filename $OUT/wikivoyage-listings-$WVLANGUAGE.$FORMAT -output-format $FORMAT
  done
  mv $OUT/wikivoyage-listings-$WVLANGUAGE.validation-report $OUT/wikivoyage-listings-$WVLANGUAGE.validation-report.html
done

cd $OUT
git add wikivoyage-listings-*
git commit -m "Update"
git push
