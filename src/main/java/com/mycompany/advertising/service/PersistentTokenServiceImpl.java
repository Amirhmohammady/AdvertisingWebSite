package com.mycompany.advertising.service;

/**
 * Created by Amir on 12/8/2021.
 */

/*import com.mycompany.advertising.model.to.PersistentLogins;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;*/

//@Repository("persistentTokenRepository")
//@Service
//@Transactional
public class PersistentTokenServiceImpl{}/* implements PersistentTokenRepository {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    PersistentLoginsRepository persistentLoginsRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLogins logins = new PersistentLogins();
        logins.setUsername(token.getUsername());
        logins.setSeries(token.getSeries());
        logins.setToken(token.getTokenValue());
        logins.setLastUsed(token.getDate());
        persistentLoginsRepository.save(logins);
        logger.debug("token created " + token.getUsername());
    }

    @Override
    public void updateToken(String series, String tokenValue, LocalDateTime lastUsed) {
        PersistentLogins logins = persistentLoginsRepository.findTopBySeries(series);
        logins.setToken(tokenValue);
        logins.setLastUsed(lastUsed);
        //persistentLoginsRepository.save(logins);
        logger.debug("token for series " + series + " updated");
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        PersistentLogins logins = persistentLoginsRepository.findTopBySeries(seriesId);
        if (logins != null) {
            logger.debug("search for seriesId " + seriesId + " and get " + logins.getUsername());
            return new PersistentRememberMeToken(logins.getUsername(),
                    logins.getSeries(), logins.getToken(), logins.getLastUsed());
        }
        logger.debug("search for seriesId " + seriesId + " and returned null");
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        logger.debug("tokens for user " + username + " deleted");
        persistentLoginsRepository.deleteByUsername(username);
    }
}*/
/*    //@Autowired
    private SessionFactory hibernatesessionFactory;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLoginsTo logins = new PersistentLoginsTo();
        logins.setUsername(token.getUsername());
        logins.setSeries(token.getSeries());
        logins.setToken(token.getTokenValue());
        logins.setLastUsed(token.getDate());
        hibernatesessionFactory.getCurrentSession().save(logins);
        logger.debug("token created " + token.getUsername());
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        PersistentLoginsTo logins = hibernatesessionFactory.getCurrentSession()
                .get(PersistentLoginsTo.class, seriesId);

        if (logins != null) {
            return new PersistentRememberMeToken(logins.getUsername(),
                    logins.getSeries(), logins.getToken(), logins.getLastUsed());
        }

        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        hibernatesessionFactory.getCurrentSession().createQuery("delete from PersistentLogins"
                + " where username=:userName")
                .setParameter("userName", username).executeUpdate();
    }

    @Override
    public void updateToken(String series, String tokenValue, LocalDateTime lastUsed) {
        Session session = hibernatesessionFactory.getCurrentSession();
        PersistentLoginsTo logins = session.get(PersistentLoginsTo.class, series);
        logins.setToken(tokenValue);
        logins.setLastUsed(lastUsed);
    }
    private SessionFactory hibernateFactory;

    //@Bean
    @Autowired
    public void SomeService(EntityManagerFactory factory) {
        if(factory.unwrap(SessionFactory.class) == null){
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.hibernatesessionFactory = factory.unwrap(SessionFactory.class);
    }
*/