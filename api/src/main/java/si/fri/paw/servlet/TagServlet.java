package si.fri.paw.servlet;

import si.fir.paw.utility.beans.CreateBean;
import si.fir.paw.utility.dtos.create.TagCreateDTO;

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
@WebServlet("/tag")
public class TagServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(JPAServlet.class.getName());

    @Inject
    private CreateBean createBean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("id");
        String description = req.getParameter("description");
        String type = req.getParameter("type");

        TagCreateDTO tdto = new TagCreateDTO();
        tdto.setName(name);
        tdto.setDescription(description);
        tdto.setType(type);

        createBean.createNewTag(tdto);

        resp.sendRedirect("servlet");
    }

}
