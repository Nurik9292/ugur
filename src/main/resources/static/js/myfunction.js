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

    sendFormData(method, url, formData).then(res => {
        showNotification(res.data, "blue");

        setTimeout(function() {
            window.location.href = host + "/routes";
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



async function sendCreatePlace() {
   await sendForm("post", host + "/places");
}

async function sendUpdatePlace() {
    const id = document.getElementById("id").value;
   await sendForm('put', host + "/places/" + id);
}

async function sendForm(method, url) {
    const formData = new FormData();
    const csrf = document.getElementById("csrf").value;
    const lat = document.getElementById("lat").value;
    const lng = document.getElementById("lng").value;
    const images = myDropzone.getAcceptedFiles();

    if (!lat || !lng) {
        showNotification("Заполните координаты", 'red');
        return;
    }

    const isStoreTm = await storeTranslation(csrf, "tm", document.getElementById("title_tm").value,
        document.getElementById("address_tm").value);
    const isStoreRu = await storeTranslation(csrf, "ru", document.getElementById("title_ru").value,
        document.getElementById("address_ru").value);
    const isStoreEn = await storeTranslation(csrf, "en", document.getElementById("title_en").value,
        document.getElementById("address_en").value);

    const cityPhone = document.getElementById("city_phone").value;
    const mobPhones = document.getElementsByClassName("mob_phone_place");
    const phones = Array.from(mobPhones).map(phone => phone.value);



    addToFormData(formData, "_csrf", document.getElementById("csrf").value);
    addToFormData(formData, "lat", lat);
    addToFormData(formData, "lng", lng);
    addToFormData(formData, "placeCategory", document.getElementById("placeCategory").value);
    addToFormData(formData, "placeSubCategory", document.getElementById("placeSubCategory").value);
    addToFormData(formData, "files", images);
    addToFormData(formData, "prev", prev);


    if(isStoreTm && isStoreEn && isStoreRu) {
        sendFormData(method, url, formData).then(res => {
            showNotification(res.data, "blue");

            setTimeout(function() {
                window.location.href = host + "/places";
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


}

function checkPhone(phone) {
    let isPhone = false;
    if(Array.isArray(phone)){
        phone.forEach(val => {
            console.log(val)
            isPhone = val.startsWith("+993") && val.length === 12;
        });
    }else
        isPhone = phone.startsWith("+993") && phone.length === 12;

    return isPhone;
}

function checkAndShowNotification(field, message) {
    if (!field) {
        displayValidationErrors({ [field]: 'Заполните поле' });
        showNotification(message, 'red');
    }
}

function checkAndShowNotificationString(field, message) {

        displayValidationErrors({ [field]: 'Заполните поле правильно +993' });
        showNotification(message, 'red');

}

async function storeTranslation(csrf, locale, title, address) {
    const formDataTm = new FormData();

    addToFormData(formDataTm, '_csrf', csrf);
    addToFormData(formDataTm, "locale", locale);
    addToFormData(formDataTm, "title", title);
    addToFormData(formDataTm, "address", address);

   return await sendTranslation(formDataTm);

}

async function sendTranslation(formData) {
   let isStore = false;
   await sendFormDataNotImage("post", host + "/place-translations", formData).then(res => {
       isStore = true;
    }).catch(err => {
        isStore = false;
        displayValidationErrors(err.response.data, true);
    });

   return isStore;
}

function addToFormData(formData, key, value) {
    if (Array.isArray(value)) {
        value.forEach(val => formData.append(key, val));
    } else {
        formData.append(key, value);
    }
}


function sendFormData(method, url, formData) {
    return axios({
        method: method,
        url: url,
        data: formData,
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });
}

async function sendFormDataNotImage(method, url, formData) {
    return await axios({
        method: method,
        url: url,
        data: formData
    });
}



function displayValidationErrors(errors, isLocale) {
    const errorElements = [];

    Object.keys(errors).forEach(fieldName => {
        console.log(fieldName)
        let errorMessage = errors[fieldName];
        const errorElement =  document.getElementById(fieldName + "-error");
        errorElements.push(errorElement);
            if (errorElement) {
                errorElement.textContent = errorMessage;
                if(isLocale) {
                    console.log(fieldName.endsWith("tm"))
                    errorMessage = fieldName.endsWith("tm") ? "Tm " + errorMessage : errorMessage;
                    errorMessage = fieldName.endsWith("ru") ? "Ru " + errorMessage : errorMessage;
                    errorMessage = fieldName.endsWith("en") ? "En " + errorMessage : errorMessage;
                    console.log(errorMessage)
                }
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


const searchStopFront = document.getElementById('search-stop-front');

if(searchStopFront) {

}

