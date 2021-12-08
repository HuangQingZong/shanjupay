package com.shanjupay.transaction.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.convert.PayChannelParamConvert;
import com.shanjupay.transaction.dto.PayChannelDTO;
import com.shanjupay.transaction.dto.PayChannelParamDTO;
import com.shanjupay.transaction.dto.PlatformChannelDTO;
import com.shanjupay.transaction.convert.PlatformChannelConvert;
import com.shanjupay.transaction.entity.AppPlatformChannel;
import com.shanjupay.transaction.entity.PayChannelParam;
import com.shanjupay.transaction.entity.PlatformChannel;
import com.shanjupay.transaction.mapper.AppPlatformChannelMapper;
import com.shanjupay.transaction.mapper.PayChannelParamMapper;
import com.shanjupay.transaction.mapper.PlatformChannelMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PayChannelServiceImpl implements PayChannelService {

    @Autowired
    PlatformChannelMapper platformChannelMapper;

    @Autowired
    AppPlatformChannelMapper appPlatformChannelMapper;

    @Autowired
    PayChannelParamMapper payChannelParamMapper;

    @Override
    public List<PlatformChannelDTO> queryPlatformChannel() throws BusinessException {
        //查询platform_channel表的全部记录
        List<PlatformChannel> platformChannelList = platformChannelMapper.selectList(null);
        //转成包含dto的list
        return PlatformChannelConvert.INSTANCE.entityList2DtoList(platformChannelList);
    }

    @Override
    @Transactional
    public void bindPlatformChannelForApp(String appId, String platformChannelCode) throws BusinessException {
        //根据应用id和服务类型code查询
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(
                new LambdaQueryWrapper<AppPlatformChannel>()
                        .eq(AppPlatformChannel::getAppId, appId)
                        .eq(AppPlatformChannel::getPlatformChannel, platformChannelCode));
        //如果已经绑定则不再插入，否则插入记录
        if (appPlatformChannel == null) {
            //向app_platform_channel插入
            AppPlatformChannel platformChannel = new AppPlatformChannel();
            platformChannel.setAppId(appId); //应用id
            platformChannel.setPlatformChannel(platformChannelCode); //服务类型code
            appPlatformChannelMapper.insert(platformChannel);
        }
    }

    @Override
    public boolean queryAppBindPlatformChannel(String appId, String platformChannel) throws BusinessException {
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(
                new LambdaQueryWrapper<AppPlatformChannel>()
                        .eq(AppPlatformChannel::getAppId, appId)
                        .eq(AppPlatformChannel::getPlatformChannel, platformChannel));
        return appPlatformChannel != null;
    }

    @Override
    public List<PayChannelDTO> findPayChannelByPlatformChannel(String platformChannelCode) throws BusinessException {
        return platformChannelMapper.findPayChannelByPlatformChannel(platformChannelCode);
    }

    @Override
    public void savePayChannelParam(PayChannelParamDTO payChannelParamDTO) throws BusinessException {
        if (payChannelParamDTO == null || payChannelParamDTO.getChannelName() == null || payChannelParamDTO.getParam() == null) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        //查询应用与服务类型的绑定id
        Long appPlatformChannelId = findAppPlatformChannelId(payChannelParamDTO.getAppId(), payChannelParamDTO.getPlatformChannelCode());
        if (appPlatformChannelId == null) {
            throw new BusinessException(CommonErrorCode.E_300010);
        }
        //查询该支付渠道是否已经配置过参数
        PayChannelParam payChannelParamFromQuery = payChannelParamMapper.selectOne(new LambdaQueryWrapper<PayChannelParam>()
                .eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId)
                .eq(PayChannelParam::getPayChannel, payChannelParamDTO.getPayChannel()));
        if (payChannelParamFromQuery != null) {
            //存在配置则更新
            payChannelParamFromQuery
                    .setChannelName(payChannelParamDTO.getChannelName()) //配置名称
                    .setParam(payChannelParamDTO.getParam()); //json格式的参数
            payChannelParamMapper.updateById(payChannelParamFromQuery);
        } else {
            //否则添加配置
            PayChannelParam payChannelParamToInsert = PayChannelParamConvert.INSTANCE.dto2Entity(payChannelParamDTO);
            payChannelParamToInsert
                    .setAppPlatformChannelId(appPlatformChannelId) //应用与服务类型绑定关系id
                    .setId(null); //清空主键，主键由Mybatis-Plus自动生成
            payChannelParamMapper.insert(payChannelParamToInsert);
        }
    }

    /**
     * 根据应用id、服务类型code查询应用与服务类型的绑定id
     *
     * @param appId
     * @param platformChannelCode
     * @return 应用与服务类型的绑定id
     */
    private Long findAppPlatformChannelId(String appId, String platformChannelCode) {
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new LambdaQueryWrapper<AppPlatformChannel>()
                .eq(AppPlatformChannel::getAppId, appId)
                .eq(AppPlatformChannel::getPlatformChannel, platformChannelCode));

        return appPlatformChannel == null ? null : appPlatformChannel.getId();
    }

    @Override
    public List<PayChannelParamDTO> findPayChannelParamList(String appId, String platformChannel) {
        //查询应用与服务类型的绑定id
        Long appPlatformChannelId = findAppPlatformChannelId(appId, platformChannel);
        if (appPlatformChannelId == null) {
            return null;
        }
        //查询支付渠道参数
        List<PayChannelParam> payChannelParamList = payChannelParamMapper.selectList(new LambdaQueryWrapper<PayChannelParam>()
                .eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId));
        List<PayChannelParamDTO> payChannelParamDTOList = PayChannelParamConvert.INSTANCE.entityList2DtoList(payChannelParamList);
        return payChannelParamDTOList;
    }

    @Override
    public PayChannelParamDTO findPayChannelParam(String appId, String platformChannel, String payChannel) {
        List<PayChannelParamDTO> payChannelParamDTOList = findPayChannelParamList(appId, platformChannel);
        for (PayChannelParamDTO payChannelParamDTO : payChannelParamDTOList) {
            if (payChannelParamDTO.getPayChannel().equals(payChannel)){
                return payChannelParamDTO;
            }
        }
        return null;

//        //查询应用与服务类型的绑定id
//        Long appPlatformChannelId = findAppPlatformChannelId(appId, platformChannel);
//        if (appPlatformChannelId == null) {
//            return null;
//        }
//        //查询支付渠道参数
//        PayChannelParam payChannelParam = payChannelParamMapper.selectOne(new LambdaQueryWrapper<PayChannelParam>()
//                .eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId)
//                .eq(PayChannelParam::getPayChannel, payChannel)); //支付渠道
//        PayChannelParamDTO payChannelParamDTO = PayChannelParamConvert.INSTANCE.entity2Dto(payChannelParam);
//        return payChannelParamDTO;
    }

}
