#!/bin/bash

#### configuration ####

# date from which the download or update of data files should start from
# update these start values by current / today's date, after sucessful execution of this script
start_year=2023
end_year=2023

#### dynamic date value generation ####

for year in `seq $start_year 1 $end_year`
do
	YYYY="$year"

	for month in `seq 1 1 12`
	do
		MM=""

		if [ $month -lt 10 ]
		then
			MM="0$month"
		else
			MM="$month"
		fi

		for day in `seq 1 1 31`
		do
			DD=""

			if [ $day -lt 10 ]
			then
				DD="0$day"
			else
				DD="$day"
			fi

			# execute another script internal_yyyymmdd.sh , and pass the values YYYY MM DD to it as command line arguments
			sh internal_yyyymmdd.sh $YYYY $MM $DD

		done
	done
done


