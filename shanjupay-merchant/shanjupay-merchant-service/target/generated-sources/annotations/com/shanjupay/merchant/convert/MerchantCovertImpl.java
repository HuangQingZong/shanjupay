package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.dto.MerchantDTO;
import com.shanjupay.merchant.entity.Merchant;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-12-05T11:17:22+0800",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_302 (Temurin)"
)
public class MerchantCovertImpl implements MerchantCovert {

    @Override
    public MerchantDTO entity2Dto(Merchant merchant) {
        if ( merchant == null ) {
            return null;
        }

        MerchantDTO merchantDTO = new MerchantDTO();

        merchantDTO.setId( merchant.getId() );
        merchantDTO.setMerchantName( merchant.getMerchantName() );
        merchantDTO.setMerchantNo( merchant.getMerchantNo() );
        merchantDTO.setMerchantAddress( merchant.getMerchantAddress() );
        merchantDTO.setMerchantType( merchant.getMerchantType() );
        merchantDTO.setBusinessLicensesImg( merchant.getBusinessLicensesImg() );
        merchantDTO.setIdCardFrontImg( merchant.getIdCardFrontImg() );
        merchantDTO.setIdCardAfterImg( merchant.getIdCardAfterImg() );
        merchantDTO.setUsername( merchant.getUsername() );
        merchantDTO.setMobile( merchant.getMobile() );
        merchantDTO.setContactsAddress( merchant.getContactsAddress() );
        merchantDTO.setAuditStatus( merchant.getAuditStatus() );
        merchantDTO.setTenantId( merchant.getTenantId() );

        return merchantDTO;
    }

    @Override
    public Merchant dto2Entity(MerchantDTO merchantDto) {
        if ( merchantDto == null ) {
            return null;
        }

        Merchant merchant = new Merchant();

        merchant.setId( merchantDto.getId() );
        merchant.setMerchantName( merchantDto.getMerchantName() );
        merchant.setMerchantNo( merchantDto.getMerchantNo() );
        merchant.setMerchantAddress( merchantDto.getMerchantAddress() );
        merchant.setMerchantType( merchantDto.getMerchantType() );
        merchant.setBusinessLicensesImg( merchantDto.getBusinessLicensesImg() );
        merchant.setIdCardFrontImg( merchantDto.getIdCardFrontImg() );
        merchant.setIdCardAfterImg( merchantDto.getIdCardAfterImg() );
        merchant.setUsername( merchantDto.getUsername() );
        merchant.setMobile( merchantDto.getMobile() );
        merchant.setContactsAddress( merchantDto.getContactsAddress() );
        merchant.setAuditStatus( merchantDto.getAuditStatus() );
        merchant.setTenantId( merchantDto.getTenantId() );

        return merchant;
    }

    @Override
    public List<MerchantDTO> entityList2DtoList(List<Merchant> list) {
        if ( list == null ) {
            return null;
        }

        List<MerchantDTO> list1 = new ArrayList<MerchantDTO>( list.size() );
        for ( Merchant merchant : list ) {
            list1.add( entity2Dto( merchant ) );
        }

        return list1;
    }
}
