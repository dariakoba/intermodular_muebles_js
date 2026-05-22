import { api } from "/js/core/api.js";
import { app } from "/js/core/app.js";

export const auth = {

    currentUser: null,

    async me() {
        if (this.currentUser) {
            return this.currentUser;
        }
        this.currentUser = await api.get("/api/me");
        return this.currentUser;
    },

    async meOptional() {
        if (this.currentUser) {
            return this.currentUser;
        }
        try {
            this.currentUser = await api.get("/api/me");
            return this.currentUser;
        } catch (e) {
            if (e.status === 401) return null;
            throw e;
        }
    },

    async register(name, email, password) {
        await api.post("/api/register", {name, email, password});
    },

    async login(email, password) {
        try {
            await api.post("/api/login", { email, password });
            this.currentUser = null;
            return true;
        } catch (e) {
            if (e.status === 401) return false;
            throw e;
        }
    },

    async logout() {
        await api.post("/api/logout");
        this.currentUser = null;
    },

    isAuthenticated() {
        return !!this.currentUser;
    },

    // Cambiado: role → rol
    hasRole(rol) {
        return !!this.currentUser && this.currentUser.rol === rol;
    }
};