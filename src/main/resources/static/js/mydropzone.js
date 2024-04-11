let myDropzone = new Dropzone("#drop", {
    url: "#",
    autoProcessQueue: false,
    addRemoveLinks: true,
    maxFiles: 3
});

let removedImageIds = [];

myDropzone.on("removedfile", (file) => {
    removedImageIds.push(file.id);
})



