
if (document.getElementById("map")) {
    const map = L.map('map',  ).setView([37.93585208752015, 58.39120934103419], 13);
    const osm = L.tileLayer('http://95.85.127.213:8083/tile/{z}/{x}/{y}.png', {
    }).addTo(map);

    let marker;

    map.on("click", e => {
        let lat = e.latlng.lat;
        let lng = e.latlng.lng;

        let inputLat = document.getElementById("lat");
        let inputLng = document.getElementById("lng");

        if(marker){
            marker.setLatLng([lat, lng]);
        }else{
            marker = L.marker([lat, lng]);
        }

        inputLat.value = lat;
        inputLng.value = lng;
        marker.addTo(map);
    });

    function showOnMap(){
        let inputLat = document.getElementById("lat").value;
        let inputLng = document.getElementById("lng").value;

        if(marker){
            marker.setLatLng([inputLat, inputLng]);
        }else{
            marker = L.marker([inputLat, inputLng]);
        }

        marker.addTo(map);
    }
}