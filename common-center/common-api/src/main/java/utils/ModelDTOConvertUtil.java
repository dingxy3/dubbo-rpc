package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyunfeng on 2017/5/5.
 */
public class ModelDTOConvertUtil {
    private static Logger logger = LoggerFactory.getLogger(ModelDTOConvertUtil.class);

    private static Logger getLogger(){
        if(logger == null){
            logger = LoggerFactory.getLogger(ModelDTOConvertUtil.class);
        }
        return logger;
    }

    public static <K,T> List<T> convertModelList2DTOList(List<K> sourceList, Class<T> targetClazz, String notCopy, boolean isCopyNull){
        if(sourceList == null || sourceList.size() == 0 || targetClazz == null){
            return null;
        }
        List<T> dtoList = new ArrayList<T>();
        for(K pObject : sourceList){
            dtoList.add(createCopyObject(pObject,targetClazz,notCopy,isCopyNull));
        }
        return dtoList;
    }

    public static <T> T createCopyObject(Object source,Class<T> targetClazz,String notCopy,boolean isCopyNull){
        try {
            T target = targetClazz.newInstance();
            ObjectCopyUtil.copyObjValue(source,target,notCopy,isCopyNull);
            return target;
        } catch (InstantiationException e) {
            getLogger().error("createCopyObject error",e);
        } catch (IllegalAccessException e) {
            getLogger().error("createCopyObject error",e);
        }
        return null;
    }
}
