# Download latest ready-to-use data

<table>
<tr>
 <td>CSV</td>
 <td>
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-en-latest.csv">English</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-ru-latest.csv">Russian</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-fr-latest.csv">French</a></td>
</tr>
<tr>
 <td>KML</td>
 <td>
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-en-latest.kml">English</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-ru-latest.kml">Russian</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-fr-latest.kml">French</a></td>
</tr>
<tr>
 <td>GPX</td>
 <td>
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-en-latest.gpx">English</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-ru-latest.gpx">Russian</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-fr-latest.gpx">French</a></td>
</tr>
<tr>
 <td>GPX (for OsmAnd)</td>
 <td>
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-en-latest.osmand.gpx">English</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-ru-latest.osmand.gpx">Russian</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-fr-latest.osmand.gpx">French</a></td>
</tr>
<tr>
 <td>XML ("wikivoyage" POI type)</td>
 <td>
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-en-latest.xml">English</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-ru-latest.xml">Russian</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-fr-latest.xml">French</a></td>
</tr>
<tr>
 <td>XML (user-defined POI type)</td>
 <td>
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-en-latest-user-defined.xml">English</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-ru-latest-user-defined.xml">Russian</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-fr-latest-user-defined.xml">French</a></td>
</tr>
<tr>
 <td>OBF ("wikivoyage" POI type)</td>
 <td>
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-en-latest.obf">English</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-ru-latest.obf">Russian</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-fr-latest.obf">French</a></td>
</tr>
<tr>
 <td>OBF (user-defined POI type)</td>
 <td>
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-en-latest-user-defined.obf">English</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-ru-latest-user-defined.obf">Russian</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-fr-latest-user-defined.obf">French</a></td>
</tr>
<tr>
 <td>SQL</td>
 <td>
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-en-latest.sql">English</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-ru-latest.sql">Russian</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-fr-latest.sql">French</a></td>
</tr>
<tr>
 <td>Validation report</td>
 <td>
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-en-latest.validation-report.html">English</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-ru-latest.validation-report.html">Russian</a>,
  <a href="http://wvpoi.batalex.ru/wikivoyage-poi/wikivoyage-listings-fr-latest.validation-report.html">French</a></td>
</tr>
</table>

More at http://wvpoi.batalex.ru

# How to build and run

You can also generate this data by yourself if you want. Requirements: Java, Gradle

1. Run `./gradlew jar`
2. Run `./wikivoyage-listings.sh`, this will show the help

Example: `./wikivoyage-listings.sh -generate -input-latest ru -output-filename ru.csv -output-format csv`
