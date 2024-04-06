const host = "http://192.168.37.61:8083";
// const host = "http://localhost:8080";
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

function addToFormData(formData, key, value) {
    if (Array.isArray(value)) {
        value.forEach(val => formData.append(key, val));
    } else {
        formData.append(key, value);
    }
}

function sendFormData(method, url, formData) {
    axios({
        method: method,
        url: url,
        data: formData,
        headers: {
            "Content-Type": "multipart/form-data",
        },
    })
        .then(res => {
            window.location.href = host + "/places";
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

function sendForm(method, url) {
    const formData = new FormData();
    const images = document.getElementById("image").files;
    const prev = document.getElementById("prev").files[0];
    const instagram = document.getElementById("instagram").value;
    const tiktok = document.getElementById("tiktok").value;
    const cityPhone = document.getElementById("city_phone").value;
    const mobPhones = document.getElementsByClassName("mob_phone_place");
    const phones = Array.from(mobPhones).map(phone => phone.value);

    for (let i = 0; i < images.length; i++) {
        formData.append('files', images[i]);
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
    addToFormData(formData, "lat", document.getElementById("lat").value);
    addToFormData(formData, "lng", document.getElementById("lng").value);
    addToFormData(formData, "placeCategory", document.getElementById("placeCategory").value);
    addToFormData(formData, "placeSubCategory", document.getElementById("placeSubCategory").value);
    addToFormData(formData, "instagram", instagram);
    addToFormData(formData, "tiktok", tiktok);
    addToFormData(formData, "telephones", phones);
    addToFormData(formData, "cityPhone", cityPhone);
    addToFormData(formData, "prev", prev);

    sendFormData(method, url, formData);
}

function sendCreatePlace() {
    sendForm('post', host + "/places");
}

function sendUpdatePlace() {
    const id = document.getElementById("id").value;
    sendForm('put', host + "/places/" + id);
}

function displayValidationErrors(errors) {
    Object.keys(errors).forEach(fieldName => {
        const errorMessage = errors[fieldName];
        // const errorMessage = errors[fieldName].join(', ');
        const errorElement =  document.getElementById(fieldName + "-error");
        if (errorElement) {
            errorElement.textContent = errorMessage;
        }
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


const placeCategorySelect = document.getElementById("placeCategory");

if(placeCategorySelect && placeCategorySelect.value){
    const categoryId = placeCategorySelect.value;
    loadSubcategories(categoryId);
}


if (placeCategorySelect) {
    placeCategorySelect.addEventListener("change", function () {
        const categoryId = this.value;
        loadSubcategories(categoryId);
    });
}

