#!/bin/sh
#
# Create a subset of a listings CSV file, containing only the columns: destination, name, lat, long
# Requires csvkit. Ubuntu: sudo pip install csvkit
# Typical usage: tools/cut-csv-columns.sh out.csv > out-cut.csv

csvcut -d "," -c 1,3,17,18 $1 | grep -v ",,$"
