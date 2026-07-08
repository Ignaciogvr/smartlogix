# Stage 1: Build
FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npm run build -- --configuration production

# Stage 2: Serve
FROM nginx:alpine
# Copiar configuración custom de nginx para soportar las rutas de Angular (SPA)
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Copiar el build de Angular
COPY --from=build /app/dist/smartlogix-app/browser /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
