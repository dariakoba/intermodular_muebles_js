import { app }  from "/js/core/app.js";
import { auth } from "/js/auth/auth.js";
import { bind } from "/js/core/events.js";

app.run(() => {
    const form = document.getElementById("form-register");
    bind(form, "submit", handleRegister);
});

async function handleRegister(e) {

    e.preventDefault();

    const errorDiv = document.getElementById("error");
    const successDiv = document.getElementById("success");

    errorDiv.style.display = "none";
    successDiv.style.display = "none";

    const nombre = document.getElementById("nombre").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const password2 = document.getElementById("password2").value;

    if (password !== password2) {
        errorDiv.textContent = "Las contraseñas no coinciden";
        errorDiv.style.display = "block";
        return;
    }

	try{
		await auth.register(nombre, email, password);
		successDiv.style.display = "block";

		setTimeout(() => {
			window.location.href = "/login.html";
		}, 1500);
	} catch (e) {
		if (e.status === 409) {
			errorDiv.textContent = "El email ya está registrado" || e.data?.message;
			errorDiv.style.display = "block";
			return;
		}
		
		throw e;
	}
}