
#ifndef __SERIAL_H__
#define __SERIAL_H__

#include "main.h"

#define SERIAL_BUF_SIZE 2048

int bt_serial_open(const char* dev,int baudrate);
int bt_serial_close(void);
void bt_serial_register_callback(int (*callback)(char* s,int mode));
int bt_serial_write(const char* command);
int bt_serial_read(char *buffer, int size);

#endif

