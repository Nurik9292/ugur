const host = "http://127.0.0.1/:8080";
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

function  data(){
    const selector = document.getElementById("selector");
    const items = selector.options[selector.selectedIndex].value;
    const active = document.querySelector("[class='active']");
    const pageNumber = active.querySelector("[data-page]").textContent;

    return {page: pageNumber, items: items};
}

if(document.getElementById("nav-data-tab")){
    const divData = document.getElementById("data-route");
    const divFront = document.getElementById("front-route");
    const divBack = document.getElementById("front-route");
    document.getElementById("nav-data-tab").addEventListener("click", function (){

        if(!divFront.classList.contains("d-none")){
            divFront.classList.add("d-none")
        }
        if(!divBack.classList.contains("d-none")){
            divBack.classList.add("d-none")
        }

        divData.classList.remove("d-none");
    });
    document.getElementById("nav-front-tab").addEventListener("click", function () {
        if (!divData.classList.contains("d-none")) {
            divData.classList.add("d-none")
        }
        if (!divBack.classList.contains("d-none")) {
            divBack.classList.add("d-none")
        }

        divFront.classList.remove("d-none");
    });

    document.getElementById("nav-front-tab").addEventListener("click", function (){

        if(!divData.classList.contains("d-none")){
            divData.classList.add("d-none")
        }
        if(!divBack.classList.contains("d-none")){
            divBack.classList.add("d-none")
        }

        divBack.classList.remove("d-none");
    });
}