package com.wjwcloud.tsl.controller;

import com.google.gson.JsonParser;
import com.wjwcloud.tsl.adaptor.JsonConverter;
import com.wjwcloud.tsl.data.kv.KvEntry;
import com.wjwcloud.tsl.data.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Date: 19-4-16
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/v1")
public class TslApiController {
    @RequestMapping(value = "/tsl",method = RequestMethod.POST)
    public DeferredResult<ResponseEntity> postJson(@RequestBody String json){
        DeferredResult<ResponseEntity> responseWriter = new DeferredResult<ResponseEntity>();
        Map<Long, List<KvEntry>> telemetryMaps = JsonConverter.convertToTelemetry(new JsonParser().parse(json)).getData();
        List<ResponseModel> responseModelList = new ArrayList<>();

        for (Map.Entry<Long,List<KvEntry>> entry : telemetryMaps.entrySet()) {
            for (KvEntry kvEntry: entry.getValue()) {
                ResponseModel responseModel = new ResponseModel();
                responseModel.setKey(kvEntry.getKey());
                switch (kvEntry.getDataType()) {
                    case STRING:
                        responseModel.setValue("String value="+kvEntry.getValueAsString());
                        responseModelList.add(responseModel);
                        break;
                    case DOUBLE:
                        responseModel.setValue("Double value="+kvEntry.getValueAsString());
                        responseModelList.add(responseModel);
                        break;
                    case LONG:
                        responseModel.setValue("Long value="+kvEntry.getValueAsString());
                        responseModelList.add(responseModel);
                        break;
                    case BOOLEAN:
                        responseModel.setValue("Boolean value="+kvEntry.getValueAsString());
                        responseModelList.add(responseModel);
                        break;
                }

            }
        }
        responseWriter.setResult(new ResponseEntity(responseModelList,HttpStatus.ACCEPTED));
        return responseWriter;
    }
}
