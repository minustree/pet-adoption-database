import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet
{
        public LoginServlet()
        {
                super();
        }

       public void drawHeader(HttpServletRequest req, PrintWriter out)
        {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Member logged in</title>");
			out.println("</head>");

			out.println("<body>");
			out.println("<p>");
			out.println("<center>");

        }


        public void drawFooter(HttpServletRequest req, PrintWriter out)
        {
			out.println("</center>");
			out.println("</p>");
			out.println("</body>");
			out.println("</html>");
        }


        private void drawUACATSOptions(HttpServletRequest req, PrintWriter out)
        {
				out.println("<font size=5 face=\"Arial,Helvetica\">");

				out.println("<b>Main Page</b></br>");
				out.println("</font>");

                out.println("<hr");
                out.println("<br><br>");

                out.println("<form name=\"Profile\" action=ProfileServlet method=get>");
                	out.println("<input type=submit name=\"showProfile\" value=\"Profile\">");
 				out.println("</form>");

 				out.println("<br>");

                out.println("<form name=\"AdoperList\" action=AdopterListServlet method=get>");
                	out.println("<input type=submit name=\"showAdopters\" value=\"Show adopters\">");
 				out.println("</form>");

                out.println("<br>");

                out.println("<form name=\"PetList\" action=PetListServlet method=get>");
					out.println("<input type=submit name=\"showPets\" value=\"Show pets\">");
				out.println("</form>");

                out.println("<br>");

                out.println("<form name=\"AddPet\" action=AddPetServlet method=get>");
					out.println("<input type=submit name=\"addPets\" value=\"Add pets\">");
				out.println("</form>");

                out.println("<br>");

                out.println("<form name=\"logout\" action=index.html>");
                	out.println("<input type=submit name=\"logoutUACATS\" value=\"Logout\">");
 				out.println("</form>");

        }

 		private void drawFailOptions(HttpServletRequest req, PrintWriter out)
        {
				out.println("<center>");
				out.println("<font size=5 face=\"Arial,Helvetica\">");

				out.println("<b>Login Failed</b></br>");
				out.println("</font>");

				out.println("</center>");

                out.println("<hr");
                out.println("<br><br>");


                out.println("<form name=\"logout\" action=index.html>");
				out.println("<center>");
				out.println("<input type=submit name=\"home\" value=\"Return to home\">");
				out.println("</center>");
 				out.println("</form>");

                out.println("<br>");
	    }


		public void drawLoginSuccess(HttpServletRequest req, PrintWriter out)
		{
				drawHeader(req,out);
				drawUACATSOptions(req,out);
			    drawFooter(req,out);
		}



		public void drawLoginFail(HttpServletRequest req, PrintWriter out)
		{
				drawHeader(req,out);
				drawFailOptions(req,out);
				drawFooter(req,out);
		}


        public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
        {

			Connection conn=null;
			try{
				Class.forName("oracle.jdbc.OracleDriver");  // load drivers
				System.out.println("Attempting to connect 000");
				conn = DriverManager.getConnection(OracleConnect.connect_string,OracleConnect.user_name,OracleConnect.password);
			}
			catch(Exception e){
				e.printStackTrace();
			}

			res.setContentType("text/html");
			PrintWriter out = res.getWriter();

			//if login success, call the following function
			if(loginSuccess(conn , req))
				drawLoginSuccess(req,out);
			//if fail, call the following function
			else
				drawLoginFail(req,out);

			try{
				conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
        }


        //Checks to see if the user exists.
        private boolean loginSuccess(Connection conn , HttpServletRequest request){

			if(request.getParameter("email") != null){
				//Get the email
				String email="";
				String[] paramValues = request.getParameterValues("email");
				email = paramValues[0];

				//Add this stuff to the session...
				HttpSession session = request.getSession();
				session.setAttribute("email" , ""+email);

				//Now query the database to see if this user exists......
				try{
					Statement s = conn.createStatement();
					System.out.println("Select EmailAddress from Member where EmailAddress = '" + email+"'");
					ResultSet rs = s.executeQuery("Select EmailAddress from Member where EmailAddress = '" + email+"'");
					int count = 0;
					while(rs.next()) {
						count++;
					}

					if(count == 0){
						System.out.println("Member does not exist");
						return false;
					}
				}catch(SQLException sqle){
					sqle.printStackTrace();
					return false;
				}

				return true;
			}
			else{
				//If the parameter is empty the session must have the info..
				HttpSession session = request.getSession();

				String email = (String)session.getAttribute("email");

				if(email != null){
					try{
						//Now query the database to see if this user exists......
						try{
							Statement s = conn.createStatement();
                            System.out.println("Select EmailAddress from Member where EmailAddress = '" + email+"'");
        					ResultSet rs = s.executeQuery("Select EmailAddress from Member where EmailAddress = '" + email+"'");
							int count = 0;
							while(rs.next())count++;

							if(count == 0){
								System.out.println("Member does not exist");
								return false;
							}
							else{
								return true;
							}
						}catch(SQLException sqle){
							sqle.printStackTrace();
							return false;
						}
					}
					catch(Exception e){
						e.printStackTrace();
						return false;
					}
				}
				else{
					return false;
				}
			}
		}
}
