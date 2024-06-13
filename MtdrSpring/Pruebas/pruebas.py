from telethon.sync import TelegramClient, events

# Definir tus credenciales
api_id = '28656631'
api_hash = 'cd3623b8696d688dcb41cf1a9e1c87a7'
phone_number = '+528443834642'
chat_id = -4245707425

# Nombre de la sesión para guardar
session_name = 'my_session'

# Crear el cliente de Telegram con la sesión persistente
client = TelegramClient(session_name, api_id, api_hash)

# Función principal para enviar el mensaje y escuchar la respuesta
async def main():
    await client.start(phone=phone_number)

    # Enviar el comando /start al chat del grupo
    await client.send_message(chat_id, '/start')

    # Manejar la respuesta del bot
    @client.on(events.NewMessage(chats=chat_id))
    async def handler(event):
        print(f'Received message: {event.text}')
        if "No eres un usuario autorizador para esta aplicacion" in event.text:
            print("Test Passed: Correct response received.")
        else:
            print("Test Failed: Incorrect response.")

        # Desconectar después de recibir la respuesta
        await client.disconnect()

    # Esperar un tiempo suficiente para recibir la respuesta
    await client.run_until_disconnected()

# Ejecutar el bucle principal de manera sincrónica
with client:
    client.loop.run_until_complete(main())

