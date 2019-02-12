package com.smk.autoradio.dao;

import android.provider.BaseColumns;

public class DBConfiguration {
    public static final String DATABASE_NAME = "AutoRadio.db";// 数据库名
    public static final int DATABASE_VERSION = 1;// 数据库版本

    public class ChannelConfiguration implements BaseColumns {
        public static final String TABLE_NAME_FULL_SEARCH_CHANNEL = "ChannelFullSearchTable";// 全搜表名
        public static final String TABLE_NAME_FAVORITE_CHANNEL = "ChannelFavoriteTable";// 收藏表名
        public static final String _CAHNNEL = "channel";// 频道
        /**
         * 频道类型
         * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_INVALID}<br>
         * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_FM}<br>
         * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_AM}<br>
         */
        public static final String _CAHNNEL_TYPE = "channelType";
        public static final String _SIGNAL = "signal";// 信号强度
        /**
         * 声道
         * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_INVALID}<br>
         * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_MONO}<br>
         * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_STEREO}<br>
         */
        public static final String _SOUNDTRACK = "soundtrack";
        public static final String _IS_FAVORITE = "isFavorite";// 1:收藏，0：未收藏
    }

}
