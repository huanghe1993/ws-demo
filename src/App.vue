<script setup lang="ts">
import { onMounted } from 'vue'
//引用
import Scoket from "./utils/socket";
 
const socket = new Scoket("ws://localhost:8080/myWs1");
//发送消息
const onSend = () => {
  console.log("onSend");
  socket.sendMessage('123');
};
//连接ws
onMounted(() => {
  socket.connectWebSocket();
});
</script>

<template>
  <div>
    ws测试
  </div>
</template>
