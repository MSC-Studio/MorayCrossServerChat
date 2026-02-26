# MorayCrossServerChat - 跨服聊天插件 🌐

[![Velocity](https://img.shields.io/badge/Velocity-3.3.0+-blue)](https://velocitypowered.com/)
[![Java](https://img.shields.io/badge/Java-17+-orange)](https://java.com)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)
[![Modrinth](https://img.shields.io/badge/Modrinth-CrossServerChat-green)](https://modrinth.com/plugin/moraycrossserverchat)

---

## 📖 插件介绍

**MorayCrossServerChat** 是一个轻量级、高性能的 Velocity 跨服聊天插件。它让你的服务器网络中的玩家可以**无缝交流**，无论玩家身处哪个子服务器，都能实时收发全局消息和私聊消息。

> ✨ **开箱即用，零配置！** 下载即用，无需任何繁琐设置。

---

## ✨ 功能特性

### 💬 全局聊天系统
- 跨服务器发送全局消息，所有在线玩家实时接收
- 清晰的消息格式：`[全局] 玩家名 @服务器名: 消息内容`
- 控制台可直接发送全局公告
- 玩家可随时开关全局聊天接收

### 💌 私聊系统
- 跨服务器私密聊天，不受子服务器限制
- 自动记忆最后私聊对象
- 快捷回复功能，无需重复输入对方ID
- 私聊消息双方可见，第三方不可见

### ⚡ 快捷操作
- `!消息` - 快速发送全局消息
- `@玩家 消息` - 快速发送私聊消息

### 📋 玩家管理
- 按服务器分组显示在线玩家列表
- 实时更新玩家状态
- 清晰的彩色显示格式


## 🎮 命令列表

| 主命令 | 别名 | 描述 | 权限 |
|--------|------|------|------|
| `/g <消息>` | `/global`, `/全局` | 发送全局消息 | 无 |
| `/togglechat` | `/toggleglobal`, `/切换聊天` | 开关全局聊天 | 无 |
| `/msg <玩家> <消息>` | `/tell`, `/whisper`, `/w`, `/私聊` | 发送私聊 | 无 |
| `/r <消息>` | `/reply`, `/回复` | 回复最后私聊 | 无 |
| `/list` | `/online`, `/players`, `/玩家列表` | 查看在线玩家 | 无 |
| `/chatinfo` | `/chathelp`, `/帮助`, `/chat` | 查看命令帮助 | 无 |
