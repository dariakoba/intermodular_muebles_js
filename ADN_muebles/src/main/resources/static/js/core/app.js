export const app = {
	async run(fn) {
		try {
			return await fn();
		} catch (e) {
			
			console.error("Error capturado:");
			console.error("status:", e.status);
			console.error("message:", e.message);
			console.error("stack:", e.stack);
			
			if (e.status === 409) {
				alert(e.data?.error || "Conflicto");
				return;
			}
			
			if (e.status) {
				handleHttpError(e.status);
				return;
			}
			
			// Error JS
			alert("Error inesperado en la aplicación");
		}

	}
};

function handleHttpError(status) {
	switch (status) {
		case 401:
			location.href = "/login.html";
			break;
		case 403:
			location.href = "/error/403.html";
			break;
		case 404:
			location.href = "/error/404.html";
			break;
		default:
			location.href = "/error/500.html";
	}
}