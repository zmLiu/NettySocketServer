package
{
	import com.bit101.components.InputText;
	import com.bit101.components.PushButton;
	import com.bit101.components.TextArea;
	import com.bit101.utils.MinimalConfigurator;
	import com.lzm.netty.NettyClient;
	
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.utils.setTimeout;
	
	public class NettyChatClient extends Sprite
	{
		
		private var _nettyClient:NettyClient;
		
		private var _config:MinimalConfigurator;
		private var _userNameInput:InputText;
		private var _loginBtn:PushButton;
		private var _sendChatInput:InputText;
		private var _sendChatBtn:PushButton;
		private var _logTextArea:TextArea;
		
		public function NettyChatClient()
		{
			_nettyClient = new NettyClient("192.168.0.221",5160);
			_nettyClient.onConnectFun = onConnect;
			_nettyClient.onCloseFun = onConnectClose;
			_nettyClient.registerCommand(Cmds.login,onLogin);
			_nettyClient.registerCommand(Cmds.chatMessage,onChatMessage);
			_nettyClient.registerCommand(Cmds.leave,onLeave);
			_nettyClient.connect();
		}
		
		private function onConnect():void{
			_config = new MinimalConfigurator(this);
			_config.loadXML("ui.xml");
			_config.addEventListener(Event.COMPLETE,loadXmlComplete);
		}
		
		private function loadXmlComplete(e:Event):void{
			_userNameInput = _config.getCompById("user_name") as InputText;
			_sendChatInput = _config.getCompById("chat_message") as InputText;
			
			_loginBtn = _config.getCompById("login_btn") as PushButton;
			_sendChatBtn = _config.getCompById("send_chat_btn") as PushButton;
			
			_logTextArea = _config.getCompById("logs") as TextArea;
			_logTextArea.editable = false;
		}
		
		public function onLoginBtn(e:MouseEvent):void{
			if(_userNameInput.text == "") return;
			_userNameInput.enabled = false;
			_loginBtn.enabled = false;
			
			_nettyClient.sendMessages(Cmds.login,[_userNameInput.text]);
		}
		
		public function onSendChatBtn(e:MouseEvent):void{
			if(_sendChatInput.text == "" || _userNameInput.enabled) return;
			_nettyClient.sendMessages(Cmds.chatMessage,[_sendChatInput.text]);
			_sendChatInput.text = "";
		}
		
		
		private function onLogin(msgs:Array):void{
			_logTextArea.textField.appendText(msgs[0] + "\n");
			setTimeout(function():void{
				_logTextArea.textField.scrollV = _logTextArea.textField.maxScrollV;
			},60);
		}
		
		private function onChatMessage(msgs:Array):void{
			_logTextArea.textField.appendText(msgs[0] + ":" + msgs[1] + "\n");
			setTimeout(function():void{
				_logTextArea.textField.scrollV = _logTextArea.textField.maxScrollV;
			},60);
		}
		
		private function onLeave(msgs:Array):void{
			_logTextArea.textField.appendText(msgs[0] + "\n");
			setTimeout(function():void{
				_logTextArea.textField.scrollV = _logTextArea.textField.maxScrollV;
			},60);
		}
		
		private function onConnectClose():void{
//			_nettyClient.connect();
		}
	}
}