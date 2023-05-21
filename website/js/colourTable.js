
function colourTablesEvenRows() {

    console.log("###################################################");
    console.log("colour tables even rows");

    let tableElements = document.getElementsByTagName("table");
    console.log("identified -> (tableElementsCount) " + tableElements.length);

    for (let tableIndex = 0; tableIndex < tableElements.length; tableIndex++) {
        let rowElements = tableElements[tableIndex].getElementsByTagName("tr");
        console.log("identified -> (rowElementsCount) " + rowElements.length);

        for (let rowIndex = 2; rowIndex < rowElements.length; rowIndex=rowIndex+2) {
            if (!rowElements[rowIndex].classList.contains("deprecated-row")) {
                rowElements[rowIndex].classList.add("even-row");
            }
        }
    }
}

colourTablesEvenRows();
