
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
    const formData = new FormData();
    const csrf = document.getElementById("csrf").value;
    const lat = document.getElementById("lat").value;
    const lng = document.getElementById("lng").value;
    const images = myDropzone.getAcceptedFiles();
    const prev = document.getElementById("prev").files[0];

    if (!lat || !lng) {
        showNotification("Заполните координаты", 'red');
        return;
    }

    const isStoreTm = await storeTranslation(csrf, "tm", document.getElementById("title_tm").value,
        document.getElementById("address_tm").value);
    const isStoreRu = await storeTranslation(csrf, "ru", document.getElementById("title_ru").value,
        document.getElementById("address_ru").value);
    const isStoreEn = await storeTranslation(csrf, "en", document.getElementById("title_en").value,
        document.getElementById("address_en").value);

    const mobPhones = document.getElementsByClassName("mob_phone_place");
    let phones = [];
    const city = document.getElementById("city_phone").value;

    let isMob = true;
    let isCity = true;

    for (const phone of Array.from(mobPhones)) {
        if(phone.value)
            phones.push(phone.value);
    }

    await deleteMobPhone(csrf);
    await deleteCityPhone(csrf);


    if(phones.length > 0){
        for (const phone of phones) {
            isMob = await storePhone(csrf, phone.trim(), "mob");
            if (!isMob)
                break;
        }
    }

    if(city)
        isCity = await storePhone(csrf, city, "city");

    const instagram = document.getElementById("instagram").value;
    const tiktok = document.getElementById("tiktok").value;

    await deleteSocial(csrf);

    if(instagram)
        await storeSocial(csrf, instagram, "instagram");

    if(tiktok)
        await storeSocial(csrf, tiktok, "tiktok");

    const email = document.getElementById("email").value;
    const website = document.getElementById("website").value;

    addToFormData(formData, "_csrf", document.getElementById("csrf").value);
    addToFormData(formData, "lat", lat);
    addToFormData(formData, "lng", lng);
    addToFormData(formData, "placeCategory", document.getElementById("placeCategory").value);
    addToFormData(formData, "placeSubCategory", document.getElementById("placeSubCategory").value);

    if(removedImageIds.length > 0)
        addToFormData(formData, "removeImageIds", removedImageIds);

    if(email)
        addToFormData(formData, "email", email);
    if(website)
        addToFormData(formData, "website", website);
    addToFormData(formData, "files", images);
    if(prev)
        addToFormData(formData, "prev", prev);


    if(isStoreTm && isStoreEn && isStoreRu && isMob && isCity) {
        sendFormData(method, url, formData).then(res => {
            showNotification(res.data, "blue");

            setTimeout(function() {
                window.location.href = host + "/places";
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


}

async function deleteMobPhone(csrf) {
    const  formData = new FormData();

    addToFormData(formData, "_csrf", csrf)

    await sendFormDataNotImage ("post", host + "/place-phones/mob", formData);
}

async function deleteCityPhone(csrf) {
    const  formData = new FormData();

    addToFormData(formData, "_csrf", csrf)

    await sendFormDataNotImage ("post", host + "/place-phones/city", formData);
}

async function deleteSocial(csrf) {
    const  formData = new FormData();

    addToFormData(formData, "_csrf", csrf)

    await sendFormDataNotImage ("post", host + "/place-socials/delete", formData);
}

async function storeSocial(csrf, link, name) {
    const formData = new FormData();

    addToFormData(formData, '_csrf', csrf);
    addToFormData(formData, "link", link);
    addToFormData(formData, "name", name);


    return await sendSocial(formData);
}

async function sendSocial(formData) {
    let isStore = false;
    await sendFormData("post", host + "/place-socials", formData).then(res => {
        isStore = true;
    }).catch(err => {
        isStore = false;
        displayValidationErrors(err.response.data, true);
    });

    return isStore;
}


async function storePhone(csrf, phone, type) {
    const formData = new FormData();

    addToFormData(formData, '_csrf', csrf);
    addToFormData(formData, "number", phone);
    addToFormData(formData, "type", type);


    return await sendPhone(formData);
}


async function sendPhone(formData) {
    let isStore = false;
    await sendFormData("post", host + "/place-phones", formData).then(res => {
        isStore = true;
    }).catch(err => {
        isStore = false;
        displayValidationErrors(err.response.data, true);
    });

    return isStore;
}

async function storeTranslation(csrf, locale, title, address) {
    const formDataTm = new FormData();

    addToFormData(formDataTm, '_csrf', csrf);
    addToFormData(formDataTm, "locale", locale);
    addToFormData(formDataTm, "title", title);
    addToFormData(formDataTm, "address", address);

    return await sendTranslation(formDataTm);

}

async function sendTranslation(formData) {
    let isStore = false;
    await sendFormDataNotImage("post", host + "/place-translations", formData).then(res => {
        isStore = true;
    }).catch(err => {
        isStore = false;
        console.log(err)
        displayValidationErrors(err.response.data, true);
    });

    return isStore;
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
        data: formData
    });
}

function displayValidationErrors(errors, isLocale) {
    const errorElements = [];

    Object.keys(errors).forEach(fieldName => {
        console.log(fieldName)
        let errorMessage = errors[fieldName];
        const errorElement =  document.getElementById(fieldName + "-error");
        errorElements.push(errorElement);
        if (errorElement) {
            errorElement.textContent = errorMessage;
            if(isLocale) {
                console.log(fieldName.endsWith("tm"))
                errorMessage = fieldName.endsWith("tm") ? "Tm " + errorMessage : errorMessage;
                errorMessage = fieldName.endsWith("ru") ? "Ru " + errorMessage : errorMessage;
                errorMessage = fieldName.endsWith("en") ? "En " + errorMessage : errorMessage;
                console.log(errorMessage)
            }
            showNotification(errorMessage, 'red');
        }
    });

    const errs = document.querySelectorAll('.error');
    errs.forEach(error => {
        if(!errorElements.includes(error))
            error.textContent = "";
    });
}


function showNotification(message, color){
    const notyf = new Notyf({
        position: {
            x: 'right',
            y: 'top',
        },
        types: [
            {
                type: 'info',
                background: color,
                icon: {
                    className: 'fas fa-info-circle',
                    tagName: 'span',
                    color: '#fff'
                },
                dismissible: false
            }
        ]
    });
    notyf.open({
        type: 'info',
        message: message
    });
}

