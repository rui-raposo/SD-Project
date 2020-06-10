package beans;

import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;

@Stateless
@ManagedBean(name="Service")

public class Service_Stuff {

    // Class variables
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    private ArrayList<Integer> values = new ArrayList<>();
    // ---------------------------------------------------------------------------------------------------------------------------------------------------

    // Getters and Setters
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public ArrayList<Integer> getValues() {
        return values;
    }

    public void setValues(ArrayList<Integer> values) {
        this.values = values;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------


    /* 
     * Goal       : Generate the graphic values
     * Parameters : None
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    

}
