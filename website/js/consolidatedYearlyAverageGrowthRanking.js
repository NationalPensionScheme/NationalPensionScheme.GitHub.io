
let startFinancialYear = 2007;
console.log("initial -> (startFinancialYear) " + startFinancialYear);

// closing of the current or running financial year (2023 - 2024) , i.e. from 1 April 2023 to 31 March 2024

let endFinancialYear = 2025;
console.log("initial -> (endFinancialYear) " + endFinancialYear);

// yearly analysis data should be finalized uptill previous (2019 - 2020) to last financial year (2020 - 2021)
// as all the dates of last financial year have passed, i.e. from 1 April 2020 to 31 March 2021
// the yearly transition of percentage growth for each day of the 2nd last financial year 19-20 can be calculated

// growth = NAV on a date of 2021 - NAV on the same date (dd) month (mm) of 2020
// percentage growth = 100 * growth / NAV on 2020

// however the same cannot be calculated for every day of transition between the last financial year and current
// since this current financial year is still running, so there are NAV missing for the days yet to come

// as an assumption, the future NAV can be considered as the latest date fetched NAV

let finalizedTillFinancialYear = endFinancialYear - 2;
console.log("initial -> (finalizedTillFinancialYear) " + finalizedTillFinancialYear);

let yearlyAverageGrowthRankingDataUrl = "https://raw.githubusercontent.com/NationalPensionScheme/NationalPensionScheme.GitHub.io/main/nav-data/growth/yearly-average-ranking.json";

function normalize2DigitYear(year) {

    if (year < 10) {
        return "0" + year;
    } else {
        return year;
    }
}

function fillTabularYearlyHeadings() {

    console.log("###################################################");
    console.log("fill tabular yearly headings");

    let yearlyHeadingRowContent = "<th class='blank'></th>";

    // finalizedTillFinancialYear

    for (let financialYear = startFinancialYear; financialYear < endFinancialYear; financialYear++) {
        console.log("iteration -> (financialYear) " + financialYear);

        let financialYearSignificantDigits = financialYear % 100;
        console.log("got -> (financialYearSignificantDigits) " + financialYearSignificantDigits);

        let open = normalize2DigitYear(financialYearSignificantDigits);
        console.log("got -> (open) " + open);

        let close = normalize2DigitYear(1 + financialYearSignificantDigits);
        console.log("got -> (close) " + close);

        let headingChunk = financialYear < finalizedTillFinancialYear
                            ? "<th>"
                            : "<th class='yellow'>";
        headingChunk = headingChunk + "FY " + open + "-" + close + "</th>";
        console.log("got -> (headingChunk) " + headingChunk);

        yearlyHeadingRowContent = yearlyHeadingRowContent + headingChunk;
    }

    console.log("got -> (yearlyHeadingRowContent) " + yearlyHeadingRowContent);

    let yearlyHeadingFinancialYearRowClass = "yearly-heading-financial-year-row";

    let yearlyHeadingFinancialYearRowElements = document.getElementsByClassName(yearlyHeadingFinancialYearRowClass);
    console.log("identified -> (yearlyHeadingFinancialYearRowCount) " + yearlyHeadingFinancialYearRowElements.length);

    for (let rowIndex = 0; rowIndex < yearlyHeadingFinancialYearRowElements.length; rowIndex++) {
        yearlyHeadingFinancialYearRowElements[rowIndex].innerHTML = yearlyHeadingRowContent;
        console.log("populated data -> (class) " + yearlyHeadingFinancialYearRowClass + " (rowIndex) " + rowIndex);
    }
}

function fillTabularYearlyData(yearlyAverageGrowthRankingData) {

    // for each schemeId
    // fill in the row id `#yearly-analysis-{schemeId}`
    // #yearly-analysis-SM001011
    console.log("###################################################");
    console.log("fill yearly tabular data");

    let categoryGrowthDivs = document.getElementsByClassName("scheme-ranking-data-table");
    for (let categoryGrowthDivIndex = 0; categoryGrowthDivIndex < categoryGrowthDivs.length; categoryGrowthDivIndex++) {
        categoryGrowthDiv = categoryGrowthDivs[categoryGrowthDivIndex];

        let categoryGrowthTable = categoryGrowthDiv.getElementsByTagName("table")[0];

        let categoryGrowthTableRows = categoryGrowthTable.getElementsByTagName("tr");

        for (categoryGrowthTableRowIndex = 1; categoryGrowthTableRowIndex < categoryGrowthTableRows.length; categoryGrowthTableRowIndex++) {
            let categoryGrowthTableRow = categoryGrowthTableRows[categoryGrowthTableRowIndex];

            let rowId = categoryGrowthTableRow.id;
            let extractedSchemeId = rowId.replace("yearly-analysis-", "");

            let schemeGrowthRankingData = yearlyAverageGrowthRankingData[extractedSchemeId];
            console.log("got -> (schemeId) " + extractedSchemeId + " (schemeGrowthRankingData) " + JSON.stringify(schemeGrowthRankingData));

            let financialYears = Object.keys(schemeGrowthRankingData);

            let rowContent = categoryGrowthTableRow.innerHTML;

            // append the growth data, to rowContent
            // each financial year average growth percentage, in a seperate <td>
            for (let financialYearIndex = 0; financialYearIndex < financialYears.length; financialYearIndex++) {
                let financialYear = financialYears[financialYearIndex];

                let financialYearGrowthRanking = schemeGrowthRankingData[financialYear];
                console.log("got -> (financialYearGrowthRanking) " + financialYearGrowthRanking);

                /* light colours for extreme ends */
                // bright green for top rankers (rank 1)
                // dark green for medium rankers (rank 10)
                // dark red for medium rankers (rank 11)
                // bright red for bottom most rankers (rank 20)

                /*
                let colour = (financialYearGrowthRanking <= 10)
                                ? 10 - financialYearGrowthRanking
                                : financialYearGrowthRanking - 10;

                let htmlClass = (financialYearGrowthRanking <= 10)
                                ? "green" + colour
                                : "red" + colour;
                */

                // color codes as per rank generated by chat gpt
                let htmlClass = "rank-component-" + financialYearGrowthRanking;

                let chunk = "<td class='" + htmlClass + "'>" + financialYearGrowthRanking + "</td>";

                rowContent = rowContent + chunk;
            }

            categoryGrowthTableRow.innerHTML = rowContent;
        }
    }
}

function fetchYearlyAverageGrowthRankingData() {

    console.log("###################################################");
    console.log("fetch yearly average growth ranking data");

    // download the data source
    console.log("download -> (yearlyAverageGrowthRankingDataUrl) " + yearlyAverageGrowthRankingDataUrl);

    let yearlyAverageGrowthRankingDataFileHttpRequest = new XMLHttpRequest();
    yearlyAverageGrowthRankingDataFileHttpRequest.onreadystatechange = function() {
        console.log("request state change -> (readyState) " + this.readyState + " (status) " + this.status);
        if (this.readyState == 4 && this.status == 200) {
            let yearlyAverageGrowthRankingData = yearlyAverageGrowthRankingDataFileHttpRequest.responseText;
            console.log("got -> (yearlyAverageGrowthRankingData) " + yearlyAverageGrowthRankingData);

            fillTabularYearlyData(JSON.parse(yearlyAverageGrowthRankingData));
        }
    };
    yearlyAverageGrowthRankingDataFileHttpRequest.open("GET", yearlyAverageGrowthRankingDataUrl, true);
    yearlyAverageGrowthRankingDataFileHttpRequest.send();
}

fillTabularYearlyHeadings();
fetchYearlyAverageGrowthRankingData();
