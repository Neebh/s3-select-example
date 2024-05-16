
package com.example.tutorials;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CompressionType;
import com.amazonaws.services.s3.model.ExpressionType;
import com.amazonaws.services.s3.model.InputSerialization;
import com.amazonaws.services.s3.model.JSONInput;
import com.amazonaws.services.s3.model.JSONOutput;
import com.amazonaws.services.s3.model.OutputSerialization;
import com.amazonaws.services.s3.model.SelectObjectContentRequest;
import com.amazonaws.services.s3.model.SelectObjectContentResult;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.stereotype.Service;
import static com.amazonaws.util.IOUtils.copy;

@Service
public class S3Service {

    private static final String BUCKET_NAME = "nb-logs-pb";
    private static final String CSV_OBJECT_KEY = "sample.json";

    final AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider("neeraj_dev")).build(); 

    public String findLogsInS3File(String userId) throws Exception {    
        String QUERY = String.format("select s from s3object[*][*] s where s.userid='%1$s'", userId);
        SelectObjectContentRequest request = generateBaseJSONRequest(BUCKET_NAME, CSV_OBJECT_KEY, QUERY);
        SelectObjectContentResult result =  s3Client.selectObjectContent(request);
        InputStream resultInputStream =  result.getPayload().getRecordsInputStream();
        OutputStream outputStream = new ByteArrayOutputStream();

        copy(resultInputStream, outputStream);
        outputStream.flush();
        outputStream.close();
        return outputStream.toString();
    }

    private static SelectObjectContentRequest generateBaseJSONRequest(String bucket, String key, String query) {
        SelectObjectContentRequest request = new SelectObjectContentRequest();
        request.setBucketName(bucket);
        request.setKey(key);
        request.setExpression(query);
        request.setExpressionType(ExpressionType.SQL);

        InputSerialization inputSerialization = new InputSerialization();
        inputSerialization.setJson(new JSONInput().withType("DOCUMENT"));
        inputSerialization.setCompressionType(CompressionType.NONE);
        request.setInputSerialization(inputSerialization);

        OutputSerialization outputSerialization = new OutputSerialization();
        outputSerialization.setJson(new JSONOutput());
        request.setOutputSerialization(outputSerialization);
        return request;
    }
}