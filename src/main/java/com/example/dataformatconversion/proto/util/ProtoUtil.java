package com.example.dataformatconversion.proto.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.DescriptorProtos;
import io.protostuff.compiler.ParserModule;
import io.protostuff.compiler.model.Field;
import io.protostuff.compiler.model.Message;
import io.protostuff.compiler.model.Proto;
import io.protostuff.compiler.parser.Importer;
import io.protostuff.compiler.parser.LocalFileReader;
import io.protostuff.compiler.parser.ProtoContext;
import org.apache.commons.collections.CollectionUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public class ProtoUtil {

    public static void main(String[] args) throws Exception{
        String schemaDir = "D:\\subbu\\workspaces\\intellij-workspace\\DataFormatUtilityProject\\src\\main\\resources\\proto\\";
        String protoFIlePath = schemaDir + "sample.proto";

       // String s = getProtoSchema(protoFIlePath);


       String fieldHeader = getPropertiesAsString(getMessage(schemaDir, protoFIlePath, "Book"));

        System.out.println(fieldHeader);


    }
    public static String getProtoSchema(String protoFIle) throws IOException {

        InputStream inputProto = new FileInputStream(protoFIle);

        DescriptorProtos.DescriptorProto descriptor =  DescriptorProtos.DescriptorProto.parseFrom(inputProto);

        if(CollectionUtils.isNotEmpty(descriptor.getFieldList())){

            for(DescriptorProtos.FieldDescriptorProto fieldDescriptor : descriptor.getFieldList()){
                System.out.println(fieldDescriptor.getName());
            }
        }

        return null;

    }

    public static String getPropertiesAsString(Message message){

        StringBuilder fieldsStr = new StringBuilder();
        if(null!=message){

           List<Field> fields = message.getFields();

           if(CollectionUtils.isNotEmpty(fields)){

               for(Field field : fields){
                 fieldsStr.append(field.getName()).append(",");
               }
           }
        }

        return fieldsStr.toString();
    }
    public static Proto getProtoSchema(String schemaDir , String protoFile){

        final Injector injector = Guice.createInjector(new ParserModule());
        final Importer importer = injector.getInstance(Importer.class);
        final ProtoContext protoContext = importer.importFile(
                new LocalFileReader(Path.of(schemaDir)),protoFile
        );
        final Proto proto = protoContext.getProto();

        final List<Message> messages = proto.getMessages();
        System.out.println(String.format("Messages: %s", messages));

        final Message searchRequestMessage = proto.getMessage("SearchRequest");
        System.out.println(String.format("SearchRequest message: %s", searchRequestMessage));

//        final List<Enum> searchRequestMessageEnums = searchRequestMessage.getEnums();
//        System.out.println(String.format("SearchRequest message enums: %s", searchRequestMessageEnums);

        return  proto;
    }

    public static Message getMessage(String schemaDir , String protoFile, String messageType){

        final Proto proto = getProtoSchema(schemaDir, protoFile);

//        final List<Message> messages = proto.getMessages();
//        System.out.println(String.format("Messages: %s", messages));

        final Message searchRequestMessage = proto.getMessage(messageType);
        System.out.println(String.format("SearchRequest message: %s", searchRequestMessage));

//        final List<Enum> searchRequestMessageEnums = searchRequestMessage.getEnums();
//        System.out.println(String.format("SearchRequest message enums: %s", searchRequestMessageEnums);

        return  searchRequestMessage;
    }
}
