package com.mycompany.advertising.service.api;

import com.mycompany.advertising.model.to.AdminMessageTo;
import com.mycompany.advertising.model.to.UserCommentTo;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Created by Amir on 1/27/2022.
 */
public interface AdminMessageService {
    AdminMessageTo addAdminMessage(AdminMessageTo adminMessage);

    AdminMessageTo getLastMessage();

    Page<AdminMessageTo> getPageAdminMessage(int page);

    Optional<AdminMessageTo> getAdminMessageById(Long id);

    void addUserComment(UserCommentTo userCommentTo);

    int deleteUserCommentById(long id);

    int deleteAdminMessageById(long id);
}
