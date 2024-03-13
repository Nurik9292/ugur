const host = "http://192.168.37.61:8083";
// const host = "http://localhost:8080";
let sortByStop = "";
let sortByRoute = "";

function onChagePageItems(){
    const url= host + '/stops';
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

function onClickSortStop(){
    const url= host + '/stops';
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

function onClickSortRoute(sortBy){
    const url= host + '/routes';
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

function onClickSortPlace(sortBy){
    const url= host + '/places';
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

function onClickSortPlaceCategory(sortBy){
    const url= host + '/place-categories';
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

function  data(){
    const selector = document.getElementById("selector");
    const items = selector.options[selector.selectedIndex].value;
    const active = document.querySelector("[class='active']");
    const pageNumber = active.querySelector("[data-page]").textContent;

    return {page: pageNumber, items: items};
}

function addInputMobPhone(){
    const addInputButton = document.getElementById("add-input");
    const mobPhoneInput = document.getElementById("mob_phone");
    const newInput = document.createElement("input");
    const newBr = document.createElement("br");
    newInput.classList.add("form-control");
    newInput.classList.add("mob_phone_place");
    newInput.type = "text";
    newInput.placeholder = "Введите другую моб телефон";
    newInput.required = true;

    mobPhoneInput.parentNode.insertBefore(newInput, mobPhoneInput.nextSibling);
}

function sendCreatePlace(){
    const formDate = new FormData();

    const image = document.getElementById("image").files[0];


    const instagram = document.getElementById("instagram").value;
    const tiktok = document.getElementById("tiktok").value;
    const socialLinks = [instagram, tiktok];

    const cityPhone = document.getElementById("city_phone").value;

    const mobs = document.getElementsByClassName("mob_phone_place");
    const phones = [];

    for (let i = 0; i < mobs.length; i++) {
        phones.push(mobs[i].value);
    }

    phones.push(cityPhone);

    formDate.append("_csrf", document.getElementById("csrf").value);
    formDate.append("title", document.getElementById("title").value);
    formDate.append("address", document.getElementById("address").value);
    formDate.append("email", document.getElementById("email").value);
    formDate.append("website", document.getElementById("site").value);
    formDate.append("lat", document.getElementById("lat").value);
    formDate.append("lng", document.getElementById("lng").value);
    formDate.append("placeCategory", document.getElementById("placeCategory").value);
    formDate.append("social_networks", socialLinks);
    formDate.append("phones", phones);
    formDate.append("image", image);


    axios.post(host + "/places", formDate, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    }).then(re => {
        window.location.href = host + "/places";
    }) .catch((error) => {
        console.error("Error sending request:", error);
    });
}

function sendUpdatePlace(){
    const formDate = new FormData();

    const image = document.getElementById("image").files[0];
    const id = document.getElementById("placeId").value;


    const instagram = document.getElementById("instagram").value;
    const tiktok = document.getElementById("tiktok").value;
    const socialLinks = [instagram, tiktok];

    const cityPhone = document.getElementById("city_phone").value;

    const mobs = document.getElementsByClassName("mob_phone_place");
    const phones = [];

    for (let i = 0; i < mobs.length; i++) {
        phones.push(mobs[i].value);
    }

    phones.push(cityPhone);

    formDate.append("_csrf", document.getElementById("csrf").value);
    formDate.append("title", document.getElementById("title").value);
    formDate.append("address", document.getElementById("address").value);
    formDate.append("email", document.getElementById("email").value);
    formDate.append("website", document.getElementById("site").value);
    formDate.append("lat", document.getElementById("lat").value);
    formDate.append("lng", document.getElementById("lng").value);
    formDate.append("placeCategory", document.getElementById("placeCategory").value);
    formDate.append("social_networks", socialLinks);
    formDate.append("phones", phones);
    formDate.append("image", image);


    axios.put(host + "/places/" + id, formDate, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    }).then(re => {
        window.location.href = host + "/places";
    }) .catch((error) => {
        console.error("Error sending request:", error);
    });
}