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
    searchPlace.addEventListener("input", function () {
        const search = this.value;
        const url = `${host}/search/places?search=${encodeURIComponent(search)}`;
        const tableRows = table.querySelectorAll("tbody tr");


        axios({
            method: "get",
            url: url,
        }).then(res => {
            tableRows.forEach((row) => {
                const cells = row.querySelectorAll("td");
                row.style.display = "none";
            })

                const row = createTableRow(res.data[0]);

                table.querySelector("tbody").appendChild(row);


        }).catch(err => {
            console.log(err)
        });

    });
}

function createTableRow(data) {

    const image1 = data.images.length > 0 ?  data.images[0].path.split('/').pop()  : "";
    const thumb = data.thumb !== null ? data.thumb.path.split('/').pop() : "";
    const phone1 = data.phones.length > 0 ? data.phones[0] : "";
    const phone2 = data.phones.length > 1 ? data.phones[1] : "";
    const phone3 = data.phones.length > 2 ? data.phones[2] : "";
    const scial1 = data.socialNetworks.length > 0 ? data.socialNetworks[0] : "";
    const scial2 = data.socialNetworks.length > 1 ? data.socialNetworks[1] : "";

    const rowTemplate = `
            <tr>
                <td>
                    <div>
                        <p>${data.titles.ru}</p>
                    </div>
                </td>
                <td>
                    <div>
                        <p>${data.address.ru}</p>
                    </div>
                </td>
                <td>
                    <div class="image-container">
                        <img src="images/places/${image1}" class="image-item" alt="Нет фото">
                    </div>
                </td>
                <td>
                    <img src="images/place/thumb/${thumb}" alt="">
                </td>
                <td class="p_text">
                    <p class="p_text">${phone1}</p>
                    <p class="p_text">${phone2}</p>
                    <p class="p_text">${phone3}</p>
                </td>
                <td>${data.website}</td>
                <td>
                     <p class="p_text">${scial1}</p>
                    <p class="p_text">${scial2}</p>
                </td>
                <td>${data.email}</td>
                <td class="d-inline-flex align-content-between">
                    <a class="btn btn-sm btn-outline-success svg" href="places/${data.id}/edit">
                          <svg class="icon icon-xs text-success me-2" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                                <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L6.832 19.82a4.5 4.5 0 0 1-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 0 1 1.13-1.897L16.863 4.487Zm0 0L19.5 7.125"></path>
                          </svg>
                        Изменить
                    </a>
                    <form th:action="@{/places/${data.id}" method="post">
                                            <input type="hidden" name="${csrfName}" value="${csrfVale}">
                                            <input type="hidden" name="_method" value="DELETE">
                                            <button class="btn btn-sm btn-outline-danger svg" type="submit" style="margin-left: 2px">
                                                <svg  class="icon icon-xs text-danger me-2" fill="none" stroke-width="1.5" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                                                    <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0"></path>
                                                </svg>
                                                Удалить
                    </button>
                    </form>
                </td>
            </tr>
        `;


    const row = document.createElement('tr');
    row.innerHTML = rowTemplate;

    return row;
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
