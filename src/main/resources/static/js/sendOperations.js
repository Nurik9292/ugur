let notyf;
let notyfNotifications = [];

function sendCreateRoute() {
    sendFromRoute("post", host + "/routes");
}

function sendUpdateRoute() {
    const id = document.getElementById("id").value;
    sendFromRoute("put", host + "/routes/" + id);
}

function sendFromRoute(method, url) {
    const frontStartStops = document.getElementById('front_id_target_list')
        .querySelectorAll('li.p-picklist-item');
    const backStartStops = document.getElementById('back_id_target_list')
        .querySelectorAll('li.p-picklist-item');
    const city = document.getElementById("city");

    let frontStartStopIds = [];
    let backStartStopIds = [];

    frontStartStops.forEach(startStop => {
        frontStartStopIds.push(startStop.id);
    });

    backStartStops.forEach(startStop => {
        backStartStopIds.push(startStop.id);
    });

    const formData = new FormData();
    addToFormData(formData, "_csrf", document.getElementById("csrf").value);
    addToFormData(formData, 'name', document.getElementById('name').value);
    addToFormData(formData, 'number', document.getElementById("number").value);
    addToFormData(formData, 'frontCoordinates', document.getElementById('frontCoordinates').value);
    addToFormData(formData, 'backCoordinates', document.getElementById('backCoordinates').value);
    addToFormData(formData, 'startStops', frontStartStopIds);
    addToFormData(formData, 'endStops', backStartStopIds);
    addToFormData(formData, "city", city.options[city.selectedIndex].value);

    sendFormData(method, url, formData).then(res => {
        showNotification(res.data, "blue");

        setTimeout(function() {
            window.location.href = host + "/routes";
        }, 2000);

    })
        .catch(error => {
            if (error.response && error.response.status === 400) {
                console.error('Validation error:', error.response.data);
                displayValidationErrors(error.response.data);
            } else {
                console.error("Error sending request:", error);
            }
        });
}

//////////////////////// END CRUD ROUTE //////////////////////////////

///////////////////// CRUD PLACE ///////////////////////////////

async function sendCreatePlace() {

    await sendForm("post", host + "/places");
}

async function sendUpdatePlace() {
    const id = document.getElementById("id").value;
    await sendForm('put', host + "/places/" + id);
}



async function sendForm(method, url) {
    const btn = document.getElementById("btn-send");
    const btnLoad = document.getElementById("btn-loading");
    btn.classList.add("d-none");
    btnLoad.classList.remove("d-none");

    const formData = new FormData();
    const csrf = document.getElementById("csrf").value;
    const lat = document.getElementById("lat").value;
    const lng = document.getElementById("lng").value;
    const images = myDropzone.getAcceptedFiles();
    const thumb = document.getElementById("prev").files[0];

    addToFormData(formData, "_csrf", csrf);
    if(lat)
        addToFormData(formData, "lat", lat);
    if(lng)
        addToFormData(formData, "lng", lng);
    addToFormData(formData, "files", images);
    if(thumb)
        addToFormData(formData, "thumb", thumb);
    addToFormData(formData, "titleTm", document.getElementById("title_tm").value);
    addToFormData(formData, "titleRu", document.getElementById("title_ru").value);
    addToFormData(formData, "titleEn", document.getElementById("title_en").value);
    addToFormData(formData, "addressTm", document.getElementById("address_tm").value);
    addToFormData(formData, "addressRu", document.getElementById("address_ru").value);
    addToFormData(formData, "addressEn", document.getElementById("address_en").value);
    const instagram =  document.getElementById("instagram").value;
    if(instagram)
        addToFormData(formData, "instagram", instagram);
    const tiktok = document.getElementById("tiktok").value;
    if(tiktok)
        addToFormData(formData, "tiktok", tiktok);
    const email = document.getElementById("email").value;
    if(email)
        addToFormData(formData, "email", email);
    const website = document.getElementById("website").value;
    if(website)
        addToFormData(formData, "website", website);
    const cityNumber = document.getElementById("city_phone").value;
    if(cityNumber)
        addToFormData(formData, "cityNumber", cityNumber);
    const mobPhones = document.getElementsByClassName("mob_phone_place");
    let phones = [];
    for (const phone of Array.from(mobPhones)) {
        if(phone.value)
            phones.push(phone.value);
    }
    if(phones.length > 0)
        addToFormData(formData, "mobNumbers", phones);

    addToFormData(formData, "categoryId", document.getElementById("placeCategory").value);
    addToFormData(formData, "subCategoryId", document.getElementById("placeSubCategory").value);

    if(removedImageIds.length > 0)
        addToFormData(formData, "removeImageIds", removedImageIds);

        sendFormData(method, url, formData).then(res => {
            showNotification(res.data, "success");

            setTimeout(function() {
                window.location.href = host + "/places";
            }, 1000);

        }).catch(error => {
            btn.classList.remove("d-none");
            btnLoad.classList.add("d-none");
                if (error.response && error.response.status === 400) {
                    console.error('Validation error:', error.response.data);
                    displayValidationErrors(error.response.data);
                } else {
                    console.error("Error sending request:", error);
                }
            });



}

///////////////////// EDN CRUD PLACE ///////////////////////////////

function addToFormData(formData, key, value) {
    if (Array.isArray(value)) {
        value.forEach(val => formData.append(key, val));
    } else {
        formData.append(key, value);
    }
}


function sendFormData(method, url, formData) {
    return axios({
        method: method,
        url: url,
        data: formData,
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });
}

async function sendFormDataNotImage(method, url, formData) {
    return await axios({
        method: method,
        url: url,
        data: formData,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    });
}

function displayValidationErrors(errors) {
    const errorElements = [];

    Object.keys(errors).forEach(fieldName => {
        console.log(fieldName)
        let errorMessage = errors[fieldName];
        const errorElement =  document.getElementById(fieldName + "-error");
        errorElements.push(errorElement);
        if (errorElement) {
            errorElement.textContent = errorMessage;
            showNotification(errorMessage, "error");
        }

    });

    const errs = document.querySelectorAll('.error');
    errs.forEach(error => {
        if(!errorElements.includes(error))
            error.textContent = "";
    });
}


function showNotification(message, show){
    if(!notyf)
        notyf = new Notyf();

    if(show === "error")
        notyf.error(message);
    if(show === "success")
        notyf.success(message);
}