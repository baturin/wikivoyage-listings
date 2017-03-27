-- For each wikidata QID, merge available information from all languages.
-- Priority is given to information from the English Wikivoyage, you can change that at the end of the script.

SELECT
    a.wikidata,
    COALESCE(b.article, a.article) article,
    COALESCE(b.type, a.type) type,
    COALESCE(b.title, a.title) title,
    COALESCE(b.wikipedia, a.wikipedia) wikipedia,
    COALESCE(b.alt, a.alt) alt,
    COALESCE(b.address, a.address) address,
    COALESCE(b.directions, a.directions) directions,
    COALESCE(b.phone, a.phone) phone,
    COALESCE(b.tollfree, a.tollfree) tollfree,
    COALESCE(b.email, a.email) email,
    COALESCE(b.fax, a.fax) fax,
    COALESCE(b.url, a.url) url,
    COALESCE(b.hours, a.hours) hours,
    COALESCE(b.checkin, a.checkin) checkin,
    COALESCE(b.checkout, a.checkout) checkout,
    COALESCE(b.image, a.image) image,
    COALESCE(b.price, a.price) price,
    COALESCE(b.latitude, a.latitude) latitude,
    COALESCE(b.longitude, a.longitude) longitude,
    COALESCE(b.wifi, a.wifi) wifi,
    COALESCE(b.accessibility, a.accessibility) accessibility,
    COALESCE(b.lastedit, a.lastedit) lastedit, -- TODO use MAX
    COALESCE(b.description, a.description) description
FROM 

(SELECT
    wikidata,
    article,
    type,
    title,
    wikipedia,
    alt,
    address,
    directions,
    phone,
    tollfree,
    email,
    fax,
    url,
    hours,
    checkin,
    checkout,
    image,
    price,
    latitude,
    longitude,
    wifi,
    accessibility,
    lastedit,
    description
FROM
    wikivoyage_listings 
GROUP BY wikidata
) a

LEFT JOIN 

(SELECT
    wikidata,
    article,
    type,
    title,
    wikipedia,
    alt,
    address,
    directions,
    phone,
    tollfree,
    email,
    fax,
    url,
    hours,
    checkin,
    checkout,
    image,
    price,
    latitude,
    longitude,
    wifi,
    accessibility,
    lastedit,
    description
FROM
    wikivoyage_listings
WHERE language='English'
) b

ON a.wikidata=b.wikidata;
