// AddPetServlet.java
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
public class AddPetServlet extends HttpServlet {
  private Connection conn;
  private Statement stmt;

  String message[] = {"Error: This record already exist", "Exception: Unable to find Pet", "Exception: Unable to add Pet", "Added this to the pet list!","No Message?!", "error1!"};



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
      System.out.println("Exception in AddPetServlet: " + e);
    }
  }
  

  public void doGet(HttpServletRequest request, HttpServletResponse response)
                                          throws ServletException, IOException {

    // Build the HTML page we want to send as a response to a query given to
    // our JDBCServletDemo servlet.
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    int messagenum = 4;

    String petname;
    String petdobday;
    String petdobmonth;
    String petdobyear;
    String breed;
    String daysforwalk;
    String bitewire;
    String petBirthday;
    String sterilized;

    HttpSession session = request.getSession();
    String email = (String)session.getAttribute("email");

    // check if this user is a sender
    try{
      ResultSet result = stmt.executeQuery ("select * " +
                                          "  from Member where EmailAddress = '" + email + "' AND IsSender = 'n'");

      if (result.next()) { 
        out.println("<html>\n" +
                "<head><title>Add Pet Result</title></head>\n" +
                "<body bgcolor=\"#FDF5E6\">\n" +
                " <p> \n");
        out.println("You are not a sender, please goto the main page");
        out.println(" </p>");
        out.println("\n\n");
        out.println("<form name=\"goMain \" action=main.html>");
        out.println("<input type=submit name=\"goMain\" value=\"return to main page\">");
        out.println("</form>");
        out.println("</body></html>");
        return;
      } 
    } catch (Exception e) {
      messagenum = 5;
      System.out.println("Exception in PetListServlet: " + e);
    }

    if (request.getParameter("Submit") == null) {
      drawAddPetMenu(request, out);
    } else{

      petname = getParam(request, "petname");
      petdobday = getParam(request, "petDoBday");
      petdobmonth = getParam(request, "petDoBmonth");
      petdobyear = getParam(request, "petDoByear");
      breed = getParam(request, "breed");
      sterilized = getParam(request, "sterilized");
      daysforwalk = getParam(request, "daysforwalk");
      bitewire = getParam(request, "bitewire");

      petBirthday = petdobday +"-"+petdobmonth +"-"+petdobyear;

      if (bitewire.equals("Yes")) bitewire = "y";
      if (bitewire.equals("No")) bitewire = "n";

      if (sterilized.equals("Yes")) sterilized = "y";
      if (sterilized.equals("No")) sterilized = "n";

      // check if this pet exist in db
      try {
        ResultSet result = stmt.executeQuery ("select * " +
                                            "  from Pet where EmailAddress = '" + email + "'" + 
                                                            "  AND Name = '" + petname + "'" +
                                                            "  AND Birthday = '" + petBirthday + "'");
        if (result.next()) { 
          messagenum = 0; 
        } else {
          // insert pet
          try {
            if (breed.equals("Rabbit")) {
              stmt.executeUpdate("INSERT INTO PET VALUES('"+email+"' , '"+petname+"' , '"+petBirthday+"' , '"
                                  +sterilized+"', '"+breed+"' , null , '" + bitewire +"')");
            } else if (breed.equals("Dog")){
              stmt.executeUpdate("INSERT INTO PET VALUES('"+email+"' , '"+petname+"' , '"+petBirthday+"' , '"
                                  +sterilized+"', '"+breed+"' , '" + daysforwalk +"' , null)");
            } else {
              stmt.executeUpdate("INSERT INTO PET VALUES('"+email+"' , '"+petname+"' , '"+petBirthday+"' , '"
                                  +sterilized+"', '"+breed+"' , null , null)");
            }
           
            messagenum = 3;
          } catch(SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Error3");
            messagenum = 2;
          }
        }
      } catch (Exception e) {
        messagenum = 1;
        System.out.println("Exception in PetListServlet: " + e);
      }
      out.println("<html>\n" +
                "<head><title>Add Pet Result</title></head>\n" +
                "<body bgcolor=\"#FDF5E6\">\n" +
                " <p> \n");
      out.println(email + " " + petname + " " + petBirthday + " " + sterilized + " " + breed + " " + daysforwalk + " " + bitewire);
      out.println(message[messagenum]);
      out.println(" </p>");
      out.println("\n\n");
      out.println("<form name=\"goMain \" action=main.html>");
      out.println("<input type=submit name=\"goMain\" value=\"return to main page\">");
      out.println("</form>");
      out.println("</body></html>");
    }
  }


  /**
  * Draws all fields related to senders.
  */
  public void drawAddPetMenu(HttpServletRequest req, PrintWriter out)
  {
    out.println("<form name=\"AddPet\" action=AddPetServlet method=get>");
    drawPetMenu(req,out);
    out.println("</form>");
    drawOptions(req,out);
    out.println("<br><br><br>");

  }

  /**
  * Draws submission and cancellation buttons.
  */
  public void drawOptions(HttpServletRequest req, PrintWriter out)
  {
    out.println("<p align=\"center\">");
    out.println("<p>");
    out.println("<input type=submit name=\"Submit\" value=\"Submit\">&nbsp&nbsp");
    out.println("</p>");

    out.println("<form name=\"logout\" action=index.html>");
    out.println("<input type=submit name=\"tohome\" value=\"Return To Home\">&nbsp&nbsp");
    out.println("</form>");
    out.println("<br><br><br>");
    out.println("</p>");
  }


  /**
  * Inputs pet info for senders.
  */
  public void drawPetMenu(HttpServletRequest req, PrintWriter out)
  {
    out.println("<br><br>");
    out.println("<font size=3>");
    out.println("<h4 align=\"center\">");

    out.println("<b>Pet</b>");

    out.println("<p>");
    out.println("<b>Name:</b>");
    out.println("<input type=text name=\"petname\">");
    out.println("<br>");
    out.println("</p>");

    out.println("<h4 align=\"center\">Date of Birth");
    out.println("<p align=\"center\">");
    out.println("<select size=\"12\" name=\"petDoBmonth\">");
    out.println("<option selected value=\"null\">Month</option>");
    out.println("<option value=\"JAN\">January</option>");
    out.println("<option value=\"FEB\">February</option>");
    out.println("<option value=\"MAR\">March</option>");
    out.println("<option value=\"APR\">April</option>");
    out.println("<option value=\"MAY\">May</option>");
    out.println("<option value=\"JUN\">June</option>");
    out.println("<option value=\"JUL\">July</option>");
    out.println("<option value=\"AUG\">August</option>");
    out.println("<option value=\"SEP\">September</option>");
    out.println("<option value=\"OCT\">October</option>");
    out.println("<option value=\"NOV\">November</option>");
    out.println("<option value=\"DEC\">December</option>");
    out.println("</select>");
    out.println("</p>");

    out.println("<p align=\"center\">");
    out.println("<select size=\"1\" name=\"petDoBday\">");
    out.println("<option selected value=\"null\">Day</option>");
    out.println("<option value=\"01\">01</option>");
    out.println("<option value=\"02\">02</option>");
    out.println("<option value=\"03\">03</option>");
    out.println("<option value=\"04\">04</option>");
    out.println("<option value=\"05\">05</option>");
    out.println("<option value=\"06\">06</option>");
    out.println("<option value=\"07\">07</option>");
    out.println("<option value=\"08\">08</option>");
    out.println("<option value=\"09\">09</option>");
    out.println("<option value=\"10\">10</option>");
    out.println("<option value=\"11\">11</option>");
    out.println("<option value=\"12\">12</option>");
    out.println("<option value=\"13\">13</option>");
    out.println("<option value=\"14\">14</option>");
    out.println("<option value=\"15\">15</option>");
    out.println("<option value=\"16\">16</option>");
    out.println("<option value=\"17\">17</option>");
    out.println("<option value=\"18\">18</option>");
    out.println("<option value=\"19\">19</option>");
    out.println("<option value=\"20\">20</option>");
    out.println("<option value=\"21\">21</option>");
    out.println("<option value=\"22\">22</option>");
    out.println("<option value=\"23\">23</option>");
    out.println("<option value=\"24\">24</option>");
    out.println("<option value=\"25\">25</option>");
    out.println("<option value=\"26\">26</option>");
    out.println("<option value=\"27\">27</option>");
    out.println("<option value=\"28\">28</option>");
    out.println("<option value=\"29\">29</option>");
    out.println("<option value=\"30\">30</option>");
    out.println("<option value=\"31\">31</option>");
    out.println("</select>");
    out.println("</p>");

    out.println("<p align=\"center\"><b>Year:</b>");
    out.println("<input type=text name=\"petDoByear\"<br><br><br>");
    out.println("</p>");

    out.println("<b>Sterilized:</b>");
    out.println("<p align=\"center\">");
    out.println("<input type=\"radio\" name=\"sterilized\" value=\"Yes\">");
    out.println("<label for=Yes>Yes</label><br>");
    out.println("<input type=\"radio\" name=\"sterilized\" value=\"No\">");
    out.println("<label for=No>No</label><br>");
    out.println("<br>");
    out.println("</p>");

    out.println("<b>Breed:</b>");
    out.println("<p align=\"center\">");
    out.println("<input type=\"radio\" name=\"breed\" value=\"Cat\">");
    out.println("<label for=Cat>Cat</label><br>");
    out.println("<input type=\"radio\" name=\"breed\" value=\"Dog\">");
    out.println("<label for=Dog>Dog</label><br>");
    out.println("<input type=\"radio\" name=\"breed\" value=\"Rabbit\">");
    out.println("<label for=Rabbit>Rabbit</label><br>");
    out.println("<br>");
    out.println("</p>");

    out.println("<b> Bite Wires:</b>");
    out.println("<p align=\"center\">");
    out.println("<input type=\"radio\" name=\"bitewire\" value=\"Yes\">");
    out.println("<label for=Yes>Yes</label><br>");
    out.println("<input type=\"radio\" name=\"bitewire\" value=\"No\">");
    out.println("<label for=No>No</label><br>");
    out.println("<br>");
    out.println("</p>");

    out.println("<p align=\"center\"><b>Days for Walk:</b>");
    out.println("<input type=text name=\"daysforwalk\"<br><br><br>");
    out.println("</p>");
  }


  /**
  * Retrieves the first parameter using the given request and param value.
  */
  String getParam(HttpServletRequest request, String param) {
    String[] values = request.getParameterValues(param);
    if (values != null) {
      return values[0];
    }
    return "NULL";
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
      System.out.println("Exception in AddPetServlet: " + e);
    }

  }
}
