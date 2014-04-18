package com.wechat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ifp.wechat.constant.ConstantWeChat;
import com.ifp.wechat.entity.message.resp.Article;
import com.ifp.wechat.entity.message.resp.NewsMessage;
import com.ifp.wechat.entity.message.resp.TextMessage;
import com.ifp.wechat.service.MessageService;
import com.ifp.wechat.util.MessageUtil;



public class CoreService {
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return String
	 */
	public static String processWebchatRequest(HttpServletRequest request) {
	    String respMessage = null;
	    try {
	        // xml请求解析
	        Map<String, String> requestMap = MessageUtil.parseXml(request);
	        // 消息类型
	        String msgType = requestMap.get("MsgType");

	        TextMessage textMessage = (TextMessage) MessageService
	                .bulidBaseMessage(requestMap,
	                        ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
	        NewsMessage newsMessage = (NewsMessage) MessageService
	                .bulidBaseMessage(requestMap,
	                        ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);

	        String respContent = "";
	        // 文本消息
	        if (msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_TEXT)) {
	            // 接收用户发送的文本消息内容
	            String content = requestMap.get("Content");

	            List<Article> articleList = new ArrayList<Article>();
	            // 单图文消息
	            if ("1".equals(content)) {
	                Article article = new Article();
	                article.setTitle("测试TITLE");
	                article.setDescription("测试Description");
	                article.setPicUrl("图片地址");
	                article.setUrl("http://m.baidu.com");
	                articleList.add(article);
	                // 设置图文消息个数
	                newsMessage.setArticleCount(articleList.size());
	                // 设置图文消息包含的图文集合
	                newsMessage.setArticles(articleList);
	                // 将图文消息对象转换成xml字符串
	                respMessage = MessageService.bulidSendMessage(newsMessage,
	                        ConstantWeChat.RESP_MESSAGE_TYPE_NEWS);
	            } else if ("#".equals(content)) {
	                textMessage.setContent("###");
	                respMessage = MessageService.bulidSendMessage(textMessage,
	                        ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
	            }
	        } else if (msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_EVENT)) {
	            // 事件类型
	            String eventType = requestMap.get("Event");

	            if (eventType.equals(ConstantWeChat.EVENT_TYPE_SUBSCRIBE)) {
	                // 关注
	                respContent = "感谢您关注偶,这里会给您提供最新的公司资讯和公告！\n";
	            } else if (eventType
	                    .equals(ConstantWeChat.EVENT_TYPE_UNSUBSCRIBE)) {
	                // 取消关注,用户接受不到我们发送的消息了，可以在这里记录用户取消关注的日志信息
	            } else if (eventType.equals(ConstantWeChat.EVENT_TYPE_CLICK)) {
	                // 事件KEY值，与创建自定义菜单时指定的KEY值对应
	                String eventKey = requestMap.get("EventKey");
	                // 自定义菜单点击事件
	                if (eventKey.equals("11")) {
	                    respContent = "天气预报菜单项被点击！";
	                } else if (eventKey.equals("12")) {
	                    respContent = "公交查询菜单项被点击！";
	                }
	            }
	            textMessage.setContent(respContent);
	            respMessage = MessageService.bulidSendMessage(textMessage,
	                    ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return respMessage;
	}
}
