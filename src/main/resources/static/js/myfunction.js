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
    const mobPhoneInput = document.getElementById("mob_phone");
    const newInput = document.createElement("input");
    const newBr = document.createElement("br");
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

function sendCreatePlace() {
    const formData = new FormData();
    const image = document.getElementById("image").files[0];
    const instagram = document.getElementById("instagram").value;
    const tiktok = document.getElementById("tiktok").value;
    const cityPhone = document.getElementById("city_phone").value;
    const socialLinks = [instagram, tiktok];
    const mobPhones = document.getElementsByClassName("mob_phone_place");
    const phones = Array.from(mobPhones).map(phone => phone.value);
    phones.push(cityPhone);

    addToFormData(formData, "_csrf", document.getElementById("csrf").value);
    addToFormData(formData, "title", document.getElementById("title").value);
    addToFormData(formData, "address", document.getElementById("address").value);
    addToFormData(formData, "email", document.getElementById("email").value);
    addToFormData(formData, "website", document.getElementById("site").value);
    addToFormData(formData, "lat", document.getElementById("lat").value);
    addToFormData(formData, "lng", document.getElementById("lng").value);
    addToFormData(formData, "placeCategory", document.getElementById("placeCategory").value);
    addToFormData(formData, "placeSubCategory", document.getElementById("placeSubCategory").value);
    addToFormData(formData, "social_networks", socialLinks);
    addToFormData(formData, "telephones", phones);
    addToFormData(formData, "file", image);

    sendFormData('post', host + "/places", formData);
}

function sendUpdatePlace() {
    const formData = new FormData();
    const id = document.getElementById("id").value;
    const image = document.getElementById("image").files[0];
    const instagram = document.getElementById("instagram").value;
    const tiktok = document.getElementById("tiktok").value;
    const cityPhone = document.getElementById("city_phone").value;
    const socialLinks = [instagram, tiktok];
    const mobPhones = document.getElementsByClassName("mob_phone_place");
    const phones = Array.from(mobPhones).map(phone => phone.value);
    phones.push(cityPhone);

    addToFormData(formData, "_csrf", document.getElementById("csrf").value);
    addToFormData(formData, "title", document.getElementById("title").value);
    addToFormData(formData, "address", document.getElementById("address").value);
    addToFormData(formData, "email", document.getElementById("email").value);
    addToFormData(formData, "website", document.getElementById("site").value);
    addToFormData(formData, "lat", document.getElementById("lat").value);
    addToFormData(formData, "lng", document.getElementById("lng").value);
    addToFormData(formData, "placeCategory", document.getElementById("placeCategory").value);
    addToFormData(formData, "placeSubCategory", document.getElementById("placeSubCategory").value);
    addToFormData(formData, "social_networks", socialLinks);
    addToFormData(formData, "telephones", phones);
    addToFormData(formData, "file", image);

    sendFormData('put', host + "/places/" + id, formData);
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
            const response = res.data;
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
