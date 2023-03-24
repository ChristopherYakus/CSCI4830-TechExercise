
/**
 * @file SimpleFormInsert.java
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Insert")
public class Insert extends HttpServlet 
{
   private static final long serialVersionUID = 1L;

   public Insert() 
   {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		   throws ServletException, IOException 
   {
      String sku = request.getParameter("sku");
      String name = request.getParameter("name");
      String quant = request.getParameter("quant");
      String msrp = request.getParameter("msrp");

      Connection connection = null;
      String insertSql = " INSERT INTO inventory (sku, name, quantity, msrp) values (?, ?, ?, ?)";

      try 
      {
         DBConnection.getDBConnection(getServletContext());
         connection = DBConnection.connection;
         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
         preparedStmt.setString(1, sku);
         preparedStmt.setString(2, name);
         preparedStmt.setString(3, quant);
         preparedStmt.setString(4, msrp);
         preparedStmt.execute();
         connection.close();
      } 
      catch (Exception e) 
      {
         e.printStackTrace();
      }

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Insert Data to DB table";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h2 align=\"center\">" + title + "</h2>\n" + //
            "<ul>\n" + //

            "  <li><b>SKU</b>: " + sku + "\n" + //
            "  <li><b>Name</b>: " + name + "\n" + //
            "  <li><b>Quantity</b>: " + quant + "\n" + //
            "  <li><b>MSRP</b>: $" + msrp + "\n" + //

            "</ul>\n");

      out.println("<a href=/TechEx/insert.html>Insert Another</a> <br>");
      out.println("<a href=/TechEx/search.html>Search Data</a> <br>");
      out.println("</body></html>");
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		   throws ServletException, IOException 
   {
      doGet(request, response);
   }

}
