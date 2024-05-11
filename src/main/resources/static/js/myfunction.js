// const host = "http://95.85.127.56:8083";
// const host = "http://192.168.37.61:8083";
const host = "http://localhost:8080";
let sortByStop = "";
let sortByRoute = "";

function onChagePageItems(page) {
    const url = host + '/' + page;
    const element = data();

    const params = {
        page: element.page,
        items: element.items
    };

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

function onClickSortPlace(sortBy) {
    const url = host + '/places';
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

function onClickSortPlaceCategory(sortBy) {
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

function data() {
    const selector = document.getElementById("selector");
    const items = selector.options[selector.selectedIndex].value;
    const active = document.querySelector("[class='active']");
    const pageNumber = active.querySelector("[data-page]").textContent;

    return {page: pageNumber, items: items};
}

function addInputMobPhone() {
    const addInputButton = document.getElementById("add-input");
    let mobPhoneInput = document.getElementById("mob_phone");
    const newInput = document.createElement("input");
    const newBr = document.createElement("br");

    if(mobPhoneInput == null)
        mobPhoneInput = document.getElementById("notPhone");

    newInput.classList.add("form-control");
    newInput.classList.add("mob_phone_place");
    newInput.type = "text";
    newInput.placeholder = "Введите другую моб телефон";
    newInput.required = true;
    jQuery(function($){
        $(".mob_phone_place").mask("+993(69) 99-99-99");
    });

    mobPhoneInput.parentNode.insertBefore(newInput, mobPhoneInput.nextSibling);
}


function sendCreateRoute() {
    sendFromRoute("post", host + "/routes");
}

function sendUpdateRoute() {
    const id = document.getElementById("id").value;
    sendFromRoute("put", host + "/routes/" + id);
}

function sendFromRoute(method, url) {
    const frontStartStops = document.getElementById('front_id_target_list')
        .querySelectorAll('li.p-picklist-item');
    const backStartStops = document.getElementById('back_id_target_list')
        .querySelectorAll('li.p-picklist-item');
    const city = document.getElementById("city");

    let frontStartStopIds = [];
    let backStartStopIds = [];

    frontStartStops.forEach(startStop => {
        frontStartStopIds.push(startStop.id);
    });

    backStartStops.forEach(startStop => {
        backStartStopIds.push(startStop.id);
    });

    const formData = new FormData();
    addToFormData(formData, "_csrf", document.getElementById("csrf").value);
    addToFormData(formData, 'name', document.getElementById('name').value);
    addToFormData(formData, 'number', document.getElementById("number").value);
    addToFormData(formData, 'frontCoordinates', document.getElementById('frontCoordinates').value);
    addToFormData(formData, 'backCoordinates', document.getElementById('backCoordinates').value);
    addToFormData(formData, 'startStops', frontStartStopIds);
    addToFormData(formData, 'endStops', backStartStopIds);
    addToFormData(formData, "city", city.options[city.selectedIndex].value);

    sendFormData(method, url, formData, "/routes");
}



function sendCreatePlace() {
    sendForm("post", host + "/places");
}

function sendUpdatePlace() {
    const id = document.getElementById("id").value;
    sendForm('put', host + "/places/" + id);
}

function sendForm(method, url) {
    const formData = new FormData();
    const images = myDropzone.getAcceptedFiles();
    const prev = document.getElementById("prev").files[0];
    const instagram = document.getElementById("instagram").value;
    const tiktok = document.getElementById("tiktok").value;
    const cityPhone = document.getElementById("city_phone").value;
    const mobPhones = document.getElementsByClassName("mob_phone_place");
    const phones = Array.from(mobPhones).map(phone => phone.value);
    const lat = document.getElementById("lat").value;
    const lng = document.getElementById("lng").value;

    if(removedImageIds.length !== 0){
        addToFormData(formData, "removeImageIds",removedImageIds);
    }


    addToFormData(formData, "_csrf", document.getElementById("csrf").value);
    addToFormData(formData, "title_tm", document.getElementById("title_tm").value);
    addToFormData(formData, "title_ru", document.getElementById("title_ru").value);
    addToFormData(formData, "title_en", document.getElementById("title_en").value);
    addToFormData(formData, "address_tm", document.getElementById("address_tm").value);
    addToFormData(formData, "address_ru", document.getElementById("address_ru").value);
    addToFormData(formData, "address_en", document.getElementById("address_en").value);
    addToFormData(formData, "email", document.getElementById("email").value);
    addToFormData(formData, "website", document.getElementById("site").value);
    addToFormData(formData, "lat", lat);
    addToFormData(formData, "lng", lng);
    addToFormData(formData, "placeCategory", document.getElementById("placeCategory").value);
    addToFormData(formData, "placeSubCategory", document.getElementById("placeSubCategory").value);
    addToFormData(formData, "instagram", instagram);
    addToFormData(formData, "tiktok", tiktok);
    addToFormData(formData, "telephones", phones);
    addToFormData(formData, "files", images)
    addToFormData(formData, "cityPhone", cityPhone);
    addToFormData(formData, "prev", prev);

    if(lat && lng)
      sendFormData(method, url, formData, "/places");
}

function addToFormData(formData, key, value) {
    if (Array.isArray(value)) {
        value.forEach(val => formData.append(key, val));
    } else {
        formData.append(key, value);
    }
}


function sendFormData(method, url, formData, indexPage) {
    return axios({
        method: method,
        url: url,
        data: formData,
        headers: {
            "Content-Type": "multipart/form-data",
        },
    })
        .then(res => {
            showNotification(res.data, "blue");

            setTimeout(function() {
                window.location.href = host + indexPage;
            }, 2000);

        })
        .catch(error => {
            if (error.response && error.response.status === 400) {
                console.error('Validation error:', error.response.data);
                displayValidationErrors(error.response.data);
            } else {
                console.error("Error sending request:", error);
            }
        });
}



function displayValidationErrors(errors) {
    const errorElements = [];

    Object.keys(errors).forEach(fieldName => {
        const errorMessage = errors[fieldName];
        const errorElement =  document.getElementById(fieldName + "-error");
        errorElements.push(errorElement);
            if (errorElement) {
                errorElement.textContent = errorMessage;
                showNotification(errorMessage, 'red');
            }
    });

    const errs = document.querySelectorAll('.error');
    errs.forEach(error => {
        if(!errorElements.includes(error))
            error.textContent = "";
    });
}

function showNotification(message, color){
    const notyf = new Notyf({
        position: {
            x: 'right',
            y: 'top',
        },
        types: [
            {
                type: 'info',
                background: color,
                icon: {
                    className: 'fas fa-info-circle',
                    tagName: 'span',
                    color: '#fff'
                },
                dismissible: false
            }
        ]
    });
    notyf.open({
        type: 'info',
        message: message
    });
}


function loadSubcategories(categoryId) {
    axios.get(host + "/place-categories/getSubcategories/" + categoryId)
        .then(res => {
            const response = res.data.map(item => ({
                id: item.id,
                title: item.titles.ru
            }));


            const placeSubCategorySelect = document.getElementById("placeSubCategory");
            placeSubCategorySelect.innerHTML = "";
            response.forEach(function (subCategory) {
                let option = document.createElement("option");
                option.value = subCategory.id;
                option.textContent = subCategory.title;
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


