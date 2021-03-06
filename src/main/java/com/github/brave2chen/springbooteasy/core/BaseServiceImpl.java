package com.github.brave2chen.springbooteasy.core;

import com.diboot.core.binding.Binder;
import com.diboot.core.config.BaseConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author brave2chen
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends com.diboot.core.service.impl.BaseServiceImpl<M, T> {

    @Override
    public boolean createOrUpdateEntity(T entity) {
        return super.createOrUpdateEntity(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean fastSaveBatch(List<T> list, int batchSize) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }

        if (list.size() <= batchSize) {
            return retBool(baseMapper.insertBatchSomeColumn(list));
        }

        for (int fromIdx = 0, endIdx = batchSize; ; fromIdx += batchSize, endIdx += batchSize) {
            if (endIdx > list.size()) {
                endIdx = list.size();
            }
            baseMapper.insertBatchSomeColumn(list.subList(fromIdx, endIdx));
            if (endIdx == list.size()) {
                return true;
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean fastSaveBatch(List<T> list) {
        int batchSize = BaseConfig.getBatchSize();
        return fastSaveBatch(list, batchSize);
    }

    public boolean updateAllColById(T t) {
        return retBool(baseMapper.alwaysUpdateSomeColumnById(t));
    }

    /**
     * 获取View Object对象
     *
     * @return entity
     */
    public <VO extends T, T> VO convertToViewObject(T entity, Class<VO> voClass) {
        // 自动转换为VO并绑定关联对象
        return Binder.convertAndBindRelations(entity, voClass);
    }

    /**
     * 获取View Object对象
     *
     * @return entity
     */
    public <VO extends T, T> List<VO> convertToViewObjectList(List<T> entityList, Class<VO> voClass) {
        // 自动转换为VO并绑定关联对象
        List<VO> voList = Binder.convertAndBindRelations(entityList, voClass);
        return voList;
    }

}
