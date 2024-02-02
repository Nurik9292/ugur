
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
    const mapFront = L.map('map-route-front', { pmIgnore: false }).setView([37.93585208752015, 58.39120934103419], 13);
    const osmFront = L.tileLayer('http://95.85.127.213:8083/tile/{z}/{x}/{y}.png', {
    }).addTo(mapFront);

    document.getElementById("map-route-front").addEventListener("mousemove", e => {
        mapFront.invalidateSize()
    });

    mapFront.pm.addControls({
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

    mapFront.pm.setPathOptions({
        color: "red",
        fillColor: "red",
        fillOpacity: 0.8,
    });


    if(coordinatesEdit){
        let coors = [];
        coordinatesEdit.forEach(function(coordinate) {
            coors.push([coordinate.x, coordinate.y]);
        });
        let polyLine = L.polyline(coors).addTo(mapFront);
        mapFront.fitBounds(polyLine.getBounds());
        mapFront.setView([37.93585208752015, 58.39120934103419], 13)
        mapFront.pm.setPathOptions({
            color: "red",
            fillColor: "red",
            fillOpacity: 0.8,
        });

    }

    mapFront.on("pm:drawstart", ({ workingLayer}) => {
        workingLayer.on("pm:vertexadded", (e) => {
            coordinates = e.layer.getLatLngs();
            inputFrontCoordinates.value = coordinates.join(",");
        });
    });

    mapFront.on('pm:create', ({ layer}) => {
        layer.on('pm:edit', e => {
            e.layer.on("pm:vertexadded", e =>{
                coordinates = e.layer.getLatLngs();
                inputFrontCoordinates.value = coordinates.join(",");
            })
        });
    });

    mapFront.on("pm:remove", (e) => {
        coordinates = [];
        inputFrontCoordinates.value = "";
    })


}

