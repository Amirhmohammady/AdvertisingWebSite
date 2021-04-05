package com.mycompany.advertising.api;

import com.mycompany.advertising.model.to.MessageTo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 11/10/2019.
 */
public interface MessageService {

    Long addMessage(MessageTo messageTo);

    List<MessageTo> getAllMessages();

    Optional<MessageTo> getMessageById(Long id);

    Page<MessageTo> getPageMessages(int page);

    Page<MessageTo> getPageMessages(int page, String search);

    void deleteMessageById(Long id);
}
