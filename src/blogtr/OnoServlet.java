package blogtr;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.rdbms.AppEngineDriver;

import java.sql.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class OnoServlet extends HttpServlet {
   
   private static final Logger log = Logger.getLogger(ContentServlet.class.getName());

   public void doPost(HttpServletRequest req, HttpServletResponse resp)
         throws IOException {
      PrintWriter out = resp.getWriter();
      Connection c = null;
      
      try {
      DriverManager.registerDriver(new AppEngineDriver());
      c = DriverManager.getConnection("jdbc:google:rdbms://transmog/transmog");
      String ono = req.getParameter("ono");
      
      if (ono == "") {
          out.println("<html><head></head><body>Missing word input. Try again! Redirecting in 3 seconds...");
      } else {
          String statement ="INSERT INTO ono (word) VALUES ( ? )";
          PreparedStatement stmt = c.prepareStatement(statement);
          stmt.setString(1, ono);
          int success = 2;
          success = stmt.executeUpdate();
          if (success == 1) {
              out.println("<html><head></head><body>Success! Redirecting in 3 seconds...</body></html>");
          } else if (success == 0) {
              out.println("<html><head></head><body>Failure! Please try again! Redirecting in 3 seconds...</body></html>");
          }
      }
      
      } catch (SQLException e) {
          e.printStackTrace();
      } finally {
          if (c != null)
              try {
                  c.close();
              } catch (SQLException ignore) {
                  
              }
      }
      
      UserService userService = UserServiceFactory.getUserService();
      User user = userService.getCurrentUser();
      
      resp.setHeader("Refresh", "3; url=/ono.jsp");    
   }
}
