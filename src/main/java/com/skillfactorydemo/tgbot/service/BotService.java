package com.skillfactorydemo.tgbot.service;

import com.skillfactorydemo.tgbot.dto.ValuteCursOnDate;
import com.skillfactorydemo.tgbot.entity.ActiveChat;
import com.skillfactorydemo.tgbot.repository.ActiveChatRepository;
import com.skillfactorydemo.tgbot.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import javax.naming.ldap.PagedResultsControl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotService extends TelegramLongPollingBot{
    private final CentralRussianBankService centralRussianBankService;
    public final FinanceService financeService;
    private final ActiveChatRepository activeChatRepository;
    private static final String CURRENT_RATES = "/currentrates";
    private static final String HELLO_TEST = "/hello";
    public static final String ADD_INCOME = "/addincome";
    public static final String ADD_SPEND = "/addspend";
    private Map<Long, List<String>> previousCommands = new ConcurrentHashMap<>();
    @Value("${bot.api.key}")
    private String apiKey;
    @Value("${bot.name")
    private String name;
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        try{
            SendMessage response = new SendMessage();
            Long chatId = message.getChatId();
            response.setChatId(String.valueOf(chatId));

            if(CURRENT_RATES.equalsIgnoreCase(message.getText())){
                for(ValuteCursOnDate valuteCursOnDate : centralRussianBankService.getCurrenciesFromCbr()){
                    response.setText(StringUtils.defaultIfBlank(response.getText(), "") + valuteCursOnDate.getName() + " - "
                    + valuteCursOnDate.getCourse() + "\n");
                }
            }else if(HELLO_TEST.equalsIgnoreCase(message.getText())){
                response.setText("Hey there");
            } else if (ADD_INCOME.equalsIgnoreCase(message.getText())) {
                response.setText("Отправьте мне сумму полученного дохода");
            }else if(ADD_SPEND.equalsIgnoreCase(message.getText())){
                response.setText("Отправьте мне сумму расходов");
            }else {
                response.setText(financeService.addFinanceOperation(getPreviousCommand(message.getChatId()), message.getText(), message.getChatId()));
            }
            putPreviousCommand(message.getChatId(), message.getText());
            execute(response);

            if (activeChatRepository.findActiveChatByChatId(chatId).isEmpty()) {
                ActiveChat activeChat = new ActiveChat();
                activeChat.setChatId(chatId);
                activeChatRepository.save(activeChat);
            }
        }catch (TelegramApiException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void sendNotificationToAllActiveChats(String message, Set<Long> chatIds) {
        for (Long id : chatIds) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(id));
            sendMessage.setText(message);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    private void putPreviousCommand(Long chatid, String command){
        if(previousCommands.get(chatid) == null){
            List<String> commands = new ArrayList<>();
            commands.add(command);
            previousCommands.put(chatid, commands);
        }else {
            previousCommands.get(chatid).add(command);
        }
    }
    private String getPreviousCommand(Long chatId){
        return previousCommands.get(chatId).get(previousCommands.get(chatId).size() - 1);
    }
    @PostConstruct
    public void start(){
        log.info("username: {}, token:{}", name, apiKey);
    }
    @Override
    public String getBotUsername(){
        return name;
    }
    @Override
    public String getBotToken(){
        return apiKey;
    }

}
