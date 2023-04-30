#!/bin/bash

echo ""

echo "script directory: `pwd`"
echo ""

cd ..

echo "current working directory: `pwd`"
echo ""

input="meta-data/unique-schemes-ids.csv"
echo "input file path: $input"

header="Date (MM/DD/YYYY),Pension Fund Manager Id,Pension Fund Manager,Pension Scheme Id,Pension Scheme,Net Asset Value (INR)"

consolidated_data_source="nav-data/nps-funds-performance-nav-data.csv"

output_directory="nav-data/scheme/"
output_extension=".csv"
temp_extension=".tmp"

output_min_directory="nav-data/scheme/min/"

echo "__________________________________________________________________________________________________________________________"
echo ""

while IFS= read -r line
do

    # input line from the file, the Scheme Identifier
    echo "line from input file: $line"
    echo ""

    # scheme file
    target_temp="$output_directory$line$output_extension$temp_extension"
    target="$output_directory$line$output_extension"
    echo "temp target file name: $target_temp"
    echo "target file name: $target"
    echo ""

    if [ -e $target_temp ]
    then
        echo "temp target file exists: true"
        echo ""

        echo "removing"
        echo ""

        rm $target_temp
    else
        echo "temp target file exists: false"
        echo ""
    fi

    if [ -e $target ]
    then
        echo "target file exists: true"
        echo ""

        echo "removing"
        echo ""

        rm $target
    else
        echo "target file exists: false"
        echo ""
    fi

    echo "writing the header"
    echo ""

    echo $header > $target_temp

    echo "extracting scheme data in individual file"
    echo ""
    cat $consolidated_data_source | grep $line >> $target_temp

    # remove carriage return
    cat $target_temp | sed 's/\r$//' > $target
    rm $target_temp

    # scheme min file
    target_min="$output_min_directory$line-min$output_extension"
    echo "target min file name: $target_min"
    echo ""

    if [ -e $target_min ]
    then
        echo "target min file exists: true"
        echo ""

        echo "removing"
        echo ""

        rm $target_min
    else
        echo "target min file exists: false"
        echo ""
    fi

    echo "extracting scheme data columns in individual min file"
    echo ""
    cat $target | cut -d "," -f 1,6 > $target_min

    echo "__________________________________________________________________________________________________________________________"
    echo ""

done < "$input"
