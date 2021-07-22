package com.example.put.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)     // 모든 곳에 네이밍 룰 적용.
public class PostRequestDto {
    private String name;
    private int age;
    private List<CarDto> carList;

    /* JSON Design
    {
        "name" : "steve",
        "age" : 20,
        "car_list" : [
            {
                "name" : "BMW",
                "car_number" : "11가 1234"
            },
            {
                "name" : "A4",
                "car_number" : "22가 3456"
            }
        ]
    }
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<CarDto> getCarList() {
        return carList;
    }

    public void setCarList(List<CarDto> carList) {
        this.carList = carList;
    }

    @Override
    public String toString() {
        return "PostRequestDto{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", carList=" + carList +
                '}';
    }
}

