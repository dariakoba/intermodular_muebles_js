import { app } from "/js/core/app.js";

export function bind(element, event, handler) {
	element.addEventListener(event, async (e) => {
		await app.run(() => handler(e));
	});
}