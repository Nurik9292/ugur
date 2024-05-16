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
        const url = `${host}/search/places?search=${encodeURIComponent(search)}`;
        const tableRows = table.querySelectorAll("tbody tr");
        const tableBody =  table.querySelector("tbody");
        console.log(search)
        if(search.length >= 3){

            axios({
                method: "get",
                url: url,
            }).then(async res => {
                console.log(res)
                console.log(tableBody)
                tableRows.forEach((row) => {
                    const cells = row.querySelectorAll("td");
                    row.style.display = "none";
                })
                for (const item of res.data) {
                    const row = await createTableRow(item);
                    tableBody.appendChild(row);
                }


            }).catch(err => {
                console.log(err)
            });
        }
    }, 500));

}

function createTableRow(data) {

    const svgEdit = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svgEdit.classList.add("icon");
    svgEdit.classList.add("icon-xs");
    svgEdit.classList.add("text-success");
    svgEdit.classList.add("me-2");
    svgEdit.setAttribute("fill", "none");
    svgEdit.setAttribute("stroke-width", "1.5");
    svgEdit.setAttribute("stroke", "currentColor");
    svgEdit.setAttribute("viewBox", "0 0 24 24");
    svgEdit.setAttribute("xmlns", "http://www.w3.org/2000/svg");
    svgEdit.setAttribute("aria-hidden", "true");
    const svgPathEdit = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    svgPathEdit.setAttribute("stroke-linecap", "round");
    svgPathEdit.setAttribute("stroke-linejoin", "round");
    svgPathEdit.setAttribute("d", "m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L6.832 19.82a4.5 4.5 0 0 1-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 0 1 1.13-1.897L16.863 4.487Zm0 0L19.5 7.125");
    svgEdit.appendChild(svgPathEdit);

    const svgDelete = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svgDelete.classList.add("icon");
    svgDelete.classList.add("icon-xs");
    svgDelete.classList.add("text-danger");
    svgDelete.classList.add("me-2");
    svgDelete.setAttribute("fill", "none");
    svgDelete.setAttribute("stroke-width", "1.5");
    svgDelete.setAttribute("stroke", "currentColor");
    svgDelete.setAttribute("viewBox", "0 0 24 24");
    svgDelete.setAttribute("xmlns", "http://www.w3.org/2000/svg");
    svgDelete.setAttribute("aria-hidden", "true");
    const svgPathDelete = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    svgPathDelete.setAttribute("stroke-linecap", "round");
    svgPathDelete.setAttribute("stroke-linejoin", "round");
    svgPathDelete.setAttribute("d", "m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0");
    svgDelete.appendChild(svgPathDelete);


    const tr = document.createElement("tr");

    const tdTitle = document.createElement("td");
    const divTitle = document.createElement("div");
    const pTitle = document.createElement("p");

    const tdAddress = document.createElement("td");
    const divAddress = document.createElement("div");
    const pAddress = document.createElement("p");

    const tdImage = document.createElement("td");
    const divImage = document.createElement("div");
    const tdThumb = document.createElement("td");

    const imgThumb = document.createElement("img");

    const tdPhone = document.createElement("td");
    const tdWebsite = document.createElement("td");
    const tdSocial = document.createElement("td");
    const tdEmail = document.createElement("td");

    const tdButton = document.createElement("td");
    const aEdit = document.createElement("a");

    const formDelete = document.createElement("form");
    const buttonDelete = document.createElement("button");
    const inputCsrfDelete = document.createElement("input");
    const inputDeleteMethod = document.createElement("input");

    formDelete.action = `/places/${data.id}`;
    formDelete.method = "post";
    inputCsrfDelete.name = csrfName;
    inputCsrfDelete.value = csrfVale;
    inputCsrfDelete.type = "hidden";
    formDelete.appendChild(inputCsrfDelete);
    inputDeleteMethod.name = "_method";
    inputDeleteMethod.value = "DELETE";
    inputDeleteMethod.type = "hidden";
    formDelete.appendChild(inputDeleteMethod);
    buttonDelete.classList.add("btn");
    buttonDelete.classList.add("btn-sm");
    buttonDelete.classList.add("btn-outline-danger");
    buttonDelete.classList.add("svg");
    buttonDelete.style.marginLeft = "2px";
    buttonDelete.appendChild(svgDelete);
    buttonDelete.textContent = "Удалить";
    formDelete.appendChild(buttonDelete);


    pTitle.textContent = data.titles.ru;
    divTitle.appendChild(pTitle);
    tdTitle.appendChild(divTitle);

    pAddress.textContent = data.address.ru;
    divAddress.appendChild(pAddress);
    tdAddress.appendChild(divAddress);


    divImage.classList.add("image-container");
    data.images.forEach(im => {
        const imagePth = im.path.split('/').pop();
        const img = document.createElement("img");
        img.classList.add("image-item");
        img.src = `images/places/${imagePth}`;
        divImage.appendChild(img);
    });
    tdImage.appendChild(divImage);
    if (data.thumb != null) {
        const thumbPath = data.thumb.path.split('/').pop();
        imgThumb.src = `images/place/thumb/${thumbPath}`;
    }
    tdThumb.appendChild(imgThumb);

    data.phones.forEach(phone => {
        const pPhone = document.createElement("p");
        pPhone.classList.add("p_text");
        pPhone.textContent = phone.number;
        tdPhone.appendChild(pPhone);
    })

    tdPhone.classList.add("p_text");
    tdWebsite.textContent = data.website === null ? "" : data.website;

    data.socialNetworks.forEach(social => {
        const pSocial = document.createElement("p");
        pSocial.classList.add("p_text");
        pSocial.textContent = social.link;
        tdSocial.appendChild(pSocial);
    });

    tdEmail.textContent = data.email === null ? "" : data.email;

    aEdit.classList.add("btn");
    aEdit.classList.add("btn-sm");
    aEdit.classList.add("btn-outline-success");
    aEdit.classList.add("svg");
    aEdit.style.marginRight = "20px";
    aEdit.href = `/places/${data.id}/edit`;
    aEdit.appendChild(document.createElement("img"));
    aEdit.textContent = "Изменить";

    tdButton.classList.add("d-inline-flex");
    tdButton.classList.add("align-content-between");



    tdButton.appendChild(aEdit);
    tdButton.appendChild(formDelete);

    tr.appendChild(tdTitle);
    tr.appendChild(tdAddress);
    tr.appendChild(tdImage);
    tr.appendChild(tdThumb);
    tr.appendChild(tdPhone);
    tr.appendChild(tdWebsite);
    tr.appendChild(tdSocial);
    tr.appendChild(tdEmail);
    tr.appendChild(tdButton);

    console.log(tr);

    return tr;
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
