/** 
 * nForeTek MAP Commands Interface for Android 4.3
 *
 * Copyright (C) 2011-2019  nForeTek Corporation
 *
 * Zeus 0.0.0 on 20140613
 * @author KC Huang	<kchuang@nforetek.com>
 * @author Piggy	<piggylee@nforetek.com>
 * @version 0.0.0
 * 
 */

package com.nforetek.bt.aidl;

import com.nforetek.bt.aidl.INfCallbackMap;

/**
 * The API interface is for Message Access Profile (MAP).
 * <br>UI program may use these specific APIs to access to nFore service.
 * <br>The naming principle of API in this doc is as follows,
 *		<blockquote><b>setXXX()</b> : 		set attributes to specific functions of nFore service.
 *		<br><b>reqXXX()</b> : 				request nFore service to implement specific function. It is an Asynchronized mode.
 *		<br><b>isXXX()</b> : 				check the current status from nFore service. It is a Synchronized mode.
 *		<br><b>getXXX()</b> : 				get the current result from nFore service. It is a Synchronized mode.</blockquote>
 *
 * <p>The constant variables in this Doc could be found and referred by importing
 * 		<br><blockquote>com.nforetek.bt.res.NfDef</blockquote>
 * <p>with prefix NfDef class name. Ex : <code>NfDef.DEFAULT_ADDRESS</code>
 *
 * <p>Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB".
 *
 * @see INfCallbackMap
 */
 
 interface INfCommandMap {

 	/** 
	 * Check if MAP service is ready.
	 */
	boolean isMapServiceReady();
	/** 
	 * Register callback functions for MAP.
	 * <br>Call this function to register callback functions for MAP.
	 * <br>Allow nFore service to call back to its registered clients, which is usually the UI application.
	 *
	 * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
    boolean registerMapCallback(INfCallbackMap cb);   
    
	/** 
	 * Remove callback functions from MAP.
     * <br>Call this function to remove previously registered callback interface for MAP.
     * 
     * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean unregisterMapCallback(INfCallbackMap cb);    
    
	/**
	 * Request to download single message from remote device and save to desired storage(bypass/nfore_database).
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback function(s) depend on the parameter <b>storage</b>, as below:
	 *		<blockquote>for storage <b>MAP_STORAGE_TYPE_BY_PASS</b>:
	 *			<blockquote>{@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified of subsequent profile state changes.
	 *			<br>And {@link INfCallbackMap#retMapDownloadedMessage retMapDownloadedMessage} to be notified of downloaded messages.</blockquote>
	 *		<br>for storage <b>MAP_STORAGE_TYPE_TO_DATABASE</b>:	
	 *			<blockquote>{@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified of subsequent profile state changes.</blockquote></blockquote>
	 * 
	 * @param address valid Bluetooth MAC address.	
	 * @param folder which folder message download from. Possible values are
	 *		<blockquote><b>MAP_FOLDER_STRUCTURE_INBOX</b>					(int) 0
	 *		<br><b>MAP_FOLDER_STRUCTURE_SENT</b>					(int) 1</blockquote>
	 * @param handle message handle
	 * @param storage message download to <b>which storage</b>. Possible values are
	 *		<blockquote><b>MAP_STORAGE_TYPE_BY_PASS</b>					(int) 0
	 *		<br><b>MAP_STORAGE_TYPE_TO_DATABASE</b>				(int) 1</blockquote>
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
	boolean reqMapDownloadSingleMessage(String address, int folder, String handle, int storage);

	/**
	 * Request to download messages from remote device and save to desired storage(bypass/nfore_database).
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback function(s) depend on the parameter <b>storage</b>, as below:
	 *		<blockquote>for storage <b>MAP_STORAGE_TYPE_BY_PASS</b>:
	 *			<blockquote>{@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified of subsequent profile state changes.
	 *			<br>And {@link INfCallbackMap#retMapDownloadedMessage retMapDownloadedMessage} to be notified of downloaded messages.</blockquote>
	 *		<br>for storage <b>MAP_STORAGE_TYPE_TO_DATABASE</b>:	
	 *			<blockquote>{@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified of subsequent profile state changes.</blockquote></blockquote>
	 * <br>Here are some examples of usage:
	 *		<blockquote>Example 1: <b>download all messages without filter</b>
	 *			<blockquote>reqMapDownloadMessage(address, folder, isContentDownload, <b>MAP_DOWNLOAD_COUNT_ALL,
	 *			<br>0, storage, null, null, null, null, 0, 0);</b></blockquote>
	 *		<br>Example 2: <b>download latest N messages without filter</b>
	 *			<blockquote>reqMapDownloadMessage(address, folder, isContentDownload, <b>N,
	 *			<br>0, storage, null, null, null, null, 0, 0);</b></blockquote>
	 *		<br>Example 3: <b>ignore latest 5 messages, and download the following 50 messages without filter</b>	
	 *			<blockquote>reqMapDownloadMessage(address, folder, isContentDownload, <b>50,
	 *			<br>5, storage, null, null, null, null, 0, 0);</b></blockquote>
	 *		<br>Example 4: <b>download unread messages only</b>	
	 *			<blockquote>reqMapDownloadMessage(address, folder, isContentDownload, <b>MAP_DOWNLOAD_COUNT_ALL,
	 *			<br>0, storage, null, null, null, null, MAP_READ_STATUS_UNREAD, 0);</b></blockquote>
	 *		<br>Example 5: <b>download messages with sender ralated to nforetek</b>	
	 *			<blockquote>reqMapDownloadMessage(address, folder, isContentDownload, <b>MAP_DOWNLOAD_COUNT_ALL,
	 *			<br>0, storage, null, null, "nforetek", null, 0, 0);</b></blockquote>
	 *		<br>Example 6: <b>download messages between period from 2014/10/10 10:10:10 to 2015/10/10 10:10:10</b>	
	 *			<blockquote>reqMapDownloadMessage(address, folder, isContentDownload, <b>MAP_DOWNLOAD_COUNT_ALL,
	 *			<br>0, storage, 20141010T101010Z, 20151010T101010Z, null, null, 0, 0);</b></blockquote></blockquote>
	 * @param address valid Bluetooth MAC address.
	 * @param folder which folder message download from. Possible values are
	 *		<blockquote><b>MAP_FOLDER_STRUCTURE_INBOX</b>					(int) 0
	 *		<br><b>MAP_FOLDER_STRUCTURE_SENT</b>					(int) 1</blockquote>
	 * @param isContentDownload
	 * <p><value>=false, download message list only.
	 * <p><value>=true, download all messages including the contents.
	 * @param count download count of latest messages must be greater than 0 except the predefined tag for download all, as below:
	 *		<blockquote>MAP_DOWNLOAD_COUNT_ALL			(int) -1</blockquote>
	 * @param startPos indicate the offset of the first entry of the returned messages. 
	 *		<blockquote>For example, if startPos is 5, then the first 5 messages are not delivered.</blockquote>
	 * @param storage message download to <b>which storage</b>. Possible values are
	 *		<blockquote><b>MAP_STORAGE_TYPE_BY_PASS</b>					(int) 0
	 *		<br><b>MAP_STORAGE_TYPE_TO_DATABASE</b>				(int) 1</blockquote>
	 * @param periodBegin download from <b>periodBegin</b> to currentTime/periodEnd.
	 * @param periodEnd download from olddest/periodBegin to <b>periodEnd</b>.
	 *		<blockquote>If the value of <b>periodBegin</b> is larger than the value of <b>periodEnd</b> 
	 *		no messages shall be delivered.
	 *		<br><b>Format of periodBegin/periodEnd: 20151010T101010Z means 2015/10/10 10:10:10</b></blockquote>
	 * @param sender sub-string of one of the attributes (Name, Tel and Email) of sender.
	 * @param recipient sub-string of one of the attributes (Name, Tel and Email) of recipient.
	 * @param readStatus possible values are:
	 *		<blockquote><b>MAP_READ_STATUS_ALL</b>	(int) 0
	 *		<br><b>MAP_READ_STATUS_UNREAD</b>		(int) 1
	 *		<br><b>MAP_READ_STATUS_READ</b>			(int) 2
	 *		<br>For example, input value MAP_READ_STATUS_UNREAD will download unread messages only.
	 *		<br>All other values are undefined.</blockquote>
	 * @param type possible values are:
	 *		<blockquote><b>MAP_TYPE_ALL</b>		(int) (0<<0) 
	 *		<br><b>MAP_TYPE_SMS_GSM</b>			(int) (1<<0) 
	 *		<br><b>MAP_TYPE_SMS_CDMA</b>		(int) (1<<1) 
	 *		<br><b>MAP_TYPE_SMS_MMS</b>			(int) (1<<2) 
	 *		<br><b>MAP_TYPE_SMS_EMAIL</b>		(int) (1<<3)
	 *		<br>Set the bit mask to download the corresponding type of messages.</blockquote>
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	    
	boolean reqMapDownloadMessage(String address, int folder,  boolean isContentDownload, int count,
		int startPos, int storage, String periodBegin, String periodEnd, String sender, String recipient, int readStatus, int typeFilter);   
     
    /**
	 * Request to register notification when there is new message on remote device with given Bluetooth hardware address.
	 * Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB".
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified if register notification success.
	 * and implement callback function {@link INfCallbackMap#onMapNewMessageReceivedEvent onMapNewMessageReceivedEvent}
	 * , {@link INfCallbackMap#onMapMemoryAvailableEvent onMapMemoryAvailableEvent}
	 * , {@link INfCallbackMap#onMapMessageSendingStatusEvent onMapMessageSendingStatusEvent}
	 * , {@link INfCallbackMap#onMapMessageDeliverStatusEvent onMapMessageDeliverStatusEvent}
	 * , {@link INfCallbackMap#onMapMessageShiftedEvent onMapMessageShiftedEvent}
	 * , {@link INfCallbackMap#onMapMessageDeletedEvent onMapMessageDeletedEvent}
	 * to be notified of receivced new message.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param downloadNewMessage if true, download the new message, including sender and message contents; if false, only notification will be sent
	 */
    boolean reqMapRegisterNotification(String address, boolean downloadNewMessage);
    
    /**
	 * Request to unregister new message notification on remote device with given Bluetooth hardware address.
	 * Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB". 
	 *
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified if unregister notification success.
	 * @param address valid Bluetooth MAC address.
	 */
    void reqMapUnregisterNotification(String address); 
    
	/** 
	 * Return true if the new message notification is registered on device with given address.
	 *
	 * @param address valid Bluetooth MAC address.	 
	 * @return true if registered.
	 */ 
    boolean isMapNotificationRegistered(String address);

    /**
	 * Request to interrupt the ongoing download on remote device.
	 * 
	 * Clients should register and implement callback function {@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified of subsequent result.    
	 *
	 * @param address valid Bluetooth MAC address.
	 * @return true if really try to interrupt.
	 */
    boolean reqMapDownloadInterrupt(String address);

	/**
	 * Request to check if nfore_database is available for query
	 * Client should register and implement {@link INfCallbackMap#retMapDatabaseAvailable retMapDatabaseAvailable} 
	 * to be notified when nfore_database is available.
	 *
	 * @param address valid Bluetooth MAC address.	 
	 */ 
    void reqMapDatabaseAvailable();

    /**
	 * Request to delete MAP data by specific address
	 * Client should register and implement {@link INfCallbackMap#retMapDeleteDatabaseByAddressCompleted retMapDeleteDatabaseByAddressCompleted} 
	 * to be notified when nfore_database is available.
	 *
	 * @param address valid Bluetooth MAC address.	 
	 */
	void reqMapDeleteDatabaseByAddress(String address);

    /**
	 * Request to clean nfore_database of MAP.
	 * This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackMap#retMapCleanDatabaseCompleted retMapCleanDatabaseCompleted} to be notified when nfore_database has been cleaned.	 
	 */ 
	void reqMapCleanDatabase();

	/** 
	 * Request for the current download state of remote connected MAP device with given Bluetooth hardware address.
	 * Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB".	 
	 *
	 * @param address valid Bluetooth MAC address of connected device.	 
	 * @return current state of MAP profile service.
	 */ 
    int getMapCurrentState(String address);

    /** 
	 * Request for the current register state of remote connected MAP device with given Bluetooth hardware address.
	 * Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB".	 
	 *
	 * @param address valid Bluetooth MAC address of connected device.	 
	 * @return current state of MAP profile service.
	 */ 
    int getMapRegisterState(String address);
    
    /**
	 * Request to send message on remote device.
	 * 
	 * Clients should register and implement callback function {@link INfCallbackMap#retMapSendMessageCompleted retMapSendMessageCompleted} to be notified of subsequent result.    
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param message send context.
	 * @param target phone number of target.
	 */
    boolean reqMapSendMessage(String address, String message, String target);
    
    /**
	 * Request to delete message on remote device.
	 * 
	 * Clients should register and implement callback function {@link INfCallbackMap#retMapDeleteMessageCompleted retMapDeleteMessageCompleted} to delete a message in remote device.
	 * Suggest that the message handle should be updated by downloading message listing before deleting a message.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param folder which folder message download from. Possible values are
	 *		<blockquote><b>MAP_FOLDER_STRUCTURE_INBOX</b>					(int) 0
	 *		<br><b>MAP_FOLDER_STRUCTURE_SENT</b>					(int) 1</blockquote>
	 * @param handle MAP handle
	 */
    boolean reqMapDeleteMessage(String address, int folder, String handle);
    
    /**
	 * Request to change read status of message.
	 * 
	 * Clients should register and implement callback function {@link INfCallbackMap#retMapChangeReadStatusCompleted retMapChangeReadStatusCompleted} to modify a read status in remote device.    
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param folder which folder message download from. Possible values are
	 *		<blockquote><b>MAP_FOLDER_STRUCTURE_INBOX</b>					(int) 0
	 *		<br><b>MAP_FOLDER_STRUCTURE_SENT</b>					(int) 1</blockquote>
	 * @param handle MAP handle
	 * @param isReadStatus that "true" (=read) or "false" (=unread) for the "readStatus" indicator
	 */
    boolean reqMapChangeReadStatus(String address, int folder, String handle, boolean isReadStatus);

    /**
	 * Set MAP download notify frequency. Will set to default value when ServiceManager restart.
	 * Default value is 0 means don't callback download notify. For example, if frequency is set to 5, every 5 contacts onPbapDownloadNofity will be notified.
	 *
	 * <br>Clients should register and implement callback function {@link INfCallbackMap#onMapDownloadNotify onMapDownloadNotify} to be notified of subsequent result.
	 * Callback will be invoked if below commands are issued     
	 * {@link INfCommandMap#reqMapDownloadAllMessages reqMapDownloadAllMessages} or
	 * {@link INfCommandMap#reqMapDownloadAllMessagesToDatabase reqMapDownloadAllMessagesToDatabase}
	 *
	 * @param frequency define the callback frequency.
	 * <p><value>=0 all messages would be downloaded first, and inserted to nfore_database. Only one callback would be invocated.
	 * <p><value>>0 Callbacks would be invocated every "frequency" messages have been downloaded and inserted to nfore_database. 
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean setMapDownloadNotify(int frequency);
}
