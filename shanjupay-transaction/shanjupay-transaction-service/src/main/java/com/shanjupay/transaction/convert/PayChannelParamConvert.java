package com.shanjupay.transaction.convert;

import com.shanjupay.transaction.dto.PayChannelParamDTO;
import com.shanjupay.transaction.entity.PayChannelParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PayChannelParamConvert {

    PayChannelParamConvert INSTANCE= Mappers.getMapper(PayChannelParamConvert.class);

    PayChannelParamDTO entity2Dto(PayChannelParam entity);

    PayChannelParam dto2Entity(PayChannelParamDTO dto);

    List<PayChannelParamDTO> entityList2DtoList(List<PayChannelParam> PlatformChannel);

    List<PayChannelParam> listdto2listentity(List<PayChannelParamDTO> PlatformChannelDTO);
}
