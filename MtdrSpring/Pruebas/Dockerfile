# Usar una imagen base de Python
FROM python:3.9-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos de la aplicación
COPY . /app

# Instalar las dependencias
RUN pip install telethon

# Comando para ejecutar la aplicación
CMD ["python", "pruebas.py"]
