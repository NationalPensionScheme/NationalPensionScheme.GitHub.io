
cd ..
cd nav-data

outputDirectory="`pwd`"
targetFileName="nps-schemes-chose-best-date-for-investment.csv"
targetFilePath="${outputDirectory}/${targetFileName}"

echo "outputDirectory : ${outputDirectory}"
echo "targetFileName : ${targetFileName}"
echo "targetFilePath : ${targetFilePath}"

echo ""

echo "deleting target"
rm ${targetFilePath}
echo "target deleted"

echo ""

echo "creating target"
touch ${targetFilePath}
echo "target created"

echo ""

sourceDirectoryName="scheme-invest"
sourceDirectoryPath="${outputDirectory}/${sourceDirectoryName}"

echo "sourceDirectoryName : ${sourceDirectoryName}"
echo "sourceDirectoryPath : ${sourceDirectoryPath}"

echo ""

tempFileName="temp-file-names.out"
tempFilePath="${outputDirectory}/${tempFileName}"

echo "tempFileName : ${tempFileName}"
echo "tempFilePath : ${tempFilePath}"

echo ""

# original command found online
# find . -name '*.csv' -print0 | xargs -0 awk '...' > out.csv

# find . -name '*.csv'
# gives output like
# ./SM006004.csv
# ./SM004001.csv

# find . -name '*.csv' -print0
# gives output like
# ./SM006004.csv ./SM004001.csv ./SM003006.csv ./SM010008.csv 

# find . -name '*.csv' -print0 | xargs -0
# gives output like
# ./SM006004.csv ./SM004001.csv ./SM003006.csv ./SM010008.csv

# find . -name '*.csv' > temp-file-names.out
# creates file with content like
# ./SM006004.csv
# ./SM004001.csv

# find scheme-invest -name '*.csv' > temp-file-names.out
# creates file with content like
# ./SM006004.csv
# ./SM004001.csv

# find ${sourceDirectoryPath} -name '*.csv' > temp-file-names.out
# creates file with content like
# /space/projects-my/NationalPensionScheme/nav-data/scheme-invest/SM006004.csv
# /space/projects-my/NationalPensionScheme/nav-data/scheme-invest/SM004001.csv

# find ${sourceDirectoryName} -name '*.csv' > temp-file-names.out
# creates file with content like
# scheme-invest/SM006004.csv
# scheme-invest/SM004001.csv

# find -name '*.csv' > temp-file-names.out
# creates file with content like
# ./SM006004.csv
# ./SM004001.csv

rm ${tempFilePath}

cd ${sourceDirectoryPath}

find -name '*.csv' -printf '%f\n' | sort > ${tempFilePath}

## TODO

# trigger java code, mismatch of columns will be there
# directly implement on the frontend, to select one scheme and diplay its data only
