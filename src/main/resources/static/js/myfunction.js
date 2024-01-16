function onChagePageItems(){
    const url='http://127.0.0.1:8080/stops';
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

function onClickSort(){
    const url='http://127.0.0.1:8080/stops';
    const element = data();

    const params = {
        page: element.page,
        items: element.items,
        sortBy: "name"
    };


    const queryString = Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');

    const fullUrl = `${url}?${queryString}`;
    window.location.href = fullUrl;
}

if(document.getElementById("search")){
    const search = document.getElementById("search");

    search.addEventListener("input", function(event) {
        const inputValue = event.target.value;

        const url = 'http://127.0.0.1:8080/stops/search';
        const params = {
            search: inputValue
        };

        const formData = new FormData();

        const queryString = Object.entries(params)
            .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
            .join('&');

        const fullUrl = `${url}?${queryString}`;
        search.onkeyup = function (e){
            console.log(e.code);
            if(e.code === "Enter"){
                window.location.href = fullUrl;
            }
        }

    });
}

function  data(){
    const selector = document.getElementById("selector");
    const items = selector.options[selector.selectedIndex].value;
    const active = document.querySelector("[class='active']");
    const pageNumber = active.querySelector("[data-page]").textContent;

    return {page: pageNumber, items: items};
}