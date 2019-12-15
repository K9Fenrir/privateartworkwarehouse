package si.fir.paw.utility.beans.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import si.fir.paw.utility.beans.entity.UserBean;
import si.fri.paw.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class AuthenticationBean implements PasswordEncoder {

    @Inject
    UserBean userBean;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final Logger log = Logger.getLogger(AuthenticationBean.class.getName());

    public boolean authenticate(String username, String password){
        User user = userBean.getUserByUsername(username);

        log.info(user.getPassHash());
        log.info(encode(password));

        return matches(password, user.getPassHash());
    }

    @Override
    public String encode(CharSequence charSequence) {
        return encoder.encode(charSequence);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encoder.matches(charSequence, s);
    }
}
