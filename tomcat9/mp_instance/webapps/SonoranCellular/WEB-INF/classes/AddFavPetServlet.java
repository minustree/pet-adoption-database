// AddFavPetServlet.java
import java.io.*;             // For PrintWriter
import java.sql.*;            // You need to import the java.sql package to use JDBC

import javax.servlet.*;       // For ServletException and other things
import javax.servlet.http.*;  // For HttpServlet and other HttpServlet classes


/**
 * This class is used to generate a response to a query given in the
   JDBCServletDemo servlet.  It will make a connection to the database,
   execute the query to find the customers that live in the given city,
   and will generate an HTML page with an HTML list for the result.
 */
public class AddFavPetServlet extends HttpServlet {
  private Connection conn;
  private Statement stmt;

  String message[] = {"Error: This record already exist", "Exception: Unable to find Fav", "Exception: Unable to add Fav", "Added this to your fav list!"};

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
      System.out.println("Exception in AddFavPetServlet: " + e);
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
    // Build the HTML page we want to send as a response to a query given to
    // our JDBCServletDemo servlet.
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    // We use the request object to get the parameter (which should
    // be the name of the city) that should have been sent to this servlet.
    String petEmail = request.getParameter("petEmail");
    String petName = request.getParameter("petName");
    String petBirthday = request.getParameter("petBirthday");
    String petIsSter = request.getParameter("petIsSter");
    String petBreed = request.getParameter("petBreed");
    String petDayWalk = request.getParameter("petDayWalk");
    String petBite = request.getParameter("petBite");
    //String userEmail = request.getParameter("userEmail");

    HttpSession session = request.getSession();
    String email = (String)session.getAttribute("email");
    
    int messagenum;
    // Makesure this like is not in the history
    try {
      ResultSet result = stmt.executeQuery ("select * " +
                                          "  from Favorite where EmailAddressMember = '" + email + "'" + 
                                                          "  AND EmailAddressPet = '" + petEmail + "'" +
                                                          "  AND PetName = '" + petName + "'" +
                                                          "  AND Birthday = '" + petBirthday + "'");

      if (result.next()) { 
        messagenum = 0; 
      } else {
        // insert member
        try {
          stmt.executeUpdate("INSERT INTO Favorite VALUES('"
          +email+"' , '"
          +petEmail+"' , '"
          +petName+"', '"
          +petBirthday+"')");
          messagenum = 3;
        } catch(SQLException sqle) {
          messagenum = 2;

          sqle.printStackTrace();
          System.out.println("not able to insert");
        } 
      }
    } catch (Exception e) {
      messagenum = 1;
      System.out.println("Exception in PetListServlet: " + e);
    }


    out.println("<html>\n" +
                "<head><title>Add Fav Result</title></head>\n" +
                "<body bgcolor=\"#FDF5E6\">\n" +
                " <p> \n");
    out.println(email + " " + petEmail + " " + petName + " " + petBirthday + " ");
    out.println(message[messagenum]);
    out.println(" </p>");
    out.println("\n\n");
    out.println("<form name=\"PetList \" action=PetListServlet method=get>");
    out.println("<input type=submit name=\"showPets\" value=\"Show pets\">");
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
      System.out.println("Exception in AddFavPetServlet: " + e);
    }

  }
}
