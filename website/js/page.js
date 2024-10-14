
let totalPages = 7
console.log("initial -> (totalPages) " + totalPages);

/*
let randomPageNumber = Math.floor(Math.random() * totalPages);
console.log("generated -> (randomPageNumber) " + randomPageNumber);
*/

let websiteBaseUrl = "https://nationalpensionscheme.github.io/";

let websitePageUriMapping = {
    0: "Information-of-Products-and-Schemes-by-Pension-Fund-Managers-offered-under-National-Pension-System-NPS-India.html",
    1: "Fund-Manager-wise-Individual-Scheme-line-Graph-charts-of-daily-Net-Asset-Value-trends-under-National-Pension-System-NPS-India.html",
    2: "Scheme-Category-wise-all-in-one-Consolidated-line-Graph-charts-of-daily-Net-Asset-Value-trends-for-Comparison-under-National-Pension-System-NPS-India.html",
    3: "Scheme-Category-wise-all-in-one-Consolidated-Tabular-Performance-Analysis-showing-Average-Growth-Percentage-for-each-Financial-Year-under-National-Pension-System-NPS-India.html",
    4: "Scheme-Category-wise-all-in-one-Consolidated-Tabular-Performance-Analysis-showing-Average-Growth-Ranking-for-each-Financial-Year-under-National-Pension-System-NPS-India.html",
    5: "Best-Day-to-Invest.html",
    6: "Contact-Book-having-Email-Ids-and-Phone-Numbers-to-Reach-out-to-various-Entities-under-National-Pension-System-NPS-India.html"
}

/*
function showPage(pageNumber) {

    console.log("show page -> (pageNumber) " + pageNumber);
    // #page-0
    let pageId = "page-" + pageNumber;
    document.getElementById(pageId).style.display = "block";
    console.log("modified -> (pageId) " + pageId);
    // #page-0-icon
    let iconId = pageId + "-icon";
    let iconElement = document.getElementById(iconId);
    iconElement.classList.add("active-page-icon");
    console.log("modified -> (iconId) " + iconId);
}

function hidePage(pageNumber) {

    console.log("hide page -> (pageNumber) " + pageNumber);
    // #page-0
    let pageId = "page-" + pageNumber;
    document.getElementById(pageId).style.display = "none";
    console.log("modified -> (pageId) " + pageId);
    // #page-0-icon
    let iconId = pageId + "-icon";
    let iconElement = document.getElementById(iconId);
    iconElement.classList.remove("active-page-icon");
    console.log("modified -> (iconId) " + iconId);
}

function viewPage(requestPageNumber) {

    console.log("###################################################");
    console.log("view page -> (requestPageNumber) " + requestPageNumber);
    for (let index = 0; index < totalPages; index++) {
        console.log("iteration -> (index) " + index);
        if (index == requestPageNumber) {
            showPage(index);
        } else {
            hidePage(index);
        }
    }
}
*/

function openPage(requestPageNumber) {

    console.log("###################################################");
    console.log("open page -> (requestPageNumber) " + requestPageNumber);
    let pageUri = websitePageUriMapping[requestPageNumber];
    console.log("identified -> (pageUri) " + pageUri);
    let actualUrl = websiteBaseUrl + pageUri;
    console.log("made -> (actualUrl) " + actualUrl);
    window.open(actualUrl, '_blank').focus();
}

document.addEventListener('DOMContentLoaded', function(event) {

    notifyMeAboutUserVisit();
    //populateConsolidatedCharts();
    loadSchemeInfo();
    //openPage(randomPageNumber);
});
