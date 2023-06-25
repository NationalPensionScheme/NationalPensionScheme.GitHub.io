
let googleAdElements = document.getElementsByClassName("adsbygoogle");

for (let elementIndex = 1; elementIndex < googleAdElements.length; elementIndex++) {

    //let googleAdElement = googleAdElements[elementIndex];

    (adsbygoogle = window.adsbygoogle || []).push({});

}

/*
jQuery(".adsbygoogle:visible").each(function () {
    (adsbygoogle = window.adsbygoogle || []).push({});
});
*/
