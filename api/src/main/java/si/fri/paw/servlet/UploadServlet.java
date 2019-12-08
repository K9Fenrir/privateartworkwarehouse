package si.fri.paw.servlet;

import si.fir.paw.utility.beans.CreateBean;
import si.fir.paw.utility.dtos.create.PostCreateDTO;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.logging.Logger;

@MultipartConfig
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(JPAServlet.class.getName());

    @Inject
    private CreateBean createBean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Create path components to save the file
        final String description = req.getParameter("description");
        final String tags = req.getParameter("tags");
        final Part filePart = req.getPart("file");
        final String fileName = getFileName(filePart);

        PostCreateDTO pdto = new PostCreateDTO();

        pdto.setAuthorID(1);
        pdto.setDescription(description);
        pdto.setRating("safe");
        pdto.setTagNames(tags.split(" "));
//        pdto.setFilePart(filePart);

//        createBean.createNewPost(pdto);

        resp.sendRedirect("servlet");

    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        log.info("Part Header = {0} " + partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}
