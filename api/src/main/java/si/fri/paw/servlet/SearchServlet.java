package si.fri.paw.servlet;

import si.fir.paw.utility.beans.ReadBean;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@MultipartConfig
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SearchServlet.class.getName());

    @Inject
    private ReadBean readBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String tags = req.getParameter("param");

        readBean.getPostsByTags(tags.split(" "));

        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print("<html>");
        printWriter.print("<body>");
        printWriter.print("<h3>");
        printWriter.print("Searched tags: " + tags);
        printWriter.print("</h3>");
        printWriter.print("</body>");
        printWriter.print("</html>");

    }
}
