#!/bin/bash

#### identify ####

echo "current working directory: `pwd`"

echo ""

YYYY=$1
MM=$2
DD=$3
echo "YYYY: $YYYY"
echo "MM: $MM"
echo "DD: $DD"

echo ""

source_date="$DD$MM$YYYY"
echo "source date: $source_date"

source_file="NAV_File_$source_date.zip"
echo "source file: $source_file"

source_base_url="https://www.npscra.proteantech.in/download/"

# source_link_old = https://npscra.nsdl.co.in/download/NAV_File_{DDMMYYYY}.zip
# source_link = https://npscra.proteantech.in/download/NAV_File_{DDMMYYYY}.zip

source_link="$source_base_url$source_file"
echo "source link: $source_link"

echo ""

target_date="$YYYY-$MM-$DD"
echo "target date: $target_date"

target_file="$target_date.csv"
echo "target file: $target_file"

echo ""

#### go to data directory ####

cd ..
cd nav-data/daily

echo "current working directory: `pwd`"

echo ""

#### check if data needs to be downloaded ####

if [ -e $target_file ]
then
	echo "target file exists: true"
else

	#### download source data zip file ####

	echo "target file exists: false"
	echo ""

	echo "downloading"
	wget $source_link

	#### check if downloaded ####

	if [ -e $source_file ]
	then
		echo "downloaded: true"
		echo ""

		#### extract zip ####

		echo "extracting"
		unzip $source_file

		extracted_file="NAV_File_$source_date.out"

		echo "checking extracted filename"

		alternate_extracted_file="NAV_File_$source_date.csv"
		if [ -e $alternate_extracted_file ]
		then
			echo "alternate filename found"
			extracted_file=$alternate_extracted_file
		fi

		echo ""

		echo "extracted file name: $extracted_file"

		echo ""

		#### rename file name <DDMMYYYY>.out to <YYYYMMDD>.csv ####

		echo "renaming"

		mv $extracted_file $target_file

		echo ""

		#### remove zip file ####

		echo "removing downloaded zip"
		rm $source_file

		echo ""

		#### append to <NPS>.csv ####
		cat $target_file >> ../nps-funds-performance-nav-data.csv
		echo "appended to file: nps-funds-performance-nav-data.csv"

	else
		echo "downloaded: false"
	fi

fi

echo "__________________________________________________________________________________________________________________________"
echo ""

