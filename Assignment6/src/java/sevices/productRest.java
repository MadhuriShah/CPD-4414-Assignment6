/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sevices;

import entities.product;
import entities.productList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author c0647610
 */
@Path("/product")
@RequestScoped
public class productRest {
    @Inject
    productList ProductList;
    @GET
    @Produces("application/json")
    public Response getAll(){
          return Response.ok(ProductList.toJSON()).build();
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getbyId(@PathParam ("id") int id){
        return Response.ok(ProductList.get(id).toJSON()).build();
    }
     @POST
    @Consumes("application/json")
    public Response add(JsonObject json) {
        return null;
    }
    @PUT
    @Path("{id}")
     @Consumes("application/json")
    public Response set(@PathParam("id") int id, JsonObject json) {
        try {
            product p = new product(json);
          ProductList.set(id, p);
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(productRest.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(500).build();
        }
    }
     @DELETE
    @Path("{id}")
    @Consumes("application/json")
    public Response delete(@PathParam("id") int id) {
        
        return null;
    
}
}
