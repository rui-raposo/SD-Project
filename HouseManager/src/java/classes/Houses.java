package classes;
public class Houses {
    
    private String owner;
    private String address;
    private String zip_Code;
    private String properties;
    private int rate;
    private float price;
    private String path;

    public Houses(String owner, String address, String zip_Code, String properties, float price, int rate, String path) {
        this.owner = owner;
        this.address = address;
        this.zip_Code = zip_Code;
        this.price = price;
        this.properties = properties;
        this.rate = rate;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip_Code() {
        return zip_Code;
    }

    public void setZip_Code(String zip_Code) {
        this.zip_Code = zip_Code;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
    
    public String getProperties() {
        return properties;
    }

}
