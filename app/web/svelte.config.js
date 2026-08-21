import adapter from '@sveltejs/adapter-static';
import { vitePreprocess } from '@sveltejs/vite-plugin-svelte';

/** @type {import('@sveltejs/kit').Config} */
const config = {
  preprocess: vitePreprocess(),
  kit: {
    // SPA build: the app runs entirely client-side (`ssr = false` in the root
    // +layout.ts). `fallback` emits a catch-all page the static host serves for
    // every route. See https://svelte.dev/docs/kit/single-page-apps
    adapter: adapter({ fallback: 'index.html' })
  }
};

export default config;
