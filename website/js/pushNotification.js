
let pushNotificationApiUrl = "https://fcm.googleapis.com/fcm/send";

let notifyMeAboutUserVisitData = {
    title: "Turquoise Space - National Pension Scheme",
    message: "User Visit on Website",
    link: "https://TurquoiseSpace.GitHub.io/National-Pension-Scheme/",
};

let notifyMeAboutUserInputMessageData = {
    title: "Turquoise Space - National Pension Scheme",
    message: "User Contact from Website",
    link: "https://TurquoiseSpace.GitHub.io/National-Pension-Scheme/",
};

let authorization = "key=AAAAucpu21Y:APA91bEYxJ6XGquZzAxqJnyUYeSwi7ocOWO4iJwi676vXnRXrDn-TazsJmESzQHHv5Dx2OO3HFOk5moxzpvEPCMCv3UA5ZopDnVFBK6lOeE1qWQugDVabmN229DAXd3G7tdul7mmlYpF";

let notifyMeAt = "crGsJs5hSfevCbEEwcuYBs:APA91bE87fjhj2pdFYvMJ2CQGk7NGnK2x8GBJ_7rQm4lXaqwIeVqEQ_khcAvpAoJrgeqPyWoI5dwIM_IRifzCoxlIN_xb3LtuwucrYmelgSA3NCmcufmBaCKWki-3RgbZTz4riZrdG2b";


function triggerGooglePushNotification(pushNotificationStringBody, alertUserFlag) {

    console.log("trigger google push notification -> (pushNotificationStringBody) ", pushNotificationStringBody);

    let pushNotificationHttpRequest = new XMLHttpRequest();

    pushNotificationHttpRequest.onreadystatechange = function() {

        console.log("request state change -> (readyState) " + this.readyState + " (status) " + this.status);
        if (this.readyState == 4 && this.status == 200) {

            let pushNotificationResponse = pushNotificationHttpRequest.responseText;
            console.log("got -> (pushNotificationResponse) " + pushNotificationResponse);

            if (alertUserFlag) {
                alert("Thank you for writing to us !" + "\n" + "We will get back to you shortly.");
            }
        }
    };

    pushNotificationHttpRequest.open("POST", pushNotificationApiUrl, true);

    pushNotificationHttpRequest.setRequestHeader("Content-Type", "application/json");
    pushNotificationHttpRequest.setRequestHeader("Authorization", authorization);

    pushNotificationHttpRequest.send(pushNotificationStringBody);
}

function sendMeUserVisitPushNotification(clientMeaningfulDetails) {

    let pushNotificationData = JSON.parse(JSON.stringify(notifyMeAboutUserVisitData));
    console.log("cloned -> (pushNotificationData) " + JSON.stringify(pushNotificationData));

    pushNotificationData.info = clientMeaningfulDetails;
    console.log("got -> (pushNotificationData) " + JSON.stringify(pushNotificationData));

    let pushNotificationBody = {
        to: notifyMeAt,
        data: pushNotificationData
    };
    console.log("built -> (pushNotificationBody) ", JSON.stringify(pushNotificationBody));

    triggerGooglePushNotification(JSON.stringify(pushNotificationBody), false);
}

function extractInner(description, innerObj) {

    console.log("extract inner -> (description) " + description + " (innerObj) ", innerObj);

    let parsedInner = {};

    if (innerObj.city) {
        parsedInner.city = innerObj.city.names.en;
    }
    if (innerObj.continent) {
        parsedInner.continentCode = innerObj.continent.code;
        parsedInner.continent = innerObj.continent.names.en;
    }
    if (innerObj.country) {
        parsedInner.countryISO = innerObj.country.iso_code;
        parsedInner.country = innerObj.country.names.en;
    }
    if (innerObj.location) {
        parsedInner.location = innerObj.location;
    }
    if (innerObj.postal) {
        parsedInner.postalCode = innerObj.postal.code;
    }
    if (innerObj.registered_country) {
        parsedInner.regCountryISO = innerObj.registered_country.iso_code;
        parsedInner.regCountry = innerObj.registered_country.names.en;
    }
    if (innerObj.subdivisions) {
        let parsedSubDivisions = [];
        for (let subDivisionIndex = 0; subDivisionIndex < innerObj.subdivisions.length; subDivisionIndex++) {
            let subDivision = innerObj.subdivisions[subDivisionIndex];
            let parsedSubDivision = {};
            if (subDivision) {
                parsedSubDivision.divisionISO = subDivision.iso_code;
                parsedSubDivision.division = subDivision.names.en;
                parsedSubDivisions.push(parsedSubDivision);
            }
        }
        parsedInner.subDivisions = parsedSubDivisions;
    }
    if (innerObj.traits) {
        parsedInner.cityTraits = innerObj.traits;
    }

    console.log("got -> (parsedInner) ", parsedInner);

    return parsedInner;
}

