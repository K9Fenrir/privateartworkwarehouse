package si.fri.paw.servlet;

import si.fir.paw.utility.beans.*;
import si.fir.paw.utility.dtos.PostCreationDTO;
import si.fir.paw.utility.dtos.PostEditDTO;
import si.fri.paw.entities.Post;
import si.fri.paw.entities.Tag;
import si.fri.paw.entities.User;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
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
    private CreationBean creationBean;

    @Inject
    private SearchBean searchBean;

    @Inject
    private EditBean editBean;

    @Inject
    private UserBean userBean;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        addTestPosts();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<User> users = userBean.getAllUsersCrit();

        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print("<html>");
        printWriter.print("<body>");
        printWriter.print("<h3>");
        printWriter.print("Users:");
        printWriter.print("</h3>");

        for (User user : users){
            log.info("Username: " + user.getUsername() + ", email: " + user.getEmail());
            printWriter.print("" +
                    "<p>" +
                        "ID: " + user.getId() + ", Username: " + user.getUsername() + ", email: " + user.getEmail() +
                    ", Fav posts:");
            for (Post p : user.getFavourites()){
                printWriter.print(" " + p.getId());
            }
            printWriter.print("</p>");
        }

        printSelectedUser("Xelphos", printWriter);

        printWriter.print("<p>Add new User:</p>");
        printWriter.print("" +
                "<form action=\"user\" method=\"post\">\n" +
                "    <input type=\"text\" name=\"username\" />\n" +
                "    <input type=\"text\" name=\"email\" />\n" +
                "    <input type=\"submit\" />\n" +
                "</form>" +
                "");

        printWriter.print("<h3>");
        printWriter.print("Tags:");
        printWriter.print("</h3>");

        List<Tag> tags = searchBean.getAllTags();

        for (Tag tag : tags){
            log.info("Name: " + tag.getId() + ", description: " + tag.getDescription());
            printWriter.print("" +
                    "<p>" +
                    "Name: " + tag.getId() + ", Description: " + tag.getDescription() +
                    ", Posts:");
            for (Post p : tag.getTaggedPosts()){
                printWriter.print(" " + p.getId());
            }
            printWriter.print("</p>");
        }


        printWriter.print("<p>Add new Tag:</p>");
        printWriter.print("" +
                "<form action=\"tag\" method=\"post\">\n" +
                "    <input type=\"text\" name=\"id\" />\n" +
                "    <input type=\"text\" name=\"description\" />\n" +
                "    <input type=\"text\" name=\"type\" />\n" +
                "    <input type=\"submit\" />\n" +
                "</form>" +
                "");

        printWriter.print("<h3>");
        printWriter.print("Posts:");
        printWriter.print("</h3>");

        List<Post> posts = searchBean.getAllPosts();

        for (Post post : posts){
            log.info("ID: " + post.getId() + ", description: " + post.getDescription());
            printWriter.print("" +
                    "<p>" +
                    "ID: " + post.getId() + ", Description: " + post.getDescription() +
                    "</p>");
            printWriter.print("Tags: ");
            String tagsString = "";
            for (Tag tag : post.getPostTags()){
                tagsString += tag.getId() + ", ";
            }
            printWriter.print(tagsString);

            printWriter.print("<br>");

            printWriter.print("FavourtedBy: ");
            String favByString = "";
            for (User user : post.getFavouritedBy()){
                favByString += user.getUsername() + ", ";
            }
            printWriter.print(favByString);

        }

        printWriter.print("<br>");

        printWriter.print("<h3>");
        printWriter.print("FILE UPLOAD:");
        printWriter.print("</h3>");

        printWriter.print("<form method=\"POST\" action=\"upload\" enctype=\"multipart/form-data\" >\n" +
                "            File:\n" +
                "            <input type=\"file\" name=\"file\" id=\"file\" /> <br/>\n" +
                "            Description:\n" +
                "            <input type=\"text\" value=\"Test description\" name=\"description\"/>\n" +
                "            </br>\n" +
                "            Tags:\n" +
                "            <input type=\"text\" value=\"canine\" name=\"tags\"/>\n" +
                "            </br>\n" +
                "            <input type=\"submit\" value=\"Upload\" name=\"upload\" id=\"upload\" />\n" +
                "        </form>");

        printWriter.print("<br>");

        printWriter.print("<h3>");
        printWriter.print("Search by tags:");
        printWriter.print("</h3>");

        printWriter.print("" +
                "<form method=\"get\" action=\"/search\">\n" +
                "  <input type=\"text\" name=\"param\">\n" +
                "  <button type=\"submit\">Search</button>\n" +
                "</form>" +
                "");

        printWriter.print("</body>");
        printWriter.print("</html>");
        printWriter.close();

    }

    private void printSelectedUser(String username, PrintWriter printWriter){
        List<User> users = searchBean.getUserByUsername(username);
        printWriter.print("SELECTED USER: " + username);
        for (User user : users){
            log.info("Username: " + user.getUsername() + ", email: " + user.getEmail());
            printWriter.print("" +
                    "<p>" +
                    "ID: " + user.getId() + ", Username: " + user.getUsername() + ", email: " + user.getEmail() +
                    "</p>");
        }
    }

    private void addTestPosts(){

        PostCreationDTO p1 = new PostCreationDTO();

        p1.setDescription("Test description for post 1");
        p1.setTagNames(new String[]{"F-R95", "canine", "outdoors"});
        p1.setRating("safe");
        p1.setAuthorID(1);

        try {
            creationBean.createNewPost(p1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PostCreationDTO p2 = new PostCreationDTO();

        p2.setDescription("Test description for post 2");
        p2.setTagNames(new String[]{"Hioshiru", "standing", "human"});
        p2.setRating("safe");
        p2.setAuthorID(2);

        try {
            creationBean.createNewPost(p2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PostEditDTO t1 = new PostEditDTO();
        t1.setEditPostID(1);
        t1.setNewTags(new String[]{"feline", "hat"});

        PostEditDTO t2 = new PostEditDTO();
        t2.setEditPostID(1);
        t2.setTagsToRemove(new String[]{"canine", "outdoors"});

        editBean.addTags(t1);
        editBean.removeTags(t2);

    }

}
