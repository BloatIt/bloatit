#!/bin/bash

AMOUNT=1
FROM_CURRENCY=EUR
TMP_FILE=/tmp/rates.properties
RATE_FILE=/home/elveos/.local/share/bloatit/locales/rates.properties

cat << EOF > "$TMP_FILE"
# File format has to be : Money_code=Euro_to_currency value
EOF

for TO_CURRENCY in USD INR SEK GBP CHF NOK
do
    QUERY_URL="http://www.google.com/ig/calculator?hl=en&q=${AMOUNT}${FROM_CURRENCY}%3D%3F${TO_CURRENCY}"

    rate=$(wget -q  -O - "$QUERY_URL" | tr -d '{}' | tr ',' '\n' | grep "^rhs:" | grep -o -E "[0-9]+\.?[0-9]+")
    echo "$TO_CURRENCY=$rate" >> "$TMP_FILE"
done
echo "EUR=1.0000" >> "$TMP_FILE"

mv "$TMP_FILE" "$RATE_FILE"

