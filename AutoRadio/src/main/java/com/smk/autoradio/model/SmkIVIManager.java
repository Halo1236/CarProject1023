package com.smk.autoradio.model;

import com.semisky.autoservice.manager.AutoConstants;
import com.semisky.autoservice.manager.RadioManager;
import com.smk.autoradio.constants.RadioConst;
import com.smk.autoradio.utils.Logutil;

/**
 * 中间件管理
 */
public class SmkIVIManager {
    private static final String TAG = Logutil.makeTagLog(SmkIVIManager.class);

    private static SmkIVIManager _INSTANCE;


    private SmkIVIManager() {

    }

    public static SmkIVIManager getInstance() {
        if (null != _INSTANCE) {
            _INSTANCE = new SmkIVIManager();
        }
        return _INSTANCE;
    }

    public int reviseChannel(int nationalRegionType,int channelType,int channel){
        int minChannel = getChannelRangeForNationalRegionAndChannelType(false,nationalRegionType,channelType);
        int maxChannel = getChannelRangeForNationalRegionAndChannelType(false,nationalRegionType,channelType);
        if(channel >= minChannel && channel <= maxChannel){
            return channel;
        }
        return minChannel;
    }

    // 获取远近程
    public int getDxLocType(int channelType) {
        int dxLocType = RadioConst.CHANNEL_DX_LOC_TYPE_INVALID;
        switch (channelType) {
            case RadioConst.CHANNEL_TYPE_FM:
            case RadioConst.CHANNEL_TYPE_FM1:
            case RadioConst.CHANNEL_TYPE_FM2:
                if (RadioManager.getInstance().getShortrangeOrRemote() == AutoConstants.ShortAndRemote.REMOTE) {
                    dxLocType = RadioConst.CHANNEL_DX_LOC_TYPE_DX;
                } else if (RadioManager.getInstance().getShortrangeOrRemote() == AutoConstants.ShortAndRemote.SHORT) {
                    dxLocType = RadioConst.CHANNEL_DX_LOC_TYPE_LOC;
                }
                break;
            case RadioConst.CHANNEL_TYPE_AM:
                dxLocType = RadioConst.SOUNDTRACK_TYPE_INVALID;
                break;
        }
        return dxLocType;

    }

    // 获取声道类型
    public int getSoundtrackType(int channelType) {
        int soundtrackType = RadioConst.SOUNDTRACK_TYPE_INVALID;
        switch (channelType) {
            case RadioConst.CHANNEL_TYPE_FM:
            case RadioConst.CHANNEL_TYPE_FM1:
            case RadioConst.CHANNEL_TYPE_FM2:
                if (RadioManager.getInstance().FmGetStereo()) {
                    soundtrackType = RadioConst.SOUNDTRACK_TYPE_STEREO;
                } else {
                    soundtrackType = RadioConst.SOUNDTRACK_TYPE_MONO;
                }
                break;
            case RadioConst.CHANNEL_TYPE_AM:
                soundtrackType = RadioConst.SOUNDTRACK_TYPE_INVALID;
                break;
        }
        return soundtrackType;
    }

    // 停止搜台(包括停止全搜、停止强台搜索)
    public void reqStopSearch() {
        RadioManager.getInstance().StopSearch();
    }

    // 下个强台
    public void reqNextStrongChannel(int channel) {
        RadioManager.getInstance().SeekUp(channel);
    }

    // 上个强台
    public void reqPrevStrongChannel(int channel) {
        RadioManager.getInstance().SeekDown(channel);
    }

    // 播放指定频道
    public void reqPlayChannel(int channelType, int channel) {
        switch (channelType) {
            case RadioConst.CHANNEL_TYPE_FM:
            case RadioConst.CHANNEL_TYPE_FM1:
            case RadioConst.CHANNEL_TYPE_FM2:
                RadioManager.getInstance().FMSearch(channel);
                break;
            case RadioConst.CHANNEL_TYPE_AM:
                RadioManager.getInstance().AMSearch(channel);
                break;
        }
    }

    // 请求全搜
    public void reqFullSearchChannel(int channelType) {
        switch (channelType) {
            case RadioConst.CHANNEL_TYPE_FM:
            case RadioConst.CHANNEL_TYPE_FM1:
            case RadioConst.CHANNEL_TYPE_FM2:
                RadioManager.getInstance().FMSearch(0);
                break;
            case RadioConst.CHANNEL_TYPE_AM:
                RadioManager.getInstance().AMSearch(0);
                break;
        }
    }


    // 获取收音机区域
    public int getNationalRegion() {
        int nationalRegion = RadioManager.getInstance().getNationalRegion();// 中间件收音机区域标识
        // 检查收音机区域是否合法，如果不合格赋值默认收音机区域
        nationalRegion = (nationalRegion >= AutoConstants.NationalRegion.CHINA &&
                nationalRegion <= AutoConstants.NationalRegion.RUSSIA
                ? nationalRegion : AutoConstants.NationalRegion.CHINA);
        return nationalRegion;
    }

