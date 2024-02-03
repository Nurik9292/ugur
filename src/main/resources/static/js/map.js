
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
    let mapLayers = [];

    const inputFrontCoordinates = document.getElementById("frontCoordinates");
    const mapFront = L.map('map-route-front', { pmIgnore: false }).setView([37.93585208752015, 58.39120934103419], 13);
    const osmFront = L.tileLayer('http://95.85.127.213:8083/tile/{z}/{x}/{y}.png', {
    }).addTo(mapFront);

    document.getElementById("map-route-front").addEventListener("mousemove", e => {
        mapFront.invalidateSize();
    });


    const drawControl = {
        draw: {

        },
    };


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
                simplifyFactor: 0, // Устанавливаем фактор упрощения в 0
            },
        }
    );

    mapFront.pm.setPathOptions({
        color: "red",
        fillColor: "red",
        fillOpacity: 0.8,
    });

    let polyLine;

    if(coordinatesEdit){
        let coors = [];
        coordinatesEdit.forEach(function(coordinate) {
            coors.push([coordinate.x, coordinate.y]);
        });

        polyLine = L.polyline(coors, { noClip: true});
        polyLine.pm.enable();

        polyLine.addTo(mapFront);
        mapFront.fitBounds(polyLine.getBounds());
        mapFront.setView([37.93585208752015, 58.39120934103419], 13);
    }

    mapFront.on("pm:drawstart", ({ workingLayer}) => {
        workingLayer.on("pm:vertexadded", (e) => {
        });
    });

    mapFront.on('pm:create', ({ layer}) => {
        layer.on('pm:edit', e => {
            mapLayers = [];
            for (let key in mapFront._layers) {
                if (mapFront._layers[key].hasOwnProperty("_bounds") && !mapFront._layers[key].hasOwnProperty("_layers")) {
                    mapLayers[key] = mapFront._layers[key];
                }
            }

            let arrCoordinates = [];
            mapLayers.forEach(ls => {
                arrCoordinates =[...arrCoordinates, ...ls.getLatLngs()];
            })
            coordinates = arrCoordinates;
            inputFrontCoordinates.value = coordinates.join(",");
        });
    });




    mapFront.on('pm:drawend', (e) => {
        let i = 0;
        mapLayers = [];
        for (let key in mapFront._layers) {
            if (mapFront._layers[key].hasOwnProperty("_bounds") && !mapFront._layers[key].hasOwnProperty("_layers")) {
                mapLayers[key] = mapFront._layers[key];
            }
        }
        let arrCoordinates = [];
        mapLayers.forEach(ls => {
           arrCoordinates =[...arrCoordinates, ...ls.getLatLngs()];
        })
        coordinates = arrCoordinates;
        inputFrontCoordinates.value = coordinates.join(",");
    })


    mapFront.on("pm:remove", (e) => {
        mapLayers = [];
        for (let key in mapFront._layers) {
            if (mapFront._layers[key].hasOwnProperty("_bounds") && !mapFront._layers[key].hasOwnProperty("_layers")) {
                mapLayers[key] = mapFront._layers[key];
            }
        }

        let arrCoordinates = [];
        mapLayers.forEach(ls => {
            arrCoordinates =[...arrCoordinates, ...ls.getLatLngs()];
        })
        coordinates = arrCoordinates;
        inputFrontCoordinates.value = coordinates.join(",");
    });

}

