
let previousCategoryId = "";

let categoryDataSourcePrefix = "https://raw.githubusercontent.com/NationalPensionScheme/NationalPensionScheme.GitHub.io/main/nav-data/scheme-categorized/";
let categoryDataSourceSuffix = ".csv";

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

function identifyColumns(headerLine) {

    let typeOfContent = typeof headerLine;
    let contentPresent = hasContent(headerLine);
    console.log("got -> (headerLine) " + headerLine + " (typeOfContent) " + typeOfContent + " (hasContent) " + contentPresent);

    let columnArray = [];

    if (contentPresent) {
        let dataChunk = headerLine.split(",");
        console.log("got split by comma -> (dataChunk)" + JSON.stringify(dataChunk));

        // TODO : fetch the mapping of scheme id vs fund manager name here

        for (let lineParts = 1; lineParts < dataChunk.length; lineParts++) {
            let schemeId = dataChunk[lineParts];
            columnArray.push(schemeId);
        }
    }

    return columnArray;
}

function transformCategoryCsvForGoogleChartData(csvCategoryLines) {

    let googleData = [];

    // skip line 0, since it is heading
    for (let csvLineIndex = 1; csvLineIndex < csvCategoryLines.length; csvLineIndex++) {
        let lineContent = csvCategoryLines[csvLineIndex];
        let typeOfContent = typeof lineContent;
        let contentPresent = hasContent(lineContent);
        console.log("got -> (csvLineIndex) " + csvLineIndex + " (lineContent) " + lineContent + " (typeOfContent) " + typeOfContent + " (hasContent) " + contentPresent);

        if (contentPresent) {
            let dataChunk = lineContent.split(",");
            console.log("got split by comma -> (dataChunk)" + JSON.stringify(dataChunk));

            let dateString = dataChunk[0];
            let dateObject = new Date(dateString);

            let normalizedDataChunk = [dateObject];

            for (let lineParts = 1; lineParts < dataChunk.length; lineParts++) {
                let navString = dataChunk[lineParts];
                let navNumber = parseFloat(navString);
                normalizedDataChunk.push(navNumber);
            }
            console.log("transformed -> (lineData) " + JSON.stringify(normalizedDataChunk));

            googleData.push(normalizedDataChunk);
        }
    }

    return googleData;
}

function plotCategoryChart(categoryId, chartElementId, columnArray, googleChartData) {

    console.log("plot category chart");

    google.charts.load('current', {
        'packages':[
            'corechart',
        ]
    });

    google.charts.setOnLoadCallback(function() {

        console.log("google chart library loaded");

        let dataTable = new google.visualization.DataTable();

        dataTable.addColumn('date', 'Date (MM/DD/YYYY)');

        let colours = [];

        for (let columnIndex = 0; columnIndex < columnArray.length; columnIndex++) {
            let schemeId = columnArray[columnIndex];
            dataTable.addColumn('number', getManagerNameFromSchemeId(schemeId));
            colours.push(getColour(schemeId));
        }

        dataTable.addRows(googleChartData);

        let options = {
            /*
            title: "Category : " + categoryId,
            title: categoryId,
            title: categoryId.replaceAll("-", " "),
            */
            title: categoryId.replaceAll("-", " ").toUpperCase(),
            /*
            width: 900,
            height: 400,
            */
            curveType: 'function',
            backgroundColor: {
                fill: "#000000"
            },
            legend: {
                position: 'right',
                textStyle: {
                    fontSize: 10,
                }
                /*
                position: 'bottom',
                */
            },
            hAxis: {
                title: 'Date',
                baselineColor: '#777',
                gridlineColor: '#777',
                minorGridlines: {
                    count: 0,
                },
            },
            vAxis: {
                title: 'Net Asset Value (INR)',
                baselineColor: '#777',
                gridlineColor: '#777',
                /*
                minorGridlines : {
                    count : 6
                },
                gridlines : {
                    count : 6
                },
                viewWindow : {
                    min : 0,
                    max : 55
                },
                */
            },
            tooltip: {
                trigger: 'both',
            },
            explorer: {
                maxZoomIn: 256,
                maxZoomOut: 5,
                axis: 'horizontal',
                zoomDelta: 1.32,
            },
            selectionMode: 'multiple',
            /*
            pointSize: 3,
            chartArea: {
                height: 700,
            },
            */
            colors: colours,
            interpolateNulls: true,
        };

        let graphChartElement = document.getElementById(chartElementId);
        graphChartElement.style.marginBottom = "5%";

        let chart = new google.visualization.LineChart(graphChartElement);

        chart.draw(dataTable, options);

        console.log("google chart generated -> (categoryId) " + categoryId + " (chartElementId) " + chartElementId);
    });
}

