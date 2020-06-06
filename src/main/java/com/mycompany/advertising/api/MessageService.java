package com.mycompany.advertising.api;

import com.mycompany.advertising.model.to.MessageTo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Amir on 11/10/2019.
 */
public interface MessageService {
    Long addMessage(MessageTo messageTo);
    List<MessageTo> getMessages();
    List<MessageTo> getPageMessages(int page);
}
