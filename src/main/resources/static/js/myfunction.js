
let sortByStop = "";
let sortByRoute = "";

////////////////// SORT //////////////////////////////
function onChangePageItems(page) {
    const url = host + '/' + page;
    const element = data();

    const params = {
        page: element.page,
        items: element.items
    }

    const queryString = Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');

    const fullUrl = `${url}?${queryString}`;
    window.location.href = fullUrl;
}

function onClickSortStop() {
    const url = host + '/stops';
    const element = data();

    const params = {
        page: element.page,
        items: element.items,
        sortBy: "name"
    };

    sortByStop = "name";

    const queryString = Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');

    const fullUrl = `${url}?${queryString}`;
    window.location.href = fullUrl;
}

function onClickSortRoute(sortBy) {
    const url = host + '/routes';
    const element = data();

    const params = {
        page: element.page,
        items: element.items,
        sortBy: sortBy
    };

    sortByRoute = sortBy;

    const queryString = Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');

    const fullUrl = `${url}?${queryString}`;
    window.location.href = fullUrl;
}

function sortPlace(sortBy) {
    const url = host + '/places';
    const element = data();

    sortByPlace = sortBy;

    const params = {
        page: element.page,
        items: element.items,
        sortBy: sortBy,
        category: document.getElementById("category")
    };

    const queryString = Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');

    const fullUrl = `${url}?${queryString}`;
    window.location.href = fullUrl;
}

function sortPlaceCategory(sortBy) {
    const url = host + '/place-categories';
    const element = data();

    const params = {
        page: element.page,
        items: element.items,
        sortBy: sortBy
    };

    sortByRoute = sortBy;

    const queryString = Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');

    const fullUrl = `${url}?${queryString}`;
    window.location.href = fullUrl;
}

function filterCategory() {
    const url = host + '/places';
    const element = data();

    const params = {
        page: element.page,
        items: element.items,
        category: document.getElementById("category").value
    };

    const queryString = Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');

    const fullUrl = `${url}?${queryString}`;
    window.location.href = fullUrl;
}

function data() {
    const selector = document.getElementById("selector");
    const items = selector.options[selector.selectedIndex].value;
    const active = document.querySelector("[class='active']");
    const pageNumber = active.querySelector("[data-page]").textContent;

    return {page: pageNumber, items: items};
}


/////////////// ADD INPUT /////////////

function addInputMobPhone() {
    let mobPhoneInput = document.getElementById("mob_phone");
    const div = document.createElement("div");
    const span = document.createElement("span");
    const newInput = document.createElement("input");



    if(mobPhoneInput != null &&  mobPhoneInput.parentElement.classList.contains("input-group"))
        mobPhoneInput = mobPhoneInput.parentElement;


    if(mobPhoneInput == null)
        mobPhoneInput = document.getElementById("notPhone");

    newInput.classList.add("form-control");
    newInput.classList.add("mob_phone_place");
    newInput.type = "text";
    newInput.placeholder = "Введите другую моб телефон";
    newInput.required = true;

    span.classList.add("input-group-text");
    span.style.background = "red";
    span.style.color = "white";
    span.style.height = "40px";
    span.style.cursor = "pointer";
    span.textContent = "Удалить";
    span.addEventListener("click", function (){
        const parentElement = span.parentElement;
       parentElement.remove();
    });

    div.classList.add("input-group");

    div.appendChild(newInput);
    div.appendChild(span);
    mobPhoneInput.parentNode.insertBefore(div, mobPhoneInput.nextSibling);
}

const inputMob = document.querySelectorAll(".edit-mob");

if(inputMob) {
    inputMob.forEach(input => {
        input.addEventListener("click", function (){
            const parent = this.parentElement;
            parent.remove();
        });
    });
}

///////// END INPUT ///////////////////


function loadSubcategories(categoryId) {
    axios.get(host + "/place-categories/getSubcategories/" + categoryId)
        .then(res => {
            const response = res.data.map(item => ({
                id: item.id,
                title: item.titles.ru
            }));


            const placeSubCategorySelect = document.getElementById("placeSubCategory");
            const subCategoryTag = document.getElementById("subCategoryId");
            let subCategoryId= 0;

            console.log(subCategoryId)

            if(subCategoryTag)
                subCategoryId = subCategoryTag.value;

            placeSubCategorySelect.innerHTML = "";
            response.forEach(function (subCategory) {
                let option = document.createElement("option");
                option.value = subCategory.id;
                option.textContent = subCategory.title;
                option.selected = subCategoryId == subCategory.id;
                placeSubCategorySelect.appendChild(option);
            })
        }).catch(err => {
            if(err.response)
            console.error("Ошибка при загрузке подкатегорий:", err);
    });
}

function getImage() {

    if(document.getElementById("placeId")){
        const placeId = document.getElementById("placeId").value;
        let images = [];

        axios.get(host + "/places/images/" + placeId)
            .then(res => {
                const images = res.data;

               for(const id in images){
                   const inner = images[id];
                   for(const image in inner){
                       let mockFile = {id: id, name: image, size: inner[image] };
                       myDropzone.displayExistingFile(mockFile, host + "/images/places/" + image);
                   }

               }

            });
    }

}


const placeCategorySelect = document.getElementById("placeCategory");

if(placeCategorySelect && placeCategorySelect.value){
    const categoryId = placeCategorySelect.value;
    loadSubcategories(categoryId);
    getImage();
}


if (placeCategorySelect) {
    placeCategorySelect.addEventListener("change", function () {
        const categoryId = this.value;
        loadSubcategories(categoryId);
    });
}


const navImageTab = document.getElementById('nav-image-tab');

if(navImageTab){
    navImageTab.addEventListener('click', function(event) {
        event.preventDefault();
        const rows = document.querySelectorAll('.dis');
        for (const row of rows) {
            row.classList.add('d-none');
        }
    });
}


const navTmTab = document.getElementById('nav-tm-tab');

if(navTmTab) {
    navTmTab.addEventListener('click', function(event) {
        event.preventDefault();
        const rows = document.querySelectorAll('.dis');
        for (const row of rows) {
            row.classList.remove('d-none');
        }
    });
}


const navRuTab = document.getElementById('nav-ru-tab');

if(navRuTab) {
    navRuTab.addEventListener('click', function (event) {
        event.preventDefault();
        const rows = document.querySelectorAll('.dis');
        for (const row of rows) {
            row.classList.remove('d-none');
        }
    });
}

const navEnTab = document.getElementById('nav-en-tab');

if(navEnTab) {
    navEnTab.addEventListener('click', function (event) {
        event.preventDefault();
        const rows = document.querySelectorAll('.dis');
        for (const row of rows) {
            row.classList.remove('d-none');
        }
    });
}


