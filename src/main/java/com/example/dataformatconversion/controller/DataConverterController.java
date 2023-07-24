package com.example.dataformatconversion.controller;

import com.example.dataformatconversion.avro.util.AvroConverter;
import com.example.dataformatconversion.avro.util.AvroToCsvConverterNew;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DataConverterController {

    @GetMapping
    public String greet() {
        return "Hello";
    }

    @PostMapping("/avro/metadata")
    public String generateMetaDataFromAvro(@RequestBody(required = true) String payload) {

        String schemeName = null;
        try {

            Schema avroSchema = new Schema.Parser().parse(payload);
            schemeName = avroSchema.getName();
            AvroToCsvConverterNew converter = new AvroToCsvConverterNew();
            converter.convertAvroSchemaToMetaDataCSV(null, payload);

        } catch (Exception ex) {
            ex.printStackTrace();

            log.error(ex.getLocalizedMessage());
        }


        return "Got Schema : " + schemeName;
    }

}
