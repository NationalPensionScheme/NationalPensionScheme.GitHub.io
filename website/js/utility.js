
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
