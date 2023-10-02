package com.example.dataformatconversion.controller;

import lombok.Data;

import java.util.List;

@Data
public class ExpressionRequest {

    String entityName;

    List requestObjectList;
}
