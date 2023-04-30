
let previousSchemeId = "";

let schemeDataSourcePrefix = "https://raw.githubusercontent.com/TurquoiseSpace/National-Pension-Scheme/master/nav-data/scheme/min/";
let schemeDataSourceSuffix = "-min.csv";

function transformMinCsvForGoogleChartData(csvMinContent) {

    // array of lines, each element is a string
    // at index 0 : `Date (MM/DD/YYYY),Net Asset Value (INR)`
    // from index 1 : `05/29/2015,10.0000`
    let csvMinLines = csvMinContent.split("\n");
    console.log("got split by new line -> (csvMinLines)\n" + JSON.stringify(csvMinLines));

    console.log("transform data -> (lineCount) " + csvMinLines.length);

    let googleData = [];

    // skip line 0, since it is heading
    for (let csvLineIndex = 1; csvLineIndex < csvMinLines.length; csvLineIndex++) {
        let lineContent = csvMinLines[csvLineIndex];
        let typeOfContent = typeof lineContent;
        let contentPresent = hasContent(lineContent);
        console.log("got -> (csvLineIndex) " + csvLineIndex + " (lineContent) " + lineContent + " (typeOfContent) " + typeOfContent + " (hasContent) " + contentPresent);

        if (contentPresent) {
            let dataChunk = lineContent.split(",");
            console.log("got split by comma -> (dataChunk)" + JSON.stringify(dataChunk));

            let dateString = dataChunk[0];
            let dateObject = new Date(dateString);

            let navString = dataChunk[1];
            let navNumber = parseFloat(navString);

            let normalizedDataChunk = [dateObject, navNumber];
            googleData.push(normalizedDataChunk);
        }
    }

    return googleData;
}

function plotGoogleDataChart(fundSchemeId, googleChartData) {

    console.log("plot google data chart");

    // visualization logic comes here
    google.charts.load('current', {
        'packages':[
            'corechart',
            // 'annotationchart'
        ]
    });

    google.charts.setOnLoadCallback(function() {

        console.log("google chart library loaded");

        let dataTable = new google.visualization.DataTable();

        dataTable.addColumn('date', 'Date (MM/DD/YYYY)');
        dataTable.addColumn('number', 'Net Asset Value (INR)');

        dataTable.addRows(googleChartData);

        let options = {
            /*
            chart: {
              title: 'Fund Manager',
              subtitle: 'Scheme'
            },
            title: "Fund Identifier : " + fundSchemeId,
            width: 900,
            height: 500,
            crosshair: {
                trigger: 'both',
                color: '#996633',
                opacity: 0.2,
                orientation: 'both',
            },
            series: {
                0: { color: '#e2431e' },
                1: { color: '#e7711b' },
                2: { color: '#f1ca3a' },
                3: { color: '#6f9654' },
                4: { color: '#1c91c0' },
                5: { color: '#43459d' },
            },
            colors: ['black', 'blue', 'red', 'green', 'yellow', 'gray'],
            displayAnnotations: true,
            displayZoomButtons: false,
            aggregationTarget: 'auto',
            aggregationTarget: 'none',
            pointSize: 5,
            */
            title: getManagerNameFromSchemeId(fundSchemeId) + " - " + getSchemeNameFromSchemeId(fundSchemeId),
            // possible in google material line charts
            /*
            chart: {
                subtitle: getSchemeNameFromSchemeId(fundSchemeId),
            },
            */
            curveType: 'function',
            backgroundColor: {
                fill: "#000000"
            },
            legend: {
                // position: 'bottom'
                // position: 'top'
                position: 'right'
            },
            hAxis: {
                title: 'Date',
                baselineColor: '#777',
                gridlineColor: '#777',
                minorGridlines: {
                    count: 0,
                },
                /*
                gridlines: {
                    count: 50,
                },
                gridlineColor: '#fff',
                */
            },
            vAxis: {
                title: 'Net Asset Value (INR)',
                baselineColor: '#777',
                gridlineColor: '#777',
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
                /*
                scaleType: 'log',
                gridlines: {
                    count: 10
                },
                gridlineColor: '#fff',
                ticks: [0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55],
                ticks: [0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50],
                ticks: [0, 10, 20, 30, 40, 50],
                ticks: [5, 10, 15, 20, 25, 30, 35, 40, 45, 50],
                ticks: [6, 12, 18, 24, 30, 36, 42, 48],
                ticks: [7, 14, 21, 28, 35, 42, 49],
                ticks: [8, 16, 24, 32, 40, 48],
                ticks: [9, 18, 27, 36, 45, 54],
                */
            },
            tooltip: {
                /*
                isHtml: true,
                */
                trigger: 'both',
            },
            explorer: {
                maxZoomIn: 256,
                maxZoomOut: 5,
                axis: 'horizontal',
                zoomDelta: 1.32,
                /*
                maxZoomOut: 4,
                maxZoomOut: 8,
                keepInBounds: true,
                */
            },
            pointSize: 3,
            selectionMode: 'multiple',
        };

        let graphChartElement = document.getElementById('google-graph-chart');
        // graphChartElement.style.marginTop = "12%";
        graphChartElement.style.marginBottom = "5%";

        let chart = new google.visualization.LineChart(graphChartElement);
        // let chart = new google.visualization.AnnotationChart(graphChartElement);

        chart.draw(dataTable, options);

        console.log("google chart generated -> (fundSchemeId) " + fundSchemeId);
    });
}

function processData(fundSchemeId, csvMinContent) {

    console.log("fetched -> (csvMinContent)\n" + csvMinContent);

    let googleChartData = transformMinCsvForGoogleChartData(csvMinContent);
    console.log("got -> (googleChartData) " + JSON.stringify(googleChartData));

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

    plotGoogleDataChart(fundSchemeId, googleChartData);

    previousSchemeId = fundSchemeId;
}

function visualize(button) {

    console.log("###################################################");

    let fundSchemeId = button.value;
    console.log("visualize -> (fundSchemeId) " + fundSchemeId);

    // download the data source
    let schemeDataSourceUrl = schemeDataSourcePrefix + fundSchemeId + schemeDataSourceSuffix;
    console.log("download -> (schemeDataSourceUrl) " + schemeDataSourceUrl);

    let csvMinFileHttpRequest = new XMLHttpRequest();
    csvMinFileHttpRequest.onreadystatechange = function() {
        console.log("request state change -> (readyState) " + this.readyState + " (status) " + this.status);
        if (this.readyState == 4 && this.status == 200) {
            let csvMinContent = csvMinFileHttpRequest.responseText;
            processData(fundSchemeId, csvMinContent);
        }
    };
    csvMinFileHttpRequest.open("GET", schemeDataSourceUrl, true);
    csvMinFileHttpRequest.send();
}
