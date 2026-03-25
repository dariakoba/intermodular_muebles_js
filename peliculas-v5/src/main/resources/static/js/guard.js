const guard = {

	async requireAuth() {
		return await auth.me();
	},

	async requireRole(role) {

		const user = await this.requireAuth();

		if (user.role !== role) {
			alert("No autorizado");
			window.location.href = "/";
			return;
		}

		return user;
	}
};