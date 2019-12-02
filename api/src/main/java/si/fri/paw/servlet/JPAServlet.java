package si.fri.paw.servlet;

import si.fir.paw.utility.beans.*;
import si.fir.paw.utility.dtos.create.PostCreateDTO;
import si.fir.paw.utility.dtos.delete.PostDeleteDTO;
import si.fir.paw.utility.dtos.delete.TagDeleteDTO;
import si.fir.paw.utility.dtos.delete.UserDeleteDTO;
import si.fir.paw.utility.dtos.read.PostDTO;
import si.fir.paw.utility.dtos.read.TagDTO;
import si.fir.paw.utility.dtos.update.PostUpdateDTO;
import si.fir.paw.utility.dtos.update.TagUpdateDTO;
import si.fir.paw.utility.dtos.update.UserUpdateDTO;
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
    private CreateBean createBean;

    @Inject
    private ReadBean readBean;

    @Inject
    private UpdateBean updateBean;

    @Inject DeleteBean deleteBean;

    @Inject
    private UserBean userBean;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        addTestPosts();

//        demoPostEdits();

//        demoTagEdits();

//        demoUserEdits();

//        demoDeleteTag();

//        demoDeletePost();

//        demoDeleteUser();

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

        List<TagDTO> tags = readBean.getAllTags();

        for (TagDTO tag : tags){
            log.info("Name: " + tag.getName() + ", description: " + tag.getDescription());
            printWriter.print("" +
                    "<p>" +
                    "Name: " + tag.getName() + ", Description: " + tag.getDescription() +
                    "");

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

        List<Post> posts = readBean.getAllPosts();

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
        List<User> users = readBean.getUserByUsername(username);
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

        PostCreateDTO p1 = new PostCreateDTO();

        p1.setDescription("Test description for post 1");
        p1.setTagNames(new String[]{"F-R95", "canine", "outdoors"});
        p1.setRating("safe");
        p1.setAuthorID(1);

        try {
            createBean.createNewPost(p1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PostCreateDTO p2 = new PostCreateDTO();

        p2.setDescription("Test description for post 2");
        p2.setTagNames(new String[]{"Hioshiru", "standing", "human"});
        p2.setRating("safe");
        p2.setAuthorID(2);

        try {
            createBean.createNewPost(p2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PostUpdateDTO t1 = new PostUpdateDTO();
        t1.setEditPostID(2);
        t1.setFavouriteEditUserID(1);

        updateBean.newFavouritePost(t1);

    }

    private void demoPostEdits(){

        PostUpdateDTO p1 = new PostUpdateDTO();
        p1.setEditPostID(2);
        p1.setNewTags(new String[]{"feline", "hat"});
        p1.setFavouriteEditUserID(1);
        p1.setScoreIncrement(1);

        PostUpdateDTO p2 = new PostUpdateDTO();
        p2.setEditPostID(1);
        p2.setTagsToRemove(new String[]{"canine", "outdoors"});
        p2.setDescriptionEdit("Edited description");
        p2.setFavouriteEditUserID(3);

        updateBean.addTags(p1);
        updateBean.removeTags(p2);

        updateBean.newFavouritePost(p2);
        updateBean.removeFavouritePost(p1);

        updateBean.updatePostDescription(p2);
        updateBean.updatePostScore(p1);
    }

    private void demoTagEdits(){

        TagUpdateDTO t1 = new TagUpdateDTO();
        t1.setId("window");
        t1.setDescription("Edited description");
        t1.setType("artist");

        updateBean.updateTag(t1);

    }

    private void demoUserEdits(){

        UserUpdateDTO u1 = new UserUpdateDTO();
        u1.setId(3);
        u1.setNewUsername("Asistent");
        u1.setNewEmail("asistent@gmail.com");

        updateBean.updateUserUsername(u1);
        updateBean.updateUserEmail(u1);
    }

    private void demoDeleteTag(){

        TagDeleteDTO t1 = new TagDeleteDTO();
        t1.setId("canine");

        deleteBean.deleteTag(t1);

    }

    private void demoDeletePost(){

        PostDeleteDTO p1 = new PostDeleteDTO();
        p1.setPostID(1);

        deleteBean.deletePost(p1);

    }

    private void demoDeleteUser(){

        UserDeleteDTO u1 = new UserDeleteDTO();
        u1.setToDeleteID(1);

        deleteBean.deleteUser(u1);
    }

}
