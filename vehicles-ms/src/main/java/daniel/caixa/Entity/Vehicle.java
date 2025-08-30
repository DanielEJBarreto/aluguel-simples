package daniel.caixa.Entity;

import jakarta.persistence.*;

@Entity
    public class Vehicle {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String brand;
        private String model;
        @Column(name = "vehicle_year")
        private Integer year;

        @Enumerated(EnumType.STRING)
        private VehicleStatus status;
        private String engine;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public Integer getYear() {
            return year;
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

        public String getEngine() {
            return engine;
        }

        public void setEngine(String engine) {
            this.engine = engine;
        }


    }