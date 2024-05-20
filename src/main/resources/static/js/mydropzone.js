let myDropzone = new Dropzone("#drop", {
    url: "#",
    autoProcessQueue: false,
    addRemoveLinks: true,
    maxFiles: 6,
    acceptedFiles: "image/jpg, image/jpeg, image/png, image/webp",
    clickable: ".fileinput-button"
});


let removedImageIds = [];

myDropzone.on("thumbnail", file => {
    console.log(file);
});

myDropzone.on("addedfile", file => {
    console.log("A file has been added");
});



myDropzone.on("removedfile", (file) => {
    removedImageIds.push(file.id);
})


