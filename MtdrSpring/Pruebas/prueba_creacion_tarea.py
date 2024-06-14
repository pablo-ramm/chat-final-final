from telethon.sync import TelegramClient, events
import datetime
import random

api_id = '28656631'
api_hash = 'cd3623b8696d688dcb41cf1a9e1c87a7'
phone_number = '+528443834642'
bot_username = 'rafha_java_bot'

session_name = 'my_session'

def generar_nombre_tarea(nombre_base):
    timestamp = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
    random_number = random.randint(1000, 9999)
    nombre_unico = f'{nombre_base}_{timestamp}_{random_number}'
    return nombre_unico

client = TelegramClient(session_name, api_id, api_hash)

async def main():
    await client.start(phone=phone_number)

    entity = await client.get_entity(bot_username)

    async with client.conversation(entity) as conv:
        await conv.send_message('/start')
        response = await conv.get_response()

        if "Hola este es un bot para tus tareas y tu rol es: Desarrollador" in response.text:
            print("Bot respondio correctamente al comando /start")

        await conv.send_message('/CrearTarea')
        response = await conv.get_response()

        if "Escriba el nombre de la tarea" in response.text:
            nombre_tarea = generar_nombre_tarea('TareaHoy')
            await conv.send_message(nombre_tarea)

        response = await conv.get_response()

        if "La tarea tiene nombre:" in response.text and "TareaHoy" in response.text:
            print("Nombre de tarea enviado correctamente")
        
        response = await conv.get_response()

        if "Por favor, ingresa la descripcion de la tarea" in response.text:
            print("Mensaje recibido exitosamente")

        await conv.send_message('Tarea para prueba de creacion de tarea')
        response = await conv.get_response()

        if "La tarea tiene descripcion:Tarea para prueba de creacion de tarea" in response.text:
            print("Descripcion de tarea enviada correctamente")

        response = await conv.get_response()

        if "Por favor, ingresa la prioridad de la tarea:" in response.text:
            print("Mensaje recibido exitosamente")

        await conv.send_message('1')
        response = await conv.get_response()

        if "La tarea tiene prioridad:1" in response.text:
            print("Prioridad de tarea enviada correctamente")

        response = await conv.get_response()

        if "Por favor, ingresa los comentarios adicionales de la tarea:" in response.text:
            print("Mensaje recibido exitosamente")

        await conv.send_message('No hay comentarios adicionales')
        response = await conv.get_response()

        if "La tarea tiene de comentario:No hay comentarios adicionales" in response.text:
            print("Comentarios adicionales de tarea enviados correctamente")

        response = await conv.get_response()
        if "La tarea ha sido creada exitosamente!" in response.text:
            print("Prueba: creacion de tarea paso exitosamente")
        await client.disconnect()

    await client.run_until_disconnected()

with client:
    client.loop.run_until_complete(main())