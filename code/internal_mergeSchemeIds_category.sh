#!/bin/bash

#### WildCard Definitions ####

: '

$0
Stores the first word of the entered command
(the name of the shell program).

$#
Stores the number of command-line arguments that were passed to the shell program.

$*
Stores all the arguments that were entered on the command line ($1 $2 ...).

"$@"
Stores all the arguments that were entered on the command line,
individually quoted ("$1" "$2" ...).

$?
Stores the exit value of the last command that was executed.

$-
(Hyphen.) Expands to the current option flags
(the single-letter option names concatenated into a string)
as specified on invocation, by the set special built-in command,
or implicitly by the shell.

$$
Expands to the decimal process ID of the invoked shell.
In a subshell (see Shell Execution Environment ),
'$' shall expand to the same value as that of the current shell.

$!
Expands to the decimal process ID of the most recent background command
(see Lists) executed from the current shell.
(For example, background commands executed from subshells
do not affect the value of "$!" in the current shell environment.)
For a pipeline, the process ID is that of the last command in the pipeline.

'

#### script starts here ####

echo ""

scriptPath="`pwd`/"

echo "current working directory: $scriptPath"
echo ""

echo "executing shell script file: $0"
echo ""


#### count command line arguments ####

commandLineArgumentsCount=$#
echo "command line arguments count: $commandLineArgumentsCount"
echo ""

commandLineArguments=$*
echo "command line arguments: $commandLineArguments"
echo ""


#### read passed command line arguments ####

# first n-1 arguments are scheme ids
# count actual schemeIds
# store actual schemeIds array
# last argument is the output scheme category name

schemeIdsTempFileName="temp-scheme-ids"
schemeIdsTempFilePath="$scriptPath$schemeIdsTempFileName"

echo "scheme ids temp file path: $schemeIdsTempFilePath"
echo ""

schemeDatesTempFileName="temp-scheme-dates"
schemeDatesTempFilePath="$scriptPath$schemeDatesTempFileName"

echo "scheme dates temp file path: $schemeDatesTempFilePath"
echo ""

schemeDatesUniqueTempFileName="temp-scheme-dates-unique"
schemeDatesUniqueTempFilePath="$scriptPath$schemeDatesUniqueTempFileName"

echo "scheme unique dates temp file path: $schemeDatesUniqueTempFilePath"
echo ""

argumentNumber=0
outputSchemeCategory=""

headerLine="Date"

for commandLineArgument in $commandLineArguments
do

    argumentNumber=`expr 1 + $argumentNumber`

    echo "command line argumentNumber: $argumentNumber, argument: $commandLineArgument"

    if [ "$argumentNumber" = "$commandLineArgumentsCount" ]
    then
        outputSchemeCategory=$commandLineArgument
    else
        schemeId=$commandLineArgument

        #### form header line : Date,schemeId1,schemeId2,... ####
        headerLine="$headerLine,$schemeId"

        #### write scheme Ids to a separate temp file ####
        echo "$schemeId" >> $schemeIdsTempFilePath
    fi

done

echo ""

echo "output scheme category: $outputSchemeCategory"
echo ""

echo "output header line: $headerLine"
echo ""

echo "content of scheme ids temp file:"
cat $schemeIdsTempFilePath

echo ""


#### form output file name / path ####

cd ..
cd nav-data
cd scheme-categorized

outputDirectory="`pwd`/"
outputFilePath="$outputDirectory$outputSchemeCategory.csv"

echo "output file path: $outputFilePath"
echo ""


#### output csv header line ####

# write line to output file, using >

echo "$headerLine" > $outputFilePath


#### get all dates within all those schemes ####

cd ..

cd scheme
cd min

inputMinDirectory="`pwd`/"

echo "input min directory: $inputMinDirectory"
echo ""


#### iterate through the scheme ids from the temp file ####

# cat all scheme-min files
# extract the dates, cut the first column

input="$schemeIdsTempFilePath"

echo "input file path: $input"
echo ""

echo "______________________________________________________________________________"
echo ""

echo "going to read scheme min files and extract dates"
echo ""

while IFS= read -r schemeIdLine
do

    echo "iteration scheme id: $schemeIdLine"
    echo ""

    minFilePath="$inputMinDirectory$schemeIdLine-min.csv"

    echo "min file path: $minFilePath"
    echo ""

    cat $minFilePath | cut -d "," -f 1 | grep -v "Date" >> $schemeDatesTempFilePath

done < "$input"

echo "______________________________________________________________________________"
echo ""


#### filter out unique dates ####

# sort the dates
# extract the unique dates
# write to a temp dates file

sort -n -t"," -k1.7,1.10 -k1.1,1.2 -k1.4,1.5 $schemeDatesTempFilePath | uniq > $schemeDatesUniqueTempFilePath

echo "unique dates count: `wc -l $schemeDatesUniqueTempFilePath`"
echo ""

#### remove the temp scheme dates file ####

rm $schemeDatesTempFilePath

echo "removed scheme dates temp file: $schemeDatesTempFilePath"
echo ""


#### read dates file ####

# for each line / date in dates file
    # initialize an empty output line string
    # for each schemeId passed as command line argument
        # cat each schemeId file
        # grep date
        # extract nav
        # if nav is present append to output line string
        # add comma
    # finally write the line string substring(0, length-1) in the output file, using >>

input="$schemeDatesUniqueTempFilePath"

echo "input file path: $input"
echo ""

echo "______________________________________________________________________________"
echo ""

echo "going to read unique dates and generate merged csv"
echo ""

while IFS= read -r dateLine
do

    echo "iteration date: $dateLine"

    csvLine="$dateLine"
    argumentNumber=0

    for commandLineArgument in $commandLineArguments
    do

        argumentNumber=`expr 1 + $argumentNumber`

        if [ "$argumentNumber" = "$commandLineArgumentsCount" ]
        then
            outputSchemeCategory=$commandLineArgument
        else
            schemeId=$commandLineArgument

            schemeMinFilePath="$inputMinDirectory$schemeId-min.csv"

            schemeNavData=`cat $schemeMinFilePath | grep $dateLine | cut -d "," -f 2`

            echo "scheme id : $schemeId , scheme nav : $schemeNavData"
            #### form csv line : MM/DD/YYYY,schemeId1Nav,schemeId2Nav,... ####
            csvLine="$csvLine,$schemeNavData"
        fi

    done

    echo "catgeory csv line: $csvLine"

    echo "$csvLine" >> $outputFilePath
    echo ""

done < "$input"

echo "______________________________________________________________________________"
echo ""


#### remove the temp scheme ids file ####

rm $schemeIdsTempFilePath

echo "removed scheme ids temp file: $schemeIdsTempFilePath"
echo ""


#### remove the temp scheme unique dates file ####

rm $schemeDatesUniqueTempFilePath

echo "removed scheme unique dates temp file: $schemeDatesTempFilePath"
echo ""

echo "______________________________________________________________________________"
echo ""

