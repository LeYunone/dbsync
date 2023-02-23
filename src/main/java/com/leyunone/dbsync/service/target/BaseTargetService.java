package com.leyunone.dbsync.service.target;

import com.leyunone.dbsync.dao.target.BaseTargetDao;
import com.leyunone.dbsync.model.BaseModel;
import com.leyunone.dbsync.utils.ApplicationContextProvider;
import com.leyunone.dbsync.utils.BeanNameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author leyunone
 * @date 2021/10/18
 */
public class BaseTargetService implements TargetService {

    private BaseTargetDao<?> baseTargetDao;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * BO对象
     */
    private Class<?> DO;

    public BaseTargetService(Class<?> clazz) {
        this.baseTargetDao = (BaseTargetDao<?>) ApplicationContextProvider.getBean(BeanNameUtil.getTargetDaoName(clazz));
        this.DO = clazz;
    }

    /**
     * 添加或删除数据
     * @param list
     * @return
     */
    @Override
    public int addOrSaveData(List<? extends BaseModel> list) {
        return baseTargetDao.batchInsertOrUpdate(list);
    }

    /**
     * 查询7天内的数据
     * @param startTime
     * @return
     */
    @Override
    public List<? extends BaseModel> selectListInDays(String startTime) {
        List dos = baseTargetDao.selectListInDays(startTime);
        baseTargetDao.fillMajorKey(dos);
        return dos;
    }

    /**
     * 移除数据
     * @param list
     * @return
     */
    @Override
    public int removeData(List<? extends BaseModel> list) {
        return baseTargetDao.batchDelete(list);
    }

    @Override
    public int removeAllData() {
        return baseTargetDao.deleteAll();
    }

}
