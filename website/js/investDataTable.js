

let previousSchemeId = "";

let schemeDataSourcePrefix = "https://raw.githubusercontent.com/NationalPensionScheme/NationalPensionScheme.GitHub.io/main/nav-data/scheme-invest/";
let csvSuffix = ".csv";
let jsonSuffix = ".json";

function hasContent(str) {

    if (null == str) {
        return false;
    }
    if (undefined === str) {
        return false;
    }
    if (typeof str == "string") {
        if (str === "") {
            return false;
        }
        if (str.length === 0) {
            return false;
        }
        if (str.trim() === 0) {
            return false;
        }
    }
    if (!str) {
        return false;
    }
    if (str) {
        return true;
    }
}

function transformCsvForDataTable(csvContent) {
    // TODO

    //return csvContent;

    // array of lines, each element is a string
    // at index 0 : `Date (MM/DD/YYYY),Net Asset Value (INR)`
    // from index 1 : `05/29/2015,10.0000`

    // "Month (MM)","Day (DD)","2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","Average Across Years"
    // 01,01,0,15.174333,6.576464,6.5834203,12.548697,13.596871,6.5183015,3.2608614,8.151164,9.051264

    let csvLines = csvContent.split("\n");
    console.log("got split by new line -> (csvLines)\n" + JSON.stringify(csvLines));

    console.log("transform data -> (lineCount) " + csvLines.length);

    /*
    const dataSet = [
        ['Tiger Nixon', 'System Architect', 'Edinburgh', '5421', '2011/04/25', '$320,800'],
        ['Garrett Winters', 'Accountant', 'Tokyo', '8422', '2011/07/25', '$170,750']
    ];

    new DataTable('#example', {
        columns: [
            { title: 'Name' },
            { title: 'Position' },
            { title: 'Office' },
            { title: 'Extn.' },
            { title: 'Start date' },
            { title: 'Salary' }
        ],
        data: dataSet
    });
    */

    let table = {};

    let lineContent = csvLines[0];
    let typeOfContent = typeof lineContent;
    let headingPresent = hasContent(lineContent);
    console.log("got -> (csvLineIndex) " + 0 + " (lineContent) " + lineContent + " (typeOfContent) " + typeOfContent + " (hasHeading) " + headingPresent);

    if (headingPresent) {
        let dataChunk = lineContent.split(",");
        console.log("got split by comma -> (dataChunk)" + JSON.stringify(dataChunk));

        let tableHeading = [];

        tableHeading.push({ title: "MM_DD" });

        for (let colIndex = 2; colIndex < dataChunk.length; colIndex++) {
            let year = dataChunk[colIndex];
            tableHeading.push({ title: JSON.parse(year) });
        }

        table.head = tableHeading;
    }

    let tableBody = [];
    // skip line 0, since it is heading
    for (let csvLineIndex = 1; csvLineIndex < csvLines.length; csvLineIndex++) {
        let lineContent = csvLines[csvLineIndex];
        let typeOfContent = typeof lineContent;
        let contentPresent = hasContent(lineContent);
        console.log("got -> (csvLineIndex) " + csvLineIndex + " (lineContent) " + lineContent + " (typeOfContent) " + typeOfContent + " (hasContent) " + contentPresent);

        if (contentPresent) {
            let dataChunk = lineContent.split(",");
            console.log("got split by comma -> (dataChunk)" + JSON.stringify(dataChunk));

            let tableRow = [];

            let mm_dd = dataChunk[0] + "-" + dataChunk[1];
            tableRow.push(mm_dd);

            for (let colIndex = 2; colIndex < dataChunk.length; colIndex++) {
                let growthString = dataChunk[colIndex];
                let growthNumber = parseFloat(growthString);
                tableRow.push(growthNumber);
            }

            tableBody.push(tableRow);
        }
    }
    table.body = tableBody;

    return table;
}

function transformJsonForDataTable(jsonContent) {
    // TODO

    return jsonContent;
}

function populateDataTable(fundSchemeId, dataTableArgs) {
    // TODO

    // let table = new DataTable('#tabular-scheme-best-day-to-invest');

    let table;

    table = new DataTable('#tabular-scheme-best-day-to-invest', {
        // options
        paging: false,
        lengthMenu: [ 366 ],
        searching: true,
        //retrieve: true,
        destroy: true,
        ordering: true,
        //select: true,
        columns: dataTableArgs.head,
        data: dataTableArgs.body,
        //ajax: {
        //    data: dataTableArgs,
        //}
        scrollX: true,
        //scrollCollapse: true,
    });

    console.log("best day to invest data table generated -> (fundSchemeId) " + fundSchemeId);
}

