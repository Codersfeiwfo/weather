public class WeatherRecord {
    private String city;
    private String temperature;
    private String description;
    private String datetime;

    public WeatherRecord(String city, String temperature, String description, String datetime) {
        this.city = city;
        this.temperature = temperature;
        this.description = description;
        this.datetime = datetime;
    }

    public String getCity() {
        return city;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getDatetime() {
        return datetime;
    }
}