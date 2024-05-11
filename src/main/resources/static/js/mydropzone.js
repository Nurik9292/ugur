let myDropzone = new Dropzone("#drop", {
    url: "#",
    autoProcessQueue: false,
    addRemoveLinks: true,
    maxFiles: 6,
    acceptedFiles: "image/*"
});

let removedImageIds = [];

myDropzone.on("addedfile", file => {
    console.log("A file has been added");
});

myDropzone.on("removedfile", (file) => {
    removedImageIds.push(file.id);
})



