#!/bin/sh
#
# Sort a CSV file by longitude.
# To sort by latitude, just replace the 4 by 3 below.
# Typical usage: tools/cut-csv-columns.sh out.csv | tools/sort-csv-by-longitude.sh > out-cut-sorted.csv

cat $1 | sort --field-separator=, -k4,4n
