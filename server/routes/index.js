var express = require('express');
var router = express.Router();

// 引入模块
const WebSocket = require("ws").Server;
const port = 3001;

// 创建服务器
const server = new WebSocket({ port }, () => {
  console.log("websocket服务开启");
});
 
const connectHandler = (ws) => {
  console.log("客户端连接");
  // 监听客户端出错
  ws.on("error", errorHandler);
  // 监听客户端断开链接
  ws.on("close", closeHandler);
  // 监听客户端发来的消息
  ws.on("message", messageHandler);
};
 
// 监听接收客户端信息回调
// 注意：因为这里用到this的指向，因此用普通的函数
function messageHandler(data) {
  console.log("messageHandler===>接收客户端消息", JSON.parse(data));
  const { ModeCode } = JSON.parse(data);
  switch (ModeCode) {
    case "message":
      console.log("收到消息");
      // 需要发送信息给客户端以此说明连接成功
      this.send(JSON.stringify(JSON.parse(data)));
      break;
    case "heart_beat":
      console.log("心跳检测");
      // 需要发送信息给客户端以此说明连接成功
      this.send(JSON.stringify(JSON.parse(data)));
      break;
  }
}
 
// 监听客户端出错回调
const errorHandler = (error) => {
  console.log("errorHandler===>客户端出错", error);
};
// 监听客户端断开连接回调
const closeHandler = (e) => {
  console.log("closeHandler===>客户端断开🔗", e);
};
 
// 建立连接
server.on("connection", connectHandler);


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

module.exports = router;
