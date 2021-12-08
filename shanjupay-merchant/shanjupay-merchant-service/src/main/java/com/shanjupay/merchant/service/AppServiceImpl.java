package com.shanjupay.merchant.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.merchant.api.AppService;
import com.shanjupay.merchant.convert.AppCovert;
import com.shanjupay.merchant.dto.AppDTO;
import com.shanjupay.merchant.entity.App;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.enums.AuditStatusEnum;
import com.shanjupay.merchant.mapper.AppMapper;
import com.shanjupay.merchant.mapper.MerchantMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@org.apache.dubbo.config.annotation.Service
public class AppServiceImpl implements AppService {

    @Autowired
    AppMapper appMapper;

    @Autowired
    MerchantMapper merchantMapper;

    @Override
    public AppDTO createApp(Long merchantId, AppDTO appDTO) throws BusinessException {
        if (merchantId == null || appDTO == null || StringUtils.isBlank(appDTO.getAppName())) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        //校验商户是否存在
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        //校验商户是否通过资质审核
        String auditStatus = merchant.getAuditStatus();
        if (!AuditStatusEnum.AuditAgree.getCode().equals(auditStatus)) {
            throw new BusinessException(CommonErrorCode.E_200003);
        }
        //校验应用名称是否重复
        Integer count = appMapper.selectCount(new LambdaQueryWrapper<App>().eq(App::getAppName, appDTO.getAppName()));
        if (count > 0) {
            throw new BusinessException(CommonErrorCode.E_200004);
        }
        //dto转entity
        App app = BeanUtil.copyProperties(appDTO, App.class);
        app.setAppId(UUID.randomUUID().toString()); //应用id
        app.setMerchantId(merchantId); //商户id
        //向app表插入数据
        appMapper.insert(app);
        return BeanUtil.copyProperties(app, AppDTO.class);
    }

    @Override
    public List<AppDTO> findAppListByMerchantId(Long merchantId) throws BusinessException {
        List<App> appList = appMapper.selectList(new LambdaQueryWrapper<App>().eq(App::getMerchantId, merchantId));
        return AppCovert.INSTANCE.listentity2dto(appList);
    }

    @Override
    public AppDTO findAppByAppId(String appId) throws BusinessException {
        App app = appMapper.selectOne(new LambdaQueryWrapper<App>().eq(App::getAppId, appId));
        return AppCovert.INSTANCE.entity2dto(app);
    }

}
