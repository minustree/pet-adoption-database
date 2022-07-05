import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LogoutServlet extends HttpServlet
{
	public LogoutServlet()
	{
			super();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		System.out.println("Loggin out");
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();

		session.removeAttribute("email");

		out.println("<html>");
		out.println("<form name=\"logout\" action=index.html>");
		out.println("<input type=submit name=\"home\" value=\"Return to home\">");
		out.println("</form>");
	}

}
