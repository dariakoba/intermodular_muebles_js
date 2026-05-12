import { app }  from "/js/core/app.js";
import { auth } from "/js/auth/auth.js";
import { api }  from "/js/core/api.js";
import { bind }  from "/js/core/events.js";

app.run(async () => {
	const form = document.getElementById("form-login");
	bind(form, "submit", handleLogin); 
});

async function handleLogin(e) {

	e.preventDefault();

	const email = document.getElementById("email").value;
	const password = document.getElementById("password").value;

	const ok = await auth.login(email, password);

	if (!ok) {
		document.getElementById("error").style.display = "block";
		return;
	}

	const user = await api.get("/api/me");

    // Redirección por rol
    if (user.role === "ADMIN") {
		window.location.href = "/admin/peliculas/index.html";
	} else {
		window.location.href = "/peliculas/index.html";
	}
}

