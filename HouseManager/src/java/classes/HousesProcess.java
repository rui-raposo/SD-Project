package classes;
public class HousesProcess {
    
    private Houses house;
    private String color, status;
    
    public HousesProcess(Houses house, String color, String status) {
        this.house = house;
        this.color = color;
        this.status = status;
    }
    
    public Houses getHouse() {
        return house;
    }

    public void setHouse(Houses house) {
        this.house = house;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
  
}
