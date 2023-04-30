#!/bin/bash


cat output/history/SM003001.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003001,LIC PENSION FUND SCHEME - CENTRAL GOVT,/' > csv-output-mergable/SM003001.csv

cat output/history/SM003002.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003002,LIC PENSION FUND SCHEME - STATE GOVT,/' > csv-output-mergable/SM003002.csv

cat output/history/SM003003.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003003,NPS TRUST A\/C-LIC PENSION FUND LIMITED- NPS LITE SCHEME - GOVT. PATTERN,/' > csv-output-mergable/SM003003.csv

cat output/history/SM003004.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003004,LIC PENSION FUND LIMITED SCHEME - CORPORATE-CG,/' > csv-output-mergable/SM003004.csv

cat output/history/SM003005.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003005,LIC PENSION FUND SCHEME E - TIER I,/' > csv-output-mergable/SM003005.csv

cat output/history/SM003006.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003006,LIC PENSION FUND SCHEME C - TIER I,/' > csv-output-mergable/SM003006.csv

cat output/history/SM003007.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003007,LIC PENSION FUND SCHEME G - TIER I,/' > csv-output-mergable/SM003007.csv

cat output/history/SM003008.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003008,LIC PENSION FUND SCHEME E - TIER II,/' > csv-output-mergable/SM003008.csv

cat output/history/SM003009.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003009,LIC PENSION FUND SCHEME C - TIER II,/' > csv-output-mergable/SM003009.csv

cat output/history/SM003010.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003010,LIC PENSION FUND SCHEME G - TIER II,/' > csv-output-mergable/SM003010.csv

cat output/history/SM003011.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003011,NPS TRUST - A\/C LIC PENSION FUND SCHEME - ATAL PENSION YOJANA (APY),/' > csv-output-mergable/SM003011.csv

cat output/history/SM003012.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003012,LIC PENSION FUND SCHEME A - TIER I,/' > csv-output-mergable/SM003012.csv

cat output/history/SM003013.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003013,LIC PENSION FUND SCHEME A - TIER II,/' > csv-output-mergable/SM003013.csv

cat output/history/SM003014.csv | sed 's/,/,PFM003,LIC PENSION FUND LIMITED,SM003014,NPS TRUST - A\/C LIC PENSION FUND SCHEME TAX SAVER TIER II,/' > csv-output-mergable/SM003014.csv



