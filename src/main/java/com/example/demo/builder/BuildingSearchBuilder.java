package com.example.demo.builder;

import lombok.Getter;

//chứa các field giống với BuildingSearchRequest
@Getter
public class BuildingSearchBuilder {
    private String name;
    private String address;
    private String rank;
    private Double areaFrom;
    private Double areaTo;
    private Double sellingPrice;
    private Integer numberLivingFloor;
    private Integer numberBasement;

    private BuildingSearchBuilder(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
        this.rank = builder.rank;
        this.areaFrom = builder.areaFrom;
        this.areaTo = builder.areaTo;
        this.sellingPrice = builder.sellingPrice;
        this.numberLivingFloor = builder.numberLivingFloor;
        this.numberBasement = builder.numberBasement;
    }

    public static class Builder {
        private String name;
        private String address;
        private String rank;
        private Double areaFrom;
        private Double areaTo;
        private Double sellingPrice;
        private Integer numberLivingFloor;
        private Integer numberBasement;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setRank(String rank) {
            this.rank = rank;
            return this;
        }

        public Builder setAreaFrom(Double areaFrom) {
            this.areaFrom = areaFrom;
            return this;
        }

        public Builder setAreaTo(Double areaTo) {
            this.areaTo = areaTo;
            return this;
        }

        public Builder setSellingPrice(Double sellingPrice) {
            this.sellingPrice = sellingPrice;
            return this;
        }

        public Builder setNumberLivingFloor(Integer numberLivingFloor) {
            this.numberLivingFloor = numberLivingFloor;
            return this;
        }

        public Builder setNumberBasement(Integer numberBasement) {
            this.numberBasement = numberBasement;
            return this;
        }

        public BuildingSearchBuilder build() {
            return new BuildingSearchBuilder(this);
        }
    }
}
