
let schemeInfoUrl = "https://raw.githubusercontent.com/NationalPensionScheme/NationalPensionScheme.GitHub.io/main/meta-data/pension-fund-schemes-v12.json";

let schemeInfo = [];
let schemeIdVsSchemeName = {};
let schemeIdVsManagerId = {};
let schemeIdVsManagerName = {};
let managerIdVsManagerName = {};

/*
let managerNameVsColour = {
    "SBI":"#0099c6",
    "UTI":"#dc3912",
    "LIC":"#ff9900",
    "IDFC":"#109618", // *
    "Kotak Mahindra":"#66aa00",
    "Reliance Capital":"#b82e2e", // *
    "ICICI Prudential":"#990099",
    "HDFC":"#dd4477",
    "DSP BlackRock":"#555555", // *
    "Aditya Birla Sun Life":"#3366cc",
};
*/

let managerNameVsColour = {
    "SBI":"#f58231",
    "UTI":"#911eb4",
    "LIC":"#e6194b",
    "IDFC":"#800000",
    "Kotak Mahindra":"#4363d8",
    "Reliance Capital":"#808000",
    "ICICI Prudential":"#ffe119",
    "HDFC":"#3cb44b",
    "DSP BlackRock":"#469990",
    "Aditya Birla Sun Life":"#42d4f4",
    "Tata":"#ff0000",
    "Max Life":"#00ff00",
    "Axis":"#0000ff",
    "DSP":"#00ff00",
};

function processSchemeInfo(schemeInfoContent) {

    console.log("fetched -> (schemeInfoContent)\n" + schemeInfoContent);

    schemeInfo = JSON.parse(schemeInfoContent);
    console.log("json parsed -> (schemeInfo) " + schemeInfo);
    console.log("identified -> (schemesMetaDataCount) " + schemeInfo.length);

    for (let schemeInfoIndex = 0; schemeInfoIndex < schemeInfo.length; schemeInfoIndex++) {
        let schemeMetaData = schemeInfo[schemeInfoIndex];
        console.log("iteration -> (index) " + schemeInfoIndex + " (schemeMetaData) " + JSON.stringify(schemeMetaData));

        let schemeId = schemeMetaData.id;
        let schemeName = schemeMetaData.scheme.value;
        let managerId = schemeMetaData.managerId.id;
        let managerName = schemeMetaData.managerId.manager.value;
        console.log("identified -> (schemeId) " + schemeId + " (schemeName) " + schemeName + " (managerId) " + managerId + " (managerName) " + managerName);

        schemeIdVsSchemeName[schemeId] = schemeName;
        schemeIdVsManagerId[schemeId] = managerId;
        schemeIdVsManagerName[schemeId] = managerName;
        managerIdVsManagerName[managerId] = managerName;
    }

    console.log("identified -> (schemeIdVsSchemeName) " + JSON.stringify(schemeIdVsSchemeName));
    console.log("identified -> (schemeIdVsManagerId) " + JSON.stringify(schemeIdVsManagerId));
    console.log("identified -> (schemeIdVsManagerName) " + JSON.stringify(schemeIdVsManagerName));
    console.log("identified -> (managerIdVsManagerName) " + JSON.stringify(managerIdVsManagerName));
}

function getSchemeNameFromSchemeId(schemeId) {

    console.log("get scheme name from scheme id -> (schemeId) " + schemeId);

    let schemeName = schemeIdVsSchemeName[schemeId];
    console.log("got -> (schemeName) " + schemeName);

    return schemeName;
}

function getManagerNameFromSchemeId(schemeId) {

    console.log("get manager name from scheme id -> (schemeId) " + schemeId);

    let managerName = schemeIdVsManagerName[schemeId];
    console.log("got -> (managerName) " + managerName);

    return managerName;
}

function getColour(schemeId) {

    console.log("get colour -> (schemeId) " + schemeId);

    let managerName = getManagerNameFromSchemeId(schemeId);

    let colour = managerNameVsColour[managerName];
    console.log("got -> (colour) " + colour);

    return colour;
}

function loadSchemeInfo() {

    console.log("###################################################");
    console.log("load scheme info");

    let schemeInfoFileHttpRequest = new XMLHttpRequest();
    schemeInfoFileHttpRequest.onreadystatechange = function() {
        console.log("request state change -> (readyState) " + this.readyState + " (status) " + this.status);
        if (this.readyState == 4 && this.status == 200) {
            let schemeInfoContent = schemeInfoFileHttpRequest.responseText;
            processSchemeInfo(schemeInfoContent);
        }
    };
    schemeInfoFileHttpRequest.open("GET", schemeInfoUrl, true);
    schemeInfoFileHttpRequest.send();
}
