import { app } from "/js/core/app.js";

app.run(() => {

    const params = new URLSearchParams(window.location.search);
    const url = params.get("url");

    if (!url) return;

    const img = document.getElementById("imagen");
    img.src = url;
});