
#include "serial.h"


static int bt_serial_fd = -1;
static pthread_t bt_serial_read_thread;
static int (*bt_serial_callback)(char* s,int mode) = NULL;

static int set_baudrate(int fd,uint32_t baudrate)
{	
    struct termios toptions;

    if (tcgetattr(fd, &toptions) < 0) 
	{
        return -1;
    }
    
    speed_t brate = baudrate;
    switch(baudrate) 
	{
        case 57600: brate=B57600; break;
        case 115200: brate=B115200; break;
        case 230400: brate=B230400; break;
        case 460800: brate=B460800; break;
        case 921600: brate=B921600; break;
		case 1000000: brate=B1000000; break;
		case 1500000: brate=B1500000; break;
		case 2000000: brate=B2000000; break;
		case 2500000: brate=B2500000; break;
		case 3000000: brate=B3000000; break;
		case 3500000: brate=B3500000; break;
		case 4000000: brate=B4000000; break;
        default: 
			break;
    }
    cfsetospeed(&toptions, brate);
    cfsetispeed(&toptions, brate);

    if( tcsetattr(fd, TCSANOW, &toptions) < 0) 
	{
        return -1;
    }
    return 0;
}
static int serial_set_option(int fd,int parity,int flowctl)
{
    struct termios toptions;
    if(tcgetattr(fd, &toptions) < 0) return -1;

    cfmakeraw(&toptions);  

    toptions.c_cflag &= ~CSTOPB;
    toptions.c_cflag |= CS8;
    toptions.c_cflag |= CREAD | CLOCAL; 
    toptions.c_iflag &= ~(IXON | IXOFF | IXANY);
    toptions.c_cc[VMIN]  = 1;
    toptions.c_cc[VTIME] = 0;	
	
	if(parity)
		toptions.c_cflag |= PARENB;
	else
		toptions.c_cflag &= ~PARENB;
	
	if(flowctl)
		toptions.c_cflag |= CRTSCTS;
	else
		toptions.c_cflag &= ~CRTSCTS;

    if(tcsetattr(fd, TCSANOW, &toptions) < 0) 
		return -1;
	
	return 0;
}

int bt_serial_write(const char* command)
{
	if(bt_serial_fd <= 0) return -2;
	while(write(bt_serial_fd,command, strlen(command)) <= 0)
	{
		usleep(50000);
	}
	return 0;
}

int bt_serial_read(char *buffer, int size)
{
	fd_set descriptors_read;	
	FD_ZERO(&descriptors_read);
	FD_SET(bt_serial_fd, &descriptors_read);
	select( bt_serial_fd+1, &descriptors_read, NULL, NULL, NULL);	
	if(!FD_ISSET(bt_serial_fd, &descriptors_read)) return 0;

	int total_bytes = read(bt_serial_fd,buffer,size);
	if(total_bytes <= 0 ) return 0;
	return total_bytes;
}

static void *serial_process(void* p)
{
	fd_set descriptors_read;	
	char read_buf[SERIAL_BUF_SIZE+1];

	/* all bt response start and end with "\r\n" , get data between which */
	
	for(;;)
	{		
		FD_ZERO(&descriptors_read);
		FD_SET(bt_serial_fd, &descriptors_read);
		select( bt_serial_fd+1, &descriptors_read, NULL, NULL, NULL);	
		if(!FD_ISSET(bt_serial_fd, &descriptors_read)) continue;
		
		int total_bytes = read(bt_serial_fd,read_buf,sizeof(read_buf));
		if(total_bytes <= 0 ) continue;
		read_buf[total_bytes] = '\0';

		int byte_processed;
		char* ptr = read_buf;
		
		if(bt_serial_callback)
		{
			do
			{
				byte_processed = bt_serial_callback(ptr,1);
				total_bytes -= byte_processed;
				ptr += byte_processed;
				
			}while(total_bytes > 0)	;	
		}		
    }
}

void bt_serial_register_callback(int (*callback)(char* s,int mode))
{
	bt_serial_callback = callback;
}

int bt_serial_close(void)
{
	if(bt_serial_fd > 0) 
	{
		close(bt_serial_fd);
		bt_serial_fd = 1;
	}
	return 0;
}

int bt_serial_open(const char* dev,int baudrate)
{
	if((bt_serial_fd = open(dev, O_RDWR | O_NOCTTY | O_NONBLOCK)) < 0 )
	{
		return -1;
	}	
	
	tcflush(bt_serial_fd,TCIOFLUSH);
	
	if(serial_set_option(bt_serial_fd,0,0) < 0 || set_baudrate(bt_serial_fd,baudrate) < 0)
	{
		return -1;
	}
	
	// create thread to handle data recieved from bt serial
    //pthread_create(&bt_serial_read_thread, NULL,serial_process,NULL);

	return 0;
}

