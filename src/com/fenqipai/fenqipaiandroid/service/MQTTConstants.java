package com.fenqipai.fenqipaiandroid.service;

import com.fenqipai.fenqipaiandroid.net.URL;


/**
 * MQTT 配置
 * @author lelouch
 *
 */
public class MQTTConstants {

	/**
	 * MQTT 服务器所在的ip 地址
	 */
	public static String MQTT_SERVER = URL.getUSERURL();
	/**
	 * MQTT 服务器所在的端口号
	 */
	public static int MQTT_PORT =  1883;

	/**
	 * 绑定的websocket端口，默认8080
	 */
	public static int MQTT_HttpPort = 9092;

	// public static final String TAG = "DemoPushService";
	// public static final String SERVER = "COM.EXAMPLE.STUDENTDEMO";
	// // 控制服务启动(大屏投影)
	// public static final String ACTION_START = "start,url,";
	// // 打开大屏，不刷新
	// public static final String ACTION_REFRESH = "start,refresh,";
	// // 大屏投影classID后面的参数，比如123456(classID)/screen,对应大屏投影配置文件方式
	// public static final String ACTION_PARAMS = "/screen";
	// // 控制服务停止
	// public static final String ACTION_STOP = "stop";
	// // 服务保持链接
	// public static final String ACTION_KEEPALIVE = SERVER + ".KEEP_ALIVE";
	// // 重新链接
	// public static final String ACTION_RECONNECT = SERVER + ".RECONNECT";
	//
	// // We store in the preferences, whether or not the service has been
	// started
	// public static final String PREF_STARTED = "isStarted";
	// // We also store the deviceID (target)
	// public static final String PREF_DEVICE_ID = "deviceID";
	//
	// // This the application level keep-alive interval, that is used by the
	// // AlarmManager
	// // to keep the connection active, even when the device goes to sleep.
	// public static final long KEEP_ALIVE_INTERVAL = 1000 * 60 * 28;
	// // Retry intervals, when the connection is lost.
	// public static final long INITIAL_RETRY_INTERVAL = 1000 * 10;
	// public static final long MAXIMUM_RETRY_INTERVAL = 1000 * 60 * 30;
	// // We store the last retry interval
	// public static final String PREF_RETRY = "retryInterval";
	// // Notification title
	// public static String NOTIF_TITLE = "Tokudu";
	// // Notification id
	// public static final int NOTIF_CONNECTED = 0;
	// /**
	// * Property name for the history field in {@link Connection} object for
	// use
	// * with {@link java.beans.PropertyChangeEvent}
	// **/
	// public static final String historyProperty = "history";
	//
	// /**
	// * Property name for the connection status field in {@link Connection}
	// * object for use with {@link java.beans.PropertyChangeEvent}
	// **/
	// public static final String ConnectionStatusProperty = "connectionStatus";
	// public static final String empty = new String();
	// /** Show History Request Code **/
	// public static final int showHistory = 3;
	//
	//
	// // 订阅消息的主题
	// public static String[] subscrbieTopics = { "sendclass", "lock", "reader",
	// "onclass", "disclass" };
	// // 订阅主题的消息级别
	// public static int[] qos = { 2, 2, 2, 2, 2 };
	//
	// // 指令
	// public static String LOCK = "com.xinqiao.lock";
	// public static String UNLOCK = "com.xinqiao.unlock";
	// public static String ONClASS = "com.xinqiao.onclass";
	// public static String DISClASS = "com.xinqiao.disclass";
	// public static String SENDTEST = "com.xinqiao.sendtest";
	// public static String READER = "com.xinqiao.reader";
	//
	// // 教师发送消息的主题
	// public static String publishTopic = "";
	// // 教师发送消息的消息级别
	// public static int publishQos = 2;
	//
	// /**
	// * 形成消息 进行发送
	// *
	// * @param msg
	// * 发送的具体内容
	// * @param type
	// * 消息类型
	// * @param uri
	// * uri 可以为""
	// * @param sendtime
	// * 发送时间
	// * @return uuid id
	// *
	// * @return userid 用户id
	// */
	// public static String formMsgToSend(String msg, int type, String uri,
	// String sendtime, String uuid, String testid, String userid) {
	// // NBEBookPerson t = NBEBookPerson.findById(userid);
	// // String lessionID = t.online;
	// String lessionID = "aa";
	// Map<String, String> hm = new HashMap<String, String>();
	// hm.put("type", type + "");
	// hm.put("message", msg);
	// hm.put("uri", uri);
	// hm.put("sendtime", sendtime);
	// hm.put("uuid", uuid);
	// hm.put("testid", testid);
	// hm.put("lessonid", lessionID);
	// Gson gson = new Gson();
	// String sendmessage = gson.toJson(hm, new TypeToken<Map<String, String>>()
	// {
	// }.getType());
	//
	// return sendmessage;
	// }
	//
	// /**
	// * 获得当前上课班级id作为发布信息的主题 上课的
	// */
	// public static String getPublishTopicONClass(String teaID) {
	// // NBEBookLesson l = null;
	// // NBEBookPerson t = NBEBookPerson.findById(teaID);
	// // if (t != null) {
	// // l = NBEBookLesson.findById(t.online);
	// // }
	//
	// // if (l != null) {
	// // publishTopic = l.getClazID();
	//
	// // }
	// publishTopic = "aaa";
	// return publishTopic;
	// }
	//
	// /**
	// * 获得当前上课班级id作为发布信息的主题 下课的或者已知上课班级id
	// */
	// public static String getPublishTopic(String classid) {
	// publishTopic = classid;
	// return publishTopic;
	// }

}
