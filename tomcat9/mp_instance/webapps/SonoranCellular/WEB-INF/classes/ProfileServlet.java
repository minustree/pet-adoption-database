// ProfileServlet.java
import java.io.*;             // For PrintWriter
import java.sql.*;            // You need to import the java.sql package to use JDBC

import javax.servlet.*;       // For ServletException and other things
import javax.servlet.http.*;  // For HttpServlet and other HttpServlet classes
import java.util.Calendar;

/**
 * This class is used to generate a response to a query given in the
   JDBCServletDemo servlet.  It will make a connection to the database,
   execute the query to find the customers that live in the given city,
   and will generate an HTML page with an HTML list for the result.
 */
public class ProfileServlet extends HttpServlet {
  private Connection conn;
  private Statement stmt;

  /**
   * The init() method is called when the servlet is first created and
   * is NOT called again for each user request.  So, it is used for
   * one-time initializations. For example, since we could use the same
   * database Connection and Statement for each call to this servlet,
   * initialize the database Connection and Statement here.
   *
   * @throws ServletException a general exception a servlet can throw when it
   *                          encounters difficulty
   */
  public void init() throws ServletException {
    try {
      // Load the Oracle JDBC driver
      Class.forName("oracle.jdbc.OracleDriver");  // load drivers

      // Connect to the database
      // You should put your name and password for the 2nd and 3rd parameter.
      conn =
        DriverManager.getConnection (
	     "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle",
             OracleConnect.user_name,
             OracleConnect.password);

      // Create a Statement
      stmt = conn.createStatement();
    } catch (Exception e) {
      System.out.println("Exception in ProfileServlet: " + e);
    }
  }

  /**
   * Processes the HTTP Get request that is sent to this servlet.
   * This is where we want to put most of our code.  Within this
   * method we will create the query we want to execute and send it to
   * the Statement object created in init().  What we get back is a
   * ResultSet, which is essentially a java.sql object that represents
   * the table (relation) that results from the query.  We iterate
   * through this ResultSet and print out the names of the customers
   * in the given city.
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
                                          throws ServletException, IOException {
    
    HttpSession session = request.getSession();
    String email = (String)session.getAttribute("email");

    // Build the HTML page we want to send as a response to a query given to
    // our JDBCServletDemo servlet.
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    out.println("<html>\n" +
                  "<head><title>Adopter list</title></head>\n" +
                  "<body bgcolor=\"#FDF5E6\">\n");

    try {
      ResultSet rset = stmt.executeQuery ("select EmailAddress, Name, SelfIntroduction, Location, Birthday, IsAdopter, IsSender, AdoptingExperience " +
                                          "  from Member where EmailAddress = '" + email + "'");

      out.println(" <p> \n" +
                  "   Info\n"+"<br>");
      if (rset.next ()){
        out.println("   \tEmailAddress:\t" + rset.getString(1) +"<br>");
        out.println("   \tname:\t" + rset.getString(2)+"<br>");
        out.println("   \tSelfIntroduction:\t" + rset.getString(3)+"<br>");
        out.println("   \tLocation:\t" + rset.getString(4)+"<br>");
        out.println("   \tBirthday:\t" + rset.getDate(5)+"<br>");
        if (rset.getString(6) == "y" && rset.getString(7) == "y") {
          out.println("   \tRoles:\tSender and Adopter"+"<br>");
        } else if (rset.getString(6) == "y" ){
          out.println("   \tRoles:\tAdoper"+"<br>");
        } else {
          out.println("   \tRoles:\tSender"+"<br>");
        }
        out.println("   \tAdoptingExperience:\t" + rset.getString(8)+"<br>");  
      } else {
        out.println("Error: Does not found your info?!"+"<br>");
      }
      out.println(" </p>");
    } catch (Exception e) {
      out.println("catch Exception in ProfileServlet 102"+"<br>");
      System.out.println("Exception in ProfileServlet: " + e);
    }


    try {
      ResultSet rset = stmt.executeQuery ("select Name, Birthday, Breed " +
                                          "  from Pet where EmailAddress = '" + email + "'");
      out.println(" <p> \n" +
                  "   Pet Info\n"+"<br>");
      while (rset.next()){
        Date date = rset.getDate(2);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int age = 2021 - calendar.get(Calendar.YEAR);
        out.println("   \tname:\t" + rset.getString(1) + "\tAge:\t" + age + "\tBreed:\t" + rset.getString(3)+"<br>");
      }
      out.println(" </p>");

    } catch (Exception e) {
      out.println("catch Exception in ProfileServlet 119"+"<br>");
      System.out.println("Exception in ProfileServlet: " + e);
    }


    try {
      ResultSet rset = stmt.executeQuery ("select distinct PetName " +
                                          "  from Favorite where EmailAddressMember = '" + email + "'");
      out.println(" <p> \n" +
                  "   Favorite Pet Names\n"+"<br>");
      while (rset.next()){
        out.println("   \tname:\t" + rset.getString(1)+"<br>");
      }
      out.println(" </p>");

    } catch (Exception e) {
      out.println("catch Exception in ProfileServlet 135"+"<br>");
      System.out.println("Exception in ProfileServlet: " + e);
    }


    out.println("\n\n");
    out.println("<form name=\"mainpage \" action=main.html>");
    out.println("<input type=submit name=\"goMain\" value=\"go back to main page\">");
    out.println("</form>");

    out.println("</body></html>");
  }


  /**
   * Called when the servlet is being destroyed.  Here we can close
   * our Statement and Connection.
   */
  public void destroy() {
    // Disconnect from the database.
    try {
      stmt.close();
      conn.close();
    } catch (Exception e) {
      System.out.println("Exception in ProfileServlet: " + e);
    }

  }
}
