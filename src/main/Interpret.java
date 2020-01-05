package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Interpret
 */
@WebServlet("/Interpret")
public class Interpret extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Interpret() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter writer = null;
		processScores p = new processScores(getServletContext().getRealPath(""));
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String command = request.getParameter("commandIn");
		if(command.equals("!help")) {
			writer.println("<html><body><h2>Help Page</h2><hr><p>!team <last four of team ID> - show team ID<br>" + 
					"!monitor <last four of team ID #1> <last four of team ID #2> (coming soon) - monitor two teams with notifications<br>" + 
					"!scoreboard <state, tier or division> <state, tier or division> <state, tier or division> - show a filtered view of the public scoreboard<br>" + 
					"!help - for help</p></body></html>");		}
		else {
			ArrayList<String[]> temp = null;
			
			if(command.contains("!team")) {
				String teamID = command.split(" ")[1];
				temp = p.getTeamboard(teamID);
				writer.println("<html>");
				writer.println("<head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"><meta http-equiv=\"refresh\" content=\"30\"></head>");
				writer.println("<body>");
				writer.println("<h2>Showing Team Detail for Team " + teamID + " as of " + new Date() + "</h2>");
				writer.println("<hr>");
				writer.println("<table border=\"4\" class='CSSTableGenerator'>");
				for(int r=0; r<2; r++) {
					writer.println("<tr>");
					for(int c=0; c<temp.get(0).length; c++) {
						writer.print("<td>"+temp.get(r)[c]+"</td>");
					}
					writer.println("</tr>");
				}
				writer.println("</table>");
				writer.println("<table border=\"4\" class='CSSTableGenerator'>");
				writer.println("<br>");
				for(int r=2; r<temp.size(); r++) {
					writer.println("<tr>");
					for(int c=0; c<temp.get(2).length; c++) {
						writer.print("<td>"+temp.get(r)[c]+"</td>");
					}
					writer.println("</tr>");
				}
				writer.println("</table>");
				writer.println("</body>");
				writer.println("</html>");
			}
			else {
				if(command.contains("!scoreboard")) {
					String[] parms = command.split(" ");
					int parmLength = parms.length - 1;
					if(parmLength == 1) {
						String cmd1 = parms[1];
						Object parm1 = this.determineCmd(cmd1);
						temp = p.trim(parm1);
					}
					if(parmLength == 2) {
						String cmd1 = parms[1];
						Object parm1 = this.determineCmd(cmd1);
						String cmd2 = parms[2];
						Object parm2 = this.determineCmd(cmd2);
						temp = p.trim(parm1, parm2);
					}
					if(parmLength == 3) {
						String cmd1 = parms[1];
						Object parm1 = this.determineCmd(cmd1);
						String cmd2 = parms[2];
						Object parm2 = this.determineCmd(cmd2);
						String cmd3 = parms[3];
						Object parm3 = this.determineCmd(cmd3);
						temp = p.trim(parm1, parm2, parm3);
					}
					writer.println("<html>");
					writer.println("<head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"><meta http-equiv=\"refresh\" content=\"30\"></head>");
					writer.println("<body>");
					writer.println("<h2>Showing Filtered Scoreboard as of " +  new Date() + "</h2>");
					writer.println("<hr>");
					writer.println("<table border=\"4\" class='CSSTableGenerator'>");
					writer.println("<tr><td>National Place</td><td>Team Number</td><td>Location/Category</td><td>Division</td><td>Tier</td><td>Score Images</td><td>Play Time (HH:MM)</td><td>Warnings</td><td>CCS Score</td><td>Filtered Place</td></tr>");

					for(int r=0; r<temp.size(); r++) {
						writer.println("<tr>");
						for(int c=0; c<temp.get(0).length; c++) {
							writer.print("<td>" + temp.get(r)[c] + "</td>");
						}
						writer.println("</tr>");
					}
					writer.println("</table>");
					writer.println("</body>");
					writer.println("</html>");
				}
				if(command.contains("!monitor")) {
					
				}
				else {
					writer.println("<html><body><p>No command found. Go back and type !help for help</p></body></html>");
					
				}
				
			}
		}
		
	}
	
	public Object determineCmd(String parm) {
		if(parm.equals("Open") || parm.equals("All Service") || parm.equals("Middle School")) {
			Division d = new Division(parm);
			return d;
		}
		if(parm.equals("Gold") || parm.equals("Silver") || parm.equals("Platinum")) {
			Tier t = new Tier(parm);
			return t;
		}
		else {
			State s = new State(parm);
			return s;
		}
	}

}