function processCategoryData(categoryId, chartElementId, csvCategoryContent) {

    console.log("fetched -> (csvCategoryContent)\n" + csvCategoryContent);

    // array of lines, each element is a string
    // at index 0 : `Date (in MM/DD/YYYY format),Scheme Id 1, Scheme Id 2,...`
    // from index 1 : `05/29/2015,10.0000,...`
    let csvCategoryLines = csvCategoryContent.split("\n");
    console.log("got split by new line -> (csvCategoryLines)\n" + JSON.stringify(csvCategoryLines));

    console.log("transform data -> (lineCount) " + csvCategoryLines.length);

    let googleChartData = transformCategoryCsvForGoogleChartData(csvCategoryLines);
    console.log("got -> (googleChartData) " + JSON.stringify(googleChartData));

    let columnArray = identifyColumns(csvCategoryLines[0]);
    console.log("got -> (columnArray) " + JSON.stringify(columnArray));

    plotCategoryChart(categoryId, chartElementId, columnArray, googleChartData);
}

function fetchDataAndVisualize(categoryId, chartElementId) {

    console.log("fetch data and visualize");

    // download the data source
    let categoryDataSourceUrl = categoryDataSourcePrefix + categoryId + categoryDataSourceSuffix;
    console.log("download -> (categoryDataSourceUrl) " + categoryDataSourceUrl);

    let csvCategoryFileHttpRequest = new XMLHttpRequest();
    csvCategoryFileHttpRequest.onreadystatechange = function() {
        console.log("request state change -> (readyState) " + this.readyState + " (status) " + this.status);
        if (this.readyState == 4 && this.status == 200) {
            let csvCategoryContent = csvCategoryFileHttpRequest.responseText;
            processCategoryData(categoryId, chartElementId, csvCategoryContent);
        }
    };
    csvCategoryFileHttpRequest.open("GET", categoryDataSourceUrl, true);
    csvCategoryFileHttpRequest.send();
}

/*
function populateConsolidatedCharts() {

    console.log("###################################################");
    console.log("populate consolidated charts");

    let schemeCategoriesClass = "scheme-category-consolidated";

    let schemeCategoryElements = document.getElementsByClassName(schemeCategoriesClass);
    console.log("identified -> (schemeCategoryElementCount) " + schemeCategoryElements.length);

    for (let elementIndex = 0; elementIndex < schemeCategoryElements.length; elementIndex++) {
        console.log("iteration -> (categoryIndex) " + elementIndex);

        let schemeCategoryElement = schemeCategoryElements[elementIndex];

        let categoryChartDivElement = schemeCategoryElement.getElementsByTagName("div")[0];

        let chartElementId = categoryChartDivElement.id;
        console.log("fetched -> (chartElementId) " + chartElementId);

        let categoryId = chartElementId.replace("daily-", "");
        console.log("identified -> (categoryId) " + categoryId);

        fetchDataAndVisualize(categoryId, chartElementId);
    }
}
*/

function visualizeCategory(button) {
    console.log("###################################################");

    let categoryId = button.value;
    console.log("visualize category -> (categoryId) " + categoryId);

    fetchDataAndVisualize(categoryId, "google-graph-chart-consolidated");

    if (hasContent(previousCategoryId)) {
        console.log("making the previous CategoryId button inactive");
        let previousCategoryButtonElement = document.getElementById(previousCategoryId);
        previousCategoryButtonElement.classList.remove("active-category-scheme");
        console.log("modified -> (previousCategoryId) " + previousCategoryId);
    }

    console.log("making the current categoryId active");
    let categoryButtonElement = document.getElementById(categoryId);
    categoryButtonElement.classList.add("active-category-scheme");
    console.log("modified -> (categoryId) " + categoryId);

    previousCategoryId = categoryId;
}
