
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

@WebServlet("/Edit")
public class Edit extends HttpServlet 
{
   private static final long serialVersionUID = 1L;

   public Edit() 
   {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		   throws ServletException, IOException 
   {
      String sku = request.getParameter("sku");
      String quant = request.getParameter("quant");
      String msrp = request.getParameter("msrp");

      Connection connection = null;
      String insertSql = "UPDATE inventory SET quantity = ?, msrp = ? WHERE sku = ?";

      try 
      {
         DBConnection.getDBConnection(getServletContext());
         connection = DBConnection.connection;
         
         if (quant.isEmpty() && msrp.isEmpty())
         {
        	 quant = "No Change";
        	 msrp = "No Change";
         }
         else if (!quant.isEmpty() && msrp.isEmpty())
         {
        	 msrp = "No Change";
        	 insertSql = "UPDATE inventory SET quantity = ? WHERE sku = ?";
        	 PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
             preparedStmt.setString(1, quant);
             preparedStmt.setString(2, sku);
             preparedStmt.execute();
             connection.close();
         }
         else if (quant.isEmpty() && !msrp.isEmpty())
         {
        	 quant = "No Change";
        	 insertSql = "UPDATE inventory SET msrp = ? WHERE sku = ?";
        	 PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
             preparedStmt.setString(1, msrp);
             preparedStmt.setString(2, sku);
             preparedStmt.execute();
             connection.close();
         }
         else
         {
        	 insertSql = "UPDATE inventory SET quantity = ?, msrp = ? WHERE sku = ?";
        	 PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
             preparedStmt.setString(1, quant);
             preparedStmt.setString(2, msrp);
             preparedStmt.setString(3, sku);
             preparedStmt.execute();
             connection.close();
         }
         
      } 
      catch (Exception e) 
      {
         e.printStackTrace();
      }

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Table edit";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h2 align=\"center\">" + title + "</h2>\n" + //
            "<ul>\n" + //

            "  <li><b>SKU</b>: " + sku + "\n" + //
            "  <li><b>New Quantity</b>: " + quant + "\n" + //
            "  <li><b>New MSRP</b>: " + msrp + "\n" + //

            "</ul>\n");

      out.println("<a href=/TechEx/edit.html>Edit Another</a> <br>");
      out.println("<a href=/TechEx/search.html>Search Data</a> <br>");
      out.println("</body></html>");
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		   throws ServletException, IOException 
   {
      doGet(request, response);
   }

}
