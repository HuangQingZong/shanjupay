package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.vo.MerchantRegisterVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MerchantRegisterConvert {

    MerchantRegisterConvert INSTANCE = Mappers.getMapper(MerchantRegisterConvert.class);

    /** VO转DTO */
    MerchantDTO vo2Dto(MerchantRegisterVO vo);

    /** DTO转VO */
    MerchantRegisterVO dto2Vo(MerchantDTO dto);
}
