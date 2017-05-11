package com.heapdragon.lots;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.joda.time.DateTime;
import java.util.HashMap;
import java.util.Map;

import static com.heapdragon.lots.DataBaseConstants.LOG_FIELD_UPDATED;
import static com.heapdragon.lots.DataBaseConstants.LOG_NODE_PREFIX;
import static com.heapdragon.lots.DataBaseConstants.LOG_NUMBER;
import static com.heapdragon.lots.DataBaseConstants.LOG_STATUS;
import static com.heapdragon.lots.DataBaseConstants.LOG_TIME_STAMP;

class DatabaseBitch {

     void createLog(int status, String siteKey,int lotNumber,String statusLevel) {
         DatabaseReference logRef = FirebaseDatabase.getInstance().getReference().child(LOG_NODE_PREFIX+siteKey);
         Map<String,Object> map = new HashMap<>();
         map.put(LOG_NUMBER,lotNumber);
         map.put(LOG_FIELD_UPDATED,statusLevel);
         map.put(LOG_STATUS,status);
         map.put(LOG_TIME_STAMP,new DateTime().getMillis());
         logRef.push().setValue(map);
    }




}
