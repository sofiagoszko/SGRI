import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0', // Asegura que la aplicación sea accesible externamente
    port: 4173,      // Usa el mismo puerto que expones en Docker
  }
});