function processCsvData(fundSchemeId, csvContent) {

    console.log("fetched -> (csvContent)\n" + csvContent);

    let dataTableArgs = transformCsvForDataTable(csvContent);
    console.log("got -> (dataTableArgs) " + JSON.stringify(dataTableArgs));

    if (hasContent(previousSchemeId)) {
        console.log("making the previous SchemeId button inactive");
        let previousSchemeButtonElement = document.getElementById(previousSchemeId);
        previousSchemeButtonElement.classList.remove("active-fund-scheme");
        console.log("modified -> (previousSchemeId) " + previousSchemeId);
    }

    console.log("making the current fundSchemeId active");
    let schemeButtonElement = document.getElementById(fundSchemeId);
    schemeButtonElement.classList.add("active-fund-scheme");
    console.log("modified -> (fundSchemeId) " + fundSchemeId);

    console.log("hiding the instructions");
    let instructionsElement = document.getElementById("instructions");
    instructionsElement.style.display = "none";

    populateDataTable(fundSchemeId, dataTableArgs);

    previousSchemeId = fundSchemeId;
}

function processJsonData(fundSchemeId, jsonContent) {

    console.log("fetched -> (jsonContent)\n" + jsonContent);

    let dataTableArgs = transformJsonForDataTable(jsonContent);
    console.log("got -> (dataTableArgs) " + JSON.stringify(dataTableArgs));

    if (hasContent(previousSchemeId)) {
        console.log("making the previous SchemeId button inactive");
        let previousSchemeButtonElement = document.getElementById(previousSchemeId);
        previousSchemeButtonElement.classList.remove("active-fund-scheme");
        console.log("modified -> (previousSchemeId) " + previousSchemeId);
    }

    console.log("making the current fundSchemeId active");
    let schemeButtonElement = document.getElementById(fundSchemeId);
    schemeButtonElement.classList.add("active-fund-scheme");
    console.log("modified -> (fundSchemeId) " + fundSchemeId);

    console.log("hiding the instructions");
    let instructionsElement = document.getElementById("instructions");
    instructionsElement.style.display = "none";

    populateDataTable(fundSchemeId, dataTableArgs);

    previousSchemeId = fundSchemeId;
}

function csv(fundSchemeId) {
    // download the csv data source
    let schemeDataCsvSourceUrl = schemeDataSourcePrefix + fundSchemeId + csvSuffix;
    console.log("download -> (schemeDataCsvSourceUrl) " + schemeDataCsvSourceUrl);

    let csvFileHttpRequest = new XMLHttpRequest();
    csvFileHttpRequest.onreadystatechange = function() {
        console.log("request state change -> (readyState) " + this.readyState + " (status) " + this.status);
        if (this.readyState == 4 && this.status == 200) {
            let csvContent = csvFileHttpRequest.responseText;
            processCsvData(fundSchemeId, csvContent);
        }
    };
    csvFileHttpRequest.open("GET", schemeDataCsvSourceUrl, true);
    csvFileHttpRequest.send();
}

function json(fundSchemeId) {
    // download the csv data source
    let schemeDataJsonSourceUrl = schemeDataSourcePrefix + fundSchemeId + jsonSuffix;
    console.log("download -> (schemeDataJsonSourceUrl) " + schemeDataJsonSourceUrl);

    let jsonFileHttpRequest = new XMLHttpRequest();
    jsonFileHttpRequest.onreadystatechange = function() {
        console.log("request state change -> (readyState) " + this.readyState + " (status) " + this.status);
        if (this.readyState == 4 && this.status == 200) {
            let jsonContent = jsonFileHttpRequest.responseText;
            processJsonData(fundSchemeId, jsonContent);
        }
    };
    jsonFileHttpRequest.open("GET", schemeDataJsonSourceUrl, true);
    jsonFileHttpRequest.send();
}

function loadSchemeTabular(button) {

    console.log("###################################################");

    console.log("loadSchemeTabular -> (button) " + button);

    let fundSchemeId = button.value;
    console.log("loadSchemeTabular -> (fundSchemeId) " + fundSchemeId);

    csv(fundSchemeId);
    //json(fundSchemeId);
}
