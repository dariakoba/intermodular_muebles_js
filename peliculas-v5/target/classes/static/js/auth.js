const auth = {

    currentUser: null,

    // Requiere autenticación
    async me() {

        if (this.currentUser) {
            return this.currentUser;
        }

        const response = await api.get("/api/me");

        this.currentUser = await api.parseJson(response);

        return this.currentUser;
    },

    // Opcional (no redirige)
    async meOptional() {

        const response = await api.get("/api/me", {
            allow401: true
        });

        if (response.status === 401) {
            return null;
        }

        this.currentUser = await api.parseJson(response);

        return this.currentUser;
    },

    // Login
    async login(email, password) {

        const response = await fetch("/api/login", {
            method: "POST",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            this.currentUser = null;
        }

        return response.ok;
    },

    // Logout
    async logout() {

        await api.post("/api/logout");

        this.currentUser = null;

        window.location.href = "/login.html";
    },

    // Rol (opcional, pero muy útil)
	hasRole(role) {
		return !!this.currentUser && this.currentUser.role === role;
	}
};