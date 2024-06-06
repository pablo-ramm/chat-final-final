package com.springboot.MyTodoList.controller;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.springboot.MyTodoList.model.Tarea;
import com.springboot.MyTodoList.service.TareaService;
import com.springboot.MyTodoList.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotHelper;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;

// Define los estados posibles
enum TaskCreationState {
    START,
    CAPTURING_NAME,
    CAPTURING_DESCRIPTION,
    CAPTURING_STATE,
    CAPTURING_DATES,
    CAPTURING_PRIORITY,
    CAPTURING_COMMENTS,
    FINISHED
}
public class BotController extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);

    private TareaService tareaService;
    private String botName;
    private Tarea tareaToBeConstructed = new Tarea();
    private UsuarioService usuarioService;
    private String typeUser;
    private long currentUserID;

    private TaskCreationState currentState = TaskCreationState.START;

    public BotController(String botToken, String botName, TareaService tareaService, UsuarioService usuarioService) {
        super(botToken);
        logger.info("Bot Token: " + botToken);
        logger.info("Bot name: " + botName);
        this.tareaService = tareaService;
        this.botName = botName;
        this.usuarioService = usuarioService;
        this.typeUser = "";
        this.currentUserID = 0;
    }
    @Override
    public void onUpdateReceived(Update update) {
        long user_id = update.getMessage().getChat().getId();
        long chatId = update.getMessage().getChatId();
        if (currentUserID != user_id ) {
            typeUser = "";
            currentUserID = user_id;
        }

            if (typeUser.equals("")){
                typeUser = getUsuarioRolByTelegramId(user_id).getBody();
            }
            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageTextFromTelegram = update.getMessage().getText();
                if (typeUser.equals("Desarrollador")) {
                    handleDeveloperMessage(messageTextFromTelegram,chatId);
                } else if (typeUser.equals("Manager")) {
                    sendMessage("Eres un manager",chatId);
                }
                else{
                    sendMessage("No eres un usuario autorizador para esta aplicacion",chatId);
                }

            }



    }
    // Método para enviar un mensaje al usuario
    private void sendMessage(String text, long chatId) {
        SendMessage messageToTelegram = new SendMessage();
        messageToTelegram.setChatId(chatId);
        messageToTelegram.setText(text);
        try {
            execute(messageToTelegram);
        } catch (TelegramApiException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
    private void showDevMainMenu(SendMessage messageToTelegram ){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // first row
        KeyboardRow row = new KeyboardRow();
        row.add("/MisTareas");
        row.add("/CrearTarea");
        // Add the first row to the keyboard
        keyboard.add(row);

        // Set the keyboard
        keyboardMarkup.setKeyboard(keyboard);

        // Add the keyboard markup
        messageToTelegram.setReplyMarkup(keyboardMarkup);

        try {
            execute(messageToTelegram);
        } catch (TelegramApiException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private void handleDeveloperMessage(String messageTextFromTelegram, long chatId){

        if (messageTextFromTelegram.equals("/start") || messageTextFromTelegram.equals("/MenuPrincipal")){
            currentState = TaskCreationState.START;
            tareaToBeConstructed.setDefaultValues();
            SendMessage messageToTelegram = new SendMessage();
            messageToTelegram.setChatId(chatId);
            messageToTelegram.setText("Hola este es un bot para tus tareas y tu rol es: " + typeUser);
            showDevMainMenu(messageToTelegram );

        }
        else if (messageTextFromTelegram.contains("/MisTareas")) {
            currentState = TaskCreationState.START;
            tareaToBeConstructed.setDefaultValues();

            SendMessage messageToTelegram = new SendMessage();
            messageToTelegram.setChatId(chatId);


            List<Tarea> allTareas = getAllTareas();
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();

            // command back to main screen
            KeyboardRow mainScreenRowTop = new KeyboardRow();
            mainScreenRowTop.add("/MenuPrincipal");
            keyboard.add(mainScreenRowTop);

            KeyboardRow firstRow = new KeyboardRow();
            firstRow.add("/CrearTarea");
            keyboard.add(firstRow);



            List<Tarea> activeTares = allTareas.stream().filter(tarea -> tarea.getEstado() == false)
                    .collect(Collectors.toList());

            for (Tarea tarea : activeTares) {

                KeyboardRow currentRow = new KeyboardRow();
                currentRow.add(tarea.getNombre());
                currentRow.add(tarea.getIdTarea() + BotLabels.DASH.getLabel() + BotLabels.DONE.getLabel());
                keyboard.add(currentRow);
            }

            List<Tarea> doneTareas = allTareas.stream().filter(tarea -> tarea.getEstado() == true)
                    .collect(Collectors.toList());

            for (Tarea tarea : doneTareas) {
                KeyboardRow currentRow = new KeyboardRow();
                currentRow.add(tarea.getNombre());
                currentRow.add(tarea.getIdTarea() + BotLabels.DASH.getLabel() + BotLabels.UNDO.getLabel());
                currentRow.add(tarea.getIdTarea() + BotLabels.DASH.getLabel() + BotLabels.DELETE.getLabel());
                keyboard.add(currentRow);
            }

            keyboardMarkup.setKeyboard(keyboard);
            messageToTelegram.setReplyMarkup(keyboardMarkup);


            messageToTelegram.setText("aqui se muestran todas mis tareas");
            try {
                execute(messageToTelegram);

            } catch (TelegramApiException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        else if (messageTextFromTelegram.contains("/CrearTarea")) {
            SendMessage messageToTelegram = new SendMessage();
            messageToTelegram.setChatId(chatId);
            messageToTelegram.setText("Escriba el nombre de la tarea");
            try {
                execute(messageToTelegram);
            } catch (TelegramApiException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            // Establecer el estado para capturar la tarea
            currentState = TaskCreationState.CAPTURING_NAME;

        }else if (messageTextFromTelegram.indexOf(BotLabels.DONE.getLabel()) != -1) {
            currentState = TaskCreationState.START;
            tareaToBeConstructed.setDefaultValues();
            String done = messageTextFromTelegram.substring(0,
                    messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
            Integer id = Integer.valueOf(done);

            try {

                Tarea tarea = getTareaById(id).getBody();
                tarea.setEstado(true);
                updateTarea(tarea, id);
                BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DONE.getMessage(), this);

            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }

        }else if (messageTextFromTelegram.indexOf(BotLabels.UNDO.getLabel()) != -1) {
            currentState = TaskCreationState.START;
            tareaToBeConstructed.setDefaultValues();
            String undo = messageTextFromTelegram.substring(0,
                    messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
            Integer id = Integer.valueOf(undo);

            try {

                Tarea tarea = getTareaById(id).getBody();
                tarea.setEstado(false);
                updateTarea(tarea, id);
                BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_UNDONE.getMessage(), this);

            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }

        } else if (messageTextFromTelegram.indexOf(BotLabels.DELETE.getLabel()) != -1) {

            String delete = messageTextFromTelegram.substring(0,
                    messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
            Integer id = Integer.valueOf(delete);

            try {

                deleteTarea(id).getBody();
                BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DELETED.getMessage(), this);

            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }

        } else if (messageTextFromTelegram.equals(BotCommands.HIDE_COMMAND.getCommand())
                || messageTextFromTelegram.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel())) {

            BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), this);

        }
        else  {
            switch (currentState) {
                case CAPTURING_NAME:
                    // Manejar la captura del nombre de la tarea
                    // Avanzar al siguiente estado
                    currentState = TaskCreationState.CAPTURING_DESCRIPTION;
                    // Guardar el nombre de la tarea capturado del mensaje del usuario
                    String taskName = messageTextFromTelegram;
                    sendMessage("La tarea tiene nombre:" + taskName + " ", chatId);
                    tareaToBeConstructed.setNombre(taskName);
                    // Solicitar la descripción de la tarea
                    sendMessage("Por favor, ingresa la descripción de la tarea:", chatId);
                    break;
                case CAPTURING_DESCRIPTION:
                    // Manejar la captura de la descripción de la tarea
                    // Avanzar al siguiente estado
                    currentState = TaskCreationState.CAPTURING_PRIORITY;
                    // Guardar la descripción de la tarea capturada del mensaje del usuario
                    String taskDescription = messageTextFromTelegram;
                    sendMessage("La tarea tiene descripcion:" + taskDescription + " ", chatId);
                    tareaToBeConstructed.setDescripcion(taskDescription);
                    // Solicitar el estado de la tarea
                    sendMessage("Por favor, ingresa la prioridad de la tarea:", chatId);
                    break;

                case CAPTURING_PRIORITY:
                    // Manejar la captura de la prioridad de la tarea

                    // Convertir el mensaje del usuario en un entero para representar la prioridad
                    try {
                        int taskPriority = Integer.parseInt(messageTextFromTelegram);
                        // Guardar la prioridad de la tarea
                        // Solicitar los comentarios adicionales de la tarea
                        // Avanzar al siguiente estado
                        sendMessage("La tarea tiene prioridad:" + taskPriority + " ", chatId);
                        tareaToBeConstructed.setPrioridad(taskPriority);
                        currentState = TaskCreationState.CAPTURING_COMMENTS;
                        sendMessage("Por favor, ingresa los comentarios adicionales de la tarea:", chatId);
                    } catch (NumberFormatException e) {
                        // Manejar el caso en el que el usuario no haya ingresado un número válido
                        sendMessage("La prioridad debe ser un número entero. Por favor, ingresa la prioridad de la tarea (un número entero):", chatId);
                    }
                    break;
                case CAPTURING_COMMENTS:
                    // Manejar la captura de los comentarios adicionales de la tarea
                    // Avanzar al estado final
                    currentState = TaskCreationState.FINISHED;
                    // Guardar los comentarios adicionales de la tarea
                    String taskComments = messageTextFromTelegram;
                    sendMessage("La tarea tiene de comentario:" + taskComments + " ", chatId);
                    tareaToBeConstructed.setComentarios(taskComments);
                    // Avanzar al siguiente estado
                    // Ahora que se han capturado todos los campos de la tarea, puedes procesarla o almacenarla según sea necesario
                    // Por ejemplo, puedes guardar la tarea en una base de datos
                    // Luego, puedes enviar un mensaje al usuario para informar que la tarea ha sido creada exitosamente

                    tareaToBeConstructed.setFechaInicio(OffsetDateTime.now());
                    tareaToBeConstructed.setFechaFinal(OffsetDateTime.now());
                    tareaToBeConstructed.setEstado(false);

                    int idDesarrollador = getDesarrolladorIdByTelegramId(currentUserID).getBody();
                    int idProyecto = getProyectoIdByTelegramId(currentUserID).getBody();
                    logger.info(" idDesarrollador: " + idDesarrollador);
                    logger.info(" idProyecto: " + idProyecto);
                    tareaToBeConstructed.setDesarrollador(idDesarrollador);
                    tareaToBeConstructed.setProyecto(idProyecto);


                    ResponseEntity entity = addTarea(tareaToBeConstructed);

                    tareaToBeConstructed.setDefaultValues();
                    currentState = TaskCreationState.START;
                    sendMessage("¡La tarea ha sido creada exitosamente!", chatId);
                    break;
                case FINISHED:


                    sendMessage("¡ya deberia estar en la base de datos!", chatId);
                    // Manejar el estado final, por ejemplo, guardar la tarea en la base de datos
                    // Este caso no debería ocurrir, ya que una vez que se llega al estado final, no se espera recibir más mensajes
                    // Pero puedes manejarlo de manera apropiada si se presenta de alguna manera
                    break;
            }

        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    // GET /tareas
    public List<Tarea> getAllTareas() {
        return tareaService.findAll();
    }

    // GET BY ID /tareas/{id}
    public ResponseEntity<Tarea> getTareaById(@PathVariable int id) {
        try {
            ResponseEntity<Tarea> responseEntity = tareaService.getTareaById(id);
            return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST /tareas
    public ResponseEntity<?> addTarea(@RequestBody Tarea tarea) {
        Tarea nuevaTarea = tareaService.addTarea(tarea);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location", "/tareas/" + nuevaTarea.getIdTarea());
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).build();
    }

    // PUT /tareas/{id}
    public ResponseEntity<Tarea> updateTarea(@RequestBody Tarea tarea, @PathVariable int id) {
        try {
            Tarea tareaActualizada = tareaService.updateTarea(id, tarea);
            if (tareaActualizada != null) {
                return new ResponseEntity<>(tareaActualizada, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE tareas/{id}
    public ResponseEntity<Boolean> deleteTarea(@PathVariable int id) {
        try {
            boolean eliminada = tareaService.deleteTarea(id);
            if (eliminada) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> getUsuarioRolByTelegramId(@PathVariable long telegramId) {
        try {
            String rol = usuarioService.findRolByTelegramId(telegramId);
            return new ResponseEntity<>(rol, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Integer> getDesarrolladorIdByTelegramId(@PathVariable long telegramId) {
        try {
            int desarrolladorID = usuarioService.findDesarrolladorIdByTelegramId(telegramId);
            return new ResponseEntity<>(desarrolladorID, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Integer> getProyectoIdByTelegramId(@PathVariable long telegramId) {
        try {
            int proyectoID = usuarioService.findProyectoIdByTelegramId(telegramId);
            return new ResponseEntity<>(proyectoID, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}