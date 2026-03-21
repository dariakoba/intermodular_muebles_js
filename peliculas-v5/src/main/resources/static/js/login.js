const form = document.getElementById("form-login");

form.addEventListener("submit", async function(e) {

	e.preventDefault();

	const ok = await auth.login(email.value, password.value);

	if (!ok) {

		document.getElementById("error").style.display = "block";
		return;
	}

	window.location.href = "/admin/peliculas/index.html";
});