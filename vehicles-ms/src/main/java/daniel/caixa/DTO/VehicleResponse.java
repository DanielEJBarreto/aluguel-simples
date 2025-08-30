package daniel.caixa.DTO;

import daniel.caixa.Entity.VehicleStatus;

public class VehicleResponse {

    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private VehicleStatus status;
    private String engine;
    private String carTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getCarTitle() {
        return carTitle;
    }

    public void setCarTitle(String carTitle) {
        this.carTitle = carTitle;
    }
}
