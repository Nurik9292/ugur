const startStops = document.getElementById("start_stops");
const endStops = document.getElementById("end_stops");

if(startStops){
    let selectedStart = [];

    if(startStops.selectedOptions){
        for(const item of startStops.selectedOptions){
            selectedStart.push(item.value);
        }

        document.getElementById("selectedOptionsOrderStart").value = selectedStart.join(",");
    }

    new MultiSelectTag('start_stops', {
        rounded: true,
        shadow: true,
        placeholder: 'Search',
        tagColor: {
            textColor: '#ffffff',
            borderColor: '#2361ce',
            bgColor: '#2361ce',
        },
        onChange: function(values) {
            let res = [];
            if(values.length < selectedStart.length){
                for(const item of values){
                    for(const sel of selectedStart)
                        if(sel === item.value){
                            res.push(sel);
                        }
                }
                selectedStart = selectedStart.filter((element) => {
                    return res.includes(element);
                });
            }else{
                for(const item of values){
                    if(!selectedStart.includes(item.value)){
                        selectedStart.push(item.value);
                    }
                }
            }

            document.getElementById("selectedOptionsOrderStart").value = selectedStart.join(",");
        }

    })}


if(endStops){
    let selectedEnd = [];

    if(endStops.selectedOptions){
        for(const item of endStops.selectedOptions){
            selectedEnd.push(item.value);
        }

        document.getElementById("selectedOptionsOrderEnd").value = selectedEnd.join(",");
    }

    new MultiSelectTag('end_stops', {
        rounded: true,
        shadow: true,
        placeholder: 'Search',
        tagColor: {
            textColor: '#ffffff',
            borderColor: '#2361ce',
            bgColor: '#2361ce',
        },
        onChange: function(values) {
            let res = [];
            if(values.length < selectedEnd.length){
                for(const item of values){
                    for(const sel of selectedEnd)
                        if(sel === item.value){
                            res.push(sel);
                        }
                }
                selectedEnd = selectedEnd.filter((element) => {
                    return res.includes(element);
                });
            }else{
                for(const item of values){
                    if(!selectedEnd.includes(item.value)){
                        selectedEnd.push(item.value);
                    }
                }
            }

            document.getElementById("selectedOptionsOrderEnd").value = selectedEnd.join(",");
        }
    })}
