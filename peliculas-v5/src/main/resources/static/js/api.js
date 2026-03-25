const api = {

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

	    if (response.status === 401 && !options.allow401) {
	        window.location.href = "/login.html";
	        throw new Error("No autenticado");
	    }

	    if (response.status === 403) {
	        alert("No tienes permisos");
	        throw new Error("Acceso prohibido");
	    }

	    if (response.status >= 500) {
	        alert("Error interno del servidor");
	        throw new Error("Error del servidor");
	    }

	    console.log("Respuesta:", response);
	    return response;
	},

	async get(url, options = {}) {
		return await this.request(url, options);
	},

	async post(url, data, options = {}) {

	    const { headers = {}, ...rest } = options;

	    return await this.request(url, {
	        method: "POST",
			headers: {
				 "Content-Type": "application/json",
				 ...headers
			},
	        body: JSON.stringify(data),
	        ...rest
	        
	    });
	},

	async put(url, data, options = {}) {
		
		const { headers = {}, ...rest } = options;
		
		return await this.request(url, {
			method: "PUT",
			headers: {
				"Content-Type": "application/json",
				...headers
			},
			body: JSON.stringify(data),
			...rest
		});
	},

	async delete(url, options = {}) {
		return await this.request(url, {
			method: "DELETE",
			...options
		});
	},
	
	async parseJson(response) {
		if (response.status === 204) return null;
		return await response.json();
	}
};