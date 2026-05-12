import { app } from "/js/core/app.js";
import { api } from "/js/core/api.js";
import { bind } from "/js/core/events.js";

app.run(() => {

    const form = document.getElementById("upload-form");
    const fileInput = document.getElementById("file");

    bind(form, "submit", handleSubmit);
    bind(fileInput, "change", showLocalPreview);

});

function showLocalPreview() {

    const fileInput = document.getElementById("file");
    const preview = document.getElementById("preview");

    const file = fileInput.files[0];

    if (!file) {
        preview.style.display = "none";
        preview.src = "";
        return;
    }

    preview.src = URL.createObjectURL(file);
    preview.style.display = "block";
}

async function handleSubmit(e) {

    e.preventDefault();

    const folder = document.getElementById("folder").value;
    const file = document.getElementById("file").files[0];
    const result = document.getElementById("result");
    const preview = document.getElementById("preview");

    result.className = "";
    result.textContent = "";

    if (!file) {
        result.className = "error";
        result.textContent = "Debes seleccionar una imagen.";
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    // 🔥 clave: usar api.request (NO api.post)
    const data = await api.request(`/api/uploads/${folder}`, {
        method: "POST",
        body: formData
    });

    result.className = "success";
    result.textContent = JSON.stringify(data, null, 2);

    if (data.url) {
        preview.src = data.url;
        preview.style.display = "block";
    }
}