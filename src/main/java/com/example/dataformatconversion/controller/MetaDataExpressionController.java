package com.example.dataformatconversion.controller;

import com.example.dataformatconversion.service.MetadataExpressionService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/expression")
public class MetaDataExpressionController {

@Autowired
MetadataExpressionService expressionService;

    @PostMapping("/process")
    public ResponseEntity<?> processExpression(@RequestBody ExpressionRequest request){
     Map<String, String> outExpressionMap = null;

        try {
            outExpressionMap = expressionService.processExpression(request);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getMessage(e));
        }

        return new ResponseEntity<>(outExpressionMap, HttpStatus.OK);
    }

}
