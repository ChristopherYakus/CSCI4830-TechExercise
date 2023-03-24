import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Search")
public class Search extends HttpServlet 
{
   private static final long serialVersionUID = 1L;

   public Search() 
   {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		   throws ServletException, IOException 
   {
      String sku = request.getParameter("sku");
      String name = request.getParameter("name");
      search(sku, name, response);
   }

   void search(String sku, String name, HttpServletResponse response) throws IOException 
   {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Database Result";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null; //the query itself
      try 
      {
         DBConnection.getDBConnection(getServletContext());
         connection = DBConnection.connection;

         if (sku.isEmpty() && name.isEmpty()) //if none 
         {
            String selectSQL = "SELECT * FROM inventory";
            preparedStatement = connection.prepareStatement(selectSQL);
         } 
         else if (sku.isEmpty() && !name.isEmpty()) //if only name
         {
        	 String selectSQL = "SELECT * FROM inventory WHERE name LIKE ?";
             String Name = name + "%";
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, Name);
         }
         else if (name.isEmpty() && !sku.isEmpty()) // if only sku
         {
        	 String selectSQL = "SELECT * FROM inventory WHERE sku = ?";
             String SKU = sku;
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, SKU);
         }
         else  //if both TODO
         {
            String selectSQL = "SELECT * FROM inventory WHERE name LIKE ? AND sku = ?";
            String Name = name + "%";
            String SKU = sku;
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, Name);
            preparedStatement.setString(2, SKU);
         }
         ResultSet rs = preparedStatement.executeQuery();
         
         boolean check = true;
         while (rs.next()) 
         {
        	check = false;
            int Sku = rs.getInt("sku");
            String Name = rs.getString("name").trim();
            int Quant = rs.getInt("quantity");
            int MSRP = rs.getInt("msrp");

            if (sku.isEmpty() || Name.contains(name)) 
            {
               out.println("SKU: " + Sku + ",  ");
               out.println("Item Name: " + Name + ",  ");
               out.println("Quantity: " + Quant + ",  ");
               out.println("MSRP: $" + MSRP + "<br>");
            }
         }
         if (check)
         {
        	 out.println("No Items Found <br>");
         }
         out.println("<a href=/TechEx/search.html>Search Data</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } 
      catch (SQLException se) 
      {
         se.printStackTrace();
      } 
      catch (Exception e) 
      {
         e.printStackTrace();
      } 
      finally 
      {
         try 
         {
            if (preparedStatement != null)
               preparedStatement.close();
         } 
         catch (SQLException se2) 
         {
         }
         try 
         {
            if (connection != null)
               connection.close();
         } 
         catch (SQLException se) 
         {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		   throws ServletException, IOException 
   {
      doGet(request, response);
   }

}
