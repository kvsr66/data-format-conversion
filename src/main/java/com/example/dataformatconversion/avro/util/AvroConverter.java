package com.example.dataformatconversion.avro.util;


import org.apache.avro.Schema;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SelectableChannel;

public class AvroConverter {

    public static Schema readAvroSchemaFromSchemaFile(String avroSchemafile) throws IOException {
        Schema schema = null;

        schema = new Schema.Parser().parse(new File(avroSchemafile));

        return schema;

    }

    public static Schema getSchema(String strSchema){

        if(null!=strSchema) {
            Schema avroSchema = new Schema.Parser().parse(strSchema);
            return  avroSchema;
        }

        return null;
    }
}
