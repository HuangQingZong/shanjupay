package com.shanjupay.merchant.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.dto.MerchantDTO;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.enums.AuditStatusEnum;
import com.shanjupay.merchant.mapper.MerchantMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    MerchantMapper merchantMapper;

    @Override
    public MerchantDTO queryMerchantById(Long id) {
        Merchant merchant = merchantMapper.selectById(id);
        //entity转dto
        MerchantDTO merchantDTO = BeanUtil.copyProperties(merchant, MerchantDTO.class);
        return merchantDTO;
    }

    @Override
    @Transactional
    public MerchantDTO merchantRegister(MerchantDTO merchantDTO) {
        //校验手机号的唯一性
        LambdaQueryWrapper<Merchant> merchantQueryWrapper = new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getMobile, merchantDTO.getMobile());
        Integer count = merchantMapper.selectCount(merchantQueryWrapper);
        if (count > 0) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        //dto转entity
        Merchant merchant = BeanUtil.copyProperties(merchantDTO, Merchant.class);
        //保存商户
        merchantMapper.insert(merchant);
//        int i = 1 / 0; //制造异常，测试@Transactional的作用
        //entity转dto
        MerchantDTO merchantDTOResult = BeanUtil.copyProperties(merchant, MerchantDTO.class);
        return merchantDTOResult;
    }

    @Override
    @Transactional
    public void merchantApplyQualification(Long merchantId, MerchantDTO merchantDTO) throws BusinessException {
        if (merchantId == null || merchantDTO == null) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        //校验商户是否存在
        Merchant merchantOfQuery = merchantMapper.selectById(merchantId);
        if (merchantOfQuery == null) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        Merchant merchantToUpdate = BeanUtil.copyProperties(merchantDTO, Merchant.class);
        merchantToUpdate.setId(merchantOfQuery.getId());
        merchantToUpdate.setMobile(merchantOfQuery.getMobile());//资质申请时不允许修改手机号，还使用数据库中原来的手机号
        merchantToUpdate.setAuditStatus(AuditStatusEnum.notAudit.getCode()); //状态改为已申请待审核
        merchantToUpdate.setTenantId(merchantOfQuery.getTenantId());
        //更新商户信息
        merchantMapper.updateById(merchantToUpdate);

    }

}
