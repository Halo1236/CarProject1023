
#ifndef __BW_MAIN_H__
#define __BW_MAIN_H__

#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <stdarg.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <termios.h>
#include <errno.h>
#include <poll.h>
#include <signal.h>
#include <sys/ioctl.h>
#include <sys/time.h>
#include <pthread.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <termios.h>
#include <errno.h>
#include <poll.h>

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>

#define BIT0   				0x00000001  
#define BIT1   				0x00000002 
#define BIT2   				0x00000004 
#define BIT3   				0x00000008 
#define BIT4   				0x00000010 
#define BIT5   				0x00000020 
#define BIT6   				0x00000040  
#define BIT7   				0x00000080
#define BIT8   				0x00000100 
#define BIT9   				0x00000200  
#define BIT10  				0x00000400  
#define BIT11  				0x00000800  
#define BIT12  				0x00001000  
#define BIT13  				0x00002000  
#define BIT14  				0x00004000 
#define BIT15  				0x00008000 


void bt_log(const char* Format, ... );
int str2dec(const char *str, int slen);

#define BT_DEBUG(x) bt_log x

#endif

