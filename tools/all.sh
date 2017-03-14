#!/bin/bash
# Please configure OUT folder variable then execute from wikivoyage-listings folder

OUT=../wikivoyage.github.io

# Generate for each language
for WVLANGUAGE in fr ru en de; do
  echo "=== LANGUAGE: $WVLANGUAGE ==="

  for FORMAT in csv kml gpx osmand.gpx xml validation-report sql; do
    tools/wikivoyage-listings.sh -generate -input-latest $WVLANGUAGE -output-filename $OUT/wikivoyage-listings-$WVLANGUAGE.$FORMAT -output-format $FORMAT
  done
  mv $OUT/$WVLANGUAGE.validation-report $OUT/$WVLANGUAGE.validation-report.html
done

cd $OUT
git add wikivoyage-listings-*
git commit -m "Update"
git push
