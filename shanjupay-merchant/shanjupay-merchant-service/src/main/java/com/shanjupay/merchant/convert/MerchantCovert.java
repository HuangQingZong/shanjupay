package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface MerchantCovert {

    MerchantCovert INSTANCE = Mappers.getMapper(MerchantCovert.class);

    /** Entity转DTO */
    MerchantDTO entity2Dto(Merchant merchant);

    /** DTO转Entity */
    Merchant dto2Entity(MerchantDTO merchantDto);

    //list之间的转换
    List<MerchantDTO> entityList2DtoList(List<Merchant> list);

    public static void main(String[] args) {

        //dto转entity
        MerchantDTO merchantDto = new MerchantDTO();
        merchantDto.setUsername("张三");
        merchantDto.setPassword("123");
        Merchant merchant = MerchantCovert.INSTANCE.dto2Entity(merchantDto);
        System.out.println(merchant);

        //entity转dto
        merchant.setMobile("11122223333");
        MerchantDTO merchantDTO2 = MerchantCovert.INSTANCE.entity2Dto(merchant);
        System.out.println(merchantDTO2);

        //测试list之间的转换
        List<Merchant> merchantList = new ArrayList<>();
        merchantList.add(merchant);
        List<MerchantDTO> merchantDtoList = MerchantCovert.INSTANCE.entityList2DtoList(merchantList);
        System.out.println(merchantDtoList);
    }
}
