export const api = {

    async request(url, options = {}) {

        console.log(url);

        const { headers = {}, ...rest } = options;

        const config = {
            credentials: "same-origin",

            ...rest,

            headers: {
                "Accept": "application/json",
                ...headers
            }
        };

        const response = await fetch(url, config);

        let data = null;

        if (response.status !== 204) {
            try {
                data = await response.json();
            } catch {
                // Puede no ser JSON (ej: texto plano)
            }
        }

        if (!response.ok) {
            const e = new Error(data?.message || "HTTP error");
            e.status = response.status;
            e.data = data;
            throw e;
        }

        return data;
    },

    async get(url, options = {}) {
        return this.request(url, options);
    },

    async post(url, data, options = {}) {
        return this._sendWithBody("POST", url, data, options);
    },

    async put(url, data, options = {}) {
        return this._sendWithBody("PUT", url, data, options);
    },

    async patch(url, data, options = {}) {
        return this._sendWithBody("PATCH", url, data, options);
    },

    async delete(url, options = {}) {
        return this.request(url, {
            method: "DELETE",
            ...options
        });
    },

    // método interno común
    async _sendWithBody(method, url, data, options = {}) {

        const { headers = {}, ...rest } = options;

        const isFormData = data instanceof FormData;

        return this.request(url, {
            method,

            headers: {
                ...(isFormData ? {} : { "Content-Type": "application/json" }),
                ...headers
            },

            body: isFormData ? data : JSON.stringify(data),

            ...rest
        });
    }
};