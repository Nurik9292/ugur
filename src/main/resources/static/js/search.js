const search = document.getElementById("search");
const searchPlace = document.getElementById("search-place");
const table = document.getElementById("datatable");

if(search && table){
    const searchInput = search;
    const dataTable = table;
    const tableRows = dataTable.querySelectorAll("tbody tr");

    searchInput.addEventListener("input", () => {
        const searchTerm = searchInput.value.toLowerCase();
        filterTableRows(searchTerm);
    });

    function filterTableRows(searchTerm) {
        tableRows.forEach((row) => {
            const cells = row.querySelectorAll("td");
            let matchFound = false;

            cells.forEach((cell) => {
                const cellText = cell.textContent.toLowerCase();
                if (cellText.includes(searchTerm)) {
                    matchFound = true;
                    return;
                }
            });

            if (matchFound) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    }
}

if (searchPlace && table) {
    searchPlace.addEventListener("input", debounce(function (){
        const search = this.value;
        const url = `${host}/places?search=${encodeURIComponent(search)}`;
        if(search.length >= 3)
            window.location.href = url;

    }, 500));

}

function debounce(func, delay) {
    let timer;

    return function () {
        if (timer) {
            clearTimeout(timer);
        }

        timer = setTimeout(() => {
            func.apply(this, arguments);
        }, delay);
    };
}



const searchInputFront = document.getElementById('search-stop-front');
const listItemsFront = document.querySelectorAll('#front_id_source_list li');
const searchInputBack = document.getElementById('search-stop-back');
const listItemsBack = document.querySelectorAll('#back_id_source_list li');

function filterListFront() {
    const inputValue = searchInputFront.value.toLowerCase();

    listItemsFront.forEach(item => {
        const itemText = item.textContent.toLowerCase();

        if (itemText.indexOf(inputValue) !== -1) {
            item.style.display = 'list-item';
        } else {
            item.style.display = 'none';
        }
    });
}

function filterListBack() {
    const inputValue = searchInputBack.value.toLowerCase();

    listItemsBack.forEach(item => {
        const itemText = item.textContent.toLowerCase();

        if (itemText.indexOf(inputValue) !== -1) {
            item.style.display = 'list-item';
        } else {
            item.style.display = 'none';
        }
    });
}

if (searchInputFront)
    searchInputFront.addEventListener('input', filterListFront);
if (searchInputBack)
    searchInputBack.addEventListener('input', filterListBack);
