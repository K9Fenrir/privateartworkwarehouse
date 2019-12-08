package si.fri.paw.servlet;

import si.fir.paw.utility.Exceptions.InvalidParameterException;
import si.fir.paw.utility.beans.CreateBean;
import si.fir.paw.utility.dtos.create.UserCreateDTO;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@MultipartConfig
@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(JPAServlet.class.getName());

    @Inject
    private CreateBean createBean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");

        UserCreateDTO udto = new UserCreateDTO();
        udto.setUsername(username);
        udto.setEmail(email);
        try {
            createBean.createNewUser(udto);
        }
        catch (InvalidParameterException e){

        }
        resp.sendRedirect("servlet");
    }
}
