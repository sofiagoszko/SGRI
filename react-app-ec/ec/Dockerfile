# 1. Usa una imagen base de Node.js
FROM node:18-alpine

# 2. Establece el directorio de trabajo en el contenedor
WORKDIR /app

# 3. Copia los archivos del proyecto al contenedor
COPY package.json package-lock.json ./
COPY . .

# 4. Instala las dependencias del proyecto
RUN npm install

# 5. Construye la aplicación (incluye la compilación de TypeScript y el build de Vite)
# RUN npm run build

# 6. Expone el puerto en el que correrá la aplicación
EXPOSE 4173

# 7. Instala un servidor estático para servir la aplicación
# RUN npm install -g serve

# 8. Comando para correr la aplicación con 'serve'
# CMD ["serve", "-s", "dist"]
CMD ["npm", "run", "dev"]

# docker rmi [id de la imagen]
# docker build -t [nombre de la imagen] . construir una imagen basada en un DockerFile 
# docker run -p [puerto local]:[puerto contenedor] --name [nombre del contenedir] [nombre de la imagen]
# docker run -it -p [puerto local]:[puerto interno del contenedor] [nombre de la imagen] /bin/sh correr la imagen del proyecto en modo interactivo
# docker ps -a listar todos los contenedores activos
# docker rm -f $(docker ps -a -q) eliminar todos los contenedores