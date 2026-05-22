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
			} catch {}
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

	    const { headers = {}, ...rest } = options;

	    return this.request(url, {
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
		
		return this.request(url, {
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
		return this.request(url, {
			method: "DELETE",
			...options
		});
	}
};