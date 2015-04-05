/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author c0647610
 */
@ApplicationScoped
public class productList {
    
    private List<product> productList = new ArrayList<>();

    public productList() {
        try(Connection cn=getConnection()){
            String query="select * from product";
            PreparedStatement pstmt = cn.prepareStatement(query);
             ResultSet rs=pstmt.executeQuery();
             product p;
             while(rs.next()){
                 p=new product(
                           rs.getInt("productId"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("quantity"));
                productList.add(p);
             
             }
            
        }
        catch (SQLException ex){
            Logger.getLogger(productList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   private Connection getConnection() {
        Connection cn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbc = "jdbc:mysql://localhost/assignment3";
            String user = "root";
            String pass = "";
            cn = DriverManager.getConnection(jdbc, user, pass);
            System.out.println("Connection");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(productList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cn;
    }
    private String getResults(String query, String... params) {
        StringBuilder sb = new StringBuilder();
        try (Connection conn =getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sb.append(String.format("%s\t%s\t%s\t%s\n", rs.getInt("productId"), rs.getString("name"), rs.getString("description"), rs.getInt("quantity")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(productList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    private int doUpdate(String query, String... params) {
        int changes = 0;
        try (Connection conn =getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            changes = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(productList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return changes;
    }
    public JsonArray toJSON() {
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (product p : productList)
            json.add(p.toJSON());
        return json.build();
    }
  public product get(int ProductId) {
        product result = null;
        for (int i =0; i < productList.size() && result == null; i++){
            product p = productList.get(i);
            if (p.getProductId()== ProductId) {
                result = p;
            }
        }
        return result;
    }
  public void set(int productId, product product) throws Exception {
        int result = doUpdate("UPDATE product SET name = ?, description = ?. quantity = ? WHERE productId = ?" ,
                product.getName(),
                product.getDescription(),
                String.valueOf(product.getQuantity()),
                String.valueOf(productId)
        );
        if(result == 1) {
            product original = get(productId);
            original.setName(product.getName());
        
        }
        else throw new Exception("Error: Update"); 
        product original = get(productId);
        original.setName(product.getName());
        original.setDescription(product.getDescription());
        original.setQuantity(product.getQuantity());
    }
        public void remove(int productId) throws Exception {
        int result = doUpdate("Delete from product where productId=?",String.valueOf(productId));
        
        if(result > 0) {
            //the successful, now remove from list
            product original = get(productId);
            productList.remove(original);
        }
         else throw new Exception("Error: Delete"); 
    }
    
}
