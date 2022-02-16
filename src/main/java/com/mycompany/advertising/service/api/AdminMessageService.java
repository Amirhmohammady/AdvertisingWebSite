package com.mycompany.advertising.service.api;

import com.mycompany.advertising.model.to.AdminMessageTo;
import com.mycompany.advertising.model.to.UserCommentTo;
import org.springframework.data.domain.Page;

/**
 * Created by Amir on 1/27/2022.
 */
public interface AdminMessageService {
    AdminMessageTo addAdminMessage(AdminMessageTo adminMessage);

    AdminMessageTo getLastMessage();

    Page<AdminMessageTo> getPageAdminMessage(int page);

    AdminMessageTo getAdminMessageById(long id);

    void addUserComment(UserCommentTo userCommentTo);
}
