import { auth } from "/js/auth/auth.js";

export const guard = {
    async requireAuth() {
        return await auth.me();
    },
    async requireRole(rol) {
        const user = await this.requireAuth();
        if (user.rol !== rol) {
            const e = new Error("No autorizado");
            e.status = 403;
            throw e;
        }
        return user;
    }
};