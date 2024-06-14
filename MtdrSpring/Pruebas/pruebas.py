import sys
from telethon.sync import TelegramClient, events

api_id = '28656631'
api_hash = 'cd3623b8696d688dcb41cf1a9e1c87a7'
phone_number = '+528443834642'
chat_id = -4245707425

session_name = 'my_session'

client = TelegramClient(session_name, api_id, api_hash)

async def main():
    await client.start(phone=phone_number)
    
    await client.send_message(chat_id, '/start')
    
    @client.on(events.NewMessage(chats=chat_id))
    async def handler(event):
        print(f'Received message: {event.text}')
        if "No eres un usuario autorizador para esta aplicacion" in event.text:
            print("Test Passed: Correct response received.")
        else:
            print("Test Failed: Incorrect response.")
            sys.exit("El test ha fallado. Saliendo del programa.")
        
        await client.disconnect()
        
    await client.run_until_disconnected()

with client:
    client.loop.run_until_complete(main())
