function onChagePageItems(){
    let selector = document.getElementById("selector");
    let items = selector.options[selector.selectedIndex].value;
    let active = document.querySelector("[class='active']");
    let pageNumber = active.querySelector("[data-page]").textContent;

    const url='http://127.0.0.1:8080/stops';
    const params = {
        page: pageNumber,
        items: items
    };

    const queryString = Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');

    const fullUrl = `${url}?${queryString}`;
    window.location.href = fullUrl;
}