function extractGeoIpMaxMind(mode, maxMindObj) {

    console.log("extract geo ip max mind -> (mode) " + mode + " (maxMindObj) " + maxMindObj);

    let parsed = {};

    if (maxMindObj.AnonymousIpResponse) {
        parsed.AnonymousIpResponse = maxMindObj.AnonymousIpResponse;
    }

    if (maxMindObj.AsnResponse) {
        parsed.AsnResponse = maxMindObj.AsnResponse;
    }

    if (maxMindObj.CityResponse) {
        parsed.cityResponse = extractInner("city", maxMindObj.CityResponse);
    }

    if (maxMindObj.ConnectionTypeResponse) {
        parsed.ConnectionTypeResponse = maxMindObj.ConnectionTypeResponse;
    }

    if (maxMindObj.CountryResponse) {
        let countryResponse = {};

        if (maxMindObj.CountryResponse.continent) {
            countryResponse.continentCode = maxMindObj.CountryResponse.continent.code;
            countryResponse.continent = maxMindObj.CountryResponse.continent.names.en;
        }
        if (maxMindObj.CountryResponse.country) {
            countryResponse.countryISO = maxMindObj.CountryResponse.country.iso_code;
            countryResponse.country = maxMindObj.CountryResponse.country.names.en;
        }
        if (maxMindObj.CountryResponse.registered_country) {
            countryResponse.regCountryISO = maxMindObj.CountryResponse.registered_country.iso_code;
            countryResponse.regCountry = maxMindObj.CountryResponse.registered_country.names.en;
        }
        if (maxMindObj.CountryResponse.traits) {
            countryResponse.countryTraits = maxMindObj.CountryResponse.traits;
        }

        parsed.countryResponse = countryResponse;
    }

    if (maxMindObj.DomainResponse) {
        parsed.DomainResponse = maxMindObj.DomainResponse;
    }

    if (maxMindObj.EnterpriseResponse) {
        parsed.EnterpriseResponse = maxMindObj.EnterpriseResponse;
    }

    if (maxMindObj.InsightsResponse) {
        parsed.insightsResponse = extractInner("insights", maxMindObj.InsightsResponse);
    }

    if (maxMindObj.IspResponse) {
        parsed.IspResponse = maxMindObj.IspResponse;
    }

    console.log("got -> (parsed) " + parsed);

    return parsed;
}

function transform_QRING_data(obj) {

    let actualData = {};

    let ip = obj.data.IP;
    let iNetAddress = obj.data.InetAddress;
    let online = obj.data.ONLINE_STATS;
    let offline = obj.data.OFFLINE_STATS;

    if (ip) {
        actualData.ip = ip;
    }

    if (iNetAddress) {
        actualData.iNetAddress = iNetAddress;
    }

    if (online) {
        actualData.online = extractGeoIpMaxMind("online", online);
    }

    if (offline) {
        actualData.offline = extractGeoIpMaxMind("offline", offline);
    }

    return actualData;
}

function transform_IP_API_data(obj) {

    return obj;
}

function getClientDetails(clientInfoApiUrl, transformer) {

    console.log("get client details -> (clientInfoApiUrl) ", clientInfoApiUrl);

    let clientInfoHttpRequest = new XMLHttpRequest();

    clientInfoHttpRequest.onreadystatechange = function() {

        console.log("request state change -> (readyState) " + this.readyState + " (status) " + this.status);

        if (this.readyState == 4 && this.status == 200) {

            let clientInfoResponseString = clientInfoHttpRequest.responseText;
            console.log("received -> (clientInfoResponseString) " + clientInfoResponseString);

            let clientDetails = JSON.parse(clientInfoResponseString);
            console.log("got -> (clientDetails) " + JSON.stringify(clientDetails));

            let transformedObject = transformer(clientDetails);
            console.log("got -> (transformedObject) " + JSON.stringify(transformedObject));

            sendMeUserVisitPushNotification(transformedObject);
        }
    };

    clientInfoHttpRequest.open("GET", clientInfoApiUrl, true);

    clientInfoHttpRequest.send();
}

function notifyMeAboutUserVisit() {

    console.log("###################################################");
    console.log("notify me about user visit");

    getClientDetails("https://staging.qring.tech/location", transform_QRING_data);
    getClientDetails("https://ipapi.co/json/", transform_IP_API_data);
}

function extractUserInputData() {

    console.log("extract user input data");

    let userInputData = {};

    userInputData.fullName = document.getElementById("fullName").value;
    userInputData.emailId = document.getElementById("emailId").value;
    userInputData.phoneNumber = document.getElementById("phoneNumber").value;
    userInputData.message = document.getElementById("message").value;

    console.log("got -> (userInputData) ", JSON.stringify(userInputData));
    return userInputData;
}

function constructPushNotificationData(userInputData) {

    let pushNotificationData = JSON.parse(JSON.stringify(notifyMeAboutUserInputMessageData));
    console.log("cloned -> (pushNotificationData) " + JSON.stringify(pushNotificationData));

    pushNotificationData.contact = userInputData;
    console.log("got -> (pushNotificationData) " + JSON.stringify(pushNotificationData));

    return pushNotificationData;
}

function constructPushNotificationBody(userSpecificData) {

    let pushNotificationBody = {
        to: notifyMeAt,
        data: userSpecificData
    };

    console.log("built -> (pushNotificationBody) ", JSON.stringify(pushNotificationBody));
    return pushNotificationBody;
}

function notifyMeAboutUserMessage() {

    console.log("###################################################");
    console.log("notify me about user message");

    let userInputData = extractUserInputData();
    let userSpecificData = constructPushNotificationData(userInputData);
    let pushNotificationBody = constructPushNotificationBody(userSpecificData);

    triggerGooglePushNotification(JSON.stringify(pushNotificationBody), true);
}

