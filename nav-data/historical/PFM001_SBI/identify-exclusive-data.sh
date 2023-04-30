#!/bin/bash

echo ""

script_path="`pwd`/"
echo "script path: $script_path"
echo ""

scheme_files_path="`pwd`/csv-normalized/"
echo "scheme files path: $scheme_files_path"
echo ""

cd ..
cd ..

combined_data_file_path="`pwd`/nps-funds-performance-nav-data.csv"

echo "combined data file path: $combined_data_file_path"
echo ""

output_directory="output"
output_path="$script_path$output_directory"
echo "output path: $output_path"
echo ""

temp_file="$output_path/temp.csv"
echo "temp file: $temp_file"
echo ""

for schemeFileName in `ls $scheme_files_path`
do

    echo "______________________________________________________________________________________________________________________________________________"
    echo ""

    echo "iteration for scheme file name: $schemeFileName"
    echo ""

    schemeFileNameWithoutExtension=`echo $schemeFileName | cut -d "." -f 1`
    echo "scheme file name without extension: $schemeFileNameWithoutExtension"
    echo ""

    input="$scheme_files_path$schemeFileName"
    echo "input file path: $input"
    echo ""

    lineNumber=0

    history_file="$output_path/history/$schemeFileName"
    echo "history file path: $history_file"
    echo ""

    discrepancy_file="$output_path/discrepancy/$schemeFileName"
    echo "discrepancy file path: $discrepancy_file"
    echo ""

    investigation_file="$output_path/investigation/$schemeFileName"
    echo "investigation file path: $investigation_file"
    echo ""

    while IFS= read -r fund_manager_line
    do
        lineNumber=`expr 1 + $lineNumber`

        echo "line: $lineNumber from input file: $fund_manager_line"

        if [ $lineNumber -gt 1 ]
        then

            fund_manager_date=`echo $fund_manager_line | cut -d "," -f 1`
            echo "extracted fund manager date: $fund_manager_date"

            dd=`echo $fund_manager_date | cut -d "-" -f 3`
            echo "extracted dd: $dd"

            mm=`echo $fund_manager_date | cut -d "-" -f 2`
            echo "extracted mm: $mm"

            yyyy=`echo $fund_manager_date | cut -d "-" -f 1`
            echo "extracted yyyy: $yyyy"

            date="$mm/$dd/$yyyy"
            echo "transformed date: $date"

            nav=`echo $fund_manager_line | cut -d "," -f 2`
            echo "extracted nav: $nav"

            line="$date,$nav"
            echo "transformed line: $line"

            cat $combined_data_file_path | grep $date | grep $schemeFileNameWithoutExtension > $temp_file

            dateSchemeSearchCount=`cat $temp_file | wc -l`
            echo "date scheme search count: $dateSchemeSearchCount"

            echo "date scheme search result: `cat $temp_file`"

            if [ $dateSchemeSearchCount -eq 0 ]
            then

                ## write to history file
                echo "write to history file"

                ## transform the $line to match the format of $combined_data_file_path
                ## here or later
                echo "$line" >> $history_file

            elif [ $dateSchemeSearchCount -gt 1 ]
            then

                ## write to investigation file
                echo "write to investigation file"

                echo "$line" >> $investigation_file

            else

                ## check further for nav
                echo "check further for nav"

                nsdl_nav=`cat $temp_file | cut -d "," -f 6`
                echo "extracted nsdl nav: $nsdl_nav"

                if [ "$nsdl_nav" = "$nav" ]
                then

                    echo "exact same entry found in combined csv"

                else

                    echo "write to discrepancy file"

                    echo "$date,$nsdl_nav,$nav" >> $discrepancy_file

                fi

            fi

        else
            echo "skipping first line"
        fi

        echo ""

    done < "$input"

done

echo "______________________________________________________________________________________________________________________________________________"
echo ""

rm $temp_file
echo "removed temp file: $temp_file"