    // 根据收音机区域获取默认频道类型
    public int getDefaultChannelTypeForNationalRegion(int nationalRegion) {
        int channelType = AutoConstants.ChannelParameter.COUNTRY_ASIA;
        switch (nationalRegion) {
            case AutoConstants.ChannelParameter.COUNTRY_ASIA:// 亚洲
            case AutoConstants.ChannelParameter.COUNTRY_EU:// 欧洲
            case AutoConstants.ChannelParameter.COUNTRY_AMERICA:// 美洲
                channelType = RadioConst.CHANNEL_TYPE_FM;
                break;
            case AutoConstants.ChannelParameter.COUNTRY_EE:// 俄罗斯
                channelType = RadioConst.CHANNEL_TYPE_FM1;
                break;
        }
        return channelType;
    }

    // 获取频道范围
    public int getChannelRangeForNationalRegionAndChannelType(boolean isGetMaxChannel, int nationalRegion, int channelType) {
        int channelRange = -1;

        if (AutoConstants.ChannelParameter.COUNTRY_ASIA == nationalRegion) {// 亚洲
            switch (channelType) {
                case RadioConst.CHANNEL_TYPE_FM:
                    if (isGetMaxChannel) {
                        channelRange = AutoConstants.ChannelParameter.FM_ASIA_MAX_CHANNEL;
                    } else {
                        channelRange = AutoConstants.ChannelParameter.FM_ASIA_MIN_CHANNEL;
                    }
                    break;
                case RadioConst.CHANNEL_TYPE_AM:
                    if (isGetMaxChannel) {
                        channelRange = AutoConstants.ChannelParameter.AM_ASIA_MAX_CHANNEL;
                    } else {
                        channelRange = AutoConstants.ChannelParameter.AM_ASIA_MIN_CHANNEL;
                    }
                    break;
            }
        } else if (AutoConstants.ChannelParameter.COUNTRY_EU == nationalRegion) {// 欧洲
            switch (channelType) {
                case RadioConst.CHANNEL_TYPE_FM:
                    if (isGetMaxChannel) {
                        channelRange = AutoConstants.ChannelParameter.FM_EU_MAX_CHANNEL;
                    } else {
                        channelRange = AutoConstants.ChannelParameter.FM_EU_MIN_CHANNEL;
                    }
                    break;
                case RadioConst.CHANNEL_TYPE_AM:
                    if (isGetMaxChannel) {
                        channelRange = AutoConstants.ChannelParameter.AM_EU_MAX_CHANNEL;
                    } else {
                        channelRange = AutoConstants.ChannelParameter.AM_EU_MIN_CHANNEL;
                    }
                    break;
            }

        } else if (AutoConstants.ChannelParameter.COUNTRY_EE == nationalRegion) {// 俄罗斯
            switch (channelType) {
                case RadioConst.CHANNEL_TYPE_FM1:
                    if (isGetMaxChannel) {
                        channelRange = AutoConstants.ChannelParameter.FM1_EE_MAX_CHANNEL;
                    } else {
                        channelRange = AutoConstants.ChannelParameter.FM1_EE_MIN_CHANNEL;
                    }
                    break;
                case RadioConst.CHANNEL_TYPE_FM2:
                    if (isGetMaxChannel) {
                        channelRange = AutoConstants.ChannelParameter.FM2_EE_MAX_CHANNEL;
                    } else {
                        channelRange = AutoConstants.ChannelParameter.FM2_EE_MIN_CHANNEL;
                    }
                    break;
                case RadioConst.CHANNEL_TYPE_AM:
                    if (isGetMaxChannel) {
                        channelRange = AutoConstants.ChannelParameter.AM_EE_MAX_CHANNEL;
                    } else {
                        channelRange = AutoConstants.ChannelParameter.AM_EE_MIN_CHANNEL;
                    }
                    break;
            }

        } else if (AutoConstants.ChannelParameter.COUNTRY_AMERICA == nationalRegion) {// 美洲
            switch (channelType) {
                case RadioConst.CHANNEL_TYPE_FM:
                    if (isGetMaxChannel) {
                        channelRange = AutoConstants.ChannelParameter.FM_AMERICA_MAX_CHANNEL;
                    } else {
                        channelRange = AutoConstants.ChannelParameter.FM_AMERICA_MIN_CHANNEL;
                    }
                    break;
                case RadioConst.CHANNEL_TYPE_AM:
                    if (isGetMaxChannel) {
                        channelRange = AutoConstants.ChannelParameter.AM_AMERICA_MAX_CHANNEL;
                    } else {
                        channelRange = AutoConstants.ChannelParameter.AM_AMERICA_MIN_CHANNEL;
                    }
                    break;
            }
        }
        return channelRange;
    }

}
