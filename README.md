

## 测试
### 客户端与服务端建立连接(建立在websocket基础上)
ws://127.0.0.1:8003/notice

可以使用在线工具：http://www.easyswoole.com/wstool.html

### 客户端发送心跳
每6秒之内需要发送数据：heart_beat

### 客户端绑定管道
客户端发送的数据格式：管道名称@#@客户端Id

Eg：channel_global@#@1   这个channel_global表示全局管道；1就表示用户ID，也就是接收者；@#@是分割符号

### 发送通知数据
访问：http://localhost:9000/publish
请求体：
```json
{
	"noticeId": "111",
	"noticeLabel": "222",
	"publisher": "lzhpo",
	"receiverChannelIds": ["1", "2"],
	"groupType": 3,
	"message": "我是lzhpo",
	"totalReceivers": 2
}
```

noticeId：消息ID。

noticeLabel：消息主题。

publisher：发送者。

receiverChannelIds：接收者。

groupType：管道名称。

message：消息内容。

totalReceivers：总共有多少个接收者。

### Kafka