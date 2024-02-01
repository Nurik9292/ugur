
if (document.getElementById("map-stop")) {
    const map = L.map('map-stop').setView([37.93585208752015, 58.39120934103419], 13);
    const osm = L.tileLayer('http://95.85.127.213:8083/tile/{z}/{x}/{y}.png', {
    }).addTo(map);

    let marker;

    window.addEventListener("load", function (){
        let inputLat = document.getElementById("lat").value;
        let inputLng = document.getElementById("lng").value;

        if(marker){
            marker.setLatLng([inputLat, inputLng]);
        }else{
            marker = L.marker([inputLat, inputLng]);
        }

        marker.addTo(map);
    });

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

if(document.getElementById("map-route-front")){
    let coordinates = [];

    const inputFrontCoordinates = document.getElementById("frontCoordinates");
    const map = L.map('map-route-front', { pmIgnore: false }).setView([37.93585208752015, 58.39120934103419], 13);
    const osm = L.tileLayer('http://95.85.127.213:8083/tile/{z}/{x}/{y}.png', {
    }).addTo(map);


    map.pm.addControls({
        position: 'topleft',
    drawCircleMarker: false,
    drawMarker: false,
    drawRectangle: false,
    drawPolygon: false,
    drawCircle: false,
    drawText: false,
    dragMode: false,
    cutPolygon: false,
    rotateMode: false,
    });

    map.pm.setPathOptions({
        color: "red",
    fillColor: "re",
    fillOpacity: 0.8,
    });

    map.on("pm:drawstart", ({ workingLayer}) => {
        workingLayer.on("pm:vertexadded", (e) => {
            coordinates = e.layer.getLatLngs();
            inputFrontCoordinates.value = coordinates.join(",");
        });
    });

    map.on('pm:create', ({ layer}) => {
        layer.on('pm:edit', e => {
            e.layer.on("pm:vertexadded", e =>{
                coordinates = e.layer.getLatLngs();
                inputFrontCoordinates.value = coordinates.join(",");
            })
        });
    });

    map.on("pm:remove", (e) => {
        coordinates = [];
        inputFrontCoordinates.value = "";
    })
}
