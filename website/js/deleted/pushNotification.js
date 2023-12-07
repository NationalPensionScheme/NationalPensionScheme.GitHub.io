
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

function notifyMeAboutUserVisit() {

    console.log("###################################################");
    console.log("notify me about user visit");

    //getClientDetails("https://staging.qring.tech/location", transform_QRING_data);
    getClientDetails("https://ipapi.co/json/", transform_IP_API_data);
}

