package si.fri.paw.servlet;

import si.fir.paw.utility.beans.UserBean;
import si.fri.paw.entities.User;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/servlet")
public class JPAServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(JPAServlet.class.getName());

    @Inject
    private UserBean userBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<User> users = userBean.getAllUsersCrit();


        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print("<html>");
        printWriter.print("<body>");
        printWriter.print("<h3>");
        printWriter.print("The users should be listed below:");
        printWriter.print("</h3>");

        for (User user : users){
            printWriter.print("<p>Username: " + user.getUsername() + ", email: " + user.getEmail());
        }

        printWriter.print("" +
                "<form action=\"upload\" method=\"post\" enctype=\"multipart/form-data\">\n" +
                "    <input type=\"text\" name=\"description\" />\n" +
                "    <input type=\"file\" name=\"file\" />\n" +
                "    <input type=\"submit\" />\n" +
                "</form>" +
                "");

        printWriter.print("</body>");
        printWriter.print("</html>");
        printWriter.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info(req.toString());
    }
}
