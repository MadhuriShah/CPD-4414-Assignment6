package sevices;
import entities.product;
import entities.productList;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author C0647610
 */
@MessageDriven(mappedName = "jms/Queue")
public class productListener implements MessageListener {

    @Inject productList ProductList;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String jsonString = ((TextMessage) message).getText();
                JsonObject json = Json.createReader(new StringReader(jsonString)).readObject();
                ProductList.add(new product(json));
            }
        } catch (JMSException ex) {
            System.err.println("JMS Failure");
        } catch (Exception ex) {
            Logger.getLogger(productListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}