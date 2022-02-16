package com.mycompany.advertising.model.dao;

import com.mycompany.advertising.model.to.UserCommentTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Amir on 2/13/2022.
 */
@Repository
public interface UserCommentRepository extends JpaRepository<UserCommentTo, Long> {

}
