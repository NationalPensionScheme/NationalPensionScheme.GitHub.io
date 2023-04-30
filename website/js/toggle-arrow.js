
var toggler = document.getElementsByClassName("toggle-arrow");
var i;

for (i = 0; i < toggler.length; i++) {
    toggler[i].addEventListener("click", function() {
        this.parentElement.querySelector(".nested-list").classList.toggle("active-list");
        this.classList.toggle("toggle-down");
    });
}
