package com.smk.autoradio.constants;

public class RadioConst {
	// 无效频道
	public static final int CHANNEL_INVALID = -1;
	public static final int NATIONAL_REGING_INVALID =  -1;
	// 频段类型
	public static final int CHANNEL_TYPE_INVALID = -1;
	public static final int CHANNEL_TYPE_FM1 = 101;
	public static final int CHANNEL_TYPE_FM2 = 102;
	public static final int CHANNEL_TYPE_AM = 103;
	// 频段声道类型(只FM有些指标参数,AM无)
	public static final int SOUNDTRACK_TYPE_INVALID = -1;
	public static final int SOUNDTRACK_TYPE_MONO = 200;
	public static final int SOUNDTRACK_TYPE_STEREO = 201;
	// 远近程类型
	public static final int CHANNEL_DX_LOC_TYPE_INVALID = -1;
	public static final int CHANNEL_DX_LOC_TYPE_DX = 300;
	public static final int CHANNEL_DX_LOC_TYPE_LOC = 301;

	// 服务控制命令
	public class RadioCmd{
		public static final int CMD_PLAY_FM = 400;
		public static final int CMD_PLAY_AM = 401;
		public static final int CMD_PREV_STRONG_CHANNEL = 402;
		public static final int CMD_NEXT_STRONG_CHANNEL = 403;
		public static final int CMD_MUTE = 404;
		public static final int CMD_UNMUTE = 405;
		public static final int CMD_RESTORE_PLAY = 406;
		public static final int CMD_STOP_SEARCH = 407;
	}

}
