// PetListServlet.java
import java.io.*;             // For PrintWriter
import java.sql.*;            // You need to import the java.sql package to use JDBC

import javax.servlet.*;       // For ServletException and other things
import javax.servlet.http.*;  // For HttpServlet and other HttpServlet classes
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This class is used to generate a response to a query given in the
   JDBCServletDemo servlet.  It will make a connection to the database,
   execute the query to find the customers that live in the given city,
   and will generate an HTML page with an HTML list for the result.
 */
public class PetListServlet extends HttpServlet {
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
      System.out.println("Exception in PetListServlet: " + e);
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

    //String userEmail = request.getParameter("userEmail");
      
    // Make the query to the database and get the results.
    try {
      ResultSet result = stmt.executeQuery ("select EmailAddress, Name, Birthday, IsSterilized, Breed, DaysWalk, BiteWire " +
                                          "  from Pet");


      out.println("<html>\n" +
                  "<head><title>Adopter list</title></head>\n" +
                  "<body bgcolor=\"#FDF5E6\">\n" +
                  " <p> \n");
      out.println("   <ul>"); // start an HTML list

      int flag = 0;
      while(result.next()){
        if (flag == 0) {
          out.println("The pets we have are:");
        }

        String IsSterilized = "sterilized";
        if (result.getString(4).equals("n")){
          IsSterilized = "unsterilized";
        } 
        out.println(" <li> ");

        String bite = null;
        Integer days = null;
        out.println("<form name=\"addFav \" action=AddFavPetServlet method=get>");
        if (result.getString(5).equals("Dog")) {
          days = result.getInt(6);
          out.println(result.getString(1) + "\t" + result.getString(2) + "\t" + result.getDate(3) + "\t" +  IsSterilized + "\t" + result.getString(5) + "\t" + result.getInt(6) + " days for a walk");
        } else if (result.getString(5).equals("Rabbit")) {
          bite = (result.getString(7).equals("y")) ? "bite wires" : "not bite wires";
          out.println(result.getString(1) + "\t" + result.getString(2) + "\t" + result.getDate(3) + "\t" +  IsSterilized + "\t" + result.getString(5) + "\t" + bite);
        } else {
          out.println(result.getString(1) + "\t" + result.getString(2) + "\t" + result.getDate(3) + "\t" +  IsSterilized + "\t" + result.getString(5));
        }
        out.println("<input type=hidden name=\"petEmail\" value=" + result.getString(1) +">");
        out.println("<input type=hidden name=\"petName\" value=" + result.getString(2)+ ">");
        out.println("<input type=hidden name=\"petBirthday\" value=" + dateConvert(result.getDate(3))+ ">");
        out.println("<input type=hidden name=\"petIsSter\" value=" +IsSterilized+">");
        out.println("<input type=hidden name=\"petBreed\" value=" +result.getString(5)+">");
        out.println("<input type=hidden name=\"petDayWalk\" value="+days+">");
        out.println("<input type=hidden name=\"petBite\" value="+bite+">");
        //out.println("<input type=hidden name=\"user\" value=userEmail>");

        out.println("<input type=submit name=\"addFav\" value=\"Add this pet to favorite\">");
        out.println("</form>");


        out.println(" </li> ");
        flag = 1;
      }
      if (flag == 0) {
        out.println("\nThere is no favorite pet of that member!\n");
      }
      out.println("   </ul>" + " </p>");

      out.println("\n\n");
      out.println("<form name=\"mainpage \" action=main.html>");
      out.println("<input type=submit name=\"goMain\" value=\"go back to main page\">");
      out.println("</form>");

      out.println("</body></html>");

    } catch (Exception e) {
      out.println("<html>\n" +
                  "<head><title>Search Fail</title></head>\n" +
                  "<body bgcolor=\"#FDF5E6\">\n" +
                  " <p> \n" +
                  "  fail to find the info for the pets");

      out.println("<form name=\"mainpage \" action=main.html>");
      out.println("<input type=submit name=\"goMain\" value=\"go back to main page\">");
      out.println("</form>");

      out.println(" </p>" +
                  "</body></html>");
      System.out.println("Exception in PetListServlet: " + e);
    }
  }


  private String dateConvert(Date D){
      SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
      try{
          return format2.format(D);
      } catch(Exception e) {
          System.out.println("Error in PetListServlet: date not date");
      }
      return null;
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
      System.out.println("Exception in PetListServlet: " + e);
    }

  }
}
