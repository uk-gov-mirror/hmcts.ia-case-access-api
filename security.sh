#!/usr/bin/env bash

#setting encoding for Python 2 / 3 compatibilities
export LC_ALL=C.UTF-8
export LANG=C.UTF-8

echo ${TEST_URL}
zap-api-scan.py -t ${TEST_URL}/v2/api-docs -f openapi -P 1001
cat zap.out
curl --fail http://0.0.0.0:1001/OTHER/core/other/jsonreport/?formMethod=GET --output report.json
zap-cli --zap-url http://0.0.0.0 -p 1001 report -o api-report.html -f html

cp report.json functional-output/
cp api-report.html functional-output/

zap-cli --zap-url http://0.0.0.0 -p 1001 alerts -l Informational --exit-code False
