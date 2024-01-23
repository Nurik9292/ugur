if(document.getElementById("search") && document.getElementById("datatable")){
    const searchInput = document.getElementById("search");
    const dataTable = document.getElementById("datatable");
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
