import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
  plugins: [sveltekit()],
  server: {
    port: 3000,
    proxy: {
      // Forward API calls to the Play backend (app-api) during development.
      // Start app-api first:  (cd ../../app-api && sbt run)
      // Same-origin proxy also lets the backend set the session cookie on :3000.
      '/ping': 'http://localhost:9000',
      '/user': 'http://localhost:9000'
    }
  }
});
