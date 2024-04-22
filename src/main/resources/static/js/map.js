
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

if (document.getElementById("map-place")) {
    const map = L.map('map-place').setView([37.93585208752015, 58.39120934103419], 13);
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

    let coordinatesFront = [];
    let mapFrontLayers = [];

    const inputFrontCoordinates = document.getElementById("frontCoordinates");
    const mapFront = L.map('map-route-front', { pmIgnore: false }).setView([37.93585208752015, 58.39120934103419], 13);
    const osmFront = L.tileLayer('http://95.85.127.213:8083/tile/{z}/{x}/{y}.png', {
    }).addTo(mapFront);

    document.getElementById("map-route-front").addEventListener("mousemove", e => {
        mapFront.invalidateSize();
    });

    mapFront.pm.addControls(
        {
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
        },
        {
            polyline: {
                simplifyFactor: 0,
            },
        }
    );

    mapFront.pm.setPathOptions({
        color: "red",
        fillColor: "red",
        fillOpacity: 0.8,
    });

    let polyFrontLine;

    if(coordinatesFrontEdit){
        let coors = [];
        coordinatesFrontEdit.forEach(function(coordinate) {
            coors.push([coordinate.x, coordinate.y]);
        });

        polyFrontLine = L.polyline(coors, { noClip: true});
        polyFrontLine.pm.enable();
        polyFrontLine.addTo(mapFront);
        // /mapFront.fitBounds(polyFrontLine.getBounds());

        mapFront.setView([37.93585208752015, 58.39120934103419], 13);
    }

    if(polyFrontLine){
        polyFrontLine.on('pm:edit', e => {
            mapFrontLayers = [];
            for (let key in mapFront._layers) {
                if (mapFront._layers[key].hasOwnProperty("_bounds") && !mapFront._layers[key].hasOwnProperty("_layers")) {
                    mapFrontLayers[key] = mapFront._layers[key];
                }
            }

            let arrCoordinates = [];
            mapFrontLayers.forEach(ls => {
                arrCoordinates =[...arrCoordinates, ...ls.getLatLngs()];
            })
            coordinatesFront = arrCoordinates;
            inputFrontCoordinates.value = coordinatesFront.join(",");
        });
    }


    mapFront.on('pm:create', ({ layer}) => {
        layer.on('pm:edit', e => {
            mapFrontLayers = [];
            for (let key in mapFront._layers) {
                if (mapFront._layers[key].hasOwnProperty("_bounds") && !mapFront._layers[key].hasOwnProperty("_layers")) {
                    mapFrontLayers[key] = mapFront._layers[key];
                }
            }

            let arrCoordinates = [];
            mapFrontLayers.forEach(ls => {
                arrCoordinates =[...arrCoordinates, ...ls.getLatLngs()];
            })
            coordinatesFront = arrCoordinates;
           x
            inputFrontCoordinates.value = coordinatesFront.join(",");
        });
    });


    mapFront.on('pm:drawend', (e) => {
        let i = 0;
        mapFrontLayers = [];
        for (let key in mapFront._layers) {
            if (mapFront._layers[key].hasOwnProperty("_bounds") && !mapFront._layers[key].hasOwnProperty("_layers")) {
                mapFrontLayers[key] = mapFront._layers[key];
            }
        }
        let arrCoordinates = [];
        mapFrontLayers.forEach(ls => {
           arrCoordinates =[...arrCoordinates, ...ls.getLatLngs()];
        })
        coordinatesFront = arrCoordinates;
        console.log(coordinatesFront);
        inputFrontCoordinates.value = coordinatesFront.join(",");
    })


    mapFront.on("pm:remove", (e) => {
        mapFrontLayers = [];
        for (let key in mapFront._layers) {
            if (mapFront._layers[key].hasOwnProperty("_bounds") && !mapFront._layers[key].hasOwnProperty("_layers")) {
                mapFrontLayers[key] = mapFront._layers[key];
            }
        }

        let arrCoordinates = [];
        mapFrontLayers.forEach(ls => {
            arrCoordinates =[...arrCoordinates, ...ls.getLatLngs()];
        })
        coordinatesFront = arrCoordinates;
        inputFrontCoordinates.value = coordinatesFront.join(",");
    });

}

