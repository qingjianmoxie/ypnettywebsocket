

## 测试
### 客户端与服务端建立连接(建立在websocket基础上)
ws://127.0.0.1:8003/notice

可以使用在线工具：http://www.easyswoole.com/wstool.html

客户端首次接入时，客户端需按照指定格式推送一条数据到服务端完成鉴权，然后服务端每间隔6秒检查心跳`{@link com.lzhpo.ypnettywebsocket.constant.MyConstant}`，
没收到心跳，计数+1，计数器到了10次，就与客户端断开连接，同时删除Redis中的此channelId的数据！

所以客户端需要一个定时器，发送告知服务端，我活着。客户端鉴权之后如果没有心跳了，错误信息会在Redis中记录。
发送的内容：管道名称@#@heart_beat

`{@link com.lzhpo.ypnettywebsocket.constant.MyConstant.HEART_BEAT}`

客户端发送的信息：管道名称@#@客户端Id

Eg：channel_global@#@1   这个channel_global表示全局管道；1就表示用户ID，也就是接收者；@#@是分割符号

### 发送通知数据
访问：http://localhost:8000/publish
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

### Kafka
Kafka采用的是手动提交offset，防止重复消费！