package com.shanjupay.merchant.api;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.merchant.api.dto.MerchantDTO;


public interface MerchantService {

    /**
     * 根据id查询商户
     * @param id
     * @return
     */
    MerchantDTO queryMerchantById(Long id);

    /**
     * 商户注册
     * @param merchantDto
     * @return
     */
    MerchantDTO merchantRegister(MerchantDTO merchantDto);

    /**
     * 资质申请接口
     * @param merchantId 商户id
     * @param merchantDTO 资质申请的信息
     * @throws BusinessException
     */
    void merchantApplyQualification(Long merchantId, MerchantDTO merchantDTO) throws BusinessException;

}