if(document.getElementById("map-route-back")){

    let coordinatesBack = [];
    let mapBackLayers = [];

    const inputBackCoordinates = document.getElementById("backCoordinates");
    const mapBack = L.map('map-route-back', { pmIgnore: false }).setView([37.93585208752015, 58.39120934103419], 13);
    const osmFront = L.tileLayer('http://95.85.127.213:8083/tile/{z}/{x}/{y}.png', {
    }).addTo(mapBack);

    document.getElementById("map-route-back").addEventListener("mousemove", e => {
        mapBack.invalidateSize();
    });

    mapBack.pm.addControls(
        {
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
        },
        {
            polyline: {
                simplifyFactor: 0,
            },
        }
    );

    mapBack.pm.setPathOptions({
        color: "red",
        fillColor: "red",
        fillOpacity: 0.8,
    });

    let polyBackLine;

    if(coordinatesBackEdit){
        let coors = [];
        coordinatesBackEdit.forEach(function(coordinate) {
            coors.push([coordinate.x, coordinate.y]);
        });

        polyBackLine = L.polyline(coors, { noClip: true});
            polyBackLine.pm.enable();
            polyBackLine.addTo(mapBack);
            // mapBack.fitBounds(polyBackLine.getBounds());
            mapBack.setView([37.93585208752015, 58.39120934103419], 13);
    }

    if(polyBackLine){
        polyBackLine.on("pm:edit", (e) =>{
            for (let key in mapBack._layers) {
                if (mapBack._layers[key].hasOwnProperty("_bounds") && !mapBack._layers[key].hasOwnProperty("_layers")) {
                    mapBackLayers[key] = mapBack._layers[key];
                }
            }

            let arrCoordinatesBack = [];
            mapBackLayers.forEach(ls => {
                arrCoordinatesBack =[...arrCoordinatesBack, ...ls.getLatLngs()];
            })
            coordinatesBack = arrCoordinatesBack;
            inputBackCoordinates.value = coordinatesBack.join(",");
        });
    }


    mapBack.on('pm:create', ({ layer}) => {
        layer.on('pm:edit', e => {
            mapBackLayers = [];
            for (let key in mapBack._layers) {
                if (mapBack._layers[key].hasOwnProperty("_bounds") && !mapBack._layers[key].hasOwnProperty("_layers")) {
                    mapBackLayers[key] = mapBack._layers[key];
                }
            }

            let arrCoordinatesBack = [];
            mapBackLayers.forEach(ls => {
                arrCoordinatesBack =[...arrCoordinatesBack, ...ls.getLatLngs()];
            })
            coordinatesBack = arrCoordinatesBack;
            inputBackCoordinates.value = coordinatesBack.join(",");
        });
    });


    mapBack.on('pm:drawend', (e) => {
        let i = 0;
        mapBackLayers = [];
        for (let key in mapBack._layers) {
            if (mapBack._layers[key].hasOwnProperty("_bounds") && !mapBack._layers[key].hasOwnProperty("_layers")) {
                mapBackLayers[key] = mapBack._layers[key];
            }
        }
        let arrCoordinatesBack = [];
        mapBackLayers.forEach(ls => {
            arrCoordinatesBack =[...arrCoordinatesBack, ...ls.getLatLngs()];
        })
        coordinatesBack = arrCoordinatesBack;
        inputBackCoordinates.value = coordinatesBack.join(",");
    })


    mapBack.on("pm:remove", (e) => {
        mapBackLayers = [];
        for (let key in mapBack._layers) {
            if (mapBack._layers[key].hasOwnProperty("_bounds") && !mapBack._layers[key].hasOwnProperty("_layers")) {
                mapBackLayers[key] = mapBack._layers[key];
            }
        }

        let arrCoordinatesBack = [];
        mapBackLayers.forEach(ls => {
            arrCoordinatesBack =[...arrCoordinatesBack, ...ls.getLatLngs()];
        })
        coordinatesBack = arrCoordinatesBack;
        inputBackCoordinates.value = coordinatesBack.join(",");
    });

}

