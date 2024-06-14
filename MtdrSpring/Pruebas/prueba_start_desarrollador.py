import sys
from telethon.sync import TelegramClient, events

api_id = '28656631'
api_hash = 'cd3623b8696d688dcb41cf1a9e1c87a7'
phone_number = '+528443834642'
bot_username = 'rafha_java_bot'

session_name = 'my_session'

client = TelegramClient(session_name, api_id, api_hash)

async def main():
    await client.start(phone=phone_number)

    entity = await client.get_entity(bot_username)

    async with client.conversation(entity) as conv:
        await conv.send_message('/start')
        response = await conv.get_response()

        if "Hola este es un bot para tus tareas y tu rol es: Desarrollador" in response.text:
            print("Bot respondio correctamente al comando /start")
            await client.disconnect()
            return
        else:
            print("Test Failed: Incorrect response.")
            sys.exit("El test ha fallado. Saliendo del programa.")
        
    await client.run_until_disconnected()

with client:
    client.loop.run_until_complete(main())