// AddIndividualServlet.java

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

/**
 * This class is used to generate a response to queries given in the servlet.
 */

public class AddIndividualServlet extends HttpServlet
{
	String mtype; // member type (adopter, sender, both)

	// member data
	String dobmonth;
	String dobday;
	String dobyear;
	String name;
	String location;
	String email;
	String selfintroduction;
	String adoptingexperience;

	// pet data
	String petname;
	String petdobday;
	String petdobmonth;
	String petdobyear;
	String sterilized;
	String breed;
	String bitewire;
	String daysforwalk;

	String error[] = {"Error: User already exists.","Error: Request could not be carried out."};
	int errorCode;

	public AddIndividualServlet()
	{
		super();
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
	* Draws message on successful registration.
	*/
	public void drawUpdateMessage(HttpServletRequest req, PrintWriter out)
	{

		String name = req.getParameter("name");
		String dobmonth = req.getParameter("DoBmonth");
		String dobday = req.getParameter("DoBday");
		String dobyear = req.getParameter("DoByear");
		String email = req.getParameter("email");

		String blank_str = "";
		if(email.equals(blank_str))
		    email = "none";

		out.println("<h2 align=\"center\">Add New Member Success!</h2>");
		out.println("<br>");

		out.println("<p><b>Name:</b>  " + name+"</p>");
		out.println("<p><b>Date of Birth:</b>  " + dobmonth + "/" + dobday + "/" +dobyear + "</p>");
		out.println("<p><b>Email:</b>  " + email+"</p>");

		out.println("<br><br><br>");

		out.println("<form name=\"Main Menu\" action=LoginServlet>");
		out.println("<input type=submit name=\"Main Menu\" value=\"Main Menu\">");
		out.println("</form>");

		out.println("<br>");

		out.println("<form name=\"logout\" action=index.html>");
			out.println("<input type=submit name=\"logoutUACATS\" value=\"Logout\">");
		out.println("</form>");

	}

	/**
	* Draws message on failed registration.
	*/
	public void drawFailOption(HttpServletRequest req, PrintWriter out)
	{
		out.println("<h2 align=\"center\">"+error[errorCode]+"</h2>");
		out.println("<form name=\"logoutbad\" action=index.html>");
		out.println("<center>");
		out.println("<input type=submit name=\"tohomebad\" value=\"Return to Home\">&nbsp&nbsp");
		out.println("</center>");
		out.println("</form>");
	}

	/**
	* Draws page header.
	*/
	public void drawHeader(HttpServletRequest req, PrintWriter out)
	{
		out.println("<html>");
		out.println("<head>");
		out.println("<title>IndividualRegister</title>");
		out.println("</head>");

		out.println("<body>");
		out.println("<p>");
		out.println("<center>");
		out.println("<font size=5 face=\"Arial,Helvetica\">");
		out.println("<b>Register as Member</b><br></font>");

		out.println("<hr");
		out.println("<br><br>");
	}

	/**
	* Draws page footer.
	*/
	public void drawFooter(HttpServletRequest req, PrintWriter out)
	{
		out.println("</center>");
		out.println("</p>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	* Gives option to choose between registering as Adopter, Sender, or Both.
	*/
	public void drawChooseMenu(HttpServletRequest req, PrintWriter out)
	{
		out.println("<form name=\"Choose\" action=AddIndividualServlet method=get>");
		out.println("<br><br>");
		out.println("<font size=3>");

		out.println("<p align=\"center\">");
		out.println("<input type=\"radio\" name=\"mtype\" value=\"adopter\">");
		out.println("<label for=adopter>Adopter</label><br>");
		out.println("<input type=\"radio\" name=\"mtype\" value=\"sender\">");
		out.println("<label for=sender>Sender</label><br>");
		out.println("<input type=\"radio\" name=\"mtype\" value=\"both\">");
		out.println("<label for=both>Both</label>");
		out.println("<br><br>");

		out.println("<input type=submit name=\"Choose\" value=\"Choose\">&nbsp&nbsp");

		out.println("</form>");
		out.println("</tr>");
		out.println("</table>");
	}

	/**
	* Inputs basic info shared among all memnbers.
	*/
	public void drawMemberMenu(HttpServletRequest req, PrintWriter out)
	{
		out.println("<br><br>");
		out.println("<font size=3>");

		out.println("<p>");
		out.println("<b>Name:</b>");
		out.println("<input type=text name=\"name\">");
		out.println("<br>");
		out.println("</p>");

		out.println("<p>");
		out.println("<b>Location:</b>");
		out.println("<input type=text name=\"location\">");
		out.println("<br>");
		out.println("</p>");

		out.println("<h4 align=\"center\">Date of Birth:");
		out.println("<p align=\"center\">");
		out.println("<select size=\"12\" name=\"DoBmonth\">");
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
		out.println("<select size=\"1\" name=\"DoBday\">");
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
		out.println("<input type=text name=\"DoByear\"<br><br><br>");
		out.println("</p>");

		out.println("<p align=\"center\"><b>Email Address:</b>");
		out.println("<input type=text name=\"email\" size=20 <br><br><br>");
		out.println("</p>");

		out.println("<p align=\"center\"><b>Self Introduction:</b>");
		out.println("<input type=text name=\"selfintroduction\" size=50 <br><br><br>");
		out.println("</p>");
	}

	/**
	* Inputs pet info for senders.
	*/
	public void drawPetMenu(HttpServletRequest req, PrintWriter out)
	{
		out.println("<br><br>");
		out.println("<font size=3>");

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
	* Inputs adopting experience for adopters.
	*/
	public void drawAdopterMenu(HttpServletRequest req, PrintWriter out)
	{
		out.println("<p align=\"center\"><b>Adopting Experience:</b>");
		out.println("<input type=text name=\"adoptingexperience\" size=50 <br><br><br>");
	}

	/**
	* Draws all fields related to adopters.
	*/
	public void drawAddAdopterMenu(HttpServletRequest req, PrintWriter out)
	{
		out.println("<form name=\"AddIndividual\" action=AddIndividualServlet method=get>");
		drawMemberMenu(req,out);
		drawAdopterMenu(req,out);
		out.println("</form>");
		drawOptions(req,out);
		out.println("<br><br><br>");

	}

	/**
	* Draws all fields related to senders.
	*/
	public void drawAddSenderMenu(HttpServletRequest req, PrintWriter out)
	{
		out.println("<form name=\"AddIndividual\" action=AddIndividualServlet method=get>");
		drawMemberMenu(req,out);
		drawPetMenu(req,out);
		out.println("</form>");
		drawOptions(req,out);
		out.println("<br><br><br>");

	}

	/**
	* Draws all fields related to adopters and senders.
	*/
	public void drawAddBothMenu(HttpServletRequest req, PrintWriter out)
	{
		out.println("<form name=\"AddIndividual\" action=AddIndividualServlet method=get>");
		drawMemberMenu(req,out);
		drawAdopterMenu(req,out);
		drawPetMenu(req,out);
		out.println("</form>");
		drawOptions(req,out);
		out.println("<br><br><br>");

	}

	/**
     * Processes the HTTP Get request that is sent to this servlet.
	 * Adds a member as an adopter, sender, or both.
     */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		System.out.println("\nIn AddIndividualServlet doGet");
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		// establish connection
		Connection conn=null;
		try{
			Class.forName ("oracle.jdbc.OracleDriver"); // register drivers
			System.out.println("Attempting to connect 111");
			conn = DriverManager.getConnection(OracleConnect.connect_string,OracleConnect.user_name,OracleConnect.password);
		}catch(Exception e){
			e.printStackTrace();
		}

		// draw menus
		drawHeader(req,out);
		try {
			if (req.getParameter("Choose") == null && req.getParameter("Submit") == null) {
				drawChooseMenu(req,out);
			} else if(req.getParameter("Submit") == null || !enterInfo(req , conn))
			{
				String getType = getParam(req,"mtype");
				if (getType.equals("adopter")) {
					mtype = getType;
					drawAddAdopterMenu(req,out);
				} else if (getType.equals("sender")) {
					mtype = getType;
					drawAddSenderMenu(req,out);
				} else if (getType.equals("both")) {
					mtype = getType;
					drawAddBothMenu(req,out);
				} else {
					drawFailOption(req,out);
				}
			}
			else
			{
				drawUpdateMessage(req,out);
			}
		} catch (Exception e) {
			drawFailOption(req,out);
			e.printStackTrace();
		}

		drawFooter(req,out);
		try{
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	// input information into database
	boolean enterInfo(HttpServletRequest request , Connection conn){
		System.out.println("\nIn AddIndividualServlet enterInfo");

		// request data
		name = getParam(request, "petname");
		location = getParam(request, "location");
		dobday = getParam(request, "DoBday");
		dobmonth = getParam(request, "DoBmonth");
		dobyear = getParam(request, "DoByear");
		email = getParam(request, "email");
		selfintroduction = getParam(request, "selfintroduction");
		if (mtype.equals("adopter") || mtype.equals("both"))
			adoptingexperience = getParam(request, "adoptingexperience");
		else adoptingexperience = "";

		// request data for sender/both
		if (mtype.equals("sender") || mtype.equals("both")) {
			petname = getParam(request, "petname");
			petdobday = getParam(request, "petDoBday");
			petdobmonth = getParam(request, "petDoBmonth");
			petdobyear = getParam(request, "petDoByear");
			breed = getParam(request, "breed");
			daysforwalk = getParam(request, "daysforwalk");

			bitewire = getParam(request, "bitewire");
			if (bitewire.equals("Yes")) bitewire = "'y'";
			if (bitewire.equals("No")) bitewire = "'n'";

			sterilized = getParam(request, "sterilized");
			if (sterilized.equals("Yes")) sterilized = "y";
			if (sterilized.equals("No")) sterilized = "n";

			if (!breed.equals("Rabbit")) bitewire = "NULL";
			if (!breed.equals("Dog")) daysforwalk = "";
		}

		// set IsAdopter/IsSender
		String memType = "";
		if (mtype.equals("adopter")) memType = "'y','n'";
		if (mtype.equals("sender")) memType = "'n','y'";
		if (mtype.equals("both")) memType = "'y','y'";

		// check if user exists
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("Select EmailAddress from Member where EmailAddress = '" + email+"'");
			int count = 0;
			while(rs.next())count++;
			if(count > 0){
				errorCode = 0;
				return false;
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("Error1");
			errorCode = 1;
			return false;
		}

		// insert member
		try {
			PreparedStatement s = conn.prepareStatement("INSERT INTO MEMBER VALUES('"+email+"' , ? , ?, ? , '"
			+dobday +"-"+dobmonth +"-"+dobyear+"' , "
			+memType+" , ?)");
			s.setString(1, name);
			s.setString(2,selfintroduction);
			s.setString(3,location);
			s.setString(4,adoptingexperience);
			s.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("Error2");
			errorCode = 1;
			return false;
		}

		// insert pet if applicable
		if (mtype.equals("sender") || mtype.equals("both")) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate("INSERT INTO PET VALUES('"
				+email+"' , '"
				+petname+"' , '"
				+petdobday +"-"+petdobmonth +"-"+petdobyear+"' , '"
				+sterilized+"', '"
				+breed+"' , '"
				+daysforwalk+"' , "
				+bitewire+")");
			} catch(SQLException sqle) {
				sqle.printStackTrace();
				System.out.println("Error3");
				errorCode = 1;
				return false;
			}
		}

		// add info into the session
		HttpSession session = request.getSession();
		session.setAttribute("email" , ""+email);
		return true;
	}
}
