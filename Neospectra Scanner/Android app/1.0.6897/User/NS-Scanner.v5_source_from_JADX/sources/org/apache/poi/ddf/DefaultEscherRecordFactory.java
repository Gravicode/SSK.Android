package org.apache.poi.ddf;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DefaultEscherRecordFactory implements EscherRecordFactory {
    private static Class<?>[] escherRecordClasses = {EscherBSERecord.class, EscherOptRecord.class, EscherClientAnchorRecord.class, EscherDgRecord.class, EscherSpgrRecord.class, EscherSpRecord.class, EscherClientDataRecord.class, EscherDggRecord.class, EscherSplitMenuColorsRecord.class, EscherChildAnchorRecord.class, EscherTextboxRecord.class};
    private static Map<Short, Constructor<? extends EscherRecord>> recordsMap = recordsToMap(escherRecordClasses);

    public EscherRecord createRecord(byte[] data, int offset) {
        EscherRecordHeader header = EscherRecordHeader.readHeader(data, offset);
        if ((header.getOptions() & 15) == 15 && header.getRecordId() != -4083) {
            EscherContainerRecord r = new EscherContainerRecord();
            r.setRecordId(header.getRecordId());
            r.setOptions(header.getOptions());
            return r;
        } else if (header.getRecordId() < -4072 || header.getRecordId() > -3817) {
            Constructor<? extends EscherRecord> recordConstructor = (Constructor) recordsMap.get(Short.valueOf(header.getRecordId()));
            if (recordConstructor == null) {
                return new UnknownEscherRecord();
            }
            try {
                EscherRecord escherRecord = (EscherRecord) recordConstructor.newInstance(new Object[0]);
                escherRecord.setRecordId(header.getRecordId());
                escherRecord.setOptions(header.getOptions());
                return escherRecord;
            } catch (Exception e) {
                return new UnknownEscherRecord();
            }
        } else {
            EscherBlipRecord r2 = (header.getRecordId() == -4065 || header.getRecordId() == -4067 || header.getRecordId() == -4066) ? new EscherBitmapBlip() : (header.getRecordId() == -4070 || header.getRecordId() == -4069 || header.getRecordId() == -4068) ? new EscherMetafileBlip() : new EscherBlipRecord();
            r2.setRecordId(header.getRecordId());
            r2.setOptions(header.getOptions());
            return r2;
        }
    }

    private static Map<Short, Constructor<? extends EscherRecord>> recordsToMap(Class<?>[] recClasses) {
        Map<Short, Constructor<? extends EscherRecord>> result = new HashMap<>();
        int i = 0;
        Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
        while (i < recClasses.length) {
            Class<? extends EscherRecord> recCls = recClasses[i];
            try {
                try {
                    result.put(Short.valueOf(recCls.getField("RECORD_ID").getShort(null)), recCls.getConstructor(EMPTY_CLASS_ARRAY));
                    i++;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            } catch (IllegalArgumentException e2) {
                throw new RuntimeException(e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimeException(e3);
            } catch (NoSuchFieldException e4) {
                throw new RuntimeException(e4);
            }
        }
        return result;
    }
}
